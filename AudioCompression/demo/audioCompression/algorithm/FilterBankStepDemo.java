package audioCompression.algorithm;

import java.awt.Color;
import java.io.File;
import java.util.Iterator;

import edu.mines.jtk.mosaic.PlotFrame;
import edu.mines.jtk.mosaic.PlotPanel;
import edu.mines.jtk.mosaic.PointsView;
import audioCompression.algorithm.dsp.CosineModulatedFilterBank;
import audioCompression.algorithm.dsp.window.HammingWindow;
import audioCompression.algorithm.dsp.window.HannWindow;
import audioCompression.algorithm.dsp.window.KaiserWindow;
import audioCompression.algorithm.dsp.window.RectangleWindow;
import audioCompression.types.AudioByteBuffer;
import audioCompression.types.Subbands;
import audioCompression.types.WavAudioInput;
import audioCompression.types.WavAudioOutput;
import audioCompression.types.testImpls.RawAudioImpl;

public class FilterBankStepDemo {
	
	private static Color[] colors = new Color[] 
			{
		Color.ORANGE, 
		Color.BLUE, 
		Color.RED, 
		Color.GREEN, 
		Color.CYAN, 
		Color.MAGENTA, 
		Color.YELLOW};
	
	public static void main(String[] args){
		
		int nbands = 8;
		int fN = 2*512;
		
		// apply to audio test
		//RawAudioImpl audio = new RawAudioImpl(1000, 500, 0);
		String filename = "../src_wavs/nokia_tune.wav";
		int nsamps = 48000/32;
		WavAudioInput audio = new WavAudioInput(new File(filename), 2048*nbands, 0);
		//System.out.println(audio.getNSamples());
		FilterBankStep fb = new FilterBankStep(nbands, new HannWindow(fN));
		//FilterBankStep fb = new FilterBankStep(nbands, new HannWindow(fN));
		//FilterBankStep fb = new FilterBankStep(nbands, new KaiserWindow(fN,0.05f));
		SubbandsByteBufferizerStep sbb = new SubbandsByteBufferizerStep();
		
		long t1 = System.currentTimeMillis();
		Subbands sub = fb.forward(audio);
		long t2 = System.currentTimeMillis();
		System.out.println("filterbank forward time: " + (t2-t1) + "ms");
		
		t1 = System.currentTimeMillis();
		AudioByteBuffer abb = sbb.forward(sub);
		t2 = System.currentTimeMillis();
		System.out.println("bufferizer forward time: " + (t2-t1) + "ms");
		
		t1 = System.currentTimeMillis();
		Subbands sub2 = sbb.reverse(abb);
		t2 = System.currentTimeMillis();
		System.out.println("bufferizer reverse time: " + (t2-t1) + "ms");
		
		t1 = System.currentTimeMillis();
		WavAudioOutput output = (WavAudioOutput)fb.reverse(sub2);
		t2 = System.currentTimeMillis();
		System.out.println("filterbank reverse: " + (t2-t1) + "ms");
		
		Subbands sub = fb.forward(audio, "subBandsForw");
		WavAudioOutput output = (WavAudioOutput)fb.reverse(sub, "subBandsRev");
		
		System.out.println(sub.getAllWindows().length);
		System.out.println(sub.getAllWindows()[0].length);
		System.out.println(sub.getAllWindows()[0][0].length);
		System.out.println(sub.getAllWindows()[0][0][0].length);
	
		float[][][][] allWindows = sub2.getAllWindows();

		System.out.println("byte depth = " + sub2.getByteDepth());
		
		
		float[][] bands = new float[nbands][sub.getNWindows()*sub.getSamplesPerWindow()];
		
		for(int i=0; i<sub2.getNWindows(); i++)
			for(int j=0; j<sub2.getSamplesPerWindow(); j++)
				for(int b=0; b<nbands; b++){
					bands[b][j + i*sub2.getSamplesPerWindow()] = allWindows[0][b][i][j];
				}
		
		PlotFrame plot = new PlotFrame(new PlotPanel(1,1));
		
		/*for(int i=0; i<nbands; i++){
			PointsView pv = plot.getPlotPanel().addPoints(bands[i]);
			pv.setLineColor(colors[i%colors.length]);
		}
		*/
		
		Iterator<float[][]> iter = audio.getWindowIterator();
		float[][] firstWin = iter.next();
		
		plot.getPlotPanel().addPoints(audio.getAudioBuffer((int)audio.getNSamples())[0]);
		PointsView pv = plot.getPlotPanel().addPoints(output.getAllWindows()[0][0]);
		//PointsView pv = plot.getPlotPanel().addPoints(sub2.getAllWindows()[0][0][1]);
		//PointsView pv = plot.getPlotPanel().addPoints(firstWin[0]);
		pv.setLineColor(Color.red);
		plot.setSize(500, 400);
		plot.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
		plot.setVisible(true);
		
	}
	
	
	
	
}
