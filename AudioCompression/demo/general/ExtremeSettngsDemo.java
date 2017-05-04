package general;

import java.io.File;

import audioCompression.compressors.FullAudioCompressor;
import audioCompression.types.AudioFile;
import audioCompression.types.CompressedAudioFile;

public class ExtremeSettngsDemo {

	public static void main(String[] args){
		
		FullAudioCompressor compressor = new FullAudioCompressor();
		String filename;
		CompressedAudioFile comp;
		
		// hi_piano.wav -- best RMS
		filename = "../src_wavs/hi_piano.wav";
		compressor.EnableAdaptiveByteBufferizer(true);
		compressor.EnableHuffmanAdaptiveEncoding(true);
		compressor.SetNumberOfSubBands(2);
		compressor.SetFilterBankLength(4096);
		compressor.SetSignalWindowSize(4096);
		compressor.SetPsychoAcousticModelFrequencies(0, 0, 0);
		comp = compressor.compress(new AudioFile(filename), "tmp.jet");
		compressor.decompress(comp, "../output_wavs/hi_piano_bestRMS.wav");
		
		
		// hi_piano.wav -- best compression
		filename = "../src_wavs/hi_piano.wav";
		compressor.EnableAdaptiveByteBufferizer(false);
		compressor.EnableHuffmanAdaptiveEncoding(false);
		compressor.SetNumberOfSubBands(2);
		compressor.SetFilterBankLength(256);
		compressor.SetSignalWindowSize(256);
		compressor.SetPsychoAcousticModelFrequencies(0, 200, 10000);
		comp = compressor.compress(new AudioFile(filename), "tmp.jet");
		compressor.decompress(comp, "../output_wavs/hi_piano_bestCompression.wav");
		
		// hi_piano.wav -- fastest compression time
		filename = "../src_wavs/hi_piano.wav";
		compressor.EnableAdaptiveByteBufferizer(false);
		compressor.EnableHuffmanAdaptiveEncoding(false);
		compressor.SetNumberOfSubBands(8);
		compressor.SetFilterBankLength(256);
		compressor.SetSignalWindowSize(4096);
		compressor.SetPsychoAcousticModelFrequencies(0, 300, 8000);
		comp = compressor.compress(new AudioFile(filename), "tmp.jet");
		compressor.decompress(comp, "../output_wavs/hi_piano_bestCompTime.wav");
		
		// hi_piano.wav -- fastest decompression time
		filename = "../src_wavs/hi_piano.wav";
		compressor.EnableAdaptiveByteBufferizer(false);
		compressor.EnableHuffmanAdaptiveEncoding(false);
		compressor.SetNumberOfSubBands(16);
		compressor.SetFilterBankLength(256);
		compressor.SetSignalWindowSize(2048);
		compressor.SetPsychoAcousticModelFrequencies(0, 300, 8000);
		comp = compressor.compress(new AudioFile(filename), "tmp.jet");
		compressor.decompress(comp, "../output_wavs/hi_piano_bestDecompTime.wav");
		
		
		
		// drumkit.wav -- best RMS
		filename = "../src_wavs/drumkit.wav";
		compressor.EnableAdaptiveByteBufferizer(true);
		compressor.EnableHuffmanAdaptiveEncoding(true);
		compressor.SetNumberOfSubBands(8);
		compressor.SetFilterBankLength(4096);
		compressor.SetSignalWindowSize(4096);
		compressor.SetPsychoAcousticModelFrequencies(0, 0, 0);
		comp = compressor.compress(new AudioFile(filename), "tmp.jet");
		compressor.decompress(comp, "../output_wavs/drumkit_bestRMS.wav");
		
		
		// drumkit.wav -- best compression
		filename = "../src_wavs/drumkit.wav";
		compressor.EnableAdaptiveByteBufferizer(false);
		compressor.EnableHuffmanAdaptiveEncoding(false);
		compressor.SetNumberOfSubBands(2);
		compressor.SetFilterBankLength(256);
		compressor.SetSignalWindowSize(256);
		compressor.SetPsychoAcousticModelFrequencies(0, 200, 10000);
		comp = compressor.compress(new AudioFile(filename), "tmp.jet");
		compressor.decompress(comp, "../output_wavs/drumkit_bestCompression.wav");
		
		// drumkit.wav -- fastest compression time
		filename = "../src_wavs/drumkit.wav";
		compressor.EnableAdaptiveByteBufferizer(false);
		compressor.EnableHuffmanAdaptiveEncoding(false);
		compressor.SetNumberOfSubBands(16);
		compressor.SetFilterBankLength(256);
		compressor.SetSignalWindowSize(4096);
		compressor.SetPsychoAcousticModelFrequencies(0, 300, 8000);
		comp = compressor.compress(new AudioFile(filename), "tmp.jet");
		compressor.decompress(comp, "../output_wavs/drumkit_bestCompTime.wav");
		
		// drumkit.wav -- fastest decompression time
		filename = "../src_wavs/drumkit.wav";
		compressor.EnableAdaptiveByteBufferizer(false);
		compressor.EnableHuffmanAdaptiveEncoding(false);
		compressor.SetNumberOfSubBands(32);
		compressor.SetFilterBankLength(256);
		compressor.SetSignalWindowSize(4096);
		compressor.SetPsychoAcousticModelFrequencies(0,300, 8000);
		comp = compressor.compress(new AudioFile(filename), "tmp.jet");
		compressor.decompress(comp, "../output_wavs/drumkit_bestDecompTime.wav");
		
		
		
		// delete the temp jet file
		(new File("tmp.jet")).delete();
		
	}
	
}
