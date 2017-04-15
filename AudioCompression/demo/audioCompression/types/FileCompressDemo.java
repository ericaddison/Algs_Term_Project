package audioCompression.types;

import java.io.File;

import audioCompression.compressors.StrippedDownMP3;

public class FileCompressDemo {

//	private StrippedDownMP3 pipeline;
	
	
	public static void RunTest(File file)
	{
		StrippedDownMP3 pipeline = new StrippedDownMP3();
		
		/// This doesn't appear to work.  The StrippedDownMP3 expects RawAudio not WavAudio
		WavAudioInput wav = new WavAudioInput(file, 48000/32, 0);
		long startTime = System.nanoTime();
		CompressedAudioFile out = pipeline.compress(wav, "name");
		long midTime = System.nanoTime();
		RawAudio wavOut = pipeline.decompress(out, "name");
	
		long endTime = System.nanoTime();
		
		long compressTime = midTime - startTime;
		long decompressTime = endTime - midTime;
		
		System.out.println("Compression Time of " + file.getName() + " was " + compressTime + " and decompress Time was " + decompressTime);
		
	}
	
	public static void main(String[] args){
		
		File[] fileNames = new File("../src_wavs").listFiles();
					
		for (File file: fileNames) {
			if(file.getName().matches("^(.*wav)")){
				System.out.println("Loading wav file: " + file.getName());
				RunTest(file);				
			}
		}
	}
	
}
