package audioCompression.compressors;

import audioCompression.algorithm.*;
import audioCompression.types.*;

/**
 * Our implementation of a full compression pipeline, with configurable
 * options for adjusting different capabilities
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
public class FullAudioCompressor implements AudioCompressor {

	// Pipeline which will hold the various steps
	private CompressionPipeline pipeline = new CompressionPipeline();
	
	// Internal flag for knowing if the MDCT step is currently active
	private boolean m_bMDCTEnabled;
	private boolean m_bAdaptiveByteBuffEnabled;
	
	// Create and keep a pointer to the filterbank, for easier configuration
	private FilterBankStep fBankStep = new FilterBankStep();
	
	private MdctStep m_mdctStep = new MdctStep();
	private AdaptiveLinesByteBufferizerStep adapt_LBB= new AdaptiveLinesByteBufferizerStep();
	private LinesByteBufferizerStep m_LineBB = new LinesByteBufferizerStep();
	
	/// Create the Sub-band byte bufferizers
	private SubbandsByteBufferizerStep subBand_BB = new SubbandsByteBufferizerStep();
	private AdaptiveSubbandsByteBufferizerStep adaptSubBand_BB = new AdaptiveSubbandsByteBufferizerStep();
	
	/// Create the huffman encoder
	private HuffmanEncoderStep huffman = new HuffmanEncoderStep();
	
	public FullAudioCompressor()
	{
		m_bMDCTEnabled = false;
		m_bAdaptiveByteBuffEnabled = false;		
		
		pipeline.addStep(fBankStep);
		pipeline.addStep(subBand_BB);
		pipeline.addStep(huffman);
		pipeline.addStep(new SerializationStep());		
		
	}
	
	@Override // compress the given input file
	public CompressedAudioFile compress(RawAudio rawInput, String compressedName) {
		return (CompressedAudioFile) pipeline.processForward(rawInput, compressedName);
	}

	@Override // decompress the given input file
	public RawAudio decompress(CompressedAudioFile compressedInput, String decompressName) {
		return (RawAudio) pipeline.processReverse(compressedInput, decompressName);
	}

	// Method for toggling the adaptive encoding of the Huffman Step
	//  if enable is false, single pass encoding will be used
	public void EnableAdaptiveEncoding(boolean enable)
	{
		// do stuff to the Huffman encoder to enable the adaptive encoding
		// huffman.getClass();
	}
	
	// Method for enabling / disabling the MDCT step
	public void EnableMDCTStep(boolean enable)
	{
		if (m_bMDCTEnabled == enable) return;
		// otherwise, we need to do stuff
		
		m_bMDCTEnabled = enable;
		if (enable) {
			// we need to add the MDCT
			pipeline.removeStep(1); // remove the sub-band byte bufferizer
			pipeline.addStep(m_mdctStep, 1);
			if (m_bAdaptiveByteBuffEnabled) {
				pipeline.addStep(adapt_LBB, 2);
			} else {
				pipeline.addStep(m_LineBB, 2);
			}
			
		} else {
			// we need to remove the MDCT and Line byte bufferizers, and add the band byte bufferizers
			pipeline.removeStep(1);
			pipeline.removeStep(1);
			if (m_bAdaptiveByteBuffEnabled) {
				pipeline.addStep(adaptSubBand_BB, 1);
			} else {
				pipeline.addStep(subBand_BB, 1);
			}
		}
		if (!pipeline.isPipelineValid()) {
			throw new IllegalArgumentException("CompressionPipeline: Invalid matching of output to input to next step");
		}
		
	}
	
}
