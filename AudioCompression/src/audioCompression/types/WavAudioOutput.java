package audioCompression.types;

import java.util.Arrays;
import java.util.Iterator;

import libs.wavParser.WavFile;

public class WavAudioOutput implements RawAudio{

	private WavFile wavFile;
	private int nChannels;
	private long nSamples;             // total number of samples (per channel) in the audio file 
	private int nWindows;             // total number of windows (including the overlap)
	private int samplesPerWindow;      // number of samples per window
	private int windowOverlap;       // percentage of window overlap
	private int sampleRate;
	private int byteDepth;
	private float[][][] windows;
	
	public WavAudioOutput(float[][][] windows, int windowOverlap, int sampleRate, int byteDepth) {
		this.nWindows = windows[0].length;
		this.samplesPerWindow = windows[0][0].length;
		this.windowOverlap = windowOverlap;
		this.nChannels = windows.length;
		this.sampleRate = sampleRate;
		this.windows = windows;
		this.byteDepth = byteDepth;
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
		float[][] buffer = new float[nChannels][samp2];
		WindowIterator iter = new WindowIterator();
		
		// prepopulate with first window
		for(int ichan=0; ichan<nChannels; ichan++){
			float[][] nextWin = iter.next();
			for(int i=0; i<samplesPerWindow; i++)
				buffer[ichan][i] = nextWin[ichan][i];
		}

		System.out.println("Overlap = " + windowOverlap);
		
		int sampsWritten = samplesPerWindow;
		while(iter.hasNext() && sampsWritten<samp2){
			float[][] nextWin = iter.next();
			sampsWritten -= windowOverlap;
			int nsamps = Math.min(samp2-sampsWritten, samplesPerWindow);

			for(int ichan=0; ichan<nChannels; ichan++){
			
				// do overlap zone
				for(int i=0; i<windowOverlap; i++){
					buffer[ichan][sampsWritten] += nextWin[ichan][i];
					buffer[ichan][sampsWritten] /= 2.0f;
					sampsWritten ++;
				}
				
				// no overlap zone
				for(int i=windowOverlap; i<nsamps; i++){
					buffer[ichan][sampsWritten] = nextWin[ichan][i];
					sampsWritten ++;
				}
					
			}
		}
		return buffer;
	}
	
	@Override
	public float[][][] getAllWindows() {
		return windows;
	}

	private class WindowIterator implements Iterator<float[][]>{
		
		private int windowCount=0;
		
		public WindowIterator() {
		}
		
		@Override
		public boolean hasNext() {
			return windowCount<nWindows;
		}

		@Override
		public float[][] next() {
			float[][] nextWin = new float[nChannels][];
			for(int i=0; i<nChannels; i++)
				nextWin[i] = Arrays.copyOf(windows[i][windowCount], samplesPerWindow);
			windowCount++;
			return nextWin;
		}
		
	}

	@Override
	public int getByteDepth() {
		return byteDepth;
	}

}
