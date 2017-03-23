package audioCompression.types;

import java.io.File;
import java.util.Arrays;

import edu.mines.jtk.mosaic.PlotFrame;
import edu.mines.jtk.mosaic.PlotPanel;

public class WavAudioDemo {

	public static void main(String[] args){
		String filename = "../src_wavs/nokia_tune.wav";
		WavAudio wav = new WavAudio(new File(filename), 120, 0.25f);
		float[] buff = wav.getAudioBuffer(0,100000);
		
		System.out.println("file " + filename + " stats:");
		System.out.println("-- sample rate: " + wav.getSampleRate() + " samples per second");
		System.out.println("-- window size: " + wav.getSamplesPerWindow() + " samples per window");
		System.out.println("-- window overlap: " + wav.getWindowOverlap()*100 + " %");
		System.out.println("-- nSamples: " + wav.getNSamples());
		System.out.println("-- some samples: " + Arrays.toString(wav.getAudioBuffer(0, 5)));
		
		PlotFrame frame = new PlotFrame(new PlotPanel(1,1));
		frame.getPlotPanel().addPoints(buff);
		frame.setSize(500, 300);
		frame.setVisible(true);
	}
	
}
