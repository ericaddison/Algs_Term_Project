package audioCompression.algorithm;

import audioCompression.types.AudioByteBuffer;
import audioCompression.types.AudioCompressionType;
import audioCompression.types.CompressedAudioFile;

public class CompressedAudioFileWriter implements AlgorithmStep<AudioByteBuffer, CompressedAudioFile> {

	@Override
	public CompressedAudioFile forward(AudioByteBuffer input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AudioByteBuffer reverse(CompressedAudioFile input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends AudioCompressionType> getInputClass() {
		return AudioByteBuffer.class;
	}

	@Override
	public Class<? extends AudioCompressionType> getOutputClass() {
		return CompressedAudioFile.class;
	}

}
