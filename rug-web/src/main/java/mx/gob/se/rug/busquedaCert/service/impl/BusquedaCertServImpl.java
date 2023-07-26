package mx.gob.se.rug.busquedaCert.service.impl;

import mx.gob.se.rug.BusquedaCertificacion.to.BusquedaCertTO;
import mx.gob.se.rug.busquedaCert.dao.BusquedaCertDAO;
import mx.gob.se.rug.exception.InfrastructureException;

public class BusquedaCertServImpl {

	public BusquedaCertTO busquedaNum(String numOperacion) throws InfrastructureException {
		return (new BusquedaCertDAO().busquedaNum(numOperacion));
	}
	
	public String geNomOtorById(Integer idTramite) {
		return (new BusquedaCertDAO().getNomOtorById(idTramite));
	}
	
	public String getFolioById(Integer idTramite) {
		return (new BusquedaCertDAO().getFolioById(idTramite));
	}
}
