package mx.gob.se.rug.boleta.to;

public class CampoTO {
private Integer idCampo;
private String label;
private String value;
private String cveCampo;
private Integer orden;
private String orientacion;
private Boolean mostrar;



public Boolean getMostrar() {
	if (mostrar !=null){
		return mostrar;
	}else{
		return true;
	}
}
public void setMostrar(Boolean mostrar) {
	this.mostrar = mostrar;
}
public Integer getIdCampo() {
	return idCampo;
}
public void setIdCampo(Integer idCampo) {
	this.idCampo = idCampo;
}
public String getLabel() {
	return label;
}
public void setLabel(String label) {
	this.label = label;
}
public String getValue() {
	return value;
}
public void setValue(String value) {
	this.value = value;
}
public String getCveCampo() {
	return cveCampo;
}
public void setCveCampo(String cveCampo) {
	this.cveCampo = cveCampo;
}

public Integer getOrden() {
	return orden;
}
public void setOrden(Integer orden) {
	this.orden = orden;
}
public String getOrientacion() {
	return orientacion;
}
public void setOrientacion(String orientacion) {
	this.orientacion = orientacion;
}


}
