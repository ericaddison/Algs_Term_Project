package audioCompression.types.testImpls;

import java.util.Arrays;
import java.util.Iterator;

import audioCompression.types.RawAudio;

/**
 * An implementation of RawAudio for testing
 * @author Eric Addison, Taylor Autry, Josh Musick
 *
 */
public class RawAudioImpl implements RawAudio{

	private float[][] buffer;
	private float[][][] windows;
	private int sampsPerWindow;
	private int overlap;
	private int nwindows;
	private int windowIncrement;
	
	public RawAudioImpl(int nSamps, int sampsPerWindow, int windowOverlap) {
		if(windowOverlap>sampsPerWindow/2 || windowOverlap<0)
			throw new IllegalArgumentException("RawAudioImpl: require 0<=windowOverlap<=sampsPerWindow/2");
		this.sampsPerWindow = sampsPerWindow;
		this.overlap = overlap;
		this.windowIncrement = (sampsPerWindow-windowOverlap);
		this.nwindows = nSamps/windowIncrement;
		
		
		buffer = new float[2][nSamps];
		float f0 = 0.001f;
		float df = 0.000025f;
		for(int i=0; i<nSamps; i++){
			buffer[0][i] = (float)Math.sin(2*Math.PI*(f0+i*df)*i);
			buffer[1][i] = (float)Math.cos(2*Math.PI*(f0+i*df)*i);
		}
		
		windows = new float[2][nwindows][sampsPerWindow];
		for(int i=0; i<nwindows; i++){
			for(int j=0; j<sampsPerWindow; j++){
				windows[0][i][j] = buffer[0][i*windowIncrement + j];
				windows[1][i][j] = buffer[1][i*windowIncrement + j];
			}
		}
	}
	
	@Override
	public long getSampleRate() {
		return 1;
	}

	@Override
	public long getNSamples() {
		return buffer[0].length;
	}

	@Override
	public long getSamplesPerWindow() {
		return sampsPerWindow;
	}

	@Override
	public long getNWindows() {
		return nwindows;
	}

	@Override
	public int getWindowOverlap() {
		return overlap;
	}

	@Override
	public int getNChannels() {
		return 2;
	}

	@Override
	public float[][] getAudioBuffer(int nsamples) {
		return buffer;
	}

	@Override
	public float[][][] getAllWindows() {
		return windows;
	}

	@Override
	public Iterator<float[][]> getWindowIterator() {
		Iterator<float[][]> iter = new Iterator<float[][]>() {
			
			int cnt=0;
			
			@Override
			public float[][] next(){
				float[][] nextWindow = new float[2][];
				nextWindow[0] = Arrays.copyOf(windows[0][cnt],sampsPerWindow);
				nextWindow[1] = Arrays.copyOf(windows[1][cnt],sampsPerWindow);
				cnt++;
				return nextWindow;
			}
			
			@Override
			public boolean hasNext() {
				return cnt<nwindows;
			}
		}; 
		return iter;
	}

}
