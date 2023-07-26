package mx.gob.se.rug.operaciones.dwr.action;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

//import com.sun.org.apache.xpath.internal.operations.String;
import mx.gob.economia.dgi.framework.dwr.action.AbstractBaseDwrAction;
import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.acreedores.to.UsuarioTO;
import mx.gob.se.rug.common.util.NullsFree;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.detallegarantia.service.impl.DetalleServiceImpl;
import mx.gob.se.rug.detallegarantia.to.DetalleTO;
import mx.gob.se.rug.detallegarantia.to.PartesTO;
import mx.gob.se.rug.garantia.to.GarantiaTO;
import mx.gob.se.rug.inscripcion.dao.InscripcionDAO;
import mx.gob.se.rug.inscripcion.service.InscripcionService;
import mx.gob.se.rug.inscripcion.service.impl.InscripcionServiceImpl;
import mx.gob.se.rug.inscripcion.to.BienEspecialTO;
import mx.gob.se.rug.inscripcion.to.DeudorTO;
import mx.gob.se.rug.inscripcion.to.OtorganteTO;
import mx.gob.se.rug.operaciones.dao.OperacionesDAO;
import mx.gob.se.rug.operaciones.service.OperacionesService;
import mx.gob.se.rug.operaciones.to.CargaMasivaResumenTO;
import mx.gob.se.rug.operaciones.to.OperacionesTO;
import mx.gob.se.rug.to.MessageDwr;
import mx.gob.se.rug.util.to.DateUtilRug;
import gt.gob.rgm.util.ExcelCreator;
import java.util.logging.Level;
import org.apache.struts2.ServletActionContext;
import javax.servlet.ServletOutputStream;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import mx.gob.se.rug.util.MyLogger;



public class OperacionesDwrAction extends AbstractBaseDwrAction {


	private String persona;
        private String filtroExcel;
	
	private List <OperacionesTO> listaPendientes;
        private List <OperacionesTO> listadoPendientesExcel;
	private List <CargaMasivaResumenTO> listaPendientesFirmaMasiva;
	
	public MessageDwr muestraMOTodos(String ruta, String idAcreedorStr, String idPersona){
		MessageDwr dwr = new MessageDwr();
		try{			
			dwr.setCodeError(0);			
			dwr.setMessage(writeMOTodos(ruta,idAcreedorStr,idPersona).toString() );
		}catch(Exception e){
			dwr.setCodeError(1);
			dwr.setMessage(" <script> alert('Sucedio un error'); </script> ");
			e.printStackTrace();
		}
		return dwr;
	}
	public MessageDwr eliminaTramite(String idTramite,String idPersona, String ruta, String idAcreedor){
		MessageDwr mensaje = new MessageDwr();
		try{
			mensaje.setCodeError(0);
			InscripcionDAO inscripcionDAO = new InscripcionDAO();
			if (inscripcionDAO.bajaTramiteIncompleto(Integer.valueOf(idTramite)).getIntPl().intValue()==0){
				mensaje.setMessage(" <script> alert('El tramite fue eliminado correctamente'); inicializaPagina(); </script> ");
			}else{
				mensaje.setMessage(" <script> alert('Sucedio un error al eliminar el tramite'); inicializaPagina();</script> ");
			}
			
		}catch(Exception e){
			mensaje.setMessage(" <script> alert('Sucedio un error al eliminar el tramite : "+e.getMessage()+"');  inicializaPagina();</script> ");
			e.printStackTrace();
		}
		return mensaje;
	}

	public MessageDwr muestraMO(String idPersona, String ruta, String idAcreedorStr){
		MessageDwr dwr = new MessageDwr();
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		try{
			OperacionesDAO operacionesDAO = new OperacionesDAO();			
			dwr.setCodeError(0);			
			dwr.setMessage(writeMO(idPersona, ruta, operacionesDAO.getUsuario(connection, Integer.valueOf(idPersona)), idAcreedorStr ).toString());
		}catch(Exception e){
			dwr.setCodeError(1);
			dwr.setMessage(" <script> alert('Sucedio un error'); </script> ");
			e.printStackTrace();
		}finally{
			bd.close(connection, null, null);
		}
		return dwr;
	}
	
	public MessageDwr getParteUsuarios(String idAcreedorStr, String ruta, String idPersona){
	System.out.println("id del acreedor "+idAcreedorStr);
		MessageDwr dwr = new MessageDwr();
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		try{
			Integer idAcreedor = new Integer(idAcreedorStr);
			OperacionesDAO operacionesDAO = new OperacionesDAO();			
			List<UsuarioTO> usuarios = operacionesDAO.getUsuarios(connection, idAcreedor);			
			dwr.setCodeError(0);
			dwr.setMessage(writeMOSinDueno(idPersona, ruta, idAcreedorStr).toString()+writeTablaUsuarios(usuarios, ruta, idAcreedorStr, idPersona).toString());
		}catch(Exception e){
			dwr.setCodeError(1);
			dwr.setMessage("<script> alert('Sucedio un error a la hora de traer los usuarios') </script>");
			e.printStackTrace();
		}finally{
			bd.close(connection, null, null);
		}
		return dwr;
	}
	private StringBuffer writeMOTodos(String ruta, String idAcreedorStr, String idPersona){
		StringBuffer sb = new StringBuffer();
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		try{
			Integer idAcreedor = new Integer(idAcreedorStr);
			OperacionesDAO operacionesDAO = new OperacionesDAO();			
			List<UsuarioTO> usuarios = operacionesDAO.getUsuarios(connection, idAcreedor);			
			Iterator<UsuarioTO> itUsuarios = usuarios.iterator();
			UsuarioTO to;
			sb.append(" <table> ");
			sb.append(" <tr> <td > <hr width=\"97%\"> ");
			sb.append(" </td> </tr> ");			
			sb.append(" </td> </tr> ");
			while (itUsuarios.hasNext()){
				to = itUsuarios.next();
				sb.append(" <tr> <td> ");
				sb.append(writeMO(to.getIdSubusuario().toString(), ruta, to, idAcreedorStr));
				sb.append(" </td> </tr> ");
				sb.append(" <tr> <td > <hr width=\"97%\"> ");
				sb.append(" </td> </tr> ");
			}
			sb.append(" </table> ");			
		}catch(Exception e){			
			e.printStackTrace();
		}finally{
			bd.close(connection, null, null);
		}
		return sb;
	}
	private StringBuffer writeMO(String idPersona, String ruta, UsuarioTO usuarioTO, String idAcreedor){
		StringBuffer sb = new StringBuffer();
		OperacionesDAO operacionesDAO = new OperacionesDAO();	
		try{
			List<OperacionesTO> opPendientes = operacionesDAO.muestraOpPendientes(Integer.valueOf(idPersona));
			List<OperacionesTO> opPendientesFirma = operacionesDAO.muestraOpPendientesFirma(Integer.valueOf(idPersona), Integer.valueOf(idAcreedor));
			List<OperacionesTO> opTerminadas= operacionesDAO.muestraOpTerminadas(Integer.valueOf(idPersona));
			sb.append(" <table> ");
			sb.append(" <tr> ");
			sb.append(" <TD colspan=\"11\" class=\"tituloInteriorRojo\" bgcolor=\"#FFFFFF\" height=\"25\" nowrap=\"nowrap\">Operaciones de : "+usuarioTO.getCveUsuario()+"/"+usuarioTO.getNombreCompleto()+" </TD> ");
			sb.append(" </tr> ");
			boolean tuvo = false;
			if (opPendientes.size() > 0){
				sb.append(" <tr> <td> ");
				sb.append(writeOperacionesPendientes(opPendientes, ruta, idPersona, idAcreedor));
				sb.append(" </td> </tr> ");
				tuvo = true;
			}
			if (opPendientesFirma.size() > 0){
				sb.append(" <tr> <td> ");
				sb.append(writeOperacionesSinFirma(opPendientesFirma, ruta, idPersona, idAcreedor));
				sb.append(" </td> </tr> ");
				tuvo = true;
			}			
			if (opTerminadas.size()>0){
				sb.append(" <tr> <td> ");
				sb.append(writeOperacionesTerminadas(opTerminadas));
				sb.append(" </td> </tr> ");
				tuvo = true;
			}
			if (!tuvo){
				sb.append(" <tr> <td> ESTE USUARIO NO CUENTA CON OPERACIONES </td> </tr> ");
			}						
			sb.append(" </table> ");			
		}catch(Exception e){
			e.printStackTrace();
		}
		return sb;
	}
	private StringBuffer writeMOSinDueno(String idPersona, String ruta, String idAcreedor){
		
		StringBuffer sb = new StringBuffer();
		OperacionesDAO operacionesDAO = new OperacionesDAO();	
		try{		
			sb.append(" <table id=\"mytable\" class=\"mytable\" cellpadding=\"3\" cellspacing=\"2\" ");
			sb.append(" border=\"0\" width=\"950\"> ");
			sb.append(" <tr> ");
			sb.append(" <td class=\"tituloInteriorRojo\">Operaciones Sin due�o</td> ");
			sb.append(" </tr> ");			
			sb.append(" <tr> <td> ");
			sb.append(writeOperacionesPendientes(operacionesDAO.muestraOpPendientesSinDueno(Integer.valueOf(idAcreedor)), ruta, idPersona, idAcreedor));
			sb.append(" </td> </tr> ");
			sb.append(" <tr> <td> ");
			sb.append(writeOperacionesSinFirma(operacionesDAO.muestraOpPendientesFirmaSinDueno(Integer.valueOf(idAcreedor)), ruta, idPersona, idAcreedor));
			sb.append(" </td> </tr> ");	
			sb.append(" <tr> <td> ");
			sb.append(writeOperacionesTerminadas(operacionesDAO.muestraOpTerminadasGeneral(Integer.valueOf(idAcreedor))));
			sb.append(" </td> </tr> ");
			sb.append(" </table> ");			
		}catch(Exception e){
			e.printStackTrace();
		}
		return sb;
	}
	
