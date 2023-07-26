package mx.gob.se.rug.detallegarantia.service;

import java.util.List;

import mx.gob.se.rug.detallegarantia.dao.DetalleDAO;
import mx.gob.se.rug.detallegarantia.service.impl.DetalleServiceImpl;
import mx.gob.se.rug.detallegarantia.to.DetalleTO;
import mx.gob.se.rug.detallegarantia.to.PartesTO;
import mx.gob.se.rug.inscripcion.to.BienEspecialTO;
import mx.gob.se.rug.inscripcion.to.TipoBienTO;

public interface DetalleService {
	public DetalleTO getDetalle(int idGarantia);
	public List <PartesTO> getAcreedor(int idGarantia);	
	public List <PartesTO> getOtorgante(int idGarantia);	
	public List <PartesTO> getDeudor(int idGarantia);		
	public List <DetalleTO> getTramites(int idGarantia, int idtramite);		
	public List<BienEspecialTO> getListaBienes(Integer idTramite, Integer pQuery);	
	public DetalleTO getDetalle(Integer idGarantia, Integer Idtramite);		
	public List <PartesTO> getAcreedor(int idGarantia, int Idtramite);		
	public List <PartesTO> getOtorgante(int idGarantia, int Idtramite);		
	public List <PartesTO> getDeudor(int idGarantia, int Idtramite);		
	public String showAcreedorR(Integer idGarantia);
	public List<TipoBienTO> getBienes(Integer idgarantia,Integer idtramite);
	public DetalleTO getTramiteCancelacion(Integer idtramiteInc);
	public DetalleTO getTramiteActualizado(Integer idInscripcion);
	public DetalleTO getDatosBasa(Integer idgarantia, Integer idtramite);
}
