package mx.gob.se.rug.seguridad.to;

import java.util.List;
import java.util.Map;

public class PrivilegiosTO {
	
	private Integer idRecurso;
	private List<PrivilegioTO> privilegioTOs;
	private Map< Integer, PrivilegioTO> mapPrivilegio;
	public Integer getIdRecurso() {
		return idRecurso;
	}
	public void setIdRecurso(Integer idRecurso) {
		this.idRecurso = idRecurso;
	}
	public List<PrivilegioTO> getPrivilegioTOs() {
		return privilegioTOs;
	}
	public void setPrivilegioTOs(List<PrivilegioTO> privilegioTOs) {
		this.privilegioTOs = privilegioTOs;
	}
	public Map<Integer, PrivilegioTO> getMapPrivilegio() {
		return mapPrivilegio;
	}
	public void setMapPrivilegio(Map<Integer, PrivilegioTO> mapPrivilegio) {
		this.mapPrivilegio = mapPrivilegio;
	}
	
	
	

}
