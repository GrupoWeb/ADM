package mx.gob.se.rug.transmision.serviceimp;

import java.util.List;

import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.detallegarantia.dao.DetalleDAO;
import mx.gob.se.rug.detallegarantia.to.DetalleTO;
import mx.gob.se.rug.detallegarantia.to.PartesTO;
import mx.gob.se.rug.inscripcion.dao.AltaParteDAO;
import mx.gob.se.rug.inscripcion.dao.NacionalidadDAO;
import mx.gob.se.rug.inscripcion.dao.TipoGarantiaDAO;
import mx.gob.se.rug.inscripcion.to.NacionalidadTO;
import mx.gob.se.rug.inscripcion.to.TipoGarantiaTO;
import mx.gob.se.rug.modificacion.dao.TipobienesDAO;
import mx.gob.se.rug.modificacion.to.TipoBienesTO;
import mx.gob.se.rug.transmision.service.TransmisionService;

public class TransmisionServiceImp implements TransmisionService{
	
	public DetalleTO getDetalle(Integer idgarantia, Integer tramite){
		return new DetalleDAO().getDetalle(idgarantia,tramite);		
	}
	
	public List <PartesTO> getAcreedor(Integer idgarantia, Integer idtramite){
		return new DetalleDAO().getAcreedor(idgarantia,idtramite);		
	}

	public List <PartesTO> getOtorgante(Integer idgarantia, Integer idtramite){
		return new DetalleDAO().getOtorgante(idgarantia,idtramite);		
	}
	
	public List <PartesTO> getDeudor(Integer idgarantia, Integer idtramite){
		return new DetalleDAO().getDeudor(idgarantia, idtramite);		
	}
	
	public List<TipoBienesTO> getTipoBienes() {
		return new TipobienesDAO().getTipoBienes();
	}
	
	public List<TipoGarantiaTO> getTiposGarantia(){
		return new TipoGarantiaDAO().getTiposGarantia();
	}
	
	public List <PartesTO> getAcreedorAdd(Integer idgarantia){
		return new DetalleDAO().getAcreedorAdd(idgarantia);
	}
	
	public List<NacionalidadTO> getNacionalidades(){
		return new NacionalidadDAO().getNacionalidades();
	}
	
	@Override
	public List<PartesTO> getListaAcreedoresAdic(Integer idInscripcion) {
		// TODO Auto-generated method stub
		return null;
	}
}
