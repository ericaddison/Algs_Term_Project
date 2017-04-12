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
	private int nchannels;
	
	public RawAudioImpl(int nSamps, int sampsPerWindow, int windowOverlap, int nchannels) {
		if(windowOverlap>sampsPerWindow/2 || windowOverlap<0)
			throw new IllegalArgumentException("RawAudioImpl: require 0<=windowOverlap<=sampsPerWindow/2");
		this.sampsPerWindow = sampsPerWindow;
		this.overlap = windowOverlap;
		this.windowIncrement = (sampsPerWindow-windowOverlap);
		this.nwindows = nSamps/windowIncrement+1;
		nSamps = nwindows*windowIncrement + windowOverlap;
		
		System.out.println("I think I should have " + nwindows + " windows and " + nSamps + " samples");
		this.nchannels = nchannels;
		
		buffer = new float[nchannels][nSamps];
		float f0 = 0.05f;
		int nDivs = 2;
		float df = 2*0.01f/nSamps * nDivs;
		float f = 0;
		
		for(int i=0; i<nSamps/nDivs; i++){
			for(int j=0; j<nDivs/2; j++){
				float ch1 = (float)Math.sin(2*Math.PI*i*(f0+f));
				for(int k=0; k<nchannels; k++){
					buffer[k][i+2*j*nSamps/nDivs] = ch1;
					buffer[k][nSamps-(i+2*j*nSamps/nDivs)-1] = ch1;
				}
			}
			f += df;
		}
		
		windows = new float[nchannels][nwindows][sampsPerWindow];
		for(int i=0; i<nwindows; i++){
			for(int j=0; j<sampsPerWindow; j++){
				for(int k=0; k<nchannels; k++){
					windows[k][i][j] = buffer[k][i*windowIncrement + j];
				}
			}
		}
	}
	
	@Override
	public void writeFile(String filename) {
		// todo - write out the wav file
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
	public int getNWindows() {
		return nwindows;
	}

	@Override
	public int getWindowOverlap() {
		return overlap;
	}

	@Override
	public int getNChannels() {
		return nchannels;
	}

	@Override
	public float[][] getAudioBuffer(int nsamples) {
		float[][] newBuf = new float[nchannels][nsamples];
		for(int ichan=0; ichan<nchannels; ichan++)
			for(int j=0; j<nsamples; j++)
				newBuf[ichan][j] = buffer[ichan][j];
		return newBuf;
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
				float[][] nextWindow = new float[nchannels][];
				for(int k=0; k<nchannels; k++)
					nextWindow[k] = Arrays.copyOf(windows[k][cnt],sampsPerWindow);
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

	@Override
	public int getByteDepth() {
		return 4;
	}
}
