package mx.gob.se.firma.co;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;



public class ClienteCadenaOriginal {

	
	
	public String getCadenaOriginal(String urlBase,Integer idUsuario, Integer idTramite,Integer idSolicitud){
		
		String cadenaOriginal=null;
		StringBuffer urlWebservice= new StringBuffer();
		
		urlWebservice.append(urlBase);
		urlWebservice.append("resources/getCadenaOriginal/");
		urlWebservice.append(idUsuario);
		urlWebservice.append("/");
		urlWebservice.append(idSolicitud);
		urlWebservice.append("/");
		urlWebservice.append(idTramite);
		try {
			cadenaOriginal = getRespuesta(new URL(urlWebservice.toString()));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return cadenaOriginal;
		
	}
	
	
public String getDocumentoXML(String urlBase,Integer idUsuario, Integer idTramite,Integer idSolicitud){
		
		String documentoXML=null;
		StringBuffer urlWebservice= new StringBuffer();
		
		urlWebservice.append(urlBase);
		urlWebservice.append("resources/getSolicitudTramiteXML/");
		urlWebservice.append(idUsuario);
		urlWebservice.append("/");
		urlWebservice.append(idTramite);
		urlWebservice.append("/");
		urlWebservice.append(idSolicitud);
		try {
			documentoXML = getRespuesta(new URL(urlWebservice.toString()));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return documentoXML;
		
	}
	
	private  String getRespuesta(URL urlWebservice) throws JAXBException, UnsupportedEncodingException {		
		Respuesta respuesta = null;
		try {
			System.out.println(urlWebservice.toString());
			JAXBContext jaxbContext = JAXBContext
					.newInstance(Respuesta.class.getPackage().getName());
			javax.xml.bind.Unmarshaller unmarshaller;
			unmarshaller = jaxbContext.createUnmarshaller();
			respuesta = (Respuesta) unmarshaller.unmarshal(urlWebservice.openStream());	

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		 return new String(respuesta.getContent().replace("|<firma:replace>certNSerie</firma:replace>", "").getBytes("ISO-8859-1"));
	}

	
	
	public static void main(String args[]){
		String co;
		ClienteCadenaOriginal cadenaOriginal= new ClienteCadenaOriginal();
		co=cadenaOriginal.getCadenaOriginal("http://172.18.53.22:4580/WebserviceGeneraTramiteXML/", 2, 212, 348790);
		System.out.println(co);
		
		String doc;
		doc=cadenaOriginal.getDocumentoXML("http://172.18.53.22:4580/WebserviceGeneraTramiteXML/", 2, 212, 348790);
		System.out.println(doc);
		System.out.println("-----------------------");
		String digestivo = null;
		  java.security.MessageDigest messageDigest;
		try {
			messageDigest = java.security.MessageDigest.getInstance("SHA-1");
			byte[] digest = messageDigest.digest(doc.getBytes());
			digestivo = new String(org.apache.commons.codec.binary.Base64.encodeBase64(digest));
			System.out.println(digestivo);
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
