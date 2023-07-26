package mx.gob.se.rug.acreedores.action;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import mx.gob.se.rug.acceso.dao.AccesoDAO;
import mx.gob.se.rug.acreedores.exception.AcreedoresException;
import mx.gob.se.rug.acreedores.service.AcreedoresCatalogosService;
import mx.gob.se.rug.acreedores.service.AcreedoresService;
import mx.gob.se.rug.acreedores.service.GrupoService;
import mx.gob.se.rug.acreedores.service.impl.AcreedoresServiceImpl;
import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.acreedores.to.GrupoPerfilTO;
import mx.gob.se.rug.acreedores.to.PerfilTO;
import mx.gob.se.rug.administracion.dto.RegistroUsuario;
import mx.gob.se.rug.administracion.service.UsuarioService;
import mx.gob.se.rug.common.dto.Mensaje;
import mx.gob.se.rug.common.service.HomeService;
import mx.gob.se.rug.common.util.RequestContext;
import mx.gob.se.rug.dto.PersonaFisica;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.fwk.listener.RugSessionListener;
import mx.gob.se.rug.garantia.dao.GarantiasDAO;
import mx.gob.se.rug.inscripcion.dao.InscripcionDAO;
import mx.gob.se.rug.inscripcion.dao.NacionalidadDAO;
import mx.gob.se.rug.inscripcion.to.AltaParteTO;
import mx.gob.se.rug.inscripcion.to.NacionalidadTO;
import mx.gob.se.rug.mailservice.MailRegistroService;
import mx.gob.se.rug.seguridad.dao.PrivilegiosDAO;
import mx.gob.se.rug.seguridad.to.PrivilegioTO;
import mx.gob.se.rug.seguridad.to.PrivilegiosTO;
import mx.gob.se.rug.to.PersonaTO;
import mx.gob.se.rug.to.TipoPersona;
import mx.gob.se.rug.to.TipoTo;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.context.annotation.Scope;

