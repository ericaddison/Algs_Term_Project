package audioCompression.algorithm.dsp.window;

public class HannWindow extends Window{


	public HannWindow(int N) {
		super(N);
	}

	@Override
	public float eval(int n) {
		return (float)(0.5*(1-Math.cos( 2*(Math.PI)*n / (N-1))));
	}

}
