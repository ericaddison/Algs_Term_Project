package audioCompression.algorithm;

import edu.mines.jtk.dsp.Fft;
import edu.mines.jtk.mosaic.PlotFrame;
import edu.mines.jtk.mosaic.PlotPanel;
import edu.mines.jtk.util.ArrayMath;

public class FilterBankStepDemo {
	
	public static void main(String[] args){
		FilterBankStep fbs = new FilterBankStep(32);
		
		// fft of filter
		
		System.out.println("Filter length: " + fbs.bpf.getCoefficients1().length);
		
		PlotFrame filterFrame = filterPlot(fbs);
		filterFrame.setVisible(true);
		
		PlotFrame filterFftFrame = filterFftPlot(fbs);
		filterFftFrame.setVisible(true);
		
	}
	
	
	public static PlotFrame filterPlot(FilterBankStep fbs){
		PlotFrame f = new PlotFrame(new PlotPanel(1,1));
		f.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
		f.setSize(400, 300);
		
		float[] filter = fbs.bpf.getCoefficients1();
		f.getPlotPanel().addPoints(filter);
		return f;
	}
	
	
	public static PlotFrame filterFftPlot(FilterBankStep fbs){
		PlotFrame f = new PlotFrame(new PlotPanel(1,1));
		f.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
		f.setSize(400, 300);
		
		float[] filter = fbs.bpf.getCoefficients1();

		Fft fft = new Fft(filter);
		System.out.println(fft.getFrequencySampling1().getFirst() + " - " + fft.getFrequencySampling1().getLast());
		float[] filterFFT = fft.applyForward(filter);
		System.out.println("filterFFT length: " + filterFFT.length);

		
		float[] mag = complexMag(filterFFT);
		f.getPlotPanel().addPoints(ArrayMath.rampfloat(0, 0.5f, mag.length), mag);
		
		return f;
	}
	
	public static float[] complexMag(float[] cpx){
		float[] magArray = new float[cpx.length/2];
		for(int i=0; i<magArray.length; i++)
			magArray[i] = (float)Math.sqrt( cpx[2*i]*cpx[2*i] + cpx[2*i+1]*cpx[2*i+1]);
		return magArray;
			
	}
}
