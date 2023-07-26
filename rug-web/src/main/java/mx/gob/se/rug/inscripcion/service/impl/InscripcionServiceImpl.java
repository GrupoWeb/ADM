package mx.gob.se.rug.inscripcion.service.impl;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.json.JsonObject;

import mx.gob.se.rug.acreedores.dao.AcreedoresDAO;
import mx.gob.se.rug.acreedores.dao.impl.AcreedoresDaoJdbcImpl;
import mx.gob.se.rug.acreedores.service.impl.AcreedoresServiceImpl;
import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.garantia.dao.GarantiasDAO;
import mx.gob.se.rug.garantia.to.BoletaPagoTO;
import mx.gob.se.rug.garantia.to.GarantiaTO;
import mx.gob.se.rug.inscripcion.dao.AltaParteDAO;
import mx.gob.se.rug.inscripcion.dao.FirmaMasivaDAO;
import mx.gob.se.rug.inscripcion.dao.InscripcionDAO;
import mx.gob.se.rug.inscripcion.dao.MonedaDAO;
import mx.gob.se.rug.inscripcion.dao.TipoBienDAO;
import mx.gob.se.rug.inscripcion.dao.TipoGarantiaDAO;
import mx.gob.se.rug.inscripcion.service.InscripcionService;
import mx.gob.se.rug.inscripcion.to.AltaParteTO;
import mx.gob.se.rug.inscripcion.to.BienEspecialTO;
import mx.gob.se.rug.inscripcion.to.DeudorTO;
import mx.gob.se.rug.inscripcion.to.FirmaMasivaTO;
import mx.gob.se.rug.inscripcion.to.InscripcionTO;
import mx.gob.se.rug.inscripcion.to.MonedaTO;
import mx.gob.se.rug.inscripcion.to.NacionalidadTO;
import mx.gob.se.rug.inscripcion.to.OtorganteTO;
import mx.gob.se.rug.inscripcion.to.TipoBienTO;
import mx.gob.se.rug.inscripcion.to.TipoGarantiaTO;
import mx.gob.se.rug.masiva.dao.ArchivoDAO;
import mx.gob.se.rug.masiva.to.ArchivoTO;
import mx.gob.se.rug.to.AccionTO;
import mx.gob.se.rug.to.PlSql;
import mx.gob.se.rug.to.TipoPersona;
import mx.gob.se.rug.util.MyLogger;


public class InscripcionServiceImpl implements InscripcionService{
	
	private GarantiasDAO garantiasDAO = new GarantiasDAO();	
	private AltaParteDAO altaParteDAO = new AltaParteDAO();
	private AcreedoresDAO acreedoresDAO = new AcreedoresDaoJdbcImpl();
	private InscripcionDAO  inscripcionDAO = new InscripcionDAO();
	private TipoBienDAO tipoBienDAO = new TipoBienDAO();
	private ArchivoDAO archivoDAO = new ArchivoDAO();
	private MonedaDAO monedaDAO = new MonedaDAO();
	
	public void setMonedaDAO(MonedaDAO monedaDAO) {
		this.monedaDAO = monedaDAO;
	}

	public void setArchivoDAO(ArchivoDAO archivoDAO) {
		this.archivoDAO = archivoDAO;
	}

	public void setTipoBienDAO(TipoBienDAO tipoBienDAO) {
		this.tipoBienDAO = tipoBienDAO;
	}

	public void setInscripcionDAO(InscripcionDAO inscripcionDAO) {
		this.inscripcionDAO = inscripcionDAO;
	}

	public void setAcreedoresDAO(AcreedoresDAO acreedoresDAO) {
		this.acreedoresDAO = acreedoresDAO;
	}

	public void setAltaParteDAO(AltaParteDAO altaParteDAO) {
		this.altaParteDAO = altaParteDAO;
	}

	public void setGarantiasDAO(GarantiasDAO garantiasDAO) {
		this.garantiasDAO = garantiasDAO;
	}
	
	
	
	public List<MonedaTO> getMonedas(){
		return monedaDAO.getMonedas();
	}
	public boolean existeSha1(String sha1){
		return archivoDAO.existeSha1(sha1);
	}
	public Integer iniciaInscripcion(InscripcionTO inscripcionTO,AcreedorTO acreedorTO){
		return inscripcionDAO.insertInscripcion(inscripcionTO, acreedorTO).getResIntPl();
	}
	public List <AcreedorTO> getAcreedoresByID(Integer idPersona){
		if (acreedoresDAO==null){
			MyLogger.Logger.log(Level.INFO, "viene nulo acreedores DAO");
			acreedoresDAO = new AcreedoresDaoJdbcImpl();
		}
		return acreedoresDAO.getAcreedoresByPersona(idPersona);
	}
	public boolean actualizaVigencia(InscripcionTO inscripcionTO, Integer idTramite, Integer idEstatus, 
			Integer idPaso, String fechaCelebracion, String banderaFecha) {
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		
		boolean regresa = false;
		try{
			connection.setAutoCommit(false);
			garantiasDAO.actualizaMeses(connection, inscripcionTO);
			garantiasDAO.altaBitacoraTramiteTX(connection, idTramite, idEstatus, idPaso, fechaCelebracion, banderaFecha);
			connection.commit();
			regresa = true;		
		}catch(Exception  e){
			e.printStackTrace();
		}finally{
			bd.close(connection, null, null);
		}
		return regresa;
	}
	
