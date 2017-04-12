package audioCompression.algorithm;

import java.util.Iterator;

import audioCompression.algorithm.dsp.CosineModulatedFilterBank;
import audioCompression.algorithm.dsp.window.HannWindow;
import audioCompression.algorithm.dsp.window.Window;
import audioCompression.types.AudioCompressionType;
import audioCompression.types.RawAudio;
import audioCompression.types.Subbands;
import audioCompression.types.WavAudioOutput;

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
		if(input.getNChannels()>1)
			throw new IllegalArgumentException("Error! Implementation not functional for >1 audio channels!");
		
		Iterator<float[][]> iter = input.getWindowIterator();
		int windowCount = 0;
		Subbands subbands = new Subbands(input, nBands);
		if(filterBank==null)
			filterBank = new CosineModulatedFilterBank(nBands, w);
		
		while(iter.hasNext()){
			
			if(windowCount == input.getNWindows()){
				System.out.println("oh no!");
				break;
			}
			
			float[][] nextWindow = iter.next();

			for(int i=0; i<input.getNChannels(); i++){
				float[][] channelized = filterBank.analysisDecimated(nextWindow[i]);
				//float[][] channelized = filterBank.analysis(nextWindow[i]);

				for(int j=0; j<nBands; j++)
					subbands.putWindow(i, j, windowCount, channelized[j]);
				
			}
			windowCount++;
		}
		
		return subbands;
	}

	@Override
	public RawAudio reverse(Subbands input) {
		if(input.getNChannels()>1)
			throw new IllegalArgumentException("Error! Implementation not functional for >1 audio channels!");
		
		Iterator<float[][][]> iter = input.getWindowIterator();
		int windowCount = 0;
		
		if(filterBank==null)
			filterBank = new CosineModulatedFilterBank(nBands, w);
		
		float[][][] windows = new float[input.getNChannels()][input.getNWindows()][input.getSamplesPerWindow()];
		
		while(iter.hasNext()){
			
			float[][][] nextWindow = iter.next();
			
			for(int i=0; i<input.getNChannels(); i++){
				//windows[i][windowCount] = filterBank.synthesis(nextWindow[i]);
				windows[i][windowCount] = filterBank.synthesisDecimated(nextWindow[i]);
			} 
			
			windowCount++;
		}
		WavAudioOutput out = 
				new WavAudioOutput(windows, input.getWindowOverlap(), 
						input.getSampleRate(), input.getByteDepth());
		return out;
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
