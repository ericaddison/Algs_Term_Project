package audioCompression.algorithm;

import audioCompression.types.AudioCompressionType;
import audioCompression.types.CompressedAudio;
import audioCompression.types.EncodedLines;

public class BitstreamFormatterStep implements AlgorithmStep<EncodedLines, CompressedAudio> {

	@Override
	public CompressedAudio forward(EncodedLines input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EncodedLines reverse(CompressedAudio input) {
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
		return EncodedLines.class;
	}

	@Override
	public Class<? extends AudioCompressionType> getOutputClass() {
		return CompressedAudio.class;
	}

}
