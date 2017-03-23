package audioCompression.types;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import libs.wavParser.WavFile;
import libs.wavParser.WavFileException;

public class WavAudio implements RawAudio{

	private WavFile wavFile;
	private long nSamples;
	private int samplesPerWindow;
	private float windowOverlap;
	
	
	public WavAudio(File wavFileFile, int samplesPerWindow, float windowOverlap) {
		try {
			this.wavFile = WavFile.openWavFile(wavFileFile);
		} catch (IOException | WavFileException e) {
			e.printStackTrace();
		}
		
		nSamples = wavFile.getNumChannels()*wavFile.getNumFrames();
		this.samplesPerWindow = samplesPerWindow;
		if(samplesPerWindow%wavFile.getNumChannels() > 0)
			throw new IllegalArgumentException("WavAudio: window size must be a multiple of" +
					" the number of channels, in this case: " + wavFile.getNumChannels());
		
		this.windowOverlap = windowOverlap;
		if(windowOverlap<0 || windowOverlap>1)
			throw new IllegalArgumentException("WavAudio: window overlap must be a percentage value," +
					" between 0 and 1");
	}

	@Override
	public long getSampleRate(){
		return wavFile.getSampleRate();
	}
	
	@Override
	public long getNSamples() {
		return nSamples;
	}

	@Override
	public int getSamplesPerWindow() {
		return samplesPerWindow;
	}

	@Override
	public float getWindowOverlap() {
		return windowOverlap;
	}

	@Override
	public Iterator<float[]> getWindowIterator() {
		return null;
	}	
	
	@Override
	public float[] getAudioBuffer(int samp1, int samp2) {
		try {
			int nframes = (int)Math.ceil(((float)(samp2 - samp1 + 1)/wavFile.getNumChannels()));
			float[] buffer = new float[nframes*wavFile.getNumChannels()];
			wavFile.readFrames(buffer, nframes);
			return buffer;
		} catch (IOException | WavFileException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
