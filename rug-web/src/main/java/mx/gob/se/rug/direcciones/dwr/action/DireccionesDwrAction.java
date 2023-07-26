package mx.gob.se.rug.direcciones.dwr.action;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import mx.gob.economia.dgi.framework.dwr.action.AbstractBaseDwrAction;
import mx.gob.se.rug.to.MessageDwr;
import mx.gob.se.rug.util.MyLogger;
import mx.gob.se.rug.util.dao.CodigoPostalDAO;
import mx.gob.se.rug.util.to.CodigoPostalTO;
import mx.gob.se.rug.util.to.ColoniaTO;
import mx.gob.se.rug.util.to.EstadoTO;
import mx.gob.se.rug.util.to.LocalidadTO;
import mx.gob.se.rug.util.to.MunicipioTO;

public class DireccionesDwrAction extends AbstractBaseDwrAction {
	
	public MessageDwr codigoPostal(String codigoPostal){
		MessageDwr dwr = new MessageDwr();
		try{
			CodigoPostalDAO codigoPostalDAO = new CodigoPostalDAO();
			MyLogger.Logger.log(Level.INFO, "selecciono");
			CodigoPostalTO codigoPostalTO = codigoPostalDAO.getByCodigoPostal(codigoPostal);
			dwr.setCodeError(0);
			dwr.setMessage(writeAllCP(codigoPostalTO));
		}catch(Exception e){
			dwr.setCodeError(1);
			dwr.setMessage("No existe el CP");
			e.printStackTrace();
		}
		return dwr;
	}
	
	public MessageDwr getMunicipios(String idEstado){
		MessageDwr dwr = new MessageDwr();
		try{
			CodigoPostalDAO codigoPostalDAO = new CodigoPostalDAO();
			List<MunicipioTO> municipioTOs = codigoPostalDAO.getMunicipiosByCve(idEstado);
			dwr.setCodeError(0);
			dwr.setMessage(writeMunicipio(municipioTOs, new Integer(1)).toString());
		}catch(Exception e){
			dwr.setCodeError(1);
			dwr.setMessage("Error en seleccion de EDO");
			e.printStackTrace();
		}
		return dwr;
	}
	public MessageDwr getColoniaLocalidad(String idMunicipio,String idEstado){
		MessageDwr dwr = new MessageDwr();
		try{
			CodigoPostalDAO codigoPostalDAO = new CodigoPostalDAO();
			CodigoPostalTO codigoPostalTO= new CodigoPostalTO();
			List<ColoniaTO> coloniaTOs = codigoPostalDAO.getColoniasByMunicipio(new Integer(idMunicipio), idEstado);
			List<LocalidadTO> localidadTOs= codigoPostalDAO.getLocalidadesByMunicipio(new Integer(idMunicipio), idEstado);
			codigoPostalTO.setColoniaTOs(coloniaTOs);
			codigoPostalTO.setLocalidadTOs(localidadTOs);
			dwr.setCodeError(0);
			dwr.setMessage(writeColoniaLocalidad(codigoPostalTO).toString());
		}catch(Exception e){
			dwr.setCodeError(1);
			dwr.setMessage("Error en seleccion de EDO");
			e.printStackTrace();
		}
		return dwr;
	}
	public MessageDwr getCPByColonia(String idColonia){
		MessageDwr dwr = new MessageDwr();
		try{
			CodigoPostalDAO codigoPostalDAO = new CodigoPostalDAO();
			CodigoPostalTO codigoPostalTO= codigoPostalDAO.getCodigoPostalByIdColonia(new Integer(idColonia));
			dwr.setCodeError(0);
			dwr.setMessage(codigoPostalTO.getCodigoPostal());
		}catch(Exception e){
			dwr.setCodeError(1);
			dwr.setMessage("Error en seleccion de colonia");
			e.printStackTrace();
		}
		return dwr;
	}
	public MessageDwr getCPByLocalidad(String idLocalidad){
		MessageDwr dwr = new MessageDwr();
		try{
			CodigoPostalDAO codigoPostalDAO = new CodigoPostalDAO();
			CodigoPostalTO codigoPostalTO= codigoPostalDAO.getCodigoPostalByIdLocalidad(new Integer(idLocalidad));
			dwr.setCodeError(0);
			dwr.setMessage(codigoPostalTO.getCodigoPostal());
		}catch(Exception e){
			dwr.setCodeError(1);
			dwr.setMessage("Error en seleccion de colonia");
			e.printStackTrace();
		}
		return dwr;
	}
	

	
	//Armado de HTML---------------------------------------------------------------
	private String writeAllCP(CodigoPostalTO codigoPostalTO){
		StringBuffer sb= new StringBuffer();
		List<EstadoTO> estadoTOs = codigoPostalTO.getEstadoTOs();
		List<MunicipioTO> municipioTOs = codigoPostalTO.getMunicipioTOs();
		sb.append("<table class='textoGeneralRojo'><tr>" +
				"<td>Estado:<input type=\"hidden\" name=\"txtConsultaCodPostalExito\" value=\"true\"/></td>" +
				"<td id='estadoDIV'>" );
		sb.append(writeEstado(estadoTOs, codigoPostalTO.getCve_estado()));
				sb.append("</td>" +
				"</tr>" +
				
				"<tr>" +
				"<td>Delegaci&oacute;n/Municipio:</td>" +
				"<td id='municipioDIV'>");
				sb.append(writeMunicipio(municipioTOs,new Integer( codigoPostalTO.getCve_municipio())));
				sb.append("</td>" +
				"</tr>" +
				
				"<tr>" +
				"<td id='coloniaLocalidadDIV' colspan='2'>") ;
			sb.append(writeColoniaLocalidad(codigoPostalTO));
				sb.append("</td>" +
				"</tr>" +
				"</table>");
		return sb.toString();
	}
	
