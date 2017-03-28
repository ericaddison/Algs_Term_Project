package audioCompression.algorithm.dsp.window;

public interface Window {
	
	public void setLength(int length);
	public int getLength();
	public float eval(int n);
	public void apply(float[] in);
	public float[] getCoefficients();
	
}
