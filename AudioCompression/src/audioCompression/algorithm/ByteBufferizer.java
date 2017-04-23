package audioCompression.algorithm;

import java.nio.ByteBuffer;

import audioCompression.algorithm.bytes.ByteUtils;

public abstract class ByteBufferizer{

	protected boolean adaptive = false;
	
	public boolean isAdaptive() {
		return adaptive;
	}

	public void setAdaptive(boolean adaptive) {
		this.adaptive = adaptive;
	}
	
	public void putChanBandWindowSamples(float[][][][] samples, ByteBuffer byteBuffer, int byteDepth){
		for(int ichan = 0; ichan < samples.length; ichan++)
			for(int iband = 0; iband < samples[ichan].length; iband++)
				for(int iwin = 0; iwin < samples[ichan][iband].length; iwin++){
					float[] minMax;
					if(adaptive){
						minMax = ByteUtils.getMinMax(samples[ichan][iband][iwin]);
						byteBuffer.putFloat(minMax[0]);
						byteBuffer.putFloat(minMax[1]);
					} else {
						minMax = new float[] {-1, 1};
					}
					for(int isamp = 0; isamp < samples[ichan][iband][iwin].length; isamp++){
						byte[] sampBytes =
								ByteUtils.float2bytes(samples[ichan][iband][iwin][isamp], byteDepth, minMax[0], minMax[1]);
						byteBuffer.put(sampBytes);
					}
				}	
	}
	
	public void getChanBandWindowSamples(float[][][][] samples, ByteBuffer bytes, int byteDepth){
		for(int ichan = 0; ichan < samples.length; ichan++)
			for(int iband = 0; iband < samples[ichan].length; iband++)
				for(int iwin = 0; iwin < samples[ichan][iband].length; iwin++){
					float minVal = (adaptive)?bytes.getFloat():-1;
					float maxVal = (adaptive)?bytes.getFloat():1;
					for(int isamp = 0; isamp < samples[ichan][iband][iwin].length; isamp++){
						byte[] nextVal = new byte[byteDepth];
						for(int i=0; i<byteDepth; i++)
							nextVal[i] = bytes.get();
						float f = ByteUtils.bytes2float(nextVal, byteDepth, minVal, maxVal);
						samples[ichan][iband][iwin][isamp] = f;
					}		
				}		
	}
}
