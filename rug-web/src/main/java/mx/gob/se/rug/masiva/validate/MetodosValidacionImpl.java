package mx.gob.se.rug.masiva.validate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import mx.gob.se.constants.Constantes;
import mx.gob.se.rug.masiva.exception.CargaMasivaException;
import mx.gob.se.rug.masiva.resultado.to.TramiteRes;
import mx.gob.se.rug.masiva.to.Acreedor;
import mx.gob.se.rug.masiva.to.AcreedorAdicional;
import mx.gob.se.rug.masiva.to.AcreedorAutoridad;
import mx.gob.se.rug.masiva.to.BienEspecial;
import mx.gob.se.rug.masiva.to.CargaMasiva;
import mx.gob.se.rug.masiva.to.Convenio;
import mx.gob.se.rug.masiva.to.Creacion;
import mx.gob.se.rug.masiva.to.DatosAnotacion;
import mx.gob.se.rug.masiva.to.DatosAvisoPreventivo;
import mx.gob.se.rug.masiva.to.DatosCancelacion;
import mx.gob.se.rug.masiva.to.DatosGarantia;
import mx.gob.se.rug.masiva.to.DatosModificacion;
import mx.gob.se.rug.masiva.to.DatosRenovacionReduccion;
import mx.gob.se.rug.masiva.to.Deudor;
import mx.gob.se.rug.masiva.to.EliminarPartesModificacion;
import mx.gob.se.rug.masiva.to.EliminarPartesRectificacion;
import mx.gob.se.rug.masiva.to.EliminarPartesTransmision;
import mx.gob.se.rug.masiva.to.Garantia;
import mx.gob.se.rug.masiva.to.Identificador;
import mx.gob.se.rug.masiva.to.IdentificadorGarantia;
import mx.gob.se.rug.masiva.to.Obligacion;
import mx.gob.se.rug.masiva.to.ObligacionGarantiza;
import mx.gob.se.rug.masiva.to.ObligacionGarantizaModificacion;
import mx.gob.se.rug.masiva.to.Otorgante;
import mx.gob.se.rug.masiva.to.Partes;
import mx.gob.se.rug.masiva.to.PartesRectificacion;
import mx.gob.se.rug.masiva.to.PersonaSolicitaAutoridadInstruyeAsiento;
import mx.gob.se.rug.masiva.to.TipoBienMueble;
import mx.gob.se.rug.masiva.to.Vigencia;
import mx.gob.se.rug.masiva.to.string.Anotacion;
import mx.gob.se.rug.masiva.to.string.AnotacionGarantia;
import mx.gob.se.rug.masiva.to.string.AvisoPreventivo;
import mx.gob.se.rug.masiva.to.string.Cancelacion;
import mx.gob.se.rug.masiva.to.string.CargaMasivaPreProcesed;
import mx.gob.se.rug.masiva.to.string.Ejecucion;
import mx.gob.se.rug.masiva.to.string.Inscripcion;
import mx.gob.se.rug.masiva.to.string.Modificacion;
import mx.gob.se.rug.masiva.to.string.RectificacionPorError;
import mx.gob.se.rug.masiva.to.string.RenovacionReduccion;
import mx.gob.se.rug.masiva.to.string.Transmision;
import mx.gob.se.rug.util.MyLogger;

public class MetodosValidacionImpl implements MetodosValidacion {
	
	private CargaMasivaPreProcesed cargaMasivaPreProcesed = new CargaMasivaPreProcesed();
	private List<Deudor> deudors;
	private List<Otorgante> otorgantes;
	private List<TipoBienMueble> tipoBienMuebleTo;
	private List<AcreedorAdicional> acreedorAdicionals;
	private List<AcreedorAutoridad> acreedorAutoridad;
	private List<BienEspecial> bienesEspeciales;
	private int contTramites = 0;
	
	public CargaMasivaPreProcesed valida_renovacion_reduccion(mx.gob.se.rug.masiva.to.string.CargaMasiva cargaMasivaString,CargaMasiva cargaMasiva){
		
		cargaMasivaPreProcesed.setCargaMasiva(cargaMasiva);
		cargaMasivaPreProcesed.setTramiteIncorrectos(new ArrayList<TramiteRes>());
		
		List<RenovacionReduccion> renovacionReduccionStr = cargaMasivaString.getRenovacionReduccion();
		List<mx.gob.se.rug.masiva.to.RenovacionReduccion> renovacionReduccions2 = cargaMasiva.getRenovacionReduccion();
		
		for (RenovacionReduccion renovacionReduccionString : renovacionReduccionStr) {
			contTramites++;
			mx.gob.se.rug.masiva.to.RenovacionReduccion reduccion = new mx.gob.se.rug.masiva.to.RenovacionReduccion();
			
			try{
				if(renovacionReduccionString.getPersonaSolicitaAutoridadInstruyeAsiento() != null){
					reduccion.setPersonaSolicitaAutoridadInstruyeAsiento(validatePersonaSolicitaAutoridadInstruyeAsiento(renovacionReduccionString.getPersonaSolicitaAutoridadInstruyeAsiento()));
				}
				reduccion.setIdentificadorGarantia(validateIdentificadorGarantia(renovacionReduccionString.getIdGarantia(),renovacionReduccionString.getIdentificador()));				
				
				DatosRenovacionReduccion datosRenovacionReduccion= new DatosRenovacionReduccion();
				datosRenovacionReduccion.setVigencia(validarVigencia(renovacionReduccionString.getVigencia().toString(), 9));								
				reduccion.setDatosRenovacionReduccion(datosRenovacionReduccion);
				
				renovacionReduccions2.add(reduccion); // LLenamos el objeto preprocesado
			}catch (CargaMasivaException e) {
				cargaMasivaPreProcesed.getTramiteIncorrectos().add(e.getTramiteIncorrecto(renovacionReduccionString.getIdentificador()));
			}
		}
		cargaMasivaPreProcesed.setTotalTramites(contTramites);
		return cargaMasivaPreProcesed;
	}
	
	public CargaMasivaPreProcesed valida_cancelacion(mx.gob.se.rug.masiva.to.string.CargaMasiva cargaMasivaString,CargaMasiva cargaMasiva){
		
		cargaMasivaPreProcesed.setCargaMasiva(cargaMasiva);
		cargaMasivaPreProcesed.setTramiteIncorrectos(new ArrayList<TramiteRes>());
		
		List<Cancelacion> cancelacionStr = cargaMasivaString.getCancelacion();
		List<mx.gob.se.rug.masiva.to.Cancelacion> cancelacion2 = cargaMasiva.getCancelacion();
		
		for (Cancelacion cancelacionString : cancelacionStr) {
			contTramites++;
			mx.gob.se.rug.masiva.to.Cancelacion cancelacion = new mx.gob.se.rug.masiva.to.Cancelacion();
			
			try{
				cancelacion.setIdentificadorGarantia(validateIdentificadorGarantia(cancelacionString.getIdGarantia(), cancelacionString.getIdentificador()));
				cancelacion.setRazon(stringValidate(cancelacionString.getRazon(),0,1000,"razon-cancelacion",true));
				
				if(cancelacionString.getPersonaSolicitaAutoridadInstruyeAsiento() != null){
					cancelacion.setPersonaSolicitaAutoridadInstruyeAsiento(validatePersonaSolicitaAutoridadInstruyeAsiento(cancelacionString.getPersonaSolicitaAutoridadInstruyeAsiento()));
				}
				
				cancelacion2.add(cancelacion); // LLenamos el objeto preprocesado
			}catch (CargaMasivaException e) {
				cargaMasivaPreProcesed.getTramiteIncorrectos().add(e.getTramiteIncorrecto(cancelacionString.getIdentificador()));
			}
		}
		cargaMasivaPreProcesed.setTotalTramites(contTramites);
		return cargaMasivaPreProcesed;
	}
	
	public CargaMasivaPreProcesed valida_ejecucion(mx.gob.se.rug.masiva.to.string.CargaMasiva cargaMasivaString,CargaMasiva cargaMasiva){
		
		cargaMasivaPreProcesed.setCargaMasiva(cargaMasiva);
		cargaMasivaPreProcesed.setTramiteIncorrectos(new ArrayList<TramiteRes>());
		
		List<Ejecucion> ejecucionStr = cargaMasivaString.getEjecucion();
		List<mx.gob.se.rug.masiva.to.Ejecucion> ejecucion2 = cargaMasiva.getEjecucion();
		
		for (Ejecucion ejecucionString : ejecucionStr) {
			contTramites++;
			mx.gob.se.rug.masiva.to.Ejecucion ejecucion = new mx.gob.se.rug.masiva.to.Ejecucion();
			
			try{
				ejecucion.setIdentificadorGarantia(validateIdentificadorGarantia(ejecucionString.getIdGarantia(), ejecucionString.getIdentificador()));
				ejecucion.setRazon(stringValidate(ejecucionString.getRazon(),0,1000,"razon-cancelacion",true));
				
				if(ejecucionString.getPersonaSolicitaAutoridadInstruyeAsiento() != null){
					ejecucion.setPersonaSolicitaAutoridadInstruyeAsiento(validatePersonaSolicitaAutoridadInstruyeAsiento(ejecucionString.getPersonaSolicitaAutoridadInstruyeAsiento()));
				}
				
				ejecucion2.add(ejecucion); // LLenamos el objeto preprocesado
			}catch (CargaMasivaException e) {
				cargaMasivaPreProcesed.getTramiteIncorrectos().add(e.getTramiteIncorrecto(ejecucionString.getIdentificador()));
			}
		}
		cargaMasivaPreProcesed.setTotalTramites(contTramites);
		return cargaMasivaPreProcesed;
	}

