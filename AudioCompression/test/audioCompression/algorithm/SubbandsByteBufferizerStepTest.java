package audioCompression.algorithm;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import audioCompression.types.AudioByteBuffer;
import audioCompression.types.Subbands;

public class SubbandsByteBufferizerStepTest {

	
	@Test
	public void testInstantiate() {
		SubbandsByteBufferizerStep sbb = new SubbandsByteBufferizerStep();
		assertNotNull(sbb);
	}
	

	@Test
	public void testRoundTrip(){
		int sampleRate = 15;
		int nWindows = 14;
		int samplesPerWindow = 10;
		int windowOverlap = 2;
		int nChannels = 2;
		int nBands = 16;
		int byteDepth = 2;

		float[][][][] windows = new float[nChannels][nBands][nWindows][samplesPerWindow];
		Subbands sub = new Subbands(sampleRate, nWindows, 
				samplesPerWindow, windowOverlap, 
				nChannels, nBands, byteDepth, windows);
		
		SubbandsByteBufferizerStep sbb = new SubbandsByteBufferizerStep();
		
		AudioByteBuffer abb = sbb.forward(sub);
		
		Subbands sub2 = sbb.reverse(abb);
		
		assertEquals(sub.getNBands(), sub2.getNBands());
		assertEquals(sub.getNChannels(), sub2.getNChannels());
		assertEquals(sub.getNWindows(), sub2.getNWindows());
		assertEquals(sub.getSampleRate(), sub2.getSampleRate());
		assertEquals(sub.getSamplesPerWindow(), sub2.getSamplesPerWindow());
		assertEquals(sub.getByteDepth(), sub2.getByteDepth());
	}
	
}
