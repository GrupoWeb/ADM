package mx.gob.se.rug.juez.service;

import mx.gob.se.rug.juez.service.impl.JuezServiceImpl;
import mx.gob.se.rug.to.PlSql;

public class JuezService {

	public Boolean insertJuez(Integer idTramiteTemp, String autoridad){
		return new JuezServiceImpl().insertJuez(idTramiteTemp, autoridad);
	}
	public PlSql insertAutoridadInstruye(Integer idTramiteTemp, String autoridad){
		return new JuezServiceImpl().insertAutoridadInstruye(idTramiteTemp, autoridad);
	}
	
	public String showJuez(Integer idTramiteTemp){
		return new JuezServiceImpl().showJuez(idTramiteTemp);
	}
	
	public String showJuezTram(Integer idTramite){
		return new JuezServiceImpl().showJuezTram(idTramite);
	}
}
