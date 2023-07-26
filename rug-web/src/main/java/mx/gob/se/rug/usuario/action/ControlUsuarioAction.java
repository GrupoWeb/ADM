package mx.gob.se.rug.usuario.action;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import mx.gob.se.rug.acreedores.dao.AcreedoresDAO;
import mx.gob.se.rug.acreedores.dao.impl.AcreedoresDaoJdbcImpl;
import mx.gob.se.rug.acreedores.exception.AcreedoresException;
import mx.gob.se.rug.acreedores.service.AcreedoresCatalogosService;
import mx.gob.se.rug.acreedores.service.GrupoService;
import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.acreedores.to.GrupoPerfilTO;
import mx.gob.se.rug.acreedores.to.UsuarioTO;
import mx.gob.se.rug.administracion.dto.RegistroUsuario;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.dto.DatosContacto;
import mx.gob.se.rug.dto.PersonaFisica;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.mailservice.MailRegistroService;
import mx.gob.se.rug.usuario.service.ControlUsuarioService;
import mx.gob.se.rug.usuario.serviceimpl.ControlUsuarioServiceImpl;
import mx.gob.se.rug.util.ExpresionesRegularesUtil;
import mx.gob.se.rug.util.MyLogger;

public class ControlUsuarioAction extends RugBaseAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AcreedorTO acreedorTO;
	private List<UsuarioTO> listaFirmados;
	private List<UsuarioTO> listaNoFirmados;
	private UsuarioTO usuarioTO;
	private String idAcreedor;
	private AcreedoresCatalogosService acreedoresCatalogosService;
	private MailRegistroService mailRegistroService;
	private GrupoService grupoService;
	private List<GrupoPerfilTO> grupos;
	private Integer idGrupoSelect;
	private Integer idPersonaModificar;
	private String grupoElegido;
	private Integer idUsuario;
	private Integer _idAcreedor;
	private String existeUsuario = "";
	private String grupo;
	private String rfcError;
	public String consultaGrupo() throws AcreedoresException{
		MyLogger.Logger.log(Level.INFO, "consultaGrupo()");
		return SUCCESS;
	}
	
	public String busquedaGrupo() throws AcreedoresException {
		MyLogger.Logger.log(Level.INFO, "busquedaGrupo()");
		return SUCCESS;
	}

	public String setAcreedorId(){
		String regresa = "failed";
		try{
			if (getIdAcreedor()!=null){
				MyLogger.Logger.log(Level.INFO, "--ID ACREEDOR - Representado :"+ getIdAcreedor());
				sessionMap.put(Constants.ID_ACREEDOR_REPRESENTADO, new Integer(getIdAcreedor()));
				regresa = "success";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return regresa;
	}
	
	public String iniciaAltaGrupo() throws AcreedoresException, NumberFormatException {
		MyLogger.Logger.log(Level.INFO, "iniciaAltaGrupo()");
		mx.gob.se.rug.to.UsuarioTO usuario = (mx.gob.se.rug.to.UsuarioTO) sessionMap.get("usuario");
		setIdUsuario(usuario.getPersona().getIdPersona());
		set_idAcreedor((Integer) sessionMap.get(Constants.ID_ACREEDOR_REPRESENTADO));
		AcreedoresDAO acreedoresDAO = new AcreedoresDaoJdbcImpl();
		setAcreedorTO(acreedoresDAO.getARByID((Integer) sessionMap.get(Constants.ID_ACREEDOR_REPRESENTADO)));
		MyLogger.Logger.log(Level.INFO, "-- idAcreedor=" + _idAcreedor);
		MyLogger.Logger.log(Level.INFO, "-- idUsuario=" + idUsuario);
		return "success";
	}
	
	public String iniciaAltaUsuario(){
		MyLogger.Logger.log(Level.INFO, "iniciaAltaUsuario()");
		String regresa = "failed";
		try{
			if (getExisteUsuario()==null){
				setExisteUsuario("");
			}
			ControlUsuarioService usuarioService = new ControlUsuarioServiceImpl();
			mx.gob.se.rug.to.UsuarioTO usuario = (mx.gob.se.rug.to.UsuarioTO) sessionMap.get("usuario");
			Integer idPersona = usuario.getPersona().getIdPersona();
			setIdUsuario(usuario.getPersona().getIdPersona());
			Integer idAcreedor = (Integer) sessionMap.get(Constants.ID_ACREEDOR_REPRESENTADO);
			setIdAcreedor(idAcreedor.toString());
			List<GrupoPerfilTO> grupoDos = getAcreedoresCatalogosService().getGrupos(idAcreedor);
			setListaFirmados(new ArrayList<UsuarioTO>());
			setListaNoFirmados(new ArrayList<UsuarioTO>());
			GrupoPerfilTO grupoPerfilTO = new GrupoPerfilTO();
			grupoPerfilTO.setDescripcion("ADMINISTRADOR");
			grupoPerfilTO.setId("3");
			grupoDos.add(0, grupoPerfilTO);
			setGrupos(grupoDos);
			
			if (usuarioService.estanRelacionados(idPersona, idAcreedor)){
				MyLogger.Logger.log(Level.INFO, "idPersona" + idPersona);
				
				MyLogger.Logger.log(Level.INFO, "idAcreedor" + idAcreedor);
				setListaFirmados(usuarioService.getListaUsuariosFirmados(idPersona, idAcreedor));
				setListaNoFirmados(usuarioService.getListaUsuariosNoFirmados(idPersona, idAcreedor));
				//setear las listas con las vistas
				MyLogger.Logger.log(Level.INFO, "" + getListaFirmados().size());
				MyLogger.Logger.log(Level.INFO, "" + getListaNoFirmados().size());
				AcreedoresDAO acreedoresDAO = new AcreedoresDaoJdbcImpl();
				setAcreedorTO(acreedoresDAO.getARByID(idAcreedor));
				setIdAcreedor(idAcreedor.toString());
				regresa = "success";
			}			
		}catch(Exception e){			
			e.printStackTrace();
		}
		
		return regresa;
	}

	public String guardaUsuario() {
		MyLogger.Logger.log(Level.INFO, "guardaUsuario()");
		String regresa = "failed";
		setRfcError(null);
		MyLogger.Logger.log(Level.INFO, "" + idGrupoSelect);
		usuarioTO.setIdGrupo(idGrupoSelect);

		try {
			Integer idAcreedor = (Integer) sessionMap.get(Constants.ID_ACREEDOR_REPRESENTADO);
			ControlUsuarioService usuarioService = new ControlUsuarioServiceImpl();
			UsuarioTO _usuarioTO = usuarioService.getByCorreoElectronico(usuarioTO.getCveUsuario());
			mx.gob.se.rug.to.UsuarioTO usuario = (mx.gob.se.rug.to.UsuarioTO) sessionMap.get("usuario");
			Integer idPersona = usuario.getPersona().getIdPersona();
			usuarioTO.setIdRepresentanteAcreedor(idPersona);
			MyLogger.Logger.log(Level.INFO, "usuarioTO= " + usuarioTO);
			MyLogger.Logger.log(Level.INFO, "_usuarioTO=" + _usuarioTO);
			if (_usuarioTO == null ){
				ExpresionesRegularesUtil eru = new ExpresionesRegularesUtil();
				if (usuarioTO.getRfc()!=null & !eru.validarRfcFisica(usuarioTO.getRfc())){
					setRfcError("hubo");
					regresa = "errorRFC";
				}else{
					if (usuarioService.saveUsuario(usuarioTO, idAcreedor, "I")) {
						PersonaFisica personaFisica = new PersonaFisica();
						personaFisica.setNombre(usuarioTO.getNombre());
						personaFisica.setApellidoPaterno(usuarioTO.getApaterno());
						personaFisica.setApellidoMaterno(usuarioTO.getAmaterno());
						personaFisica.setDatosContacto(new DatosContacto());
						personaFisica.getDatosContacto().setEmailPersonal(usuarioTO.getCveUsuario());
						RegistroUsuario registroUsuario = new RegistroUsuario();
						
						HttpServletRequest request = ServletActionContext.getRequest();
						StringBuffer uri = request.getRequestURL();
						int  posicion = uri.indexOf("/", 7);
						MyLogger.Logger.log(Level.INFO, "POSICION: "+posicion );
						String url = uri.substring(0, posicion);
						MyLogger.Logger.log(Level.INFO, "IMPRIME LA URL"+url );
						
						registroUsuario.setUri(url);
						AcreedoresDAO acreedoresDAO = new AcreedoresDaoJdbcImpl();
						setAcreedorTO(acreedoresDAO.getARByID(idAcreedor));
						MyLogger.Logger.log(Level.INFO, "--nombre del acreedor--: "+ getAcreedorTO().getNombreCompleto());
						registroUsuario.setNombreAcreedor(getAcreedorTO().getNombreCompleto());
						
						try{
							if(usuarioTO.getPassword()!=null ){
								registroUsuario.setPassword(usuarioTO.getPassword());
								registroUsuario.setConfirmacion(usuarioTO.getPassword());
								mailRegistroService.sendMailRegUsuarioAcreedor(personaFisica, registroUsuario);	
							}else{
								mailRegistroService.sendMailRegUsuarioAcreedorExiste(personaFisica, registroUsuario);
							}
							
						}
						catch(Exception e){
							e.printStackTrace();
						}
						regresa = "success";
					}
				}
				
			} else {
				_usuarioTO.setCveUsuario(usuarioTO.getCveUsuario().trim());
				_usuarioTO.setIdAcreedor(idAcreedor);
				UsuarioTO _usuarioTO2 = usuarioService.getByCorreoElectronicoAcreedor(usuarioTO.getCveUsuario(), idAcreedor.toString());
				if (_usuarioTO2!=null){					
					setExisteUsuario("Y");
					MyLogger.Logger.log(Level.WARNING, "---Se encontro este usuario agregado a este acreedor--");
					regresa = "success";
					
				}else{
					MyLogger.Logger.log(Level.WARNING, "---Este usuario no esta agregado todavia a este acreedor---");
					if (usuarioService.relationUsuario(_usuarioTO, idPersona,Integer.valueOf(idGrupoSelect))) {
						PersonaFisica personaFisica = new PersonaFisica();
                        personaFisica.setDatosContacto(new DatosContacto());
                        personaFisica.getDatosContacto().setEmailPersonal(usuarioTO.getCveUsuario());
                        RegistroUsuario registroUsuario = new RegistroUsuario();
                       
                        HttpServletRequest request = ServletActionContext.getRequest();
                        StringBuffer uri = request.getRequestURL();
                        int  posicion = uri.indexOf("/", 7);
                        MyLogger.Logger.log(Level.INFO, "POSICION: "+posicion );
                        String url = uri.substring(0, posicion);
                        MyLogger.Logger.log(Level.INFO, "--url--"+url );
                       
                        registroUsuario.setUri(url);
                           
                       
                        AcreedoresDAO acreedoresDAO = new AcreedoresDaoJdbcImpl();
                        setAcreedorTO(acreedoresDAO.getARByID(idAcreedor));
                        MyLogger.Logger.log(Level.INFO, "--nombre del acreedor--: "+ getAcreedorTO().getNombreCompleto());
                        registroUsuario.setNombreAcreedor(getAcreedorTO().getNombreCompleto());
                        mailRegistroService.sendMailRegUsuarioAcreedorExiste(personaFisica, registroUsuario);
						regresa = "success";
						setExisteUsuario("");
					}else{
						setExisteUsuario("Sucedio un error al tratar de relacionar al usuario favor de reportarlo.");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return regresa;
	}
	
	public String eliminaUsuario(){
		String regresa = "failed";
		try{
			if (getIdPersonaModificar()!=null){
				ControlUsuarioService controlUsuarioService = new ControlUsuarioServiceImpl();
				
				Integer idAcreedor = (Integer) sessionMap.get(Constants.ID_ACREEDOR_REPRESENTADO);
				mx.gob.se.rug.to.UsuarioTO usuario = (mx.gob.se.rug.to.UsuarioTO) sessionMap.get("usuario");
				UsuarioTO usuarioTO = new UsuarioTO();
				usuarioTO.setIdPersona(getIdPersonaModificar());
				usuarioTO.setIdRepresentanteAcreedor(usuario.getPersona().getIdPersona());
				
				boolean regresaEliminar = controlUsuarioService.saveUsuario(usuarioTO, idAcreedor, "D");
				if (regresaEliminar){
					MyLogger.Logger.log(Level.INFO, "se logro eliminar");
					regresa= "success";
				}else{
					MyLogger.Logger.log(Level.INFO, "no se logro eliminar");
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return regresa;
	}

	public void setAcreedorTO(AcreedorTO acreedorTO) {
		this.acreedorTO = acreedorTO;
	}

	public AcreedorTO getAcreedorTO() {
		return acreedorTO;
	}
	public List<UsuarioTO> getListaFirmados() {
		return listaFirmados;
	}
	public void setListaFirmados(List<UsuarioTO> listaFirmados) {
		this.listaFirmados = listaFirmados;
	}
	public List<UsuarioTO> getListaNoFirmados() {
		return listaNoFirmados;
	}
	public void setListaNoFirmados(List<UsuarioTO> listaNoFirmados) {
		this.listaNoFirmados = listaNoFirmados;
	}

	public void setIdAcreedor(String idAcreedor) {
		this.idAcreedor = idAcreedor;
	}

	public String getIdAcreedor() {
		return idAcreedor;
	}

	public void setUsuarioTO(UsuarioTO usuarioTO) {
		this.usuarioTO = usuarioTO;
	}

	public UsuarioTO getUsuarioTO() {
		return usuarioTO;
	}

	public void setAcreedoresCatalogosService(AcreedoresCatalogosService acreedoresCatalogosService) {
		this.acreedoresCatalogosService = acreedoresCatalogosService;
	}

	public AcreedoresCatalogosService getAcreedoresCatalogosService() {
		return acreedoresCatalogosService;
	}

	public void setMailRegistroService(MailRegistroService mailRegistroService) {
		this.mailRegistroService = mailRegistroService;
	}

	public MailRegistroService getMailRegistroService() {
		return mailRegistroService;
	}

	public void setGrupoService(GrupoService grupoService) {
		this.grupoService = grupoService;
	}

	public GrupoService getGrupoService() {
		return grupoService;
	}

	public void setGrupos(List<GrupoPerfilTO> grupos) {
		this.grupos = grupos;
	}

	public List<GrupoPerfilTO> getGrupos() {
		return grupos;
	}

	public void setIdPersonaModificar(Integer idPersonaModificar) {
		this.idPersonaModificar = idPersonaModificar;
	}

	public Integer getIdPersonaModificar() {
		return idPersonaModificar;
	}
	public Integer getIdGrupoSelect() {
		return idGrupoSelect;
	}
	
	public void setIdGrupoSelect(Integer idGrupoSelect) {
		this.idGrupoSelect = idGrupoSelect;
	}
	public void setGrupoElegido(String grupoElegido) {
		this.grupoElegido = grupoElegido;
	}

	public String getGrupoElegido() {
		return grupoElegido;
	}
	
	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	public Integer get_idAcreedor() {
		return _idAcreedor;
	}

	public void set_idAcreedor(Integer idAcreedor) {
		_idAcreedor = idAcreedor;
	}

	public void setExisteUsuario(String existeUsuario) {
		this.existeUsuario = existeUsuario;
	}

	public String getExisteUsuario() {
		return existeUsuario;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public void setRfcError(String rfcError) {
		this.rfcError = rfcError;
	}

	public String getRfcError() {
		return rfcError;
	}
	
}
