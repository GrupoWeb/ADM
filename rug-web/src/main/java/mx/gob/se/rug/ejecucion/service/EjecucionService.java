package mx.gob.se.rug.ejecucion.service;

import mx.gob.se.rug.garantia.to.GarantiaTO;

public interface EjecucionService {

	public Integer setEjecucion(Integer idGarantia, Integer idPersona, Integer idTipoTramite, String observaciones);
	public GarantiaTO getEjecucion(Integer idGarantia);
}
