package audioCompression.algorithm.dsp;

import java.util.Arrays;

import audioCompression.algorithm.dsp.window.Window;
import edu.mines.jtk.dsp.Fft;

public class ShortTimeFft {

	private Window w;
	private int sampleOverlap;
	

	public ShortTimeFft(Window w, int sampleOverlap) {
		super();
		this.w = w;
		this.sampleOverlap = sampleOverlap;
		checkWindow(w, sampleOverlap);
	}

	public Window getWindow() {
		return w;
	}

	public void setWindow(Window w) {
		checkWindow(w, sampleOverlap);
		this.w = w;
	}

	public int getSampleOverlap() {
		return sampleOverlap;
	}

	public void setSampleOverlap(int sampleOverlap) {
		checkWindow(w, sampleOverlap);
		this.sampleOverlap = sampleOverlap;
	}




	/**
	 * Computes the forward STFT for the given array 
	 * using the set Window and overlap. Pads the last 
	 * segment of data with zeros to match the window length
	 * @param arr
	 * @return computed STFT
	 */
	public float[][] computeForward(float[] arr){
		int N = arr.length;		// length of full array
		int M = w.getLength();	// window length
		
		int increment = M-sampleOverlap;
		
		int nWindows = (N-M)/increment+1;

		float[] arr2 = arr;
		if(N%M>0)
			arr2 = Arrays.copyOf(arr, (M-N%M));
		
		float[][] stft = new float[nWindows][];
		
		Fft fft = new Fft(M);
		for(int i=0; i<nWindows; i++)
			stft[i] = fft.applyForward(w.applyNormalized(arr2, i*increment));
		
		return stft;
	}
	

	private void checkWindow(Window w, int sampleOverlap) {
		if(sampleOverlap > w.getLength()/2)
			throw new IllegalArgumentException("ShortTimeFft: Require sampleOverlap < windowLength/2");
	}

	
}
