package mx.gob.se.rug.busquedaOp.dwr.action;

import java.util.List;

import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.inscripcion.to.OtorganteTO;
import mx.gob.se.rug.operaciones.dao.OperacionesDAO;
import mx.gob.se.rug.operaciones.service.OperacionesService;
import mx.gob.se.rug.operaciones.to.CargaMasivaResumenTO;
import mx.gob.se.rug.operaciones.to.OperacionesTO;
import mx.gob.se.rug.to.MessageDwr;

public class BusquedaOpDwrAction {
	
	private List <OperacionesTO> listaPendientes;
	private List <CargaMasivaResumenTO> listaPendientesFirmaMasiva;
	
	
	public MessageDwr iniciaBusPendientesPagin(Integer idPersona, Integer tipoOperacion, String dateStart, String dateEnd) {
		MessageDwr dwr = new MessageDwr();
		dwr.setCodeError(0);
		String metodoJS="";
		Integer countNumOperaciones=0;
		try{
			OperacionesDAO operacionesDAO = new OperacionesDAO();
			OperacionesService opService= new OperacionesService();
			switch (tipoOperacion) {
			//el caso 1 es para operaciones pendientes de captura de datos
			case 1:
				countNumOperaciones = opService.getCountBusOpPendCap(idPersona, dateStart, dateEnd);
				System.out.println("::::::::::::::countNumOperaciones: "+countNumOperaciones);
				metodoJS= "pagBusPendientes";
				break;
//			el caso 2 es para operaciones pendientes de firma
			case 2:
				countNumOperaciones = opService.getCountBusOpPendFirma(idPersona, dateStart, dateEnd);
				metodoJS= "pagBusPendFirma";
				break;
			case 3:
				System.out.println("Entro al case 3 idPersona: "+idPersona);
				countNumOperaciones = opService.getCountBusOpTerminadas(idPersona, dateStart, dateEnd);
				metodoJS= "pagBusOpTerminadas";
				break;
			default:
				break;
			}
//			listaPendientes = opService.getOpPendientes(idPersona);
			StringBuffer sb = new StringBuffer();
			int numeroPaginas = 0;
			int registroTotales = 0;
			Integer registroPaginas = 20;
			if (countNumOperaciones>0){
				System.out.println(":::::La lista viene con elementos ");
				numeroPaginas = countNumOperaciones/registroPaginas;
				registroTotales = countNumOperaciones;
			}
			if (registroTotales % registroPaginas > 0){
				numeroPaginas++;
			}
			if (countNumOperaciones>0){
				sb.append("<div align=\"right\">")
//				.append(writeSeccionHeader(numeroPaginas, 1, registroPaginas, registroTotales,metodoJS,""))
				.append("</div>");
				switch (tipoOperacion) {
					case 1:
						sb.append(writeTablaPendientes( opService.getBusOpPendientes(idPersona,1, registroPaginas>registroTotales?registroTotales:registroPaginas, dateStart, dateEnd)));
						break;
					case 2:
						sb.append(writeTablaPendientesFirma( opService.getBusOpPenFirma(idPersona,1, registroPaginas>registroTotales?registroTotales:registroPaginas, dateStart, dateEnd)));
						break;
					case 3:
						sb.append(writeTablaTerminadas(opService.getBusOpTerminadas(idPersona,1, registroPaginas>registroTotales?registroTotales:registroPaginas, dateStart, dateEnd)));
						break;
					default:
						break;
				}
				sb.append(writeSeccionPaginado(numeroPaginas, 1, registroPaginas, registroTotales, metodoJS, ""));
			}else{
//				sb.append(" <div class=\"alert alert-info\">").append("No se encontraron resultados").append(" </div>");
				sb.append(" <br></br> ");
				sb.append(" <br></br> ");
				sb.append(" <div style=\" color: red; font-size: 13px;\"> NO SE ENCONTRARON RESULTADOS</div> ");
			}
			dwr.setMessage(sb.toString());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return dwr;
	}
	
	public MessageDwr iniBusPendByOtorgante(Integer idPersona, Integer tipoOperacion, String nomOtorgante) {
		MessageDwr dwr = new MessageDwr();
		dwr.setCodeError(0);
		String metodoJS="";
		Integer countNumOperaciones=0;
		try{
			OperacionesDAO operacionesDAO = new OperacionesDAO();
			OperacionesService opService= new OperacionesService();
			switch (tipoOperacion) {
			//el caso 1 es para operaciones pendientes de captura de datos
			case 1:
				countNumOperaciones = opService.getCountBusOpPendCapByOtorgante(idPersona, nomOtorgante);
				System.out.println("::::::::::::::countNumOperaciones: "+countNumOperaciones);
				metodoJS= "pagBusPenByOtorgante";
				break;
//			el caso 2 es para operaciones pendientes de firma
			case 2:
				countNumOperaciones = opService.getCountBusOpPendFirmaByOtorgante(idPersona, nomOtorgante);
				metodoJS= "pagBusPendFirmaByOtorgante";
				break;
			case 3:
				countNumOperaciones = opService.getCountBusOpTermByOtorgante(idPersona, nomOtorgante);
				metodoJS= "pagBusOpTermByOtorgante";
				break;
			default:
				break;
			}
//			listaPendientes = opService.getOpPendientes(idPersona);
			StringBuffer sb = new StringBuffer();
			int numeroPaginas = 0;
			int registroTotales = 0;
			Integer registroPaginas = 20;
			if (countNumOperaciones>0){
				System.out.println(":::::La lista viene con elementos ");
				numeroPaginas = countNumOperaciones/registroPaginas;
				registroTotales = countNumOperaciones;
			}
			if (registroTotales % registroPaginas > 0){
				numeroPaginas++;
			}
			if (countNumOperaciones>0){
				sb.append("<div align=\"right\">")
//				.append(writeSeccionHeader(numeroPaginas, 1, registroPaginas, registroTotales,metodoJS,""))
				.append("</div>");
				switch (tipoOperacion) {
					case 1:
						sb.append(writeTablaPendientes( opService.getBusOpPenByOtorgante(idPersona,1, registroPaginas>registroTotales?registroTotales:registroPaginas, nomOtorgante)));
						break;
					case 2:
						sb.append(writeTablaPendientesFirma( opService.getBusOpPenFirmaByOtorgante(idPersona,1, registroPaginas>registroTotales?registroTotales:registroPaginas, nomOtorgante)));
						break;
					case 3:
						sb.append(writeTablaTerminadas(opService.getBusOpTermByOtorgante(idPersona,1, registroPaginas>registroTotales?registroTotales:registroPaginas, nomOtorgante)));
						break;
					default:
						break;
				}
				sb.append(writeSeccionPaginado(numeroPaginas, 1, registroPaginas, registroTotales, metodoJS, ""));
			}else{
//				sb.append(" <div class=\"alert alert-info\">").append("No se encontraron resultados").append(" </div>");
				sb.append(" <br></br> ");
				sb.append(" <br></br> ");
				sb.append(" <div style=\" color: red; font-size: 13px;\"> NO SE ENCONTRARON RESULTADOS</div> ");
			}
			dwr.setMessage(sb.toString());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return dwr;
	}
	
	
	public MessageDwr iniciaBusPenFirMasiva(Integer idPersona, String dateStart, String dateEnd) {
		MessageDwr dwr = new MessageDwr();
		dwr.setCodeError(0);
		Integer countNumOperaciones=0;
		try{
			OperacionesService opService= new OperacionesService();
//			listaPendientesFirmaMasiva = opService.muestraOpPendientesFirmaMasiva(idPersona);
			countNumOperaciones = opService.getCountBusOpPendFirmaMasiva(idPersona, dateStart, dateEnd);
//			listaPendientes = opService.getOpPendientes(idPersona);
			StringBuffer sb = new StringBuffer();
			int numeroPaginas = 0;
			int registroTotales = 0;
			Integer registroPaginas = 20;
			if (countNumOperaciones>0){
				System.out.println(":::::La lista viene con elementos ");
				numeroPaginas = countNumOperaciones/registroPaginas;
				registroTotales = countNumOperaciones;
			}
			if (registroTotales % registroPaginas > 0){
				numeroPaginas++;
			}
			if (countNumOperaciones>0){
				sb.append("<div align=\"right\">")
//				.append(writeSeccionHeader(numeroPaginas, 1, registroPaginas, registroTotales,metodoJS,""))
				.append("</div>");
				sb.append(writeTablaPenAsientosMultiples( opService.getBusOpPenFirmaMasiva(idPersona,1, registroPaginas>registroTotales?registroTotales:registroPaginas, dateStart, dateEnd)));
				sb.append(writeSeccionPaginado(numeroPaginas, 1, registroPaginas, registroTotales, "pagBusPenAsiMultiples", ""));
			}else{
//				sb.append(" <div class=\"alert alert-info\">").append("No se encontraron resultados").append(" </div>");
				sb.append(" <br></br> ");
				sb.append(" <br></br> ");
				sb.append(" <div style=\" color: red; font-size: 13px;\"> NO SE ENCONTRARON RESULTADOS</div> ");
				
			}
			dwr.setMessage(sb.toString());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return dwr;
	}
	
	public MessageDwr iniBusPendFirmaMasivaByClvRastreo(Integer idPersona, String clvRastreo) {
		MessageDwr dwr = new MessageDwr();
		dwr.setCodeError(0);
		Integer countNumOperaciones=0;
		try{
			OperacionesService opService= new OperacionesService();
//			listaPendientesFirmaMasiva = opService.muestraOpPendientesFirmaMasiva(idPersona);
			countNumOperaciones = opService.getCountBusOpPendFirMasivaByClvRastreo(idPersona, clvRastreo);
//			listaPendientes = opService.getOpPendientes(idPersona);
			StringBuffer sb = new StringBuffer();
			int numeroPaginas = 0;
			int registroTotales = 0;
			Integer registroPaginas = 20;
			if (countNumOperaciones>0){
				System.out.println(":::::La lista viene con elementos ");
				numeroPaginas = countNumOperaciones/registroPaginas;
				registroTotales = countNumOperaciones;
			}
			if (registroTotales % registroPaginas > 0){
				numeroPaginas++;
			}
			if (countNumOperaciones>0){
				sb.append("<div align=\"right\">")
//				.append(writeSeccionHeader(numeroPaginas, 1, registroPaginas, registroTotales,metodoJS,""))
				.append("</div>");
				sb.append(writeTablaPenAsientosMultiples( opService.getBusOpPenFirmaMasByClvRastreo(idPersona,1, registroPaginas>registroTotales?registroTotales:registroPaginas, clvRastreo)));
				sb.append(writeSeccionPaginado(numeroPaginas, 1, registroPaginas, registroTotales, "pagBusPenAsiMulByClvRastreo", ""));
			}else{
//				sb.append(" <div class=\"alert alert-info\">").append("No se encontraron resultados").append(" </div>");
				sb.append(" <br></br> ");
				sb.append(" <br></br> ");
				sb.append(" <div style=\" color: red; font-size: 13px;\"> NO SE ENCONTRARON RESULTADOS</div> ");
			}
			dwr.setMessage(sb.toString());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return dwr;
	}
	
	public MessageDwr pagBusOpPendientes(String idPersonaString, String registroTotalesString, String pagActivaString, String regPaginaString, String dateStart, String  dateEnd) {	
		MessageDwr dwr = new MessageDwr();
		
		int pagActiva = Integer.valueOf(pagActivaString);
		int regPagina = Integer.valueOf(regPaginaString);
		int registroTotales = Integer.valueOf(registroTotalesString);
		int numeroPaginas = registroTotales/regPagina;
		int idPersona = Integer.valueOf(idPersonaString);
		if ( registroTotales %regPagina > 0){
			numeroPaginas++;
		}
		dwr.setCodeError(0);
		if (numeroPaginas < pagActiva){
			pagActiva = 1;
		}
		try {
//			int idUsuario =  getUser().getIdUsuario();
			int inicio = 1;
			int regFinal;
			System.out.println("paginaActiva:"+pagActiva);
			System.out.println("registro totales:"+registroTotales);
			if (pagActiva != 1){
				inicio = ((pagActiva-1)*regPagina) + 1;				
			}
			regFinal = inicio + (regPagina-1);
			OperacionesService opService= new OperacionesService();
			listaPendientes = opService.getBusOpPendientes(idPersona, inicio, regFinal, dateStart, dateEnd);
			System.out.println("::::::El tamaño debe de ser igual a 20 tamaño: "+listaPendientes.size());
			StringBuffer sb = new StringBuffer();
			sb.append("<div align=\"right\">")
//			.append(writeSeccionHeader(numeroPaginas, pagActiva, regPagina, registroTotales,"pagPendientes",""))
			.append("</div>");
			sb.append(writeTablaPendientes(listaPendientes));	
			sb.append(writeSeccionPaginado(numeroPaginas, pagActiva, regPagina,registroTotales,"pagBusPendientes",""));
			dwr.setMessage(sb.toString());
		} catch (Exception  e) {
			e.printStackTrace();
		}	
		return dwr;
	}
	
	public MessageDwr pagBusOpPenByOtorgante(String idPersonaString, String registroTotalesString, String pagActivaString, String regPaginaString, String nomOtorgante) {	
		MessageDwr dwr = new MessageDwr();
		
		int pagActiva = Integer.valueOf(pagActivaString);
		int regPagina = Integer.valueOf(regPaginaString);
		int registroTotales = Integer.valueOf(registroTotalesString);
		int numeroPaginas = registroTotales/regPagina;
		int idPersona = Integer.valueOf(idPersonaString);
		if ( registroTotales %regPagina > 0){
			numeroPaginas++;
		}
		dwr.setCodeError(0);
		if (numeroPaginas < pagActiva){
			pagActiva = 1;
		}
		try {
//			int idUsuario =  getUser().getIdUsuario();
			int inicio = 1;
			int regFinal;
			System.out.println("paginaActiva:"+pagActiva);
			System.out.println("registro totales:"+registroTotales);
			if (pagActiva != 1){
				inicio = ((pagActiva-1)*regPagina) + 1;				
			}
			regFinal = inicio + (regPagina-1);
			OperacionesService opService= new OperacionesService();
			listaPendientes = opService.getBusOpPenByOtorgante(idPersona, inicio, regFinal, nomOtorgante);
			System.out.println("::::::El tamaño debe de ser igual a 20 tamaño: "+listaPendientes.size());
			StringBuffer sb = new StringBuffer();
			sb.append("<div align=\"right\">")
//			.append(writeSeccionHeader(numeroPaginas, pagActiva, regPagina, registroTotales,"pagPendientes",""))
			.append("</div>");
			sb.append(writeTablaPendientes(listaPendientes));	
			sb.append(writeSeccionPaginado(numeroPaginas, pagActiva, regPagina,registroTotales,"pagBusPenByOtorgante",""));
			dwr.setMessage(sb.toString());
		} catch (Exception  e) {
			e.printStackTrace();
		}	
		return dwr;
	}
	
	public MessageDwr pagBusOpPendFirma(String idPersonaString, String registroTotalesString, String pagActivaString, String regPaginaString, String dateStart, String  dateEnd) {	
		MessageDwr dwr = new MessageDwr();
		
		int pagActiva = Integer.valueOf(pagActivaString);
		int regPagina = Integer.valueOf(regPaginaString);
		int registroTotales = Integer.valueOf(registroTotalesString);
		int numeroPaginas = registroTotales/regPagina;
		int idPersona = Integer.valueOf(idPersonaString);
		if ( registroTotales %regPagina > 0){
			numeroPaginas++;
		}
		dwr.setCodeError(0);
		if (numeroPaginas < pagActiva){
			pagActiva = 1;
		}
		try {
//			int idUsuario =  getUser().getIdUsuario();
			int inicio = 1;
			int regFinal;
			System.out.println("paginaActiva:"+pagActiva);
			System.out.println("registro totales:"+registroTotales);
			if (pagActiva != 1){
				inicio = ((pagActiva-1)*regPagina) + 1;				
			}
			regFinal = inicio + (regPagina-1);
			OperacionesService opService= new OperacionesService();
			listaPendientes = opService.getBusOpPenFirma(idPersona, inicio, regFinal, dateStart, dateEnd);
			System.out.println("::::::El tamaño debe de ser igual a 20 tamaño: "+listaPendientes.size());
			StringBuffer sb = new StringBuffer();
			sb.append("<div align=\"right\">")
//			.append(writeSeccionHeader(numeroPaginas, pagActiva, regPagina, registroTotales,"pagPendientes",""))
			.append("</div>");
			sb.append(writeTablaPendientesFirma(listaPendientes));	
			sb.append(writeSeccionPaginado(numeroPaginas, pagActiva, regPagina,registroTotales,"pagBusPendFirma",""));
			dwr.setMessage(sb.toString());
		} catch (Exception  e) {
			e.printStackTrace();
		}	
		return dwr;
	}
	
	public MessageDwr pagBusOpPenFirmaByOtorgante(String idPersonaString, String registroTotalesString, String pagActivaString, String regPaginaString, String nomOtorgante) {	
		MessageDwr dwr = new MessageDwr();
		
		int pagActiva = Integer.valueOf(pagActivaString);
		int regPagina = Integer.valueOf(regPaginaString);
		int registroTotales = Integer.valueOf(registroTotalesString);
		int numeroPaginas = registroTotales/regPagina;
		int idPersona = Integer.valueOf(idPersonaString);
		if ( registroTotales %regPagina > 0){
			numeroPaginas++;
		}
		dwr.setCodeError(0);
		if (numeroPaginas < pagActiva){
			pagActiva = 1;
		}
		try {
//			int idUsuario =  getUser().getIdUsuario();
			int inicio = 1;
			int regFinal;
			System.out.println("paginaActiva:"+pagActiva);
			System.out.println("registro totales:"+registroTotales);
			if (pagActiva != 1){
				inicio = ((pagActiva-1)*regPagina) + 1;				
			}
			regFinal = inicio + (regPagina-1);
			OperacionesService opService= new OperacionesService();
			listaPendientes = opService.getBusOpPenFirmaByOtorgante(idPersona, inicio, regFinal, nomOtorgante);
			System.out.println("::::::El tamaño debe de ser igual a 20 tamaño: "+listaPendientes.size());
			StringBuffer sb = new StringBuffer();
			sb.append("<div align=\"right\">")
//			.append(writeSeccionHeader(numeroPaginas, pagActiva, regPagina, registroTotales,"pagPendientes",""))
			.append("</div>");
			sb.append(writeTablaPendientesFirma(listaPendientes));	
			sb.append(writeSeccionPaginado(numeroPaginas, pagActiva, regPagina,registroTotales,"pagBusPendFirmaByOtorgante",""));
			dwr.setMessage(sb.toString());
		} catch (Exception  e) {
			e.printStackTrace();
		}	
		return dwr;
	}
	
	public MessageDwr pagBusOpPendAsiMultiples(String idPersonaString, String registroTotalesString, String pagActivaString, String regPaginaString, String dateStart, String  dateEnd) {	
		MessageDwr dwr = new MessageDwr();
		
		int pagActiva = Integer.valueOf(pagActivaString);
		int regPagina = Integer.valueOf(regPaginaString);
		int registroTotales = Integer.valueOf(registroTotalesString);
		int numeroPaginas = registroTotales/regPagina;
		int idPersona = Integer.valueOf(idPersonaString);
		if ( registroTotales %regPagina > 0){
			numeroPaginas++;
		}
		dwr.setCodeError(0);
		if (numeroPaginas < pagActiva){
			pagActiva = 1;
		}
		try {
//			int idUsuario =  getUser().getIdUsuario();
			int inicio = 1;
			int regFinal;
			System.out.println("paginaActiva:"+pagActiva);
			System.out.println("registro totales:"+registroTotales);
			if (pagActiva != 1){
				inicio = ((pagActiva-1)*regPagina) + 1;				
			}
			regFinal = inicio + (regPagina-1);
			OperacionesService opService= new OperacionesService();
			listaPendientesFirmaMasiva = opService.getBusOpPenFirmaMasiva(idPersona, inicio, regFinal, dateStart, dateEnd);
			StringBuffer sb = new StringBuffer();
			sb.append("<div align=\"right\">")
//			.append(writeSeccionHeader(numeroPaginas, pagActiva, regPagina, registroTotales,"pagPendientes",""))
			.append("</div>");
			sb.append(writeTablaPenAsientosMultiples(listaPendientesFirmaMasiva));	
			sb.append(writeSeccionPaginado(numeroPaginas, pagActiva, regPagina,registroTotales,"pagBusPenAsiMultiples",""));
			dwr.setMessage(sb.toString());
		} catch (Exception  e) {
			e.printStackTrace();
		}	
		return dwr;
	}
	
	
	public MessageDwr pagBusPenAsiMulByClvRastreo(String idPersonaString, String registroTotalesString, String pagActivaString, String regPaginaString, String clvRastreo) {	
		MessageDwr dwr = new MessageDwr();
		
		int pagActiva = Integer.valueOf(pagActivaString);
		int regPagina = Integer.valueOf(regPaginaString);
		int registroTotales = Integer.valueOf(registroTotalesString);
		int numeroPaginas = registroTotales/regPagina;
		int idPersona = Integer.valueOf(idPersonaString);
		if ( registroTotales %regPagina > 0){
			numeroPaginas++;
		}
		dwr.setCodeError(0);
		if (numeroPaginas < pagActiva){
			pagActiva = 1;
		}
		try {
//			int idUsuario =  getUser().getIdUsuario();
			int inicio = 1;
			int regFinal;
			System.out.println("paginaActiva:"+pagActiva);
			System.out.println("registro totales:"+registroTotales);
			if (pagActiva != 1){
				inicio = ((pagActiva-1)*regPagina) + 1;				
			}
			regFinal = inicio + (regPagina-1);
			OperacionesService opService= new OperacionesService();
			listaPendientesFirmaMasiva = opService.getBusOpPenFirmaMasByClvRastreo(idPersona, inicio, regFinal, clvRastreo);
			StringBuffer sb = new StringBuffer();
			sb.append("<div align=\"right\">")
//			.append(writeSeccionHeader(numeroPaginas, pagActiva, regPagina, registroTotales,"pagPendientes",""))
			.append("</div>");
			sb.append(writeTablaPenAsientosMultiples(listaPendientesFirmaMasiva));	
			sb.append(writeSeccionPaginado(numeroPaginas, pagActiva, regPagina,registroTotales,"pagBusPenAsiMulByClvRastreo",""));
			dwr.setMessage(sb.toString());
		} catch (Exception  e) {
			e.printStackTrace();
		}	
		return dwr;
	}
	
	public MessageDwr pagBusOpTerminadas(String idPersonaString, String registroTotalesString, String pagActivaString, String regPaginaString, String dateStart, String  dateEnd) {	
		MessageDwr dwr = new MessageDwr();
		
		int pagActiva = Integer.valueOf(pagActivaString);
		int regPagina = Integer.valueOf(regPaginaString);
		int registroTotales = Integer.valueOf(registroTotalesString);
		int numeroPaginas = registroTotales/regPagina;
		int idPersona = Integer.valueOf(idPersonaString);
		if ( registroTotales %regPagina > 0){
			numeroPaginas++;
		}
		dwr.setCodeError(0);
		if (numeroPaginas < pagActiva){
			pagActiva = 1;
		}
		try {
//			int idUsuario =  getUser().getIdUsuario();
			int inicio = 1;
			int regFinal;
			System.out.println("paginaActiva:"+pagActiva);
			System.out.println("registro totales:"+registroTotales);
			if (pagActiva != 1){
				inicio = ((pagActiva-1)*regPagina) + 1;				
			}
			regFinal = inicio + (regPagina-1);
			OperacionesService opService= new OperacionesService();
			listaPendientes = opService.getBusOpTerminadas(idPersona, inicio, regFinal, dateStart, dateEnd);
			System.out.println("::::::El tamaño debe de ser igual a 20 tamaño: "+listaPendientes.size());
			StringBuffer sb = new StringBuffer();
			sb.append("<div align=\"right\">")
//			.append(writeSeccionHeader(numeroPaginas, pagActiva, regPagina, registroTotales,"pagPendientes",""))
			.append("</div>");
			sb.append(writeTablaTerminadas(listaPendientes));	
			sb.append(writeSeccionPaginado(numeroPaginas, pagActiva, regPagina,registroTotales,"pagBusOpTerminadas",""));
			dwr.setMessage(sb.toString());
		} catch (Exception  e) {
			e.printStackTrace();
		}	
		return dwr;
	}
	
	public MessageDwr pagBusOpTermByOtorgante(String idPersonaString, String registroTotalesString, String pagActivaString, String regPaginaString, String nomOtorgante) {	
		MessageDwr dwr = new MessageDwr();
		
		int pagActiva = Integer.valueOf(pagActivaString);
		int regPagina = Integer.valueOf(regPaginaString);
		int registroTotales = Integer.valueOf(registroTotalesString);
		int numeroPaginas = registroTotales/regPagina;
		int idPersona = Integer.valueOf(idPersonaString);
		if ( registroTotales %regPagina > 0){
			numeroPaginas++;
		}
		dwr.setCodeError(0);
		if (numeroPaginas < pagActiva){
			pagActiva = 1;
		}
		try {
//			int idUsuario =  getUser().getIdUsuario();
			int inicio = 1;
			int regFinal;
			System.out.println("paginaActiva:"+pagActiva);
			System.out.println("registro totales:"+registroTotales);
			if (pagActiva != 1){
				inicio = ((pagActiva-1)*regPagina) + 1;				
			}
			regFinal = inicio + (regPagina-1);
			OperacionesService opService= new OperacionesService();
			listaPendientes = opService.getBusOpTermByOtorgante(idPersona, inicio, regFinal, nomOtorgante);
			System.out.println("::::::El tamaño debe de ser igual a 20 tamaño: "+listaPendientes.size());
			StringBuffer sb = new StringBuffer();
			sb.append("<div align=\"right\">")
//			.append(writeSeccionHeader(numeroPaginas, pagActiva, regPagina, registroTotales,"pagPendientes",""))
			.append("</div>");
			sb.append(writeTablaTerminadas(listaPendientes));	
			sb.append(writeSeccionPaginado(numeroPaginas, pagActiva, regPagina,registroTotales,"pagBusOpTermByOtorgante",""));
			dwr.setMessage(sb.toString());
		} catch (Exception  e) {
			e.printStackTrace();
		}	
		return dwr;
	}
	
	public String writeSeccionPaginado(int numeroPaginas,int pagActiva, int regPagina, 
			int registroTotales, String metodoJs, String parametros){
		StringBuffer sb = new StringBuffer();
		if (registroTotales>0){
			sb.append("<table class=\"table\"> <tr><td> ")
			.append("<span class=\"well\">Mostrando ").append(pagActiva==1?1:((pagActiva-1)*regPagina)+1).append(" a ")
			.append(((pagActiva*regPagina)>registroTotales)?registroTotales:(pagActiva*regPagina)).append(" de ").append(registroTotales).append(" registros")
			.append("</span>").append("</td>");
			sb.append("<td>").append(" <div class=\"pagination\"> <ul>");
			
			if (numeroPaginas> 5){
				if (pagActiva > 1){
					sb.append("<li><a onclick=\"").append(metodoJs).append("(")
					.append(registroTotales).append(",")
					.append(pagActiva-1).append(",")
					.append(regPagina).append(",")
					.append("'").append(parametros).append("'")
					.append(")\">").append(" Anterior <span class=\"icon-fast-backward\"> </span>").append("</a></li>");
				}
				for (int x=(pagActiva-2<1?1:pagActiva-2);x<=(pagActiva+2>numeroPaginas?numeroPaginas:pagActiva+2); x++){
					if (x==pagActiva){
						sb.append("<li class=\"active\"><a href=\"#\">").append(x).append("</a></li>");
					}else{
						sb.append("<li><a onclick=\"").append(metodoJs).append("(")
						.append(registroTotales).append(",")
						.append(x).append(",")
						.append(regPagina).append(",")
						.append("'").append(parametros).append("'")
						.append(")\">").append(x).append("</a></li>");
					}
				}
				if (pagActiva < numeroPaginas){
					sb.append("<li><a onclick=\"").append(metodoJs).append("(")
					.append(registroTotales).append(",")
					.append(pagActiva+1).append(",")
					.append(regPagina).append(",")
					.append("'").append(parametros).append("'")
					.append(")\">").append(" Siguiente <span class=\"icon-fast-forward\"> </span>  ").append("</a></li>");
				}
				
			}else{
				for (int x=1;x<=numeroPaginas; x++){
					if (x==pagActiva){
						sb.append("<li class=\"active\"><a href=\"#\">").append(x).append("</a></li>");
					}else{
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
	
	public String writeTablaPendientes(List<OperacionesTO> tramitesPendientes){
		StringBuffer sb = new StringBuffer();
		try{
			sb.append(" <table class=\"mytabledaO\" width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" align=\"left\"> ");
			sb.append(" 	<thead> ");
			sb.append(" 		<tr height=\"25\"> ");
			sb.append("				<td width=\"89px\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">Tipo de Operaci&oacute;n</td>");
			sb.append(" 			<td width=\"89px\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">N&uacute;mero de Garant&iacute;a</td>");
			sb.append(" 			<td width=\"501px\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">");
			sb.append(" 				<table width=\"100%\">");
			sb.append(" 					<tr height=\"25\">");
			sb.append(" 						<td width=\"88px\" style=\"text-align: center\" colspan=\"2\"><b>Datos de los Otorgantes</b></td>");
			sb.append(" 					</tr>");
			sb.append(" 					<tr height=\"25\">");
			sb.append(" 						<td width=\"50%\" style=\"text-align: center\">Nombre</td>");
			sb.append(" 						<td width=\"50%\" style=\"text-align: center\">Folio Electr&oacute;nico</td>");
			sb.append(" 					</tr>");
			sb.append(" 				</table>");
			sb.append(" 			</td>");
			sb.append(" 			<td width=\"89px\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">Fecha de Inicio de la Vigencia</td>");
			sb.append(" 			<td width=\"145px\" class=\"encabezadoTablaResumen\" style=\"text-align: center\" colspan=\"2\">Opciones</td>");
			sb.append(" 		</tr> ");
			sb.append(" 	</thead>");
			if (tramitesPendientes.size() > 0){
				sb.append(" <tr> ");
				sb.append(" 	<td colspan=\"6\">");
				sb.append(" 		<table>") ;
				for(OperacionesTO tramite : tramitesPendientes){
					sb.append(" 		<tr height=\"25\">");
					sb.append(" 			<td width=\"88px\" class=\"cuerpo1TablaResumen\" >").append(tramite.getTipoTransaccion()).append(" </td>");
					sb.append(" 			<td width=\"88px\" class=\"cuerpo1TablaResumen\" >");
					sb.append(" 				<div align=\"center\"> ");
													sb.append(tramite.getNumGarantia());
					sb.append(" 				</div> ");
					sb.append(" 			</td>");
					sb.append(" 			<td width=\"500px\" class=\"cuerpo1TablaResumen\">");
					sb.append(" 				<div align=\"center\">");
					sb.append("						<table width=\"100%\">");
					List<OtorganteTO> otorgantes = tramite.getOtorgantes();
					if(otorgantes.size()>0){
						for(OtorganteTO otorgante : otorgantes){
							sb.append(" 				<tr>");
							sb.append(" 					<td align=\"left\" width=\"50%\" style=\"padding-left: 11px;\" >").append(otorgante.getNombreCompleto()).append(" </td>");
							sb.append(" 					<td align=\"left\" width=\"50%\" style=\"padding-left: 11px;\" >").append(otorgante.getFolioMercantil()).append(" </td>");
							sb.append(" 				</tr>");
							sb.append("				</table>");
							sb.append(" 		</div>");
							sb.append(" 	</td> ");
						}
					}else{
						sb.append(" 					<tr>");
						sb.append(" 						<td class=\"cuerpo1TablaResumen\">&nbsp</td><td class=\"cuerpo1TablaResumen\">&nbsp</td>");
						sb.append(" 					</tr>");
						sb.append("					</table>");
						sb.append(" 			</div>");
						sb.append(" 		</td> ");
					}
					sb.append(" 			<td width=\"88px\" class=\"cuerpo1TablaResumen\">");
					sb.append(" 				<div align=\"center\"> ");
													sb.append(tramite.getFechaInicio());
					sb.append(" 				</div> ");								
					sb.append(" 			</td>");
					sb.append(" 			<td width=\"145px\">");
					sb.append("					<table width=\"100%\">");
					sb.append(" 					<tr> ");
					sb.append(" 						<td align=\"center\"> ");
					sb.append(" 							<input type=\"button\" value=\"Continuar\" class=\"boton\" onclick=\"window.location.href='"+Constants.getContextPath()).append(tramite.getPaso()).append("?idInscripcion=").append(tramite.getIdInscripcion()).append("'\"/> ");
					sb.append("							</td>");
					sb.append(" 						<td align=\"center\"> ");
					sb.append(" 							<input type=\"button\" value=\"Eliminar\" class=\"boton\" onclick=\"window.location.href='"+Constants.getContextPath()+"/home/eliminarTP.do?idTramitePendiente=").append(tramite.getIdInscripcion()).append("'\" /> ");
					sb.append(" 						</td>");
					sb.append(" 					</tr>");
					sb.append("					</table>");
					sb.append(" 			</td> ");
					sb.append(" 		</tr>");
				}
				sb.append("			</table>");
				sb.append(" 	</td> ");
				sb.append("	</tr> ");
			}else{
				sb.append(" <tr>");
				sb.append("	<td colspan=\"6\">No existen Operaciones Pendientes.</td>");
				sb.append(" </tr>");
			}
			sb.append(" </table>");
		}catch(Exception e){
			e.printStackTrace();
		}	
		return sb.toString();
	}
	
	public String writeTablaPendientesFirma(List<OperacionesTO> tramitesPendientes){
		StringBuffer sb = new StringBuffer();
		try{
			sb.append(" <table class=\"mytabledaO\" width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" align=\"left\"> ");
			sb.append(" 	<thead> ");
			sb.append(" 		<tr height=\"25\"> ");
			sb.append(" 			<td width=\"92px\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">Tipo de Operación</td>");
			sb.append(" 			<td width=\"92px\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">Numero de Garantía</td>");
			sb.append(" 			<td width=\"496px\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">");
			sb.append(" 				<table style=\"width: 100%\" class=\"mytabledaO\"");
			sb.append(" 					<tr height=\"25\">");
			sb.append(" 						<td style=\"text-align: center\" colspan=\"2\"> <b>Datos de los Otorgantes</b> </td>");
			sb.append(" 					</tr>");
			sb.append(" 					<tr height=\"25\">");
			sb.append(" 						<td align=\"center\" width=\"50%\" style=\"padding-left: 11px;\">Nombre</td>");
			sb.append(" 						<td align=\"center\" width=\"50%\" style=\"padding-left: 11px;\">Folio Electronico</td>");
			sb.append(" 					</tr>");
			sb.append(" 				</table>");
			sb.append(" 			</td>");
			sb.append(" 			<td width=\"92px\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">Fecha de Inicio de la Vigencia</td>");
			sb.append(" 			<td width=\"132px\" class=\"encabezadoTablaResumen\" style=\"text-align: center\" colspan=\"2\">Opciones</td>");
			sb.append(" 		</tr> ");
			sb.append(" 	</thead>");
			sb.append(" 	<tr>");
			sb.append(" 		</td colspan=\"7\"> ");
			if (tramitesPendientes.size() > 0){
				sb.append(" 		<table>");
				for(OperacionesTO tramite : tramitesPendientes){
					sb.append(" 		<tr height=\"25\">");
					sb.append(" 			<td width=\"92px\" class=\"cuerpo1TablaResumen\">").append(tramite.getTipoTransaccion()).append(" </td>");
					sb.append(" 			<td width=\"92px\" class=\"cuerpo1TablaResumen\">");
					if(tramite.getTipoTransaccion().contains("Inscr")){
						sb.append(" 			<div align=\"center\"> ");
						sb.append(" 				<a style=\"cursor: pointer;\" tabindex=\"40\" href='"+Constants.getContextPath()+"/home/modalDetalle.do?idInscripcion="+tramite.getIdInscripcion()+"&keepThis=true&height=500&width=800&TB_iframe=true'"+"class=\"thickbox\">"+tramite.getNumGarantia()+ " </a> ");
						sb.append(" 			</div> ");
					}else{
						sb.append(" 			<div align=\"center\"> ");
													sb.append(tramite.getTipoTransaccion());
						sb.append(" 			</div> ");
					}
					sb.append(" 			</td> ");
					sb.append(" 			<td width=\"515px\" class=\"cuerpo1TablaResumen\">");
					sb.append(" 				<div align=\"center\">");
					sb.append("						<table width=\"100%\">");
					List<OtorganteTO> otorgantes = tramite.getOtorgantes();
					if(otorgantes.size()>0){
						for(OtorganteTO otorgante : otorgantes){
							sb.append(" 				<tr>");
							sb.append(" 					<td align=\"left\" width=\"50%\" style=\"padding-left: 11px;\" >").append(otorgante.getNombreCompleto()).append(" </td>");
							sb.append(" 					<td align=\"left\" width=\"50%\" style=\"padding-left: 11px;\" >").append(otorgante.getFolioMercantil()).append(" </td>");
							sb.append(" 				</tr>");
						}
						sb.append("					</table>");
						sb.append(" 			</div>");
						sb.append(" 		</td> ");
					}else{
						sb.append(" 					<tr>");
						sb.append(" 						<td class=\"cuerpo1TablaResumen\">&nbsp</td><td class=\"cuerpo1TablaResumen\">&nbsp</td>");
						sb.append(" 					</tr>");
						sb.append("					</table>");
						sb.append(" 			</div>");
						sb.append(" 		</td> ");
					}
					sb.append(" 			<td width=\"92px\" class=\"cuerpo1TablaResumen\">");
					sb.append(" 				<div align=\"center\">");
													sb.append(tramite.getFechaInicio());
					sb.append(" 				</div>");
					sb.append(" 			</td> ");
					sb.append(" 			<td width=\"132px\" class=\"cuerpo1TablaResumen\"> ");
					sb.append("					<table width=\"100%\">");
					sb.append(" 					<tr>");
					sb.append("							<td><input type=\"button\" value=\"Firmar\" class=\"boton\" onclick=\"window.location.href='"+Constants.getContextPath()).append(tramite.getPaso()).append("?idInscripcion=").append(tramite.getIdInscripcion()).append("'\"/> </td>");
					sb.append(" 						<td><input type=\"button\" value=\"Eliminar\" class=\"boton\" onclick=\"window.location.href='"+Constants.getContextPath()+"/home/eliminarTP.do?idTramitePendiente=").append(tramite.getIdInscripcion()).append("'\"/> </td>");
					sb.append(" 						</tr>");
					sb.append("					</table>");
					sb.append(" 			</td>");			
					sb.append(" 		</tr>");
				}
			}else{
				sb.append(" 			<tr>");
				sb.append("					<td colspan=\"6\" >No existen Operaciones Pendientes de Firma.</td>");
				sb.append(" 			</tr>");
			}
			sb.append("				</table>");
			sb.append(" 		</td> ");
			sb.append(" 	</tr> ");
			sb.append(" </table>");
		}catch(Exception e){
			e.printStackTrace();
		}	
		return sb.toString();
	}
	
	public String writeTablaPenAsientosMultiples(List<CargaMasivaResumenTO> tramitesPendientes){
		StringBuffer sb = new StringBuffer();
		try{
			sb.append(" <table class=\"mytabledaO\" width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" align=\"left\"> ");
			sb.append(" <thead> ");
			sb.append(" <tr height=\"50\"> ");
			sb.append(	"<th width=\"105px\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">Tipo de Asiento</th>");
			sb.append(" <th width=\"118px\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">Nombre del archivo</th>");
			sb.append(" <th width=\"66px\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">Clave de Rastreo</th>");
			sb.append(" <th width=\"105px\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">Tramites Correctos</th>");
			sb.append(" <th width=\"105px\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">Tramites Incorrectos</th>");
			sb.append(" <th width=\"105px\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">Opciones</th>");
			sb.append(" </tr>");
			sb.append(" </thead>");
			sb.append(" <tbody> ");
			if (tramitesPendientes.size() > 0){
				for(CargaMasivaResumenTO tramite : tramitesPendientes){
					sb.append(" <tr>");
					sb.append(" <td width=\"91px\" class=\"cuerpo1TablaResumen\"><div align=\"center\"> ").append(tramite.getTipoTramiteFirma()).append(" multiple").append("</div></td>");
					sb.append(" <td width=\"272px\" class=\"cuerpo1TablaResumen\"><div align=\"center\"> ").append(tramite.getNombreArchivo()).append("</div></td>");
					sb.append(" <td width=\"186px\" class=\"cuerpo1TablaResumen\"><div align=\"center\"> ").append(tramite.getClaveRastreo()).append("</div></td>");
					sb.append(" <td width=\"90px\" class=\"cuerpo1TablaResumen\"><div align=\"center\"> ").append(tramite.getBuenas()).append("</div></td>");
					sb.append(" <td width=\"90px\" class=\"cuerpo1TablaResumen\"><div align=\"center\"> ").append(tramite.getMalas()).append("</div></td>");
					sb.append(" <td width=\"194px\"class=\"cuerpo1TablaResumen\">");
					sb.append(" 	<table width=\"100%\">");
					sb.append(" 		<tr>");
					sb.append(" 			<td class=\"cuerpo1TablaResumen\"><input type=\"button\" value=\"Firmar\" onclick=\"window.location.href='"+Constants.getContextPath()+"/firma/firma.do?idTramite="+tramite.getIdFirmaMasivaTemp()+"'\"/></td> ");
					sb.append(" 			<td class=\"cuerpo1TablaResumen\"><input type=\"button\" value=\"Eliminar\" onclick=\"window.location.href='"+Constants.getContextPath()+"/home/eliminarTP.do?idTramitePendiente="+tramite.getIdFirmaMasivaTemp()+"'\"/></td> ");
					sb.append(" 		</tr>");
					sb.append(" 	</table>");
					sb.append(" </tr>");
					
				}
			}else{
				sb.append(" <tr>");
				sb.append("	<td colspan=\"6\" >No existen Operaciones Pendientes de Confirmación Registros Multiples.</td>");
				sb.append(" </tr>");
			}
			sb.append(" </tbody> ");
			sb.append(" </table>");
		}catch(Exception e){
			e.printStackTrace();
		}	
		return sb.toString();
	}
	
	public String writeTablaTerminadas(List<OperacionesTO> tramitesPendientes){
		StringBuffer sb = new StringBuffer();
		try{
			sb.append(" <table class=\"mytabledaO\" width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" align=\"left\"> ");
			sb.append(" <thead> ");
			sb.append(" <th width=\"105px\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">N&uacute;mero de Operaci&oacute;n</th>");
			sb.append(" <th width=\"118px\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">Tipo de Transacci&oacute;n</th>");
			sb.append(" <th width=\"66px\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">Fecha de Inicio</th>");
			sb.append(" <th width=\"105px\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">N&uacute;mero de Garant&iacute;a Mobiliaria</th>");
			sb.append(" <th width=\"539px\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">");
			sb.append(" 	<table width=\"100%\">");
			sb.append(" 		<tr height=\"25\">");
			sb.append(" 			<th class=\"encabezadoTablaResumen\" style=\"text-align: center\" colspan=\"2\"><b>Datos de los Otorgantes</b></th>");
			sb.append(" 		</tr>");
			sb.append(" 		<tr height=\"25\">");
			sb.append(" 			<th width=\"50%\" style=\"text-align: center\">Nombre</th>");
			sb.append(" 			<th width=\"50%\" style=\"text-align: center\">Folio Electr&oacute;nico</th>");
			sb.append(" 		</tr>");
			sb.append(" 	</table>");
			sb.append(" </th>");
//			sb.append(" <th class=\"cuerpo1TablaResumen\" style=\"width: 10px;\">&nbsp;</th>");
			sb.append(" </thead>");
			sb.append(" <tbody> ");
			if (tramitesPendientes.size() > 0){
				for(OperacionesTO tramite : tramitesPendientes){
					sb.append(" <tr height=\"25\">");
					if(tramite.getIdTipoTramite()==3){
						sb.append(" <td width=\"105px\" class=\"cuerpo1TablaResumen\"><div align=\"center\"><a style=\"cursor: pointer;\" onclick=\"window.location.href='"+Constants.getContextPath()+"/home/detalleAviso.do?idTramite="+tramite.getIdInscripcion()+"'\">"+tramite.getIdInscripcion()+"</a></div></td>"); 
					}else if(tramite.getIdTipoTramite()==2){
						sb.append(" <td width=\"105px\" class=\"cuerpo1TablaResumen\"><div align=\"center\"><a style=\"cursor: pointer;\" onclick=\"window.location.href='"+Constants.getContextPath()+"/home/detalle.do?idGarantia="+tramite.getNumGarantia()+"&idTramite="+tramite.getIdInscripcion()+"'\">"+tramite.getIdInscripcion()+"</a></div></td>");
					}else if(tramite.getIdTipoTramite()==26 || tramite.getIdTipoTramite()==27 || tramite.getIdTipoTramite()== 28 || tramite.getIdTipoTramite()== 29 || tramite.getIdTipoTramite()==10 ){
						sb.append(" <td width=\"105px\" class=\"cuerpo1TablaResumen\"><div align=\"center\"><a style=\"cursor: pointer;\" onclick=\"window.location.href='"+Constants.getContextPath()+"/anotacion/detalleAnotacion.do?idTramite="+tramite.getIdInscripcion()+"'\">"+tramite.getIdInscripcion()+"</a></div></td>");	
					}else{
						sb.append(" <td width=\"105px\" class=\"cuerpo1TablaResumen\"><div align=\"center\"><a style=\"cursor: pointer;\" onclick=\"window.location.href='"+Constants.getContextPath()+"/home/detalle.do?idGarantia="+tramite.getNumGarantia()+"&idTramite="+tramite.getIdInscripcion()+"'\">"+tramite.getIdInscripcion()+"</a></div></td>");
					}
					if(tramite.getIdTipoTramite()==10){
						sb.append(" <td  width=\"118px\"  class=\"cuerpo1TablaResumen\"><div align=\"center\">Anotaci&oacute;n</div></td> ");
					}else{
						sb.append(" <td  width=\"118px\"  class=\"cuerpo1TablaResumen\"><div align=\"center\">"+tramite.getTipoTransaccion()+"</div></td> ");
					}
					sb.append(" <td width=\"66px\" class=\"cuerpo1TablaResumen\" ><div align=\"center\">").append(tramite.getFechaOperacionInicio()).append("<br> </div> </td>");
					sb.append(" <td width=\"105px\" class=\"cuerpo1TablaResumen\"><div align=\"center\"> ").append(tramite.getNumGarantia()).append("</div></td>");
					sb.append(" <td width=\"539px\"class=\"cuerpo1TablaResumen\">");
					sb.append(" 	<div align=\"center\">");
					sb.append(" 		<table width=\"100%\">");
					List<OtorganteTO> otorgantes = tramite.getOtorgantes();
					if(otorgantes.size()>0){
						for(OtorganteTO otorgante : otorgantes){
							sb.append(" 		<tr>");
							sb.append(" 			<td align=\"left\" width=\"50%\" style=\"padding-left: 11px;\">").append(otorgante.getNombreCompleto()).append("<br></br>").append(" </td>");
							sb.append(" 			<td align=\"left\" width=\"50%\" style=\"padding-left: 11px;\">").append(otorgante.getFolioMercantil()).append("<br></br>").append(" </td>");
							sb.append(" 		</tr>");
						}
					}else{
						sb.append(" <td class=\"cuerpo1TablaResumen\">&nbsp</td><td class=\"cuerpo1TablaResumen\">&nbsp</td>");
					}
					sb.append(" 		</table>");
					sb.append(" 	</div>");
					sb.append(" </td>");
					sb.append(" </tr>");
				}
			}else{
				sb.append(" <tr>");
				sb.append("	<td colspan=\"6\" >No existen Operaciones Terminadas.</td>");
				sb.append(" </tr>");
			}
			sb.append(" </tbody> ");
			sb.append(" </table>");
		}catch(Exception e){
			e.printStackTrace();
		}	
		return sb.toString();
	}

}
