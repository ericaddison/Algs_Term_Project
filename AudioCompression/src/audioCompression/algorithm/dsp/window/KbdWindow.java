package audioCompression.algorithm.dsp.window;

/**
 * Kaiser-Bessel derived window
 * @author eric
 *
 */
public class KbdWindow extends Window{

	private Window w;
	private float[] csum;
	
	/**
	 * width = width of the transition zone of the underlying Kaiser window
	 * @param N
	 * @param width
	 */
	public KbdWindow(int N, float width) {
		super(N);
		w = new KaiserWindow(N/2, width);
		csum = cumsum(w.getCoefficients());
		setLength(N);
	}

	@Override
	public float eval(int n) {
		if(w==null)
			return 0;
		
		int M = w.getLength();
		
		if(n < M)
			return (float)Math.sqrt(csum[n]/csum[M-1]);
		
		if(n < 2*M)
			return (float)Math.sqrt(csum[2*M-1-n]/csum[M-1]);
		
		return 0;
	}
	
	private float[] cumsum(float[] in){
		float[] out = new float[in.length];
		out[0] = in[0];
		
		for(int i = 1; i<in.length; i++)
			out[i] = out[i-1] + in[i];
		
		return out;
	}
	
}
