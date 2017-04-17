package audioCompression.algorithm;

import org.junit.runners.Suite;
import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	BitstreamFormatterStepTest.class,
	CompressionPipelineTest.class,
	HuffmanEncoderStepTest.class,
	MdctStepTest.class
	})


public class AlgorithmTestSuite {}
