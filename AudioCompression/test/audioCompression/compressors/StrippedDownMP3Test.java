package audioCompression.compressors;

import static org.junit.Assert.*;

import org.junit.Test;

public class StrippedDownMP3Test {

	@Test
	public void instantiate() {
		StrippedDownMP3 compressor = new StrippedDownMP3();
		assertNotNull(compressor);
	}

}