	private StringBuffer writeOperacionesTerminadas(List<OperacionesTO> operacionesTerminadas){
		StringBuffer sb = new StringBuffer();
		try{
			if (operacionesTerminadas.size()>0){			
			sb.append(" <table id=\"mytabledaO\" class=\"mytabledaO\" border=\"0\" width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" align=\"left\"> ");
			sb.append(" <thead> ");
			sb.append(" <tr> ");
			sb.append(" <TD colspan=\"11\" class=\"tituloInteriorRojo\" bgcolor=\"#FFFFFF\" height=\"25\" nowrap=\"nowrap\">Terminados</TD> ");
			sb.append(" </tr> ");
			sb.append(" <tr> ");
			sb.append(" <td class=\"encabezadoTablaResumen\" style=\"text-align: center\">Tipo de Transacci&oacute;n</td> ");
			sb.append(" <td class=\"encabezadoTablaResumen\" style=\"text-align: center\">Fecha de Inicio</td> ");
			sb.append(" <td class=\"encabezadoTablaResumen\" style=\"text-align: center\">N&uacute;mero de Garant&iacute;a Mobiliaria del Otorgante</td> ");
			sb.append(" </tr> ");
			sb.append(" </thead> ");
			sb.append(" <tbody> ");			
			Iterator<OperacionesTO> it = operacionesTerminadas.iterator();
			OperacionesTO operacionesTO;
			DateUtilRug dateUtilRug = new DateUtilRug();
			while (it.hasNext()){
				operacionesTO = it.next();
				sb.append(" <tr> ");		
				if (operacionesTO.getTipoTransaccion().contains("sin")){
					sb.append(" <td class=\"cuerpo1TablaResumen\"><div align=\"center\"> Anotaci�n </div></td> ");
				}else{
					sb.append(" <td class=\"cuerpo1TablaResumen\"><div align=\"center\">"+operacionesTO.getTipoTransaccion()+"</div></td> ");
				}				
				sb.append(" <td class=\"cuerpo1TablaResumen\"><div align=\"center\"> ");			
				sb.append(dateUtilRug.cambioDeFormato(operacionesTO.getFechaOperacionInicio()) +"<br> ");
				sb.append(" </div> ");
				sb.append(" </td> ");
				
				if(operacionesTO.getIdTipoTramite()==3){
					sb.append(" <td class=\"cuerpo1TablaResumen\"><div align=\"center\">" +
							"<a style=\"cursor: pointer;\" " +
							"onclick=\"window.location.href='"+Constants.getContextPath()+"/home/detalleAviso.do?idTramite="+operacionesTO.getIdInscripcion()+"'\">"
							+operacionesTO.getNumGarantia()+"</a></div></td> ");
				}else if(operacionesTO.getIdTipoTramite()==2){
					sb.append(" <td class=\"cuerpo1TablaResumen\"><div align=\"center\">" +
							"<a style=\"cursor: pointer;\" " +
							"onclick=\"window.location.href='"+Constants.getContextPath()+"/home/detalle.do?idGarantia="+operacionesTO.getNumGarantia()+"&idTramite="+operacionesTO.getIdInscripcion()+"'\">"
							+operacionesTO.getNumGarantia()+"</a></div></td> ");
				}else if(operacionesTO.getIdTipoTramite()==10){
					sb.append(" <td class=\"cuerpo1TablaResumen\"><div align=\"center\">" +
							"<a style=\"cursor: pointer;\" " +
							"onclick=\"window.location.href='"+Constants.getContextPath()+"/anotacion/detalleAnotacion.do?idTramite="+operacionesTO.getIdInscripcion()+"'\">"
							+operacionesTO.getNumGarantia()+"</a></div></td> ");
				}else {
					sb.append(" <td class=\"cuerpo1TablaResumen\"><div align=\"center\">" +
							"<a style=\"cursor: pointer;\" " +
							"onclick=\"window.location.href='"+Constants.getContextPath()+"/home/detalle.do?idGarantia="+operacionesTO.getNumGarantia()+"&idTramite="+operacionesTO.getIdInscripcion()+"'\">"
							+operacionesTO.getNumGarantia()+"</a></div></td> ");
				}
							    
				sb.append(" </tr> ");
			}			
			sb.append(" </tbody> ");
			sb.append(" </table> ");
		}else{
			throw new Exception("RUG-OperacionesDWRAction>>WriteOperacionesTerminadas::: la lista de operaciones viene vacia");
		}
	}catch(Exception e){
		e.printStackTrace();
	}	
			
		return sb;
	}
	
	private StringBuffer writeOperacionesSinFirma(List <OperacionesTO> listaPendientesFirma, String ruta, String idPersona, String idAcreedor){
		StringBuffer sb = new StringBuffer();
		try{
			if (listaPendientesFirma.size()>0){			
				sb.append(" <table id=\"mytabledaO\" class=\"mytabledaO\" border=\"0\" width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" align=\"left\"> ");
				sb.append(" <thead> ");
				sb.append(" <tr> ");
				sb.append(" <TD colspan=\"11\" class=\"tituloInteriorRojo\" bgcolor=\"#FFFFFF\" height=\"25\" nowrap=\"nowrap\">Pendientes de Firma</TD> ");
				sb.append(" </tr> ");
				sb.append(" <tr> ");
				sb.append(" <td class=\"encabezadoTablaResumen\" style=\"text-align: center\">Tipo de Operaci&oacute;n</td> ");
				sb.append(" <td class=\"encabezadoTablaResumen\" style=\"text-align: center\">N&uacute;mero de Garant&iacute;a</td> ");			
				sb.append(" <td class=\"encabezadoTablaResumen\" style=\"text-align: center\">Fecha de Inicio de la Vigencia</td> ");
				sb.append(" <td class=\"encabezadoTablaResumen\" style=\"text-align: center\">Opciones</td> ");			
				sb.append(" </tr> ");
				sb.append(" </thead> ");
				sb.append(" <tbody> ");				
				Iterator<OperacionesTO> it = listaPendientesFirma.iterator();     	
				OperacionesTO operacionesTO;
				while (it.hasNext()){
					operacionesTO = it.next();
					sb.append(" <tr> ");
					if (operacionesTO.getTipoTransaccion().contains("sin")){
						sb.append(" <td class=\"cuerpo1TablaResumen\"><div align=\"center\"> Anotaci�n </div></td> ");
					}else{
						sb.append(" <td class=\"cuerpo1TablaResumen\"><div align=\"center\">"+operacionesTO.getTipoTransaccion()+"</div></td> ");
					}					
					if (operacionesTO.getTipoTransaccion().contains("Inscr")){
						sb.append(" <td class=\"cuerpo1TablaResumen\"><div align=\"center\"> <a style=\"cursor:pointer;\" onclick=\"muestraDetalle('"+operacionesTO.getIdInscripcion()+"','"+ruta+"');\"  class='thickbox'> "+operacionesTO.getNumGarantia()+" </a></div></td> ");
					}else{						
						sb.append(" <td class=\"cuerpo1TablaResumen\"><div align=\"center\">"+operacionesTO.getIdGarantiaModificar()+"</div></td> ");
					}
					sb.append(" <td class=\"cuerpo1TablaResumen\"><div align=\"center\">"+operacionesTO.getFechaInicio()+"</div></td> ");
					sb.append(" <td class=\"cuerpo1TablaResumen\"> 	 ");
					sb.append(" <table> <tr> ");
					sb.append(" <td class=\"cuerpo1TablaResumen\"><input type=\"button\" value=\"Firmar\" onclick=\"window.location.href='"+ruta+"/firma/firma.do?idTramite="+operacionesTO.getIdInscripcion()+"'\"/></td> ");
					sb.append(" <td class=\"cuerpo1TablaResumen\"> ");
					sb.append(" <input type=\"button\" value=\"Eliminar\" onclick=\"eliminaTramite("+operacionesTO.getIdInscripcion()+", "+idPersona+",'"+ruta+"', '"+idAcreedor+"');\"/>  "); 								
					sb.append(" </td> ");
					sb.append(" </tr> </table> ");			
					sb.append(" </td> ");
					sb.append(" </tr> ");
				}
				sb.append(" </tbody> ");
				sb.append(" </table> ");
			
			}else{
				sb.append(" <table id=\"mytabledaO\" class=\"mytabledaO\" border=\"0\" width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" align=\"left\"> ");
				sb.append(" <thead> ");				
				sb.append(" <tr> ");
				sb.append(" <TD colspan=\"11\" class=\"tituloInteriorRojo\" bgcolor=\"#FFFFFF\" height=\"25\" nowrap=\"nowrap\">Pendientes de Firma</TD> ");
				sb.append(" </tr> ");
				sb.append(" </thead> ");
				sb.append(" <tbody> ");
				sb.append(" <tr> ");
				sb.append(" <TD >No se encontraron Registros</TD> ");
				sb.append(" </tr> ");
				sb.append(" </tbody> ");
				sb.append(" </table> ");
			}
		}catch(Exception e){
			e.printStackTrace();
		}		
		return sb;
	}
	
