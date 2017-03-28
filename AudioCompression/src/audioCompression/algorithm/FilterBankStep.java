package audioCompression.algorithm;

import java.util.Arrays;
import java.util.Iterator;

import com.sun.org.apache.xerces.internal.impl.dv.xs.DecimalDV;

import edu.mines.jtk.dsp.BandPassFilter;
import edu.mines.jtk.dsp.Conv;

import audioCompression.types.AudioCompressionType;
import audioCompression.types.RawAudio;
import audioCompression.types.Subbands;

public class FilterBankStep implements AlgorithmStep<RawAudio, Subbands> {

	private static final String NAME = "Polyphase Filterbank";
	private int nBands = 32;
	BandPassFilter bpf;
	float[] filterCoeffs;
	float[][] polyphaseFilters;
	
	
	public FilterBankStep() {
		this(32);
	}
	 
	public FilterBankStep(int nBands) {
		this.nBands = nBands;
		bpf = new BandPassFilter(0, 0.5/nBands, 0.005, 0.01);
		padFilter();
		polyphaseFilters = downsample(filterCoeffs);
	}
	
	
	@Override
	public Subbands forward(RawAudio input) {
		Iterator<float[][]> iter = input.getWindowIterator();
		
		while(iter.hasNext()){
			
			float[][] nextWindow = iter.next();
			
			for(int i=0; i<nextWindow.length; i++){
			// step 1: decimate
				float[][] decimated = downsample(nextWindow[i]);
				
			// step 2: filter
				for(int j=0; j<nBands; j++){
					float[] out = new float[decimated[j].length];
					Conv.conv(decimated[j].length, 
								0, 
								decimated[j], 
								polyphaseFilters[j].length,
								0, 
								polyphaseFilters[j], 
								decimated[j].length,
								0,
								out);
					System.out.println(Arrays.toString(out));
				}
			// step 3: DFT
			
			}
		}
		return null;
	}

	@Override
	public RawAudio reverse(Subbands input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Class<? extends AudioCompressionType> getInputClass() {
		return RawAudio.class;
	}

	@Override
	public Class<? extends AudioCompressionType> getOutputClass() {
		return Subbands.class;
	}

	public int getnBands() {
		return nBands;
	}

	public void setnBands(int nBands) {
		this.nBands = nBands;
	}
	
	
	public float[][] downsample(float[] inArray){
		if(inArray.length%nBands!=0)
			throw new IllegalArgumentException("FilterBankStep.downsample: inArray.length%nBands!=0");
		
		int M = nBands;
		
		float[][] outArray = new float[M][inArray.length/M];
		
		for(int i=0; i<M; i++)
			for(int j=0; j<inArray.length/M; j++)
				outArray[i][j] = inArray[i + j*M];
		
		return outArray;
	}

	
	public float[] padSymmetric(float[] arr, int padAmount) {
		float[] padded = new float[arr.length + padAmount];
		for(int i=0; i<arr.length; i++)
			padded[i+padAmount/2] = arr[i];
		return padded;
	}
	
	
	private void padFilter() {
		bpf.getCoefficients1();
		int padAmount = 0;
		if(filterCoeffs.length%nBands!=0)
			padAmount = nBands - filterCoeffs.length%nBands;
		filterCoeffs = padSymmetric(bpf.getCoefficients1(), padAmount);
	}

}
