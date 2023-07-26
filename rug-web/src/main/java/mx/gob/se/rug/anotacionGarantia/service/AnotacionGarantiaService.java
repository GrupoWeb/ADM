package mx.gob.se.rug.anotacionGarantia.service;

import mx.gob.se.rug.anotacionGarantia.serviceimpl.AnotacionGarantiaServImpl;

public class AnotacionGarantiaService {

	public Integer anotacionGarantia(Integer idPersona,Integer idTipoTramite, String autoridad,String anotacion, Integer idGarantia,String meses){
		return new AnotacionGarantiaServImpl().anotacionGarantia(idPersona, idTipoTramite, autoridad, anotacion, idGarantia,meses);
	}
	
	public String showAcreedorR(Integer idGarantia) {
		return new AnotacionGarantiaServImpl().showAcreedorR(idGarantia);
	}
}
