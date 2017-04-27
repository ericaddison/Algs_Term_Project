package audioCompression.algorithm;

import java.util.Iterator;
import java.util.LinkedList;

import audioCompression.types.AudioCompressionType;

/**
 * A container for algorithmic steps used to perform audio compression. 
 * Steps can be added to a pipeline object, but a check is made to ensure the
 * output type of the last step matches the input type of the new step.
 * Processing the pipeline can be run in forward or reverse.
 * 
 * @author Eric Addison, Taylor Autry, Josh Musick
 *
 */
@SuppressWarnings("rawtypes")
public class CompressionPipeline {

	protected LinkedList<AlgorithmStep> pipeline = new LinkedList<>();
	
	
	/**
	 * Add a new algorithm step to the processing pipeline.
	 * @param newStep the new step to add
	 * @return result of add operation
	 * @throws IllegalArgumentException thrown for input/output mismatch
	 */
	public boolean addStep(AlgorithmStep newStep){
		if(checkStep(newStep))
			return pipeline.add(newStep);
		else
			throw new IllegalArgumentException("CompressionPipeline: Attempted"
					+ " to add step with Input/Output type mismatch");
	}
	
	public void addStep(AlgorithmStep step, int index)
	{
		pipeline.add(index, step);
	}
	
	public void removeStep(AlgorithmStep step)
	{
		pipeline.remove(step);
	}
	
	public void removeStep(int index)
	{
		pipeline.remove(index);
	}
	
	public AlgorithmStep getStep(int index)
	{
		if ((index < 0) || (index >= pipeline.size())) {
			throw new IllegalArgumentException("Invalid step index given!");
		}
		return pipeline.get(index);
	
	}
	
	// These enums describe the pipeline stage for triggering the pipeline callback
	public enum PipelineStageType {
		STAGE_COMPRESSION_BEGINNING,
		STAGE_FORWARD_STEP,
		STAGE_DECOMPRESSION_BEGINNING,
		STAGE_REVERSE_STEP,
	};
	
	/**
	 * This function handles the intermediate steps of the compress and decompress pipeline,
	 * this provides a handle to capture step specific metrics, ie time etc.
	 * @param stage		Enum of which part of the cycle is happening, start, middle, end
	 * @param index		Index of the step which just completed
	 * @param stageName	Name of the stage which is about to begin or just finished
	 */
	protected void OnPipelineEvent(PipelineStageType stage, int index, String stageName)
	{
		String msg = "- Pipeline Event ";
		if (stage == PipelineStageType.STAGE_COMPRESSION_BEGINNING) {
			msg += stageName;
		} else if (stage == PipelineStageType.STAGE_FORWARD_STEP) {
			msg += "Forward - " + stageName + " has completed -"; 
		} else if (stage == PipelineStageType.STAGE_DECOMPRESSION_BEGINNING) {
			msg += stageName;
		} else if (stage == PipelineStageType.STAGE_REVERSE_STEP) {
			msg += "Reverse - " + stageName + " has completed -"; 
		}
		msg += "\n";
		System.out.printf(msg);
	}
	
	
	/**
	 * Process an input dataset through the pipeline in the 
	 * forward (compression) direction
	 * @param input the input data
	 * @return the forward processed data
	 */
	@SuppressWarnings("unchecked")
	public AudioCompressionType processForward(AudioCompressionType input, String name){
		if( !pipeline.getFirst().getInputClass().isInstance(input) )
			throw new IllegalArgumentException("CompressionPipeline: Invalid "
					+ "input type to processForward() -- "
					+ "expected " + pipeline.getFirst().getInputClass() 
					+ ", got " + input.getClass());
		
		int stepIndex = 0;
		// Trigger alert event that pipeline has begun
		OnPipelineEvent(PipelineStageType.STAGE_COMPRESSION_BEGINNING, stepIndex, "- Compress Starting -");
				
		Iterator<AlgorithmStep> iter = pipeline.iterator();
		AudioCompressionType workingData = input;
		
		while(iter.hasNext()){
			AlgorithmStep nextStep = iter.next();
			workingData = nextStep.forward(workingData, name);
			OnPipelineEvent(PipelineStageType.STAGE_FORWARD_STEP, stepIndex, nextStep.getName());
			stepIndex++;
		}
		return workingData;
	}
	

	/**
	 * Process an input dataset through the pipeline in the reverse 
	 * (decompression) direction
	 * @param input the input data
	 * @return the reverse processed data
	 */
	@SuppressWarnings("unchecked")
	public AudioCompressionType processReverse(AudioCompressionType input, String name){
		if( !pipeline.getLast().getOutputClass().isInstance(input) )
			throw new IllegalArgumentException("CompressionPipeline: Invalid "
					+ "input type to processReverse() -- "
					+ "expected " + pipeline.getLast().getOutputClass() 
					+ ", got " + input.getClass());
		
		int nextIndex = pipeline.size() - 1;
		
		// Trigger alert event that pipeline has begun
		OnPipelineEvent(PipelineStageType.STAGE_DECOMPRESSION_BEGINNING, nextIndex, "- Decompress Starting -");
				
		Iterator<AlgorithmStep> iter = pipeline.descendingIterator();
		AudioCompressionType workingData = input;
		while(iter.hasNext()){
			AlgorithmStep nextStep = iter.next();
			workingData = nextStep.reverse(workingData, name);
			
			OnPipelineEvent(PipelineStageType.STAGE_REVERSE_STEP, nextIndex, nextStep.getName());
			nextIndex--;
		}
			
		return workingData;
	}
	
	
	/**
	 * Get the current number of steps in the pipeline
	 * @return # of steps
	 */
	public int getNumberOfSteps(){
		return pipeline.size();
	}
	
	public boolean isPipelineValid()
	{
		// go through all steps and check for the correct output to input between each step
		Iterator<AlgorithmStep> iter = pipeline.iterator();
		AlgorithmStep prevStep = pipeline.getFirst();
		iter.next();
		while(iter.hasNext()) {
			AlgorithmStep nextStep = iter.next(); 
			
			if (prevStep.getOutputClass() != nextStep.getInputClass()) {
				return false;
			}
			prevStep = nextStep;
		}	
		
		return true;
	}
	
	// Ensure a new step is valid to add to the end of the pipeline
	private boolean checkStep(AlgorithmStep newStep){
		return pipeline.size()==0 
			|| pipeline.getLast().getOutputClass()==newStep.getInputClass();
	}
	
	
	
}
