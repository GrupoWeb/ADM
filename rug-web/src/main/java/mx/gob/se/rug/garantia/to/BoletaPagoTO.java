package mx.gob.se.rug.garantia.to;

public class BoletaPagoTO {

	private Integer idGarantia;
	private Integer idTipoGarantia;
	private Integer identificador;
	private String serie;
	private String numero;
	private String banco;
	private String formaPago;
	private Double monto;
	private byte[] boletaBytes;
	private Integer idPersonaCarga;
	private String nombrePersonaCarga;
	
	public Integer getIdGarantia() {
		return idGarantia;
	}
	public void setIdGarantia(Integer idGarantia) {
		this.idGarantia = idGarantia;
	}
	public Integer getIdTipoGarantia() {
		return idTipoGarantia;
	}
	public void setIdTipoGarantia(Integer idTipoGarantia) {
		this.idTipoGarantia = idTipoGarantia;
	}
	public Integer getIdentificador() {
		return identificador;
	}
	public void setIdentificador(Integer identificador) {
		this.identificador = identificador;
	}
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public Double getMonto() {
		return monto;
	}
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	public byte[] getBoletaBytes() {
		return boletaBytes;
	}
	public void setBoletaBytes(byte[] boletaBytes) {
		this.boletaBytes = boletaBytes.clone();
	}
	public String getBanco() {
		return banco;
	}
	public void setBanco(String banco) {
		this.banco = banco;
	}
	public String getFormaPago() {
		return formaPago;
	}
	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}
	public Integer getIdPersonaCarga() {
		return idPersonaCarga;
	}
	public void setIdPersonaCarga(Integer idPersonaCarga) {
		this.idPersonaCarga = idPersonaCarga;
	}
	public String getNombrePersonaCarga() {
		return nombrePersonaCarga;
	}
	public void setNombrePersonaCarga(String nombrePersonaCarga) {
		this.nombrePersonaCarga = nombrePersonaCarga;
	}
}
