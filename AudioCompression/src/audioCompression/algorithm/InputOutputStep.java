package audioCompression.algorithm;

import java.io.File;

import audioCompression.types.AudioCompressionType;
import audioCompression.types.AudioFile;
import audioCompression.types.RawAudio;
import audioCompression.types.WavAudioInput;

public class InputOutputStep implements AlgorithmStep<AudioFile, RawAudio>{

	private int windowLength = 1024;
	private int windowOverlap = 0;
	
	public int getWindowLength() {
		return windowLength;
	}

	public void setWindowLength(int windowLength) {
		this.windowLength = windowLength;
	}

	public int getWindowOverlap() {
		return windowOverlap;
	}

	public void setWindowOverlap(int windowOverlap) {
		this.windowOverlap = windowOverlap;
	}

	@Override
	public RawAudio forward(AudioFile input, String name) {
		WavAudioInput wav = new WavAudioInput(new File(input.getFilename()), windowLength, windowOverlap);
		return wav;
	}

	@Override
	public AudioFile reverse(RawAudio input, String name) {
		input.writeFile(name);
		return new AudioFile(name);
	}

	@Override
	public String getName() {
		return "InputOutputStep";
	}

	@Override
	public Class<? extends AudioCompressionType> getInputClass() {
		return AudioFile.class;
	}

	@Override
	public Class<? extends AudioCompressionType> getOutputClass() {
		return RawAudio.class;
	}

}
