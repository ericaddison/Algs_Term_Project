package audioCompression.algorithm.dsp;

import audioCompression.algorithm.dsp.window.Window;

/**
 * Simple spectrogram implementation for a coarse time/frequency analysis
 * @author Eric Addison, Taylor Autry, Josh Musick
 *
 */
public class Spectrogram{

	private ShortTimeFft stft;
	
	public Spectrogram(int sampleOverlap, Window w) {
		stft = new ShortTimeFft(w, sampleOverlap);
	}
	
	public float[][] computeSpectrogram(float[] in){
		return STFTMagDb(in);
	}
	
	private float[][] STFTMagDb(float[] arr){
		float[][] s = stft.computeForward(arr);
		for(int i=0; i<s.length; i++)
			s[i] = complexMag(s[i]);
		return s;
	}
	
	private float[] complexMag(float[] cpx){
		float[] mag = new float[cpx.length/2];
		
		for(int i=0; i<mag.length; i++)
			mag[i] = (float)Math.sqrt( cpx[2*i]*cpx[2*i] + cpx[2*i+1]*cpx[2*i+1] );
		return mag;
	}
	
}
