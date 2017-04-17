package audioCompression.algorithm;

import audioCompression.types.AudioByteBuffer;
import audioCompression.types.AudioCompressionType;

public class HuffmanEncoderStep implements AlgorithmStep<AudioByteBuffer, AudioByteBuffer>  {

	@Override
	public AudioByteBuffer forward(AudioByteBuffer input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AudioByteBuffer reverse(AudioByteBuffer input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public Class<? extends AudioCompressionType> getInputClass() {
		return AudioByteBuffer.class;
	}

	@Override
	public Class<? extends AudioCompressionType> getOutputClass() {
		return AudioByteBuffer.class;
	}

}
