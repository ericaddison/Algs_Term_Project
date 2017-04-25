package audioCompression.compressors;

import audioCompression.types.CompressedAudioFile;
import audioCompression.types.RawAudio;
import audioCompression.types.AudioCompressionType;
import audioCompression.types.AudioFile;

/**
 * Interface to define operation of our AudioCompressors.
 * 
 * @author Eric Addison, Taylor Autry, Josh Musick
 *
 */
public interface AudioCompressor {

	public CompressedAudioFile compress(AudioCompressionType input, String compressedName);
	
	public AudioFile decompress(CompressedAudioFile compressedInput, String decompressName);
	
}
