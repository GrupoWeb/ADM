package gt.gob.rgm.adm.domain;

import java.io.Serializable;

public class SurveyStatsMulti implements Serializable{

	private String respuesta;
	private Long total;
	public String getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
		
}
