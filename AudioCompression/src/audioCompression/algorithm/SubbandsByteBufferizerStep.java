package audioCompression.algorithm;

import java.nio.ByteBuffer;

import audioCompression.ByteRestrictionAcousticModel;
import audioCompression.types.AudioByteBuffer;
import audioCompression.types.AudioCompressionType;
import audioCompression.types.Subbands;

public class SubbandsByteBufferizerStep extends ByteBufferizer implements AlgorithmStep<Subbands, AudioByteBuffer>{

	public SubbandsByteBufferizerStep(boolean adaptive) {
		this.adaptive = adaptive;
	}
	
	public SubbandsByteBufferizerStep() {
		this(false);
	}

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
		
		capacity += (adaptive)?(nChannels * nBands * nWindows * 2*(Float.SIZE/Byte.SIZE)):0;
		
		// setup model
		if(model==null)
			model = new ByteRestrictionAcousticModel(sampleRate, 0, sampleRate/2.0f);
		model.setSampleRate(sampleRate);
		
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
		int written = putChanBandWindowSamples(samples, bytes, byteDepth);
		
		// copy into new bytebuffer with proper length
		bytes.rewind();
		int newCap = written + 7*(Integer.SIZE/Byte.SIZE);
		ByteBuffer newBuf = ByteBuffer.allocate(newCap);
		for(int i=0; i<newCap; i++)
			newBuf.put(bytes.get());
		
		newBuf.rewind();
		return new AudioByteBuffer(newBuf);
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

		if(model==null)
			model = new ByteRestrictionAcousticModel(sampleRate, 0, 1/(2.0f*sampleRate));
		model.setSampleRate(sampleRate);
		
		
		float[][][][] windows = new float[nChannels][nBands][nWindows][samplesPerWindow];
		getChanBandWindowSamples(windows, input.getBuffer(), byteDepth);	
			
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
