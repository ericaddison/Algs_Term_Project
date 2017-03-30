package audioCompression.algorithm.dsp.window;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import edu.mines.jtk.mosaic.GridView;
import edu.mines.jtk.mosaic.GridView.Style;
import edu.mines.jtk.mosaic.PlotFrame;
import edu.mines.jtk.mosaic.PlotPanel;
import edu.mines.jtk.mosaic.PointsView;

public class WindowsDemo {

	private static Color[] colors = new Color[] 
			{
		Color.BLACK, 
		Color.BLUE, 
		Color.RED, 
		Color.GREEN, 
		Color.CYAN, 
		Color.MAGENTA, 
		Color.YELLOW};
	
	public static void main(String args[]){
		
		List<Window> windows = new ArrayList<>();
		int N = (int)Math.pow(2, 10);
		windows.add(new RectangleWindow(N));
		windows.add(new BartlettWindow(N));
		windows.add(new HammingWindow(N));
		windows.add(new HannWindow(N));
		windows.add(new BlackmannWindow(N));
		windows.add(new KaiserWindow(N, 5.0f/N));
		
		PlotFrame f1 = new PlotFrame(new PlotPanel(1,1));
		List<float[]> data = new ArrayList<>();
		
		for(Window w : windows){
			data.add(w.getCoefficients());
			PointsView pv = f1.getPlotPanel().addPoints(w.getCoefficients());
			pv.setLineColor(colors[windows.indexOf(w)%windows.size()]);
		}

		GridView gv = f1.getPlotPanel().addGrid();
		gv.setColor(Color.LIGHT_GRAY);
		gv.setStyle(Style.DASH);
		
		PlotFrame f2 = new TwoSidedFftDbPlot(N, data);
		
		f1.setSize(500, 300);
		f1.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
		f1.setVisible(true);
		f2.setSize(500, 300);
		f2.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
		f2.setVisible(true);		
	}
	
}
