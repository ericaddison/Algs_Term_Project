package audioCompression.blocks;

import audioCompression.types.Lines;
import audioCompression.types.Subbands;

public class MDCT implements AlgorithmStep<Subbands, Lines> {

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


}
