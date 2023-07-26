package mx.gob.se.rug.inscripcion.to;

import java.io.Serializable;

public class FirmaMasivaTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idUsuario;
	private String tramites;
	private Integer idArchivo;
	private Integer idTramiteFirmaMas;
	private Integer idFirmaMasiva;
	private Integer idAcreedor;
	/**
	 * @return the idUsuario
	 */
	public Integer getIdUsuario() {
		return idUsuario;
	}
	/**
	 * @param idUsuario the idUsuario to set
	 */
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	/**
	 * @return the tramites
	 */
	public String getTramites() {
		return tramites;
	}
	/**
	 * @param tramites the tramites to set
	 */
	public void setTramites(String tramites) {
		this.tramites = tramites;
	}
	/**
	 * @return the idArchivo
	 */
	public Integer getIdArchivo() {
		return idArchivo;
	}
	/**
	 * @param idArchivo the idArchivo to set
	 */
	public void setIdArchivo(Integer idArchivo) {
		this.idArchivo = idArchivo;
	}
	/**
	 * @return the idTramiteFirmaMas
	 */
	public Integer getIdTramiteFirmaMas() {
		return idTramiteFirmaMas;
	}
	/**
	 * @param idTramiteFirmaMas the idTramiteFirmaMas to set
	 */
	public void setIdTramiteFirmaMas(Integer idTramiteFirmaMas) {
		this.idTramiteFirmaMas = idTramiteFirmaMas;
	}
	/**
	 * @return the idFirmaMasiva
	 */
	public Integer getIdFirmaMasiva() {
		return idFirmaMasiva;
	}
	/**
	 * @param idFirmaMasiva the idFirmaMasiva to set
	 */
	public void setIdFirmaMasiva(Integer idFirmaMasiva) {
		this.idFirmaMasiva = idFirmaMasiva;
	}
	public void setIdAcreedor(Integer idAcreedor) {
		this.idAcreedor = idAcreedor;
	}
	public Integer getIdAcreedor() {
		return idAcreedor;
	}
	
	

}
