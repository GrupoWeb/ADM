package mx.gob.se.rug.masiva.thread;

import java.io.FileNotFoundException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.exception.NoDateInfrastructureException;
import mx.gob.se.rug.garantia.dao.GarantiasDAO;
import mx.gob.se.rug.garantia.to.ActoContratoTO;
import mx.gob.se.rug.garantia.to.GarantiaTO;
import mx.gob.se.rug.garantia.to.ObligacionTO;
import mx.gob.se.rug.inscripcion.dao.AltaParteDAO;
import mx.gob.se.rug.inscripcion.dao.FirmaMasivaDAO;
import mx.gob.se.rug.inscripcion.dao.InscripcionDAO;
import mx.gob.se.rug.inscripcion.service.InscripcionService;
import mx.gob.se.rug.inscripcion.service.impl.InscripcionServiceImpl;
import mx.gob.se.rug.inscripcion.to.AltaParteTO;
import mx.gob.se.rug.inscripcion.to.FirmaMasivaTO;
import mx.gob.se.rug.inscripcion.to.InscripcionTO;
import mx.gob.se.rug.inscripcion.to.OtorganteTO;
import mx.gob.se.rug.masiva.dao.ArchivoDAO;
import mx.gob.se.rug.masiva.dao.MasivaDAO;
import mx.gob.se.rug.masiva.resultado.to.ResultadoCargaMasiva;
import mx.gob.se.rug.masiva.resultado.to.Resumen;
import mx.gob.se.rug.masiva.resultado.to.TramiteRes;
import mx.gob.se.rug.masiva.resultado.to.Tramites;
import mx.gob.se.rug.masiva.service.MasivaService;
import mx.gob.se.rug.masiva.to.AcreedorAdicional;
import mx.gob.se.rug.masiva.to.ArchivoTO;
import mx.gob.se.rug.masiva.to.CargaMasiva;
import mx.gob.se.rug.masiva.to.ControlError;
import mx.gob.se.rug.masiva.to.Creacion;
import mx.gob.se.rug.masiva.to.Deudor;
import mx.gob.se.rug.masiva.to.Garantia;
import mx.gob.se.rug.masiva.to.Inscripcion;
import mx.gob.se.rug.masiva.to.Modificacion;
import mx.gob.se.rug.masiva.to.Obligacion;
import mx.gob.se.rug.masiva.to.Otorgante;
import mx.gob.se.rug.masiva.to.TipoBienMueble;
import mx.gob.se.rug.partes.dao.FolioElectronicoDAO;
import mx.gob.se.rug.to.PlSql;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;
import mx.gob.se.rug.util.to.DateUtilRug;


public class CargaMasivaThread extends Thread{
	
	private ResultadoCargaMasiva resultado;
	private UsuarioTO usuario;
	private CargaMasiva cm;
	private AcreedorTO acreedor;
	private ControlError cerror;
	private Integer idArchivo;
	private Integer idTramite; 
	
	private List<Integer> listaTramites;
	private List<Integer> listaTramitesErroneos;
	
	private MasivaService masivaService;
	
	
	
	public void setMasivaService(MasivaService masivaService) {
		this.masivaService = masivaService;
	}

	public CargaMasivaThread(UsuarioTO usuario, CargaMasiva cm, AcreedorTO acreedor, Integer idArchivo, Integer idTramite){
		this.usuario = usuario;
		this.cm = cm;
		this.acreedor = acreedor;
		this.idArchivo = idArchivo;
		this.listaTramites = new ArrayList<Integer>();
		this.listaTramitesErroneos = new ArrayList<Integer>();
		this.setIdTramite(idTramite);
	}
	
