package mx.gob.se.rug.anotacionGarantia.serviceimpl;


import mx.gob.se.rug.anotacionGarantia.dao.AnotacionGarantiaDAO;
import mx.gob.se.rug.detallegarantia.dao.DetalleDAO;
import mx.gob.se.rug.exception.AnotacionGarantiaException;
import mx.gob.se.rug.garantia.dao.GarantiasDAO;

import mx.gob.se.rug.inscripcion.dao.InscripcionDAO;


public class AnotacionGarantiaServImpl {
	
	public Integer anotacionGarantia (Integer idPersona,Integer idTipoTramite, String autoridad,String anotacion, Integer idGarantia,String meses){
		int regresa = 0;
		try{			
			Integer idTramite = new InscripcionDAO().insert(idPersona, idTipoTramite); // alta de un tramite incompleto
			if (idTramite.intValue()!=0){
				if (new GarantiasDAO().altaBitacoraTramite(idTramite, 5, 0,null, "V")){
					
				}else{
					throw new AnotacionGarantiaException("no se pudo actualizar la bitacora de la anotacion con garantia para el tramite:"+idTramite);
				}
				int idError = new AnotacionGarantiaDAO().insertAnotacionGarantia(idPersona, idTramite, autoridad, anotacion, idGarantia,meses);
				if (idError == 0){
					regresa =idTramite;
				}else{
					throw new AnotacionGarantiaException("no se pudo crear la anotacion con garantia para el nuevo tramite :"+idTramite);
				}				
			}else{
				throw new AnotacionGarantiaException("no se pudo crear el tramite para la anotacion con garantia");
			}
			
		}catch(AnotacionGarantiaException e){
			e.printStackTrace();
		}		 
		return regresa;		
	}

	public String showAcreedorR(Integer idGarantia) {
		return new DetalleDAO().showAcreedorR(idGarantia);
	}
}
