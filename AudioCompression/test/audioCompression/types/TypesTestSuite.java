package audioCompression.types;

import org.junit.runners.Suite;
import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	CompressedAudioTest.class,
	EncodedLinesTest.class,
	LinesTest.class,
	RawAudioTest.class,
	SubbandsTest.class
	})


public class TypesTestSuite {}
