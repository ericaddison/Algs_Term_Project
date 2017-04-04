package audioCompression.algorithm.dsp;

import audioCompression.algorithm.dsp.window.HammingWindow;
import audioCompression.algorithm.dsp.window.HannWindow;
import audioCompression.algorithm.dsp.window.KaiserWindow;
import audioCompression.algorithm.dsp.window.KbdWindow;
import audioCompression.algorithm.dsp.window.TwoSidedFftDbPlot;
import edu.mines.jtk.mosaic.PlotFrame;

public class FilterBankDemo {

	public static void main(String[] args){
		
		int L = 50;
		int N = 8;
		int M = L*N;
		
		//CosineModulatedFilterBank filterBank = new CosineModulatedFilterBank(N, new HannWindow(M));
		CosineModulatedFilterBank filterBank = new CosineModulatedFilterBank(N, new HammingWindow(M));
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
