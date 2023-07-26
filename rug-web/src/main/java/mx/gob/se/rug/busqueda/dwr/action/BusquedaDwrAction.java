package mx.gob.se.rug.busqueda.dwr.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import mx.gob.economia.dgi.framework.dwr.action.AbstractBaseDwrAction;
import mx.gob.se.rug.busqueda.dao.BusquedaDAO;
import mx.gob.se.rug.busqueda.to.BusquedaTO;
import mx.gob.se.rug.common.util.NullsFree;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.detallegarantia.service.impl.DetalleServiceImpl;
import mx.gob.se.rug.detallegarantia.to.DetalleTO;
import mx.gob.se.rug.inscripcion.service.InscripcionService;
import mx.gob.se.rug.inscripcion.service.impl.InscripcionServiceImpl;
import mx.gob.se.rug.to.MessageDwr;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;
import mx.gob.se.rug.util.pdf.to.PdfTO;

public class BusquedaDwrAction extends AbstractBaseDwrAction {

	public MessageDwr searchInvoice2(String invoice, String serial, String idPersona, String idTipoTramite, String ruta,
			String nit, String consultaNombre, String consultaId) {
		BusquedaTO searchTO = new BusquedaTO();

		searchTO.setInvoice(invoice.trim());
		searchTO.setSet(serial.trim());
		searchTO.setNit(nit);
		searchTO.setIdPersona(idPersona);
		searchTO.setIdTipoTramite(idTipoTramite);

		searchTO.setIdGarantia("");
		searchTO.setIdTramite("");
		searchTO.setFolioMercantil("");
		searchTO.setNombre("");
		searchTO.setDescGarantia("");
		searchTO.setCurpOtorgante("");
		searchTO.setRfcOtorgante("");

		searchTO.setNoSerial("");

		MyLogger.Logger.log(Level.INFO, "Metodo Search Independiente");

		StringBuffer sb = new StringBuffer();
		BusquedaDAO busquedaDAO = new BusquedaDAO();
		InscripcionService inscripcionService = new InscripcionServiceImpl();
		MessageDwr dwrTO = new MessageDwr();
		dwrTO.setCodeError(0);
		try {
			Integer start = 1;
			Integer finish = 20;

			MyLogger.Logger.log(Level.INFO, "Usuario " + (Integer.parseInt(idPersona) == 17381));
			if (Integer.parseInt(idPersona) == 17381) {
				List<BusquedaTO> busquedaGeneral = busquedaDAO.searchInvoice2(searchTO, start, finish, consultaNombre,
						consultaId);
				int pagActiva = Integer.valueOf(1);
				int regPagina = Integer.valueOf(20);

				int registroTotales = 0;

				if (!busquedaGeneral.isEmpty())
					registroTotales = busquedaGeneral.get(0).getNumReg(); // searchTO.getNumReg();

				System.out.println("NUMERO DE REGISTROS 1:::: " + registroTotales);
				int numeroPaginas = registroTotales / regPagina;
				if (registroTotales % regPagina > 0) {
					numeroPaginas++;
				}
				if (numeroPaginas < pagActiva) {
					pagActiva = 1;
				}
				if (pagActiva != 1) {
					start = ((pagActiva - 1) * regPagina) + 1;
				}

				sb.append(tableSearch(searchTO, busquedaGeneral, registroTotales, ruta));
				sb.append(writeSeccionPaginado(numeroPaginas, 1, 20, registroTotales, "pagBusquedaDwr", ""));
				sb.append("<div class=\"row\">");
				sb.append("<p><span>Para descargar la boleta dar click en el siguiente bot&oacute;n :</span></p>");
				sb.append(
						"<input type=\"button\"class=\"btn btn-large waves-effect indigo\" value=\"Descargar PDF \" onclick=\"showBoleta();\"  />");
				sb.append("</div>");
			} else if (1 == 1) {
				List<BusquedaTO> busquedaGeneral = busquedaDAO.searchInvoice2(searchTO, start, finish, consultaNombre,
						consultaId);
				int pagActiva = Integer.valueOf(1);
				int regPagina = Integer.valueOf(20);

				int registroTotales = 0;

				if (!busquedaGeneral.isEmpty())
					registroTotales = busquedaGeneral.get(0).getNumReg(); // searchTO.getNumReg();

				System.out.println("NUMERO DE REGISTROS 2:::: " + registroTotales);
				int numeroPaginas = registroTotales / regPagina;
				if (registroTotales % regPagina > 0) {
					numeroPaginas++;
				}
				if (numeroPaginas < pagActiva) {
					pagActiva = 1;
				}
				if (pagActiva != 1) {
					start = ((pagActiva - 1) * regPagina) + 1;
				}

				sb.append(tableSearchAnonima(searchTO, busquedaGeneral, registroTotales, ruta));
				sb.append(writeSeccionPaginado(numeroPaginas, 1, 20, registroTotales, "pagBusquedaDwr", ""));
				/*
				 * sb.append("<div class=\"row\">");
				 * sb.
				 * append("<p><span>Para descargar la boleta dar click en el siguiente bot&oacute;n :</span></p>"
				 * );
				 * sb.
				 * append("<input type=\"button\"class=\"btn btn-large waves-effect indigo\" value=\"Descargar PDF \" onclick=\"showBoleta();\"  />"
				 * );
				 * sb.append("</div>");
				 */
			} else {
				System.out.println("NUMERO DE REGISTROS 3:::: ");
				sb.append("<div class=\"row\">");
				sb.append(
						"<p><span class=\"red-text text-darken-4\">No tiene saldo para realizar la operaci&oacute;n</span></p>");
				sb.append("</div>");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		dwrTO.setMessage(sb.toString());
		return dwrTO;
	}

	public MessageDwr searchInvoice(String invoice, String serial, String idPersona, String idTipoTramite, String ruta,
			String nit) {
		BusquedaTO searchTO = new BusquedaTO();

		searchTO.setInvoice(invoice.trim());
		searchTO.setSet(serial.trim());
		searchTO.setNit(nit);
		searchTO.setIdPersona(idPersona);
		searchTO.setIdTipoTramite(idTipoTramite);

		searchTO.setIdGarantia("");
		searchTO.setIdTramite("");
		searchTO.setFolioMercantil("");
		searchTO.setNombre("");
		searchTO.setDescGarantia("");
		searchTO.setCurpOtorgante("");
		searchTO.setRfcOtorgante("");

		searchTO.setNoSerial("");

		MyLogger.Logger.log(Level.INFO, "Metodo Search Independiente");

		StringBuffer sb = new StringBuffer();
		BusquedaDAO busquedaDAO = new BusquedaDAO();
		InscripcionService inscripcionService = new InscripcionServiceImpl();
		MessageDwr dwrTO = new MessageDwr();
		dwrTO.setCodeError(0);
		try {
			Integer start = 1;
			Integer finish = 20;

			MyLogger.Logger.log(Level.INFO, "Usuario " + (Integer.parseInt(idPersona) == 17381));
			if (Integer.parseInt(idPersona) == 17381) {
				List<BusquedaTO> busquedaGeneral = busquedaDAO.searchInvoice(searchTO, start, finish);
				int pagActiva = Integer.valueOf(1);
				int regPagina = Integer.valueOf(20);

				int registroTotales = 0;

				if (!busquedaGeneral.isEmpty())
					registroTotales = busquedaGeneral.get(0).getNumReg(); // searchTO.getNumReg();

				System.out.println("NUMERO DE REGISTROS 4:::: " + registroTotales);
				int numeroPaginas = registroTotales / regPagina;
				if (registroTotales % regPagina > 0) {
					numeroPaginas++;
				}
				if (numeroPaginas < pagActiva) {
					pagActiva = 1;
				}
				if (pagActiva != 1) {
					start = ((pagActiva - 1) * regPagina) + 1;
				}

				sb.append(tableSearch(searchTO, busquedaGeneral, registroTotales, ruta));
				sb.append(writeSeccionPaginado(numeroPaginas, 1, 20, registroTotales, "pagBusquedaDwr", ""));
				sb.append("<div class=\"row\">");
				sb.append("<p><span>Para descargar la boleta dar click en el siguiente bot&oacute;n :</span></p>");
				sb.append(
						"<input type=\"button\"class=\"btn btn-large waves-effect indigo\" value=\"Descargar PDF \" onclick=\"showBoleta();\"  />");
				sb.append("</div>");
			} else if (inscripcionService.getSaldoByUsuario(idPersona, Integer.valueOf(idTipoTramite), 0, 0)) {
				List<BusquedaTO> busquedaGeneral = busquedaDAO.searchInvoice(searchTO, start, finish);
				int pagActiva = Integer.valueOf(1);
				int regPagina = Integer.valueOf(20);

				int registroTotales = 0;

				if (!busquedaGeneral.isEmpty())
					registroTotales = busquedaGeneral.get(0).getNumReg(); // searchTO.getNumReg();

				System.out.println("NUMERO DE REGISTROS 5:::: " + registroTotales);
				int numeroPaginas = registroTotales / regPagina;
				if (registroTotales % regPagina > 0) {
					numeroPaginas++;
				}
				if (numeroPaginas < pagActiva) {
					pagActiva = 1;
				}
				if (pagActiva != 1) {
					start = ((pagActiva - 1) * regPagina) + 1;
				}

				sb.append(tableSearch(searchTO, busquedaGeneral, registroTotales, ruta));
				sb.append(writeSeccionPaginado(numeroPaginas, 1, 20, registroTotales, "pagBusquedaDwr", ""));
				sb.append("<div class=\"row\">");
				sb.append("<p><span>Para descargar la boleta dar click en el siguiente bot&oacute;n :</span></p>");
				sb.append(
						"<input type=\"button\"class=\"btn btn-large waves-effect indigo\" value=\"Descargar PDF \" onclick=\"showBoleta();\"  />");
				sb.append("</div>");
			} else {
				System.out.println("NUMERO DE REGISTROS 6:::: ");
				sb.append("<div class=\"row\">");
				sb.append(
						"<p><span class=\"red-text text-darken-4\">No tiene saldo para realizar la operaci&oacute;n</span></p>");
				sb.append("</div>");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		dwrTO.setMessage(sb.toString());
		return dwrTO;
	}

	public MessageDwr buscar(String idPersona, String noSerial, String idGarantia, String nombre, String folioMercantil,
			String descGarantia, String curpOtorgante, String rfcOtorgante, String ruta, String idTipoTramite) {
		BusquedaTO busquedaInTO = new BusquedaTO();
		busquedaInTO.setIdGarantia(idGarantia.trim());
		busquedaInTO.setIdTramite(idGarantia.trim());
		busquedaInTO.setFolioMercantil(folioMercantil.trim());
		busquedaInTO.setNombre(nombre.trim());
		busquedaInTO.setDescGarantia(descGarantia.trim());
		busquedaInTO.setCurpOtorgante(curpOtorgante.trim());
		busquedaInTO.setRfcOtorgante(rfcOtorgante.trim());
		busquedaInTO.setIdPersona(idPersona);
		busquedaInTO.setNoSerial(noSerial);
		busquedaInTO.setIdTipoTramite(idTipoTramite);

		StringBuffer sb = new StringBuffer();
		BusquedaDAO busquedaDAO = new BusquedaDAO();
		InscripcionService inscripcionService = new InscripcionServiceImpl();
		MessageDwr dwrTO = new MessageDwr();
		dwrTO.setCodeError(new Integer(0));
		try {
			Integer inicio = 1;
			Integer fin = 100;

			if (inscripcionService.getSaldoByUsuario(idPersona, Integer.valueOf(idTipoTramite), 0,0)) {
				List<BusquedaTO> busquedaGeneral = busquedaDAO.busqueda(busquedaInTO, inicio, fin, 0);
				int pagActiva = Integer.valueOf(1);
				int regPagina = Integer.valueOf(20);
				int registroTotales = busquedaInTO.getNumReg();
				System.out.println("NUMERO DE REGISTROS 7:::: " + registroTotales);
				int numeroPaginas = registroTotales / regPagina;
				if (registroTotales % regPagina > 0) {
					numeroPaginas++;
				}
				if (numeroPaginas < pagActiva) {
					pagActiva = 1;
				}
				if (pagActiva != 1) {
					inicio = ((pagActiva - 1) * regPagina) + 1;
				}

				sb.append(tableSearch(busquedaInTO, busquedaGeneral, registroTotales, ruta));
				sb.append(writeSeccionPaginado(numeroPaginas, 1, 20, registroTotales, "pagBusquedaDwr", ""));
				sb.append("<div class=\"row\">");
				sb.append("<p><span>Para descargar la boleta dar click en el siguiente bot&oacute;n :</span></p>");
				sb.append(
						"<input type=\"button\"class=\"btn btn-large waves-effect indigo\" value=\"Descargar PDF \" onclick=\"showBoleta();\"  />");
				sb.append("</div>");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		dwrTO.setMessage(sb.toString());
		return dwrTO;
	}

	public MessageDwr buscarSinSaldo(String idPersona, String noSerial, String idGarantia, String nombre,
			String folioMercantil, String descGarantia, String curpOtorgante, String rfcOtorgante,
			String ruta, String idTipoTramite, String consultaNombre, String consultaId) {
		BusquedaTO busquedaInTO = new BusquedaTO();
		busquedaInTO.setIdGarantia(idGarantia.trim());
		busquedaInTO.setIdTramite(idGarantia.trim());
		busquedaInTO.setFolioMercantil(folioMercantil.trim());
		busquedaInTO.setNombre(nombre.trim());
		busquedaInTO.setDescGarantia(descGarantia.trim());
		busquedaInTO.setCurpOtorgante(curpOtorgante.trim());
		busquedaInTO.setRfcOtorgante(rfcOtorgante.trim());
		busquedaInTO.setIdPersona(idPersona);
		busquedaInTO.setNoSerial(noSerial);
		busquedaInTO.setIdTipoTramite(idTipoTramite);

		// System.out.println("Info de envio: " +
		// " Persona " + idPersona +
		// " Serial" + noSerial +
		// " Garantia " + idGarantia +
		// " Nombre " + nombre +
		// " Mercantil " + folioMercantil +
		// "Desc Garantia " + descGarantia +
		// "Otorgante " + curpOtorgante +
		// "Rf Otorgante " + rfcOtorgante +
		// " Ruta " + ruta +
		// " Tipo Tramite " +idTipoTramite);

		StringBuffer sb = new StringBuffer();
		BusquedaDAO busquedaDAO = new BusquedaDAO();
		InscripcionService inscripcionService = new InscripcionServiceImpl();
		MessageDwr dwrTO = new MessageDwr();
		dwrTO.setCodeError(new Integer(0));
		try {
			Integer inicio = 1;
			Integer fin = 20;
			MyLogger.Logger.log(Level.INFO, "saldo " + idPersona + " garantia " + idGarantia);
			// if(inscripcionService.getSaldoByUsuario(idPersona,Integer.valueOf(idTipoTramite),0))
			// {
			if (1 == 1) {

				List<BusquedaTO> busquedaGeneral = busquedaDAO.busquedaSinSaldo(busquedaInTO, inicio, fin,
						consultaNombre, consultaId);
				int pagActiva = Integer.valueOf(1);
				int regPagina = Integer.valueOf(20);
				int registroTotales = busquedaInTO.getNumReg();
				System.out.println("NUMERO DE REGISTROS 8:::: " + registroTotales);
				int numeroPaginas = registroTotales / regPagina;
				if (registroTotales % regPagina > 0) {
					numeroPaginas++;
				}
				if (numeroPaginas < pagActiva) {
					pagActiva = 1;
				}
				if (pagActiva != 1) {
					inicio = ((pagActiva - 1) * regPagina) + 1;
				}

				sb.append(tableSearchAnonima(busquedaInTO, busquedaGeneral, registroTotales, ruta));
				// corellana: esto es nuevo==> para que no salga el pdf en el pagineo
				sb.append(writeSeccionPaginado(numeroPaginas, 1, 20, registroTotales, "pagBusquedaSinCostoDwr", ""));

			}
			// if(Integer.parseInt(idPersona) == 51071){
			// MyLogger.Logger.log(Level.INFO,"busqueda Into:" + busquedaInTO + " inicio: "+
			// inicio + " fin:" + fin);
			// List<BusquedaTO> busquedaGeneral = busquedaDAO.busqueda(busquedaInTO, inicio,
			// fin);
			// int pagActiva = Integer.valueOf(1);
			// int regPagina = Integer.valueOf(20);
			// int registroTotales = busquedaInTO.getNumReg();
			// System.out.println("NUMERO DE REGISTROS :::: " + registroTotales);
			// int numeroPaginas = registroTotales/regPagina;
			// if ( registroTotales %regPagina > 0){
			// numeroPaginas++;
			// }
			// if (numeroPaginas < pagActiva){
			// pagActiva = 1;
			// }
			// if (pagActiva != 1){
			// inicio = ((pagActiva-1)*regPagina) + 1;
			// }

			// sb.append(tableSearch(busquedaInTO,busquedaGeneral,registroTotales,ruta));
			// sb.append(writeSeccionPaginado(numeroPaginas, 1, 20,
			// registroTotales,"pagBusquedaDwr",""));
			// sb.append("<div class=\"row\">");
			// sb.append("<p><span>Para descargar la boleta dar click en el siguiente
			// bot&oacute;n :</span></p>");
			// sb.append("<input type=\"button\"class=\"btn btn-large waves-effect indigo\"
			// value=\"Descargar PDF \" onclick=\"showBoleta();\" />");
			// sb.append("</div>");
			// }else
			// if(inscripcionService.getSaldoByUsuario(idPersona,Integer.valueOf(idTipoTramite),0))
			// {

			// List<BusquedaTO> busquedaGeneral = busquedaDAO.busqueda(busquedaInTO, inicio,
			// fin);
			// int pagActiva = Integer.valueOf(1);
			// int regPagina = Integer.valueOf(20);
			// int registroTotales = busquedaInTO.getNumReg();
			// System.out.println("NUMERO DE REGISTROS :::: " + registroTotales);
			// int numeroPaginas = registroTotales/regPagina;
			// if ( registroTotales %regPagina > 0){
			// numeroPaginas++;
			// }
			// if (numeroPaginas < pagActiva){
			// pagActiva = 1;
			// }
			// if (pagActiva != 1){
			// inicio = ((pagActiva-1)*regPagina) + 1;
			// }

			// sb.append(tableSearch(busquedaInTO,busquedaGeneral,registroTotales,ruta));
			// sb.append(writeSeccionPaginado(numeroPaginas, 1, 20,
			// registroTotales,"pagBusquedaDwr",""));
			// sb.append("<div class=\"row\">");
			// sb.append("<p><span>Para descargar la boleta dar click en el siguiente
			// bot&oacute;n :</span></p>");
			// sb.append("<input type=\"button\"class=\"btn btn-large waves-effect indigo\"
			// value=\"Descargar PDF \" onclick=\"showBoleta();\" />");
			// sb.append("</div>");
			// } else {
			// sb.append("<div class=\"row\">");
			// sb.append("<p><span class=\"red-text text-darken-4\">No tiene saldo para
			// realizar la operaci&oacute;n</span></p>");
			// sb.append("</div>");
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
		dwrTO.setMessage(sb.toString());
		return dwrTO;
	}

	public MessageDwr tramites(String idGarantia, String ruta) {
		MessageDwr dwr = new MessageDwr();

		DetalleServiceImpl detserv = new DetalleServiceImpl();

		List<DetalleTO> detalles = detserv.getTramites(new Integer(idGarantia).intValue(), 0);

		StringBuffer sb = new StringBuffer();

		if (detalles.isEmpty()) {
			sb.append("<div style=\" color: red; font-size: 13px;\"> NO SE ENCONTRARON RESULTADOS</div>");
		} else {
			sb.append("<table id=\"mytabledaO\"\r\n" +
					"						class=\"centered striped bordered responsive-table\">\r\n" +
					"						<thead>\r\n" +
					"							<tr>\r\n" +
					"								<th>N&uacute;mero de  Garant&iacute;a</th>\r\n" +
					"								<th>N&uacute;mero de operaci&oacute;n</th>\r\n" +
					"								<th>Tipos de operaci&oacute;n</th>\r\n" +
					"								<th>Fecha de creaci&oacute;n</th>\r\n" +
					"							</tr>\r\n" +
					"						</thead>\r\n" +
					"						<tbody>");

			for (DetalleTO detalle : detalles) {
				sb.append("<tr>");
				sb.append("<td><a href=\"#\" onclick=\"certificacion("
						+ detalle.getIdgarantia() + "," + detalle.getIdTramite()
						+ ");\">");
				sb.append(detalle.getIdgarantia());
				sb.append("</a></td>");
				sb.append("<td>");
				sb.append(detalle.getIdTramite());
				sb.append("</td>");
				sb.append("<td>");
				sb.append(detalle.getDescripcionTramite());
				sb.append("</td>");
				sb.append("<td>");
				sb.append(detalle.getFechaModificacion());
				sb.append("</td>");
				// sb.append("<td><a href=\"#\" onclick=\"download("
				// + detalle.getIdgarantia() + "," + detalle.getIdTramite()
				// + ");\">");
				// sb.append(detalle.getIdgarantia());
				// sb.append("</a></td>");
				sb.append("</tr>");
			}

			sb.append("                     </tbody>\r\n" +
					"</table>");
		}
		dwr.setMessage(sb.toString());
		return dwr;
	}

	public MessageDwr pagBuscar(String idGarantia, String nombre, String folioMercantil, String noSerial,
			String curpOtorgante, String rfcOtorgante, String registroTotalesString,
			String pagActivaString, String regPaginaString, Integer inicio, Integer fin, String ruta) {
		MessageDwr dwr = new MessageDwr();
		BusquedaTO busquedaInTO = new BusquedaTO();
		busquedaInTO.setIdGarantia(idGarantia.trim());
		busquedaInTO.setIdTramite(idGarantia.trim());
		busquedaInTO.setFolioMercantil(folioMercantil.trim());
		busquedaInTO.setNombre(nombre.trim());
		busquedaInTO.setDescGarantia("");
		busquedaInTO.setCurpOtorgante(curpOtorgante.trim());
		busquedaInTO.setRfcOtorgante(rfcOtorgante.trim());
		busquedaInTO.setNoSerial(noSerial);

		UsuarioTO usuario = (UsuarioTO) getSession().getAttribute(Constants.USUARIO);

		if (usuario == null)
			busquedaInTO.setIdPersona("51071");
		else
			busquedaInTO.setIdPersona(String.valueOf(usuario.getPersona().getIdPersona()));

		busquedaInTO.setIdTipoTramite("11");

		MyLogger.Logger.log(Level.INFO, busquedaInTO.getNombre());
		MyLogger.Logger.log(Level.INFO, "Entro al resBusqueda DWR");
		BusquedaDAO busquedaDAO = new BusquedaDAO();
		MessageDwr dwrTO = new MessageDwr();
		dwrTO.setCodeError(new Integer(0));

		int pagActiva = Integer.valueOf(pagActivaString);
		int regPagina = Integer.valueOf(regPaginaString);
		int registroTotales = Integer.valueOf(registroTotalesString);
		int numeroPaginas = registroTotales / regPagina;

		if (registroTotales % regPagina > 0) {
			numeroPaginas++;
		}
		dwr.setCodeError(0);
		if (numeroPaginas < pagActiva) {
			pagActiva = 1;
		}
		try {
			inicio = 1;
			System.out.println("paginaActiva:" + pagActiva);
			System.out.println("registro totales:" + registroTotales);
			if (pagActiva != 1) {
				inicio = ((pagActiva - 1) * regPagina) + 1;
			}
			fin = inicio + (regPagina - 1);

			StringBuffer sb = new StringBuffer();
			sb.append(tableSearch(busquedaInTO, busquedaDAO.busqueda(busquedaInTO, inicio, fin, 1), registroTotales,
					ruta));
			sb.append(writeSeccionPaginado(numeroPaginas, pagActiva, 20, registroTotales, "pagBusquedaDwr", ""));

			sb.append("<div class=\"row\">");
			sb.append("<p><span>Para descargar la boleta dar click en el siguiente bot&oacute;n:</span></p>");
			sb.append(
					"<input type=\"button\"class=\"btn btn-large waves-effect indigo\" value=\"Descargar PDF \" onclick=\"showBoleta();\"  />");
			sb.append("</div>");

			dwr.setMessage(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dwr;
	}

	public MessageDwr pagBuscarSinCosto(String idGarantia, String nombre, String folioMercantil, String noSerial,
			String curpOtorgante, String rfcOtorgante, String registroTotalesString,
			String pagActivaString, String regPaginaString, Integer inicio, Integer fin, String ruta) {
		MessageDwr dwr = new MessageDwr();
		BusquedaTO busquedaInTO = new BusquedaTO();
		busquedaInTO.setIdGarantia(idGarantia.trim());
		busquedaInTO.setIdTramite(idGarantia.trim());
		busquedaInTO.setFolioMercantil(folioMercantil.trim());
		busquedaInTO.setNombre(nombre.trim());
		busquedaInTO.setDescGarantia("");
		busquedaInTO.setCurpOtorgante(curpOtorgante.trim());
		busquedaInTO.setRfcOtorgante(rfcOtorgante.trim());
		busquedaInTO.setNoSerial(noSerial);

		UsuarioTO usuario = (UsuarioTO) getSession().getAttribute(Constants.USUARIO);

		if (usuario == null)
			busquedaInTO.setIdPersona("51071");
		else
			busquedaInTO.setIdPersona(String.valueOf(usuario.getPersona().getIdPersona()));

		busquedaInTO.setIdTipoTramite("11");

		MyLogger.Logger.log(Level.INFO, busquedaInTO.getNombre());
		MyLogger.Logger.log(Level.INFO, "Entro al resBusqueda DWR");
		BusquedaDAO busquedaDAO = new BusquedaDAO();
		MessageDwr dwrTO = new MessageDwr();
		dwrTO.setCodeError(new Integer(0));

		int pagActiva = Integer.valueOf(pagActivaString);
		int regPagina = Integer.valueOf(regPaginaString);
		int registroTotales = Integer.valueOf(registroTotalesString);
		int numeroPaginas = registroTotales / regPagina;

		if (registroTotales % regPagina > 0) {
			numeroPaginas++;
		}
		dwr.setCodeError(0);
		if (numeroPaginas < pagActiva) {
			pagActiva = 1;
		}
		try {
			inicio = 1;
			System.out.println("paginaActiva:" + pagActiva);
			System.out.println("registro totales:" + registroTotales);
			if (pagActiva != 1) {
				inicio = ((pagActiva - 1) * regPagina) + 1;
			}
			fin = inicio + (regPagina - 1);

			StringBuffer sb = new StringBuffer();
			sb.append(tableSearch(busquedaInTO, busquedaDAO.busqueda(busquedaInTO, inicio, fin, 1), registroTotales,
					ruta));
			sb.append(
					writeSeccionPaginado(numeroPaginas, pagActiva, 20, registroTotales, "pagBusquedaSinCostoDwr", ""));

			dwr.setMessage(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dwr;
	}

	private String tableSearch(BusquedaTO pIniciales, List<BusquedaTO> busquedaTOs, Integer registroTotales,
			String ruta) {
		System.out.println("DAtos ingresados: pinical " + pIniciales + " Lista  " + busquedaTOs);
		StringBuffer html = new StringBuffer();

		StringBuffer plantilla = new StringBuffer();
		plantilla.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" +
				"<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n" +
				"<head>\r\n" +
				"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"/>\r\n" +
				"<style>\r\n" +
				".row {margin-left: auto;margin-right: auto;margin-bottom: 20px;}.row:after {content: \"\";display: table;clear: both;}.row .col {float: left;-webkit-box-sizing: border-box;box-sizing: border-box;padding: 0 0.75rem;min-height: 1px;}.row .col[class*=\"push-\"], .row .col[class*=\"pull-\"] {position: relative;}.row .col.s1 {width: 8.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.s2 {width: 16.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.s3 {width: 25%;margin-left: auto;left: auto;right: auto;}.row .col.s4 {width: 33.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.s5 {width: 41.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.s6 {width: 50%;margin-left: auto;left: auto;right: auto;}.row .col.s7 {width: 58.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.s8 {width: 66.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.s9 {width: 75%;margin-left: auto;left: auto;right: auto;}.row .col.s10 {width: 83.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.s11 {width: 91.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.s12 {width: 100%;margin-left: auto;left: auto;right: auto;}.row .col.offset-s1 {margin-left: 8.3333333333%;}.row .col.pull-s1 {right: 8.3333333333%;}.row .col.push-s1 {left: 8.3333333333%;}.row .col.offset-s2 {margin-left: 16.6666666667%;}.row .col.pull-s2 {right: 16.6666666667%;}.row .col.push-s2 {left: 16.6666666667%;}.row .col.offset-s3 {margin-left: 25%;}.row .col.pull-s3 {right: 25%;}.row .col.push-s3 {left: 25%;}.row .col.offset-s4 {margin-left: 33.3333333333%;}.row .col.pull-s4 {right: 33.3333333333%;}.row .col.push-s4 {left: 33.3333333333%;}.row .col.offset-s5 {margin-left: 41.6666666667%;}.row .col.pull-s5 {right: 41.6666666667%;}.row .col.push-s5 {left: 41.6666666667%;}.row .col.offset-s6 {margin-left: 50%;}.row .col.pull-s6 {right: 50%;}.row .col.push-s6 {left: 50%;}.row .col.offset-s7 {margin-left: 58.3333333333%;}.row .col.pull-s7 {right: 58.3333333333%;}.row .col.push-s7 {left: 58.3333333333%;}.row .col.offset-s8 {margin-left: 66.6666666667%;}.row .col.pull-s8 {right: 66.6666666667%;}.row .col.push-s8 {left: 66.6666666667%;}.row .col.offset-s9 {margin-left: 75%;}.row .col.pull-s9 {right: 75%;}.row .col.push-s9 {left: 75%;}.row .col.offset-s10 {margin-left: 83.3333333333%;}.row .col.pull-s10 {right: 83.3333333333%;}.row .col.push-s10 {left: 83.3333333333%;}.row .col.offset-s11 {margin-left: 91.6666666667%;}.row .col.pull-s11 {right: 91.6666666667%;}.row .col.push-s11 {left: 91.6666666667%;}.row .col.offset-s12 {margin-left: 100%;}.row .col.pull-s12 {right: 100%;}.row .col.push-s12 {left: 100%;}@media only screen and (min-width: 601px) {.row .col.m1 {width: 8.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.m2 {width: 16.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.m3 {width: 25%;margin-left: auto;left: auto;right: auto;}.row .col.m4 {width: 33.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.m5 {width: 41.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.m6 {width: 50%;margin-left: auto;left: auto;right: auto;}.row .col.m7 {width: 58.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.m8 {width: 66.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.m9 {width: 75%;margin-left: auto;left: auto;right: auto;}.row .col.m10 {width: 83.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.m11 {width: 91.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.m12 {width: 100%;margin-left: auto;left: auto;right: auto;}.row .col.offset-m1 {margin-left: 8.3333333333%;}.row .col.pull-m1 {right: 8.3333333333%;}.row .col.push-m1 {left: 8.3333333333%;}.row .col.offset-m2 {margin-left: 16.6666666667%;}.row .col.pull-m2 {right: 16.6666666667%;}.row .col.push-m2 {left: 16.6666666667%;}.row .col.offset-m3 {margin-left: 25%;}.row .col.pull-m3 {right: 25%;}.row .col.push-m3 {left: 25%;}.row .col.offset-m4 {margin-left: 33.3333333333%;}.row .col.pull-m4 {right: 33.3333333333%;}.row .col.push-m4 {left: 33.3333333333%;}.row .col.offset-m5 {margin-left: 41.6666666667%;}.row .col.pull-m5 {right: 41.6666666667%;}.row .col.push-m5 {left: 41.6666666667%;}.row .col.offset-m6 {margin-left: 50%;}.row .col.pull-m6 {right: 50%;}.row .col.push-m6 {left: 50%;}.row .col.offset-m7 {margin-left: 58.3333333333%;}.row .col.pull-m7 {right: 58.3333333333%;}.row .col.push-m7 {left: 58.3333333333%;}.row .col.offset-m8 {margin-left: 66.6666666667%;}.row .col.pull-m8 {right: 66.6666666667%;}.row .col.push-m8 {left: 66.6666666667%;}.row .col.offset-m9 {margin-left: 75%;}.row .col.pull-m9 {right: 75%;}.row .col.push-m9 {left: 75%;}.row .col.offset-m10 {margin-left: 83.3333333333%;}.row .col.pull-m10 {right: 83.3333333333%;}.row .col.push-m10 {left: 83.3333333333%;}.row .col.offset-m11 {margin-left: 91.6666666667%;}.row .col.pull-m11 {right: 91.6666666667%;}.row .col.push-m11 {left: 91.6666666667%;}.row .col.offset-m12 {margin-left: 100%;}.row .col.pull-m12 {right: 100%;}.row .col.push-m12 {left: 100%;}}@media only screen and (min-width: 993px) {.row .col.l1 {width: 8.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.l2 {width: 16.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.l3 {width: 25%;margin-left: auto;left: auto;right: auto;}.row .col.l4 {width: 33.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.l5 {width: 41.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.l6 {width: 50%;margin-left: auto;left: auto;right: auto;}.row .col.l7 {width: 58.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.l8 {width: 66.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.l9 {width: 75%;margin-left: auto;left: auto;right: auto;}.row .col.l10 {width: 83.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.l11 {width: 91.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.l12 {width: 100%;margin-left: auto;left: auto;right: auto;}.row .col.offset-l1 {margin-left: 8.3333333333%;}.row .col.pull-l1 {right: 8.3333333333%;}.row .col.push-l1 {left: 8.3333333333%;}.row .col.offset-l2 {margin-left: 16.6666666667%;}.row .col.pull-l2 {right: 16.6666666667%;}.row .col.push-l2 {left: 16.6666666667%;}.row .col.offset-l3 {margin-left: 25%;}.row .col.pull-l3 {right: 25%;}.row .col.push-l3 {left: 25%;}.row .col.offset-l4 {margin-left: 33.3333333333%;}.row .col.pull-l4 {right: 33.3333333333%;}.row .col.push-l4 {left: 33.3333333333%;}.row .col.offset-l5 {margin-left: 41.6666666667%;}.row .col.pull-l5 {right: 41.6666666667%;}.row .col.push-l5 {left: 41.6666666667%;}.row .col.offset-l6 {margin-left: 50%;}.row .col.pull-l6 {right: 50%;}.row .col.push-l6 {left: 50%;}.row .col.offset-l7 {margin-left: 58.3333333333%;}.row .col.pull-l7 {right: 58.3333333333%;}.row .col.push-l7 {left: 58.3333333333%;}.row .col.offset-l8 {margin-left: 66.6666666667%;}.row .col.pull-l8 {right: 66.6666666667%;}.row .col.push-l8 {left: 66.6666666667%;}.row .col.offset-l9 {margin-left: 75%;}.row .col.pull-l9 {right: 75%;}.row .col.push-l9 {left: 75%;}.row .col.offset-l10 {margin-left: 83.3333333333%;}.row .col.pull-l10 {right: 83.3333333333%;}.row .col.push-l10 {left: 83.3333333333%;}.row .col.offset-l11 {margin-left: 91.6666666667%;}.row .col.pull-l11 {right: 91.6666666667%;}.row .col.push-l11 {left: 91.6666666667%;}.row .col.offset-l12 {margin-left: 100%;}.row .col.pull-l12 {right: 100%;}.row .col.push-l12 {left: 100%;}}@media only screen and (min-width: 1201px) {.row .col.xl1 {width: 8.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.xl2 {width: 16.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.xl3 {width: 25%;margin-left: auto;left: auto;right: auto;}.row .col.xl4 {width: 33.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.xl5 {width: 41.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.xl6 {width: 50%;margin-left: auto;left: auto;right: auto;}.row .col.xl7 {width: 58.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.xl8 {width: 66.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.xl9 {width: 75%;margin-left: auto;left: auto;right: auto;}.row .col.xl10 {width: 83.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.xl11 {width: 91.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.xl12 {width: 100%;margin-left: auto;left: auto;right: auto;}.row .col.offset-xl1 {margin-left: 8.3333333333%;}.row .col.pull-xl1 {right: 8.3333333333%;}.row .col.push-xl1 {left: 8.3333333333%;}.row .col.offset-xl2 {margin-left: 16.6666666667%;}.row .col.pull-xl2 {right: 16.6666666667%;}.row .col.push-xl2 {left: 16.6666666667%;}.row .col.offset-xl3 {margin-left: 25%;}.row .col.pull-xl3 {right: 25%;}.row .col.push-xl3 {left: 25%;}.row .col.offset-xl4 {margin-left: 33.3333333333%;}.row .col.pull-xl4 {right: 33.3333333333%;}.row .col.push-xl4 {left: 33.3333333333%;}.row .col.offset-xl5 {margin-left: 41.6666666667%;}.row .col.pull-xl5 {right: 41.6666666667%;}.row .col.push-xl5 {left: 41.6666666667%;}.row .col.offset-xl6 {margin-left: 50%;}.row .col.pull-xl6 {right: 50%;}.row .col.push-xl6 {left: 50%;}.row .col.offset-xl7 {margin-left: 58.3333333333%;}.row .col.pull-xl7 {right: 58.3333333333%;}.row .col.push-xl7 {left: 58.3333333333%;}.row .col.offset-xl8 {margin-left: 66.6666666667%;}.row .col.pull-xl8 {right: 66.6666666667%;}.row .col.push-xl8 {left: 66.6666666667%;}.row .col.offset-xl9 {margin-left: 75%;}.row .col.pull-xl9 {right: 75%;}.row .col.push-xl9 {left: 75%;}.row .col.offset-xl10 {margin-left: 83.3333333333%;}.row .col.pull-xl10 {right: 83.3333333333%;}.row .col.push-xl10 {left: 83.3333333333%;}.row .col.offset-xl11 {margin-left: 91.6666666667%;}.row .col.pull-xl11 {right: 91.6666666667%;}.row .col.push-xl11 {left: 91.6666666667%;}.row .col.offset-xl12 {margin-left: 100%;}.row .col.pull-xl12 {right: 100%;}.row .col.push-xl12 {left: 100%;}}a {text-decoration: none;}html {line-height: 1.5;font-family: -apple-system, BlinkMacSystemFont, \"Segoe UI\", Roboto, Oxygen-Sans, Ubuntu, Cantarell, \"Helvetica Neue\", sans-serif;font-weight: normal;color: rgba(0, 0, 0, 0.87);}@media only screen and (min-width: 0) {html {font-size: 14px;}}@media only screen and (min-width: 992px) {html {font-size: 14.5px;}}@media only screen and (min-width: 1200px) {html {font-size: 15px;}}h1, h2, h3, h4, h5, h6 {font-weight: 400;line-height: 1.3;}h1 a, h2 a, h3 a, h4 a, h5 a, h6 a {font-weight: inherit;}h1 {font-size: 4.2rem;line-height: 110%;margin: 2.8rem 0 1.68rem 0;}h2 {font-size: 3.56rem;line-height: 110%;margin: 2.3733333333rem 0 1.424rem 0;}h3 {font-size: 2.92rem;line-height: 110%;margin: 1.9466666667rem 0 1.168rem 0;}h4 {font-size: 2.28rem;line-height: 110%;margin: 1.52rem 0 0.912rem 0;}h5 {font-size: 1.64rem;line-height: 110%;margin: 1.0933333333rem 0 0.656rem 0;}h6 {font-size: 1.15rem;line-height: 110%;margin: 0.7666666667rem 0 0.46rem 0;}em {font-style: italic;}strong {font-weight: 500;}small {font-size: 75%;}.light {font-weight: 300;}.thin {font-weight: 200;}@media only screen and (min-width: 360px) {.flow-text {font-size: 1.2rem;}}@media only screen and (min-width: 390px) {.flow-text {font-size: 1.224rem;}}@media only screen and (min-width: 420px) {.flow-text {font-size: 1.248rem;}}@media only screen and (min-width: 450px) {.flow-text {font-size: 1.272rem;}}@media only screen and (min-width: 480px) {.flow-text {font-size: 1.296rem;}}@media only screen and (min-width: 510px) {.flow-text {font-size: 1.32rem;}}@media only screen and (min-width: 540px) {.flow-text {font-size: 1.344rem;}}@media only screen and (min-width: 570px) {.flow-text {font-size: 1.368rem;}}@media only screen and (min-width: 600px) {.flow-text {font-size: 1.392rem;}}@media only screen and (min-width: 630px) {.flow-text {font-size: 1.416rem;}}@media only screen and (min-width: 660px) {.flow-text {font-size: 1.44rem;}}@media only screen and (min-width: 690px) {.flow-text {font-size: 1.464rem;}}@media only screen and (min-width: 720px) {.flow-text {font-size: 1.488rem;}}@media only screen and (min-width: 750px) {.flow-text {font-size: 1.512rem;}}@media only screen and (min-width: 780px) {.flow-text {font-size: 1.536rem;}}@media only screen and (min-width: 810px) {.flow-text {font-size: 1.56rem;}}@media only screen and (min-width: 840px) {.flow-text {font-size: 1.584rem;}}@media only screen and (min-width: 870px) {.flow-text {font-size: 1.608rem;}}@media only screen and (min-width: 900px) {.flow-text {font-size: 1.632rem;}}@media only screen and (min-width: 930px) {.flow-text {font-size: 1.656rem;}}@media only screen and (min-width: 960px) {.flow-text {font-size: 1.68rem;}}@media only screen and (max-width: 360px) {.flow-text {font-size: 1.2rem;}}.card-panel {-webkit-transition: -webkit-box-shadow .25s;transition: -webkit-box-shadow .25s;transition: box-shadow .25s;transition: box-shadow .25s, -webkit-box-shadow .25s;padding: 24px;margin: 0.5rem 0 1rem 0;border-radius: 2px;background-color: #fff;}.card {position: relative;margin: 0.5rem 0 1rem 0;background-color: #fff;-webkit-transition: -webkit-box-shadow .25s;transition: -webkit-box-shadow .25s;transition: box-shadow .25s;transition: box-shadow .25s, -webkit-box-shadow .25s;border-radius: 2px;}.card .card-title {font-size: 24px;font-weight: 300;}.card .card-title.activator {cursor: pointer;}.card.small, .card.medium, .card.large {position: relative;}.card.small .card-image, .card.medium .card-image, .card.large .card-image {max-height: 60%;overflow: hidden;}.card.small .card-image + .card-content, .card.medium .card-image + .card-content, .card.large .card-image + .card-content {max-height: 40%;}.card.small .card-content, .card.medium .card-content, .card.large .card-content {max-height: 100%;overflow: hidden;}.card.small .card-action, .card.medium .card-action, .card.large .card-action {position: absolute;bottom: 0;left: 0;right: 0;}.card.small {height: 300px;}.card.medium {height: 400px;}.card.large {height: 500px;}.card.horizontal {display: -webkit-box;display: -webkit-flex;display: -ms-flexbox;display: flex;}.card.horizontal.small .card-image, .card.horizontal.medium .card-image, .card.horizontal.large .card-image {height: 100%;max-height: none;overflow: visible;}.card.horizontal.small .card-image img, .card.horizontal.medium .card-image img, .card.horizontal.large .card-image img {height: 100%;}.card.horizontal .card-image {max-width: 50%;}.card.horizontal .card-image img {border-radius: 2px 0 0 2px;max-width: 100%;width: auto;}.card.horizontal .card-stacked {display: -webkit-box;display: -webkit-flex;display: -ms-flexbox;display: flex;-webkit-box-orient: vertical;-webkit-box-direction: normal;-webkit-flex-direction: column;-ms-flex-direction: column;flex-direction: column;-webkit-box-flex: 1;-webkit-flex: 1;-ms-flex: 1;flex: 1;position: relative;}.card.horizontal .card-stacked .card-content {-webkit-box-flex: 1;-webkit-flex-grow: 1;-ms-flex-positive: 1;flex-grow: 1;}.card.sticky-action .card-action {z-index: 2;}.card.sticky-action .card-reveal {z-index: 1;padding-bottom: 64px;}.card .card-image {position: relative;}.card .card-image img {display: block;border-radius: 2px 2px 0 0;position: relative;left: 0;right: 0;top: 0;bottom: 0;width: 100%;}.card .card-image .card-title {color: #fff;position: absolute;bottom: 0;left: 0;max-width: 100%;padding: 24px;}.card .card-content {padding: 24px;border-radius: 0 0 2px 2px;}.card .card-content p {margin: 0;}.card .card-content .card-title {display: block;line-height: 32px;margin-bottom: 8px;}.card .card-content .card-title i {line-height: 32px;}.card .card-action {background-color: inherit;border-top: 1px solid rgba(160, 160, 160, 0.2);position: relative;padding: 16px 24px;}.card .card-action:last-child {border-radius: 0 0 2px 2px;}.card .card-action a:not(.btn):not(.btn-large):not(.btn-small):not(.btn-large):not(.btn-floating) {color: #ffab40;margin-right: 24px;-webkit-transition: color .3s ease;transition: color .3s ease;text-transform: uppercase;}.card .card-action a:not(.btn):not(.btn-large):not(.btn-small):not(.btn-large):not(.btn-floating):hover {color: #ffd8a6;}.card .card-reveal {padding: 24px;position: absolute;background-color: #fff;width: 100%;overflow-y: auto;left: 0;top: 100%;height: 100%;z-index: 3;display: none;}.card .card-reveal .card-title {cursor: pointer;display: block;}.input-field.col .dropdown-content [type=\"checkbox\"] + label {top: 1px;left: 0;height: 18px;-webkit-transform: none;transform: none;}.input-field {position: relative;margin-top: 1rem;margin-bottom: 1rem;}.input-field.inline {display: inline-block;vertical-align: middle;margin-left: 5px;}.input-field.inline input, .input-field.inline .select-dropdown {margin-bottom: 1rem;}.input-field.col label {left: 0.75rem;}.input-field.col .prefix ~ label, .input-field.col .prefix ~ .validate ~ label {width: calc(100% - 3rem - 1.5rem);}.input-field > label {color: #9e9e9e;position: absolute;top: 0;left: 0;font-size: 1rem;cursor: text;-webkit-transition: color .2s ease-out, -webkit-transform .2s ease-out;transition: color .2s ease-out, -webkit-transform .2s ease-out;transition: transform .2s ease-out, color .2s ease-out;transition: transform .2s ease-out, color .2s ease-out, -webkit-transform .2s ease-out;-webkit-transform-origin: 0% 100%;transform-origin: 0% 100%;text-align: initial;-webkit-transform: translateY(12px);transform: translateY(12px);}.input-field > label:not(.label-icon).active {-webkit-transform: translateY(-14px) scale(0.8);transform: translateY(-14px) scale(0.8);-webkit-transform-origin: 0 0;transform-origin: 0 0;}.input-field > input[type=date]:not(.browser-default) + label, .input-field > input[type=time]:not(.browser-default) + label {-webkit-transform: translateY(-14px) scale(0.8);transform: translateY(-14px) scale(0.8);-webkit-transform-origin: 0 0;transform-origin: 0 0;}.input-field .helper-text {position: relative;min-height: 18px;display: block;font-size: 12px;color: rgba(0, 0, 0, 0.54);}.input-field .helper-text::after {opacity: 1;position: absolute;top: 0;left: 0;}.input-field .prefix {position: absolute;width: 3rem;font-size: 2rem;-webkit-transition: color .2s;transition: color .2s;top: 0.5rem;}.input-field .prefix.active {color: #26a69a;}.input-field .prefix ~ input, .input-field .prefix ~ textarea, .input-field .prefix ~ label, .input-field .prefix ~ .validate ~ label, .input-field .prefix ~ .helper-text, .input-field .prefix ~ .autocomplete-content {margin-left: 3rem;width: 92%;width: calc(100% - 3rem);}.input-field .prefix ~ label {margin-left: 3rem;}.blue-text {color: #2196F3 !important;}.blue-text.text-darken-2 {color: #1976D2 !important;}.teal {background-color: #009688 !important;}.white-text {color: #FFFFFF !important;}table, th, td {border: none;}table {width: 100%;display: table;border-collapse: collapse;border-spacing: 0;}table.striped tr {border-bottom: none;}table.striped > tbody > tr:nth-child(odd) {background-color: rgba(242, 242, 242, 0.5);}table.striped > tbody > tr > td {border-radius: 0;}table.highlight > tbody > tr {-webkit-transition: background-color .25s ease;transition: background-color .25s ease;}table.highlight > tbody > tr:hover {background-color: rgba(242, 242, 242, 0.5);}table.centered thead tr th, table.centered tbody tr td {text-align: center;}tr {border-bottom: 1px solid rgba(0, 0, 0, 0.12);}td, th {padding: 15px 5px;display: table-cell;text-align: left;vertical-align: middle;border-radius: 2px;}\r\n"
				+
				"</style>\r\n" +
				"</head>\r\n" +
				"<body>\r\n" +
				"<div class=\"container-fluid\">\r\n" +
				"<div class=\"row\">\r\n" +
				"  	<div class=\"col s12\"><div class=\"card\">		\r\n" +
				"		<div class=\"col s12\">\r\n" +
				"      <br />\r\n" +
				"      <br />\r\n" +
				"      <span class=\"card-title center-align\">CONSULTA DE  GARANT&Iacute;AS MOBILIARIA </span>  \r\n" +
				"      <br />\r\n" +
				"      <br />\r\n" +
				"       <div class=\"row\">\r\n" +
				"					<div class=\"input-field col s12\">\r\n" +
				"						<span class=\"blue-text text-darken-2\">Lugar y fecha </span> \r\n" +
				"            <span>[*fechaRegistro*]</span>						\r\n" +
				"					</div>\r\n" +
				"				</div>\r\n" +
				"        <div class=\"row note teal\">\r\n" +
				"  				<span class=\"white-text\">Datos de Ingreso</span>\r\n" +
				"				</div>\r\n" +
				"        <div class=\"row\">\r\n" +
				"          [*ingresoTable*]\r\n" +
				"        </div>\r\n" +
				"        <div class=\"row note teal\">\r\n" +
				"  				<span class=\"white-text\">Resultados</span>\r\n" +
				"        </div>\r\n" +
				"        <div class=\"row\">\r\n" +
				"					<div class=\"input-field col s12\">\r\n" +
				"						<span class=\"blue-text text-darken-2\">Total de Registros: </span> \r\n" +
				"            <span>[*totalRegistros*]</span>						\r\n" +
				"					</div>\r\n" +
				"				</div>" +
				"        <div class=\"row\">\r\n" +
				"            [*resultadoTable*]\r\n" +
				"        </div>\r\n" +
				"    </div>    \r\n" +
				"</div>\r\n" +
				"</div>\r\n" +
				"<br />\r\n" +
				"<br />\r\n" +
				"<br />\r\n" +
				"</body>\r\n" +
				"</html>");

		PdfTO pdfTO = new PdfTO();
		pdfTO.setHtml(plantilla.toString());

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		pdfTO.setHtml("[*fechaRegistro*]", dateFormat.format(date));

		StringBuffer inTable = new StringBuffer();

		if (pIniciales.getCurpOtorgante() != null && !pIniciales.getCurpOtorgante().equalsIgnoreCase("")) {
			inTable.append("<p>No. Identificaci\u00f3n del Deudor Garante : ");
			inTable.append(pIniciales.getCurpOtorgante());
			inTable.append("</p>");
		}

		if (pIniciales.getRfcOtorgante() != null && !pIniciales.getRfcOtorgante().equalsIgnoreCase("")) {
			inTable.append("<p>No. NIT del Deudor Garante : ");
			inTable.append(pIniciales.getRfcOtorgante());
			inTable.append("</p>");
		}

		if (pIniciales.getNoSerial() != null || pIniciales.getNoSerial().equalsIgnoreCase("")) {
			inTable.append("<p>No. de serie : ");
			inTable.append(pIniciales.getNoSerial());
			inTable.append("</p>");
		}

		if (pIniciales.getNombre() != null || pIniciales.getNombre().equalsIgnoreCase("")) {
			inTable.append("<p>Nombre del Deudor Garante : ");
			inTable.append(pIniciales.getNombre());
			inTable.append("</p>");
		}

		if (pIniciales.getIdGarantia() != null || pIniciales.getIdGarantia().equalsIgnoreCase("")) {
			inTable.append("<p>N&uacute;mero de inscripci&oacute;n de la Garant&iacute;a : ");
			inTable.append(pIniciales.getIdGarantia());
			inTable.append("</p>");
		}

		pdfTO.setHtml("[*ingresoTable*]", inTable.toString());
		pdfTO.setHtml("[*totalRegistros*]", registroTotales.toString());
		StringBuffer outTable = new StringBuffer();
		System.out.println("Tabla " + busquedaTOs.size());

		if (busquedaTOs.size() > 0) {
			if (registroTotales == 0) {
				html.append("<div style=\" color: red; font-size: 13px;\"> NO SE ENCONTRARON RESULTADOS</div>");
				outTable.append("<div style=\" color: red; font-size: 13px;\"> NO SE ENCONTRARON RESULTADOS</div>");
			} else {
				html.append("<table class=\"bordered highlight centered responsive-table\">");
				outTable.append("<table class=\"bordered highlight centered responsive-table\">");
				html.append("<thead>");
				outTable.append("<thead>");
				html.append("<tr>");
				outTable.append("<tr>");
				html.append("<th>N&uacute;mero de Garant&iacute;a</th>");
				outTable.append("<th>N&uacute;mero de Garant&iacute;a</th>");
				html.append("<th>N&uacute;mero de Operaci&oacute;n</th>");
				outTable.append("<th>N&uacute;mero de Operaci&oacute;n</th>");
				html.append("<th>Tipo de Operaci&oacute;n</th>");
				outTable.append("<th>Tipo de Operaci&oacute;n</th>");
				html.append("<th>Nombre del Deudor Garante</th>");
				outTable.append("<th>Nombre del Deudor Garante</th>");
				// html.append("<td width='133px;' class='encabezadoTablaResumen'
				// style='text-align: center'>CURP </td>");
				html.append("<th>Documento de Identificaci&oacute;n</th>");
				outTable.append("<th>Documento de Identificaci&oacute;n</th>");
				html.append("<th> Fecha de Inscripci&oacute;n</th>");
				outTable.append("<th> Fecha de Inscripci&oacute;n</th>");
				html.append("<th>Descripci&oacute;n de los Bienes</th>");
				outTable.append("<th>Descripci&oacute;n de los Bienes</th>");
				// html.append("<th>Operaci&oacute;n</th>");
				html.append("</tr>");
				outTable.append("</tr>");
				html.append("</thead>");
				outTable.append("</thead>");
				html.append("<tbody>");
				outTable.append("<tbody>");
				Iterator<BusquedaTO> iterator = busquedaTOs.iterator();
				while (iterator.hasNext()) {
					BusquedaTO busquedaTO = (BusquedaTO) iterator.next();
					html.append("<tr>");
					outTable.append("<tr>");
					html.append("<td>");
					outTable.append("<td>");

					if (!busquedaTO.getIdGarantia().equalsIgnoreCase("0")) {
						if (busquedaTO.getIdTipoTramite().equalsIgnoreCase("27")
								|| busquedaTO.getIdTipoTramite().equalsIgnoreCase("28")
								|| busquedaTO.getIdTipoTramite().equalsIgnoreCase("29")
								|| busquedaTO.getIdTipoTramite().equalsIgnoreCase("26")
								|| busquedaTO.getIdTipoTramite().equalsIgnoreCase("10")) {
							html.append("<a>");
							html.append(busquedaTO.getIdGarantia());
							outTable.append(busquedaTO.getIdGarantia());
							html.append("</a>");
						} else if (pIniciales.getIdTipoTramite().equalsIgnoreCase("32")) {
							html.append(
									"<a style=\"cursor:pointer;\" onclick=\"muestraInfo('" + busquedaTO.getIdGarantia()
											+ "','" + busquedaTO.getIdTramite() + "');\"  class='thickbox'>");
							html.append(busquedaTO.getIdGarantia());
							outTable.append(busquedaTO.getIdGarantia());
							html.append("</a>");
						} else {
							html.append("<a>");
							html.append(busquedaTO.getIdGarantia());
							outTable.append(busquedaTO.getIdGarantia());
							html.append("</a>");
						}
					}
					MyLogger.Logger.log(Level.INFO, "id grantia");
					MyLogger.Logger.log(Level.INFO, busquedaTO.getIdGarantia());
					html.append("  </td>");
					outTable.append("  </td>");
					if (busquedaTO.getIdTipoTramite().equalsIgnoreCase("3")) {// Aviso preventivo
						html.append("<td>");
						html.append("<a href='");
						html.append("detalleAviso.do?idTramite=" + busquedaTO.getIdTramite() + "' >");
						html.append(busquedaTO.getIdTramite());
						html.append("</a>");
						html.append(" </td>");
					} else {
						html.append("<td>");
						outTable.append("<td>");
						html.append(busquedaTO.getIdTramite());
						outTable.append(busquedaTO.getIdTramite());
						html.append(" </td>");
						outTable.append(" </td>");
					}
					if (busquedaTO.getDescripcion().contains("SIN")) {
						html.append("<td> Anotaci�n </td>");
					} else {
						html.append("<td> ");
						outTable.append("<td> ");
						html.append(busquedaTO.getDescripcion());
						outTable.append(busquedaTO.getDescripcion());
						html.append("</td>");
						outTable.append("</td>");
					}

					html.append("<td> ");
					outTable.append("<td> ");
					html.append(busquedaTO.getNombre());
					outTable.append(busquedaTO.getNombre());
					html.append("</td>");
					outTable.append("</td>");

					html.append("<td> ");
					outTable.append("<td> ");
					html.append(NullsFree.getNotNullValue(busquedaTO.getRfcOtorgante()));
					outTable.append(NullsFree.getNotNullValue(busquedaTO.getRfcOtorgante()));
					html.append("</td>");
					outTable.append("</td>");

					html.append("<td> ");
					outTable.append("<td> ");
					html.append(NullsFree.getNotNullValue(busquedaTO.getFechaCreacion()));
					outTable.append(NullsFree.getNotNullValue(busquedaTO.getFechaCreacion()));
					html.append("</td>");
					outTable.append("</td>");

					html.append("<td> ");
					outTable.append("<td> ");
					html.append(NullsFree.getNotNullValue(busquedaTO.getDescGarantia()));
					outTable.append(NullsFree.getNotNullValue(busquedaTO.getDescGarantia()));
					html.append("</td>");
					outTable.append("</td>");

					/*
					 * html.append("<td>");
					 * 
					 * html.
					 * append(" <a style=\"color: #515151; text-decoration: underline;\" href='");
					 * html.append(getRequest().getContextPath());
					 * html.append("/home/certificaTramite.do?idGarantia=");
					 * html.append(busquedaTO.getIdGarantia());
					 * html.append("&idTramite=");
					 * html.append(busquedaTO.getIdTramite());
					 * html.append("' > Certificaci&oacute;n</a>");
					 * html.append("</td>");
					 */
					html.append("</tr>");
					outTable.append("</tr>");
				}
			}
			html.append("</tbody>");
			outTable.append("</tbody>");
			html.append("</table>");
			outTable.append("</table>");

		} else {
			html.append("<div style=\" color: red; font-size: 13px;\"> NO SE ENCONTRARON RESULTADOS</div>");
			outTable.append("<div style=\" color: red; font-size: 13px;\"> NO SE ENCONTRARON RESULTADOS</div>");
		}

		pdfTO.setHtml("[*resultadoTable*]", outTable.toString());

		this.getSession().setAttribute("pdfTO", pdfTO);
		// MyLogger.Logger.log(Level.INFO,"PDF_TO ---" + pdfTO.getPathRoot());
		this.getSession().setAttribute("Consulta", 2);
		return html.toString();
	}

	private String tableSearchAnonima(BusquedaTO pIniciales, List<BusquedaTO> busquedaTOs, Integer registroTotales,
			String ruta) {
		StringBuffer html = new StringBuffer();

		StringBuffer plantilla = new StringBuffer();
		plantilla.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" +
				"<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n" +
				"<head>\r\n" +
				"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"/>\r\n" +
				"<style>\r\n" +
				".row {margin-left: auto;margin-right: auto;margin-bottom: 20px;}.row:after {content: \"\";display: table;clear: both;}.row .col {float: left;-webkit-box-sizing: border-box;box-sizing: border-box;padding: 0 0.75rem;min-height: 1px;}.row .col[class*=\"push-\"], .row .col[class*=\"pull-\"] {position: relative;}.row .col.s1 {width: 8.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.s2 {width: 16.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.s3 {width: 25%;margin-left: auto;left: auto;right: auto;}.row .col.s4 {width: 33.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.s5 {width: 41.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.s6 {width: 50%;margin-left: auto;left: auto;right: auto;}.row .col.s7 {width: 58.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.s8 {width: 66.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.s9 {width: 75%;margin-left: auto;left: auto;right: auto;}.row .col.s10 {width: 83.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.s11 {width: 91.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.s12 {width: 100%;margin-left: auto;left: auto;right: auto;}.row .col.offset-s1 {margin-left: 8.3333333333%;}.row .col.pull-s1 {right: 8.3333333333%;}.row .col.push-s1 {left: 8.3333333333%;}.row .col.offset-s2 {margin-left: 16.6666666667%;}.row .col.pull-s2 {right: 16.6666666667%;}.row .col.push-s2 {left: 16.6666666667%;}.row .col.offset-s3 {margin-left: 25%;}.row .col.pull-s3 {right: 25%;}.row .col.push-s3 {left: 25%;}.row .col.offset-s4 {margin-left: 33.3333333333%;}.row .col.pull-s4 {right: 33.3333333333%;}.row .col.push-s4 {left: 33.3333333333%;}.row .col.offset-s5 {margin-left: 41.6666666667%;}.row .col.pull-s5 {right: 41.6666666667%;}.row .col.push-s5 {left: 41.6666666667%;}.row .col.offset-s6 {margin-left: 50%;}.row .col.pull-s6 {right: 50%;}.row .col.push-s6 {left: 50%;}.row .col.offset-s7 {margin-left: 58.3333333333%;}.row .col.pull-s7 {right: 58.3333333333%;}.row .col.push-s7 {left: 58.3333333333%;}.row .col.offset-s8 {margin-left: 66.6666666667%;}.row .col.pull-s8 {right: 66.6666666667%;}.row .col.push-s8 {left: 66.6666666667%;}.row .col.offset-s9 {margin-left: 75%;}.row .col.pull-s9 {right: 75%;}.row .col.push-s9 {left: 75%;}.row .col.offset-s10 {margin-left: 83.3333333333%;}.row .col.pull-s10 {right: 83.3333333333%;}.row .col.push-s10 {left: 83.3333333333%;}.row .col.offset-s11 {margin-left: 91.6666666667%;}.row .col.pull-s11 {right: 91.6666666667%;}.row .col.push-s11 {left: 91.6666666667%;}.row .col.offset-s12 {margin-left: 100%;}.row .col.pull-s12 {right: 100%;}.row .col.push-s12 {left: 100%;}@media only screen and (min-width: 601px) {.row .col.m1 {width: 8.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.m2 {width: 16.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.m3 {width: 25%;margin-left: auto;left: auto;right: auto;}.row .col.m4 {width: 33.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.m5 {width: 41.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.m6 {width: 50%;margin-left: auto;left: auto;right: auto;}.row .col.m7 {width: 58.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.m8 {width: 66.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.m9 {width: 75%;margin-left: auto;left: auto;right: auto;}.row .col.m10 {width: 83.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.m11 {width: 91.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.m12 {width: 100%;margin-left: auto;left: auto;right: auto;}.row .col.offset-m1 {margin-left: 8.3333333333%;}.row .col.pull-m1 {right: 8.3333333333%;}.row .col.push-m1 {left: 8.3333333333%;}.row .col.offset-m2 {margin-left: 16.6666666667%;}.row .col.pull-m2 {right: 16.6666666667%;}.row .col.push-m2 {left: 16.6666666667%;}.row .col.offset-m3 {margin-left: 25%;}.row .col.pull-m3 {right: 25%;}.row .col.push-m3 {left: 25%;}.row .col.offset-m4 {margin-left: 33.3333333333%;}.row .col.pull-m4 {right: 33.3333333333%;}.row .col.push-m4 {left: 33.3333333333%;}.row .col.offset-m5 {margin-left: 41.6666666667%;}.row .col.pull-m5 {right: 41.6666666667%;}.row .col.push-m5 {left: 41.6666666667%;}.row .col.offset-m6 {margin-left: 50%;}.row .col.pull-m6 {right: 50%;}.row .col.push-m6 {left: 50%;}.row .col.offset-m7 {margin-left: 58.3333333333%;}.row .col.pull-m7 {right: 58.3333333333%;}.row .col.push-m7 {left: 58.3333333333%;}.row .col.offset-m8 {margin-left: 66.6666666667%;}.row .col.pull-m8 {right: 66.6666666667%;}.row .col.push-m8 {left: 66.6666666667%;}.row .col.offset-m9 {margin-left: 75%;}.row .col.pull-m9 {right: 75%;}.row .col.push-m9 {left: 75%;}.row .col.offset-m10 {margin-left: 83.3333333333%;}.row .col.pull-m10 {right: 83.3333333333%;}.row .col.push-m10 {left: 83.3333333333%;}.row .col.offset-m11 {margin-left: 91.6666666667%;}.row .col.pull-m11 {right: 91.6666666667%;}.row .col.push-m11 {left: 91.6666666667%;}.row .col.offset-m12 {margin-left: 100%;}.row .col.pull-m12 {right: 100%;}.row .col.push-m12 {left: 100%;}}@media only screen and (min-width: 993px) {.row .col.l1 {width: 8.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.l2 {width: 16.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.l3 {width: 25%;margin-left: auto;left: auto;right: auto;}.row .col.l4 {width: 33.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.l5 {width: 41.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.l6 {width: 50%;margin-left: auto;left: auto;right: auto;}.row .col.l7 {width: 58.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.l8 {width: 66.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.l9 {width: 75%;margin-left: auto;left: auto;right: auto;}.row .col.l10 {width: 83.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.l11 {width: 91.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.l12 {width: 100%;margin-left: auto;left: auto;right: auto;}.row .col.offset-l1 {margin-left: 8.3333333333%;}.row .col.pull-l1 {right: 8.3333333333%;}.row .col.push-l1 {left: 8.3333333333%;}.row .col.offset-l2 {margin-left: 16.6666666667%;}.row .col.pull-l2 {right: 16.6666666667%;}.row .col.push-l2 {left: 16.6666666667%;}.row .col.offset-l3 {margin-left: 25%;}.row .col.pull-l3 {right: 25%;}.row .col.push-l3 {left: 25%;}.row .col.offset-l4 {margin-left: 33.3333333333%;}.row .col.pull-l4 {right: 33.3333333333%;}.row .col.push-l4 {left: 33.3333333333%;}.row .col.offset-l5 {margin-left: 41.6666666667%;}.row .col.pull-l5 {right: 41.6666666667%;}.row .col.push-l5 {left: 41.6666666667%;}.row .col.offset-l6 {margin-left: 50%;}.row .col.pull-l6 {right: 50%;}.row .col.push-l6 {left: 50%;}.row .col.offset-l7 {margin-left: 58.3333333333%;}.row .col.pull-l7 {right: 58.3333333333%;}.row .col.push-l7 {left: 58.3333333333%;}.row .col.offset-l8 {margin-left: 66.6666666667%;}.row .col.pull-l8 {right: 66.6666666667%;}.row .col.push-l8 {left: 66.6666666667%;}.row .col.offset-l9 {margin-left: 75%;}.row .col.pull-l9 {right: 75%;}.row .col.push-l9 {left: 75%;}.row .col.offset-l10 {margin-left: 83.3333333333%;}.row .col.pull-l10 {right: 83.3333333333%;}.row .col.push-l10 {left: 83.3333333333%;}.row .col.offset-l11 {margin-left: 91.6666666667%;}.row .col.pull-l11 {right: 91.6666666667%;}.row .col.push-l11 {left: 91.6666666667%;}.row .col.offset-l12 {margin-left: 100%;}.row .col.pull-l12 {right: 100%;}.row .col.push-l12 {left: 100%;}}@media only screen and (min-width: 1201px) {.row .col.xl1 {width: 8.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.xl2 {width: 16.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.xl3 {width: 25%;margin-left: auto;left: auto;right: auto;}.row .col.xl4 {width: 33.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.xl5 {width: 41.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.xl6 {width: 50%;margin-left: auto;left: auto;right: auto;}.row .col.xl7 {width: 58.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.xl8 {width: 66.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.xl9 {width: 75%;margin-left: auto;left: auto;right: auto;}.row .col.xl10 {width: 83.3333333333%;margin-left: auto;left: auto;right: auto;}.row .col.xl11 {width: 91.6666666667%;margin-left: auto;left: auto;right: auto;}.row .col.xl12 {width: 100%;margin-left: auto;left: auto;right: auto;}.row .col.offset-xl1 {margin-left: 8.3333333333%;}.row .col.pull-xl1 {right: 8.3333333333%;}.row .col.push-xl1 {left: 8.3333333333%;}.row .col.offset-xl2 {margin-left: 16.6666666667%;}.row .col.pull-xl2 {right: 16.6666666667%;}.row .col.push-xl2 {left: 16.6666666667%;}.row .col.offset-xl3 {margin-left: 25%;}.row .col.pull-xl3 {right: 25%;}.row .col.push-xl3 {left: 25%;}.row .col.offset-xl4 {margin-left: 33.3333333333%;}.row .col.pull-xl4 {right: 33.3333333333%;}.row .col.push-xl4 {left: 33.3333333333%;}.row .col.offset-xl5 {margin-left: 41.6666666667%;}.row .col.pull-xl5 {right: 41.6666666667%;}.row .col.push-xl5 {left: 41.6666666667%;}.row .col.offset-xl6 {margin-left: 50%;}.row .col.pull-xl6 {right: 50%;}.row .col.push-xl6 {left: 50%;}.row .col.offset-xl7 {margin-left: 58.3333333333%;}.row .col.pull-xl7 {right: 58.3333333333%;}.row .col.push-xl7 {left: 58.3333333333%;}.row .col.offset-xl8 {margin-left: 66.6666666667%;}.row .col.pull-xl8 {right: 66.6666666667%;}.row .col.push-xl8 {left: 66.6666666667%;}.row .col.offset-xl9 {margin-left: 75%;}.row .col.pull-xl9 {right: 75%;}.row .col.push-xl9 {left: 75%;}.row .col.offset-xl10 {margin-left: 83.3333333333%;}.row .col.pull-xl10 {right: 83.3333333333%;}.row .col.push-xl10 {left: 83.3333333333%;}.row .col.offset-xl11 {margin-left: 91.6666666667%;}.row .col.pull-xl11 {right: 91.6666666667%;}.row .col.push-xl11 {left: 91.6666666667%;}.row .col.offset-xl12 {margin-left: 100%;}.row .col.pull-xl12 {right: 100%;}.row .col.push-xl12 {left: 100%;}}a {text-decoration: none;}html {line-height: 1.5;font-family: -apple-system, BlinkMacSystemFont, \"Segoe UI\", Roboto, Oxygen-Sans, Ubuntu, Cantarell, \"Helvetica Neue\", sans-serif;font-weight: normal;color: rgba(0, 0, 0, 0.87);}@media only screen and (min-width: 0) {html {font-size: 14px;}}@media only screen and (min-width: 992px) {html {font-size: 14.5px;}}@media only screen and (min-width: 1200px) {html {font-size: 15px;}}h1, h2, h3, h4, h5, h6 {font-weight: 400;line-height: 1.3;}h1 a, h2 a, h3 a, h4 a, h5 a, h6 a {font-weight: inherit;}h1 {font-size: 4.2rem;line-height: 110%;margin: 2.8rem 0 1.68rem 0;}h2 {font-size: 3.56rem;line-height: 110%;margin: 2.3733333333rem 0 1.424rem 0;}h3 {font-size: 2.92rem;line-height: 110%;margin: 1.9466666667rem 0 1.168rem 0;}h4 {font-size: 2.28rem;line-height: 110%;margin: 1.52rem 0 0.912rem 0;}h5 {font-size: 1.64rem;line-height: 110%;margin: 1.0933333333rem 0 0.656rem 0;}h6 {font-size: 1.15rem;line-height: 110%;margin: 0.7666666667rem 0 0.46rem 0;}em {font-style: italic;}strong {font-weight: 500;}small {font-size: 75%;}.light {font-weight: 300;}.thin {font-weight: 200;}@media only screen and (min-width: 360px) {.flow-text {font-size: 1.2rem;}}@media only screen and (min-width: 390px) {.flow-text {font-size: 1.224rem;}}@media only screen and (min-width: 420px) {.flow-text {font-size: 1.248rem;}}@media only screen and (min-width: 450px) {.flow-text {font-size: 1.272rem;}}@media only screen and (min-width: 480px) {.flow-text {font-size: 1.296rem;}}@media only screen and (min-width: 510px) {.flow-text {font-size: 1.32rem;}}@media only screen and (min-width: 540px) {.flow-text {font-size: 1.344rem;}}@media only screen and (min-width: 570px) {.flow-text {font-size: 1.368rem;}}@media only screen and (min-width: 600px) {.flow-text {font-size: 1.392rem;}}@media only screen and (min-width: 630px) {.flow-text {font-size: 1.416rem;}}@media only screen and (min-width: 660px) {.flow-text {font-size: 1.44rem;}}@media only screen and (min-width: 690px) {.flow-text {font-size: 1.464rem;}}@media only screen and (min-width: 720px) {.flow-text {font-size: 1.488rem;}}@media only screen and (min-width: 750px) {.flow-text {font-size: 1.512rem;}}@media only screen and (min-width: 780px) {.flow-text {font-size: 1.536rem;}}@media only screen and (min-width: 810px) {.flow-text {font-size: 1.56rem;}}@media only screen and (min-width: 840px) {.flow-text {font-size: 1.584rem;}}@media only screen and (min-width: 870px) {.flow-text {font-size: 1.608rem;}}@media only screen and (min-width: 900px) {.flow-text {font-size: 1.632rem;}}@media only screen and (min-width: 930px) {.flow-text {font-size: 1.656rem;}}@media only screen and (min-width: 960px) {.flow-text {font-size: 1.68rem;}}@media only screen and (max-width: 360px) {.flow-text {font-size: 1.2rem;}}.card-panel {-webkit-transition: -webkit-box-shadow .25s;transition: -webkit-box-shadow .25s;transition: box-shadow .25s;transition: box-shadow .25s, -webkit-box-shadow .25s;padding: 24px;margin: 0.5rem 0 1rem 0;border-radius: 2px;background-color: #fff;}.card {position: relative;margin: 0.5rem 0 1rem 0;background-color: #fff;-webkit-transition: -webkit-box-shadow .25s;transition: -webkit-box-shadow .25s;transition: box-shadow .25s;transition: box-shadow .25s, -webkit-box-shadow .25s;border-radius: 2px;}.card .card-title {font-size: 24px;font-weight: 300;}.card .card-title.activator {cursor: pointer;}.card.small, .card.medium, .card.large {position: relative;}.card.small .card-image, .card.medium .card-image, .card.large .card-image {max-height: 60%;overflow: hidden;}.card.small .card-image + .card-content, .card.medium .card-image + .card-content, .card.large .card-image + .card-content {max-height: 40%;}.card.small .card-content, .card.medium .card-content, .card.large .card-content {max-height: 100%;overflow: hidden;}.card.small .card-action, .card.medium .card-action, .card.large .card-action {position: absolute;bottom: 0;left: 0;right: 0;}.card.small {height: 300px;}.card.medium {height: 400px;}.card.large {height: 500px;}.card.horizontal {display: -webkit-box;display: -webkit-flex;display: -ms-flexbox;display: flex;}.card.horizontal.small .card-image, .card.horizontal.medium .card-image, .card.horizontal.large .card-image {height: 100%;max-height: none;overflow: visible;}.card.horizontal.small .card-image img, .card.horizontal.medium .card-image img, .card.horizontal.large .card-image img {height: 100%;}.card.horizontal .card-image {max-width: 50%;}.card.horizontal .card-image img {border-radius: 2px 0 0 2px;max-width: 100%;width: auto;}.card.horizontal .card-stacked {display: -webkit-box;display: -webkit-flex;display: -ms-flexbox;display: flex;-webkit-box-orient: vertical;-webkit-box-direction: normal;-webkit-flex-direction: column;-ms-flex-direction: column;flex-direction: column;-webkit-box-flex: 1;-webkit-flex: 1;-ms-flex: 1;flex: 1;position: relative;}.card.horizontal .card-stacked .card-content {-webkit-box-flex: 1;-webkit-flex-grow: 1;-ms-flex-positive: 1;flex-grow: 1;}.card.sticky-action .card-action {z-index: 2;}.card.sticky-action .card-reveal {z-index: 1;padding-bottom: 64px;}.card .card-image {position: relative;}.card .card-image img {display: block;border-radius: 2px 2px 0 0;position: relative;left: 0;right: 0;top: 0;bottom: 0;width: 100%;}.card .card-image .card-title {color: #fff;position: absolute;bottom: 0;left: 0;max-width: 100%;padding: 24px;}.card .card-content {padding: 24px;border-radius: 0 0 2px 2px;}.card .card-content p {margin: 0;}.card .card-content .card-title {display: block;line-height: 32px;margin-bottom: 8px;}.card .card-content .card-title i {line-height: 32px;}.card .card-action {background-color: inherit;border-top: 1px solid rgba(160, 160, 160, 0.2);position: relative;padding: 16px 24px;}.card .card-action:last-child {border-radius: 0 0 2px 2px;}.card .card-action a:not(.btn):not(.btn-large):not(.btn-small):not(.btn-large):not(.btn-floating) {color: #ffab40;margin-right: 24px;-webkit-transition: color .3s ease;transition: color .3s ease;text-transform: uppercase;}.card .card-action a:not(.btn):not(.btn-large):not(.btn-small):not(.btn-large):not(.btn-floating):hover {color: #ffd8a6;}.card .card-reveal {padding: 24px;position: absolute;background-color: #fff;width: 100%;overflow-y: auto;left: 0;top: 100%;height: 100%;z-index: 3;display: none;}.card .card-reveal .card-title {cursor: pointer;display: block;}.input-field.col .dropdown-content [type=\"checkbox\"] + label {top: 1px;left: 0;height: 18px;-webkit-transform: none;transform: none;}.input-field {position: relative;margin-top: 1rem;margin-bottom: 1rem;}.input-field.inline {display: inline-block;vertical-align: middle;margin-left: 5px;}.input-field.inline input, .input-field.inline .select-dropdown {margin-bottom: 1rem;}.input-field.col label {left: 0.75rem;}.input-field.col .prefix ~ label, .input-field.col .prefix ~ .validate ~ label {width: calc(100% - 3rem - 1.5rem);}.input-field > label {color: #9e9e9e;position: absolute;top: 0;left: 0;font-size: 1rem;cursor: text;-webkit-transition: color .2s ease-out, -webkit-transform .2s ease-out;transition: color .2s ease-out, -webkit-transform .2s ease-out;transition: transform .2s ease-out, color .2s ease-out;transition: transform .2s ease-out, color .2s ease-out, -webkit-transform .2s ease-out;-webkit-transform-origin: 0% 100%;transform-origin: 0% 100%;text-align: initial;-webkit-transform: translateY(12px);transform: translateY(12px);}.input-field > label:not(.label-icon).active {-webkit-transform: translateY(-14px) scale(0.8);transform: translateY(-14px) scale(0.8);-webkit-transform-origin: 0 0;transform-origin: 0 0;}.input-field > input[type=date]:not(.browser-default) + label, .input-field > input[type=time]:not(.browser-default) + label {-webkit-transform: translateY(-14px) scale(0.8);transform: translateY(-14px) scale(0.8);-webkit-transform-origin: 0 0;transform-origin: 0 0;}.input-field .helper-text {position: relative;min-height: 18px;display: block;font-size: 12px;color: rgba(0, 0, 0, 0.54);}.input-field .helper-text::after {opacity: 1;position: absolute;top: 0;left: 0;}.input-field .prefix {position: absolute;width: 3rem;font-size: 2rem;-webkit-transition: color .2s;transition: color .2s;top: 0.5rem;}.input-field .prefix.active {color: #26a69a;}.input-field .prefix ~ input, .input-field .prefix ~ textarea, .input-field .prefix ~ label, .input-field .prefix ~ .validate ~ label, .input-field .prefix ~ .helper-text, .input-field .prefix ~ .autocomplete-content {margin-left: 3rem;width: 92%;width: calc(100% - 3rem);}.input-field .prefix ~ label {margin-left: 3rem;}.blue-text {color: #2196F3 !important;}.blue-text.text-darken-2 {color: #1976D2 !important;}.teal {background-color: #009688 !important;}.white-text {color: #FFFFFF !important;}table, th, td {border: none;}table {width: 100%;display: table;border-collapse: collapse;border-spacing: 0;}table.striped tr {border-bottom: none;}table.striped > tbody > tr:nth-child(odd) {background-color: rgba(242, 242, 242, 0.5);}table.striped > tbody > tr > td {border-radius: 0;}table.highlight > tbody > tr {-webkit-transition: background-color .25s ease;transition: background-color .25s ease;}table.highlight > tbody > tr:hover {background-color: rgba(242, 242, 242, 0.5);}table.centered thead tr th, table.centered tbody tr td {text-align: center;}tr {border-bottom: 1px solid rgba(0, 0, 0, 0.12);}td, th {padding: 15px 5px;display: table-cell;text-align: left;vertical-align: middle;border-radius: 2px;}\r\n"
				+
				"</style>\r\n" +
				"</head>\r\n" +
				"<body>\r\n" +
				"<div class=\"container-fluid\">\r\n" +
				"<div class=\"row\">\r\n" +
				"  	<div class=\"col s12\"><div class=\"card\">		\r\n" +
				"		<div class=\"col s12\">\r\n" +
				"      <br />\r\n" +
				"      <br />\r\n" +
				"      <span class=\"card-title center-align\">CONSULTA DE  GARANT&Iacute;AS MOBILIARIA </span>  \r\n" +
				"      <br />\r\n" +
				"      <br />\r\n" +
				"       <div class=\"row\">\r\n" +
				"					<div class=\"input-field col s12\">\r\n" +
				"						<span class=\"blue-text text-darken-2\">Lugar y fecha </span> \r\n" +
				"            <span>[*fechaRegistro*]</span>						\r\n" +
				"					</div>\r\n" +
				"				</div>\r\n" +
				"        <div class=\"row note teal\">\r\n" +
				"  				<span class=\"white-text\">Datos de Ingreso</span>\r\n" +
				"				</div>\r\n" +
				"        <div class=\"row\">\r\n" +
				"          [*ingresoTable*]\r\n" +
				"        </div>\r\n" +
				"        <div class=\"row note teal\">\r\n" +
				"  				<span class=\"white-text\">Resultados</span>\r\n" +
				"        </div>\r\n" +
				"        <div class=\"row\">\r\n" +
				"					<div class=\"input-field col s12\">\r\n" +
				"						<span class=\"blue-text text-darken-2\">Total de Registros: </span> \r\n" +
				"            <span>[*totalRegistros*]</span>						\r\n" +
				"					</div>\r\n" +
				"				</div>" +
				"        <div class=\"row\">\r\n" +
				"            [*resultadoTable*]\r\n" +
				"        </div>\r\n" +
				"    </div>    \r\n" +
				"</div>\r\n" +
				"</div>\r\n" +
				"<br />\r\n" +
				"<br />\r\n" +
				"<br />\r\n" +
				"</body>\r\n" +
				"</html>");

		PdfTO pdfTO = new PdfTO();
		pdfTO.setHtml(plantilla.toString());

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		pdfTO.setHtml("[*fechaRegistro*]", dateFormat.format(date));

		StringBuffer inTable = new StringBuffer();

		if (pIniciales.getCurpOtorgante() != null && !pIniciales.getCurpOtorgante().equalsIgnoreCase("")) {
			inTable.append("<p>No. Identificaci\u00f3n del Deudor Garante : ");
			inTable.append(pIniciales.getCurpOtorgante());
			inTable.append("</p>");
		}

		if (pIniciales.getRfcOtorgante() != null && !pIniciales.getRfcOtorgante().equalsIgnoreCase("")) {
			inTable.append("<p>No. NIT del Deudor Garante : ");
			inTable.append(pIniciales.getRfcOtorgante());
			inTable.append("</p>");
		}

		if (pIniciales.getNoSerial() != null || pIniciales.getNoSerial().equalsIgnoreCase("")) {
			inTable.append("<p>No. de serie : ");
			inTable.append(pIniciales.getNoSerial());
			inTable.append("</p>");
		}

		if (pIniciales.getNombre() != null || pIniciales.getNombre().equalsIgnoreCase("")) {
			inTable.append("<p>Nombre del Deudor Garante : ");
			inTable.append(pIniciales.getNombre());
			inTable.append("</p>");
		}

		if (pIniciales.getIdGarantia() != null || pIniciales.getIdGarantia().equalsIgnoreCase("")) {
			inTable.append("<p>N&uacute;mero de inscripci&oacute;n de la Garant&iacute;a : ");
			inTable.append(pIniciales.getIdGarantia());
			inTable.append("</p>");
		}

		pdfTO.setHtml("[*ingresoTable*]", inTable.toString());
		pdfTO.setHtml("[*totalRegistros*]", registroTotales.toString());
		StringBuffer outTable = new StringBuffer();

		if (busquedaTOs.size() > 0) {
			if (registroTotales == 0) {
				html.append("<div style=\" color: red; font-size: 13px;\"> NO SE ENCONTRARON RESULTADOS</div>");
				outTable.append("<div style=\" color: red; font-size: 13px;\"> NO SE ENCONTRARON RESULTADOS</div>");
			} else {
				html.append("<table class=\"bordered highlight centered responsive-table\">");
				outTable.append("<table class=\"bordered highlight centered responsive-table\">");
				html.append("<thead>");
				outTable.append("<thead>");
				html.append("<tr>");
				outTable.append("<tr>");
				html.append("<th>N&uacute;mero de Garant&iacute;a</th>");
				outTable.append("<th>N&uacute;mero de Garant&iacute;a</th>");
				// html.append("<th>N&uacute;mero de Operaci&oacute;n</th>");
				// outTable.append("<th>N&uacute;mero de Operaci&oacute;n</th>");
				html.append("<th>Tipo de Operaci&oacute;n</th>");
				outTable.append("<th>Tipo de Operaci&oacute;n</th>");
				html.append("<th>Nombre del Deudor Garante</th>");
				outTable.append("<th>Nombre del Deudor Garante</th>");
				// html.append("<th>Documento de Identificaci&oacute;n</th>");
				// outTable.append("<th>Documento de Identificaci&oacute;n</th>");
				html.append("<th> Fecha de Inscripci&oacute;n</th>");
				outTable.append("<th> Fecha de Inscripci&oacute;n</th>");
				html.append("<th>Descripci&oacute;n de los Bienes</th>");
				outTable.append("<th>Descripci&oacute;n de los Bienes</th>");
				// html.append("<th>Operaci&oacute;n</th>");
				html.append("</tr>");
				outTable.append("</tr>");
				html.append("</thead>");
				outTable.append("</thead>");
				html.append("<tbody>");
				outTable.append("<tbody>");
				Iterator<BusquedaTO> iterator = busquedaTOs.iterator();
				while (iterator.hasNext()) {
					BusquedaTO busquedaTO = (BusquedaTO) iterator.next();
					html.append("<tr>");
					outTable.append("<tr>");
					html.append("<td>");
					outTable.append("<td>");

					if (!busquedaTO.getIdGarantia().equalsIgnoreCase("0")) {
						if (busquedaTO.getIdTipoTramite().equalsIgnoreCase("27")
								|| busquedaTO.getIdTipoTramite().equalsIgnoreCase("28")
								|| busquedaTO.getIdTipoTramite().equalsIgnoreCase("29")
								|| busquedaTO.getIdTipoTramite().equalsIgnoreCase("26")
								|| busquedaTO.getIdTipoTramite().equalsIgnoreCase("10")) {
							html.append("<a>");
							html.append(busquedaTO.getIdGarantia());
							outTable.append(busquedaTO.getIdGarantia());
							html.append("</a>");
						} else if (pIniciales.getIdTipoTramite().equalsIgnoreCase("32")) {
							html.append(
									"<a style=\"cursor:pointer;\" onclick=\"muestraInfo('" + busquedaTO.getIdGarantia()
											+ "','" + busquedaTO.getIdTramite() + "');\"  class='thickbox'>");
							html.append(busquedaTO.getIdGarantia());
							outTable.append(busquedaTO.getIdGarantia());
							html.append("</a>");
						} else {
							html.append("<a>");
							html.append(busquedaTO.getIdGarantia());
							outTable.append(busquedaTO.getIdGarantia());
							html.append("</a>");
						}
					}
					MyLogger.Logger.log(Level.INFO, "id grantia");
					MyLogger.Logger.log(Level.INFO, busquedaTO.getIdGarantia());
					html.append("  </td>");
					outTable.append("  </td>");
					// corellana: esperar a que hector confirme cuales columnas se quitan
					/*
					 * if (busquedaTO.getIdTipoTramite().equalsIgnoreCase("3")){//Aviso preventivo
					 * html.append("<td>");
					 * html.append("<a href='" );
					 * html.append("detalleAviso.do?idTramite="+busquedaTO.getIdTramite()+"' >");
					 * html.append(busquedaTO.getIdTramite());
					 * html.append("</a>" );
					 * html.append(" </td>");
					 * }else{
					 * html.append("<td>");
					 * outTable.append("<td>");
					 * html.append(busquedaTO.getIdTramite());
					 * outTable.append(busquedaTO.getIdTramite());
					 * html.append(" </td>");
					 * outTable.append(" </td>");
					 * }
					 */
					if (busquedaTO.getDescripcion().contains("SIN")) {
						html.append("<td> Anotaci�n </td>");
					} else {
						html.append("<td> ");
						outTable.append("<td> ");
						html.append(busquedaTO.getDescripcion());
						outTable.append(busquedaTO.getDescripcion());
						html.append("</td>");
						outTable.append("</td>");
					}

					html.append("<td> ");
					outTable.append("<td> ");
					html.append(busquedaTO.getNombre());
					outTable.append(busquedaTO.getNombre());
					html.append("</td>");
					outTable.append("</td>");

					/*
					 * corellana: pendiente que hector confirme cuales columnas se quitan
					 * html.append("<td> ");
					 * outTable.append("<td> ");
					 * html.append(NullsFree.getNotNullValue(busquedaTO.getRfcOtorgante()));
					 * outTable.append(NullsFree.getNotNullValue(busquedaTO.getRfcOtorgante()));
					 * html.append("</td>");
					 * outTable.append("</td>");
					 */
					html.append("<td> ");
					outTable.append("<td> ");
					html.append(NullsFree.getNotNullValue(busquedaTO.getFechaCreacion()));
					outTable.append(NullsFree.getNotNullValue(busquedaTO.getFechaCreacion()));
					html.append("</td>");
					outTable.append("</td>");

					html.append("<td> ");
					outTable.append("<td> ");
					html.append(NullsFree.getNotNullValue(busquedaTO.getDescGarantia()));
					outTable.append(NullsFree.getNotNullValue(busquedaTO.getDescGarantia()));
					html.append("</td>");
					outTable.append("</td>");

					/*
					 * html.append("<td>");
					 * 
					 * html.
					 * append(" <a style=\"color: #515151; text-decoration: underline;\" href='");
					 * html.append(getRequest().getContextPath());
					 * html.append("/home/certificaTramite.do?idGarantia=");
					 * html.append(busquedaTO.getIdGarantia());
					 * html.append("&idTramite=");
					 * html.append(busquedaTO.getIdTramite());
					 * html.append("' > Certificaci&oacute;n</a>");
					 * html.append("</td>");
					 */
					html.append("</tr>");
					outTable.append("</tr>");
				}
			}
			html.append("</tbody>");
			outTable.append("</tbody>");
			html.append("</table>");
			outTable.append("</table>");

		} else {
			html.append("<div style=\" color: red; font-size: 13px;\"> NO SE ENCONTRARON RESULTADOS</div>");
			outTable.append("<div style=\" color: red; font-size: 13px;\"> NO SE ENCONTRARON RESULTADOS</div>");
		}
		pdfTO.setHtml("[*resultadoTable*]", outTable.toString());

		this.getSession().setAttribute("pdfTO", pdfTO);
		// MyLogger.Logger.log(Level.INFO,"PDF_TO ---" + pdfTO.getPathRoot());
		this.getSession().setAttribute("Consulta", 2);
		return html.toString();
	}

	public String writeSeccionPaginado(int numeroPaginas, int pagActiva, int regPagina, int registroTotales,
			String metodoJs, String parametros) {
		StringBuffer sb = new StringBuffer();
		if (registroTotales > 0) {
			sb.append("<table class=\"table\"> <tr><td> ")
					.append("<span class=\"well\">Mostrando ")
					.append(pagActiva == 1 ? 1 : ((pagActiva - 1) * regPagina) + 1).append(" a ")
					.append(((pagActiva * regPagina) > registroTotales) ? registroTotales : (pagActiva * regPagina))
					.append(" de ").append(registroTotales).append(" registros")
					.append("</span>").append("</td>");
			sb.append("<td>").append(" <div class=\"pagination\"> <ul>");

			if (numeroPaginas > 5) {
				if (pagActiva > 1) {
					sb.append("<li><a onclick=\"").append(metodoJs).append("(").append(registroTotales).append(",")
							.append(pagActiva - 1).append(",").append(regPagina).append(",").append("'")
							.append(parametros).append("'").append(")\">")
							.append(" Anterior <span class=\"icon-fast-backward\"> </span>").append("</a></li>");
				}
				for (int x = (pagActiva - 2 < 1 ? 1 : pagActiva - 2); x <= (pagActiva + 2 > numeroPaginas
						? numeroPaginas
						: pagActiva + 2); x++) {
					if (x == pagActiva) {
						sb.append("<li class=\"active\"><a href=\"#\">").append(x).append("</a></li>");
					} else {
						sb.append("<li><a onclick=\"").append(metodoJs).append("(")
								.append(registroTotales).append(",")
								.append(x).append(",")
								.append(regPagina).append(",")
								.append("'").append(parametros).append("'")
								.append(")\">").append(x).append("</a></li>");
					}
				}
				if (pagActiva < numeroPaginas) {
					sb.append("<li><a onclick=\"").append(metodoJs).append("(")
							.append(registroTotales).append(",")
							.append(pagActiva + 1).append(",")
							.append(regPagina).append(",")
							.append("'").append(parametros).append("'")
							.append(")\">").append(" Siguiente <span class=\"icon-fast-forward\"> </span>  ")
							.append("</a></li>");
				}

			} else {
				for (int x = 1; x <= numeroPaginas; x++) {
					if (x == pagActiva) {
						sb.append("<li class=\"active\"><a href=\"#\">").append(x).append("</a></li>");
					} else {
						sb.append("<li><a onclick=\"").append(metodoJs).append("(")
								.append(registroTotales).append(",")
								.append(x).append(",")
								.append(regPagina).append(",")
								.append("'").append(parametros).append("'")
								.append(")\">").append(x).append("</a></li>");
					}
				}
			}
			sb.append(" </ul> </div> </td> </tr> </table>");
		}

		return sb.toString();
	}

}
