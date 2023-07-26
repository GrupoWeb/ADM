package mx.gob.se.rug.embargo.service;

import mx.gob.se.rug.garantia.to.GarantiaTO;

public interface EmbargoService {

	public Integer setEmbargo(Integer idGarantia, Integer idPersona, Integer idTipoTramite, String observaciones);	
	public GarantiaTO getEmbargo(Integer idGarantia);
	public Boolean isEmbargo(Integer idGarantia);
}
