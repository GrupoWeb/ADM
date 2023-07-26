package mx.gob.se.rug.partes.action;

import java.util.logging.Level;

import mx.gob.se.rug.inscripcion.service.InscripcionService;
import mx.gob.se.rug.inscripcion.service.impl.InscripcionServiceImpl;
import mx.gob.se.rug.inscripcion.to.AltaParteTO;
import mx.gob.se.rug.util.MyLogger;

public class ParteAction {
	
	private String folioElectronico;
	private String docExtranjero;
	private String rfc;
	private String nombre;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String razonSocial;
	private String nacionalidad;
	private String tipoPersona;
	
	public String agregaParte(){
		String regresa = "failed";
		try{
			AltaParteTO altaParteTO = new AltaParteTO();
			altaParteTO.setFolioMercantil(getFolioElectronico());
			altaParteTO.setCurp(getDocExtranjero());
			altaParteTO.setRfc(getRfc());
			altaParteTO.setNombre(getNombre());
			altaParteTO.setApellidoMaterno(getApellidoMaterno());
			altaParteTO.setApellidoPaterno(getApellidoPaterno());
			altaParteTO.setRazonSocial(getRazonSocial());
			altaParteTO.setIdNacionalidad(Integer.valueOf(getNacionalidad()));
			altaParteTO.setTipoPersona(getTipoPersona());
			InscripcionService inscripcionService = new InscripcionServiceImpl();			
			int idCreado = inscripcionService.insertaParte(altaParteTO);
			MyLogger.Logger.log(Level.INFO, "id del objeto creado" + idCreado);			
			regresa = "success";
		}catch(Exception e){
			e.printStackTrace();
		}
		return regresa;
	}

	public String getFolioElectronico() {
		return folioElectronico;
	}

	public void setFolioElectronico(String folioElectronico) {
		this.folioElectronico = folioElectronico;
	}

	public String getDocExtranjero() {
		return docExtranjero;
	}

	public void setDocExtranjero(String docExtranjero) {
		this.docExtranjero = docExtranjero;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidoPaterno() {
		return apellidoPaterno;
	}

	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	public String getApellidoMaterno() {
		return apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public String getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}
	
	
}
