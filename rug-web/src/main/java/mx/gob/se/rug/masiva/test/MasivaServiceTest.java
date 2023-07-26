package mx.gob.se.rug.masiva.test;

import java.io.File;
import java.io.StringWriter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import mx.gob.se.rug.boleta.to.FirmaMasiva;
import mx.gob.se.rug.boleta.to.Tramite;
import mx.gob.se.rug.masiva.to.CargaMasiva;

public class MasivaServiceTest {
	public String getXMLCarga(CargaMasiva cargaMasiva,String pathResXML) {
		StringWriter stringWriter= new StringWriter();
		File file= new File(pathResXML);
		try {

			JAXBContext jaxbContext = JAXBContext.newInstance(CargaMasiva.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");

			jaxbMarshaller.marshal(cargaMasiva, System.out);
			jaxbMarshaller.marshal(cargaMasiva, file);
			jaxbMarshaller.marshal(cargaMasiva,stringWriter );
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		return stringWriter.toString();

	}
	

	private static final String SECURE_RANDOM_ALGORITHM = "SHA1PRNG";
	public static int generateRandom(int limit){
		SecureRandom sr = null;
        int aa = 0;
        try
        {
            sr = SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM);
            aa = sr.nextInt(limit);
        }catch(NoSuchAlgorithmException nsae)
        {
            
        }
        return aa;
	 }

	 public static void main(String args[]){

	 }
	 
	 public  FirmaMasiva getDataFromResFirma(String pathToXMLResp){
		 File fileResp = new File(pathToXMLResp);
		 JAXBContext jc;
		 FirmaMasiva firmaMasiva =null;
		try {
			jc = JAXBContext.newInstance (FirmaMasiva.class);

         Unmarshaller u = jc.createUnmarshaller ();
		 firmaMasiva = (FirmaMasiva) u.unmarshal (fileResp);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return firmaMasiva;
		
		
	 }
}

