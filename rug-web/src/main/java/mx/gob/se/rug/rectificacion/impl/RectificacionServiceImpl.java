package mx.gob.se.rug.rectificacion.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.exception.InfrastructureException;
import mx.gob.se.rug.juez.dao.JuezDAO;
import mx.gob.se.rug.masiva.dao.MasivaDAO;
import mx.gob.se.rug.masiva.exception.CargaMasivaException;
import mx.gob.se.rug.masiva.exception.GarantiaRepetidaException;
import mx.gob.se.rug.masiva.resultado.to.CargaMasivaResultado;
import mx.gob.se.rug.masiva.resultado.to.ResultadoCargaMasiva;
import mx.gob.se.rug.masiva.resultado.to.Resumen;
import mx.gob.se.rug.masiva.resultado.to.TramiteRes;
import mx.gob.se.rug.masiva.resultado.to.Tramites;
import mx.gob.se.rug.masiva.service.MasivaService;
import mx.gob.se.rug.masiva.service.impl.MasivaServiceImpl;
import mx.gob.se.rug.masiva.service.impl.ModificaGarantiaServiceImpl;
import mx.gob.se.rug.masiva.to.CargaMasiva;
import mx.gob.se.rug.masiva.to.RectificacionPorError;
import mx.gob.se.rug.masiva.to.Tramite;
import mx.gob.se.rug.to.UsuarioTO;

public class RectificacionServiceImpl {