	public CargaMasivaPreProcesed valida_modificacion(mx.gob.se.rug.masiva.to.string.CargaMasiva cargaMasivaString,CargaMasiva cargaMasiva){
		
		cargaMasivaPreProcesed.setCargaMasiva(cargaMasiva);
		cargaMasivaPreProcesed.setTramiteIncorrectos(new ArrayList<TramiteRes>());
		
		List<Modificacion>modificacionStr = cargaMasivaString.getModificacion();
		List<mx.gob.se.rug.masiva.to.Modificacion> modificacion2 = cargaMasiva.getModificacion();
		
		for (Modificacion modificacionString : modificacionStr) {
			contTramites++;
			mx.gob.se.rug.masiva.to.Modificacion modificacion = new mx.gob.se.rug.masiva.to.Modificacion();
			
			try{
				modificacion.setIdentificadorGarantia(validateIdentificadorGarantia(modificacionString.getIdGarantia(),modificacionString.getIdentificador()));
				
				modificacion.setPartes(validatePartes(modificacionString.getPartes(),7));
				modificacion.setGarantia(validateGarantia(modificacionString.getGarantia(),7));
				modificacion.setRazon(stringValidate(modificacionString.getRazon(),0,1000,"razon-modificacion",true));
				
				modificacion2.add(modificacion); // LLenamos el objeto preprocesado
			}catch (CargaMasivaException e) {
				cargaMasivaPreProcesed.getTramiteIncorrectos().add(e.getTramiteIncorrecto(modificacionString.getIdentificador()));
			}
		}
		cargaMasivaPreProcesed.setTotalTramites(contTramites);
		return cargaMasivaPreProcesed;
	}
	
	public CargaMasivaPreProcesed valida_inscripcion(mx.gob.se.rug.masiva.to.string.CargaMasiva cargaMasivaString,CargaMasiva cargaMasiva){
		
		cargaMasivaPreProcesed.setCargaMasiva(cargaMasiva);
		cargaMasivaPreProcesed.setTramiteIncorrectos(new ArrayList<TramiteRes>());
		
		List<Inscripcion>inscripcionStr = cargaMasivaString.getInscripcion();
		List<mx.gob.se.rug.masiva.to.Inscripcion> inscripcion2 = cargaMasiva.getInscripcion();
		
		for (Inscripcion inscripcionString : inscripcionStr) {
			contTramites++;
			mx.gob.se.rug.masiva.to.Inscripcion inscripcion = new mx.gob.se.rug.masiva.to.Inscripcion();
			
			try{
				if(inscripcionString.getPersonaSolicitaAutoridadInstruyeAsiento() != null){
					inscripcion.setPersonaSolicitaAutoridadInstruyeAsiento(validatePersonaSolicitaAutoridadInstruyeAsiento(inscripcionString.getPersonaSolicitaAutoridadInstruyeAsiento()));
				}
				inscripcion.setPartes(validatePartes(inscripcionString.getPartes(),1));
				inscripcion.setGarantia(validateGarantia(inscripcionString.getGarantia(),1));
				inscripcion.setVigencia(validateVigencia(inscripcionString.getVigencia()));				
				inscripcion.setIdentificador(validateIdentificador(inscripcionString.getIdentificador()));
				
				inscripcion2.add(inscripcion); // LLenamos el objeto preprocesado
			}catch (CargaMasivaException e) {
				cargaMasivaPreProcesed.getTramiteIncorrectos().add(e.getTramiteIncorrecto(inscripcionString.getIdentificador()));
			}
		}
		cargaMasivaPreProcesed.setTotalTramites(contTramites);
		return cargaMasivaPreProcesed;
	}
	
	public CargaMasivaPreProcesed valida_traslado(mx.gob.se.rug.masiva.to.string.CargaMasiva cargaMasivaString,CargaMasiva cargaMasiva){
		
		cargaMasivaPreProcesed.setCargaMasiva(cargaMasiva);
		cargaMasivaPreProcesed.setTramiteIncorrectos(new ArrayList<TramiteRes>());
		
		List<Inscripcion>inscripcionStr = cargaMasivaString.getTraslado();
		List<mx.gob.se.rug.masiva.to.Inscripcion> inscripcion2 = cargaMasiva.getTraslado();
		
		System.out.println("valida-traslado********************" + inscripcionStr.size());
		
		for (Inscripcion inscripcionString : inscripcionStr) {
			contTramites++;
			mx.gob.se.rug.masiva.to.Inscripcion inscripcion = new mx.gob.se.rug.masiva.to.Inscripcion();
			
			try{
				if(inscripcionString.getPersonaSolicitaAutoridadInstruyeAsiento() != null){
					inscripcion.setPersonaSolicitaAutoridadInstruyeAsiento(validatePersonaSolicitaAutoridadInstruyeAsiento(inscripcionString.getPersonaSolicitaAutoridadInstruyeAsiento()));
				}
				inscripcion.setPartes(validatePartes(inscripcionString.getPartes(),31));
				inscripcion.setGarantia(validateGarantia(inscripcionString.getGarantia(),31));
				inscripcion.setVigencia(validateVigencia(inscripcionString.getVigencia()));
				inscripcion.setIdentificador(validateIdentificador(inscripcionString.getIdentificador()));
				
				inscripcion2.add(inscripcion); // LLenamos el objeto preprocesado
			}catch (CargaMasivaException e) {
				e.printStackTrace();
				cargaMasivaPreProcesed.getTramiteIncorrectos().add(e.getTramiteIncorrecto(inscripcionString.getIdentificador()));
			}
		}
		cargaMasivaPreProcesed.setTotalTramites(contTramites);
		return cargaMasivaPreProcesed;
	}

	public CargaMasivaPreProcesed valida_transmision(mx.gob.se.rug.masiva.to.string.CargaMasiva cargaMasivaString,CargaMasiva cargaMasiva){
		
		cargaMasivaPreProcesed.setCargaMasiva(cargaMasiva);
		cargaMasivaPreProcesed.setTramiteIncorrectos(new ArrayList<TramiteRes>());
		
		List<Transmision>transmisionStr = cargaMasivaString.getTransmision();
		List<mx.gob.se.rug.masiva.to.Transmision> transmision2 = cargaMasiva.getTransmision();
		
		for (Transmision transmisionString : transmisionStr) {
			contTramites++;
			mx.gob.se.rug.masiva.to.Transmision transmision = new mx.gob.se.rug.masiva.to.Transmision();
			
			try{
				if(transmisionString.getPersonaSolicitaAutoridadInstruyeAsiento() != null){
					transmision.setPersonaSolicitaAutoridadInstruyeAsiento(validatePersonaSolicitaAutoridadInstruyeAsiento(transmisionString.getPersonaSolicitaAutoridadInstruyeAsiento()));
				}
				if(transmisionString.getAcreedor() != null){
					transmision.setAcreedor(validateAcreedor(transmisionString.getAcreedor()));
				}
				if(transmisionString.getEliminarPartesTransmision() != null){
					transmision.setEliminarPartesTransmision(validateEliminarPartesTransmision(transmisionString.getEliminarPartesTransmision()));
				}
				transmision.setConvenio(validateConvenio(transmisionString.getConvenio()));
				//transmision.setIdentificadorGarantia(validateIdentificadorGarantia(transmisionString.getIdentificadorGarantia()));
				
				otorgantes = transmision.getOtorgante();
				Iterator<mx.gob.se.rug.masiva.to.string.Otorgante> iterator= transmisionString.getOtorgante().iterator();
				while(iterator.hasNext()) {
					mx.gob.se.rug.masiva.to.string.Otorgante otorgante = (mx.gob.se.rug.masiva.to.string.Otorgante) iterator.next();
					otorgantes.add(validateOtorgante(otorgante));
				}
				
				acreedorAdicionals = transmision.getAcreedorAdicional();
				Iterator<mx.gob.se.rug.masiva.to.string.AcreedorAdicional> iterator2 = transmisionString.getAcreedorAdicional().iterator();
				while(iterator2.hasNext()) {
					mx.gob.se.rug.masiva.to.string.AcreedorAdicional acreedorAdicional = (mx.gob.se.rug.masiva.to.string.AcreedorAdicional) iterator2.next();
					acreedorAdicionals.add(validateAcreedorAdicional(acreedorAdicional,9));
				}
				
				transmision2.add(transmision); // LLenamos el objeto preprocesado
			}catch (CargaMasivaException e) {
				cargaMasivaPreProcesed.getTramiteIncorrectos().add(e.getTramiteIncorrecto(transmisionString.getIdentificadorGarantia().getClaveRastreo()));
			}
		}
		cargaMasivaPreProcesed.setTotalTramites(contTramites);
		return cargaMasivaPreProcesed;
	}
	
	public CargaMasivaPreProcesed valida_rectificacion_por_error(mx.gob.se.rug.masiva.to.string.CargaMasiva cargaMasivaString,CargaMasiva cargaMasiva){
		
		cargaMasivaPreProcesed.setCargaMasiva(cargaMasiva);
		cargaMasivaPreProcesed.setTramiteIncorrectos(new ArrayList<TramiteRes>());
		
		List<RectificacionPorError>rectificacionPorErrorStr = cargaMasivaString.getRectificacionPorError();
		List<mx.gob.se.rug.masiva.to.RectificacionPorError> rectificacionPorError2 = cargaMasiva.getRectificacionPorError();
		
		for (RectificacionPorError rectificacionPorErrorString : rectificacionPorErrorStr) {
			contTramites++;
			mx.gob.se.rug.masiva.to.RectificacionPorError rectificacionPorError = new mx.gob.se.rug.masiva.to.RectificacionPorError();
			
			try{
				if(rectificacionPorErrorString.getPersonaSolicitaAutoridadInstruyeAsiento() != null){
					rectificacionPorError.setPersonaSolicitaAutoridadInstruyeAsiento(validatePersonaSolicitaAutoridadInstruyeAsiento(rectificacionPorErrorString.getPersonaSolicitaAutoridadInstruyeAsiento()));
				}
				if(rectificacionPorErrorString.getAcreedor() != null){
					rectificacionPorError.setAcreedor(validateAcreedor(rectificacionPorErrorString.getAcreedor()));
				}
				if(rectificacionPorErrorString.getEliminarPartesRectificacion() != null){
					rectificacionPorError.setEliminarPartesRectificacion(validateEliminarPartesRectificacion(rectificacionPorErrorString.getEliminarPartesRectificacion()));
				}
				if(rectificacionPorErrorString.getPartesRectificacion() != null){
					rectificacionPorError.setPartesRectificacion(validatePartesRectificacion(rectificacionPorErrorString.getPartesRectificacion()));
				}
				if(rectificacionPorErrorString.getDatosGarantia() != null){
					rectificacionPorError.setDatosGarantia(validateDatosGarantia(rectificacionPorErrorString.getDatosGarantia()));
				}
				if(rectificacionPorErrorString.getObligacionGarantiza() != null){
					rectificacionPorError.setObligacionGarantiza(validateObligacionGarantiza(rectificacionPorErrorString.getObligacionGarantiza()));
				}
				//rectificacionPorError.setIdentificadorGarantia(validateIdentificadorGarantia(rectificacionPorErrorString.getIdentificadorGarantia()));

				rectificacionPorError2.add(rectificacionPorError); // LLenamos el objeto preprocesado
			}catch (CargaMasivaException e) {
				cargaMasivaPreProcesed.getTramiteIncorrectos().add(e.getTramiteIncorrecto(rectificacionPorErrorString.getIdentificadorGarantia().getClaveRastreo()));
			}
		}
		cargaMasivaPreProcesed.setTotalTramites(contTramites);
		
		return cargaMasivaPreProcesed;
	}
	
