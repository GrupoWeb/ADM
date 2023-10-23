package gt.gob.rgm.files.service;

import java.io.IOException;
import java.io.InputStream;

public interface FilesService {
	public String saveFile(InputStream fileStream, String basepath, String filename, Object key, Boolean generateHashName) throws IOException;
}
