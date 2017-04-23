package audioCompression.algorithm;

import java.nio.ByteBuffer;

import audioCompression.algorithm.bytes.ByteUtils;
import audioCompression.types.AudioByteBuffer;
import audioCompression.types.AudioCompressionType;
import audioCompression.types.Lines;

public class LinesByteBufferizerStep implements AlgorithmStep<Lines, AudioByteBuffer>{

	
	private boolean adaptive = false;
	
	public boolean isAdaptive() {
		return adaptive;
	}


	public void setAdaptive(boolean adaptive) {
		this.adaptive = adaptive;
	}


	@Override
	public AudioByteBuffer forward(Lines input, String fileName) {
		int sampleRate = input.getSampleRate();
		int nWindows = input.getNWindows();
		int samplesPerWindow = input.getSamplesPerWindow();
		int nChannels = input.getNChannels();
		int nBands = input.getNBands();
		int byteDepth = input.getByteDepth();
		
		
		int capacity = 6*(Integer.SIZE/Byte.SIZE)
				+ nChannels * nBands * nWindows * samplesPerWindow * byteDepth;
		
		capacity += (adaptive)?(nChannels * nBands * nWindows * 2*(Float.SIZE/Byte.SIZE)):0;
		
		// create byte buffer
		ByteBuffer bytes = ByteBuffer.allocate(capacity);  
		
		// write out top level meta-data
		bytes.putInt(sampleRate);
		bytes.putInt(nWindows);
		bytes.putInt(nChannels);
		bytes.putInt(nBands);
		bytes.putInt(samplesPerWindow);
		bytes.putInt(byteDepth);
		
		// write out samples in channel/band/window/sample order
		float[][][][] samples = input.getAllWindows();
		for(int ichan = 0; ichan < nChannels; ichan++)
			for(int iband = 0; iband < nBands; iband++)
				for(int iwin = 0; iwin < nWindows; iwin++)
					for(int isamp = 0; isamp < samplesPerWindow; isamp++){
						byte[] sampBytes;
						float[] minMax;
						if(adaptive){
							minMax = ByteUtils.getMinMax(samples[ichan][iband][iwin]);
							bytes.putFloat(minMax[0]);
							bytes.putFloat(minMax[1]);
						} else {
							minMax = new float[] {-1, 1};
						}
						sampBytes = 
								ByteUtils.float2bytes(samples[ichan][iband][iwin][isamp], byteDepth, minMax[0], minMax[1]);
						bytes.put(sampBytes);
					}
		
		bytes.rewind();
		return new AudioByteBuffer(bytes);
	}

	
	@Override
	public Lines reverse(AudioByteBuffer input, String fileName) {
		input.getBuffer().rewind();
		int sampleRate = input.getBuffer().getInt();
		int nWindows = input.getBuffer().getInt();
		int nChannels = input.getBuffer().getInt();
		int nBands = input.getBuffer().getInt();
		int samplesPerWindow = input.getBuffer().getInt();
		int byteDepth = input.getBuffer().getInt();

		float[][][][] windows = new float[nChannels][nBands][nWindows][samplesPerWindow];
		for(int ichan = 0; ichan < nChannels; ichan++)
			for(int iband = 0; iband < nBands; iband++)
				for(int iwin = 0; iwin < nWindows; iwin++)
					for(int isamp = 0; isamp < samplesPerWindow; isamp++){
						byte[] nextVal = new byte[byteDepth];
						
						float minVal = (adaptive)?input.getBuffer().getFloat():-1;
						float maxVal = (adaptive)?input.getBuffer().getFloat():1;
						
						for(int i=0; i<byteDepth; i++)
							nextVal[i] = input.getBuffer().get();
						float f = ByteUtils.bytes2float(nextVal, byteDepth, minVal, maxVal);
							
						windows[ichan][iband][iwin][isamp] = f;
					}		
		
		
			
		return new Lines(sampleRate, nWindows, 
				samplesPerWindow, nChannels, nBands, byteDepth, windows);
	}
	


	@Override
	public String getName() {
		return "Lines Byte Bufferizer";
	}

	@Override
	public Class<? extends AudioCompressionType> getInputClass() {
		return Lines.class;
	}

	@Override
	public Class<? extends AudioCompressionType> getOutputClass() {
		return AudioByteBuffer.class;
	}

	
}
