package audioCompression.algorithm;

import audioCompression.types.AudioCompressionType;
import audioCompression.types.Lines;
import audioCompression.types.Subbands;

public class MdctStep implements AlgorithmStep<Subbands, Lines> {

	private static final String NAME = "MDCT";
	
	@Override
	public Lines forward(Subbands input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Subbands reverse(Lines input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Class<? extends AudioCompressionType> getInputClass() {
		return Subbands.class;
	}

	@Override
	public Class<? extends AudioCompressionType> getOutputClass() {
		return Lines.class;
	}


}