	private StringBuffer writeEstado(List<EstadoTO> estadoTOs,String cve_estado){
		StringBuffer sb= new StringBuffer();
		Iterator<EstadoTO> iterator= estadoTOs.iterator();
		sb.append("<select id='idEstado' name='idEstado' onchange='cargaMunicipioDWR();'> " );		
		sb.append("<option value='-1'>SELECCIONA</option>");
		while (iterator.hasNext()) {
			EstadoTO estadoTO = (EstadoTO) iterator.next();
			sb.append("<option " );
			if(estadoTO.getIdEstado().equalsIgnoreCase(cve_estado)){
			sb.append(" selected='selected' " );
			}
			sb.append("value='");
			sb.append(estadoTO.getIdEstado());
			sb.append("'>");
			sb.append(estadoTO.getDescEstado());
			sb.append("</option>");
	
		}
		sb.append("</select> " );		
		return sb;
	}
	private StringBuffer writeMunicipio(List<MunicipioTO> municipioTOs,Integer cve_municipio){
		StringBuffer sb= new StringBuffer();
		Iterator<MunicipioTO> iterator= municipioTOs.iterator();
		sb.append("<select id='idMunicipio' name='idMunicipio' onchange='cargaColoniaLocalidadDWR();'>" );
		sb.append("<option value='-1'>SELECCIONA</option>");
		while (iterator.hasNext()) {
			MunicipioTO municipioTO = (MunicipioTO) iterator.next();
			sb.append("<option " );
			if(municipioTO.getIdMunicipio().intValue()==cve_municipio.intValue()){
				sb.append(" selected='selected' " );
			}
			sb.append("value='");
			sb.append(municipioTO.getIdMunicipio());
			sb.append("'>");
			sb.append(municipioTO.getDescMunicipio());
			sb.append("</option>");
			
		}
		sb.append("</select> " );		
		return sb;
	}
	
private StringBuffer writeColoniaLocalidad(CodigoPostalTO codigoPostalTO){
	StringBuffer sb= new StringBuffer();
	sb.append("<table width='100%' class='textoGeneralRojo'><tr>" +
	"<td>Colonia:</td>" +
	"<td id='coloniaDIV'>" );
	if(codigoPostalTO.getCve_colonia()!=null){
		sb.append(writeColonia(codigoPostalTO.getColoniaTOs(),new Integer( codigoPostalTO.getCve_colonia())));
	}else{
		sb.append(writeColonia(codigoPostalTO.getColoniaTOs(),new Integer(999999)));
	}
	sb.append("</td>" +
	"</tr>" +
	"<tr>" +
	"<td align='center' colspan='2'>" +
	"o" +
	"</td>" +
	"</tr>" +
	"<tr>" +
	"<td>Localidad:</td>" +
	"<td id='localidadDIV'>" );
	
	if(codigoPostalTO.getCve_localidad()!=null){
		
		sb.append(writeLocalidad(codigoPostalTO.getLocalidadTOs(),new Integer( codigoPostalTO.getCve_localidad())));
	}else{
		sb.append(writeLocalidad(codigoPostalTO.getLocalidadTOs(),new Integer( 999999)));
		
	}
	
	
	sb.append("</td>" +
	"</tr>" +
	"</table>");
	return sb;
}
private StringBuffer writeColonia(List<ColoniaTO> coloniaTOs,Integer cve_colonia){
	StringBuffer sb= new StringBuffer();
	Iterator<ColoniaTO> iterator= coloniaTOs.iterator();
	sb.append("<select id='idColonia' name='idColonia' onchange='setCPbyColonia();'>" );
	sb.append("<option value='-1'>SELECCIONA</option>");
	while (iterator.hasNext()) {
		ColoniaTO coloniaTO = (ColoniaTO) iterator.next();
		sb.append("<option " );
		if(coloniaTO.getIdColonia().intValue()==cve_colonia.intValue()){
			sb.append(" selected='selected' " );
		}
		sb.append("value='");
		sb.append(coloniaTO.getIdColonia());
		sb.append("'>");
		sb.append(coloniaTO.getDescColonia());
		sb.append("</option>");
		
	}
	sb.append("</select> " );		
	return sb;
}

private StringBuffer writeLocalidad(List<LocalidadTO> localidadTOs,Integer cve_localidad){
	StringBuffer sb= new StringBuffer();
	Iterator<LocalidadTO> iterator= localidadTOs.iterator();
	sb.append("<select id='idLocalidad' name='idLocalidad' onchange='setCPbyLocalidad();'>" );
	sb.append("<option value='-1'>SELECCIONA</option>");
	while (iterator.hasNext()) {
		LocalidadTO localidadTO = (LocalidadTO) iterator.next();
		sb.append("<option " );
		if(localidadTO.getIdLocalidad()==cve_localidad.intValue()){
			sb.append(" selected='selected' " );
		}
		sb.append("value='");
		sb.append(localidadTO.getIdLocalidad());
		sb.append("'>");
		sb.append(localidadTO.getDescLocalidad());
		sb.append("</option>");
		
	}
	sb.append("</select> " );		
	return sb;
}
}
