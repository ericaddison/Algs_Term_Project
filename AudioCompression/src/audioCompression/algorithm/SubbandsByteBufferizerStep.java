package audioCompression.algorithm;

import java.nio.ByteBuffer;
import java.util.Arrays;

import audioCompression.types.AudioByteBuffer;
import audioCompression.types.AudioCompressionType;
import audioCompression.types.Subbands;

public class SubbandsByteBufferizerStep implements AlgorithmStep<Subbands, AudioByteBuffer>{


	@Override
	public AudioByteBuffer forward(Subbands input) {
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
								float2bytes(samples[ichan][iband][iwin][isamp], byteDepth);
						bytes.put(sampBytes);
					}
		
		bytes.rewind();
		return new AudioByteBuffer(bytes);
	}

	
	@Override
	public Subbands reverse(AudioByteBuffer input) {
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
						float f = bytes2float(nextVal, byteDepth);
							
						windows[ichan][iband][iwin][isamp] = f;
					}		
		
		
			
		return new Subbands(sampleRate, nWindows, 
				samplesPerWindow, windowOverlap, 
				nChannels, nBands, byteDepth, windows);
	}
	
	
	public static byte[] float2bytes(float f, int byteDepth) {
		// clip at +/- 1
		f = (float)(Math.max(Math.min(1, f),-1));
		int maxval = getMaxIntVal(byteDepth);
		
		// scale to int
		int intVal = (int)(((f + 1)/2) * (maxval));
		
		// write bytes
		ByteBuffer bb = ByteBuffer.allocate((Integer.SIZE)/Byte.SIZE);
		bb.putInt(intVal);
		bb.rewind();
		byte[] byteArray = new byte[byteDepth];
		int offset = ((Integer.SIZE)/Byte.SIZE) - byteDepth;
		for(int i=0; i<byteDepth; i++)
			byteArray[i] = bb.get(i + offset);
		return byteArray;
	}
	
	public static float bytes2float(byte[] b, int byteDepth) {
		
		int nFloatBytes = Float.SIZE/Byte.SIZE;
		
		ByteBuffer floatBytes = ByteBuffer.allocate(nFloatBytes);
		for(int i=0; i<(nFloatBytes-byteDepth); i++)
			floatBytes.put((byte)0);
		floatBytes.put(b);
		

		// get the int value
		floatBytes.rewind();
		int intVal = floatBytes.getInt();
		
		// convert to float
		int maxval = getMaxIntVal(byteDepth);
		float f = (2.0f*intVal)/maxval - 1;
		
		return f;
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

	
	public static void main(String[] args){
		
		
		
		float f = -0.147f;
		int byteDepth = 2;
		byte[] b = float2bytes(f, byteDepth);
		float f2 = bytes2float(b, byteDepth);
		System.out.println("my float: " + f);
		System.out.println("To bytes: " + Arrays.toString(b));
		System.out.println("BAck to float: " + f2);
		System.out.println("maxval = " + getMaxIntVal(byteDepth));

		
		
		
	}


	private static int getMaxIntVal(int byteDepth) {
		return (Integer.MAX_VALUE)>>((Integer.SIZE)-byteDepth*8);
	}
	
}
