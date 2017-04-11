package audioCompression.types;

import edu.mines.jtk.mosaic.PlotFrame;
import edu.mines.jtk.mosaic.PlotPanel;
import audioCompression.types.testImpls.RawAudioImpl;

public class RawAudioImplDemo {

	public static void main(String[] args){
		
		RawAudioImpl audio = new RawAudioImpl(1000, 100, 20);
		
		PlotFrame f1 = new PlotFrame(new PlotPanel(2, 1));
		f1.getPlotPanel().addPoints(0,0,audio.getAudioBuffer(1000)[0]);
		f1.getPlotPanel().addPoints(1,0,audio.getAudioBuffer(1000)[1]);
		
		f1.setSize(400, 300);
		f1.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
		f1.setVisible(true);
		
	}
	
}
