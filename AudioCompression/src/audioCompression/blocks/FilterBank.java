package audioCompression.blocks;

import audioCompression.types.RawAudio;
import audioCompression.types.Subbands;

public class FilterBank implements AlgorithmStep<RawAudio, Subbands> {

	private static final String NAME = "Polyphase Filterbank";
	
	@Override
	public Subbands forward(RawAudio input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RawAudio reverse(Subbands input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return NAME;
	}

}