	public CargaMasivaResultado cargaMasivaRectificacion(UsuarioTO usuario, CargaMasiva cm, AcreedorTO acreedor, Integer idArchivo) {
		System.out.println("entro a carga masiva rectificacion");
		CargaMasivaResultado cargaMasivaResultado= new CargaMasivaResultado();
		ResultadoCargaMasiva resultado = new ResultadoCargaMasiva();
		int corectos=0;
		int incorrectos=0;
		boolean isCorrecto;
		int totalTramites=cm.getRectificacionPorError().size();
		List<Tramite> tramitesCorrectos =new ArrayList<Tramite>();
		List<Tramite> tramitesIncorrectos =new ArrayList<Tramite>();
		
		int consecutivo = 1;
		Tramite tramite;
		resultado.setTramites(new Tramites());
		resultado.setResumen(new Resumen());
		
		MasivaDAO masivaDAO= new MasivaDAO();
		MasivaService masivaService= new MasivaServiceImpl();
		ModificaGarantiaServiceImpl garantiaServiceImpl= new ModificaGarantiaServiceImpl();
		JuezDAO juezDAO= new JuezDAO();
		
		
		Set<Integer> idGarantiasInXML = new HashSet<Integer>();
		//InscripcionServiceImpl inscripcionService = new  InscripcionServiceImpl();
		try {
			//acreedor = inscripcionService.getAcreedorByID(acreedor.getIdPersona());
			for (RectificacionPorError rectificacionPorError : cm.getRectificacionPorError()) {
				isCorrecto=true;
				TramiteRes tramiteRes= new TramiteRes();
				tramite = new Tramite();
				try{
					tramite.setIdArchivo(idArchivo);
					tramite.setIdUsuario(usuario.getPersona().getIdPersona());
					tramite.setConsecutivo(consecutivo++);
					tramite.setIdGarantia(rectificacionPorError.getIdentificadorGarantia().getIdGarantia().intValue());
					tramite.setIdTipoTramite(6);
					tramite.setIdEstatus(5);
					tramite.setIdAcreedor(acreedor.getIdPersona());
					tramite.setClaveRastreo(rectificacionPorError.getIdentificadorGarantia().getClaveRastreo());
					
					tramiteRes.setClaveRastreo(tramite.getClaveRastreo());
					
					
					masivaService.verifyAcreedorOfGarantia(tramite.getIdAcreedor(), tramite.getIdGarantia());
					
					
					masivaService.validaGarantia(tramite.getIdGarantia(), idGarantiasInXML);
					
					masivaDAO.altaParteTramIncRast(tramite);
					masivaDAO.spCopiaGarantia(tramite);
					
					//Anotacion que instruye 
					juezDAO.insertAutoridadInstruyeAsiento(tramite.getIdTramite(), rectificacionPorError.getPersonaSolicitaAutoridadInstruyeAsiento());
					
					// Eliminar PARTES
					if(rectificacionPorError.getEliminarPartesRectificacion()!=null){
						if(rectificacionPorError.getEliminarPartesRectificacion().isEliminaOtorgantes()!=null&&rectificacionPorError.getEliminarPartesRectificacion().isEliminaOtorgantes()){
							if(rectificacionPorError.getPartesRectificacion()!=null&&rectificacionPorError.getPartesRectificacion().getOtorgante().size()>0){
								masivaDAO.executeBajaParteLogicaMasiva(usuario.getPersona().getIdPersona(), 1, tramite.getIdTramite());
							}else{
								throw new CargaMasivaException(100);
							}
						}
						
						if(rectificacionPorError.getEliminarPartesRectificacion().isEliminaDeudores()!=null&&rectificacionPorError.getEliminarPartesRectificacion().isEliminaDeudores()){
							masivaDAO.executeBajaParteLogicaMasiva(usuario.getPersona().getIdPersona(), 2, tramite.getIdTramite());
						}
						
						if(rectificacionPorError.getEliminarPartesRectificacion().isEliminaAcreedorAdicional()!=null&&rectificacionPorError.getEliminarPartesRectificacion().isEliminaAcreedorAdicional()){
							masivaDAO.executeBajaParteLogicaMasiva(usuario.getPersona().getIdPersona(), 3, tramite.getIdTramite());
						}
					}
					
					
					//Añadir las Partes
					if(rectificacionPorError.getPartesRectificacion()!=null){
						if(rectificacionPorError.getPartesRectificacion().getOtorgante().size()>0){
							masivaService.addOtorgantes(rectificacionPorError.getPartesRectificacion().getOtorgante(),usuario,tramite);
						}
						if(rectificacionPorError.getPartesRectificacion().getDeudor().size()>0){
							masivaService.addDeudores(rectificacionPorError.getPartesRectificacion().getDeudor(),usuario,tramite);
						}
						if(rectificacionPorError.getPartesRectificacion().getAcreedorAdicional().size()>0){
							
							masivaService.addAcreedorAdicional(rectificacionPorError.getPartesRectificacion().getAcreedorAdicional(),usuario,tramite);
						}
					}
					
					
				//validacion de Datos
					//validacion y transmision de Acreedor
					if(rectificacionPorError.getAcreedor()!=null&&rectificacionPorError.getAcreedor().getIdAcreedor()!=null){
						masivaService.changeAcreedorGarantia(rectificacionPorError.getAcreedor().getIdAcreedor().intValue(),tramite.getIdAcreedor(), tramite.getIdUsuario(), tramite.getIdTramite());
					}
					
					Integer idRelacionBien=null;
					
					
					if(rectificacionPorError.getDatosGarantia()!=null){
						//validacion de Datos Catalogo
						if( rectificacionPorError.getDatosGarantia().getIdMoneda()!=null){
						masivaDAO.validaCatalogo(5, rectificacionPorError.getDatosGarantia().getIdMoneda().intValue());
						}
						
						//Validacion Datos Garantia
						garantiaServiceImpl.validaDatos(rectificacionPorError.getDatosGarantia().getIdTipoGarantia(), "id-tipo-garantia", true, false, null);
						garantiaServiceImpl.validaDatos(rectificacionPorError.getDatosGarantia().getDescripcionBienesMuebles(), "descripcion-bienes-muebles", true, false, null);
						garantiaServiceImpl.validaDatos(rectificacionPorError.getDatosGarantia().getMontoMaximo(), "monto-maximo", false, false, null);
						garantiaServiceImpl.validaDatos(rectificacionPorError.getDatosGarantia().getTerminosCondiciones(), "terminos-condiciones", true, false, null);
						garantiaServiceImpl.validaDatos(rectificacionPorError.getDatosGarantia().getDatosInstrumentoPublico(), "datos-instrumento-publico", false, false, null);
						garantiaServiceImpl.validaDatos(rectificacionPorError.getDatosGarantia().isBDatosModificables(), "b-datos-modificables", false, false, null);
						
						
						//VAlidacion Fechas
						Date fechaInicio=null;
						
						if(rectificacionPorError.getDatosGarantia().getFechaCelebracion()!=null){
						
							fechaInicio = new Date(rectificacionPorError.getDatosGarantia().getFechaCelebracion().toGregorianCalendar().getTime().getTime());
							garantiaServiceImpl.validaDatos(fechaInicio, "fecha-celebracion", true, false, 1);
						}
						
						//Insert Bienes Muebles
						 idRelacionBien=masivaService.agregaTipoBien(rectificacionPorError.getDatosGarantia().getTipoBienMueble(), tramite);
						 
						//Update Garantia
							
							
							/*masivaDAO.actualizaDatosGantiaCarga(tramite.getIdGarantiaPendiente(), 
									masivaService.bigIntegerToInteger(rectificacionPorError.getDatosGarantia().getIdTipoGarantia()),
									rectificacionPorError.getDatosGarantia().getDescripcionBienesMuebles(),
									idRelacionBien, 
									rectificacionPorError.getDatosGarantia().getMontoMaximo(), 
									rectificacionPorError.getDatosGarantia().getTerminosCondiciones(), 
									fechaInicio, 
									rectificacionPorError.getDatosGarantia().isBDatosModificables(),
									rectificacionPorError.getDatosGarantia().getDatosInstrumentoPublico(), 
									masivaService.bigIntegerToInteger(rectificacionPorError.getDatosGarantia().getIdMoneda()));
					*/
					}
					
					
					if(rectificacionPorError.getObligacionGarantiza()!=null){
						//Validacion Datos
						garantiaServiceImpl.validaDatos(rectificacionPorError.getObligacionGarantiza().getTerminos(), "terminos", false, false, null);
						garantiaServiceImpl.validaDatos(rectificacionPorError.getObligacionGarantiza().getActoContrato(), "acto-contrato", false, false, null);
						
						Date fechaCelebracion=null;
						//VAlidacion Fechas
						if(rectificacionPorError.getObligacionGarantiza().getFechaCelebracion()!=null){
							fechaCelebracion=rectificacionPorError.getObligacionGarantiza().getFechaCelebracion().toGregorianCalendar().getTime();
						}
						garantiaServiceImpl.validaDatos(fechaCelebracion, "fecha-celebracion", true, false, 1);
						
						Date fechaTerminacion=null;
						if(rectificacionPorError.getObligacionGarantiza().getFechaTerminacion()!=null){
							fechaTerminacion = new Date(rectificacionPorError.getObligacionGarantiza().getFechaTerminacion().toGregorianCalendar().getTime().getTime());
						}
						garantiaServiceImpl.validaDatos(fechaTerminacion, "fecha-terminacion", true, false, 2);
						
						
						//update Contrato
						masivaDAO.actualizaContrato(fechaCelebracion, 
								fechaTerminacion,
								rectificacionPorError.getObligacionGarantiza().getTerminos(), 
								rectificacionPorError.getObligacionGarantiza().getActoContrato(), 
								tramite.getIdGarantiaPendiente());
					
					}
					
					//Resumen
					
					corectos= corectos+1;
					tramiteRes.setCodigoError("0");
					tramiteRes.setMensajeError("La Rectificacion se agrego correctamente"); 
					tramitesCorrectos.add(tramite);
				
				}catch (CargaMasivaException e) {
					
					incorrectos= incorrectos+1;
					tramiteRes.setCodigoError(e.getCodeError().toString());
					tramiteRes.setMensajeError(e.getMessage());
					tramitesIncorrectos.add(tramite);
					isCorrecto=false;
				}catch(GarantiaRepetidaException e){
					
					incorrectos= incorrectos+1;
					tramiteRes.setCodigoError(e.getCodeError().toString());
					tramiteRes.setMensajeError(e.getMessage());
					tramitesIncorrectos.add(tramite);
					isCorrecto=false;
					
				}catch (InfrastructureException e) {
					incorrectos= incorrectos+1;
					tramiteRes.setCodigoError("999");
					tramiteRes.setMensajeError(e.getMessage());
					tramitesIncorrectos.add(tramite);
					isCorrecto=false;
					
				}catch (Exception e) {
					
					incorrectos= incorrectos+1;
					tramiteRes.setCodigoError("999");
					tramiteRes.setMensajeError(e.getMessage());
					tramitesIncorrectos.add(tramite);
					isCorrecto=false;
					e.printStackTrace();
					
				}finally{
					if(rectificacionPorError.getIdentificadorGarantia().getClaveRastreo()!=null){
						tramiteRes.setClaveRastreo(rectificacionPorError.getIdentificadorGarantia().getClaveRastreo());
					}
					
					
					resultado.getTramites().getTramite().add(tramiteRes);
					
					if(!isCorrecto){
						rectificacionPorError=null;
					}
				}
				
			}
		} catch (Exception e) {
			java.util.Date date = new java.util.Date();
			resultado.getResumen().setMensajeError("Sucedio un error en el sistema la fecha :"
					+ date.toString() + ", enviar el XML a soporte. codError["
					+ date.getTime() + "]");
			System.out
					.println("ThreadCargaMasiva::cargaMasivaRectificacion:archivo ["
							+ idArchivo + "]::: Sucedio un error el proceso:::");
			e.printStackTrace();
		} finally {

			resultado.getResumen().setCorrectos(String.valueOf(corectos));
			resultado.getResumen().setErroneos(String.valueOf(incorrectos));
			resultado.getResumen().setNumeroRegistros(String.valueOf(totalTramites));
			cargaMasivaResultado.setResultadoCargaMasiva(resultado);
			cargaMasivaResultado.setTramitesCorrectos(tramitesCorrectos);
			cargaMasivaResultado.setTramitesIncorrectos(tramitesIncorrectos);
		}

		return cargaMasivaResultado;
	}

}