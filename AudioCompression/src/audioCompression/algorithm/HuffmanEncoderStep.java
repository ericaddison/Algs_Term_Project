package audioCompression.algorithm;

import audioCompression.types.AudioCompressionType;
import audioCompression.types.EncodedLines;
import audioCompression.types.Lines;

public class HuffmanEncoderStep implements AlgorithmStep<Lines, EncodedLines>  {

	@Override
	public EncodedLines forward(Lines input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Lines reverse(EncodedLines input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public Class<? extends AudioCompressionType> getInputClass() {
		return Lines.class;
	}

	@Override
	public Class<? extends AudioCompressionType> getOutputClass() {
		return EncodedLines.class;
	}

}
