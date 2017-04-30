package audioCompression.compressors;

import audioCompression.algorithm.*;
import audioCompression.types.CompressedAudioFile;
import audioCompression.types.AudioCompressionType;
import audioCompression.types.AudioFile;

/**
 * Our implementation of a stripped down version of MP3 compression.
 * This compression scheme consists of:
 * <ul>
 * <li> a polyphase filter bank</li>
 * <li> a modified discrete cosine transform (MDCT)</li>
 * <li> a byte bufferizer from line / sub-band data</li>
 * <li> huffman encoding</li>
 * <li> serialization</li>
 * </ul>
 * 
 * @author Eric Addison, Taylor Autry, Josh Musick
 *
 */
public class StrippedDownMP3 implements AudioCompressor{

	private CompressionPipeline pipeline = new CompressionPipeline();
	
	public StrippedDownMP3() {
		pipeline.addStep(new InputOutputStep());
		pipeline.addStep(new FilterBankStep());
	//	pipeline.addStep(new MdctStep());
		pipeline.addStep(new SubbandsByteBufferizerStep());
	//	pipeline.addStep(new LinesByteBufferizerStep());
		pipeline.addStep(new HuffmanEncoderStep());
		pipeline.addStep(new SerializationStep());
	}
	
	@Override
	public CompressedAudioFile compress(AudioCompressionType rawInput, String compressedName){
		return (CompressedAudioFile) pipeline.processForward(rawInput, compressedName);
	}
	
	@Override
	public AudioFile decompress(CompressedAudioFile compressedInput, String decompressName){
		return (AudioFile) pipeline.processReverse(compressedInput, decompressName);
	}
	
}