	public boolean findBoleta(BoletaPagoTO boletaTO) {
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		
		boolean regresa = false;
		try{			
			return garantiasDAO.findBoleta(connection, boletaTO);									
		}catch(Exception  e){
			e.printStackTrace();
		}finally{
			bd.close(connection, null, null);
		}
		return regresa;
	} 
	
	public boolean registrarBoleta(BoletaPagoTO boletaTO) {
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		
		boolean regresa = false;
		try{
			connection.setAutoCommit(false);
			garantiasDAO.insertarBoleta(connection, boletaTO);			
			connection.commit();
			regresa = true;		
		}catch(Exception  e){
			e.printStackTrace();
		}finally{
			bd.close(connection, null, null);
		}
		return regresa;
	}
		
	public List<BoletaPagoTO> getBoletasByGarantia(Integer idGarantia) {
		return garantiasDAO.getBoletasByGarantia(idGarantia);
	}
	public List<BoletaPagoTO> getBoletasByUsuario(Integer idUsuario, Integer estado, Integer inicio, Integer fin) {
		return garantiasDAO.getBoletasByUsuario(idUsuario,estado,"", inicio,fin);
	}
	
	public Double getValorSaldoUsuario(String idUsuario) {
		return garantiasDAO.getValorSaldoByUsuario(idUsuario);
	}
	
	public boolean getSaldoByUsuario(String idUsuario, Integer idTipoTramite, Integer idTramite, Integer idGarantia) {
		// si es subcuenta, utilizar idUsuario de cuenta maestra
		long idUsuarioMaestro = acreedoresDAO.getCuentaMaestra(Integer.valueOf(idUsuario));
		return garantiasDAO.getSaldoByUsuario(String.valueOf(idUsuarioMaestro), idTipoTramite, idTramite, idGarantia);
	}
	
	public boolean getSaldoMasivoByUsuario(String idUsuario, Integer idTipoTramite, Integer idTramite, Integer cantidad) {
		// si es subcuenta, utilizar idUsuario de cuenta maestra
		long idUsuarioMaestro = acreedoresDAO.getCuentaMaestra(Integer.valueOf(idUsuario));
		return garantiasDAO.getSaldoMasivoByUsuario(String.valueOf(idUsuarioMaestro), idTipoTramite, idTramite, cantidad);
	}
	public AcreedorTO getByID(Integer idAcreedor){
		if (acreedoresDAO==null){
			MyLogger.Logger.log(Level.WARNING, "viene nulo acreedores DAO");
			acreedoresDAO = new AcreedoresDaoJdbcImpl();
		}
		return acreedoresDAO.getARByID(idAcreedor);
	}
	public List<AccionTO> getTramitesEfectuados(Integer idPersona, String filtro, Integer inicio, Integer fin) {
		return garantiasDAO.getTramitesEfectuados(idPersona,filtro,inicio, fin);
	}
	public List<AccionTO> getTramitesEfectuadosOptimizado(Integer idPersona, JsonObject filtro, Integer inicio, Integer fin) {
		return garantiasDAO.getTramitesEfectuadosOptimizado(idPersona.longValue(),filtro,inicio, fin);
	}
	public AcreedorTO getByIDTramite(Integer idTramite){
		if (acreedoresDAO==null){
			MyLogger.Logger.log(Level.WARNING, "viene nulo acreedores DAO");
			acreedoresDAO = new AcreedoresDaoJdbcImpl();
		}
		return acreedoresDAO.getByIDTramite(idTramite);
	}
	
	public Integer insertaParte(AltaParteTO altaParteTO){
		return altaParteDAO.insert(altaParteTO).getResIntPl();
	}
	
	public List <OtorganteTO> getOtorganteByID(Integer idInscripcion){
		return altaParteDAO.getOtorganteByInscripcion(idInscripcion);		
	}
	
	public boolean insertBitacoraTramite(Integer idTramite, Integer idEstatus, 
			Integer idPaso, String fechaCelebracion, String banderaFecha){
		return garantiasDAO.altaBitacoraTramite(idTramite, idEstatus, idPaso, fechaCelebracion, banderaFecha);
	}
	
