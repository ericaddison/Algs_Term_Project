package audioCompression.algorithm;

import audioCompression.types.AudioByteBuffer;
import audioCompression.types.AudioCompressionType;
import audioCompression.types.Lines;

public class ByteBufferizerStep implements AlgorithmStep<Lines, AudioByteBuffer>{

	@Override
	public AudioByteBuffer forward(Lines input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Lines reverse(AudioByteBuffer input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends AudioCompressionType> getInputClass() {
		// TODO Auto-generated method stub
		return Lines.class;
	}

	@Override
	public Class<? extends AudioCompressionType> getOutputClass() {
		// TODO Auto-generated method stub
		return AudioByteBuffer.class;
	}

}
