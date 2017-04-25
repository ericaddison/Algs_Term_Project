package audioCompression.algorithm;

import audioCompression.algorithm.CompressionPipeline;

public class TimedCompressionPipeline extends CompressionPipeline {

	/**
	 * This function handles the intermediate steps of the compress and decompress pipeline,
	 * this provides a handle to capture step specific metrics, ie time etc.
	 * @param stage		Enum of which part of the cycle is happening, start, middle, end
	 * @param nextIndex	Index of the next step that will be ran, invalid for end stage type (there is no next step)
	 * @param prevStage	Name of previous stage performed
	 * @param nextStage	Name of next stage to be performed
	 */
	@Override
	protected void OnPipelineEvent(PipelineStage stage, int nextIndex, String prevStage, String nextStage)
	{
		long curTime = System.nanoTime();
		String msg = "- Pipeline Event ";
		if (stage == PipelineStage.STAGE_FORWARD_BEGIN) {
			compressStartTime = curTime;
			compressionIntervalTimes = new long[pipeline.size()];
			msg += "Forward Begin with " + nextStage + " - ";
		} else if (stage == PipelineStage.STAGE_FORWARD_INTERMEDIATE) {
			compressionIntervalTimes[nextIndex] = curTime;
			msg += "Forward Between " + prevStage + " and " + nextStage + " -"; 
		} else if (stage == PipelineStage.STAGE_FORWARD_END) {
			compressionIntervalTimes[decompressIntervalTimes.length - 1] = curTime;
			msg += "Forward Ending -";
		} else if (stage == PipelineStage.STAGE_REVERSE_BEGIN) {
			decompressStartTime = curTime;
			decompressIntervalTimes = new long[pipeline.size()];
			msg += "Reverse Begin with " + nextStage + " - ";
		} else if (stage == PipelineStage.STAGE_REVERSE_INTERMEDIATE) {
			decompressIntervalTimes[nextIndex] = curTime;
			msg += "Reverse Between " + prevStage + " and " + nextStage + " -"; 
		} else if (stage == PipelineStage.STAGE_REVERSE_END) {
			decompressIntervalTimes[decompressIntervalTimes.length - 1] = curTime;
			msg += "Reverse Ending -";
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
	
	public void GetMetricTitles(StringBuilder sb, boolean mdctEnabled) {
		
	}
	
	public void GetMetricss(StringBuilder sb, boolean mdctEnabled) {
		
	}
}
