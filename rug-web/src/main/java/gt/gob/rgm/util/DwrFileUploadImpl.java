package gt.gob.rgm.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.dwrp.FileUpload;
import org.directwebremoting.extend.FormField;
import org.directwebremoting.extend.ServerException;

public class DwrFileUploadImpl implements FileUpload {

	@Override
	public Map<String, FormField> parseRequest(HttpServletRequest arg0) throws ServerException {
		// TODO Auto-generated method stub
		System.out.println("Entre a la implementacion");
		return null;
	}

}
