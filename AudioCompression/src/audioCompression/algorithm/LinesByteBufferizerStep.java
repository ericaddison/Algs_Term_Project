package audioCompression.algorithm;

import java.nio.ByteBuffer;

import audioCompression.ByteRestrictionAcousticModel;
import audioCompression.types.AudioByteBuffer;
import audioCompression.types.AudioCompressionType;
import audioCompression.types.Lines;

public class LinesByteBufferizerStep extends ByteBufferizer implements AlgorithmStep<Lines, AudioByteBuffer>{

	public LinesByteBufferizerStep(boolean adaptive) {
		this.adaptive = adaptive;
	}
	
	public LinesByteBufferizerStep() {
		this(false);
	}
	
	@Override
	public AudioByteBuffer forward(Lines input, String fileName) {
		int sampleRate = input.getSampleRate();
		int nWindows = input.getNWindows();
		int samplesPerWindow = input.getSamplesPerWindow();
		int nChannels = input.getNChannels();
		int nBands = input.getNBands();
		int byteDepth = input.getByteDepth();
		
		if(model==null)
			model = new ByteRestrictionAcousticModel(sampleRate, 0, 1/(2.0f*sampleRate));
		model.setSampleRate(sampleRate);
		
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
		putChanBandWindowSamples(samples, bytes, byteDepth);
		
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
		if(model==null)
			model = new ByteRestrictionAcousticModel(sampleRate, 0, 1/(2.0f*sampleRate));
		model.setSampleRate(sampleRate);
		
		
		float[][][][] windows = new float[nChannels][nBands][nWindows][samplesPerWindow];
		getChanBandWindowSamples(windows, input.getBuffer(), byteDepth);	
		
			
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
