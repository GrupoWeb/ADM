package mx.gob.se.rug.masiva.service;

import java.util.List;

import mx.gob.se.rug.masiva.resultado.to.TramiteRes;
import mx.gob.se.rug.masiva.to.ControlError;
import mx.gob.se.rug.masiva.to.Otorgante;
import mx.gob.se.rug.masiva.to.Tramite;

public interface PartesMasivaService {
	public ControlError agregaOtorgantes(List<Otorgante> otorgantes, Tramite tramite, TramiteRes tramiteRes); 
}
