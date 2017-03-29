package audioCompression.algorithm.dsp;

import audioCompression.algorithm.dsp.window.Window;

/**
 * Create a lowpass filter at a given normalized frequency cutoff,
 * by windowing a sinc function with a given window.
 * @author eric
 *
 */
public class LowPassFilter {

	private float[] coefficients;
	
	public LowPassFilter(float cutoff, Window w) {
		if(w==null)
			throw new IllegalArgumentException("LowPassFilter: Window w cannot be null");
		
		int N = w.getLength();
		coefficients = new float[N];
		for(int i=0; i<N; i++)
			coefficients[i] = (float)sinc( 2.0f*(i-N/2)*cutoff );
		coefficients = w.apply(coefficients);
	}
	
	public float[] getCoefficients(){
		return coefficients;
	}
	
	public float[] applyTimeDomain(float[] in){
		return in;
	}
	
	private double sinc(float x){
		return (x==0)?1.0:(Math.sin(Math.PI*x)/(Math.PI*x));
	}
	
}
