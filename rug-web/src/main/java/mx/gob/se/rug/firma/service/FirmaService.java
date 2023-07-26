package mx.gob.se.rug.firma.service;

import java.io.StringWriter;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import mx.gob.se.rug.acceso.dao.AccesoDAO;
import mx.gob.se.rug.anotacion.tramites.to.AnotacionSnGarantiaTO;
import mx.gob.se.rug.dto.PersonaFisica;
import mx.gob.se.rug.exception.RugException;
import mx.gob.se.rug.firma.cadenaoriginal.anotacion.Anotacion;
import mx.gob.se.rug.firma.cadenaoriginal.anotacion.Anotacion.Informacion;
import mx.gob.se.rug.firma.cadenaoriginal.anotacion.Anotacion.Otorgante;
import mx.gob.se.rug.firma.cadenaoriginal.anotacion.Anotacion.Otorgante.Fisico;
import mx.gob.se.rug.firma.cadenaoriginal.anotacion.Anotacion.Otorgante.Moral;
import mx.gob.se.rug.firma.cadenaoriginal.anotacion.Anotacion.Tramite;
import mx.gob.se.rug.firma.cadenaoriginal.anotacion.Anotacion.UsuarioCaptura;
import mx.gob.se.rug.firma.cadenaoriginal.anotacion.Anotacion.UsuarioFirma;
import mx.gob.se.rug.firma.dao.FirmaDAO;
import mx.gob.se.rug.operaciones.dao.OperacionesDAO;
import mx.gob.se.rug.operaciones.to.OperacionesTO;
import mx.gob.se.rug.util.MyLogger;

public class FirmaService {
	public Boolean validateTramite(Integer idTramiteNuevo) {
		Boolean valido = new Boolean(false);
        FirmaDAO firmaDAO = new FirmaDAO();
        valido=firmaDAO.validateTramiteFirma(idTramiteNuevo);
		return valido;
	}
	
	public Boolean validaFecha(Integer idTramiteNuevo){
		boolean regresa = false;
		OperacionesDAO operacionesDAO = new OperacionesDAO();
		OperacionesTO operacionesTO = null;
		try {
			operacionesTO = operacionesDAO.getById(idTramiteNuevo);
			MyLogger.Logger.log(Level.INFO, "IdTramiteNuevo dentro de validaFecha: " + idTramiteNuevo);
			//ID_TIPO_TRAMITE ins 1 ap 3 anotsin 10 firma masiva 18 
			if (operacionesTO!=null){
				if ( operacionesTO.getIdGarantiaModificar()!=null){
					MyLogger.Logger.log(Level.INFO, "operacionesTO.getIdTipoTramite().intValue()" + operacionesTO.getIdTipoTramite().intValue());
					if (operacionesTO.getIdTipoTramite().intValue() == 1 || operacionesTO.getIdTipoTramite().intValue() == 3 || operacionesTO.getIdTipoTramite().intValue() == 10 || operacionesTO.getIdTipoTramite().intValue() == 18){
						regresa = true;
					}else{
						
						regresa = operacionesDAO.validaFecha(Integer.valueOf(operacionesTO.getIdGarantiaModificar()), operacionesTO.getIdTramiteTemporal());
						MyLogger.ErrorFirma.log(Level.INFO,"::Inicia LOG idTramiteNuevo: "+ idTramiteNuevo +" idGarantia: "+ operacionesTO.getIdGarantiaModificar() +" idTramTemporal: "+ operacionesTO.getIdTramiteTemporal() +"::");
					}
				}else{
					regresa = true;
				}
				
			}else{
				
				MyLogger.ErrorFirma.log(Level.INFO,"::Inicia Error idTramiteNuevo: "+ idTramiteNuevo +"::");
				MyLogger.ErrorFirma.log(Level.INFO,"operacionesTO=null");
			}
		} catch (RugException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MyLogger.ErrorFirma.log(Level.INFO,"::Inicia Error idTramiteNuevo: "+ idTramiteNuevo +"::");
			MyLogger.ErrorFirma.log(Level.INFO,"Excepcion",e);
			MyLogger.ErrorFirma.log(Level.INFO,"::Fin::");
		}
	
		return regresa;
	}
	
