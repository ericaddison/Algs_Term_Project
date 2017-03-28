package audioCompression.algorithm;

import static org.junit.Assert.*;

import org.junit.Test;

public class FilterBankStepTest {

	@Test
	public void downsampleTest_2bands() {
		int nBands = 2;
		FilterBankStep fb = new FilterBankStep(nBands);
		float[] in = new float[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		float[][] downsamples = fb.downsample(in);
		assertEquals(nBands, downsamples.length);
		for(int i=0; i<in.length/nBands; i++){
			for(int j=0; j<nBands; j++)
				assertEquals(in[j+i*nBands], downsamples[j][i],0.001f);
		}
	}

	@Test
	public void downsampleTest_5bands() {
		int nBands = 5;
		FilterBankStep fb = new FilterBankStep(nBands);
		float[] in = new float[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		float[][] downsamples = fb.downsample(in);
		assertEquals(nBands, downsamples.length);
		for(int i=0; i<in.length/nBands; i++){
			for(int j=0; j<nBands; j++)
				assertEquals(in[j+i*nBands], downsamples[j][i],0.001f);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void downsampleTest_3bands() {
		int nBands = 3;
		FilterBankStep fb = new FilterBankStep(nBands);
		float[] in = new float[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		fb.downsample(in);
	}
	

}