	public CargaMasivaPreProcesed valida_aviso_preventivo(mx.gob.se.rug.masiva.to.string.CargaMasiva cargaMasivaString,CargaMasiva cargaMasiva){
		
		cargaMasivaPreProcesed.setCargaMasiva(cargaMasiva);
		cargaMasivaPreProcesed.setTramiteIncorrectos(new ArrayList<TramiteRes>());
		
		List<AvisoPreventivo>avisoPreventivoStr = cargaMasivaString.getAvisoPreventivo();
		List<mx.gob.se.rug.masiva.to.AvisoPreventivo> avisoPreventivo2 = cargaMasiva.getAvisoPreventivo();
		
		for (AvisoPreventivo avisoPreventivoString : avisoPreventivoStr) {
			contTramites++;
			mx.gob.se.rug.masiva.to.AvisoPreventivo avisoPreventivo = new mx.gob.se.rug.masiva.to.AvisoPreventivo();
			
			try{
				if(avisoPreventivoString.getPersonaSolicitaAutoridadInstruyeAsiento() != null){
					avisoPreventivo.setPersonaSolicitaAutoridadInstruyeAsiento(validatePersonaSolicitaAutoridadInstruyeAsiento(avisoPreventivoString.getPersonaSolicitaAutoridadInstruyeAsiento()));
				}
				
				avisoPreventivo.setOtorgante(validateOtorgante(avisoPreventivoString.getOtorgante()));
				avisoPreventivo.setDatosAvisoPreventivo(validateDatosAvisoPreventivo(avisoPreventivoString.getDatosAvisoPreventivo()));
				avisoPreventivo.setIdentificador(validateIdentificador(avisoPreventivoString.getIdentificador()));
				
				avisoPreventivo2.add(avisoPreventivo); // LLenamos el objeto preprocesado
			}catch (CargaMasivaException e) {
				cargaMasivaPreProcesed.getTramiteIncorrectos().add(e.getTramiteIncorrecto(avisoPreventivoString.getIdentificador().getClaveRastreo()));
			}
		}
		cargaMasivaPreProcesed.setTotalTramites(contTramites);
		return cargaMasivaPreProcesed;
	}
	
	public CargaMasivaPreProcesed valida_anotacion(mx.gob.se.rug.masiva.to.string.CargaMasiva cargaMasivaString,CargaMasiva cargaMasiva){
		
		cargaMasivaPreProcesed.setCargaMasiva(cargaMasiva);
		cargaMasivaPreProcesed.setTramiteIncorrectos(new ArrayList<TramiteRes>());
		
		List<Anotacion>anotacionStr = cargaMasivaString.getAnotacion();
		List<mx.gob.se.rug.masiva.to.Anotacion> anotacion2 = cargaMasiva.getAnotacion();
		
		for (Anotacion anotacionString : anotacionStr) {
			contTramites++;
			mx.gob.se.rug.masiva.to.Anotacion anotacion = new mx.gob.se.rug.masiva.to.Anotacion();
			
			try{
				anotacion.setIdentificador(validateIdentificador(anotacionString.getIdentificador()));				
				anotacion.setPersonaSolicitaAutoridadInstruyeAsiento(validatePersonaSolicitaAutoridadInstruyeAsiento(anotacionString.getPersonaSolicitaAutoridadInstruyeAsiento()));				
				anotacion.setOtorgante(validateOtorgante(anotacionString.getOtorgante()));
				anotacion.setVigencia(validateVigencia(anotacionString.getVigencia()));
				anotacion.setDatosAnotacion(validateDatosAnotacion(anotacionString.getDatosAnotacion()));
				
				anotacion2.add(anotacion); // LLenamos el objeto preprocesado
			}catch (CargaMasivaException e) {
				cargaMasivaPreProcesed.getTramiteIncorrectos().add(e.getTramiteIncorrecto(anotacionString.getIdentificador().getClaveRastreo()));
			}
		}
		cargaMasivaPreProcesed.setTotalTramites(contTramites);
		return cargaMasivaPreProcesed;
	}
	
	public CargaMasivaPreProcesed valida_anotacion_garantia(mx.gob.se.rug.masiva.to.string.CargaMasiva cargaMasivaString,CargaMasiva cargaMasiva){
		
		cargaMasivaPreProcesed.setCargaMasiva(cargaMasiva);
		cargaMasivaPreProcesed.setTramiteIncorrectos(new ArrayList<TramiteRes>());
		
		List<AnotacionGarantia>anotacionGarantiaStr = cargaMasivaString.getAnotacionGarantia();
		List<mx.gob.se.rug.masiva.to.AnotacionGarantia> anotacionGarantia2 = cargaMasiva.getAnotacionGarantia();
		
		for (AnotacionGarantia anotacionGarantiaString : anotacionGarantiaStr) {
			contTramites++;
			mx.gob.se.rug.masiva.to.AnotacionGarantia anotacionGarantia = new mx.gob.se.rug.masiva.to.AnotacionGarantia();
			
			try{
				//anotacionGarantia.setIdentificadorGarantia(validateIdentificadorGarantia(anotacionGarantiaString.getIdentificadorGarantia()));
				anotacionGarantia.setPersonaSolicitaAutoridadInstruyeAsiento(validatePersonaSolicitaAutoridadInstruyeAsiento(anotacionGarantiaString.getPersonaSolicitaAutoridadInstruyeAsiento()));
				anotacionGarantia.setVigencia(validateVigencia(anotacionGarantiaString.getVigencia()));
				anotacionGarantia.setDatosAnotacion(validateDatosAnotacion(anotacionGarantiaString.getDatosAnotacion()));
				
				anotacionGarantia2.add(anotacionGarantia); // LLenamos el objeto preprocesado
			}catch (CargaMasivaException e) {
				cargaMasivaPreProcesed.getTramiteIncorrectos().add(e.getTramiteIncorrecto(anotacionGarantiaString.getIdentificadorGarantia().getClaveRastreo()));
			}
		}
		cargaMasivaPreProcesed.setTotalTramites(contTramites);
		return cargaMasivaPreProcesed;
	}
	
	public CargaMasivaPreProcesed valida_acreedores(mx.gob.se.rug.masiva.to.string.CargaMasiva cargaMasivaString,CargaMasiva cargaMasiva){
		
		cargaMasivaPreProcesed.setCargaMasiva(cargaMasiva);
		cargaMasivaPreProcesed.setTramiteIncorrectos(new ArrayList<TramiteRes>());
		
		mx.gob.se.rug.masiva.to.Acreedores acreedoresTo = new mx.gob.se.rug.masiva.to.Acreedores();
		acreedorAutoridad = acreedoresTo.getAcreedorAutoridad();

		List<mx.gob.se.rug.masiva.to.string.AcreedorAutoridad> acreedorAutoridadString = cargaMasivaString.getAcreedores().getAcreedorAutoridad();
		
		for(mx.gob.se.rug.masiva.to.string.AcreedorAutoridad acreedorAutoridadStr : acreedorAutoridadString){
			contTramites++;
			try{
				acreedorAutoridad.add(validateAcreedorAutoridad(acreedorAutoridadStr));
			}catch (CargaMasivaException e) {
					cargaMasivaPreProcesed.getTramiteIncorrectos().add(e.getTramiteIncorrecto(getClaveRastreoAcreedor(acreedorAutoridadStr)));
			}
		}
		cargaMasiva.setAcreedores(acreedoresTo);
		cargaMasivaPreProcesed.setTotalTramites(contTramites);
		return cargaMasivaPreProcesed;
	}
	
	public String getClaveRastreoAcreedor(mx.gob.se.rug.masiva.to.string.AcreedorAutoridad acreedorAutoridad){
		String claveRastreoAcreedor= "No clave";
		if(acreedorAutoridad.getRfc() != null&& !acreedorAutoridad.getRfc().trim().equalsIgnoreCase("")){
			claveRastreoAcreedor=acreedorAutoridad.getRfc();
		}else if(acreedorAutoridad.getDenominacionRazonSocial() != null&&! acreedorAutoridad.getDenominacionRazonSocial().trim().equalsIgnoreCase("")){
			claveRastreoAcreedor=acreedorAutoridad.getDenominacionRazonSocial();
		}else if(acreedorAutoridad.getNombre() != null&& !acreedorAutoridad.getNombre().trim().equalsIgnoreCase("")){
			claveRastreoAcreedor=acreedorAutoridad.getNombre();
			if(acreedorAutoridad.getApellidoPaterno() != null&& !acreedorAutoridad.getApellidoPaterno().trim().equalsIgnoreCase("")){
				claveRastreoAcreedor=claveRastreoAcreedor+" "+acreedorAutoridad.getApellidoPaterno();
			}
		}else if(acreedorAutoridad.getCorreoElectronico() != null&& !acreedorAutoridad.getCorreoElectronico().trim().equalsIgnoreCase("")){
			claveRastreoAcreedor=acreedorAutoridad.getCorreoElectronico();
		}
		return claveRastreoAcreedor;
	}
	//Metodos Genericos de Validacion

