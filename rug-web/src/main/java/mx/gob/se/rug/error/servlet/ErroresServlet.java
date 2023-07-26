/*
 * ErroresServlet.java        11/11/2009
 *
 * Copyright (c) 2009 Secretaría de Economía
 * Alfonso Reyes No. 30 Col. Hipódromo Condesa C.P. 06140,
 * Delegación Cuauhtémoc, México, D.F.
 * Todos los Derechos Reservados.
 *
 * Este software es confidencial y de uso exclusivo de la
 * Secretaría de Economía.
 *
 */
package mx.gob.se.rug.error.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.gob.economia.dgi.framework.web.servlet.AbstractBaseServlet;

/**
 * @author Alfonso Esquivel
 * 
 */
@SuppressWarnings("serial")
public class ErroresServlet extends AbstractBaseServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		logger.info("doGet");
		// obtengo parametro del codigo de error
		String stringCode = (String) req.getParameter("code");
		int code = Integer.parseInt(stringCode);

		// String nameSpace = "/httpError/";
		String nameSpace = "/home";
		String path = req.getContextPath() + nameSpace;

		switch (code) {
		case 400:
		case 408:
		case 406:
		case 414:
			path += "/badRequestHandler.do";
			break;
		case 404:
			path += "/inicio.do";
			break;
		case 407:
		case 403:
			path += "/authorizationHandler.do";
			break;
		case 500:
		case 501:
		case 503:
			path += "/serverErrorHandler.do";
			break;
		case 502:
		case 504:
			path += "/gatewayServerHandler.do";
			break;
		default:
			path += "/badRequestHandler.do";
			break;
		}

		logger.info("stringCode: " + stringCode);
		logger.info("code: " + code);

		// redirect
		String url = resp.encodeRedirectURL(path);
		logger.info("url: " + url);
		resp.sendRedirect(url);

		logger.info("termino doGet");
		return;
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestUri = (String) req.getAttribute("javax.servlet.error.request_uri");
		String url = resp.encodeRedirectURL(requestUri);
		logger.info("---------------url: " + url);
		if(url.endsWith("j_security_check")) {
			String path = req.getContextPath() + "/home/login.do?error=true";
			resp.sendRedirect(path);
		}
		doGet(req, resp);
	}

}
