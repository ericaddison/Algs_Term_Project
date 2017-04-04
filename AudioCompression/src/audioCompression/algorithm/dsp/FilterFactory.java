package audioCompression.algorithm.dsp;

import audioCompression.algorithm.dsp.window.Window;

public class FilterFactory {

	public static Filter makeLowpassFilter(float cutoff, Window w){
		return new Filter(cutoff, w);
	}
	
	public static Filter makeFilter(float[] coefficients){
		return new Filter(coefficients);
	}
	
	public static Filter makeDecimatedFilter(Filter filter, int decimationFactor){
		return new Filter(filter, decimationFactor);
	}
	
}
