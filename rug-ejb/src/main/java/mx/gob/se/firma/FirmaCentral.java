package mx.gob.se.firma;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Holder;

import mx.gob.se.constants.Constantes;
import mx.gob.se.exception.InfrastructureException;
import mx.gob.se.firma.co.to.MessageResponse;
import mx.gob.se.firma.co.to.ResponseWs;
import mx.gob.se.firma.to.FirmasTO;
import certificacion.WSDLCertificacionProcessPortType;
import certificacion.WSDLCertificacionProcessService;
import firmacentral.WSDLCertificacionPortType;
import firmacentral.WSDLCertificacionService;
import firmahistorico.WSDLGetInfoFirmaPortType;
import firmahistorico.WSDLGetInfoFirmaService;

public class FirmaCentral {

	public ResponseWs signCentral(String cadenaOriginalUsu, String certB64Usu,
			String firmaB64Usu, String xmlCo, Integer idTramite,
			Integer idUsuario) {

		Constantes constantes = new Constantes();
		ResponseWs responseWs = new ResponseWs();
		try {
			URL urlEndPoint = new URL(
					constantes.getParamValue(Constantes.ENDPOINT_FIRMACENTRAL));

			QName serviceName = new QName(
					"http://j2ee.netbeans.org/wsdl/certificacion/WSDL_certificacion",
					"WSDL_certificacionService");

			WSDLCertificacionService certificacionService = new WSDLCertificacionService(
					urlEndPoint, serviceName);
			WSDLCertificacionPortType portType = certificacionService
					.getWSDLCertificacionPort();
			Holder<String> code = new Holder<String>();
			Holder<String> messageError = new Holder<String>();

			portType.wsdlCertificacionOperation(cadenaOriginalUsu, certB64Usu,
					firmaB64Usu, idTramite, idUsuario, xmlCo, code,
					messageError);

			responseWs.setCodeResponse(Integer.valueOf(code.value));
			responseWs.setMessageError(messageError.value);

		} catch (MalformedURLException e) {
			responseWs.setCodeResponse(Integer.valueOf(999));
			responseWs
					.setMessageError("Error al Formar el EndPoint de firma central");
		}

		return responseWs;
	}

	public FirmasTO getFirmaHistorico(Integer idTramiteTemporal) throws InfrastructureException {

		Constantes constantes = new Constantes();
		FirmasTO firmasTO = new FirmasTO();
		MessageResponse messageResponse = new MessageResponse();
		try {
			 URL urlEndPoint = new URL(constantes.getParamValue(Constantes.ENDPOINT_HISTORICO));
			//URL urlEndPoint = new URL("http://localhost:9081/WSDL_getInfoFirmaService/WSDL_getInfoFirmaPort");

			QName serviceName = new QName(
					"http://j2ee.netbeans.org/wsdl/FirmaCentralRug/WSDL_getInfoFirma",
					"WSDL_getInfoFirmaService");

			Holder<String> stringOriginalUser = new Holder<String>();
			Holder<String> signUser = new Holder<String>();
			Holder<String> certUser = new Holder<String>();

			Holder<String> tsHuman = new Holder<String>();

			Holder<String> stringOriginalCentral = new Holder<String>();
			Holder<String> signCentral = new Holder<String>();
			Holder<String> certNumber = new Holder<String>();
			Holder<String> certCentral = new Holder<String>();

			Holder<String> code = new Holder<String>();
			Holder<String> messageError = new Holder<String>();

			WSDLGetInfoFirmaService getInfo = new WSDLGetInfoFirmaService(
					urlEndPoint, serviceName);
			WSDLGetInfoFirmaPortType getInfoFirmaPortType = getInfo
					.getWSDLGetInfoFirmaPort();

			getInfoFirmaPortType.wsdlGetInfoFirmaOperation(idTramiteTemporal,
					stringOriginalUser, signUser, certUser, tsHuman,
					stringOriginalCentral, signCentral, certNumber,
					certCentral, code, messageError);

			firmasTO.setStringOriginalUser(stringOriginalUser.value);
			firmasTO.setSignUser(signUser.value);
			firmasTO.setCertUser(certUser.value);
			
			if (!tsHuman.value.isEmpty()||!stringOriginalCentral.value.isEmpty()||!signCentral.value.isEmpty()
					||!certNumber.value.isEmpty()||!certCentral.value.isEmpty()){
				

				firmasTO.setTsHuman(tsHuman.value);
				firmasTO.setStringOriginalCentral(stringOriginalCentral.value);
				firmasTO.setSignCentral(signCentral.value);
				firmasTO.setCertNumber(certNumber.value);
				firmasTO.setCertCentral(certCentral.value);
				
				messageResponse.setCode(Integer.valueOf(code.value));
				messageResponse.setMessage(messageError.value);
				
			}else{
				throw new InfrastructureException("Error viene vacio la firma central Code Error::" + code.value+ "Message:: "+messageError.value);
			}
			
		

			firmasTO.setMr(messageResponse);
		} catch (MalformedURLException e) {
			messageResponse.setCode(Integer.valueOf(999));
			messageResponse
					.setMessage("Error al Formar el EndPoint de firma central");
			firmasTO.setMr(messageResponse);
		}
		return firmasTO;

	}

	public MessageResponse doCertificacion(String cadenaOriginal,
			Integer idTramiteTemp, Integer idUsuario) {

		Constantes constantes = new Constantes();
		MessageResponse messageResponse = new MessageResponse();
		try {

			URL urlEndPoint = new URL(
					constantes.getParamValue(Constantes.ENDPOINT_CERTIFICACION));
			//URL urlEndPoint = new URL("http://localhost:9081/WSDL_certificacionProcessService/WSDL_certificacionProcessPort");

			QName serviceName = new QName(
					"http://j2ee.netbeans.org/wsdl/FirmaCentralRug/WSDL_certificacionProcess",
					"WSDL_certificacionProcessService");
			WSDLCertificacionProcessService certificacionProcessService = new WSDLCertificacionProcessService(
					urlEndPoint, serviceName);
			WSDLCertificacionProcessPortType portType = certificacionProcessService
					.getWSDLCertificacionProcessPort();

			Holder<String> code = new Holder<String>();
			Holder<String> messageError = new Holder<String>();
			
			portType.wsdlCertificacionProcessOperation(cadenaOriginal,idTramiteTemp.toString(),
					idUsuario, code, messageError);

			messageResponse.setCode(Integer.valueOf(code.value));
			messageResponse.setMessage(messageError.value);

		} catch (MalformedURLException e) {
			messageResponse.setCode(Integer.valueOf(999));
			messageResponse
					.setMessage("Error al Formar el EndPoint de firma central");
		}

		return messageResponse;

	}

	public static void main(String args[]) {
//		 FirmaCentral central = new FirmaCentral();
//		 MessageResponse messageResponse= central.doCertificacion("CO", 609053,
//		 Integer.valueOf(2));
//		 System.out.println(messageResponse.getCode());
		FirmaCentral central = new FirmaCentral();
		try {
			System.out.println(central.getFirmaHistorico(609053).getCertCentral());
		} catch (InfrastructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
