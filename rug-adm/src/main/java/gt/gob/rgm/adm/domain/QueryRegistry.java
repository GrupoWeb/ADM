package gt.gob.rgm.adm.domain;

import java.io.Serializable;

public class QueryRegistry implements Serializable {
	Long idConsultaReg;
	Long idPersona;
	Integer idTipoTramite;
	Integer totalResultado;
	String nombrePersona;
	String fechaHora;
	
	public QueryRegistry() {
	}

	public Long getIdConsultaReg() {
		return idConsultaReg;
	}

	public void setIdConsultaReg(Long idConsultaReg) {
		this.idConsultaReg = idConsultaReg;
	}

	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	public Integer getIdTipoTramite() {
		return idTipoTramite;
	}

	public void setIdTipoTramite(Integer idTipoTramite) {
		this.idTipoTramite = idTipoTramite;
	}

	public Integer getTotalResultado() {
		return totalResultado;
	}

	public void setTotalResultado(Integer totalResultado) {
		this.totalResultado = totalResultado;
	}

	public String getNombrePersona() {
		return nombrePersona;
	}

	public void setNombrePersona(String nombrePersona) {
		this.nombrePersona = nombrePersona;
	}

	public String getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
	}
}
