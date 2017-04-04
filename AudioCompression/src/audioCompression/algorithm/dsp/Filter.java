package audioCompression.algorithm.dsp;

import edu.mines.jtk.dsp.Conv;
import audioCompression.algorithm.dsp.window.Window;

/**
 * Create a lowpass filter at a given normalized frequency cutoff,
 * by windowing a sinc function with a given window.
 * @author eric
 *
 */
public class Filter {

	private float[] coefficients;
	
	Filter(float cutoff, Window w) {
		if(w==null)
			throw new IllegalArgumentException("Filter: Window w cannot be null");
		
		int N = w.getLength();
		coefficients = new float[N];
		for(int i=0; i<N; i++)
			coefficients[i] = (float)sinc( 2.0f*(i-N/2)*cutoff );
		coefficients = w.apply(coefficients);
	}

	Filter(float[] coefficients) {
		this.coefficients = coefficients;
	}
	
	Filter(Filter filter, int decimationFactor){
		int N = filter.coefficients.length;
		coefficients = new float[N/decimationFactor];
		for(int i=0; i<N; i+=decimationFactor)
			coefficients[i/decimationFactor] = filter.coefficients[i];
	}
	
	public float[] getCoefficients(){
		return coefficients;
	}
	
	public float[] applyTimeDomain(float[] in){
		float[] out = new float[in.length];
		Conv.conv(in.length, 0, in, 
				coefficients.length, 0, coefficients,
				in.length, Math.abs(in.length-coefficients.length)/2, out);
		return out;
	}
	
	private double sinc(float x){
		return (x==0)?1.0:(Math.sin(Math.PI*x)/(Math.PI*x));
	}
	
}
