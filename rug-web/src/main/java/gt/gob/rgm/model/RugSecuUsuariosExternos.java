package gt.gob.rgm.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="RUG_SECU_USUARIOS_EXTERNOS")
@NamedQuery(name="RugSecuUsuariosExternos.findAll", query="SELECT r FROM RugSecuUsuariosExternos r")
public class RugSecuUsuariosExternos implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_USUARIO")
	private Long idUsuario;
	
	@Column(name="CVE_USUARIO")
	private String cveUsuario;
	
	private String password;
	
	@Column(name="SIT_USUARIO")
	private String sitUsuario;
	
	@Column(name="CVE_PERFIL")
	private String cvePerfil;
	
	@Column(name="USUARIO_INGRESO")
	private String usuarioIngreso;
	
	@Column(name="FECHA_INGRESO")
	private Timestamp fechaIngreso;
	
	public RugSecuUsuariosExternos() {
		
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getCveUsuario() {
		return cveUsuario;
	}

	public void setCveUsuario(String cveUsuario) {
		this.cveUsuario = cveUsuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSitUsuario() {
		return sitUsuario;
	}

	public void setSitUsuario(String sitUsuario) {
		this.sitUsuario = sitUsuario;
	}

	public String getCvePerfil() {
		return cvePerfil;
	}

	public void setCvePerfil(String cvePerfil) {
		this.cvePerfil = cvePerfil;
	}

	public String getUsuarioIngreso() {
		return usuarioIngreso;
	}

	public void setUsuarioIngreso(String usuarioIngreso) {
		this.usuarioIngreso = usuarioIngreso;
	}

	public Timestamp getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Timestamp fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
	
}
