import java.awt.Color;
import java.util.Arrays;

import audioCompression.algorithm.dsp.Filter;
import audioCompression.algorithm.dsp.FilterFactory;
import audioCompression.algorithm.dsp.window.HannWindow;
import audioCompression.types.testImpls.RawAudioImpl;

import edu.mines.jtk.dsp.Conv;
import edu.mines.jtk.mosaic.PlotFrame;
import edu.mines.jtk.mosaic.PlotPanel;
import edu.mines.jtk.mosaic.PointsView;
import edu.mines.jtk.util.ArrayMath;

public class ConvTest {

	public static void main(String[] args){
		
		float[] x1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		float[] x2 = {10, 20, 30, 40};
		float[] x3 = new float[x1.length+x2.length-1];
		
		Conv.conv(x1.length, 0, x1, x2.length, 0, x2, x1.length+x2.length-1, 0, x3);
		System.out.println(Arrays.toString(x3));
		
		x3 = new float[x1.length];
		Conv.conv(x1.length, 0, x1, x2.length, 0, x2, x1.length, (x1.length-x2.length-1)/2, x3);
		System.out.println(Arrays.toString(x3));
		
		RawAudioImpl aud = new RawAudioImpl(500, 500, 0);
		Filter f = FilterFactory.makeLowpassFilter(0.1f, new HannWindow(500));
		float[] filtered = f.applyTimeDomain(aud.getAudioBuffer(500)[0]);
		
		PlotFrame plot = new PlotFrame(new PlotPanel(1,1));
		
		plot.getPlotPanel().addPoints(aud.getAudioBuffer(500)[0]);
		PointsView pv = plot.getPlotPanel().addPoints(filtered);
		pv.setLineColor(Color.red);
		plot.setSize(500, 400);
		plot.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
		plot.setVisible(true);
		
	}
}