	private StringBuffer writeOperacionesPendientes(List <OperacionesTO> listaPendientes, String ruta, String idPersona, String idAcreedor){
		StringBuffer sb = new StringBuffer();
		try{
			if (listaPendientes.size()>0){
				sb.append(" <table id=\"mytabledaO\" class=\"mytabledaO\" border=\"0\" width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" align=\"left\"> ");
				sb.append(" <thead> ");				
				sb.append(" <tr> ");
				sb.append(" <TD colspan=\"11\" class=\"tituloInteriorRojo\" bgcolor=\"#FFFFFF\" height=\"25\" nowrap=\"nowrap\">Pendientes de Captura de Datos</TD> ");
				sb.append(" </tr> ");
				sb.append(" <tr> ");
				sb.append(" <td class=\"encabezadoTablaResumen\" style=\"text-align: center\">Tipo de Operaci&oacute;n</td> ");
				sb.append(" <td class=\"encabezadoTablaResumen\" style=\"text-align: center\">N&uacute;mero de Garant&iacute;a</td> ");			
				sb.append(" <td class=\"encabezadoTablaResumen\" style=\"text-align: center\">Fecha de Inicio de la Vigencia</td> ");
				sb.append(" <td class=\"encabezadoTablaResumen\" style=\"text-align: center\">Opciones</td> ");			
				sb.append(" </tr> ");
				sb.append(" </thead> ");
				sb.append(" <tbody> ");
				Iterator<OperacionesTO> itOperaciones = listaPendientes.iterator();
				OperacionesTO operacionesTO;
				while (itOperaciones.hasNext()){
					operacionesTO = itOperaciones.next();
					sb.append(" <tr> ");				
					sb.append(" <td class=\"cuerpo1TablaResumen\"><div align=\"center\">"+operacionesTO.getTipoTransaccion()+"</div></td> ");
					sb.append(" <td class=\"cuerpo1TablaResumen\"><div align=\"center\">"+operacionesTO.getNumGarantia()+"</div></td> ");
					sb.append(" <td class=\"cuerpo1TablaResumen\"><div align=\"center\">"+operacionesTO.getFechaInicio()+"</div></td> ");
					sb.append(" <td class=\"cuerpo1TablaResumen\"> ");	
					sb.append(" <table> <tr> ");
					sb.append(" <td class=\"cuerpo1TablaResumen\"> ");
					sb.append(" <input type=\"button\" value=\"Continuar\" class=\"textoGeneralrojo\" onclick=\"window.location.href='"+ruta+operacionesTO.getPaso()+"?idInscripcion="+operacionesTO.getIdInscripcion()+"'\"/> ");
					sb.append(" </td> ");
					sb.append(" <td class=\"cuerpo1TablaResumen\"> ");
					sb.append(" <input type=\"button\" value=\"Eliminar\" class=\"textoGeneralrojo\" onclick=\"eliminaTramite("+operacionesTO.getIdInscripcion()+", "+idPersona+",'"+ruta+"', '"+idAcreedor+"');\"/>  "); 								
					sb.append(" </td> ");
					sb.append(" </tr> </table> ");  				
					sb.append(" </td> ");
					sb.append(" </tr>	 ");
					
				}
				sb.append(" </tbody> ");
				sb.append(" </table> ");
			}else{
				sb.append(" <table id=\"mytabledaO\" class=\"mytabledaO\" border=\"0\" width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" align=\"left\"> ");
				sb.append(" <thead> ");				
				sb.append(" <tr> ");
				sb.append(" <TD colspan=\"11\" class=\"tituloInteriorRojo\" bgcolor=\"#FFFFFF\" height=\"25\" nowrap=\"nowrap\">Pendientes de Captura de Datos</TD> ");
				sb.append(" </tr> ");
				sb.append(" </thead> ");
				sb.append(" <tbody> ");
				sb.append(" <tr> ");
				sb.append(" <TD >No se encontraron Registros</TD> ");
				sb.append(" </tr> ");
				sb.append(" </tbody> ");
				sb.append(" </table> ");
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		
		return sb;
	}
	
	private StringBuffer writeTablaUsuarios(List<UsuarioTO> usuarios, String ruta, String idAcreedor, String idPersona){
		StringBuffer sb = new StringBuffer();
		try{
			if (usuarios.size() > 0){				
				sb.append(" <table> ");
				sb.append(" <tr> <td> </td> </tr> ");
				sb.append(" <table id=\"mytable\" class=\"mytable\" cellpadding=\"3\" cellspacing=\"2\" ");
				sb.append(" border=\"0\" width=\"650\"> ");
				sb.append(" <tr> ");
				sb.append(" <td class=\"tituloInteriorRojo\">Usuarios</td> ");
				sb.append(" </tr> ");
				sb.append(" <tr> ");
				sb.append("					<td class=\"encabezadoTablaResumen\">Correo Electronico</td> ");
				sb.append("					<td class=\"encabezadoTablaResumen\">Nombre</td> ");
				sb.append(" </tr> ");
				Iterator<UsuarioTO> itUsuario = usuarios.iterator();
				UsuarioTO to;
				while (itUsuario.hasNext()){
					to = itUsuario.next();
					sb.append("				<tr> ");
					sb.append(" <td class=\"cuerpo1TablaResumen\"><a style=\"cursor: pointer;\" onclick=\"muestraMO("+to.getIdSubusuario()+", '"+ruta+"','"+idAcreedor+"');\">"+to.getCveUsuario()+"</a></td> ");
					sb.append(" <td class=\"cuerpo1TablaResumen\"><a style=\"cursor: pointer;\" onclick=\"muestraMO("+to.getIdSubusuario()+", '"+ruta+"','"+idAcreedor+"');\">"+to.getNombreCompleto()+"</a></td> ");				
					sb.append(" </tr> ");
				}		
				sb.append("				<tr> ");
				sb.append(" <td class=\"cuerpo1TablaResumen\"><a style=\"cursor: pointer;\" onclick=\"muestraMOTodos('"+ruta+"',"+idAcreedor+","+idPersona+");\">Todos los usuarios</a></td> ");
				sb.append(" <td class=\"cuerpo1TablaResumen\"><a style=\"cursor: pointer;\" onclick=\"muestraMOTodos('"+ruta+"',"+idAcreedor+","+idPersona+");\">Todos los usuarios</a></td> ");				
				sb.append(" </tr> ");
				sb.append(" </table> ");
			}else{
				throw new Exception("RUG-OperacionesDWRAction>>WriteTablaUsuarios:::el acreedor no tiene usuarios o el administrador no puede verlos");
			}
		}catch(Exception e){
			e.printStackTrace();
		}		
		return sb;
	}
	
	public MessageDwr detalleGarantiaFinal(Integer idGarantia, Integer idTramite) {
		
		MessageDwr dwr = new MessageDwr();
		dwr.setCodeError(0);
		
		StringBuffer sb = new StringBuffer();	
		try {
			
			DetalleServiceImpl detservd = new DetalleServiceImpl();
			InscripcionServiceImpl inscripcionserv = new InscripcionServiceImpl();
			
			DetalleTO garantiaTO = detservd.getDetalle(idGarantia, idTramite);
			
			List<String> textos = inscripcionserv.getTextosFormularioByIdGarantia(garantiaTO.getIdtipogarantia());
							
			List<PartesTO> deudores = detservd.getDeudor(idGarantia, idTramite);
			List<PartesTO> acreedores = detservd.getAcreedor(idGarantia, idTramite);
			List<PartesTO> otorgantes = detservd.getOtorgante(idGarantia, idTramite);
			List<BienEspecialTO> bienes = detservd.getListaBienes(idTramite, 2);
		
			sb.append("<div class=\"container-fluid\">\r\n" + 
					"       <div class=\"row\"><div id=\"menuh\"><ul><li><a class=\"btn waves-effect indigo\" href=\"/Rug/home/embargo.do?idGarantia=" + idGarantia + "\"><span class=\"white-text\">Formulario Embargo</span></a></li></ul></div></div>" + 
					"		<div class=\"row\">\r\n" + 
					"			<div class=\"col s12\">\r\n" + 
					"				<div class=\"card\">\r\n" +  
					"					<div class=\"col s12\">	\r\n" + 
					"");
			sb.append("<div class=\"row note teal\">\r\n" + 
					"							<span class=\"white-text\">Sujetos de la Garant&iacute;a Mobiliaria</span>\r\n" + 
					"						</div>	");
			if(!deudores.isEmpty()) {
				sb.append("<div class=\"row\"><div class=\"input-field col s12\">\r\n" + 
						"						    		<table id=\"mytable\" class=\"bordered 2 striped centered responsive-table\" >\r\n" + 
						"										<thead>	\r\n" + 
						"											<tr>\r\n" + 
						"												<th colspan=\"2\">"+textos.get(1)+"</th>\r\n" + 
						"											</tr>\r\n" + 
						"											<tr>\r\n" + 
						"												<th>Nombre, Denominaci&oacute;n o Raz&oacute;n Social</th>\r\n" + 
						"												<th>No. Identificaci&oacute;n \\ No. Identificaci&oacute;n Tributaria</th>														\r\n" + 
						"											</tr>\r\n" + 
						"										</thead>\r\n" + 
						"										<tbody>");
				for(Iterator<PartesTO> itD = deudores.iterator(); itD.hasNext();) {
					PartesTO deudor = itD.next();
					sb.append("<tr>\r\n" + 
							"													<td>"+deudor.getNombre()+"</td>\r\n" + 																			
							"														<td>"+deudor.getRfc()+"</td>\r\n" + 
							"												</tr>");
				}
								
				sb.append("</tbody>\r\n" + 
						"									</table>\r\n" + 
						"						    	</div>\r\n" + 
						"						    </div>");
			}
			
			if(!acreedores.isEmpty()) {
				sb.append("<div class=\"row\">\r\n" + 
						"						    	<div class=\"input-field col s12\">\r\n" + 
						"						    		<table id=\"mytable\" class=\"bordered striped centered responsive-table\" >\r\n" + 
						"										<thead>	\r\n" + 
						"											<tr>\r\n" + 
						"												<th colspan=\"2\">"+textos.get(2)+"</th>\r\n" + 
						"											</tr>\r\n" + 
						"											<tr>\r\n" + 
						"												<th>Nombre, Denominaci&oacute;n o Raz&oacute;n Social</th>\r\n" + 
						"												<th>No. Identificaci&oacute;n \\ No. Identificaci&oacute;n Tributaria</th>			\r\n" + 
						"											</tr>\r\n" + 
						"										</thead>\r\n" + 
						"										<tbody>");
				
				for(Iterator<PartesTO> itA = acreedores.iterator(); itA.hasNext();) {
					PartesTO acreedor = itA.next();
					sb.append("<tr>\r\n" + 
							"													<td>"+acreedor.getNombre()+"</td>\r\n" + 																			
							"														<td>"+acreedor.getRfc()+"</td>\r\n" + 
							"												</tr>");
				}
				
				sb.append("</tbody>\r\n" + 
						"									</table>\r\n" + 
						"						    	</div>\r\n" + 
						"						    </div>");
			}
			
			if(!otorgantes.isEmpty()) {
				sb.append("<div class=\"row\">\r\n" + 
						"						    	<div class=\"input-field col s12\">\r\n" + 
						"						    		<table id=\"mytable\" class=\"bordered striped centered responsive-table\" >\r\n" + 
						"										<thead>	\r\n" + 
						"											<tr>\r\n" + 
						"												<th colspan=\"2\">"+textos.get(3)+"</th>\r\n" + 
						"											</tr>\r\n" + 
						"											<tr>\r\n" + 
						"												<th>Nombre, Denominaci&oacute;n o Raz&oacute;n Social</th>\r\n" + 
						"												<th>No. Identificaci&oacute;n \\ No. Identificaci&oacute;n Tributaria</th>			\r\n" + 
						"											</tr>\r\n" + 
						"										</thead>\r\n" + 
						"										<tbody>");
				
				for(Iterator<PartesTO> itO = otorgantes.iterator(); itO.hasNext();) {
					PartesTO otorgante = itO.next();
					sb.append("<tr>\r\n" + 
							"													<td>"+otorgante.getNombre()+"</td>\r\n" + 																			
							"														<td>"+otorgante.getRfc()+"</td>\r\n" + 
							"												</tr>");
				}
				
				sb.append("</tbody>\r\n" + 
						"									</table>\r\n" + 
						"						    	</div>\r\n" + 
						"						    </div>");
			}
			
			sb.append("<div class=\"row note teal\">\r\n" + 
					"							<span class=\"white-text\">Datos de la Garant&iacute;a Mobiliaria</span>\r\n" + 
					"						</div>						\r\n" + 
					"					    <div class=\"row\">\r\n" + 
					"					    	<div class=\"input-field col s12\">\r\n" + 
					"					    		<span class=\"blue-text text-darken-2\">"+textos.get(4)+"</span>\r\n" + 
					"					    		<p>"+garantiaTO.getDescbienes()+"</p>\r\n" + 
					"					    	</div>\r\n" + 
					"					    </div>");
			
			if(!bienes.isEmpty()) {
				sb.append("<div class=\"row\">\r\n" + 
						"						    	<div class=\"input-field col s12\">\r\n" + 
														   "<span class=\"blue-text text-darken-2\">Bienes en garant&iacute;a si estos tienen n&uacute;mero de serie:</span>" +
						"						    		<table id=\"mytable\" class=\"bordered striped centered responsive-table\" >\r\n" + 
						"										<thead>	\r\n" + 						 
						"											<tr>\r\n" + 
						"												<th>Tipo Bien Especial</th>\r\n" + 
						"												<th>Tipo Identificador</th>\r\n" + 
						"												<th>Identificador</th>\r\n" + 
						"												<th>Descripci&oacute;n</th>														\r\n" + 
						"											</tr>\r\n" + 
						"										</thead>\r\n" + 
						"										<tbody>");
				for(Iterator<BienEspecialTO> itB = bienes.iterator(); itB.hasNext();){
					BienEspecialTO bien = itB.next();
					sb.append("<tr>\r\n" + 
							"													<td>"+bien.getTipoBien()+"</td>\r\n" + 
							"													<td>"+bien.getTipoIdentificador()+"</td>\r\n" + 
							"													<td>"+bien.getIdentificador()+"</td>	\r\n" +  
							"													<td>"+bien.getDescripcion()+"</td>					\r\n" + 
							"												</tr>");
				}
				
				sb.append("</tbody>\r\n" + 
						"									</table>\r\n" + 
						"						    	</div>\r\n" + 
						"						    </div>");
			}	
			sb.append("<div class=\"row\">\r\n" + 
					"						    	<div class=\"input-field col s12\">\r\n" + 
					"						    		<span class=\"blue-text text-darken-2\">"+textos.get(6)+"</span>\r\n" + 
					"						    		<p>"+garantiaTO.getInstrumento()+"</p>						    		\r\n" + 
					"						    	</div>\r\n" + 
					"						    </div>");
			
			sb.append("<div class=\"row\">\r\n" + 
					"						    	<div class=\"input-field col s12\">\r\n" + 
					"						    		<span class=\"blue-text text-darken-2\">Datos del Representante(s)::</span>\r\n" + 
					"						    		<p>"+garantiaTO.getOtroscontrato()+"</p>						    		\r\n" + 
					"						    	</div>\r\n" + 
					"						    </div>");
			
			sb.append("<div class=\"row\"><div class=\"input-field col s12\">\r\n" + 
					"					    		<span class=\"blue-text text-darken-2\">"+textos.get(8)+"</span>\r\n" + 
					"					    		<p>"+garantiaTO.getOtrosgarantia()+"</p>						    		\r\n" + 
					"					    	</div>");
			sb.append("<div class=\"row\"><div class=\"input-field col s12\">\r\n" + 
					"					    		<span class=\"blue-text text-darken-2\">Anotaci&oacute;n de la operaci&oacute;n: </span>\r\n" + 
					"					    		<p>"+garantiaTO.getOtrosterminos()+"</p>						    		\r\n" + 
					"					    	</div>");
			
			sb.append("</div>\r\n" + 						 
					"				</div>\r\n" + 
					"			</div>\r\n" + 
					"		</div>\r\n" + 
					"	</div>");
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		dwr.setMessage(sb.toString());
		
		return dwr;
		
	}
	
	public MessageDwr detalleGarantia(Integer idInscripcion){
		MessageDwr dwr = new MessageDwr();
		dwr.setCodeError(0);
		
		StringBuffer sb = new StringBuffer();	
		try {
			InscripcionService inscripcionService = new InscripcionServiceImpl();	
			Integer idTramite ;	
			idTramite = idInscripcion;
			
			Integer idGarantia = inscripcionService.getIDGarantiaByIDTramite(idTramite);
				
			GarantiaTO garantiaTO = inscripcionService.getInscripcionByID(idGarantia);
			
			List<String> textos = inscripcionService.getTextosFormularioByIdGarantia(garantiaTO.getIdTipoGarantia());
			
			List<DeudorTO> deudores = inscripcionService.getDeudoresByTramite(idTramite);
			List<AcreedorTO> acreedores = inscripcionService.getAcreedoresByTramite(idTramite);
			List<BienEspecialTO> bienes = inscripcionService.getListaBienes(idTramite, 1);
			List<DeudorTO> otorgantes = inscripcionService.getOtorgantesByTramite(idTramite);
		
		
			sb.append("<div class=\"container-fluid\">\r\n" + 
					"		<div class=\"row\">\r\n" + 
					"			<div class=\"col s12\">\r\n" + 
					"				<div class=\"card\">\r\n" +  
					"					<div class=\"col s12\">	\r\n" + 
					"");
			sb.append("<div class=\"row note teal\">\r\n" + 
					"							<span class=\"white-text\">Sujetos de la Garant&iacute;a Mobiliaria</span>\r\n" + 
					"						</div>	");
			if(!deudores.isEmpty()) {
				sb.append("<div class=\"row\"><div class=\"input-field col s12\">\r\n" + 
						"						    		<table id=\"mytable\" class=\"bordered striped centered responsive-table\" >\r\n" + 
						"										<thead>	\r\n" + 
						"											<tr>\r\n" + 
						"												<th colspan=\"2\">"+textos.get(1)+"</th>\r\n" + 
						"											</tr>\r\n" + 
						"											<tr>\r\n" + 
						"												<th>Nombre, Denominaci&oacute;n o Raz&oacute;n Social</th>\r\n" + 
						"												<th>No. Identificaci&oacute;n \\ No. Identificaci&oacute;n Tributaria</th>														\r\n" + 
						"											</tr>\r\n" + 
						"										</thead>\r\n" + 
						"										<tbody>");
				for(Iterator<DeudorTO> itD = deudores.iterator(); itD.hasNext();) {
					DeudorTO deudor = itD.next();
					sb.append("<tr>\r\n" + 
							"													<td>"+deudor.getNombreCompleto()+"</td>\r\n" + 																			
							"														<td>"+deudor.getRfc()+"</td>\r\n" + 
							"												</tr>");
				}
								
				sb.append("</tbody>\r\n" + 
						"									</table>\r\n" + 
						"						    	</div>\r\n" + 
						"						    </div>");
			}
			
			if(!acreedores.isEmpty()) {
				sb.append("<div class=\"row\">\r\n" + 
						"						    	<div class=\"input-field col s12\">\r\n" + 
						"						    		<table id=\"mytable\" class=\"bordered striped centered responsive-table\" >\r\n" + 
						"										<thead>	\r\n" + 
						"											<tr>\r\n" + 
						"												<th colspan=\"2\">"+textos.get(2)+"</th>\r\n" + 
						"											</tr>\r\n" + 
						"											<tr>\r\n" + 
						"												<th>Nombre, Denominaci&oacute;n o Raz&oacute;n Social</th>\r\n" + 
						"												<th>No. Identificaci&oacute;n \\ No. Identificaci&oacute;n Tributaria</th>			\r\n" + 
						"											</tr>\r\n" + 
						"										</thead>\r\n" + 
						"										<tbody>");
				
				for(Iterator<AcreedorTO> itA = acreedores.iterator(); itA.hasNext();) {
					AcreedorTO acreedor = itA.next();
					sb.append("<tr>\r\n" + 
							"													<td>"+acreedor.getNombreCompleto()+"</td>\r\n" + 																			
							"														<td>"+acreedor.getRfc()+"</td>\r\n" + 
							"												</tr>");
				}
				
				sb.append("</tbody>\r\n" + 
						"									</table>\r\n" + 
						"						    	</div>\r\n" + 
						"						    </div>");
			}
			
			if(!otorgantes.isEmpty()) {
				sb.append("<div class=\"row\"><div class=\"input-field col s12\">\r\n" + 
						"						    		<table id=\"mytable\" class=\"bordered striped centered responsive-table\" >\r\n" + 
						"										<thead>	\r\n" + 
						"											<tr>\r\n" + 
						"												<th colspan=\"2\">"+textos.get(3)+"</th>\r\n" + 
						"											</tr>\r\n" + 
						"											<tr>\r\n" + 
						"												<th>Nombre, Denominaci&oacute;n o Raz&oacute;n Social</th>\r\n" + 
						"												<th>No. Identificaci&oacute;n \\ No. Identificaci&oacute;n Tributaria</th>														\r\n" + 
						"											</tr>\r\n" + 
						"										</thead>\r\n" + 
						"										<tbody>");
				for(Iterator<DeudorTO> itO = otorgantes.iterator(); itO.hasNext();) {
					DeudorTO otorgante = itO.next();
					sb.append("<tr>\r\n" + 
							"													<td>"+otorgante.getNombreCompleto()+"</td>\r\n" + 																			
							"														<td>"+otorgante.getRfc()+"</td>\r\n" + 
							"												</tr>");
				}
								
				sb.append("</tbody>\r\n" + 
						"									</table>\r\n" + 
						"						    	</div>\r\n" + 
						"						    </div>");
			}
			
			sb.append("<div class=\"row note teal\">\r\n" + 
					"							<span class=\"white-text\">Datos de la Garant&iacute;a Mobiliaria</span>\r\n" + 
					"						</div>						\r\n" + 
					"					    <div class=\"row\">\r\n" + 
					"					    	<div class=\"input-field col s12\">\r\n" + 
					"					    		<span class=\"blue-text text-darken-2\">"+textos.get(4)+"</span>\r\n" + 
					"					    		<p>"+garantiaTO.getActoContratoTO().getDescripcion()+"</p>\r\n" + 
					"					    	</div>\r\n" + 
					"					    </div>");
			
			if(!bienes.isEmpty()) {
				sb.append("<div class=\"row\">\r\n" + 
						"						    	<div class=\"input-field col s12\">\r\n" + 
														   "<span class=\"blue-text text-darken-2\">Bienes en garant&iacute;a si estos tienen n&uacute;mero de serie:</span>" +
						"						    		<table id=\"mytable\" class=\"bordered striped centered responsive-table\" >\r\n" + 
						"										<thead>	\r\n" + 						 
						"											<tr>\r\n" + 
						"												<th>Tipo Bien Especial</th>\r\n" + 
						"												<th>Tipo Identificador</th>\r\n" + 
						"												<th>Identificador</th>\r\n" + 
						"												<th>Descripci&oacute;n</th>														\r\n" + 
						"											</tr>\r\n" + 
						"										</thead>\r\n" + 
						"										<tbody>");
				for(Iterator<BienEspecialTO> itB = bienes.iterator(); itB.hasNext();){
					BienEspecialTO bien = itB.next();
					sb.append("<tr>\r\n" + 
							"													<td>"+bien.getTipoBien()+"</td>\r\n" + 
							"													<td>"+bien.getTipoIdentificador()+"</td>\r\n" + 
							"													<td>"+bien.getIdentificador()+"</td>	\r\n" + 
							"													<td>"+bien.getDescripcion()+"</td>					\r\n" + 
							"												</tr>");
				}
				
				sb.append("</tbody>\r\n" + 
						"									</table>\r\n" + 
						"						    	</div>\r\n" + 
						"						    </div>");
			}	
			sb.append("<div class=\"row\">\r\n" + 
					"						    	<div class=\"input-field col s12\">\r\n" + 
					"						    		<span class=\"blue-text text-darken-2\">"+textos.get(6)+"</span>\r\n" + 
					"						    		<p>"+garantiaTO.getActoContratoTO().getInstrumentoPub()+"</p>						    		\r\n" + 
					"						    	</div>\r\n" + 
					"						    </div>");
			
			sb.append("<div class=\"row\">\r\n" + 
					"						    	<div class=\"input-field col s12\">\r\n" + 
					"						    		<span class=\"blue-text text-darken-2\">Datos del Representante(s)::</span>\r\n" + 
					"						    		<p>"+garantiaTO.getObligacionTO().getOtrosTerminos()+"</p>						    		\r\n" + 
					"						    	</div>\r\n" + 
					"						    </div>");
			
			sb.append("<div class=\"row\"><div class=\"input-field col s12\">\r\n" + 
					"					    		<span class=\"blue-text text-darken-2\">"+textos.get(8)+"</span>\r\n" + 
					"					    		<p>"+garantiaTO.getActoContratoTO().getOtrosTerminos()+"</p>						    		\r\n" + 
					"					    	</div>");
			
			sb.append("</div>\r\n" + 						 
					"				</div>\r\n" + 
					"			</div>\r\n" + 
					"		</div>\r\n" + 
					"	</div>");
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		dwr.setMessage(sb.toString());
		
		return dwr;
	}


	public MessageDwr getAcreedores(Integer idPersona, String filtro)
	{
		MessageDwr dw = new MessageDwr();
		dw.setCodeError(0);
		String js;
		Integer cantidadOperaciones = 0, numeroPagina = 0, registroTotal = 0, paginado = 20;

		try{
			OperacionesDAO operaciones = new OperacionesDAO();
			OperacionesService servicio = new OperacionesService();

			if (!filtro.isEmpty())
			{
				cantidadOperaciones = servicio.getCountAcreedorFilter(idPersona, filtro);
				js = "pagSoyAcreedor";

			}
			else
			{
				cantidadOperaciones = servicio.getCantidadTramitesSoyAcreedor(idPersona);
				js = "pagSoyAcreedor";

			}

			StringBuffer sb = new StringBuffer();

			if(cantidadOperaciones > 0){
				numeroPagina = cantidadOperaciones / paginado;
				registroTotal = cantidadOperaciones;
			}

			if(registroTotal % paginado > 0){
				numeroPagina++;
			}

			if(cantidadOperaciones > 0){
				if (!filtro.isEmpty())
				{
					sb.append(TablaSoyAcreedor(operaciones.SoyAcreedorDatosFiltro(idPersona, filtro, 1, paginado > registroTotal ? registroTotal:paginado)));
					sb.append(writeSeccionPaginado(numeroPagina, 1, paginado, registroTotal, js, ""));
				}
				else
				{
					sb.append(TablaSoyAcreedor(operaciones.SoyAcreedorDatos(idPersona, 1, paginado > registroTotal ? registroTotal:paginado)));
					sb.append(writeSeccionPaginado(numeroPagina, 1, paginado, registroTotal, js, ""));
				}
			}else{
				sb.append(" <br></br> ");
				sb.append(" <br></br> ");
				sb.append(" <div style=\" color: red; font-size: 13px;\"> NO SE ENCONTRARON RESULTADOS</div> ");
			}

			dw.setMessage(sb.toString());

		}catch (Exception e){
			e.printStackTrace();
		}

		return dw;
	}

	public MessageDwr pagSoyAcreedor(String idPersonaString, String registroTotalesString, String pagActivaString, String regPaginaString ) {	
		MessageDwr dwr = new MessageDwr();
		int pagActiva = Integer.valueOf(pagActivaString);
		int regPagina = Integer.valueOf(regPaginaString);
		int registroTotales = Integer.valueOf(registroTotalesString);
		int numeroPaginas = registroTotales/regPagina;
		int idPersona = Integer.valueOf(idPersonaString);
		OperacionesDAO operacionesDAO = new OperacionesDAO();
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
			listaPendientes = operacionesDAO.SoyAcreedorDatos(idPersona, inicio, regFinal);
			StringBuffer sb = new StringBuffer();
			sb.append("<div align=\"right\">").append("</div>");
			sb.append(TablaSoyAcreedor(listaPendientes));	
			sb.append(writeSeccionPaginado(numeroPaginas, pagActiva, regPagina,registroTotales,"pagSoyAcreedor",""));
			dwr.setMessage(sb.toString());
		} catch (Exception  e) {
			e.printStackTrace();
		}	
		return dwr;
	}
	
	public java.lang.String TablaSoyAcreedor(List<OperacionesTO> tramitesPendientes){
		StringBuffer sb = new StringBuffer();
		try{
			sb.append(" <table id=\"acreedores\" class=\"table\" data-paging=\"true\" data-filtering=\"true\" data-sorting=\"true\" > ");
			sb.append(" <thead> <tr>");
			sb.append(" <th>N&uacute;mero de Operaci&oacute;n</th>");
			sb.append(" <th>Tipo de Transacci&oacute;n</th>");
			sb.append(" <th>Fecha de Inicio</th>");
			sb.append(" <th>N&uacute;mero de Garant&iacute;a Mobiliaria</th>");
			sb.append(" <th>Deudor Garante</th>");
			sb.append(" <th>N&uacute;mero de Identificaci&oacute;n</th>");
			sb.append(" <th>Usuario</th>");
			sb.append(" <th>Opciones</th>");
			sb.append(" </tr></thead>");
			sb.append(" <tbody> ");
			if (tramitesPendientes.size() > 0){
				for(OperacionesTO tramite : tramitesPendientes){
					sb.append(" <tr>");
					//Operacion
					if(tramite.getIdTipoTramite()==3){
						sb.append(" <td style=\"vertical-align: middle;\"><div align=\"center\"><a style=\"cursor: pointer;\" onclick=\"window.location.href='"+Constants.getContextPath()+"/home/detalleAviso.do?idTramite="+tramite.getIdInscripcion()+"'\">"+tramite.getIdInscripcion()+"</a></div></td>");
					}else if(tramite.getIdTipoTramite()==2){
						sb.append(" <td style=\"vertical-align: middle;\"><div align=\"center\"><a style=\"cursor: pointer;\" onclick=\"window.location.href='"+Constants.getContextPath()+"/home/detalle.do?idGarantia="+tramite.getNumGarantia()+"&idTramite="+tramite.getIdInscripcion()+"'\">"+tramite.getIdInscripcion()+"</a></div></td>");
					}else if(tramite.getIdTipoTramite()==26 || tramite.getIdTipoTramite()==27 || tramite.getIdTipoTramite()== 28 || tramite.getIdTipoTramite()== 29 || tramite.getIdTipoTramite()==10 ){
						sb.append(" <td style=\"vertical-align: middle;\"><div align=\"center\"><a style=\"cursor: pointer;\" onclick=\"window.location.href='"+Constants.getContextPath()+"/anotacion/detalleAnotacion.do?idTramite="+tramite.getIdInscripcion()+"'\">"+tramite.getIdInscripcion()+"</a></div></td>");
					}else{
						sb.append(" <td style=\"vertical-align: middle;\"><div align=\"center\"><a style=\"cursor: pointer;\" onclick=\"window.location.href='"+Constants.getContextPath()+"/home/detalle.do?idGarantia="+tramite.getNumGarantia()+"&idTramite="+tramite.getIdInscripcion()+"'\">"+tramite.getIdInscripcion()+"</a></div></td>");
					}
					if(tramite.getIdTipoTramite()==10){
						sb.append(" <td style=\"vertical-align: middle;\"><div align=\"center\">Anotaci&oacute;n</div></td> ");
					}else{
						sb.append(" <td style=\"vertical-align: middle;\"><div align=\"center\">"+tramite.getTipoTransaccion()+"</div></td> ");
					}
					// Tipo
					sb.append(" <td  style=\"vertical-align: middle;\"><div align=\"center\">").append(tramite.getFechaOperacionInicio()).append("<br> </div> </td>");
					// Fecha
					sb.append(" <td style=\"vertical-align: middle;\"><div align=\"center\"> ").append(tramite.getNumGarantia()).append("</div></td>");
					// garantia
					List<OtorganteTO> otorgantes = tramite.getOtorgantes();
					StringBuffer sn = new StringBuffer();
					StringBuffer si = new StringBuffer();
					if(otorgantes.size()>0){
						sn.append("<table>");
						si.append("<table>");
						for(OtorganteTO otorgante : otorgantes){
							sn.append(" 				<tr>");
							sn.append(" 					<td style=\"vertical-align: middle;\" class=\"center-align\">").append(otorgante.getNombreCompleto()).append(" </td>");
							sn.append(" 				</tr>");
							si.append(" 				<tr>");
							si.append(" 					<td style=\"vertical-align: middle;\" class=\"center-align\">").append(otorgante.getTipoPersona().equalsIgnoreCase("PF")?NullsFree.getNotNullValue(otorgante.getCurp()):NullsFree.getNotNullValue(otorgante.getRfc())).append(" </td>");
							si.append(" 				</tr>");
						}
						sn.append("</table>");
						si.append("</table>");
					}else{
						sn.append("&nbsp");
						si.append("&nbsp");
					}
					sb.append("<td>");
					sb.append(sn.toString());
					sb.append("</td>");
					sb.append("<td>");
					sb.append(si.toString());
					sb.append("</td>");
					// usuario
					sb.append(" <td  style=\"vertical-align: middle;\" class=\"center-align\" >" + NullsFree.getNotNullValue(tramite.getUsuario()) + "</td>");
					// opciones
					sb.append(" <td  style=\"vertical-align: middle;\" class=\"center-align\" ><a class=\"waves-effect waves-light btn modal-trigger\" onClick=\"vinculacion("+ tramite.getNumGarantia()+ ")\"><i class=\"material-icons\">find_replace</i></a></td>");
					sb.append(" </tr>");
				}
			}else{
				sb.append(" <tr>");
				sb.append("	<td colspan=\"6\" >No existen Garantias.</td>");
				sb.append(" </tr>");
			}
			sb.append(" </tbody> ");
			sb.append(" </table>");
		}catch(Exception e){
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	public MessageDwr iniciaPagPendientes(Integer idPersona, Integer tipoOperacion) {
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
				countNumOperaciones = opService.getCountOpPendCap(idPersona);				
				metodoJS= "pagPendientes";
				break;
//			el caso 2 es para operaciones pendientes de firma
			case 2:
				countNumOperaciones = opService.getCountOpPendFirma(idPersona);
				metodoJS= "pagPendientesFirma";
				break;
			case 3:				
				countNumOperaciones = opService.getCountOpTerminadasFecha(idPersona);
				metodoJS= "pagOpTerminadas";
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
				numeroPaginas = countNumOperaciones/registroPaginas;
				registroTotales = countNumOperaciones;
			}
			if (registroTotales % registroPaginas > 0){
				numeroPaginas++;
			}
			if (countNumOperaciones>0){
				//sb.append("<div align=\"right\">")
				//.append(writeSeccionHeader(numeroPaginas, 1, registroPaginas, registroTotales,metodoJS,""))
				//.append("</div>");
				switch (tipoOperacion) {
					case 1:
						sb.append(writeTablaPendientes( opService.getOpPendientes(idPersona,1, registroPaginas>registroTotales?registroTotales:registroPaginas)));
						break;
					case 2:
						sb.append(writeTablaPendientesFirma( opService.getOpPendientesFirma(idPersona,1, registroPaginas>registroTotales?registroTotales:registroPaginas)));
						break;
					case 3:
						//se llena por primera vez la tabla						
						sb.append(writeTablaTerminadas(operacionesDAO.muestraOpTerminadasPagInicioFin(idPersona,1, registroPaginas > registroTotales?registroTotales:registroPaginas)));
						break;
					default:
						break;
				}
				sb.append(writeSeccionPaginado(numeroPaginas, 1, registroPaginas, registroTotales, metodoJS, ""));
				/*if(tipoOperacion!=3) {
					sb.append(writeSeccionPaginado(numeroPaginas, 1, registroPaginas, registroTotales, metodoJS, ""));
				}*/
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
	
	public MessageDwr iniciaPagFiltro(Integer idPersona, Integer tipoOperacion, String filtro) {
		MessageDwr dwr = new MessageDwr();
		dwr.setCodeError(0);
		java.lang.String metodoJS="";
		Integer countNumOperaciones=0;
		try{
			OperacionesDAO operacionesDAO = new OperacionesDAO();
			OperacionesService opService= new OperacionesService();
			switch (tipoOperacion) {
			//el caso 1 es para operaciones pendientes de captura de datos
			case 1:
				countNumOperaciones = opService.getCountOpPendCap(idPersona);				
				metodoJS= "pagPendientes";
				break;
//			el caso 2 es para operaciones pendientes de firma
			case 2:
				countNumOperaciones = opService.getCountOpPendFirma(idPersona);
				metodoJS= "pagPendientesFirma";
				break;
			case 3:				
				countNumOperaciones = opService.getCountOpTerminadasFechaFiltro(idPersona, filtro);
				metodoJS= "pagOpTerminadasFiltro";
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
				numeroPaginas = countNumOperaciones/registroPaginas;
				registroTotales = countNumOperaciones;
			}
			if (registroTotales % registroPaginas > 0){
				numeroPaginas++;
			}
			if (countNumOperaciones>0){
				//sb.append("<div align=\"right\">")
				//.append(writeSeccionHeader(numeroPaginas, 1, registroPaginas, registroTotales,metodoJS,""))
				//.append("</div>");
				switch (tipoOperacion) {
					case 1:
						sb.append(writeTablaPendientes( opService.getOpPendientes(idPersona,1, registroPaginas>registroTotales?registroTotales:registroPaginas)));
						break;
					case 2:
						sb.append(writeTablaPendientesFirma( opService.getOpPendientesFirma(idPersona,1, registroPaginas>registroTotales?registroTotales:registroPaginas)));
						break;
					case 3:						
						sb.append(writeTablaTerminadas(operacionesDAO.muestraOpTerminadasPagInicioFinFiltro(idPersona, filtro, 1, registroPaginas > registroTotales?registroTotales:registroPaginas)));
						break;
					default:
						break;
				}
				sb.append(writeSeccionPaginado(numeroPaginas, 1, registroPaginas, registroTotales, metodoJS, filtro));
				/*if(tipoOperacion!=3) {
					sb.append(writeSeccionPaginado(numeroPaginas, 1, registroPaginas, registroTotales, metodoJS, ""));
				}*/
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
	
	public MessageDwr iniciaPagPenAsiMultiples(Integer idPersona) {
		MessageDwr dwr = new MessageDwr();
		dwr.setCodeError(0);
		Integer countNumOperaciones=0;
		try{
			OperacionesService opService= new OperacionesService();
//			listaPendientesFirmaMasiva = opService.muestraOpPendientesFirmaMasiva(idPersona);
			countNumOperaciones = opService.getCountOpPendFirmaMasiva(idPersona);
//			listaPendientes = opService.getOpPendientes(idPersona);
			StringBuffer sb = new StringBuffer();
			int numeroPaginas = 0;
			int registroTotales = 0;
			Integer registroPaginas = 20;
			if (countNumOperaciones>0){
				//System.out.println(":::::La lista viene con elementos ");
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
						sb.append(writeTablaPenAsientosMultiples( opService.muestraOpPendientesFirmaMasiva(idPersona,1, registroPaginas>registroTotales?registroTotales:registroPaginas)));
				
				//sb.append(writeSeccionPaginado(numeroPaginas, 1, registroPaginas, registroTotales, "pagPenAsiMultiples", ""));
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
	
	public MessageDwr pagOperacionesPendientes(String idPersonaString, String registroTotalesString, String pagActivaString, String regPaginaString ) {	
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
			listaPendientes = opService.getOpPendientes(idPersona, inicio, regFinal);
			StringBuffer sb = new StringBuffer();
			sb.append("<div align=\"right\">")
//			.append(writeSeccionHeader(numeroPaginas, pagActiva, regPagina, registroTotales,"pagPendientes",""))
			.append("</div>");
			sb.append(writeTablaPendientes(listaPendientes));	
			sb.append(writeSeccionPaginado(numeroPaginas, pagActiva, regPagina,registroTotales,"pagPendientes",""));
			dwr.setMessage(sb.toString());
		} catch (Exception  e) {
			e.printStackTrace();
		}	
		return dwr;
	}
	
	public MessageDwr pagOperacionesPendientesFirma(String idPersonaString, String registroTotalesString, String pagActivaString, String regPaginaString ) {	
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
			listaPendientes = opService.getOpPendientesFirma(idPersona, inicio, regFinal);
			System.out.println("Lista Pendientes de Firma Tama�o: "+listaPendientes.size());
			StringBuffer sb = new StringBuffer();
//			sb.append("<div align=\"right\">")
//			.append(writeSeccionHeader(numeroPaginas, pagActiva, regPagina, registroTotales,"pagPendientesFirma",""))
//			.append("</div>");
			sb.append(writeTablaPendientesFirma(listaPendientes));
			sb.append(writeSeccionPaginado(numeroPaginas, pagActiva, regPagina,registroTotales,"pagPendientesFirma",""));
			dwr.setMessage(sb.toString());
		} catch (Exception  e) {
			e.printStackTrace();
		}	
		return dwr;
	}
	
	public MessageDwr pagOperacionesPenAsienMul(String idPersonaString, String registroTotalesString, String pagActivaString, String regPaginaString ) {	
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
			listaPendientesFirmaMasiva = opService.muestraOpPendientesFirmaMasiva(idPersona, inicio, regFinal);
			StringBuffer sb = new StringBuffer();
			sb.append("<div align=\"right\">")
//			.append(writeSeccionHeader(numeroPaginas, pagActiva, regPagina, registroTotales,"pagPendientes",""))
			.append("</div>");
			sb.append(writeTablaPenAsientosMultiples(listaPendientesFirmaMasiva));	
			sb.append(writeSeccionPaginado(numeroPaginas, pagActiva, regPagina,registroTotales,"pagPenAsiMultiples",""));
			dwr.setMessage(sb.toString());
		} catch (Exception  e) {
			e.printStackTrace();
		}	
		return dwr;
	}
	
	public MessageDwr pagOperacionesTerminadasFiltro(String idPersonaString, String filtro, String registroTotalesString, String pagActivaString, String regPaginaString ) {	
		MessageDwr dwr = new MessageDwr();
		
		int pagActiva = Integer.valueOf(pagActivaString);
		int regPagina = Integer.valueOf(regPaginaString);
		int registroTotales = Integer.valueOf(registroTotalesString);
		int numeroPaginas = registroTotales/regPagina;
		int idPersona = Integer.valueOf(idPersonaString);
		OperacionesDAO operacionesDAO = new OperacionesDAO();
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
			//OperacionesService opService= new OperacionesService();
			listaPendientes = operacionesDAO.muestraOpTerminadasPagInicioFinFiltro(idPersona, filtro, inicio, regFinal);
			StringBuffer sb = new StringBuffer();
			sb.append("<div align=\"right\">")
//			.append(writeSeccionHeader(numeroPaginas, pagActiva, regPagina, registroTotales,"pagPendientes",""))
			.append("</div>");
			sb.append(writeTablaTerminadas(listaPendientes));	
			sb.append(writeSeccionPaginado(numeroPaginas, pagActiva, regPagina,registroTotales,"pagOpTerminadasFiltro",filtro));
			dwr.setMessage(sb.toString());
		} catch (Exception  e) {
			e.printStackTrace();
		}	
		return dwr;
	}
	
	public MessageDwr pagOperacionesTerminadas(String idPersonaString, String registroTotalesString, String pagActivaString, String regPaginaString ) {	
		MessageDwr dwr = new MessageDwr();
		
		int pagActiva = Integer.valueOf(pagActivaString);
		int regPagina = Integer.valueOf(regPaginaString);
		int registroTotales = Integer.valueOf(registroTotalesString);
		int numeroPaginas = registroTotales/regPagina;
		int idPersona = Integer.valueOf(idPersonaString);
		OperacionesDAO operacionesDAO = new OperacionesDAO();
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
			//OperacionesService opService= new OperacionesService();
			listaPendientes = operacionesDAO.muestraOpTerminadasPagInicioFin(idPersona, inicio, regFinal);
			// listaPendientesExcel = operacionesDAO.muestraOpTerminadasPagInicioFinExcel(idPersona);
			StringBuffer sb = new StringBuffer();
			sb.append("<div align=\"right\">")
//			.append(writeSeccionHeader(numeroPaginas, pagActiva, regPagina, registroTotales,"pagPendientes",""))
			.append("</div>");
			//Tabla de terminadas
			System.out.println("Registros:"+regPagina);
			sb.append(writeTablaTerminadas(listaPendientes));	
			// sb.append(writeTablaTerminadas(listaPendientesExcel));	
			sb.append(writeSeccionPaginado(numeroPaginas, pagActiva, regPagina,registroTotales,"pagOpTerminadas",""));
			dwr.setMessage(sb.toString());
		} catch (Exception  e) {
			e.printStackTrace();
		}	
		return dwr;
	}

	// public MessageDwr ExportExcel(String idPersonaString,String nombre){
	public String ExportExcel() throws Exception {

		

		OperacionesDAO operacionesDAO = new OperacionesDAO();
		List<OperacionesTO> listadoPendientesExcel = operacionesDAO.muestraOpTerminadasPagInicioFinExcel(Integer.parseInt(getPersona()), getFiltroExcel());

	// public String ExportExcel(String idPersonaString,String nombre) throws Exception {


    // MessageDwr dwr = new MessageDwr();
		//int idPersona = Integer.valueOf(idPersonaString);

			// StringBuffer sb = new StringBuffer();
			// sb.append("<div align=\"right\">")
			// .append(listadoPendientesExcel)
			// .append("</div>");
			// dwr.setMessage(sb.toString());

			ExcelCreator excelCreator = new ExcelCreator();
			//XSSFWorkbook workbook = excelCreator.createTramitesWorkbook(listadoPendientesExcel);
                        XSSFWorkbook workbook = excelCreator.createOperacionesWorkbook(listadoPendientesExcel);
			ServletActionContext.getResponse().setHeader("Content-Disposition", "attachment; filename=Operaciones.xlsx");
			ServletOutputStream out = ServletActionContext.getResponse().getOutputStream();
			workbook.write(out);
			out.flush();
			out.close();
			return null;

		// return dwr;

	}

	public String ExportExcelAcreedores() throws Exception
	{

		OperacionesDAO operaciones = new OperacionesDAO();
		if (!getFiltroExcel().isEmpty())
		{

			List<OperacionesTO> listadoPendientesExcel = operaciones.SoyAcreedorDatosFiltro(Integer.parseInt(getPersona()), getFiltroExcel(), 1, 20);
			ExcelCreator excelCreator = new ExcelCreator();
			XSSFWorkbook workbook = excelCreator.createOperacionesWorkbook(listadoPendientesExcel);
			ServletActionContext.getResponse().setHeader("Content-Disposition", "attachment; filename=AcrredoresFiltro.xlsx");
			ServletOutputStream out = ServletActionContext.getResponse().getOutputStream();
			workbook.write(out);
			out.flush();
			out.close();
		}
		else
		{
			List<OperacionesTO> listadoPendientesExcel = operaciones.SoyAcreedorDatos(Integer.parseInt(getPersona()), 1, 20);
			ExcelCreator excelCreator = new ExcelCreator();
			XSSFWorkbook workbook = excelCreator.createOperacionesWorkbook(listadoPendientesExcel);
			ServletActionContext.getResponse().setHeader("Content-Disposition", "attachment; filename=Acreedores.xlsx");
			ServletOutputStream out = ServletActionContext.getResponse().getOutputStream();
			workbook.write(out);
			out.flush();
			out.close();
		}
		return null;
	}

	public String writeSeccionHeader(int numeroPaginas,int pagActiva, int regPagina, 
			int registroTotales, String metodoJs, String parametros){
		StringBuffer sb = new StringBuffer();
		if (registroTotales>0 && registroTotales>5){
			sb.append(" <div  class=\"form-inline\"> <select value=\"")
				.append(regPagina).append("\" id=\"")
				.append(metodoJs).append("ID").append("\">");
			sb.append(" <option value=\"5\"> 5 </option>");
			sb.append(" <option value=\"10\"> 10 </option>");
			sb.append(" <option value=\"15\"> 15 </option> </select>")
				.append(" <input type=\"button\" class=\"btn btn-primary\" onclick=\"").append(metodoJs)
				.append("(")
				.append(registroTotales).append(",")
				.append(pagActiva).append(",")
				.append("getObject('")
				.append(metodoJs).append("ID")
				.append("').value,")
				.append("'").append(parametros).append("'")
			.append(")\" value=\"Aceptar\"/>");
			sb.append(" </div> ");
			sb.append(" <script> getObject('").append(metodoJs).append("ID").append("').value=").append(regPagina).append(" </script>");
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
			sb.append("<td>").append(" <div  class=\"pagination\"> <ul>");
			
			if (numeroPaginas> 5){
				if (pagActiva > 1){
					sb.append("<li><a style='cursor: pointer;' onclick=\"").append(metodoJs).append("(")
					.append(registroTotales).append(",")
					.append(pagActiva-1).append(",")
					.append(regPagina).append(",")
					.append("'").append(parametros).append("'")					
					.append(")\">").append(" Anterior <span class=\"icon-fast-backward\"> </span>").append("</a></li>");
				}
				for (int x=(pagActiva-2<1?1:pagActiva-2);x<=(pagActiva+2>numeroPaginas?numeroPaginas:pagActiva+2); x++){
					if (x==pagActiva){
						sb.append("<li class=\"active\"><a  href=\"#\">").append(x).append("</a></li>");
					}else{
						sb.append("<li><a style='cursor: pointer;' onclick=\"").append(metodoJs).append("(")
						.append(registroTotales).append(",")
						.append(x).append(",")
						.append(regPagina).append(",")
						.append("'").append(parametros).append("'")						
						.append(")\">").append(x).append("</a></li>");
					}
				}
				if (pagActiva < numeroPaginas){
					sb.append("<li><a style='cursor: pointer;' onclick=\"").append(metodoJs).append("(")
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
						sb.append("<li><a style='cursor: pointer;' onclick=\"").append(metodoJs).append("(")
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
			sb.append(" <table class=\"table\" data-paging=\"true\" data-filtering=\"true\" data-sorting=\"true\"> ");
			sb.append(" 	<thead> ");	
			sb.append(" 		<tr> ");
			sb.append("				<th>Tipo de Operaci&oacute;n</th>");
			sb.append(" 			<th>N&uacute;mero de Garant&iacute;a</th>");
			sb.append(" 			<th>Deudor Garante</th>");
			sb.append(" 			<th>N&uacute;mero de Identificaci&oacute;n</th>");			
			sb.append(" 			<th>Fecha de Inicio</th>");
			sb.append(" 			<th>Opciones</th>");
			sb.append(" 		</tr> ");
			sb.append(" 	</thead>");
			sb.append(" <tbody> ");
			if (tramitesPendientes.size() > 0){				
				for(OperacionesTO tramite : tramitesPendientes){
					sb.append(" 		<tr>");
					sb.append(" 			<td style=\"vertical-align: middle;\" class=\"center-align\">").append(tramite.getTipoTransaccion()).append(" </td>");
					sb.append(" 			<td style=\"vertical-align: middle;\" class=\"center-align\">");
					sb.append(" 				<div align=\"center\"> ");
													sb.append(tramite.getNumGarantia());
					sb.append(" 				</div> ");
					sb.append(" 			</td>");
					/*sb.append(" 			<td>");
					sb.append(" 				<div align=\"center\">");
					sb.append("						<table width=\"100%\">");*/
					List<OtorganteTO> otorgantes = tramite.getOtorgantes();
					StringBuffer sn = new StringBuffer();
					StringBuffer si = new StringBuffer();
					if(otorgantes.size()>0){												
						sn.append("<table>");
						si.append("<table>");
						for(OtorganteTO otorgante : otorgantes){
							sn.append(" 				<tr>");
							sn.append(" 					<td style=\"vertical-align: middle;\" class=\"center-align\">").append(otorgante.getNombreCompleto()).append(" </td>");
							sn.append(" 				</tr>");
							si.append(" 				<tr>");
							si.append(" 					<td style=\"vertical-align: middle;\" class=\"center-align\">").append(otorgante.getTipoPersona()=="PF"?otorgante.getCurp():otorgante.getRfc()).append(" </td>");
							si.append(" 				</tr>");
						}
						sn.append("</table>");
						si.append("</table>");
					}else{
						sn.append("&nbsp");
						si.append("&nbsp");
					}
					sb.append("<td>");
					sb.append(sn.toString());
					sb.append("</td>");
					sb.append("<td>");
					sb.append(si.toString());
					sb.append("</td>");
					sb.append(" 			<td style=\"vertical-align: middle;\" class=\"center-align\">");
					sb.append(" 				<div align=\"center\"> ");
													sb.append(tramite.getFechaInicio());
					sb.append(" 				</div> ");								
					sb.append(" 			</td>");
					sb.append(" 			<td>");
					sb.append("					<table width=\"100%\">");
					sb.append(" 					<tr> ");
					sb.append(" 						<td align=\"center\"> ");
                                        
                                        //sb.append(" 							<a class=\"btn waves-effect indigo darken-4\" onclick=\"window.location.href='"+Constants.getContextPath()).append(tramite.getPaso()).append("?idInscripcion=").append(tramite.getIdInscripcion()).append("'\"><i class=\"material-icons\">edit</i></a> ");
                                        
                                        String paso = tramite.getPaso();


                                        if(tramite.getIdTipoGarantia() != null && tramite.getIdTipoGarantia() == 16  )
                                            paso = paso.replaceAll("inscripcion", "leasing");
                                            
                                        
                                            
                                        sb.append(" 							<a class=\"btn waves-effect indigo darken-4\" onclick=\"window.location.href='"+Constants.getContextPath()).append(paso).append("?idInscripcion=").append(tramite.getIdInscripcion()).append("'\"><i class=\"material-icons\">edit</i></a> ");                                            
					
                                        
                                        sb.append("							</td>");
					sb.append(" 						<td align=\"center\"> ");
					sb.append(" 							<a class=\"btn waves-effect red darken-4\" onclick=\"window.location.href='"+Constants.getContextPath()+"/home/eliminarTP.do?idTramitePendiente=").append(tramite.getIdInscripcion()).append("'\" ><i class=\"material-icons\">delete</i></a> ");
					sb.append(" 						</td>");
					sb.append(" 					</tr>");
					sb.append("					</table>");
					sb.append(" 			</td> ");
					sb.append(" 		</tr>");
				}				
			}else{
				sb.append(" <tr>");
				sb.append("	<td colspan=\"6\">No existen Operaciones Pendientes.</td>");
				sb.append(" </tr>");
			}
			sb.append(" </tbody> ");
			sb.append(" </table>");
			sb.append("<script type=\"text/javascript\">");
			sb.append("    $(function () {");
			sb.append("        $('.table').footable({");
			sb.append("            filtering: {");
			sb.append("            		placeholder: 'Buscar',");
			sb.append("            		position: 'left',");
			sb.append("            		empty: 'No se encontraron resultados'");
			sb.append("            	}");
			sb.append("        })    ");
			sb.append("        })    ");
			sb.append("</script> ");
		}catch(Exception e){
			e.printStackTrace();
		}	
		return sb.toString();
	}
	
	public String writeTablaPendientesFirma(List<OperacionesTO> tramitesPendientes){
		StringBuffer sb = new StringBuffer();
		try{
			sb.append(" <table class=\"table\" data-paging=\"true\" data-filtering=\"true\" data-sorting=\"true\"> ");
			sb.append(" 	<thead> ");
			sb.append(" 		<tr> ");
			sb.append(" 			<th>Tipo de Operaci&oacute;n</th>");
			sb.append(" 			<th>Numero de Garant&iacute;a</th>");
			sb.append(" 			<th>Deudor Garante</th>");
			sb.append(" 			<th>N&uacute;mero de Identificaci&oacute;n</th>");
			sb.append(" 			<th>Fecha de Inicio</th>");
			sb.append(" 			<th>Opciones</th>");
			sb.append(" 		</tr> ");
			sb.append(" 	</thead>");
			sb.append(" <tbody> ");			
			if (tramitesPendientes.size() > 0){				
				for(OperacionesTO tramite : tramitesPendientes){
					sb.append(" 		<tr>");
					sb.append(" 			<td style=\"vertical-align: middle;\" class=\"center-align\">").append(tramite.getTipoTransaccion()).append(" </td>");
					sb.append(" 			<td style=\"vertical-align: middle;\" class=\"center-align\">");
					if(tramite.getTipoTransaccion().contains("Inscr")){
						sb.append(" 			<div align=\"center\"> ");
						sb.append(" <a style=\"cursor:pointer;\" onclick=\"muestraInfo('"+tramite.getIdInscripcion()+"');\"  class='thickbox'> "+tramite.getIdInscripcion()+" </a> ");

						sb.append(" 			</div> ");
					}else{
						sb.append(" 			<div align=\"center\"> ");
													sb.append(tramite.getTipoTransaccion());
						sb.append(" 			</div> ");
					}
					sb.append(" 			</td> ");
					/*sb.append(" 			<td width=\"515px\" class=\"cuerpo1TablaResumen\">");
					sb.append(" 				<div align=\"center\">");
					sb.append("						<table width=\"100%\">");*/
					List<OtorganteTO> otorgantes = tramite.getOtorgantes();
					StringBuffer sn = new StringBuffer();
					StringBuffer si = new StringBuffer();
					if(otorgantes.size()>0){
						sn.append("<table>");
						si.append("<table>");
						for(OtorganteTO otorgante : otorgantes){
							sn.append(" 				<tr>");
							sn.append(" 					<td style=\"vertical-align: middle;\" class=\"center-align\">").append(otorgante.getNombreCompleto()).append(" </td>");
							sn.append(" 				</tr>");
							si.append(" 				<tr>");
							si.append(" 					<td style=\"vertical-align: middle;\" class=\"center-align\">").append(otorgante.getTipoPersona()=="PF"?otorgante.getCurp():otorgante.getRfc()).append(" </td>");
							si.append(" 				</tr>");
						}
						sn.append("</table>");
						si.append("</table>");
					}else{
						sn.append("&nbsp");
						si.append("&nbsp");
					}
					sb.append("<td>");
					sb.append(sn.toString());
					sb.append("</td>");
					sb.append("<td>");
					sb.append(si.toString());
					sb.append("</td>");
					sb.append(" 			<td style=\"vertical-align: middle;\" class=\"center-align\">");
					sb.append(" 				<div align=\"center\">");
													sb.append(tramite.getFechaInicio());
					sb.append(" 				</div>");
					sb.append(" 			</td> ");
					sb.append(" 			<td> ");
					sb.append("					<table width=\"100%\">");
					sb.append(" 					<tr>");
					sb.append("							<td><a title=\"Confirmar\" class=\"btn waves-effect indigo darken-4\" onclick=\"window.location.href='"+Constants.getContextPath()).append("/firma/firma.do").append("?idTramite=").append(tramite.getIdInscripcion()).append("'\"><i class=\"material-icons\">check_box</i></a> </td>");
					sb.append(" 						<td><a title=\"Eliminar\" class=\"btn waves-effect red darken-4\" onclick=\"window.location.href='"+Constants.getContextPath()+"/home/eliminarTP.do?idTramitePendiente=").append(tramite.getIdInscripcion()).append("'\"><i class=\"material-icons\">delete</i></a> </td>");
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
			sb.append(" </tbody> ");
			sb.append(" </table>");
			sb.append("<script type=\"text/javascript\">");
			sb.append("    $(function () {");
			sb.append("        $('.table').footable({");
			sb.append("            filtering: {");
			sb.append("            		placeholder: 'Buscar',");
			sb.append("            		position: 'left',");
			sb.append("            		empty: 'No se encontraron resultados'");
			sb.append("            	}");
			sb.append("        })    ");
			sb.append("        })    ");
			sb.append("</script> ");
		}catch(Exception e){
			e.printStackTrace();
		}	
		return sb.toString();
	}
	
	public String writeTablaPenAsientosMultiples(List<CargaMasivaResumenTO> tramitesPendientes){
		StringBuffer sb = new StringBuffer();
		try{
			sb.append(" <table class=\"table\" data-paging=\"true\" data-filtering=\"true\" data-sorting=\"true\" > ");
			sb.append(" <thead> ");
			sb.append(" <tr> ");
			sb.append(	"<th>Tipo de Operacion</th>");
			sb.append(" <th>Nombre del archivo</th>");
			sb.append(" <th>Clave de Rastreo</th>");
			sb.append(" <th>Tramites Correctos</th>");
			sb.append(" <th>Tramites Incorrectos</th>");
			sb.append(" <th>Opciones</th>");
			sb.append(" </tr>");
			sb.append(" </thead>");
			sb.append(" <tbody> ");
			if (tramitesPendientes.size() > 0){
				for(CargaMasivaResumenTO tramite : tramitesPendientes){
					sb.append(" <tr>");
					sb.append(" <td style=\"vertical-align: middle;\" class=\"center-align\"><div align=\"center\"> ").append(tramite.getTipoTramiteFirma()).append(" multiple").append("</div></td>");
					sb.append(" <td style=\"vertical-align: middle;\" class=\"center-align\"><div align=\"center\"> ").append(tramite.getNombreArchivo()).append("</div></td>");
					sb.append(" <td style=\"vertical-align: middle;\" class=\"center-align\"><div align=\"center\"> ").append(tramite.getClaveRastreo()).append("</div></td>");
					sb.append(" <td style=\"vertical-align: middle;\" class=\"center-align\"><div align=\"center\"> ").append(tramite.getBuenas()).append("</div></td>");
					sb.append(" <td style=\"vertical-align: middle;\" class=\"center-align\"><div align=\"center\"> ").append(tramite.getMalas()).append("</div></td>");
					sb.append(" <td>");
					sb.append(" 	<table width=\"100%\">");
					sb.append(" 		<tr>");
					sb.append(" 			<td><a title=\"Confirmar\" class=\"btn waves-effect indigo darken-4\" onclick=\"window.location.href='"+Constants.getContextPath()+"/firma/firma.do?idTramite="+tramite.getIdFirmaMasivaTemp()+"'\"><i class=\"material-icons\">check_box</i></a></td> ");
					sb.append(" 			<td><a title=\"Eliminar\" class=\"btn waves-effect red darken-4\" onclick=\"window.location.href='"+Constants.getContextPath()+"/home/eliminarTP.do?idTramitePendiente="+tramite.getIdFirmaMasivaTemp()+"'\"><i class=\"material-icons\">delete</i></a></td> ");
					sb.append(" 		</tr>");
					sb.append(" 	</table>");
					sb.append(" </tr>");
					
				}
			}else{
				sb.append(" <tr>");
				sb.append("	<td colspan=\"6\" >No existen Operaciones Pendientes de Firma Asientos Multiples.</td>");
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
			sb.append(" <table id=\"prueba\" class=\"table\" data-paging=\"true\" data-filtering=\"true\" data-sorting=\"true\" > ");			
			sb.append(" <thead> <tr>");			
			sb.append(" <th>N&uacute;mero de Operaci&oacute;n</th>");
			sb.append(" <th>Tipo de Transacci&oacute;n</th>");
			sb.append(" <th>Fecha de Inicio</th>");
			sb.append(" <th>N&uacute;mero de Garant&iacute;a Mobiliaria</th>");
			sb.append(" <th>Deudor Garante</th>");
			sb.append(" <th>N&uacute;mero de Identificaci&oacute;n</th>");
			sb.append(" <th>Usuario</th>");
			sb.append(" </tr></thead>");
			sb.append(" <tbody> ");
			if (tramitesPendientes.size() > 0){
				for(OperacionesTO tramite : tramitesPendientes){
					sb.append(" <tr>");
					if(tramite.getIdTipoTramite()==3){
						sb.append(" <td style=\"vertical-align: middle;\"><div align=\"center\"><a style=\"cursor: pointer;\" onclick=\"window.location.href='"+Constants.getContextPath()+"/home/detalleAviso.do?idTramite="+tramite.getIdInscripcion()+"'\">"+tramite.getIdInscripcion()+"</a></div></td>"); 
					}else if(tramite.getIdTipoTramite()==2){
						sb.append(" <td style=\"vertical-align: middle;\"><div align=\"center\"><a style=\"cursor: pointer;\" onclick=\"window.location.href='"+Constants.getContextPath()+"/home/detalle.do?idGarantia="+tramite.getNumGarantia()+"&idTramite="+tramite.getIdInscripcion()+"'\">"+tramite.getIdInscripcion()+"</a></div></td>");
					}else if(tramite.getIdTipoTramite()==26 || tramite.getIdTipoTramite()==27 || tramite.getIdTipoTramite()== 28 || tramite.getIdTipoTramite()== 29 || tramite.getIdTipoTramite()==10 ){
						sb.append(" <td style=\"vertical-align: middle;\"><div align=\"center\"><a style=\"cursor: pointer;\" onclick=\"window.location.href='"+Constants.getContextPath()+"/anotacion/detalleAnotacion.do?idTramite="+tramite.getIdInscripcion()+"'\">"+tramite.getIdInscripcion()+"</a></div></td>");	
					}else{
						sb.append(" <td style=\"vertical-align: middle;\"><div align=\"center\"><a style=\"cursor: pointer;\" onclick=\"window.location.href='"+Constants.getContextPath()+"/home/detalle.do?idGarantia="+tramite.getNumGarantia()+"&idTramite="+tramite.getIdInscripcion()+"'\">"+tramite.getIdInscripcion()+"</a></div></td>");
					}
					if(tramite.getIdTipoTramite()==10){
						sb.append(" <td style=\"vertical-align: middle;\"><div align=\"center\">Anotaci&oacute;n</div></td> ");
					}else{
						sb.append(" <td style=\"vertical-align: middle;\"><div align=\"center\">"+tramite.getTipoTransaccion()+"</div></td> ");
					}
					sb.append(" <td  style=\"vertical-align: middle;\"><div align=\"center\">").append(tramite.getFechaOperacionInicio()).append("<br> </div> </td>");
					sb.append(" <td style=\"vertical-align: middle;\"><div align=\"center\"> ").append(tramite.getNumGarantia()).append("</div></td>");
					/*sb.append(" <td>");
					sb.append(" 	<div align=\"center\">");
					sb.append(" 		<table width=\"100%\">");*/
					List<OtorganteTO> otorgantes = tramite.getOtorgantes();
					StringBuffer sn = new StringBuffer();
					StringBuffer si = new StringBuffer();
					if(otorgantes.size()>0){
						sn.append("<table>");
						si.append("<table>");
						for(OtorganteTO otorgante : otorgantes){
							sn.append(" 				<tr>");
							sn.append(" 					<td style=\"vertical-align: middle;\" class=\"center-align\">").append(otorgante.getNombreCompleto()).append(" </td>");
							sn.append(" 				</tr>");
							si.append(" 				<tr>");
							si.append(" 					<td style=\"vertical-align: middle;\" class=\"center-align\">").append(otorgante.getTipoPersona().equalsIgnoreCase("PF")?NullsFree.getNotNullValue(otorgante.getCurp()):NullsFree.getNotNullValue(otorgante.getRfc())).append(" </td>");
							si.append(" 				</tr>");
						}
						sn.append("</table>");
						si.append("</table>");
					}else{
						sn.append("&nbsp");
						si.append("&nbsp");
					}
					sb.append("<td>");
					sb.append(sn.toString());
					sb.append("</td>");
					sb.append("<td>");
					sb.append(si.toString());
					sb.append("</td>");
										
					sb.append(" <td style=\"vertical-align: middle;\" class=\"center-align\" >" + NullsFree.getNotNullValue(tramite.getUsuario()) + "</td>");
					sb.append(" </tr>");
				}
			}else{
				sb.append(" <tr>");
				sb.append("	<td colspan=\"6\" >No existen Operaciones Terminadas.</td>");
				sb.append(" </tr>");
			}
			sb.append(" </tbody> ");
			sb.append(" </table>");
			//sb.append("<script src=\"http://code.jquery.com/jquery-3.3.1.min.js\" integrity=\"sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=\" crossorigin=\"anonymous\"></script>");			
			//sb.append("<script src=\"<%=request.getContextPath()%>/resources/js/footable.min.js\"></script>");
			/*sb.append("<script type=\"text/javascript\">");
			sb.append("    $(function () {");
			sb.append("        $('.table').footable({");
			sb.append("            filtering: {");
			sb.append("            		placeholder: 'Buscar',");
			sb.append("            		position: 'left',");
			sb.append("            		empty: 'No se encontraron resultados'");
			sb.append("            	}");
			sb.append("        })    ");
			sb.append("        })    ");
			sb.append("</script> ");*/
		}catch(Exception e){
			e.printStackTrace();
		}	
		return sb.toString();
	}

    /**
     * @return the persona
     */
    public String getPersona() {
        return persona;
    }

    /**
     * @param persona the persona to set
     */
    public void setPersona(String persona) {
        this.persona = persona;
    }

    /**
     * @return the filtroExcel
     */
    public String getFiltroExcel() {
        return filtroExcel;
    }

    /**
     * @param filtroExcel the filtroExcel to set
     */
    public void setFiltroExcel(String filtroExcel) {
        this.filtroExcel = filtroExcel;
    }
	
	
}
