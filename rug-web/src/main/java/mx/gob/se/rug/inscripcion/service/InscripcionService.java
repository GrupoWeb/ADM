package mx.gob.se.rug.inscripcion.service;

import java.util.List;

import javax.json.JsonObject;

import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.garantia.to.BoletaPagoTO;
import mx.gob.se.rug.garantia.to.GarantiaTO;

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
import mx.gob.se.rug.masiva.to.ArchivoTO;
import mx.gob.se.rug.to.AccionTO;
import mx.gob.se.rug.to.PlSql;
import mx.gob.se.rug.to.TipoPersona;

public interface InscripcionService {	
	
	public PlSql insertArchivo(ArchivoTO archivoTO);
	public boolean existeSha1(String sha1);
	public Integer iniciaInscripcion(InscripcionTO inscripcionTO,AcreedorTO acreedorTO);
	public List<MonedaTO> getMonedas();
	public boolean actualizaVigencia(InscripcionTO inscripcionTO, Integer idTramite, Integer idEstatus, 
			Integer idPaso, String fechaCelebracion, String banderaFecha);
	public boolean registrarBoleta(BoletaPagoTO boletaTO);
	public boolean findBoleta(BoletaPagoTO boletaTO);
	public String registrarBien(BienEspecialTO bienEspecialTO);
	public String modificarBien(BienEspecialTO bienEspecialTO);
	public String eliminarBien(Integer idTramiteGar);
	public List<BienEspecialTO> getListaBienes(Integer idTramite, Integer pQuery);
	public List<BoletaPagoTO> getBoletasByGarantia(Integer idGarantia);
	public List<BoletaPagoTO> getBoletasByUsuario(Integer idUsuario, Integer estado, Integer inicio, Integer fin);
	public List<AccionTO> getTramitesEfectuados(Integer idPersona, String filtro, Integer inicio, Integer fin);
	public List<AccionTO> getTramitesEfectuadosOptimizado(Integer idPersona, JsonObject filtro, Integer inicio, Integer fin);
	public boolean getSaldoByUsuario(String idUsuario, Integer idTipoTramite, Integer idTramite, Integer idGarantia);
	public boolean getSaldoMasivoByUsuario(String idUsuario, Integer idTipoTramite, Integer idTramite, Integer cantidad);
	public Double getValorSaldoUsuario(String idUsuario);
	public Integer creaFirmaMasiva(FirmaMasivaTO firmaMasivaTO);
	public List <AcreedorTO> getAcreedoresByID(Integer idPersona);
	public AcreedorTO getAcreedorByID(Integer idAcreedor);
	public Integer insertaParte(AltaParteTO altaParteTO);
	public boolean insertBitacoraTramite(Integer idTramite, Integer idEstatus, 
			Integer idPaso, String fechaCelebracion, String banderaFecha);
	public List<NacionalidadTO> getNacionalidades();
	public List<TipoPersona> getTiposPersona();
	public List <DeudorTO> getOtorgantesByTramite(Integer idTramite);
	public List <OtorganteTO> getOtorganteByID(Integer idInscripcion);
	public List <DeudorTO> getDeudoresByTramite(Integer idTramite);
	public List <AcreedorTO> getAcreedoresByTramite(Integer idTramite);
	public List<TipoBienTO> getTiposBien();
	public Integer getIDGarantiaByIDTramite(Integer idTramite);
	public List <TipoBienTO> getTipoBienesByIdGarantiaPendiente(Integer idGarantiaPendiente);
	public List<TipoGarantiaTO> getTiposGarantia();
	public Integer insertaGarantia(InscripcionTO inscripcionTO, GarantiaTO garantiaTO);
	public Integer updateGarantia(InscripcionTO inscripcionTO, GarantiaTO garantiaTO, Integer idGarantiaPendiente);
	public String descTipoGarantiaByID(Integer idGarantia);
	public List<String> getTextosFormularioByIdGarantia(Integer idTipoGarantia);
	public String descBienByID(Integer idBien);
	
	public AcreedorTO getByIDTramite(Integer idTramite);
	
	public GarantiaTO getInscripcionByID(Integer idGarantia);
	
	public boolean eliminaParte(Integer idTramite, Integer idPersona,Integer idParte, String bandera);
	public boolean bajaTramiteIncompleto(Integer idTramite);
	public boolean tienePartes(Integer idTramiteTemp);

}
