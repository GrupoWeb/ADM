package mx.gob.se.rug.masiva.to;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "bien-especial")
public class BienEspecial {

	@XmlElement(name = "tipo-bien-especial", required = true)
    protected String tipoBienEspecial;
	@XmlElement(name = "tipo-identificador", required = true)
    protected String tipoIdentificador;
	@XmlElement(name = "identificador", required = true)
    protected String identificador;
	@XmlElement(name = "descripcion", required = true)
	protected String descripcion;
	@XmlElement(name = "fecha", required = true)
	protected String fecha;
	@XmlElement(name = "operacion")
	protected String operacion;
	
    
	public String getTipoBienEspecial() {
		return tipoBienEspecial;
	}
	public void setTipoBienEspecial(String tipoBienEspecial) {
		this.tipoBienEspecial = tipoBienEspecial;
	}
	public String getTipoIdentificador() {
		return tipoIdentificador;
	}
	public void setTipoIdentificador(String tipoIdentificador) {
		this.tipoIdentificador = tipoIdentificador;
	}
	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getOperacion() {
		return operacion;
	}
	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}
		
}
