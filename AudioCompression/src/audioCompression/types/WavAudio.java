package audioCompression.types;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;

import libs.wavParser.WavFile;
import libs.wavParser.WavFileException;

public class WavAudio implements RawAudio{

	private WavFile wavFile;
	private long nSamples;             // total number of samples (per channel) in the audio file 
	private long nWindows;             // total number of windows (including the overlap)
	private int samplesPerWindow;      // number of samples per window
	private int windowOverlapSamples;  // number of samples in the window overlap
	private float windowOverlap;       // percentage of window overlap
	private File inputFile;
	
	public WavAudio(File inputFile, int samplesPerWindow, float windowOverlap) {
		try {
			this.inputFile = inputFile;
			this.wavFile = WavFile.openWavFile(inputFile);
			nSamples = wavFile.getNumFrames();
			
			this.samplesPerWindow = samplesPerWindow;
			
			this.windowOverlap = windowOverlap;
			if(windowOverlap<0 || windowOverlap>=1)
				throw new IllegalArgumentException("WavAudio: window overlap must be a percentage value," +
						" in the interval [0,1)");
			
			// set up window information, using wav frame-based values as intermediate information
			this.windowOverlapSamples = (int)(windowOverlap*samplesPerWindow);
			this.windowOverlap = (float)(windowOverlapSamples)/samplesPerWindow;
			this.nWindows = (wavFile.getNumFrames()-samplesPerWindow)/(samplesPerWindow-windowOverlapSamples) + 1;
		} catch (IOException | WavFileException e) {
			e.printStackTrace();
		} finally{
			try {
				wavFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public int getNChannels(){
		return wavFile.getNumChannels();
	}

	@Override
	public long getNWindows(){
		return nWindows;
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
	public long getSamplesPerWindow() {
		return samplesPerWindow;
	}

	@Override
	public float getWindowOverlap() {
		return windowOverlap;
	}

	@Override
	public Iterator<float[][]> getWindowIterator() {
		return new WindowIterator();
	}	
	
	@Override
	// only returns the first audio channel...
	public float[][] getAudioBuffer(int samp2) {
		try {
			wavFile = WavFile.openWavFile(inputFile);
			int nframes = (int)Math.ceil(((float)(samp2)/wavFile.getNumChannels()));
			float[][] buffer = new float[wavFile.getNumChannels()][nframes*wavFile.getNumChannels()];
			wavFile.readFrames(buffer, nframes);
			return buffer;
		} catch (IOException | WavFileException e) {
			e.printStackTrace();
		} finally{
			try {
				wavFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	@Override
	public float[][][] getAllWindows() {
		WindowIterator iter = new WindowIterator();
		float[][][] allWindows = new float[wavFile.getNumChannels()][(int)nWindows][];
		int windowCount = 0;
		while(iter.hasNext() && windowCount<nWindows){
			float[][] nextWindow = iter.next();
			for(int i=0; i<getNChannels(); i++)
				allWindows[i][windowCount] = Arrays.copyOf(nextWindow[i], samplesPerWindow);
			windowCount++;
		}
		return allWindows;
	}

	private class WindowIterator implements Iterator<float[][]>{
		
		private float[][] currentWindow;
		private int windowIncrement;
		private WavFile myWavFile;
		
		public WindowIterator() {
			try {
				myWavFile = WavFile.openWavFile(inputFile);
				currentWindow = new float[myWavFile.getNumChannels()][(int)samplesPerWindow];
				windowIncrement = samplesPerWindow-windowOverlapSamples;
				// pre-populate window
				myWavFile.readFrames(currentWindow, windowOverlapSamples, windowIncrement);
			} catch (IOException | WavFileException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public boolean hasNext() {
			return myWavFile.getFramesRemaining()>0;
		}

		@Override
		public float[][] next() {
			try {
				// shift data in current window
				shiftWindow();
				// read new frames
				myWavFile.readFrames(currentWindow, windowOverlapSamples, windowIncrement);
				return currentWindow;
			} catch (IOException | WavFileException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public void remove() {
			throw new IllegalAccessError("Method not implemented");
		}

		@Override
		public void forEachRemaining(Consumer<? super float[][]> action) {
			throw new IllegalAccessError("Method not implemented");
		}
		
		private void shiftWindow(){
			for(int i=0; i<myWavFile.getNumChannels(); i++)
				for(int j=0; j<windowIncrement; j++)
					currentWindow[i][j] = currentWindow[i][(j+windowOverlapSamples)];
		}
		
	}

}
