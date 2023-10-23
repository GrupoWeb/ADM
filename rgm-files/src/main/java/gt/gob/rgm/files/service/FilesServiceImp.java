package gt.gob.rgm.files.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import gt.gob.rgm.files.utils.HashUtils;

public class FilesServiceImp implements FilesService {

	public String saveFile(InputStream fileStream, String basepath, String filename, Object key, Boolean generateHashName) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String folder = generateHashName ? HashUtils.hashString(sdf.format(new Date()) + "_" + key) : "";
		String extension = filename.lastIndexOf(".") > -1 ? filename.substring(filename.lastIndexOf(".")) : "";
		String generatedFilename = generateHashName ? HashUtils.hashString(key + "_" + filename) + extension : filename;
		
		File carpetafs = new File(basepath + folder);
		if(!carpetafs.exists()) {
			carpetafs.mkdirs();
		}
        OutputStream out = new FileOutputStream(new File(basepath + folder + "/" + generatedFilename));
        int read = 0;
        byte[] bytes = new byte[1024];

        while((read = fileStream.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
        out.flush();
        out.close();
        fileStream.close();
        
        String resultado = folder + "/" + generatedFilename;
        
        return resultado;
	}
}
