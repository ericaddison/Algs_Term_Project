package audioCompression.algorithm.dsp;

import audioCompression.algorithm.dsp.window.KaiserWindow;
import audioCompression.algorithm.dsp.window.TwoSidedFftDbPlot;
import edu.mines.jtk.mosaic.PlotFrame;

public class FilterBankDemo {

	public static void main(String[] args){
		
		int L = 50;
		int N = 8;
		int M = L*N;
		
		//CosineModulatedFilterBank filterBank = new CosineModulatedFilterBank(N, new HannWindow(M));
		//CosineModulatedFilterBank filterBank = new CosineModulatedFilterBank(N, new HammingWindow(M));
		CosineModulatedFilterBank filterBank = new CosineModulatedFilterBank(N, new KaiserWindow(M,35.0f/M));
		
		TwoSidedFftDbPlot plot = new TwoSidedFftDbPlot(M, filterBank.getFilters());
		
		plot.setSize(500, 400);
		plot.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
		plot.setVisible(true);
		
	}
	
}
