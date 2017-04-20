package audioCompression.algorithm.bytes;


import java.nio.ByteBuffer;
import java.util.Arrays;

public class ByteUtils {

	
	/**
	 * Get the min and max values in an array, packed into a 2 element array as [min, max].
	 * @param fs
	 * @return
	 */
	public static float[] getMinMax(float[] fs) {
		float[] minMax = new float[] {Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY};
		for(int i=0; i<fs.length; i++){
			minMax[0] = Math.min(minMax[0], fs[i]);
			minMax[1] = Math.max(minMax[1], fs[i]);
		}
		return minMax;
	}
	
	public static byte[] float2bytes(float f, int byteDepth, float minVal, float maxVal) {
		// clip at +/- 1
		f = (float)(Math.max(Math.min(maxVal, f),minVal));
		int maxIntVal = getMaxIntVal(byteDepth);
		
		// scale to int
		int intVal = (int)(((f - minVal)/(maxVal-minVal)) * (maxIntVal));
		
		// write bytes
		ByteBuffer bb = ByteBuffer.allocate((Integer.SIZE)/Byte.SIZE);
		bb.putInt(intVal);
		bb.rewind();
		byte[] byteArray = new byte[byteDepth];
		int offset = ((Integer.SIZE)/Byte.SIZE) - byteDepth;
		for(int i=0; i<byteDepth; i++)
			byteArray[i] = bb.get(i + offset);
		return byteArray;
	}
	
	public static float bytes2float(byte[] b, int byteDepth, float minVal, float maxVal) {
		
		int nFloatBytes = Float.SIZE/Byte.SIZE;
		
		ByteBuffer floatBytes = ByteBuffer.allocate(nFloatBytes);
		for(int i=0; i<(nFloatBytes-byteDepth); i++)
			floatBytes.put((byte)0);
		floatBytes.put(b);
		

		// get the int value
		floatBytes.rewind();
		int intVal = floatBytes.getInt();
		
		// convert to float
		int maxIntVal = getMaxIntVal(byteDepth);
		float f = ((maxVal-minVal)*intVal)/maxIntVal + minVal;
		
		return f;
	}
	

	public static int getMaxIntVal(int byteDepth) {
		return (Integer.MAX_VALUE)>>((Integer.SIZE)-byteDepth*8);
	}
	
	
	
	public static void main(String[] args){
		
		float f = -0.147f;
		int byteDepth = 2;
		byte[] b = float2bytes(f, byteDepth, -0.2f, 0.2f);
		float f2 = bytes2float(b, byteDepth, -0.2f, 0.2f);
		System.out.println("my float: " + f);
		System.out.println("To bytes: " + Arrays.toString(b));
		System.out.println("BAck to float: " + f2);
		System.out.println("maxval = " + getMaxIntVal(byteDepth));
		
		
	}
}
