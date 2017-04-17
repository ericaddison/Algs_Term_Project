package audioCompression.types;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import libs.wavParser.WavFile;
import libs.wavParser.WavFileException;

public class WavAudioOutput implements RawAudio{

	private WavFile wavFile;
	private int nChannels;
	private long nSamples;             // total number of samples (per channel) in the audio file 
	private int nWindows;             // total number of windows (including the overlap)
	private int samplesPerWindow;      // number of samples per window
	private int windowOverlap;       // percentage of window overlap
	private int sampleRate;
	private float[][][] windows;
	
	public WavAudioOutput(float[][][] windows, int windowOverlap, int sampleRate) {
		this.nWindows = windows[0].length;
		this.samplesPerWindow = windows[0][0].length;
		this.windowOverlap = windowOverlap;
		this.nChannels = windows.length;
		this.sampleRate = sampleRate;
		this.windows = windows;
	}
	
	@Override
	public void writeFile(String filename) {
		// todo - write out the wav file
	}
	
	@Override
	public int getNChannels(){
		return nChannels;
	}

	@Override
	public int getNWindows(){
		return nWindows;
	}
	
	@Override
	public long getSampleRate(){
		return sampleRate;
	}
	
	@Override
	public long getNSamples() {
		return nSamples;
	}

	@Override
	public long getSamplesPerWindow() {
		return samplesPerWindow;
	}

	@Override
	public int getWindowOverlap() {
		return windowOverlap;
	}

	@Override
	public Iterator<float[][]> getWindowIterator() {
		return new WindowIterator();
	}	
	
	@Override
	public float[][] getAudioBuffer(int samp2) {
		return null;
	}
	
	@Override
	public float[][][] getAllWindows() {
		return windows;
	}

	private class WindowIterator implements Iterator<float[][]>{
		
		private float[][] windowBuffer;
		private int windowIncrement;
		
		public WindowIterator() {
		}
		
		@Override
		public boolean hasNext() {
			return false;
		}

		@Override
		public float[][] next() {
			return null;
		}
		
	}

}
