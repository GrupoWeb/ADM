package gt.gob.rgm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the RUG_GRUPOS database table.
 * 
 */
@Entity
@Table(name="RUG_GRUPOS")
@NamedQuery(name="RugGrupos.findAll", query="SELECT r FROM RugGrupos r")
public class RugGrupos implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_GRUPO")
	private long idGrupo;

	@Column(name="DESC_GRUPO")
	private String descGrupo;

	@Temporal(TemporalType.DATE)
	@Column(name="FH_BAJA")
	private Date fhBaja;

	@Temporal(TemporalType.DATE)
	@Column(name="FH_CREACION")
	private Date fhCreacion;

	@Column(name="ID_ACREEDOR")
	private BigDecimal idAcreedor;

	@Column(name="ID_PERSONA_CREA")
	private BigDecimal idPersonaCrea;

	@Column(name="ID_USUARIO_BAJA")
	private BigDecimal idUsuarioBaja;

	@Column(name="SIT_GRUPO")
	private String sitGrupo;

	//bi-directional many-to-one association to RugSecuUsuario
	@OneToMany(mappedBy="grupo")
	private List<RugSecuUsuarios> rugSecuUsuarios;

	public RugGrupos() {
	}

	public long getIdGrupo() {
		return this.idGrupo;
	}

	public void setIdGrupo(long idGrupo) {
		this.idGrupo = idGrupo;
	}

	public String getDescGrupo() {
		return this.descGrupo;
	}

	public void setDescGrupo(String descGrupo) {
		this.descGrupo = descGrupo;
	}

	public Date getFhBaja() {
		return this.fhBaja;
	}

	public void setFhBaja(Date fhBaja) {
		this.fhBaja = fhBaja;
	}

	public Date getFhCreacion() {
		return this.fhCreacion;
	}

	public void setFhCreacion(Date fhCreacion) {
		this.fhCreacion = fhCreacion;
	}

	public BigDecimal getIdAcreedor() {
		return this.idAcreedor;
	}

	public void setIdAcreedor(BigDecimal idAcreedor) {
		this.idAcreedor = idAcreedor;
	}

	public BigDecimal getIdPersonaCrea() {
		return this.idPersonaCrea;
	}

	public void setIdPersonaCrea(BigDecimal idPersonaCrea) {
		this.idPersonaCrea = idPersonaCrea;
	}

	public BigDecimal getIdUsuarioBaja() {
		return this.idUsuarioBaja;
	}

	public void setIdUsuarioBaja(BigDecimal idUsuarioBaja) {
		this.idUsuarioBaja = idUsuarioBaja;
	}

	public String getSitGrupo() {
		return this.sitGrupo;
	}

	public void setSitGrupo(String sitGrupo) {
		this.sitGrupo = sitGrupo;
	}

	public List<RugSecuUsuarios> getRugSecuUsuarios() {
		return this.rugSecuUsuarios;
	}

	public void setRugSecuUsuarios(List<RugSecuUsuarios> rugSecuUsuarios) {
		this.rugSecuUsuarios = rugSecuUsuarios;
	}

	public RugSecuUsuarios addRugSecuUsuario(RugSecuUsuarios rugSecuUsuario) {
		getRugSecuUsuarios().add(rugSecuUsuario);
		rugSecuUsuario.setRugGrupo(this);

		return rugSecuUsuario;
	}

	public RugSecuUsuarios removeRugSecuUsuario(RugSecuUsuarios rugSecuUsuario) {
		getRugSecuUsuarios().remove(rugSecuUsuario);
		rugSecuUsuario.setRugGrupo(null);

		return rugSecuUsuario;
	}

}