	public String registrarBien(BienEspecialTO bienEspecialTO) {
		return inscripcionDAO.registrarBien(bienEspecialTO);
	}
	
	public String modificarBien(BienEspecialTO bienEspecialTO) {
		return inscripcionDAO.modificarBien(bienEspecialTO);
	}
	
	public String eliminarBien(Integer idTramiteGar) {
		return inscripcionDAO.eliminarBien(idTramiteGar);
	}
	
	public List<BienEspecialTO> getListaBienes(Integer idTramite, Integer pQuery) {
		return inscripcionDAO.getListaBienes(idTramite, pQuery);
	}
	
	public GarantiaTO getGarantiaByID(Integer idGarantia){
		return inscripcionDAO.getByID(idGarantia);
	}
	
	public Integer getIDGarantiaByIDTramite(Integer idTramite){
		return garantiasDAO.getIDGarantiaByIdTramite(idTramite);
	}
	
	public List <TipoBienTO> getTipoBienesByIdGarantiaPendiente(Integer idGarantiaPendiente){
		ConexionBD bd = new ConexionBD();
		List <TipoBienTO> lista = new ArrayList<TipoBienTO>();
		Connection connection = bd.getConnection(); 
		try{
			lista = tipoBienDAO.getTipoBienesByIdGarantiaPendiente(connection, idGarantiaPendiente);
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			bd.close(connection, null, null);
		}
		
		
		
		return lista;
	}
	
	public List<String> getTextosFormularioByIdGarantia(Integer idTipoGarantia) {
		return inscripcionDAO.getTextosFormularioByIdGarantia(idTipoGarantia);
	}
	
	public Integer insertaGarantia(InscripcionTO inscripcionTO, GarantiaTO garantiaTO){
		return garantiasDAO.insertGarantia(inscripcionTO, garantiaTO).getResIntPl();
	}
	public Integer updateGarantia(InscripcionTO inscripcionTO, GarantiaTO garantiaTO, Integer idGarantiaPendiente){
		return garantiasDAO.actualizaGarantia(inscripcionTO, garantiaTO, idGarantiaPendiente);
	}
	public List <DeudorTO> getDeudoresByTramite(Integer idTramite){
		return altaParteDAO.getListaDeudores(idTramite);
	}
	
	public List <DeudorTO> getOtorgantesByTramite(Integer idTramite){
		return altaParteDAO.getListaOtorgantes(idTramite);
	}
	
	public List <AcreedorTO> getAcreedoresByTramite(Integer idTramite){
		return altaParteDAO.getListaAcreedores(idTramite);
	}
	
	public List<TipoBienTO> getTiposBien(){
		return tipoBienDAO.getTiposBien();
	}
	public List<TipoGarantiaTO> getTiposGarantia(){
		return new TipoGarantiaDAO().getTiposGarantia();
	}
	
	public String descBienByID(Integer idBien){
		return tipoBienDAO.getDescByID(idBien);
	}
	
	public String descTipoGarantiaByID(Integer idGarantia){
		return new TipoGarantiaDAO().getDescByID(idGarantia);
	}
	
	public boolean eliminaParte(Integer idTramite, Integer idPersona,Integer idParte, String bandera){
		return altaParteDAO.bajaParte(idTramite,idPersona, idParte, bandera);
	}

	@Override
	public AcreedorTO getAcreedorByID(Integer idAcreedor) {
		// TODO Auto-generated method stub
		return new InscripcionServiceImpl().getByID(idAcreedor);
	}

	@Override
	public GarantiaTO getInscripcionByID(Integer idGarantia) {
		// TODO Auto-generated method stub
		return inscripcionDAO.getByID(idGarantia);
	}

	@Override
	public List<NacionalidadTO> getNacionalidades() {
		// TODO Auto-generated method stub
		return new AcreedoresServiceImpl().getNacionalidades();
	}

	@Override
	public List<TipoPersona> getTiposPersona() {
		// TODO Auto-generated method stub
		return new AcreedoresServiceImpl().getTiposPersona();
	}
	@Override
	public PlSql insertArchivo(ArchivoTO archivoTO) {
		return archivoDAO.insertArchivo(archivoTO);
	}
	@Override
	public Integer creaFirmaMasiva(FirmaMasivaTO firmaMasivaTO) {
		return new FirmaMasivaDAO().crearFirmaMasiva(firmaMasivaTO);
	}

	@Override
	public boolean bajaTramiteIncompleto(Integer idTramite) {
		return inscripcionDAO.bajaTramiteIncompleto(idTramite).getIntPl().intValue() == 0 ? true: false;
	}
	
	
	public boolean tienePartes(Integer idTramiteTemp) {
		return garantiasDAO.tienePartes(idTramiteTemp);
	}
}	
