package audioCompression.algorithm.dsp.window;

public class KaiserWindow extends Window{

	private edu.mines.jtk.dsp.KaiserWindow kw;
	
	/**
	 * width = transition zone width
	 * @param N
	 * @param width
	 */
	public KaiserWindow(int N, float width) {
		super(0);
		this.N = N;
		kw = edu.mines.jtk.dsp.KaiserWindow.fromWidthAndLength(width, N);
		coefficients = new float[N];
		for(int i=0; i<N; i++)
			coefficients[i] = eval(i);
	}
	
	@Override
	public float eval(int n) {
		return (float)kw.evaluate(n - N/2);
	}

}
