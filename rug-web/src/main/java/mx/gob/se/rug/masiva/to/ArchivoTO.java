package mx.gob.se.rug.masiva.to;

import java.io.Serializable;

public class ArchivoTO implements Serializable{
	/**
	 * 
	 */
	/*
	 *  ID_ARCHIVO      NUMBER                        NOT NULL,
  ID_USUARIO      NUMBER                        NOT NULL,
  ALGORITMO_HASH  VARCHAR2(4000 CHAR),
  NOMBRE_ARCHIVO  VARCHAR2(500 CHAR)            NOT NULL,
  ARCHIVO         BLOB                          NOT NULL,
  TIPO_ARCHIVO    VARCHAR2(50 CHAR)             NOT NULL,
  DESCRIPCION     VARCHAR2(4000 CHAR),
  STATUS_REG 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idArchivo;
	private Integer idUsuario;
	private byte[] archivo;
	private String algoritoHash;
	private String nombreArchivo;
	private String tipoArchivo;
	private String descripcion;
	
	private Integer idFirmaMasiva;
	public Integer getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	public byte[] getArchivo() {
		return archivo;
	}
	public void setArchivo(byte[] archivo) {
		this.archivo = archivo.clone();
	}
	public String getAlgoritoHash() {
		return algoritoHash;
	}
	public void setAlgoritoHash(String algoritoHash) {
		this.algoritoHash = algoritoHash;
	}
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	public String getTipoArchivo() {
		return tipoArchivo;
	}
	public void setTipoArchivo(String tipoArchivo) {
		this.tipoArchivo = tipoArchivo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public void setIdArchivo(Integer idArchivo) {
		this.idArchivo = idArchivo;
	}
	public Integer getIdArchivo() {
		return idArchivo;
	}
	public Integer getIdFirmaMasiva() {
		return idFirmaMasiva;
	}
	public void setIdFirmaMasiva(Integer idFirmaMasiva) {
		this.idFirmaMasiva = idFirmaMasiva;
	}
	

	


}
