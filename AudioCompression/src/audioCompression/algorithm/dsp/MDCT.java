package audioCompression.algorithm.dsp;

import java.util.Arrays;

import audioCompression.algorithm.dsp.window.KbdWindow;
import audioCompression.algorithm.dsp.window.Window;

/**
 * Modified discrete cosine transform.
 * Vanilla implementation, NOT a "fast" implementation using FFTs...
 * @author eric
 *
 */
public class MDCT {

	
	public static float[] forward(float[] x){
		
		int N = x.length/2;
		float[] X = new float[N];
		
		for(int k=0; k<N; k++)
			for(int n=0; n<2*N; n++)
				X[k] += x[n]*Math.cos(Math.PI/N * (n + 0.5 + N/2) * (k + 0.5));
		
		return X;
	}
	
	public static float[] reverse(float[] X){
		
		int N = X.length;
		float[] y = new float[2*N];
		
		for(int n=0; n<2*N; n++){
			for(int k=0; k<N; k++)
				y[n] += X[k]*Math.cos(Math.PI/N * (n + 0.5 + N/2) * (k + 0.5));
			y[n] /= (N/2);
		}
		
		return y;
	}
	
	
	public static void main(String args[]){
		
		Window w = new KbdWindow(8, 5);
		
		float[] x1 = new float[] {1, 2, 3, 4, 5, 6, 7, 8};
		float[] x2 = new float[] {5, 6, 7, 8, 9, 10, 11, 12};
		float[] x3 = new float[] {9, 10, 11, 12, 13, 14, 15, 16};
		float[] x4 = new float[] {13, 14, 15, 16, 9, 9, 9, 19};
		
		float[] X1 = forward(w.apply(x1));
		float[] X2 = forward(w.apply(x2));
		float[] X3 = forward(w.apply(x3));
		float[] X4 = forward(w.apply(x4));
		
		float[] y1 = w.apply(reverse(X1));
		float[] y2 = w.apply(reverse(X2));
		float[] y3 = w.apply(reverse(X3));
		float[] y4 = w.apply(reverse(X4));
		
		System.out.println(Arrays.toString(x1));
		System.out.println(Arrays.toString(X1));
		System.out.println(Arrays.toString(X2));
		System.out.println(Arrays.toString(X3));
		System.out.println();
		System.out.println(Arrays.toString(y1));
		System.out.println(Arrays.toString(y2));
		System.out.println(Arrays.toString(y3));
		System.out.println(Arrays.toString(y4));
		
	}
	
}
