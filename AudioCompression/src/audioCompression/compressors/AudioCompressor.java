package audioCompression.compressors;

import audioCompression.types.CompressedAudioFile;
import audioCompression.types.RawAudio;


/**
 * Interface to define operation of our AudioCompressors.
 * 
 * @author Eric Addison, Taylor Autry, Josh Musick
 *
 */
public interface AudioCompressor {

	public CompressedAudioFile compress(RawAudio rawInput, String compressedName);
	
	public RawAudio decompress(CompressedAudioFile compressedInput, String decompressName);
	
}
