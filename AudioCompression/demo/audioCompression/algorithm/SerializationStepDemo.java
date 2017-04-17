package audioCompression.algorithm;

import java.nio.ByteBuffer;
import java.util.Random;

import audioCompression.algorithm.SerializationStep;
import audioCompression.types.AudioByteBuffer;
import audioCompression.types.CompressedAudioFile;

public class SerializationStepDemo {

	public static void main(String[] args) {
		
		// This will test the implementation of the Serialization step
		// create an audio Byte buffer, pass into the forward step, it should create
		// a file with the data, and then pass that back through the reverse stage,
		// the data on each end should be identical
		
		int numEle = 1000;
		int []data = new int[numEle];
		
		Random r = new Random(System.currentTimeMillis());
		ByteBuffer buff = ByteBuffer.allocate(numEle * (Integer.SIZE/Byte.SIZE));
		for (int i = 0; i < numEle; ++i) {
			data[i] = r.nextInt();
			buff.putInt(data[i]);
		}
		
		int testIndex = 10;
		// intentionally change the data at one point to verify the original data stream
		if (data[testIndex] != 5) {
			data[testIndex] = 5;
		} else {
			data[testIndex] = 6;
		}
				
	//	System.out.println("buffer position after inserting " + String.valueOf(numEle) + " is at " + String.valueOf(buff.position()));
		buff.rewind();
	//	System.out.println("buffer position after inserting " + String.valueOf(numEle) + " is at " + String.valueOf(buff.position()));
		
		AudioByteBuffer aBuff = new AudioByteBuffer(buff);
		SerializationStep step = new SerializationStep();
		
		CompressedAudioFile cf = step.forward(aBuff, "../data/test_buffer_comp.dat");
		
		AudioByteBuffer outBuff = step.reverse(cf, "name");
		ByteBuffer ob = outBuff.getBuffer();	
		for (int i = 0; i < numEle; ++i) {
			boolean valid = true;
			// check the data in the output buffer, to verify it matches the input
			if (ob.getInt() != data[i]) {
				valid = false;
			}
			if (!valid && (testIndex != i)) {
				System.out.println("Mismatch data at index " + String.valueOf(i));
			}			
		}		
		System.out.println("Successfully Serialized and Deserialized the data stream");
	}	
}
