package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the RUG_CONSULTA_REGISTRO database table.
 * 
 */
@Entity
@Table(name="RUG_CONSULTA_REGISTRO")
@NamedQuery(name="RugConsultaRegistro.findAll", query="SELECT r FROM RugConsultaRegistro r")
public class RugConsultaRegistro implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_CONSULTA_REG")
	private long idConsultaReg;

	@Lob
	@Basic(fetch=FetchType.LAZY)
	private String consulta;

	private String estatus;

	@Temporal(TemporalType.TIMESTAMP)
	private Date fechahora;

	@Column(name="ID_PERSONA")
	private BigDecimal idPersona;

	@Column(name="ID_TIPO_TRAMITE")
	private BigDecimal idTipoTramite;

	@Lob
	@Basic(fetch=FetchType.LAZY)
	private String respuesta;

	@Column(name="TOTAL_RESULTADO")
	private BigDecimal totalResultado;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ID_PERSONA", insertable=false, updatable=false)
	private RugSecuUsuario usuario;

	public RugConsultaRegistro() {
	}

	public long getIdConsultaReg() {
		return this.idConsultaReg;
	}

	public void setIdConsultaReg(long idConsultaReg) {
		this.idConsultaReg = idConsultaReg;
	}

	public String getConsulta() {
		return this.consulta;
	}

	public void setConsulta(String consulta) {
		this.consulta = consulta;
	}

	public String getEstatus() {
		return this.estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Date getFechahora() {
		return this.fechahora;
	}

	public void setFechahora(Date fechahora) {
		this.fechahora = fechahora;
	}

	public BigDecimal getIdPersona() {
		return this.idPersona;
	}

	public void setIdPersona(BigDecimal idPersona) {
		this.idPersona = idPersona;
	}

	public BigDecimal getIdTipoTramite() {
		return this.idTipoTramite;
	}

	public void setIdTipoTramite(BigDecimal idTipoTramite) {
		this.idTipoTramite = idTipoTramite;
	}

	public String getRespuesta() {
		return this.respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public BigDecimal getTotalResultado() {
		return this.totalResultado;
	}

	public void setTotalResultado(BigDecimal totalResultado) {
		this.totalResultado = totalResultado;
	}

	public RugSecuUsuario getUsuario() {
		return usuario;
	}

	public void setUsuario(RugSecuUsuario usuario) {
		this.usuario = usuario;
	}
}