	public AcreedorAutoridad validateAcreedorAutoridad(mx.gob.se.rug.masiva.to.string.AcreedorAutoridad acreedorAutoridadStr) throws CargaMasivaException{		
		AcreedorAutoridad acreedorAutoridadTo = new AcreedorAutoridad();
		acreedorAutoridadTo.setIdNacionalidad(stringToBigInteger(acreedorAutoridadStr.getIdNacionalidad(),1,9000000,"id-nacionalidad",true));
		acreedorAutoridadTo.setTipoPersona(stringValidate(acreedorAutoridadStr.getTipoPersona(),1,1000,"tipo-persona",true));
		acreedorAutoridadTo.setDenominacionRazonSocial(stringValidate(acreedorAutoridadStr.getDenominacionRazonSocial(),1,1000,"denominacion-razon-social",false));
		acreedorAutoridadTo.setNombre(stringValidate(acreedorAutoridadStr.getNombre(),1,1000,"nombre",false));
		acreedorAutoridadTo.setApellidoPaterno(stringValidate(acreedorAutoridadStr.getApellidoPaterno(),1,1000,"apellido-paterno",false));
		acreedorAutoridadTo.setApellidoMaterno(stringValidate(acreedorAutoridadStr.getApellidoMaterno(),1,1000,"apellido-materno",false));
		acreedorAutoridadTo.setTelefono(stringToBigInteger(acreedorAutoridadStr.getTelefono(),1,90000000,"telefono",true));
		acreedorAutoridadTo.setTelefonoExtension(stringToBigInteger(acreedorAutoridadStr.getTelefonoExtension(),1,9000000,"telefono-extension",false));
		acreedorAutoridadTo.setCorreoElectronico(stringValidate(acreedorAutoridadStr.getCorreoElectronico(),1,1000,"correo-electronico",false));
		acreedorAutoridadTo.setCalle(stringValidate(acreedorAutoridadStr.getCalle(),1,1000,"calle",false));
		acreedorAutoridadTo.setNumeroExterior(stringValidate(acreedorAutoridadStr.getNumeroExterior(),1,1000,"numero-exterior",false));
		acreedorAutoridadTo.setNumeroInterior(stringValidate(acreedorAutoridadStr.getNumeroInterior(),1,1000,"numero-interior",false));
		acreedorAutoridadTo.setIdColonia(stringToBigInteger(acreedorAutoridadStr.getIdColonia(),1,9000000,"id-colonia",false));
		acreedorAutoridadTo.setIdLocalidad(stringToBigInteger(acreedorAutoridadStr.getIdLocalidad(),1,9000000,"id-localidad",false));
		acreedorAutoridadTo.setIdPaisResidencia(stringToBigInteger(acreedorAutoridadStr.getIdPaisResidencia(),1,9000000,"id-pais-residencia",true));
		acreedorAutoridadTo.setDomicilioExtranjeroUno(stringValidate(acreedorAutoridadStr.getDomicilioExtranjeroUno(),1,1000,"domicilio-extranjero-uno",false));
		acreedorAutoridadTo.setDomicilioExtranjeroDos(stringValidate(acreedorAutoridadStr.getDomicilioExtranjeroDos(),1,1000,"domicilio-extranjero-dos",false));
		acreedorAutoridadTo.setPoblacion(stringValidate(acreedorAutoridadStr.getPoblacion(),1,1000,"poblacion",false));
		acreedorAutoridadTo.setZonaPostal(stringValidate(acreedorAutoridadStr.getZonaPostal(),1,1000,"zona-postal",false));
		acreedorAutoridadTo.setRfc(stringValidate(acreedorAutoridadStr.getRfc(),1,1000,"rfc",false));
		return acreedorAutoridadTo;
	}
	
	public DatosAnotacion validateDatosAnotacion(mx.gob.se.rug.masiva.to.string.DatosAnotacion datosAnotacionString) throws CargaMasivaException{
		DatosAnotacion datosAnotacionTo = new DatosAnotacion();
		datosAnotacionTo.setContenidoResolucion(stringValidate(datosAnotacionString.getContenidoResolucion(),0,1000,"contenido-resolucion",true));
		return datosAnotacionTo;
	}
	
	public DatosAvisoPreventivo validateDatosAvisoPreventivo(mx.gob.se.rug.masiva.to.string.DatosAvisoPreventivo datosAvisoPreventivoString) throws CargaMasivaException{
		DatosAvisoPreventivo datosAvisoPreventivoTo = new DatosAvisoPreventivo();
		datosAvisoPreventivoTo.setDescripcionBienesMuebles(stringValidate(datosAvisoPreventivoString.getDescripcionBienesMuebles(),0,4000000,"descripcion-bienes-muebles",true));
		return datosAvisoPreventivoTo;
	}
	
	public Identificador validateIdentificador(Object identificadorString) throws CargaMasivaException{
		Identificador identificadorTo = new Identificador();
		identificadorTo.setClaveRastreo(stringValidate((String) identificadorString,0,1000,"clave-rastreo",true));
		return identificadorTo;
	}

	public Vigencia validateVigencia(Object vigenciaString) throws CargaMasivaException{
		Vigencia vigenciaTo = new Vigencia();
		vigenciaTo.setMeses(validarVigencia(vigenciaString.toString(),1));
		return vigenciaTo;
	}
	
	public Garantia validateGarantia(mx.gob.se.rug.masiva.to.string.Garantia garantiaString, Integer pTramite) throws CargaMasivaException{
		Garantia garantiaTo = new Garantia();				
		
		Creacion creacionTo = new Creacion();		
		creacionTo.setIdTipoGarantia(stringToBigInteger(null,1,9000000,"id-tipo-garantia",false));
		//creacionTo.setFechaCelebracion(stringToXMLGregorianCalendar(null,"fecha-celebracion",false));
		creacionTo.setMontoMaximo(stringToDouble(null,1,new BigInteger("99999999999999999999"),"monto-maximo",false));
		creacionTo.setIdMoneda(stringToBigInteger(null,1,9000000,"id-moneda",false));
		
		if(pTramite==7) {
			if(garantiaString==null) {
				return garantiaTo;
			}
			creacionTo.setDescripcionBienesMuebles(stringValidate(garantiaString.getDescripcionBienesMuebles(),0,4000000,"descripcion-bienes-muebles",false));
			creacionTo.setTerminosCondiciones(stringValidate(garantiaString.getTerminosCondiciones(),0,1000,"observaciones-adicionales",false));
			creacionTo.setDatosInstrumentoPublico(stringValidate(garantiaString.getDatosInstrumentoPublico(),0,1000,"info-general-contrato",false));
			creacionTo.setbDatosModificables(stringToBoolean(garantiaString.getbDatosModificables(),"b-atribuibles-no-afectos",false));
			creacionTo.setbNoBienesOtorgados(stringToBoolean(garantiaString.getbNoBienesOtorgados(),"b-no-bienes-otorgados",false));
			creacionTo.setbEsPrioritaria(stringToBoolean(garantiaString.getbEsPrioritaria(),"b-es-prioritaria",false));
			creacionTo.setbEnOtroRegistro(stringToBoolean(garantiaString.getbEnOtroRegistro(),"b-en-otro-registro",false));
		} else {			
			creacionTo.setDescripcionBienesMuebles(stringValidate(garantiaString.getDescripcionBienesMuebles(),0,4000000,"descripcion-bienes-muebles",true));
			creacionTo.setTerminosCondiciones(stringValidate(garantiaString.getTerminosCondiciones(),0,1000,"observaciones-adicionales",true));
			creacionTo.setDatosInstrumentoPublico(stringValidate(garantiaString.getDatosInstrumentoPublico(),0,1000,"info-general-contrato",true));
			creacionTo.setbDatosModificables(stringToBoolean(garantiaString.getbDatosModificables(),"b-atribuibles-no-afectos",true));
			creacionTo.setbNoBienesOtorgados(stringToBoolean(garantiaString.getbNoBienesOtorgados(),"b-no-bienes-otorgados",true));
			creacionTo.setbEsPrioritaria(stringToBoolean(garantiaString.getbEsPrioritaria(),"b-es-prioritaria",true));
			creacionTo.setbEnOtroRegistro(stringToBoolean(garantiaString.getbEnOtroRegistro(),"b-en-otro-registro",true));
		}
		
		if(pTramite==31) {
			stringValidate(garantiaString.getFechaCelebracion(),0,1000,"fecha-registro",true);
			stringValidate(garantiaString.getInfoRegistro(),0,1000,"info-registro",true);
			creacionTo.setInfoRegistro(garantiaString.getInfoRegistro() + " Fecha registro: " + garantiaString.getFechaCelebracion());
		} else {		
			if(creacionTo.getbEnOtroRegistro() !=null && creacionTo.getbEnOtroRegistro()){		
				creacionTo.setInfoRegistro(stringValidate(garantiaString.getInfoRegistro(),0,1000,"info-registro",true));
			} else {
				creacionTo.setInfoRegistro(stringValidate(garantiaString.getInfoRegistro(),0,1000,"info-registro",false));
			}
		}
		
		garantiaTo.setCreacion(creacionTo);
		/*if(garantiaString.getCreacion().getTipoBienMueble() != null) {
			tipoBienMuebleTo = creacionTo.getTipoBienMueble();
			Iterator<mx.gob.se.rug.masiva.to.string.TipoBienMueble> iterator= garantiaString.getCreacion().getTipoBienMueble().iterator();
			while(iterator.hasNext()) {
				mx.gob.se.rug.masiva.to.string.TipoBienMueble tipoBienMueble = iterator.next();
				tipoBienMuebleTo.add(validateTipoBienMueble(tipoBienMueble));
			}
		}*/
		
		if(garantiaString.getBienEspecial() != null) {
			bienesEspeciales = creacionTo.getBienesEspeciales().getBienEspecial();
			Iterator<BienEspecial> itBien = garantiaString.getBienEspecial().iterator();
			while(itBien.hasNext()) {
				BienEspecial tBienEspecial = itBien.next();
				bienesEspeciales.add(validateBienEspecial(tBienEspecial, pTramite));
			}
		}
		
		Obligacion obligacionTo = new Obligacion();
		
		if(garantiaString.getTerminos()!=null){	
			//obligacionTo.setActoContrato(stringValidate(garantiaString.getObligacion().getActoContrato(),0,1000,"acto-contrato",false));
			//obligacionTo.setFechaCelebracion(stringToXMLGregorianCalendar(garantiaString.getObligacion().getFechaCelebracion(),"fecha-celebracion",false));
			//obligacionTo.setFechaTerminacion(stringToXMLGregorianCalendar(garantiaString.getObligacion().getFechaTerminacion(),"fecha-terminacion",false));
			obligacionTo.setTerminos(stringValidate(garantiaString.getTerminos(),0,1000,"terminos",false));
		}
		
		garantiaTo.setObligacion(obligacionTo);
		
		return garantiaTo;
	}
	
	
	public Partes validatePartes(mx.gob.se.rug.masiva.to.string.Partes partesString, Integer pTramite) throws CargaMasivaException{
		Partes partesTo = new Partes();
		
		if(pTramite==7 && partesString==null) {
			return partesTo;
		}
		
		otorgantes = partesTo.getOtorgante();		
		Iterator<mx.gob.se.rug.masiva.to.string.Otorgante> iterator = partesString.getOtorgante().iterator();
		while(iterator.hasNext()) {
			mx.gob.se.rug.masiva.to.string.Otorgante otorgante = iterator.next();
			otorgantes.add(validateOtorgante(otorgante));
		}
		
		deudors = partesTo.getDeudor();		
		Iterator<mx.gob.se.rug.masiva.to.string.Deudor> iterator2 = partesString.getDeudor().iterator();
		while(iterator2.hasNext()) {
			mx.gob.se.rug.masiva.to.string.Deudor deudor = iterator2.next();
			deudors.add(validateDeudor(deudor, pTramite));
		}
		
		acreedorAdicionals = partesTo.getAcreedorAdicional();
		Iterator<mx.gob.se.rug.masiva.to.string.AcreedorAdicional> iterator3 = partesString.getAcreedorAdicional().iterator();
		while(iterator3.hasNext()) {
			mx.gob.se.rug.masiva.to.string.AcreedorAdicional acreedorAdicional = iterator3.next();
			acreedorAdicionals.add(validateAcreedorAdicional(acreedorAdicional, pTramite));
		}
		
		return partesTo;
	}
	
