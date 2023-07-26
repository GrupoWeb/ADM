package gt.gob.rgm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the ARCHIVO database table.
 * 
 */
@Entity
@NamedQuery(name="Archivo.findAll", query="SELECT a FROM Archivo a")
public class Archivo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ARCHIVO_ARCHIVOID_GENERATOR", sequenceName="SEQ_ARCHIVO", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ARCHIVO_ARCHIVOID_GENERATOR")
	@Column(name="ARCHIVO_ID")
	private long archivoId;

	private Timestamp fecha;

	@Column(name="OBJETO_ID")
	private long objetoId;

	private String tipo;

	private String url;
	
	private String estado;

	public Archivo() {
	}

	public long getArchivoId() {
		return this.archivoId;
	}

	public void setArchivoId(long archivoId) {
		this.archivoId = archivoId;
	}

	public Timestamp getFecha() {
		return this.fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public long getObjetoId() {
		return this.objetoId;
	}

	public void setObjetoId(long objetoId) {
		this.objetoId = objetoId;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}