package audioCompression.types;

import java.io.File;

import libs.wavParser.WavFile;
import audioCompression.compressors.StrippedDownMP3;

public class FileCompressDemo {

//	private StrippedDownMP3 pipeline;
	
	
	public static void RunTest(File file)
	{
		StrippedDownMP3 pipeline = new StrippedDownMP3();
		
		/// This doesn't appear to work.  The StrippedDownMP3 expects RawAudio not WavAudio
		WavAudioInput wav = new WavAudioInput(file, 48000/32, 0);
		long startTime = System.nanoTime();
		CompressedAudioFile out = pipeline.compress(wav, "name_compressed.jet");
		
		System.out.println("Output string should be [ name_compressed.jet ] and actually was [ " + out.GetCompressedFilename() + " ]");
		
		
		long midTime = System.nanoTime();
		RawAudio wavOut = pipeline.decompress(out, "name_decompressed.wav");
	
		long endTime = System.nanoTime();
		
		long compressTime = midTime - startTime;
		long decompressTime = endTime - midTime;
		
		System.out.println("Compression Time of " + file.getName() + " was " + String.valueOf(compressTime) + " and decompress Time was " + String.valueOf(decompressTime));
		
	}
	
	public static void CheckForMultipleWindows(File file) 
	{
		try
		{
			WavFile wavFile = WavFile.openWavFile(file);
			wavFile.display();
			int numChannels = wavFile.getNumChannels();
			int numFrames = (int) wavFile.getNumFrames();
			int numValidBits = wavFile.getValidBits();
			long sampleRate = wavFile.getSampleRate();
			
			
			if (numChannels != 1) {
				System.out.println("Wave file " + file.getName() + " has " + String.valueOf(numChannels) + " channels" );
				// do stuff
				
				double[][] buffer = new double[numChannels][numFrames];
				
				int framesRead;
				do
				{
					framesRead = wavFile.readFrames(buffer, numFrames);	
				}
				while (framesRead != 0);
				
				wavFile.close();
			
				// remove the .wav from the string name
				String fileName = file.getName();
				fileName = fileName.substring(0, fileName.length() - 4);
							
							
				for (int i = 0; i < 2; ++i) {
					File newFile = new File("../src_wavs/" + fileName + (i == 0 ? "_L.wav" : "_R.wav"));
					
					wavFile = WavFile.newWavFile(newFile, 1, numFrames, numValidBits, sampleRate);
					
					int frameCounter = 0;
					while (frameCounter < numFrames)
					{
						long remaining = wavFile.getFramesRemaining();
						int toWrite = (remaining > 100) ? 100 : (int) remaining;
						
						int frames = wavFile.writeFrames(buffer[i], frameCounter, toWrite);
						frameCounter += frames;
						
					}
					wavFile.close();
					
				}				
			} else {
				wavFile.close();
				return;
			}
			wavFile.close();
		}
		catch(Exception e)
		{
			System.err.println(e);
		}		
	}
	
	public static void main(String[] args){
		
		File[] fileNames = new File("../src_wavs").listFiles();
					
		for (File file: fileNames) {
			if(file.getName().matches("^(.*wav)")){
				System.out.println("\nLoading wav file: " + file.getName());
				RunTest(file);			
				//CheckForMultipleWindows(file);
			}
		}
	}
	
}
