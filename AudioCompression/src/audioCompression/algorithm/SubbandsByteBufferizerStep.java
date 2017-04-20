package audioCompression.algorithm;

import java.nio.ByteBuffer;

import audioCompression.algorithm.bytes.ByteUtils;
import audioCompression.types.AudioByteBuffer;
import audioCompression.types.AudioCompressionType;
import audioCompression.types.Subbands;

public class SubbandsByteBufferizerStep implements AlgorithmStep<Subbands, AudioByteBuffer>{


	@Override
	public AudioByteBuffer forward(Subbands input, String fileName) {
		int sampleRate = input.getSampleRate();
		int nWindows = input.getNWindows();
		int samplesPerWindow = input.getSamplesPerWindow();
		int windowOverlap = input.getWindowOverlap();
		int nChannels = input.getNChannels();
		int nBands = input.getNBands();
		int byteDepth = input.getByteDepth();
		
		int capacity = 7*(Integer.SIZE/Byte.SIZE) 
				+ nChannels * nBands * nWindows * samplesPerWindow * byteDepth;
		
		// create byte buffer
		ByteBuffer bytes = ByteBuffer.allocate(capacity);  
		
		// write out top level meta-data
		bytes.putInt(sampleRate);
		bytes.putInt(nWindows);
		bytes.putInt(nChannels);
		bytes.putInt(nBands);
		bytes.putInt(samplesPerWindow);
		bytes.putInt(windowOverlap);
		bytes.putInt(byteDepth);
		
		// write out samples in channel/band/window/sample order
		float[][][][] samples = input.getAllWindows();
		for(int ichan = 0; ichan < nChannels; ichan++)
			for(int iband = 0; iband < nBands; iband++)
				for(int iwin = 0; iwin < nWindows; iwin++)
					for(int isamp = 0; isamp < samplesPerWindow; isamp++){
						byte[] sampBytes = 
								ByteUtils.float2bytes(samples[ichan][iband][iwin][isamp], byteDepth, -1, 1);
						bytes.put(sampBytes);
					}
		
		bytes.rewind();
		return new AudioByteBuffer(bytes);
	}

	
	@Override
	public Subbands reverse(AudioByteBuffer input, String fileName) {
		input.getBuffer().rewind();
		int sampleRate = input.getBuffer().getInt();
		int nWindows = input.getBuffer().getInt();
		int nChannels = input.getBuffer().getInt();
		int nBands = input.getBuffer().getInt();
		int samplesPerWindow = input.getBuffer().getInt();
		int windowOverlap = input.getBuffer().getInt();
		int byteDepth = input.getBuffer().getInt();

		float[][][][] windows = new float[nChannels][nBands][nWindows][samplesPerWindow];
		for(int ichan = 0; ichan < nChannels; ichan++)
			for(int iband = 0; iband < nBands; iband++)
				for(int iwin = 0; iwin < nWindows; iwin++)
					for(int isamp = 0; isamp < samplesPerWindow; isamp++){
						byte[] nextVal = new byte[byteDepth];
						for(int i=0; i<byteDepth; i++)
							nextVal[i] = input.getBuffer().get();
						float f = ByteUtils.bytes2float(nextVal, byteDepth,-1, 1);
							
						windows[ichan][iband][iwin][isamp] = f;
					}		
		
		
			
		return new Subbands(sampleRate, nWindows, 
				samplesPerWindow, windowOverlap, 
				nChannels, nBands, byteDepth, windows);
	}

	@Override
	public String getName() {
		return "Subbands Byte Bufferizer";
	}

	@Override
	public Class<? extends AudioCompressionType> getInputClass() {
		return Subbands.class;
	}

	@Override
	public Class<? extends AudioCompressionType> getOutputClass() {
		return AudioByteBuffer.class;
	}
	
}
