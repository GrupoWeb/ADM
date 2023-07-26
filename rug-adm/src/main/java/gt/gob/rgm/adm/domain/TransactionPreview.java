package gt.gob.rgm.adm.domain;

import java.io.Serializable;
import java.util.List;

public class TransactionPreview implements Serializable {
	private Long idTramite;
	private Long idGarantia;
	private String fechaInscripcion;
	private String fechaUltAsiento;
	private String fechaAsiento;
	private String tipoAsiento;
	private Integer vigencia;
	private String descbienes;
	private boolean aBoolean;
	private boolean aBooleanNoGaraOt;
	private boolean aPrioridad;
	private boolean aRegistro;
	private String instrumento;
	private String otrosterminos;
	private String otroscontrato;
	private String otrosgarantia;
	private List<TransactionPart> deudorTOs;
	private List<TransactionPart> acreedorTOs;
	private List<TransactionPart> otorganteTOs;
	private List<SpecialGood> bienesEspTOs;
	private List<String> textos;

	public TransactionPreview() {
	}

	public Long getIdTramite() {
		return idTramite;
	}

	public void setIdTramite(Long idTramite) {
		this.idTramite = idTramite;
	}

	public Long getIdGarantia() {
		return idGarantia;
	}

	public void setIdGarantia(Long idGarantia) {
		this.idGarantia = idGarantia;
	}

	public String getFechaInscripcion() {
		return fechaInscripcion;
	}

	public void setFechaInscripcion(String fechaInscripcion) {
		this.fechaInscripcion = fechaInscripcion;
	}

	public String getFechaUltAsiento() {
		return fechaUltAsiento;
	}

	public void setFechaUltAsiento(String fechaUltAsiento) {
		this.fechaUltAsiento = fechaUltAsiento;
	}

	public String getFechaAsiento() {
		return fechaAsiento;
	}

	public void setFechaAsiento(String fechaAsiento) {
		this.fechaAsiento = fechaAsiento;
	}

	public String getTipoAsiento() {
		return tipoAsiento;
	}

	public void setTipoAsiento(String tipoAsiento) {
		this.tipoAsiento = tipoAsiento;
	}

	public Integer getVigencia() {
		return vigencia;
	}

	public void setVigencia(Integer vigencia) {
		this.vigencia = vigencia;
	}

	public String getDescbienes() {
		return descbienes;
	}

	public void setDescbienes(String descbienes) {
		this.descbienes = descbienes;
	}

	public boolean isaBoolean() {
		return aBoolean;
	}

	public void setaBoolean(boolean aBoolean) {
		this.aBoolean = aBoolean;
	}

	public boolean isaBooleanNoGaraOt() {
		return aBooleanNoGaraOt;
	}

	public void setaBooleanNoGaraOt(boolean aBooleanNoGaraOt) {
		this.aBooleanNoGaraOt = aBooleanNoGaraOt;
	}

	public boolean isaPrioridad() {
		return aPrioridad;
	}

	public void setaPrioridad(boolean aPrioridad) {
		this.aPrioridad = aPrioridad;
	}

	public boolean isaRegistro() {
		return aRegistro;
	}

	public void setaRegistro(boolean aRegistro) {
		this.aRegistro = aRegistro;
	}

	public String getInstrumento() {
		return instrumento;
	}

	public void setInstrumento(String instrumento) {
		this.instrumento = instrumento;
	}

	public String getOtrosterminos() {
		return otrosterminos;
	}

	public void setOtrosterminos(String otrosterminos) {
		this.otrosterminos = otrosterminos;
	}

	public String getOtroscontrato() {
		return otroscontrato;
	}

	public void setOtroscontrato(String otroscontrato) {
		this.otroscontrato = otroscontrato;
	}

	public String getOtrosgarantia() {
		return otrosgarantia;
	}

	public void setOtrosgarantia(String otrosgarantia) {
		this.otrosgarantia = otrosgarantia;
	}

	public List<TransactionPart> getDeudorTOs() {
		return deudorTOs;
	}

	public void setDeudorTOs(List<TransactionPart> deudorTOs) {
		this.deudorTOs = deudorTOs;
	}

	public List<TransactionPart> getAcreedorTOs() {
		return acreedorTOs;
	}

	public void setAcreedorTOs(List<TransactionPart> acreedorTOs) {
		this.acreedorTOs = acreedorTOs;
	}

	public List<SpecialGood> getBienesEspTOs() {
		return bienesEspTOs;
	}

	public void setBienesEspTOs(List<SpecialGood> bienesEspTOs) {
		this.bienesEspTOs = bienesEspTOs;
	}

	public List<TransactionPart> getOtorganteTOs() {
		return otorganteTOs;
	}

	public void setOtorganteTOs(List<TransactionPart> otorganteTOs) {
		this.otorganteTOs = otorganteTOs;
	}

	public List<String> getTextos() {
		return textos;
	}

	public void setTextos(List<String> textos) {
		this.textos = textos;
	}
}
