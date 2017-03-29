package audioCompression.algorithm.dsp.window;

public abstract class Window {
	
	protected float[] coefficients;
	protected int N;
	protected float norm;
	
	abstract public float eval(int n);

	public Window(int N) {
		this.N = N;
		coefficients = new float[N];
		for(int i=0; i<N; i++)
			coefficients[i] = eval(i);
		norm = computeNorm();
	}
	
	public void setLength(int length) {
		this.N = length;
		for(int i=0; i<N; i++)
			coefficients[i] = eval(i);
		norm = computeNorm();
	}
	
	public int getLength() {
		return N;
	}

	public float[] getCoefficients() {
		return coefficients;
	}
	
	public float[] apply(float[] in) {
		return apply(in, 1.0f, 0);
	}
	
	public float[] apply(float[] in, int offset) {
		return apply(in, 1.0f, offset);
	}

	public float[] applyNormalized(float[] in, int offset) {
		return apply(in, norm, offset);
	}
	
	private float computeNorm() {
		float norm = 0;
		for(int i=0; i<N; i++)
			norm += eval(i);
		return norm;
	}

	private float[] apply(float[] in, float fac, int offset){
		float[] applied = new float[N];
		for(int i=0; i<N; i++)
			applied[i] = in[i+offset]*fac*coefficients[i];
		return applied;
	}
	
}
