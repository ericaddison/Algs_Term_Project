package audioCompression.algorithm;

import audioCompression.algorithm.dsp.window.HannWindow;
import audioCompression.types.Subbands;
import audioCompression.types.testImpls.RawAudioImpl;

public class FilterBankStepDemo {
	
	public static void main(String[] args){
		
		int nbands = 16;
		
		// apply to audio test
		RawAudioImpl audio = new RawAudioImpl(10000, 1152, 0);
		FilterBankStep fb = new FilterBankStep(nbands, new HannWindow(100));
		
		Subbands sub = fb.forward(audio);
		
		System.out.println(sub.getAllWindows().length);
		System.out.println(sub.getAllWindows()[0].length);
		System.out.println(sub.getAllWindows()[0][0].length);
		System.out.println(sub.getAllWindows()[0][0][0].length);
		
	}
	
	
}
