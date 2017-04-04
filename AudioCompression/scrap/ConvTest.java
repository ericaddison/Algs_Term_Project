import java.util.Arrays;

import edu.mines.jtk.dsp.Conv;

public class ConvTest {

	public static void main(String[] args){
		
		float[] x1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		float[] x2 = {10, 20, 30, 40};
		float[] x3 = new float[x1.length+x2.length-1];
		
		Conv.conv(x1.length, 0, x1, x2.length, 0, x2, x1.length+x2.length-1, 0, x3);
		System.out.println(Arrays.toString(x3));
		
		x3 = new float[x1.length];
		Conv.conv(x1.length, 0, x1, x2.length, 0, x2, x1.length, (x1.length-x2.length-1)/2, x3);
		System.out.println(Arrays.toString(x3));
	}
}
