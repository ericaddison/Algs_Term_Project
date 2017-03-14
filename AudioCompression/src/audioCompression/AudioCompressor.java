package audioCompression;

import audioCompression.blocks.BitstreamFormatter;
import audioCompression.blocks.FilterBank;
import audioCompression.blocks.HuffmanEncoder;
import audioCompression.blocks.MDCT;
import audioCompression.types.CompressedAudio;
import audioCompression.types.EncodedLines;
import audioCompression.types.Lines;
import audioCompression.types.RawAudio;
import audioCompression.types.Subbands;

public class AudioCompressor {

	private FilterBank filterbank = new FilterBank();
	private MDCT mdct = new MDCT();
	private HuffmanEncoder huffman = new HuffmanEncoder();
	private BitstreamFormatter bitstreamF = new BitstreamFormatter();
	
	public CompressedAudio compress(RawAudio input){
		Subbands subbands = filterbank.forward(input);
		Lines lines = mdct.forward(subbands);
		EncodedLines encLines = huffman.forward(lines);
		CompressedAudio output = bitstreamF.forward(encLines);
		return output;
	}
	
	public RawAudio decompress(CompressedAudio input){
		EncodedLines encLines = bitstreamF.reverse(input);
		Lines lines = huffman.reverse(encLines);
		Subbands subbands = mdct.reverse(lines);
		RawAudio output = filterbank.reverse(subbands);
		return output;
	}
	
}
