package mx.gob.se.rug.juez.service.impl;

import mx.gob.se.rug.juez.dao.JuezDAO;
import mx.gob.se.rug.to.PlSql;

public class JuezServiceImpl {
	
	public Boolean insertJuez(Integer idTramiteTemp, String autoridad){
		return new JuezDAO().insertJuez(idTramiteTemp, autoridad);
	}
	public PlSql insertAutoridadInstruye(Integer idTramiteTemp, String autoridad){
		return new JuezDAO().insertAutoridadInstruye(idTramiteTemp, autoridad);
	}
	public String showJuez(Integer idTramiteTemp){
		return new JuezDAO().showJuez(idTramiteTemp);
	}

	public String showJuezTram(Integer idTramite){
		return new JuezDAO().showJuezTram(idTramite);
	}
}
