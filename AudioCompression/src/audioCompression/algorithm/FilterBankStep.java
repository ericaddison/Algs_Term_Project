package audioCompression.algorithm;

import audioCompression.types.AudioCompressionType;
import audioCompression.types.RawAudio;
import audioCompression.types.Subbands;

public class FilterBankStep implements AlgorithmStep<RawAudio, Subbands> {

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

	@Override
	public Class<? extends AudioCompressionType> getInputClass() {
		return RawAudio.class;
	}

	@Override
	public Class<? extends AudioCompressionType> getOutputClass() {
		return Subbands.class;
	}

}
