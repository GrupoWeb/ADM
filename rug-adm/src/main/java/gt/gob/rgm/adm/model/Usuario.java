package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the USUARIO database table.
 * 
 */
@Entity
@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="USUARIO_USUARIOID_GENERATOR", sequenceName="SEQ_USUARIO", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USUARIO_USUARIOID_GENERATOR")
	@Column(name="USUARIO_ID")
	private long usuarioId;

	private Timestamp actualizado;

	private Timestamp creado;

	private String email;

	private String estado;

	private String nombre;

	private String password;

	private String rol;

	private String token;

	public Usuario() {
	}

	public long getUsuarioId() {
		return this.usuarioId;
	}

	public void setUsuarioId(long usuarioId) {
		this.usuarioId = usuarioId;
	}

	public Timestamp getActualizado() {
		return this.actualizado;
	}

	public void setActualizado(Timestamp actualizado) {
		this.actualizado = actualizado;
	}

	public Timestamp getCreado() {
		return this.creado;
	}

	public void setCreado(Timestamp creado) {
		this.creado = creado;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRol() {
		return this.rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public static Usuario clonar(Usuario original) {
		Usuario clonado = new Usuario();
		clonado.setNombre(original.getNombre());
		clonado.setEmail(original.getEmail());
		clonado.setRol(original.getRol());
		clonado.setEstado(original.getEstado());
		clonado.setUsuarioId(original.getUsuarioId());
		clonado.setCreado(original.getCreado());
		clonado.setActualizado(original.getActualizado());
		
		return clonado;
	}
}
