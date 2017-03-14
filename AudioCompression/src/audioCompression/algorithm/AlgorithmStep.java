package audioCompression.algorithm;

import audioCompression.types.AudioCompressionType;

public interface AlgorithmStep<InputType, OutputType> {

	public OutputType forward(InputType input);
	
	public InputType reverse(OutputType input);
	
	public String getName();
	
	public Class<? extends AudioCompressionType> getInputClass();
	
	public Class<? extends AudioCompressionType> getOutputClass();
	
}
