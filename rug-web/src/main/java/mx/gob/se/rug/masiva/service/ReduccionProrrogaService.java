package mx.gob.se.rug.masiva.service;

import mx.gob.se.rug.masiva.to.CargaMasiva;
import mx.gob.se.rug.masiva.to.ResCargaMasiva;
import mx.gob.se.rug.masiva.to.Tramite;

public interface ReduccionProrrogaService {
	public ResCargaMasiva cargaMasivaReduccionProrrogaService(Tramite tramite, CargaMasiva cm);
}