	public String generaXMLAnotacionSnGarantia(AnotacionSnGarantiaTO snGarantiaTO) throws JAXBException{
		
		Anotacion anotacion = new Anotacion();
		AccesoDAO accesoDAO = new AccesoDAO();
		UsuarioCaptura usuarioCaptura = new UsuarioCaptura();
		UsuarioFirma usuarioFirma = new UsuarioFirma();
		Tramite tramite = new Tramite();
		Informacion informacion = new Informacion();
		Otorgante otorgante = new Otorgante();
		
		PersonaFisica usuarioCapturo=accesoDAO.getUsuario(snGarantiaTO.getIdUsuario());
		PersonaFisica usuarioFirmo=accesoDAO.getUsuario(snGarantiaTO.getIdUsuarioFirmo());
		
		usuarioCaptura.setIdUsuario(snGarantiaTO.getIdUsuario().toString());
		usuarioCaptura.setNombre(usuarioCapturo.getNombre());
		usuarioCaptura.setApaterno(usuarioCapturo.getApellidoPaterno());
		usuarioCaptura.setAmaterno(usuarioCapturo.getApellidoMaterno());
		
		usuarioFirma.setIdUsuario(snGarantiaTO.getIdUsuarioFirmo().toString());
		usuarioFirma.setNombre(usuarioFirmo.getNombre());
		usuarioFirma.setApaterno(usuarioFirmo.getApellidoPaterno());
		usuarioFirma.setAmaterno(usuarioFirmo.getApellidoMaterno());
		
		
		if(snGarantiaTO.getOtorganteDenominacion()!= null){
			//Moral
			Moral moral = new Moral();
			moral.setIdPersona(snGarantiaTO.getOtorganteId().toString());
			moral.setDenominacionSocial(snGarantiaTO.getOtorganteDenominacion());
			moral.setFolioElectronico(snGarantiaTO.getOtorganteFolioElectronico());
			otorgante.setMoral(moral);
		}else{
			//Fisica
			Fisico fisico = new Fisico();
			fisico.setNombre(snGarantiaTO.getOtorganteNombre());
			fisico.setApaterno(snGarantiaTO.getOtorganteAPaterno());
			fisico.setAmaterno(snGarantiaTO.getOtorganteAMaterno());
			fisico.setFolioElectronico(snGarantiaTO.getOtorganteFolioElectronico());
			fisico.setIdPersona(snGarantiaTO.getOtorganteId().toString());
			otorgante.setFisico(fisico);
		}
		
		informacion.setAnotacion(snGarantiaTO.getAnotacion());
		informacion.setPersonaAutoridadAutoriza(snGarantiaTO.getAutoridadAutorizaTramite());
		informacion.setOrigen(snGarantiaTO.getOrigenTramite());
		informacion.setVigencia(snGarantiaTO.getVigenciaStr());
		
		tramite.setIdTipoTramite(snGarantiaTO.getIdTipoTramite().toString());
		tramite.setIdTramiteNuevo(snGarantiaTO.getIdTramiteNuevo().toString());
		tramite.setIdTramitePadre(snGarantiaTO.getIdTramitePadre().toString());
		
		anotacion.setInformacion(informacion);
		anotacion.setTramite(tramite);
		anotacion.setUsuarioCaptura(usuarioCaptura);
		anotacion.setUsuarioFirma(usuarioFirma);
		anotacion.setOtorgante(otorgante);
		
		 JAXBContext context = JAXBContext.newInstance(anotacion.getClass());
	        Marshaller marshaller = context.createMarshaller();
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
	        StringWriter  stringWriter = new StringWriter();
	        marshaller.marshal(anotacion, stringWriter);
	        return stringWriter.toString();
	}
	public String generaCOAnotacionSnGarantia(String xml, AnotacionSnGarantiaTO snGarantiaTO){
		String digestivo = new String();
		try {
			java.security.MessageDigest messageDigest = java.security.MessageDigest
					.getInstance("SHA-1");
			byte[] digest = messageDigest.digest(xml.getBytes());
			digestivo = new String(	org.apache.commons.codec.binary.Base64.encodeBase64(digest));
			
			
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		
		return digestivo + "|" + snGarantiaTO.getIdUsuario() + "|" + snGarantiaTO.getIdUsuarioFirmo();
	}
}
