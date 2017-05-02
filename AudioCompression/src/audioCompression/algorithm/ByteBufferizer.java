package audioCompression.algorithm;

import java.nio.ByteBuffer;

import audioCompression.ByteRestrictionAcousticModel;
import audioCompression.algorithm.bytes.ByteUtils;

public abstract class ByteBufferizer{

	protected ByteRestrictionAcousticModel model;
	
	public ByteRestrictionAcousticModel getPsychoAcousticModel() {
		return model;
	}

	public void setPsychoAcousticModel(ByteRestrictionAcousticModel model) {
		this.model = model;
	}

	protected boolean adaptive = false;
	
	public boolean isAdaptive() {
		return adaptive;
	}

	public void setAdaptive(boolean adaptive) {
		this.adaptive = adaptive;
	}
	
	public int putChanBandWindowSamples(float[][][][] samples, ByteBuffer byteBuffer, int byteDepth){
		int byteCount = 0;
		for(int ichan = 0; ichan < samples.length; ichan++)
			for(int iband = 0; iband < samples[ichan].length; iband++){
				int currentByteDepth = (model==null)?byteDepth:model.getByteDepth(byteDepth, iband, samples[ichan].length);
				if(currentByteDepth<=0)
					throw new IllegalStateException("ByteBufferizer: found currentByteDepth==0");
				
				for(int iwin = 0; iwin < samples[ichan][iband].length; iwin++){
					float[] minMax;
					if(adaptive){
						minMax = ByteUtils.getMinMax(samples[ichan][iband][iwin]);
						byteBuffer.putFloat(minMax[0]);
						byteBuffer.putFloat(minMax[1]);
						byteCount += 2*(Float.SIZE/Byte.SIZE);
					} else {
						minMax = new float[] {-1, 1};
					}
					for(int isamp = 0; isamp < samples[ichan][iband][iwin].length; isamp++){
						byte[] sampBytes =
								ByteUtils.float2bytes(samples[ichan][iband][iwin][isamp], currentByteDepth, minMax[0], minMax[1]);
						byteBuffer.put(sampBytes);
						byteCount += currentByteDepth;
					}
				}	
			}
		
		return byteCount;
	}
	
	public void getChanBandWindowSamples(float[][][][] samples, ByteBuffer bytes, int byteDepth){
		for(int ichan = 0; ichan < samples.length; ichan++)
			for(int iband = 0; iband < samples[ichan].length; iband++){
				int currentByteDepth = (model==null)?byteDepth:model.getByteDepth(byteDepth, iband, samples[ichan].length);
				if(currentByteDepth<=0)
					throw new IllegalStateException("ByteBufferizer: found currentByteDepth==0");
				
				for(int iwin = 0; iwin < samples[ichan][iband].length; iwin++){
					float minVal = (adaptive)?bytes.getFloat():-1;
					float maxVal = (adaptive)?bytes.getFloat():1;
					for(int isamp = 0; isamp < samples[ichan][iband][iwin].length; isamp++){
						byte[] nextVal = new byte[currentByteDepth];
						for(int i=0; i<currentByteDepth; i++)
							nextVal[i] = bytes.get();
						float f = ByteUtils.bytes2float(nextVal, currentByteDepth, minVal, maxVal);
						samples[ichan][iband][iwin][isamp] = f;
					}		
				}		
			}
	}
}
