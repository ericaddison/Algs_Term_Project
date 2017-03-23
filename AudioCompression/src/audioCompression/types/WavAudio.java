package audioCompression.types;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import edu.mines.jtk.mosaic.PlotFrame;
import edu.mines.jtk.mosaic.PlotPanel;

import libs.wavParser.WavFile;
import libs.wavParser.WavFileException;

public class WavAudio implements RawAudio{

	private WavFile wavFile;
	private long nSamples;
	private int samplesPerWindow;
	private int windowOverlap;
	
	public WavAudio(File wavFileFile, int samplesPerWindow, int windowOverlap) {
		try {
			this.wavFile = WavFile.openWavFile(wavFileFile);
		} catch (IOException | WavFileException e) {
			e.printStackTrace();
		}
		
		nSamples = wavFile.getNumChannels()*wavFile.getNumFrames();
		this.samplesPerWindow = samplesPerWindow;
		this.windowOverlap = windowOverlap;
		if(samplesPerWindow%wavFile.getNumChannels() > 0)
			throw new IllegalArgumentException("WavAudio: window size must be a multiple of" +
					" the number of channels, in this case: " + wavFile.getNumChannels());
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
	public int getWindowOverlap() {
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
	
	public static void main(String[] args){
		WavAudio wav = new WavAudio(new File("../src_wavs/nokia_tune.wav"), 120, 10);
		float[] buff = wav.getAudioBuffer(0,100000);
		System.out.println(wav.getNSamples());
		
		PlotFrame frame = new PlotFrame(new PlotPanel(1,1));
		frame.getPlotPanel().addPoints(buff);
		frame.setSize(500, 300);
		frame.setVisible(true);
	}

}
