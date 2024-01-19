package mx.gob.se.rug.masiva.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import mx.gob.se.rug.boleta.to.FirmaMasiva;
import mx.gob.se.rug.boleta.to.Tramite;
import mx.gob.se.rug.exception.InfrastructureException;
import mx.gob.se.rug.inscripcion.service.InscripcionService;
import mx.gob.se.rug.inscripcion.service.impl.InscripcionServiceImpl;
import mx.gob.se.rug.masiva.daemon.CargaMasivaController;
import mx.gob.se.rug.masiva.dao.MasivaDAO;
import mx.gob.se.rug.masiva.exception.CargaMasivaException;
import mx.gob.se.rug.masiva.exception.CargaMasivaExceptionMaxNumber;
import mx.gob.se.rug.masiva.exception.NoTramiteFound;
import mx.gob.se.rug.masiva.to.ArchivoTO;
import mx.gob.se.rug.masiva.to.CargaMasiva;
import mx.gob.se.rug.masiva.to.CargaMasivaProcess;
import mx.gob.se.rug.masiva.to.Convenio;
import mx.gob.se.rug.masiva.to.DatosModificacion;
import mx.gob.se.rug.masiva.to.Deudor;
import mx.gob.se.rug.masiva.to.EliminarPartesModificacion;
import mx.gob.se.rug.masiva.to.IdentificadorGarantia;
import mx.gob.se.rug.masiva.to.Modificacion;
import mx.gob.se.rug.masiva.to.ObligacionGarantizaModificacion;
import mx.gob.se.rug.masiva.to.PersonaSolicitaAutoridadInstruyeAsiento;
import mx.gob.se.rug.masiva.to.TipoBienMueble;
import mx.gob.se.rug.masiva.validate.ValidateDataType;
import mx.gob.se.rug.to.PersonaTO;
import mx.gob.se.rug.to.PlSql;
import mx.gob.se.rug.to.UsuarioTO;

public class ModificacionTest {

	private static ArchivoTO archivoTO;
	private static InscripcionService inscripcionService = new InscripcionServiceImpl();
	private static CargaMasivaProcess masivaProcess;
	private static MasivaDAO masivaDAO = new MasivaDAO();
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		 * PAra correr esta clase se enecesitan 3 parametros
		 * 1.- path XML de resultado final de inscripcion masiva
		 * 2.- path de destino con nombre y extension del xmla generar
		 * 3.- Si quieres que te genere para autoridad o no 
		 */
		
