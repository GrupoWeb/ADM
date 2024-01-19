package mx.gob.se.rug.masiva.validate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;

import mx.gob.economia.dgi.framework.dao.exception.JdbcDaoException;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.masiva.exception.CargaMasivaException;
import mx.gob.se.rug.masiva.exception.CargaMasivaExceptionMaxNumber;
import mx.gob.se.rug.masiva.exception.CargaMasivaFileLoadException;
import mx.gob.se.rug.masiva.exception.NoTramiteFound;
import mx.gob.se.rug.masiva.to.CargaMasiva;
import mx.gob.se.rug.masiva.to.string.CargaMasivaPreProcesed;

public class ValidateDataType {
	
	private MetodosValidacion mv = new MetodosValidacionImpl();
	
	private JAXBContext jaxbContext;	
	private mx.gob.se.rug.masiva.to.string.CargaMasiva cmString = null;
	private javax.xml.bind.Unmarshaller unmarshaller;
	
	private CargaMasivaPreProcesed cargaMasivaPreProcesed = new CargaMasivaPreProcesed();
	private CargaMasiva cargaMasiva = new CargaMasiva();	
	private mx.gob.se.rug.masiva.to.string.CargaMasiva cargaMasivaString;
	
	public mx.gob.se.rug.masiva.to.string.CargaMasiva unmarshallCargaMasivaString(InputStream InputStreamXML) throws CargaMasivaFileLoadException{
		
		cmString = null;
		
		try {
			jaxbContext = JAXBContext.newInstance(mx.gob.se.rug.masiva.to.string.CargaMasiva.class.getPackage().getName());
			unmarshaller = jaxbContext.createUnmarshaller();
			cmString = (mx.gob.se.rug.masiva.to.string.CargaMasiva) unmarshaller.unmarshal(InputStreamXML);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CargaMasivaFileLoadException(103);
			
		}
		
		return cmString;
	}
	
	private void validateNMaxTramites(Integer nRegistros) throws CargaMasivaExceptionMaxNumber{
		Constants constants = new Constants();
		String maxTramites = constants.getParamValue(Constants.VAL_MAX_TRAMITES);
		if(nRegistros>new Integer(maxTramites)){
			throw new CargaMasivaExceptionMaxNumber("No se pueden procesar mas de "+ maxTramites+ " tramites");
		}
	}

