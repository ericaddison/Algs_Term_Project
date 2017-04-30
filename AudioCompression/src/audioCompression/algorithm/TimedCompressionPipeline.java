package audioCompression.algorithm;

import audioCompression.algorithm.CompressionPipeline;

public class TimedCompressionPipeline extends CompressionPipeline {

	// This constant tells how many stages at most to expect
	private static final int MAX_NUM_STAGES = 6;
	// this flag indicates if the mdct was present at compress start time (num stages == MAX_NUM_STAGES)
	private boolean m_mdctEnabled = false;
	
	public TimedCompressionPipeline()
	{
		/// these arrays assume a step for io, filter bank, MDCT, byte bufferizer, huffman, and serialization 
		compressionIntervalTimes = new long[MAX_NUM_STAGES];
		decompressIntervalTimes = new long[MAX_NUM_STAGES];
	}
	
	/**
	 * This function handles the intermediate steps of the compress and decompress pipeline,
	 * this provides a handle to capture step specific metrics, ie time etc.
	 * @param stage		Enum of which part of the cycle is happening, start, middle, end
	 * @param nextIndex	Index of the next step that will be ran, invalid for end stage type (there is no next step)
	 * @param prevStage	Name of previous stage performed
	 * @param nextStage	Name of next stage to be performed
	 */
	@Override
	protected void OnPipelineEvent(PipelineStageType stage, int index, String stageName)
	{		
		long curTime = System.nanoTime();
		String msg = "- Pipeline Event ";
		if (stage == PipelineStageType.STAGE_COMPRESSION_BEGINNING) {
			m_mdctEnabled = (pipeline.size() == MAX_NUM_STAGES);
			compressStartTime = curTime;
			msg += stageName;
		} else if (stage == PipelineStageType.STAGE_FORWARD_STEP) {
			compressionIntervalTimes[index] = curTime;
			msg += "Forward - " + stageName + " has completed -"; 		
		} else if (stage == PipelineStageType.STAGE_DECOMPRESSION_BEGINNING) {
			decompressStartTime = curTime;
			msg += stageName;
		} else if (stage == PipelineStageType.STAGE_REVERSE_STEP) {
			decompressIntervalTimes[index] = curTime;
			msg += "Reverse - " + stageName + " has completed -"; 
		}
		if (bEnableDebugging) {
			msg += "\n";
			System.out.printf(msg);	
		}		
	}
	
	// these are the persistent compression time values from the pipeline
	protected long compressStartTime;
	protected long[] compressionIntervalTimes;
	protected long decompressStartTime;
	protected long[] decompressIntervalTimes;
	
	protected boolean bEnableDebugging = false;
	
	/// Access method to enable verbose printing of pipeline step progress
	public void SetDebuggingEnable(boolean enable) {
		bEnableDebugging = enable;
	}
	
	public void GetMetricTitles(StringBuilder sb) {
		sb.append("Comp IO Time (ms),");
		sb.append("Decomp IO Time (ms),");
		sb.append("Comp Filter Bank Time (ms),");
		sb.append("Decomp Filter Bank Time (ms),");
		sb.append("Comp MDCT Time (ms),");
		sb.append("Decomp MDCT Time (ms),");
		sb.append("Comp Byte Bufferizer (ms),");
		sb.append("Decomp Byte Bufferizer (ms),");
		sb.append("Comp Huffman Time (ms),");
		sb.append("Decomp Huffman Time (ms),");
		sb.append("Comp Serialize Time (ms),");
		sb.append("Decomp Serialize Time (ms),");
	}
	
	public void GetMetrics(StringBuilder sb) {
		double oneMill = 1000000.0;
		double compT = (double)(compressionIntervalTimes[0] - compressStartTime) / oneMill;
		double decompT = (double)(decompressIntervalTimes[0] - decompressStartTime) / oneMill;
		
		double[] compTimes = new double[MAX_NUM_STAGES];
		double[] decompTimes = new double[MAX_NUM_STAGES];
		
		compTimes[0] = compT;
		decompTimes[0] = decompT;
		
		int num = pipeline.size();
		for (int i = 1; i < num; ++i) {
						
			compT = (double)(compressionIntervalTimes[i] - compressionIntervalTimes[i - 1]) / oneMill;
			decompT = (double)(decompressIntervalTimes[i - 1] - decompressIntervalTimes[i]) / oneMill;
			
			int index = i;
			if (!m_mdctEnabled && i > 1) {
				index++;
			}
			
			compTimes[index] = compT;
			decompTimes[index] = decompT;
		}
		
		for (int i = 0; i < MAX_NUM_STAGES; ++i) {
			sb.append(String.valueOf(compTimes[i]) + ",");
			sb.append(String.valueOf(decompTimes[i]) + ",");
		}
	}
}
