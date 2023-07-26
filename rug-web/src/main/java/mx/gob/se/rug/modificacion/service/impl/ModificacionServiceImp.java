package mx.gob.se.rug.modificacion.service.impl;

import java.util.List;

import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.detallegarantia.dao.DetalleDAO;
import mx.gob.se.rug.detallegarantia.to.DetalleTO;
import mx.gob.se.rug.detallegarantia.to.PartesTO;
import mx.gob.se.rug.detallegarantia.to.TramiteHTO;
import mx.gob.se.rug.exception.InfrastructureException;
import mx.gob.se.rug.exception.NoDataFoundException;
import mx.gob.se.rug.garantia.dao.GarantiasDAO;
import mx.gob.se.rug.inscripcion.dao.InscripcionDAO;
import mx.gob.se.rug.inscripcion.to.InscripcionTO;
import mx.gob.se.rug.inscripcion.to.TipoBienTO;
import mx.gob.se.rug.modificacion.dao.ModificacionDAO;
import mx.gob.se.rug.modificacion.dao.TipobienesDAO;
import mx.gob.se.rug.modificacion.servicie.ModificacionService;
import mx.gob.se.rug.modificacion.to.AutoridadInstruye;
import mx.gob.se.rug.modificacion.to.ModificacionTO;
import mx.gob.se.rug.modificacion.to.TipoBienesTO;

public class ModificacionServiceImp implements ModificacionService{
	
	public DetalleTO getDetalle(Integer idgarantia, Integer idtramite){
		return new DetalleDAO().getDetalle(idgarantia,idtramite);		
	}
	
	public List <PartesTO> getAcreedor(Integer idgarantia, Integer idtramite){
		return new DetalleDAO().getAcreedor(idgarantia,idtramite);		
	}

	public List <PartesTO> getOtorgante(Integer idgarantia,Integer idtramite){
		return new DetalleDAO().getOtorgante(idgarantia,idtramite);		
	}
	
	public List <PartesTO> getDeudor(Integer idgarantia, Integer idtramite){
		return new DetalleDAO().getDeudor(idgarantia,idtramite);		
	}
	
	public List<TipoBienesTO> getTipoBienes() {
		return new TipobienesDAO().getTipoBienes();
	}
	
	public int insertInscripcion (InscripcionTO inscripcionTO, AcreedorTO acreedorTO){
		return new InscripcionDAO().insertInscripcion(inscripcionTO, acreedorTO).getResIntPl();		
	}	
	
	public boolean altaBitacoraTramite(Integer idTramite, Integer idEstatus, 
			Integer idPaso, String fechaCelebracion, String banderaFecha	){
		return new GarantiasDAO().altaBitacoraTramite(idTramite, idEstatus, idPaso, fechaCelebracion, banderaFecha);		   
	}
	
	public int actualiza(ModificacionTO modifica, DetalleTO detalleTO){
		return new ModificacionDAO().actualiza(modifica, detalleTO);		
	}

	public int insertatramiteinc(Integer idPersona, Integer idTipoTramite) {	
		return	new ModificacionDAO().insertatramiteinc(idPersona, idTipoTramite);		 
	}

	public List <PartesTO> getAcreedorAdd(int idgarantia){
		return new DetalleDAO().getAcreedorAdd(idgarantia);
	}
	
	public int bajaparte (Integer idPersona, Integer idParte, Integer idTramite){
		return new ModificacionDAO().bajaparte(idPersona, idParte, idTramite);
	}
	
	public List<TipoBienTO> getBienes(Integer idgarantia,Integer idtramite) {
		return new DetalleDAO().getBienes(idgarantia,idtramite);
	}
	public int altaTransmmision (Integer idPersona, Integer idGarantia){
		return new ModificacionDAO().altaTransmmision(idPersona, idGarantia);
	}
	public int altapartesTramiteInc (Integer idPersona,Integer idTipoPersona, Integer idGarantia){
		return new ModificacionDAO().altapartesTramiteInc(idPersona, idTipoPersona, idGarantia);
	}	
	public Integer altapartesBienesInc (Integer idUltimoTramite, Integer idNuevoTramite) {
		return new ModificacionDAO().altapartesBienesInc(idUltimoTramite, idNuevoTramite);
	}
	public int getModparte (Integer idGarantia,Integer idTramite ){
		return new ModificacionDAO().getModparte(idGarantia, idTramite);
	}
	public int altaCancelTramite(Integer idTramite,Integer idGarantia, Integer idUsuario){
		return new ModificacionDAO().altaCancelTramite(idTramite, idGarantia, idUsuario);
	}	
	public List<TramiteHTO> getHtramite (Integer idTramite, Integer idgarantia ){
		return new ModificacionDAO().getHtramite(idTramite,idgarantia );		
	}

	// TODO Revisar si este metodo aplicaria tanto para modificacion, rectificacion,
	// 		Transmision.
	@Override
	public DetalleTO getDetalleModificacion(Integer idgarantia,
			Integer idtramite) {
		return new DetalleDAO().getDetalleModificacion(idgarantia,idtramite);
	}
	
	public int modificaRectificacion(ModificacionTO modifica){
		return new ModificacionDAO().modificaRectificacion(modifica);
	}
	
	public int alta_Parte_Rectifica (Integer idPersona, Integer idTramite){
		return new ModificacionDAO().alta_Parte_Rectifica(idPersona, idTramite);
	}

	@Override
	public int cambioAcreedores(Integer idTramite, Integer idGarantia) {
		return new ModificacionDAO().cambioAcreedores(idTramite, idGarantia);
	}
	
	public AutoridadInstruye getAutoridadInstruye(Integer idGarantia) throws InfrastructureException, NoDataFoundException{
		
		return new ModificacionDAO().getAutoridadInstruyeTramite(idGarantia);
	}

	
	public Boolean saveAutoridadInstruyeTramite(Integer idTramite , String autoridadInstruye) throws InfrastructureException, NoDataFoundException{
		return new ModificacionDAO().saveAutoridadInstruyeTramite(idTramite, autoridadInstruye);
	}
	
}
