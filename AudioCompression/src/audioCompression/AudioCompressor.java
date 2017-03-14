package audioCompression;

import audioCompression.algorithm.BitstreamFormatterStep;
import audioCompression.algorithm.FilterBankStep;
import audioCompression.algorithm.HuffmanEncoderStep;
import audioCompression.algorithm.MdctStep;
import audioCompression.types.CompressedAudio;
import audioCompression.types.EncodedLines;
import audioCompression.types.Lines;
import audioCompression.types.RawAudio;
import audioCompression.types.Subbands;

public class AudioCompressor {

	private FilterBankStep filterbank = new FilterBankStep();
	private MdctStep mdct = new MdctStep();
	private HuffmanEncoderStep huffman = new HuffmanEncoderStep();
	private BitstreamFormatterStep bitstreamF = new BitstreamFormatterStep();
	
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
