package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the HOMOLOGADO database table.
 * 
 */
@Entity
@NamedQuery(name="Homologado.findAll", query="SELECT h FROM Homologado h")
public class Homologado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="HOMOLOGADO_ID")
	private long homologadoId;

	private String causa;

	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;

	@Column(name="ID_GARANTIA")
	private BigDecimal idGarantia;

	@Column(name="ID_PERSONA_MIGRACION")
	private BigDecimal idPersonaMigracion;
	
	@JoinColumn(name="ID_PERSONA_MIGRACION", insertable=false, updatable=false)
	private RugSecuUsuario solicitanteOriginal;

	@Column(name="ID_PERSONA_RUG")
	private BigDecimal idPersonaRug;
	
	@JoinColumn(name="ID_PERSONA_RUG", insertable=false, updatable=false)
	private RugSecuUsuario solicitanteNuevo;

	@Column(name="USUARIO_ID")
	private BigDecimal usuarioId;
	
	@JoinColumn(name="USUARIO_ID", insertable=false, updatable=false)
	private Usuario operadaPor;

	public Homologado() {
	}

	public long getHomologadoId() {
		return this.homologadoId;
	}

	public void setHomologadoId(long homologadoId) {
		this.homologadoId = homologadoId;
	}

	public String getCausa() {
		return this.causa;
	}

	public void setCausa(String causa) {
		this.causa = causa;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public BigDecimal getIdGarantia() {
		return this.idGarantia;
	}

	public void setIdGarantia(BigDecimal idGarantia) {
		this.idGarantia = idGarantia;
	}

	public BigDecimal getIdPersonaMigracion() {
		return this.idPersonaMigracion;
	}

	public void setIdPersonaMigracion(BigDecimal idPersonaMigracion) {
		this.idPersonaMigracion = idPersonaMigracion;
	}

	public BigDecimal getIdPersonaRug() {
		return this.idPersonaRug;
	}

	public void setIdPersonaRug(BigDecimal idPersonaRug) {
		this.idPersonaRug = idPersonaRug;
	}

	public BigDecimal getUsuarioId() {
		return this.usuarioId;
	}

	public void setUsuarioId(BigDecimal usuarioId) {
		this.usuarioId = usuarioId;
	}

	public Usuario getOperadaPor() {
		return operadaPor;
	}

	public void setOperadaPor(Usuario operadaPor) {
		this.operadaPor = operadaPor;
	}

	public RugSecuUsuario getSolicitanteOriginal() {
		return solicitanteOriginal;
	}

	public void setSolicitanteOriginal(RugSecuUsuario solicitanteOriginal) {
		this.solicitanteOriginal = solicitanteOriginal;
	}

	public RugSecuUsuario getSolicitanteNuevo() {
		return solicitanteNuevo;
	}

	public void setSolicitanteNuevo(RugSecuUsuario solicitanteNuevo) {
		this.solicitanteNuevo = solicitanteNuevo;
	}
}