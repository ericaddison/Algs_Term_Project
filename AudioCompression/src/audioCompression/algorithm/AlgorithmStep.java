package audioCompression.algorithm;

import audioCompression.types.AudioCompressionType;

/**
 * This class represents a single algorithmic step in our Audio Compression
 * pipeline. The step should be able to operate in both forward (compressing)
 * and reverse (decompressing) modes.
 * 
 * @author Eric Addison, Taylor Autry, Josh Musick
 *
 * @param <InputType>
 *            The class this step expects as an input
 * @param <OutputType>
 *            The class this step provides as an output
 */
public interface AlgorithmStep<InputType extends AudioCompressionType, 
					OutputType extends AudioCompressionType> {

	/**
	 * Perform the forward (compression) operation for this algorithm step.
	 * 
	 * @param input
	 *            the input data for the forward operation
	 * @param name
	 *      	  the name of the file to be written, this gets passed through 
	 *      	  each stage mostly for writing out to disk
	 * @return the forward processed data
	 */
	public OutputType forward(InputType input, String name);

	/**
	 * Perform the reverse (decompression) operation for this algorithm step.
	 * 
	 * @param input
	 *            the input data for the reverse operation
	 * @param name
	 *      	  the name of the file to be written, this gets passed through 
	 *      	  each stage mostly for writing out to disk 	
	 * @return the reverse processed data
	 */
	public InputType reverse(OutputType input, String name);

	/**
	 * Get the name of this step
	 * 
	 * @return step name
	 */
	public String getName();

	/**
	 * Get the class this step expects as input
	 * 
	 * @return input class
	 */
	public Class<? extends AudioCompressionType> getInputClass();

	/**
	 * Get the class this step provides as output
	 * 
	 * @return output class
	 */
	public Class<? extends AudioCompressionType> getOutputClass();

}
