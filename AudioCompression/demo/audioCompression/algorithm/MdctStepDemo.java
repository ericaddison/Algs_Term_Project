package audioCompression.algorithm;

import java.awt.Color;
import java.io.File;

import audioCompression.algorithm.dsp.window.HannWindow;
import audioCompression.types.AudioByteBuffer;
import audioCompression.types.Lines;
import audioCompression.types.Subbands;
import audioCompression.types.WavAudioInput;
import audioCompression.types.WavAudioOutput;
import edu.mines.jtk.mosaic.PlotFrame;
import edu.mines.jtk.mosaic.PlotPanel;
import edu.mines.jtk.mosaic.PointsView;

public class MdctStepDemo {


	public static void main(String[] args){
		
		int nbands = 8;
		int fN = 2*512;
		
		String filename = "../src_wavs/nokia_tune.wav";
		
		int winSize = 50*nbands;
		int winOverlap = winSize/2;
		
		System.out.println("winSize = " + winSize);
		System.out.println("winOverlap = " + winOverlap);
		
		if(winSize%nbands>0)
			System.out.println("WARNING! winsize and overlap incompatible!");
		WavAudioInput audio = new WavAudioInput(new File(filename), winSize, winOverlap);
		FilterBankStep fb = new FilterBankStep(nbands, new HannWindow(fN));
		MdctStep mdct = new MdctStep();
		LinesByteBufferizerStep lbb = new LinesByteBufferizerStep();
		lbb.setAdaptive(true);
		
		String fileName = "testName";
		
		long t1 = System.currentTimeMillis();
		Subbands sub = fb.forward(audio, fileName);
		long t2 = System.currentTimeMillis();
		System.out.println("filterbank forward time: " + (t2-t1) + "ms");
		
		t1 = System.currentTimeMillis();
		Lines lines = mdct.forward(sub, fileName);
		t2 = System.currentTimeMillis();
		System.out.println("mdct forward time: " + (t2-t1) + "ms");
		
		t1 = System.currentTimeMillis();
		AudioByteBuffer abb = lbb.forward(lines, fileName);
		t2 = System.currentTimeMillis();
		System.out.println("bufferizer forward time: " + (t2-t1) + "ms");
		
		t1 = System.currentTimeMillis();
		Lines lines2 = lbb.reverse(abb, fileName);
		t2 = System.currentTimeMillis();
		System.out.println("bufferizer reverse time: " + (t2-t1) + "ms");
		
		
		
		t1 = System.currentTimeMillis();
		Subbands sub2 = mdct.reverse(lines2, fileName);
		t2 = System.currentTimeMillis();
		System.out.println("mdct reverse time: " + (t2-t1) + "ms");
		
		t1 = System.currentTimeMillis();
		WavAudioOutput output = (WavAudioOutput)fb.reverse(sub2, fileName);
		t2 = System.currentTimeMillis();
		System.out.println("filterbank reverse: " + (t2-t1) + "ms");
		
		// write the file
		output.writeFile("../output_wavs/" + FilterBankStepDemo.class.getSimpleName() + ".wav");
		
		System.out.println(sub.getAllWindows().length);
		System.out.println(sub.getAllWindows()[0].length);
		System.out.println(sub.getAllWindows()[0][0].length);
		System.out.println(sub.getAllWindows()[0][0][0].length);
	

		PlotFrame plot = new PlotFrame(new PlotPanel(1,1));

		float[] firstHalf = output.getAudioBuffer((int)audio.getNSamples()/2)[0];
		plot.getPlotPanel().addPoints(audio.getAudioBuffer((int)audio.getNSamples())[0]);
		PointsView pv = plot.getPlotPanel().addPoints(firstHalf);
		
		pv.setLineColor(Color.red);
		plot.setSize(500, 400);
		plot.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
		plot.setVisible(true);
		
	}
	
	
}