	public CargaMasivaPreProcesed parseCargaMasiva(String xmlFromDB,Integer idTipoTramite) throws CargaMasivaException, NoTramiteFound, CargaMasivaExceptionMaxNumber{
                System.out.println("parseCargaMasiva: Para saber si hay inscripciones 1");
		cargaMasivaPreProcesed.setCargaMasiva(cargaMasiva);
		
		try {
			InputStream stream = new ByteArrayInputStream(xmlFromDB.getBytes());
			cargaMasivaString = unmarshallCargaMasivaString(stream);
			
			switch(idTipoTramite){
			case 1:
					if(cargaMasivaString.getInscripcion().size() > 0){
						validateNMaxTramites(cargaMasivaString.getInscripcion().size());
						cargaMasivaPreProcesed = mv.valida_inscripcion(cargaMasivaString, cargaMasiva);
					}else{
						throw new NoTramiteFound("El Archivo no contiene inscripciones");
					}
					break;
			case 31:
				if(cargaMasivaString.getTraslado().size() > 0){
					validateNMaxTramites(cargaMasivaString.getTraslado().size());
					cargaMasivaPreProcesed = mv.valida_traslado(cargaMasivaString, cargaMasiva);
				}else{
					throw new NoTramiteFound("El Archivo no contiene traslados");
				}
				break;
			case 4:
					if(cargaMasivaString.getCancelacion().size() > 0){
						validateNMaxTramites(cargaMasivaString.getCancelacion().size());
						cargaMasivaPreProcesed = mv.valida_cancelacion(cargaMasivaString, cargaMasiva);
					}else{
						throw new NoTramiteFound("El Archivo no contiene cancelaciones");
					}
					break;
			case 30:
				if(cargaMasivaString.getEjecucion().size() > 0){
					validateNMaxTramites(cargaMasivaString.getEjecucion().size());
					cargaMasivaPreProcesed = mv.valida_ejecucion(cargaMasivaString, cargaMasiva);
				}else{
					throw new NoTramiteFound("El Archivo no contiene ejecuciones");
				}
				break;
			case 7:
					if(cargaMasivaString.getModificacion().size() > 0){
						validateNMaxTramites(cargaMasivaString.getModificacion().size());
						cargaMasivaPreProcesed = mv.valida_modificacion(cargaMasivaString, cargaMasiva);
					}else{
						throw new NoTramiteFound("El Archivo no contiene modificaciones");
					}
					break;
			case 9:
					if(cargaMasivaString.getRenovacionReduccion().size() > 0){
						validateNMaxTramites(cargaMasivaString.getRenovacionReduccion().size());
						cargaMasivaPreProcesed = mv.valida_renovacion_reduccion(cargaMasivaString, cargaMasiva);
					}else{
						throw new NoTramiteFound("El Archivo no contiene renovaciones o reducciones de vigencia");
					}
					break;
			case 8:
					if(cargaMasivaString.getTransmision().size() > 0){
						validateNMaxTramites(cargaMasivaString.getTransmision().size());
						cargaMasivaPreProcesed = mv.valida_transmision(cargaMasivaString, cargaMasiva);
					}else{
						throw new NoTramiteFound("El Archivo no contiene transmisiones");
					}
					break;
			case 6:
					if(cargaMasivaString.getRectificacionPorError().size() > 0){
						validateNMaxTramites(cargaMasivaString.getRectificacionPorError().size());
						cargaMasivaPreProcesed = mv.valida_rectificacion_por_error(cargaMasivaString, cargaMasiva);
					}else{
						throw new NoTramiteFound("El Archivo no contiene rectificaciones por error");
					}
					break;
			case 2:
					if(cargaMasivaString.getAnotacionGarantia().size() > 0){
						validateNMaxTramites(cargaMasivaString.getAnotacionGarantia().size());
						cargaMasivaPreProcesed = mv.valida_anotacion_garantia(cargaMasivaString, cargaMasiva);
					}else{
						throw new NoTramiteFound("El Archivo no contiene anotaciones con garantia");
					}
					break;
			case 10:
					if(cargaMasivaString.getAnotacion().size() > 0){
						validateNMaxTramites(cargaMasivaString.getAnotacion().size());
						cargaMasivaPreProcesed = mv.valida_anotacion(cargaMasivaString, cargaMasiva);
					}else{
						throw new NoTramiteFound("El Archivo no contiene anotaciones");
					}
					break;
			case 3:
					if(cargaMasivaString.getAvisoPreventivo().size() > 0){
						validateNMaxTramites(cargaMasivaString.getAvisoPreventivo().size());
						cargaMasivaPreProcesed = mv.valida_aviso_preventivo(cargaMasivaString, cargaMasiva);
					}else{
						throw new NoTramiteFound("El Archivo no contiene avisos preventivos");
					}
					break;
			case 12:
					if(cargaMasivaString.getAcreedores().getAcreedorAutoridad().size() > 0){
						validateNMaxTramites(cargaMasivaString.getAcreedores().getAcreedorAutoridad().size());
						cargaMasivaPreProcesed = mv.valida_acreedores(cargaMasivaString, cargaMasiva);
					}else{
						throw new NoTramiteFound("El Archivo no contiene acreedores");
					}
					break;
                        case 36:
				if(cargaMasivaString.getInscripcion().size() > 0){
						validateNMaxTramites(cargaMasivaString.getInscripcion().size());
						cargaMasivaPreProcesed = mv.valida_inscripcion(cargaMasivaString, cargaMasiva);
					}else{
						throw new NoTramiteFound("El Archivo no contiene inscripciones");
					}
					break;
			default:
					throw new AssertionError("El valor de tramite "+idTipoTramite+" no es valido.");
			}
		}catch(CargaMasivaFileLoadException e) {
			// finaliza por no corresponder con el XSD
			throw new CargaMasivaException(103);
		}
		
		return  cargaMasivaPreProcesed;
	}
	
