package mx.gob.se.rug.boleta.to;

import java.util.List;

public class DetalleTO {
	
private Integer idTramite;
private Integer idTipoTramite;
private Integer idGarantia;
private Integer idTramiteNuevo;
private String tipoTramite;
private List<PersonaTO> acreedorRepresentado;
private List<PersonaTO> acreedorAdicional;
private List<PersonaTO> acreedorOtorgante;
private List<PersonaTO> acreedorDeudor;
private GarantiaTO garantiaTO;
private PersonaTO personaInscribe;

private FirmaTO firmaTO;



public Integer getIdTramite() {
	return idTramite;
}
public void setIdTramite(Integer idTramite) {
	this.idTramite = idTramite;
}
public String getTipoTramite() {
	return tipoTramite;
}
public void setTipoTramite(String tipoTramite) {
	this.tipoTramite = tipoTramite;
}
public List<PersonaTO> getAcreedorRepresentado() {
	return acreedorRepresentado;
}
public void setAcreedorRepresentado(List<PersonaTO> acreedorRepresentado) {
	this.acreedorRepresentado = acreedorRepresentado;
}
public List<PersonaTO> getAcreedorAdicional() {
	return acreedorAdicional;
}
public void setAcreedorAdicional(List<PersonaTO> acreedorAdicional) {
	this.acreedorAdicional = acreedorAdicional;
}
public List<PersonaTO> getAcreedorOtorgante() {
	return acreedorOtorgante;
}
public void setAcreedorOtorgante(List<PersonaTO> acreedorOtorgante) {
	this.acreedorOtorgante = acreedorOtorgante;
}
public List<PersonaTO> getAcreedorDeudor() {
	return acreedorDeudor;
}
public void setAcreedorDeudor(List<PersonaTO> acreedorDeudor) {
	this.acreedorDeudor = acreedorDeudor;
}
public GarantiaTO getGarantiaTO() {
	return garantiaTO;
}
public void setGarantiaTO(GarantiaTO garantiaTO) {
	this.garantiaTO = garantiaTO;
}
public PersonaTO getPersonaInscribe() {
	return personaInscribe;
}
public void setPersonaInscribe(PersonaTO personaInscribe) {
	this.personaInscribe = personaInscribe;
}
public FirmaTO getFirmaTO() {
	return firmaTO;
}
public void setFirmaTO(FirmaTO firmaTO) {
	this.firmaTO = firmaTO;
}
public Integer getIdTipoTramite() {
	return idTipoTramite;
}
public void setIdTipoTramite(Integer idTipoTramite) {
	this.idTipoTramite = idTipoTramite;
}
public Integer getIdGarantia() {
	return idGarantia;
}
public void setIdGarantia(Integer idGarantia) {
	this.idGarantia = idGarantia;
}

public Integer getIdTramiteNuevo() {
	return idTramiteNuevo;
}
public void setIdTramiteNuevo(Integer idTramiteNuevo) {
	this.idTramiteNuevo = idTramiteNuevo;
}




}