	public ObligacionGarantiza validateObligacionGarantiza(mx.gob.se.rug.masiva.to.string.ObligacionGarantiza obligacionGarantizaString) throws CargaMasivaException{
		ObligacionGarantiza obligacionGarantizaTo = new ObligacionGarantiza();
		obligacionGarantizaTo.setActoContrato(stringValidate(obligacionGarantizaString.getActoContrato(),0,1000,"acto-contrato",false));
		obligacionGarantizaTo.setFechaTerminacion(stringToXMLGregorianCalendar(obligacionGarantizaString.getFechaTerminacion(),"fecha-terminacion",false));
		obligacionGarantizaTo.setFechaCelebracion(stringToXMLGregorianCalendar(obligacionGarantizaString.getFechaCelebracion(),"fecha-celbracion",false));
		obligacionGarantizaTo.setTerminos(stringValidate(obligacionGarantizaString.getTerminos(),0,1000,"terminos",false));
		return obligacionGarantizaTo;
	}
	
	public DatosGarantia validateDatosGarantia(mx.gob.se.rug.masiva.to.string.DatosGarantia datosGarantiaString) throws CargaMasivaException{
		DatosGarantia datosGarantiaTo = new DatosGarantia();
		datosGarantiaTo.setIdTipoGarantia(stringToBigInteger(datosGarantiaString.getIdTipoGarantia(),1,9000000,"id-tipo-garantia",false));
		datosGarantiaTo.setFechaCelebracion(stringToXMLGregorianCalendar(datosGarantiaString.getFechaCelebracion(),"fecha-celebracion",false));
		datosGarantiaTo.setMontoMaximo(stringToDouble(datosGarantiaString.getMontoMaximo(),1,new BigInteger("99999999999999999999"),"monto-maximo",false));
		datosGarantiaTo.setIdMoneda(stringToBigInteger(datosGarantiaString.getIdMoneda(),1,9000000,"id-moneda",false));
		datosGarantiaTo.setDescripcionBienesMuebles(stringValidate(datosGarantiaString.getDescripcionBienesMuebles(),0,4000000,"descripcion-bienes-muebles",false));
		datosGarantiaTo.setTerminosCondiciones(stringValidate(datosGarantiaString.getTerminosCondiciones(),0,1000,"terminos-condiciones",false));
		datosGarantiaTo.setDatosInstrumentoPublico(stringValidate(datosGarantiaString.getDatosInstrumentoPublico(),0,1000,"datos-instrumento-publico",false));
		datosGarantiaTo.setBDatosModificables(stringToBoolean(datosGarantiaString.getBDatosModificables(),"b-datos-modificables",false));
		
		tipoBienMuebleTo = datosGarantiaTo.getTipoBienMueble();
		Iterator<mx.gob.se.rug.masiva.to.string.TipoBienMueble> iterator= datosGarantiaString.getTipoBienMueble().iterator();
		while(iterator.hasNext()) {
			mx.gob.se.rug.masiva.to.string.TipoBienMueble tipoBienMueble = iterator.next();
			tipoBienMuebleTo.add(validateTipoBienMueble(tipoBienMueble));
		}
		
		return datosGarantiaTo;
	}
	
	public PartesRectificacion validatePartesRectificacion(mx.gob.se.rug.masiva.to.string.PartesRectificacion partesRectificacionString) throws CargaMasivaException{
		PartesRectificacion partesRectificacionTo = new PartesRectificacion();
		
		otorgantes = partesRectificacionTo.getOtorgante();		
		Iterator<mx.gob.se.rug.masiva.to.string.Otorgante> iterator = partesRectificacionString.getOtorgante().iterator();
		while(iterator.hasNext()) {
			mx.gob.se.rug.masiva.to.string.Otorgante otorgante = iterator.next();
			otorgantes.add(validateOtorgante(otorgante));
		}
		
		deudors = partesRectificacionTo.getDeudor();		
		Iterator<mx.gob.se.rug.masiva.to.string.Deudor> iterator2 = partesRectificacionString.getDeudor().iterator();
		while(iterator2.hasNext()) {
			mx.gob.se.rug.masiva.to.string.Deudor deudor = iterator2.next();
			deudors.add(validateDeudor(deudor,8));
		}
		
		acreedorAdicionals = partesRectificacionTo.getAcreedorAdicional();
		Iterator<mx.gob.se.rug.masiva.to.string.AcreedorAdicional> iterator3 = partesRectificacionString.getAcreedorAdicional().iterator();
		while(iterator3.hasNext()) {
			mx.gob.se.rug.masiva.to.string.AcreedorAdicional acreedorAdicional = iterator3.next();
			acreedorAdicionals.add(validateAcreedorAdicional(acreedorAdicional,8));
		}
		
		return partesRectificacionTo;
	}
	
	
	public EliminarPartesRectificacion validateEliminarPartesRectificacion(mx.gob.se.rug.masiva.to.string.EliminarPartesRectificacion eliminarPartesRectificacionString) throws CargaMasivaException{
		EliminarPartesRectificacion eliminarPartesRectificacionTo = new EliminarPartesRectificacion();
		eliminarPartesRectificacionTo.setEliminaOtorgantes(stringToBoolean(eliminarPartesRectificacionString.getEliminaOtorgantes(),"elimina-otorgantes",false));
		eliminarPartesRectificacionTo.setEliminaDeudores(stringToBoolean(eliminarPartesRectificacionString.getEliminaDeudores(),"elimina-deudores",false));
		eliminarPartesRectificacionTo.setEliminaAcreedorAdicional(stringToBoolean(eliminarPartesRectificacionString.getEliminaAcreedorAdicional(),"elimina-acreedor-adicional",false));		
		return eliminarPartesRectificacionTo;
	}
	