		/*String pathXMLInscripcion=args[0];
		String pathXMLModificacion=args[1];
		String autoridad = args[2];
		
		MasivaServiceTest masivaServiceTest= new MasivaServiceTest();

		int rastreoId=masivaServiceTest.generateRandom(300);
		
		FirmaMasiva firmaMasiva=masivaServiceTest.getDataFromResFirma(pathXMLInscripcion);
		
		List<Tramite> tramites= firmaMasiva.getTramite();
		
		CargaMasiva cargaMasiva = new CargaMasiva();
		List<Modificacion> modificaciones= cargaMasiva.getModificacion();
		Modificacion modificacion= null;
		int conteo=1;
		for (Tramite tramite : tramites) {
			modificacion= new Modificacion();
			
			if(autoridad.equalsIgnoreCase("si")){
				PersonaSolicitaAutoridadInstruyeAsiento autoridadInstruyeAsiento= new PersonaSolicitaAutoridadInstruyeAsiento();
				autoridadInstruyeAsiento.setContenidoResolucion("PersonaSolicitaAutoridadInstruyeAsiento"+tramite.getIdGarantia().toString());
				modificacion.setPersonaSolicitaAutoridadInstruyeAsiento(autoridadInstruyeAsiento);
			}
			//Parte deudor borrar
			if(masivaServiceTest.generateRandom(2)==1){
				EliminarPartesModificacion eliminarPartesModificacion= new EliminarPartesModificacion();
				eliminarPartesModificacion.setEliminaDeudores(true);
				//modificacion.setEliminarPartesModificacion(eliminarPartesModificacion);
			}
			//parte deudor agregar 
			List<Deudor> deudors= modificacion.getPartes().getDeudor();
			int nDeudores=masivaServiceTest.generateRandom(5);
			for(int i=0;i<nDeudores;i++ ){
				int deudorDesicion=masivaServiceTest.generateRandom(4);//0..3
				Deudor deudor= new Deudor();
				switch (deudorDesicion) {
				case 0://Persona Fisica Mexicana
					deudor.setIdNacionalidad(new BigInteger("1"));
					deudor.setTipoPersona("PF");
					deudor.setNombre("Nombre"+i);
					deudor.setApellidoPaterno("ApellidoPaterno");
					deudor.setApellidoMaterno("ApellidoMaterno");
					break;
				case 1://Persona Moral Mexicana
					deudor.setIdNacionalidad(new BigInteger("1"));
					deudor.setTipoPersona("PM");
					deudor.setDenominacionRazonSocial("DenominacionRazonSocial"+i);
					break;
				case 2://Persona Fisica Extranjera
					deudor.setIdNacionalidad(new BigInteger("214"));
					deudor.setTipoPersona("PF");
					deudor.setNombre("Nombre Extranjero"+i);
					deudor.setApellidoPaterno("ApellidoPaterno");
					deudor.setApellidoMaterno("ApellidoMaterno");
					break;
				case 3://Persona Moral Extranjera
					deudor.setIdNacionalidad(new BigInteger("214"));
					deudor.setTipoPersona("PM");
					deudor.setDenominacionRazonSocial("DenominacionRazonSocial"+i);
					break;
	
				default:
					break;
				}
				deudors.add(deudor);
			}
			
			DatosModificacion datosModificacion= new DatosModificacion();
			
			datosModificacion.setDescripcionBienesMuebles("DescripcionBienesMuebles"+conteo);
			if(masivaServiceTest.generateRandom(2)==0){
				datosModificacion.setIdMoneda(new BigInteger("15"));
			}
			if(masivaServiceTest.generateRandom(2)==0){
				datosModificacion.setMontoMaximo(new Double(masivaServiceTest.generateRandom(300)+"."+masivaServiceTest.generateRandom(100)));
			}
			if(masivaServiceTest.generateRandom(2)==0){
				
			boolean f= true;
				if(masivaServiceTest.generateRandom(2)==0){
					f=false;
				}
				datosModificacion.setPreveCambiosBienesMueblesMonto(f);
			}
			
			
			if(masivaServiceTest.generateRandom(2)==0){
				datosModificacion.setTerminosCondiciones("TerminosCondiciones"+conteo);
			}
			List<TipoBienMueble> bienMuebles= datosModificacion.getTipoBienMueble();
			TipoBienMueble bienMueble= null;
			int nBienesMueble=masivaServiceTest.generateRandom(9);
			for(int i = 0;i<nBienesMueble;i++){
				bienMueble= new TipoBienMueble();
				bienMueble.setId(new BigInteger((i+1)+""));
				bienMuebles.add(bienMueble);
			}
			
			//modificacion.setDatosModificacion(datosModificacion);
			
			ObligacionGarantizaModificacion obligacionGarantiza= new ObligacionGarantizaModificacion();
			obligacionGarantiza.setActoContrato("ActoContrato"+conteo);
			obligacionGarantiza.setTerminos("Terminos"+conteo);
			
			GregorianCalendar c = new GregorianCalendar();
			c.set(2013, masivaServiceTest.generateRandom(12)+1, masivaServiceTest.generateRandom(28)+1);
			XMLGregorianCalendar fterminacion;
			try {
				fterminacion = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
				obligacionGarantiza.setFechaTerminacion(fterminacion);
			} catch (DatatypeConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//modificacion.setObligacionGarantizaModificacion(obligacionGarantiza);
			
			Convenio convenio= new Convenio();
			convenio.setTerminosCondiciones("TerminosCondiciones"+conteo);
			GregorianCalendar c1 = new GregorianCalendar();
			c1.set(2013, masivaServiceTest.generateRandom(12)+1, masivaServiceTest.generateRandom(28)+1);
			XMLGregorianCalendar fterminacion2;
			try {
				fterminacion2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c1);
				convenio.setFechaTerminacion(fterminacion2);
			} catch (DatatypeConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			GregorianCalendar c2 = new GregorianCalendar();
			c2.set(2011, masivaServiceTest.generateRandom(12)+1, masivaServiceTest.generateRandom(28)+1);
			XMLGregorianCalendar fterminacion3;
			try {
				fterminacion3 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c2);
				convenio.setFechaCelebracion(fterminacion3);
			} catch (DatatypeConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//modificacion.setConvenio(convenio);
			
			
			IdentificadorGarantia identificador= new IdentificadorGarantia();
			identificador.setClaveRastreo("cve-rastreo-"+rastreoId+"-"+conteo);
			identificador.setIdGarantia(tramite.getIdGarantia());
			modificacion.setIdentificadorGarantia(identificador);
			
			modificaciones.add(modificacion);
			conteo=conteo+1;
		}

		
		masivaServiceTest.getXMLCarga(cargaMasiva, pathXMLModificacion);*/
		String path = "C:\\carga\\modificacion101.xml";
		File archivo = new File(path);
		UsuarioTO usuario = new UsuarioTO();
		PersonaTO persona = new PersonaTO();
		persona.setIdPersona(1);
		usuario.setPersona(persona);
		String sha1 = getSha1FromFile(archivo);
		
