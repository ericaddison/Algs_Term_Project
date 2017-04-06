package audioCompression.algorithm.dsp;

import java.util.Arrays;

import audioCompression.algorithm.dsp.window.HammingWindow;
import audioCompression.algorithm.dsp.window.HannWindow;
import audioCompression.algorithm.dsp.window.KaiserWindow;
import audioCompression.algorithm.dsp.window.KbdWindow;
import audioCompression.algorithm.dsp.window.TwoSidedFftDbPlot;
import audioCompression.types.Subbands;
import audioCompression.types.testImpls.RawAudioImpl;
import edu.mines.jtk.mosaic.PlotFrame;

public class FilterBankDemo {

	public static void main(String[] args){
		
		int N = 4;
		int M = 512;
		
		CosineModulatedFilterBank filterBank = new CosineModulatedFilterBank(N, new HannWindow(M));
		//CosineModulatedFilterBank filterBank = new CosineModulatedFilterBank(N, new HammingWindow(M));
		//CosineModulatedFilterBank filterBank = new CosineModulatedFilterBank(N, new KaiserWindow(M,35.0f/M));
		//CosineModulatedFilterBank filterBank = new CosineModulatedFilterBank(N, new KbdWindow(M,10.0f/M));
		
		float[][] filterCoeffs = new float[N][];
		
		for(int i=0; i<N; i++)
			filterCoeffs[i] = filterBank.getFilters()[i].getCoefficients();
		
		TwoSidedFftDbPlot plot = new TwoSidedFftDbPlot(M, filterCoeffs);
		plot.setSize(500, 400);
		plot.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
		plot.setVisible(true);
		
		
		
		
	}
	
}