	public AcreedorAdicional validateAcreedorAdicional(mx.gob.se.rug.masiva.to.string.AcreedorAdicional acreedorAdicionalString, Integer pTramite) throws CargaMasivaException{
		AcreedorAdicional acreedorAdicionalTo = new AcreedorAdicional();
		acreedorAdicionalTo.setTipoPersona(stringValidate(acreedorAdicionalString.getTipoPersona(),1,1000,"tipo-persona",true));
		if(pTramite==7) {
			acreedorAdicionalTo.setOperacion(stringToBigInteger(acreedorAdicionalString.getOperacion(),1,9000000,"operacion",false));
			if(acreedorAdicionalTo.getTipoPersona().equals("PF")) {
				acreedorAdicionalTo.setRfc(stringValidate(acreedorAdicionalString.getRfc(),1,1000,"nit",false));
				acreedorAdicionalTo.setCurp(stringValidate(acreedorAdicionalString.getCurp(),1,1000,"identificador",true));
			} else if(acreedorAdicionalTo.getTipoPersona().equals("PM")){ //Persona Moral 
				acreedorAdicionalTo.setRfc(stringValidate(acreedorAdicionalString.getRfc(),1,1000,"nit",true));
				acreedorAdicionalTo.setCurp(stringValidate(acreedorAdicionalString.getCurp(),1,1000,"identificador",false));
			} else {
				throw new CargaMasivaException("El tipo de persona no es correcto");
			}
			acreedorAdicionalTo.setIdNacionalidad(stringToBigInteger(acreedorAdicionalString.getIdNacionalidad(),1,9000000,"id-nacionalidad",false));
			acreedorAdicionalTo.setDenominacionRazonSocial(stringValidate(acreedorAdicionalString.getDenominacionRazonSocial(),1,1000,"denominacion-razon-social",false));
			acreedorAdicionalTo.setNombre(stringValidate(acreedorAdicionalString.getNombre(),1,1000,"nombre",false));
			acreedorAdicionalTo.setApellidoPaterno(stringValidate(acreedorAdicionalString.getApellidoPaterno(),1,1000,"apellido-paterno",false));
			acreedorAdicionalTo.setApellidoMaterno(stringValidate(acreedorAdicionalString.getApellidoMaterno(),1,1000,"apellido-materno",false));
			acreedorAdicionalTo.setTelefono(stringValidate(acreedorAdicionalString.getTelefono(),1,1000,"telefono",false));
			acreedorAdicionalTo.setTelefonoExtension(stringValidate(acreedorAdicionalString.getTelefonoExtension(),1,1000,"telefono-extension",false));		
			acreedorAdicionalTo.setCalle(stringValidate(acreedorAdicionalString.getCalle(),1,1000,"calle",false));
			acreedorAdicionalTo.setNumeroExterior(stringValidate(acreedorAdicionalString.getNumeroExterior(),1,1000,"numero-exterior",false));
			acreedorAdicionalTo.setNumeroInterior(stringValidate(acreedorAdicionalString.getNumeroInterior(),1,1000,"numero-interior",false));
			acreedorAdicionalTo.setIdColonia(stringToBigInteger(acreedorAdicionalString.getIdColonia(),1,9000000,"id-colonia",false));
			acreedorAdicionalTo.setIdLocalidad(stringToBigInteger(acreedorAdicionalString.getIdLocalidad(),1,9000000,"id-localidad",false));
			acreedorAdicionalTo.setIdPaisResidencia(stringToBigInteger(acreedorAdicionalString.getIdPaisResidencia(),1,9000000,"id-pais-residencia",false));		
			acreedorAdicionalTo.setDomicilioExtranjeroDos(stringValidate(acreedorAdicionalString.getDomicilioExtranjeroDos(),1,1000,"domicilio-extranjero-dos",false));
			acreedorAdicionalTo.setPoblacion(stringValidate(acreedorAdicionalString.getPoblacion(),1,1000,"poblacion",false));
			acreedorAdicionalTo.setZonaPostal(stringValidate(acreedorAdicionalString.getZonaPostal(),1,1000,"zona-postal",false));
					
			acreedorAdicionalTo.setCorreoElectronico(stringValidate(acreedorAdicionalString.getCorreoElectronico(),1,1000,"correo-electronico",false));
			acreedorAdicionalTo.setDomicilioExtranjeroUno(stringValidate(acreedorAdicionalString.getDomicilioExtranjeroUno(),1,1000,"domicilio",false));
			acreedorAdicionalTo.setInfoInscripcion(stringValidate(acreedorAdicionalString.getInfoInscripcion(),1,1000,"info-inscripcion",false));
		} else {
			if(acreedorAdicionalTo.getTipoPersona().equals("PF")) {
				acreedorAdicionalTo.setIdNacionalidad(stringToBigInteger(acreedorAdicionalString.getIdNacionalidad(),1,9000000,"id-nacionalidad",true));
				acreedorAdicionalTo.setDenominacionRazonSocial(stringValidate(acreedorAdicionalString.getDenominacionRazonSocial(),1,1000,"denominacion-razon-social",false));
				acreedorAdicionalTo.setNombre(stringValidate(acreedorAdicionalString.getNombre(),1,1000,"nombre",true));
				acreedorAdicionalTo.setRfc(stringValidate(acreedorAdicionalString.getRfc(),1,1000,"nit",false));
				acreedorAdicionalTo.setCurp(stringValidate(acreedorAdicionalString.getCurp(),1,1000,"identificador",true));
			} else if(acreedorAdicionalTo.getTipoPersona().equals("PM")){ //Persona Moral
				acreedorAdicionalTo.setIdNacionalidad(stringToBigInteger("1",1,9000000,"id-nacionalidad",false));
				acreedorAdicionalTo.setDenominacionRazonSocial(stringValidate(acreedorAdicionalString.getDenominacionRazonSocial(),1,1000,"denominacion-razon-social",true));
				acreedorAdicionalTo.setNombre(stringValidate(acreedorAdicionalString.getNombre(),1,1000,"nombre",false));
				acreedorAdicionalTo.setRfc(stringValidate(acreedorAdicionalString.getRfc(),1,1000,"nit",true));
				acreedorAdicionalTo.setCurp(stringValidate(acreedorAdicionalString.getCurp(),1,1000,"identificador",false));
			} else {
				throw new CargaMasivaException("El tipo de persona no es correcto");
			}
					
			acreedorAdicionalTo.setApellidoPaterno(stringValidate(acreedorAdicionalString.getApellidoPaterno(),1,1000,"apellido-paterno",false));
			acreedorAdicionalTo.setApellidoMaterno(stringValidate(acreedorAdicionalString.getApellidoMaterno(),1,1000,"apellido-materno",false));
			acreedorAdicionalTo.setTelefono(stringValidate(acreedorAdicionalString.getTelefono(),1,1000,"telefono",false));
			acreedorAdicionalTo.setTelefonoExtension(stringValidate(acreedorAdicionalString.getTelefonoExtension(),1,1000,"telefono-extension",false));		
			acreedorAdicionalTo.setCalle(stringValidate(acreedorAdicionalString.getCalle(),1,1000,"calle",false));
			acreedorAdicionalTo.setNumeroExterior(stringValidate(acreedorAdicionalString.getNumeroExterior(),1,1000,"numero-exterior",false));
			acreedorAdicionalTo.setNumeroInterior(stringValidate(acreedorAdicionalString.getNumeroInterior(),1,1000,"numero-interior",false));
			acreedorAdicionalTo.setIdColonia(stringToBigInteger(acreedorAdicionalString.getIdColonia(),1,9000000,"id-colonia",false));
			acreedorAdicionalTo.setIdLocalidad(stringToBigInteger(acreedorAdicionalString.getIdLocalidad(),1,9000000,"id-localidad",false));
			acreedorAdicionalTo.setIdPaisResidencia(stringToBigInteger(acreedorAdicionalString.getIdPaisResidencia(),1,9000000,"id-pais-residencia",false));		
			acreedorAdicionalTo.setDomicilioExtranjeroDos(stringValidate(acreedorAdicionalString.getDomicilioExtranjeroDos(),1,1000,"domicilio-extranjero-dos",false));
			acreedorAdicionalTo.setPoblacion(stringValidate(acreedorAdicionalString.getPoblacion(),1,1000,"poblacion",false));
			acreedorAdicionalTo.setZonaPostal(stringValidate(acreedorAdicionalString.getZonaPostal(),1,1000,"zona-postal",false));
					
			acreedorAdicionalTo.setCorreoElectronico(stringValidate(acreedorAdicionalString.getCorreoElectronico(),1,1000,"correo-electronico",true));
			acreedorAdicionalTo.setDomicilioExtranjeroUno(stringValidate(acreedorAdicionalString.getDomicilioExtranjeroUno(),1,1000,"domicilio",true));
			acreedorAdicionalTo.setInfoInscripcion(stringValidate(acreedorAdicionalString.getInfoInscripcion(),1,1000,"info-inscripcion",false));
		}
		
		return acreedorAdicionalTo;
	}
	
	
	public Otorgante validateOtorgante(mx.gob.se.rug.masiva.to.string.Otorgante otorganteString) throws CargaMasivaException{
		Otorgante otorganteTo = new Otorgante();
		otorganteTo.setIdNacionalidad(stringToBigInteger(otorganteString.getIdNacionalidad(),1,9000000,"id-nacionalidad",true));
		otorganteTo.setTipoPersona(stringValidate(otorganteString.getTipoPersona(),1,1000,"tipo-persona",true));
		otorganteTo.setDenominacionRazonSocial(stringValidate(otorganteString.getDenominacionRazonSocial(),1,1000,"denominacion-razon-social",false));
		otorganteTo.setNombre(stringValidate(otorganteString.getNombre(),1,1000,"nombre",false));
		otorganteTo.setApellidoPaterno(stringValidate(otorganteString.getApellidoPaterno(),1,1000,"apellido-paterno",false));
		otorganteTo.setApellidoMaterno(stringValidate(otorganteString.getApellidoMaterno(),1,1000,"apellido-materno",false));
		otorganteTo.setCurp(stringValidate(otorganteString.getCurp(),1,1000,"curp",false));
		otorganteTo.setFolioElectronico(stringValidate(otorganteString.getFolioElectronico(),1,1000,"folio-electronico",false));
		return otorganteTo;
	}
	
	public EliminarPartesTransmision validateEliminarPartesTransmision(mx.gob.se.rug.masiva.to.string.EliminarPartesTransmision eliminarPartesTransmisionString) throws CargaMasivaException{
		EliminarPartesTransmision eliminarPartesTransmisionTo = new EliminarPartesTransmision();
		eliminarPartesTransmisionTo.setEliminaAcreedorAdicional(stringToBoolean(eliminarPartesTransmisionString.getEliminaAcreedorAdicional(),"elimina-acreedor-adicional",false));
		eliminarPartesTransmisionTo.setEliminaOtorgantes(stringToBoolean(eliminarPartesTransmisionString.getEliminaOtorgantes(),"elimina-otorgantes",false));
		return eliminarPartesTransmisionTo;
	}
	
	public Acreedor validateAcreedor(mx.gob.se.rug.masiva.to.string.Acreedor acreedorString) throws CargaMasivaException{
		Acreedor acreedorTo = new Acreedor();
		acreedorTo.setIdAcreedor(stringToBigInteger(acreedorString.getIdAcreedor(),1,9000000,"id-acreedor",true));
		return acreedorTo;
	}
	
