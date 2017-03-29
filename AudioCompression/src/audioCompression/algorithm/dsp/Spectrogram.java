package audioCompression.algorithm.dsp;

import audioCompression.algorithm.dsp.window.Window;

/**
 * Simple spectrogram implementation for a coarse time/frequency analysis
 * @author Eric Addison, Taylor Autry, Josh Musick
 *
 */
public class Spectrogram {

	public Spectrogram(float[] input, int sampsPerWindow, Window w) {
		
	}
	
	
	private float[][] STFT(Window w, float[] arr, ){
		int N = arr.length;
		int M = w.getLength();
		
		// get window scalar
		
		
	}
	
}
