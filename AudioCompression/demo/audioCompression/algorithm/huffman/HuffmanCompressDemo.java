package audioCompression.algorithm.huffman;

import audioCompression.types.AudioByteBuffer;
import audioCompression.types.testImpls.AudioByteBufferImpl;
import libs.huffmanEncode.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by taylor on 4/25/2017.
 */
public class HuffmanCompressDemo {

    public static void main(String[] args) {

        //generate audiobytebuffer for input
        AudioByteBufferImpl abb = new AudioByteBufferImpl(1024);

        //byte[] byteArray = bbInput.getBuffer().array();

        //this is the code that will go in HuffmanEncoderStep.forward()
        //modify HuffmanCompress to use the audiobytebuffer

        ByteBuffer bbInput = abb.getBuffer();
        System.out.println(Arrays.toString(bbInput.array()));
        System.out.println("input cap: " + bbInput.capacity() + " input pos: " + bbInput.position() + " input limit: " + bbInput.limit());

        //todo how to estimate size of output buffer?  using size of input breaks for small inputs bc of codelength table
        ByteBuffer bbOutput = ByteBuffer.allocate(bbInput.capacity() + 1028);

        //build the frequency table
        FrequencyTable freqs = getFrequencies(bbInput);
        freqs.increment(256);  // EOF symbol gets a frequency of 1
        CodeTree code = freqs.buildCodeTree();
        CanonicalCode canonCode = new CanonicalCode(code, 257);
        // Replace code tree with canonical one. For each symbol,
        // the code value may change but the code length stays the same.
        code = canonCode.toCodeTree();

        // compress using Huffman coding
        BitOutputStream out = new BitOutputStream(bbOutput);
        try {
            writeCodeLengthTable(out, canonCode);
            compress(code, bbInput, out);
        } finally {
            out.close();
        }

        System.out.println(Arrays.toString(bbOutput.array()));
        System.out.println("output cap: " + bbOutput.capacity() + " output pos: " + bbOutput.position() + " output limit: " + bbOutput.limit());
        System.out.println("decompressing");

        //try to decompress the output
        // Perform file decompression
        BitInputStream in = new BitInputStream(bbOutput);
        ByteBuffer bbInput2 = ByteBuffer.allocate(bbInput.capacity() + 1028);

        try {
            CanonicalCode canonCode2 = readCodeLengthTable(in);
            CodeTree code2 = canonCode2.toCodeTree();
            decompress(code2, in, bbInput2);
        } finally {
            in.close();
        }

        System.out.println(Arrays.toString(bbInput2.array()));
        System.out.println("output cap: " + bbInput2.capacity() + " output pos: " + bbInput2.position() + " output limit: " + bbInput2.limit());

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
            int b = (in.get() & 0xff);
            enc.write(b);
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
            int symbol = in.read();
            if (symbol == 256)  // EOF symbol //todo again, not sure this is necessary since we aren't reading from a file
                break;
            out.put((byte)symbol);
        }
    }
}
