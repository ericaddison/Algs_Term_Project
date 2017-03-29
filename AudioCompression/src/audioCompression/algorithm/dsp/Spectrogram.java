package audioCompression.algorithm.dsp;

import edu.mines.jtk.dsp.Fft;
import edu.mines.jtk.mosaic.PixelsView;
import edu.mines.jtk.mosaic.PlotFrame;
import edu.mines.jtk.mosaic.PlotPanel;
import audioCompression.algorithm.dsp.window.BlackmannWindow;
import audioCompression.algorithm.dsp.window.Window;
import audioCompression.types.testImpls.RawAudioImpl;

/**
 * Simple spectrogram implementation for a coarse time/frequency analysis
 * @author Eric Addison, Taylor Autry, Josh Musick
 *
 */
public class Spectrogram {

	public Spectrogram(float[] input, int sampleOverlap, Window w) {
		float[][] s = STFT(input, sampleOverlap, w);
		
		System.out.println(s[0].length);
		
		PlotFrame f = new PlotFrame(new PlotPanel(1,1));
		PixelsView pv = f.getPlotPanel().addPixels(s);
		f.setSize(400, 300);
		f.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		
	}
	
	private float[][] STFT(float[] arr, int sampleOverlap, Window w){
		int N = arr.length;		// length of full array
		int M = w.getLength();	// window length
		
		int increment = M-sampleOverlap;
		
		int nWindows = N/increment;
		
		float[][] stft = new float[nWindows][];
		
		for(int i=0; i<nWindows; i++)
			stft[i] = fftMagDb(w.applyNormalized(arr, i*increment));
		
		return stft;
	}
	
	
	private float[] fftMagDb(float[] arr){
		Fft fft = new Fft(arr.length);
		float[] fftMag = complexMag(fft.applyForward(arr));
		fftMag[fftMag.length-1] = fftMag[fftMag.length-2]; 
		
		
		// find max
		float max = -Float.MAX_VALUE;
		for(int i=0; i<fftMag.length; i++)
			max = Math.max(max, Math.abs(fftMag[i]));
		
		// db relative to max
		for(int i=0; i<fftMag.length; i++){
			if(fftMag[i]==0)
				fftMag[i] = fftMag[i-1];
			else
				fftMag[i] = 10*(float)Math.log10(Math.abs(fftMag[i])/max);
		}
		
		return fftMag;
	}
	
	private float[] complexMag(float[] cpx){
		float[] mag = new float[cpx.length/2];
		
		for(int i=0; i<mag.length; i++)
			mag[i] = (float)Math.sqrt( cpx[2*i]*cpx[2*i] + cpx[2*i+1]*cpx[2*i+1] );
		return mag;
	}
	
	
	public static void main(String args[]){
		
		int nSamps = 10000;
		int nStftWindows = 100;
		int stftWindowLength = nSamps/nStftWindows;
		float overlap = 0.25f; 
		
		RawAudioImpl audio = new RawAudioImpl(nSamps, nSamps, 0);
		
		Spectrogram s = new Spectrogram(
				audio.getAudioBuffer(nSamps)[0], 
				(int)(stftWindowLength*overlap), 
				new BlackmannWindow(stftWindowLength));
		
	}
	
}