	public Deudor validateDeudor(mx.gob.se.rug.masiva.to.string.Deudor deudorString, Integer pTramite) throws CargaMasivaException{
		Deudor deudorTo = new Deudor();
		deudorTo.setTipoPersona(stringValidate(deudorString.getTipoPersona(),1,1000,"tipo-persona",true));
		if(pTramite==7) {
			deudorTo.setOperacion(stringToBigInteger(deudorString.getOperacion(),1,40,"operacion",true));
			if(deudorTo.getTipoPersona().equalsIgnoreCase("PF")) {
				deudorTo.setCurp(stringValidate(deudorString.getCurp(),1,1000,"identificador",true));
				deudorTo.setRfc(stringValidate(deudorString.getRfc(),1,1000,"nit",false));
			} else if(deudorTo.getTipoPersona().equalsIgnoreCase("PM")){
				deudorTo.setCurp(stringValidate(deudorString.getCurp(),1,1000,"identificador",false));
				deudorTo.setRfc(stringValidate(deudorString.getRfc(),1,1000,"nit",true));
			} else {
				throw new CargaMasivaException("El tipo de persona no es correcto");
			}
			deudorTo.setIdNacionalidad(stringToBigInteger(deudorString.getIdNacionalidad(),1,9000000,"id-nacionalidad",false));
			deudorTo.setDenominacionRazonSocial(stringValidate(deudorString.getDenominacionRazonSocial(),1,1000,"denominacion-razon-social",false));
			deudorTo.setNombre(stringValidate(deudorString.getNombre(),1,1000,"nombre",false));
			deudorTo.setApellidoPaterno(stringValidate(deudorString.getApellidoPaterno(),1,1000,"apellido-paterno",false));
			deudorTo.setApellidoMaterno(stringValidate(deudorString.getApellidoMaterno(),1,1000,"apellido-materno",false));
			deudorTo.setInfoInscripcion(stringValidate(deudorString.getInfoInscripcion(),1,1000,"info-inscripcion",false));		
			deudorTo.setEmail(stringValidate(deudorString.getEmail(),1,1000,"correo-electronico",false));
			deudorTo.setDomicilio(stringValidate(deudorString.getDomicilio(),1,1000,"domicilio",false));
		} else {
			if(deudorTo.getTipoPersona().equalsIgnoreCase("PF")) {
				deudorTo.setIdNacionalidad(stringToBigInteger(deudorString.getIdNacionalidad(),1,9000000,"id-nacionalidad",true));
				deudorTo.setDenominacionRazonSocial(stringValidate(deudorString.getDenominacionRazonSocial(),1,1000,"denominacion-razon-social",false));
				deudorTo.setNombre(stringValidate(deudorString.getNombre(),1,1000,"nombre",true));
				deudorTo.setCurp(stringValidate(deudorString.getCurp(),1,1000,"identificador",true));
				deudorTo.setRfc(stringValidate(deudorString.getRfc(),1,1000,"nit",false));
			} else if(deudorTo.getTipoPersona().equalsIgnoreCase("PM")){ //Persona moral
				deudorTo.setIdNacionalidad(stringToBigInteger("1",1,9000000,"id-nacionalidad",false));
				deudorTo.setDenominacionRazonSocial(stringValidate(deudorString.getDenominacionRazonSocial(),1,1000,"denominacion-razon-social",true));
				deudorTo.setNombre(stringValidate(deudorString.getNombre(),1,1000,"nombre",false));
				deudorTo.setCurp(stringValidate(deudorString.getCurp(),1,1000,"identificador",false));
				deudorTo.setRfc(stringValidate(deudorString.getRfc(),1,1000,"nit",true));
			} else {
				throw new CargaMasivaException("El tipo de persona no es correcto");
			}
			deudorTo.setApellidoPaterno(stringValidate(deudorString.getApellidoPaterno(),1,1000,"apellido-paterno",false));
			deudorTo.setApellidoMaterno(stringValidate(deudorString.getApellidoMaterno(),1,1000,"apellido-materno",false));
			deudorTo.setInfoInscripcion(stringValidate(deudorString.getInfoInscripcion(),1,1000,"info-inscripcion",false));		
			deudorTo.setEmail(stringValidate(deudorString.getEmail(),1,1000,"correo-electronico",true));
			deudorTo.setDomicilio(stringValidate(deudorString.getDomicilio(),1,1000,"domicilio",true));
		}
		
		return deudorTo;
	}
	
	public IdentificadorGarantia validateIdentificadorGarantia(String pIdGarantia, String pClaveRastreo) throws CargaMasivaException, NullPointerException{
		IdentificadorGarantia identificadorGarantia= new IdentificadorGarantia();
		identificadorGarantia.setClaveRastreo(stringValidate(pClaveRastreo,1,1000,"clave-rastreo",true));
		identificadorGarantia.setIdGarantia(stringToBigInteger(pIdGarantia,1,9000000,"id-garantia",true));
		return identificadorGarantia;
	}
	
	public PersonaSolicitaAutoridadInstruyeAsiento validatePersonaSolicitaAutoridadInstruyeAsiento(mx.gob.se.rug.masiva.to.string.PersonaSolicitaAutoridadInstruyeAsiento personaSolicitaAutoridadInstruyeAsiento) throws CargaMasivaException{
		PersonaSolicitaAutoridadInstruyeAsiento autoridadInstruyeAsiento = new PersonaSolicitaAutoridadInstruyeAsiento();
		autoridadInstruyeAsiento.setContenidoResolucion(stringValidate(personaSolicitaAutoridadInstruyeAsiento.getContenidoResolucion(),0,4000,"contenido-resolucion",true));
		return autoridadInstruyeAsiento;
	}
	
	public DatosCancelacion validateDatosCancelacion(mx.gob.se.rug.masiva.to.string.DatosCancelacion datosCancelacionString) throws CargaMasivaException{
		DatosCancelacion datosCancelacion=new DatosCancelacion();
		datosCancelacion.setFundamentoCancelacion(stringValidate(datosCancelacionString.getFundamentoCancelacion(),0,1000,"fundamento-cancelacion",false));
		return datosCancelacion;
	}
	
	
	public EliminarPartesModificacion validateEliminarPartesModificacion(mx.gob.se.rug.masiva.to.string.EliminarPartesModificacion eliminarPartesModificacionString) throws CargaMasivaException{
		EliminarPartesModificacion eliminarPartesModificacion = new EliminarPartesModificacion();
		eliminarPartesModificacion.setEliminaDeudores(stringToBoolean(eliminarPartesModificacionString.getEliminaDeudores(),"elimina-deudores",false));
		return eliminarPartesModificacion;
	}
	
	public DatosModificacion validateDatosModificacion(mx.gob.se.rug.masiva.to.string.DatosModificacion datosModificacionString) throws CargaMasivaException{
		DatosModificacion datosModificacion = new DatosModificacion();
		datosModificacion.setMontoMaximo(stringToDouble(datosModificacionString.getMontoMaximo(),1,new BigInteger("99999999999999999999"),"monto-maximo",false));
		datosModificacion.setIdMoneda(stringToBigInteger(datosModificacionString.getIdMoneda(),1,9000000,"id-moneda",false));
		datosModificacion.setDescripcionBienesMuebles(stringValidate(datosModificacionString.getDescripcionBienesMuebles(),0,4000000,"descripcion-bienes-muebles",false));
		datosModificacion.setTerminosCondiciones(stringValidate(datosModificacionString.getTerminosCondiciones(),0,1000,"terminos-condiciones",false));
		datosModificacion.setPreveCambiosBienesMueblesMonto(stringToBoolean(datosModificacionString.getPreveCambiosBienesMueblesMonto(),"preve-cambios-bienes-muebles-monto",false));
		
		tipoBienMuebleTo = datosModificacion.getTipoBienMueble();
		Iterator<mx.gob.se.rug.masiva.to.string.TipoBienMueble> iterator= datosModificacionString.getTipoBienMueble().iterator();
		
		while(iterator.hasNext()) {
			mx.gob.se.rug.masiva.to.string.TipoBienMueble tipoBienMueble = (mx.gob.se.rug.masiva.to.string.TipoBienMueble) iterator.next();
			tipoBienMuebleTo.add(validateTipoBienMueble(tipoBienMueble));
		}
		return datosModificacion;
	}
	
	public TipoBienMueble validateTipoBienMueble(mx.gob.se.rug.masiva.to.string.TipoBienMueble tipoBienMuebleString) throws CargaMasivaException{
		TipoBienMueble tipoBienMuebleTo = new TipoBienMueble();
		tipoBienMuebleTo.setId(stringToBigInteger(tipoBienMuebleString.getId(),1,9000000,"id",true));
		tipoBienMuebleTo.setDescripcion(stringValidate(tipoBienMuebleString.getDescripcion(),0,1000,"descripcion",false));
		return tipoBienMuebleTo;
	}
	
	public BienEspecial validateBienEspecial(BienEspecial pBienEspecial, Integer pTramite) throws CargaMasivaException{
		BienEspecial tBienEspecial = new BienEspecial();
		tBienEspecial.setIdentificador(stringValidate(pBienEspecial.getIdentificador(),0,1000,"identificador",true));
		if(pTramite == 7) {
			tBienEspecial.setOperacion(stringToBigInteger(pBienEspecial.getOperacion(),1,9000000,"operacion",true).toString());
			tBienEspecial.setTipoBienEspecial(stringToBigInteger(pBienEspecial.getTipoBienEspecial(),1,9000000,"tipo-bien-especial",false).toString());
			tBienEspecial.setTipoIdentificador(stringToBigInteger(pBienEspecial.getTipoIdentificador(),1,9000000,"tipo-identificador",false).toString());			
		} else {			
			tBienEspecial.setTipoBienEspecial(stringToBigInteger(pBienEspecial.getTipoBienEspecial(),1,9000000,"tipo-bien-especial",true).toString());
			tBienEspecial.setTipoIdentificador(stringToBigInteger(pBienEspecial.getTipoIdentificador(),1,9000000,"tipo-identificador",true).toString());			
		}
		if(tBienEspecial.getTipoBienEspecial()=="4" && pTramite!=7) {
			stringValidate(pBienEspecial.getFecha(),0,1000,"fecha-factura",true);
			stringValidate(pBienEspecial.getDescripcion(),0,1000,"descripcion",true);
			tBienEspecial.setDescripcion(pBienEspecial.getDescripcion()  + " Fecha: " + pBienEspecial.getFecha());
		} else {
			if(pTramite == 7) {
				tBienEspecial.setDescripcion(stringValidate(pBienEspecial.getDescripcion(),0,1000,"descripcion",false));
			} else {
				tBienEspecial.setDescripcion(stringValidate(pBienEspecial.getDescripcion(),0,1000,"descripcion",true));
			}
		}
		
		return tBienEspecial;
	}
	
