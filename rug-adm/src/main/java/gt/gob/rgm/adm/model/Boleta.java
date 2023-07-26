package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the BOLETA database table.
 * 
 */
@Entity
@Table(name="BOLETA", schema="RUG_UTIL")
@NamedQuery(name="Boleta.findAll", query="SELECT b FROM Boleta b")
public class Boleta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	private String agencia;

	@Lob
	@Basic(fetch=FetchType.LAZY)
	private byte[] bytes;

	@Column(name="CODIGO_TRAMITE")
	private Integer codigoTramite;

	@Column(name="FECHA_HORA")
	private Timestamp fechaHora;

	@Column(name="ID_GARANTIA")
	private Long idGarantia;

	@Column(name="ID_TRANSACCION")
	private String idTransaccion;

	private String identificador;

	private BigDecimal monto;
	
	@Column(name="MONTO_OTROS_BANCOS")
	private BigDecimal montoOtrosBancos;

	private String numero;

	private String resolucion;

	private String serie;

	@Column(name="TIPO_PAGO")
	private String tipoPago;

	private Integer usada;

	private String usuario;	
	
	private String causa;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="IDENTIFICADOR", insertable=false, updatable=false)
	private RugSecuUsuario secuUser;

	public Boleta() {
	}

	public String getAgencia() {
		return this.agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public byte[] getBytes() {
		return this.bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
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

	public Long getIdGarantia() {
		return this.idGarantia;
	}

	public void setIdGarantia(Long idGarantia) {
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

	public String getTipoPago() {
		return this.tipoPago;
	}

	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
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

	public String getCausa() {
		return causa;
	}

	public void setCausa(String causa) {
		this.causa = causa;
	}

	public RugSecuUsuario getSecuUser() {
		return secuUser;
	}

	public void setSecuUser(RugSecuUsuario secuUser) {
		this.secuUser = secuUser;
	}

	public BigDecimal getMontoOtrosBancos() {
		return montoOtrosBancos;
	}

	public void setMontoOtrosBancos(BigDecimal montoOtrosBancos) {
		this.montoOtrosBancos = montoOtrosBancos;
	}
}