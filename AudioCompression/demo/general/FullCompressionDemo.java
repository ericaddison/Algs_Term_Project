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
	        
	        boolean enableDebug = true;
						
	        compressor.EnableDebugging(enableDebug);
			for (File file: files) {
				String name = file.getName();
				if (name.matches("^(.*wav)")) {
					System.out.println("Loading wav file: " + name + " - running tests ...");
					/// do loops in here, to modify the settings on the compressor
					compressor.EnableMDCTStep(false);
					System.out.println("Running Tests with MDCT Disabled...\n");
					for (int mdct = 0; mdct < 2; ++mdct) {
						System.out.println("Running Tests with Adaptive Byte Bufferizer Disabled...");
						compressor.EnableAdaptiveByteBufferizer(false);
						for (int bb = 0; bb < 2; ++bb) {
							// run the compress - decompress with adaptive encoding off, then run with it enabled
							System.out.println("Running Tests with Huffman Adaptive Encoding Enabled...");
							compressor.EnableHuffmanAdaptiveEncoding(true);
							for(int ibands=2; ibands<32; ibands*=2){
								System.out.println("Running test with nBands = " + ibands);
								compressor.SetNumberOfSubBands(ibands);
								for(int filterLen=256; filterLen<=4096; filterLen*=2){
									System.out.println("Running test with filter length = " + filterLen);
									compressor.SetFilterBankLength(filterLen);
									for (int h = 0; h < 2; ++h) {
										int windowLen = 256;
										for (int i = 0; i < 5; ++i) {
											System.out.println("Running test with Window size of " + String.valueOf(windowLen) + "\n");
											compressor.SetSignalWindowSize(windowLen);
											RunCompressDecompress(file, sb);				
											windowLen *= 2;
										}
										// now enable huffman encoding
										compressor.EnableHuffmanAdaptiveEncoding(false);
										System.out.println("Running Tests with Huffman Adaptive Encoding Disabled...");
									}
								}
							}
							// now enable adaptive byte bufferizer
							compressor.EnableAdaptiveByteBufferizer(true);
							System.out.println("Running Tests with Adaptive Byte Bufferizer Enabled...");
						}						
						// now enable mdct step and re-run the loops
						compressor.EnableMDCTStep(true);
						System.out.println("Running Tests with MCDT Enabled...");
					}
					
					System.out.println("Finished running tests on wav file: " + name + "\n-----------------------------------------------------\n");
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

