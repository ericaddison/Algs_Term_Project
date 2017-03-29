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

	public void apply(float[] in) {
		apply(in, 1.0f);
	}

	public float[] getCoefficients() {
		return coefficients;
	}

	public void applyNormalized(float[] in) {
		float oldNorm = norm;
		if(in.length != N)
			norm = computeNorm();
		apply(in, norm);
		norm = oldNorm;
	}
	
	private float computeNorm() {
		float norm = 0;
		for(int i=0; i<N; i++)
			norm += eval(i);
		return norm;
	}

	private void apply(float[] in, float fac){
		int oldN = N;
		if(in.length != N){
			N = in.length;
			for(int i=0; i<N; i++)
				in[i] *= eval(i);
		} else
			for(int i=0; i<N; i++)
				in[i] *= fac*coefficients[i];
		N = oldN;
	}
	
}
