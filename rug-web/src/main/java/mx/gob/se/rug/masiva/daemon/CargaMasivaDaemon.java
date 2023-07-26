package mx.gob.se.rug.masiva.daemon;

import java.io.IOException;
import java.util.logging.Level;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.gob.se.rug.mailservice.MailRegistroService;
import mx.gob.se.rug.util.MyLogger;

public class CargaMasivaDaemon extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	public void init(ServletConfig config) throws ServletException {
		proccesDesatendido();
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processRequest(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processRequest(req, resp);
	}

	private void processRequest(HttpServletRequest req, HttpServletResponse resp) {
		proccesDesatendido();
	}
	
	private void proccesDesatendido(){
		MyLogger.CargaMasivaLog.log(Level.INFO,"Inicia Demonio Desatendido");
		CargaMasivaController cargaMasivaController = new CargaMasivaController();
		cargaMasivaController.start();
	}
}
