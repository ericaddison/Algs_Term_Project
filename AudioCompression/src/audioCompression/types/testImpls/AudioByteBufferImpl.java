package audioCompression.types.testImpls;

import java.nio.ByteBuffer;
import java.util.Random;

import audioCompression.types.AudioByteBuffer;

public class AudioByteBufferImpl extends AudioByteBuffer{

	public AudioByteBufferImpl(int N) {
		super(ByteBuffer.allocate(N*(Integer.SIZE/Byte.SIZE)));
		Random r = new Random(System.currentTimeMillis()); 
		for(int i=0; i<N; i++)
			getBuffer().putInt(r.nextInt());
		getBuffer().rewind();
	}

}
