package audioCompression.algorithm.dsp.window;


/**
 * "Exact" Blackmann from wikipedia
 * @author eric
 *
 */
public class BlackmannWindow extends Window{

	private static final float a0 = 0.42659f;
	private static final float a1 = 0.49656f;
	private static final float a2 = 0.076849f;

	public BlackmannWindow(int N) {
		super(N);
	}
	
	@Override
	public float eval(int n) {
		return (float)(a0 
				- a1*Math.cos( 2*(Math.PI)*n / (N-1))
				+ a2*Math.cos( 4*(Math.PI)*n / (N-1)));
	}
	
}