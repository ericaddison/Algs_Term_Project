package general;

import java.io.File;
import java.io.PrintWriter;

import audioCompression.compressors.FullAudioCompressor;
import audioCompression.types.AudioFile;
import audioCompression.types.CompressedAudioFile;

public class FullCompressionDemo {

	private float[] f1 = new float[]{0, 100, 200, 300};
	private float[] f2 = new float[]{15000, 12500, 10000, 8000};
	
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
	        boolean enableDebug = false;
			int itest=0;			
	        compressor.EnableDebugging(enableDebug);
			for (File file: files) {
				String name = file.getName();
				if (name.matches("^(.*wav)")) {
					System.out.println("Loading wav file: " + name + " - running tests ...");
					
					// Create a csv file for logging the metric data
					PrintWriter pw = new PrintWriter(new File("../data/"+name+"_data.csv"));
			        StringBuilder sb = new StringBuilder();
			        
			        sb.append("original filename,compressed name,");
			        compressor.AddMetricTitles(sb);
			        sb.append("Total Compression time (ms), Total Decompression time (ms),");
			        sb.append('\n');
					
					/// do loops in here, to modify the settings on the compressor
					//for (int mdct = 0; mdct < 2; ++mdct) {
						//boolean useMDCT = (mdct==0);
						//System.out.println("Running Tests with MDCT set to " + useMDCT + "...");
						//compressor.EnableMDCTStep(useMDCT);
						
						compressor.EnableAdaptiveByteBufferizer(false);
						for (int bb = 0; bb < 2; ++bb) {
							boolean useAdaptiveBB = (bb==0);
							System.out.println("Running Tests with Adaptive Byte Bufferizer set to " + useAdaptiveBB + "...");
							compressor.EnableAdaptiveByteBufferizer(useAdaptiveBB);
							
							// run the compress - decompress with adaptive encoding off, then run with it enabled
							for(int hadapt=0; hadapt<2; hadapt++){
								boolean useAdaptiveHuffman = (hadapt==0);
								System.out.println("Running Tests with Adaptive Huffman set to " + useAdaptiveHuffman + "...");
								compressor.EnableHuffmanAdaptiveEncoding(useAdaptiveHuffman);
								
								for(int ibands=2; ibands<=32; ibands*=2){
									System.out.println("Running test with nBands = " + ibands);
									compressor.SetNumberOfSubBands(ibands);
									
									for(int filterLen=256; filterLen<=4096; filterLen*=2){
										System.out.println("Running test with filter length = " + filterLen);
										compressor.SetFilterBankLength(filterLen);
										
										int windowLen = 256;
										for (int i = 0; i < 5; ++i) {
											System.out.println("Running test with Window size of " + String.valueOf(windowLen) + "\n");
											compressor.SetSignalWindowSize(windowLen);
											
											for(int iModel=0; iModel<5; iModel++){
												if(iModel==0){
													compressor.SetPsychoAcousticModelFrequencies(0, 0, 0);
													System.out.println("Running test with no PsychoAcousticModel");
												}
												else{
													compressor.SetPsychoAcousticModelFrequencies(0, f1[iModel-1], f2[iModel-1]);
													System.out.println("Running test with PsychoAcousticModel " + f1[iModel-1] + " - " + f2[iModel-1]);	
												}
												
												System.out.println("Running test #" + (++itest));
												RunCompressDecompress(file, sb);				
												
											}//END model loop
											windowLen *= 2;
										} // END window length loop
									} // END filter length loop 
								} // END subbands loop
							} // END adaptive Huffman loop
						} // END adaptive byte bufferizer loop
					//} // END MDCT loop
					
					pw.write(sb.toString());
					pw.close();	
					System.out.println("Finished running tests on wav file: " + name + "\n-----------------------------------------------------\n");
				} // END file match .wav if
			} // END file loop
			
			
		}
		catch (Exception e)
		{
			System.err.println(e);
			e.printStackTrace();
		}
	}	
	
	public static void main(String[] args) {

		FullCompressionDemo demo = new FullCompressionDemo();
		
		File[] files = new File("../src_wavs").listFiles();
		
		demo.RunTest1(files);		
		
		System.out.println("----------------------\n-- Test 1 complete --\n----------------------");
	}

	
	
}

