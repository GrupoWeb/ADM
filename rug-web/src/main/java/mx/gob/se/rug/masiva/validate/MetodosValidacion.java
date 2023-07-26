package mx.gob.se.rug.masiva.validate;

import mx.gob.se.rug.masiva.to.CargaMasiva;
import mx.gob.se.rug.masiva.to.string.CargaMasivaPreProcesed;

public interface MetodosValidacion {
	
	public CargaMasivaPreProcesed valida_renovacion_reduccion(mx.gob.se.rug.masiva.to.string.CargaMasiva cargaMasivaString,CargaMasiva cargaMasiva);
	public CargaMasivaPreProcesed valida_cancelacion(mx.gob.se.rug.masiva.to.string.CargaMasiva cargaMasivaString,CargaMasiva cargaMasiva);
	public CargaMasivaPreProcesed valida_ejecucion(mx.gob.se.rug.masiva.to.string.CargaMasiva cargaMasivaString,CargaMasiva cargaMasiva);
	public CargaMasivaPreProcesed valida_modificacion(mx.gob.se.rug.masiva.to.string.CargaMasiva cargaMasivaString,CargaMasiva cargaMasiva);
	public CargaMasivaPreProcesed valida_inscripcion(mx.gob.se.rug.masiva.to.string.CargaMasiva cargaMasivaString,CargaMasiva cargaMasiva);
	public CargaMasivaPreProcesed valida_transmision(mx.gob.se.rug.masiva.to.string.CargaMasiva cargaMasivaString,CargaMasiva cargaMasiva);
	public CargaMasivaPreProcesed valida_rectificacion_por_error(mx.gob.se.rug.masiva.to.string.CargaMasiva cargaMasivaString,CargaMasiva cargaMasiva);
	public CargaMasivaPreProcesed valida_aviso_preventivo(mx.gob.se.rug.masiva.to.string.CargaMasiva cargaMasivaString,CargaMasiva cargaMasiva);
	public CargaMasivaPreProcesed valida_anotacion(mx.gob.se.rug.masiva.to.string.CargaMasiva cargaMasivaString,CargaMasiva cargaMasiva);
	public CargaMasivaPreProcesed valida_anotacion_garantia(mx.gob.se.rug.masiva.to.string.CargaMasiva cargaMasivaString,CargaMasiva cargaMasiva);
	public CargaMasivaPreProcesed valida_acreedores(mx.gob.se.rug.masiva.to.string.CargaMasiva cargaMasivaString,CargaMasiva cargaMasiva);
	public CargaMasivaPreProcesed valida_traslado(mx.gob.se.rug.masiva.to.string.CargaMasiva cargaMasivaString,CargaMasiva cargaMasiva);
}