package mx.gob.se.rug.operaciones_leasing.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import mx.gob.se.rug.acreedores.service.AcreedoresService;
import mx.gob.se.rug.acreedores.service.impl.AcreedoresServiceImpl;
import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.common.util.RequestContext;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.exception.InfrastructureException;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.fwk.listener.RugSessionListener;
import mx.gob.se.rug.garantia.to.ActoContratoTO;
import mx.gob.se.rug.garantia.to.GarantiaTO;
import mx.gob.se.rug.garantia.to.ObligacionTO;
import mx.gob.se.rug.inscripcion.dao.BienPendienteDAO;
import mx.gob.se.rug.inscripcion.service.InscripcionService;
import mx.gob.se.rug.inscripcion.service.impl.InscripcionServiceImpl;
import mx.gob.se.rug.inscripcion.to.BienPendienteTO;
import mx.gob.se.rug.inscripcion.to.DeudorTO;
import mx.gob.se.rug.inscripcion.to.InscripcionTO;
import mx.gob.se.rug.inscripcion.to.OtorganteTO;
import mx.gob.se.rug.inscripcion.to.TipoBienInscripcionTXT;
import mx.gob.se.rug.juez.dao.JuezDAO;
import mx.gob.se.rug.juez.service.JuezService;
import mx.gob.se.rug.operaciones.service.OperacionesService;
import mx.gob.se.rug.operaciones.to.CargaMasivaResumenTO;
import mx.gob.se.rug.operaciones.to.OperacionesTO;
import mx.gob.se.rug.seguridad.dao.PrivilegiosDAO;
import mx.gob.se.rug.seguridad.to.PrivilegioTO;
import mx.gob.se.rug.seguridad.to.PrivilegiosTO;
import mx.gob.se.rug.to.TipoPersona;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;

import org.apache.struts2.interceptor.ServletRequestAware;

