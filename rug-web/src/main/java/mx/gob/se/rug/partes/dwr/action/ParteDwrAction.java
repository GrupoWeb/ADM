package mx.gob.se.rug.partes.dwr.action;

/**
 * @author Licenciado Abraham Stalin Aguilar Valencia
 * Componente para el control de todas las partes
 * del RUG, favor de no modificar
 * sin antes informarme de ello.
 * 
 */
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//import com.sun.xml.rpc.processor.modeler.j2ee.xml.string;

import mx.gob.economia.dgi.framework.dwr.action.AbstractBaseDwrAction;
import mx.gob.rug.curp.consulta.ws.RenapoConsultaCurp;
import mx.gob.se.rug.acreedores.dao.impl.AcreedoresCatalogosDaoJdbcImpl;
import mx.gob.se.rug.acreedores.dao.impl.GrupoDaoJdbcImpl;
import mx.gob.se.rug.acreedores.service.AcreedoresService;
import mx.gob.se.rug.acreedores.service.impl.AcreedoresServiceImpl;
import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.acreedores.to.GrupoPerfilTO;
import mx.gob.se.rug.acreedores.to.TramitesMasivosTO;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.exception.AltaParteException;
import mx.gob.se.rug.garantia.dao.GarantiasDAO;
import mx.gob.se.rug.inscripcion.dao.AltaParteDAO;
import mx.gob.se.rug.inscripcion.dao.InscripcionDAO;
import mx.gob.se.rug.inscripcion.dao.NacionalidadDAO;
import mx.gob.se.rug.inscripcion.service.InscripcionService;
import mx.gob.se.rug.inscripcion.service.impl.InscripcionServiceImpl;
import mx.gob.se.rug.inscripcion.to.AltaParteTO;
import mx.gob.se.rug.inscripcion.to.BienEspecialTO;
import mx.gob.se.rug.inscripcion.to.DeudorTO;
import mx.gob.se.rug.inscripcion.to.InscripcionTO;
import mx.gob.se.rug.inscripcion.to.NacionalidadTO;
import mx.gob.se.rug.inscripcion.to.OtorganteTO;
import mx.gob.se.rug.partes.dao.FolioElectronicoDAO;
import mx.gob.se.rug.seguridad.dao.PrivilegiosDAO;
import mx.gob.se.rug.seguridad.to.PrivilegioTO;
import mx.gob.se.rug.seguridad.to.PrivilegiosTO;
import mx.gob.se.rug.to.MessageDwr;
import mx.gob.se.rug.to.PersonaTO;
import mx.gob.se.rug.to.PlSql;
import mx.gob.se.rug.to.TipoPersona;
import mx.gob.se.rug.to.TipoTo;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.usuario.dao.impl.ControlUsuarioDaoJdbcImpl;
import mx.gob.se.rug.util.CharSetUtil;
import mx.gob.se.rug.util.ExpresionesRegularesUtil;
import mx.gob.se.rug.util.MyLogger;
import mx.gob.se.rug.util.to.DateUtilRug;

import mx.gob.se.rug.inscripcion.dao.WSExternoDAO;
import mx.gob.se.rug.inscripcion.to.WSExternoTO;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;


public class ParteDwrAction extends AbstractBaseDwrAction {
        private RestTemplate restTemplate;
	private AcreedoresService acreedoresService = new AcreedoresServiceImpl();
	
	private InscripcionService inscripcionService = new InscripcionServiceImpl();
	
	public MessageDwr registrarBien(String elementId, String idTramite, String mdDescripcion, String idTipo, String mdIdentificador,
//			String mdIdentificador1, String mdIdentificador2, String mdIdentificador3) {
			String mdIdentificador1, String mdIdentificador2) {
		MessageDwr dwr = new MessageDwr();
		
		MyLogger.Logger.log(Level.INFO, "Entre a registar bien");
		
		try {		
			BienEspecialTO bienEspecialTO = new BienEspecialTO();
			bienEspecialTO.setIdTramite(new Integer(idTramite));
			bienEspecialTO.setDescripcion(mdDescripcion);
			bienEspecialTO.setTipoBien(new Integer(idTipo));
			System.out.println("Tipo bien" + idTipo);
			if(idTipo.equalsIgnoreCase("1")) {//vehiculos		
				if(!mdIdentificador2.equalsIgnoreCase("")) {
					bienEspecialTO.setIdentificador(mdIdentificador2);
					bienEspecialTO.setTipoIdentificador(2); //VIN
				} else if(!mdIdentificador.equalsIgnoreCase("")&&!mdIdentificador1.equalsIgnoreCase("")) {					
					bienEspecialTO.setIdentificador(mdIdentificador+"-"+mdIdentificador1);
					bienEspecialTO.setTipoIdentificador(1); //por placa y uso
				}
			} else if(idTipo.equalsIgnoreCase("2")) {//facturas
				bienEspecialTO.setIdentificador(mdIdentificador2);
				bienEspecialTO.setTipoIdentificador(3); // no factura
                                
                                
			} else if(idTipo.equalsIgnoreCase("5")) {//facturas
				if(!mdIdentificador2.equalsIgnoreCase("")) {
					bienEspecialTO.setIdentificador(mdIdentificador2);
					bienEspecialTO.setTipoIdentificador(2); //VIN
				} else if(!mdIdentificador.equalsIgnoreCase("")&&!mdIdentificador1.equalsIgnoreCase("")) {					
					bienEspecialTO.setIdentificador(mdIdentificador+"-"+mdIdentificador1);
					bienEspecialTO.setTipoIdentificador(1); //por placa y uso
				}
			} else {
				bienEspecialTO.setIdentificador(mdIdentificador2);
				bienEspecialTO.setTipoIdentificador(4); // no serie
			}
			inscripcionService.registrarBien(bienEspecialTO);
			
			dwr = getParteBienes(elementId, idTramite);
		} catch(Exception e) {
			e.printStackTrace();
		}
			
		return dwr;
	}

        //corellana: se agrego para RUG, hector solicita se grabe el numero de serie)
	public MessageDwr registrarBien_v2(String elementId, String idTramite, String mdDescripcion, String idTipo, String mdIdentificador,
			String mdIdentificador1, String mdIdentificador2, String mdIdentificador3) {
		MessageDwr dwr = new MessageDwr();
		
		MyLogger.Logger.log(Level.INFO, "Entre a registar bien");
		
		try {		
			BienEspecialTO bienEspecialTO = new BienEspecialTO();
			bienEspecialTO.setIdTramite(new Integer(idTramite));
			bienEspecialTO.setDescripcion(mdDescripcion);
			bienEspecialTO.setTipoBien(new Integer(idTipo));
			if(idTipo.equalsIgnoreCase("1")) {//vehiculos		
				if(!mdIdentificador2.equalsIgnoreCase("")) {
					bienEspecialTO.setIdentificador(mdIdentificador2);
					bienEspecialTO.setTipoIdentificador(2); //VIN
				} else if(!mdIdentificador.equalsIgnoreCase("")&&!mdIdentificador1.equalsIgnoreCase("")) {					
					bienEspecialTO.setIdentificador(mdIdentificador+"-"+mdIdentificador1);
					bienEspecialTO.setTipoIdentificador(1); //por placa y uso
				}
			} else if(idTipo.equalsIgnoreCase("2")) {//facturas
				bienEspecialTO.setIdentificador(mdIdentificador2);
				bienEspecialTO.setTipoIdentificador(3); // no factura
                                
                                bienEspecialTO.setSerie(mdIdentificador3); //corellana se tiene que poner el numero de serie
			} else {
				bienEspecialTO.setIdentificador(mdIdentificador2);
				bienEspecialTO.setTipoIdentificador(4); // no serie
			}
//			bienEspecialTO.setSerie(mdIdentificador3);
//                        System.out.println("Identificador: " + mdIdentificador3);
			inscripcionService.registrarBien(bienEspecialTO);
			
			dwr = getParteBienes(elementId, idTramite);
		} catch(Exception e) {
			e.printStackTrace();
		}
			
		return dwr;
	}
        
        
	public MessageDwr cambiaUsuarioGrupo(String idUsuario, String idSubUsuario,
			String idAcreedor, String idGrupoNuevo) {
		MessageDwr dwr = new MessageDwr();
		ConexionBD bd = new ConexionBD();
		MyLogger.Logger.log(Level.INFO, idUsuario);
		MyLogger.Logger.log(Level.INFO,
				"Entro en cambiar los datos del grupo del usuario");
		Connection connection = bd.getConnection();
		try {
			GrupoDaoJdbcImpl gdao = new GrupoDaoJdbcImpl();
			if (gdao.updateUsuarioGrupo(Integer.valueOf(idUsuario),
					idSubUsuario, Integer.valueOf(idAcreedor),
					Integer.valueOf(idGrupoNuevo))) {
				dwr.setCodeError(0);
				dwr.setMessage(" <script> alert('El cambio de grupo fue satisfactorio '); recargaControlGrupo(); </script>");
			} else {
				dwr.setCodeError(1);
				dwr.setMessage("<script> alert('no se pudo realizar el cambio de grupo'); </script>");

			}

		} catch (Exception e) {
			dwr.setMessage(" <script> alert('Sucedio un error en el sistema :"
					+ e.getMessage() + "'); </script>");
			e.printStackTrace();
		} finally {
			bd.close(connection, null, null);
		}

		return dwr;
	}

	public MessageDwr muestraDesUsuario(String idUsuario) {
		MessageDwr dwr = new MessageDwr();
		ConexionBD bd = new ConexionBD();
		MyLogger.Logger.log(Level.INFO, idUsuario);
		MyLogger.Logger.log(Level.INFO, "Entro en muestra des usuario");
		Connection connection = bd.getConnection();
		try {
			ControlUsuarioDaoJdbcImpl cn = new ControlUsuarioDaoJdbcImpl();
			mx.gob.se.rug.acreedores.to.UsuarioTO usuarioTO = cn
					.getBySubUsuario(connection, idUsuario);
			dwr.setCodeError(0);
			dwr.setMessage(writeDesUsuario(usuarioTO).toString());
		} catch (Exception e) {
			dwr.setCodeError(0);
			dwr.setMessage(" <script> alert('Sucedio un error en el sistema :"
					+ e.getMessage() + "'); </script>");
			e.printStackTrace();
		} finally {
			bd.close(connection, null, null);
		}

		return dwr;
	}

	public MessageDwr muestraModificarUsuarioGrupo(String idUsuario,
			String idSubUsuario, String idAcreedor) {
		MessageDwr dwr = new MessageDwr();

		MyLogger.Logger.log(Level.INFO, idUsuario + "-" + idSubUsuario + "-"
				+ idAcreedor);
		MyLogger.Logger.log(Level.INFO, "Entro en muestra modifica usuario");
		try {
			dwr.setCodeError(0);
			dwr.setMessage(writeModificaUsuarioGrupo(
					Integer.valueOf(idUsuario), Integer.valueOf(idSubUsuario),
					Integer.valueOf(idAcreedor)).toString());
		} catch (Exception e) {
			dwr.setCodeError(0);
			dwr.setMessage(" <script> alert('Sucedio un error en el sistema :"
					+ e.getMessage() + "'); </script>");
			e.printStackTrace();
		}

		return dwr;
	}

	private StringBuffer writeModificaUsuarioGrupo(Integer idUsuario,
			Integer idSubUsuario, Integer idAcreedor) {

		StringBuffer sb = new StringBuffer();

		GrupoDaoJdbcImpl gdao = new GrupoDaoJdbcImpl();
		AcreedoresCatalogosDaoJdbcImpl acs = new AcreedoresCatalogosDaoJdbcImpl();
		GrupoPerfilTO grupo = gdao.getGrupo(idAcreedor, idSubUsuario);
		sb.append(" <table> ");
		sb.append(" <tr> ");
		sb.append(" <td> Grupo Actual del usuario: </td> ");
		sb.append(" <td> " + grupo.getDescripcion() + " </td> ");
		sb.append(" </tr> ");
		sb.append(" <tr> ");
		sb.append(" <td> Grupo a donde va a ir </td> ");
		sb.append(" <td>");
		sb.append(" <select id='grupoNuevo' name='grupoNuevo'>");
		if (!(grupo.getDescripcion().equals("ADMINISTRADOR"))) {
			sb.append(" <option value=\"3\"> ADMINISTRADOR </option> ");
		}
		for (GrupoPerfilTO gp : acs.getGrupos(idAcreedor)) {
			if (!(grupo.getId().equals(gp.getId()))) {
				sb.append("<option value=" + gp.getId() + ">"
						+ gp.getDescripcion() + "</option>");
			}
		}
		sb.append(" </select> ");
		sb.append(" </td> ");
		sb.append(" </tr> ");
		sb.append(" <tr> ");
		sb.append(" <td colspan=\"2\"> <input type=\"button\" value=\"Cambiar\" onclick=\"cambiarUsuarioGrupo('"
				+ idUsuario
				+ "','"
				+ idSubUsuario
				+ "','"
				+ idAcreedor
				+ "')\" /> </td> ");
		sb.append(" </tr> ");
		sb.append(" </table> <script> </script>");
		return sb;
	}

	private StringBuffer writeDesUsuario(
			mx.gob.se.rug.acreedores.to.UsuarioTO usuarioTO) {
		StringBuffer sb = new StringBuffer();
		sb.append(" <table> ");
		sb.append(" <tr> ");
		sb.append(" <td> <b>Datos del Usuario:</b> </td> ");
		sb.append(" </tr> ");
		sb.append(" <tr> ");
		sb.append(" <td> Nombre: " + usuarioTO.getNombre() + " </td> ");
		sb.append(" </tr> ");
		sb.append(" <tr> ");
		sb.append(" <td> Apellido Paterno: " + usuarioTO.getApaterno()
				+ " </td> ");
		sb.append(" </tr> ");
		sb.append(" <tr> ");
		sb.append(" <td> Apellido Materno: " + usuarioTO.getAmaterno()
				+ " </td> ");
		sb.append(" </tr> ");
		sb.append(" <tr> ");
		sb.append(" <td> RFC: " + usuarioTO.getRfc() + " </td> ");
		sb.append(" </tr> ");
		sb.append(" <tr> <td> <input class=\"boton_rug\" type=\"button\" value=\"Ocultar\" onclick=\"ocultarDesUsuario()\" /> </td> </tr> ");
		sb.append(" </table> <script> </script>");

		return sb;
	}

	private StringBuffer writeMensajeAlta(
			mx.gob.se.rug.acreedores.to.UsuarioTO usuarioTO) {
		StringBuffer sb = new StringBuffer();
		sb.append(" <table> ");
		sb.append(" <tr> ");
		sb.append(" <td> <b>ADVERTENCIA: En la base de datos del RUG no existe un Otorgante con los atributos que ha ingresado. Aseg煤rese de la veracidad y exactitud de dicha informaci贸n, ya que el sistema matricular谩 al Otorgante con base en la informaci贸n ingresada por usted.</b> </td> ");
		sb.append(" </tr> ");
		sb.append(" <tr> ");
		sb.append(" <td> Nombre: " + usuarioTO.getNombre() + " </td> ");
		sb.append(" </tr> ");
		sb.append(" <tr> ");
		sb.append(" <td> Apellido Paterno: " + usuarioTO.getApaterno()
				+ " </td> ");
		sb.append(" </tr> ");
		sb.append(" <tr> ");
		sb.append(" <td> Apellido Materno: " + usuarioTO.getAmaterno()
				+ " </td> ");
		sb.append(" </tr> ");
		sb.append(" <tr> ");
		sb.append(" <td> RFC: " + usuarioTO.getRfc() + " </td> ");
		sb.append(" </tr> ");
		sb.append(" <tr> <td> <input class=\"boton_rug\" type=\"button\" value=\"Ocultar\" onclick=\"ocultarDesUsuario()\" /> </td> </tr> ");
		sb.append(" </table> <script> </script>");

		return sb;
	}

	public MessageDwr getBuscaCorreoUsuario(String correoElectronico) {
		MessageDwr dwr = new MessageDwr();
		dwr.setCodeError(0);
		MyLogger.Logger.log(Level.INFO,
				"Entro a parte dwr getBuscaCorreoUsuario");
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		try {
			ControlUsuarioDaoJdbcImpl cn = new ControlUsuarioDaoJdbcImpl();
			mx.gob.se.rug.acreedores.to.UsuarioTO usuarioTO = cn
					.getByCorreoElectronico(connection,
							correoElectronico.trim());
			if (usuarioTO != null) {
				if (usuarioTO.getPerfil() != null
						&& usuarioTO.getPerfil().trim()
								.equalsIgnoreCase("AUTORIDAD")) {
					dwr.setMessage(" <input name=\"usuarioAutoridadId\" id=\"usuarioAutoridadId\" type=\"hidden\" value=\""
							+ usuarioTO.getNombreCompleto()
							+ "\" /> <script>  alert('El usuario "
							+ usuarioTO.getNombreCompleto()
							+ "  est谩 habilitado como autoridad en el sistema por lo cual cuenta con todos los privilegios y no puede ser asignado a ning煤n grupo '); </script> ");
				} else {
					MyLogger.Logger.log(Level.INFO, "Encontro el correo");
					dwr.setMessage(" <script> "
							+ " habilitarNvoUsuario(); "
							+ " document.getElementById('passDos').style.visibility = 'hidden';"
							+ " document.getElementById('passDos').style.display = 'none';"
							+ " document.getElementById('passDos2').style.visibility = 'hidden';"
							+ " document.getElementById('passDos2').style.display = 'none';"
							+ " document.getElementById('passDos3').style.visibility = 'hidden';"
							+ " document.getElementById('passDos3').style.display = 'none';"
							+ " document.getElementById('passDos4').style.visibility = 'hidden';"
							+ " document.getElementById('passDos4').style.display = 'none';"
							+ " document.getElementById('correoElectronico').value = '"
							+ correoElectronico.trim()
							+ "'; "
							+ " document.getElementById('usuarioTO.nombre').value = '"
							+ usuarioTO.getNombre()
							+ "'; "
							+ " document.getElementById('usuarioTO.apaterno').value = '"
							+ usuarioTO.getApaterno()
							+ "'; "
							+ " document.getElementById('usuarioTO.amaterno').value = '"
							+ usuarioTO.getAmaterno()
							+ "'; "
							+ " document.getElementById('usuarioTO.rfc').value = '"
							+ usuarioTO.getRfc()
							+ "'; "
							+
							// " document.getElementById('usuarioTO.perfil').value = '"+usuarioTO.getPerfil()+"';  "
							// +
							// " document.getElementById('usuarioTO.grupo').value = '"+usuarioTO.getIdGrupo()+"';  "
							// +
							" document.getElementById('passwd1').disabled = true; "
							+ " document.getElementById('passwd2').disabled = true; "
							+
							// " document.getElementById('usuarioTO.idGrupo').value = '"+usuarioTO.getPerfil()+"';  "
							// +
							" document.getElementById('comPasswd').value = 'Y'; "
							+ " desabilitarNvoUsuario(); "
							+ "  </script> <input type='hidden' name='comPasswd' value='Y' id='comPasswd' /> ");
				}

			} else {
				MyLogger.Logger.log(Level.WARNING, "No encontro el correo");
				dwr.setMessage(" <script> "
						+ " habilitarNvoUsuario();"
						+ " document.getElementById('usuarioTO.nombre').value = ''; "
						+ " document.getElementById('usuarioTO.apaterno').value = ''; "
						+ " document.getElementById('usuarioTO.amaterno').value = ''; "
						+ " document.getElementById('usuarioTO.rfc').value = ''; "
						+
						// " document.getElementById('usuarioTO.perfil').value = '';  "
						// +
						" document.getElementById('usuarioTO.grupo').value = '';  "
						+ " document.getElementById('passwd1').disabled = false; "
						+ " document.getElementById('passwd2').disabled = false; "
						+ " document.getElementById('passwd1').value = '';  "
						+ " document.getElementById('passwd2').value = '';  "
						+ " document.getElementById('passDos').style.visibility = 'visible';"
						+ " document.getElementById('passDos').style.display = 'block';"
						+ " document.getElementById('passDos2').style.visibility = 'visible';"
						+ " document.getElementById('passDos2').style.display = 'block';"
						+ " document.getElementById('passDos3').style.visibility = 'visible';"
						+ " document.getElementById('passDos3').style.display = 'block';"
						+ " document.getElementById('passDos4').style.visibility = 'visible';"
						+ " document.getElementById('passDos4').style.display = 'block';"
						+ "  </script> ");
			}

		} catch (Exception e) {
			e.printStackTrace();
			dwr.setMessage(" <script> alert('Sucedio un error en el sistema :"
					+ e.getMessage() + "'); </script>");
		} finally {
			bd.close(connection, null, null);
		}
		return dwr;
	}