	public ObligacionGarantizaModificacion validateObligacionGarantizaModificacion(mx.gob.se.rug.masiva.to.string.ObligacionGarantizaModificacion obligacionGarantizaModificacionString) throws CargaMasivaException{
		ObligacionGarantizaModificacion obligacionGarantizaModificacion =null;
		if(obligacionGarantizaModificacionString!=null){
			obligacionGarantizaModificacion =		new ObligacionGarantizaModificacion();
			obligacionGarantizaModificacion.setActoContrato(stringValidate(obligacionGarantizaModificacionString.getActoContrato(),0,1000,"acto-contrato",true));
			obligacionGarantizaModificacion.setFechaTerminacion(stringToXMLGregorianCalendar(obligacionGarantizaModificacionString.getFechaTerminacion(),"fecha-terminacion",false));
			obligacionGarantizaModificacion.setTerminos(stringValidate(obligacionGarantizaModificacionString.getTerminos(),0,1000,"terminos",false));
		}
		return obligacionGarantizaModificacion;
	}
	
	public Convenio validateConvenio(mx.gob.se.rug.masiva.to.string.Convenio convenioString) throws CargaMasivaException{
		Convenio convenio = new Convenio();
		convenio.setActoConvenio(stringValidate(convenioString.getActoConvenio(),0,1000,"acto-convenio",true));
		convenio.setFechaCelebracion(stringToXMLGregorianCalendar(convenioString.getFechaCelebracion(),"fecha-celebracion",true));
		convenio.setFechaTerminacion(stringToXMLGregorianCalendar(convenioString.getFechaTerminacion(),"fecha-terminacion",false));
		convenio.setTerminosCondiciones(stringValidate(convenioString.getTerminosCondiciones(),0,1000,"terminos-condiciones",false));
		return convenio;
	}
	
	public BigInteger validarVigencia(String pVigencia, Integer pIdTramite) throws CargaMasivaException{
				
		if(pIdTramite == 1 || pIdTramite == 31) {
			if(!Pattern.matches("(1|2|3|4|5)",pVigencia)) {
				Map< String , String > params = new HashMap<String, String>();
				params.put("campo","vigencia");
				params.put("limiteInferior","1");
				params.put("limiteSuperior","5");
				throw new CargaMasivaException(104, params); // El valor no se encuentra entre los rangos permitidos				
			} else {
				return stringToBigInteger(pVigencia.toString(),-9999,9999,"vigencia",true);
			}
		} else if(pIdTramite == 9) {
			if(!Pattern.matches("(1|2|3)",pVigencia)) {
				Map< String , String > params = new HashMap<String, String>();
				params.put("campo","vigencia");
				params.put("limiteInferior","1");
				params.put("limiteSuperior","3");
				throw new CargaMasivaException(104, params); // El valor no se encuentra entre los rangos permitidos
			} else {
				return stringToBigInteger(pVigencia.toString(),-9999,9999,"vigencia",true);
			}
		}
		return null;
	}

	// Metodos CAST
	
	public XMLGregorianCalendar stringToXMLGregorianCalendar(String datoValidar,String nombreCampo,Boolean obligatorio) throws CargaMasivaException{
		XMLGregorianCalendar fterminacion = null;
		Pattern patron = Pattern.compile("((19|20)\\d\\d)-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])(-([0-1][0-9]|2[0-4]):([0-5][0-9]))?");
		
		if( datoValidar != null && !datoValidar.trim().isEmpty() ){
			if(!Pattern.matches("((19|20)\\d\\d)-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])(-([0-1][0-9]|2[0-4]):([0-5][0-9]))?",datoValidar)){
				Map< String , String > params = new HashMap<String, String>();
				params.put("campo",nombreCampo);
				params.put("formatoFecha",datoValidar);
				throw new CargaMasivaException(109, params);
			}else{
				Matcher matcher = patron.matcher(datoValidar);		
				
				matcher.find();
				int anio = Integer.valueOf(matcher.group(1));
				int mes = Integer.valueOf(matcher.group(3));
				int dia = Integer.valueOf(matcher.group(4));
				
				//System.out.println("anio:"+anio+" mes:"+mes+" dia:"+dia);
				
				GregorianCalendar cal = new GregorianCalendar();
				cal.set(anio,mes-1,dia);
				
				try{
					fterminacion = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
				}catch (DatatypeConfigurationException e) {			
					e.printStackTrace();
				}
			}
		}else if(obligatorio.booleanValue()){
			Map< String , String > params = new HashMap<String, String>();
			params.put("campo",nombreCampo);
			throw new CargaMasivaException(106, params); // El valor es obligatorio
		}
		
		return fterminacion;
	}
	
	public String stringValidate(String datoValidar,Integer limiteInferior,Integer limiteSuperior,String nombreCampo,Boolean obligatorio) throws CargaMasivaException{
		
		if( datoValidar != null && !datoValidar.trim().isEmpty() ){
			if( datoValidar.length() < limiteInferior || datoValidar.length() > limiteSuperior ){
				Map< String , String > params = new HashMap<String, String>();
				params.put("campo",nombreCampo);
				params.put("limiteInferior",limiteInferior.toString());
				params.put("limiteSuperior",limiteSuperior.toString());
				throw new CargaMasivaException(104, params); // El valor no se encuentra entre los rangos permitidos
			}
			if(nombreCampo.equals("tipo-persona")){
				if( !(datoValidar.equals("PF") || datoValidar.equals("PM")) ){
					Map< String , String > params = new HashMap<String, String>();
					params.put("campo",nombreCampo);					
					throw new CargaMasivaException(108, params); // El valor no es valido
				}
			}
		}else if(obligatorio.booleanValue()){
			Map< String , String > params = new HashMap<String, String>();
			params.put("campo",nombreCampo);
			throw new CargaMasivaException(106, params); // El valor es obligatorio
		}
		
		return datoValidar;
	}
	
	public BigInteger stringToBigInteger(String datoValidar,Integer limiteInferior,Integer limiteSuperior,String nombreCampo,Boolean obligatorio) throws CargaMasivaException{
		
		BigInteger bigInteger = null;

		try{
			if( datoValidar != null && !datoValidar.trim().isEmpty() ){
				bigInteger = new BigInteger(datoValidar.trim());
				if( bigInteger.longValue() < limiteInferior || bigInteger.longValue() > limiteSuperior ){
					Map< String , String > params = new HashMap<String, String>();
					params.put("campo",nombreCampo);
					params.put("limiteInferior",limiteInferior.toString());
					params.put("limiteSuperior",limiteSuperior.toString());
					throw new CargaMasivaException(104, params); // El valor no se encuentra entre los rangos permitidos
				}
			}else if (obligatorio.booleanValue()) {
				Map< String , String > params = new HashMap<String, String>();
				params.put("campo",nombreCampo);
				throw new CargaMasivaException(106, params); // El valor es obligatorio
			}
		}catch(NumberFormatException e){ 
			MyLogger.Logger.log(Level.WARNING, "Error al parsear de String a Biginteger", e);
			Map< String , String > params = new HashMap<String, String>();
			params.put("campo",nombreCampo);
			throw new CargaMasivaException(105, params); // El valor no es numerico
		}
		return bigInteger;
	}
	
	public Double stringToDouble(String datoValidar,Integer limiteInferior,BigInteger limiteSuperior,String nombreCampo,Boolean obligatorio) throws CargaMasivaException{
		
		Double doubleValue = null;

		try{
			if( datoValidar != null && !datoValidar.trim().isEmpty() ){
				doubleValue = new Double(datoValidar.trim());
				if( doubleValue.doubleValue() < limiteInferior || doubleValue.doubleValue() > limiteSuperior.longValue() ){
					Map< String , String > params = new HashMap<String, String>();
					params.put("campo",nombreCampo);
					params.put("limiteInferior",limiteInferior.toString());
					params.put("limiteSuperior",limiteSuperior.toString());
					throw new CargaMasivaException(104, params); // El valor no se encuentra entre los rangos permitidos
				}
			}else if (obligatorio.booleanValue()) {
				Map< String , String > params = new HashMap<String, String>();
				params.put("campo",nombreCampo);
				throw new CargaMasivaException(106, params); // El valor es obligatorio
			}
		}catch(NumberFormatException e){ 
			MyLogger.Logger.log(Level.WARNING, "Error al parsear de String a Double", e);
			Map< String , String > params = new HashMap<String, String>();
			params.put("campo",nombreCampo);
			throw new CargaMasivaException(105, params); // El valor no es numerico
		}
		return doubleValue;
	}
	
	public Boolean stringToBoolean(String datoValidar,String nombreCampo,Boolean obligatorio) throws CargaMasivaException{
		
		Boolean flag = null;

		if( datoValidar != null && !datoValidar.trim().isEmpty() ){
			flag = Boolean.valueOf(datoValidar);
			
			if( !(datoValidar.equals("true") || datoValidar.equals("false")) ){
				Map< String , String > params = new HashMap<String, String>();
				params.put("campo",nombreCampo);				
				throw new CargaMasivaException(107, params);
			}
		}else if (obligatorio.booleanValue()) {
			Map< String , String > params = new HashMap<String, String>();
			params.put("campo",nombreCampo);
			throw new CargaMasivaException(106, params); // El valor es obligatorio
		}
		return flag;
	}
}
