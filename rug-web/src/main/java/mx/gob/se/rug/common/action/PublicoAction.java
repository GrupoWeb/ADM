package mx.gob.se.rug.common.action;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Level;

import mx.gob.economia.dgi.framework.struts2.action.AbstractBaseAction;
import mx.gob.se.rug.util.MyLogger;

import org.apache.struts2.ServletActionContext;

@SuppressWarnings("serial")
public class PublicoAction extends AbstractBaseAction {

	private String url;
	private String llave;
	private InputStream fileStream;
	private String contentType;
	private String fileName;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLlave() {
		return llave;
	}

	public void setLlave(String llave) {
		this.llave = llave;
	}

	public void setFileStream(InputStream fileStream) {
		this.fileStream = fileStream;
	}

	public InputStream getFileStream() {
		return fileStream;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentType() {
		return contentType;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public String execute() throws Exception {
		MyLogger.Logger.log(Level.INFO,"publico");
		MyLogger.Logger.log(Level.INFO,"llave: " + llave);
		url = getText(llave);
		MyLogger.Logger.log(Level.INFO,"url: " + url);

		if ((llave == null || url == null) || (llave.equals(url))) {
			logger.error("No existe la pagina que solicito");
			logger.error("llave: " + llave + " - url: " + url);
			return ERROR;
		}
		return SUCCESS;
	}

	public String getFile() throws Exception {
		MyLogger.Logger.log(Level.INFO,"-- getPdf --");
		MyLogger.Logger.log(Level.INFO,"llave: " + llave);
		url = getText(llave);

		if ((llave == null || url == null) || (llave.equals(url))) {
			logger.error("No existe la pagina que solicito");
			logger.error("llave: " + llave + " - url: " + url);
			return ERROR;
		}

		contentType = "application/pdf";
		fileName = getText(url).substring(1);
		MyLogger.Logger.log(Level.INFO,"contentType: " + contentType);
		MyLogger.Logger.log(Level.INFO,"fileName: " + fileName);
		String targetDir = ServletActionContext.getServletContext()
				.getRealPath("/");
		String archivo = targetDir + getText("pdf.path") + getText(url);
		fileStream = new DataInputStream(new FileInputStream(archivo));
		return SUCCESS;
	}

}
