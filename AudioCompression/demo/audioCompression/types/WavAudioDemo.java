package audioCompression.types;

import java.io.File;
import java.util.Arrays;

import edu.mines.jtk.mosaic.PixelsView;
import edu.mines.jtk.mosaic.PixelsView.Orientation;
import edu.mines.jtk.mosaic.PlotFrame;
import edu.mines.jtk.mosaic.PlotPanel;

public class WavAudioDemo {

	public static void main(String[] args){
		String filename = "../src_wavs/nokia_tune.wav";
		int nsamps = 48000/32;
		WavAudio wav = new WavAudio(new File(filename), nsamps, nsamps/10);
		
		System.out.println("file " + filename + " stats:");
		System.out.println("-- sample rate: " + wav.getSampleRate() + " samples per second");
		System.out.println("-- window size: " + wav.getSamplesPerWindow() + " samples per window");
		System.out.println("-- window overlap: " + wav.getWindowOverlap()*100 + " %");
		System.out.println("-- nSamples: " + wav.getNSamples());
		System.out.println("-- some samples: " + Arrays.toString(wav.getAudioBuffer(10)[0]));
		System.out.println("-- A window: " + Arrays.toString((wav.getWindowIterator()).next()[0]));
		
		float[][][] allWindows = wav.getAllWindows();
		
		// get the raw audio buffer and plot it
		float[] buff = wav.getAudioBuffer((int)wav.getNSamples())[0];
		PlotFrame frame1 = new PlotFrame(new PlotPanel(1,1));
		frame1.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
		frame1.getPlotPanel().addPoints(buff);
		frame1.setSize(500, 300);
		frame1.setVisible(true);
		
		// get all the windows and plot 2D
		PlotFrame frame2 = new PlotFrame(new PlotPanel(1,1));
		frame2.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
		PixelsView pv = frame2.getPlotPanel().addPixels(allWindows[0]);
		pv.setOrientation(Orientation.X1DOWN_X2RIGHT);
		pv.setClips(-0.25f, 0.25f);
		frame2.setSize(500, 300);
		frame2.setVisible(true);
	}
	
}
