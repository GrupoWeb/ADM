package mx.gob.se.rug.garantia.to;

import java.io.Serializable;
import java.util.Date;

public class GarantiaTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idGarantia;
	private Integer idTipoGarantia;
	private ActoContratoTO actoContratoTO;
	private ObligacionTO obligacionTO;
	private Integer numeroGarantia;
	private String pathDocumento;
	private Integer vigencia;
	private Date fechaFin;
	private Integer idMoneda;
	private String descMoneda;
	private boolean chekcBox;
	
	public Integer getVigencia() {
		return vigencia;
	}
	public void setVigencia(Integer vigencia) {
		this.vigencia = vigencia;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	public ActoContratoTO getActoContratoTO() {
		return actoContratoTO;
	}
	public void setActoContratoTO(ActoContratoTO actoContratoTO) {
		this.actoContratoTO = actoContratoTO;
	}
	public ObligacionTO getObligacionTO() {
		return obligacionTO;
	}
	public void setObligacionTO(ObligacionTO obligacionTO) {
		this.obligacionTO = obligacionTO;
	}
	public void setIdGarantia(Integer idGarantia) {
		this.idGarantia = idGarantia;
	}
	public Integer getIdGarantia() {
		return idGarantia;
	}
	public void setNumeroGarantia(Integer numeroGarantia) {
		this.numeroGarantia = numeroGarantia;
	}
	public Integer getNumeroGarantia() {
		return numeroGarantia;
	}
	public void setPathDocumento(String pathDocumento) {
		this.pathDocumento = pathDocumento;
	}
	public String getPathDocumento() {
		return pathDocumento;
	}
	public void setIdTipoGarantia(Integer idTipoGarantia) {
		this.idTipoGarantia = idTipoGarantia;
	}
	public Integer getIdTipoGarantia() {
		return idTipoGarantia;
	}
	public void setIdMoneda(Integer idMoneda) {
		this.idMoneda = idMoneda;
	}
	public Integer getIdMoneda() {
		return idMoneda;
	}
	public void setDescMoneda(String descMoneda) {
		this.descMoneda = descMoneda;
	}
	public String getDescMoneda() {
		return descMoneda;
	}
	public void setChekcBox(boolean chekcBox) {
		this.chekcBox = chekcBox;
	}
	public boolean isChekcBox() {
		return chekcBox;
	}
	
	
}
