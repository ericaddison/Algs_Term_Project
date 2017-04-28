package audioCompression.algorithm;

import audioCompression.algorithm.huffman.*;
import audioCompression.types.AudioByteBuffer;
import audioCompression.types.AudioCompressionType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class HuffmanEncoderStep implements AlgorithmStep<AudioByteBuffer, AudioByteBuffer>  {

	private static final String NAME = "Huffman Encoder";

	//todo make the adaptive stuff work
    protected boolean adaptive = false;

    public boolean isAdaptive() {
        return adaptive;
    }

    public void setAdaptive(boolean adaptive) {
        this.adaptive = adaptive;
    }


    @Override
	public AudioByteBuffer forward(AudioByteBuffer input, String name) {

        ByteBuffer bbInput = input.getBuffer();
        ByteBuffer bbOutput = ByteBuffer.allocate(bbInput.capacity() + 1028);
        BitOutputStream out = new BitOutputStream(bbOutput);

        if (adaptive) {
            try {
                compress(bbInput, out);
            } finally {
                out.close();
            }
        }
        else {
            //build the frequency table
            FrequencyTable freqs = getFrequencies(bbInput);
            freqs.increment(256);  // EOF symbol gets a frequency of 1
            CodeTree code = freqs.buildCodeTree();
            CanonicalCode canonCode = new CanonicalCode(code, 257);
            // Replace code tree with canonical one. For each symbol,
            // the code value may change but the code length stays the same.
            code = canonCode.toCodeTree();
            // compress using Huffman coding
            try {
                writeCodeLengthTable(out, canonCode);
                compress(code, bbInput, out);
            } finally {
                out.close();
            }
        }

        AudioByteBuffer abb = new AudioByteBuffer(bbOutput);
     	return abb;

	}

	@Override
	public AudioByteBuffer reverse(AudioByteBuffer input, String name) {

        BitInputStream in = new BitInputStream(input.getBuffer());
        ByteBuffer bbOutput = ByteBuffer.allocate(input.getBuffer().capacity());

        if (adaptive) {
            try {
                decompress(in, bbOutput);
            } finally {
                in.close();
                bbOutput.flip();
            }

        }
        else {
            try {
                CanonicalCode canonCode = readCodeLengthTable(in);
                CodeTree code = canonCode.toCodeTree();
                decompress(code, in, bbOutput);
            } finally {
                in.close();
                bbOutput.flip();
            }
        }

        AudioByteBuffer abb = new AudioByteBuffer(bbOutput);
        return abb;

    }

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Class<? extends AudioCompressionType> getInputClass() {
		return AudioByteBuffer.class;
	}

	@Override
	public Class<? extends AudioCompressionType> getOutputClass() {
		return AudioByteBuffer.class;
	}

    // Returns a frequency table based on the bytes in the buffer.
    // Also contains an extra entry for symbol 256, whose frequency is set to 0.
    private static FrequencyTable getFrequencies(ByteBuffer bb) {
        FrequencyTable freqs = new FrequencyTable(new int[257]);

        try {
            while (bb.hasRemaining()) {
                freqs.increment(Byte.toUnsignedInt(bb.get()));
            }
        } finally {
            bb.rewind();
        }
        return freqs;
    }

    static void writeCodeLengthTable(BitOutputStream out, CanonicalCode canonCode) {
        for (int i = 0; i < canonCode.getSymbolLimit(); i++) {
            int val = canonCode.getCodeLength(i);
            // we only support codes up to 255 bits long
            if (val >= 256)
                throw new RuntimeException("The code for a symbol is too long.");

            // Write value as 8 bits in big endian
            for (int j = 7; j >= 0; j--)
                out.write((val >>> j) & 1);
        }
    }

    static void compress(CodeTree code, ByteBuffer in, BitOutputStream out) {
        HuffmanEncoder enc = new HuffmanEncoder(out);
        enc.codeTree = code;
        while (in.hasRemaining()) {
            int b = Byte.toUnsignedInt(in.get());
            enc.write(b);
        }
        enc.write(256);  // EOF
    }

    //overloaded compress method for adaptive call
    static void compress(ByteBuffer in, BitOutputStream out) {
        int[] initFreqs = new int[257];
        Arrays.fill(initFreqs, 1);

        FrequencyTable freqs = new FrequencyTable(initFreqs);
        HuffmanEncoder enc = new HuffmanEncoder(out);
        enc.codeTree = freqs.buildCodeTree();  // Don't need to make canonical code because we don't transmit the code tree
        int count = 0;  // Number of bytes read from the input file
        while (in.hasRemaining()) {
            // Read and encode one byte
            int b = Byte.toUnsignedInt(in.get());
            enc.write(b);
            count++;

            // Update the frequency table and possibly the code tree
            freqs.increment(b);
            if (count < 262144 && isPowerOf2(count) || count % 262144 == 0)  // Update code tree
                enc.codeTree = freqs.buildCodeTree();
            if (count % 262144 == 0)  // Reset frequency table
                freqs = new FrequencyTable(initFreqs);
        }
        enc.write(256);  // EOF
    }

    //decompress functions
    static CanonicalCode readCodeLengthTable(BitInputStream in) {
        int[] codeLengths = new int[257];
        for (int i = 0; i < codeLengths.length; i++) {
            // For this file format, we read 8 bits in big endian
            int val = 0;
            for (int j = 0; j < 8; j++)
                val = (val << 1) | in.readNoEof();
            codeLengths[i] = val;
        }
        return new CanonicalCode(codeLengths);
    }

    static void decompress(CodeTree code, BitInputStream in, ByteBuffer out) {
        HuffmanDecoder dec = new HuffmanDecoder(in);
        dec.codeTree = code;
        while (in.input.hasRemaining()) {
            int symbol = dec.read();
            if (symbol == 256)  // EOF symbol
                break;
            out.put((byte)symbol);
        }
    }

    //overloaded decompress method for adaptive call
    static void decompress(BitInputStream in, ByteBuffer out) {
        int[] initFreqs = new int[257];
        Arrays.fill(initFreqs, 1);

        FrequencyTable freqs = new FrequencyTable(initFreqs);
        HuffmanDecoder dec = new HuffmanDecoder(in);
        dec.codeTree = freqs.buildCodeTree();  // Use same algorithm as the compressor
        int count = 0;  // Number of bytes written to the output file
        while (in.input.hasRemaining()) {
            // Decode and write one byte
            int symbol = dec.read();
            if (symbol == 256)  // EOF symbol
                break;
            out.put((byte)symbol);
            count++;

            // Update the frequency table and possibly the code tree
            freqs.increment(symbol);
            if (count < 262144 && isPowerOf2(count) || count % 262144 == 0)  // Update code tree
                dec.codeTree = freqs.buildCodeTree();
            if (count % 262144 == 0)  // Reset frequency table
                freqs = new FrequencyTable(initFreqs);
        }
    }

    //helper functions
    private static boolean isPowerOf2(int x) {
        return x > 0 && Integer.bitCount(x) == 1;
    }
}
