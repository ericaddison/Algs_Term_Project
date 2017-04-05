package audioCompression.algorithm;

import edu.mines.jtk.mosaic.PlotFrame;
import edu.mines.jtk.mosaic.PlotPanel;
import audioCompression.algorithm.dsp.window.HammingWindow;
import audioCompression.algorithm.dsp.window.HannWindow;
import audioCompression.algorithm.dsp.window.KaiserWindow;
import audioCompression.algorithm.dsp.window.RectangleWindow;
import audioCompression.types.Subbands;
import audioCompression.types.testImpls.RawAudioImpl;

public class FilterBankStepDemo {
	
	public static void main(String[] args){
		
		int nbands = 2;
		
		// apply to audio test
		RawAudioImpl audio = new RawAudioImpl(500, 500, 0);
		FilterBankStep fb = new FilterBankStep(nbands, new KaiserWindow(2000,0.1f));
		
		Subbands sub = fb.forward(audio);
		
		System.out.println(sub.getAllWindows().length);
		System.out.println(sub.getAllWindows()[0].length);
		System.out.println(sub.getAllWindows()[0][0].length);
		System.out.println(sub.getAllWindows()[0][0][0].length);
	
		float[][][][] allWindows = sub.getAllWindows();

		
		float[][] bands = new float[nbands][sub.getNWindows()*sub.getSamplesPerWindow()];
		
		for(int i=0; i<sub.getNWindows(); i++)
			for(int j=0; j<sub.getSamplesPerWindow(); j++)
				for(int b=0; b<nbands; b++)
					bands[b][j + i*sub.getSamplesPerWindow()] = allWindows[0][b][i][j];
		
		PlotFrame plot = new PlotFrame(new PlotPanel(1,1));
		
		for(int i=0; i<nbands; i++)
			plot.getPlotPanel().addPoints(bands[i]);
		//plot.getPlotPanel().addPoints(audio.getAudioBuffer((int)audio.getNSamples())[0]);
		plot.setSize(500, 400);
		plot.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
		plot.setVisible(true);
		
	}
	
	
	
	
}