	public CargaMasivaPreProcesed validateCargaMasiva(String xmlFromDB,Integer idTipoTramite) throws CargaMasivaException, NoTramiteFound, CargaMasivaExceptionMaxNumber{
                System.out.println("validateCargaMasiva: Para saber si hay inscripciones 2 : "+idTipoTramite);
		cargaMasivaPreProcesed.setCargaMasiva(cargaMasiva);
		
		try {
			InputStream stream = new ByteArrayInputStream(xmlFromDB.getBytes());
                        
			cargaMasivaString = unmarshallCargaMasivaString(stream);
			
			switch(idTipoTramite){
			case 1:
					if(cargaMasivaString.getInscripcion().size() > 0){
						validateNMaxTramites(cargaMasivaString.getInscripcion().size());
					}else{
						throw new NoTramiteFound("El Archivo no contiene inscripciones2");
					}
					break;
			case 31:
                            System.out.println("cargaMasivaString.getTraslado().size(): "+cargaMasivaString.getTraslado().size());
				if(cargaMasivaString.getTraslado().size() > 0){
					validateNMaxTramites(cargaMasivaString.getTraslado().size());
				}else{
					throw new NoTramiteFound("El Archivo no contiene traslados");
				}
				break;
			case 4:
					if(cargaMasivaString.getCancelacion().size() > 0){
						validateNMaxTramites(cargaMasivaString.getCancelacion().size());
					}else{
						throw new NoTramiteFound("El Archivo no contiene cancelaciones");
					}
					break;
			case 30:
				if(cargaMasivaString.getEjecucion().size() > 0){
					validateNMaxTramites(cargaMasivaString.getEjecucion().size());
				}else{
					throw new NoTramiteFound("El Archivo no contiene ejecuciones");
				}
				break;
			case 7:
					if(cargaMasivaString.getModificacion().size() > 0){
						validateNMaxTramites(cargaMasivaString.getModificacion().size());
					}else{
						throw new NoTramiteFound("El Archivo no contiene modificaciones");
					}
					break;
			case 9:
					if(cargaMasivaString.getRenovacionReduccion().size() > 0){
						validateNMaxTramites(cargaMasivaString.getRenovacionReduccion().size());
					}else{
						throw new NoTramiteFound("El Archivo no contiene renovaciones o reducciones de vigencia");
					}
					break;
			case 8:
					if(cargaMasivaString.getTransmision().size() > 0){
						validateNMaxTramites(cargaMasivaString.getTransmision().size());
					}else{
						throw new NoTramiteFound("El Archivo no contiene transmisiones");
					}
					break;
			case 6:
					if(cargaMasivaString.getRectificacionPorError().size() > 0){
						validateNMaxTramites(cargaMasivaString.getRectificacionPorError().size());
					}else{
						throw new NoTramiteFound("El Archivo no contiene rectificaciones por error");
					}
					break;
			case 2:
					if(cargaMasivaString.getAnotacionGarantia().size() > 0){
						validateNMaxTramites(cargaMasivaString.getAnotacionGarantia().size());
					}else{
						throw new NoTramiteFound("El Archivo no contiene anotaciones con garantia");
					}
					break;
			case 10:
					if(cargaMasivaString.getAnotacion().size() > 0){
						validateNMaxTramites(cargaMasivaString.getAnotacion().size());
					}else{
						throw new NoTramiteFound("El Archivo no contiene anotaciones");
					}
					break;
			case 3:
					if(cargaMasivaString.getAvisoPreventivo().size() > 0){
						validateNMaxTramites(cargaMasivaString.getAvisoPreventivo().size());
					}else{
						throw new NoTramiteFound("El Archivo no contiene avisos preventivos");
					}
					break;
			case 12:
					if(cargaMasivaString.getAcreedores().getAcreedorAutoridad().size() > 0){
						validateNMaxTramites(cargaMasivaString.getAcreedores().getAcreedorAutoridad().size());
					}else{
						throw new NoTramiteFound("El Archivo no contiene acreedores");
					}
					break;
                                        
                        case 36:
					if(cargaMasivaString.getInscripcion().size() > 0){
						validateNMaxTramites(cargaMasivaString.getInscripcion().size());
					}else{
						throw new NoTramiteFound("El Archivo no contiene inscripciones2");
					}
					break;
			default:
					throw new AssertionError("El valor de tramite "+idTipoTramite+" no es valido.");
			}
		}catch(CargaMasivaFileLoadException e) {
			// finaliza por no corresponder con el XSD
			throw new CargaMasivaException(103);
		}
		
		return  cargaMasivaPreProcesed;
	}
	
	
	public static void TestCancelacion(mx.gob.se.rug.masiva.to.string.CargaMasivaPreProcesed cargaMasivaPreProcesed){
		Iterator<mx.gob.se.rug.masiva.to.Cancelacion> iterator = cargaMasivaPreProcesed.getCargaMasiva().getCancelacion().iterator();
		while(iterator.hasNext()){
			mx.gob.se.rug.masiva.to.Cancelacion obj = iterator.next();
			System.out.println(obj.getPersonaSolicitaAutoridadInstruyeAsiento().getContenidoResolucion());
			System.out.println(obj.getIdentificadorGarantia().getClaveRastreo());
			System.out.println(obj.getIdentificadorGarantia().getIdGarantia());
			System.out.println(" ------------------------- ");
		}
	}
	
