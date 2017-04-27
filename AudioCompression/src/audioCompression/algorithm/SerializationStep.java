package audioCompression.algorithm;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

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
	private int compressFileSize = 0;
	
	@Override
	public CompressedAudioFile forward(AudioByteBuffer input, String name) {
		// TODO Auto-generated method stub
		
		//long fileSize = 0;
		int fileSize = 0;
		try {
			File file = new File(name);
			FileOutputStream strm = new FileOutputStream(file, false); 
			FileChannel channel = strm.getChannel();
			fileSize = input.getBuffer().remaining();
			
			compressFileSize = fileSize;
			
			// Writes a sequence of bytes to this channel from the given buffer.
			channel.write(input.getBuffer());
			// close the channel
			channel.close();
			strm.close();		
		}
		catch (IOException exc) {
			System.out.println(exc);
            System.exit(1);
		}	
		
		return new CompressedAudioFile(name, fileSize);
	}

	@Override
	public AudioByteBuffer reverse(CompressedAudioFile input, String name) {
		
		String compName = input.GetCompressedFilename();	
		try {
			// load the file and pull out the byte buffer stream... 
			
			RandomAccessFile aFile = new RandomAccessFile(compName, "r");	
			FileChannel inChannel = aFile.getChannel();
			
			long fileSize = inChannel.size();			
			ByteBuffer buffer = ByteBuffer.allocate((int)fileSize);
			
			// read in the data to the buffer
			inChannel.read(buffer);
			buffer.rewind();		
			
			AudioByteBuffer abuff = new AudioByteBuffer(buffer);
		
			inChannel.close();
			aFile.close();
			
			return abuff;
		}
		catch(IOException exc)
		{
			System.out.println(exc);
            System.exit(1);
		}
		return null;
	}
	
	public int getCompressFileSize() {
		return compressFileSize;
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
