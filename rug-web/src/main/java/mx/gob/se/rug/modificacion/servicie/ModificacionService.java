package mx.gob.se.rug.modificacion.servicie;

import java.util.List;

import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.detallegarantia.to.DetalleTO;
import mx.gob.se.rug.detallegarantia.to.PartesTO;
import mx.gob.se.rug.detallegarantia.to.TramiteHTO;
import mx.gob.se.rug.inscripcion.to.InscripcionTO;
import mx.gob.se.rug.inscripcion.to.TipoBienTO;
import mx.gob.se.rug.modificacion.to.ModificacionTO;
import mx.gob.se.rug.modificacion.to.TipoBienesTO;		

public interface ModificacionService {
	public DetalleTO getDetalle(Integer idgarantia, Integer idtramite);	
	public List <PartesTO> getAcreedor(Integer idgarantia, Integer idtramite);
	public List <PartesTO> getOtorgante(Integer idgarantia, Integer idtramite);
	public List <PartesTO> getDeudor(Integer idgarantia, Integer idtramite);
	public List <TipoBienesTO> getTipoBienes();
	public int insertatramiteinc(Integer idPersona, Integer idTipoTramite);	
	public int actualiza(ModificacionTO modifica, DetalleTO detalleTO);
	public int insertInscripcion (InscripcionTO inscripcionTO, AcreedorTO acreedorTO);
	public boolean altaBitacoraTramite(Integer idTramite, Integer idEstatus, 
			Integer idPaso, String fechaCelebracion, String banderaFecha);
	public List <PartesTO> getAcreedorAdd(int idgarantia);
	public int bajaparte (Integer idPersona, Integer idParte, Integer idTramite);
	public List<TipoBienTO> getBienes(Integer idgarantia, Integer idtramite); 
	public int altaTransmmision (Integer idPersona, Integer idGarantia);
	public int altapartesTramiteInc (Integer idPersona,Integer idTipoPersona, Integer idGarantia);
	public Integer altapartesBienesInc (Integer idUltimoTramite, Integer idNuevoTramite);
	public int getModparte (Integer idGarantia,Integer idTramite );
	public int altaCancelTramite(Integer idTramite,Integer idGarantia, Integer idUsuario);
	public List<TramiteHTO> getHtramite (Integer idTramite, Integer idgarantia );
	
	DetalleTO getDetalleModificacion(Integer idgarantia, Integer idtramite);
	
	public int modificaRectificacion(ModificacionTO modifica);
	public int alta_Parte_Rectifica (Integer idPersona, Integer idTramite);
	
	int cambioAcreedores(Integer idTramite,Integer idGarantia);
}	