public class OperacionesLeasingAction extends RugBaseAction implements ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List <OperacionesTO> listaPendientes;
	private List <OperacionesTO> listaPendientesPago;
	private List <OperacionesTO> listaPendientesFirma;
	private List <CargaMasivaResumenTO> listaPendientesFirmaMasiva;
	private List <OperacionesTO> listaTerminadas;
	private List <AcreedorTO> listaAcreedores;
	private Integer idTramitePendiente;
	private String idAcreedorTO;
	private boolean esdamin;
	private InscripcionTO inscripcionTO;
	private Integer idInscripcion;
	private AcreedorTO acreedorTORep;
	private String autoridad;
	private ObligacionTO obligacionTO;
	private ActoContratoTO actoContratoTO;
	private GarantiaTO garantiaTO;
	private String idTipoGarantia;
	private boolean insPublico;
	private boolean hayOtorgante;
	private boolean hayAcreedores;
	private boolean hayDeudores;
	private List <TipoBienInscripcionTXT> listaTipoBienes;
	private List <BienPendienteTO> listaBienesPendientes;
	private List <OtorganteTO> otorganteTO;
	private List<TipoPersona> listaTipoPersona;
	private List <DeudorTO> listaDeudores;
	private String tipoGarantiaStr;
	private String tipoBienesStr;
	private Integer idPersona;
	
	private AcreedoresService acreedoresService = new AcreedoresServiceImpl();
	public String cargaPagina(){
		String regresa = Constants.SUCCESS;
		try{
			UsuarioTO usuario = (UsuarioTO) sessionMap.get(Constants.USUARIO);
			OperacionesService opService= new OperacionesService();
			InscripcionService inscripcionService = new InscripcionServiceImpl();
			List <AcreedorTO> lista = inscripcionService.getAcreedoresByID(usuario.getPersona().getIdPersona());
			Integer idPersona = usuario.getPersona().getIdPersona();
			List <AcreedorTO> listaAcreedores = new ArrayList<AcreedorTO>();
			Iterator<AcreedorTO> it = lista.iterator();
			PrivilegiosDAO privilegiosDAO = new PrivilegiosDAO();
			AcreedorTO acre;
			setEsdamin(false);
			
			while (it.hasNext()){
				acre = it.next();
				if (privilegiosDAO.esAdministrador(acre.getIdPersona(),idPersona)){
					setEsdamin(true);
					listaAcreedores.add(acre);
				}
			}
			setIdPersona(idPersona);
		}catch (Exception e) {
			setMessageError(e.getMessage()+"  -- "+e.getCause());
			MyLogger.Logger.log(Level.SEVERE,"Error en mostrar mis operaciones",e);
			regresa=Constants.FAILED;
			e.printStackTrace();
		}
		MyLogger.Logger.log(Level.INFO, "Regresa::" +regresa);
		return regresa;
	}
	
	public String modalDetalle(){
		String regresa = "failed";
		try{
			InscripcionService inscripcionService = new InscripcionServiceImpl();
			UsuarioTO usuario = (UsuarioTO) sessionMap.get("usuario");
			Integer idTramite ;			
			if (getIdInscripcion()!= null){
				if (inscripcionTO == null){
					inscripcionTO = new InscripcionTO();
				}
				sessionMap.put(Constants.ID_TRAMITE_NUEVO, new Integer(getIdInscripcion()) );
				idTramite = getIdInscripcion();
				inscripcionTO.setIdInscripcion(idTramite);
				inscripcionTO.setIdPersona(usuario.getPersona().getIdPersona());
				setAcreedorTORep(inscripcionService.getByIDTramite(getIdInscripcion()));
				sessionMap.put(Constants.ID_ACREEDOR_REPRESENTADO, new Integer(acreedorTORep.getIdPersona()));
				
			}else{
				setAcreedorTORep(acreedoresService.getARByID((Integer) sessionMap.get(Constants.ID_ACREEDOR_REPRESENTADO)));
				idTramite = (Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO);	
				if (inscripcionTO == null){
					inscripcionTO = new InscripcionTO();
				}
			}
			if (idTramite!=null){
				Integer idGarantia = inscripcionService.getIDGarantiaByIDTramite(idTramite);
				PrivilegiosDAO privilegiosDAO = new PrivilegiosDAO();
				PrivilegiosTO privilegiosTO = new PrivilegiosTO();
				privilegiosTO.setIdRecurso(new Integer(6));
				
				privilegiosTO=privilegiosDAO.getPrivilegios(privilegiosTO,(UsuarioTO)sessionMap.get(Constants.USUARIO));
				Map<Integer,PrivilegioTO> priv= privilegiosTO.getMapPrivilegio();
				if ((priv.get(new Integer(20))!=null )){
					JuezDAO dao = new JuezDAO();
					String cadena =dao.showJuez(idTramite);
					setAutoridad(cadena);
				}
				if (idGarantia > 0){
					
					setGarantiaTO(inscripcionService.getInscripcionByID(idGarantia));
					//Correccion vigencia
					inscripcionTO.setMeses(garantiaTO.getVigencia().toString());
					setActoContratoTO(getGarantiaTO().getActoContratoTO());
					setObligacionTO(getGarantiaTO().getObligacionTO());
					setIdTipoGarantia(getGarantiaTO().getIdTipoGarantia().toString());
					setInsPublico(false);
					if (getActoContratoTO().getInstrumentoPub()!=null && (!getActoContratoTO().getInstrumentoPub().equals(""))){
						  setInsPublico(true);
					}
					JuezService juezService = new JuezService();
					setAutoridad(juezService.showJuez(idTramite)); // aki se llena la anotacion del juez en el paso 3					
					setOtorganteTO(inscripcionService.getOtorganteByID(Integer.valueOf(idTramite)));				
					setListaDeudores(inscripcionService.getDeudoresByTramite(idTramite));
					setListaAcreedores(inscripcionService.getAcreedoresByTramite(idTramite));
					Integer idTipoGarantia = getGarantiaTO().getIdTipoGarantia();
					MyLogger.Logger.log(Level.INFO, "Paso antes de rfc: 530");
					if (acreedorTORep.getRfc()==null){
						acreedorTORep.setRfc(acreedorTORep.getCurp());
					}else if(acreedorTORep.getCurp()==null){
						acreedorTORep.setCurp(acreedorTORep.getRfc());
					}
					MyLogger.Logger.log(Level.INFO, "536");
					setListaBienesPendientes(new BienPendienteDAO().bienesPendientesById(idGarantia));
					if (idTipoGarantia!=null){						
						setTipoGarantiaStr(inscripcionService.descTipoGarantiaByID(idTipoGarantia));
						if (getOtorganteTO()==null){
							MyLogger.Logger.log(Level.WARNING, "No hay otorgantes");
						}else{
							MyLogger.Logger.log(Level.WARNING, "Existen un otorgante");
							setHayOtorgante(true);
						}
						if(getListaDeudores().size()>0){
							MyLogger.Logger.log(Level.WARNING, "Hay deudores");
							setHayDeudores(true);
						}else{
							MyLogger.Logger.log(Level.WARNING, "no hay deudores");
						}
						
						if(getListaAcreedores().size()>0){
							setHayAcreedores(true);
							MyLogger.Logger.log(Level.INFO, "Hay acreedores");
						}else{
							MyLogger.Logger.log(Level.WARNING, "no hay acreedores");
						}
						setListaTipoBienes(new ArrayList<TipoBienInscripcionTXT>());
						regresa = "success";
					}else{
						setTipoGarantiaStr("no tenia idTipoGarantia");
						
					}
					
				}			
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		MyLogger.Logger.log(Level.INFO, "regresa del paso 3 Linea 578" + regresa);
		return regresa;
	}
	
	public String eliminarTramitePendiente(){
		String regresa = "failed";
		try{
			OperacionesService operacionesService = new OperacionesService();
			if (getIdTramitePendiente()!=null){
				if (operacionesService.eliminaTramiteIncompleto(getIdTramitePendiente())){
					regresa = "success";
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return regresa;
	}
	
	public String iniciaSubManejo(){
		String regresa = "failed";
		try{
			PrivilegiosDAO privilegiosDAO = new PrivilegiosDAO();
			UsuarioTO usuario = (UsuarioTO) sessionMap.get("usuario");
			Integer idPersona = usuario.getPersona().getIdPersona();
			InscripcionService inscripcionService = new InscripcionServiceImpl();
				List <AcreedorTO> lista = inscripcionService.getAcreedoresByID(usuario.getPersona().getIdPersona());
				List <AcreedorTO> listaAcreedores = new ArrayList<AcreedorTO>();
				Iterator<AcreedorTO> it = lista.iterator();
				AcreedorTO acre;
				while (it.hasNext()){
					acre = it.next();
					if (privilegiosDAO.esAdministrador(acre.getIdPersona(),idPersona)){
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
	
	public String moSubUsuarios(){
		setIdAcreedorTO(getIdAcreedorTO());
		UsuarioTO usuario = (UsuarioTO) sessionMap.get("usuario");
		Integer idPersona = usuario.getPersona().getIdPersona();
		setIdPersona(idPersona);
		return "success";
	}
	
	public List<OperacionesTO> getListaPendientesFirma() {
		return listaPendientesFirma;
	}

	public void setListaPendientesFirma(List<OperacionesTO> listaPendientesFirma) {
		this.listaPendientesFirma = listaPendientesFirma;
	}

	public List<OperacionesTO> getListaTerminadas() {
		return listaTerminadas;
	}
	
	public InscripcionTO getInscripcionTO() {
		return inscripcionTO;
	}

	public void setInscripcionTO(InscripcionTO inscripcionTO) {
		this.inscripcionTO = inscripcionTO;
	}

	public void setListaTerminadas(List<OperacionesTO> listaTerminadas) {
		this.listaTerminadas = listaTerminadas;
	}
	
	public List<OperacionesTO> getListaPendientes() {
		return listaPendientes;
	}

	public Integer getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Integer idPersona) {
		this.idPersona = idPersona;
	}

	public Integer getIdInscripcion() {
		return idInscripcion;
	}

	public void setIdInscripcion(Integer idInscripcion) {
		this.idInscripcion = idInscripcion;
	}

	public void setListaPendientes(List<OperacionesTO> listaPendientes) {
		this.listaPendientes = listaPendientes;
	}

	public List<OperacionesTO> getListaPendientesPago() {
		return listaPendientesPago;
	}

	public void setListaPendientesPago(List<OperacionesTO> listaPendientesPago) {
		this.listaPendientesPago = listaPendientesPago;
	}


@Override
public void setServletRequest(HttpServletRequest request) {
	HttpSession session=request.getSession();
	session.setAttribute(RugSessionListener.KEY_REQUESTURI, RequestContext.getAttribute(RequestContext.KEY_REQUESTURI));
	session.setAttribute(RugSessionListener.KEY_REMOTEADDR, RequestContext.getAttribute(RequestContext.KEY_REMOTEADDR));
}

public void setIdTramitePendiente(Integer idTramitePendiente) {
	this.idTramitePendiente = idTramitePendiente;
}

public Integer getIdTramitePendiente() {
	return idTramitePendiente;
}

public void setListaAcreedores(List <AcreedorTO> listaAcreedores) {
	this.listaAcreedores = listaAcreedores;
}

public List <AcreedorTO> getListaAcreedores() {
	return listaAcreedores;
}

public void setIdAcreedorTO(String idAcreedorTO) {
	this.idAcreedorTO = idAcreedorTO;
}

public String getIdAcreedorTO() {
	return idAcreedorTO;
}
public boolean isEsdamin() {
	return esdamin;
}

public void setEsdamin(boolean esdamin) {
	this.esdamin = esdamin;
}

public AcreedorTO getAcreedorTORep() {
	return acreedorTORep;
}

public void setAcreedorTORep(AcreedorTO acreedorTORep) {
	this.acreedorTORep = acreedorTORep;
}

public boolean isHayOtorgante() {
	return hayOtorgante;
}

public void setHayOtorgante(boolean hayOtorgante) {
	this.hayOtorgante = hayOtorgante;
}

public boolean isHayAcreedores() {
	return hayAcreedores;
}

public void setHayAcreedores(boolean hayAcreedores) {
	this.hayAcreedores = hayAcreedores;
}

public boolean isHayDeudores() {
	return hayDeudores;
}

public void setHayDeudores(boolean hayDeudores) {
	this.hayDeudores = hayDeudores;
}

public void setAutoridad(String autoridad) {
	this.autoridad = autoridad;
}

public String getAutoridad() {
	return autoridad;
}

public ObligacionTO getObligacionTO() {
	return obligacionTO;
}

public void setObligacionTO(ObligacionTO obligacionTO) {
	this.obligacionTO = obligacionTO;
}

public ActoContratoTO getActoContratoTO() {
	return actoContratoTO;
}

public void setActoContratoTO(ActoContratoTO actoContratoTO) {
	this.actoContratoTO = actoContratoTO;
}

public GarantiaTO getGarantiaTO() {
	return garantiaTO;
}

public void setGarantiaTO(GarantiaTO garantiaTO) {
	this.garantiaTO = garantiaTO;
}

public void setIdTipoGarantia(String idTipoGarantia) {
	this.idTipoGarantia = idTipoGarantia;
}

public List<TipoBienInscripcionTXT> getListaTipoBienes() {
	return listaTipoBienes;
}

public void setListaTipoBienes(List<TipoBienInscripcionTXT> listaTipoBienes) {
	this.listaTipoBienes = listaTipoBienes;
}

public List<BienPendienteTO> getListaBienesPendientes() {
	return listaBienesPendientes;
}

public void setListaBienesPendientes(List<BienPendienteTO> listaBienesPendientes) {
	this.listaBienesPendientes = listaBienesPendientes;
}

public List <OtorganteTO> getOtorganteTO() {
	return otorganteTO;
}

public void setOtorganteTO(List <OtorganteTO> otorganteTO) {
	this.otorganteTO = otorganteTO;
}

public String getIdTipoGarantia() {
	return idTipoGarantia;
}

public void setInsPublico(boolean insPublico) {
	this.insPublico = insPublico;
}

public boolean isInsPublico() {
	return insPublico;
}

public void setListaTipoPersona(List<TipoPersona> listaTipoPersona) {
	this.listaTipoPersona = listaTipoPersona;
}

public List<TipoPersona> getListaTipoPersona() {
	return listaTipoPersona;
}

public void setListaDeudores(List <DeudorTO> listaDeudores) {
	this.listaDeudores = listaDeudores;
}

public List <DeudorTO> getListaDeudores() {
	return listaDeudores;
}

public void setTipoGarantiaStr(String tipoGarantiaStr) {
	this.tipoGarantiaStr = tipoGarantiaStr;
}

public String getTipoGarantiaStr() {
	return tipoGarantiaStr;
}

public void setTipoBienesStr(String tipoBienesStr) {
	this.tipoBienesStr = tipoBienesStr;
}

public String getTipoBienesStr() {
	return tipoBienesStr;
}

public List<CargaMasivaResumenTO> getListaPendientesFirmaMasiva() {
	return listaPendientesFirmaMasiva;
}

public void setListaPendientesFirmaMasiva(
		List<CargaMasivaResumenTO> listaPendientesFirmaMasiva) {
	this.listaPendientesFirmaMasiva = listaPendientesFirmaMasiva;
}


}