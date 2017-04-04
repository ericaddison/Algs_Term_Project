package audioCompression.algorithm.dsp.window;

import java.awt.Color;

import edu.mines.jtk.mosaic.GridView;
import edu.mines.jtk.mosaic.PlotFrame;
import edu.mines.jtk.mosaic.PlotPanel;
import edu.mines.jtk.mosaic.GridView.Style;
import edu.mines.jtk.mosaic.PointsView;
import edu.mines.jtk.mosaic.PointsView.Line;

public class MultiDataPlot extends PlotFrame{

	private static final long serialVersionUID = 1L;
	private static Color[] colors = new Color[] 
			{
		Color.BLACK, 
		Color.BLUE, 
		Color.RED, 
		Color.GREEN, 
		Color.CYAN, 
		Color.MAGENTA, 
		Color.YELLOW};

	public MultiDataPlot(int N, float[][] data) {
		super(new PlotPanel(1,1));
		
		for(int i=0; i<data.length; i++){
			PointsView pv = 
					this.getPlotPanel().addPoints(data[i]);
			pv.setLineColor(colors[i%colors.length]);
			pv.setLineWidth(2);
			if(i%2==1)
				pv.setLineStyle(Line.DASH);
		}

		GridView gv = this.getPlotPanel().addGrid();
		gv.setColor(Color.LIGHT_GRAY);
		gv.setStyle(Style.DASH);

	}	
	
}
