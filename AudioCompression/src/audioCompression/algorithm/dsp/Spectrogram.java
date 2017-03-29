package audioCompression.algorithm.dsp;

import audioCompression.algorithm.dsp.window.Window;

/**
 * Simple spectrogram implementation for a coarse time/frequency analysis
 * @author Eric Addison, Taylor Autry, Josh Musick
 *
 */
public class Spectrogram {

	public Spectrogram(float[] input, int sampleOverlap, Window w) {
		
	}
	
	
	private float[][] STFT(float[] arr, int sampleOverlap, Window w){
		int N = arr.length;		// length of full array
		int M = w.getLength();	// window length
		
		
		return null;
	}
	
}
