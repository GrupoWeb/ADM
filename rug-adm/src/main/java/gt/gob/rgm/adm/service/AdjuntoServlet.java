package gt.gob.rgm.adm.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gt.gob.rgm.adm.dao.AdjuntoRepository;
import gt.gob.rgm.adm.model.Adjunto;

public class AdjuntoServlet extends HttpServlet {

	@Inject
	private AdjuntoRepository adjuntoDao;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String strId = req.getRequestURI().substring(req.getContextPath().length()).replaceAll("/attachmenti/", "");
		Long id = Long.valueOf(strId);
		
		Adjunto adjuntoBD = adjuntoDao.findById(id);
		
		ByteArrayInputStream in = new ByteArrayInputStream(adjuntoBD.getAdjunto());
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
