package audioCompression.algorithm.dsp.window;

import java.util.Arrays;


/**
 * "Exact" Blackmann from wikipedia
 * @author eric
 *
 */
public class BlackmannWindow implements Window{

	private int N;
	private static final float a0 = 0.42659f;
	private static final float a1 = 0.49656f;
	private static final float a2 = 0.076849f;
	
	public BlackmannWindow(int N) {
		this.N = N;
	}
	
	@Override
	public void setLength(int length) {
		this.N = length;
	}

	@Override
	public int getLength() {
		return N;
	}

	@Override
	public float eval(int n) {
		return (float)(a0 
				- a1*Math.cos( 2*(Math.PI)*n / (N-1))
				+ a2*Math.cos( 4*(Math.PI)*n / (N-1)));
	}

	@Override
	public void apply(float[] in) {
		int oldN = N;
		N = in.length;
		for(int i=0; i<N; i++)
			in[i] *= eval(i);
		N = oldN;
	}

	@Override
	public float[] getCoefficients() {
		float[] window = new float[N];
		Arrays.fill(window, 1.0f);
		apply(window);
		return window;
	}

}
