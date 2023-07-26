
package mx.gob.se.rug.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import mx.gob.se.rug.acreedores.dao.AcreedoresDAO;
import mx.gob.se.rug.acreedores.dao.impl.AcreedoresDaoJdbcImpl;
import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.avisopreventivo.service.AvisoPreventivoService;
import mx.gob.se.rug.boleta.serviceImpl.BoletaServiceImpl;
import mx.gob.se.rug.common.util.RequestContext;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.exception.InfrastructureException;
import mx.gob.se.rug.exception.NoDataFoundException;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.fwk.listener.RugSessionListener;
import mx.gob.se.rug.inscripcion.service.InscripcionService;
import mx.gob.se.rug.inscripcion.service.impl.InscripcionServiceImpl;
import mx.gob.se.rug.juez.service.JuezService;
import mx.gob.se.rug.seguridad.dao.PrivilegiosDAO;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;

import org.apache.struts2.interceptor.ServletRequestAware;


public class AvisoAction extends RugBaseAction implements ServletRequestAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String SUCCESS = "success";
	private String descripcionBienes;
	private Integer idAcreedorTO;
	private AcreedorTO acreedorTO;
	private List <AcreedorTO> listaAcreedores;
	private String idTramite;
	private Integer idUsuario;
	private String nomAcreedor;
	private String autoridad;
	private String contenidoHTML;
	
	public String detalle(){
		BoletaServiceImpl boleta = new BoletaServiceImpl();
		String regresa = "failed";
		try {
			String string = new String (boleta.getAvisoPreventivo(Integer.valueOf(getIdTramite())).getHtml().toString().replaceAll("<!\\[CDATA\\["," ").replaceAll("\\]\\]>",""));
			setContenidoHTML(string);
			regresa = "success";
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoDataFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InfrastructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return regresa;
	}
	
	public String creaTramAviso(){
		String regresa = "failed";
		try{
			if (getIdAcreedorTO()!=null){
				InscripcionService inscripcionService = new InscripcionServiceImpl();
				AvisoPreventivoService avisoService= new AvisoPreventivoService();
				UsuarioTO usuarioTO = (UsuarioTO) sessionMap.get(Constants.USUARIO);
				
				MyLogger.Logger.log(Level.INFO, "el id acreedor que me trae es: "+ idAcreedorTO);
				
				setAcreedorTO(inscripcionService.getAcreedorByID(idAcreedorTO));
				setIdTramite(avisoService.altaTramite(usuarioTO.getPersona().getIdPersona(), 3, getAcreedorTO()).toString());
				setIdUsuario(usuarioTO.getPersona().getIdPersona());
				
				sessionMap.put(Constants.ID_TRAMITE_NUEVO, new Integer(getIdTramite()));
				sessionMap.put(Constants.ID_ACREEDOR_REPRESENTADO,new Integer(getIdAcreedorTO()));
				
				MyLogger.Logger.log(Level.INFO, "el id tramite incompleto en el aviso preventivo es; " + getIdTramite());
				MyLogger.Logger.log(Level.INFO, "el id usuario en el aviso preventivo es; " + getIdUsuario());
				regresa="success";
			}else{
				MyLogger.Logger.log(Level.WARNING, "no sel Acreedor");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		MyLogger.Logger.log(Level.INFO, regresa+"::creatram");
		return regresa;
	}
	
	
	public String iniciaAviso(){
		String regresa = "failed";
		try{
			MyLogger.Logger.log(Level.INFO, "-Inicia Aviso --");
			UsuarioTO usuario = (UsuarioTO) sessionMap.get("usuario");
			
			setAcreedorTO(new AcreedorTO());
			setIdUsuario(usuario.getPersona().getIdPersona());
			AvisoPreventivoService avisoService= new AvisoPreventivoService();
			PrivilegiosDAO privilegiosDAO = new PrivilegiosDAO();
			Integer idPersona = usuario.getPersona().getIdPersona();
			List <AcreedorTO> lista = avisoService.getAcreedoresByID(usuario.getPersona().getIdPersona());
			List <AcreedorTO> listaAcreedores = new ArrayList<AcreedorTO>();
			Iterator<AcreedorTO> it = lista.iterator();
			AcreedorTO acre;
			while (it.hasNext()){
				acre = it.next();
				if (privilegiosDAO.getTienePermiso(acre.getIdPersona(),idPersona,new Integer(14))){
					listaAcreedores.add(acre);
				}
			}
			setListaAcreedores(listaAcreedores);	
			
			if (getListaAcreedores().size()>0){
				regresa = "success";
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return regresa;
	}
	
	public String avisoPreventivo(){
		String regresa = "failed";
		try{
			AvisoPreventivoService avisoService= new AvisoPreventivoService();
			Integer idPersonaAcreedor= (Integer)sessionMap.get(Constants.ID_ACREEDOR_REPRESENTADO);
			UsuarioTO usuarioTO= (UsuarioTO) sessionMap.get(Constants.USUARIO);
			Integer idTramiteNuevo=(Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO);
			
			boolean res = avisoService.aviso(usuarioTO.getPersona().getIdPersona(), getDescripcionBienes(), idTramiteNuevo);
			
			InscripcionService inscripcionService= new InscripcionServiceImpl();
			boolean actbit = inscripcionService.insertBitacoraTramite(idTramiteNuevo, 5, 0, null, "V");
			if ((getAutoridad()!= "") && (getAutoridad()!= null)){
				JuezService juezService = new JuezService();
				MyLogger.Logger.log(Level.INFO, "entra al action de aviso preventivo: " +idTramiteNuevo+ "la autoridad"+ getAutoridad());
				juezService.insertJuez(idTramiteNuevo, getAutoridad());
			}
			if (actbit){
				regresa = "success";
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return regresa;
	}
	
	public String inicia(){
		String regresa = "error";
		try{
			MyLogger.Logger.log(Level.INFO, "--inicia--");
Integer idPersonaAcreedor= (Integer)sessionMap.get(Constants.ID_ACREEDOR_REPRESENTADO);
UsuarioTO usuarioTO= (UsuarioTO) sessionMap.get(Constants.USUARIO);
Integer idTramiteNuevo=(Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO);

				AcreedoresDAO acreedoresDAO= new AcreedoresDaoJdbcImpl();
				AcreedorTO acreedorTO= acreedoresDAO.getARByID(idPersonaAcreedor);
				MyLogger.Logger.log(Level.INFO, "--idTramiteNuevo--"+idTramiteNuevo);
				MyLogger.Logger.log(Level.INFO, "--idPersonaAcreedor--"+idPersonaAcreedor);		
				if (acreedorTO.getNombre()==null){
					setNomAcreedor(acreedorTO.getRazonSocial());
				}else{
					setNomAcreedor(acreedorTO.getNombre() + " " + acreedorTO.getApellidoPaterno() + " " + acreedorTO.getApellidoMaterno());
				}
				MyLogger.Logger.log(Level.INFO, "------nombre de acreedor aviso  " + getNomAcreedor() );
				setIdTramite(idTramiteNuevo.toString());
				setIdUsuario(usuarioTO.getPersona().getIdPersona());
				setAutoridad("");
				regresa = "success";
				MyLogger.Logger.log(Level.INFO, "OK");
			
		}catch(Exception e){ 
			e.printStackTrace();
		}
		return regresa;
	}

	public String getDescripcionBienes() {
		return descripcionBienes;
	}

	public void setDescripcionBienes(String descripcionBienes) {
		this.descripcionBienes = descripcionBienes;
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

	public void setAcreedorTO(AcreedorTO acreedorTO) {
		this.acreedorTO = acreedorTO;
	}

	public AcreedorTO getAcreedorTO() {
		return acreedorTO;
	}

	public void setIdTramite(String idTramite) {
		this.idTramite = idTramite;
	}

	public String getIdTramite() {
		return idTramite;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getNomAcreedor() {
		return nomAcreedor;
	}

	public void setNomAcreedor(String nomAcreedor) {
		this.nomAcreedor = nomAcreedor;
	}

	public Integer getIdAcreedorTO() {
		return idAcreedorTO;
	}

	public void setIdAcreedorTO(Integer idAcreedorTO) {
		this.idAcreedorTO = idAcreedorTO;
	}

	public AvisoAction(){
		super();		
	}
	
	public String aviso(){
		return SUCCESS;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getAutoridad() {
		return autoridad;
	}

	public void setAutoridad(String autoridad) {
		this.autoridad = autoridad;
	}

	public void setContenidoHTML(String contenidoHTML) {
		this.contenidoHTML = contenidoHTML;
	}

	public String getContenidoHTML() {
		return contenidoHTML;
	}
	

	
}