		if(matchXmltoXsd(archivo)){
			//archivoTO = creaArchivoTo(usuario,sha1,archivo,"testModificacion.xml");
			masivaProcess = new CargaMasivaProcess();
			masivaProcess.setIdArchivo(420);
			masivaProcess.setIdTipoTramite(7);
			
			masivaProcess.setIdUsuario(1);
			masivaProcess.setIdAcreedor(1);
			masivaProcess.setbTipoProceso("A");
			
			/*try {
				masivaDAO.insertProcessCarga(masivaProcess);
			} catch (CargaMasivaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InfrastructureException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			ValidateDataType validateDataType = new ValidateDataType();
			
			String xmlFromDB = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <car:carga-masiva xmlns:car=\"carga-masiva\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"carga-masiva-string.xsd\"> <car:modificacion> <car:identificador>87400107</car:identificador> <car:id-garantia>284</car:id-garantia> <car:partes> <car:deudor-garante> <car:codigo-nacionalidad>1</car:codigo-nacionalidad> <car:tipo-persona>PF</car:tipo-persona> <car:identificador>101</car:identificador> <car:nit>901</car:nit> <car:nombre>MARTY KEANE 901 Modificada 2a vez</car:nombre> <car:correo-electronico>marty.keane@prueba.com</car:correo-electronico> <car:domicilio>Guatemala</car:domicilio> <car:operacion>2</car:operacion> </car:deudor-garante> </car:partes> <car:garantia> <car:info-general-contrato>Segun Contrato de Aceptacion en Escritura Publica 614 autorizada por la Notaria Monica Maria Barrios Alvarado el 20/12/2018</car:info-general-contrato> </car:garantia> <car:razon-modificacion>Prueba 284</car:razon-modificacion> </car:modificacion> </car:carga-masiva>"; 
					//validateDataType.getFileFromDB(masivaProcess.getIdArchivo());
			try {
                            System.out.println("validateCargaMasiva de ModificacionTest");
				validateDataType.validateCargaMasiva(xmlFromDB, masivaProcess.getIdTipoTramite());
			} catch (CargaMasivaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoTramiteFound e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CargaMasivaExceptionMaxNumber e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			CargaMasivaController cargaMasivaController= new CargaMasivaController();
			cargaMasivaController.procesaCargaMasiva(masivaProcess);
			
		}else{
			System.out.println("El archivo que estas tratando de subir no corresponde al XSD.");
		}
	}

	public static boolean matchXmltoXsd(File xml) {
		System.out.println("-- start matchXmltoXsd--");
		boolean regresa = false;
		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		try {
			@SuppressWarnings("deprecation") File fileXsd = new File("C:\\carga\\carga-masiva-simple.xsd");
			Schema schema = factory.newSchema(fileXsd);
			Validator validator = schema.newValidator();
			Source source = new StreamSource(xml);
			validator.validate(source);
			regresa = true;
		} catch (SAXParseException ex) {
			StringBuffer sb= new StringBuffer();
			sb.append("Linea:");
			sb.append(ex.getLineNumber());
			sb.append("	Columna:");
			sb.append(ex.getColumnNumber());
			sb.append("	Mensaje:");
			sb.append(ex.getMessage());
			System.out.println("mensaje:::" + ex.getMessage());
			ex.printStackTrace();
		} catch (SAXException ex) {
			System.out.println("mensaje:::" + ex.getMessage());
			ex.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return regresa;
	}
	
	private static ArchivoTO creaArchivoTo(UsuarioTO usuario,	String sha1, File archivo, String archivoFileName){
		try{
			archivoTO = new ArchivoTO();
			archivoTO.setAlgoritoHash(sha1);
			archivoTO.setArchivo(getBytesFromFile(archivo));
			archivoTO.setDescripcion("Carga masiva del usuario:"+ usuario.getNombre()+ ", id:"+ usuario.getPersona().getIdPersona());
			archivoTO.setIdUsuario(usuario.getPersona()	.getIdPersona());
			archivoTO.setNombreArchivo(archivoFileName);
			archivoTO.setTipoArchivo("xml");
			PlSql plSql = inscripcionService.insertArchivo(archivoTO);
			archivoTO.setIdArchivo(plSql.getResIntPl());
	
			if (archivoTO.getIdArchivo().intValue() == 0) {
				System.out.println("El archivo no se pudo guardar en el sistema.");
			}
		}catch (Exception e) {
			System.out.println(e.getMessage() + "-" + e.getCause());
			e.printStackTrace();
		}
		return archivoTO;
	}
	
	private static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		long length = file.length();
		byte[] bytes = new byte[(int) length];
		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}
		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "
					+ file.getName());
		}
		// Close the input stream and return bytes
		is.close();
		return bytes;
	}
	
	private static String getSha1FromFile(File in) {
		String regresa = null;
		try {
			regresa = getDigest(getBytesFromFile(in));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return regresa;
	}
	
	private static String getDigest(byte[] attributes) {
		String digestivo = null;
		if (attributes == null) {
			return null;
		}
		try {
			java.security.MessageDigest messageDigest = java.security.MessageDigest
					.getInstance("SHA-1");
			byte[] digest = messageDigest.digest(attributes);
			digestivo = new String(
					org.apache.commons.codec.binary.Base64.encodeBase64(digest));
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		return digestivo;
	}

	public ArchivoTO getArchivoTO() {
		return archivoTO;
	}

	public void setArchivoTO(ArchivoTO archivoTO) {
		this.archivoTO = archivoTO;
	}

	public InscripcionService getInscripcionService() {
		return inscripcionService;
	}

	public void setInscripcionService(InscripcionService inscripcionService) {
		this.inscripcionService = inscripcionService;
	}

	public CargaMasivaProcess getMasivaProcess() {
		return masivaProcess;
	}

	public void setMasivaProcess(CargaMasivaProcess masivaProcess) {
		this.masivaProcess = masivaProcess;
	}

	public MasivaDAO getMasivaDAO() {
		return masivaDAO;
	}

	public void setMasivaDAO(MasivaDAO masivaDAO) {
		this.masivaDAO = masivaDAO;
	}
}
