package mx.gob.se.rug.boleta.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import mx.gob.se.rug.boleta.dao.CamposDAO;
import mx.gob.se.rug.boleta.dao.PartesDAO;
import mx.gob.se.rug.boleta.dao.SeccionDAO;
import mx.gob.se.rug.boleta.to.BoletaInfoTO;
import mx.gob.se.rug.boleta.to.CampoTO;
import mx.gob.se.rug.boleta.to.SeccionTO;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.exception.InfrastructureException;
import mx.gob.se.rug.exception.NoDataFoundException;
import mx.gob.se.rug.util.MyLogger;

public class BoletaInfService {
	private BoletaInfoTO getTemplateBoleta(Integer idBoleta) {
		BoletaInfoTO boletaInfoTO = new BoletaInfoTO();
		boletaInfoTO.setIdBoleta(idBoleta);
		SeccionDAO seccionDAO = new SeccionDAO();
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		try {
			seccionDAO.getSeccionesbyIdBoleta(boletaInfoTO, connection);
			getCampos(boletaInfoTO, connection);
		} catch (InfrastructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoDataFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection, null, null);
		}

		
		return boletaInfoTO;
	}
	
	public String getTemplateParte(Integer idBoleta,Integer idParte,Integer idTipoGarantia,Connection connection) throws InfrastructureException, NoDataFoundException{
		StringBuffer sb = new StringBuffer();
	    PartesDAO partesDAO= new PartesDAO();
	    List<CampoTO> campoTOs=partesDAO.getCampoPartes(idBoleta, idParte, connection);
	    
	    if(idTipoGarantia != null && idParte == 4 &&idTipoGarantia !=8){//Arrendamiento Financiero
	    	for (CampoTO campoTO : campoTOs) {
				if(campoTO.getIdCampo() == 7){//Folio electronico de Acreedor
					campoTO.setMostrar(false);
				}
			}
	    }
	    
	    sb=getCampos(campoTOs);
		return sb.toString();
		
	}
	
	private BoletaInfoTO getCampos(BoletaInfoTO boletaInfoTO,
			Connection connection) throws InfrastructureException, NoDataFoundException {
		Iterator<SeccionTO> iterator=boletaInfoTO.getSeccionTOs().iterator();
		CamposDAO camposDAO = new CamposDAO();
		
		List<SeccionTO> seccionTOs= new ArrayList<SeccionTO>();
		while (iterator.hasNext()) {
			SeccionTO seccionTO = (SeccionTO) iterator.next();
			seccionTO.setCampoTOs(camposDAO.getCamposbyIdBoletaIdSeccion(boletaInfoTO.getIdBoleta(), seccionTO.getIdSeccion(), connection));
			seccionTOs.add(seccionTO);
		}
		boletaInfoTO.setSeccionTOs(seccionTOs);
		return boletaInfoTO;
	}
	
	public StringBuffer getHtmlInfo(Integer  idBoleta){
		StringBuffer sb = new  StringBuffer();
		
		BoletaInfoTO boletaInfoTO=getTemplateBoleta(idBoleta);
		
		List<SeccionTO> seccionTOs= boletaInfoTO.getSeccionTOs();
		Iterator< SeccionTO> iterator=seccionTOs.iterator();
		while (iterator.hasNext()) {
			SeccionTO seccionTO = (SeccionTO) iterator.next();
		    sb.append("<table class=\"seccionTableClass\" >");
		    sb.append("<tbody>");
		    if(!seccionTO.getLabel().trim().equalsIgnoreCase("")){
		    	sb.append("<tr><td class=\"tituloSeccion\">");
		    	sb.append(seccionTO.getLabel());
		    	sb.append("</td></tr>");
		    }
		    sb.append("<tr><td>");
	    	sb.append(getCampos(seccionTO.getCampoTOs()));
	    	sb.append("</td></tr>");
		    sb.append("</tbody>");
		    sb.append("</table>");
		}
		MyLogger.Logger.log(Level.INFO, sb.toString());
		return sb;
	}
	
	private StringBuffer getCampos(List<CampoTO> campoTOs){
		StringBuffer sb = new  StringBuffer();
		Iterator< CampoTO> iterator= campoTOs.iterator();
	    sb.append("<table class=\"camposTableClass\">");
	    sb.append("<tbody>");
		while (iterator.hasNext()) {
			CampoTO campoTO = (CampoTO) iterator.next();
			if(campoTO.getMostrar()){
				if(campoTO.getOrientacion().equalsIgnoreCase("H")){//Horizontal
					sb.append(getCampoHorizontal(campoTO));
				}else if (campoTO.getOrientacion().equalsIgnoreCase("V")) {//vertical
					sb.append(getCampoVertical(campoTO));
				}
			}
			
		}
	    sb.append("</tbody>");
	    sb.append("</table>");
		return sb;
	}
	private StringBuffer getCampoVertical(CampoTO campoTO){
		StringBuffer  sb = new StringBuffer();
	    sb.append("<tr><td colspan=\"2\" class=\"tituloCampo\">");
    	sb.append(campoTO.getLabel());
    	sb.append("</td></tr>");
    	sb.append("<tr><td  colspan=\"2\">[*");
    	sb.append(campoTO.getCveCampo());
    	sb.append("*]</td></tr>");
		return sb;
	}
	private StringBuffer getCampoHorizontal(CampoTO campoTO){
		StringBuffer  sb = new StringBuffer();
		if(campoTO.getCveCampo().equalsIgnoreCase("1")||campoTO.getCveCampo().equalsIgnoreCase("2")||campoTO.getCveCampo().equalsIgnoreCase("3")||campoTO.getCveCampo().equalsIgnoreCase("4")){
	    sb.append("<tr><td  >[*");
    	sb.append(campoTO.getCveCampo());
    	sb.append("*]</td></tr>");
		}else{
			  sb.append("<tr><td class=\"tituloCampo\">");
		    	sb.append(campoTO.getLabel());
		    	sb.append("</td><td  >[*");
		    	sb.append(campoTO.getCveCampo());
		    	sb.append("*]</td></tr>");
		}
		return sb;
	}
	
}
