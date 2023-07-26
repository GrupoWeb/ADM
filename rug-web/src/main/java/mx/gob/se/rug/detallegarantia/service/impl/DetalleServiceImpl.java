package mx.gob.se.rug.detallegarantia.service.impl;

import java.util.List;

import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.detallegarantia.dao.DetalleDAO;
import mx.gob.se.rug.detallegarantia.to.DatosAnotacion;
import mx.gob.se.rug.detallegarantia.to.DatosGaranTO;
import mx.gob.se.rug.detallegarantia.to.DetalleTO;
import mx.gob.se.rug.detallegarantia.to.PartesTO;
import mx.gob.se.rug.exception.NoDataFoundException;
import mx.gob.se.rug.inscripcion.to.BienEspecialTO;
import mx.gob.se.rug.inscripcion.to.TipoBienTO;

public class DetalleServiceImpl {
	
	public List <DetalleTO> getTramites(int idGarantia,int idtramite){
		return new DetalleDAO().getTramites(idGarantia, idtramite);		
	}
	public Integer getIdAcreedorByIdTramite(Integer idTramite){
		return new DetalleDAO().getIdAcreedorByIdTramite(idTramite);		
	}
	
	//CONSULTA MAESTRO-DETALLE
	public DetalleTO getDetalle(Integer idGarantia, Integer Idtramite){
		return new DetalleDAO().getDetalle(idGarantia, Idtramite);		
	}
	
	public List <PartesTO> getAcreedor(int idGarantia, int Idtramite){
		return new DetalleDAO().getAcreedor(idGarantia, Idtramite);		
	}
	
	public List<BienEspecialTO> getListaBienes(Integer idTramite, Integer pQuery) {
		return new DetalleDAO().getListaBienes(idTramite, pQuery);
	}

	public List <PartesTO> getOtorgante(int idGarantia, int Idtramite){
		return new DetalleDAO().getOtorgante(idGarantia, Idtramite);		
	}
	
	public List <PartesTO> getDeudor(int idGarantia, int Idtramite){
		return new DetalleDAO().getDeudor(idGarantia, Idtramite);		
	}
	
	public String showAcreedorR(Integer idGarantia) {
		return new DetalleDAO().showAcreedorR(idGarantia);
	}
	
	public String showAcreedorRepresentado(Integer idTramite) {
		return new DetalleDAO().showAcreedorRepresentado(idTramite);
	}
	
	public String showAcreedorRepresentadoNew(Integer idTramite) {
		return new DetalleDAO().showAcreedorRepresentadoNew(idTramite);
	}
	
	public List<TipoBienTO> getBienes(Integer idgarantia,Integer idtramite) {
		return new DetalleDAO().getBienes(idgarantia, idtramite);
	}
	
	public Boolean noHayCancel (Integer idGarantia){
		return new DetalleDAO().noHayCancel(idGarantia);
	}
//----------------------Validacion de la Vigencia--------------------------------------------------------------------------------------------------------	
	public Boolean vigenciaValida (Integer idGarantia){
		return new DetalleDAO().vigenciaValida(idGarantia);
	}
//-------------------------------------------------------------------------------------------------------------------------------------------------------	
	public DetalleTO getTramiteCancelacion(Integer idtramiteInc) {
		return new DetalleDAO().getTramiteCancelacion(idtramiteInc);
	}
	
	public DetalleTO getTramiteActualizado(Integer idInscripcion) {
		return new DetalleDAO().getTramiteActualizado(idInscripcion);
	}
	public DetalleTO getDatosBasa(Integer idgarantia, Integer idtramite) {
		return new DetalleDAO().getDatosBasa(idgarantia, idtramite);
	}
	
	public AcreedorTO showAcreedorDetalle(Integer idGarantia) {
		return new DetalleDAO().showAcreedorDetalle(idGarantia);
	}
	
	public DatosGaranTO datosGaran (Integer idGarantia, Integer idTramite){
		return new DetalleDAO().datosGaran(idGarantia, idTramite);
	}

	public Integer getIdUltimoTramite(Integer idGarantia){
		return new DetalleDAO().getIdUltimoTramite(idGarantia);
	}
	public DatosAnotacion getDatosAnotacion(Integer idTramite) throws NoDataFoundException{
		return new DetalleDAO().getDatosAnotacion(idTramite);
	}
}
