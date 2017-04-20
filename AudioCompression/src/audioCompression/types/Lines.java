package audioCompression.types;

import java.util.Arrays;
import java.util.Iterator;

public class Lines implements AudioCompressionType {

	int sampleRate;
	int nWindows;
	int samplesPerWindow;
	int nChannels;
	int nBands;
	int byteDepth;
	float[][][][] windows;
	
	public Lines(int sampleRate, int nWindows, int samplesPerWindow,
			int nChannels, int nBands, int byteDepth, float[][][][] windows) {
		super();
		this.sampleRate = sampleRate;
		this.nWindows = nWindows;
		this.samplesPerWindow = samplesPerWindow;
		this.nChannels = nChannels;
		this.nBands = nBands;
		this.windows = windows;
		this.byteDepth = byteDepth;
	}

	public int getSampleRate() {
		return sampleRate;
	}

	public int getNWindows() {
		return nWindows;
	}

	public int getSamplesPerWindow() {
		return samplesPerWindow;
	}

	public int getNChannels() {
		return nChannels;
	}

	public int getNBands() {
		return nBands;
	}

	public int getByteDepth() {
		return byteDepth;
	}

	public void putWindow(int chan, int band, int window, float[] val){
		if(val.length != samplesPerWindow)
			throw new IllegalArgumentException("Subbands: Require val.length==samplesPerWindow");
		windows[chan][band][window] = val;
	}
	
	
	/**
	 * window dimensions: float[nChannels][nbands][nWindows][nSamples]
	 * @return
	 */
	public float[][][][] getAllWindows(){
		return windows;
	}
	
	
	/**
	 * Get an iterator over the windows of samples 
	 * dimesions float[nChannels][nBands][nSamples]
	 * @return
	 */
	public Iterator<float[][][]> getWindowIterator() {
		
		Iterator<float[][][]> iter = new Iterator<float[][][]>() {
			
			int cnt=0;
			
			@Override
			public float[][][] next(){
				float[][][] nextWindow = new float[nChannels][][];
				for(int iChan=0; iChan<nChannels; iChan++){
					nextWindow[iChan] = new float[nBands][];
					for(int jBand=0; jBand<nBands; jBand++)
						nextWindow[iChan][jBand] = Arrays.copyOf(windows[iChan][jBand][cnt],samplesPerWindow);
				}
				cnt++;
				return nextWindow;
			}
			
			@Override
			public boolean hasNext() {
				return cnt<nWindows;
			}
		}; 
		return iter;
		
	}
	
	
	
	
}
