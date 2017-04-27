package audioCompression;


/**
 * A class of psychoacoustic models that resticts the amount of 
 * bytes used to store samples outside of a given frequency range.
 * 
 * Currently hard-coded to cut the byte depth in half outside of the pass band.
 * 
 * @author eric
 *
 */
public class ByteRestrictionAcousticModel {

	float sampleRate;
	float lowFreq;
	float highFreq;
	float restrictFactor = 2;
	
	public ByteRestrictionAcousticModel(float sampleRate, float lowFreq, float highFreq) {
		super();
		this.sampleRate = sampleRate;
		this.lowFreq = lowFreq;
		this.highFreq = highFreq;
	}
	
	public float getLowFreq() {
		return lowFreq;
	}

	public void setLowFreq(float lowFreq) {
		this.lowFreq = lowFreq;
	}

	public float getHighFreq() {
		return highFreq;
	}

	public void setHighFreq(float highFreq) {
		this.highFreq = highFreq;
	}

	public float getSampleRate() {
		return sampleRate;
	}

	public void setSampleRate(float sampleRate) {
		this.sampleRate = sampleRate;
	}

	/**
	 * Get the byte depth prescribed by this model for the given
	 * band out of the given total number of bands.
	 * @param iband
	 * @param nBands
	 * @return
	 */
	public int getByteDepth(int byteDepth, int iband, int nBands){
		float freq = ((0.5f*iband*sampleRate)/ (nBands));
		return getByteDepth(byteDepth, freq );
	}
	
	public int getByteDepth(int byteDepth, float freq){
		if(freq>highFreq || freq<lowFreq)
			return (int)(byteDepth / restrictFactor);
		return byteDepth;
	}
	
}
