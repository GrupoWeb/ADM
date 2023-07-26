package mx.gob.se.rug.transmision.service;

import java.util.List;

import mx.gob.se.rug.detallegarantia.to.DetalleTO;
import mx.gob.se.rug.detallegarantia.to.PartesTO;
import mx.gob.se.rug.inscripcion.to.NacionalidadTO;
import mx.gob.se.rug.inscripcion.to.TipoGarantiaTO;
import mx.gob.se.rug.modificacion.to.TipoBienesTO;

public interface TransmisionService {   
	public DetalleTO getDetalle(Integer idgarantia, Integer idtramite);	
	public List <PartesTO> getAcreedor(Integer idgarantia, Integer idtramite);
	public List <PartesTO> getOtorgante(Integer idgarantia, Integer idtramite);
	public List <PartesTO> getDeudor(Integer idgarantia, Integer idtramite);
	public List <TipoBienesTO> getTipoBienes();
	public List <TipoGarantiaTO> getTiposGarantia();
	public List <PartesTO> getListaAcreedoresAdic(Integer idInscripcion);
	public List <PartesTO> getAcreedorAdd(Integer idgarantia);
	public List<NacionalidadTO> getNacionalidades();
}
