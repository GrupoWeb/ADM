package gt.gob.rgm.adm.domain;

import java.io.Serializable;

public class Deposit implements Serializable {
	private Long id;
	private String agencia;
	private Integer codigoTramite;
	private String fechaHora;
	private Long idGarantia;
	private String idTransaccion;
	private String identificador;
	private Double monto;
	private Double montoOtrosBancos;
	private String numero;
	private String resolucion;
	private String serie;
	private Integer usada;
	private String usuario;
	private Boolean tieneArchivo;
	private String url;
	private String tipoPago;
	private String cause;
	private ExternalUser externalUser;
	private String fechaInicio;
        private String fechaFin;
	public Deposit() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public Integer getCodigoTramite() {
		return codigoTramite;
	}

	public void setCodigoTramite(Integer codigoTramite) {
		this.codigoTramite = codigoTramite;
	}

	public String getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
	}

	public Long getIdGarantia() {
		return idGarantia;
	}

	public void setIdGarantia(Long idGarantia) {
		this.idGarantia = idGarantia;
	}

	public String getIdTransaccion() {
		return idTransaccion;
	}

	public void setIdTransaccion(String idTransaccion) {
		this.idTransaccion = idTransaccion;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public Double getMonto() {
		return monto;
	}

	public void setMonto(Double monto) {
		this.monto = monto;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getResolucion() {
		return resolucion;
	}

	public void setResolucion(String resolucion) {
		this.resolucion = resolucion;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public Integer getUsada() {
		return usada;
	}

	public void setUsada(Integer usada) {
		this.usada = usada;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Boolean getTieneArchivo() {
		return tieneArchivo;
	}

	public void setTieneArchivo(Boolean tieneArchivo) {
		this.tieneArchivo = tieneArchivo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTipoPago() {
		return tipoPago;
	}

	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public ExternalUser getExternalUser() {
		return externalUser;
	}

	public void setExternalUser(ExternalUser externalUser) {
		this.externalUser = externalUser;
	}

	public Double getMontoOtrosBancos() {
		return montoOtrosBancos;
	}

	public void setMontoOtrosBancos(Double montoOtrosBancos) {
		this.montoOtrosBancos = montoOtrosBancos;
	}
        
        public String getFechaInicio() {
            return fechaInicio;
        }
        
        public void setFechaInicio(String fechaInicio) {
            this.fechaInicio = fechaInicio;
        }
        
        public String getFechaFin() {
            return fechaFin;
        }
        
        public void setFechaFin(String fechaFin) {
            this.fechaFin = fechaFin;
        }
}
