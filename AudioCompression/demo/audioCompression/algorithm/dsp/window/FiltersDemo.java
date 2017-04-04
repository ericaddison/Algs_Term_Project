package audioCompression.algorithm.dsp.window;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import audioCompression.algorithm.dsp.Filter;
import audioCompression.algorithm.dsp.FilterFactory;

import edu.mines.jtk.mosaic.GridView;
import edu.mines.jtk.mosaic.GridView.Style;
import edu.mines.jtk.mosaic.PlotFrame;
import edu.mines.jtk.mosaic.PlotPanel;
import edu.mines.jtk.mosaic.PointsView;

public class FiltersDemo {

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
		
		List<Filter> filters = new ArrayList<>();
		int N = (int)Math.pow(2, 9);
		
		float cutoff = 0.1f;
		filters.add(FilterFactory.makeLowpassFilter(cutoff, new RectangleWindow(N)));
		filters.add(FilterFactory.makeLowpassFilter(cutoff, new BartlettWindow(N)));
		filters.add(FilterFactory.makeLowpassFilter(cutoff, new HammingWindow(N)));
		filters.add(FilterFactory.makeLowpassFilter(cutoff, new HannWindow(N)));
		filters.add(FilterFactory.makeLowpassFilter(cutoff, new BlackmannWindow(N)));
		filters.add(FilterFactory.makeLowpassFilter(cutoff, new KaiserWindow(N, 5.0f/N)));
		filters.add(FilterFactory.makeLowpassFilter(cutoff, new KbdWindow(N, 15.0f/N)));
		
		PlotFrame f1 = new PlotFrame(new PlotPanel(1,1));
		List<float[]> data = new ArrayList<>();
		
		for(Filter f : filters){
			data.add(f.getCoefficients());
			PointsView pv = f1.getPlotPanel().addPoints(f.getCoefficients());
			pv.setLineColor(colors[filters.indexOf(f)%filters.size()]);
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
