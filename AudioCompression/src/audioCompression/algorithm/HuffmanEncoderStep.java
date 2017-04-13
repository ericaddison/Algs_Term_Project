package audioCompression.algorithm;

import audioCompression.types.AudioByteBuffer;
import audioCompression.types.AudioCompressionType;

public class HuffmanEncoderStep implements AlgorithmStep<AudioByteBuffer, AudioByteBuffer>  {

	private static final String NAME = "Huffman Encoder";

	@Override
	public AudioByteBuffer forward(AudioByteBuffer input, String name) {
		// TODO Auto-generated method stub
		return input;
	}

	@Override
	public AudioByteBuffer reverse(AudioByteBuffer input, String name) {
		// TODO Auto-generated method stub
		return input;
	}

	@Override
	public String getName() {
		return NAME;
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
