package audioCompression.algorithm.dsp.window;

import java.util.Arrays;

public class KaiserWindow implements Window{


	private int N;
	private edu.mines.jtk.dsp.KaiserWindow kw;
	
	/**
	 * width = transition zone width
	 * @param N
	 * @param width
	 */
	public KaiserWindow(int N, float width) {
		this.N = N;
		kw = edu.mines.jtk.dsp.KaiserWindow.fromWidthAndLength(width, N);
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
		return (float)kw.evaluate(n - N/2);
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
