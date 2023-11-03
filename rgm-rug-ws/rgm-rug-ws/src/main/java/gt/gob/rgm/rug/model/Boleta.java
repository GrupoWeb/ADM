package gt.gob.rgm.rug.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the BOLETA database table.
 * 
 */
@Entity
@Table(name="BOLETA", schema="RUG_UTIL")
@NamedQueries({
@NamedQuery(name="Boleta.findAll", query="SELECT b FROM Boleta b"),
@NamedQuery(name="Boleta.getSaldo", query="SELECT SUM(b.monto) FROM Boleta b WHERE b.identificador = :persona AND b.usada = :usada")
})
public class Boleta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="BOLETA_BOLETAID_GENERATOR", sequenceName="SEQ_BOLETA", schema="RUG_UTIL", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BOLETA_BOLETAID_GENERATOR")
	private Long id;

	private String agencia;

	@Column(name="CODIGO_TRAMITE")
	private Integer codigoTramite;

	@Column(name="FECHA_HORA")
	private Timestamp fechaHora;

	@Column(name="ID_GARANTIA")
	private BigDecimal idGarantia;

	@Column(name="ID_TRANSACCION")
	private String idTransaccion;

	private String identificador;

	private BigDecimal monto;
	
	@Column(name="MONTO_OTROS_BANCOS")
	private BigDecimal montoOtrosBancos;

	private String numero;

	private String resolucion;

	private String serie;

	private Integer usada;

	private String usuario;
	
	@Column(name="TIPO_PAGO")
	private String tipoPago;

	public Boleta() {
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

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getIdGarantia() {
		return this.idGarantia;
	}

	public void setIdGarantia(BigDecimal idGarantia) {
		this.idGarantia = idGarantia;
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

	public String getTipoPago() {
		return tipoPago;
	}

	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
	}

	public BigDecimal getMontoOtrosBancos() {
		return montoOtrosBancos;
	}

	public void setMontoOtrosBancos(BigDecimal montoOtrosBancos) {
		this.montoOtrosBancos = montoOtrosBancos;
	}

}