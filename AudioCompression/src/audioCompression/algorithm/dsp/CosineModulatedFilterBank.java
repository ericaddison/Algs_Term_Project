package audioCompression.algorithm.dsp;

import edu.mines.jtk.mosaic.PlotFrame;
import audioCompression.algorithm.dsp.window.BlackmannWindow;
import audioCompression.algorithm.dsp.window.HannWindow;
import audioCompression.algorithm.dsp.window.TwoSidedFftDbPlot;
import audioCompression.algorithm.dsp.window.Window;

public class CosineModulatedFilterBank {

	private float[][] filters;
	int nBands;
	private LowPassFilter prototypeFilter;
	
	public CosineModulatedFilterBank(int nBands, Window w) {
		if(w.getLength()%nBands != 0)
			w.setLength(w.getLength() + (nBands - w.getLength()%nBands));
		this.prototypeFilter = new LowPassFilter(0.5f/(2*nBands), w);
		this.nBands = nBands;
		filters = makeFilters();
	}
	
	private float[][] makeFilters(){
		float[][] h = new float[nBands][];
		float[] h0 = prototypeFilter.getCoefficients();
		int M = h0.length;	// length of prototype filter
		int N = nBands;		// number of subbands
		int L = M/N;		// length of polyphase components
		
		for(int k=0; k<N; k++){
			h[k] = new float[M];
			double phi_k = (k+0.5)*(L+1)*Math.PI/2.0;
			for(int n=0; n<M; n++){
				double phi = (k+0.5)*(n-(M-1)/2.0)*Math.PI/N;
				h[k][n] = (float)(h0[n]*Math.cos(phi+phi_k));
			}
		}
		
		return h;
	}
	
	
	public static void main(String[] args){
		
		int L = 100;
		int N = 4;
		int M = L*N;
		
		CosineModulatedFilterBank filterBank = new CosineModulatedFilterBank(N, new HannWindow(M));
		
		TwoSidedFftDbPlot plot = new TwoSidedFftDbPlot(M, filterBank.filters);
		
		plot.setSize(500, 400);
		plot.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
		plot.setVisible(true);
		
	}
	
}
