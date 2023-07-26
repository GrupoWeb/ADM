package mx.gob.se.rug.to;

import java.io.Serializable;
import java.util.List;

import mx.gob.se.rug.dto.DatosContacto;
/**
 * 
 * @author Adat Gonzalez Dorantes,Carmen Aburto Castañeda
 * @see DireccionTO
 * @see TelefonoTO
 * @version 1.0
 * @category DataTransferObject
 */
public class PersonaTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private int idPersona;
	private String correoElectronico;
	private List <DireccionTO> direccion;
	private List <TelefonoTO> telefono;
	private String RFC;
	private DatosContacto datosContacto;
	private String perfil;
	private Integer idGrupo;
	private String nombre;
	
	public int getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(int idPersona) {
		this.idPersona = idPersona;
	}
	public String getCorreoElectronico() {
		return correoElectronico;
	}
	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}
	public List<DireccionTO> getDireccion() {
		return direccion;
	}
	public void setDireccion(List<DireccionTO> direccion) {
		this.direccion = direccion;
	}

	public List<TelefonoTO> getTelefono() {
		return telefono;
	}
	public void setTelefono(List<TelefonoTO> telefono) {
		this.telefono = telefono;
	}
	public String getRFC() {
		return RFC;
	}
	public void setRFC(String rFC) {
		RFC = rFC;
	}
	public void setDatosContacto(DatosContacto datosContacto) {
		this.datosContacto = datosContacto;
	}
	public DatosContacto getDatosContacto() {
		return datosContacto;
	}
	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}
	public String getPerfil() {
		return perfil;
	}
	public Integer getIdGrupo() {
		return idGrupo;
	}
	public void setIdGrupo(Integer idGrupo) {
		this.idGrupo = idGrupo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
