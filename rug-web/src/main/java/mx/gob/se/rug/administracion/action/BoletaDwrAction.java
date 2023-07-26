package mx.gob.se.rug.administracion.action;

import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import gt.gob.rgm.service.BoletaService;
import mx.gob.economia.dgi.framework.dwr.action.AbstractBaseDwrAction;
import mx.gob.se.rug.garantia.dao.GarantiasDAO;
import mx.gob.se.rug.garantia.to.BoletaPagoTO;
import mx.gob.se.rug.to.AccionTO;
import mx.gob.se.rug.to.MessageDwr;

public class BoletaDwrAction extends AbstractBaseDwrAction {
	
	BoletaService boletaSvc;
	List<BoletaPagoTO> listaBoletas;
	List<BoletaPagoTO> listaBoletasSinFirma;
	List<AccionTO> listaTramitesR;
	
	private static DecimalFormat df2 = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.ENGLISH));
	private static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
	
	public MessageDwr iniciaPagBoletas(Integer idPersona, Integer tipoOperacion, String filtro) {
		MessageDwr dwr = new MessageDwr();
		dwr.setCodeError(0);
		String metodoJS="";
		Integer countNumOperaciones=0;
		JsonReader jsonReader;
		JsonObject jsonFiltro = null;
		
		try{
			GarantiasDAO garantiasDAO = new GarantiasDAO();
			System.out.println(" Operacion " + tipoOperacion);
			switch(tipoOperacion) {
			//el caso 1 es para las boletas aprobadas
			case 1:
				countNumOperaciones = garantiasDAO.getCountBoletasByUsuario(idPersona, 1, filtro);				
				metodoJS= "pagBoletasAprobadas";
				break;
			case 2:
				countNumOperaciones = garantiasDAO.getCountBoletasByUsuario(idPersona, 0, filtro);
				metodoJS= "pagBoletasPendientes";
				break;
			case 3:
				jsonReader = Json.createReader(new StringReader(filtro));
				jsonFiltro = jsonReader.readObject();
				countNumOperaciones = garantiasDAO.getCountTramitesEfectuadosOptimizado(idPersona.longValue(), jsonFiltro);
				metodoJS= "pagTramitesRealizados";
				break;
			default:
				break;
			}
			
			StringBuffer sb = new StringBuffer();
			int numeroPaginas = 0;
			int registroTotales = 0;
			Integer registroPaginas = 15;
			if (countNumOperaciones>0){				
				numeroPaginas = countNumOperaciones/registroPaginas;
				registroTotales = countNumOperaciones;
			}
			if (registroTotales % registroPaginas > 0){
				numeroPaginas++;
			}
			if (countNumOperaciones>0){
				switch (tipoOperacion) {
				case 1:
					sb.append(writeTablaAprobadas(garantiasDAO.getBoletasByUsuario(idPersona, 1, filtro, 1, registroPaginas>registroTotales?registroTotales:registroPaginas)));
					sb.append(writeSeccionPaginado(numeroPaginas, 1, registroPaginas, registroTotales, metodoJS, filtro));
					break;
				case 2:
					sb.append(writeTablaAprobadas(garantiasDAO.getBoletasByUsuario(idPersona, 0, filtro, 1, registroPaginas>registroTotales?registroTotales:registroPaginas)));
					sb.append(writeSeccionPaginado(numeroPaginas, 1, registroPaginas, registroTotales, metodoJS, filtro));
					break;
				case 3:
					//sb.append(writeTablaTramites(garantiasDAO.getTramitesEfectuados(idPersona, filtro, 1, registroPaginas>registroTotales?registroTotales:registroPaginas)));
					System.out.println(" tabla " + numeroPaginas);
					sb.append(writeTablaTramites(garantiasDAO.getTramitesEfectuadosOptimizado(idPersona.longValue(), jsonFiltro, 1, registroPaginas > registroTotales ? registroTotales : registroPaginas)));
					sb.append(writeSeccionPaginadoTramitesRealizados(numeroPaginas, 1, registroPaginas, registroTotales, metodoJS, jsonFiltro.getString("nombre"), jsonFiltro.getString("fechaInicial"), jsonFiltro.getString("fechaFinal")));
					break;
				default:
					break;
				}
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
	
	public MessageDwr pagBoletasAprobadasFiltro(String idPersonaString, String filtro, String registroTotalesString, String pagActivaString, String regPaginaString ) {
		MessageDwr dwr = new MessageDwr();
		
		int pagActiva = Integer.valueOf(pagActivaString);
		int regPagina = Integer.valueOf(regPaginaString);
		int registroTotales = Integer.valueOf(registroTotalesString);
		int numeroPaginas = registroTotales/regPagina;
		int idPersona = Integer.valueOf(idPersonaString);
		
		GarantiasDAO garantiasDAO = new GarantiasDAO();
		
		if ( registroTotales %regPagina > 0){
			numeroPaginas++;
		}
		dwr.setCodeError(0);
		if (numeroPaginas < pagActiva){
			pagActiva = 1;
		}
				
		try {			
			int inicio = 1;
			int regFinal;
			
			if (pagActiva != 1){
				inicio = ((pagActiva-1)*regPagina) + 1;				
			}
			regFinal = inicio + (regPagina-1);
			
			listaBoletas = garantiasDAO.getBoletasByUsuario(idPersona, 1, filtro, inicio, regFinal);
			StringBuffer sb = new StringBuffer();
			
			sb.append(writeTablaAprobadas(listaBoletas));	
			sb.append(writeSeccionPaginado(numeroPaginas, pagActiva, regPagina,registroTotales,"pagBoletasAprobadas",filtro));
			dwr.setMessage(sb.toString());
		} catch (Exception  e) {
			e.printStackTrace();
		}	
		return dwr;
	}
	
	public MessageDwr pagBoletasPendientesFiltro(String idPersonaString, String filtro, String registroTotalesString, String pagActivaString, String regPaginaString ) {
		MessageDwr dwr = new MessageDwr();
		
		int pagActiva = Integer.valueOf(pagActivaString);
		int regPagina = Integer.valueOf(regPaginaString);
		int registroTotales = Integer.valueOf(registroTotalesString);
		int numeroPaginas = registroTotales/regPagina;
		int idPersona = Integer.valueOf(idPersonaString);
		
		GarantiasDAO garantiasDAO = new GarantiasDAO();
		
		if ( registroTotales %regPagina > 0){
			numeroPaginas++;
		}
		dwr.setCodeError(0);
		if (numeroPaginas < pagActiva){
			pagActiva = 1;
		}
				
		try {			
			int inicio = 1;
			int regFinal;
			
			if (pagActiva != 1){
				inicio = ((pagActiva-1)*regPagina) + 1;				
			}
			regFinal = inicio + (regPagina-1);
			
			listaBoletas = garantiasDAO.getBoletasByUsuario(idPersona, 0, filtro, inicio, regFinal);
			StringBuffer sb = new StringBuffer();
			
			sb.append(writeTablaAprobadas(listaBoletas));	
			sb.append(writeSeccionPaginado(numeroPaginas, pagActiva, regPagina,registroTotales,"pagBoletasPendientes",filtro));
			dwr.setMessage(sb.toString());
		} catch (Exception  e) {
			e.printStackTrace();
		}	
		return dwr;
	}
	
	public MessageDwr pagTramitesRealizadosFiltro(String idPersonaString, String nombre, String fechaInicial, String fechaFinal, String registroTotalesString, String pagActivaString, String regPaginaString ) {
		MessageDwr dwr = new MessageDwr();
		
		int pagActiva = Integer.valueOf(pagActivaString);
		int regPagina = Integer.valueOf(regPaginaString);
		int registroTotales = Integer.valueOf(registroTotalesString);
		int numeroPaginas = registroTotales/regPagina;
		long idPersona = Long.valueOf(idPersonaString);
		
		GarantiasDAO garantiasDAO = new GarantiasDAO();
		
		if ( registroTotales %regPagina > 0){
			numeroPaginas++;
		}
		dwr.setCodeError(0);
		if (numeroPaginas < pagActiva){
			pagActiva = 1;
		}
				
		try {			
			int inicio = 1;
			int regFinal;
			
			if (pagActiva != 1){
				inicio = ((pagActiva-1)*regPagina) + 1;				
			}
			regFinal = inicio + (regPagina-1);
			
			JsonObject jsonFiltro = Json.createObjectBuilder()
					.add("nombre", nombre)
					.add("fechaInicial", fechaInicial)
					.add("fechaFinal", fechaFinal)
					.build();
			listaTramitesR = garantiasDAO.getTramitesEfectuadosOptimizado(idPersona, jsonFiltro, inicio, regFinal);
			StringBuffer sb = new StringBuffer();
			
			sb.append(writeTablaTramites(listaTramitesR));	
			sb.append(writeSeccionPaginadoTramitesRealizados(numeroPaginas, pagActiva, regPagina,registroTotales,"pagTramitesRealizados",nombre, fechaInicial, fechaFinal));
			dwr.setMessage(sb.toString());
		} catch (Exception  e) {
			e.printStackTrace();
		}	
		return dwr;
	}
	
	public String writeTablaTramites(List<AccionTO> listaTramites) {
		StringBuffer sb = new StringBuffer();
		
		try{
			sb.append(" <table id=\"boleta\" class=\"table\" data-sorting=\"true\"> ");			
			sb.append(" <thead> <tr>");			
			sb.append(" <th>N&uacute;mero de Garant&iacute;a</th>");
			sb.append(" <th>Tr&aacute;mite</th>");
			sb.append(" <th>Fecha</th>");
			sb.append(" <th>Valor</th>");
			sb.append(" <th>Usuario</th>");
			sb.append(" </tr></thead>");
			sb.append(" <tbody> ");
			if (listaTramites.size() > 0) {
				for(AccionTO tramite : listaTramites) {
					System.out.println("Valor del campo " + tramite.getImporte());
					
					sb.append(" <tr>");
					// sb.append(" <td style=\"vertical-align: middle;\"><div align=\"left\">").append(tramite.getGarantia().getRow()).append("</div> </td>");
					sb.append(" <td style=\"vertical-align: middle;\"><div align=\"left\">").append(tramite.getGarantia().getIdgarantia()).append("</div> </td>");
					sb.append(" <td style=\"vertical-align: middle;\"><div align=\"left\">").append(tramite.getGarantia().getDescripcion()).append("</div> </td>");
					sb.append(" <td style=\"vertical-align: middle;\"><div align=\"left\">").append(formatter.format(tramite.getFecha())).append("</div> </td>");
					sb.append(" <td style=\"vertical-align: middle;\"><div align=\"left\">").append("Q. ").append(tramite.getImporte()).append("</div> </td>");
//					sb.append(" <td style=\"vertical-align: middle;\"><div align=\"left\">").append("Q. ").append(df2.format(tramite.getImporte())).append("</div> </td>");
					sb.append(" <td style=\"vertical-align: middle;\"><div align=\"left\">").append(tramite.getUsuario().getNombre()).append("</div> </td>");
					sb.append(" </tr>");
				}
			} else {
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
	
	public String writeTablaAprobadas(List<BoletaPagoTO> listaBoletas) {
		StringBuffer sb = new StringBuffer();
		
		try{
			sb.append(" <table id=\"boleta\" class=\"table\" data-sorting=\"true\"> ");			
			sb.append(" <thead> <tr>");			
			sb.append(" <th>N&uacute;mero de Boleta</th>");
			sb.append(" <th>Monto</th>");
			sb.append(" <th>Usuario</th>");
			sb.append(" </tr></thead>");
			sb.append(" <tbody> ");
			if (listaBoletas.size() > 0) {
				for(BoletaPagoTO boleta : listaBoletas) {
					sb.append(" <tr>");
					sb.append(" <td style=\"vertical-align: middle;\"><div align=\"left\">").append(boleta.getNumero()).append("</div> </td>");
					sb.append(" <td style=\"vertical-align: middle;\"><div align=\"left\">").append("Q. ").append(df2.format(boleta.getMonto())).append("</div> </td>");
					sb.append(" <td style=\"vertical-align: middle;\"><div align=\"left\">").append(boleta.getNombrePersonaCarga()).append("</div> </td>");
					sb.append(" </tr>");
				}
			} else {
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
	
	public String writeSeccionPaginadoTramitesRealizados(int numeroPaginas,int pagActiva, int regPagina, 
			int registroTotales, String metodoJs, String nombre, String fechaInicial, String fechaFinal){
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
					.append("'").append(nombre).append("',")
					.append("'").append(fechaInicial).append("',")
					.append("'").append(fechaFinal).append("'")
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
						.append("'").append(nombre).append("',")
						.append("'").append(fechaInicial).append("',")
						.append("'").append(fechaFinal).append("'")
						.append(")\">").append(x).append("</a></li>");
					}
				}
				if (pagActiva < numeroPaginas){
					sb.append("<li><a onclick=\"").append(metodoJs).append("(")
					.append(registroTotales).append(",")
					.append(pagActiva+1).append(",")
					.append(regPagina).append(",")
					.append("'").append(nombre).append("',")
					.append("'").append(fechaInicial).append("',")
					.append("'").append(fechaFinal).append("'")
					.append(")\">").append(" Siguiente <span class=\"icon-fast-forward\"> </span>  ").append("</a></li>");
				}
				
			}else{
				for (int x=1;x<=numeroPaginas; x++){
					if (x==pagActiva){
						sb.append("<li class=\"active\"><a href=\"#\">").append(x).append("</a></li>");
					}else{
						sb.append("<li class=\"waves-effect\"><a onclick=\"").append(metodoJs).append("(")
						.append(registroTotales).append(",")
						.append(x).append(",")
						.append(regPagina).append(",")
						.append("'").append(nombre).append("',")
						.append("'").append(fechaInicial).append("',")
						.append("'").append(fechaFinal).append("'")
						.append(")\">").append(x).append("</a></li>");
					}
				}
			}
			sb.append(" </ul> </div> </td> </tr> </table>");
		}
		
		return sb.toString();
	}
	
	public MessageDwr actualizaEstadoBoleta(Long id, String estado) {
		MessageDwr response = new MessageDwr();
		
		Integer usada = estado.equals("A") ? 1 : -1;
		boletaSvc.update(id, usada);
		response.setMessage("Boleta actualizada exitosamente");
		response.setData(id);
		return response;
	}
	
	public BoletaService getBoletaSvc() {
		return boletaSvc;
	}

	public void setBoletaSvc(BoletaService boletaSvc) {
		this.boletaSvc = boletaSvc;
	}
}
