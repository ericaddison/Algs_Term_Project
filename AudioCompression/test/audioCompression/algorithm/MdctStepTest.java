package audioCompression.algorithm;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import audioCompression.types.Lines;
import audioCompression.types.Subbands;
import audioCompression.types.testImpls.SubbandsImpl;

public class MdctStepTest {

	@Test
	public void testReverse() {
		Subbands sub = new SubbandsImpl(1024);
		MdctStep step = new MdctStep();
		
		Lines lines = step.forward(sub, "");
		
		Subbands sub2 = step.reverse(lines, "");
		
		
		for(int ichan=0; ichan<sub.getNChannels(); ichan++)
			for(int jband=0; jband<sub.getNBands(); jband++)
				for(int kwin=0; kwin<sub.getNWindows(); kwin++)
					for(int n=0; n<sub.getSamplesPerWindow(); n++)
						assertEquals(sub.getAllWindows()[ichan][jband][kwin][n], sub2.getAllWindows()[ichan][jband][kwin][n], 0.1f);
		
	}

}
