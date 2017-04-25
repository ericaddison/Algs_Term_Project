package audioCompression.algorithm;

import java.io.File;

import audioCompression.types.AudioCompressionType;
import audioCompression.types.AudioFile;
import audioCompression.types.RawAudio;
import audioCompression.types.WavAudioInput;

public class InputOutputStep implements AlgorithmStep<AudioFile, RawAudio>{

	private int windowLength = 1024;
	private int windowOverlap = 0;
	private RawAudio inputAudio;
	private RawAudio outputWav;
	private float rmsError;
	
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
	
	public float getRmsError() {
		return rmsError;
	}
	
	public RawAudio getInputAudio() {
		return inputAudio;
	}

	public RawAudio getOutputWav() {
		return outputWav;
	}

	@Override
	public RawAudio forward(AudioFile input, String name) {
		inputAudio = new WavAudioInput(new File(input.getFilename()), windowLength, windowOverlap);
		return inputAudio;
	}

	@Override
	public AudioFile reverse(RawAudio input, String name) {
		input.writeFile(name);
		rmsError = rmsDiff(inputAudio.getAudioBuffer((int)inputAudio.getNSamples())[0], 
				input.getAudioBuffer((int)inputAudio.getNSamples())[0]);
		outputWav = input;
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
	
	private static float rmsDiff(float[] arr1, float[] arr2){
		if(arr1.length<arr2.length)
			throw new IllegalArgumentException("arr1.length<arr2.length");
		float rms = 0;
		for(int i=0; i<arr1.length; i++){
			rms += (arr1[i] - arr2[i])*(arr1[i] - arr2[i]);
		}
		return (float)Math.sqrt(rms/arr1.length);
	}

}
