package audioCompression.compressors;

import audioCompression.ByteRestrictionAcousticModel;
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
	private TimedCompressionPipeline pipeline = new TimedCompressionPipeline();
	
	// Internal flag for knowing if the MDCT step is currently active
	private boolean m_bMDCTEnabled;
	private boolean m_bAdaptiveByteBuffEnabled;
	private boolean m_bAdaptiveHuffmanEnabled;
	private float model_f1 = 0;
	private float model_f2 = 0;
	
	// Create the InputOutput step for the front end of the pipeline
	private InputOutputStep io = new InputOutputStep();
	
	// Create and keep a pointer to the filterbank, for easier configuration
	private FilterBankStep fBankStep = new FilterBankStep();
	
	private MdctStep m_mdctStep = new MdctStep();
	private LinesByteBufferizerStep lineBB = new LinesByteBufferizerStep();
	
	/// Create the Sub-band byte bufferizers
	private SubbandsByteBufferizerStep subBand_BB = new SubbandsByteBufferizerStep();
	
	/// Create the huffman encoder
	private HuffmanEncoderStep huffman = new HuffmanEncoderStep();
	
	// Create the serialization step
	private SerializationStep serialStep = new SerializationStep();
	
	public FullAudioCompressor()
	{
		m_bMDCTEnabled = false;
		m_bAdaptiveByteBuffEnabled = false;
		m_bAdaptiveHuffmanEnabled = false;
		
		pipeline.addStep(io);
		pipeline.addStep(fBankStep);
		pipeline.addStep(subBand_BB);
		pipeline.addStep(huffman);
		pipeline.addStep(serialStep);		
		
	}
	
	@Override // compress the given input file
	public CompressedAudioFile compress(AudioCompressionType input, String compressedName) {
		return (CompressedAudioFile) pipeline.processForward(input, compressedName);
	}

	@Override // decompress the given input file
	public AudioFile decompress(CompressedAudioFile compressedInput, String decompressName) {
		return (AudioFile) pipeline.processReverse(compressedInput, decompressName);
	}
	
	public void EnableDebugging(boolean enable) {
		pipeline.SetDebuggingEnable(enable);
	}

	// Method for toggling the adaptive encoding of the Huffman Step
	//  if enable is false, single pass encoding will be used
	public void EnableHuffmanAdaptiveEncoding(boolean enable)
	{
		// do stuff to the Huffman encoder to enable the adaptive encoding
		huffman.setAdaptive(enable);
		m_bAdaptiveHuffmanEnabled = enable;
	}
	
	// Method for enabling / disabling the MDCT step
	public void EnableMDCTStep(boolean enable)
	{
		if (m_bMDCTEnabled == enable) return;
		// otherwise, we need to do stuff
		
		m_bMDCTEnabled = enable;
		if (enable) {
			// we need to add the MDCT
			pipeline.removeStep(2); // remove the sub-band byte bufferizer
			pipeline.addStep(m_mdctStep, 2);
			pipeline.addStep(lineBB, 3);		
		} else {
			// we need to remove the MDCT and Line byte bufferizers, and add the band byte bufferizers
			pipeline.removeStep(2);
			pipeline.removeStep(2);
			pipeline.addStep(subBand_BB, 2);
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
		
		// regardless of which is active, this will toggle both, we don't really care which is 
		//  currently in the pipeline
		lineBB.setAdaptive(enable);
		subBand_BB.setAdaptive(enable);	
		m_bAdaptiveByteBuffEnabled = enable;
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
		io.setWindowLength(winSize);
	}
	
	public void SetPsychoAcousticModelFrequencies(float sampRate, float f1, float f2){
		this.model_f1 = f1;
		this.model_f2 = f2;
		if(f1==0 && f2==0)
			subBand_BB.setPsychoAcousticModel(null);
		else	
			subBand_BB.setPsychoAcousticModel(new ByteRestrictionAcousticModel(sampRate, f1, f2));
	}
	
	public void AddMetricTitles(StringBuilder sb)
	{
		sb.append("Window Length,");
		sb.append("Window Overlap,");
		sb.append("RMS Error,");
		sb.append("Input Size,");
		sb.append("Compress File Size,");
		sb.append("Compression Ratio,");
		
		sb.append("FilterBank Num SubBands ,");
		sb.append("FilterBank Length,");
		sb.append("Adaptive Byte Buff,");
		sb.append("Adaptive Huffman,");
		sb.append("Model f1,");
		sb.append("Model f2,");
	
		
		// add other metric "titles" here (always trail with a comma)
		
		/// This will add the timer titles from the pipeline
		pipeline.GetMetricTitles(sb);		
		
	}
	
	public void AddMetrics(StringBuilder sb)
	{
		sb.append(String.valueOf(io.getWindowLength()));
		sb.append(",");
		sb.append(String.valueOf(io.getWindowOverlap()));
		sb.append(",");
		sb.append(String.valueOf(io.getRmsError()));
		sb.append(",");
		sb.append(String.valueOf(io.getInputSize()));
		sb.append(",");
		sb.append(String.valueOf(serialStep.getCompressFileSize()));
		sb.append(",");
		sb.append(String.valueOf(serialStep.getCompressFileSize()/(float)io.getInputSize()));
		sb.append(",");
		
		sb.append(String.valueOf(fBankStep.getnBands()));
		sb.append(",");
		sb.append(String.valueOf(fBankStep.getFilterWindowLength()));
		sb.append(",");
		sb.append(String.valueOf(m_bAdaptiveByteBuffEnabled));
		sb.append(",");
		sb.append(String.valueOf(m_bAdaptiveHuffmanEnabled));
		sb.append(",");
		sb.append(String.valueOf(model_f1));
		sb.append(",");
		sb.append(String.valueOf(model_f2));
		sb.append(",");
		
		
		// add other metric values here (always trail with a comma)
		
		/// This will add the timers from the pipeline
		pipeline.GetMetrics(sb);
		
	}
	
}