	public static void TestModificacion(mx.gob.se.rug.masiva.to.string.CargaMasivaPreProcesed cargaMasivaPreProcesed){
		Iterator<mx.gob.se.rug.masiva.to.Modificacion> iterator = cargaMasivaPreProcesed.getCargaMasiva().getModificacion().iterator();
		while(iterator.hasNext()){
			mx.gob.se.rug.masiva.to.Modificacion obj = iterator.next();
			System.out.println(obj.getPersonaSolicitaAutoridadInstruyeAsiento().getContenidoResolucion());
			//System.out.println(obj.getEliminarPartesModificacion().isEliminaDeudores());
			
			/*Iterator<mx.gob.se.rug.masiva.to.Deudor> deudorI = obj.getDeudor().iterator();
			while(deudorI.hasNext()){
				mx.gob.se.rug.masiva.to.Deudor objDeudor = deudorI.next();
				System.out.println(objDeudor.getIdNacionalidad());
				System.out.println(objDeudor.getTipoPersona());
				System.out.println(objDeudor.getDenominacionRazonSocial());
				System.out.println(objDeudor.getNombre());
				System.out.println(objDeudor.getApellidoPaterno());
				System.out.println(objDeudor.getApellidoMaterno());
			}
			
			System.out.println(obj.getDatosModificacion().getMontoMaximo());
			System.out.println(obj.getDatosModificacion().getIdMoneda());
			System.out.println(obj.getDatosModificacion().getDescripcionBienesMuebles());
			System.out.println(obj.getDatosModificacion().getTerminosCondiciones());
			System.out.println(obj.getDatosModificacion().isPreveCambiosBienesMueblesMonto());
			
			Iterator<mx.gob.se.rug.masiva.to.TipoBienMueble> tipoBienMuebleI = obj.getDatosModificacion().getTipoBienMueble().iterator();
			while(tipoBienMuebleI.hasNext()){
				mx.gob.se.rug.masiva.to.TipoBienMueble objTipoBienMueble = tipoBienMuebleI.next();
				System.out.println(objTipoBienMueble.getId());
				System.out.println(objTipoBienMueble.getDescripcion());				
			}
			
			System.out.println(obj.getObligacionGarantizaModificacion().getActoContrato());
			System.out.println(obj.getObligacionGarantizaModificacion().getFechaTerminacion());
			System.out.println(obj.getObligacionGarantizaModificacion().getTerminos());
			
			System.out.println(obj.getConvenio().getActoConvenio());
			System.out.println(obj.getConvenio().getFechaCelebracion());
			System.out.println(obj.getConvenio().getFechaTerminacion());
			System.out.println(obj.getConvenio().getTerminosCondiciones());
			
			System.out.println(obj.getIdentificadorGarantia().getClaveRastreo());
			System.out.println(obj.getIdentificadorGarantia().getIdGarantia());
			System.out.println(" ------------------------- ");*/
		}
	}
	
	public static void TestTransmision(mx.gob.se.rug.masiva.to.string.CargaMasivaPreProcesed cargaMasivaPreProcesed){
		Iterator<mx.gob.se.rug.masiva.to.Transmision> iterator = cargaMasivaPreProcesed.getCargaMasiva().getTransmision().iterator();
		while(iterator.hasNext()){
			mx.gob.se.rug.masiva.to.Transmision obj = iterator.next();
			System.out.println(obj.getPersonaSolicitaAutoridadInstruyeAsiento().getContenidoResolucion());
			System.out.println(obj.getAcreedor().getIdAcreedor());
			System.out.println(obj.getEliminarPartesTransmision().isEliminaOtorgantes());
			System.out.println(obj.getEliminarPartesTransmision().isEliminaAcreedorAdicional());
			
			
			Iterator<mx.gob.se.rug.masiva.to.Otorgante> otorganteI = obj.getOtorgante().iterator();
			while(otorganteI.hasNext()){
				mx.gob.se.rug.masiva.to.Otorgante objOtorgante = otorganteI.next();
				System.out.println(objOtorgante.getIdNacionalidad());
				System.out.println(objOtorgante.getTipoPersona());
				System.out.println(objOtorgante.getDenominacionRazonSocial());
				System.out.println(objOtorgante.getNombre());
				System.out.println(objOtorgante.getApellidoPaterno());				
				System.out.println(objOtorgante.getApellidoMaterno());
				System.out.println(objOtorgante.getCurp());
				System.out.println(objOtorgante.getFolioElectronico());
			}
			
			Iterator<mx.gob.se.rug.masiva.to.AcreedorAdicional> aAdicionalI = obj.getAcreedorAdicional().iterator();
			while(aAdicionalI.hasNext()){
				mx.gob.se.rug.masiva.to.AcreedorAdicional objAAdicional = aAdicionalI.next();
				System.out.println(objAAdicional.getIdNacionalidad());
				System.out.println(objAAdicional.getTipoPersona());
				System.out.println(objAAdicional.getDenominacionRazonSocial());
				System.out.println(objAAdicional.getNombre());
				System.out.println(objAAdicional.getApellidoPaterno());				
				System.out.println(objAAdicional.getApellidoMaterno());
				System.out.println(objAAdicional.getTelefono());
				System.out.println(objAAdicional.getTelefonoExtension());
				System.out.println(objAAdicional.getCorreoElectronico());
				System.out.println(objAAdicional.getCalle());
				System.out.println(objAAdicional.getNumeroExterior());
				System.out.println(objAAdicional.getNumeroInterior());
				System.out.println(objAAdicional.getIdColonia());
				System.out.println(objAAdicional.getIdLocalidad());
				System.out.println(objAAdicional.getIdPaisResidencia());
				System.out.println(objAAdicional.getDomicilioExtranjeroUno());
				System.out.println(objAAdicional.getDomicilioExtranjeroDos());
				System.out.println(objAAdicional.getPoblacion());
				System.out.println(objAAdicional.getZonaPostal());
				System.out.println(objAAdicional.getRfc());
			}						
			
			System.out.println(obj.getConvenio().getActoConvenio());
			System.out.println(obj.getConvenio().getFechaCelebracion());
			System.out.println(obj.getConvenio().getFechaTerminacion());
			System.out.println(obj.getConvenio().getTerminosCondiciones());
			
			System.out.println(obj.getIdentificadorGarantia().getClaveRastreo());
			System.out.println(obj.getIdentificadorGarantia().getIdGarantia());
			System.out.println(" ------------------------- ");
		}
	}
	
