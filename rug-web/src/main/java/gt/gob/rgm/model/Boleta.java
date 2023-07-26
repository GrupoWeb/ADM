package gt.gob.rgm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the BOLETA database table.
 * 
 */
@Entity
@NamedQuery(name="Boleta.findAll", query="SELECT b FROM Boleta b")
public class Boleta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long id;

	private String agencia;

	@Column(name="CODIGO_TRAMITE")
	private Integer codigoTramite;

	@Column(name="FECHA_HORA")
	private Timestamp fechaHora;

	@Column(name="ID_TRANSACCION")
	private String idTransaccion;

	private String identificador;

	private BigDecimal monto;

	private String numero;

	private String resolucion;

	private String serie;

	private Integer usada;

	private String usuario;

	public Boleta() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAgencia() {
		return this.agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public Integer getCodigoTramite() {
		return this.codigoTramite;
	}

	public void setCodigoTramite(Integer codigoTramite) {
		this.codigoTramite = codigoTramite;
	}

	public Timestamp getFechaHora() {
		return this.fechaHora;
	}

	public void setFechaHora(Timestamp fechaHora) {
		this.fechaHora = fechaHora;
	}

	public String getIdTransaccion() {
		return this.idTransaccion;
	}

	public void setIdTransaccion(String idTransaccion) {
		this.idTransaccion = idTransaccion;
	}

	public String getIdentificador() {
		return this.identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public BigDecimal getMonto() {
		return this.monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getResolucion() {
		return this.resolucion;
	}

	public void setResolucion(String resolucion) {
		this.resolucion = resolucion;
	}

	public String getSerie() {
		return this.serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public Integer getUsada() {
		return this.usada;
	}

	public void setUsada(Integer usada) {
		this.usada = usada;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

}