package mx.gob.se.rug.masiva.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.gob.se.rug.masiva.dao.ArchivoDAO;
import mx.gob.se.rug.util.MyLogger;

/**
 * Servlet implementation class DescargaArchivoXML
 */
public class DescargaArchivoXML extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DescargaArchivoXML() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		MyLogger.Logger.log(Level.INFO, "idArchivo:::"+request.getParameter("idArchivo"));
		try {
			if (request.getParameter("idArchivo") != null) {
				Integer idFirmaMasiva = Integer.valueOf(request
						.getParameter("idArchivo"));
				ArchivoDAO archivoDAO = new ArchivoDAO();
				byte[] bytes = archivoDAO.getBytesToIdFirma(idFirmaMasiva);
				response.setContentType("text/xml");
				response.setContentLength(bytes.length);
				ServletOutputStream ouputStream = response.getOutputStream();
				response.addHeader("content-disposition",
						"attachment; filename=ResultadoCargaMasiva.xml");
				ouputStream.write(bytes, 0, bytes.length);
				ouputStream.flush();
				ouputStream.close();	
			} else {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println("<html>");
				out.println("<body>");
				out.println("<h1>No estas autorizado para acceder a esta pagina</h1>");
				out.println("</body>");
				out.println("</html>");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<body>");
			out.println("<h1>No estas autorizado para acceder a esta pagina</h1>");
			out.println("<p style=\"color:white;\"> "+stack2string(e)+" </p>");
			out.println("</body>");
			out.println("</html>");

		}

	}

	public String stack2string(Exception e) {
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return "------\r\n" + sw.toString() + "------\r\n";
		} catch (Exception e2) {
			return "bad stack2string";
		}
	}

}
