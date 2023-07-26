package gt.gob.rgm.model;

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

	@Column(name="ID_GRUPO")
	private Long idGrupo;

	@Column(name="STATUS_REG")
	private String statusReg;
	
	@Column(name="ID_ACREEDOR")
	private Long idAcreedor;

	//bi-directional many-to-one association to RugPersona
	@ManyToOne
	@JoinColumn(name="ID_ACREEDOR", insertable=false, updatable=false)
	private RugPersonas acreedor;

	@Column(name="ID_USUARIO")
	private Long idUsuario;
	
	//bi-directional many-to-one association to RugPersona
	@ManyToOne
	@JoinColumn(name="ID_USUARIO", insertable=false, updatable=false)
	private RugPersonas usuario;

	@Column(name="ID_SUB_USUARIO")
	private Long idSubUsuario;
	
	//bi-directional many-to-one association to RugPersona
	@ManyToOne
	@JoinColumn(name="ID_SUB_USUARIO", insertable=false, updatable=false)
	private RugPersonas subUsuario;

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

	public Long getIdGrupo() {
		return this.idGrupo;
	}

	public void setIdGrupo(Long idGrupo) {
		this.idGrupo = idGrupo;
	}

	public String getStatusReg() {
		return this.statusReg;
	}

	public void setStatusReg(String statusReg) {
		this.statusReg = statusReg;
	}

	public Long getIdAcreedor() {
		return idAcreedor;
	}

	public void setIdAcreedor(Long idAcreedor) {
		this.idAcreedor = idAcreedor;
	}

	public RugPersonas getAcreedor() {
		return this.acreedor;
	}

	public void setAcreedor(RugPersonas acreedor) {
		this.acreedor = acreedor;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public RugPersonas getUsuario() {
		return this.usuario;
	}

	public void setUsuario(RugPersonas usuario) {
		this.usuario = usuario;
	}

	public Long getIdSubUsuario() {
		return idSubUsuario;
	}

	public void setIdSubUsuario(Long idSubUsuario) {
		this.idSubUsuario = idSubUsuario;
	}

	public RugPersonas getSubUsuario() {
		return this.subUsuario;
	}

	public void setSubUsuario(RugPersonas subUsuario) {
		this.subUsuario = subUsuario;
	}

}