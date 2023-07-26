package gt.gob.rgm.adm.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gt.gob.rgm.adm.util.Constants;

public class FilesServlet extends HttpServlet {
	@Inject
	RugParametroConfService parametroService;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String imagePath = req.getRequestURI().substring(req.getContextPath().length());
		
		// obtener carpeta configurada en parametros
		String carpeta = parametroService.getParam(Constants.FS_BASE_PATH).getValorParametro();
		
		File file = new File(carpeta + imagePath.replace("attachment/", "/"));
		FileInputStream in = new FileInputStream(file);
		OutputStream out = res.getOutputStream();
		
		byte[] buf = new byte[1024];
		int count = 0;
		while((count = in.read(buf)) >= 0) {
			out.write(buf, 0, count);
		}
		out.flush();
		out.close();
	}

}
