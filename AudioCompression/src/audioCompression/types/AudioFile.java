package audioCompression.types;

public class AudioFile implements AudioCompressionType {

	private String filename;
	
	public AudioFile(String filename) {
		super();
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

}
