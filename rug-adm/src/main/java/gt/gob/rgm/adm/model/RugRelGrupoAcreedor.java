package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the RUG_REL_GRUPO_ACREEDOR database table.
 * 
 */
@Entity
@Table(name="RUG_REL_GRUPO_ACREEDOR")
@NamedQuery(name="RugRelGrupoAcreedor.findAll", query="SELECT r FROM RugRelGrupoAcreedor r")
public class RugRelGrupoAcreedor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="RUGRELGRUPOACREEDOR_IDREG_GENERATOR", sequenceName="SEQ_RUG_REL_PRIVILEGIO_ACREEDO", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RUGRELGRUPOACREEDOR_IDREG_GENERATOR")
	@Column(name="ID_REG")
	private long idReg;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_REG")
	private Date fechaReg;

	@Column(name="ID_ACREEDOR")
	private Long idAcreedor;

	@Column(name="ID_GRUPO")
	private Integer idGrupo;

	@Column(name="ID_SUB_USUARIO")
	private Long idSubUsuario;

	@Column(name="ID_USUARIO")
	private Long idUsuario;

	@Column(name="STATUS_REG")
	private String statusReg;

	public RugRelGrupoAcreedor() {
	}

	public long getIdReg() {
		return this.idReg;
	}

	public void setIdReg(long idReg) {
		this.idReg = idReg;
	}

	public Date getFechaReg() {
		return this.fechaReg;
	}

	public void setFechaReg(Date fechaReg) {
		this.fechaReg = fechaReg;
	}

	public Long getIdAcreedor() {
		return this.idAcreedor;
	}

	public void setIdAcreedor(Long idAcreedor) {
		this.idAcreedor = idAcreedor;
	}

	public Integer getIdGrupo() {
		return this.idGrupo;
	}

	public void setIdGrupo(Integer idGrupo) {
		this.idGrupo = idGrupo;
	}

	public Long getIdSubUsuario() {
		return this.idSubUsuario;
	}

	public void setIdSubUsuario(Long idSubUsuario) {
		this.idSubUsuario = idSubUsuario;
	}

	public Long getIdUsuario() {
		return this.idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getStatusReg() {
		return this.statusReg;
	}

	public void setStatusReg(String statusReg) {
		this.statusReg = statusReg;
	}

}