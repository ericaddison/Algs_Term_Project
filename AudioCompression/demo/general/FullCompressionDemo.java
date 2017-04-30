package general;

import java.io.File;
import java.io.PrintWriter;

import audioCompression.compressors.FullAudioCompressor;
import audioCompression.types.AudioFile;
import audioCompression.types.CompressedAudioFile;

public class FullCompressionDemo {

	protected FullAudioCompressor compressor = new FullAudioCompressor();
	
	public void RunCompressDecompress(File file, StringBuilder sb)
	{
		String name = file.getName();
		long startTime = System.nanoTime();	
		AudioFile aFile = new AudioFile(file.getPath());
		String sName = name.substring(0, name.length() - 4);
		
		sb.append(name + "," + sName + ".jet,");
		CompressedAudioFile cfile = compressor.compress(aFile, "../data/" + sName + ".jet");
		long midTime = System.nanoTime();
		compressor.decompress(cfile, "../output_wavs/" + sName +".wav");
		long endTime = System.nanoTime();
		
		double compressTime = (double)(midTime - startTime) / 1000000.0;
		double decompressTime = (double)(endTime - midTime) / 1000000.0;
		
		
		compressor.AddMetrics(sb);
		sb.append(String.valueOf(compressTime));
		sb.append(",");
		sb.append(String.valueOf(decompressTime));
		sb.append(",");
		
		sb.append('\n');
		
	}
	
	public void RunTest1(File[] files) 
	{
		try 
		{
			// Create a csv file for logging the metric data
			PrintWriter pw = new PrintWriter(new File("../data/test1_data.csv"));
	        StringBuilder sb = new StringBuilder();
	        
	        sb.append("original filename,compressed name,");
	        compressor.AddMetricTitles(sb);
	        sb.append("Total Compression time (ms), Total Decompression time (ms),");
	        sb.append('\n');
						
			for (File file: files) {
				String name = file.getName();
				if (name.matches("^(.*wav)")) {
					System.out.println("Loading wav file: " + name + "\n");
									
					/// do loops in here, to modify the settings on the compressor
					
					int windowLen = 256;
					for (int i = 0; i < 5; ++i) {
						System.out.println("Running test with Window size of " + String.valueOf(windowLen) + "\n");
						compressor.SetSignalWindowSize(windowLen);
						RunCompressDecompress(file, sb);				
						windowLen *= 2;
					}
				}
			}
			
			pw.write(sb.toString());
			pw.close();			
			
		}
		catch (Exception e)
		{
			System.err.println(e);
		}
	}	
	
	public static void main(String[] args) {

		FullCompressionDemo demo = new FullCompressionDemo();
		
		File[] files = new File("../src_wavs").listFiles();
		
		demo.RunTest1(files);		
		
		System.out.println("----------------------\n-- Test 1 complete --\n----------------------");
	}

	
	
}