	public static void TestRectificacionError(mx.gob.se.rug.masiva.to.string.CargaMasivaPreProcesed cargaMasivaPreProcesed){
		Iterator<mx.gob.se.rug.masiva.to.RectificacionPorError> iterator = cargaMasivaPreProcesed.getCargaMasiva().getRectificacionPorError().iterator();
		while(iterator.hasNext()){
			mx.gob.se.rug.masiva.to.RectificacionPorError obj = iterator.next();
			System.out.println(obj.getPersonaSolicitaAutoridadInstruyeAsiento().getContenidoResolucion());
			System.out.println(obj.getAcreedor().getIdAcreedor());
			System.out.println(obj.getEliminarPartesRectificacion().isEliminaOtorgantes());
			System.out.println(obj.getEliminarPartesRectificacion().isEliminaDeudores());
			System.out.println(obj.getEliminarPartesRectificacion().isEliminaAcreedorAdicional());			
			
			
			Iterator<mx.gob.se.rug.masiva.to.Otorgante> otorganteI = obj.getPartesRectificacion().getOtorgante().iterator();
			while(otorganteI.hasNext()){
				mx.gob.se.rug.masiva.to.Otorgante objOtorgante = otorganteI.next();
				System.out.println(objOtorgante.getIdNacionalidad());
				System.out.println(objOtorgante.getTipoPersona());
				System.out.println(objOtorgante.getDenominacionRazonSocial());
				System.out.println(objOtorgante.getNombre());
				System.out.println(objOtorgante.getApellidoPaterno());				
				System.out.println(objOtorgante.getApellidoMaterno());
				System.out.println(objOtorgante.getCurp());
				System.out.println(objOtorgante.getFolioElectronico());
			}
			
			Iterator<mx.gob.se.rug.masiva.to.Deudor> deudorI = obj.getPartesRectificacion().getDeudor().iterator();
			while(deudorI.hasNext()){
				mx.gob.se.rug.masiva.to.Deudor objDeudor = deudorI.next();
				System.out.println(objDeudor.getIdNacionalidad());
				System.out.println(objDeudor.getTipoPersona());
				System.out.println(objDeudor.getDenominacionRazonSocial());
				System.out.println(objDeudor.getNombre());
				System.out.println(objDeudor.getApellidoPaterno());
				System.out.println(objDeudor.getApellidoMaterno());
			}
			
			Iterator<mx.gob.se.rug.masiva.to.AcreedorAdicional> aAdicionalI = obj.getPartesRectificacion().getAcreedorAdicional().iterator();
			while(aAdicionalI.hasNext()){
				mx.gob.se.rug.masiva.to.AcreedorAdicional objAAdicional = aAdicionalI.next();
				System.out.println(objAAdicional.getIdNacionalidad());
				System.out.println(objAAdicional.getTipoPersona());
				System.out.println(objAAdicional.getDenominacionRazonSocial());
				System.out.println(objAAdicional.getNombre());
				System.out.println(objAAdicional.getApellidoPaterno());				
				System.out.println(objAAdicional.getApellidoMaterno());
				System.out.println(objAAdicional.getTelefono());
				System.out.println(objAAdicional.getTelefonoExtension());
				System.out.println(objAAdicional.getCorreoElectronico());
				System.out.println(objAAdicional.getCalle());
				System.out.println(objAAdicional.getNumeroExterior());
				System.out.println(objAAdicional.getNumeroInterior());
				System.out.println(objAAdicional.getIdColonia());
				System.out.println(objAAdicional.getIdLocalidad());
				System.out.println(objAAdicional.getIdPaisResidencia());
				System.out.println(objAAdicional.getDomicilioExtranjeroUno());
				System.out.println(objAAdicional.getDomicilioExtranjeroDos());
				System.out.println(objAAdicional.getPoblacion());
				System.out.println(objAAdicional.getZonaPostal());
				System.out.println(objAAdicional.getRfc());
			}						
						
			System.out.println(obj.getDatosGarantia().getIdTipoGarantia());
			System.out.println(obj.getDatosGarantia().getFechaCelebracion());
			System.out.println(obj.getDatosGarantia().getMontoMaximo());
			System.out.println(obj.getDatosGarantia().getIdMoneda());
			System.out.println(obj.getDatosGarantia().getDescripcionBienesMuebles());
			System.out.println(obj.getDatosGarantia().getTerminosCondiciones());
			System.out.println(obj.getDatosGarantia().getDatosInstrumentoPublico());
			System.out.println(obj.getDatosGarantia().isBDatosModificables());
			
			Iterator<mx.gob.se.rug.masiva.to.TipoBienMueble> tipoBienMuebleI = obj.getDatosGarantia().getTipoBienMueble().iterator();
			while(tipoBienMuebleI.hasNext()){
				mx.gob.se.rug.masiva.to.TipoBienMueble objTipoBienMueble = tipoBienMuebleI.next();
				System.out.println(objTipoBienMueble.getId());
				System.out.println(objTipoBienMueble.getDescripcion());				
			}
			
			System.out.println(obj.getObligacionGarantiza().getActoContrato());
			System.out.println(obj.getObligacionGarantiza().getFechaCelebracion());
			System.out.println(obj.getObligacionGarantiza().getFechaTerminacion());
			System.out.println(obj.getObligacionGarantiza().getTerminos());
			
			System.out.println(obj.getIdentificadorGarantia().getClaveRastreo());
			System.out.println(obj.getIdentificadorGarantia().getIdGarantia());
			System.out.println(" ------------------------- ");
		}
	}
	
