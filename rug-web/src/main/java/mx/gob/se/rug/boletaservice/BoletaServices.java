package mx.gob.se.rug.boletaservice;

import mx.gob.se.rug.boleta.dao.BoletaDAO;
import mx.gob.se.rug.boleta.service.BoletaInfService;
import mx.gob.se.rug.boleta.to.BoletaAvisoPreventivoTO;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.exception.NoDataFoundException;

import org.apache.commons.collections.map.HashedMap;

public class BoletaServices {

	private String Boleta;
	private Integer idTramite;
	private String historicos;
	private String anotacions;
		

	public String getAnotacions() {
		return anotacions;
	}

	public void setAnotacions(String anotacions) {
		this.anotacions = anotacions;
	}

	public void setHistoricos(String historicos) {
		this.historicos = historicos;
	}

	public void setBoleta(String boleta) {
		Boleta = boleta;
	}
	
	public void setIdTramite(Integer idTramite) {
		this.idTramite = idTramite;
	}
	public Integer getIdTramite() {
		return idTramite;
	}
	public String getBoleta() {
		Constants constants = new Constants();
		Boleta =constants.getParamValue(Constants.BOLETA_CERTIFICADO);
		return Boleta;
	}
	public String getCertificacionHtml() {
		Constants constants = new Constants();
		return constants.getParamValue(Constants.CERTIFICACION_HTML);
	}
	public String getBoletaHtml() {
		Constants constants = new Constants();
		return constants.getParamValue(Constants.BOLETA_HTML);
	}
		
	public String getBoletaAnotacionSNGarantia() {
		BoletaInfService boletaInfService= new BoletaInfService();
		return boletaInfService.getHtmlInfo(1).toString();
	}
	
	public String getTemplateBoleta(Integer idBoleta) {
		
		if(Constants.boletaTemplate==null){
			Constants.boletaTemplate=new HashedMap();
		}
		
		if(Constants.boletaTemplate.get(idBoleta)==null){
			BoletaInfService boletaInfService= new BoletaInfService();
			Constants.boletaTemplate.put(idBoleta,  boletaInfService.getHtmlInfo(idBoleta).toString());
		}
		
		
		return new String( Constants.boletaTemplate.get(idBoleta));
	}
	public String getBoletaAnotacionCNGarantia() {
		Constants constants = new Constants();
		Boleta =constants.getParamValue(Constants.BOLETA_ANOTACION_CN_GARANTIA);
		return Boleta;
	}
	public String getBoletaAvisoPreventivol() {
		BoletaInfService boletaInfService= new BoletaInfService();
		return boletaInfService.getHtmlInfo(2).toString();
	}	
	public String getBoletaDetalle() {
		Constants constants = new Constants();
		Boleta =constants.getParamValue(Constants.BOLETA_DETALLE);
		return Boleta;
	}	
	public String getInscripcionHtml() {
		BoletaInfService boletaInfService= new BoletaInfService();
		return boletaInfService.getHtmlInfo(4).toString();
	}	
	public String getTransmisionHtml() {
		BoletaInfService boletaInfService= new BoletaInfService();
		return boletaInfService.getHtmlInfo(7).toString();
	}	
	public String getModificacionHtml() {
		Constants constants = new Constants();
		return constants.getParamValue(Constants.MODIFICACION_HTML);
		//BoletaInfService boletaInfService= new BoletaInfService();
		//return boletaInfService.getHtmlInfo(5).toString();
	}	
	public String getANotacionConHtml() {
		BoletaInfService boletaInfService= new BoletaInfService();
		return boletaInfService.getHtmlInfo(6).toString();
	}	
	public String getRectificacionHtml() {
		BoletaInfService boletaInfService= new BoletaInfService();
		return boletaInfService.getHtmlInfo(8).toString();
	}	
	public String getCancelacionHtml() {
		Constants constants = new Constants();
		return constants.getParamValue(Constants.CANCELACION_HTML);
		//BoletaInfService boletaInfService= new BoletaInfService();
		//return boletaInfService.getHtmlInfo(3).toString();
	}	
	public String getRenovacionHtml() {
		Constants constants = new Constants();
		return constants.getParamValue(Constants.RENOVACION_HTML);
		//BoletaInfService boletaInfService= new BoletaInfService();
		//return boletaInfService.getHtmlInfo(9).toString();
	}
	public String getEjecucionHtml() {
		Constants constants = new Constants();
		return constants.getParamValue(Constants.EJECUCION_HTML);
		//BoletaInfService boletaInfService= new BoletaInfService();
		//return boletaInfService.getHtmlInfo(9).toString();
	}
	public String getanotacionS() {
		Constants constants = new Constants();
		Boleta =constants.getParamValue(Constants.ANOTACION_SIN_GARANTIA);
		return Boleta;
	}	
	public String getHistoricos() {
		Constants constants = new Constants();
		historicos =constants.getParamValue(Constants.TRAMITES_HISTORICOS);
		return historicos;
	}	
	public BoletaAvisoPreventivoTO getAvisoPrevetivo(Integer idTramite) throws NoDataFoundException{
		return new BoletaDAO().getAvisoPreventivo(idTramite);
	}
}
