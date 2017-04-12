package audioCompression.algorithm.dsp;

import audioCompression.algorithm.dsp.window.BlackmannWindow;
import audioCompression.types.testImpls.RawAudioImpl;
import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.mosaic.PlotFrame;
import edu.mines.jtk.mosaic.PlotPanel;

public class SpectrogramDemo {

public static void main(String args[]){
		
		int nSamps = 10000;
		int stftWindowLength = nSamps/100;
		float overlap = 0.5f; 
		
		RawAudioImpl audio = new RawAudioImpl(nSamps, nSamps, 0, 2);
		
		Spectrogram spec = new Spectrogram( 
				(int)(stftWindowLength*overlap), 
				new BlackmannWindow(stftWindowLength));
		
		float[][] s = spec.computeSpectrogram(audio.getAudioBuffer(nSamps)[0]);
		
		// axis values
		Sampling freqs = new Sampling(s[0].length, 0.5/s[0].length, 0);
		Sampling samps = new Sampling(s.length, nSamps/s.length, stftWindowLength/2.0);
		
		PlotFrame f = new PlotFrame(new PlotPanel(1,1));
		f.getPlotPanel().addPixels(freqs,samps,s);
		f.setSize(400, 300);
		f.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		
	}
	
}
