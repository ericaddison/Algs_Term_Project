package audioCompression.algorithm.dsp.window;

import java.util.Arrays;

public class HannWindow implements Window{

	private int N;
	
	public HannWindow(int N) {
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
		return (float)(0.5*(1-Math.cos( 2*(Math.PI)*n / (N-1))));
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
