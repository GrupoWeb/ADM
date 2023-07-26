package mx.gob.se.rug.action;

import java.util.logging.Level;

import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;

import com.opensymphony.xwork2.ActionSupport;

public class IdentificarseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String SUCCESS = "success";
	private String nombre;
	private String contrasena;
	public IdentificarseAction(){
		super();
	}
	
	public String identificarse(){
		try{
			UsuarioTO usuarioTO = new UsuarioTO();
			usuarioTO.setContrasena(getContrasena());
			usuarioTO.setNombre(getNombre());
			MyLogger.Logger.log(Level.INFO, usuarioTO.getNombre());
			MyLogger.Logger.log(Level.INFO, usuarioTO.getContrasena());
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return SUCCESS;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
	
	
}
