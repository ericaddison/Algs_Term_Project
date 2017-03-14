package audioCompression.compressors;

import audioCompression.types.CompressedAudio;
import audioCompression.types.RawAudio;

public interface AudioCompressor {

	public CompressedAudio compress(RawAudio rawInput);
	
	public RawAudio decompress(CompressedAudio compressedInput);
	
}