	public MessageDwr copiaParteOtorgante(String elementId,
										  String idTramite,
										  String idPersona,
										  String idPersonaModificar,
										  String isInscripcion,
										  String folioElectronico,
										  String rfc,
										  String isMultiple) {

		MessageDwr dwr;
		try {
			AltaParteDAO altaParteDAO = new AltaParteDAO();
			OtorganteTO otorganteTO = altaParteDAO.getOtorganteFisicoByFolioElectronico(folioElectronico);
			if (otorganteTO != null) {
				boolean agrego = altaParteDAO.relParte(otorganteTO.getIdOtorgante(), new Integer(idTramite), 1, rfc);
				if (agrego) {
					MyLogger.Logger.log(Level.INFO, "Se agrego");
				} else {
					MyLogger.Logger.log(Level.INFO, "NO SE AGREGO");
				}
			} else {
				MyLogger.Logger.log(Level.INFO, "No existe este otorgante");
				MyLogger.Logger.log(Level.INFO, folioElectronico);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		dwr = getParteOtorgante(elementId, idTramite, idPersona, "0", isInscripcion, isMultiple);
		return dwr;
	}

	/*********************/
	public MessageDwr copiaParteOtorganteMoral(String elementId,
											   String idTramite,
											   String idPersona,
											   String idPersonaModificar,
											   String isInscripcion,
											   String folioElectronico,
											   String rfc,
											   String isMultiple) {
		MessageDwr dwr;
		try {
			AltaParteDAO altaParteDAO = new AltaParteDAO();
			OtorganteTO otorganteTO = altaParteDAO.getOtorganteMoralByFolioElectronico(folioElectronico);
			if (otorganteTO != null) {
				boolean agrego = altaParteDAO.relParte(otorganteTO.getIdOtorgante(), new Integer(idTramite), 1, rfc);
				if (agrego) {
					MyLogger.Logger.log(Level.INFO, "Se agrego");
				} else {
					MyLogger.Logger.log(Level.INFO, "NO SE AGREGO");
				}
			} else {
				MyLogger.Logger.log(Level.INFO, "No existe este otorgante");
				MyLogger.Logger.log(Level.INFO, folioElectronico);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		dwr = getParteOtorgante(elementId, idTramite, idPersona, "0", isInscripcion, isMultiple);
		return dwr;
	}

	/*********************/
	public MessageDwr buscaFolioElectronicoFisica(String folioElectronico,
			String idTramite, String idPersona, String isInscripcion,
			String isMultiple) {
		MessageDwr dwr = new MessageDwr();
		try {
			dwr.setCodeError(0);
			dwr.setMessage(writeBusquedaFolioelectronico(folioElectronico,
					idTramite, idPersona, isInscripcion, isMultiple).toString());
		} catch (Exception e) {
			dwr.setCodeError(1);
			dwr.setMessage("Sucedio un error en la busqueda");
			e.printStackTrace();
		}

		return dwr;
	}

	/**********************************/
	public MessageDwr buscaFolioElectronicoMoral(String folioElectronico,
			String idTramite, String idPersona, String isInscripcion,
			String isMultiple) {

		MessageDwr dwr = new MessageDwr();
		try {
			dwr.setCodeError(0);
			dwr.setMessage(writeBusquedaFolioelectronicoMoral(folioElectronico, idTramite, idPersona, isInscripcion, isMultiple).toString());
		} catch (Exception e) {
			dwr.setCodeError(1);
			dwr.setMessage("Sucedio un error en la busqueda");
			e.printStackTrace();
		}

		return dwr;
	}
	
	/*** bienes **/
	public MessageDwr getParteBienes(String elementId, String idTramite) {
            System.out.println("getParteBienes");
		MessageDwr dwr = new MessageDwr();
		MyLogger.Logger.log(Level.INFO, "getParteBienes idtramite" + idTramite);
		try {
			InscripcionDAO inscripcionDAO = new InscripcionDAO();
			List<BienEspecialTO> listaBienes = inscripcionDAO.getListaBienes(new Integer(idTramite), 1);
			MyLogger.Logger.log(Level.INFO, "Lista bienes" + listaBienes.size());
			if (listaBienes.size() > 0) {
                            
                            if (listaBienes.get(0).tipoBien != 2)
								dwr.setMessage(writeTablaBienes(elementId, idTramite, listaBienes).toString());
                            else
                                dwr.setMessage(writeTablaBienes_factoraje(elementId, idTramite, listaBienes).toString());
			}
			else {
				dwr.setMessage("<table></table>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		dwr.setMessage(dwr.getMessage()
				+ " <script type=\"text/javascript\">  prepareInputsForHints(); </script> ");		
		
		return dwr;
	}

	/**********************************/
	public MessageDwr getParteDeudor(String elementId, String idTramite,
			String idPersona, String idPersonaModificar, String isInscripcion) {
		MessageDwr dwr = new MessageDwr();
		MyLogger.Logger.log(Level.INFO, "isInscripcion Deudor" + isInscripcion);
		MyLogger.Logger.log(Level.INFO, "--idPersonaModificar"
				+ idPersonaModificar);
		MyLogger.Logger.log(Level.INFO, "----------- idPerson" + idPersona);
		try {
			AltaParteDAO altaParteDAO = new AltaParteDAO();
			MyLogger.Logger.log(Level.INFO, "ID TRAMITE" + idTramite);
			List<DeudorTO> listaDeudores = altaParteDAO.getListaDeudores(Integer.valueOf(idTramite));
			dwr.setCodeError(0);
			AltaParteTO altaParte;
			if (Integer.valueOf(idPersonaModificar) == 0) {
				altaParte = new AltaParteTO();
				altaParte.setIdNacionalidad(1);
				altaParte.setIdPersona(0);
				
				altaParte.setRazonSocial("");
				altaParte.setInscrita("");
				altaParte.setFolioInscrito("");
				altaParte.setLibroInscrito("");
				altaParte.setUbicacionInscrito("");
				
				altaParte.setNombre("");
				altaParte.setApellidoMaterno("");
				altaParte.setApellidoPaterno("");
				altaParte.setRfc(""); //nit
				altaParte.setExtencion("");
				altaParte.setEdad("");
				altaParte.setEstadoCivil("");
				altaParte.setProfesion("");
				
				altaParte.setTelefono("");
				altaParte.setCorreoElectronico("");
				
				altaParte.setIdPaisResidencia(1);
				altaParte.setDomicilioUno("");
				
				altaParte.setFolioMercantil("");
				altaParte.setCurp(""); //dpi
				altaParte.setNif("");
			} else {
				DeudorTO deudorTO = altaParteDAO.getDeudorID(
						Integer.valueOf(idTramite),
						Integer.valueOf(idPersonaModificar));

				altaParte = new AltaParteTO();
				
				altaParte.setIdNacionalidad(deudorTO.getIdNacionalidad());
				altaParte.setIdPersona(0);
                                
                                //orellana:nuevo para Hector que se pueda cambiar el tipo de persona
                                //altaParte.setTipoPersona(deudorTO.getTipoPersona());
				
				altaParte.setRazonSocial(notNull(deudorTO.getRazon()));
				altaParte.setInscrita(notNull(deudorTO.getInscrita()));
				altaParte.setFolioInscrito(notNull(deudorTO.getFolioInscrito()));
				altaParte.setLibroInscrito(notNull(deudorTO.getLibroInscrito()));
				altaParte.setUbicacionInscrito(notNull(deudorTO.getUbicacionInscrito()));
				
				altaParte.setNombre(notNull(deudorTO.getNombre()));
				altaParte.setApellidoMaterno(deudorTO.getApellidoMaterno());
				altaParte.setApellidoPaterno(deudorTO.getApellidoPaterno());
				altaParte.setTipoPersona(notNull(deudorTO.getTipoPersona()));
				altaParte.setRfc(notNull(deudorTO.getRfc()));
				altaParte.setExtencion(notNull(deudorTO.getExtendido()));
				altaParte.setEdad(notNull(deudorTO.getEdad()));
				altaParte.setEstadoCivil(notNull(deudorTO.getEstadoCivil()));
				altaParte.setProfesion(notNull(deudorTO.getProfesion()));
				
				altaParte.setTelefono(notNull(deudorTO.getTelefono()));
				altaParte.setCorreoElectronico(notNull(deudorTO.getCorreo()));
				
				altaParte.setIdPaisResidencia(deudorTO.getIdResidencia());
				altaParte.setDomicilioUno(notNull(deudorTO.getUbicacion()));
				
				altaParte.setFolioMercantil("");
				altaParte.setCurp(deudorTO.getCurp());	
				altaParte.setNif(deudorTO.getRfc());
			}
			dwr.setMessage(writeParteDeudor(elementId, idTramite, idPersona,
					idPersonaModificar, listaDeudores, altaParte, isInscripcion)
					.toString());
			MyLogger.Logger.log(Level.INFO, "Numero de deudores"
					+ listaDeudores.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		dwr.setMessage(dwr.getMessage()
				+ " <script type=\"text/javascript\">  prepareInputsForHints(); </script> ");
		/*MyLogger.Logger.log(Level.INFO, "Formulario deudor garante***********************************"
				+ dwr.getMessage());*/
		
		return dwr;
	}

	//Para Acreedores
	public MessageDwr buscaPersonaByFolioElectronico(String folioElectronico, String tipoPersona){
            System.out.println("folioElectronico: "+folioElectronico );
            System.out.println("tipoPersona: "+tipoPersona );
		MessageDwr messageDwr = new MessageDwr();
		OtorganteTO otorganteTO = null;
		AltaParteDAO apdao = new AltaParteDAO();
		StringBuffer sb = new StringBuffer();
		
		if(tipoPersona.equalsIgnoreCase("PF")){
			messageDwr.setCodeError(0);
			otorganteTO = apdao.getOtorganteFisicoByFolioElectronico(folioElectronico);
			if(otorganteTO!=null){
			sb.append("{");
			sb.append("	\"perJuridica\": \""+otorganteTO.getTipoPersona()+"\",");
			sb.append("    \"personaFisica\": {");
			sb.append("     \"nombre\": \""+otorganteTO.getNombreCompleto()+"\",");
			sb.append("		\"apaterno\": \""+otorganteTO.getApellidoPaterno()+"\",");
			sb.append("		\"amaterno\": \""+otorganteTO.getApellidoMaterno()+"\",");
			sb.append("		\"folioMercantil\": \""+otorganteTO.getFolioMercantil()+"\",");
			sb.append("		\"curp\": \""+otorganteTO.getCurp()+"\",");
			sb.append("		\"nacionalidad\": \""+otorganteTO.getDescNacionalidad()+"\",");
			sb.append("		\"idNacionalidad\": \""+otorganteTO.getIdNacionalidad()+"\",");
			sb.append("		\"rfc\": \""+otorganteTO.getRfc()+"\",");
			sb.append("		\"extension\": \""+otorganteTO.getExtendido()+"\",");
			sb.append("		\"edad\": \""+otorganteTO.getEdad()+"\",");
			sb.append("		\"estadoCivil\": \""+otorganteTO.getEstadoCivil()+"\",");
			sb.append("		\"profesion\": \""+otorganteTO.getProfesion()+"\",");
			sb.append("		\"telefono\": \""+otorganteTO.getTelefono()+"\",");
			sb.append("		\"correo\": \""+otorganteTO.getCorreo()+"\",");
			sb.append("		\"idResidencia\": \""+otorganteTO.getIdResidencia()+"\",");
			sb.append("		\"domicilio\": \""+otorganteTO.getUbicacion()+"\"");
			
			sb.append("	}	");
			sb.append("}");
			
			MyLogger.Logger.log(Level.INFO, "jason..." + sb.toString());
			messageDwr.setMessage(sb.toString());
			}else{
				messageDwr.setCodeError(1);//no encontro
			}
		}else if(tipoPersona.equalsIgnoreCase("PM")){
			System.out.println("El folio elctronico PM : "+folioElectronico);
			messageDwr.setCodeError(0);//Error, no encontro persona con el folio mencionado
			otorganteTO = apdao.getOtorganteMoralByFolioElectronico(folioElectronico);			
			
			if(otorganteTO!=null){
			sb.append("{");
			sb.append("	\"perJuridica\": \""+otorganteTO.getTipoPersona()+"\",");
			sb.append("	\"personaMoral\": {");
			sb.append("     \"razonSocial\": \""+otorganteTO.getRazon()+"\",");
			sb.append("		\"curp\": \""+otorganteTO.getCurp()+"\",");
			sb.append("		\"rfc\": \""+otorganteTO.getRfc()+"\",");		
			sb.append("		\"nacionalidad\": \""+otorganteTO.getDescNacionalidad()+"\",");
			sb.append("		\"idNacionalidad\": \""+otorganteTO.getIdNacionalidad()+"\",");
			sb.append("		\"tipoSociedad\": \""+otorganteTO.getTipoSociedad()+"\",");
			sb.append("		\"representante\": \""+otorganteTO.getExtendido()+"\",");
			sb.append("		\"correo\": \""+otorganteTO.getCorreo()+"\",");
			sb.append("		\"domicilio\": \""+otorganteTO.getUbicacion()+"\"");
			sb.append("	}");
			sb.append("}");
			MyLogger.Logger.log(Level.INFO, "jason..." + sb.toString());
			messageDwr.setMessage(sb.toString());
			}else{
				messageDwr.setCodeError(1);//no encontro
			}
			
		}else{
			messageDwr.setCodeError(1);//Error, no encontro persona con el folio mencionado
			messageDwr.setMessage("Error, no encontro persona con el folio mencionado");
		}
		
		return messageDwr;
	}

	public MessageDwr getParteOtorgante(String elementId, String idTramite,
			String idPersona, String idPersonaModificar, String isInscripcion,
			String isMultiple) {
		MessageDwr dwr = new MessageDwr();
		MyLogger.Logger.log(Level.INFO, "Valor de isMultiple >>" + isMultiple);
		AltaParteDAO altaParteDAO = new AltaParteDAO();
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("<div class=\"row\"><div class=\"col s12\"><div class=\"card\"><div class=\"card-content\">");
			List<OtorganteTO> listaOtorganteTO = altaParteDAO
					.getOtorganteByInscripcion(Integer.valueOf(idTramite));
			AltaParteTO altaParte;
			if (Integer.valueOf(idPersonaModificar) == 0) {
				if (listaOtorganteTO.size() > 0) {
					sb.append(writeTabla(listaOtorganteTO, elementId, idTramite,
							isInscripcion, idPersona, isMultiple));
				}
				altaParte = new AltaParteTO();
				altaParte.setIdNacionalidad(1);				
				altaParte.setIdPersona(0);
				
				altaParte.setRazonSocial("");
				altaParte.setInscrita("");
				altaParte.setFolioInscrito("");
				altaParte.setLibroInscrito("");
				altaParte.setUbicacionInscrito("");
				
				altaParte.setNombre("");
				altaParte.setApellidoMaterno("");
				altaParte.setApellidoPaterno("");
				altaParte.setRfc(""); //nit
				altaParte.setExtencion("");
				altaParte.setEdad("");
				altaParte.setEstadoCivil("");
				altaParte.setProfesion("");
				
				altaParte.setTelefono("");
				altaParte.setCorreoElectronico("");
				
				altaParte.setIdPaisResidencia(1);
				altaParte.setDomicilioUno("");
				
				altaParte.setFolioMercantil("");
				altaParte.setCurp(""); //dpi
				altaParte.setNif("");
				
				sb.append(writeFormulario(elementId, idTramite,
						idPersona, altaParte, isInscripcion, isMultiple));
			} else {
				if (listaOtorganteTO.size() > 0) {
					sb.append(writeTabla(listaOtorganteTO, elementId, idTramite,
							isInscripcion, idPersona, isMultiple));
				}
				OtorganteTO otorganteTO = altaParteDAO.getOtorganteById(
						Integer.valueOf(idTramite),
						Integer.valueOf(idPersonaModificar));
				altaParte = new AltaParteTO();
				altaParte.setNombre(otorganteTO.getNombre());
				altaParte.setApellidoMaterno(otorganteTO.getApellidoMaterno());
				altaParte.setApellidoPaterno(otorganteTO.getApellidoPaterno());
				altaParte.setRazonSocial(otorganteTO.getRazon());
				altaParte.setFolioMercantil(otorganteTO.getFolioMercantil());
				altaParte.setCurp(otorganteTO.getCurp());				
				altaParte.setTelefono(otorganteTO.getTelefono());
				altaParte.setRfc(otorganteTO.getRfc());
				altaParte.setExtencion(otorganteTO.getExtendido());
				altaParte.setCorreoElectronico(otorganteTO
						.getCorreo());
				altaParte.setTipoPersona(otorganteTO.getTipoPersona());
				altaParte.setIdNacionalidad(otorganteTO.getIdNacionalidad());				
				altaParte.setIdPersona(Integer.valueOf(otorganteTO
						.getIdPersona()));
				altaParte.setIdUsuario(Integer.valueOf(Integer
						.valueOf(idPersona)));								
				altaParte.setIdPaisResidencia(otorganteTO.getIdResidencia());
				
				altaParte.setEdad(otorganteTO.getEdad());
				altaParte.setEstadoCivil(otorganteTO.getEstadoCivil());
				altaParte.setProfesion(otorganteTO.getProfesion());
				altaParte.setInscrita(otorganteTO.getInscrita());
				altaParte.setFolioInscrito(otorganteTO.getFolioInscrito());
				altaParte.setLibroInscrito(otorganteTO.getLibroInscrito());
				altaParte.setUbicacionInscrito(otorganteTO.getUbicacionInscrito());
				altaParte.setDomicilioUno(otorganteTO.getUbicacion());
				
				sb.append(writeFormulario(elementId, idTramite,
						idPersona, altaParte, isInscripcion, isMultiple));
			}
			sb.append("</div></div></div>");
			
			dwr.setMessage(sb.toString());
			dwr.setCodeError(0);
		} catch (Exception e) {
			dwr.setCodeError(1);
			dwr.setMessage("Error el cargar el otorgante");
			e.printStackTrace();
		}
		dwr.setMessage(dwr.getMessage()
				+ " <script type=\"text/javascript\">  prepareInputsForHints(); </script> ");
		return dwr;
	}

	// Parte Acreedor Representado
	public MessageDwr getParteAcreedorRepresentado(String elementId, String idPersona, String idPersonaModificar, String cadena) {
		MessageDwr dwr = new MessageDwr();
		boolean uboFirmados = false;
		try {
			MyLogger.Logger.log(Level.INFO,	"El id de la persona que va en session" + idPersona);
			AltaParteDAO altaParteDAO = new AltaParteDAO();
			boolean esAutoridad = false;

			PrivilegiosDAO privilegiosDAO = new PrivilegiosDAO();
			PrivilegiosTO privilegiosTO = new PrivilegiosTO();
			privilegiosTO.setIdRecurso(new Integer(3));
			privilegiosTO = privilegiosDAO.getPrivilegios(privilegiosTO, new Integer(idPersona));
			Map<Integer, PrivilegioTO> priv = privilegiosTO.getMapPrivilegio();
			if (priv.get(new Integer(36)) != null) {
				esAutoridad = true;
			}

			StringBuffer sb = new StringBuffer();
			AltaParteTO altaParte;
			List<AcreedorTO> listaAcreedores = acreedoresService .getAcreedoresByPersona(Integer.valueOf(idPersona));
			if (listaAcreedores.size() > 0) {
				uboFirmados = true;
			}
			List<AcreedorTO> listaAcreedoresSinFirma = acreedoresService.getAcreedoresSinFirmaByPersona(Integer.valueOf(idPersona));

			List<TramitesMasivosTO> listaTramitesMasivosSinFirmar = acreedoresService.getTramitesCargaMasivaSinFirmar(Integer.valueOf(idPersona));
			if (Integer.valueOf(idPersonaModificar) == 0) {
				if (listaAcreedores.size() > 0) {

					sb.append(writeTablaAcreedorRepresentado(elementId,	idPersona, listaAcreedores));
				}
				if (listaAcreedoresSinFirma.size() > 0) {
					sb.append(writeTablaAcreedorRepresentadoSinFirma(elementId, idPersona, listaAcreedoresSinFirma));
				}
				if (listaTramitesMasivosSinFirmar.size() > 0) {
					sb.append(writeTablaTramitesMasivosSinFirma(elementId,idPersona, listaTramitesMasivosSinFirmar));
				}
				altaParte = new AltaParteTO();
				altaParte.setNombre("");
				altaParte.setApellidoMaterno("");
				altaParte.setApellidoPaterno("");
				altaParte.setRazonSocial("");
				altaParte.setFolioMercantil("");
				altaParte.setCurp("");
				altaParte.setIdNacionalidad(1);
				altaParte.setIdPaisResidencia(1);
				altaParte.setRfc("");
				altaParte.setIdPersona(0);

				sb.append(writeFormularioAcreedorRepresentado(elementId,idPersona, altaParte, uboFirmados, esAutoridad));
			} else {
				if (listaAcreedores.size() > 0) {
					sb.append(writeTablaAcreedorRepresentado(elementId,
							idPersona, listaAcreedores));
					uboFirmados = true;
				}
				if (listaAcreedoresSinFirma.size() > 0) {
					sb.append(writeTablaAcreedorRepresentadoSinFirma(elementId,
							idPersona, listaAcreedoresSinFirma));
				}
				if (listaTramitesMasivosSinFirmar.size() > 0) {
					sb.append(writeTablaTramitesMasivosSinFirma(elementId,
							idPersona, listaTramitesMasivosSinFirmar));
				}
				AcreedorTO acreedorTO = acreedoresService.getARByID(Integer.valueOf(idPersonaModificar));
				altaParte = new AltaParteTO();
				altaParte.setNombre(acreedorTO.getNombre());
				altaParte.setApellidoMaterno(acreedorTO.getApellidoMaterno());
				altaParte.setApellidoPaterno(acreedorTO.getApellidoPaterno());
				altaParte.setRazonSocial(acreedorTO.getRazonSocial());
				altaParte.setFolioMercantil(acreedorTO.getFolioMercantil());
				altaParte.setCurp(acreedorTO.getCurp());
				altaParte.setTelefono(acreedorTO.getTelefono());
				altaParte.setRfc(acreedorTO.getRfc());
				altaParte.setExtencion(acreedorTO.getExtencion());
				altaParte.setIdColonia(acreedorTO.getIdColonia());
				altaParte.setIdLocalidad(acreedorTO.getIdLocalidad());
				altaParte.setIdPaisResidencia(acreedorTO.getIdPaisResidencia());
				altaParte.setCorreoElectronico(acreedorTO.getCorreoElectronico());
				altaParte.setCalle(acreedorTO.getCalle());
				altaParte.setTipoPersona(acreedorTO.getTipoPersona());
				altaParte.setNumeroExterior(acreedorTO.getCalleNumero());
				altaParte.setNumeroInterior(acreedorTO.getCalleNumeroInterior());
				altaParte.setIdNacionalidad(acreedorTO.getIdNacionalidad());
				altaParte.setRfc(acreedorTO.getRfc());
				altaParte.setIdPersona(Integer.valueOf(acreedorTO.getIdPersona()));
				altaParte.setIdUsuario(Integer.valueOf(Integer.valueOf(idPersona)));
				altaParte.setCorreoElectronico(acreedorTO.getCorreoElectronico());
				altaParte.setCodigoPostal(acreedorTO.getCodigoPostal());
				altaParte.setDomicilioUno(acreedorTO.getDomicilioUno());
				altaParte.setDomicilioDos(acreedorTO.getDomicilioDos());
				altaParte.setZonaPostal(acreedorTO.getZonaPostal());
				altaParte.setPoblacion(acreedorTO.getPoblacion());
				sb.append(writeFormularioAcreedorRepresentado(elementId,idPersona, altaParte, uboFirmados, esAutoridad));
			}

			dwr.setMessage(sb.toString());
			dwr.setCodeError(0);
		} catch (Exception e) {
			dwr.setCodeError(1);
			dwr.setMessage("Error el cargar el otorgante");
			e.printStackTrace();
		}
		dwr.setMessage(dwr.getMessage()
				+ cadena
				+ " <script type=\"text/javascript\">  prepareInputsForHints(); </script> ");
		return dwr;
	}

	// Parte Acreedor
	public MessageDwr getParteAcreedor(String elementId, String idTramite,
			String idPersona, String idPersonaModificar, String isInscripcion) {
		MessageDwr dwr = new MessageDwr();
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("<div class=\"row\"><div class=\"col s12\"><div class=\"card\"><div class=\"card-content\">");
			AltaParteDAO altaParteDAO = new AltaParteDAO();
			AltaParteTO altaParte;
			boolean esAutoridad = altaParteDAO.esAutoridad(Integer
					.valueOf(idPersona));
			MyLogger.Logger.log(Level.INFO, "idTramite : " + idTramite);
			List<AcreedorTO> listaAcreedores = altaParteDAO
					.getListaAcreedores(Integer.valueOf(idTramite));
			MyLogger.Logger.log(Level.INFO, "listaAcreedores : " + listaAcreedores.size());
			if (Integer.valueOf(idPersonaModificar) == 0) {
				if (listaAcreedores.size() > 0) {
					sb.append(writeTablaAcreedor(elementId, idTramite,
							listaAcreedores, idPersona, isInscripcion));
				}
				altaParte = new AltaParteTO();
				
				altaParte.setIdNacionalidad(1);
				altaParte.setIdPersona(0);
				
				altaParte.setRazonSocial("");
				altaParte.setInscrita("");
				altaParte.setFolioInscrito("");
				altaParte.setLibroInscrito("");
				altaParte.setUbicacionInscrito("");
				
				altaParte.setNombre("");
				altaParte.setApellidoMaterno("");
				altaParte.setApellidoPaterno("");
				altaParte.setRfc("");
				altaParte.setExtencion("");
				altaParte.setEdad("");
				altaParte.setEstadoCivil("");
				altaParte.setProfesion("");
				
				altaParte.setTelefono("");
				altaParte.setCorreoElectronico("");
				
				altaParte.setIdPaisResidencia(1);
				altaParte.setDomicilioUno("");
				
				altaParte.setFolioMercantil("");
				altaParte.setCurp("");
				altaParte.setNif("");
				
				sb.append(writeFormularioAcreedor(elementId, idTramite,
						idPersona, altaParte, esAutoridad, isInscripcion));
			} else {
				if (listaAcreedores.size() > 0) {
					sb.append(writeTablaAcreedor(elementId, idTramite,
							listaAcreedores, idPersona, isInscripcion));
				}
				AcreedorTO acreedorTO = altaParteDAO.getAcreedoresByID(
						Integer.valueOf(idTramite),
						Integer.valueOf(idPersonaModificar));
				altaParte = new AltaParteTO();
				altaParte.setNombre(acreedorTO.getNombre());
				altaParte.setApellidoMaterno(acreedorTO.getApellidoMaterno());
				altaParte.setApellidoPaterno(acreedorTO.getApellidoPaterno());
				altaParte.setRazonSocial(acreedorTO.getRazonSocial());
				altaParte.setFolioMercantil(acreedorTO.getFolioMercantil());
				altaParte.setCurp(acreedorTO.getCurp());				
				altaParte.setTelefono(acreedorTO.getTelefono());
				altaParte.setRfc(acreedorTO.getRfc());
				altaParte.setExtencion(acreedorTO.getExtencion());
				altaParte.setIdColonia(acreedorTO.getIdColonia());
				altaParte.setIdLocalidad(acreedorTO.getIdLocalidad());
				altaParte.setCorreoElectronico(acreedorTO
						.getCorreoElectronico());
				altaParte.setCalle(acreedorTO.getCalle());
				altaParte.setTipoPersona(acreedorTO.getTipoPersona());
				altaParte.setNumeroExterior(acreedorTO.getCalleNumero());
				altaParte
						.setNumeroInterior(acreedorTO.getCalleNumeroInterior());
				altaParte.setIdNacionalidad(acreedorTO.getIdNacionalidad());				
				altaParte.setIdPersona(Integer.valueOf(acreedorTO
						.getIdPersona()));
				altaParte.setIdUsuario(Integer.valueOf(Integer
						.valueOf(idPersona)));				
				altaParte.setCodigoPostal(acreedorTO.getCodigoPostal());
				altaParte.setIdPaisResidencia(acreedorTO.getIdPaisResidencia());
				
				altaParte.setEdad(acreedorTO.getEdad());
				altaParte.setEstadoCivil(acreedorTO.getEstadoCivil());
				altaParte.setProfesion(acreedorTO.getProfesion());
				altaParte.setInscrita(acreedorTO.getInscrita());
				altaParte.setFolioInscrito(acreedorTO.getFolioInscrito());
				altaParte.setLibroInscrito(acreedorTO.getLibroInscrito());
				altaParte.setUbicacionInscrito(acreedorTO.getUbicacionInscrito());
				altaParte.setDomicilioUno(acreedorTO.getDomicilioUno());
				
				sb.append(writeFormularioAcreedor(elementId, idTramite,
						idPersona, altaParte, esAutoridad, isInscripcion));
			}
			sb.append("</div></div></div>");
			
			dwr.setMessage(sb.toString());
			dwr.setCodeError(0);
		} catch (Exception e) {
			dwr.setCodeError(1);
			dwr.setMessage("Error el cargar el Acreerdor");
			e.printStackTrace();
		}
		dwr.setMessage(dwr.getMessage()
				+ " <script type=\"text/javascript\">  prepareInputsForHints(); </script> ");
		return dwr;
	}

	public MessageDwr firmaAcreedorRepresentado(String elementId,
			String idPersona, String idAcreedor, String idTramite) {
		MessageDwr dwr;
		GarantiasDAO garantiasDAO = new GarantiasDAO();
		DateUtilRug dateUtilRug = new DateUtilRug();
		
		MyLogger.Logger.log(Level.INFO, "---------------------------------------Entro Aca-----------------------------------------***");
		MyLogger.Logger.log(Level.INFO, "idTramite : " + idTramite);

		MyLogger.Logger.log(Level.INFO, "idTramite : " + idTramite);
		PrivilegiosDAO privilegiosDAO = new PrivilegiosDAO();
		PersonaTO personaTO = new PersonaTO();
		personaTO.setIdPersona(new Integer(idPersona));
		personaTO.setIdGrupo(1);
		UsuarioTO usuarioTO = new UsuarioTO();
		usuarioTO.setPersona(personaTO);
		privilegiosDAO.modificaPerfil(usuarioTO);
		MyLogger.Logger.log(Level.INFO, "");

		if (garantiasDAO.altaBitacoraTramite(Integer.valueOf(idTramite), 3, 3,
				dateUtilRug.parseDateToStr(Calendar.getInstance().getTime()),
				"V")) {
			MyLogger.Logger.log(Level.INFO, "se actulizo");
		}
		dwr = getParteAcreedorRepresentado(elementId, idPersona, "0", "");
		return dwr;
	}
	
	//el metodo sin generacion de folio electronico 7744585696
	public MessageDwr guardaParteAcreedorRepresentadoSinMensaje(String elementId,
			String idPersona, String idPersonaModificar, String nombre,
			String apellidoP, String apellidoM, String razonSocial,
			String nacionalidad, String tipoPersona, String folioElectronico,
			String rfc, String idColonia, String idLocalidad, String calle,
			String numExterior, String numInterior, String correoElectronico,
			String telefono, String extencion, String curpdoc,
			String domicilioUno, String domicilioDos, String poblacion,
			String zonaPostal, String idPaisResidencia, String tipo, Boolean acreedorInscribe, String nif,
			String inscrita, String folioInscrito, String libroInscrito, String ubicacionInscrito, 
			String edad, String estadoCivil, String profesion) {
		System.out.println(" nombre "+nombre+" apellidoP "+apellidoP +" apellidoM "+apellidoM);
		String cadena = "";
		System.out.println("================== acreedorInscribe "+acreedorInscribe);
		//Validacion renapo
		MessageDwr dwr = new MessageDwr();
		System.out.println("================== codeError "+dwr.getCodeError());
		boolean muestraOtorgante = true;
		int usaRenapo = 0;
		if (Constants.getParamValue(Constants.ACTIVAR_RENAPO).equalsIgnoreCase("true"))
		{
			usaRenapo=1;
		}
		if(folioElectronico.equals("") && curpdoc.equals("")){
			usaRenapo = 0;
		}
		AltaParteDAO altaParteDAO = new AltaParteDAO();
		//Validacion renapo
		MyLogger.Logger.log(Level.INFO, "entro --guardaParteAcreedorRepresentadoSinMensaje");
		try {
			ExpresionesRegularesUtil eru = new ExpresionesRegularesUtil();
			boolean valido = false;
			if (rfc == null || rfc.equals("")) {
				valido = true;
			} else {
				
				rfc=rfc.trim();
				
				valido = true;
			}
			if (valido) {
				MyLogger.Logger.log(Level.INFO,"RUG-Action:ParteDwrAction.guardaParteAcreedorRepresentado------Entro en el metodo ::::");
				AcreedorTO acreedorTO = new AcreedorTO();
				acreedorTO.setDomicilioUno(domicilioUno);
				acreedorTO.setDomicilioDos(domicilioDos);
				acreedorTO.setPoblacion(poblacion);
				acreedorTO.setZonaPostal(zonaPostal);
				acreedorTO.setTipoPersona(tipoPersona);
				acreedorTO.setRazonSocial(razonSocial);
				acreedorTO.setRfc(rfc);
				acreedorTO.setFolioMercantil(folioElectronico);
				acreedorTO.setCalle(calle);
				acreedorTO.setCalleNumero(numExterior);
				acreedorTO.setCalleNumeroInterior(numInterior);
				acreedorTO.setNombre(nombre);
				acreedorTO.setCurp(curpdoc);
				acreedorTO.setApellidoPaterno(apellidoP);
				acreedorTO.setApellidoMaterno(apellidoM);
				acreedorTO.setIdColonia(idColonia.equalsIgnoreCase("")?0:Integer.valueOf(idColonia));
				acreedorTO.setIdLocalidad(idLocalidad.equalsIgnoreCase("")?0:Integer.valueOf(idLocalidad));
				acreedorTO.setIdNacionalidad(nacionalidad.equalsIgnoreCase("")?0:Integer.valueOf(nacionalidad));
				acreedorTO.setTelefono(telefono);
				acreedorTO.setCorreoElectronico(correoElectronico);
				acreedorTO.setExtencion(extencion);
				acreedorTO.setIdPaisResidencia(Integer.valueOf(idPaisResidencia));
				acreedorTO.setTipo(tipo);
				acreedorTO.setAcreedorInscribe(acreedorInscribe);
				acreedorTO.setNif(nif);
				
				//campos nuevos
				acreedorTO.setInscrita(inscrita);
				acreedorTO.setFolioInscrito(folioInscrito);
				acreedorTO.setLibroInscrito(libroInscrito);
				acreedorTO.setUbicacionInscrito(ubicacionInscrito);
				acreedorTO.setEdad(edad);
				acreedorTO.setEstadoCivil(estadoCivil);
				acreedorTO.setProfesion(profesion);
				
				InscripcionTO inscripcionTO = new InscripcionTO();
				inscripcionTO.setIdPersona(Integer.valueOf(idPersona));
				inscripcionTO.setIdTipoTramite(new Integer(12));
				InscripcionDAO inscripcionDAO = new InscripcionDAO();
				
				List<AcreedorTO> listaAcreedores = acreedoresService.getAcreedoresByPersona(Integer.valueOf(idPersona));
				List<AcreedorTO> listaAcreedoresSinFirma = acreedoresService.getAcreedoresSinFirmaByPersona(Integer.valueOf(idPersona));
				List<AcreedorTO> listaTotal = new ArrayList<AcreedorTO>();
				listaTotal.addAll(listaAcreedores);
				listaTotal.addAll(listaAcreedoresSinFirma);
					Integer idTramite = inscripcionDAO.insert(inscripcionTO);
					acreedorTO.setIdTramitePendiente(idTramite);
					
					//==========================================================================================validaciones
					
					if (tipoPersona.equals("PF")) {
						if (nacionalidad.equals("1")) {
							
							if (usaRenapo == 0) {
								// fisico mexicano
								OtorganteTO otorganteTO = null;
								
								
								if(acreedorInscribe){
									if(!curpdoc.equals("")){
											if(folioElectronico.equals("")){
												otorganteTO = altaParteDAO.getOtorganteFisicoByCURPAcreedor(curpdoc);
											}
									}
								}
								
								MyLogger.Logger.log(Level.INFO,"obtiene otorgante por curp");
								if (otorganteTO != null) {
									MyLogger.Logger.log(Level.INFO,"en muestraFolioOtorganteExistente: OtorganteTO ES DIFERENTE DE NULO");
									boolean igual = true;
									System.out.println("nombre antes de eliminar espacios" + nombre);
									int longitud = nombre.length();
									System.out.println("cantidad de caracteres antes de eliminar espacios" + longitud);
									nombre = nombre.trim();
									int longitud2 = nombre.length();
									System.out.println("nombre despues de eliminarle espacios" + nombre);
									System.out.println("cantidad de caracteres despues de eliminar espacios" + longitud2);
									System.out.println("nombre antes de eliminar espacios" + apellidoP);
									int longitud3 = apellidoP.length();
									System.out.println("cantidad de caracteres antes de eliminar espacios"+ longitud3);
									apellidoP = apellidoP.trim();
									int longitud4 = apellidoP.length();
									System.out.println("nombre despues de eliminarle espacios" + apellidoP);
									System.out.println("cantidad de caracteres despues de eliminar espacios"+ longitud4);
									String nombre2 = otorganteTO.getNombre().trim();
									System.out.println("nombre despues de eliminarle espacios base de datos" + nombre2);
									nombre2 = nombre2.toUpperCase();
									System.out.println("Comvirtiendo nombre a mayusculas"+ nombre2);
									String apellidoP2 = otorganteTO.getApellidoPaterno().trim();
									System.out.println("apellido despues de eliminarle espacios base de datos"+ apellidoP2);
									apellidoP2 = apellidoP2.toUpperCase();
									System.out.println("Comvirtiendo apellido paterno mayusculas" + apellidoP2);
									if (!nombre2.equals(nombre)) {
										igual = false;
									}
									if (!apellidoP2.equals(apellidoP)) {
										igual = false;
									}
									MyLogger.Logger.log(Level.INFO, "valor del igual::"+ igual);

									dwr.setCodeError(0);
									dwr.setMessage(cadena);
									muestraOtorgante = false;
									cadena = " <script> alert('La CURP que ingres贸 corresponde al siguiente Folio Electronico : "
											+ otorganteTO.getFolioMercantil()
											+ "'); document.getElementById('tipoPersona').value = 'PF'; cambiaTipoPersona(); "
											+ " document.getElementById('nombre').value = '"
											+ notNull(nombre)
											+ "'; "
											+ " document.getElementById('apellidoPaterno').value = '"
											+ notNull(apellidoP)
											+ "'; "
											+ " document.getElementById('apellidoMaterno').value = '"
											+ notNull(apellidoM)
											+ "'; "
											+ " document.getElementById('nacionalidad').value = "
											+ nacionalidad
											+ "; "
											+ " document.getElementById('docExtranjero').value = '"
											+ curpdoc + "'; "
											+ " cambiaNacionalidad(); </script> ";
									
								} else {
									// otorgante es nulo
									System.out.println("========================================= "+tipoPersona);
									int longitud = acreedorTO.getNombre().length();
									System.out.println("cantidad de caracteres antes de eliminar espacios" + longitud);
									nombre = acreedorTO.getNombre().trim();
									int longitud2 = nombre.length();
									System.out.println("nombre despues de eliminarle espacios" + nombre);
									System.out.println("cantidad de caracteres despues de eliminar espacios" + longitud2);

									/***********************************************************************************************/
									/****************************** Apellido paterno desde el formulario *****************************/
									System.out.println("Apellido antes de eliminar espacios"+ acreedorTO.getApellidoPaterno());
									int longitud5 = acreedorTO.getApellidoPaterno().length();
									System.out.println("cantidad de caracteres antes de eliminar espacios" + longitud5);
									String apellidoP3 = acreedorTO.getApellidoPaterno().trim();
									int longitud6 = apellidoP3.length();
									System.out.println("Apellido despues de eliminarle espacios" + apellidoP3);
									System.out.println("cantidad de caracteres despues de eliminar espacios"+ longitud6);
									System.out.println("====================================== RFC : "+ acreedorTO.getRfc());
									System.out.println(" nombre "+nombre+" apellidoP "+apellidoP +" apellidoM "+apellidoM);
									
									//Modificacion de envio sin mostrar alerta
									dwr = guardarAcreedor(
											domicilioUno,
											domicilioDos,
											poblacion,
											zonaPostal,
											tipoPersona,
											razonSocial,
											rfc,
											acreedorTO.getFolioMercantil(),
											calle,
											acreedorTO.getCalleNumero(),
											acreedorTO.getCalleNumeroInterior(),
											nombre,
											acreedorTO.getCurp(),
											apellidoP,
											apellidoM,
											acreedorTO.getIdColonia(),
											acreedorTO.getIdLocalidad(),
											acreedorTO.getIdNacionalidad(),
											telefono,
											correoElectronico,
											extencion,
											Integer.parseInt(idPaisResidencia),
											idPersona,
											idTramite.toString(),
											elementId,
											acreedorInscribe,
											acreedorTO);
								}
							}// Termina sin usar renapo
						}// fin mexicano
						else {
							// fisico extranjero
							PlSql personaExtranjeraFisica = acreedoresService.altaAcreedorRepFinal(acreedorTO,Integer.valueOf(idPersona));
							MyLogger.Logger.log(Level.INFO, personaExtranjeraFisica.getResFolio());
							if (personaExtranjeraFisica.getIntPl() == 0) {
								MyLogger.Logger.log(Level.WARNING,"RUG-Action:ParteDwrAction.guardaParteAcreedorRepresentado------Termina proceso de guardar persona fisica extranjera ::::");
								
								if(acreedorInscribe){
									cadena = " <script languaje='javascript'> alert('El folio electronico :"+personaExtranjeraFisica.getResFolio()+"'); </script>";
								}else{
									cadena = " <script languaje='javascript'> alert('El registro fue realizado correctamente.'); </script>";
								}
							} else {
								MyLogger.Logger.log(Level.WARNING,"RUG-Action:ParteDwrAction.guardaParteAcreedorRepresentado------Sucedio un error en guardar persona fisica extranjera ::::");
								cadena = " <script languaje='javascript'> alert('Error : "+personaExtranjeraFisica.getStrPl()+"'); </script>";
							}
						}
					}// fin fisico
					else {
						
						System.out.println("==================================== tipo Persona "+tipoPersona);
						// insertar persona moral
						if (nacionalidad.equals("-1")) {
							if (tipo.equals("SM")) {
								MyLogger.Logger.log(Level.INFO,"Entro agregar parte Moral");
									PlSql personaExtranjeraFisica = acreedoresService.altaAcreedorRepFinal(acreedorTO,Integer.valueOf(idPersona));
									MyLogger.Logger.log(Level.INFO, personaExtranjeraFisica.getResFolio());
									if (personaExtranjeraFisica.getIntPl() == 0) {
										MyLogger.Logger.log(Level.WARNING,"RUG-Action:ParteDwrAction.guardaParteAcreedorRepresentado------Termina proceso de guardar persona fisica extranjera ::::");
										
										if(acreedorInscribe){
											cadena = " <script languaje='javascript'> alert('El folio electronico :"+personaExtranjeraFisica.getResFolio()+"'); </script>";
										}else{
											cadena = " <script languaje='javascript'> alert('El registro fue realizado correctamente.'); </script>";
										}
										
									} else {
										MyLogger.Logger.log(Level.WARNING,"RUG-Action:ParteDwrAction.guardaParteAcreedorRepresentado------Sucedio un error en guardar persona fisica extranjera ::::");
										cadena = " <script languaje='javascript'> alert('Error : "+personaExtranjeraFisica.getStrPl()+"'); </script>";
									}
									
							} else if (tipo.equals("OT")) {
								OtorganteTO otorganteTO = null;
								if(!rfc.equals("")){
										if(folioElectronico.equals("")){
											if(acreedorInscribe){
												otorganteTO = altaParteDAO.getOtorganteMoralByNIPOrigenFENULL(rfc,nacionalidad);
											}
										}
								}
								/************************************/
								if (otorganteTO != null) {
									boolean igual = true;
									if (!otorganteTO.getRazon().equals(razonSocial)) {
										igual = false;
									}
									if (!otorganteTO.getFolioMercantil().equals(folioElectronico)) {
										igual = false;
									}
									if (!otorganteTO.getRfc().equals(rfc)) {
										igual = false;
									}
									if (otorganteTO.getIdNacionalidad() == Integer.parseInt(nacionalidad)) {
										igual = false;
									}

									if (!igual) {
										muestraOtorgante = false;
										cadena = " <script languaje='javascript'> alert('RFC: "+ rfc
												+ " ya fue registrado en el sistema y esta asociado al folio electr贸nico: "+ otorganteTO.getFolioMercantil()
												+ "'); </script> ";
									}
								} else {
									MyLogger.Logger.log(Level.INFO,"Entro agregar parte Moral");
									PlSql personaExtranjeraFisica = acreedoresService.altaAcreedorRepFinal(acreedorTO,Integer.valueOf(idPersona));
									MyLogger.Logger.log(Level.INFO, personaExtranjeraFisica.getResFolio());
									if (personaExtranjeraFisica.getIntPl() == 0) {
										MyLogger.Logger.log(Level.WARNING,"Termino correctamente Persona Moral Mexicana sociedad mercantil del tipo Otras.");
										
										if(acreedorInscribe){
											cadena = " <script languaje='javascript'> alert('El folio electronico :"+personaExtranjeraFisica.getResFolio()+"'); </script>";
										}else{
											cadena = " <script languaje='javascript'> alert('El registro fue realizado correctamente.'); </script>";
										}
									} else {
										MyLogger.Logger.log(Level.WARNING,"Ocurrio un error Persona Moral Mexicana sociedad mercantil del tipo Otras");
										cadena = " <script languaje='javascript'> alert('Error : "+personaExtranjeraFisica.getStrPl()+"'); </script>";
									}
								}
								/************************************/
							}

						} else {
							MyLogger.Logger.log(Level.INFO,"Entro agregar parte Moral");
							PlSql personaExtranjeraFisica = acreedoresService.altaAcreedorRepFinal(acreedorTO,Integer.valueOf(idPersona));
							MyLogger.Logger.log(Level.INFO, personaExtranjeraFisica.getResFolio());
							if (personaExtranjeraFisica.getIntPl() == 0) {
								MyLogger.Logger.log(Level.WARNING,"Termino correctamente Persona Moral Extranjera sociedad mercantil del tipo Otras.");
								
								if(acreedorInscribe){
									cadena = " <script languaje='javascript'> alert('El folio electronico :"+personaExtranjeraFisica.getResFolio()+"'); </script>";
								}else{
									cadena = " <script languaje='javascript'> alert('El registro fue realizado correctamente.'); </script>";
								}
							} else {
								MyLogger.Logger.log(Level.WARNING,"Ocurrio un error Persona Moral Extranjera sociedad mercantil del tipo Otras");
								cadena = " <script languaje='javascript'> alert('Error : "+personaExtranjeraFisica.getStrPl()+"'); </script>";
							}
						}
					}
			} else {
				cadena = " <script> alert('El Documento de Identificaci贸n que ingreso fue incorrecto, Favor de llenar el formulario nuevamente.'); agregarNuevo(); </script> ";
			}
			MyLogger.Logger.log(Level.WARNING,cadena);
			System.out.println("===============la cadena "+cadena);
			dwr.setMessage(dwr.getMessage() + cadena);
		} catch (Exception e) {
			MyLogger.Logger.log(Level.INFO, "entro catch 4");
			e.printStackTrace();
			dwr = new MessageDwr();
			dwr = getParteAcreedorRepresentado(elementId, idPersona, "0", " ");
			dwr.setMessage(dwr.getMessage() + cadena);
			dwr.setCodeError(0);
		}
		
		return dwr;
	}
	
	//termina metodo sin generacion de folio
	
	
	public MessageDwr guardaParteAcreedorRepresentado(String elementId,
			String idPersona, String idPersonaModificar, String nombre,
			String apellidoP, String apellidoM, String razonSocial,
			String nacionalidad, String tipoPersona, String folioElectronico,
			String rfc, String idColonia, String idLocalidad, String calle,
			String numExterior, String numInterior, String correoElectronico,
			String telefono, String extencion, String curpdoc,
			String domicilioUno, String domicilioDos, String poblacion,
			String zonaPostal, String idPaisResidencia, String tipo, Boolean acreedorInscribe, String nif) {
		System.out.println(" nombre "+nombre+" apellidoP "+apellidoP +" apellidoM "+apellidoM);
		String cadena = "";
		System.out.println("================== acreedorInscribe "+acreedorInscribe);
		//Validacion renapo
		MessageDwr dwr = new MessageDwr();
		System.out.println("================== codeError "+dwr.getCodeError());
		boolean muestraOtorgante = true;
		int usaRenapo = 0;
		if (Constants.getParamValue(Constants.ACTIVAR_RENAPO).equalsIgnoreCase("true"))
		{
			usaRenapo=1;
		}
		if(folioElectronico.equals("") && curpdoc.equals("")){
			usaRenapo = 0;
		}
		AltaParteDAO altaParteDAO = new AltaParteDAO();
		//Validacion renapo
		MyLogger.Logger.log(Level.INFO, "entro");
		try {
			ExpresionesRegularesUtil eru = new ExpresionesRegularesUtil();
			boolean valido = false;
			if (rfc == null || rfc.equals("")) {
				valido = true;
			} else {
				if (tipoPersona.equals("PM")) {
					valido = eru.validarRfcMoral(rfc);
				} else {
					valido = eru.validarRfcFisica(rfc);
				}
			}
			if (valido) {
				MyLogger.Logger.log(Level.INFO,"RUG-Action:ParteDwrAction.guardaParteAcreedorRepresentado------Entro en el metodo ::::");
				AcreedorTO acreedorTO = new AcreedorTO();
				acreedorTO.setDomicilioUno(domicilioUno);
				acreedorTO.setDomicilioDos(domicilioDos);
				acreedorTO.setPoblacion(poblacion);
				acreedorTO.setZonaPostal(zonaPostal);
				acreedorTO.setTipoPersona(tipoPersona);
				acreedorTO.setRazonSocial(razonSocial);
				acreedorTO.setRfc(rfc);
				acreedorTO.setFolioMercantil(folioElectronico);
				acreedorTO.setCalle(calle);
				acreedorTO.setCalleNumero(numExterior);
				acreedorTO.setCalleNumeroInterior(numInterior);
				acreedorTO.setNombre(nombre);
				acreedorTO.setCurp(curpdoc);
				acreedorTO.setApellidoPaterno(apellidoP);
				acreedorTO.setApellidoMaterno(apellidoM);
				acreedorTO.setIdColonia(Integer.valueOf(idColonia));
				acreedorTO.setIdLocalidad(Integer.valueOf(idLocalidad));
				acreedorTO.setIdNacionalidad(Integer.valueOf(nacionalidad));
				acreedorTO.setTelefono(telefono);
				acreedorTO.setCorreoElectronico(correoElectronico);
				acreedorTO.setExtencion(extencion);
				acreedorTO.setIdPaisResidencia(Integer.valueOf(idPaisResidencia));
				acreedorTO.setTipo(tipo);
				acreedorTO.setAcreedorInscribe(acreedorInscribe);
				acreedorTO.setNif(nif);
				
				InscripcionTO inscripcionTO = new InscripcionTO();
				inscripcionTO.setIdPersona(Integer.valueOf(idPersona));
				inscripcionTO.setIdTipoTramite(new Integer(12));
				InscripcionDAO inscripcionDAO = new InscripcionDAO();
				
				List<AcreedorTO> listaAcreedores = acreedoresService.getAcreedoresByPersona(Integer.valueOf(idPersona));
				List<AcreedorTO> listaAcreedoresSinFirma = acreedoresService.getAcreedoresSinFirmaByPersona(Integer.valueOf(idPersona));
				List<AcreedorTO> listaTotal = new ArrayList<AcreedorTO>();
				listaTotal.addAll(listaAcreedores);
				listaTotal.addAll(listaAcreedoresSinFirma);
					Integer idTramite = inscripcionDAO.insert(inscripcionTO);
					acreedorTO.setIdTramitePendiente(idTramite);
					
					//==========================================================================================validaciones
					
					if (tipoPersona.equals("PF")) {
						if (nacionalidad.equals("1")) {
							
							if (usaRenapo == 0) {
								// fisico mexicano
								OtorganteTO otorganteTO = null;
								if(!curpdoc.equals("")){
										if(folioElectronico.equals("")){
											otorganteTO = altaParteDAO.getOtorganteFisicoByCURP(curpdoc);
										}
								}
								
								MyLogger.Logger.log(Level.INFO,"obtiene otorgante por curp");
								if (otorganteTO != null) {
									MyLogger.Logger.log(Level.INFO,"en muestraFolioOtorganteExistente: OtorganteTO ES DIFERENTE DE NULO");
									boolean igual = true;
									System.out.println("nombre antes de eliminar espacios" + nombre);
									int longitud = nombre.length();
									System.out.println("cantidad de caracteres antes de eliminar espacios" + longitud);
									nombre = nombre.trim();
									int longitud2 = nombre.length();
									System.out.println("nombre despues de eliminarle espacios" + nombre);
									System.out.println("cantidad de caracteres despues de eliminar espacios" + longitud2);
									System.out.println("nombre antes de eliminar espacios" + apellidoP);
									int longitud3 = apellidoP.length();
									System.out.println("cantidad de caracteres antes de eliminar espacios"+ longitud3);
									apellidoP = apellidoP.trim();
									int longitud4 = apellidoP.length();
									System.out.println("nombre despues de eliminarle espacios" + apellidoP);
									System.out.println("cantidad de caracteres despues de eliminar espacios"+ longitud4);
									String nombre2 = otorganteTO.getNombre().trim();
									System.out.println("nombre despues de eliminarle espacios base de datos" + nombre2);
									nombre2 = nombre2.toUpperCase();
									System.out.println("Comvirtiendo nombre a mayusculas"+ nombre2);
									String apellidoP2 = otorganteTO.getApellidoPaterno().trim();
									System.out.println("apellido despues de eliminarle espacios base de datos"+ apellidoP2);
									apellidoP2 = apellidoP2.toUpperCase();
									System.out.println("Comvirtiendo apellido paterno mayusculas" + apellidoP2);
									if (!nombre2.equals(nombre)) {
										igual = false;
									}
									if (!apellidoP2.equals(apellidoP)) {
										igual = false;
									}
									MyLogger.Logger.log(Level.INFO, "valor del igual::"+ igual);

									dwr.setCodeError(0);
									dwr.setMessage(cadena);
									muestraOtorgante = false;
									cadena = " <script> alert('La CURP que ingres贸 corresponde al siguiente Folio Electronico : "
											+ otorganteTO.getFolioMercantil()
											+ "'); document.getElementById('tipoPersona').value = 'PF'; cambiaTipoPersona(); "
											+ " document.getElementById('nombre').value = '"
											+ notNull(nombre)
											+ "'; "
											+ " document.getElementById('apellidoPaterno').value = '"
											+ notNull(apellidoP)
											+ "'; "
											+ " document.getElementById('apellidoMaterno').value = '"
											+ notNull(apellidoM)
											+ "'; "
											+ " document.getElementById('nacionalidad').value = "
											+ nacionalidad
											+ "; "
											+ " document.getElementById('docExtranjero').value = '"
											+ curpdoc + "'; "
											+ " cambiaNacionalidad(); </script> ";
									
									dwr = getParteAcreedorRepresentado(elementId, idPersona, "0", " ");
								} else {
									// otorgante es nulo
									System.out.println("========================================= "+tipoPersona);
									MyLogger.Logger.log(Level.INFO,"隆隆en OTORGANTE nulo !! "+ acreedorTO.getCurp());

									int longitud = acreedorTO.getNombre().length();
									System.out.println("cantidad de caracteres antes de eliminar espacios" + longitud);
									nombre = acreedorTO.getNombre().trim();
									int longitud2 = nombre.length();
									System.out.println("nombre despues de eliminarle espacios" + nombre);
									System.out.println("cantidad de caracteres despues de eliminar espacios" + longitud2);

									/***********************************************************************************************/
									/****************************** Apellido paterno desde el formulario *****************************/
									System.out.println("Apellido antes de eliminar espacios"+ acreedorTO.getApellidoPaterno());
									int longitud5 = acreedorTO.getApellidoPaterno().length();
									System.out.println("cantidad de caracteres antes de eliminar espacios" + longitud5);
									String apellidoP3 = acreedorTO.getApellidoPaterno().trim();
									int longitud6 = apellidoP3.length();
									System.out.println("Apellido despues de eliminarle espacios" + apellidoP3);
									System.out.println("cantidad de caracteres despues de eliminar espacios"+ longitud6);
									System.out.println("====================================== RFC : "+ acreedorTO.getRfc());
									System.out.println(" nombre "+nombre+" apellidoP "+apellidoP +" apellidoM "+apellidoM);
									cadena = "<script>"
											+ "displayMessageNuevoAcreedorRepresentado(true,' ','ADVERTENCIA: En la base de datos del RUG no existe un Otorgante con los atributos que ha ingresado. Aseg煤rese de la veracidad y exactitud de dicha informaci贸n, ya que el sistema matricular谩 al Otorgante con base en la informaci贸n ingresada por usted.','"
											+ acreedorTO.getCurp()
											+ "','"+ nombre
											+ "','"+ apellidoP
											+ "','"+ apellidoM
											+ "','"+ elementId
											+ "','"+ acreedorTO.getIdTramitePendiente()
											+ "','"+ idPersona
											+ "','"+ "1"//isInscripcion
											+ "','"+ nacionalidad
											+ "','"+ tipoPersona
											+ "','"+ "0"//isMultiple
											+ "','"+acreedorTO.getDomicilioUno()
											+ "','"+acreedorTO.getDomicilioDos()
											+ "','"+acreedorTO.getPoblacion()
											+ "','"+acreedorTO.getZonaPostal()
											+ "','"+acreedorTO.getTipoPersona()
											+ "','"+acreedorTO.getRazonSocial()
											+ "','"+acreedorTO.getRfc()
											+ "','"+acreedorTO.getFolioMercantil()
											+ "','"+acreedorTO.getCalle()
											+ "','"+acreedorTO.getCalleNumero()
											+ "','"+acreedorTO.getCalleNumeroInterior()
											+ "','"+acreedorTO.getIdColonia()
											+ "','"+acreedorTO.getIdLocalidad()
											+ "','"+acreedorTO.getTelefono()
											+ "','"+acreedorTO.getCorreoElectronico()
											+ "','"+acreedorTO.getExtencion()
											+ "','"+acreedorTO.getIdPaisResidencia()
											+ "','"+acreedorTO.getAcreedorInscribe()
											+ "'); </script>";
									
									dwr = getParteAcreedorRepresentado(elementId, idPersona, "0", " ");
								}
							}// Termina sin usar renapo
						}// fin mexicano
						else {
							// fisico extranjero
							PlSql personaExtranjeraFisica = acreedoresService.altaAcreedorRepFinal(acreedorTO,Integer.valueOf(idPersona));
							MyLogger.Logger.log(Level.INFO, personaExtranjeraFisica.getResFolio());
							if (personaExtranjeraFisica.getIntPl() == 0) {
								MyLogger.Logger.log(Level.WARNING,"RUG-Action:ParteDwrAction.guardaParteAcreedorRepresentado------Termina proceso de guardar persona fisica extranjera ::::");
								
								if(acreedorInscribe){
									cadena = " <script languaje='javascript'> alert('El folio electronico :"+personaExtranjeraFisica.getResFolio()+"'); </script>";
								}else{
									cadena = " <script languaje='javascript'> alert('El registro fue realizado correctamente.'); </script>";
								}
								dwr = getParteAcreedorRepresentado(elementId, idPersona, "0", " ");
							} else {
								MyLogger.Logger.log(Level.WARNING,"RUG-Action:ParteDwrAction.guardaParteAcreedorRepresentado------Sucedio un error en guardar persona fisica extranjera ::::");
								cadena = " <script languaje='javascript'> alert('Error : "+personaExtranjeraFisica.getStrPl()+"'); </script>";
								dwr = getParteAcreedorRepresentado(elementId, idPersona, "0", " ");
							}
						}
					}// fin fisico
					else {
						
						System.out.println("==================================== tipo Persona "+tipoPersona);
						// insertar persona moral
						if (nacionalidad.equals("1")) {
							if (tipo.equals("SM")) {
								MyLogger.Logger.log(Level.INFO,"Entro agregar parte Moral");
									PlSql personaExtranjeraFisica = acreedoresService.altaAcreedorRepFinal(acreedorTO,Integer.valueOf(idPersona));
									MyLogger.Logger.log(Level.INFO, personaExtranjeraFisica.getResFolio());
									if (personaExtranjeraFisica.getIntPl() == 0) {
										MyLogger.Logger.log(Level.WARNING,"RUG-Action:ParteDwrAction.guardaParteAcreedorRepresentado------Termina proceso de guardar persona fisica extranjera ::::");
										
										if(acreedorInscribe){
											cadena = " <script languaje='javascript'> alert('El folio electronico :"+personaExtranjeraFisica.getResFolio()+"'); </script>";
										}else{
											cadena = " <script languaje='javascript'> alert('El registro fue realizado correctamente.'); </script>";
										}
										
										dwr = getParteAcreedorRepresentado(elementId, idPersona, "0", " ");
									} else {
										MyLogger.Logger.log(Level.WARNING,"RUG-Action:ParteDwrAction.guardaParteAcreedorRepresentado------Sucedio un error en guardar persona fisica extranjera ::::");
										cadena = " <script languaje='javascript'> alert('Error : "+personaExtranjeraFisica.getStrPl()+"'); </script>";
										dwr = getParteAcreedorRepresentado(elementId, idPersona, "0", " ");
									}
									
							} else if (tipo.equals("OT")) {
								OtorganteTO otorganteTO = null;
								if(!rfc.equals("")){
										if(folioElectronico.equals("")){
											otorganteTO = altaParteDAO.getOtorganteMoralByNIPOrigenFENULL(rfc,nacionalidad);
										}
								}
								/************************************/
								if (otorganteTO != null) {
									boolean igual = true;
									if (!otorganteTO.getRazon().equals(razonSocial)) {
										igual = false;
									}
									if (!otorganteTO.getFolioMercantil().equals(folioElectronico)) {
										igual = false;
									}
									if (!otorganteTO.getRfc().equals(rfc)) {
										igual = false;
									}
									if (otorganteTO.getIdNacionalidad() == Integer.parseInt(nacionalidad)) {
										igual = false;
									}

									if (!igual) {
										muestraOtorgante = false;
										cadena = " <script languaje='javascript'> alert('RFC: "+ rfc
												+ " ya fue registrado en el sistema y esta asociado al folio electr贸nico: "+ otorganteTO.getFolioMercantil()
												+ "'); </script> ";
									}
								} else {
									MyLogger.Logger.log(Level.INFO,"Entro agregar parte Moral");
									PlSql personaExtranjeraFisica = acreedoresService.altaAcreedorRepFinal(acreedorTO,Integer.valueOf(idPersona));
									MyLogger.Logger.log(Level.INFO, personaExtranjeraFisica.getResFolio());
									if (personaExtranjeraFisica.getIntPl() == 0) {
										MyLogger.Logger.log(Level.WARNING,"Termino correctamente Persona Moral Mexicana sociedad mercantil del tipo Otras.");
										
										if(acreedorInscribe){
											cadena = " <script languaje='javascript'> alert('El folio electronico :"+personaExtranjeraFisica.getResFolio()+"'); </script>";
										}else{
											cadena = " <script languaje='javascript'> alert('El registro fue realizado correctamente.'); </script>";
										}
										
										dwr = getParteAcreedorRepresentado(elementId, idPersona, "0", " ");
									} else {
										MyLogger.Logger.log(Level.WARNING,"Ocurrio un error Persona Moral Mexicana sociedad mercantil del tipo Otras");
										cadena = " <script languaje='javascript'> alert('Error : "+personaExtranjeraFisica.getStrPl()+"'); </script>";
										dwr = getParteAcreedorRepresentado(elementId, idPersona, "0", " ");
									}
								}
								/************************************/
							}

						} else {
							// insertar persona moral extranjera
							OtorganteTO otorganteTO = altaParteDAO
									.getOtorganteMoralByNIPOrigenExtranjero(rfc,
											nacionalidad, razonSocial);
							/************************************/
							if (otorganteTO != null) {
								cadena = " <script languaje='javascript'> alert('El n煤mero de Identificador Fiscal en el pa铆s "
										+ otorganteTO.getDescNacionalidad()
										+ " correpondiente a la sociedad "
										+ razonSocial
										+ " se encuentra asociado al folio electr贸nico: "
										+ otorganteTO.getFolioMercantil()
										+ "'); </script> ";
								
								dwr = getParteAcreedorRepresentado(elementId, idPersona, "0", " ");
							} else {// este es el 1
								MyLogger.Logger.log(Level.INFO,"Entro agregar parte Moral");
								PlSql personaExtranjeraFisica = acreedoresService.altaAcreedorRepFinal(acreedorTO,Integer.valueOf(idPersona));
								MyLogger.Logger.log(Level.INFO, personaExtranjeraFisica.getResFolio());
								if (personaExtranjeraFisica.getIntPl() == 0) {
									MyLogger.Logger.log(Level.WARNING,"Termino correctamente Persona Moral Extranjera sociedad mercantil del tipo Otras.");
									
									if(acreedorInscribe){
										cadena = " <script languaje='javascript'> alert('El folio electronico :"+personaExtranjeraFisica.getResFolio()+"'); </script>";
									}else{
										cadena = " <script languaje='javascript'> alert('El registro fue realizado correctamente.'); </script>";
									}
									
									dwr = getParteAcreedorRepresentado(elementId, idPersona, "0", " ");
								} else {
									MyLogger.Logger.log(Level.WARNING,"Ocurrio un error Persona Moral Extranjera sociedad mercantil del tipo Otras");
									cadena = " <script languaje='javascript'> alert('Error : "+personaExtranjeraFisica.getStrPl()+"'); </script>";
									dwr = getParteAcreedorRepresentado(elementId, idPersona, "0", " ");
								}
							}
						}
					}
			} else {
				cadena = " <script> alert('El RFC que ingreso fue incorrecto, Favor de llenar el formulario nuevamente.'); agregarNuevo(); </script> ";
			}
			MyLogger.Logger.log(Level.WARNING,cadena);
			System.out.println("===============la cadena "+cadena);
			dwr.setMessage(dwr.getMessage() + cadena);
		} catch (Exception e) {
			MyLogger.Logger.log(Level.INFO, "entro catch 4");
			e.printStackTrace();
			dwr = new MessageDwr();
			dwr = getParteAcreedorRepresentado(elementId, idPersona, "0", " ");
			dwr.setMessage(dwr.getMessage() + cadena);
			dwr.setCodeError(0);
//			dwr = getParteAcreedorRepresentado(elementId, idPersona, "0", " ");
//			dwr.setMessage(" <script> alert('Hubo un problema al Guargar en la base'); </script>");
		}
		
		return dwr;
	}


	public MessageDwr guardaParteAcreedor(String elementId, String idTramite,
			String idPersona, String idPersonaModificar, String isInscripcion, String nombre,
			String apellidoP, String apellidoM, String razonSocial,
			String nacionalidad, String tipoPersona, String folioElectronico,
			String idColonia, String idLocalidad, String calle,
			String numExterior, String numInterior, String correoElectronico,
			String telefono, String nit, String curpdoc,
			String domicilioUno, String domicilioDos, String poblacion,
			String zonaPostal, String idResidencia, String inscrita, String folio, 
			String libro, String direccion, String edad, String estadoCivil, 
			String profesion, String dpi) {
		MessageDwr dwr;
		String cadena = "";
		MyLogger.Logger.log(Level.INFO, "entro");
		try {
			MyLogger.Logger.log(Level.INFO, "entro try con : " + razonSocial);
			AltaParteTO altaParteTO = new AltaParteTO();
			altaParteTO.setDomicilioUno(domicilioUno);
			altaParteTO.setDomicilioDos(domicilioDos);
			altaParteTO.setPoblacion(poblacion);
			altaParteTO.setZonaPostal(zonaPostal);
			altaParteTO.setIdParte(3);
			altaParteTO.setIdTramite(Integer.valueOf(idTramite));
			altaParteTO.setFolioMercantil(folioElectronico);

			altaParteTO.setCurp(dpi);
			altaParteTO.setNombre(nombre);
			altaParteTO.setApellidoMaterno(apellidoM);
			altaParteTO.setApellidoPaterno(apellidoP);
			altaParteTO.setRazonSocial(razonSocial);
			altaParteTO.setIdNacionalidad(Integer.valueOf(nacionalidad));
			altaParteTO.setTipoPersona(tipoPersona);
			altaParteTO.setHayDomicilio("V");
			altaParteTO.setIdColonia(Integer.valueOf(idColonia));
			altaParteTO.setIdLocalidad(Integer.valueOf(idLocalidad));
			altaParteTO.setCalle(calle);
			altaParteTO.setNumeroExterior(numExterior);
			altaParteTO.setNumeroInterior(numInterior);
			altaParteTO.setCorreoElectronico(correoElectronico);
			altaParteTO.setTelefono(telefono);
			altaParteTO.setExtencion("");
			altaParteTO.setNif(nit);
			altaParteTO.setIdPersona(Integer.valueOf(idPersonaModificar));
			altaParteTO.setIdUsuario(Integer.valueOf(idPersona));
			altaParteTO.setIdPaisResidencia(Integer.valueOf(idResidencia));
			//campos nuevos
			altaParteTO.setInscrita(inscrita);
			altaParteTO.setFolioInscrito(folio);
			altaParteTO.setLibroInscrito(libro);
			altaParteTO.setUbicacionInscrito(direccion);
			altaParteTO.setEdad(edad);
			altaParteTO.setEstadoCivil(estadoCivil);
			altaParteTO.setProfesion(profesion);
			altaParteTO.setRfc(nit);

			MyLogger.Logger.log(Level.INFO, "Guardando acreedor representado");
			AltaParteDAO altaParteDAO = new AltaParteDAO();
			altaParteDAO.insert(altaParteTO);

			MyLogger.Logger.log(Level.INFO, "finalizo correctamente");
			dwr = getParteAcreedor(elementId, idTramite, idPersona,
					idPersonaModificar, isInscripcion);
			dwr.setMessage(dwr.getMessage() + cadena);
		} catch (Exception e) {
			MyLogger.Logger.log(Level.INFO, "entro catch 4");
			e.printStackTrace();
			dwr = new MessageDwr();
			dwr.setMessage(" <script> alert('Hubo un problema al Guargar en la base'); </script>");
		}

		return dwr;
	}

	public MessageDwr guardaParteDeudor(String elementId, String idTramite,
			String idPersona, String idPersonaModificar, String isInscripcion,
			String nombre, String apellidoP, String apellidoM, String razonSocial,
			String dpi, String nacionalidad, String inscrita, String folio, 
			String libro, String direccion, String nit, String edad, 
			String estadoCivil, String profesion, String telefono, String correo, 
			String residencia, String domicilio, String tipoPersona) {
		MessageDwr dwr = new MessageDwr();
		try {

			AltaParteTO altaParteTO = new AltaParteTO();
			altaParteTO.setIdParte(2);
			altaParteTO.setIdTramite(Integer.valueOf(idTramite));
			altaParteTO.setRazonSocial(razonSocial);
			altaParteTO.setTipoPersona(tipoPersona);
			altaParteTO.setNombre(nombre);
			altaParteTO.setApellidoPaterno(apellidoP);
			altaParteTO.setApellidoMaterno(apellidoM);
			altaParteTO.setRfc(nit);
			altaParteTO.setCurp(dpi);
			altaParteTO.setHayDomicilio("V");
			altaParteTO.setIdNacionalidad(Integer.valueOf(nacionalidad));
			altaParteTO.setIdPersona(Integer.valueOf(idPersona));
			//Campos Nuevos
			altaParteTO.setInscrita(inscrita);
			altaParteTO.setFolioInscrito(folio);
			altaParteTO.setLibroInscrito(libro);
			altaParteTO.setUbicacionInscrito(direccion);
			altaParteTO.setExtencion("");
			altaParteTO.setNif(nit);
			altaParteTO.setEdad(edad);
			altaParteTO.setEstadoCivil(estadoCivil);
			altaParteTO.setProfesion(profesion);
			altaParteTO.setTelefono(telefono);
			altaParteTO.setCorreoElectronico(correo);
			altaParteTO.setIdPaisResidencia(Integer.valueOf(residencia));
			altaParteTO.setDomicilioUno(domicilio);
			
			AltaParteDAO altaParteDAO = new AltaParteDAO();
			PlSql idCreado = altaParteDAO.insert(altaParteTO);
			MyLogger.Logger.log(Level.INFO,	"id del objeto creado" + idCreado.getResIntPl());
			dwr = getParteDeudor(elementId, idTramite, idPersona, idPersonaModificar, isInscripcion);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dwr;
	}

	// -------------------
	// showCurpResponse
	// alex
	
	public MessageDwr guardarAcreedor(
			String domicilioUno,
			String domicilioDos,
			String poblacion,
			String zonaPostal,
			String tipoPersona,
			String razonSocial,
			String rfc,
			String folioMercantil,
			String calle,
			String calleNumero,
			String calleNumeroInterior,
			String nombre,
			String curp,
			String apellidoPaterno,
			String apellidoMaterno,
			int idColonia,
			int idLocalidad,
			int idNacionalidad,
			String telefono,
			String correoElectronico,
			String extencion,
			int idPaisResidencia, 
			String idPersona,
			String idTramite,
			String elementId,
			Boolean acreedorInscribe,
			AcreedorTO pAcreedorTO){
		
		MessageDwr dwr = new MessageDwr();
		
		AcreedorTO acreedorTO = new AcreedorTO();
		acreedorTO.setDomicilioUno(domicilioUno);
		acreedorTO.setDomicilioDos(domicilioDos);
		acreedorTO.setPoblacion(poblacion);
		acreedorTO.setZonaPostal(zonaPostal);
		acreedorTO.setTipoPersona(tipoPersona);
		acreedorTO.setRazonSocial(razonSocial);
		acreedorTO.setRfc(rfc);
		acreedorTO.setFolioMercantil(folioMercantil);
		acreedorTO.setCalle(calle);
		acreedorTO.setCalleNumero(calleNumero);
		acreedorTO.setCalleNumeroInterior(calleNumeroInterior);
		acreedorTO.setNombre(nombre);
		acreedorTO.setCurp(curp);
		acreedorTO.setApellidoPaterno(apellidoPaterno);
		acreedorTO.setApellidoMaterno(apellidoMaterno);
		acreedorTO.setIdColonia(Integer.valueOf(idColonia));
		acreedorTO.setIdLocalidad(Integer.valueOf(idLocalidad));
		acreedorTO.setIdNacionalidad(Integer.valueOf(idNacionalidad));
		acreedorTO.setTelefono(telefono);
		acreedorTO.setCorreoElectronico(correoElectronico);
		acreedorTO.setExtencion(extencion);
		acreedorTO.setIdPaisResidencia(Integer.valueOf(idPaisResidencia));
		acreedorTO.setIdTramitePendiente(Integer.parseInt(idTramite));
		acreedorTO.setAcreedorInscribe(acreedorInscribe);
		
		//campos nuevos
		acreedorTO.setInscrita(pAcreedorTO.getInscrita());
		acreedorTO.setFolioInscrito(pAcreedorTO.getFolioInscrito());
		acreedorTO.setLibroInscrito(pAcreedorTO.getLibroInscrito());
		acreedorTO.setUbicacionInscrito(pAcreedorTO.getUbicacionInscrito());
		acreedorTO.setEdad(pAcreedorTO.getEdad());
		acreedorTO.setEstadoCivil(pAcreedorTO.getEstadoCivil());
		acreedorTO.setProfesion(pAcreedorTO.getProfesion());
				
		String cadena="";
		
		MyLogger.Logger.log(Level.INFO,"Entro agregar parte Moral");
		PlSql personaExtranjeraFisica = acreedoresService.altaAcreedorRepFinal(acreedorTO,Integer.valueOf(idPersona));
		MyLogger.Logger.log(Level.INFO, personaExtranjeraFisica.getResFolio());
		if (personaExtranjeraFisica.getIntPl() == 0) {
			MyLogger.Logger.log(Level.WARNING,"Termino correctamente Persona Moral Extranjera sociedad mercantil del tipo Otras.");
			
			if(acreedorInscribe){
				cadena = " <script languaje='javascript'> alert('El folio electronico :"+personaExtranjeraFisica.getResFolio()+"'); </script>";
			}else{
				cadena = " <script languaje='javascript'> alert('El registro fue realizado correctamente.'); </script>";
			}
			ObjectMapper mapper = new ObjectMapper();
			
			try {
				dwr.setData(mapper.writeValueAsString(acreedorTO));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			
		} else {
			MyLogger.Logger.log(Level.WARNING,"Ocurrio un error Persona Fisica.");
			cadena = " <script languaje='javascript'> alert('Ocurrio un error al generar el registro.'); </script>";
			dwr.setCodeError(-1001);
		}
		
		//dwr = getParteAcreedorRepresentado(elementId, idPersona, "0", " ");
		dwr.setMessage(dwr.getMessage() + cadena);
		return dwr;
	}
	
	public MessageDwr curpRenapo(String elementId, String idTramite,
			String idPersona, String idPersonaModificar, String nombre,
			String apellidoP, String apellidoM, String razonSocial,
			String nacionalidad, String tipoPersona, String folioElectronico,
			String docExtranjero, String rfc, String isInscripcion,
			String isMultiple, String curp) {

		MessageDwr dwr = new MessageDwr();
		String cadena = " ";

		RenapoConsultaCurp consultaCurp = new RenapoConsultaCurp();
		/*ResponseCurp persona;
		try {
			persona = consultaCurp.getDataFromCurp(curp);

			cadena = " <script> displayMessageConfirmacionCurp(true,' ','Favor de confirmar informaci贸n.','"
					+ curp
					+ "','"
					+ persona.getNombre()
					+ "','"
					+ persona.getApellidoPaterno()
					+ "',"
					+ "'"
					+ persona.getApellidoMaterno()
					+ "','"
					+ elementId
					+ "','"
					+ idTramite
					+ "','"
					+ idPersona
					+ "','"
					+ isInscripcion
					+ "','" + isMultiple + "'); </script>";

		} catch (NoCurpFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cadena = " <script> displayMessageConfirmacionCurpFail(true,' ','"
					+ e.getMessage() + "'); </script>";

		} catch (NoServiceRenapoAvailable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cadena = " <script> displayMessageConfirmacionCurpFail(true,' ','"
					+ e.getMessage() + "'); </script>";
		} catch (Exception e) {
			cadena = " <script> displayMessageConfirmacionCurpFail(true,' ','"
					+ e.getMessage() + "'); </script>";
		}*/

		dwr.setCodeError(0);
		dwr.setMessage(cadena);
		return dwr;
	}

	public MessageDwr guardaParteOtorgante(String elementId, String idTramite,
			String idPersona, String idPersonaModificar, String isInscripcion,
			String nombre, String apellidoP, String apellidoM, String razonSocial,
			String dpi, String nacionalidad, String inscrita, String folio, 
			String libro, String direccion, String nit, String edad, 
			String estadoCivil, String profesion, String telefono, String correo, 
			String residencia, String domicilio, String tipoPersona, String isMultiple) {

		MessageDwr dwr = new MessageDwr();
		 
		try {
			AltaParteTO altaParteTO = new AltaParteTO();
			altaParteTO.setIdParte(1);
			altaParteTO.setIdTramite(Integer.valueOf(idTramite));
			altaParteTO.setRazonSocial(razonSocial);
			altaParteTO.setTipoPersona(tipoPersona);
			altaParteTO.setNombre(nombre);
			altaParteTO.setApellidoPaterno(apellidoP);
			altaParteTO.setApellidoMaterno(apellidoM);
			altaParteTO.setRfc(nit);
			altaParteTO.setCurp(dpi);
			altaParteTO.setHayDomicilio("V");
			altaParteTO.setIdNacionalidad(Integer.valueOf(nacionalidad));
			altaParteTO.setIdPersona(Integer.valueOf(idPersona));
			//Campos Nuevos
			altaParteTO.setInscrita(inscrita);
			altaParteTO.setFolioInscrito(folio);
			altaParteTO.setLibroInscrito(libro);
			altaParteTO.setUbicacionInscrito(direccion);
			altaParteTO.setExtencion("");
			altaParteTO.setNif(nit);
			altaParteTO.setEdad(edad);
			altaParteTO.setEstadoCivil(estadoCivil);
			altaParteTO.setProfesion(profesion);
			altaParteTO.setTelefono(telefono);
			altaParteTO.setCorreoElectronico(correo);
			altaParteTO.setIdPaisResidencia(Integer.valueOf(residencia));
			altaParteTO.setDomicilioUno(domicilio);
			
			
			AltaParteDAO altaParteDAO = new AltaParteDAO();
			
			PlSql idPersonaINT = altaParteDAO.insert(altaParteTO);

			MyLogger.Logger.log(Level.INFO,	"id del objeto creado" + idPersonaINT.getResIntPl());

			dwr = getParteOtorgante(elementId, idTramite, idPersona, "0",
					isInscripcion, isMultiple);


		} catch (Exception e) {
			e.printStackTrace();			
		}

		return dwr;

	}

	// ------------------------------hola2

	// public MessageDwr guardaParteComerciante(String elementId,
	// String idTramite, String idPersona, String idPersonaModificar,
	// String nombre, String apellidoP, String apellidoM,
	// String razonSocial, String nacionalidad, String tipoPersona,
	// String folioElectronico, String docExtranjero, String rfc,
	// String isInscripcion) {
	// MessageDwr dwr;
	// MyLogger.Logger.log(Level.INFO, "entro");
	// try {
	// MyLogger.Logger.log(Level.INFO, "entro try con : " + razonSocial);
	//
	// AltaParteTO altaParteTO = new AltaParteTO();
	// altaParteTO.setIdParte(1);
	// altaParteTO.setIdTramite(Integer.valueOf(idTramite));
	// altaParteTO.setFolioMercantil(folioElectronico);
	// altaParteTO.setCurp(docExtranjero);
	// altaParteTO.setRfc(rfc);
	// altaParteTO.setNombre(nombre);
	// altaParteTO.setApellidoMaterno(apellidoM);
	// altaParteTO.setApellidoPaterno(apellidoP);
	// altaParteTO.setRazonSocial(razonSocial);
	// altaParteTO.setIdNacionalidad(Integer.valueOf(nacionalidad));
	// altaParteTO.setTipoPersona(tipoPersona);
	// altaParteTO.setHayDomicilio("F");
	// altaParteTO.setIdColonia(1);
	// altaParteTO.setIdLocalidad(1);
	// altaParteTO.setIdNacionalidad(1);
	// altaParteTO.setCorreoElectronico(" ");
	// altaParteTO.setTelefono(" ");
	// altaParteTO.setExtencion(" ");
	// altaParteTO.setTelefono("a");
	// altaParteTO.setExtencion("a");
	// altaParteTO.setCorreoElectronico("a");
	// altaParteTO.setIdUsuario(Integer.valueOf(idPersona));
	// altaParteTO.setIdPersona(Integer.valueOf(idPersonaModificar));
	// AltaParteDAO altaParteDAO = new AltaParteDAO();
	// altaParteDAO.insert(altaParteTO);
	// } catch (Exception e) {
	// MyLogger.Logger.log(Level.INFO, "entro catch");
	// e.printStackTrace();
	// }
	//
	// MyLogger.Logger.log(Level.INFO, "finalizo correctamente");
	// dwr = getParteComerciante(elementId, idTramite, idPersona,
	// idPersonaModificar);
	// return dwr;
	// }

	public MessageDwr modificaParteDeudor(String elementId, String idTramite,
			String idPersona, String idPersonaModificar, String isInscripcion,
			String nombre, String apellidoP, String apellidoM, String dpi,
			String razonSocial, String nacionalidad, String inscrita, String folio, 
			String libro, String direccion, String nit, String edad, 
			String estadoCivil, String profesion, String telefono, String correo, 
			String residencia, String domicilio, String tipoPersona) {
		MessageDwr dwr = new MessageDwr();
		System.out.println("Elemento" +  elementId +
				"Tramite " + idTramite +
				"Persona " + idPersona + "" +
				"Persona Modificar " + idPersonaModificar +
				"Es Inscripcion " + isInscripcion +
				"Nombre " +	nombre +
				"Apellido Paterno" + apellidoP +
				"Apellido Materno " + apellidoM +
				"DPI " + dpi +
				"Razon Social " + razonSocial +
				"Nacionalidad " + nacionalidad +
				"Inscrita " +	inscrita +
				"Folio " + folio +
				"Libro " + libro +
				"Direccion " + direccion +
				"Nit " + nit +
				"Edad " + edad +
				"Estado Civil " + estadoCivil +
				"Profesion" + profesion +
				"Telefono " + telefono +
				"Correo " + correo +
				"Residencia " + residencia +
				"Domicilio " + domicilio +
				"Tipo Persona " + tipoPersona);
		try {

			AltaParteTO altaParteTO = new AltaParteTO();
			altaParteTO.setIdParte(2);
			altaParteTO.setIdTramite(Integer.valueOf(idTramite));
			altaParteTO.setRazonSocial(razonSocial);
			altaParteTO.setTipoPersona(tipoPersona);
			altaParteTO.setNombre(nombre);
			altaParteTO.setApellidoPaterno(apellidoP);
			altaParteTO.setApellidoMaterno(apellidoM);
			altaParteTO.setRfc(nit);
			altaParteTO.setCurp(dpi);
			altaParteTO.setHayDomicilio("V");
			altaParteTO.setIdNacionalidad(Integer.valueOf(nacionalidad));
			altaParteTO.setIdUsuario(Integer.valueOf(idPersona));
			altaParteTO.setIdPersona(Integer.valueOf(idPersonaModificar));
			//Campos Nuevos
			altaParteTO.setInscrita(inscrita);
			altaParteTO.setFolioInscrito(folio);
			altaParteTO.setLibroInscrito(libro);
			altaParteTO.setUbicacionInscrito(direccion);
			altaParteTO.setExtencion("");
			altaParteTO.setNif(nit);
			altaParteTO.setEdad(edad);
			altaParteTO.setEstadoCivil(estadoCivil);
			altaParteTO.setProfesion(profesion);
			altaParteTO.setTelefono(telefono);
			altaParteTO.setCorreoElectronico(correo);
			altaParteTO.setIdPaisResidencia(Integer.valueOf(residencia));
			altaParteTO.setDomicilioUno(domicilio);
			
			AltaParteDAO altaParteDAO = new AltaParteDAO();
                        
                        //PREGUNTAR SI YA EXISTE LA PERSONA CON OTRO TIPO DE PERSONA ID
                        
                        
                        
			altaParteDAO.actualizaParte(altaParteTO);

                        //ORELLANA:ORIGINAL 
			dwr = getParteDeudor(elementId, idTramite, idPersona, "0",isInscripcion);
                        //dwr = getParteDeudor(elementId, idTramite, idPersona, idPersonaModificar,isInscripcion);
                        
                        
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dwr;
	}

	public MessageDwr modificaParteAcreedorRepresentado(String elementId,
			String idPersona, String idPersonaModificar, String nombre,
			String apellidoP, String apellidoM, String razonSocial,
			String nacionalidad, String tipoPersona, String folioElectronico,
			String rfc, String idColonia, String idLocalidad, String calle,
			String numExterior, String numInterior, String correoElectronico,
			String telefono, String extencion, String curpdoc,
			String domicilioUno, String domicilioDos, String poblacion,
			String zonaPostal, String idPaisResidencia, String tipo, Boolean acreedorInscribe, String nif) {

		String cadena = "";
		MessageDwr dwr = new MessageDwr();
		MyLogger.Logger
				.log(Level.INFO,
						"RUG-DWRAction:ParteDWRAction.modificaParteAcreedorRepresentado------Entramos al Metodo ::::");
		try {

			AltaParteTO altaParteTO = new AltaParteTO();
			altaParteTO.setDomicilioUno(domicilioUno);
			altaParteTO.setDomicilioDos(domicilioDos);
			altaParteTO.setPoblacion(poblacion);
			altaParteTO.setZonaPostal(zonaPostal);
			altaParteTO.setIdParte(4);
			altaParteTO.setIdTramite(0);
			altaParteTO.setFolioMercantil(folioElectronico);

			altaParteTO.setRfc(rfc);
			altaParteTO.setNombre(nombre);
			altaParteTO.setApellidoMaterno(apellidoM);
			altaParteTO.setApellidoPaterno(apellidoP);
			altaParteTO.setRazonSocial(razonSocial);
			altaParteTO.setIdNacionalidad(Integer.valueOf(nacionalidad));
			altaParteTO.setTipoPersona(tipoPersona);
			altaParteTO.setHayDomicilio("V");
			MyLogger.Logger.log(Level.INFO, "Nacionalidad dentro de modifica :"
					+ nacionalidad);
			altaParteTO.setIdColonia(Integer.valueOf(idColonia));
			altaParteTO.setIdLocalidad(Integer.valueOf(idLocalidad));
			altaParteTO.setCalle(calle);
			altaParteTO.setNumeroExterior(numExterior);
			altaParteTO.setNumeroInterior(numInterior);
			altaParteTO.setIdPersona(Integer.valueOf(idPersonaModificar));
			altaParteTO.setIdUsuario(Integer.valueOf(idPersona));
			altaParteTO.setCorreoElectronico(correoElectronico);
			altaParteTO.setTelefono(telefono);
			altaParteTO.setCurp(curpdoc);
			altaParteTO.setExtencion(extencion);
			altaParteTO.setIdPaisResidencia(Integer.valueOf(idPaisResidencia));
			altaParteTO.setTipo(tipo);
			altaParteTO.setAcreedorInscribe(acreedorInscribe);
			altaParteTO.setNif(nif);
			MyLogger.Logger
					.log(Level.WARNING,
							"RUG-DWRAction:ParteDWRAction.modificaParteAcreedorRepresentado------Intentamos modificar al acreedor ::::");
			
			PlSql personaExtranjeraFisica = acreedoresService.modificarAcreedorRepresentado(altaParteTO);
			MyLogger.Logger.log(Level.INFO, personaExtranjeraFisica.getResFolio());
			
			if (personaExtranjeraFisica.getIntPl() == 0) {
				MyLogger.Logger.log(Level.WARNING,"RUG-Action:Modificacion ::::");
				
				if(acreedorInscribe){
					cadena = " <script languaje='javascript'> alert('El folio electronico :"+personaExtranjeraFisica.getResFolio()+"'); </script>";
				}else{
					cadena = " <script languaje='javascript'> alert('El registro fue realizado correctamente.'); </script>";
				}
				dwr = getParteAcreedorRepresentado(elementId, idPersona, "0", " ");
			} else {
				MyLogger.Logger.log(Level.WARNING,"RUG-Action:ParteDwrAction.guardaParteAcreedorRepresentado------Sucedio un error en modificar persona ::::");
				cadena = " <script languaje='javascript'> alert('Error : "+personaExtranjeraFisica.getStrPl()+"'); </script>";
				dwr = getParteAcreedorRepresentado(elementId, idPersona, "0", " ");
			}
			
		} catch (Exception e) {
			MyLogger.Logger.log(Level.WARNING,"RUG-DWRAction:ParteDWRAction.modificaParteAcreedorRepresentado------ Sucedio una exception en el metodo ::::");
			e.printStackTrace();
		}

		MyLogger.Logger.log(Level.INFO, "finalizo correctamente");
		dwr.setMessage(dwr.getMessage() + cadena);
//		dwr = getParteAcreedorRepresentado(elementId, idPersona, "0", " ");
		return dwr;
	}

	public MessageDwr modificaParteAcreedor(String elementId, String idTramite,
			String idPersona, String idPersonaModificar, String isInscripcion, String nombre,
			String apellidoP, String apellidoM, String razonSocial,
			String nacionalidad, String tipoPersona, String folioElectronico,
			String dpi, String idColonia, String idLocalidad, String calle,
			String numExterior, String numInterior, String correoElectronico,
			String telefono, String nit, String curpdoc, String inscrita, 
			String folio, String libro, String direccion, String edad, 
			String estadoCivil, String profesion) {
		MessageDwr dwr;
		MyLogger.Logger.log(Level.INFO, "entro");
		try {
			MyLogger.Logger.log(Level.INFO, "entro try con : " + razonSocial);
			AltaParteTO altaParteTO = new AltaParteTO();
			altaParteTO.setIdParte(3);
			altaParteTO.setIdTramite(Integer.valueOf(idTramite));
			altaParteTO.setFolioMercantil(folioElectronico);
			altaParteTO.setRfc(nit);
			altaParteTO.setNombre(nombre);
			altaParteTO.setApellidoMaterno(apellidoM);
			altaParteTO.setApellidoPaterno(apellidoP);
			altaParteTO.setRazonSocial(razonSocial);
			altaParteTO.setIdNacionalidad(Integer.valueOf(nacionalidad));
			altaParteTO.setTipoPersona(tipoPersona);
			altaParteTO.setHayDomicilio("V");
			altaParteTO.setCurp(dpi);
			altaParteTO.setIdColonia(Integer.valueOf(idColonia));
			altaParteTO.setIdLocalidad(Integer.valueOf(idLocalidad));
			altaParteTO.setCalle(calle);
			altaParteTO.setNumeroExterior(numExterior);
			altaParteTO.setNumeroInterior(numInterior);
			altaParteTO.setIdPersona(Integer.valueOf(idPersonaModificar));
			altaParteTO.setIdUsuario(Integer.valueOf(idPersona));
			altaParteTO.setCorreoElectronico(correoElectronico);
			altaParteTO.setTelefono(telefono);
			altaParteTO.setExtencion("");
			altaParteTO.setNif(nit);
			//campos nuevos
			altaParteTO.setInscrita(inscrita);
			altaParteTO.setFolioInscrito(folio);
			altaParteTO.setLibroInscrito(libro);
			altaParteTO.setUbicacionInscrito(direccion);
			altaParteTO.setEdad(edad);
			altaParteTO.setEstadoCivil(estadoCivil);
			altaParteTO.setProfesion(profesion);
			
			AltaParteDAO altaParteDAO = new AltaParteDAO();
			altaParteDAO.actualizaParte(altaParteTO);
		} catch (Exception e) {
			MyLogger.Logger.log(Level.INFO, "entro catch 2");
			e.printStackTrace();
		}

		MyLogger.Logger.log(Level.INFO, "finalizo correctamente");
		dwr = getParteAcreedor(elementId, idTramite, idPersona, "0", isInscripcion);
		return dwr;
	}

	public MessageDwr modificaParteOtorgante(String elementId, String idTramite,
			String idPersona, String idPersonaModificar, String isInscripcion,
			String nombre, String apellidoP, String apellidoM, String dpi,
			String razonSocial, String nacionalidad, String inscrita, String folio, 
			String libro, String direccion, String nit, String edad, 
			String estadoCivil, String profesion, String telefono, String correo, 
			String residencia, String domicilio, String tipoPersona, String isMultiple) {
		
		MessageDwr dwr = new MessageDwr();
		
		try {
			AltaParteTO altaParteTO = new AltaParteTO();
			altaParteTO.setIdParte(1);

			altaParteTO.setIdTramite(Integer.valueOf(idTramite));
			altaParteTO.setRazonSocial(razonSocial);
			altaParteTO.setTipoPersona(tipoPersona);
			altaParteTO.setNombre(nombre);
			altaParteTO.setApellidoPaterno(apellidoP);
			altaParteTO.setApellidoMaterno(apellidoM);
			altaParteTO.setRfc(nit);
			altaParteTO.setCurp(dpi);
			altaParteTO.setHayDomicilio("V");
			altaParteTO.setIdNacionalidad(Integer.valueOf(nacionalidad));
			altaParteTO.setIdUsuario(Integer.valueOf(idPersona));
			altaParteTO.setIdPersona(Integer.valueOf(idPersonaModificar));
			//Campos Nuevos
			altaParteTO.setInscrita(inscrita);
			altaParteTO.setFolioInscrito(folio);
			altaParteTO.setLibroInscrito(libro);
			altaParteTO.setUbicacionInscrito(direccion);
			altaParteTO.setExtencion("");
			altaParteTO.setNif(nit);
			altaParteTO.setEdad(edad);
			altaParteTO.setEstadoCivil(estadoCivil);
			altaParteTO.setProfesion(profesion);
			altaParteTO.setTelefono(telefono);
			altaParteTO.setCorreoElectronico(correo);
			altaParteTO.setIdPaisResidencia(Integer.valueOf(residencia));
			altaParteTO.setDomicilioUno(domicilio);

			AltaParteDAO altaParteDAO = new AltaParteDAO();
			altaParteDAO.actualizaParte(altaParteTO);
	
			dwr = getParteOtorgante(elementId, idTramite, idPersona, "0", isInscripcion,
					isMultiple);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dwr;
	}

	// ------------------------------------hola
	public MessageDwr eliminaAcreedorRepresentado(String idPersona, String idPersonaModificar) {
		MessageDwr dwr = new MessageDwr();
		String cadena = "";
		if (acreedoresService.tieneUsuarios(Integer.valueOf(idPersonaModificar), Integer.valueOf(idPersona))) {
			MyLogger.Logger.log(Level.INFO, "############## tenia acredores");
			cadena = " <script> alert('No se puede desvincular de un acreedor con usuarios activos. Eliminelos e intente de nuevo.'); </script>  ";
			dwr.setMessage("No se puede desvincular de un acreedor con usuarios activos. Eliminelos e intente de nuevo.");
			dwr.setCodeError(-1002);
		} else {
			if (acreedoresService.tieneTramites(Integer.valueOf(idPersonaModificar), Integer.valueOf(idPersona))) {
				MyLogger.Logger.log(Level.INFO, "############### Tiene tramites");
				cadena = " <script> alert('No es posible desvincularse de este acreedor porque tiene tramites pendientes. Complete los tramites e intente de nuevo.') </script>";
				dwr.setMessage("No es posible desvincularse de este acreedor porque tiene tramites pendientes. Complete los tramites e intente de nuevo.");
				dwr.setCodeError(-1003);
			} else {
				MyLogger.Logger.log(Level.INFO, "############## no tenia acreedores ni tramites");
				acreedoresService.bajaAcreedor(Integer.valueOf(idPersona), Integer.valueOf(idPersonaModificar));
				dwr.setMessage("El acreedor ha sido desvinculado.");
				dwr.setData(idPersonaModificar);
				dwr.setCodeError(0);
			}
		}
		//return getParteAcreedorRepresentado(elementId, idPersona, "0", cadena);
		return dwr;
	}

	public MessageDwr eliminaTramiteMasivo(String elementId, String idPersona,
			String idTramiteFirma) {
		String cadena = "";
		MyLogger.Logger.log(Level.INFO,
				"############## no tenia acreedores ni tramites");
		acreedoresService.bajaTramiteMasivo(Integer.valueOf(idTramiteFirma),
				Integer.valueOf(idPersona));
		return getParteAcreedorRepresentado(elementId, idPersona, "0", cadena);
	}
	
	public MessageDwr eliminaParteBien(String elementId, String idTramite, String idTramiteGar) {
		MessageDwr dwr = new MessageDwr();
		try {
			inscripcionService.eliminarBien(new Integer(idTramiteGar));
			dwr = getParteBienes(elementId, idTramite);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dwr;
	}
	
	public MessageDwr modificaParteBien(String elementId, String idTramite, String mdDescripcion, String idTipo, String mdIdentificador,
			String mdIdentificador1, String mdIdentificador2, String idTramiteGar) {
		MessageDwr dwr = new MessageDwr();
		try {
			BienEspecialTO bienEspecialTO = new BienEspecialTO();
			bienEspecialTO.setIdTramite(new Integer(idTramite));
			bienEspecialTO.setIdTramiteGarantia(new Integer(idTramiteGar));
			bienEspecialTO.setDescripcion(mdDescripcion);
			bienEspecialTO.setTipoBien(new Integer(idTipo));
			
			if(idTipo.equalsIgnoreCase("1")) {//vehiculos			
				/*if(!mdIdentificador.equalsIgnoreCase("")&&!mdIdentificador1.equalsIgnoreCase("")) {
					bienEspecialTO.setIdentificador(mdIdentificador+"-"+mdIdentificador1);
					bienEspecialTO.setTipoIdentificador(1); //por placa y uso
				} else {
					bienEspecialTO.setIdentificador(mdIdentificador2);
					bienEspecialTO.setTipoIdentificador(2); //VIN
				}*/
                if(!mdIdentificador2.equalsIgnoreCase("")) {
					bienEspecialTO.setIdentificador(mdIdentificador2);
					bienEspecialTO.setTipoIdentificador(2); //VIN
				} else if(!mdIdentificador.equalsIgnoreCase("")&&!mdIdentificador1.equalsIgnoreCase("")) {					
					bienEspecialTO.setIdentificador(mdIdentificador+"-"+mdIdentificador1);
					bienEspecialTO.setTipoIdentificador(1); //por placa y uso
				}
			} else if(idTipo.equalsIgnoreCase("2")) {//facturas
				bienEspecialTO.setIdentificador(mdIdentificador2);
				bienEspecialTO.setTipoIdentificador(3); // no factura
			
			} else {
				bienEspecialTO.setIdentificador(mdIdentificador2);
				bienEspecialTO.setTipoIdentificador(4); // no serie
			}

//			bienEspecialTO.setSerie(mdIdentificador3);
						
			inscripcionService.modificarBien(bienEspecialTO);
			dwr = getParteBienes(elementId, idTramite);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dwr;
	}
          
        public MessageDwr modificaParteBienV2(String elementId, String idTramite, String mdDescripcion, String idTipo, String mdIdentificador,
			String mdIdentificador1, String mdIdentificador2, String idTramiteGar, String mdSerie) {
		MessageDwr dwr = new MessageDwr();
		try {
			BienEspecialTO bienEspecialTO = new BienEspecialTO();
			bienEspecialTO.setIdTramite(new Integer(idTramite));
			bienEspecialTO.setIdTramiteGarantia(new Integer(idTramiteGar));
			bienEspecialTO.setDescripcion(mdDescripcion);
			bienEspecialTO.setTipoBien(new Integer(idTipo));
			if(idTipo.equalsIgnoreCase("1")) {//vehiculos			
				if(!mdIdentificador.equalsIgnoreCase("")&&!mdIdentificador1.equalsIgnoreCase("")) {
					bienEspecialTO.setIdentificador(mdIdentificador+"-"+mdIdentificador1);
					bienEspecialTO.setTipoIdentificador(1); //por placa y uso
				} else {
					bienEspecialTO.setIdentificador(mdIdentificador2);
					bienEspecialTO.setTipoIdentificador(2); //VIN
				}
			} else if(idTipo.equalsIgnoreCase("2")) {//facturas
				bienEspecialTO.setIdentificador(mdIdentificador2);
				bienEspecialTO.setTipoIdentificador(3); // no factura
                                bienEspecialTO.setSerie(mdSerie);
			} else {
				bienEspecialTO.setIdentificador(mdIdentificador2);
				bienEspecialTO.setTipoIdentificador(4); // no serie
			}

//			bienEspecialTO.setSerie(mdIdentificador3);
						
			inscripcionService.modificarBien(bienEspecialTO);
			dwr = getParteBienes(elementId, idTramite);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dwr;
	}
        
         public MessageDwr modificaParteBienFactoraje(String elementId, String idTramite, String mdDescripcion, String idTipo, String mdIdentificador,
			String mdIdentificador1, String mdIdentificador2, String idTramiteGar, String mdSerie) {
		MessageDwr dwr = new MessageDwr();
		try {
			BienEspecialTO bienEspecialTO = new BienEspecialTO();
			bienEspecialTO.setIdTramite(new Integer(idTramite));
			bienEspecialTO.setIdTramiteGarantia(new Integer(idTramiteGar));
			bienEspecialTO.setDescripcion(mdDescripcion);
			bienEspecialTO.setTipoBien(new Integer(idTipo));
			if(idTipo.equalsIgnoreCase("1")) {//vehiculos			
				if(!mdIdentificador.equalsIgnoreCase("")&&!mdIdentificador1.equalsIgnoreCase("")) {
					bienEspecialTO.setIdentificador(mdIdentificador+"-"+mdIdentificador1);
					bienEspecialTO.setTipoIdentificador(1); //por placa y uso
				} else {
					bienEspecialTO.setIdentificador(mdIdentificador2);
					bienEspecialTO.setTipoIdentificador(2); //VIN
				}
			} else if(idTipo.equalsIgnoreCase("2")) {//facturas
				bienEspecialTO.setIdentificador(mdIdentificador2);
				bienEspecialTO.setTipoIdentificador(3); // no factura
                                bienEspecialTO.setSerie(mdSerie);
			} else {
				bienEspecialTO.setIdentificador(mdIdentificador2);
				bienEspecialTO.setTipoIdentificador(4); // no serie
			}

//			bienEspecialTO.setSerie(mdIdentificador3);
						
			inscripcionService.modificarBien(bienEspecialTO);
			dwr = getParteBienes(elementId, idTramite);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dwr;
	}

	public MessageDwr eliminaParte(String elementId, String idTramite,
			String idPersona, String idPersonaModificar, String idParte,
			String isInscripcion, String isMultiple) {
		MessageDwr dwr = null;
		try {
			MyLogger.Logger.log(Level.INFO, "eliminaParte   isInscripcion "
					+ isInscripcion);
			AltaParteDAO altaParteDAO = new AltaParteDAO();
			altaParteDAO.bajaParte(Integer.valueOf(idTramite),
					Integer.valueOf(idPersonaModificar),
					Integer.valueOf(idParte), "F");
			int idParteInt = Integer.valueOf(idParte);
			// try {
			// if (Integer.valueOf(isInscripcion).intValue() == 2) {
			// dwr = getParteComerciante(elementId, idTramite, idPersona,
			// idPersonaModificar);
			// }
			// } catch (Exception e) {
			// MyLogger.Logger.log(Level.INFO, "Error en isInscripcion");
			// }
			switch (idParteInt) {
			case 1:
				dwr = getParteOtorgante(elementId, idTramite, idPersona, "0",
						isInscripcion, isMultiple);
				break;
			case 2:
				dwr = getParteDeudor(elementId, idTramite, idPersona, "0",
						isInscripcion);
				break;
			case 3:
				dwr = getParteAcreedor(elementId, idTramite, idPersona, "0", isInscripcion);
				break;

			default:
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return dwr;
	}

	public MessageDwr insertMismoDeudor(String elementId, String idTramite,
			String idPersona, String idPersonaModificar, String isInscripcion) {
		AltaParteDAO altaParteDAO = new AltaParteDAO();

		List<OtorganteTO> otorganteTO = altaParteDAO
				.getOtorganteByInscripcion(Integer.valueOf(idTramite));
		if (otorganteTO.size() > 0) {
			for (OtorganteTO otorgante : otorganteTO) {
				boolean ingreso = altaParteDAO.insertElMismo(
						Integer.valueOf(idTramite), otorgante.getIdOtorgante());
				if (ingreso) {
					MyLogger.Logger.log(Level.INFO, "Fue exitoso");
				} else {
					MyLogger.Logger.log(Level.INFO, "No fue exitoso");
				}
			}

		} else {
			MyLogger.Logger.log(Level.INFO, "No encontro Otorgante");
		}
		return getParteDeudor(elementId, idTramite, idPersona,
				idPersonaModificar, isInscripcion);
	}

	public String notNull(String cadena) {
		if (cadena == null) {
			return "";
		} else {
			return cadena;
		}
	}

	// Html

	// Html Deudor

	private StringBuffer writeBusquedaFolioelectronico(String folioElectronico,	String idTramite, String idPersona, String isInscripcion, String isMultiple) {
		StringBuffer sb = new StringBuffer();
		AltaParteDAO altaParteDAO = new AltaParteDAO();
		OtorganteTO otorganteTO = altaParteDAO.getOtorganteFisicoByFolioElectronico(folioElectronico);
		sb.append(" <table> ");
		if (otorganteTO == null) {
			sb.append("<tr> <td> El Folio Electronico no existe </td> </tr>");
		} else {
			sb.append(" <table> <tr> <td>  ");
			sb.append(" <tr> <td style=\"padding-left: 14px\"> Nombre : <span> " + notNull(otorganteTO.getNombreCompleto()) + " </span> </td> </tr> ");
			sb.append(" <tr> <td style=\"padding-left: 14px\"> Nacionalidad : <span> " + notNull(otorganteTO.getDescNacionalidad()) + " </span> </td> </tr> ");
			MyLogger.Logger.log(Level.INFO, "aaa {" + otorganteTO.getIdNacionalidad() + "}");
			if (otorganteTO.getIdNacionalidad().intValue() == 1) {
				sb.append(" <tr> <td style=\"padding-left: 14px\"> CURP 2: <span> " + notNull(otorganteTO.getCurp()) + " </span> </td> </tr> ");
			}
			sb.append("		<tr><td id=\"rfcOtFolIDMoral\">");
			sb.append(" <table style=\"visibility: visible; display: block\">");
			sb.append("			<tr id=\"tbejrfcOtFoltitMoral\">");
			sb.append("				<td style=\"padding-left: 20px;\" colspan=\"2\" class=\"tituloHeader2\" align=\"left\">" );
			sb.append("					* RFC :");
			sb.append("				</td>");
			sb.append("			</tr>");
			sb.append("			<tr>");
			sb.append("				<td style=\"padding-left: 33px;\">");
			sb.append("	<dl style=\"width: 120px\">");
			sb.append("	<dd style=\"width: 120px\">");
			sb.append("	 <input type=\"text\" maxlength=\"14\" value=\"" + notNull(otorganteTO.getRfc()) 
					+ "\" name=\"rfcOtFolMoral\" id=\"rfcOtFolMoral\" onkeyup=\"this.value = this.value.toUpperCase()\" maxlength=\"16\" >");
			sb.append(" <span class=\"hint\"> <div class=\"cerrar\">" +
					" <a onclick=\"this.parentNode.getElementsByTagName('span')[0].style.display = 'none';\" style=\"cursor: pointer;\">x</a> </div> <div class=\"contenido\"> " +
					"N煤mero de registro asignado por el Registro Federal de Contribuyentes. </div><span class=\"hint-pointer\">&nbsp;</span></span>");
			sb.append("	</dd>");
			sb.append("	</dl>");
			sb.append("					</td>");
			sb.append("			</tr>						");
			sb.append("		</table>");
			sb.append("<table id=\"tbejrfcOtFolMoral\">");
			sb.append("		<tr> ");
			sb.append("			<td style=\"padding-left:34px;\" class=\"textoEjemplo\">Ejemplo&nbsp;</td><td>");
			sb.append("				<table>");
			sb.append("					<tr>");
			sb.append("						<td class=\"textoGris\"  align=\"justify\">b) RFC: DEFT123409RT2</td>");
			sb.append("					</tr>");
			sb.append("				</table>");
			sb.append("			</td> ");
			sb.append("		</tr> ");
			sb.append("</table>");
			sb.append("		</td></tr>");
			sb.append("<table>");
			sb.append(" <tr> <td  id=\"rfcOtFolValidarMoral\"  style= \"padding-left: 39px;\" \"visibility: hidden; display: none\" >");
			sb.append("<input  type=\"checkbox\"  name=\"rfcOtFolValidar1Moral\" id=\"rfcOtFolValidar1Moral\" onclick=\"checkboxValRfcOtFolMoral()\">");
			sb.append("<td id=\"txrfcOtFolMoral\" align=\"justify\">No cuenta con RFC</td>");
			sb.append("</td> </tr>");		
			sb.append("</table>");
			sb.append("<br>");
			sb.append(" <tr> <td style=\"padding-left: 14px\"> <input id=\"butonIDAceptar\" class=\"boton_rug\" value=\"Aceptar\" type=\"button\" onclick=\"copiaParteOtorgante( ");
			sb.append(idTramite);
			sb.append(",");
			sb.append(idPersona);
			sb.append(",'");
			sb.append(isMultiple);
			sb.append("','");
			sb.append(isInscripcion);
			sb.append("')\"/> </td> </tr> </table> <script> activarBoton(); </script>  </td> </tr> ");
		}
		sb.append(" </table> ");
		return sb;
	}

	/*************************************/

	@SuppressWarnings("unused")
	private StringBuffer writeBusquedaFolioelectronicoMoral(String folioElectronico, String idTramite, String idPersona, String isInscripcion, String isMultiple) {
		StringBuffer sb = new StringBuffer();
		AltaParteDAO altaParteDAO = new AltaParteDAO();
		OtorganteTO otorganteTO = altaParteDAO.getOtorganteMoralByFolioElectronico(folioElectronico);
		sb.append(" <table> ");
		if (otorganteTO == null) {
			sb.append("<tr> <td> El Folio Electronico no existe </td> </tr>");
		} else {
			sb.append(" <table> <tr> <td>  ");
			sb.append(" <tr> <td style=\"padding-left: 14px\"> Nombre Sociedad Mercantil: <span> " + notNull(otorganteTO.getRazon()) + " </span> </td> </tr> ");
			sb.append(" <tr> <td style=\"padding-left: 14px\"> Nacionalidad : <span> " + notNull(otorganteTO.getDescNacionalidad()) + " </span> </td> </tr> ");
			MyLogger.Logger.log(Level.INFO, "aaa {" + otorganteTO.getIdNacionalidad() + "}");
				sb.append("		<tr><td id=\"rfcOtFolIDMoral\">");
				sb.append(" <table style=\"visibility: visible; display: block\">");
				sb.append("			<tr id=\"tbejrfcOtFoltitMoral\">");
				sb.append("				<td style=\"padding-left: 20px;\" colspan=\"2\" class=\"tituloHeader2\" align=\"left\">" );
				sb.append("					* RFC :");
				sb.append("				</td>");
				sb.append("			</tr>");
				sb.append("			<tr>");
				sb.append("				<td style=\"padding-left: 33px;\">");
				sb.append("	<dl style=\"width: 120px\">");
				sb.append("	<dd style=\"width: 120px\">");
				sb.append("	 <input type=\"text\" maxlength=\"14\" value=\"" + notNull(otorganteTO.getRfc()) 
						+ "\" name=\"rfcOtFolMoral\" id=\"rfcOtFolMoral\" onkeyup=\"this.value = this.value.toUpperCase()\" maxlength=\"16\" >");
				sb.append(" <span class=\"hint\"> <div class=\"cerrar\">" +
						" <a onclick=\"this.parentNode.getElementsByTagName('span')[0].style.display = 'none';\" style=\"cursor: pointer;\">x</a> </div> <div class=\"contenido\"> " +
						"N煤mero de registro asignado por el Registro Federal de Contribuyentes. </div><span class=\"hint-pointer\">&nbsp;</span></span>");
				sb.append("	</dd>");
				sb.append("	</dl>");
				sb.append("					</td>");
				sb.append("			</tr>						");
				sb.append("		</table>");
				sb.append("<table id=\"tbejrfcOtFolMoral\">");
				sb.append("		<tr> ");
				sb.append("			<td style=\"padding-left:34px;\" class=\"textoEjemplo\">Ejemplo&nbsp;</td><td>");
				sb.append("				<table>");
				sb.append("					<tr>");
				sb.append("						<td class=\"textoGris\"  align=\"justify\">a) RFC: RFT632393H21</td>");
				sb.append("					</tr>");
				sb.append("				</table>");
				sb.append("			</td> ");
				sb.append("		</tr> ");
				sb.append("</table>");
				sb.append("		</td></tr>");
				sb.append("<table>");
				sb.append(" <tr> <td  id=\"rfcOtFolValidarMoral\"  style= \"padding-left: 39px;\" \"visibility: hidden; display: none\" >");
				sb.append("<input  type=\"checkbox\"  name=\"rfcOtFolValidar1Moral\" id=\"rfcOtFolValidar1Moral\" onclick=\"checkboxValRfcOtFolMoral()\">");
				sb.append("<td id=\"txrfcOtFolMoral\" align=\"justify\">No cuenta con RFC</td>");
				sb.append("</td> </tr>");		
				sb.append("</table>");
				sb.append("<br>");
				sb.append(" <tr> <td style=\"padding-left: 14px\"> <input id=\"butonIDAceptar\" class=\"boton_rug\" value=\"Aceptar\" type=\"button\" onclick=\"copiaParteOtorgante( ");
				sb.append(idTramite);
				sb.append(",");
				sb.append(idPersona);
				sb.append(",'");
				sb.append(isMultiple);
				sb.append("','");
				sb.append(isInscripcion);
				sb.append("')\"/> </td> </tr> </table> <script> activarBoton(); </script>  </td> </tr> ");	
		}
		sb.append(" </table> ");
		return sb;
	}

	/*************************************/

	private StringBuffer writeFormulario(String elementId, String idTramite,
			String idPersona, AltaParteTO altaParteTO, String isInscripcion,
			String isMultiple) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("<div class=\"row\">");
		sb.append("<a onclick=\"agregarNuevoO()\" style=\"cursor: pointer;\" class=\"tituloHeader2\"> + Agregar Persona</a>");
		sb.append("</div>");
		if (altaParteTO.getIdPersona() == null) {
			sb.append("<div class=\"row\" style=\"visibility: hidden; display: none\" id=\"agreNuevoO\">");
		} else {
			if (altaParteTO.getIdPersona() == 0) {				
				sb.append("<div class=\"row\" style=\"visibility: hidden; display: none\" id=\"agreNuevoO\">");
			} else {				
				sb.append("<div class=\"row\" id=\"agreNuevoO\">");
			}
		}
		
		List<TipoPersona> lista = new ArrayList<TipoPersona>();
		TipoPersona persona = new TipoPersona();
		persona.setId("PM");
		persona.setDesc("Persona Jur&iacute;dica");
		lista.add(persona);
		persona = new TipoPersona();
		persona.setId("PF");
		persona.setDesc("Persona Individual");
		lista.add(persona);
		
		NacionalidadDAO dao = new NacionalidadDAO();
		List<NacionalidadTO> listaNacionalidad = dao.getNacionalidades();
		
		sb.append(writeTiposPersona(lista, altaParteTO));
		sb.append(writeNacionaliad(listaNacionalidad, altaParteTO));
		
		if(altaParteTO.getTipoPersona()!=null && altaParteTO.getTipoPersona().equalsIgnoreCase("PM"))
			sb.append("<div id=\"personaMoralTR\">");
		else 
			sb.append("<div id=\"personaMoralTR\" style=\"visibility: hidden; display: none\">");		
		sb.append(writePersonaMoral(altaParteTO, isInscripcion));
		sb.append("</div>");
		
		if(altaParteTO.getTipoPersona()!=null && altaParteTO.getTipoPersona().equalsIgnoreCase("PM"))
			sb.append("<div id=\"personaFisicaTR\" style=\"visibility: hidden; display: none\">");
		else
			sb.append("<div id=\"personaFisicaTR\">");
		sb.append(writePersonaFisica(altaParteTO));
		sb.append("</div>");

		/** Telefono*/
		sb.append(writeTelefonoCorreoO(altaParteTO));
		sb.append("<br />");
		
		sb.append(writeCalleExtranjeraO(altaParteTO));
		
		if (altaParteTO.getIdPersona() == 0) {
			sb.append("<center><div class='row'><input type=\"button\" class=\"btn btn-large waves-effect indigo\" value=\"Aceptar\" onclick=\"guardaParteOtorgante('"
					+ elementId
					+ "',"
					+ idTramite
					+ ","
					+ idPersona
					+ ",'0','"
					+ isInscripcion
					+ "','"
					+ isMultiple
					+ "')\"/>  <input type=\"button\" class=\"btn btn-large waves-effect indigo\" value=\"Ocultar\" onclick=\"ocultarOT()\"/></div></center>");
		} else {
			sb.append("<center><div class='row'><input type=\"button\" class=\"btn btn-large waves-effect indigo\" value=\"Aceptar\" onclick=\"modificaParteOtorgante('"
					+ elementId
					+ "',"
					+ idTramite
					+ ","
					+ idPersona
					+ ","
					+ altaParteTO.getIdPersona()
					+ ",'"
					+ isInscripcion
					+ "','"
					+ isMultiple
					+ "')\"/>  <input type=\"button\" class=\"btn btn-large waves-effect indigo\" value=\"Ocultar\" onclick=\"ocultarOT()\"/></div></center>");
		}

		sb.append("</div></div>");
		
		return sb;
	}

	// private StringBuffer writeFormulario(String elementId, String idTramite,
	// String idPersona, AltaParteTO altaParteTO) {
	// StringBuffer sb = new StringBuffer();
	// List<TipoPersona> lista = new ArrayList<TipoPersona>();
	// TipoPersona persona = new TipoPersona();
	// MyLogger.Logger.log(Level.INFO, "entro a otro lado");
	// persona.setId("PM");
	// persona.setDesc("Persona Moral");
	// lista.add(persona);
	// persona = new TipoPersona();
	// persona.setId("PF");
	// persona.setDesc("Persona Fisica");
	// lista.add(persona);
	// NacionalidadDAO dao = new NacionalidadDAO();
	// List<NacionalidadTO> listaNacionalidad = dao.getNacionalidades();
	// sb.append("<tr> <td> ");
	// sb.append(" <table>");
	// sb.append(writeTiposPersona(lista, altaParteTO));
	// sb.append(writeNacionaliad(listaNacionalidad, altaParteTO));
	// sb.append("	<tr id=\"personaMoralTR\"><td>");
	// sb.append(writePersonaMoral(altaParteTO));
	// sb.append("	</td></tr>");
	// sb.append("	<tr id=\"personaFisicaTR\" style=\"visibility: hidden; display: none\"><td>");
	// sb.append(writePersonaFisica(altaParteTO));
	// sb.append("		</td></tr>");
	// sb.append("		<tr id=\"folioElectronicoTR\"><td>");
	// sb.append(writeFolioElectronico(altaParteTO));
	// sb.append("		</td></tr>");
	// sb.append("		<tr><td id=\"rfcID\">");
	// sb.append(writeRFC(altaParteTO));
	// sb.append("		</td></tr>");
	// sb.append("		<tr><td id=\"docExtranjeroTR\" style=\"visibility: hidden; display: none\">");
	// sb.append(writeDocExtranjero(altaParteTO));
	// sb.append("		</td></tr>");
	// MyLogger.Logger.log(Level.INFO, "'" + elementId + "'," + idTramite + ","
	// + idPersona);
	// sb.append("		<tr>");
	// if (altaParteTO.getIdPersona() == 0) {
	// sb.append("			<td><input class=\"boton_rug\" value=\"Aceptar\" type=\"button\" onclick=\"guardaParteComerciante('"
	// + elementId
	// + "',"
	// + idTramite
	// + ","
	// + idPersona
	// +
	// ",'0')\"/> <script>   document.getElementById('nacionalidadA').value = 1; </script>  </td>");
	// } else {
	// sb.append("			<td><input class=\"boton_rug\" value=\"Aceptar\" type=\"button\" onclick=\"modificaParteComerciante('"
	// + elementId
	// + "',"
	// + idTramite
	// + ","
	// + idPersona
	// + ","
	// + altaParteTO.getIdPersona() + ")\"/></td>");
	// }
	// sb.append("		</tr>");
	// sb.append("	</table>");
	// if (altaParteTO.getIdNacionalidad() != null) {
	// sb.append("	</table> <script>  document.getElementById('nacionalidad').value = "
	// + altaParteTO.getIdNacionalidad()
	// + ";  cambiaNacionalidad(); </script> ");
	//
	// } else {
	// sb.append("	</table> <script>  document.getElementById('nacionalidad').value = 1;  cambiaNacionalidad(); </script> ");
	//
	// }
	// sb.append(" </td> </tr> </table>  <script>  cambiaNacionalidad(); </script>");
	// if (altaParteTO.getTipoPersona() != null) {
	// String tipoPersona = "PM";
	// MyLogger.Logger.log(Level.INFO, "{" + altaParteTO.getTipoPersona() +
	// "}");
	// if (altaParteTO.getTipoPersona().equals("Persona Fisica")) {
	// tipoPersona = "PF";
	// }
	// sb.append(" <script> document.getElementById('tipoPersona').value = '"
	// + tipoPersona
	// + "'; document.getElementById('nacionalidad').value = "
	// + altaParteTO.getIdNacionalidad() + "; </script>");
	// }
	//
	// return sb;
	// }

	private StringBuffer writeFolioElectronico(AltaParteTO altaParteTO) {
		StringBuffer sb = new StringBuffer();
		sb.append("<table>");

		sb.append("<script type=\"text/javascript\">");
		sb.append("    $(function () {");
		sb.append("        $('.ayuda').ayuda({");
		sb.append("            url: '/Rug/comun/publico/help.do',");
		sb.append("            ico: '/Rug/resources/imgs/documentinfo.png',");
		sb.append("            width: 510,");
		sb.append("            height: 400");
		sb.append("        })    ");
		sb.append("        })    ");
		sb.append("</script> ");

		sb.append("			<tr><td colspan=\"2\" class=\"tituloHeader2\" align=\"left\">");

		sb.append("             <span id=\"anotacionFolioElectronico\" class=\"ayuda\">Folio Electr贸nico : </span></td>");

		sb.append("			</tr>");

		sb.append("			<tr>"
				+ "					<td> "
				+ "						<table>"
				+ "							<tr>"
				+ "								<td style=\"padding-left: 11px;\" class=\"texto_azul\" width=\"18px\">*</td>"
				+ "								 <td>");
		sb.append("									<dl style=\"width: 260px\">");
		sb.append("										<dd style=\"width: 120px\">");
		sb.append("	 										<input type=\"text\" value=\""
				+ notNull(altaParteTO.getFolioMercantil())
				+ "\" name=\"folioElectronico\" id=\"folioElectronico\" maxlength=\"100\" size=\"50\" >");
		sb.append(" <span class=\"hint\"> <div class=\"cerrar\"> <a onclick=\"this.parentNode.getElementsByTagName('span')[0].style.display = 'none';\" style=\"cursor: pointer;\">x</a> </div> <div class=\"contenido\"> El Folio Electr贸nico es el expediente electr贸nico de un Otorgante en el que se contienen los asientos relativos a Garant铆as Mobiliarias, as铆 como los actos jur铆dicos por los que se constituya un privilegio especial o derecho de retenci贸n sobre bienes muebles a favor de terceros, incluyendo las anotaciones.  Art. 21 fr. XX y 30 bis 1 RRPC. </div><span class=\"hint-pointer\">&nbsp;</span></span>");
		sb.append("	</dd>");
		sb.append("	</dl>");

		sb.append("				</td> </tr> </table> </td> </tr>");
		sb.append("				<td id=\"MensajeFisica\" style=\"visibility: hidden; display: none\">");
		sb.append("				<table style=\"cursor: pointer;\"><tr>");
		sb.append("					<td class=\"ComunicaTexto\" width=\"40\"> Nota : </td>");
		sb.append("					<td class=\"contenidoNota\">Si no cuenta con Folio Electr&oacute;nico, &eacute;ste ser&aacute; generado de forma autom&aacute;tica por el Sistema.</td>");
		sb.append("				</tr></table>");
		sb.append("			</td></tr>");
		sb.append("</table>");

		sb.append("<table>");
		sb.append("		<tr> ");
		sb.append("			<td style=\"padding-left:32px;\" class=\"textoEjemplo\">Ejemplo&nbsp;</td><td>");
		sb.append("				<table>");
		sb.append("					<tr>");
		sb.append("						<td class=\"textoGris\"  align=\"justify\">a) Otorgante Persona F铆sica:  \"R20101026B040\"</td>");
		sb.append("					</tr>");
		sb.append("					<tr>");
		sb.append("						<td class=\"textoGris\" align=\"justify\">b) Otorgante Persona Moral:  \"34011*7\"I</td>");
		sb.append("					</tr>");
		sb.append("				</table>");
		sb.append("			</td> ");
		sb.append("		</tr> ");
		sb.append("</table>");

		return sb;
	}

	private StringBuffer writeDocExtranjero(AltaParteTO altaParteTO) {
		StringBuffer sb = new StringBuffer();
		Constants constants = new Constants();
		MyLogger.Logger.log(
				Level.INFO,
				"writeDocExtranjero-altaParteTO-curpdoc-"
						+ notNull(altaParteTO.getCurp()));
		sb.append("<table>");
		sb.append("			<tr>");
		sb.append("				<td style=\"padding-left: 13px\" colspan=\"2\" class=\"texto_general\" align=\"left\" id=\"curpDocO\"><span");
		sb.append("					class=\"textoGeneralRojo\">CURP :</span></td>");
		sb.append("			</tr>");
		sb.append("			<tr>");
		sb.append("				<td class=\"texto_general\"> <table> <tr><td style=\"padding-left: 13px;\" class=\"texto_azul\" width=\"18px\">*</td> <td>");
		sb.append("	 <input type=\"text\" onkeyup=\"this.value = this.value.toUpperCase()\" value=\""
				+ notNull(altaParteTO.getCurp())
				+ "\" name=\"docExtranjero\" id=\"docExtranjero\" maxlength=\"50\" >");
		sb.append("				</td><td id=\"curpAyudaID\" style=\"visibility: hidden; display: none\"><a href=\"");
		sb.append(constants.getParamValue(Constants.URL_RENAPO_CURP)); // "http://www.renapo.gob.mx/swb/swb/RENAPO/consultacurp"
																		// +
		sb.append("\" target=\"blank\" style=\"color=#4A766E;\" /> Si no la conoce, consulte su CURP </a></td></tr></table></td>");
		sb.append("			</tr>						");
		sb.append("</table>");
		return sb;
	}

	private StringBuffer writeRFC(AltaParteTO altaParteTO) {
		StringBuffer sb = new StringBuffer();

		sb.append("<script type=\"text/javascript\">");
		sb.append("    $(function () {");
		sb.append("        $('.ayudaRFC2').ayuda({");
		sb.append("            url: '/Rug/comun/publico/help.do',");
		sb.append("            ico: '/Rug/resources/imgs/documentinfo.png',");
		sb.append("            width: 505,");
		sb.append("            height: 170");
		sb.append("        })    ");
		sb.append("        })    ");
		sb.append("</script> ");

		sb.append(" <table style=\"visibility: visible; display: block\">");
		sb.append("			<tr id=\"tbejrfctit\">");
		sb.append("				<td style=\"padding-left: 20px;\" colspan=\"2\" class=\"tituloHeader2\" align=\"left\">" );
		sb.append("					* Documento de Identificaci贸n :");
		sb.append("				</td>");
		sb.append("			</tr>");
		sb.append("			<tr>");
		sb.append("				<td style=\"padding-left: 33px;\">");
		sb.append("	<dl style=\"width: 120px\">");
		sb.append("	<dd style=\"width: 120px\">");
		
		sb.append("	 <input type=\"text\" maxlength=\"14\" value=\"" + notNull(altaParteTO.getRfc())
				+ "\" name=\"rfc\" id=\"rfc\" onkeyup=\"this.value = this.value.toUpperCase()\" maxlength=\"16\" >");
		sb.append(" <span class=\"hint\"> <div class=\"cerrar\"> <a onclick=\"this.parentNode.getElementsByTagName('span')[0].style.display = 'none';\" style=\"cursor: pointer;\">x</a> </div> <div class=\"contenido\"> N煤mero de registro asignado por el Registro Federal de Contribuyentes. </div><span class=\"hint-pointer\">&nbsp;</span></span>");
		sb.append("	</dd>");
		sb.append("	</dl>");
		sb.append("					</td>");
		sb.append("			</tr>						");
		sb.append("		</table>");

		sb.append("<table id=\"tbejrfc\">");
		sb.append("		<tr> ");
		sb.append("			<td style=\"padding-left:34px;\" class=\"textoEjemplo\">Ejemplo&nbsp;</td><td>");
		sb.append("				<table>");
		sb.append("					<tr>");
		sb.append("						<td class=\"textoGris\"  align=\"justify\">a) DPI: 10013456789</td>");
		sb.append("					</tr>");
		sb.append("					<tr>");
		sb.append("						<td class=\"textoGris\"  align=\"justify\">b) Pasaporte: H23585858</td>");
		sb.append("					</tr>");
		sb.append("				</table>");
		sb.append("			</td> ");
		sb.append("		</tr> ");
		sb.append("</table>");
		/*sb.append("<table>");
		sb.append(" <tr> <td  id=\"rfcValidar\"  style= \"padding-left: 39px;\" \"visibility: hidden; display: none\" >");
		sb.append("<input  type=\"checkbox\"  name=\"rfcValidar1\" id=\"rfcValidar1\" onclick=\"checkboxValRfc()\">");
		sb.append("<td id=\"txrfc\" align=\"justify\">No cuenta con RFC</td>");
		
		sb.append("</td> </tr>");		
		sb.append("</table>");*/
		
		/** Extendido en **/
		sb.append(" <table style=\"visibility: visible; display: block\">");
		sb.append("			<tr id=\"tbejrfctit\">");
		sb.append("				<td style=\"padding-left: 20px;\" colspan=\"2\" class=\"tituloHeader2\" align=\"left\">" );
		sb.append("					Extendido en :");
		sb.append("				</td>");
		sb.append("			</tr>");
		sb.append("			<tr>");
		sb.append("				<td style=\"padding-left: 33px;\">");
		sb.append("	<dl style=\"width: 120px\">");
		sb.append("	<dd style=\"width: 120px\">");
		
		sb.append("	 <input type=\"text\" maxlength=\"14\" value=\"" + notNull(altaParteTO.getExtencion())
				+ "\" name=\"extendidoEn\" id=\"extendidoEn\" onkeyup=\"this.value = this.value.toUpperCase()\" maxlength=\"16\" >");
		sb.append(" <span class=\"hint\"> <div class=\"cerrar\"> <a onclick=\"this.parentNode.getElementsByTagName('span')[0].style.display = 'none';\" style=\"cursor: pointer;\">x</a> </div> <div class=\"contenido\"> N鷐ero de registro asignado por el Registro Federal de Contribuyentes. </div><span class=\"hint-pointer\">&nbsp;</span></span>");
		sb.append("	</dd>");
		sb.append("	</dl>");
		sb.append("					</td>");
		sb.append("			</tr>						");
		sb.append("		</table>");
		/** Departamento **/
		sb.append(" <table style=\"visibility: visible; display: block\">");
		sb.append("			<tr id=\"tbejrfctit\">");
		sb.append("				<td style=\"padding-left: 20px;\" colspan=\"2\" class=\"tituloHeader2\" align=\"left\">" );
		sb.append("					Departamento :");
		sb.append("				</td>");
		sb.append("			</tr>");
		sb.append("			<tr>");
		sb.append("				<td style=\"padding-left: 33px;\">");
		sb.append("	<dl style=\"width: 120px\">");
		sb.append("	<dd style=\"width: 120px\">");
		
		sb.append("	 <input type=\"text\" maxlength=\"14\" value=\"" + notNull(altaParteTO.getPoblacion())
				+ "\" name=\"departamento\" id=\"departamento\" onkeyup=\"this.value = this.value.toUpperCase()\" maxlength=\"16\" >");
		sb.append(" <span class=\"hint\"> <div class=\"cerrar\"> <a onclick=\"this.parentNode.getElementsByTagName('span')[0].style.display = 'none';\" style=\"cursor: pointer;\">x</a> </div> <div class=\"contenido\"> N鷐ero de registro asignado por el Registro Federal de Contribuyentes. </div><span class=\"hint-pointer\">&nbsp;</span></span>");
		sb.append("	</dd>");
		sb.append("	</dl>");
		sb.append("					</td>");
		sb.append("			</tr>						");
		sb.append("		</table>");

		return sb;
	}
	private StringBuffer writeRFCDeudor(AltaParteTO altaParteTO) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("<script type=\"text/javascript\">");
		sb.append("    $(function () {");
		sb.append("        $('.ayudaRFC2').ayuda({");
		sb.append("            url: '/Rug/comun/publico/help.do',");
		sb.append("            ico: '/Rug/resources/imgs/documentinfo.png',");
		sb.append("            width: 505,");
		sb.append("            height: 170");
		sb.append("        })    ");
		sb.append("        })    ");
		sb.append("</script> ");
		
		
		
		
		
		sb.append(" <table style=\"visibility: visible; display: block\">");
		sb.append("			<tr id=\"tbejrfcDeudtit\">");
		sb.append("				<td style=\"padding-left: 20px;\" colspan=\"2\" class=\"tituloHeader2\" align=\"left\">" );
		sb.append("					* RFC :");
		sb.append("				</td>");
		sb.append("			</tr>");
		sb.append("			<tr>");
		sb.append("				<td style=\"padding-left: 33px;\">");
		sb.append("	<dl style=\"width: 120px\">");
		sb.append("	<dd style=\"width: 120px\">");
		sb.append("	 <input type=\"text\" maxlength=\"14\" value=\"" + notNull(altaParteTO.getRfc())
				+ "\" name=\"rfcD\" id=\"rfcD\" onkeyup=\"this.value = this.value.toUpperCase()\" maxlength=\"16\" >");
		sb.append(" <span class=\"hint\"> <div class=\"cerrar\"> <a onclick=\"this.parentNode.getElementsByTagName('span')[0].style.display = 'none';\" style=\"cursor: pointer;\">x</a> </div> <div class=\"contenido\"> N煤mero de registro asignado por el Registro Federal de Contribuyentes. </div><span class=\"hint-pointer\">&nbsp;</span></span>");
		sb.append("	</dd>");
		sb.append("	</dl>");
		sb.append("					</td>");
		sb.append("			</tr>						");
		sb.append("		</table>");
		
		sb.append("<table id=\"tbejrfcDeud\">");
		sb.append("		<tr> ");
		sb.append("			<td style=\"padding-left:34px;\" class=\"textoEjemplo\">Ejemplo&nbsp;</td><td>");
		sb.append("				<table>");
		sb.append("					<tr>");
		sb.append("						<td class=\"textoGris\"  align=\"justify\">b) RFC: DEFT123409RT2</td>");
		sb.append("					</tr>");
		sb.append("					<tr>");
		sb.append("						<td class=\"textoGris\"  align=\"justify\">a) RFC: RFT632393H21</td>");
		sb.append("					</tr>");
		sb.append("				</table>");
		sb.append("			</td> ");
		sb.append("		</tr> ");
		sb.append("</table>");
		sb.append("<table>");
		sb.append(" <tr> <td  id=\"rfcValidar\"  style= \"padding-left: 39px;\" \"visibility: hidden; display: none\" >");
		sb.append("<input  type=\"checkbox\"  name=\"rfcValidaRfcDeud\" id=\"rfcValidaRfcDeud\" onclick=\"checkboxValRfcDeud()\">");
		sb.append("<td id=\"txrfc\" align=\"justify\">No cuenta con RFC</td>");
		
		sb.append("</td> </tr>");		
		sb.append("</table>");
		
		return sb;
	}

	private StringBuffer writePersonaFisica(AltaParteTO altaParteTO) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"input-field col s12\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getNombre()) 
				+ "\" type=\"text\" maxlength=\"150\" class=\"validate\" required=\"true\" name=\"nombreO\" id=\"nombreO\" onkeyup=\"this.value = this.value.toUpperCase()\">");
		sb.append("<label name=\"labelNombreO\" id=\"labelNombreO\" for=\"nombreO\">Nombre Completo</label>");
		sb.append("</div>");
		sb.append("</div>");
		
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"input-field col s4\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getCurp()) 
				+ "\" type=\"text\" maxlength=\"40\" class=\"validate tooltipped\" required=\"true\" maxlength=\"30\" name=\"rfcO\" id=\"rfcO\" data-position=\"right\" data-delay=\"50\" data-tooltip=\"Ingrese su n?mero sin espacios en blanco\" onkeypress=\"return aceptaalfa(event);\">");
		sb.append("<label id=\"labelRfcO\" for=\"rfcO\">Documento de Identificaci&oacute;n</label>");
		sb.append("</div>");
		sb.append("<div class=\"col s4\" id=\"buttonValidar\">");
		sb.append("<button type=\"button\" onclick=\"buscaDocumentoOtorgante()\" class=\"btn waves-effect indigo\">Buscar</button>");
		sb.append("</div>");
		sb.append("<div class=\"input-field col s4\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getRfc()) 
				+ "\" type=\"text\" maxlength=\"40\" class=\"validate tooltipped\" required=\"true\" maxlength=\"30\" name=\"nitOF\" id=\"nitOF\" data-position=\"right\" data-delay=\"50\" data-tooltip=\"Ingrese su n?mero sin espacios en blanco\" onkeypress=\"return aceptaalfa(event);\">");
		sb.append("<label id=\"labelNitOF\" for=\"nitOF\">NIT</label>");
		sb.append("</div>");		
		sb.append("</div>");
				
		return sb;
	}

	private StringBuffer writePersonaMoral(AltaParteTO altaParteTO, String isInscripcion) {
		StringBuffer sb = new StringBuffer();

		sb.append("<div class=\"row\">");
		sb.append("<div class=\"input-field col s6\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getRfc()) 
				+ "\" type=\"text\" maxlength=\"40\" class=\"validate tooltipped\" maxlength=\"30\" required=\"true\" name=\"nitO\" id=\"nitO\" data-position=\"right\" data-delay=\"50\" data-tooltip=\"Ingrese su numero sin espacios en blanco\" onkeypress=\"return aceptaalfa(event);\">");
		sb.append("<label id=\"labelNitO\" for=\"nitO\">NIT</label>");
		sb.append("</div>");		
		sb.append("<div class=\"col s6\" id=\"buttonValidar\">");
		sb.append("<button type=\"button\" onclick=\"buscaDocumentoOtorgante()\" class=\"btn waves-effect indigo\">Buscar</button>");
		sb.append("</div>");
		sb.append("</div>");
		
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"input-field col s12\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getRazonSocial()) 
				+ "\" type=\"text\" maxlength=\"150\" class=\"validate\" name=\"razonSocialO\" id=\"razonSocialO\" onkeyup=\"this.value = this.value.toUpperCase()\">");
		sb.append("<label id=\"labelRazonSocialO\" for=\"razonSocialO\">Raz&oacute;n o Denominaci&oacute;n Social</label>");
		sb.append("</div>");
		sb.append("</div>");
			
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"input-field col s12\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getInscrita()) 
				+ "\" type=\"text\" maxlength=\"150\" class=\"validate\" name=\"inscritaO\" id=\"inscritaO\">");
		sb.append("<label id=\"labelInscritaO\" for=\"inscritaO\">Informaci&oacute;n de la Inscripci&oacute;n de la persona jur&iacute;dica</label>");
		sb.append("</div>");
		sb.append("</div>");
			
		if(!isInscripcion.equalsIgnoreCase("2")) {
			sb.append("<div class=\"row note note-example\">\r\n"); 
			sb.append("<p>Si deudor garante y/o acreedor garantizado son personas jur&iacute;dicas, deber&aacute;\r\n"); 
			sb.append("adem&aacute;s identificarse la persona que act&uacute;a en su representaci&oacute;n, as&iacute; como el\r\n"); 
			sb.append("documento con que se Identifica y el documento con base en el, cual ejerce\r\n");
			sb.append("tal representaci&oacute;n. </p>\r\n");
			sb.append("</div>");	
		
			sb.append("<div class=\"row\">");
			sb.append("<div class=\"input-field col s12\">");		
			sb.append("<textarea rows=\"5\" onKeyUp=\"return maximaLongitud(this,3500)\" cols=\"53\" name=\"ubicacionInO\" id=\"ubicacionInO\" maxlength=\"3500\">" 
					+ notNull(altaParteTO.getUbicacionInscrito()) + " </textarea>");		
			sb.append("<label id=\"labelUbicacionInO\" for=\"ubicacionInO\">Datos del Representante(s)</label>");
			sb.append("</div>");
			sb.append("</div>");
		}
		
		return sb;
	}

	private boolean estaAdentro(int id, List<OtorganteTO> lista) {
		boolean regresa = false;
		Iterator<OtorganteTO> it = lista.iterator();

		while (it.hasNext() && !regresa) {
			OtorganteTO oto = it.next();
			if (oto.getIdOtorgante().intValue() == id) {
				regresa = true;
			}
		}

		return regresa;
	}

	private StringBuffer writeParteDeudor(String elementID, String idTramite,
			String idPersona, String idPersonaModificar,
			List<DeudorTO> listaDeudores, AltaParteTO altaParteTO,
			String isInscripcion) {
		StringBuffer sb = new StringBuffer();
		MyLogger.Logger.log(Level.INFO, "Is Inscripcion" + isInscripcion);
		sb.append("<div class=\"row\"><div class=\"col s12\"><div class=\"card\"><div class=\"card-content\">");
		AltaParteDAO altaParteDAO = new AltaParteDAO();
		List<OtorganteTO> listaOtorganteTO = altaParteDAO
				.getOtorganteByInscripcion(Integer.valueOf(idTramite));
		MyLogger.Logger.log(Level.INFO, "numero de otorgantes:"
				+ listaOtorganteTO.size());
		if (Integer.valueOf(idPersonaModificar) == 0) {
			if (listaDeudores.size() > 0) {
				if (Integer.valueOf(isInscripcion) != 0) {
					Iterator<DeudorTO> itDeudorTO = listaDeudores.iterator();
					DeudorTO deudorTO;
					boolean yaEsta = false;
					while ((!yaEsta) && itDeudorTO.hasNext()) {
						deudorTO = itDeudorTO.next();

						if (listaOtorganteTO.size() > 0) {
							yaEsta = estaAdentro(deudorTO.getIdPersona()
									.intValue(), listaOtorganteTO);
						}

					}
					MyLogger.Logger.log(Level.INFO, "Valor del yaEsta 1"
							+ yaEsta);
					if ((!yaEsta) && (listaOtorganteTO.size() > 0)) {
						MyLogger.Logger.log(Level.INFO,
								"No esta el otorgante como deudor");
						sb.append("<tr>");
						sb.append("<td>");
						sb.append("<span class=\"textoGeneralRojo\"><input type=\"checkbox\" style=\"cursor: pointer;\" onclick=\"elmismo('"
								+ elementID
								+ "','"
								+ idTramite
								+ "','"
								+ idPersona
								+ "','"
								+ idPersonaModificar
								+ "','"
								+ isInscripcion
								+ "')\">El Otorgante de la Garant&iacute;a Mobiliaria es Deudor </span>");
						sb.append("</td>");
						sb.append("</tr>");

					} else {
						MyLogger.Logger.log(Level.INFO,
								"Ya esta el otorgante como deudor");
					}

				}
				sb.append("<div class=\"row\">");
				sb.append(writeTablaDeudor(elementID, idTramite, idPersona,
						idPersonaModificar, listaDeudores, listaOtorganteTO,
						isInscripcion));
				sb.append("</div>");
			} else {
				if (Integer.valueOf(isInscripcion) != 0) {
					Iterator<DeudorTO> itDeudorTO = listaDeudores.iterator();
					DeudorTO deudorTO;
					boolean yaEsta = false;
					while ((!yaEsta) && itDeudorTO.hasNext()) {
						deudorTO = itDeudorTO.next();
						if (listaOtorganteTO.size() > 0) {
							yaEsta = estaAdentro(deudorTO.getIdPersona()
									.intValue(), listaOtorganteTO);
						}

					}
					MyLogger.Logger
							.log(Level.INFO, "Valor del yaEsta" + yaEsta);
					if ((!yaEsta) && (listaOtorganteTO.size() > 0)) {
						MyLogger.Logger.log(Level.INFO,
								"No esta el otorgante como deudor");
						sb.append("<tr>");
						sb.append("<td>");
						sb.append("<span class=\"textoGeneralRojo\"><input type=\"checkbox\" style=\"cursor: pointer;\" onclick=\"elmismo('"
								+ elementID
								+ "','"
								+ idTramite
								+ "','"
								+ idPersona
								+ "','"
								+ idPersonaModificar
								+ "','"
								+ isInscripcion
								+ "')\">El Otorgante de la Garant&iacute;a Mobiliaria es Deudor </span>");
						sb.append("</td>");
						sb.append("</tr>");

					} else {
						MyLogger.Logger.log(Level.INFO,
								"Ya esta el otorgante como deudor");
					}

				}
			}

		} else {
			sb.append("<script type=\"text/javascript\"> mostrarDeudor(); </script>");
		}

		sb.append("<div class=\"row\">");
		sb.append("<a onclick=\"mostrarDeudor()\" style=\"cursor: pointer;\" class=\"tituloHeader2\"> + Agregar Persona</a>");
		sb.append("</div>");
		sb.append("<div class=\"row\" style=\"visibility: hidden; display: none\" id=\"parteDeudorDTD\">");
		sb.append(writeFormularioDeudor(elementID, idTramite, idPersona,
				idPersonaModificar, altaParteTO, isInscripcion));
		sb.append("</div>");

		sb.append("</div></div></div></div>");
		return sb;
	}

	private StringBuffer writeFormularioDeudor(String elementID,
			String idTramite, String idPersona, String idPersonaModificar,
			AltaParteTO altaParteTO, String isInscripcion) {
		StringBuffer sb = new StringBuffer();
		List<TipoPersona> lista = new ArrayList<TipoPersona>();
		TipoPersona persona = new TipoPersona();
		persona.setId("PM");
		persona.setDesc("Persona Juridica");
		lista.add(persona);
		persona = new TipoPersona();
		persona.setId("PF");
		persona.setDesc("Persona Individual");
		lista.add(persona);
		//sb.append("<div class=\"row\">");
		MyLogger.Logger.log(Level.INFO, "writeFormularioDeudor tipoPersona"
				+ altaParteTO.getTipoPersona());
		sb.append(writeTiposPersonaD(lista, altaParteTO));
		
		NacionalidadDAO dao = new NacionalidadDAO();
		List<NacionalidadTO> listaNacionalidad = dao.getNacionalidades();		
		
		sb.append(writeNacionaliadD(listaNacionalidad, altaParteTO));
		if(altaParteTO.getTipoPersona()!=null && altaParteTO.getTipoPersona().equalsIgnoreCase("PM")) {
			sb.append("<div id=\"personMoralTRD\">");			
		} else { 
			sb.append("<div id=\"personMoralTRD\" style=\"visibility: hidden; display: none\">");
		}		
		sb.append(writePersonaMoralD(altaParteTO, isInscripcion));		
		sb.append("</div>");
		
		if(altaParteTO.getTipoPersona()!=null && altaParteTO.getTipoPersona().equalsIgnoreCase("PM")) {
			sb.append("<div id=\"personaFisicaTRD\" style=\"visibility: hidden; display: none\">");			
		} else {
			sb.append("<div id=\"personaFisicaTRD\" >");
		}		
		sb.append(writePersonaFisicaD(altaParteTO));		
		sb.append("</div>");	
		
		sb.append(writeTelefonoCorreo(altaParteTO));
		sb.append("<br />");
		//sb.append(writeResidenciaAcreedor(listaNacionalidad, altaParteTO));
		sb.append(writeCalleExtranjera(altaParteTO));
				
		if (Integer.valueOf(idPersonaModificar) == 0) {
			sb.append("<center><div class='row'><input class=\"btn btn-large waves-effect indigo\" type=\"button\" value=\"Aceptar\" onclick=\"guardaParteDeudor('"
					+ elementID
					+ "','"
					+ idTramite
					+ "','"
					+ idPersona
					+ "','0','"
					+ isInscripcion
					+ "') \"> <script> desbloqueaDeudorTP(); </script> <input class=\"btn btn-large waves-effect indigo\"  type=\"button\" value=\"Ocultar\" onclick= \"ocultarDeudorLim('"
					+ elementID
					+ "','"
					+ idTramite
					+ "','"
					+ idPersona
					+ "','0','" + isInscripcion + "'); \" /> </div></center>");
		} else {
			sb.append("<center><div class='row'><input type=\"button\" class=\"btn btn-large waves-effect indigo\" value=\"Aceptar\" onclick=\"modificaParteDeudor('"
					+ elementID
					+ "','"
					+ idTramite
					+ "','"
					+ idPersona
					+ "','"
					+ idPersonaModificar
					+ "','"
					+ isInscripcion
					+ "'); \">  <script> bloqueaDeudorTP(); </script><input type=\"button\" class=\"btn btn-large waves-effect indigo\" value=\"Ocultar\" onclick= \"ocultarDeudorLim('"
					+ elementID
					+ "','"
					+ idTramite
					+ "','"
					+ idPersona
					+ "','0','" + isInscripcion + "')\"  /> </div></center>");
		}
		
		return sb;

	}
        
        private StringBuffer writeTablaBienes(String elementID, String idTramite, List<BienEspecialTO> listaBienesTO) {
		StringBuffer sb = new StringBuffer();
		
		Iterator<BienEspecialTO> it = listaBienesTO.iterator();


		sb.append("<table id=\"bienes\" class=\"table responsive-table centered\" data-paging=\"true\" data-filtering=\"false\" data-sorting=\"true\">");
		sb.append("<thead>");
		sb.append("<tr>");
		sb.append("<th>Tipo Bien Especial</th>");
		sb.append("<th>Tipo Identificador</th>");
		sb.append("<th>Identificador</th>");
//		sb.append("<th>Serie</th>");
		sb.append("<th>Descripcion</th>");
		sb.append("<th>Opciones</th>");
		sb.append("</tr>");
		sb.append("</thead>");
		sb.append("<tbody>");
		
		BienEspecialTO bienEspecialTO;
		while (it.hasNext()) {
			bienEspecialTO = it.next();
			sb.append("<tr>");
			sb.append("<td>"	+ bienEspecialTO.getTipoBien() + "</td>");
			sb.append("<td>"	+ bienEspecialTO.getTipoIdentificador() + "</td>");
			sb.append("<td>"	+ notNull(bienEspecialTO.getIdentificador()) + "</td>");
//			sb.append("<td>"	+ notNull(bienEspecialTO.getSerie()) + "</td>");
			sb.append("<td>"	+ notNull(bienEspecialTO.getDescripcion()) + "</td>");
			sb.append("<td> <a class=\"btn waves-effect red darken-4\" onclick=\"eliminaParteBien('"
					+ elementID
					+ "','"
					+ idTramite
					+ "','"
					+ bienEspecialTO.getIdTramiteGarantia()+ "')\"><i class=\"material-icons\">delete</i></a>");
			sb.append(" <a class=\"btn waves-effect indigo darken-4\" onclick=\"modificaParteBien('"
					+ elementID
					+ "','"
					+ idTramite
					+ "','"
					+ bienEspecialTO.getTipoBien()
					+ "','"
					+ bienEspecialTO.getTipoIdentificador()
					+ "','"
					+ notNull(bienEspecialTO.getIdentificador())
					+ "','"
					+ notNull(bienEspecialTO.getDescripcion())
					+ "','"
					+ bienEspecialTO.getIdTramiteGarantia()
					+ "')\"><i class=\"material-icons\">edit</i></a></td>");
			sb.append("</tr>");

			//					+ "','"
//					+ notNull(bienEspecialTO.getSerie())
		}
		sb.append("</tbody>");
		sb.append("</table>");

		
		return sb;
	}

	
	
	private StringBuffer writeTablaBienes_factoraje(String elementID, String idTramite, List<BienEspecialTO> listaBienesTO) {
		StringBuffer sb = new StringBuffer();
		
		Iterator<BienEspecialTO> it = listaBienesTO.iterator();


		sb.append("<table id=\"bienes\" class=\"table responsive-table centered\" data-paging=\"true\" data-filtering=\"false\" data-sorting=\"true\">");
		sb.append("<thead>");
		sb.append("<tr>");
		sb.append("<th>Tipo Bien Especial</th>");
		
                sb.append("<th>Serie</th>");
		sb.append("<th>Numero Factura</th>");
		
		sb.append("<th>Descripcion</th>");
		sb.append("<th>Opciones</th>");
		sb.append("</tr>");
		sb.append("</thead>");
		sb.append("<tbody>");
		
		BienEspecialTO bienEspecialTO;
		while (it.hasNext()) {
			bienEspecialTO = it.next();
			sb.append("<tr>");
			sb.append("<td>"	+ bienEspecialTO.getTipoBien() + "</td>");
			
                        sb.append("<td>"	+ notNull(bienEspecialTO.getSerie()) + "</td>");
			sb.append("<td>"	+ notNull(bienEspecialTO.getIdentificador()) + "</td>");
			
			sb.append("<td>"	+ notNull(bienEspecialTO.getDescripcion()) + "</td>");
			sb.append("<td> <a class=\"btn waves-effect red darken-4\" onclick=\"eliminaParteBien('"
					+ elementID
					+ "','"
					+ idTramite
					+ "','"
					+ bienEspecialTO.getIdTramiteGarantia()+ "')\"><i class=\"material-icons\">delete</i></a>");
			sb.append(" <a class=\"btn waves-effect indigo darken-4\" onclick=\"modificaParteBienFactoraje('"
					+ elementID
					+ "','"
					+ idTramite
					+ "','"
					+ bienEspecialTO.getTipoBien()
					+ "','"
					+ bienEspecialTO.getTipoIdentificador()
					+ "','"
					+ notNull(bienEspecialTO.getIdentificador())
					+ "','"
					+ notNull(bienEspecialTO.getDescripcion())
					+ "','"
					+ bienEspecialTO.getIdTramiteGarantia()
                                	+ "','"
					+ bienEspecialTO.getSerie()
					+ "')\"><i class=\"material-icons\">edit</i></a></td>");
			sb.append("</tr>");

			//					+ "','"
//					+ notNull(bienEspecialTO.getSerie())
		}
		sb.append("</tbody>");
		sb.append("</table>");

		
		return sb;
	}
	
	private StringBuffer writeTablaDeudor(String elementID, String idTramite, String idPersona, String idPersonaModificar,
			List<DeudorTO> listaDeudores, List<OtorganteTO> listaOtorganteTO,
			String isInscripcion) {
		StringBuffer sb = new StringBuffer();

		Iterator<DeudorTO> it = listaDeudores.iterator();
		
		sb.append("<table id=\"tableDeudores\" class=\"striped\" >");
		sb.append("<thead>");
		sb.append("<tr>");
		sb.append("<th>Nombre, Denominaci&oacute;n o Raz&oacute;n Social</th>");
		sb.append("<th>No. Identificaci&oacute;n \\ No. Identificaci&oacute;n Tributaria</th>");
		sb.append("<th>Opciones</th>");
		sb.append("</tr>");
		sb.append("</thead>");
		sb.append("<tbody>");
		DeudorTO deudorTO;
		while (it.hasNext()) {
			deudorTO = it.next();
			sb.append("<tr>");
			System.out.println("nombre  = " + deudorTO.getNombreCompleto() + " razon " + deudorTO.getTipoPersona());
			sb.append("<td>"	+ notNull(deudorTO.getNombreCompleto()) + "</td>");
			if(deudorTO.getTipoPersona().equalsIgnoreCase("PM"))
				sb.append("<td>" + notNull(deudorTO.getRfc()) + "</td>");
			else
				sb.append("<td>" + notNull(deudorTO.getCurp()) + "</td>");
			MyLogger.Logger.log(Level.INFO,	"ID del deudor : " + deudorTO.getIdPersona());
			sb.append("<td> <table><tr><td><a class=\"btn waves-effect red darken-4\" data-toggle=\"tooltip\" title=\"Eliminar\" onclick=\"eliminaParteDeudor('"
					+ elementID
					+ "','"
					+ idTramite
					+ "','"
					+ idPersona
					+ "','"
					+ deudorTO.getIdPersona()
					+ "','"
					+ isInscripcion
					+ "')\"><i class=\"material-icons\">delete</i></a></td><td><a class=\"btn waves-effect indigo\" data-toggle=\"tooltip\" title=\"Modificar\" onclick=\"cargaParteDeudor('"
					+ elementID
					+ "','"
					+ idTramite
					+ "','"
					+ idPersona
					+ "','"
					+ deudorTO.getIdPersona()
					+ "','"
					+ isInscripcion + "')\"><i class=\"material-icons\">edit</i></a></td></tr></table></td>");
			
			sb.append("</tr>");
		}
                   
		sb.append("</tbody>");
		sb.append("</table>");
                sb.append("<strong><h5 style='text-align: center;' >Si desea modificar un deudor, se recomienda que lo elimine y lo ingrese nuevamente.</h5><strong>");

		return sb;
	}

	private StringBuffer writePersonaFisicaD(AltaParteTO altaParteTO) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"input-field col s12\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getNombre()) 
				+ "\" type=\"text\" maxlength=\"150\" class=\"validate\" required=\"true\" name=\"nombreD\" id=\"nombreD\" onkeyup=\"this.value = this.value.toUpperCase()\">");
		sb.append("<label name=\"labelNombreD\" id=\"labelNombreD\" for=\"nombreD\">Nombre Completo</label>");
		sb.append("</div>");
		sb.append("</div>");

		sb.append("<div class=\"row\">");
		sb.append("<div class=\"input-field col s3\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getCurp()) 
				+ "\" type=\"text\" maxlength=\"40\" class=\"validate tooltipped\" maxlength=\"30\" required=\"true\" name=\"rfcD\" id=\"rfcD\" data-position=\"right\" data-delay=\"50\" data-tooltip=\"Ingrese su n?mero sin espacios en blanco\" onkeypress=\"return aceptaalfa(event);\">");
		sb.append("<label id=\"labelRfcD\" for=\"rfcD\">Documento de Identificaci&oacute;n</label>");
		sb.append("</div>");
		sb.append("<div class=\"col s3\" id=\"buttonValidar\">");
		sb.append("<button type=\"button\" onclick=\"buscaDocumentoDeudor()\" class=\"btn waves-effect indigo\">Buscar</button>");
		sb.append("</div>");
                sb.append("<div class=\"col s3\" id=\"buttonValidarInst\" >");
		sb.append("<button type=\"button\" onclick=\"buscaDocumentoDeudorInstitucion(1)\" class=\"btn waves-effect indigo\">Busqueda SAT (Nit)</button>");
		sb.append("</div>");
		sb.append("<div class=\"input-field col s3\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getRfc()) 
				+ "\" type=\"text\" maxlength=\"40\" class=\"validate tooltipped\" maxlength=\"30\" required=\"true\" name=\"nitDF\" id=\"nitDF\" data-position=\"right\" data-delay=\"50\" data-tooltip=\"Ingrese su n?mero sin espacios en blanco ni gui?n\" onkeypress=\"return aceptaalfa(event);\">");
		sb.append("<label id=\"labelNitDF\" for=\"nitDF\">NIT</label>");
		sb.append("</div>");
		sb.append("</div>");
				
		/*sb.append("<div class=\"row\">");
		sb.append("<div class=\"input-field col s6\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getExtencion()) 
				+ "\" type=\"text\" maxlength=\"100\" class=\"validate\" required=\"true\" name=\"extensionD\" id=\"extensionD\">");
		sb.append("<label id=\"labelExtensionD\" for=\"extensionD\">Extendido en</label>");
		sb.append("</div>");		
		
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"input-field col s6\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getEdad()) 
				+ "\" type=\"text\" maxlength=\"10\" class=\"validate\" required=\"true\" name=\"edadD\" id=\"edadD\">");
		sb.append("<label id=\"labelEdadD\" for=\"edadD\">Edad</label>");
		sb.append("</div>");
		sb.append("</div>");
		
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"input-field col s6\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getEstadoCivil()) 
				+ "\" type=\"text\" maxlength=\"50\" class=\"validate\" required=\"true\" name=\"estadoCivilD\" id=\"estadoCivilD\">");
		sb.append("<label id=\"labelEstadoCivilD\" for=\"estadoCivilD\">Estado Civil</label>");
		sb.append("</div>");		
		
		sb.append("<div class=\"input-field col s6\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getProfesion()) 
				+ "\" type=\"text\" maxlength=\"100\" class=\"validate\" name=\"profesionD\" id=\"profesionD\">");
		sb.append("<label id=\"labelProfesionD\" for=\"profesionD\">Profesi&oacute;n u Oficio</label>");
		sb.append("</div>");
		sb.append("</div>");*/
		
		return sb;
	}

	private StringBuffer writePersonaMoralD(AltaParteTO altaParteTO, String isInscripcion) {
		StringBuffer sb = new StringBuffer();

		
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"input-field col s6\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getRfc()) 
				+ "\" type=\"text\" maxlength=\"40\" class=\"validate tooltipped\" maxlength=\"30\" required=\"true\" name=\"nitD\" id=\"nitD\" data-position=\"right\" data-delay=\"50\" data-tooltip=\"Ingrese su n?mero sin espacios en blanco ni gui?n\" onkeypress=\"return aceptaalfa(event);\">");
		sb.append("<label id=\"labelNitD\" for=\"nitD\">NIT</label>");
		sb.append("</div>");		
		sb.append("<div class=\"col s2\" id=\"buttonValidar\">");
		sb.append("<button type=\"button\" onclick=\"buscaDocumentoDeudor()\" class=\"btn waves-effect indigo\">Buscar</button>");
		sb.append("</div>");
                sb.append("<div class=\"col s4\" id=\"buttonValidarInst\" >");
		sb.append("<button type=\"button\" onclick=\"buscaDocumentoDeudorInstitucion(1)\" class=\"btn waves-effect indigo\">Busqueda SAT (Nit)</button>");
		sb.append("</div>");
                
		sb.append("</div>");
		
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"input-field col s12\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getRazonSocial()) 
				+ "\" type=\"text\" maxlength=\"150\" class=\"validate\" name=\"razonSocialD\" id=\"razonSocialD\" onkeyup=\"this.value = this.value.toUpperCase()\">");
		sb.append("<label id=\"labelRazonSocialD\" for=\"razonSocialD\">Raz&oacute;n o Denominaci&oacute;n Social</label>");
		sb.append("</div>");
		sb.append("</div>");

		/*sb.append("<div class=\"row note note-example\">");
		sb.append("<div class=\"col s1\"><p>Ejemplos</p></div>");
		sb.append("<div class=\"col s11\"><ol type=\"a\">");
		sb.append("<li>Denominaci&oacute;n: Electr&oacute;n, S.A. de C.V.</li>");
		sb.append("<li>Raz&oacute;n Social: L&oacute;pez M&eacute;ndez y C&iacute;a, S. A. de C.V.</li>");
		sb.append("</ol></div>");
		sb.append("</div>");*/
		
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"input-field col s12\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getInscrita()) 
				+ "\" type=\"text\" maxlength=\"150\" class=\"validate\" name=\"inscritaD\" id=\"inscritaD\">");
		sb.append("<label id=\"labelInscritaD\" for=\"inscritaD\">Informaci&oacute;n de la de Inscripci&oacute;n de la persona jur&iacute;dica</label>");
		sb.append("</div>");
		sb.append("</div>");
		
		if(!isInscripcion.equalsIgnoreCase("2")) {
			sb.append("<div class=\"row note note-example\">\r\n"); 
			sb.append("<p>Si deudor garante y/o acreedor garantizado son personas jur&iacute;dicas, deber&aacute;\r\n"); 
			sb.append("adem&aacute;s identificarse la persona que act&uacute;a en su representaci&oacute;n, as&iacute; como el\r\n"); 
			sb.append("documento con que se Identifica y el documento con base en el, cual ejerce\r\n");
			sb.append("tal representaci&oacute;n. </p>\r\n");
			sb.append("</div>");	
			
			sb.append("<div class=\"row\">");
			sb.append("<div class=\"input-field col s12\">");		
			sb.append("<textarea rows=\"5\" onKeyUp=\"return maximaLongitud(this,3500)\" cols=\"53\" name=\"ubicacionInD\" id=\"ubicacionInD\" maxlength=\"3500\">" 
					+ notNull(altaParteTO.getUbicacionInscrito()) + " </textarea>");		
			sb.append("<label id=\"labelUbicacionInD\" for=\"ubicacionInD\">Datos del Representante(s)</label>");
			sb.append("</div>");
			sb.append("</div>");
		}
				
		/*sb.append("<div class=\"input-field col s6\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getFolioInscrito()) 
				+ "\" type=\"text\" maxlength=\"40\" class=\"validate\" name=\"folioInD\" id=\"folioInD\">");
		sb.append("<label for=\"folioInD\">Folio</label>");
		sb.append("</div>");
		
		
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"input-field col s6\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getLibroInscrito())
				+ "\" type=\"text\" maxlength=\"40\" class=\"validate\" name=\"libroInD\" id=\"libroInD\">");
		sb.append("<label for=\"libroInD\">Libro</label>");
		sb.append("</div>");
		
		sb.append("<div class=\"input-field col s6\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getUbicacionInscrito()) 
				+ "\" type=\"text\" maxlength=\"40\" class=\"validate\" name=\"ubicacionInD\" id=\"ubicacionInD\">");
		sb.append("<label for=\"ubicacionInD\">Direcci&oacute;n</label>");
		sb.append("</div>");
		sb.append("</div>");*/
		
		return sb;
	}

	private StringBuffer writeNacionaliadD(
			List<NacionalidadTO> listaNacionalidades, AltaParteTO altaParteTO) {
		StringBuffer sb = new StringBuffer();
		try {
			Iterator<NacionalidadTO> it = listaNacionalidades.iterator();
			Integer nacSelected = altaParteTO.getIdNacionalidad()==null?1:altaParteTO.getIdNacionalidad();
			
			sb.append("<div class=\"row\">");
			sb.append("<div class=\"input-field col s12\">");
			sb.append("<select id='nacionalidadD' name='nacionalidadD' onchange='cambiaNacionalidadD()'> ");
			NacionalidadTO nacionalidadTO;
			while (it.hasNext()) {
				nacionalidadTO = it.next();
				sb.append("<option ");
				sb.append("value='");
				sb.append(nacionalidadTO.getIdNacionalidad());
				if(nacionalidadTO.getIdNacionalidad().compareTo(nacSelected)==0) {
					sb.append("' selected=\"selected\">");
				} else {
					sb.append("'>");
				}						
				sb.append(nacionalidadTO.getDescNacionalidad());
				sb.append("</option>");					
			}
			sb.append("</select> ");
			sb.append("<label for=\"tipoPersonaD\"><span id=\"anotacionTipoPersona\">Nacionalidad :</span></label>");
			sb.append("</div>");
			sb.append("</div>");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb;
	}

	// por aqui inicia mi codigo

	private StringBuffer writeTipo(List<TipoTo> listaTipo,
			AltaParteTO altaParteTO) {
		StringBuffer sb = new StringBuffer();

		try {
			Iterator<TipoTo> it = listaTipo.iterator();

			sb.append("<script type=\"text/javascript\">");
			sb.append("    $(function () {");
			sb.append("        $('.ayudaTipoSociedad').ayuda({");
			sb.append("            url: '/Rug/comun/publico/help.do',");
			sb.append("            ico: '/Rug/resources/imgs/documentinfo.png',");
			sb.append("            width: 515,");
			sb.append("            height: 450");
			sb.append("        })    ");
			sb.append("        })    ");
			sb.append("</script> ");

			sb.append(" <table style=\"visibility: visible; display: block\">");
			sb.append("			<tr>");

			sb.append("				<td style=\"padding-left: 20px;\" colspan=\"2\" class=\"tituloHeader2\" align=\"left\"><span");
			sb.append("					id=\"TipoSociedad\" class=\"ayudaTipoSociedad\" >Tipo :</span>");
			sb.append("				</td>");

			sb.append("			</tr>");

			sb.append("			<tr>");
			sb.append("			<td style=\"padding-left: 30px;\">");

			sb.append("	<dl style=\"width: 120px\">");
			sb.append("	<dd style=\"width: 120px\">");

			sb.append("<select id='TipoD' name='TipoD' onchange='cambiaTipoD()'> ");

			TipoTo tipoTo;
			while (it.hasNext()) {
				tipoTo = it.next();
				sb.append("<option ");
				sb.append("value='");
				sb.append(tipoTo.getIdTipo());
				sb.append("'>");
				sb.append(tipoTo.getDesTipo());
				sb.append("</option>");
			}
			sb.append("</select> ");
			// sb.append(" <span class=\"hint\"> <div class=\"cerrar\"> <a onclick=\"this.parentNode.getElementsByTagName('span')[0].style.display = 'none';\" style=\"cursor: pointer;\">x</a> </div> <div class=\"contenido\">'Otras' Ejemplo RFC: RFT632393H21</div><span class=\"hint-pointer\">&nbsp;</span></span>");
			sb.append("	</dd>");
			sb.append("	</dl>");
			sb.append("</td></tr> </table> </td>");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb;
	}

	private StringBuffer NIFPaisOrigen(AltaParteTO altaParteTO) {
		StringBuffer sb = new StringBuffer();

		sb.append("<script type=\"text/javascript\">");
		sb.append("    $(function () {");
		sb.append("        $('.ayudaNIF').ayuda({");
		sb.append("            url: '/Rug/comun/publico/help.do',");
		sb.append("            ico: '/Rug/resources/imgs/documentinfo.png',");
		sb.append("            width: 515,");
		sb.append("            height: 370");
		sb.append("        })    ");
		sb.append("        })    ");
		sb.append("</script> ");

		sb.append(" <table style=\"visibility: visible; display: block\">");
		sb.append("			<tr>");
		sb.append("				<td style=\"padding-left: 20px;\" colspan=\"2\" class=\"tituloHeader2\" align=\"left\"><span");
		sb.append("					id=\"anotacionNif\" class=\"ayudaNIF\" >N煤mero de Identificaci贸n Fiscal en el pa铆s de origen :</span>");
		sb.append("				</td>");
		sb.append("			</tr>");

		sb.append("			<tr>");

		sb.append("<td id=\"tipoObliga\" style=\"padding-left: 18px;\" class=\"texto_azul\" width=\"1px\">*</td>");

		sb.append("				<td style=\"padding-left: 10px;\">");
		sb.append("	<dl style=\"width: 120px\">");
		sb.append("	<dd style=\"width: 120px\">");

		sb.append("	 <input type=\"text\" maxlength=\"14\" value=\""
				+ notNull(altaParteTO.getRfc())
				+ "\" name=\"nif\" id=\"nif\" onkeyup=\"this.value = this.value.toUpperCase()\" maxlength=\"16\" >");

		sb.append(" <span class=\"hint\"> <div class=\"cerrar\"> <a onclick=\"this.parentNode.getElementsByTagName('span')[0].style.display = 'none';\" style=\"cursor: pointer;\">x</a> </div> <div class=\"contenido\"> Tax Id Number Estados Unidos</div><span class=\"hint-pointer\">&nbsp;</span></span>");
		sb.append("	</dd>");
		sb.append("	</dl>");
		sb.append("					</td>");
		sb.append("			</tr>						");
		sb.append("		</table>");

		return sb;
	}

	// por aqui termina mi codigo

	private StringBuffer writeTiposPersonaD(List<TipoPersona> listaPersona,
			AltaParteTO altaParteTO) {
		StringBuffer sb = new StringBuffer();
		try {
			Iterator<TipoPersona> it = listaPersona.iterator();
			String tTipoPersona = altaParteTO.getTipoPersona()==null?"PF":altaParteTO.getTipoPersona();

			sb.append("<div class=\"row\">");
			sb.append("<div class=\"input-field col s12\">");
			sb.append("<select id='tipoPersonaD' name='tipoPersonaD' onchange='cambiaTipoPersonaD()'> ");
			TipoPersona persona;
			while (it.hasNext()) {
				persona = it.next();
				sb.append("<option ");
				sb.append("value='");
				sb.append(persona.getId());
				if(persona.getId().equalsIgnoreCase(tTipoPersona)) {
					sb.append("' selected=\"selected\">");
				} else {
					sb.append("'>");
				}				
				sb.append(persona.getDesc());
				sb.append("</option>");
			}
			sb.append("</select> ");
			sb.append("<label for=\"tipoPersonaD\"><span id=\"anotacionTipoPersona\">Seleccione Tipo de Persona:</span></label>");
			sb.append("</div>");
			sb.append("</div>");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb;
	}

	private StringBuffer writeTabla(List<OtorganteTO> listaOtorganteTO,
			String elementId, String tramiteId, String isInscripcion,
			String idPersona, String isMultiple) {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("<div class=\"row\" >");
		sb.append("<table id=\"tableOtorgantes\" class=\"striped\" >");
		sb.append("<thead>");
		sb.append("<tr>");
		sb.append("<th>Nombre, Denominaci&oacute;n o Raz&oacute;n Social</th>");
		sb.append("<th>No. Identificaci&oacute;n \\ No. Identificaci&oacute;n Tributaria</th>");
		sb.append("<th>Opciones</th>");
		sb.append("</tr>");
		sb.append("</thead>");
		sb.append("<tbody>");
		Iterator<OtorganteTO> it = listaOtorganteTO.iterator();
		OtorganteTO otorganteTO;
		while (it.hasNext()) {
			otorganteTO = it.next();
			sb.append("	<tr>");
			sb.append("<td>" + otorganteTO.getNombreCompleto() + "</td>");
			if(otorganteTO.getTipoPersona().equalsIgnoreCase("PM")) 
				sb.append("<td>" + notNull(otorganteTO.getRfc()) + "</td>");
			else 
				sb.append("<td>" + notNull(otorganteTO.getCurp()) + "</td>");
			// sb.append("<td class=\"cuerpo1TablaResumen\">"+acreedorTO.getRfc()+"</td>");
			sb.append("<td> <table> <tr> <td> <a class=\"btn waves-effect red darken-4\" data-toggle=\"tooltip\" title=\"Eliminar\" onclick=\"eliminaParteOtorgante('"
					+ elementId
					+ "','"
					+ tramiteId
					+ "','"
					+ idPersona
					+ "','"
					+ otorganteTO.getIdOtorgante()
					+ "','"
					+ isMultiple
					+ "')\"><i class=\"material-icons\">delete</i></a></td> " +
					 "<td> <a class=\"btn waves-effect indigo\" data-position=\"bottom\" data-toggle=\"tooltip\" title=\"Modificar\" onclick=\"cargaParteOtorgante('"
					 + elementId
					 + "','"
					 + tramiteId
					 + "','"
					 + otorganteTO.getIdOtorgante()
					 + "','"
					 + otorganteTO.getIdOtorgante()
					 + "','"
					 + isInscripcion
					 + "','"
					 + isMultiple 
					 + "')\"><i class=\"material-icons\">edit</i></a></td> " +
					" </tr> </table></td>");
			sb.append("</tr>");
		}

		sb.append("</tbody>");
		sb.append("</table></div>");
		return sb;
	}

	// private StringBuffer writeTablaComerciante(OtorganteTO otorganteTO,
	// String elementId, String tramiteId) {
	// StringBuffer sb = new StringBuffer();
	// sb.append("<table width=\"650px\"><thead><tr>");
	// sb.append("<td width=\"32%\" class=\"encabezadoTablaResumen\" ");
	// sb.append(" style=\"text-align: center\">Nombre, Denominaci&oacute;n o Raz&oacute;n Social</td> ");
	// sb.append("<td width=\"28%\" class=\"encabezadoTablaResumen\"");
	// sb.append(" style=\"text-align: center\">Folio Electr&oacute;nico del Otorgante</td>");
	// // sb.append("<td width=\"12%\" class=\"encabezadoTablaResumen\"");
	// // sb.append(" style=\"text-align: center\">RFC</td>");
	// sb.append("<td width=\"28%\" class=\"encabezadoTablaResumen\"");
	// sb.append(" style=\"text-align: center\">Opciones</td>	");
	// sb.append("</tr>");
	// sb.append("</thead>");
	// sb.append("<tbody>");
	// sb.append("	<tr>");
	//
	// sb.append("<td class=\"cuerpo1TablaResumen\">"
	// + notNull(otorganteTO.getNombreCompleto()) + "</td>");
	// sb.append("<td class=\"cuerpo1TablaResumen\">"
	// + notNull(otorganteTO.getFolioMercantil()) + "</td>");
	// //
	// sb.append("<td class=\"cuerpo1TablaResumen\">"+otorganteTO.getRfc()+"</td>");
	// sb.append("<td class=\"cuerpo1TablaResumen\"> <table> <tr> <td><input type=\"button\" class=\"boton_rug\" value=\"Eliminar\" onclick=\"eliminaParteComerciante('"
	// + elementId
	// + "','"
	// + tramiteId
	// + "','"
	// + otorganteTO.getIdPersona()
	// +
	// "','0')\"></td><td><input type=\"button\" class=\"boton_rug\" value=\"Modificar\" onclick=\"cargaParteComerciante('"
	// + elementId
	// + "','"
	// + tramiteId
	// + "','"
	// + otorganteTO.getIdPersona()
	// + "','"
	// + otorganteTO.getIdPersona() + "')\"></td> </tr> </table> ");
	// sb.append("		</td>					</tr>");
	// sb.append("</tbody>");
	// sb.append("</table>");
	// return sb;
	// }

	private StringBuffer writeNacionaliad(
			List<NacionalidadTO> listaNacionalidades, AltaParteTO altaParteTO) {
		StringBuffer sb = new StringBuffer();
		try {
			Iterator<NacionalidadTO> it = listaNacionalidades.iterator();
			Integer nacSelected = altaParteTO.getIdNacionalidad()==null?1:altaParteTO.getIdNacionalidad();
			
			sb.append("<div class=\"row\">");
			sb.append("<div class=\"input-field col s12\">");
			sb.append("<select id='nacionalidad' name='nacionalidad' onchange='cambiaNacionalidad()'> ");
			NacionalidadTO nacionalidadTO;
			while (it.hasNext()) {
				nacionalidadTO = it.next();
				sb.append("<option ");
				sb.append("value='");
				sb.append(nacionalidadTO.getIdNacionalidad());
				if(nacionalidadTO.getIdNacionalidad().compareTo(nacSelected)==0) {
					sb.append("' selected=\"selected\">");
				} else {
					sb.append("'>");
				}						
				sb.append(nacionalidadTO.getDescNacionalidad());
				sb.append("</option>");					
			}
			sb.append("</select> ");
			sb.append("<label for=\"nacionalidad\">Nacionalidad</label>");
			sb.append("</div>");
			sb.append("</div>");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb;
	}

	private StringBuffer writeTiposPersona(List<TipoPersona> listaPersona,
			AltaParteTO altaParteTO) {	
		StringBuffer sb = new StringBuffer();
		try {
			Iterator<TipoPersona> it = listaPersona.iterator();
			String tTipoPersona = altaParteTO.getTipoPersona()==null?"PF":altaParteTO.getTipoPersona();

			sb.append("<div class=\"row\">");
			sb.append("<div class=\"input-field col s12\">");
			sb.append("<select id='tipoPersona' name='tipoPersona' onchange='cambiaTipoPersona()'> ");
			TipoPersona persona;
			while (it.hasNext()) {
				persona = it.next();
				sb.append("<option ");
				sb.append("value='");
				sb.append(persona.getId());				
				if(persona.getId().equalsIgnoreCase(tTipoPersona)) {
					sb.append("' selected=\"selected\">");
				} else {
					sb.append("'>");
				}				
				sb.append(persona.getDesc());
				sb.append("</option>");
			}
			sb.append("</select> ");
			sb.append("<label for=\"tipoPersona\">Seleccione Tipo de Persona</label>");
			sb.append("</div>");
			sb.append("</div>");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb;
	}

	// Acreedor Representado
	private StringBuffer writeTablaAcreedorRepresentado(String elementId,String idPersona, List<AcreedorTO> listaAcreedores) {
		StringBuffer sb = new StringBuffer();
		PrivilegiosDAO privilegiosDAO = new PrivilegiosDAO();
		PrivilegiosTO privilegiosTO = new PrivilegiosTO();
		privilegiosTO.setIdRecurso(new Integer(4));
		UsuarioTO usuarioTO = new UsuarioTO();
		PersonaTO to = new PersonaTO();
		to.setIdPersona(Integer.valueOf(idPersona));
		usuarioTO.setPersona(to);
		privilegiosTO = privilegiosDAO.getPrivilegios(privilegiosTO, usuarioTO);
		CharSetUtil csu = new CharSetUtil();
		sb.append("<table><tr>");
		sb.append("	<td width=\"12%\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">Identificador</td>");
		sb.append("	<td width=\"12%\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">");
		sb.append("Nombre, Denominaci&oacute;n o Raz&oacute;n Social</td>");
		// sb.append("<td width=\"8%\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">");
		// sb.append("Folio Electr&oacute;nico</td>");
		sb.append("<td width=\"12%\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">RFC, CURP, Documento que acredita su legal estancia en el pa&iacute;s </td>");
		sb.append("	<td width=\"18%\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">Opciones</td>");
		sb.append("</tr>");
		Iterator<AcreedorTO> it = listaAcreedores.iterator();
		AcreedorTO acreedorTO;

		while (it.hasNext()) {
			acreedorTO = it.next();
			sb.append("	<tr>");

			sb.append("<td class=\"cuerpo1TablaResumen\"  width=\"300\"> ");
			sb.append(acreedorTO.getIdPersona());
			sb.append("</td>");

			String rfcurdoc = "";
			if (acreedorTO.getRfc() != null) {
				MyLogger.Logger.log(Level.INFO, "--writeTablaAcreedorRep 1--");
				rfcurdoc = acreedorTO.getRfc();
			} else {
				MyLogger.Logger.log(Level.INFO, "--writeTablaAcreedorRep 2--");
				rfcurdoc = acreedorTO.getCurp();
			}
			sb.append("<td class=\"cuerpo1TablaResumen\"  width=\"300\"> "
					+ csu.presentacionMaximaDeTexto(notNull(acreedorTO.getNombreCompleto()), 50, "")
					+ " </td>");
			sb.append("<td class=\"cuerpo1TablaResumen\"  width=\"300\">  "
					+ csu.presentacionMaximaDeTexto(notNull(rfcurdoc), 50, "")
					+ "</td>");
			// sb.append("<td class=\"cuerpo1TablaResumen\">"+acreedorTO.getRfc()+"</td>");
			sb.append("<td class=\"cuerpo1TablaResumen\"> ");
			sb.append(" <table> <tr> ");
			if (privilegiosDAO.getTienePermiso(acreedorTO.getIdPersona(), Integer.valueOf(idPersona), new Integer(17))) {

				// "<td>  <a style=\"cursor: pointer;\" onclick=\"eliminaParteAcreedorRepresentado('"
				// + elementId
				// + "','"
				// + idPersona
				// + "','"
				// + acreedorTO.getIdPersona()
				// + "')\">  Eliminar | </a> </td> " +

				sb.append(" <td> <a style=\"cursor: pointer;\" onclick=\"controlUsuarioJS('"+ acreedorTO.getIdPersona()	+ "')\"> Usuarios y Grupos</a> </td> ");
			}
			if (privilegiosDAO.getTienePermiso(acreedorTO.getIdPersona(),
					Integer.valueOf(idPersona), new Integer(22))) {
				sb.append("<td> <a style=\"cursor: pointer;\" onclick=\"cargaParteAcreedorRepresentado('"
						+ elementId
						+ "','"
						+ idPersona
						+ "','"
						+ acreedorTO.getIdPersona()
						+ "')\"> |  Modificar </a>  </td>");
			}
			sb.append(" </tr> </table> ");
			sb.append("</td>");
			sb.append("</tr>");
		}

		sb.append("</table>");
		return sb;
	}

	private StringBuffer writeTablaAcreedorRepresentadoSinFirma(
			String elementId, String idPersona, List<AcreedorTO> listaAcreedores) {
		StringBuffer sb = new StringBuffer();

		PrivilegiosDAO privilegiosDAO = new PrivilegiosDAO();
		PrivilegiosTO privilegiosTO = new PrivilegiosTO();
		privilegiosTO.setIdRecurso(new Integer(4));
		UsuarioTO usuarioTO = new UsuarioTO();
		PersonaTO to = new PersonaTO();
		to.setIdPersona(Integer.valueOf(idPersona));
		usuarioTO.setPersona(to);
		privilegiosTO = privilegiosDAO.getPrivilegios(privilegiosTO, usuarioTO);

		MyLogger.Logger.log(Level.INFO,
				"writeTablaAcreedorRepresentado sin firma : id de la persona: "
						+ idPersona);
		sb.append("<table><tr>");
		sb.append("<td colspan=\"2\" class=\"tituloInteriorRojo\">Acreedores Pendientes por Firmar</td>");
		sb.append("</tr><tr>");
		sb.append("	<td width=\"12%\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">");
		sb.append("Nombre, Denominaci&oacute;n o Raz&oacute;n Social</td>");
		// sb.append("<td width=\"8%\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">");
		// sb.append("Folio Electr&oacute;nico</td>");
		sb.append("<td width=\"12%\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">RFC, CURP, Documento que acredita su legal estancia en el pa&iacute;s </td>");
		sb.append("	<td width=\"18%\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">Opciones</td>");
		sb.append("</tr>");
		Iterator<AcreedorTO> it = listaAcreedores.iterator();
		AcreedorTO acreedorTO;
		while (it.hasNext()) {
			acreedorTO = it.next();
			sb.append("	<tr>");
			String rfcurdoc = "";
			if (acreedorTO.getRfc() != null) {
				rfcurdoc = acreedorTO.getRfc();
			} else {
				rfcurdoc = acreedorTO.getCurp();
			}
			sb.append("<td class=\"cuerpo1TablaResumen\">"
					+ notNull(acreedorTO.getNombreCompleto()) + "</td>");
			sb.append("<td class=\"cuerpo1TablaResumen\">" + notNull(rfcurdoc)
					+ "</td>");
			// sb.append("<td class=\"cuerpo1TablaResumen\">"+acreedorTO.getRfc()+"</td>");
			sb.append("<td class=\"cuerpo1TablaResumen\"> ");

			sb.append("<table> <tr> ");
			sb.append(" <td> "
					+ " <a style=\"cursor: pointer;\"  onclick=\"firmaAcreedorRepresentado('"
					+ elementId + "','" + idPersona + "','"
					+ acreedorTO.getIdPersona() + "','"
					+ acreedorTO.getIdTramitePendiente() + "')\"> Firmar </a>"
					+ " </td>  ");
			sb.append("<td>  <a style=\"cursor: pointer;\" onclick=\"eliminaParteAcreedorRepresentado('"
					+ elementId
					+ "','"
					+ idPersona
					+ "','"
					+ acreedorTO.getIdPersona() + "')\"> | Eliminar </a></td>"

					+ "</tr> </table>");

			// " <td>  <a style=\"cursor: pointer;\" onclick=\"cargaParteAcreedorRepresentado('"
			// + elementId
			// + "','"
			// + idPersona
			// + "','"
			// + acreedorTO.getIdPersona()
			// + "')\"> | Modificar </a> </td> "

			sb.append("</td></tr>");
		}

		sb.append("</table>");
		return sb;
	}

	// Tramites masivos
	private StringBuffer writeTablaTramitesMasivosSinFirma(String elementId,
			String idPersona, List<TramitesMasivosTO> listaTramitesMasivos) {
		StringBuffer sb = new StringBuffer();

		PrivilegiosDAO privilegiosDAO = new PrivilegiosDAO();
		PrivilegiosTO privilegiosTO = new PrivilegiosTO();
		privilegiosTO.setIdRecurso(new Integer(4));
		UsuarioTO usuarioTO = new UsuarioTO();
		PersonaTO to = new PersonaTO();
		to.setIdPersona(Integer.valueOf(idPersona));
		usuarioTO.setPersona(to);
		privilegiosTO = privilegiosDAO.getPrivilegios(privilegiosTO, usuarioTO);

		MyLogger.Logger.log(Level.INFO,
				"writeTablaTramitesMasivos sin firma : id de la persona: "
						+ idPersona);
		System.out
				.println("writeTablaTramitesMasivos sin firma : id de la persona: "
						+ idPersona);

		sb.append("<table><tr>");
		sb.append("<td colspan=\"3\" class=\"tituloInteriorRojo\">Acreedores Multiples Pendientes por Firmar</td>");
		sb.append("</tr><tr>");

		sb.append(" <td width=\"10%\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">Nombre</td>");
		sb.append(" <td width=\"14%\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">Nombre, Denominaci&oacute;n o Raz&oacute;n Social</td>");
		sb.append(" <td width=\"14%\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">RFC, Documento que acredita su legal estancia en el pa&iacute;s</td>");
		sb.append("	<td width=\"8%\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">Tramites Correctos</td>");
		sb.append("	<td width=\"8%\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">Tramites Incorrectos</td>");
		sb.append("	<td width=\"14%\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">Opciones</td>");

		sb.append("</tr>");
		Iterator<TramitesMasivosTO> it = listaTramitesMasivos.iterator();
		TramitesMasivosTO tramiteTO;
		while (it.hasNext()) {
			tramiteTO = it.next();
			sb.append("	<tr>");
			sb.append("<td class=\"cuerpo1TablaResumen\">"
					+ notNull(tramiteTO.getNombre()) + "</td>");

			sb.append("<td class=\"cuerpo1TablaResumen\">"
					+ notNull(tramiteTO.getNombreArchivo()) + "</td>");

			sb.append("<td class=\"cuerpo1TablaResumen\">"
					+ notNull(tramiteTO.getRfc()) + "</td>");

			sb.append("<td class=\"cuerpo1TablaResumen\">"
					+ notNull(tramiteTO.getNumCorrectos()) + "</td>");

			sb.append("<td class=\"cuerpo1TablaResumen\">"
					+ notNull(tramiteTO.getNumErroneos()) + "</td>");

			sb.append("<td class=\"cuerpo1TablaResumen\"> ");

			sb.append("<table> <tr> ");
			sb.append(" <td> "
					+ " <a style=\"cursor: pointer;\"  onclick=\"firmaAcreedorRepresentado('"
					+ elementId + "','" + idPersona + "','"
					+ tramiteTO.getIdPersona() + "','"
					+ tramiteTO.getIdFirmaMasiva() + "')\"> Firmar </a>"
					+ " </td>  ");
			sb.append(" <td>  <a style=\"cursor: pointer;\" onclick=\"eliminaTramiteMasivoSinFirmar('"
					+ elementId
					+ "','"
					+ idPersona
					+ "','"
					+ tramiteTO.getIdFirmaMasiva()
					+ "')\"> | Eliminar </a></td>"

					+ "</tr> </table>");

			sb.append("</td></tr>");
		}

		sb.append("</table>");
		return sb;
	}

	private String notNull(Integer num) {
		if (num == null) {
			return "";
		} else {
			return num.toString();
		}
	}

	// Acreedor
	private StringBuffer writeTablaAcreedor(String elementId, String tramiteId,
			List<AcreedorTO> listaAcreedores, String idPersona, String isInscripcion) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("<div class=\"row\" >");
		sb.append("<table id=\"tableAcreedores\" class=\"striped\" >");
		sb.append("<thead>");
		sb.append("<tr>");
		sb.append("<th>Nombre, Denominaci&oacute;n o Raz&oacute;n Social</th>");
		sb.append("<th>No. Identificaci&oacute;n \\ No. Identificaci&oacute;n Tributaria</th>");
		sb.append("<th>Opciones</th>");
		sb.append("</tr>");
		sb.append("</thead>");
		sb.append("<tbody>");
		Iterator<AcreedorTO> it = listaAcreedores.iterator();
		AcreedorTO acreedorTO;
		while (it.hasNext()) {
			acreedorTO = it.next();
			sb.append("	<tr>");
			sb.append("<td>" + acreedorTO.getNombreCompleto() + "</td>");
			if(acreedorTO.getTipoPersona().equalsIgnoreCase("PM")) 
				sb.append("<td>" + notNull(acreedorTO.getRfc()) + "</td>");
			else 
				sb.append("<td>" + notNull(acreedorTO.getCurp()) + "</td>");
			// sb.append("<td class=\"cuerpo1TablaResumen\">"+acreedorTO.getRfc()+"</td>");
			sb.append("<td> <table> <tr> <td> <a class=\"btn waves-effect red darken-4\" data-toggle=\"tooltip\" title=\"Eliminar\" onclick=\"eliminaParteAcreedor('"
					+ elementId
					+ "','"
					+ tramiteId
					+ "','"
					+ idPersona
					+ "','"
					+ acreedorTO.getIdAcreedor() + "')\"><i class=\"material-icons\">delete</i></a></td> " +
					 "<td> <a class=\"btn waves-effect indigo\" data-position=\"bottom\" data-toggle=\"tooltip\" title=\"Modificar\" onclick=\"cargaParteAcreedor('"
					 + elementId
					 + "','"
					 + tramiteId
					 + "','"
					 + acreedorTO.getIdAcreedor()
					 + "','"
					 + acreedorTO.getIdAcreedor()
					 + "','"
					 + isInscripcion
					 + "')\"><i class=\"material-icons\">edit</i></a></td> " +
					" </tr> </table></td>");
			sb.append("</tr>");
		}

		sb.append("</tbody>");
		sb.append("</table></div>");
                sb.append("<strong><h5 style='text-align: center;' >Si desea modificar un deudor, se recomienda que lo elimine y lo ingrese nuevamente.</h5><strong>");
		return sb;
	}

	private StringBuffer writeFormularioAcreedor(String elementID,
			String idTramite, String idPersona, AltaParteTO altaParteTO,
			boolean esAutoridad, String isInscripcion) {
		StringBuffer sb = new StringBuffer();
		String autoridad = null;
		
		if (esAutoridad) {
			autoridad = "1";
		} else {
			autoridad = "0";
		}

		sb.append("<div class=\"row\">");
		sb.append("<a onclick=\"agregarNuevo()\" style=\"cursor: pointer;\" class=\"tituloHeader2\"> + Agregar Persona</a>");
		sb.append("</div>");
		if (altaParteTO.getIdPersona() == null) {
			sb.append("<div class=\"row\" style=\"visibility: hidden; display: none\" id=\"agreNuevo\">");
		} else {
			if (altaParteTO.getIdPersona() == 0) {				
				sb.append("<div class=\"row\" style=\"visibility: hidden; display: none\" id=\"agreNuevo\">");
			} else {				
				sb.append("<div class=\"row\" id=\"agreNuevo\">");
			}
		}
		
		List<TipoPersona> listaPersona = new ArrayList<TipoPersona>();
		TipoPersona persona = new TipoPersona();
		persona.setId("PM");
		persona.setDesc("Persona Jur&iacute;dica");
		listaPersona.add(persona);
		persona = new TipoPersona();
		persona.setId("PF");
		persona.setDesc("Persona Individual");
		listaPersona.add(persona);
		
		NacionalidadDAO dao = new NacionalidadDAO();
		List<NacionalidadTO> listaNacionalidades = dao.getNacionalidades();
		
		sb.append(writeTiposPersonaAcreedor(listaPersona, altaParteTO,
				esAutoridad));
		sb.append(writeNacionaliadAcreedor(listaNacionalidades, altaParteTO,
				esAutoridad));
		
		if(altaParteTO.getTipoPersona()!=null && altaParteTO.getTipoPersona().equalsIgnoreCase("PM"))
			sb.append("<div id=\"personaMoralTRA\">");
		else 
			sb.append("<div id=\"personaMoralTRA\" style=\"visibility: hidden; display: none\">");		
		sb.append(writePersonaMoralAcreedor(altaParteTO, isInscripcion));
		sb.append("</div>");
		
		if(altaParteTO.getTipoPersona()!=null && altaParteTO.getTipoPersona().equalsIgnoreCase("PM"))
			sb.append("<div id=\"personaFisicaTRA\" style=\"visibility: hidden; display: none\">");
		else
			sb.append("<div id=\"personaFisicaTRA\">");		
		sb.append(writePersonaFisicaAcreedorRepresentado(altaParteTO));	
		sb.append("</div>");
		
		sb.append(writeTelefonoCorreoA(altaParteTO));
		sb.append("<br />");
		//sb.append(writeResidenciaAcreedor(listaNacionalidades, altaParteTO));
		
		sb.append(writeCalleExtranjeraA(altaParteTO));
		
		if (altaParteTO.getIdPersona() == 0) {
			if (esAutoridad) {
				sb.append(" <script>   document.getElementById('nacionalidadA').value = 1; </script> <center><div class='row'><input class=\"btn btn-large waves-effect indigo\" type=\"button\"  value=\"Aceptar\" onclick=\"guardaParteAcreedor('"
						+ elementID
						+ "','"
						+ idTramite
						+ "','"
						+ idPersona
						+ "','0','1'"
						+ ",'"
						+ isInscripcion
						+ "')\" /><input class=\"btn btn-large waves-effect indigo\" type=\"button\" value=\"Ocultar\" onclick=\"ocultar()\"/></div></center>  ");
			} else {
				sb.append(" <script>   document.getElementById('nacionalidadA').value = 1; </script> <center><div class='row'><input type=\"button\" class=\"btn btn-large waves-effect indigo\" value=\"Aceptar\" onclick=\"guardaParteAcreedor('"
						+ elementID
						+ "','"
						+ idTramite
						+ "','"
						+ idPersona
						+ "','0','0'"
						+ ",'"
						+ isInscripcion
						+ "')\" /> <input type=\"button\" class=\"btn btn-large waves-effect indigo\" value=\"Ocultar\" onclick=\"ocultar()\"/></div></center>  ");
			}
		} else {
			if (esAutoridad) {
				sb.append("<center><div class='row'><input type=\"button\" class=\"btn btn-large waves-effect indigo\" value=\"Aceptar\" onclick=\"modificaParteAcreedor('"
						+ elementID
						+ "','"
						+ idTramite
						+ "','"
						+ idPersona
						+ "','"
						+ altaParteTO.getIdPersona()						
						+ "','"
						+ isInscripcion
						+ "')\" /> <input type=\"button\" class=\"btn btn-large waves-effect indigo\" value=\"Ocultar\" onclick=\"ocultar()\"/></div></center>");
			} else {
				sb.append("<center><div class='row'><input type=\"button\" class=\"btn btn-large waves-effect indigo\" value=\"Aceptar\" onclick=\"modificaParteAcreedor('"
						+ elementID
						+ "','"
						+ idTramite
						+ "','"
						+ idPersona
						+ "','"
						+ altaParteTO.getIdPersona()						
						+ "','"
						+ isInscripcion
						+ "')\" /> <input type=\"button\" class=\"btn btn-large waves-effect indigo\" value=\"Ocultar\" onclick=\"ocultar()\"/></div></center>");
			}

		}		
				
		sb.append("</div></div>");
		
		return sb;
	}

	private StringBuffer writeFormularioAcreedorRepresentado(String elementID,
			String idPersona, AltaParteTO altaParteTO, boolean uboFirmados,
			boolean esAutoridad) {
		
		List<TipoTo> listaTipo = new ArrayList<TipoTo>();
		TipoTo tipo = new TipoTo();
		tipo.setIdTipo("TI");
		tipo.setDesTipo("Seleccione");
		listaTipo.add(tipo);
		tipo = new TipoTo();
		tipo.setIdTipo("SM");
		tipo.setDesTipo("Sociedad Mercantil");
		listaTipo.add(tipo);
		tipo = new TipoTo();
		tipo.setIdTipo("OT");
		tipo.setDesTipo("Otras");
		listaTipo.add(tipo);
		
		String autoridad = null;
		if (esAutoridad) {
			autoridad = "1";
		} else {
			autoridad = "0";
		}

		StringBuffer sb = new StringBuffer();
		MyLogger.Logger
				.log(Level.INFO,
						"RUG:::::: entro a writeFormularioAcreedorRepresentado--------");

		sb.append(" <table>");
		sb.append(" <tr><td><a onclick=\"agregarNuevo()\" style=\"cursor: pointer;\" class=\"tituloHeader2\"> + Agregar Acreedor</a></td></tr>");
		if (altaParteTO.getIdPersona() == 0) {
			sb.append("<tr><td style=\"visibility: hidden; display: none\" id=\"agreNuevo\">");
		} else {
			sb.append("<tr><td id=\"agreNuevo\">");
		}
		sb.append("<table>");
		List<TipoPersona> listaPersona = new ArrayList<TipoPersona>();
		TipoPersona persona = new TipoPersona();
		persona.setId("PF");
		persona.setDesc("Persona Individual");		
		listaPersona.add(persona);
		persona = new TipoPersona();
		persona.setId("PM");
		persona.setDesc("Persona Jur铆dica");
		listaPersona.add(persona);
		NacionalidadDAO dao = new NacionalidadDAO();
		List<NacionalidadTO> listaNacionalidades = dao.getNacionalidades();

		sb.append("<tr><td><table width=\"600\">");
		sb.append("<tr><td height=\"32\" class=\"encabezadoTablaResumen\">");
		sb.append(" Acreedor Garantizado");
		sb.append("</td></tr>");
		sb.append("<tr><td>");

		sb.append("<tr><td height=\"32\">");
		sb.append("<table align=\"center\" class=\"nota\" style=\"width: 650px\">");
		sb.append("<tr>");
		sb.append("	<td align=\"center\" class=\"imgNota\"> <br><br><br><br><br>");
		sb.append("		<img src=\"/Rug/resources/imgs/ico_nota.png\" >");
		sb.append("		");
		sb.append("	</td>");
		sb.append("<td align=\"justify\" class=\"contenidoNotaDwr\">"
				+ "La persona o personas en cuyo favor el deudor garante o por la ley constituye una garant铆a mobiliaria, con o sin posesi贸n, ya sea en el beneficio propio o de un tercero."
				+ "</td>");
		sb.append("		</tr>");
		sb.append("</table>");
		/** esto no aplica
		sb.append(" <tr> <td class=\"tituloDomicilio\" >");
		sb.append("<input  type=\"checkbox\"  name=\"acreedorInscribe\" id=\"acreedorInscribe\" onclick=\"cambiaInscribe()\"> Seleccione en el caso del Arrendamiento Financiero ya que 茅ste se debe inscribir tambi茅n en el folio electr贸nico del arrendador");
		sb.append("</td> </tr>");
		**/
		sb.append(" </td></tr>");
		sb.append(writeTiposPersonaAcreedorRep(listaPersona, altaParteTO,
				esAutoridad));
		sb.append(" </td></tr>");
		
		sb.append(" <tr><td>");
		sb.append(writeNacionaliadAcreedorRep(listaNacionalidades, altaParteTO,
				esAutoridad));
		sb.append(" </td></tr>");
		
		/*Inicia Generacion de la nueva funcionalidad tipo sociedad y folio electronico*/
		
		/** no aplica
		sb.append("	<tr id=\"personaTipoSociedadAC\"><td>");
		sb.append(writeTipoAcreedores(listaTipo, altaParteTO));
		sb.append("	</td></tr>");
		
		
		sb.append("		<tr id=\"folioElectronicoTRAC\"><td>");
		sb.append(writeFolioElectronicoAcreedores(altaParteTO));
		sb.append("		</td></tr>");
		*/
		
		// numero pais origen miguel
		sb.append(" <tr> <td  id=\"trtdoptogantefisicoAcreedores\"  style= \"padding-left: 39px;\" \"visibility: hidden; display: none\" >");
		sb.append("<input  type=\"checkbox\"  name=\"optotorganteAC\" id=\"optotorganteAC\" onclick=\"cambiaFolioElectronicoAcreedor()\"> El Acreedor cuenta con Folio Electr贸nico en el RUG <span id=\"anotacionFolioElectronico\" class=\"ayuda\"> </span>");
		sb.append("</td> </tr>");
		
		sb.append("<tr id=\"folioIDExtAC\" style=\"visibility: hidden; display: none\"><td> ");
		sb.append("<table> ");
		sb.append("<tr><td style=\"padding-left: 16px;\" class=\"tituloHeader2\"> ");
		sb.append(" Introduzca el Folio Electr贸nico :");
		sb.append("</td></tr>");
		sb.append(" <tr> <td style=\"padding-left: 35px;\"> ");
		sb.append(" <input type=\"text\" name=\"folioExistenteAC\" id=\"folioExistenteAC\" onkeydown=\"desactivaBoton()\"/>");
		sb.append(" </td> </tr> ");
		sb.append(" <tr> <td> <table>");
		sb.append("		<tr> ");
		sb.append("			<td style=\"padding-left: 34px;\" class=\"textoEjemplo\">Ejemplo&nbsp;</td><td>");
		sb.append("				<table>");
		sb.append("					<tr>");
		sb.append("						<td class=\"textoGris\"  align=\"justify\">a) Persona F铆sica:  \"R20101026B040\"</td>");
		sb.append("					</tr>");
		sb.append("					<tr>");
		sb.append("						<td class=\"textoGris\" align=\"justify\">b) Persona Moral:  \"34011*7\"</td>");
		sb.append("					</tr>");
		sb.append("				</table>");
		sb.append("			</td> ");
		sb.append("		</tr> ");
		sb.append("</table> </td></tr> ");
		sb.append("<tr><td style=\"padding-left: 59px;\" id=\"buttonValidar\"> ");
		sb.append("<input class=\"boton_rug\" type=\"button\" value=\"Validar\" onclick=\"buscaFolioElectronicoAltaAcreedorRepresentado()\" /> ");
		sb.append("</td></tr> ");
		sb.append("<tr><td id=\"resBusqueda\"> ");
		sb.append("</td></tr> ");
		sb.append("</table> ");
		sb.append("</td></tr> ");
		sb.append("<tr><td  id=\"folioIDNoExtAC\">");
		sb.append("<table>");
		
		
		
		sb.append("		<tr><td id=\"NIFpaisAC\" style=\"visibility: hidden; display: none\">");
		sb.append(NIFPaisOrigenAcreedores(altaParteTO));
		sb.append("		</td></tr>");
		// numero pais origen miguel
		
		/*Termina Generacion de la nueva funcionalidad tipo sociedad y folio electronico*/
						
		sb.append(" <tr id=\"personaFisicaTRA\"> <td>");
		sb.append(writePersonaFisicaAcreedorRepresentado(altaParteTO));
		sb.append(" </td></tr>");
		
		sb.append(" <tr id=\"personaMoralTRA\" style=\"visibility: hidden; display: none\"><td>");
		sb.append(writePersonaMoralAcreedor(altaParteTO,"0"));
		sb.append(" </td></tr>");
		
		sb.append(" <tr id=\"rfcIDA\"><td>");
		sb.append(writeRFCAcreedor(altaParteTO, esAutoridad));
		sb.append(" </td></tr>");
		
		sb.append(" <tr id=\"personaMoralTRA\" style=\"visibility: hidden; display: none\"><td>");
		sb.append(writePersonaMoralAcreedor(altaParteTO,"0"));
		sb.append(" </td></tr>");
		
		//Modificacion 2014 CURP
		/** no aplica
		sb.append("		<tr><td id=\"docExtranjeroTRAE\"style=\"visibility: hidden; display: none\">");
		sb.append(writeDocExtranjeroAcreedor(altaParteTO));
		sb.append("		</td></tr>");
		
		sb.append("<tr><td style=\"visibility: hidden; display: none\">");
		sb.append(writeFolioElectronicoAcreedor(altaParteTO));
		sb.append("</td></tr>");*/

		sb.append("<tr><td><table width=\"600\">");
		sb.append("<tr><td class=\"tituloHeader1\">");
		sb.append("Domicilio para efectos del RUG");
		sb.append("</td></tr>");
		sb.append(" <tr><td>");
		sb.append(writeResidenciaAcreedor(listaNacionalidades, altaParteTO));
		sb.append(" </td></tr>");
		sb.append("<tr><td><table>");
		/** no aplica
		sb.append("<tr><td id=\"calleNac\">");
		sb.append(writeCalleRep(altaParteTO, esAutoridad));
		sb.append("</td></tr>");
		**/
		sb.append("<tr><td id=\"calleExt\">");
		sb.append(writeCalleExtranjera(altaParteTO));
		sb.append("</td></tr>");
		sb.append("</table></td></tr>");

		sb.append("</td></tr></table>");

		if (altaParteTO.getIdPersona() == 0) {
			if (esAutoridad) {
				sb.append("<tr><td>  <table><tr><td><input type=\"button\" class=\"boton_rug\" value=\"Aceptar\" onclick=\"guardaParteAcreedorRepresentado('"
						+ elementID
						+ "','"
						+ idPersona
						+ "','0','1');\" /></td><td><input type=\"button\" class=\"boton_rug\" value=\"Ocultar\" onclick=\"ocultarAcreRep('"
						+ elementID
						+ "','"
						+ idPersona
						+ "','0')\"></td></tr></table><script>   document.getElementById('nacionalidadA').value = 1; </script> </td></tr>");

			} else {

				sb.append("<tr><td> <script>   document.getElementById('nacionalidadA').value = 1; </script>  <table><tr><td><input type=\"button\" class=\"boton_rug\" value=\"Aceptar\" onclick=\"guardaParteAcreedorRepresentado('"
						+ elementID
						+ "','"
						+ idPersona
						+ "','0','0');\" /></td><td><input type=\"button\" class=\"boton_rug\" value=\"Ocultar\" onclick=\"ocultarAcreRep('"
						+ elementID
						+ "','"
						+ idPersona
						+ "','0')\"></td></tr></table><script>   document.getElementById('nacionalidadA').value = 1; </script> </td></tr>");
			}
		} else {
			if (esAutoridad) {
				sb.append("<tr><td><table><tr><td><input type=\"button\" class=\"boton_rug\" value=\"Aceptar\" onclick=\"modificaParteAcreedorRepresentado('"
						+ elementID
						+ "','"
						+ idPersona
						+ "','"
						+ altaParteTO.getIdPersona()
						+ "','1');\" /></td><td><input type=\"button\" class=\"boton_rug\" value=\"Ocultar\" onclick=\"ocultarAcreRep('"
						+ elementID
						+ "','"
						+ idPersona
						+ "','0')\"></td></tr></table></tr>");
			} else {
				sb.append("<tr><td><table><tr><td><input type=\"button\" class=\"boton_rug\" value=\"Aceptar\" onclick=\"modificaParteAcreedorRepresentado('"
						+ elementID
						+ "','"
						+ idPersona
						+ "','"
						+ altaParteTO.getIdPersona()
						+ "','0');\" /></td><td><input type=\"button\" class=\"boton_rug\" value=\"Ocultar\" onclick=\"ocultarAcreRep('"
						+ elementID
						+ "','"
						+ idPersona
						+ "','0')\"></td></tr></table></tr>");
			}

		}
		if (uboFirmados) {

			sb.append("</table></td></tr>");
			sb.append("</table>");
		}

		MyLogger.Logger.log(Level.INFO,
				"::::::::::::: nacionalidad de la persona que se va a modificar  ::::::: "
						+ altaParteTO.getIdNacionalidad());
		MyLogger.Logger.log(Level.INFO,
				"::::::::::::: Pais de residencia al que se va a modificar       ::::::: "
						+ altaParteTO.getIdPaisResidencia());

		sb.append(" <script> document.getElementById('residenciaA').value = "
				+ altaParteTO.getIdPaisResidencia()
				+ "; document.getElementById('nacionalidadA').value = "
				+ altaParteTO.getIdNacionalidad()
				+ "; cambiaResidencia(); cambiaNacionalidadARep(" + autoridad
				+ ");</script> ");

		if (altaParteTO.getTipoPersona() != null) {
			String tipoPersona = "PM";
			if (altaParteTO.getTipoPersona().equals("PF")) {
				tipoPersona = "PF";
			}

			sb.append(" <script> document.getElementById('tipoPersonaA').value = '"
					+ tipoPersona
					+ "'; document.getElementById('nacionalidadA').value = "
					+ altaParteTO.getIdNacionalidad()
					+ "; document.getElementById('residenciaA').value = "
					+ altaParteTO.getIdPaisResidencia()
					+ ";  cambiaNacionalidadARep("
					+ autoridad
					+ "); cambiaTipoPersonaARep("
					+ autoridad
					+ ");  cambiaResidencia(); </script>");
		}

		if (altaParteTO.getCodigoPostal() != null) {
			MyLogger.Logger.log(Level.INFO, "si tubo codigo postal: "
					+ altaParteTO.getCodigoPostal());
			sb.append(" <script> document.getElementById('codigoPostal').value = '"
					+ altaParteTO.getCodigoPostal()
					+ "'; getDireccionByCP(); </script> ");
			if (altaParteTO.getIdColonia() != null && altaParteTO.getIdColonia() != 0 ) {
				MyLogger.Logger.log(Level.INFO, "Encontro ID COLONIA");
				sb.append(" <script> setTimeout(\"document.getElementById('idColonia').value = '"
						+ altaParteTO.getIdColonia() + "'\",4000); </script> ");
			} else if (altaParteTO.getIdLocalidad() != null) {
				MyLogger.Logger.log(Level.INFO, "Encontro ID Localidad");
				sb.append(" <script>setTimeout(\"document.getElementById('idLocalidad').value = '"
						+ altaParteTO.getIdLocalidad()
						+ "'\",4000); </script> ");
			}
		} else {
			MyLogger.Logger.log(Level.INFO, "NO tiene codigo postal");
		}

		sb.append(" <script> cambiaNacionalidadARep(" + autoridad
				+ "); cambiaTipoPersonaARep(" + autoridad
				+ "); cambiaResidencia(); </script>");

		return sb;
	}
	
	//Metodo de inicio para la funcion de tipo mantenimiento acreedores
		
	private StringBuffer writeTipoAcreedores(List<TipoTo> listaTipo,
			AltaParteTO altaParteTO) {
		StringBuffer sb = new StringBuffer();

		try {
			Iterator<TipoTo> it = listaTipo.iterator();

			sb.append("<script type=\"text/javascript\">");
			sb.append("    $(function () {");
			sb.append("        $('.ayudaTipoSociedad').ayuda({");
			sb.append("            url: '/Rug/comun/publico/help.do',");
			sb.append("            ico: '/Rug/resources/imgs/documentinfo.png',");
			sb.append("            width: 515,");
			sb.append("            height: 450");
			sb.append("        })    ");
			sb.append("        })    ");
			sb.append("</script> ");

			sb.append(" <table style=\"visibility: visible; display: block\">");
			sb.append("			<tr>");

			sb.append("				<td style=\"padding-left: 20px;\" colspan=\"2\" class=\"tituloHeader2\" align=\"left\"><span");
			sb.append("					id=\"TipoSociedad\" class=\"ayudaTipoSociedad\" >Tipo :</span>");
			sb.append("				</td>");

			sb.append("			</tr>");

			sb.append("			<tr>");
			sb.append("			<td style=\"padding-left: 30px;\">");

			sb.append("	<dl style=\"width: 120px\">");
			sb.append("	<dd style=\"width: 120px\">");

			sb.append("<select id='TipoDA' name='TipoDA' onchange='cambiaTipoDAcreedor()'> ");

			TipoTo tipoTo;
			while (it.hasNext()) {
				tipoTo = it.next();
				sb.append("<option ");
				sb.append("value='");
				sb.append(tipoTo.getIdTipo());
				sb.append("'>");
				sb.append(tipoTo.getDesTipo());
				sb.append("</option>");
			}
			sb.append("</select> ");
			// sb.append(" <span class=\"hint\"> <div class=\"cerrar\"> <a onclick=\"this.parentNode.getElementsByTagName('span')[0].style.display = 'none';\" style=\"cursor: pointer;\">x</a> </div> <div class=\"contenido\">'Otras' Ejemplo RFC: RFT632393H21</div><span class=\"hint-pointer\">&nbsp;</span></span>");
			sb.append("	</dd>");
			sb.append("	</dl>");
			sb.append("</td></tr> </table> </td>");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb;
	}
	
	private StringBuffer NIFPaisOrigenAcreedores(AltaParteTO altaParteTO) {
		StringBuffer sb = new StringBuffer();

		sb.append("<script type=\"text/javascript\">");
		sb.append("    $(function () {");
		sb.append("        $('.ayudaNIF').ayuda({");
		sb.append("            url: '/Rug/comun/publico/help.do',");
		sb.append("            ico: '/Rug/resources/imgs/documentinfo.png',");
		sb.append("            width: 515,");
		sb.append("            height: 370");
		sb.append("        })    ");
		sb.append("        })    ");
		sb.append("</script> ");

		sb.append(" <table style=\"visibility: visible; display: block\">");
		sb.append("			<tr>");
		sb.append("				<td style=\"padding-left: 20px;\" colspan=\"2\" class=\"tituloHeader2\" align=\"left\"><span");
		sb.append("					id=\"anotacionNif\" class=\"ayudaNIF\" >N煤mero de Identificaci贸n Fiscal en el pa铆s de origen :</span>");
		sb.append("				</td>");
		sb.append("			</tr>");

		sb.append("			<tr>");

		sb.append("<td id=\"tipoObliga\" style=\"padding-left: 18px;\" class=\"texto_azul\" width=\"1px\"></td>");

		sb.append("				<td style=\"padding-left: 10px;\">");
		sb.append("	<dl style=\"width: 120px\">");
		sb.append("	<dd style=\"width: 120px\">");

		sb.append("	 <input type=\"text\" maxlength=\"14\" value=\""
				+ notNull(altaParteTO.getRfc())
				+ "\" name=\"nif\" id=\"nif\" onkeyup=\"this.value = this.value.toUpperCase()\" maxlength=\"16\" >");

		sb.append(" <span class=\"hint\"> <div class=\"cerrar\"> <a onclick=\"this.parentNode.getElementsByTagName('span')[0].style.display = 'none';\" style=\"cursor: pointer;\">x</a> </div> <div class=\"contenido\"> Tax Id Number Estados Unidos</div><span class=\"hint-pointer\">&nbsp;</span></span>");
		sb.append("	</dd>");
		sb.append("	</dl>");
		sb.append("					</td>");
		sb.append("			</tr>						");
		sb.append("		</table>");

		return sb;
	}

	private StringBuffer writeFolioElectronicoAcreedores(AltaParteTO altaParteTO) {
		StringBuffer sb = new StringBuffer();
		sb.append("<table>");

		sb.append("<script type=\"text/javascript\">");
		sb.append("    $(function () {");
		sb.append("        $('.ayuda').ayuda({");
		sb.append("            url: '/Rug/comun/publico/help.do',");
		sb.append("            ico: '/Rug/resources/imgs/documentinfo.png',");
		sb.append("            width: 510,");
		sb.append("            height: 400");
		sb.append("        })    ");
		sb.append("        })    ");
		sb.append("</script> ");

		sb.append("			<tr><td colspan=\"2\" class=\"tituloHeader2\" align=\"left\">");

		sb.append("             <span id=\"anotacionFolioElectronico\" class=\"ayuda\">Folio Electr贸nico : </span></td>");

		sb.append("			</tr>");

		sb.append("			<tr>"
				+ "					<td> "
				+ "						<table>"
				+ "							<tr>"
				+ "								<td id=\"folioxxx\" style=\"padding-left: 11px;\" class=\"texto_azul\" width=\"18px\"></td>"
				+ "								 <td>");
		sb.append("									<dl style=\"width: 260px\">");
		sb.append("										<dd style=\"width: 120px\">");
		sb.append("	 										<input type=\"text\" value=\""
				+ notNull(altaParteTO.getFolioMercantil())
				+ "\" name=\"folioElectronico\" id=\"folioElectronico\" maxlength=\"100\" size=\"50\" >");
		sb.append(" <span class=\"hint\"> <div class=\"cerrar\"> <a onclick=\"this.parentNode.getElementsByTagName('span')[0].style.display = 'none';\" style=\"cursor: pointer;\">x</a> </div> <div class=\"contenido\"> El Folio Electr贸nico es el expediente electr贸nico de un Otorgante en el que se contienen los asientos relativos a Garant铆as Mobiliarias, as铆 como los actos jur铆dicos por los que se constituya un privilegio especial o derecho de retenci贸n sobre bienes muebles a favor de terceros, incluyendo las anotaciones.  Art. 21 fr. XX y 30 bis 1 RRPC. </div><span class=\"hint-pointer\">&nbsp;</span></span>");
		sb.append("	</dd>");
		sb.append("	</dl>");

		sb.append("				</td> </tr> </table> </td> </tr>");
		sb.append("				<td id=\"MensajeFisica\" style=\"visibility: hidden; display: none\">");
		sb.append("				<table style=\"cursor: pointer;\"><tr>");
		sb.append("					<td class=\"ComunicaTexto\" width=\"40\"> Nota : </td>");
		sb.append("					<td class=\"contenidoNota\">Si no cuenta con Folio Electr&oacute;nico, &eacute;ste ser&aacute; generado de forma autom&aacute;tica por el Sistema.</td>");
		sb.append("				</tr></table>");
		sb.append("			</td></tr>");
		sb.append("</table>");

		sb.append("<table>");
		sb.append("		<tr> ");
		sb.append("			<td style=\"padding-left:32px;\" class=\"textoEjemplo\">Ejemplo&nbsp;</td><td>");
		sb.append("				<table>");
		sb.append("					<tr>");
		sb.append("						<td class=\"textoGris\"  align=\"justify\">a) Persona Individual:  \"R20101026B040\"</td>");
		sb.append("					</tr>");
		sb.append("					<tr>");
		sb.append("						<td class=\"textoGris\" align=\"justify\">b) Persona Jur铆dica:  \"34011*7\"I</td>");
		sb.append("					</tr>");
		sb.append("				</table>");
		sb.append("			</td> ");
		sb.append("		</tr> ");
		sb.append("</table>");

		return sb;
	}
	//Metodo de inicio para la funcion de tipo mantenimiento acreedores
	private StringBuffer writeNacionaliadAcreedorRep(
			List<NacionalidadTO> listaNacionalidades, AltaParteTO altaParteTO,
			Boolean esAutoridad) {

		String autoridad = null;
		if (esAutoridad) {
			autoridad = "1";
		} else {
			autoridad = "0";
		}

		StringBuffer sb = new StringBuffer();
		try {
			Iterator<NacionalidadTO> it = listaNacionalidades.iterator();
			sb.append("<table><tr><td style=\"padding-left: 4px;\" colspan=\"2\" class=\"texto_general\" align=\"left\"><span");
			sb.append(" class=\"tituloHeader2\">Nacionalidad :</span>");
			sb.append("</td></tr>");
			sb.append("<tr>"
					+ "		<td>"
					+ "			<table>"
					+ "				<tr>"
					+ "					<td style=\"padding-left: 15px;\" class=\"texto_azul\" width=\"18px\">*</td>"
					+ "					<td>");
			sb.append("<select id='nacionalidadA' name='nacionalidadA' onchange='cambiaNacionalidadARep("
					+ autoridad + ")'> ");

			NacionalidadTO nacionalidadTO;
			while (it.hasNext()) {
				nacionalidadTO = it.next();
				sb.append("<option ");
				sb.append("value='");
				sb.append(nacionalidadTO.getIdNacionalidad());
				if(nacionalidadTO.getIdNacionalidad()==1) {
					sb.append("' selected=\"selected\">");
				} else {
					sb.append("'>");
				}
				sb.append(nacionalidadTO.getDescNacionalidad());
				sb.append("</option>");
			}
			sb.append("</select> ");
			sb.append("</td></tr></table> </td>");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb;
	}
	
	private StringBuffer writeNacionaliadAcreedor(
			List<NacionalidadTO> listaNacionalidades, AltaParteTO altaParteTO,
			Boolean esAutoridad) {

		String autoridad = null;
		if (esAutoridad) {
			autoridad = "1";
		} else {
			autoridad = "0";
		}

		StringBuffer sb = new StringBuffer();
		try {
			Iterator<NacionalidadTO> it = listaNacionalidades.iterator();
			Integer nacSelected = altaParteTO.getIdNacionalidad()==null?1:altaParteTO.getIdNacionalidad();
			
			sb.append("<div class=\"row\">");
			sb.append("<div class=\"input-field col s12\">");
			sb.append("<select id='nacionalidadA' name='nacionalidadA' onchange='cambiaNacionalidadA("
					+ autoridad + ")'> ");
			NacionalidadTO nacionalidadTO;
			while (it.hasNext()) {
				nacionalidadTO = it.next();
				sb.append("<option ");
				sb.append("value='");
				sb.append(nacionalidadTO.getIdNacionalidad());
				if(nacionalidadTO.getIdNacionalidad().compareTo(nacSelected)==0) {
					sb.append("' selected=\"selected\">");
				} else {
					sb.append("'>");
				}						
				sb.append(nacionalidadTO.getDescNacionalidad());
				sb.append("</option>");					
			}
			sb.append("</select> ");
			sb.append("<label for=\"nacionalidadA\">Nacionalidad</label>");
			sb.append("</div>");
			sb.append("</div>");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb;
	}
	
	private StringBuffer writeResidenciaAcreedor(
			List<NacionalidadTO> listaNacionalidades, AltaParteTO altaParteTO) {
		StringBuffer sb = new StringBuffer();
		try {
			Iterator<NacionalidadTO> it = listaNacionalidades.iterator();
			
			sb.append("<div class=\"row\">");
			sb.append("<div class=\"input-field col s12\">");
			sb.append("<select id='residenciaA' name='residenciaA' onchange='cambiaResidencia()'> ");
			NacionalidadTO nacionalidadTO;
			while (it.hasNext()) {
				nacionalidadTO = it.next();
				sb.append("<option ");
				sb.append("value='");
				sb.append(nacionalidadTO.getIdNacionalidad());
				if(nacionalidadTO.getIdNacionalidad()==1) {
					sb.append("' selected=\"selected\">");
				} else {
					sb.append("'>");
				}				
				sb.append(nacionalidadTO.getDescNacionalidad());
				sb.append("</option>");
			}
			sb.append("</select> ");
			sb.append("<label for=\"residenciaA\"><span>Pa&iacute;s de Residencia</span></label>");
			sb.append("</div>");
			sb.append("</div>");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb;
	}

	private StringBuffer writeTiposPersonaAcreedor(
			List<TipoPersona> listaPersona, AltaParteTO altaParteTO,
			Boolean esAutoridad) {

		String autoridad = null;
		if (esAutoridad) {
			autoridad = "1";
		} else {
			autoridad = "0";
		}

		StringBuffer sb = new StringBuffer();
		try {
			Iterator<TipoPersona> it = listaPersona.iterator();
			String tTipoPersona = altaParteTO.getTipoPersona()==null?"PF":altaParteTO.getTipoPersona();

			sb.append("<div class=\"row\">");
			sb.append("<div class=\"input-field col s12\">");
			sb.append("<select id='tipoPersonaA' name='tipoPersonaA' onchange='cambiaTipoPersonaA("
					+ autoridad + ")'> ");
			TipoPersona persona;
			while (it.hasNext()) {
				persona = it.next();
				sb.append("<option ");
				sb.append("value='");
				sb.append(persona.getId());				
				if(persona.getId().equalsIgnoreCase(tTipoPersona)) {
					sb.append("' selected=\"selected\">");
				} else {
					sb.append("'>");
				}				
				sb.append(persona.getDesc());
				sb.append("</option>");
			}
			sb.append("</select> ");
			sb.append("<label for=\"tipoPersonaA\">Seleccione Tipo de Persona</label>");
			sb.append("</div>");
			sb.append("</div>");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb;
	}
	
	private StringBuffer writeTiposPersonaAcreedorRep(
			List<TipoPersona> listaPersona, AltaParteTO altaParteTO,
			Boolean esAutoridad) {

		String autoridad = null;
		if (esAutoridad) {
			autoridad = "1";
		} else {
			autoridad = "0";
		}

		StringBuffer sb = new StringBuffer();
		try {
			Iterator<TipoPersona> it = listaPersona.iterator();

			sb.append(" <script type=\"text/javascript\">");
			sb.append("    $(function () {");
			sb.append("        $('.ayudaTipoPersona2').ayuda({");
			sb.append("            url: '/Rug/comun/publico/help.do',");
			sb.append("            ico: '/Rug/resources/imgs/documentinfo.png',");
			sb.append("            width: 510,");
			sb.append("            height: 400");
			sb.append("        })    ");
			sb.append("        })    ");
			sb.append("</script> ");

			sb.append("<table>	<tr>");
			sb.append("<td colspan=\"2\" class=\"tituloHeader2\" align=\"left\"><span");
			sb.append(" style=\"padding-left: 6px;\" id=\"anotacionTipoPersona\" class=\"ayudaTipoPersona2\">Seleccione Tipo de Persona :</span></td>");
			sb.append(" </tr>"
					+ "	<tr>"
					+ "		<td> "
					+ "			<table>"
					+ "				<tr>"
					+ "					<td style=\"padding-left: 17px;\" class=\"texto_azul\" width=\"18px\">* </td>"
					+ "					<td> 	");
			sb.append("<select id='tipoPersonaA' name='tipoPersonaA' onchange='cambiaTipoPersonaARep("
					+ autoridad + ")'> ");

			TipoPersona persona;
			while (it.hasNext()) {
				persona = it.next();
				sb.append("<option ");
				sb.append("value='");
				sb.append(persona.getId());
				sb.append("'>");				
				sb.append(persona.getDesc());
				sb.append("</option>");
			}
			sb.append("</select> ");
			sb.append(" </td>");
			sb.append("</tr></table> </td>");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb;
	}
	
	private StringBuffer writeFolioElectronicoAcreedor(AltaParteTO altaParteTO) {
		StringBuffer sb = new StringBuffer();
		sb.append("<table>");
		sb.append("			<tr><td colspan=\"2\" class=\"tituloHeader2\" align=\"left\">");
		sb.append("				<span class=\"tituloHeader2\">Folio Electr&oacute;nico:</span></td>");
		sb.append("			</tr>");
		sb.append("			<tr>"
				+ "				<td>	"
				+ "					<table>"
				+ "						<tr>"
				+ "							<td style=\"padding-left: 11px;\" class=\"texto_azul\" width=\"18px\">*</td> ");

		sb.append("				<td class=\"texto_general\"> ");
		sb.append("	 <input type=\"text\" value=\""
				+ notNull(altaParteTO.getFolioMercantil())
				+ "\" name=\"folioElectronicoA\" id=\"folioElectronicoA\" maxlength=\"100\" size =\"50\" >");
		sb.append("				</td>");
		sb.append("				<td id=\"MensajeFisica\" style=\"visibility: hidden; display: none\">");
		sb.append("				<table style=\"cursor: pointer;\"><tr>");
		sb.append("					<td class=\"ComunicaTexto\" width=\"40\"> Nota : </td>");
		sb.append("					<td class=\"ComunicaCampo\">Si no cuenta con Folio Electr&oacute;nico, &eacute;ste ser&iacute;a generado de forma autom&aacute;tica por el Sistema.</td>");
		sb.append("				</tr></table>");
		sb.append("			</td></tr> </table> </td> </tr>");
		sb.append("</table>");
		return sb;
	}

	private StringBuffer writeDocExtranjeroAcreedor(AltaParteTO altaParteTO) {
		StringBuffer sb = new StringBuffer();
		Constants constants = new Constants();
		MyLogger.Logger.log(
				Level.INFO,
				"writeDocExtranjero-altaParteTO-curpdoc-"
						+ notNull(altaParteTO.getCurp()));
		sb.append("<table>");
		sb.append("			<tr>");
		sb.append("				<td style=\"padding-left: 13px\" colspan=\"2\" class=\"texto_general\" align=\"left\" id=\"curpDocA\"><span");
		sb.append("					class=\"textoGeneralRojo\">* CURP :</span></td>");
		sb.append("			</tr>");
		sb.append("			<tr>");
		sb.append("				<td class=\"texto_general\"> <table> <tr><td style=\"padding-left: 13px;\" class=\"texto_azul\" width=\"18px\"></td> <td>");
		sb.append("	 <input type=\"text\" onkeyup=\"this.value = this.value.toUpperCase()\" value=\""
				+ notNull(altaParteTO.getCurp())
				+ "\" name=\"docExtranjeroA\" id=\"docExtranjeroA\" maxlength=\"50\" >");
		sb.append("				</td><td id=\"curpAyudaIDAcre\" style=\"visibility: hidden; display: none\"><a href=\"");
		sb.append(constants.getParamValue(Constants.URL_RENAPO_CURP)); // "http://www.renapo.gob.mx/swb/swb/RENAPO/consultacurp"
																		// +
		sb.append("\" target=\"blank\" style=\"color=#4A766E;\" /> Si no la conoce, consulte su CURP </a></td></tr></table></td>");
		sb.append("			</tr>						");
		sb.append("</table>");
		return sb;
	}

	private StringBuffer writeRFCAcreedor(AltaParteTO altaParteTO,
			boolean esAutoridad) {
		StringBuffer sb = new StringBuffer();

		sb.append(" <script type=\"text/javascript\">");
		sb.append("    $(function () {");
		sb.append("        $('.ayudaRFC3').ayuda({");
		sb.append("            url: '/Rug/comun/publico/help.do',");
		sb.append("            ico: '/Rug/resources/imgs/documentinfo.png',");
		sb.append("            width: 505,");
		sb.append("            height: 170");
		sb.append("        })    ");
		sb.append("        })    ");
		sb.append("</script> ");

		sb.append("<table>");
		sb.append("			<tr>");
		sb.append("				<td style=\"padding-left: 16px;\" colspan=\"2\"  class=\"tituloHeader2\" align=\"left\"><span");
		if (esAutoridad) {
			sb.append("			id=\"anotacionRfc\"  class=\"ayudaRFC3\"	> Documento de Identificaci贸n :</span>");
		} else {
			sb.append("			id=\"anotacionRfc\"  class=\"ayudaRFC3\"	> * Documento de Identificaci贸n :</span>");
		}

		sb.append("				</td>");
		sb.append("			</tr>");
		sb.append("			<tr>");
		sb.append("				<td style=\"padding-left: 35px;\">");
		sb.append("	<dl style=\"width: 105px\">");
		sb.append("	<dd style=\"width: 105px\">");
		sb.append("	 <input type=\"text\" value=\""	+ notNull(altaParteTO.getRfc())
				+ "\" name=\"rfcA\" maxlength=\"14\" size=\"16\" id=\"rfcA\" onkeyup=\"this.value = this.value.toUpperCase()\" style=\"visibility:visible;\">");
		sb.append(" <span class=\"hint\"> <div class=\"cerrar\"> <a onclick=\"this.parentNode.getElementsByTagName('span')[0].style.display = 'none';\" style=\"cursor: pointer;\">x</a> " +
				"</div> <div class=\"contenido\"> N煤mero de registro asignado por el Registro Federal de Contribuyentes. </div><span class=\"hint-pointer\">&nbsp;</span></span>");
		sb.append("	</dd>");
		sb.append("	</dl>");
		sb.append("					</td>");
		sb.append("			</tr>						");
		sb.append("		</table>");

		sb.append("<table>");
		sb.append("		<tr> ");
		sb.append("			<td style=\"padding-left:34px;\" class=\"textoEjemplo\">Ejemplo&nbsp;</td><td>");
		sb.append("				<table>");
		sb.append("					<tr>");
		sb.append("						<td class=\"textoGris\"  align=\"justify\">a) DPI:  10013456789</td>");
		sb.append("					</tr>");
		sb.append("					<tr>");
		sb.append("						<td class=\"textoGris\" align=\"justify\">b) Pasaporte:  H23585858</td>");
		sb.append("					</tr>");
		sb.append("				</table>");
		sb.append("			</td> ");
		sb.append("		</tr> ");
		sb.append("</table>");
		
		// aqui va lo nuevo
		sb.append("<table>");
		sb.append("			<tr>");
		sb.append("				<td style=\"padding-left: 16px;\" colspan=\"2\"  class=\"tituloHeader2\" align=\"left\"><span");
		if (esAutoridad) {
			sb.append("			id=\"extensionRfc\"  class=\"ayudaRFC3\"	> Extendido en :</span>");
		} else {
			sb.append("			id=\"extensionRfc\"  class=\"ayudaRFC3\"	> * Extendido en :</span>");
		}

		sb.append("				</td>");
		sb.append("			</tr>");
		sb.append("			<tr>");
		sb.append("				<td style=\"padding-left: 35px;\">");
		sb.append("	<dl style=\"width: 105px\">");
		sb.append("	<dd style=\"width: 105px\">");
		sb.append("	 <input type=\"text\" value=\""	+ notNull(altaParteTO.getExtencion())
				+ "\" name=\"extencionA\" maxlength=\"14\" size=\"16\" id=\"extencionA\" onkeyup=\"this.value = this.value.toUpperCase()\" style=\"visibility:visible;\">");
		sb.append(" <span class=\"hint\"> <div class=\"cerrar\"> <a onclick=\"this.parentNode.getElementsByTagName('span')[0].style.display = 'none';\" style=\"cursor: pointer;\">x</a> " +
				"</div> <div class=\"contenido\"> N鷐ero de registro asignado por el Registro Federal de Contribuyentes. </div><span class=\"hint-pointer\">&nbsp;</span></span>");
		sb.append("	</dd>");
		sb.append("	</dl>");
		sb.append("					</td>");
		sb.append("			</tr>						");
		sb.append("		</table>");
		
		sb.append("<table>");
		sb.append("			<tr>");
		sb.append("				<td style=\"padding-left: 16px;\" colspan=\"2\"  class=\"tituloHeader2\" align=\"left\"><span");
		sb.append("			id=\"edad\"  class=\"ayudaRFC3\"	> * Edad :</span>");
		sb.append("				</td>");
		sb.append("			</tr>");
		sb.append("			<tr>");
		sb.append("				<td style=\"padding-left: 35px;\">");
		sb.append("	<dl style=\"width: 105px\">");
		sb.append("	<dd style=\"width: 105px\">");
		sb.append("	 <input type=\"text\" value=\""	+ notNull(altaParteTO.getEdad())
				+ "\" name=\"edadA\" maxlength=\"14\" size=\"16\" id=\"edadA\" onkeyup=\"this.value = this.value.toUpperCase()\" style=\"visibility:visible;\">");
		sb.append(" <span class=\"hint\"> <div class=\"cerrar\"> <a onclick=\"this.parentNode.getElementsByTagName('span')[0].style.display = 'none';\" style=\"cursor: pointer;\">x</a> " +
				"</div> <div class=\"contenido\"> N鷐ero de registro asignado por el Registro Federal de Contribuyentes. </div><span class=\"hint-pointer\">&nbsp;</span></span>");
		sb.append("	</dd>");
		sb.append("	</dl>");
		sb.append("					</td>");
		sb.append("			</tr>						");
		sb.append("		</table>");
		
		sb.append("<table>");
		sb.append("			<tr>");
		sb.append("				<td style=\"padding-left: 16px;\" colspan=\"2\"  class=\"tituloHeader2\" align=\"left\"><span");
		sb.append("			id=\"estadoCivil\"  class=\"ayudaRFC3\"	> * Estado Civil :</span>");
		sb.append("				</td>");
		sb.append("			</tr>");
		sb.append("			<tr>");
		sb.append("				<td style=\"padding-left: 35px;\">");
		sb.append("	<dl style=\"width: 105px\">");
		sb.append("	<dd style=\"width: 105px\">");
		sb.append("	 <input type=\"text\" value=\""	+ notNull(altaParteTO.getEstadoCivil())
				+ "\" name=\"estadoCivilA\" maxlength=\"14\" size=\"16\" id=\"estadoCivilA\" onkeyup=\"this.value = this.value.toUpperCase()\" style=\"visibility:visible;\">");
		sb.append(" <span class=\"hint\"> <div class=\"cerrar\"> <a onclick=\"this.parentNode.getElementsByTagName('span')[0].style.display = 'none';\" style=\"cursor: pointer;\">x</a> " +
				"</div> <div class=\"contenido\"> N鷐ero de registro asignado por el Registro Federal de Contribuyentes. </div><span class=\"hint-pointer\">&nbsp;</span></span>");
		sb.append("	</dd>");
		sb.append("	</dl>");
		sb.append("					</td>");
		sb.append("			</tr>						");
		sb.append("		</table>");
		
		sb.append("<table>");
		sb.append("			<tr>");
		sb.append("				<td style=\"padding-left: 16px;\" colspan=\"2\"  class=\"tituloHeader2\" align=\"left\"><span");
		sb.append("			id=\"profesion\"  class=\"ayudaRFC3\"	> * Profesi髇 u Oficio :</span>");
		sb.append("				</td>");
		sb.append("			</tr>");
		sb.append("			<tr>");
		sb.append("				<td style=\"padding-left: 35px;\">");
		sb.append("	<dl style=\"width: 105px\">");
		sb.append("	<dd style=\"width: 105px\">");
		sb.append("	 <input type=\"text\" value=\""	+ notNull(altaParteTO.getProfesion())
				+ "\" name=\"profesionA\" maxlength=\"14\" size=\"16\" id=\"profesionA\" onkeyup=\"this.value = this.value.toUpperCase()\" style=\"visibility:visible;\">");
		sb.append(" <span class=\"hint\"> <div class=\"cerrar\"> <a onclick=\"this.parentNode.getElementsByTagName('span')[0].style.display = 'none';\" style=\"cursor: pointer;\">x</a> " +
				"</div> <div class=\"contenido\"> N鷐ero de registro asignado por el Registro Federal de Contribuyentes. </div><span class=\"hint-pointer\">&nbsp;</span></span>");
		sb.append("	</dd>");
		sb.append("	</dl>");
		sb.append("					</td>");
		sb.append("			</tr>						");
		sb.append("		</table>");

		return sb;
	}

	private StringBuffer writePersonaFisicaAcreedorRepresentado(
			AltaParteTO altaParteTO) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"input-field col s12\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getNombre()) 
				+ "\" type=\"text\" maxlength=\"150\" class=\"validate\" required=\"true\" name=\"nombreA\" id=\"nombreA\" onkeyup=\"this.value = this.value.toUpperCase()\">");
		sb.append("<label name=\"labelNombreA\" id=\"labelNombreA\" for=\"nombreA\">Nombre Completo</label>");
		sb.append("</div>");
		sb.append("</div>");
		
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"input-field col s3\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getCurp()) 
				+ "\" type=\"text\" maxlength=\"40\" class=\"validate tooltipped\" required=\"true\" maxlength=\"30\" name=\"rfcA\" id=\"rfcA\" data-position=\"right\" data-delay=\"50\" data-tooltip=\"Ingrese su n?mero sin espacios en blanco\" onkeypress=\"return aceptaalfa(event);\">");
		sb.append("<label id=\"labelRfcA\" for=\"rfcA\">Documento de Identificaci&oacute;n</label>");
		sb.append("</div>");
		sb.append("<div class=\"col s3\" id=\"buttonValidar\">");
		sb.append("<button type=\"button\" onclick=\"buscaDocumentoAcreedor()\" class=\"btn waves-effect indigo\">Buscar</button>");
		sb.append("</div>");
                 sb.append("<div class=\"col s3\" id=\"buttonValidarInst\" >");
		sb.append("<button type=\"button\" onclick=\"buscaDocumentoDeudorInstitucion(2)\" class=\"btn waves-effect indigo\">Busqueda SAT (Nit)</button>");
		sb.append("</div>");
		sb.append("<div class=\"input-field col s3\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getRfc()) 
				+ "\" type=\"text\" maxlength=\"40\" class=\"validate tooltipped\" required=\"true\" maxlength=\"30\" name=\"nitAF\" id=\"nitAF\" data-position=\"right\" data-delay=\"50\" data-tooltip=\"Ingrese su n?mero sin espacios en blanco\" onkeypress=\"return aceptaalfa(event);\">");
		sb.append("<label id=\"labelNitAF\" for=\"nitAF\">NIT</label>");
		sb.append("</div>");		
		sb.append("</div>");
				
		/*sb.append("<div class=\"row\">");
		sb.append("<div class=\"input-field col s6\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getExtencion()) 
				+ "\" type=\"text\" maxlength=\"100\" class=\"validate\" required=\"true\" name=\"extensionA\" id=\"extensionA\">");
		sb.append("<label id=\"labelExtensionA\" for=\"extensionA\">Extendido en</label>");
		sb.append("</div>");		
		
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"input-field col s6\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getEdad()) 
				+ "\" type=\"text\" maxlength=\"10\" class=\"validate\" required=\"true\" name=\"edadA\" id=\"edadA\">");
		sb.append("<label id=\"labelEdadA\" for=\"edadA\">Edad</label>");
		sb.append("</div>");
		sb.append("</div>");
		
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"input-field col s6\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getEstadoCivil()) 
				+ "\" type=\"text\" maxlength=\"50\" class=\"validate\" required=\"true\" name=\"estadoCivilA\" id=\"estadoCivilA\">");
		sb.append("<label id=\"labelEstadoCivilA\" for=\"estadoCivilA\">Estado Civil</label>");
		sb.append("</div>");		
		
		sb.append("<div class=\"input-field col s6\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getProfesion()) 
				+ "\" type=\"text\" maxlength=\"100\" class=\"validate\" name=\"profesionA\" id=\"profesionA\">");
		sb.append("<label id=\"labelProfesionA\" for=\"profesionA\">Profesi&oacute;n u Oficio</label>");
		sb.append("</div>");
		sb.append("</div>");*/
		
		return sb;
	}

	private StringBuffer writePersonaFisicaAcreedor(AltaParteTO altaParteTO) {
		StringBuffer sb = new StringBuffer();
		sb.append("		<table>");
		sb.append("			<tr>");
		sb.append("						<td style=\"padding-left: 14px\" colspan=\"2\" class=\"texto_general\" align=\"left\"><span");
		sb.append("							class=\"textoGeneralRojo\">Nombre :</span></td>");
		sb.append("</tr>");
		sb.append("	<tr>"
				+ "		<td>"
				+ "			<table>"
				+ "				<tr>"
				+ "					<td style=\"padding-left: 11px;\" class=\"texto_azul\" width=\"18px\">*</td>");
		sb.append("					<td>");
		sb.append("	 <input type=\"text\" onkeyup=\"this.value = this.value.toUpperCase()\" value=\""
				+ notNull(altaParteTO.getNombre())
				+ "\" name=\"nombreA\" id=\"nombreA\" maxlength=\"50\">");
		sb.append("		</td>");
		sb.append("</tr> </table></td> </tr>");
		sb.append("					<tr>");
		sb.append("						");
		sb.append("						<td style=\"padding-left: 14px;\" colspan=\"2\" class=\"texto_general\" align=\"left\"><span");
		sb.append(" class=\"textoGeneralRojo\">Apellido Paterno :</span></td>");
		sb.append("		</tr>");
		sb.append("<tr>"
				+ "		<td>"
				+ "			<table>"
				+ "				<tr>"
				+ "					<td style=\"padding-left: 11px;\" class=\"texto_azul\" width=\"18px\">*</td>");
		sb.append("					<td class=\"texto_general\">");
		sb.append("	 <input type=\"text\" onkeyup=\"this.value = this.value.toUpperCase()\" value=\""
				+ notNull(altaParteTO.getApellidoPaterno())
				+ "\" name=\"apellidoPaternoA\" id=\"apellidoPaternoA\" maxlength=\"50\" >");
		sb.append("									</td>");
		sb.append("							</tr> </table></td> </tr>");
		sb.append("						<tr>");
		sb.append("							<td colspan=\"2\" class=\"texto_general\" align=\"left\" id=\"apellidoMaternoTxtA\"><span");
		sb.append("								class=\"textoGeneralRojo\">  Apellido Materno :</span></td>");
		sb.append("						</tr>");
		sb.append("						<tr>");
		sb.append("							<td style=\"padding-left: 33px;\" class=\"texto_general\">");
		sb.append("	 <input type=\"text\" onkeyup=\"this.value = this.value.toUpperCase()\" value=\""
				+ notNull(altaParteTO.getApellidoMaterno())
				+ "\" name=\"apellidoMaternoA\" id=\"apellidoMaternoA\" maxlength=\"50\" >");
		sb.append("								</td>");
		sb.append("						</tr>");
		sb.append("			</table>");
		return sb;
	}

	private StringBuffer writePersonaMoralAcreedor(AltaParteTO altaParteTO, String isInscripcion) {
		StringBuffer sb = new StringBuffer();

		sb.append("<div class=\"row\">");
		sb.append("<div class=\"input-field col s6\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getRfc()) 
				+ "\" type=\"text\" maxlength=\"40\" class=\"validate tooltipped\" maxlength=\"30\" required=\"true\" name=\"nitA\" id=\"nitA\" data-position=\"right\" data-delay=\"50\" data-tooltip=\"Ingrese su n?mero sin espacios en blanco\" onkeypress=\"return aceptaalfa(event);\">");
		sb.append("<label id=\"labelNitA\" for=\"nitA\">NIT</label>");
		sb.append("</div>");		
		sb.append("<div class=\"col s2\" id=\"buttonValidar\">");
		sb.append("<button type=\"button\" onclick=\"buscaDocumentoAcreedor()\" class=\"btn waves-effect indigo\">Buscar</button>");
		sb.append("</div>");
                 sb.append("<div class=\"col s4\" id=\"buttonValidarInst\" >");
		sb.append("<button type=\"button\" onclick=\"buscaDocumentoDeudorInstitucion(2)\" class=\"btn waves-effect indigo\">Busqueda SAT (Nit)</button>");
		sb.append("</div>");
		sb.append("</div>");
		
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"input-field col s12\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getRazonSocial()) 
				+ "\" type=\"text\" maxlength=\"150\" class=\"validate\" name=\"razonSocialA\" id=\"razonSocialA\" onkeyup=\"this.value = this.value.toUpperCase()\">");
		sb.append("<label id=\"labelRazonSocialA\" for=\"razonSocialA\">Raz&oacute;n o Denominaci&oacute;n Social</label>");
		sb.append("</div>");
		sb.append("</div>");
		
		/*sb.append("<div class=\"row note note-example\">");
		sb.append("<div class=\"col s1\"><p>Ejemplos</p></div>");
		sb.append("<div class=\"col s11\"><ol type=\"a\">");
		sb.append("<li>Denominaci&oacute;n: Electr&oacute;n, S.A.</li>");
		sb.append("<li>Raz&oacute;n Social: L&oacute;pez M&eacute;ndez y C&iacute;a, S. A.</li>");
		sb.append("</ol></div>");
		sb.append("</div>");*/
		
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"input-field col s12\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getInscrita()) 
				+ "\" type=\"text\" maxlength=\"150\" class=\"validate\" name=\"inscritaA\" id=\"inscritaA\">");
		sb.append("<label id=\"labelInscritaA\" for=\"inscritaA\">Informaci&oacute;n de la Inscripci&oacute;n de la persona jur&iacute;dica</label>");
		sb.append("</div>");
		sb.append("</div>");
			
		if(!isInscripcion.equalsIgnoreCase("2")) {
			sb.append("<div class=\"row note note-example\">\r\n"); 
			sb.append("<p>Si deudor garante y/o acreedor garantizado son personas jur&iacute;dicas, deber&aacute;\r\n"); 
			sb.append("adem&aacute;s identificarse la persona que act&uacute;a en su representaci&oacute;n, as&iacute; como el\r\n"); 
			sb.append("documento con que se Identifica y el documento con base en el, cual ejerce\r\n");
			sb.append("tal representaci&oacute;n. </p>\r\n");
			sb.append("</div>");	
		
			sb.append("<div class=\"row\">");
			sb.append("<div class=\"input-field col s12\">");		
			sb.append("<textarea rows=\"5\" onKeyUp=\"return maximaLongitud(this,3500)\" cols=\"53\" name=\"ubicacionA\" id=\"ubicacionA\" maxlength=\"3500\">" 
					+ notNull(altaParteTO.getUbicacionInscrito()) + " </textarea>");		
			sb.append("<label id=\"labelUbicacionA\" for=\"ubicacionA\">Datos del Representante(s)</label>");
			sb.append("</div>");
			sb.append("</div>");
		}
		
		/*
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"input-field col s6\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getLibroInscrito())
				+ "\" type=\"text\" maxlength=\"40\" class=\"validate\" name=\"libroA\" id=\"libroA\">");
		sb.append("<label for=\"libroA\">Libro</label>");
		sb.append("</div>");
		
		sb.append("<div class=\"input-field col s6\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getUbicacionInscrito()) 
				+ "\" type=\"text\" maxlength=\"40\" class=\"validate\" name=\"ubicacionA\" id=\"ubicacionA\">");
		sb.append("<label for=\"ubicacionA\">Direcci&oacute;n</label>");
		sb.append("</div>");*/
		
	
		return sb;
	}

	private StringBuffer writeCalle(AltaParteTO altaParteTO, Boolean esAutoridad) {
		String autoridad = null;
		if (esAutoridad) {
			autoridad = "1";
		} else {
			autoridad = "0";
		}

		StringBuffer sb = new StringBuffer();
		sb.append("<table>");
		sb.append("<tr> <td style=\"padding-left: 10px;\" colspan=\"2\" class=\"texto_general\" align=\"left\">");
		sb.append("<span class=\"textoGeneralRojo\">Calle :</span></td></tr>");

		sb.append("<tr> "
				+ "		<td>"
				+ "			<table>"
				+ "				<tr>"
				+ "					<td style=\"padding-left: 7px;\" align=\"left\" class=\"texto_azul\" width=\"18px\">*</td>"
				+ "					<td> ");
		sb.append("	<dl style=\"width: 120px\">");
		sb.append("	<dd style=\"width: 120px\">");
		sb.append("<input value=\""
				+ notNull(altaParteTO.getCalle())
				+ "\" type=\"text\" name=\"calle\" id=\"calle\" maxlength=\"50\" >");
		sb.append(" <span class=\"hint\"> <div class=\"cerrar\"> <a onclick=\"this.parentNode.getElementsByTagName('span')[0].style.display = 'none';\" style=\"cursor: pointer;\">x</a> </div> <div class=\"contenido\"> El domicilio para efectos del RUG es aquel que los acreedores se帽alen para efecto de ser contactados. </div><span class=\"hint-pointer\">&nbsp;</span></span>");
		sb.append("	</dd>");
		sb.append("	</dl>");
		sb.append("</td> </tr> </table> </td> </tr>");
		sb.append("<tr><td style=\"padding-left: 10px;\" colspan=\"2\" class=\"texto_general\" align=\"left\">");
		sb.append("<span class=\"textoGeneralRojo\">N煤mero Exterior :</span></td> </tr>");
		sb.append("<tr> "
				+ "		<td>"
				+ "			<table>"
				+ "				<tr>"
				+ "					<td style=\"padding-left: 7px;\" align=\"left\"  class=\"texto_azul\" width=\"18px\">*</td>"
				+ "					<td> ");
		sb.append("<input value=\""
				+ notNull(altaParteTO.getNumeroExterior())
				+ "\" type=\"text\" maxlength=\"20\" name=\"numExterior\" id=\"numExterior\"></td> </tr> </table> </td> </tr>");
		sb.append("<tr> <td style=\"padding-left: 10px;\" colspan=\"2\" class=\"texto_general\" align=\"left\">");
		sb.append("<span class=\"textoGeneralRojo\">N煤mero Interior :</span></td> </tr>");
		sb.append("<tr> <td style=\"padding-left: 29px;\" class=\"texto_general\">");
		sb.append("<input value=\""
				+ notNull(altaParteTO.getNumeroInterior())
				+ "\" type=\"text\" maxlength=\"20\" name=\"numInterior\" id=\"numInterior\"></td></tr>");
		sb.append("<tr><td id=\"cpTab\">");
		sb.append("</td></tr> <script>  cambiaNacionalidadA(" + autoridad
				+ "); </script>  ");
		sb.append("</table>");

		return sb;
	}

	private StringBuffer writeCalleExtranjera(AltaParteTO altaParteTO) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"input-field col s12\">");
		sb.append("<textarea rows=\"5\" onKeyUp=\"return maximaLongitud(this,200)\" cols=\"53\" name=\"domicilioUno\" id=\"domicilioUno\">"
				+ notNull(altaParteTO.getDomicilioUno()) + " </textarea>");
		sb.append("<label id=\"labelDomicilio\" for=\"domicilioUno\">Domicilio</label>");
		sb.append("</div>");
		sb.append("</div>");
				
		return sb;
	}
	
	private StringBuffer writeCalleExtranjeraO(AltaParteTO altaParteTO) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"input-field col s12\">");
		sb.append("<textarea rows=\"5\" onKeyUp=\"return maximaLongitud(this,200)\" cols=\"53\" name=\"domicilioUnoO\" id=\"domicilioUnoO\">"
				+ notNull(altaParteTO.getDomicilioUno()) + " </textarea>");
		sb.append("<label id=\"labelDomicilioO\" for=\"domicilioUnoO\">Domicilio</label>");
		sb.append("</div>");
		sb.append("</div>");
				
		return sb;
	}
	
	private StringBuffer writeCalleExtranjeraA(AltaParteTO altaParteTO) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"input-field col s12\">");
		sb.append("<textarea rows=\"5\" onKeyUp=\"return maximaLongitud(this,200)\" cols=\"53\" name=\"domicilioUnoA\" id=\"domicilioUnoA\">"
				+ notNull(altaParteTO.getDomicilioUno()) + " </textarea>");
		sb.append("<label id=\"labelDomicilioA\" for=\"domicilioUnoA\">Domicilio</label>");
		sb.append("</div>");
		sb.append("</div>");
				
		return sb;
	}

	private StringBuffer writeTelefonoCorreo(AltaParteTO altaParteTO) {
		StringBuffer sb = new StringBuffer();
		
		/*sb.append("<div class=\"row\">");
		sb.append("<div class=\"input-field col s12\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getTelefono()) 
				+ "\" type=\"text\" maxlength=\"150\" class=\"validate\" name=\"telefono\" id=\"telefono\">");
		sb.append("<label id=\"labelTelefono\" for=\"telefono\">Tel&eacute;fono</label>");
		sb.append("</div>");
		sb.append("</div>");*/
		
		String cadenaCorreos = notNull(altaParteTO.getCorreoElectronico());
		String[] correos = cadenaCorreos.split(",");
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"input-field col s12\">");
		sb.append("<input value=\"" + correos[0] 
				+ "\" type=\"text\" maxlength=\"150\" class=\"validate\" onkeyup=\"this.value = this.value.toLowerCase()\" name=\"correoElectronico\" id=\"correoElectronico\">");
		sb.append("<label id=\"labelCorreo\" for=\"correoElectronico\">Correo Electr&oacute;nico</label>");
		sb.append("</div>");
		sb.append("</div>");
		
		sb.append("<div id= \"correosExtra\" class=\"row\">");
		sb.append("</div>");
		
		sb.append("<div class=\"row\">");
		sb.append("<a onclick=\"agregaMail();\">+ Agregar Correos</a>");
		sb.append("</div>");
		
		sb.append(" <script type=\"text/javascript\"> muestraCorreosAdd('"
				+ cadenaCorreos + "'); </script>");
		return sb;
	}
	
	private StringBuffer writeTelefonoCorreoO(AltaParteTO altaParteTO) {
		StringBuffer sb = new StringBuffer();
		
		String cadenaCorreos = notNull(altaParteTO.getCorreoElectronico());
		String[] correos = cadenaCorreos.split(",");
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"input-field col s12\">");
		sb.append("<input value=\"" + correos[0] 
				+ "\" type=\"text\" maxlength=\"150\" class=\"validate\" onkeyup=\"this.value = this.value.toLowerCase()\" name=\"correoElectronicoO\" id=\"correoElectronicoO\">");
		sb.append("<label id=\"labelCorreoO\" for=\"correoElectronicoO\">Correo Electr&oacute;nico</label>");
		sb.append("</div>");
		sb.append("</div>");
		
		sb.append("<div id= \"correosExtraO\" class=\"row\">");
		sb.append("</div>");
		
		sb.append("<div class=\"row\">");
		sb.append("<a onclick=\"agregaMailO();\">+ Agregar Correos</a>");
		sb.append("</div>");
		
		sb.append(" <script type=\"text/javascript\"> muestraCorreosOAdd('"
				+ cadenaCorreos + "'); </script>");
		return sb;
	}

	private StringBuffer writeTelefonoCorreoA(AltaParteTO altaParteTO) {
		StringBuffer sb = new StringBuffer();
		
		/*sb.append("<div class=\"row\">");
		sb.append("<div class=\"input-field col s12\">");
		sb.append("<input value=\"" + notNull(altaParteTO.getTelefono()) 
				+ "\" type=\"text\" maxlength=\"150\" class=\"validate\" name=\"telefonoA\" id=\"telefonoA\">");
		sb.append("<label id=\"labelTelefonoA\" for=\"telefonoA\">Tel&eacute;fono</label>");
		sb.append("</div>");
		sb.append("</div>");*/
		
		String cadenaCorreos = notNull(altaParteTO.getCorreoElectronico());
		String[] correos = cadenaCorreos.split(",");
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"input-field col s12\">");
		sb.append("<input value=\"" + correos[0] 
				+ "\" type=\"text\" maxlength=\"150\" class=\"validate\" onkeyup=\"this.value = this.value.toLowerCase()\" name=\"correoElectronicoA\" id=\"correoElectronicoA\">");
		sb.append("<label id=\"labelCorreoA\" for=\"correoElectronicoA\">Correo Electr&oacute;nico</label>");
		sb.append("</div>");
		sb.append("</div>");
		
		sb.append("<div id= \"correosExtraA\" class=\"row\">");
		sb.append("</div>");
		
		sb.append("<div class=\"row\">");
		sb.append("<a onclick=\"agregaMailA();\">+ Agregar Correos</a>");
		sb.append("</div>");
		
		sb.append(" <script type=\"text/javascript\"> muestraCorreosAddA('"
				+ cadenaCorreos + "'); </script>");
		return sb;
	}
	
	//Nuevo acreedor
	public MessageDwr guardaParteAcreedorRepresentadoNuevo(String elementId,
			String idTramite, String idPersona, String idPersonaModificar,
			String nombre, String apellidoP, String apellidoM,
			String razonSocial, String nacionalidad, String tipoPersona,
			String folioElectronico, String docExtranjero, String rfc,
			String isInscripcion, String isMultiple, String tipo) {

		MessageDwr dwr = new MessageDwr();
		String cadena = "";
		boolean muestraOtorgante = true;
		int usaRenapo = 0;
		if (Constants.getParamValue(Constants.ACTIVAR_RENAPO).equalsIgnoreCase("true"))
		{
			usaRenapo=1;
		}
		try {
			MyLogger.Logger.log(Level.INFO,
					"entro guardaParteOtorgante Nuevo con : " + razonSocial);
			MyLogger.Logger.log(Level.INFO, "IDNACIONALIDAD GUARDA PARTE : "
					+ nacionalidad);
			MyLogger.Logger.log(Level.INFO, "Tipo :" + tipo);
			AltaParteTO altaParteTO = new AltaParteTO();
			altaParteTO.setIdParte(1);
			altaParteTO.setIdTramite(Integer.valueOf(idTramite));
			altaParteTO.setFolioMercantil(folioElectronico);
			altaParteTO.setCurp(docExtranjero);
			altaParteTO.setRfc(rfc);
			altaParteTO.setNombre(nombre);
			altaParteTO.setApellidoMaterno(apellidoM);
			altaParteTO.setApellidoPaterno(apellidoP);
			altaParteTO.setRazonSocial(razonSocial);
			altaParteTO.setIdNacionalidad(Integer.valueOf(nacionalidad));
			altaParteTO.setTipoPersona(tipoPersona);
			altaParteTO.setHayDomicilio("F");
			altaParteTO.setIdUsuario(Integer.valueOf(idPersona));
			altaParteTO.setIdPersona(Integer.valueOf(idPersonaModificar));
			// agregar tipo
			altaParteTO.setTipo(tipo);
			AltaParteDAO altaParteDAO = new AltaParteDAO();

			// MyLogger.Logger.log(Level.INFO,
			// "en muestraFolioOtorganteExistente: tip: " + tipo);
			if (tipoPersona.equals("PF")) {
				if (nacionalidad.equals("1")) {
					// fisico mexicano
					if (usaRenapo == 0) {
						OtorganteTO otorganteTO = altaParteDAO
								.getOtorganteFisicoByCURP(docExtranjero);
						if (otorganteTO != null) {
							MyLogger.Logger
									.log(Level.INFO,
											"en muestraFolioOtorganteExistente: OtorganteTO ES DIFERENTE DE NULO");
							boolean igual = true;
							if (!otorganteTO.getNombre().equals(nombre)) {
								igual = false;
							}
							if (!otorganteTO.getApellidoPaterno().equals(
									apellidoP)) {
								igual = false;
							}

							if (!igual) {
							}

						} else {
							// otorgante es nulo
							PlSql idPersonaINT = altaParteDAO
									.insert(altaParteTO);
							FolioElectronicoDAO folioElectronicoDAO = new FolioElectronicoDAO();
							folioElectronicoDAO
									.creaFolioElectronico(idPersonaINT
											.getResIntPl().intValue());

						}
					}else{
						PlSql idPersonaINT = altaParteDAO
								.insert(altaParteTO);
						FolioElectronicoDAO folioElectronicoDAO = new FolioElectronicoDAO();
						folioElectronicoDAO
								.creaFolioElectronico(idPersonaINT
										.getResIntPl().intValue());
					}
					
				}// fin mexicano
				else {
					// fisico extranjero
					PlSql idPersonaINT = altaParteDAO.insert(altaParteTO);
					FolioElectronicoDAO folioElectronicoDAO = new FolioElectronicoDAO();
					String folioE = folioElectronicoDAO
							.creaFolioElectronico(idPersonaINT.getResIntPl());
					MyLogger.Logger
							.log(Level.INFO, "ID PERSONA" + idPersonaINT);
					MyLogger.Logger.log(Level.INFO, folioE);
				}
			}// fin fisico

			dwr = getParteOtorgante(elementId, idTramite, idPersona, "0",
					isInscripcion, isMultiple);
			dwr.setMessage(dwr.getMessage() + cadena);

		} catch (Exception e) {
			MyLogger.Logger.log(Level.INFO, "entro catch 1" + e.getMessage());
			e.printStackTrace();

		}

		return dwr;

	}
	//Nuevo acreedor
	
	// metodo para insertar otorgante nuevo
	public MessageDwr guardaParteOtorganteNuevo(String elementId,
			String idTramite, String idPersona, String idPersonaModificar,
			String nombre, String apellidoP, String apellidoM,
			String razonSocial, String nacionalidad, String tipoPersona,
			String folioElectronico, String docExtranjero, String rfc,
			String isInscripcion, String isMultiple, String tipo) {

		MessageDwr dwr = new MessageDwr();
		String cadena = "";
		boolean muestraOtorgante = true;
		int usaRenapo = 0;
		if (Constants.getParamValue(Constants.ACTIVAR_RENAPO).equalsIgnoreCase("true"))
		{
			usaRenapo=1;
		}
		try {
			MyLogger.Logger.log(Level.INFO,
					"entro guardaParteOtorgante Nuevo con : " + razonSocial);
			MyLogger.Logger.log(Level.INFO, "IDNACIONALIDAD GUARDA PARTE : "
					+ nacionalidad);
			MyLogger.Logger.log(Level.INFO, "Tipo :" + tipo);
			AltaParteTO altaParteTO = new AltaParteTO();
			altaParteTO.setIdParte(1);
			altaParteTO.setIdTramite(Integer.valueOf(idTramite));
			altaParteTO.setFolioMercantil(folioElectronico);
			altaParteTO.setCurp(docExtranjero);
			altaParteTO.setRfc(rfc);
			altaParteTO.setNombre(nombre);
			altaParteTO.setApellidoMaterno(apellidoM);
			altaParteTO.setApellidoPaterno(apellidoP);
			altaParteTO.setRazonSocial(razonSocial);
			altaParteTO.setIdNacionalidad(Integer.valueOf(nacionalidad));
			altaParteTO.setTipoPersona(tipoPersona);
			altaParteTO.setHayDomicilio("F");
			altaParteTO.setIdUsuario(Integer.valueOf(idPersona));
			altaParteTO.setIdPersona(Integer.valueOf(idPersonaModificar));
			// agregar tipo
			altaParteTO.setTipo(tipo);
			AltaParteDAO altaParteDAO = new AltaParteDAO();

			// MyLogger.Logger.log(Level.INFO,
			// "en muestraFolioOtorganteExistente: tip: " + tipo);
			if (tipoPersona.equals("PF")) {
				if (nacionalidad.equals("1")) {
					// fisico mexicano
					if (usaRenapo == 0) {
						OtorganteTO otorganteTO = altaParteDAO
								.getOtorganteFisicoByCURP(docExtranjero);
						if (otorganteTO != null) {
							MyLogger.Logger
									.log(Level.INFO,
											"en muestraFolioOtorganteExistente: OtorganteTO ES DIFERENTE DE NULO");
							boolean igual = true;
							if (!otorganteTO.getNombre().equals(nombre)) {
								igual = false;
							}
							if (!otorganteTO.getApellidoPaterno().equals(
									apellidoP)) {
								igual = false;
							}

							if (!igual) {
							}

						} else {
							// otorgante es nulo
							PlSql idPersonaINT = altaParteDAO
									.insert(altaParteTO);
							FolioElectronicoDAO folioElectronicoDAO = new FolioElectronicoDAO();
							folioElectronicoDAO
									.creaFolioElectronico(idPersonaINT
											.getResIntPl().intValue());

						}
					}else{
						PlSql idPersonaINT = altaParteDAO.insert(altaParteTO);
						FolioElectronicoDAO folioElectronicoDAO = new FolioElectronicoDAO();
						folioElectronicoDAO.creaFolioElectronico(idPersonaINT.getResIntPl().intValue());
					}
					/*if (usaRenapo == 1) {
						// fisico mexicano
						OtorganteTO otorganteTO = altaParteDAO
								.getOtorganteFisicoByCURP(docExtranjero);
						if (otorganteTO != null) {
							MyLogger.Logger
									.log(Level.INFO,
											"en muestraFolioOtorganteExistente: OtorganteTO ES DIFERENTE DE NULO");
							boolean igual = true;
							if (!otorganteTO.getNombre().equals(nombre)) {
								igual = false;
							}
							if (!otorganteTO.getApellidoPaterno().equals(
									apellidoP)) {
								igual = false;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
							}

							if (!igual) {
								RenapoConsultaCurp consultaCurp = new RenapoConsultaCurp();
								ResponseCurp persona = null;
								try {
									persona = consultaCurp
											.getDataFromCurp(altaParteTO
													.getCurp());
								} catch (NoCurpFound e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									cadena = " <script> displayMessageConfirmacionCurpFail(true,' ','"
											+ e.getMessage() + "'); </script>";

								} catch (NoServiceRenapoAvailable e) {
									// TODO Auto-generated catch block
									MyLogger.Logger.log(Level.INFO,
											"SERVICIO RENAPO NO DISPONIBLE");
									e.printStackTrace();
									cadena = " <script> displayMessageConfirmacionCurpFail(true,' ','"
											+ e.getMessage() + "'); </script>";
								} catch (Exception e) {
									cadena = " <script> displayMessageConfirmacionCurpFail(true,' ','"
											+ e.getMessage() + "'); </script>";
								}

								cadena = " <script> displayMessageConfirmacionCurpMatriculado(true,' ','La CURP no corresponde a los datos ingresados.','"
										+ altaParteTO.getCurp()
										+ "','"
										+ persona.getNombre()
										+ "','"
										+ persona.getApellidoPaterno()
										+ "',"
										+ "'"
										+ persona.getApellidoMaterno()
										+ "','"
										+ elementId
										+ "','"
										+ idTramite
										+ "','"
										+ idPersona
										+ "','"
										+ isInscripcion
										+ "','"
										+ nacionalidad
										+ "','"
										+ tipoPersona
										+ "','"
										+ isMultiple
										+ "','"
										+ otorganteTO.getFolioMercantil()
										+ "'); </script>";

								muestraOtorgante = false;

							} else {
								boolean agrego = altaParteDAO.relParte(
										otorganteTO.getIdOtorgante(),
										new Integer(idTramite), 1);
								if (!agrego) {
									cadena = " <script> alert('Error al relacionar al OTORGANTE'); </script> ";
								}
							}
						} else {
							// otorgante es nulo
							// MyLogger.Logger.log(Level.INFO,
							// "隆隆en OTORGANTE nulo !! "+
							// altaParteTO.getCurp());

							RenapoConsultaCurp consultaCurp = new RenapoConsultaCurp();
							ResponseCurp persona = null;
							try {
								MyLogger.Logger.log(Level.INFO,
										"zntes de consultar curp");
								persona = consultaCurp
										.getDataFromCurp(altaParteTO.getCurp());
								MyLogger.Logger.log(Level.INFO,
										"despues de consultar curp");
							} catch (NoCurpFound e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								MyLogger.Logger.log(Level.INFO,
										"catch NoCurpFound e" + e.getMessage());
								cadena = " <script> displayMessageConfirmacionCurpFail(true,' ','"
										+ "La CURP"
										+ " "
										+ altaParteTO.getCurp()
										+ " "
										+ " no se encuentra en la base de datos de RENAPO"
										+ "'); </script>";

							} catch (NoServiceRenapoAvailable e) {
								// TODO Auto-generated catch block
								PlSql idPersonaINT = altaParteDAO
										.insert(altaParteTO);
								FolioElectronicoDAO folioElectronicoDAO = new FolioElectronicoDAO();
								String folioE = folioElectronicoDAO
										.creaFolioElectronico(idPersonaINT
												.getResIntPl());
								e.printStackTrace();

							} catch (Exception e) {
								cadena = " <script> displayMessageConfirmacionCurpFail(true,' ','"
										+ e.getMessage() + "'); </script>";
							}
							dwr.setCodeError(0);
							dwr.setMessage(cadena);
							MyLogger.Logger.log(
									Level.INFO,
									"condicion Persona true: "
											+ altaParteTO.getNombre() + ","
											+ altaParteTO.getCurp() + ","
											+ persona.getNombre() + ","
											+ persona.getCurp());
							if (altaParteTO.getNombre().equals(
									persona.getNombre())
									&& altaParteTO.getApellidoPaterno().equals(
											persona.getApellidoPaterno())) {
								MyLogger.Logger.log(Level.INFO,
										"condicion altaParteTO en otorganteNuevo true: "
												+ altaParteTO.getNombre() + ","
												+ persona.getNombre());
							}

							PlSql idPersonaINT = altaParteDAO
									.insert(altaParteTO);
							FolioElectronicoDAO folioElectronicoDAO = new FolioElectronicoDAO();
							folioElectronicoDAO
									.creaFolioElectronico(idPersonaINT
											.getResIntPl().intValue());

						}*///fin con renapo
					
				}// fin mexicano
				else {
					// fisico extranjero
					PlSql idPersonaINT = altaParteDAO.insert(altaParteTO);
					FolioElectronicoDAO folioElectronicoDAO = new FolioElectronicoDAO();
					String folioE = folioElectronicoDAO
							.creaFolioElectronico(idPersonaINT.getResIntPl());
					MyLogger.Logger
							.log(Level.INFO, "ID PERSONA" + idPersonaINT);
					MyLogger.Logger.log(Level.INFO, folioE);
				}
			}// fin fisico

			dwr = getParteOtorgante(elementId, idTramite, idPersona, "0",
					isInscripcion, isMultiple);
			dwr.setMessage(dwr.getMessage() + cadena);

		} catch (Exception e) {
			MyLogger.Logger.log(Level.INFO, "entro catch 1" + e.getMessage());
			e.printStackTrace();

		}

		return dwr;

	}// fin metodo otorgante nuevo
	
	//m茅todo que valida tipo de garantia derivada de un arrendamiento financiero folios acreedores

	public MessageDwr verificarFolios(int idInscripcion){
		
		MessageDwr messageDwr= new MessageDwr();
		AltaParteDAO valida=new AltaParteDAO();
		messageDwr.setCodeError(valida.validaFolios(idInscripcion));
	
	return messageDwr;
	
}
	
	private StringBuffer writeCalleRep(AltaParteTO altaParteTO, Boolean esAutoridad) {
		String autoridad = null;
		if (esAutoridad) {
			autoridad = "1";
		} else {
			autoridad = "0";
		}

		StringBuffer sb = new StringBuffer();
		sb.append("<table>");
		sb.append("<tr> <td style=\"padding-left: 10px;\" colspan=\"2\" class=\"texto_general\" align=\"left\">");
		sb.append("<span class=\"textoGeneralRojo\">Calle :</span></td></tr>");

		sb.append("<tr> "
				+ "		<td>"
				+ "			<table>"
				+ "				<tr>"
				+ "					<td style=\"padding-left: 7px;\" align=\"left\" class=\"texto_azul\" width=\"18px\">*</td>"
				+ "					<td> ");
		sb.append("	<dl style=\"width: 120px\">");
		sb.append("	<dd style=\"width: 120px\">");
		sb.append("<input value=\""
				+ notNull(altaParteTO.getCalle())
				+ "\" type=\"text\" name=\"calle\" id=\"calle\" maxlength=\"50\" >");
		sb.append(" <span class=\"hint\"> <div class=\"cerrar\"> <a onclick=\"this.parentNode.getElementsByTagName('span')[0].style.display = 'none';\" style=\"cursor: pointer;\">x</a> </div> <div class=\"contenido\"> El domicilio para efectos del RUG es aquel que los acreedores se帽alen para efecto de ser contactados. </div><span class=\"hint-pointer\">&nbsp;</span></span>");
		sb.append("	</dd>");
		sb.append("	</dl>");
		sb.append("</td> </tr> </table> </td> </tr>");
		sb.append("<tr><td style=\"padding-left: 10px;\" colspan=\"2\" class=\"texto_general\" align=\"left\">");
		sb.append("<span class=\"textoGeneralRojo\">N煤mero Exterior :</span></td> </tr>");
		sb.append("<tr> "
				+ "		<td>"
				+ "			<table>"
				+ "				<tr>"
				+ "					<td style=\"padding-left: 7px;\" align=\"left\"  class=\"texto_azul\" width=\"18px\">*</td>"
				+ "					<td> ");
		sb.append("<input value=\""
				+ notNull(altaParteTO.getNumeroExterior())
				+ "\" type=\"text\" maxlength=\"20\" name=\"numExterior\" id=\"numExterior\"></td> </tr> </table> </td> </tr>");
		sb.append("<tr> <td style=\"padding-left: 10px;\" colspan=\"2\" class=\"texto_general\" align=\"left\">");
		sb.append("<span class=\"textoGeneralRojo\">N煤mero Interior :</span></td> </tr>");
		sb.append("<tr> <td style=\"padding-left: 29px;\" class=\"texto_general\">");
		sb.append("<input value=\""
				+ notNull(altaParteTO.getNumeroInterior())
				+ "\" type=\"text\" maxlength=\"20\" name=\"numInterior\" id=\"numInterior\"></td></tr>");
		sb.append("<tr><td id=\"cpTab\">");
		sb.append("</td></tr> <script>  cambiaNacionalidadARep(" + autoridad
				+ "); </script>  ");
		sb.append("</table>");

		return sb;
	}
	
	public String getAcreedor(Integer id) {
		ObjectMapper mapper = new ObjectMapper();
		String strAcreedor = "";
		
		try {
			strAcreedor = mapper.writeValueAsString(acreedoresService.getAcreedor(id));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return strAcreedor;
	}
        
   public String buscaPersonaByFolioElectronicoInst(String folioElectronico, String tipoPersona){
       Persona persona1 = new Persona();
        persona1.setNit("45389829");
        persona1.setNombre("ESPA袮,Y ESPA袮,,CARLOS,MAURICIO");
        persona1.setTo("0");
        persona1.setCn("1");
        persona1.setCed("S-20 76001");
        persona1.setPas(" ");
        persona1.setFn("25051983");
        persona1.setSexo("M");
        persona1.setDir("SECTOR 9 MANZANA H");
        persona1.setNc("0-58");
        persona1.setApa("58");
        persona1.setZon("7");
        persona1.setCol("COLONIA PRADOS DE VILLA HERMOSA, SAN MIGUEL PETAPA");
        persona1.setCd("1");
        persona1.setCm("17");
        persona1.setTel("42128220");
        persona1.setFax(" ");
        persona1.setCp("01066");
        persona1.setEma("mauricio.espana.3@gmail.com");
        persona1.setFirmp(" ");
        persona1.setFirmd(" ");
        persona1.setFirtu(null);
        persona1.setResponse(null);

        Persona persona2 = new Persona();
        persona2.setNit("7301073");
        persona2.setNombre("MEJ虯,CUELLAR,,H蒀TOR,RAMIRO");
        persona2.setTo("0");
        persona2.setCn("1");
        persona2.setCed("F-6 15110");
        persona2.setPas(" ");
        persona2.setFn("13091973");
        persona2.setSexo("M");
        persona2.setDir("14 CALLE A");
        persona2.setNc("32-86");
        persona2.setApa("30");
        persona2.setZon("21");
        persona2.setCol("COLONIA JUSTO RUFINO BARRIOS");
        persona2.setCd("1");
        persona2.setCm("1");
        persona2.setTel("24498821");
        persona2.setFax(" ");
        persona2.setCp("01000");
        persona2.setEma("hectormejiacuellar@gmail.com");
        persona2.setFirmp(" ");
        persona2.setFirmd(" ");
        persona2.setFirtu(null);
        persona2.setResponse(null);

        Persona persona3 = new Persona();
        persona3.setNit("26295660");
        persona3.setNombre("MENDEZ,PAZ,,EDDY,ORLANDO");
        persona3.setTo("0");
        persona3.setCn("1");
        persona3.setCed("A-1 937738");
        persona3.setPas(" ");
        persona3.setFn("03061976");
        persona3.setSexo("M");
        persona3.setDir("2 AVENIDA");
        persona3.setNc("36-61");
        persona3.setApa(" ");
        persona3.setZon("12");
        persona3.setCol("COLONIA EL CARMEN");
        persona3.setCd("1");
        persona3.setCm("1");
        persona3.setTel("24766471");
        persona3.setFax(" ");
        persona3.setCp("1000");
        persona3.setEma("MENDEZ.EDDY@GMAIL.COM");
        persona3.setFirmp(" ");
        persona3.setFirmd(" ");
        persona3.setFirtu(null);
        persona3.setResponse(null);

        Persona persona4 = new Persona();
        persona4.setNit("26284138");
        persona4.setNombre("LOBOS,BARRERA,M蒒DEZ,EVELYN,YESENIA");
        persona4.setTo("0");
        persona4.setCn("1");
        persona4.setCed("A-1 907607");
        persona4.setPas(" ");
        persona4.setFn("12051975");
        persona4.setSexo("F");
        persona4.setDir("2 AVENIDA");
        persona4.setNc("36-61");
        persona4.setApa(" ");
        persona4.setZon("12");
        persona4.setCol("COLONIA EL CARMEN");
        persona4.setCd("1");
        persona4.setCm("1");
        persona4.setTel("24766471");
        persona4.setFax(" ");
        persona4.setCp("1000");
        persona4.setEma("EVELYNLOBOS12@YAHOO.COM.MX");
        persona4.setFirmp(" ");
        persona4.setFirmd(" ");
        persona4.setFirtu(null);
        persona4.setResponse(null);

        // Crear un registro de personas y agregar las instancias de personas
        RegistroPersonas registro = new RegistroPersonas();
        registro.agregarPersona(persona1);
        registro.agregarPersona(persona2);
        registro.agregarPersona(persona3);
        registro.agregarPersona(persona4);
        
        // Buscar persona por NIT
        List<Persona> personas = registro.getPersonas();
        String nitBuscado = folioElectronico;
        JSONObject jsonPersona = new JSONObject();
        boolean personaEncontrada = false;
       for (Persona persona : personas) {
        if (persona.getNit().equals(nitBuscado)) {
             personaEncontrada = true;
        System.out.println("Persona encontrada:");
        System.out.println("NIT: " + persona.getNit());
        System.out.println("Nombre: " + persona.getNombre());
        // Imprimir otros campos de la persona encontrada

        // Crear un objeto JSON con los datos encontrados
        
        jsonPersona.put("nit", persona.getNit());
        jsonPersona.put("nombre", persona.getNombre());
        jsonPersona.put("to", persona.getTo());
        jsonPersona.put("cn", persona.getCn());
        jsonPersona.put("ced", persona.getCed());
        jsonPersona.put("pas", persona.getPas());
        jsonPersona.put("fn", persona.getFn());
        jsonPersona.put("sexo", persona.getSexo());
        jsonPersona.put("dir", persona.getDir());
        jsonPersona.put("nc", persona.getNc());
        jsonPersona.put("apa", persona.getApa());
        jsonPersona.put("zon", persona.getZon());
        jsonPersona.put("col", persona.getCol());
        jsonPersona.put("cd", persona.getCd());
        jsonPersona.put("cm", persona.getCm());
        jsonPersona.put("tel", persona.getTel());
        jsonPersona.put("fax", persona.getFax());
        jsonPersona.put("cp", persona.getCp());
        jsonPersona.put("ema", persona.getEma());
        jsonPersona.put("firmp", persona.getFirmp());
        jsonPersona.put("firmd", persona.getFirmd());
        jsonPersona.put("firtu", persona.getFirtu());
        jsonPersona.put("response", persona.getResponse());

        // Imprimir el objeto JSON
        System.out.println("Datos encontrados en formato JSON:");
        System.out.println(jsonPersona.toString());

        break; // Detener el bucle una vez que se encuentra la persona
    }
}
       if (!personaEncontrada) {
            return "Datos no encontrados.";
        }
          /*  WSExternoDAO dao = new WSExternoDAO();
            List<WSExternoTO> parametros = dao.getWebService();
            String CnxSAT_login = "";
            String CnxSAT_consulta_nit = "";
            String CnxSAT_usuario = "";
            String CnxSAT_password = "";
                    
            String respuesta="false";
            for (WSExternoTO parametro : parametros) 
            {
                String cveParametro = parametro.getCVE_PARAMETRO();
                String valorParametro = parametro.getVALOR_PARAMETRO();                
                if(cveParametro.equals("CnxSAT_login")){                    
                    CnxSAT_login = valorParametro;
                }
                if(cveParametro.equals("CnxSAT_consulta_nit")){                    
                    CnxSAT_consulta_nit = valorParametro;
                }
                if(cveParametro.equals("CnxSAT_usuario")){                    
                    CnxSAT_usuario = valorParametro;
                }
                if(cveParametro.equals("CnxSAT_password")){                    
                    CnxSAT_password = valorParametro;
                }                
            }
            StringBuffer responses = new StringBuffer();
            MessageDwr messageDwr = new MessageDwr();   
            String apiUrl = CnxSAT_login;
            String username = CnxSAT_usuario;
            String password = CnxSAT_password;
            X509Certificate trustedCertificate = null;
            
            // Solo para pruebas, NO usar en producci髇 se valida si hay certificado
            TrustManager[] trustAllCertificates = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        
                    return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            };

            // Instalar el TrustManager que no valida certificados
            try {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());
                SSLContext.setDefault(sslContext);
            } catch (Exception e) {
                 System.out.println(e.getStackTrace());
            }
            
            
            
            try {
                    URL url = new URL(apiUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    String requestBody = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";

                    DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                            wr.writeBytes(requestBody);
                            wr.flush();
                    

                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                                    String inputLine;
                                    StringBuffer response = new StringBuffer();

                                    while ((inputLine = in.readLine()) != null) {
                                            response.append(inputLine);
                                    }
                                    String otro = null;
                                    StringBuffer sb = new StringBuffer();
                                    System.out.println("Response: " + response.toString());
                                    String jsonResponse = response.toString();
                                    try {
                                        JSONObject jsonObject = new JSONObject(jsonResponse);
                                        String accessToken = jsonObject.getString("accessToken");
                                        
                                        String apiUrls = CnxSAT_consulta_nit;
                                        String token = accessToken; // Reemplaza con tu token Bearer
                                        String nit = folioElectronico;
                                        URL urls = new URL(apiUrls);
                                        HttpURLConnection connections = (HttpURLConnection) urls.openConnection();

                                        connections.setRequestMethod("POST");
                                        connections.setRequestProperty("Content-Type", "application/json");
                                        connections.setRequestProperty("Authorization", "Bearer " + token);
                                        connections.setDoOutput(true);

                                        String requestBodys = "{\"nit\":\"" + nit + "\"}";
                                        
                                        DataOutputStream wrr = new DataOutputStream(connections.getOutputStream());
                                            wrr.writeBytes(requestBodys);
                                            wrr.flush();
                                        
                                        int responseCodes = connections.getResponseCode();
                                        
                                        if (responseCodes == HttpURLConnection.HTTP_OK) {
                                            try (BufferedReader ins = new BufferedReader(new InputStreamReader(connections.getInputStream(), "UTF-8"))) {
                                                String inputLines;
                                                

                                                while ((inputLines = ins.readLine()) != null) {
                                                    responses.append(inputLines);
                                                }

                                                System.out.println("Response SAT: " + responses.toString());
                                            }
                                        } else {
                                            System.out.println("Request failed with response code: " + responseCode);
                                        }
                                        
                                       
                                        // Aqu� puedes hacer lo que necesites con el accessToken
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    messageDwr.setCodeError(0);
                                    messageDwr.setMessage(response.toString());
                            
                    } else {
                            System.out.println("Request failed with response code: " + responseCode);
                           responses.append(responseCode);
                    }
            } catch (IOException e) {
                    e.printStackTrace();
                    responses.append(e);
            }
		*/
		return jsonPersona.toString();
	}
	 public static void disableCertificateValidation() throws Exception {
        // Create a trust manager that trusts all certificates
        TrustManager[] trustAllCertificates = new TrustManager[]{
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
        };

        // Create an SSL context with the custom trust manager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());

        // Set the custom SSL context as default
        SSLContext.setDefault(sslContext);
    }	
        
        public String bnit(String token_,String link,String nit_){
            StringBuffer responses = new StringBuffer();
            String apiUrls = link;
            String token = token_; // Reemplaza con tu token Bearer
            String nit = nit_;
             try {
                URL url = new URL(apiUrls);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + token);
                connection.setDoOutput(true);
                String requestBody = "{\"nit\":\"" + nit + "\"}";
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(requestBody);
                wr.flush();
                 int responseCode = connection.getResponseCode();
                                        
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader ins = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
                        String inputLines;


                        while ((inputLines = ins.readLine()) != null) {
                            responses.append(inputLines);
                        }

                        System.out.println("Response SAT: " + responses.toString());
                    }
                } else {
                    System.out.println("Request failed with response code: " + responseCode);
                }
                
            } catch (IOException e) {
               e.printStackTrace();
               responses.append(e);
            }
            
            return responses.toString();
            
        }
        
        private SSLSocketFactory getInsecureSSLFactory() {
            try {
                TrustManager[] trustAllCertificates = new TrustManager[] {
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
                };

                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCertificates, new SecureRandom());
                return sslContext.getSocketFactory();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
	public MessageDwr verificarbotonsat(){
            MessageDwr dwr = new MessageDwr();
            WSExternoDAO dao = new WSExternoDAO();
            List<WSExternoTO> parametros = dao.getWebService();
            StringBuilder resp = new StringBuilder();
            
            String respuesta="false";
            for (WSExternoTO parametro : parametros) 
            {
                String cveParametro = parametro.getCVE_PARAMETRO();
                String valorParametro = parametro.getVALOR_PARAMETRO();                
                if(cveParametro.equals("CnxSAT_BotonVisible")){                    
                    respuesta = valorParametro;                    
                    dwr.setCodeError(0);
                    dwr.setMessage(respuesta);
                    
                }
                resp.append("cveParametro").append(cveParametro)
                        .append("valorParametro").append(valorParametro)
                        .append("\n");
            }
           
            return dwr;
        }
}

