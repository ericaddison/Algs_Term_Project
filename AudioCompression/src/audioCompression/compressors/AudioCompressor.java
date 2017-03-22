package audioCompression.compressors;

import audioCompression.types.CompressedAudio;
import audioCompression.types.RawAudio;


/**
 * Interface to define operation of our AudioCompressors.
 * 
 * @author Eric Addison, Taylor Autry, Josh Musick
 *
 */
public interface AudioCompressor {

	public CompressedAudio compress(RawAudio rawInput);
	
	public RawAudio decompress(CompressedAudio compressedInput);
	
}
