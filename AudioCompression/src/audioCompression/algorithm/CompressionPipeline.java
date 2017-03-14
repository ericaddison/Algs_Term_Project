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
		if(checkStep(pipeline.getLast(), newStep))
			return pipeline.add(newStep);
		else
			throw new IllegalArgumentException("CompressionPipeline: Attempted"
					+ " to add step with Input/Output type mismatch");
	}
	
	
	/**
	 * Process an input dataset through the pipeline in the 
	 * forward (compression) direction
	 * @param input the input data
	 * @return the forward processed data
	 */
	public AudioCompressionType processForward(AudioCompressionType input){
		if(input.getClass() != pipeline.getFirst().getInputClass())
			throw new IllegalArgumentException("CompressionPipeline: Invalid "
					+ "input type to processForward() -- "
					+ "expected " + pipeline.getFirst().getInputClass() 
					+ ", got " + input.getClass());
		
		Iterator<AlgorithmStep> iter = pipeline.iterator();
		return iterate(iter, input);
	}
	

	/**
	 * Process an input dataset through the pipeline in the reverse 
	 * (decompression) direction
	 * @param input the input data
	 * @return the reverse processed data
	 */
	public AudioCompressionType processReverse(AudioCompressionType input){
		if(input.getClass() != pipeline.getLast().getOutputClass())
			throw new IllegalArgumentException("CompressionPipeline: Invalid "
					+ "input type to processReverse() -- "
					+ "expected " + pipeline.getLast().getOutputClass() 
					+ ", got " + input.getClass());
		
		Iterator<AlgorithmStep> iter = pipeline.descendingIterator();
		return iterate(iter, input);
	}
	
	
	// iterate through the pipeline operations using the provided iterator
	@SuppressWarnings("unchecked")
	private AudioCompressionType iterate(Iterator<AlgorithmStep> iter, 
			AudioCompressionType input){
		AudioCompressionType workingData = input;
		while(iter.hasNext()){
			AlgorithmStep nextStep = iter.next(); 
			workingData = nextStep.reverse(workingData);
		}
		
		return workingData;
	}
	
	
	// Ensure a new step is valid to add to the end of the pipeline
	private boolean checkStep(AlgorithmStep lastStep, AlgorithmStep newStep){
		return pipeline.size()==0 
				|| lastStep.getOutputClass()==newStep.getInputClass();
	}
	
}
