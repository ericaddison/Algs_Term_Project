package audioCompression.algorithm;

import java.awt.Color;
import java.io.File;
import java.util.Iterator;

import edu.mines.jtk.mosaic.PlotFrame;
import edu.mines.jtk.mosaic.PlotPanel;
import edu.mines.jtk.mosaic.PointsView;
import audioCompression.ByteRestrictionAcousticModel;
import audioCompression.algorithm.dsp.CosineModulatedFilterBank;
import audioCompression.algorithm.dsp.window.HammingWindow;
import audioCompression.algorithm.dsp.window.HannWindow;
import audioCompression.algorithm.dsp.window.KaiserWindow;
import audioCompression.algorithm.dsp.window.RectangleWindow;
import audioCompression.algorithm.dsp.window.Window;
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
		// adjustable parameters
		int nbands = 2;				// number of subbands 2, 4, 8, 16, 32, 64
		int fN = 2*512;					// length of the filter-bank filter, 128, 256, 512, 1024, 2048, 4096
		int winSize = 1000*nbands;		// size of signal window
		Window w = new HannWindow(fN);	// window used to construct filter
		
		// I think there might be some offset problem with too many bands (or channels)!!!
		
		// apply to audio test
		String filename = "../src_wavs/nokia_tune.wav";
		
		if(winSize%nbands>0)
			System.out.println("WARNING! winsize and overlap incompatible!");
		WavAudioInput audio = new WavAudioInput(new File(filename), winSize, 0);
		FilterBankStep fb = new FilterBankStep(nbands, w);
		SubbandsByteBufferizerStep sbb = new SubbandsByteBufferizerStep(true);
		sbb.setPsychoAcousticModel(new ByteRestrictionAcousticModel(audio.getSampleRate(), 0, 10000));
		
		String fileName = "testName";
		
		long t1 = System.currentTimeMillis();
		Subbands sub = fb.forward(audio, fileName);
		long t2 = System.currentTimeMillis();
		System.out.println("filterbank forward time: " + (t2-t1) + "ms");
		
		t1 = System.currentTimeMillis();
		AudioByteBuffer abb = sbb.forward(sub, fileName);
		t2 = System.currentTimeMillis();
		System.out.println("bufferizer forward time: " + (t2-t1) + "ms");
		
		System.out.println("\nByteBuffer size = " + abb.getBuffer().capacity() + "\n");
		(new SerializationStep()).forward(abb, "../output_wavs/" + FilterBankStepDemo.class.getSimpleName() + ".jet");
		
		
		t1 = System.currentTimeMillis();
		Subbands sub2 = sbb.reverse(abb, fileName);
		t2 = System.currentTimeMillis();
		System.out.println("bufferizer reverse time: " + (t2-t1) + "ms");
		
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
	
		float[][][][] allWindows = sub2.getAllWindows();

		System.out.println("byte depth = " + sub2.getByteDepth());

		int difflength = (int)audio.getNSamples();
		float rmsDiff = rmsDiff(audio.getAudioBuffer(difflength)[0], 
				output.getAudioBuffer(difflength)[0]);
		System.out.println("rmsDiff: " + rmsDiff);
		
		
		PlotFrame plot = new PlotFrame(new PlotPanel(1,1));

		/*
		for(int i=0; i<output.getNWindows(); i++){
			PointsView pv = plot.getPlotPanel().addPoints(output.getAllWindows()[0][i]);
			pv.setLineColor(colors[i%colors.length]);
		}
		*/
		
		Iterator<float[][]> iter = audio.getWindowIterator();
		float[][] firstWin = iter.next();
		
		float[] firstHalf = output.getAudioBuffer((int)audio.getNSamples()/2)[0];
		plot.getPlotPanel().addPoints(audio.getAudioBuffer((int)audio.getNSamples())[0]);
		PointsView pv = plot.getPlotPanel().addPoints(firstHalf);
		
		int iwin = 1;
		float rmsDiffWin = rmsDiff(audio.getAllWindows()[0][iwin], 
				output.getAllWindows()[0][iwin]);
		System.out.println("rmsDiffWin: " + rmsDiffWin);
		
		//plot.getPlotPanel().addPoints(audio.getAllWindows()[0][iwin]);
		//PointsView pv = plot.getPlotPanel().addPoints(output.getAllWindows()[0][iwin]);
		
		//PointsView pv = plot.getPlotPanel().addPoints(sub.getAllWindows()[0][0][0]);
		//PointsView pv = plot.getPlotPanel().addPoints(sub2.getAllWindows()[0][0][1]);
		//PointsView pv = plot.getPlotPanel().addPoints(firstWin[0]);
		pv.setLineColor(Color.red);
		plot.setSize(500, 400);
		plot.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
//		plot.setVisible(true);
		
	}
	
	
	private static float[] padLeft(float[] arr, int nZeros){
		float[] newArr = new float[arr.length + nZeros];
		for(int i=nZeros; i<newArr.length; i++)
			newArr[i] = arr[i-nZeros];
		return newArr;
	}
	
	private static float rmsDiff(float[] arr1, float[] arr2){
		System.out.println(arr1.length + ", " + arr2.length);
		if(arr1.length<arr2.length)
			throw new IllegalArgumentException("arr1.length<arr2.length");
		float rms = 0;
		for(int i=0; i<arr1.length; i++){
			rms += (arr1[i] - arr2[i])*(arr1[i] - arr2[i]);
		}
		return (float)Math.sqrt(rms/arr1.length);
	}
	
	
}
