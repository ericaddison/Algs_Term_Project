package audioCompression.compressors;

import audioCompression.algorithm.*;
import audioCompression.algorithm.dsp.window.HannWindow;
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
	private LinesByteBufferizerStep adapt_LBB= new LinesByteBufferizerStep(true);
	private LinesByteBufferizerStep lineBB = new LinesByteBufferizerStep();
	
	/// Create the Sub-band byte bufferizers
	private SubbandsByteBufferizerStep subBand_BB = new SubbandsByteBufferizerStep();
	private SubbandsByteBufferizerStep adaptSubBand_BB = new SubbandsByteBufferizerStep(true);
	
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
		// huffman.enableAdaptiveEncoding(enable);
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
				pipeline.addStep(lineBB, 2);
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
	
	/// Method for enabling / disabling the Adaptive Byte Buffer vs Regular Byte Buffer
	public void EnableAdaptiveByteBufferizer(boolean enable)
	{
		// if the adaptive byte bufferizer is already set properly, do nothing
		if (m_bAdaptiveByteBuffEnabled == enable) { return; }
		
		int index = 1;
		if (m_bMDCTEnabled) {
			index = 2;
		}
		m_bAdaptiveByteBuffEnabled = enable;
		pipeline.removeStep(index);
		if (enable) {
			// switch out regular for the adaptive
			pipeline.addStep(m_bMDCTEnabled ? adapt_LBB : adaptSubBand_BB, index);		
		} else {
			pipeline.addStep(m_bMDCTEnabled ? lineBB : subBand_BB, index);			
		}		
	}
	
	/// Sets the number of sub bands to be used in the Filter Bank
	public void SetNumberOfSubBands(int num)
	{
		if (num == fBankStep.getnBands()) return;
		
		// otherwise, set the number of bands to use
		fBankStep.setnBands(num);
	}
	
	/// Sets the length of the filter bank (128, 256, ..., 4096)
	/// using a fixed window type of Hann window
	public void SetFilterBankLength(int len)
	{
		fBankStep.setFilterWindow(new HannWindow(len));
	}
	
	/// Sets the signal window size for how small to chop up the input signal (10 ... 1000)
	public void SetSignalWindowSize(int winSize)
	{
		//fBankStep.setSignalWindowSize(winSize);
	}
		
}
