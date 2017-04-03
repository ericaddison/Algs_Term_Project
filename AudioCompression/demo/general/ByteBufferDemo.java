package general;

import java.nio.ByteBuffer;


/**
 * Simple demo showing how to use a byte buffer
 * @author Eric Addison, Taylor Autry, Josh Musick
 *
 */
public class ByteBufferDemo {

	public static void main(String[] args){
		
		// create an array of random floats
		int N = 10;
		float[] floatArray = new float[N];
		for(int i=0; i<N; i++)
			floatArray[i] = (float)Math.random();
		
		// create a ByteBuffer
		ByteBuffer buffer = ByteBuffer.allocate(N*Float.SIZE/Byte.SIZE);
		
		// load the floats into the buffer
		for(float f : floatArray)
			buffer.putFloat(f);
		
		// rewind the buffer to play back
		buffer.rewind();
		
		// read all the bytes immediately as an array of bytes
		byte[] floatBytes = buffer.array();
		System.out.println("The first byte in the buffer is: " + Integer.toHexString((int)floatBytes[0]));
		
		// read the floats back from the byte buffer (need to rewind first)
		buffer.rewind();
		float readFloat = buffer.getFloat();
		System.out.println("The first float in the buffer is: " + readFloat);
		System.out.println("The first float from the original array is: " + floatArray[0]);
		System.out.println("The difference is: " + (floatArray[0]-readFloat));
		
	}
	
}
