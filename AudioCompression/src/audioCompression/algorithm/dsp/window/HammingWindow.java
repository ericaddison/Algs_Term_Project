package audioCompression.algorithm.dsp.window;

public class HammingWindow extends Window{

	private static final float a = 0.53836f;
	private static final float b = (1-a);

	public HammingWindow(int N) {
		super(N);
	}

	@Override
	public float eval(int n) {
		return (float)(a - b*Math.cos( 2*(Math.PI)*n / (N-1)));
	}

}
