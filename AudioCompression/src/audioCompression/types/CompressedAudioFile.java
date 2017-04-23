package audioCompression.types;

/**
 * Output file from the compression pipeline, and input file to the 
 * decompression pipeline.  This file stores no data, only a filename
 * which was created (on compress) or a filename to be decompressed
 * 
 * @author Eric Addison, Taylor Autry, Josh Musick
 *
 */

public class CompressedAudioFile implements AudioCompressionType {
	
	String m_compressedFilename;
	int m_compressedSizeBytes;

	public CompressedAudioFile(String filename, int compressedSize) {
		m_compressedFilename = filename;
		m_compressedSizeBytes = compressedSize;
	}
	
	/**
	 * Filename which was data was written to, or filename which should be 
	 * loaded from for decompression
	 */
	public String GetCompressedFilename() { return m_compressedFilename; }
	
}
