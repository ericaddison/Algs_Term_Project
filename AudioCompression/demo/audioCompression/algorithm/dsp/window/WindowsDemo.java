package audioCompression.algorithm.dsp.window;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import edu.mines.jtk.dsp.Fft;
import edu.mines.jtk.mosaic.PlotFrame;
import edu.mines.jtk.mosaic.PlotPanel;
import edu.mines.jtk.mosaic.PointsView;

public class WindowsDemo {

	private static Color[] colors = new Color[] {Color.BLACK, Color.BLUE, Color.RED, Color.GREEN, Color.CYAN, Color.MAGENTA, Color.YELLOW};
	
	public static void main(String args[]){
		
		List<Window> windows = new ArrayList<>();
		int N = (int)Math.pow(2, 11);
		windows.add(new HammingWindow(N));
		windows.add(new HannWindow(N));
		windows.add(new BlackmannWindow(N));
		windows.add(new KaiserWindow(N, 0.006f));
		
		PlotFrame f1 = new PlotFrame(new PlotPanel(1,1));
		PlotFrame f2 = new PlotFrame(new PlotPanel(1,1));
		for(Window w : windows){
			PointsView pv = f1.getPlotPanel().addPoints(w.getCoefficients());
			pv.setLineColor(colors[windows.indexOf(w)%windows.size()]);
			pv = f2.getPlotPanel()
					.addPoints(
							(new Fft(N)).getFrequencySampling1(),
							fftMagDb(w.getCoefficients()));
			pv.setLineColor(colors[windows.indexOf(w)%windows.size()]);
			
		}
		
		f1.setSize(500, 300);
		f1.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
		f1.setVisible(true);
		f2.setSize(500, 300);
		f2.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
		f2.setVisible(true);		
	}
	
	private static float[] fftMagDb(float[] arr){
		Fft fft = new Fft(arr.length);
		float[] fftMag = complexMag(fft.applyForward(arr));
		fftMag[fftMag.length-1] = fftMag[fftMag.length-2]; 
		
		
		// find max
		float max = -Float.MAX_VALUE;
		for(int i=0; i<fftMag.length; i++)
			max = Math.max(max, Math.abs(fftMag[i]));
		
		// db relative to max
		for(int i=0; i<fftMag.length; i++)
			fftMag[i] = 20*(float)Math.log10(Math.abs(fftMag[i])/max);
		
		return fftMag;
	}
	
	private static float[] complexMag(float[] cpx){
		float[] mag = new float[cpx.length/2];
		
		for(int i=0; i<mag.length; i++)
			mag[i] = (float)Math.sqrt( cpx[2*i]*cpx[2*i] + cpx[2*i+1]*cpx[2*i+1] );
		return mag;
	}
	
}
