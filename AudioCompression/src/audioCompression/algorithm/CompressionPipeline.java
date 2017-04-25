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

	private LinkedList<AlgorithmStep> pipeline = new LinkedList<>();
	
	
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
	enum PipelineStage {
		STAGE_FORWARD_BEGIN,
		STAGE_FORWARD_INTERMEDIATE,
		STAGE_FORWARD_END,
		STAGE_REVERSE_BEGIN,
		STAGE_REVERSE_INTERMEDIATE,
		STAGE_REVERSE_END
	};
	
	private void OnPipelineEvent(PipelineStage stage, String prevStage, String nextStage) {}
	
	
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
		
		OnPipelineEvent(STAGE_FORWARD_BEGIN, "", pipeline.getFirst().getName());
		
		Iterator<AlgorithmStep> iter = pipeline.iterator();
		AudioCompressionType workingData = input;
		while(iter.hasNext()){
			AlgorithmStep nextStep = iter.next(); 
			workingData = nextStep.forward(workingData, name);
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
		
		Iterator<AlgorithmStep> iter = pipeline.descendingIterator();
		AudioCompressionType workingData = input;
		while(iter.hasNext()){
			AlgorithmStep nextStep = iter.next(); 
			workingData = nextStep.reverse(workingData, name);
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
