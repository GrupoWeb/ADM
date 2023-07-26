package mx.gob.se.rug.masiva.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import mx.gob.se.rug.acreedores.service.AcreedoresService;
import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.exception.InfrastructureException;
import mx.gob.se.rug.inscripcion.dao.AltaParteDAO;
import mx.gob.se.rug.inscripcion.dao.FirmaMasivaDAO;
import mx.gob.se.rug.inscripcion.service.impl.InscripcionServiceImpl;
import mx.gob.se.rug.inscripcion.to.AltaParteTO;
import mx.gob.se.rug.inscripcion.to.FirmaMasivaTO;
import mx.gob.se.rug.inscripcion.to.OtorganteTO;
import mx.gob.se.rug.juez.service.JuezService;
import mx.gob.se.rug.masiva.dao.MasivaDAO;
import mx.gob.se.rug.masiva.exception.CargaMasivaException;
import mx.gob.se.rug.masiva.exception.GarantiaRepetidaException;
import mx.gob.se.rug.masiva.exception.NoDataFound;
import mx.gob.se.rug.masiva.exception.XSDException;
import mx.gob.se.rug.masiva.resultado.to.CargaMasivaResultado;
import mx.gob.se.rug.masiva.resultado.to.ResultadoCargaMasiva;
import mx.gob.se.rug.masiva.resultado.to.TramiteRes;
import mx.gob.se.rug.masiva.service.MasivaService;
import mx.gob.se.rug.masiva.to.AcreedorAdicional;
import mx.gob.se.rug.masiva.to.ArchivoTO;
import mx.gob.se.rug.masiva.to.CargaMasivaProcess;
import mx.gob.se.rug.masiva.to.ControlError;
import mx.gob.se.rug.masiva.to.Deudor;
import mx.gob.se.rug.masiva.to.Modificacion;
import mx.gob.se.rug.masiva.to.Otorgante;
import mx.gob.se.rug.masiva.to.TipoBienMueble;
import mx.gob.se.rug.masiva.to.Tramite;
import mx.gob.se.rug.masiva.to.string.CargaMasivaPreProcesed;
import mx.gob.se.rug.partes.dao.FolioElectronicoDAO;
import mx.gob.se.rug.to.PersonaTO;
import mx.gob.se.rug.to.PlSql;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class MasivaServiceImpl implements MasivaService {

	private AcreedoresService acreedoresService;
	
	private List<Integer> listaTramites;
	private List<Integer> listaTramitesErrores;
	private ResultadoCargaMasiva resultado;
	private List<mx.gob.se.rug.masiva.resultado.to.TramiteRes> inscripcionesErroneas;
	private String errorArchivo;
	
	private MasivaDAO masivaDAO = new MasivaDAO();
	

	
	
	
	
	public String getErrorArchivo() {
		return errorArchivo;
	}



	public void setErrorArchivo(String errorArchivo) {
		this.errorArchivo = errorArchivo;
	}



	public void setListaTramites(List<Integer> listaTramites) {
		this.listaTramites = listaTramites;
	}



	public void setListaTramitesErrores(List<Integer> listaTramitesErrores) {
		this.listaTramitesErrores = listaTramitesErrores;
	}



	public void setResultado(ResultadoCargaMasiva resultado) {
		this.resultado = resultado;
	}



	public void setInscripcionesErroneas(
			List<mx.gob.se.rug.masiva.resultado.to.TramiteRes> inscripcionesErroneas) {
		this.inscripcionesErroneas = inscripcionesErroneas;
	}



	public List<Integer> getListaTramites() {
		return listaTramites;
	}



	public List<Integer> getListaTramitesErrores() {
		return listaTramitesErrores;
	}


	public ResultadoCargaMasiva getResultado() {
		return resultado;
	}



	public List<mx.gob.se.rug.masiva.resultado.to.TramiteRes> getInscripcionesErroneas() {
		return inscripcionesErroneas;
	}


	public void setAcreedoresService(AcreedoresService acreedoresService) {
		this.acreedoresService = acreedoresService;
	}

	@Override
	public byte[] convertXMLObjetc(Object obj) throws JAXBException,
			FileNotFoundException, UnsupportedEncodingException {
		JAXBContext context = JAXBContext.newInstance(obj.getClass());
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
		StringWriter stringWriter = new StringWriter();
		marshaller.marshal(obj, stringWriter);
		return stringWriter.toString().getBytes();
	}

	@Override
	public String getDigest(byte[] attributes) {
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

	@Override
	public String getSha1FromFile(File in) {
		String regresa = null;
		try {
			regresa = getDigest(getBytesFromFile(in));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return regresa;
	}

	@Override
	public byte[] getBytesFromFile(File file) throws IOException {
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

	@Override
	public String listToString(List<Integer> lista) {
		String regresa = "";
		Iterator<Integer> it = lista.iterator();
		if (it.hasNext()) {
			regresa += it.next();
		}
		while (it.hasNext()) {
			regresa += ("|" + it.next());
		}
		return regresa;
	}

	@Override
	public String getSha1FromFile(byte[] in) {
		return getDigest(in);
	}

	@Override
	public boolean isAutoridad(Integer id) {
		return acreedoresService.isAutoridad(id);
	}

	@Override
	public boolean matchXmltoXsd(File xml, HttpServletRequest request) throws XSDException {
		MyLogger.Logger.log(Level.INFO, "-- start matchXmltoXsd--");
		boolean regresa = false;
		SchemaFactory factory = SchemaFactory
				.newInstance("http://www.w3.org/2001/XMLSchema");
		try {
			@SuppressWarnings("deprecation")
			File fileXsd = new File(request.getRealPath("resources")
					+ "/xsd/carga-masiva-simple.xsd");
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
		
			ex.printStackTrace();
			throw new XSDException(sb.toString());
		} catch (SAXException ex) {
			ex.printStackTrace();
			throw new XSDException(ex.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new XSDException(e.getLocalizedMessage());
		}catch (Exception e) {
			e.printStackTrace();
			throw new XSDException(e.getLocalizedMessage());
		}
		return regresa;
	}


	
	@Override
	public String[] stack2string(Exception e) {
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String mensajeError ="";
			if (sw.toString().contains("UTF-8")) {
				mensajeError="El archivo que estas subiendo tiene caracteres no validos para el encoding: UTF-8 favor de cambiar a : ISO-8859-1, e intentarlo nuevamente";
			}
			if (sw.toString().length() > 400) {
				return new String[]{mensajeError,sw.toString().substring(0, 399)};
			} else {
				return  new String[]{mensajeError,sw.toString()};
			}
		} catch (Exception e2) {
			return new String[]{"bad stack2string"};
		}
	}
	
	
	//Modificacion
	
	public ControlError agregaModificacion(Modificacion modificacion,
			Tramite tramite, UsuarioTO usuarioTO) {
		ControlError regresa = null;
		TramiteRes inscripcionRes = new TramiteRes();
		
			regresa = verificaDatosModificacion(modificacion,tramite,inscripcionRes);
			if (regresa == null) {
				
				PlSql plSql = masivaDAO.executeAltaParteTramIncRast(tramite);
				if (plSql.getIntPl().intValue() == 0) {
					tramite.setIdTramite(plSql.getResIntPl());
					regresa = verificaDatosModificacion(modificacion, tramite,inscripcionRes);
					regresa = agregarDeudores(modificacion.getPartes().getDeudor(), tramite);
					if (regresa == null) {
						//plSql = //masivaDAO.executeModificaGarantia(modificacion,tramite);
						if (usuarioTO.getPersona().getPerfil().equalsIgnoreCase("AUTORIDAD")){
							JuezService juezService = new JuezService();
							plSql= juezService.insertAutoridadInstruye(Integer.valueOf(tramite.getIdTramite()), modificacion.getPersonaSolicitaAutoridadInstruyeAsiento().getContenidoResolucion());
						}
						if (plSql.getIntPl().intValue() == 0) {
							plSql = masivaDAO.executeAltaBitacoraTramite(tramite);
							if (plSql.getIntPl().intValue() == 0) {
								listaTramites.add(tramite.getIdTramite());
								inscripcionRes
										.setMensajeError("La modificacion fue procesada correctamente");
								inscripcionRes.setClaveRastreo(modificacion
										.getIdentificadorGarantia().getClaveRastreo());
								inscripcionRes.setCodigoError("0");
								resultado.getTramites().getTramite()
										.add(inscripcionRes);
								regresa = null;
							} else {
								listaTramitesErrores.add(tramite.getIdTramite());
								inscripcionRes.setMensajeError(plSql.getStrPl());
								inscripcionRes.setClaveRastreo(modificacion
										.getIdentificadorGarantia().getClaveRastreo());
								inscripcionRes.setCodigoError(plSql.getIntPl()
										.toString());
								inscripcionesErroneas.add(inscripcionRes);
								resultado.getTramites().getTramite()
										.add(inscripcionRes);
							}
						
					}else {
						listaTramitesErrores.add(plSql.getIntPl().intValue());
						inscripcionRes.setMensajeError(plSql.getStrPl());
						inscripcionRes.setClaveRastreo(modificacion.getIdentificadorGarantia()
								.getClaveRastreo());
						inscripcionRes.setCodigoError(plSql.getIntPl().toString());
						inscripcionesErroneas.add(inscripcionRes);
						resultado.getTramites().getTramite().add(inscripcionRes);
					}
				} else {
					listaTramitesErrores.add(plSql.getResIntPl().intValue());
					inscripcionRes.setMensajeError(plSql.getStrPl());
					inscripcionRes.setClaveRastreo(modificacion.getIdentificadorGarantia()
							.getClaveRastreo());
					inscripcionRes.setCodigoError(plSql.getIntPl().toString());
					inscripcionesErroneas.add(inscripcionRes);
					resultado.getTramites().getTramite().add(inscripcionRes);
				}
				
				//
			}else {
				listaTramitesErrores.add(plSql.getResIntPl().intValue());
				inscripcionRes.setMensajeError(plSql.getStrPl());
				inscripcionRes.setClaveRastreo(modificacion.getIdentificadorGarantia()
						.getClaveRastreo());
				inscripcionRes.setCodigoError(plSql.getIntPl().toString());
				inscripcionesErroneas.add(inscripcionRes);
				resultado.getTramites().getTramite().add(inscripcionRes);
			}
			if (regresa != null) {
				regresa.setClaveRastreo(tramite.getClaveRastreo());
			}
		}else{
			listaTramitesErrores.add(0 );
			TramiteRes modificacionRes = new TramiteRes();
			modificacionRes.setMensajeError("No contiene Garantia a modificar");
			modificacionRes.setClaveRastreo(modificacion.getIdentificadorGarantia().getClaveRastreo());
			modificacionRes.setCodigoError("40");
			inscripcionesErroneas.add(modificacionRes);
			resultado.getTramites().getTramite().add(modificacionRes);
		}
		return regresa;
	}
	

	
	private ControlError verificaDatosModificacion(Modificacion modificacion,
			Tramite tramite,
			mx.gob.se.rug.masiva.resultado.to.TramiteRes inscripcion) {
		ControlError regresa = null;
		int deudorSize=modificacion.getPartes().getDeudor().size();
		try {
			/*if(modificacion.getEliminarPartesModificacion()!=null&&modificacion.getEliminarPartesModificacion().isEliminaDeudores() == true){
				
				if(!(tramite.getIdTramite()==null)){
					PlSql plSql = masivaDAO.bajaParteLogicaMasiva(tramite.getIdUsuario(), 2, tramite.getIdTramite());
					if (plSql.getIntPl().intValue() != 0) {
						setErrorArchivo("El archivo no se pudo modificar");
						regresa = new ControlError();
						regresa.setPlSql(plSql);
					}
				}	
			}*/
			if(deudorSize > 0) {
				int x = 1;
				for (Deudor deudor : modificacion.getPartes().getDeudor()) {
					regresa = verificarDeudor(deudor, tramite, inscripcion, x++);
					if (regresa != null) {
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return regresa;
	}
	
	
	private ControlError verificarDeudor(Deudor deudor, Tramite tramite,
			mx.gob.se.rug.masiva.resultado.to.TramiteRes inscripcion,
			Integer numeroDeudor) {
		ControlError regresa = null;
		inscripcion = new mx.gob.se.rug.masiva.resultado.to.TramiteRes();
		if (deudor.getTipoPersona() == null
				&& deudor.getTipoPersona().trim().equals("")) {
			listaTramitesErrores.add(0);

			regresa = new ControlError();
			inscripcion.setMensajeError("El deudor: " + numeroDeudor
					+ ", no cuenta con tipo de persona ");
			inscripcion.setClaveRastreo(tramite.getClaveRastreo());
			inscripcion.setCodigoError("301");
		} else if (deudor.getTipoPersona().trim().equals("PF")
				|| deudor.getTipoPersona().trim().equals("PM")) {
			try {
					if (deudor.getTipoPersona().trim().equals("PF")) {
						try {
							if (deudor.getNombre() == null
									|| deudor.getNombre().trim().equals("")) {
								regresa = new ControlError();
								inscripcion
										.setMensajeError("El campo nombre del deudor: "
												+ numeroDeudor + ", esta vacio");
								inscripcion.setClaveRastreo(tramite
										.getClaveRastreo());
								inscripcion.setCodigoError("307");
							}
						} catch (NullPointerException e) {
							regresa = new ControlError();
							inscripcion.setMensajeError("El deudor: "
									+ numeroDeudor + ", no tiene nombre ");
							inscripcion.setClaveRastreo(tramite
									.getClaveRastreo());
							inscripcion.setCodigoError("307");
						}
						try {
							if (deudor.getApellidoPaterno() == null
									|| deudor.getApellidoPaterno().trim()
											.equals("")) {
								regresa = new ControlError();
								inscripcion
										.setMensajeError("El campo apellido paterno del deudor: "
												+ numeroDeudor + ", esta vacio");
								inscripcion.setClaveRastreo(tramite
										.getClaveRastreo());
								inscripcion.setCodigoError("308");
							}
						} catch (NullPointerException e) {
							regresa = new ControlError();
							inscripcion.setMensajeError("El deudor: "
									+ numeroDeudor
									+ ", no tiene apellido paterno ");
							inscripcion.setClaveRastreo(tramite
									.getClaveRastreo());
							inscripcion.setCodigoError("308");
						}
					} else {
						try {
							if (deudor.getDenominacionRazonSocial() == null
									|| deudor.getDenominacionRazonSocial()
											.trim().equals("")) {
								listaTramitesErrores.add(0);
								regresa = new ControlError();
								inscripcion
										.setMensajeError("El campo denominacion o razon social del deudor: "
												+ numeroDeudor + ", esta vacio");
								inscripcion.setClaveRastreo(tramite
										.getClaveRastreo());
								inscripcion.setCodigoError("306");
							}
						} catch (NullPointerException e) {
							regresa = new ControlError();
							inscripcion
									.setMensajeError("El deudor: "+ numeroDeudor+ ", no tiene denominacion o razon social ");
							inscripcion.setClaveRastreo(tramite
									.getClaveRastreo());
							inscripcion.setCodigoError("305");
						}

					}
			
			} catch (NullPointerException e) {
				regresa = new ControlError();
				inscripcion.setMensajeError("El deudor: " + numeroDeudor
						+ ", no cuenta con id nacionalidad ");
				inscripcion.setClaveRastreo(tramite.getClaveRastreo());
				inscripcion.setCodigoError("303");

			}
		} else {
			regresa = new ControlError();
			inscripcion.setMensajeError("El deudor: " + numeroDeudor
					+ ", no tiene un tipo de persona valido ");
			inscripcion.setClaveRastreo(tramite.getClaveRastreo());
			inscripcion.setCodigoError("302");
		}

		if (regresa != null) {
			resultado.getTramites().getTramite().add(inscripcion);
			regresa = new ControlError();
			PlSql plSql = new PlSql();
			plSql.setResStrPl(inscripcion.getCodigoError());
			plSql.setStrPl(inscripcion.getCodigoError());
			regresa.setPlSql(plSql);
			regresa.setIdInscripcion(tramite.getConsecutivo());
		}
		return regresa;
	}
	


	private ControlError agregarDeudores(List<Deudor> deudores, Tramite tramite) {
		ControlError regresa = null;
		try {
			if (deudores.size() > 0) {
				Iterator<Deudor> itDe = deudores.iterator();
				PlSql plSql = new PlSql();
				plSql.setIntPl(0);
				AltaParteDAO altaParteDAO = new AltaParteDAO();
				while (plSql.getIntPl().intValue() == 0 && itDe.hasNext()) {
					Deudor deudor = itDe.next();
					AltaParteTO altaParteTO = new AltaParteTO();
					altaParteTO.setIdParte(2);
					altaParteTO.setIdTramite(tramite.getIdTramite());
					altaParteTO.setRazonSocial(deudor.getDenominacionRazonSocial());
					altaParteTO.setRfc(deudor.getRfc());
					altaParteTO.setTipoPersona(deudor.getTipoPersona());
					altaParteTO.setNombre(deudor.getNombre());
					altaParteTO.setApellidoPaterno(deudor.getApellidoPaterno());
					altaParteTO.setApellidoMaterno(deudor.getApellidoMaterno());
					altaParteTO.setHayDomicilio("F");
					altaParteTO.setIdNacionalidad(deudor.getIdNacionalidad().intValue());
					altaParteTO.setIdPersona(Integer.valueOf(tramite
							.getIdUsuario()));
					plSql = altaParteDAO.insert(altaParteTO);
				}
				if (plSql.getIntPl().intValue() != 0) {
					regresa = new ControlError();
					regresa.setPlSql(plSql);
					regresa.setIdInscripcion(tramite.getConsecutivo());
				}

			}

		} catch (NumberFormatException e) {
			regresa = new ControlError();
			PlSql plSql = new PlSql();
			plSql.setIntPl(999);
			plSql.setResStrPl("Error al tratar de convertir a entero:"
					+ e.getMessage() + ", " + e.getCause());
			plSql.setStrPl("Error al tratar de convertir a entero:"
					+ e.getMessage() + ", " + e.getCause());
			regresa.setPlSql(plSql);
			regresa.setIdInscripcion(tramite.getConsecutivo());
			e.printStackTrace();
		} catch (Exception e) {
			regresa = new ControlError();
			PlSql plSql = new PlSql();
			plSql.setResStrPl(e.getMessage() + ", " + e.getCause());
			plSql.setStrPl(e.getMessage() + ", " + e.getCause());
			regresa.setPlSql(plSql);
			regresa.setIdInscripcion(tramite.getConsecutivo());
			e.printStackTrace();
		}

		return regresa;

	}
	public PlSql agregaOtorganteInd(Otorgante otorgante, Tramite tramite) {
		PlSql plSql = new PlSql();
		try {
			AltaParteDAO altaParteDAO = new AltaParteDAO();
			if (otorgante.getTipoPersona().trim().equals("PF")
					&& otorgante.getFolioElectronico() != null
					&& !otorgante.getFolioElectronico().trim().equals("")) {
				OtorganteTO otorganteTO = altaParteDAO
						.getOtorganteFisicoByFolioElectronico(otorgante
								.getFolioElectronico());
				if (otorganteTO != null) {
					if (altaParteDAO.relParte(otorganteTO.getIdOtorgante(),
							tramite.getIdTramite(), 1, null)) {
						plSql.setResStrPl("El otorgante fue generado exitosamente");
						plSql.setStrPl("El otorgante fue generado exitosamente");
						plSql.setIntPl(0);
						plSql.setResIntPl(0);

					} else {
						plSql.setResStrPl("No se pudo relacionar al otorgante");
						plSql.setStrPl("No se pudo relacionar al otorgante");
						plSql.setIntPl(591);
						plSql.setResIntPl(887);
					}
				} else {
					plSql.setResStrPl("El Folio Electronico no existe");
					plSql.setStrPl("El Folio Electronico no existe");
					plSql.setIntPl(592);
					plSql.setResIntPl(887);

				}
			} else {
				AltaParteTO altaParteTO = new AltaParteTO();
				altaParteTO.setIdParte(1);
				altaParteTO.setIdTramite(tramite.getIdTramite());
				altaParteTO.setFolioMercantil(otorgante.getFolioElectronico());
				altaParteTO.setCurp(otorgante.getCurp());
				altaParteTO.setNombre(otorgante.getNombre());
				altaParteTO.setApellidoMaterno(otorgante.getApellidoMaterno());
				altaParteTO.setApellidoPaterno(otorgante.getApellidoPaterno());
				altaParteTO.setRazonSocial(otorgante.getDenominacionRazonSocial());
				altaParteTO.setRfc(otorgante.getRfc());
				altaParteTO.setIdNacionalidad(otorgante.getIdNacionalidad().intValue());
				altaParteTO.setTipoPersona(otorgante.getTipoPersona());
				altaParteTO.setHayDomicilio("F");
				altaParteTO.setIdUsuario(tramite.getIdUsuario());
				altaParteTO.setIdPersona(tramite.getIdUsuario());
				if (altaParteTO.getTipoPersona().trim().equals("PM")) {
					System.out.println("es una persona moral");
					if (altaParteTO.getFolioMercantil() == null
							|| altaParteTO.getFolioMercantil().trim()
									.equals("")) {
						plSql.setResStrPl("Error en el Otorgante:  El campo folio electrónico es obligatorio.");
						plSql.setStrPl("Error en el Otorgante:  El campo folio electrónico es obligatorio.");
						plSql.setIntPl(2);
					} else {
						plSql = altaParteDAO.insert(altaParteTO);
					}

				} else {
					plSql = altaParteDAO.insert(altaParteTO);
					if (plSql.getIntPl().intValue() == 0) {		
						FolioElectronicoDAO folioElectronicoDAO = new FolioElectronicoDAO();
						String strMsj = folioElectronicoDAO
								.creaFolioElectronico(plSql.getResIntPl()
										.intValue());
						
							if (strMsj != null) {
								mx.gob.se.rug.masiva.resultado.to.Otorgante otorgante2 = new mx.gob.se.rug.masiva.resultado.to.Otorgante();
								otorgante2.setCurp(altaParteTO.getCurp());
								otorgante2.setFolioElectronico(strMsj);
								System.out
										.println("--se genero el siguiente folio electronico de persona fisica -- "
												+ strMsj);
								otorgante2.setNombreCompleto(altaParteTO
										.getNombre()
										+ " "
										+ altaParteTO.getApellidoPaterno()
										+ " "
										+ altaParteTO.getApellidoMaterno());
							} else {
								plSql.setIntPl(501);
								plSql.setResStrPl("Error al tratar de generar folio del RUG, reportarlo al area de sistemas");
								plSql.setStrPl("Error al tratar de generar folio del RUG, reportarlo al area de sistemas");
							}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			plSql.setIntPl(999);
			plSql.setStrPl("Sucedio un error en el alta del otorgante");
		}

		return plSql;

	}
	// agregar otorgantes
	public PlSql agregaOtorgante(List<Otorgante> otorgantes, Tramite tramite) {
		PlSql regresa = new PlSql();
		for (Otorgante otorgante : otorgantes) {
			regresa = agregaOtorganteInd(otorgante, tramite);
			if (regresa != null) {
				break;
			}
		}

		return regresa;

	}
	public void validaGarantia(Integer idGarantia, Set< Integer> idGarantiasInXML) throws GarantiaRepetidaException, NoDataFound{
		
		validaGarantia(idGarantia.toString(), idGarantiasInXML);
	}
	
	public void validaGarantia(String idGarantia, Set< Integer> idGarantiasInXML) throws GarantiaRepetidaException, NoDataFound{
		try{
			isGarantiaRepetida(new Integer(idGarantia), idGarantiasInXML);
		}catch( NumberFormatException exception){
			throw new NoDataFound("idGarantia");
		}
	}
	public void isGarantiaRepetida(Integer idGarantia, Set< Integer> idGarantiasInXML) throws GarantiaRepetidaException{
		if(idGarantiasInXML.contains(idGarantia)){
			throw new GarantiaRepetidaException(Constants.CODE_EXCEPTION_GARANTIA_REPETIDA, Constants.MESASAGE_EXCEPTION_GARANTIA_REPETIDA);
		}else{
			idGarantiasInXML.add(idGarantia);
		}
	}
	
	public void changeAcreedorGarantia(Integer idAcreedorNuevo,Integer idAcreedorViejo,Integer idUsuario,Integer idTramiteTemp) throws CargaMasivaException{
		if(idAcreedorNuevo>0){
			if(idAcreedorNuevo.intValue()!=idAcreedorViejo.intValue()){
			masivaDAO.verificaIdAcreedor(idUsuario, idAcreedorNuevo);
			masivaDAO.actualizaAcreedor(idAcreedorViejo, idAcreedorNuevo, idTramiteTemp);
			}else{
				throw new CargaMasivaException(102);
			}
		}else{
			throw new CargaMasivaException(96);
		}
	}
	
	public Integer agregaTipoBien(List<TipoBienMueble> bienMuebles,Tramite tramite) throws InfrastructureException, CargaMasivaException{
		Integer idRelacionBien=null;
		if(bienMuebles!=null&&bienMuebles.size()>0){
			for (TipoBienMueble tipoBienMueble : bienMuebles) {
				masivaDAO.validaCatalogo(10, tipoBienMueble.getId().intValue());
			}
			//valida no repetido
			Set<Integer> idTipoMueble =  new HashSet<Integer>();
			try{
				for (TipoBienMueble tipoBienMueble : bienMuebles) {
					if(idTipoMueble.contains(tipoBienMueble.getId().intValue())){
						throw new CargaMasivaException(81); 
					}else{
						idTipoMueble.add(tipoBienMueble.getId().intValue());
					}
				}
			}catch (Exception e) {
				throw new CargaMasivaException(81); 
			}
			
			//inserta las garantias
			 idRelacionBien= masivaDAO.getNextRelTipoBien();
			for (TipoBienMueble tipoBienMueble : bienMuebles) {
				masivaDAO.insertaTipoBienMueble(tramite.getIdGarantiaPendiente(), tipoBienMueble.getId().intValue(), idRelacionBien);
			}
		}
		return idRelacionBien;
	}
	
	public void generaArchivoResumen(ResultadoCargaMasiva resultado ,UsuarioTO usuarioTO ,ArchivoTO ArchivoViejo,CargaMasivaProcess masivaProcess  ) throws InfrastructureException{
		
		if(usuarioTO==null){
			usuarioTO= new UsuarioTO();
			PersonaTO personaTO = new PersonaTO();
			personaTO.setIdPersona(masivaProcess.getIdUsuario());
			usuarioTO.setPersona(personaTO);
		}
		InscripcionServiceImpl inscripcionService= new InscripcionServiceImpl();
		byte[] archivoResumenBytes;
		try {
			archivoResumenBytes = convertXMLObjetc(resultado);
	
		
		ArchivoTO archivoResumen = new ArchivoTO();
		archivoResumen.setAlgoritoHash(getSha1FromFile(archivoResumenBytes));
		archivoResumen.setArchivo(archivoResumenBytes);
		archivoResumen.setDescripcion("Archivo de Resumen de carga masiva del usuario : "+
						" con el id :"
						+ usuarioTO.getPersona().getIdPersona());
		
		archivoResumen.setIdUsuario(usuarioTO.getPersona().getIdPersona());
		archivoResumen.setNombreArchivo(ArchivoViejo.getNombreArchivo()+" Resumem");
		archivoResumen.setTipoArchivo("xml");
		
		
		archivoResumen.setIdArchivo(inscripcionService.insertArchivo(archivoResumen).getResIntPl());
		
		masivaProcess.setIdArchivo(ArchivoViejo.getIdArchivo());
		masivaProcess.setIdArchivoResumen(archivoResumen.getIdArchivo());
		masivaDAO.actualizaProcessCargaIdResumen(masivaProcess);
		
		Integer correctos=Integer.valueOf(resultado.getResumen().getCorrectos());
		Integer InCorrectos=Integer.valueOf(resultado.getResumen().getErroneos());
				
		//Buenas Malas
		masivaDAO.actualizaDatosCarga(correctos,InCorrectos,masivaProcess.getIdArchivo());
		
		} catch (Exception e) {
			e.printStackTrace();
			MyLogger.Logger.log(Level.SEVERE, "Error al crear la firma Archivo de Resumen", e);
			throw new InfrastructureException("Error al crear la firma Archivo de Resumen",e);
		}
		
	
		
	}
	
	public CargaMasivaResultado generaFirmaMasiva(CargaMasivaResultado  cargaMasivaResultado,CargaMasivaPreProcesed cargaMasivaPreProcesed,UsuarioTO usuario, ArchivoTO ArchivoViejo, AcreedorTO acreedorTO ) throws InfrastructureException{
		Integer idFirmaMasiva= null; 
		try {
			InscripcionServiceImpl inscripcionService= new InscripcionServiceImpl();
			CargaMasivaProcess masivaProcess = new CargaMasivaProcess();
			Integer idArchivoCargaMasiva=ArchivoViejo.getIdArchivo();
			
			List<TramiteRes> incorrectosFiltro = cargaMasivaPreProcesed.getTramiteIncorrectos();
			Integer nIncorrectos = Integer.valueOf(cargaMasivaResultado.getResultadoCargaMasiva().getResumen().getErroneos())+incorrectosFiltro.size();
			Integer nTramites = cargaMasivaPreProcesed.getTotalTramites();
			cargaMasivaResultado.getResultadoCargaMasiva().getResumen().setErroneos(nIncorrectos.toString());
			cargaMasivaResultado.getResultadoCargaMasiva().getResumen().setNumeroRegistros(nTramites.toString());
			
			//Resumen
			
			generaArchivoResumen(cargaMasivaResultado.getResultadoCargaMasiva(), usuario, ArchivoViejo,masivaProcess);
			
			cargaMasivaResultado.setIdArchivoResumen(masivaProcess.getIdArchivoResumen());
			
			// Generamos nuevo archivo para firmaElectronica
			if (cargaMasivaResultado.getTramitesIncorrectos().size() > 0) {
				if (cargaMasivaResultado.getTramitesCorrectos().size() > 0) {
					System.out.println("### se genera un nuevo archivo XML");
					byte[] bytes = convertXMLObjetc(cargaMasivaPreProcesed.getCargaMasiva());

					ArchivoTO archivoNuevoCarga = new ArchivoTO();
					archivoNuevoCarga.setAlgoritoHash(getSha1FromFile(bytes));
					archivoNuevoCarga.setArchivo(bytes);
					archivoNuevoCarga.setDescripcion("Archivo nuevo de carga masiva del usuario : "
							+ usuario.getNombre()
							+ ", con el id :"
							+ usuario.getPersona().getIdPersona()
							+ ", resultado de una carga que contenia archivos incorrectos");
					archivoNuevoCarga.setIdUsuario(usuario.getPersona().getIdPersona());
					archivoNuevoCarga.setNombreArchivo(ArchivoViejo.getNombreArchivo()+" Nuevo");
					archivoNuevoCarga.setTipoArchivo("xml");
					
					archivoNuevoCarga.setIdArchivo( inscripcionService.insertArchivo(archivoNuevoCarga).getResIntPl());
					idArchivoCargaMasiva= archivoNuevoCarga.getIdArchivo();
				}
			}
			
			
			cargaMasivaResultado.setIdArchivoFirma(idArchivoCargaMasiva);
			
			if (cargaMasivaResultado.getTramitesCorrectos().size() > 0) {
				masivaProcess.setIdArchivoFirma(idArchivoCargaMasiva);
				masivaDAO.actualizaProcessCargaIdFirma(masivaProcess);
				ArchivoViejo.setIdArchivo(idArchivoCargaMasiva);
				FirmaMasivaTO firmaMasivaTO = new FirmaMasivaTO();
				firmaMasivaTO.setIdUsuario(usuario.getPersona().getIdPersona());
				firmaMasivaTO.setIdArchivo(idArchivoCargaMasiva);
				firmaMasivaTO.setIdAcreedor(new Integer(acreedorTO.getIdAcreedor()));
				
				firmaMasivaTO.setTramites(listTramites(cargaMasivaResultado.getTramitesCorrectos()));
				
				FirmaMasivaDAO firmaDao = new FirmaMasivaDAO();
				idFirmaMasiva = firmaDao.crearFirmaMasiva(firmaMasivaTO);
				
				if (idFirmaMasiva != 0) {
					cargaMasivaResultado.setIdFirmaMasiva(idFirmaMasiva);
				}else{
					throw new InfrastructureException("Error al crear la firma Masiva");
				}
			}
			
		}catch (Exception e) {
			MyLogger.Logger.log(Level.SEVERE, "Error al crear la firma Masiva", e);
			throw new InfrastructureException("Error al crear la firma Masiva",e);
		}
		
		return cargaMasivaResultado;
	}
	
	private String listTramites(List<Tramite> tramites){
		String tramitesLista="";
		for (Tramite tramite : tramites) {
			tramitesLista=tramitesLista+tramite.getIdTramite()+"|";
		}
		
		return tramitesLista.substring(0, tramitesLista.length()-1);
		
	}

	public void addOtorgantes(List<Otorgante> otorgantes,UsuarioTO usuario, Tramite tramite) throws CargaMasivaException, InfrastructureException{
		AltaParteDAO altaParteDAO= new AltaParteDAO();
		AltaParteTO altaParte;
		for (Otorgante otorgante : otorgantes) {
			
			if(otorgante.getFolioElectronico()!=null&&otorgante.getTipoPersona().equalsIgnoreCase("PF")){//otorgante por folio 
				OtorganteTO otorganteTO = altaParteDAO.getOtorganteFisicoByFolio(otorgante.getFolioElectronico());
				altaParteDAO.relacionaParte(otorganteTO.getIdOtorgante(),tramite.getIdTramite(), 1, null);
			}else{//Otorgante Nuevo
				altaParte= new AltaParteTO();
				
				altaParte.setIdParte(new Integer(1));
				altaParte.setIdUsuario(usuario.getPersona().getIdPersona());
				altaParte.setIdTramite(tramite.getIdTramite());
				altaParte.setApellidoMaterno(otorgante.getApellidoMaterno());
				altaParte.setApellidoPaterno(otorgante.getApellidoPaterno());
				altaParte.setCurp(otorgante.getCurp());
				altaParte.setRfc(otorgante.getRfc());
				altaParte.setRazonSocial(otorgante.getDenominacionRazonSocial());
				altaParte.setFolioMercantil(otorgante.getFolioElectronico());
				altaParte.setIdNacionalidad(bigIntegerToInteger(otorgante.getIdNacionalidad()));
				altaParte.setNombre(otorgante.getNombre());
				altaParte.setTipoPersona(otorgante.getTipoPersona());
				
				altaParteDAO.excuteAltaParteMasiva(altaParte);
				
				//Folio electronico si es persona Fisica
				if(altaParte.getTipoPersona().equalsIgnoreCase("PF")){
					FolioElectronicoDAO electronicoDAO = new FolioElectronicoDAO();
					electronicoDAO.generaFolioElectronico(altaParte.getIdPersona());
				}
			
			}
		}
		
	}
	
	public void addDeudores(List<Deudor> deudores,UsuarioTO usuario, Tramite tramite) throws CargaMasivaException, InfrastructureException{
		AltaParteDAO altaParteDAO= new AltaParteDAO();
		AltaParteTO altaParte;
		for (Deudor deudor : deudores) {
			
			altaParte= new AltaParteTO();
			
			altaParte.setIdParte(new Integer(2));
			altaParte.setIdUsuario(usuario.getPersona().getIdPersona());
			altaParte.setIdTramite(tramite.getIdTramite());
			
			altaParte.setApellidoMaterno(deudor.getApellidoMaterno());
			altaParte.setApellidoPaterno(deudor.getApellidoPaterno());
			altaParte.setRazonSocial(deudor.getDenominacionRazonSocial());
			altaParte.setIdNacionalidad(bigIntegerToInteger(deudor.getIdNacionalidad()));
			altaParte.setNombre(deudor.getNombre());
			altaParte.setTipoPersona(deudor.getTipoPersona());
			
			altaParteDAO.excuteAltaParteMasiva(altaParte);
			}
		
	}
	public void addAcreedorAdicional(List<AcreedorAdicional> acreedoresAdicionales,UsuarioTO usuario, Tramite tramite) throws CargaMasivaException, InfrastructureException{
		AltaParteDAO altaParteDAO= new AltaParteDAO();
		AltaParteTO altaParte;
		for (AcreedorAdicional acreedoresAdicional : acreedoresAdicionales) {
			
			altaParte= new AltaParteTO();
			
			altaParte.setIdParte(new Integer(3));
			altaParte.setIdUsuario(usuario.getPersona().getIdPersona());
			altaParte.setIdTramite(tramite.getIdTramite());
			
			altaParte.setApellidoMaterno(acreedoresAdicional.getApellidoMaterno());
			altaParte.setApellidoPaterno(acreedoresAdicional.getApellidoPaterno());
			altaParte.setRazonSocial(acreedoresAdicional.getDenominacionRazonSocial());
			altaParte.setIdNacionalidad(bigIntegerToInteger(acreedoresAdicional.getIdNacionalidad()));
			altaParte.setNombre(acreedoresAdicional.getNombre());
			altaParte.setTipoPersona(acreedoresAdicional.getTipoPersona());
			altaParte.setCalle(acreedoresAdicional.getCalle());
			altaParte.setCorreoElectronico(acreedoresAdicional.getCorreoElectronico());
			altaParte.setDomicilioUno(acreedoresAdicional.getDomicilioExtranjeroUno());
			altaParte.setDomicilioDos(acreedoresAdicional.getDomicilioExtranjeroDos());
			altaParte.setIdColonia(bigIntegerToInteger(acreedoresAdicional.getIdColonia()));
			altaParte.setIdLocalidad(bigIntegerToInteger(acreedoresAdicional.getIdLocalidad()));
			altaParte.setIdNacionalidad(bigIntegerToInteger(acreedoresAdicional.getIdNacionalidad()));
			altaParte.setIdPaisResidencia(bigIntegerToInteger(acreedoresAdicional.getIdPaisResidencia()));
			altaParte.setNumeroExterior(acreedoresAdicional.getNumeroExterior());
			altaParte.setNumeroInterior(acreedoresAdicional.getNumeroInterior());
			altaParte.setPoblacion(acreedoresAdicional.getPoblacion());
			altaParte.setRfc(acreedoresAdicional.getRfc());
			altaParte.setTelefono(acreedoresAdicional.getTelefono());
			altaParte.setExtencion(acreedoresAdicional.getTelefonoExtension());
			altaParte.setZonaPostal(acreedoresAdicional.getZonaPostal());
			
			altaParte.setCurp(acreedoresAdicional.getCurp());
			altaParte.setInscrita(acreedoresAdicional.getInfoInscripcion());
			altaParte.setIdPersona(usuario.getPersona().getIdPersona());
			
			altaParteDAO.excuteAltaParteMasiva(altaParte);
			}
	}
	
	public Integer bigIntegerToInteger(BigInteger bigInteger){
		Integer integer=null; 
		if(bigInteger!=null){
			integer= bigInteger.intValue();
		}
		return integer;
	}
	
	public void verifyAcreedorOfGarantia(Integer idAcreedor ,Integer idGarantia ) throws InfrastructureException, CargaMasivaException{
		Integer idAcreedorOfGarantia = masivaDAO.getIdAcreedorFromGarantia(idGarantia);
		if (idAcreedorOfGarantia.intValue() != idAcreedor.intValue()){
			//El acreedor es diferente al de la garantia
			throw new CargaMasivaException(60);
		}
	}
	
	
}