@Scope("prototype")
public class AcreedorAction extends RugBaseAction implements ServletRequestAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String OPERACION_I = "I";
	private List<TipoPersona> listaTipoPersona;
	private List <AcreedorTO> listaAcreedores;
	private List<AcreedorTO> listaAcreedoresSinFirma;
	private List <NacionalidadTO> listaNacionalidad;
	private AcreedorTO acreedorTO = new AcreedorTO();
	private boolean vacia;
	private Integer idPersona;
	private Integer idTramite;
	private Integer idAcreedorBaja;
	private Integer idLocalidad;
	private Integer idColonia;
	private Integer idNacionalidad;
	private Integer idNacionalidadMoral;
	private RegistroUsuario registroUsuario;
	private PersonaFisica personaFisica; 
	private UsuarioService usuarioService;
	private MailRegistroService mailRegistroService;
	private AcreedoresCatalogosService acreedoresCatalogosService;
	private GrupoPerfilTO grupoPerfilTO;
	private GrupoService grupoService;
	private List<GrupoPerfilTO> grupos;
	private List<PersonaFisica> usuariosAfiliados;
	private List<PerfilTO> usuariosPerfiles;
	private String perfilValida;
	
	private PersonaFisica personaFisicaDos;
	private PersonaFisica personaFisicaTres;
	private List<AcreedorTO> listaAcreedoresRep;
	private List<AcreedorTO> listaAcreedoresRepSinFirma;
	private HomeService homeService;
	private List<GrupoPerfilTO> listaGrupos;
	private List<PerfilTO> listaperfiles;
	private String grupoElegido;
	
	List<TipoTo> listaTipo;
	private Integer esAutoridad = 0;
	AltaParteTO altaParte;
	
	private AcreedoresService acreedoresService = new AcreedoresServiceImpl();
	
	
	public Integer getIdLocalidad() {
		return idLocalidad;
	}

	public void setIdLocalidad(Integer idLocalidad) {
		this.idLocalidad = idLocalidad;
	}

	public Integer getIdColonia() {
		return idColonia;
	}

	public void setIdColonia(Integer idColonia) {
		this.idColonia = idColonia;
	}
	
	public List<GrupoPerfilTO> getGrupos() throws AcreedoresException{
		UsuarioTO usuario = (UsuarioTO) sessionMap.get("usuario");
		List<GrupoPerfilTO> grupos = acreedoresCatalogosService.getGrupos(usuario.getPersona().getIdPersona());
		return grupos;
	}

	public String getVariableValida(){
		UsuarioTO usuario = (UsuarioTO) sessionMap.get("usuario");
		String variableValida = usuario.getPersona().getPerfil();
		return variableValida;
	}
	

	public String inicia(){
		String regresa = "failed";
		//===============================================================================================
		UsuarioTO u = new UsuarioTO();
		PersonaTO persona = new PersonaTO();
		AccesoDAO udao = new AccesoDAO();

		u.setNombre(principal.getUserPrincipal().toString());
		
		if(principal.getUserPrincipal().toString().equals(u.getNombre())){
			MyLogger.Logger.log(Level.INFO, " logeado ");
			PersonaFisica personaFisica = udao.getIdPersona(u.getNombre());
			
			persona.setIdPersona(personaFisica.getIdPersona());
			persona.setPerfil(personaFisica.getPerfil());
			u.setPersona(persona);
			sessionMap.put("usuario",u);

		}	
		//===============================================================================================
		listaTipoPersona = new ArrayList<>();
		TipoPersona tipoPersona = new TipoPersona();
		tipoPersona.setId("PF");
		tipoPersona.setDesc("Persona Individual");
		listaTipoPersona.add(tipoPersona);
		tipoPersona = new TipoPersona();
		tipoPersona.setId("PM");
		tipoPersona.setDesc("Persona Jurídica");
		listaTipoPersona.add(tipoPersona);
		
		NacionalidadDAO dao = new NacionalidadDAO();
		listaNacionalidad = dao.getNacionalidades();
		
		listaTipo = new ArrayList<>();
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
		
		listaAcreedores = acreedoresService .getAcreedoresByPersona(persona.getIdPersona());
		listaAcreedoresSinFirma = acreedoresService.getAcreedoresSinFirmaByPersona(persona.getIdPersona());
		
		PrivilegiosDAO privilegiosDAO = new PrivilegiosDAO();
		PrivilegiosTO privilegiosTO = new PrivilegiosTO();
		privilegiosTO.setIdRecurso(new Integer(3));
		privilegiosTO = privilegiosDAO.getPrivilegios(privilegiosTO, new Integer(persona.getIdPersona()));
		Map<Integer, PrivilegioTO> priv = privilegiosTO.getMapPrivilegio();
		if (priv.get(new Integer(36)) != null) {
			esAutoridad = 1;
		}
		
		try{
						
			UsuarioTO usuario = (UsuarioTO) sessionMap.get(mx.gob.se.rug.constants.Constants.USUARIO);
			setPerfilValida(usuario.getPersona().getPerfil());
			MyLogger.Logger.log(Level.INFO,"--ID PERSONA AUTENTICADA--: "+usuario.getPersona().getIdPersona());
			MyLogger.Logger.log(Level.INFO, "--clave perfil en nuevo variable--"+getPerfilValida());
			setIdPersona(usuario.getPersona().getIdPersona());
			//Felix
//			setListaAcreedores(acreedoresService.getAcreedores(getIdPersona()));
//			setListaTipoPersona(acreedoresService.getTiposPersona());
//			setListaNacionalidad(acreedoresService.getNacionalidades());
			getPerfiles();

			List<AcreedorTO> acreedoresConPrivilegio = new ArrayList<>();
			for(AcreedorTO acreedor : listaAcreedores) {
				if(privilegiosDAO.getTienePermiso(acreedor.getIdPersona(), usuario.getPersona().getIdPersona(), new Integer(17))) {
					// tiene privilegio para usuarios y grupos
					acreedor.setPrivilegioGrupos(1);
				}
				if(privilegiosDAO.getTienePermiso(acreedor.getIdPersona(), usuario.getPersona().getIdPersona(), new Integer(22))) {
					// tiene privilegio para modificar
					acreedor.setPrivilegioModificar(1);
				}
				acreedoresConPrivilegio.add(acreedor);
			}
			listaAcreedores = acreedoresConPrivilegio;
			
			MyLogger.Logger.log(Level.INFO, "--clave Perfil--: "+usuario.getPersona().getPerfil());
			setGrupos(acreedoresCatalogosService.getGrupos(usuario.getPersona().getIdPersona()));
			MyLogger.Logger.log(Level.INFO, "--clave usuario--: "+usuario.getNombre());
			setUsuariosAfiliados(acreedoresCatalogosService.getUsuariosAfiliados(usuario.getPersona().getIdPersona()));
			
			personaFisicaDos = acreedoresCatalogosService.getRegistro(usuario.getNombre());
			
			MyLogger.Logger.log(Level.INFO, "IMPRIME LA CLAVE USUARIO DEL ACREEDOR DE ESTE REGISTRO: "+personaFisicaDos.getClaveAcreedor());
			MyLogger.Logger.log(Level.INFO, "IMPRIME EL ID PERSONA DE ESTE REGISTRO: "+personaFisicaDos.getIdPersona());
			if(personaFisicaDos.getClaveAcreedor()!=null){
				personaFisicaTres=acreedoresCatalogosService.getRegistro(personaFisicaDos.getClaveAcreedor());
				MyLogger.Logger.log(Level.INFO, "IMPRIME LA CLAVE USUARIO SOLO DEL ACREEDOR: "+personaFisicaTres.getClaveAcreedor());
				MyLogger.Logger.log(Level.INFO, "IMPRIME EL ID PERSONA DEL ACREEDOR: "+personaFisicaTres.getIdPersona());
				
				setListaAcreedoresRep(acreedoresService.getAcreedoresByPersona(personaFisicaTres.getIdPersona()));
				
				setListaAcreedoresRepSinFirma(acreedoresService.getAcreedoresSinFirmaByPersona(personaFisicaTres.getIdPersona()));
				
			}
			
			String perfil=usuario.getPersona().getPerfil();
			MyLogger.Logger.log(Level.INFO, "--perfil-- :"+perfil );
//			setUsuariosPerfiles(acreedoresCatalogosService.getUsuariosPerfiles(usuario.getPersona().getPerfil()));
			MyLogger.Logger.log(Level.INFO, "--grupos--"+  grupos);

//			if (getListaAcreedores().size() == 0){
//				setVacia(true);
//			}else{
//				setVacia(false);
//			}
			regresa = SUCCESS;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return regresa;
	}
	
	public String guarda(){
		
		System.out.println("----------------entre aqui, si aqui---------------------------");
		
		String regresa = "failed";
		try{
			if (getAcreedorTO()!=null){
				
				
				MyLogger.Logger.log(Level.INFO, "Valor de la colonia : " + getIdColonia());
				acreedorTO.setIdColonia(getIdColonia());
				acreedorTO.setIdNacionalidad(1);
				acreedorTO.setIdLocalidad(getIdLocalidad());
				boolean seAgrego = acreedoresService.insertAcreedorRep(acreedorTO, getIdPersona());
				if (seAgrego){
					regresa = "success";
				}
			}
			setAcreedorTO(new AcreedorTO());
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return regresa;
	}
	
	public String eliminaAcreedor(){
		String regresa = "failed";
		if (getIdAcreedorBaja() != null){
			boolean res = acreedoresService.bajaAcreedorRep(getIdPersona(), getIdAcreedorBaja());
			if (res){
				regresa = "success";
			}
		}
		return regresa;
	}
	
	public String addUsuario(){
		String regresa = "failed";
		try{
			UsuarioTO usuario = (UsuarioTO) sessionMap.get("usuario");
			setPerfilValida(usuario.getPersona().getPerfil());
			getPerfilValida();
			MyLogger.Logger.log(Level.INFO, "--clave perfil en nuevo variable--"+getPerfilValida());
			setIdPersona(usuario.getPersona().getIdPersona());
			MyLogger.Logger.log(Level.INFO, "IMPRIME EL PERFIL:"+getPerfilValida());
			getPerfilValida();

			MyLogger.Logger.log(Level.INFO, "--clave Perfil--: "+usuario.getPersona().getPerfil());			
			setGrupos(acreedoresCatalogosService.getGrupos(usuario.getPersona().getIdPersona()));
			MyLogger.Logger.log(Level.INFO, "--clave usuario--: "+usuario.getNombre());
			setUsuariosAfiliados(acreedoresCatalogosService.getUsuariosAfiliados(usuario.getPersona().getIdPersona()));
			
			
			String perfil=usuario.getPersona().getPerfil();
			MyLogger.Logger.log(Level.INFO, "--perfil-- :"+perfil );
			MyLogger.Logger.log(Level.INFO, "--grupos--"+  grupos);

			regresa = SUCCESS;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return regresa;
	}
	
	public String save() throws Exception {
		
		System.out.println("----------------entre por aca, si por aca---------------------------");
		
		MyLogger.Logger.log(Level.INFO, "-- save --");
		//======================================================================
		UsuarioTO usuario = (UsuarioTO) sessionMap.get("usuario");
		setIdPersona(usuario.getPersona().getIdPersona());
		MyLogger.Logger.log(Level.INFO, "--clave Perfil SAVE-ACTION--: "+usuario.getPersona().getPerfil());
		MyLogger.Logger.log(Level.INFO, "--clave usuario--: "+usuario.getNombre());
		setUsuariosAfiliados(acreedoresCatalogosService.getUsuariosAfiliados(usuario.getPersona().getIdPersona()));
		setPerfilValida(usuario.getPersona().getPerfil());
		MyLogger.Logger.log(Level.INFO, "IMPRIME EL PERFIL:"+getPerfilValida());
		getPerfilValida();
		String perfil=usuario.getPersona().getPerfil();
		MyLogger.Logger.log(Level.INFO, "--perfil ACTION-- :"+perfil );
		//construccion de uri
		HttpServletRequest request = ServletActionContext.getRequest();
				
		
		StringBuffer uri = request.getRequestURL();
		int  posicion = uri.indexOf("/", 7);
		MyLogger.Logger.log(Level.INFO, "POSICION: "+posicion );
		String url = uri.substring(0, posicion);
		MyLogger.Logger.log(Level.INFO, "IMPRIME LA URL"+url );
		
		
		registroUsuario.setUri(url);
		MyLogger.Logger.log(Level.INFO, "--obteniedo nuevo parametro uri: --"+ registroUsuario.getUri());
		

		
		if(!getPersonaFisica().getPerfil().equals("ADMINISTRADOR")){
			MyLogger.Logger.log(Level.WARNING, "VALIDACION DE OPERATIVO Y GRUPO NULL");
			if(personaFisica.getGrupo()==null){
				MyLogger.Logger.log(Level.WARNING, "CUANDO EL GRUPO ES NULL");
				addActionError(getText("acreedores.usuario.msg.registro.grupo.error"));
				return ERROR;
			}else{
				
				
				personaFisica.setClaveUsuarioPadre(getUser().getProfile().getEmail());
				registroUsuario.setTipoOperacion(OPERACION_I);
				
				MyLogger.Logger.log(Level.INFO, "IMPRIME EL PERFIL: "+ personaFisica.getPerfil());
				InscripcionDAO inscripcionDAO= new InscripcionDAO();
				
				MyLogger.Logger.log(Level.INFO, "usuario.getPersona().getIdPersona()::::::::"+usuario.getPersona().getIdPersona());
				Integer idTramiteNuevo=inscripcionDAO.insert(usuario.getPersona().getIdPersona(), new Integer("14"));
				getRegistroUsuario().setIdTramiteNuevo(idTramiteNuevo);
				Mensaje mensaje = usuarioService.save(getPersonaFisica(), getRegistroUsuario());
				GarantiasDAO garantiasDAO= new GarantiasDAO();
				garantiasDAO.altaBitacoraTramite(idTramiteNuevo, new Integer(5),new Integer(0), null, "V");
				
				
				MyLogger.Logger.log(Level.INFO, "PASO 6 ES ADMINISTRADOR");
				MyLogger.Logger.log(Level.INFO, "mensaje Action: " + mensaje);
				MyLogger.Logger.log(Level.INFO, "--clave usuario padre o auntenticado-- "+ getUser().getProfile().getEmail());
				MyLogger.Logger.log(Level.INFO, "--clave suario padre: "+ personaFisica.getClaveUsuarioPadre());
				
				if (mensaje != null) {
					if (mensaje.getRespuesta() == 0) {
						addActionMessage(getText("acreedores.usuario.msg.registro.success"));
						//ENVIO DEL MAIL SI FUE EXITOSA LA INSERCION
						mailRegistroService.sendMailRegUsuarioAcreedor(personaFisica, registroUsuario);
						return SUCCESS;
					}
					if (mensaje.getRespuesta() == 1 ) {
						addActionError(getText("acreedores.usuario.msg.registro.error"));
						return ERROR;
					}
				}
				
			}
		}else{
			personaFisica.setClaveUsuarioPadre(getUser().getProfile().getEmail());
			registroUsuario.setTipoOperacion(OPERACION_I);
			MyLogger.Logger.log(Level.INFO, "IMPRIME EL PERFIL: "+ personaFisica.getPerfil());
			
//			int tipoTramite = 12;
//			Mensaje mensajeAltaTramite =usuarioService.AltaTramite(personaFisica, tipoTramite);
//			MyLogger.Logger.log(Level.INFO,"IMPRIME EL ID MENSAJE: "+mensajeAltaTramite.getId());
//			MyLogger.Logger.log(Level.INFO,"IMPRIME EL TEXTO MENSAJE: "+mensajeAltaTramite.getMensaje());
//			MyLogger.Logger.log(Level.INFO,"IMPRIME EL RESPUESTA MENSAJE: "+mensajeAltaTramite.getRespuesta());
			InscripcionDAO inscripcionDAO= new InscripcionDAO();
			
			MyLogger.Logger.log(Level.INFO, "usuario.getPersona().getIdPersona()::::::::"+usuario.getPersona().getIdPersona());
			Integer idTramiteNuevo=inscripcionDAO.insert(usuario.getPersona().getIdPersona(), new Integer("14"));
			getRegistroUsuario().setIdTramiteNuevo(idTramiteNuevo);
			Mensaje mensaje = usuarioService.save(getPersonaFisica(), getRegistroUsuario());
			GarantiasDAO garantiasDAO= new GarantiasDAO();
			garantiasDAO.altaBitacoraTramite(idTramiteNuevo, new Integer(5),new Integer(0), null, "V");
			
			
			MyLogger.Logger.log(Level.INFO, "PASO 6 ES ADMINISTRADOR");
			MyLogger.Logger.log(Level.INFO, "mensaje Action: " + mensaje);
			MyLogger.Logger.log(Level.INFO, "--clave usuario padre o auntenticado-- "+ getUser().getProfile().getEmail());
			MyLogger.Logger.log(Level.INFO, "--clave suario padre: "+ personaFisica.getClaveUsuarioPadre());
						
			if (mensaje != null) {
				if (mensaje.getRespuesta() == 0) {
					addActionMessage(getText("acreedores.usuario.msg.registro.success"));
					//ENVIO DEL MAIL SI FUE EXITOSA LA INSERCION
					mailRegistroService.sendMailRegUsuarioAcreedor(personaFisica, registroUsuario);
					return SUCCESS;
				}
				if (mensaje.getRespuesta() == 1 ) {
					addActionError(getText("acreedores.usuario.msg.registro.error"));
					return ERROR;
				}
			}
		}
	
		return ERROR;
	}
	
	public List<PerfilTO> getPerfiles() throws Exception{
		MyLogger.Logger.log(Level.INFO, "-- getPerfiles --"+acreedoresCatalogosService.getPerfiles());
		return acreedoresCatalogosService.getPerfiles();
	}
	
	public String altaGrupo() throws Exception{
		MyLogger.Logger.log(Level.INFO, "-- getPerfiles --"+acreedoresCatalogosService.getPerfiles());
		
		MyLogger.Logger.log(Level.INFO, "--id Persona-- "+ getUser().getIdUser());
		MyLogger.Logger.log(Level.INFO, "--nombre--"+ getPersonaFisica().getNombre());
		MyLogger.Logger.log(Level.INFO, "--apellido Paterno--"+ getPersonaFisica().getApellidoPaterno());
		MyLogger.Logger.log(Level.INFO, "--apellido Materno--"+ getPersonaFisica().getApellidoMaterno());
		MyLogger.Logger.log(Level.INFO, "--Email"+ getPersonaFisica().getDatosContacto().getEmailPersonal());
		MyLogger.Logger.log(Level.INFO, "--perfil--"+ getPersonaFisica().getPerfil());
		MyLogger.Logger.log(Level.INFO, "--grupo--"+ getPersonaFisica().getGrupo().getId());
		
		GrupoPerfilTO grupoPerfilTO = new GrupoPerfilTO();
		setGrupoPerfilTO(grupoPerfilTO);
		Mensaje mensaje = new Mensaje();
		setMensaje(mensaje);
		getPerfiles();
		return SUCCESS;
	}
	
	public String saveGrupo() throws AcreedoresException{
		
		MyLogger.Logger.log(Level.INFO, "--metodo save--");
		
		MyLogger.Logger.log(Level.INFO, "--nombre--"+ getPersonaFisica().getNombre());
		MyLogger.Logger.log(Level.INFO, "--apellido Paterno--"+ getPersonaFisica().getApellidoPaterno());
		MyLogger.Logger.log(Level.INFO, "--apellido Materno--"+ getPersonaFisica().getApellidoMaterno());
		MyLogger.Logger.log(Level.INFO, "--Email"+ getPersonaFisica().getDatosContacto().getEmailPersonal());
		MyLogger.Logger.log(Level.INFO, "--perfil--"+ getPersonaFisica().getPerfil());
		MyLogger.Logger.log(Level.INFO, "--grupo--"+ getPersonaFisica().getGrupo().getId());
		
//		Mensaje mensaje = grupoService.saveGrupo(grupoPerfilTO, grupoPerfilTO.getId());
//		if(mensaje.getRespuesta() == 0){
//			addActionMessage(getText("acreedores.grupo.msg.registro.success"));
			return SUCCESS;
//		}
//		if(mensaje.getRespuesta() == -1){
//			addActionError(mensaje.getMensaje());
//			Mensaje mensajeDos = new Mensaje();
//			setMensaje(mensajeDos);
//			return ERROR;
//		}
//		return ERROR;
	}
	
	public String consultaGrupo() throws AcreedoresException{
		MyLogger.Logger.log(Level.INFO, "CONSULTA DE GRUPOS");
		UsuarioTO usuario = (UsuarioTO) sessionMap.get("usuario");
		setGrupos(acreedoresCatalogosService.getGrupos(usuario.getPersona().getIdPersona()));
		return SUCCESS;
	}
	
	public String busquedaGrupo() throws AcreedoresException {
		MyLogger.Logger.log(Level.INFO, "--busqueda de grupos--");
		UsuarioTO usuario = (UsuarioTO) sessionMap.get("usuario");
		setGrupos(acreedoresCatalogosService.getGrupos(usuario.getPersona().getIdPersona()));
		MyLogger.Logger.log(Level.INFO, "--id de opcion grupo: -- "+personaFisica.getGrupo().getId());
		grupoElegido =  personaFisica.getGrupo().getId();
		listaperfiles = acreedoresCatalogosService.busquedaGrupo(personaFisica);
		return SUCCESS;
	}
	
	public void setListaTipoPersona(List<TipoPersona> listaTipoPersona) {
		this.listaTipoPersona = listaTipoPersona;
	}

	public List<TipoPersona> getListaTipoPersona() {
		return listaTipoPersona;
	}

	public void setAcreedorTO(AcreedorTO acreedorTO) {
		this.acreedorTO = acreedorTO;
	}

	public AcreedorTO getAcreedorTO() {
		return acreedorTO;
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		HttpSession session=request.getSession();
		session.setAttribute(RugSessionListener.KEY_REQUESTURI, RequestContext.getAttribute(RequestContext.KEY_REQUESTURI));
		session.setAttribute(RugSessionListener.KEY_REMOTEADDR, RequestContext.getAttribute(RequestContext.KEY_REMOTEADDR));
	}

	public void setListaAcreedores(List <AcreedorTO> listaAcreedores) {
		this.listaAcreedores = listaAcreedores;
	}

	public List <AcreedorTO> getListaAcreedores() {
		return listaAcreedores;
	}

	public void setVacia(boolean vacia) {
		this.vacia = vacia;
	}

	public boolean isVacia() {
		return vacia;
	}

	public void setIdPersona(Integer idPersona) {
		this.idPersona = idPersona;
	}

	public Integer getIdPersona() {
		return idPersona;
	}

	public void setIdAcreedorBaja(Integer idAcreedorBaja) {
		this.idAcreedorBaja = idAcreedorBaja;
	}

	public Integer getIdAcreedorBaja() {
		return idAcreedorBaja;
	}

	public void setListaNacionalidad(List <NacionalidadTO> listaNacionalidad) {
		this.listaNacionalidad = listaNacionalidad;
	}

	public List <NacionalidadTO> getListaNacionalidad() {
		return listaNacionalidad;
	}

	public void setIdNacionalidad(Integer idNacionalidad) {
		this.idNacionalidad = idNacionalidad;
	}

	public Integer getIdNacionalidad() {
		return idNacionalidad;
	}

	public void setIdNacionalidadMoral(Integer idNacionalidadMoral) {
		this.idNacionalidadMoral = idNacionalidadMoral;
	}

	public Integer getIdNacionalidadMoral() {
		return idNacionalidadMoral;
	}

	public void setRegistroUsuario(RegistroUsuario registroUsuario) {
		this.registroUsuario = registroUsuario;
	}

	public RegistroUsuario getRegistroUsuario() {
		return registroUsuario;
	}

	public void setPersonaFisica(PersonaFisica personaFisica) {
		this.personaFisica = personaFisica;
	}

	public PersonaFisica getPersonaFisica() {
		return personaFisica;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setMailRegistroService(MailRegistroService mailRegistroService) {
		this.mailRegistroService = mailRegistroService;
	}

	public MailRegistroService getMailRegistroService() {
		return mailRegistroService;
	}

	public void setAcreedoresCatalogosService(AcreedoresCatalogosService acreedoresCatalogosService) {
		this.acreedoresCatalogosService = acreedoresCatalogosService;
	}

	public void setGrupoPerfilTO(GrupoPerfilTO grupoPerfilTO) {
		this.grupoPerfilTO = grupoPerfilTO;
	}

	public GrupoPerfilTO getGrupoPerfilTO() {
		return grupoPerfilTO;
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

//	public List<GrupoPerfilTO> getGrupos() {
//		return grupos;
//	}

	public void setUsuariosAfiliados(List<PersonaFisica> usuariosAfiliados) {
		this.usuariosAfiliados = usuariosAfiliados;
	}

	public List<PersonaFisica> getUsuariosAfiliados() {
		return usuariosAfiliados;
	}

	public void setUsuariosPerfiles(List<PerfilTO> usuariosPerfiles) {
		this.usuariosPerfiles = usuariosPerfiles;
	}

	public List<PerfilTO> getUsuariosPerfiles() {
		return usuariosPerfiles;
	}

	public void setPerfilValida(String perfilValida) {
		this.perfilValida = perfilValida;
	}

	public String getPerfilValida() {
		return perfilValida;
	}

	public void setPersonaFisicaDos(PersonaFisica personaFisicaDos) {
		this.personaFisicaDos = personaFisicaDos;
	}

	public PersonaFisica getPersonaFisicaDos() {
		return personaFisicaDos;
	}

	public void setPersonaFisicaTres(PersonaFisica personaFisicaTres) {
		this.personaFisicaTres = personaFisicaTres;
	}

	public PersonaFisica getPersonaFisicaTres() {
		return personaFisicaTres;
	}

	public void setListaAcreedoresRep(List<AcreedorTO> listaAcreedoresRep) {
		this.listaAcreedoresRep = listaAcreedoresRep;
	}

	public List<AcreedorTO> getListaAcreedoresRep() {
		return listaAcreedoresRep;
	}

	public void setListaAcreedoresRepSinFirma(
			List<AcreedorTO> listaAcreedoresRepSinFirma) {
		this.listaAcreedoresRepSinFirma = listaAcreedoresRepSinFirma;
	}

	public List<AcreedorTO> getListaAcreedoresRepSinFirma() {
		return listaAcreedoresRepSinFirma;
	}
	
	public List<PersonaFisica> getListPerfiles() {
		List<PersonaFisica> listPerfil = new ArrayList<PersonaFisica>();
		PersonaFisica administrador = new PersonaFisica();
		administrador.setPerfil(getText("ADMINISTRADOR"));
		administrador.setDescripcionPerfil(getText("ADMINISTRADOR"));
		PersonaFisica operativo = new PersonaFisica();
		operativo.setPerfil(getText("OPERATIVO"));
		operativo.setDescripcionPerfil(getText("OPERATIVO"));
		listPerfil.add(administrador);
		listPerfil.add(operativo);
		return listPerfil;

	}

	public void setHomeService(HomeService homeService) {
		this.homeService = homeService;
	}

	public HomeService getHomeService() {
		return homeService;
	}

	public void setListaGrupos(List<GrupoPerfilTO> listaGrupos) {
		this.listaGrupos = listaGrupos;
	}

	public List<GrupoPerfilTO> getListaGrupos() {
		return listaGrupos;
	}

	public void setListaperfiles(List<PerfilTO> listaperfiles) {
		this.listaperfiles = listaperfiles;
	}

	public List<PerfilTO> getListaperfiles() {
		return listaperfiles;
	}

	public void setGrupoElegido(String grupoElegido) {
		this.grupoElegido = grupoElegido;
	}

	public String getGrupoElegido() {
		return grupoElegido;
	}

	public Integer getIdTramite() {
		return idTramite;
	}

	public void setIdTramite(Integer idTramite) {
		this.idTramite = idTramite;
	}

	public List<TipoTo> getListaTipo() {
		return listaTipo;
	}

	public void setListaTipo(List<TipoTo> listaTipo) {
		this.listaTipo = listaTipo;
	}

	public Integer isEsAutoridad() {
		return esAutoridad;
	}

	public void setEsAutoridad(Integer esAutoridad) {
		this.esAutoridad = esAutoridad;
	}

	public AltaParteTO getAltaParte() {
		return altaParte;
	}

	public void setAltaParte(AltaParteTO altaParte) {
		this.altaParte = altaParte;
	}

	public List<AcreedorTO> getListaAcreedoresSinFirma() {
		return listaAcreedoresSinFirma;
	}

	public void setListaAcreedoresSinFirma(List<AcreedorTO> listaAcreedoresSinFirma) {
		this.listaAcreedoresSinFirma = listaAcreedoresSinFirma;
	}
}
