package mx.gob.se.rug.masiva.validate;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.xml.bind.JAXBContext;

import mx.gob.se.rug.masiva.exception.CargaMasivaFileLoadException;

public class ValidateTest {

	private static JAXBContext jaxbContext;
	private static mx.gob.se.rug.masiva.to.string.CargaMasiva cmString = null;
	private static javax.xml.bind.Unmarshaller unmarshaller;
	
	public static void main(String[] args) {
		
		/*ValidateDataType validateDataType = new ValidateDataType();
		
		try {
			jaxbContext = JAXBContext.newInstance(mx.gob.se.rug.masiva.to.string.CargaMasiva.class.getPackage().getName());
			unmarshaller = jaxbContext.createUnmarshaller();
			
			File initialFile = new File("C:\\export2\\archivo.txt");
			InputStream stream = new FileInputStream(initialFile);

			cmString = (mx.gob.se.rug.masiva.to.string.CargaMasiva) unmarshaller.unmarshal(stream);
		} catch (Exception e) {
			e.printStackTrace();			
		}
		
		System.out.println("xml: "+cmString.getTraslado().size());
		*/
		
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
		try {
			Date date = format.parse("08/04/2018 23:15:54");
			System.out.println(date);
		} catch(ParseException ex) {
			ex.printStackTrace();
		}
		
        
    }
}
