package mx.gob.se.rug.masiva.dwr.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import mx.gob.economia.dgi.framework.dwr.action.AbstractBaseDwrAction;
import mx.gob.se.rug.acreedores.service.impl.AcreedoresServiceImpl;
import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.garantia.dao.GarantiasDAO;
import mx.gob.se.rug.masiva.dao.MasivaDAO;
import mx.gob.se.rug.to.MessageDwr;
import mx.gob.se.rug.util.CharSetUtil;
import mx.gob.se.rug.util.MyLogger;

public class MasivaDwrAction extends AbstractBaseDwrAction{
	public MessageDwr getCargaMasiva(String idAcreedor, String idUsuario){
		MessageDwr messageDwr = new MessageDwr();
		MyLogger.Logger.log(Level.INFO,"entro a MasivaDwrAction.getCargaMasiva v1="+ idAcreedor +"v2="+idUsuario);
		try{
			messageDwr.setMessage(writeBodyHtml(Integer.valueOf(idAcreedor), Integer.valueOf(idUsuario)));
			messageDwr.setCodeError(0);
		}catch(Exception e){
			messageDwr.setMessage(" <script> alert('"+e.getMessage()+"') </script>");
			e.printStackTrace();
			
		}
		
		return messageDwr;
	}
	
	public MessageDwr writeInputIdAcreedor(String idUsuario){
		MessageDwr messageDwr = new MessageDwr();
		try{
			messageDwr.setMessage(writeParteAcreedor(Integer.valueOf(idUsuario)));
			messageDwr.setCodeError(0);
		}catch(Exception e){
			messageDwr.setMessage(" <script> alert('"+e.getMessage()+"') </script>");
			e.printStackTrace();
			
		}
		return messageDwr;
	}
	
	private String writeParteAcreedor(Integer idUsuario){
		StringBuffer sb = new StringBuffer();
		try{
			List<AcreedorTO> listaAcreedores = new AcreedoresServiceImpl()
					.getAcreedoresByPersona(Integer.valueOf(idUsuario));
			CharSetUtil csu = new CharSetUtil();
			sb.append(" <table>");
			sb.append(" <tr>");
			sb.append(" <td class=\"tituloHeader2\"> Seleccione el acreedor con el cual se realizaran los avisos preventivos </td> ");
			sb.append(" <td> ");
			sb.append(" <select name=\"idAcreedor\">");
			for (AcreedorTO acreedorTO : listaAcreedores) {
				sb.append(" <option value=\""+acreedorTO.getIdPersona()+"\">"+csu.presentacionMaximaDeTexto(acreedorTO.getNombreCompleto(),50,"")+"  </option>");
			}
			sb.append(" </select>");
			sb.append(" </td> ");
			sb.append(" </tr>");
			sb.append(" </table>");
		}catch(Exception e){
			sb.append("sucedio el siguiente error al traer los acreedores  :" + e.getMessage());
		}
		
		return sb.toString();
	}
	
	@SuppressWarnings("rawtypes")
	private String writeBodyHtml(Integer idAcreedor,Integer idUsuario){
		StringBuffer sb = new StringBuffer();
		
		MasivaDAO masivaDAO = new MasivaDAO();
		GarantiasDAO garantiaDao = new GarantiasDAO();
		HashMap <String, String > mapTipoTramite = masivaDAO.getTiposTramite(garantiaDao.getCuentaMaestra(new Long(idAcreedor)).intValue(), idUsuario);
		sb.append(" <table> ");
		if (mapTipoTramite.size()>0){
			Iterator itr = mapTipoTramite.entrySet().iterator();
			sb.append(" <tr> ");
			sb.append("  <td class=\"tituloHeader2\">Seleccione el tipo de tr&aacute;mite :</td> ");
			sb.append(" <td> ");
			sb.append(" <select name=\"idListaTramite\"  id=\"idListaTramite\">   ");
			while (itr.hasNext()) {
				Map.Entry e = (Map.Entry)itr.next();
				sb.append("  <option value=\""+Constants.getIdTipoTramite(Integer.valueOf(e.getKey().toString()))+"\"> "+e.getValue()+" </option> ");
			}
			sb.append("  </select> ");
			sb.append(" </td> ");
			sb.append(" </tr> ");
			sb.append(" <tr> ");
			sb.append(" <td class=\"tituloHeader2\"> Seleccione el Archivo : </td> ");
			sb.append(" <td> <input type=\"file\" name=\"archivo\"/ ></td> 	 		 ");
			sb.append(" </tr> ");
			sb.append(" <tr> ");
			sb.append(" <td class=\"tituloHeader2\">Seleccione el tipo de proceso :</td> ");
			sb.append(" <td> ");
			
			sb.append(" <select name=\"idListaProceso\"  id=\"idListaProceso\">   ");
			sb.append("  <option value=\"1\"> Atendido </option> ");
			//sb.append("  <option value=\"2\"> Desatendido </option> ");
			sb.append("  </select> ");
			sb.append(" </td> ");
			sb.append(" </tr> ");
			sb.append(" <tr> ");
			sb.append(" <td colspan=\"2\" align=\"center\" style=\"padding-left: 1px;\"><input type=\"submit\" class=\"btn btn-large indigo\" value=\"Subir\" align=\"center\"/>   </td> ");
			sb.append(" </tr> ");
		}else{
			sb.append(" <tr> ");
			sb.append("  <td class=\"tituloHeader2\"> No tiene privilegios de carga masiva sobre ese acreedor</td> ");
			sb.append(" <td> ");
		}
		
		sb.append(" </table> ");
		return sb.toString();
	}
}
