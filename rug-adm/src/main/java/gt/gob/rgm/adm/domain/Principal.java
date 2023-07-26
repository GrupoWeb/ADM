package gt.gob.rgm.adm.domain;

import java.io.Serializable;

public class Principal implements Serializable {
	public static final int LOGIN_SUCCESS = 1;
	public static final int USER_NOT_FOUND = 2;
	public static final int USER_HAS_NO_ROLES = 3;
	public static final int AUTHENTICATION_FAILED = 4;
	public static final int USER_TEMPORARILY_DISABLED = 5;
	public static final int USER_DISABLED = 6;
	
	long usuarioId;
	String nombre;
	String email;
	String password;
	String token;
	String rol;
	int code;
	
	public Principal() {
	}

	public long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(long usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
