package mx.gob.se.rug.inscripcion.to;

import java.io.Serializable;
import java.util.List;

import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.garantia.to.GarantiaTO;


public class InscripcionTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idInscripcion;
	private Integer idPersona;
	private Integer idTipoTramite;
	private Integer idAcreedorRepresentado;	
	private List<AcreedorTO> listaAcreedoresAdicionales;
	private List<DeudorTO> listaDeudores;
	private OtorganteTO otorganteTO;
	private AcreedorTO acreedorTO;
	private GarantiaTO garantiaTO;
	private VigenciaTO vigenciaTO;
	private String meses;
	
	public InscripcionTO(){
		this.meses = "5";
	}
	public List<AcreedorTO> getListaAcreedoresAdicionales() {
		return listaAcreedoresAdicionales;
	}
	public void setListaAcreedoresAdicionales(
			List<AcreedorTO> listaAcreedoresAdicionales) {
		this.listaAcreedoresAdicionales = listaAcreedoresAdicionales;
	}
	public List<DeudorTO> getListaDeudores() {
		return listaDeudores;
	}
	public void setListaDeudores(List<DeudorTO> listaDeudores) {
		this.listaDeudores = listaDeudores;
	}
	public OtorganteTO getOtorganteTO() {
		return otorganteTO;
	}
	public void setOtorganteTO(OtorganteTO otorganteTO) {
		this.otorganteTO = otorganteTO;
	}
	public AcreedorTO getAcreedorTO() {
		return acreedorTO;
	}
	public void setAcreedorTO(AcreedorTO acreedorTO) {
		this.acreedorTO = acreedorTO;
	}
	public GarantiaTO getGarantiaTO() {
		return garantiaTO;
	}
	public void setGarantiaTO(GarantiaTO garantiaTO) {
		this.garantiaTO = garantiaTO;
	}
	public VigenciaTO getVigenciaTO() {
		return vigenciaTO;
	}
	public void setVigenciaTO(VigenciaTO vigenciaTO) {
		this.vigenciaTO = vigenciaTO;
	}
	public void setIdInscripcion(Integer idInscripcion) {
		this.idInscripcion = idInscripcion;
	}
	public Integer getIdInscripcion() {
		return idInscripcion;
	}
	public void setIdPersona(Integer idPersona) {
		this.idPersona = idPersona;
	}
	public Integer getIdPersona() {
		return idPersona;
	}
	public void setIdTipoTramite(Integer idTipoTramite) {
		this.idTipoTramite = idTipoTramite;
	}
	public Integer getIdTipoTramite() {
		return idTipoTramite;
	}
	public void setIdAcreedorRepresentado(Integer idAcreedorRepresentado) {
		this.idAcreedorRepresentado = idAcreedorRepresentado;
	}
	public Integer getIdAcreedorRepresentado() {
		return idAcreedorRepresentado;
	}
	public void setMeses(String meses) {
		this.meses = meses;
	}
	public String getMeses() {
		return meses;
	}

	
	
	
	
	

}
