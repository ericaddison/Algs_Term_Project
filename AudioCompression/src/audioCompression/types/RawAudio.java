package audioCompression.types;

import java.util.Iterator;

public interface RawAudio extends AudioCompressionType {

	public long getSampleRate();
	
	public long getNSamples();
	
	public int getSamplesPerWindow();
	
	public float getWindowOverlap();
	
	/**
	 * Get the raw audio buffer, cast to floats,
	 * from sample samp1 to samp2. length of returned buffer
	 * may vary from actual requested number of samples based
	 * on implementation. Check return array length for
	 * number of samples read. 
	 * @param samp1
	 * @param samp2
	 * @return
	 */
	public float[] getAudioBuffer(int samp1, int samp2);
	
	/**
	 * Get an iterator over the windows of samples 
	 * @return
	 */
	public Iterator<float[]> getWindowIterator();
	
}
