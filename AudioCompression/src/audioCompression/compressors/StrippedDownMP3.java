package audioCompression.compressors;

import audioCompression.algorithm.*;
import audioCompression.types.CompressedAudioFile;
import audioCompression.types.RawAudio;

/**
 * Our implementation of a stripped down version of MP3 compression.
 * This compression scheme consists of:
 * <ul>
 * <li> a polyphase filter bank</li>
 * <li> a modified discrete cosine transform (MDCT)</li>
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
		pipeline.addStep(new FilterBankStep());
		pipeline.addStep(new MdctStep());
		pipeline.addStep(new ByteBufferizerStep());
		pipeline.addStep(new HuffmanEncoderStep());
		pipeline.addStep(new CompressedAudioFileWriter());
	}
	
	@Override
	public CompressedAudioFile compress(RawAudio rawInput){
		return (CompressedAudioFile) pipeline.processForward(rawInput);
	}
	
	@Override
	public RawAudio decompress(CompressedAudioFile compressedInput){
		return (RawAudio) pipeline.processReverse(compressedInput);
	}
	
}
