package mx.gob.se.rug.masiva.service;

import java.util.Map;

import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.common.dto.Mensaje;
import mx.gob.se.rug.masiva.to.CargaMasiva;
import mx.gob.se.rug.masiva.to.ResCargaMasiva;
import mx.gob.se.rug.masiva.to.string.CargaMasivaPreProcesed;
import mx.gob.se.rug.to.UsuarioTO;

public interface AvisoPreventivoMasivaService  {
	
	public ResCargaMasiva cargaAvisoPreventivo(ResCargaMasiva resCargaMasiva,CargaMasivaPreProcesed cargaMasivaPreProcesed, CargaMasiva cargaMasiva,UsuarioTO usuario, AcreedorTO acreedor,Integer idArchivo,String nombreArchivo);
	public void setMensaje(Mensaje mensaje);
	public void setSessionMap(Map<String, Object> sessionMap);
}