	public static void TestInscripcion(mx.gob.se.rug.masiva.to.string.CargaMasivaPreProcesed cargaMasivaPreProcesed){
		Iterator<mx.gob.se.rug.masiva.to.Inscripcion> iterator = cargaMasivaPreProcesed.getCargaMasiva().getInscripcion().iterator();
		while(iterator.hasNext()){
			mx.gob.se.rug.masiva.to.Inscripcion obj = iterator.next();
			System.out.println(obj.getPersonaSolicitaAutoridadInstruyeAsiento().getContenidoResolucion());
			
			Iterator<mx.gob.se.rug.masiva.to.Otorgante> otorganteI = obj.getPartes().getOtorgante().iterator();
			while(otorganteI.hasNext()){
				mx.gob.se.rug.masiva.to.Otorgante objOtorgante = otorganteI.next();
				System.out.println(objOtorgante.getIdNacionalidad());
				System.out.println(objOtorgante.getTipoPersona());
				System.out.println(objOtorgante.getDenominacionRazonSocial());
				System.out.println(objOtorgante.getNombre());
				System.out.println(objOtorgante.getApellidoPaterno());				
				System.out.println(objOtorgante.getApellidoMaterno());
				System.out.println(objOtorgante.getCurp());
				System.out.println(objOtorgante.getFolioElectronico());
			}
			
			Iterator<mx.gob.se.rug.masiva.to.Deudor> deudorI = obj.getPartes().getDeudor().iterator();
			while(deudorI.hasNext()){
				mx.gob.se.rug.masiva.to.Deudor objDeudor = deudorI.next();
				System.out.println(objDeudor.getIdNacionalidad());
				System.out.println(objDeudor.getTipoPersona());
				System.out.println(objDeudor.getDenominacionRazonSocial());
				System.out.println(objDeudor.getNombre());
				System.out.println(objDeudor.getApellidoPaterno());
				System.out.println(objDeudor.getApellidoMaterno());
			}
			
			Iterator<mx.gob.se.rug.masiva.to.AcreedorAdicional> aAdicionalI = obj.getPartes().getAcreedorAdicional().iterator();
			while(aAdicionalI.hasNext()){
				mx.gob.se.rug.masiva.to.AcreedorAdicional objAAdicional = aAdicionalI.next();
				System.out.println(objAAdicional.getIdNacionalidad());
				System.out.println(objAAdicional.getTipoPersona());
				System.out.println(objAAdicional.getDenominacionRazonSocial());
				System.out.println(objAAdicional.getNombre());
				System.out.println(objAAdicional.getApellidoPaterno());				
				System.out.println(objAAdicional.getApellidoMaterno());
				System.out.println(objAAdicional.getTelefono());
				System.out.println(objAAdicional.getTelefonoExtension());
				System.out.println(objAAdicional.getCorreoElectronico());
				System.out.println(objAAdicional.getCalle());
				System.out.println(objAAdicional.getNumeroExterior());
				System.out.println(objAAdicional.getNumeroInterior());
				System.out.println(objAAdicional.getIdColonia());
				System.out.println(objAAdicional.getIdLocalidad());
				System.out.println(objAAdicional.getIdPaisResidencia());
				System.out.println(objAAdicional.getDomicilioExtranjeroUno());
				System.out.println(objAAdicional.getDomicilioExtranjeroDos());
				System.out.println(objAAdicional.getPoblacion());
				System.out.println(objAAdicional.getZonaPostal());
				System.out.println(objAAdicional.getRfc());
			}

			System.out.println(obj.getGarantia().getCreacion().getIdTipoGarantia());
			System.out.println(obj.getGarantia().getCreacion().getFechaCelebracion());
			System.out.println(obj.getGarantia().getCreacion().getMontoMaximo());
			System.out.println(obj.getGarantia().getCreacion().getIdMoneda());
			System.out.println(obj.getGarantia().getCreacion().getDescripcionBienesMuebles());
			System.out.println(obj.getGarantia().getCreacion().getTerminosCondiciones());
			System.out.println(obj.getGarantia().getCreacion().getDatosInstrumentoPublico());
			System.out.println(obj.getGarantia().getCreacion().getbDatosModificables());
			
			Iterator<mx.gob.se.rug.masiva.to.TipoBienMueble> tipoBienMuebleI = obj.getGarantia().getCreacion().getTipoBienMueble().iterator();
			while(tipoBienMuebleI.hasNext()){
				mx.gob.se.rug.masiva.to.TipoBienMueble objTipoBienMueble = tipoBienMuebleI.next();
				System.out.println(objTipoBienMueble.getId());
				System.out.println(objTipoBienMueble.getDescripcion());				
			}
			
			System.out.println(obj.getGarantia().getObligacion().getActoContrato());
			System.out.println(obj.getGarantia().getObligacion().getFechaCelebracion());
			System.out.println(obj.getGarantia().getObligacion().getFechaTerminacion());
			System.out.println(obj.getGarantia().getObligacion().getTerminos());
			
			System.out.println(obj.getVigencia().getMeses());
			
			System.out.println(obj.getIdentificador().getClaveRastreo());
			System.out.println(" ------------------------- ");
		}
	}
	