	public void run(){
		resultado = new ResultadoCargaMasiva();
		Tramites inscripciones = new Tramites();
		resultado.setTramites(inscripciones);
		MyLogger.Logger.log(Level.INFO,"--- cargaMasivaInscripcion");
		this.listaTramites = new ArrayList<Integer>();	
		InscripcionService inscripcionService = new InscripcionServiceImpl();		
		resultado = new ResultadoCargaMasiva();
		Resumen resumen = new Resumen();
		mx.gob.se.rug.masiva.to.Tramite tramite;
		resultado.setTramites(inscripciones);
		try {
			try{
				MyLogger.Logger.log(Level.INFO,"ThreadCargaMasiva:::archivo ["+idArchivo+"]::: Inicia el proceso::");
				switch (idTramite.intValue()) {
				case 24:
					if (cm.getInscripcion().size() > 0) {
						int consecutivo = 0;
						for (Inscripcion inscripcion : cm.getInscripcion()){
							cerror = agregaInscripcion(inscripcion,	usuario.getPersona().getIdPersona(), acreedor,	consecutivo++, idArchivo);
							if (cerror != null) {
								MyLogger.Logger.log(Level.WARNING,"ThreadCargaMasiva:::archivo ["+idArchivo+"]::: sucedio un error en la inscripcion ::" + cerror.getClaveRastreo());
							}
						}
					}
					break;
				case 28:
					int consecutivo = 1;
					for (Modificacion modificacion : cm.getModificacion()){
							tramite = new mx.gob.se.rug.masiva.to.Tramite();
							tramite.setIdArchivo(idArchivo);
							tramite.setIdUsuario(usuario.getPersona().getIdPersona());
							tramite.setConsecutivo(consecutivo++);
							tramite.setIdGarantia(modificacion.getIdentificadorGarantia().getIdGarantia().intValue());
							tramite.setIdTipoTramite(7);
							tramite.setIdEstatus(5);
							tramite.setIdAcreedor(acreedor.getIdPersona());
							tramite.setClaveRastreo(modificacion.getIdentificadorGarantia().getClaveRastreo());
							masivaService.setListaTramites(listaTramites);
							masivaService.setResultado(resultado);
							masivaService.setListaTramitesErrores(listaTramitesErroneos);
							cerror = masivaService.agregaModificacion(modificacion, tramite,usuario);
							if (cerror != null) {
								MyLogger.Logger.log(Level.WARNING,"ThreadCargaMasiva:::archivo ["+idArchivo+"]::: sucedio un error en la modificacion ::" + cerror.getClaveRastreo());
							}
							TramiteRes modificacionRes = new TramiteRes();
						
							listaTramitesErroneos.add(0);
							modificacionRes.setMensajeError("El campo id garantia es  obligatorio");
							modificacionRes.setClaveRastreo(modificacion.getIdentificadorGarantia().getClaveRastreo());
							modificacionRes.setCodigoError("40");
							resultado.getTramites().getTramite().add(modificacionRes);
							
							
					}
					break;

				default:
					break;
				}
				
			}catch(Exception e){
				java.util.Date date = new java.util.Date();
				resumen.setMensajeError("Sucedio un error en el sistema la fecha :" + date.toString() +", enviar el XML a soporte. codError["+date.getTime()+"]");
				resultado.setResumen(resumen);
				MyLogger.Logger.log(Level.WARNING,"ThreadCargaMasiva:::archivo ["+idArchivo+"]::: Sucedio un error el proceso:::");
				e.printStackTrace();
			} finally{
				try {
					
					resumen.setNumeroRegistros(cm.getInscripcion().size()+"");
					resumen.setCorrectos(cm.getInscripcion().size()-listaTramitesErroneos.size()+"");
					resumen.setErroneos(listaTramitesErroneos.size()+"");
					
					resultado.setResumen(resumen);
					ArchivoTO archivoRes = new ArchivoTO();
					byte[] bytes2 = convertXMLObjetc(resultado);
					archivoRes.setAlgoritoHash(getSha1FromFile(bytes2));
					archivoRes.setArchivo(bytes2);
					archivoRes.setDescripcion("Archivo nuevo de carga masiva del usuario : "
							+ usuario.getNombre()
							+ ", con el id :"
							+ usuario.getPersona().getIdPersona()+", resultado de una carga que contenia archivos incorrectos");
					archivoRes.setIdUsuario(usuario.getPersona().getIdPersona());
					archivoRes.setNombreArchivo("cmResnuevo");
					archivoRes.setTipoArchivo("xml");
					Integer idRes = inscripcionService.insertArchivo(archivoRes).getResIntPl();
					MyLogger.Logger.log(Level.INFO,"idRes---"+idRes);
					if (listaTramitesErroneos.size()>0){
						MyLogger.Logger.log(Level.WARNING,"#### sucedieron errores en la carga");
						// crear nuevo archivo XML y guardarlo para el tramite de firma
						List<Inscripcion> listaCorrectos = new ArrayList<Inscripcion>();
						for(mx.gob.se.rug.masiva.resultado.to.TramiteRes inscripcionRes : resultado.getTramites().getTramite()){
							if (inscripcionRes.getCodigoError().equals("0")){
								for (Inscripcion ins : cm.getInscripcion()){
									if (ins.getIdentificador().getClaveRastreo().trim().equals(inscripcionRes.getClaveRastreo().trim())){
										listaCorrectos.add(ins);
										break;
									}
								}
							}
						}
						
						if (listaCorrectos.size()>0){
							
							MyLogger.Logger.log(Level.INFO,"### se genera un nuevo archivo XML");
							CargaMasiva carga = new CargaMasiva();
							carga.getInscripcion().addAll(listaCorrectos);
							byte[] bytes = convertXMLObjetc(carga);
							
							ArchivoTO archivoN = new ArchivoTO();
							archivoN.setAlgoritoHash(getSha1FromFile(bytes));
							archivoN.setArchivo(bytes);
							archivoN.setDescripcion("Archivo nuevo de carga masiva del usuario : "
									+ usuario.getNombre()
									+ ", con el id :"
									+ usuario.getPersona().getIdPersona()+", resultado de una carga que contenia archivos incorrectos");
							archivoN.setIdUsuario(usuario.getPersona().getIdPersona());
							archivoN.setNombreArchivo("cmResnuevo");
							archivoN.setTipoArchivo("xml");
							idArchivo = inscripcionService.insertArchivo(archivoN).getResIntPl();
						}
						
					}
					FirmaMasivaTO firmaMasivaTO = new FirmaMasivaTO();
					firmaMasivaTO.setIdUsuario(usuario.getPersona().getIdPersona());
					firmaMasivaTO.setIdArchivo(idArchivo);
					firmaMasivaTO.setIdAcreedor(Integer.valueOf(acreedor.getIdAcreedor()));
					String tramites = listaTramites.size()>0?listToString(listaTramites):listToString(listaTramitesErroneos);
					firmaMasivaTO.setTramites(tramites);
					FirmaMasivaDAO firmaDao = new FirmaMasivaDAO();
					int valor = firmaDao.crearFirmaMasiva(firmaMasivaTO);
					ArchivoTO archivoResult = new ArchivoTO();
					archivoResult.setArchivo(convertXMLObjetc(resultado));
					archivoResult.setIdFirmaMasiva(valor);
					archivoResult.setIdUsuario(usuario.getPersona().getIdPersona());
					ArchivoDAO archivoDAO = new ArchivoDAO();
					archivoDAO.insertArchivoFirmaMasiva(archivoResult);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					MyLogger.Logger.log(Level.SEVERE,"ThreadCargaMasiva:::archivo ["+idArchivo+"]::: sucedio un error faltal:::");
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					MyLogger.Logger.log(Level.SEVERE,"ThreadCargaMasiva:::archivo ["+idArchivo+"]::: sucedio un error faltal:::");
					e.printStackTrace();
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					MyLogger.Logger.log(Level.SEVERE,"ThreadCargaMasiva:::archivo ["+idArchivo+"]::: sucedio un error faltal:::");
					e.printStackTrace();
				}catch(Exception e){
					// TODO Auto-generated catch block
					MyLogger.Logger.log(Level.SEVERE,"ThreadCargaMasiva:::archivo ["+idArchivo+"]::: sucedio un error faltal:::");
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			InscripcionDAO inscripcionDAO = new InscripcionDAO();
			MyLogger.Logger.log(Level.WARNING,"tramites erroneos :" +listaTramitesErroneos.size());
			for (Integer tramiteErroneo:listaTramitesErroneos){
				inscripcionDAO.bajaTramiteIncompleto(tramiteErroneo);
			}
		}	
	}
	
	private ControlError agregaModificacion(Modificacion modificacion,mx.gob.se.rug.masiva.to.Tramite tramite) {
		ControlError regresa = null;
		mx.gob.se.rug.masiva.resultado.to.TramiteRes inscripcionRes = new  mx.gob.se.rug.masiva.resultado.to.TramiteRes();
		regresa = verificaDatosModificacion(modificacion, tramite,inscripcionRes);
		resultado = new ResultadoCargaMasiva();
		resultado.setTramites(new Tramites());
		MasivaDAO masivaDAO = new MasivaDAO();
		if (regresa == null){
			PlSql plSql = masivaDAO.executeAltaParteTramIncRast(tramite);
			if (plSql.getIntPl().intValue() == 0) {
				tramite.setIdTramite( plSql.getResIntPl());
				regresa = agregarDeudores(modificacion.getPartes().getDeudor(), tramite);
				if (regresa == null){
					//plSql = masivaDAO.executeModificaGarantia(modificacion, tramite);
					if (plSql.getIntPl().intValue()==0){
						plSql = masivaDAO.executeAltaBitacoraTramite(tramite);
						if (plSql.getIntPl().intValue()==0){
							regresa = new ControlError();
							regresa.setPlSql(plSql);
							listaTramites.add(tramite.getIdTramite());
							inscripcionRes.setMensajeError(regresa.getPlSql().getStrPl());
							inscripcionRes.setClaveRastreo(modificacion.getIdentificadorGarantia().getClaveRastreo());
							inscripcionRes.setCodigoError(regresa.getPlSql().getIntPl().toString());
							resultado.getTramites().getTramite().add(inscripcionRes);
							regresa = null;
						}else{
							regresa = new ControlError();
							regresa.setPlSql(plSql);
							listaTramitesErroneos.add(tramite.getIdTramite());
							inscripcionRes.setMensajeError(regresa.getPlSql().getStrPl());
							inscripcionRes.setClaveRastreo(modificacion.getIdentificadorGarantia().getClaveRastreo());
							inscripcionRes.setCodigoError(regresa.getPlSql().getIntPl().toString());
							resultado.getTramites().getTramite().add(inscripcionRes);
						}
					}else{
						regresa = new ControlError();
						regresa.setPlSql(plSql);
						listaTramitesErroneos.add(tramite.getIdTramite());
						inscripcionRes.setMensajeError(regresa.getPlSql().getStrPl());
						inscripcionRes.setClaveRastreo(modificacion.getIdentificadorGarantia().getClaveRastreo());
						inscripcionRes.setCodigoError(regresa.getPlSql().getIntPl().toString());
						resultado.getTramites().getTramite().add(inscripcionRes);
					}
				}
			}else{				
				regresa = new ControlError();
				regresa.setPlSql(plSql);
				inscripcionRes.setMensajeError(regresa.getPlSql().getStrPl());
				inscripcionRes.setClaveRastreo(modificacion.getIdentificadorGarantia().getClaveRastreo());
				inscripcionRes.setCodigoError(regresa.getPlSql().getIntPl().toString());
				resultado.getTramites().getTramite().add(inscripcionRes);
				regresa.setPlSql(plSql);
			}
		}
		if (regresa != null) {
			regresa.setClaveRastreo(tramite.getClaveRastreo());
		}
		return regresa;
	}
	private ControlError verificaDatosModificacion(Modificacion modificacion, mx.gob.se.rug.masiva.to.Tramite tramite, mx.gob.se.rug.masiva.resultado.to.TramiteRes inscripcion){
		ControlError regresa = null;
		try{
			if (modificacion.getPartes().getDeudor().size()>0){
				int x = 1;
				for (Deudor deudor: modificacion.getPartes().getDeudor()){
					regresa = verificarDeudor(deudor, tramite, inscripcion, x++);
					if (regresa != null){
						break;
					}
				}
			}
		}catch(Exception e){}	
		
		return regresa;
	}
		
	private ControlError verificarDeudor(Deudor deudor, mx.gob.se.rug.masiva.to.Tramite tramite,mx.gob.se.rug.masiva.resultado.to.TramiteRes inscripcion, Integer numeroDeudor){
		ControlError regresa = null;
		inscripcion = new mx.gob.se.rug.masiva.resultado.to.TramiteRes();
		if (deudor.getTipoPersona()==null && deudor.getTipoPersona().trim().equals("")){
			regresa = new ControlError();
			inscripcion.setMensajeError("El deudor: "+numeroDeudor+", no cuenta con tipo de persona ");
			inscripcion.setClaveRastreo(tramite.getClaveRastreo());
			inscripcion.setCodigoError("301");
		}else if (deudor.getTipoPersona().trim().equals("PF") || deudor.getTipoPersona().trim().equals("PM")){
			try{
					if (deudor.getTipoPersona().trim().equals("PF")){
						try{
							if (deudor.getNombre()== null || deudor.getNombre().trim().equals("")){
								regresa = new ControlError();
								inscripcion.setMensajeError("El campo nombre del deudor: "+numeroDeudor+", esta vacio");
								inscripcion.setClaveRastreo(tramite.getClaveRastreo());
								inscripcion.setCodigoError("307");
							}
						}catch(NullPointerException e){
							regresa = new ControlError();
							inscripcion.setMensajeError("El deudor: "+numeroDeudor+", no tiene nombre ");
							inscripcion.setClaveRastreo(tramite.getClaveRastreo());
							inscripcion.setCodigoError("307");
						}
						try{
							if (deudor.getApellidoPaterno()== null || deudor.getApellidoPaterno().trim().equals("")){
								regresa = new ControlError();
								inscripcion.setMensajeError("El campo apellido paterno del deudor: "+numeroDeudor+", esta vacio");
								inscripcion.setClaveRastreo(tramite.getClaveRastreo());
								inscripcion.setCodigoError("308");
							}
						}catch(NullPointerException e){
							regresa = new ControlError();
							inscripcion.setMensajeError("El deudor: "+numeroDeudor+", no tiene apellido paterno ");
							inscripcion.setClaveRastreo(tramite.getClaveRastreo());
							inscripcion.setCodigoError("308");
						}
					}else{
						try{
							if (deudor.getDenominacionRazonSocial()== null || deudor.getDenominacionRazonSocial().trim().equals("")){
								regresa = new ControlError();
								inscripcion.setMensajeError("El campo denominacion o razon social del deudor: "+numeroDeudor+", esta vacio");
								inscripcion.setClaveRastreo(tramite.getClaveRastreo());
								inscripcion.setCodigoError("306");
							}
						}catch(NullPointerException e){
							regresa = new ControlError();
							inscripcion.setMensajeError("El deudor: "+numeroDeudor+", no tiene denominacion o razon social ");
							inscripcion.setClaveRastreo(tramite.getClaveRastreo());
							inscripcion.setCodigoError("305");
						}
						
					}
	
			}catch(NullPointerException e){
				regresa = new ControlError();
				inscripcion.setMensajeError("El deudor: "+numeroDeudor+", no cuenta con id nacionalidad ");
				inscripcion.setClaveRastreo(tramite.getClaveRastreo());
				inscripcion.setCodigoError("303");
				
			}
		}else{
			regresa = new ControlError();
			inscripcion.setMensajeError("El deudor: "+numeroDeudor+", no tiene un tipo de persona valido ");
			inscripcion.setClaveRastreo(tramite.getClaveRastreo());
			inscripcion.setCodigoError("302");
		}
		
		if (regresa != null){
			resultado.getTramites().getTramite().add(inscripcion);
			regresa = new ControlError();
			PlSql plSql = new PlSql();
			plSql.setResStrPl(inscripcion.getCodigoError());
			plSql.setStrPl(inscripcion.getCodigoError());
			regresa.setPlSql(plSql);
			regresa.setIdInscripcion(tramite.getIdTramite());
		}
		return regresa;
	}
	
	public ControlError revisaGarantia(Inscripcion inscripcion,TramiteRes inscripcionRes, Integer consecutivo){
		ControlError regresa =null;
		/**
		 * try{
					Integer.valueOf(modificacion.getGarantia().getCreacion().getListaTipoBienesMuebles());
				}catch(Exception e){
					listaTramitesErrores.add(0);
					regresa = new ControlError();
					inscripcion.setMensajeError("El campo lista de tipo bienes muebles solo puede contener numeros separados por comas (,).");
					inscripcion.setClaveRastreo(tramite.getClaveRastreo());
					inscripcion.setCodigoError("309");
				}
		 */
		try{
			if (inscripcion.getGarantia().getCreacion().getIdTipoGarantia().intValue()>0){	}
		}catch(Exception e){
			listaTramitesErroneos.add(0);
			regresa = new ControlError();
			inscripcionRes.setMensajeError("El id tipo garantia, es un dato obligatorio.");
			inscripcionRes.setClaveRastreo(inscripcion.getIdentificador().getClaveRastreo());
			inscripcionRes.setCodigoError("310");
		}
		if (regresa == null){
			try{
				if (inscripcion.getGarantia().getCreacion().getFechaCelebracion() ==null ){	
					regresa = new ControlError();
					inscripcionRes.setMensajeError("la fecha de celebracion del acto o contrato es un campo obligatorio.");
					inscripcionRes.setClaveRastreo(inscripcion.getIdentificador().getClaveRastreo());
					inscripcionRes.setCodigoError("311");
				}
			}catch(Exception e){
				regresa = new ControlError();
				inscripcionRes.setMensajeError("El id tipo garantia, es un dato obligatorio.");
				inscripcionRes.setClaveRastreo(inscripcion.getIdentificador().getClaveRastreo());
				inscripcionRes.setCodigoError("310");
			}
		}
		if (regresa == null){
			try{
				if (inscripcion.getGarantia().getCreacion().getIdMoneda() ==null){	
					regresa = new ControlError();
					inscripcionRes.setMensajeError("El id moneda, es un dato obligatorio.");
					inscripcionRes.setClaveRastreo(inscripcion.getIdentificador().getClaveRastreo());
					inscripcionRes.setCodigoError("312");
				}
			}catch(Exception e){
				regresa = new ControlError();
				inscripcionRes.setMensajeError("El id moneda, es un dato obligatorio.");
				inscripcionRes.setClaveRastreo(inscripcion.getIdentificador().getClaveRastreo());
				inscripcionRes.setCodigoError("312");
			}
		}
		if (regresa != null){
			resultado.getTramites().getTramite().add(inscripcionRes);
			regresa = new ControlError();
			PlSql plSql = new PlSql();
			plSql.setResStrPl(inscripcionRes.getMensajeError());
			plSql.setStrPl(inscripcionRes.getCodigoError());
			regresa.setPlSql(plSql);
			regresa.setIdInscripcion(consecutivo);
			MyLogger.Logger.log(Level.WARNING,"error en la validacion de la garantia"+inscripcionRes.getMensajeError());
		}
		return regresa;
	}
	private String getDigest(byte[] attributes) {
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

	private String getSha1FromFile(byte[] in) {
		return getDigest(in);
	}

	private String listToString(List<Integer> lista) {
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
	
	
	private ControlError agregaInscripcion(Inscripcion inscripcion,
			Integer idUsuario, AcreedorTO acreedor, Integer numInscripcion,
			Integer idArchivo) {
		ControlError regresa = null;
		String claveRastreo = "No identificada";
		TramiteRes inscripcionRes = new TramiteRes();
		try {
			Integer idAcreedor = Integer.valueOf(acreedor.getIdAcreedor());
			InscripcionTO inscripcionTO = new InscripcionTO();
			inscripcionTO.setIdTipoTramite(1);
			inscripcionTO.setIdPersona(idUsuario);
			InscripcionDAO inscripcionDAO = new InscripcionDAO();
			MyLogger.Logger.log(Level.INFO,"<<CargaMasivaThread>>-------se trata de revisa la garantia ");
			regresa = revisaGarantia(inscripcion, inscripcionRes, numInscripcion);
			if (regresa==null){
				PlSql plIns = inscripcionDAO.insertInscripcionClaveRastreo(
						inscripcionTO, acreedor, inscripcion.getIdentificador()
								.getClaveRastreo(), idArchivo);
				claveRastreo = inscripcion.getIdentificador().getClaveRastreo();
				if (plIns.getIntPl().intValue() == 0) {
					MyLogger.Logger.log(Level.INFO,"<<CargaMasivaThread>>-------se trata de agregar a un otorgante ");
					regresa = agregaOtorgante(inscripcion, idUsuario, idAcreedor,plIns.getResIntPl().intValue(), numInscripcion,inscripcionRes);
					if (regresa == null) {
						MyLogger.Logger.log(Level.INFO,"<<CargaMasivaThread>>-------no sucedio error con el otorgante ");
						regresa = agregarDeudores(inscripcion, idUsuario, idAcreedor, plIns.getResIntPl().intValue(),
								numInscripcion);
						if (regresa == null) {
							regresa = agregarAcreedoresAdicionales(inscripcion,
									idUsuario, idAcreedor, plIns.getResIntPl()
											.intValue(), numInscripcion);
							if (regresa == null) {
								regresa = agregaGarantia(inscripcion, idUsuario,
										idAcreedor, plIns.getResIntPl().intValue(),
										numInscripcion);
								if (regresa.getPlSql().getIntPl().intValue() == 0) {
									GarantiasDAO dao = new GarantiasDAO();
									inscripcionTO
											.setGarantiaTO(garantiaToGarantiaTO(inscripcion
													.getGarantia()));
									inscripcionTO.getGarantiaTO().setIdGarantia(
											regresa.getPlSql().getResIntPl());
									if (dao.actualizaMeses(inscripcionTO)) {
										if (dao.altaBitacoraTramite(new Integer(
												plIns.getResIntPl().intValue()),
												new Integer(5), new Integer(3),
												null, "V")) {
											listaTramites.add(plIns.getResIntPl().intValue());
											inscripcionRes.setMensajeError("La inscripcion fue procesada correctamente");
											inscripcionRes.setClaveRastreo(inscripcion.getIdentificador().getClaveRastreo());
											inscripcionRes.setCodigoError("0");
											resultado.getTramites().getTramite().add(inscripcionRes);
											regresa = null;
										} else {
											listaTramitesErroneos.add(plIns.getResIntPl().intValue());
											regresa = new ControlError();
											inscripcionRes.setMensajeError("La inscripcion no se pudo dar de alta en la bitacora");
											inscripcionRes.setClaveRastreo(inscripcion.getIdentificador().getClaveRastreo());
											inscripcionRes.setCodigoError("1");
											resultado.getTramites().getTramite().add(inscripcionRes);
										}

									} else {
										listaTramitesErroneos.add(plIns.getResIntPl().intValue());
										inscripcionRes.setMensajeError(regresa.getPlSql().getStrPl());
										inscripcionRes.setClaveRastreo(inscripcion.getIdentificador().getClaveRastreo());
										inscripcionRes.setCodigoError(regresa.getPlSql().getIntPl().toString());
										resultado.getTramites().getTramite().add(inscripcionRes);
									}
								}else{
									listaTramitesErroneos.add(plIns.getResIntPl().intValue());
									inscripcionRes.setMensajeError(regresa.getPlSql().getStrPl());
									inscripcionRes.setClaveRastreo(inscripcion.getIdentificador().getClaveRastreo());
									inscripcionRes.setCodigoError(regresa.getPlSql().getIntPl().toString());
									resultado.getTramites().getTramite().add(inscripcionRes);
								}
							} else {
								listaTramitesErroneos.add(plIns.getResIntPl().intValue());
								inscripcionRes.setMensajeError(regresa.getPlSql().getStrPl());
								inscripcionRes.setClaveRastreo(inscripcion.getIdentificador().getClaveRastreo());
								inscripcionRes.setCodigoError(regresa.getPlSql().getIntPl().toString());
								resultado.getTramites().getTramite().add(inscripcionRes);
							}
						}else{
							listaTramitesErroneos.add(plIns.getResIntPl().intValue());
							inscripcionRes.setMensajeError(regresa.getPlSql().getStrPl());
							inscripcionRes.setClaveRastreo(inscripcion.getIdentificador().getClaveRastreo());
							inscripcionRes.setCodigoError(regresa.getPlSql().getIntPl().toString());
							resultado.getTramites().getTramite().add(inscripcionRes);
						}
					}else{
						listaTramitesErroneos.add(plIns.getResIntPl().intValue());
						inscripcionRes.setMensajeError(regresa.getPlSql().getStrPl());
						inscripcionRes.setClaveRastreo(inscripcion.getIdentificador().getClaveRastreo());
						inscripcionRes.setCodigoError(regresa.getPlSql().getIntPl().toString());
						resultado.getTramites().getTramite().add(inscripcionRes);
					}
				}else{
					MyLogger.Logger.log(Level.WARNING,"sucedio un error en el otorgante");
					listaTramitesErroneos.add(plIns.getResIntPl().intValue());
					inscripcionRes.setMensajeError(plIns.getStrPl());
					inscripcionRes.setClaveRastreo(inscripcion.getIdentificador().getClaveRastreo());
					inscripcionRes.setCodigoError(plIns.getIntPl().toString());
					resultado.getTramites().getTramite().add(inscripcionRes);
				}
			}else{
				MyLogger.Logger.log(Level.WARNING,"la garantia fue erronea");
				inscripcionRes.setMensajeError(regresa.getPlSql().getStrPl());
				inscripcionRes.setClaveRastreo(inscripcion.getIdentificador().getClaveRastreo());
				inscripcionRes.setCodigoError(regresa.getPlSql().getIntPl().toString());
				resultado.getTramites().getTramite().add(inscripcionRes);
			}
			
			
			
		} catch (Exception e) {
			regresa = new ControlError();
			inscripcionRes.setMensajeError("Sucedio un error en el sistema favor de reportarlo.");
			inscripcionRes.setClaveRastreo(inscripcion.getIdentificador().getClaveRastreo());
			inscripcionRes.setCodigoError("2");
			resultado.getTramites().getTramite().add(inscripcionRes);
			e.printStackTrace();
		}
		if (regresa != null) {
			MyLogger.Logger.log(Level.WARNING,"sucedio un error en el thread");
			regresa.setClaveRastreo(claveRastreo);
		}
		return regresa;
	}
				
				
	private ControlError agregaOtorgante(Inscripcion inscripcion,
			Integer idUsuario, Integer idAcreedor, Integer idInscripcion,
			Integer numInscripcion, mx.gob.se.rug.masiva.resultado.to.TramiteRes tramite) {
		ControlError regresa = null;
		MyLogger.Logger.log(Level.INFO,"numero de otorganto :::---"+inscripcion.getPartes().getOtorgante().size());
		for (Otorgante otorgante :inscripcion.getPartes().getOtorgante()){
			regresa =agregaOtorganteInd(otorgante, idUsuario, idAcreedor, idInscripcion, numInscripcion, tramite);
			if (regresa!=null){
				break;
			}
		}

		return regresa;
		}
	private ControlError agregaOtorganteInd(Otorgante otorgante,
			Integer idUsuario, Integer idAcreedor, Integer idInscripcion,
			Integer numInscripcion, mx.gob.se.rug.masiva.resultado.to.TramiteRes tramite) {
		ControlError regresa = null;
		
		try {
			
			AltaParteDAO altaParteDAO = new AltaParteDAO();
			MyLogger.Logger.log(Level.INFO,"RUG-Action:CargaMasivaActionThread.agregaOtorgante------ Iniciamos agregarOtorgante ::::");
			if (otorgante == null) {
				MyLogger.Logger.log(Level.INFO,"RUG-Action:CargaMasivaActionThread.agregaOtorgante------ La inscripcion no cuenta con un otorgante ::::");
				regresa = new ControlError();
				PlSql plSql = new PlSql();
				plSql.setResStrPl("La inscripcion no cuenta con un Otorgante");
				regresa.setPlSql(plSql);
				regresa.setIdInscripcion(numInscripcion);
			} else {
				MyLogger.Logger.log(Level.INFO,"Folop Electronio :::"+otorgante.getFolioElectronico());
				MyLogger.Logger.log(Level.INFO,"tipo persona::"+otorgante.getTipoPersona());
				if (otorgante.getTipoPersona().trim().equals("PF")
						&& otorgante.getFolioElectronico() != null
						&& !otorgante.getFolioElectronico().trim().equals("")) {
					OtorganteTO otorganteTO = altaParteDAO.getOtorganteFisicoByFolioElectronico(otorgante.getFolioElectronico());
					if (otorganteTO != null) {
						MyLogger.Logger.log(Level.INFO,"RUG-Action:CargaMasivaActionThread.agregaOtorgante------ Se relaciona con el folio electronico ::::");
						if (!altaParteDAO.relParte(otorganteTO.getIdOtorgante(),	idInscripcion, 1, null)){
							regresa = new ControlError();
							PlSql plSql = new PlSql();
							plSql.setResStrPl("No se pudo relacionar al otorgante");
							plSql.setStrPl("No se pudo relacionar al otorgante");
							plSql.setIntPl(591);
							plSql.setResIntPl(887);
							regresa.setPlSql(plSql);
							regresa.setIdInscripcion(numInscripcion);
							
						}
					} else {
						MyLogger.Logger.log(Level.INFO,"RUG-Action:CargaMasivaActionThread.agregaOtorgante------ el Folio electronico proporcionado es invalido ::::");
						regresa = new ControlError();
						PlSql plSql = new PlSql();
						plSql.setResStrPl("El Folio Electronico no existe");
						plSql.setStrPl("El Folio Electronico no existe");
						plSql.setIntPl(591);
						plSql.setResIntPl(887);
						regresa.setPlSql(plSql);
						regresa.setIdInscripcion(numInscripcion);
						
					}
				} else {
					MyLogger.Logger.log(Level.INFO,"RUG-Action:CargaMasivaActionThread.agregaOtorgante------ Se trata de agregar una nueva persona ::::");
					AltaParteTO altaParteTO = new AltaParteTO();
					altaParteTO.setIdParte(1);
					altaParteTO.setIdTramite(idInscripcion);
					altaParteTO.setFolioMercantil(otorgante.getFolioElectronico());
					altaParteTO.setCurp(otorgante.getCurp());
					altaParteTO.setRfc(otorgante.getRfc());
					altaParteTO.setNombre(otorgante.getNombre());
					altaParteTO.setApellidoMaterno(otorgante.getApellidoMaterno());
					altaParteTO.setApellidoPaterno(otorgante.getApellidoPaterno());
					altaParteTO.setRazonSocial(otorgante.getDenominacionRazonSocial());
					altaParteTO.setIdNacionalidad(otorgante.getIdNacionalidad().intValue());
					altaParteTO.setTipoPersona(otorgante.getTipoPersona());
					altaParteTO.setHayDomicilio("F");
					altaParteTO.setIdUsuario(idUsuario);
					altaParteTO.setIdPersona(idUsuario);
					if (altaParteTO.getTipoPersona().trim().equals("PM")) {
						MyLogger.Logger.log(Level.INFO,"es una persona moral");
						if (altaParteTO.getFolioMercantil() == null	|| altaParteTO.getFolioMercantil().trim().equals("")) {
							MyLogger.Logger.log(Level.INFO, "El folio electronico es obligatorio");
							regresa = new ControlError();
							PlSql plSql = new PlSql();
							plSql.setResStrPl("Error en el Otorgante:  El campo folio electrónico es obligatorio.");
							plSql.setStrPl("Error en el Otorgante:  El campo folio electrónico es obligatorio.");
							regresa.setPlSql(plSql);
							regresa.setIdInscripcion(numInscripcion);
						} else {
							PlSql intPer = altaParteDAO.insert(altaParteTO);
							if (intPer == null) {
								MyLogger.Logger.log(Level.INFO,"RUG-Action:CargaMasivaActionThread.agregaOtorgante------ No se pudo generar la nueva persona ::::");
								regresa = new ControlError();
								PlSql plSql = new PlSql();
								plSql.setResStrPl("Error en el Otorgante - No se pudo agregar al otorgante");
								plSql.setStrPl("Error en el Otorgante - No se pudo agregar al otorgante");
								regresa.setPlSql(plSql);
								regresa.setIdInscripcion(numInscripcion);
							} else {
								if (intPer.getIntPl().intValue() != 0) {
									MyLogger.Logger.log(Level.INFO,"RUG-Action:CargaMasivaActionThread.agregaOtorgante------ la nueva persona fue dada de alta con el siguiente ID  :::: "
													+ intPer.getIntPl()
															.intValue());
									regresa = new ControlError();
									regresa.setPlSql(intPer);
								}
							}
						}

					} else {
						PlSql intPer = altaParteDAO.insert(altaParteTO);
						if (intPer == null) {
							MyLogger.Logger.log(Level.WARNING,"RUG-Action:CargaMasivaActionThread.agregaOtorgante------ No se pudo generar la nueva persona ::::");
							regresa = new ControlError();
							PlSql plSql = new PlSql();
							plSql.setResStrPl("Error en el Otorgante - No se pudo agregar al otorgante");
							plSql.setStrPl("Error en el Otorgante - No se pudo agregar al otorgante");
							regresa.setPlSql(plSql);
							regresa.setIdInscripcion(numInscripcion);
						} else {
							if (intPer.getIntPl().intValue() != 0) {							
								regresa = new ControlError();
								regresa.setPlSql(intPer);
							} else {
								FolioElectronicoDAO folioElectronicoDAO = new FolioElectronicoDAO();
								String strMsj = folioElectronicoDAO
										.creaFolioElectronico(intPer
												.getResIntPl().intValue());
								if (strMsj!=null){
									mx.gob.se.rug.masiva.resultado.to.Otorgante otorgante2 = new mx.gob.se.rug.masiva.resultado.to.Otorgante();
									otorgante2.setCurp(altaParteTO.getCurp());
									otorgante2.setFolioElectronico(strMsj);
									MyLogger.Logger.log(Level.INFO,"--se genero el siguiente folio electronico de persona fisica -- " + strMsj);
									otorgante2.setNombreCompleto(altaParteTO.getNombre() +" " + altaParteTO.getApellidoPaterno() +" "+ altaParteTO.getApellidoMaterno());
									tramite.getOtorgante().add(otorgante2);
								}else{
									regresa = new ControlError();
									PlSql plSql = new PlSql();
									plSql.setIntPl(501);
									plSql.setResStrPl("Error al tratar de generar folio del RUG, reportarlo al area de sistemas");
									plSql.setStrPl("Error al tratar de generar folio del RUG, reportarlo al area de sistemas");
									regresa.setPlSql(plSql);
									regresa.setIdInscripcion(numInscripcion);
								}
								
							}
						}
					}
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
			regresa.setIdInscripcion(numInscripcion);
			e.printStackTrace();
		} catch (Exception e) {
			regresa = new ControlError();
			PlSql plSql = new PlSql();
			plSql.setIntPl(999);
			plSql.setResStrPl(e.getMessage() + ", " + e.getCause());
			plSql.setStrPl(e.getMessage() + ", " + e.getCause());
			regresa.setPlSql(plSql);
			regresa.setIdInscripcion(numInscripcion);
			e.printStackTrace();
		}
			return regresa;

		}
	
	private ControlError agregarDeudores(List<Deudor> deudores,
			mx.gob.se.rug.masiva.to.Tramite tramite) {
		ControlError regresa = null;
		try {
			if (deudores.size() > 0) {
				Iterator<Deudor> itDe = deudores.iterator();
				PlSql plSql = new PlSql();
				plSql.setResIntPl(0);
				AltaParteDAO altaParteDAO = new AltaParteDAO();
				while (plSql.getResIntPl().intValue() != 0 && itDe.hasNext()) {
					Deudor deudor = itDe.next();
					AltaParteTO altaParteTO = new AltaParteTO();
					altaParteTO.setIdParte(2);
					altaParteTO.setIdTramite(tramite.getIdTramite());
					altaParteTO.setRazonSocial(deudor.getDenominacionRazonSocial());
					altaParteTO.setTipoPersona(deudor.getTipoPersona());
					altaParteTO.setNombre(deudor.getNombre());
					altaParteTO.setApellidoPaterno(deudor.getApellidoPaterno());
					altaParteTO.setApellidoMaterno(deudor.getApellidoMaterno());
					altaParteTO.setRfc(deudor.getRfc());
					altaParteTO.setHayDomicilio("F");
					altaParteTO.setIdNacionalidad(deudor.getIdNacionalidad().intValue());
					altaParteTO.setIdPersona(Integer.valueOf(tramite.getIdUsuario()));
					plSql = altaParteDAO.insert(altaParteTO);
				}
				if (plSql.getResIntPl().intValue() != 0) {
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
	private ControlError agregarDeudores(Inscripcion inscripcion,
			Integer idUsuario, Integer idAcreedor, Integer idInscripcion,
			Integer numInscripcion) {
		ControlError regresa = null;
		try {
			List<Deudor> deudores = inscripcion.getPartes().getDeudor();
			if (deudores.size() > 0) {
				Iterator<Deudor> itDe = deudores.iterator();
				PlSql plSql = new PlSql();
				plSql.setIntPl(0);
				AltaParteDAO altaParteDAO = new AltaParteDAO();
				while (plSql.getIntPl().intValue() == 0 && itDe.hasNext()) {
					Deudor deudor = itDe.next();
					AltaParteTO altaParteTO = new AltaParteTO();
					altaParteTO.setIdParte(2);
					altaParteTO.setIdTramite(idInscripcion);
					altaParteTO.setRazonSocial(deudor.getDenominacionRazonSocial());
					altaParteTO.setTipoPersona(deudor.getTipoPersona());
					altaParteTO.setNombre(deudor.getNombre());
					altaParteTO.setApellidoPaterno(deudor.getApellidoPaterno());
					altaParteTO.setApellidoMaterno(deudor.getApellidoMaterno());
					altaParteTO.setRfc(deudor.getRfc());
					altaParteTO.setHayDomicilio("F");
					altaParteTO.setIdNacionalidad(deudor.getIdNacionalidad().intValue());
					altaParteTO.setIdPersona(Integer.valueOf(idUsuario));
					altaParteTO.setIdPersona(idUsuario);
					plSql = altaParteDAO.insert(altaParteTO);
				}
				if (plSql.getResIntPl().intValue() != 0) {
					regresa = new ControlError();
					regresa.setPlSql(plSql);
					regresa.setIdInscripcion(numInscripcion);
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
			regresa.setIdInscripcion(numInscripcion);
			e.printStackTrace();
		} catch (Exception e) {
			regresa = new ControlError();
			PlSql plSql = new PlSql();
			plSql.setResStrPl(e.getMessage() + ", " + e.getCause());
			plSql.setStrPl(e.getMessage() + ", " + e.getCause());
			regresa.setPlSql(plSql);
			regresa.setIdInscripcion(numInscripcion);
			e.printStackTrace();
		}

		return regresa;

	}

	private ControlError agregarAcreedoresAdicionales(Inscripcion inscripcion,
			Integer idUsuario, Integer idAcreedor, Integer idInscripcion,
			Integer numInscripcion) {
		ControlError regresa = null;
		try {
			List<AcreedorAdicional> acreedores = inscripcion.getPartes()
					.getAcreedorAdicional();
			if (acreedores.size() > 0) {
				Iterator<AcreedorAdicional> itDe = acreedores.iterator();
				PlSql plSql = new PlSql();
				plSql.setResIntPl(0);
				AltaParteDAO altaParteDAO = new AltaParteDAO();
				while (plSql.getResIntPl().intValue() != 0 && itDe.hasNext()) {
					AcreedorAdicional acreedoAdicional = itDe.next();
					AltaParteTO altaParteTO = new AltaParteTO();
					altaParteTO.setDomicilioUno(acreedoAdicional.getDomicilioExtranjeroUno());
					altaParteTO.setDomicilioDos(acreedoAdicional.getDomicilioExtranjeroDos());
					altaParteTO.setPoblacion(acreedoAdicional.getPoblacion());
					altaParteTO.setZonaPostal(acreedoAdicional.getZonaPostal());
					altaParteTO.setIdParte(3);
					altaParteTO.setIdPersona(idUsuario);
					altaParteTO.setIdTramite(idInscripcion);
					altaParteTO.setRfc("");
					altaParteTO.setNombre(acreedoAdicional.getNombre());
					altaParteTO.setApellidoMaterno(acreedoAdicional.getApellidoMaterno());
					altaParteTO.setApellidoPaterno(acreedoAdicional.getApellidoPaterno());
					altaParteTO.setRazonSocial(acreedoAdicional.getDenominacionRazonSocial());
					altaParteTO.setIdNacionalidad(acreedoAdicional.getIdNacionalidad().intValue());
					altaParteTO.setTipoPersona(acreedoAdicional.getTipoPersona());
					altaParteTO.setHayDomicilio("V");
					altaParteTO.setIdColonia(acreedoAdicional.getIdColonia().intValue());
					altaParteTO.setIdLocalidad(acreedoAdicional.getIdLocalidad().intValue());
					altaParteTO.setCalle(acreedoAdicional.getCalle());
					altaParteTO.setNumeroExterior(acreedoAdicional.getNumeroExterior());
					altaParteTO.setNumeroInterior(acreedoAdicional.getNumeroInterior());
					altaParteTO.setCorreoElectronico(acreedoAdicional.getCorreoElectronico());
					altaParteTO.setTelefono(acreedoAdicional.getTelefono());
					altaParteTO.setExtencion(acreedoAdicional.getTelefonoExtension());
					altaParteTO.setIdPersona(idUsuario);
					altaParteTO.setIdUsuario(idUsuario);

					plSql = altaParteDAO.insert(altaParteTO);
				}
				if (plSql.getResIntPl().intValue() != 0) {
					regresa = new ControlError();
					regresa.setPlSql(plSql);
					regresa.setIdInscripcion(numInscripcion);
				}

			}
		} catch (NumberFormatException e) {
			regresa = new ControlError();
			PlSql plSql = new PlSql();
			plSql.setResStrPl("Error al tratar de convertir a entero:"
					+ e.getMessage() + ", " + e.getCause());
			plSql.setStrPl("Error al tratar de convertir a entero:"
					+ e.getMessage() + ", " + e.getCause());
			plSql.setIntPl(999);
			regresa.setPlSql(plSql);
			regresa.setIdInscripcion(numInscripcion);
			e.printStackTrace();
		} catch (Exception e) {
			regresa = new ControlError();
			PlSql plSql = new PlSql();
			plSql.setResStrPl(e.getMessage() + ", " + e.getCause());
			plSql.setStrPl(e.getMessage() + ", " + e.getCause());
			regresa.setPlSql(plSql);
			regresa.setIdInscripcion(numInscripcion);
			e.printStackTrace();
		}

		return regresa;

	}
	private ControlError agregaGarantia(Inscripcion inscripcion,
			Integer idUsuario, Integer idAcreedor, Integer idInscripcion,
			Integer numInscripcion) {
		ControlError regresa = null;
		MyLogger.Logger.log(Level.INFO,"RUG-Action:CargaMasivaActionThread.agregaGarantia------Se trate de dar de alta la nueva garantia :::: ");
		try {
			if (regresa == null) {
				if (regresa ==null){
					GarantiasDAO garantiasDAO = new GarantiasDAO();
					InscripcionTO inscripcionTO = new InscripcionTO();
					inscripcionTO.setIdInscripcion(idInscripcion);
					inscripcionTO.setIdPersona(idUsuario);
					GarantiaTO garantiaTO = garantiaToGarantiaTO(inscripcion
							.getGarantia());
					PlSql insGar = garantiasDAO.insertGarantia(inscripcionTO,
							garantiaTO);
					regresa = new ControlError();
					regresa.setPlSql(insGar);
					regresa.setIdInscripcion(numInscripcion);
					if (insGar.getIntPl().intValue() == 0) {
//						getListaIdGarantias().add(insGar.getIntPl().intValue());
//						MyLogger.Logger.log(Level.INFO,"RUG-Action:CargaMasivaActionThread.agregaGarantia------La alta de la garantia es exitosa :::: ");
					} else {
						MyLogger.Logger.log(Level.WARNING,"RUG-Action:CargaMasivaActionThread.agregaGarantia------Sucedio un error en la alta de la garantia :::: ");
					}
				}
				
			}
		} catch (Exception e) {
			if (e instanceof NullPointerException) {
				regresa = new ControlError();
				PlSql plSql = new PlSql();
				plSql.setResStrPl("Sucedio un error de precicion nupex, favor de reportarlo. ");
				plSql.setStrPl("Sucedio un error de precicion nupex, favor de reportarlo. ");
				plSql.setIntPl(999);
				regresa.setPlSql(plSql);
				regresa.setIdInscripcion(numInscripcion);
			} else {
				regresa = new ControlError();
				PlSql plSql = new PlSql();
				plSql.setResStrPl(e.getStackTrace().toString());
				plSql.setStrPl(e.getStackTrace().toString());
				plSql.setIntPl(999);
				regresa.setPlSql(plSql);
				regresa.setIdInscripcion(numInscripcion);
			}

			e.printStackTrace();
		}

		return regresa;
	}
	
	private GarantiaTO garantiaToGarantiaTO(Garantia garantia) {
		GarantiaTO garantiaTO = new GarantiaTO();
		try {
			DateUtilRug dateUtilRug = new DateUtilRug();
			Creacion creacion = garantia.getCreacion();
			Obligacion obligacion = garantia.getObligacion();
			ActoContratoTO actoContratoTO = new ActoContratoTO();
			actoContratoTO.setFechaCelebracion(dateUtilRug.formatDate(Calendar
					.getInstance().getTime()));
			actoContratoTO.setMontoMaximo(creacion.getMontoMaximo().toString());
			actoContratoTO.setTipoBienes(listaTipoBienMuebleToString(creacion.getTipoBienMueble()));
			actoContratoTO.setDescripcion(creacion
					.getDescripcionBienesMuebles());
			actoContratoTO.setOtrosTerminos(creacion.getTerminosCondiciones());
			MyLogger.Logger.log(Level.INFO,"RUG:CargaMasivaAction.garantiaTOgarantiaTO--------------------------valor del id moneda::"
							+ creacion.getIdMoneda());
			actoContratoTO.setTipoMoneda(creacion.getIdMoneda().toString());
			actoContratoTO.setCambiosBienesMonto(creacion
					.getbDatosModificables());
			garantiaTO.setActoContratoTO(actoContratoTO);
			ObligacionTO obligacionTO = new ObligacionTO();
			obligacionTO.setTipoActoContrato(obligacion.getActoContrato()
					.toString());
			obligacionTO.setOtrosTerminos(obligacion.getTerminos());
			garantiaTO.setObligacionTO(obligacionTO);
		} catch (NoDateInfrastructureException e) {

		} catch (Exception e) {

		}
		return garantiaTO;
	}
	private String listaTipoBienMuebleToString(List<TipoBienMueble> tipoBienes){
		String regresa = "";
		Iterator<TipoBienMueble> it = tipoBienes.iterator();
		if (it.hasNext()){
			regresa = it.next().getId()+"";
		}
		while(it.hasNext()){
			regresa += ("|"+it.next().getId());
		}
		return regresa;	
	}
	private ControlError validaFechaActual(String fecha1, Integer numInscripcion) {
		ControlError regresa = null;
		if (fecha1 != null | fecha1.equals("")) {
			try {
				DateUtilRug dateUtil = new DateUtilRug();
				Date date = dateUtil.parseToSQLDate(dateUtil
						.parseStrDate1(fecha1));
				if ((new java.util.Date(date.getTime())).after(Calendar
						.getInstance().getTime())) {
					regresa = new ControlError();
					PlSql plSql = new PlSql();
					plSql.setResStrPl("La Fecha de celebración del Acto o Contrato debe ser menor o igual a la actual ");
					plSql.setStrPl("La Fecha de celebración del Acto o Contrato debe ser menor o igual a la actual");
					plSql.setIntPl(120);
					regresa.setPlSql(plSql);
					regresa.setIdInscripcion(numInscripcion);
				}
			} catch (Exception e) {
				regresa = new ControlError();
				PlSql plSql = new PlSql();
				plSql.setResStrPl("La Fecha de celebración del acto o contrato no tiene el formato correto. ");
				plSql.setStrPl("La Fecha de celebración del acto o contrato no tiene el formato correto.");
				plSql.setIntPl(119);
				regresa.setPlSql(plSql);
				regresa.setIdInscripcion(numInscripcion);
			}
		} else {
			regresa = new ControlError();
			PlSql plSql = new PlSql();
			plSql.setResStrPl("La Fecha de celebración del acto o contrato es un campo obligatorio.");
			plSql.setStrPl("La Fecha de celebración del acto o contrato es un campo obligatorio.");
			plSql.setIntPl(118);
			regresa.setPlSql(plSql);
			regresa.setIdInscripcion(numInscripcion);
		}
		return regresa;

	}

	private ControlError validaFechas(String fecha1, String fecha2,
			Integer numInscripcion) {
		ControlError regresa = null;
		if (fecha1 != null | !fecha1.equals("")) {
			DateUtilRug dateUtil = new DateUtilRug();
			try {
				Date date = dateUtil.parseToSQLDate(dateUtil
						.parseStrDate1(fecha1));
				if ((new java.util.Date(date.getTime())).after(Calendar
						.getInstance().getTime())) {
					regresa = new ControlError();
					PlSql plSql = new PlSql();
					plSql.setResStrPl("La Fecha de celebración del Acto o Contrato debe ser menor o igual a la actual ");
					plSql.setStrPl("La Fecha de celebración del Acto o Contrato debe ser menor o igual a la actual");
					plSql.setIntPl(120);
					regresa.setPlSql(plSql);
					regresa.setIdInscripcion(numInscripcion);
				} else {
					if (fecha2 != null | !fecha2.equals("")) {
						Date date2 = dateUtil.parseToSQLDate(dateUtil
								.parseStrDate1(fecha1));
						if (date.after(date2)) {
							regresa = new ControlError();
							PlSql plSql = new PlSql();
							plSql.setResStrPl("La Fecha de Terminación  del acto o contrato debe ser posterior a la fecha de celebración. ");
							plSql.setStrPl("La Fecha de Terminación  del acto o contrato debe ser posterior a la fecha de celebración.");
							plSql.setIntPl(120);
							regresa.setPlSql(plSql);
							regresa.setIdInscripcion(numInscripcion);
						}

					}
				}

			} catch (NoDateInfrastructureException e) {
				// TODO Auto-generated catch block
				regresa = new ControlError();
				PlSql plSql = new PlSql();
				plSql.setResStrPl("La Fecha de celebración del acto o contrato no tiene el formato correto. ");
				plSql.setStrPl("La Fecha de celebración del acto o contrato no tiene el formato correto.");
				plSql.setIntPl(119);
				regresa.setPlSql(plSql);
				regresa.setIdInscripcion(numInscripcion);
			}
		}
		return regresa;

	}
	
    public byte[] convertXMLObjetc(Object obj) throws JAXBException, FileNotFoundException, UnsupportedEncodingException{
        JAXBContext context = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
        StringWriter  stringWriter = new StringWriter();
        marshaller.marshal(obj, stringWriter);
        return stringWriter.toString().getBytes("ISO-8859-1");
    }

	public Integer getIdTramite() {
		return idTramite;
	}

	public void setIdTramite(Integer idTramite) {
		this.idTramite = idTramite;
	}

	
}
