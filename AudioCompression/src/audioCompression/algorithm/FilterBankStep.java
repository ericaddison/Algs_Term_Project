package audioCompression.algorithm;

import java.util.Arrays;
import java.util.Iterator;

import audioCompression.algorithm.dsp.CosineModulatedFilterBank;
import audioCompression.algorithm.dsp.window.HannWindow;
import audioCompression.algorithm.dsp.window.Window;
import audioCompression.types.AudioCompressionType;
import audioCompression.types.RawAudio;
import audioCompression.types.Subbands;

public class FilterBankStep implements AlgorithmStep<RawAudio, Subbands> {

	private static final String NAME = "Multirate Filterbank";
	private int nBands = 32;
	private Window w;
	private CosineModulatedFilterBank filterBank;
	
	
	public FilterBankStep() {
		this(32, new HannWindow(100));
	}
	 
	public FilterBankStep(int nBands, Window w) {
		this.nBands = nBands;
		this.w = w;
	}
	
	
	@Override
	public Subbands forward(RawAudio input) {
		Iterator<float[][]> iter = input.getWindowIterator();
		int windowCount = 0;
		Subbands subbands = new Subbands(input, nBands);
		if(filterBank==null){
			w.setLength((int)input.getSamplesPerWindow());
			filterBank = new CosineModulatedFilterBank(nBands, w);
		}
		
		while(iter.hasNext()){
			
			float[][] nextWindow = iter.next();

			for(int i=0; i<input.getNChannels(); i++){
				float[][] channelized = filterBank.applyFilters(nextWindow[i]);
				//System.out.println("channeized result: " + channelized.length + ", " + channelized[0].length);
				//float[][] channelized = new float[1][];
				//channelized[0] = nextWindow[i];
				
				for(int j=0; j<nBands; j++)
					subbands.setWindowArray(i, j, windowCount, channelized[j]);
			}
			windowCount++;
		}
		
		System.out.println(Arrays.toString(subbands.getAllWindows()[0][0][0]));
		return subbands;
	}

	@Override
	public RawAudio reverse(Subbands input) {
		/*
		Iterator<float[][][]> iter = input.getWindowIterator();
		int windowCount = 0;
		WavAudio out = new WavAudio(samplesPerWindow, windowOverlap);
		
		if(filterBank==null){
			w.setLength(input.getSamplesPerWindow());
			filterBank = new CosineModulatedFilterBank(nBands, w);
		}
		
		while(iter.hasNext()){
			
			float[][][] nextWindow = iter.next();
			
			for(int i=0; i<input.getNChannels(); i++){
				float[][] channelized = filterBank.applyFilters(nextWindow[i]);
				for(int j=0; j<nBands; j++)
					subbands.setWindowArray(i, j, windowCount, channelized[j]);
			}
			
		}
		return subbands;
		*/
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

}