	public static void TestAvisoPreventivo(mx.gob.se.rug.masiva.to.string.CargaMasivaPreProcesed cargaMasivaPreProcesed){
		Iterator<mx.gob.se.rug.masiva.to.AvisoPreventivo> iterator = cargaMasivaPreProcesed.getCargaMasiva().getAvisoPreventivo().iterator();
		while(iterator.hasNext()){
			mx.gob.se.rug.masiva.to.AvisoPreventivo obj = iterator.next();
			System.out.println(obj.getPersonaSolicitaAutoridadInstruyeAsiento().getContenidoResolucion());
			
			System.out.println(obj.getOtorgante().getIdNacionalidad());
			System.out.println(obj.getOtorgante().getTipoPersona());
			System.out.println(obj.getOtorgante().getDenominacionRazonSocial());
			System.out.println(obj.getOtorgante().getNombre());
			System.out.println(obj.getOtorgante().getApellidoPaterno());				
			System.out.println(obj.getOtorgante().getApellidoMaterno());
			System.out.println(obj.getOtorgante().getCurp());
			System.out.println(obj.getOtorgante().getFolioElectronico());
			
			System.out.println(obj.getDatosAvisoPreventivo().getDescripcionBienesMuebles());
			
			System.out.println(obj.getIdentificador().getClaveRastreo());
			System.out.println(" ------------------------- ");
		}
	}
	
