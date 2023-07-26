package mx.gob.se.rug.masiva.to;

public class CargaMasivaProcess {
	private Integer idArchivo;
	private Integer idStatus;
	private Integer idArchivoFirma;
	private Integer idArchivoResumen;
	private Integer idUsuario;
	private Integer idAcreedor;
	private Integer idTipoTramite;
	private String bTipoProceso;// 'D' o 'A'
	
	
	public Integer getIdStatus() {
		return idStatus;
	}
	public void setIdStatus(Integer idStatus) {
		this.idStatus = idStatus;
	}
	public Integer getIdArchivo() {
		return idArchivo;
	}
	public void setIdArchivo(Integer idArchivo) {
		this.idArchivo = idArchivo;
	}
	
	
	public Integer getIdArchivoResumen() {
		return idArchivoResumen;
	}
	public void setIdArchivoResumen(Integer idArchivoResumen) {
		this.idArchivoResumen = idArchivoResumen;
	}
	public Integer getIdArchivoFirma() {
		return idArchivoFirma;
	}
	public void setIdArchivoFirma(Integer idArchivoFirma) {
		this.idArchivoFirma = idArchivoFirma;
	}
	public Integer getIdTipoTramite() {
		return idTipoTramite;
	}
	public void setIdTipoTramite(Integer idTipoTramite) {
		this.idTipoTramite = idTipoTramite;
	}
	public Integer getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	public Integer getIdAcreedor() {
		return idAcreedor;
	}
	public void setIdAcreedor(Integer idAcreedor) {
		this.idAcreedor = idAcreedor;
	}
	public String getbTipoProceso() {
		return bTipoProceso;
	}
	public void setbTipoProceso(String bTipoProceso) {
		this.bTipoProceso = bTipoProceso;
	}
	
	
	
	

}
