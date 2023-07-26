package mx.gob.se.rug.certificacion.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileUtil {
	
	public String fileToString(String path) {
		File file = new File(path);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;
		StringBuffer sb = new StringBuffer();
		try {
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);
			while (dis.available() != 0) {
				sb.append(dis.readLine());
			}
			fis.close();
			bis.close();
			dis.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	public String acentosTildesHtml(String html) {
		html = html.replace("&aacute;", "á");
		html = html.replace("&eacute;", "é");
		html = html.replace("&iacute;", "í");
		html = html.replace("&oacute;", "ó");
		html = html.replace("&uacute;", "ú");
		html = html.replace("&ntilde;", "ñ");
		html = html.replace("&nbsp;", " ");
		return html;
	}
}