	public static void TestAnotacion(mx.gob.se.rug.masiva.to.string.CargaMasivaPreProcesed cargaMasivaPreProcesed){
		Iterator<mx.gob.se.rug.masiva.to.Anotacion> iterator = cargaMasivaPreProcesed.getCargaMasiva().getAnotacion().iterator();
		while(iterator.hasNext()){
			mx.gob.se.rug.masiva.to.Anotacion obj = iterator.next();
			System.out.println(obj.getPersonaSolicitaAutoridadInstruyeAsiento().getContenidoResolucion());
			
			System.out.println(obj.getOtorgante().getIdNacionalidad());
			System.out.println(obj.getOtorgante().getTipoPersona());
			System.out.println(obj.getOtorgante().getDenominacionRazonSocial());
			System.out.println(obj.getOtorgante().getNombre());
			System.out.println(obj.getOtorgante().getApellidoPaterno());				
			System.out.println(obj.getOtorgante().getApellidoMaterno());
			System.out.println(obj.getOtorgante().getCurp());
			System.out.println(obj.getOtorgante().getFolioElectronico());
			
			System.out.println(obj.getVigencia().getMeses());
			System.out.println(obj.getDatosAnotacion().getContenidoResolucion());
			System.out.println(obj.getIdentificador().getClaveRastreo());
			System.out.println(" ------------------------- ");
		}
	}
	
	public static void TestAnotacionGarantia(mx.gob.se.rug.masiva.to.string.CargaMasivaPreProcesed cargaMasivaPreProcesed){
		Iterator<mx.gob.se.rug.masiva.to.AnotacionGarantia> iterator = cargaMasivaPreProcesed.getCargaMasiva().getAnotacionGarantia().iterator();
		while(iterator.hasNext()){
			mx.gob.se.rug.masiva.to.AnotacionGarantia obj = iterator.next();
			System.out.println(obj.getPersonaSolicitaAutoridadInstruyeAsiento().getContenidoResolucion());
			System.out.println(obj.getVigencia().getMeses());
			System.out.println(obj.getDatosAnotacion().getContenidoResolucion());
			System.out.println(obj.getIdentificadorGarantia().getClaveRastreo());
			System.out.println(obj.getIdentificadorGarantia().getIdGarantia());
			System.out.println(" ------------------------- ");
		}
	}
	
	public static void TestAcreedores(mx.gob.se.rug.masiva.to.string.CargaMasivaPreProcesed cargaMasivaPreProcesed){
		Iterator<mx.gob.se.rug.masiva.to.AcreedorAutoridad> iterator = cargaMasivaPreProcesed.getCargaMasiva().getAcreedores().getAcreedorAutoridad().iterator();
		while(iterator.hasNext()){
			mx.gob.se.rug.masiva.to.AcreedorAutoridad obj = iterator.next();
			System.out.println(obj.getIdNacionalidad());
			System.out.println(obj.getTipoPersona());
			System.out.println(obj.getDenominacionRazonSocial());
			System.out.println(obj.getNombre());
			System.out.println(obj.getApellidoPaterno());				
			System.out.println(obj.getApellidoMaterno());
			System.out.println(obj.getTelefono());
			System.out.println(obj.getTelefonoExtension());
			System.out.println(obj.getCorreoElectronico());
			System.out.println(obj.getCalle());
			System.out.println(obj.getNumeroExterior());
			System.out.println(obj.getNumeroInterior());
			System.out.println(obj.getIdColonia());
			System.out.println(obj.getIdLocalidad());
			System.out.println(obj.getIdPaisResidencia());
			System.out.println(obj.getDomicilioExtranjeroUno());
			System.out.println(obj.getDomicilioExtranjeroDos());
			System.out.println(obj.getPoblacion());
			System.out.println(obj.getZonaPostal());
			System.out.println(obj.getRfc());
			System.out.println(" ------------------------- ");
		}
	}
	
	public String getFileFromDB(Integer idArchivo){
                System.out.println("ID DEL ARCHIVO: "+idArchivo);
		String varBLOB = "";
		InputStream inputStream = null;
		Blob blob = null;		
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		
		ConexionBD bd = new ConexionBD();
		connection = bd.getConnection();
		
		try {
			String Query_para_leer_archivo_Db = "SELECT ARCHIVO FROM RUG_ARCHIVO WHERE ID_ARCHIVO = ?";
			ps = connection.prepareStatement(Query_para_leer_archivo_Db);
			ps.setInt(1,idArchivo);
			rs = ps.executeQuery();
			if (rs.next()) {
				blob = rs.getBlob("ARCHIVO");				
			}			
		} catch (Exception e) {
			throw new JdbcDaoException(e);
		}
		
		try{
			StringBuffer strOut = new StringBuffer();
			int bytesread = 0;
			inputStream = blob.getBinaryStream();
			byte[] bytebuffer = new byte[ (int)blob.length() ];
			
			while( ( bytesread = inputStream.read( bytebuffer ) ) != -1 ){
					strOut.append( new String(bytebuffer) );
			}
			varBLOB = strOut.toString();
			inputStream.close();
			
			
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}finally{
			bd.close(connection, rs, null);
		}
				
		return varBLOB;
	}
	
	
}
