package audioCompression.algorithm.dsp.window;


/**
 * A Bartlett (triangular) window
 * @author eric
 *
 */
public class BartlettWindow extends Window{

	public BartlettWindow(int N) {
		super(N);
	}
	
	@Override
	public float eval(int n) {
		return (float)(2.0/N) * ((n<N/2)?n:(N-n));
	}
	
}