package mx.gob.se.rug.boleta.to;

public class BoletaAvisoPreventivoTO {

	private Integer idTramite;
	private String descripcionBienes;
	private String nombreUsuarioInscribe;
	private String fechaCreacion;
	private String perfil;
	private String vigencia;
	private String anotacionJuez;
	private String folioOtorgante;
	
	
	public String getAnotacionJuez() {
		return anotacionJuez;
	}
	public void setAnotacionJuez(String anotacionJuez) {
		this.anotacionJuez = anotacionJuez;
	}
	public Integer getIdTramite() {
		return idTramite;
	}
	public void setIdTramite(Integer idTramite) {
		this.idTramite = idTramite;
	}
	public String getDescripcionBienes() {
		return descripcionBienes;
	}
	public void setDescripcionBienes(String descripcionBienes) {
		this.descripcionBienes = descripcionBienes;
	}
	public String getNombreUsuarioInscribe() {
		return nombreUsuarioInscribe;
	}
	public void setNombreUsuarioInscribe(String nombreUsuarioInscribe) {
		this.nombreUsuarioInscribe = nombreUsuarioInscribe;
	}
	public String getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public String getPerfil() {
		return perfil;
	}
	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}
	public String getVigencia() {
		return vigencia;
	}
	public void setVigencia(String vigencia) {
		this.vigencia = vigencia;
	}
	public String getFolioOtorgante() {
		return folioOtorgante;
	}
	public void setFolioOtorgante(String folioOtorgante) {
		this.folioOtorgante = folioOtorgante;
	}
	
	
}
