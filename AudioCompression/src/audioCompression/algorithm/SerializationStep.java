package audioCompression.algorithm;

import java.nio.ByteBuffer;
import java.util.Random;

import audioCompression.types.AudioByteBuffer;
import audioCompression.types.AudioCompressionType;
import audioCompression.types.CompressedAudioFile;

/**
 * Serialization step for the audio compression pipeline,
 * this step takes in an AudioByteBuffer and writes it to a file
 * and returns a simple Compressed Audio File object, which contains 
 * the name of the file written.  This automatically will write the file
 * to disk, similarly, the reverse function takes in a CompressedAudioFile 
 * object and uses the name of the file given to create an Audio Byte Buffer
 * for decompression
 * 
 * @author Eric Addison, Taylor Autry, Josh Musick
 *
 */

public class SerializationStep implements AlgorithmStep<AudioByteBuffer, CompressedAudioFile> {

	private static final String NAME = "Serializer";
	
	@Override
	public CompressedAudioFile forward(AudioByteBuffer input, String name) {
		// TODO Auto-generated method stub
		
		
		
		
		
		CompressedAudioFile output = new CompressedAudioFile(name);
		
		return output;
	}

	@Override
	public AudioByteBuffer reverse(CompressedAudioFile input, String name) {
		// TODO Auto-generated method stub
		
		String compName = input.GetCompressedFilename();
		
		// load the file and pull out the byte buffer stream... 		
		
		int n = 100;
		AudioByteBuffer buffer = new AudioByteBuffer(ByteBuffer.allocate(n*(Integer.SIZE/Byte.SIZE)));
		Random r = new Random(System.currentTimeMillis()); 
		for(int i=0; i<n; i++)
			buffer.getBuffer().putInt(r.nextInt());
		
		buffer.getBuffer().rewind();
		return buffer;
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
		return CompressedAudioFile.class;
	}

}
