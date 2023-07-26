package mx.gob.se.rug.masiva.service;

import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.masiva.to.CargaMasiva;
import mx.gob.se.rug.masiva.to.ResCargaMasiva;
import mx.gob.se.rug.to.UsuarioTO;

public interface CancelacionMasivaService {
	public ResCargaMasiva cargaMasivaCancelacion(UsuarioTO usuario, CargaMasiva cm, AcreedorTO acreedor, Integer idArchivo,
			Integer idAcreedorTO);
}
