package audioCompression.blocks;

public interface AlgorithmStep<InputType, OutputType> {

	public OutputType forward(InputType input);
	
	public InputType reverse(OutputType input);
	
	public String getName();
	
}
