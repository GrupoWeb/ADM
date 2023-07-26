package mx.gob.se.firma;

import javax.ejb.Remote;

import mx.gob.se.firma.co.to.ResponseWs;
import mx.gob.se.firma.to.FirmasTO;

@Remote
public interface FooterAcuseBeanRemote {

	public FirmasTO getFirmasTO();
	
	public int getStatus();

	public String getDescStatus();

	public String getCadenaOriginal();

	public String getCadenaOriginalSE();

	public String getCertNSerieSE();

	public String getCertNSerieSolicitante();

	public String getCertSE();

	public String getCertSolicitante();

	public String getCmsSE();

	public String getCmsSolicitante();

	public String getDatosEstampillados();

	public String getTspCertificado();

	public String getTspEmisor();

	public String getTspFecha();

	public String getTspNSerie();

//	public void FooterAcuseInit(String sIdTramite,
//			String sUrlWebserviceFirmaTramites);
//
//	public void FooterCertificacionInit(String sUsuario, String sIdTramite,
//			String sContenido, String sUrlWebserviceFirmaTramites);

	public ResponseWs signCentral(String cadenaOriginalUsu, String certB64Usu,
			String firmaB64Usu, String xmlCO,Integer idTramite, Integer idUsuario);

	public String getCO(String urlBase, Integer idUsuario, Integer idTramite,
			Integer idSolicitud);
	public String getDocXML(String urlBase, Integer idUsuario, Integer idTramite,Integer idSolicitud);
	public void getFirmaHistorico(Integer idTramiteTemporal);
	
	public void doCertificacion(String cadenaOriginal,Integer idTramiteTemp,Integer idUsuario);
}
