package audioCompression.types;

import java.nio.ByteBuffer;

public class AudioByteBuffer implements AudioCompressionType{

	private ByteBuffer buffer;
	
	public AudioByteBuffer(ByteBuffer buffer) {
		this.buffer = buffer;
	}
	
	public ByteBuffer getBuffer(){
		return buffer;
	}

}
