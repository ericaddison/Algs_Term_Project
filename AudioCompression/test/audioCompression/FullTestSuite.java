package audioCompression;

import org.junit.runners.Suite;
import audioCompression.algorithm.AlgorithmTestSuite;
import audioCompression.compressors.CompressorsTestSuite;
import audioCompression.types.TypesTestSuite;

import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	AlgorithmTestSuite.class,
	CompressorsTestSuite.class,
	TypesTestSuite.class
	})

public class FullTestSuite {}
