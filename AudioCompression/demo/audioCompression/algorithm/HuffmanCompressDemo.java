package audioCompression.algorithm;

import audioCompression.algorithm.HuffmanEncoderStep;
import audioCompression.types.AudioByteBuffer;
import audioCompression.types.testImpls.AudioByteBufferImpl;
import java.util.Arrays;

/**
 * Created by taylor on 4/25/2017.
 */
public class HuffmanCompressDemo {

    public static void main(String[] args) {

        //generate audiobytebuffer for input
        AudioByteBufferImpl abbSource = new AudioByteBufferImpl(1024);

        System.out.println(Arrays.toString(abbSource.getBuffer().array()));
        System.out.println("input cap: " + abbSource.getBuffer().capacity() + " input pos: " + abbSource.getBuffer().position() + " input limit: " + abbSource.getBuffer().limit());

        HuffmanEncoderStep enc = new HuffmanEncoderStep();
        //enc.setAdaptive(true);
        AudioByteBuffer abbCompressed = enc.forward(abbSource, "whatever");

        System.out.println(Arrays.toString(abbCompressed.getBuffer().array()));
        System.out.println("output cap: " + abbCompressed.getBuffer().capacity() + " output pos: " + abbCompressed.getBuffer().position() + " output limit: " + abbCompressed.getBuffer().limit());
        System.out.println("decompressing");

        AudioByteBuffer abbDecompressed = enc.reverse(abbCompressed, "whatever");

        System.out.println(Arrays.toString(abbDecompressed.getBuffer().array()));
        System.out.println("output cap: " + abbDecompressed.getBuffer().capacity() + " output pos: " + abbDecompressed.getBuffer().position() + " output limit: " + abbDecompressed.getBuffer().limit());

    }


}
