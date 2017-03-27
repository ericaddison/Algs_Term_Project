package audioCompression.types;

import java.util.Iterator;

public interface RawAudio extends AudioCompressionType {

	public long getSampleRate();
	
	public long getNSamples();
	
	public long getSamplesPerWindow();
	
	public long getNWindows();
	
	public float getWindowOverlap();
	
	public int getNChannels();
	
	/**
	 * Get the next N samples from the raw audio buffer
	 * @param nsamples the number of audio samples to get
	 * @return 2d array of size [nChannels][nsamples]
	 */
	public float[][] getAudioBuffer(int nsamples);
	
	public float[][][] getAllWindows();
	
	
	/**
	 * Get an iterator over the windows of samples 
	 * @return
	 */
	public Iterator<float[][]> getWindowIterator();
	
}
