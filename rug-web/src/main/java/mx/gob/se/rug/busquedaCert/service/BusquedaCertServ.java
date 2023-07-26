package mx.gob.se.rug.busquedaCert.service;

import mx.gob.se.rug.BusquedaCertificacion.to.BusquedaCertTO;
import mx.gob.se.rug.busquedaCert.service.impl.BusquedaCertServImpl;
import mx.gob.se.rug.exception.InfrastructureException;

public class BusquedaCertServ {
	
	public BusquedaCertTO busquedaNum(String numOperacion) throws InfrastructureException {
		return (new BusquedaCertServImpl().busquedaNum(numOperacion));
	}
	
	public String getNomOtorById(Integer idTramite) {
		return (new BusquedaCertServImpl().geNomOtorById(idTramite));
	}
	
	public String getFolioById(Integer idTramite) {
		return (new BusquedaCertServImpl().getFolioById(idTramite));
	}
}
