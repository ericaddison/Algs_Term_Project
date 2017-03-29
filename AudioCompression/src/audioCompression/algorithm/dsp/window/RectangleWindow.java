package audioCompression.algorithm.dsp.window;

public class RectangleWindow extends Window{


	public RectangleWindow(int N) {
		super(N);
	}

	@Override
	public float eval(int n) {
		return 1.0f;
	}


}
