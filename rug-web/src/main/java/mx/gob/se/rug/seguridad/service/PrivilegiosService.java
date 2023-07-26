package mx.gob.se.rug.seguridad.service;

import java.io.Serializable;

import mx.gob.se.rug.seguridad.to.PrivilegiosTO;
import mx.gob.se.rug.to.UsuarioTO;

public interface PrivilegiosService extends Serializable {
	
	public PrivilegiosTO cargaPrivilegios(PrivilegiosTO privilegios, UsuarioTO usuario);
	public String getPrivilegios(Integer idPersona);

}
