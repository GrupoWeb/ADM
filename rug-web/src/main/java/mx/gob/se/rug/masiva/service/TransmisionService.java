package mx.gob.se.rug.masiva.service;

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
import mx.gob.se.rug.masiva.service.impl.MasivaServiceImpl;
import mx.gob.se.rug.masiva.service.impl.ModificaGarantiaServiceImpl;
import mx.gob.se.rug.masiva.to.CargaMasiva;
import mx.gob.se.rug.masiva.to.Tramite;
import mx.gob.se.rug.masiva.to.Transmision;
import mx.gob.se.rug.to.UsuarioTO;

public class TransmisionService {
	
	public CargaMasivaResultado cargaMasivaTransmision(UsuarioTO usuario, CargaMasiva cm, AcreedorTO acreedor, Integer idArchivo) {
		System.out.println("entro a carga masiva cargaMasivaTransmision");
		CargaMasivaResultado cargaMasivaResultado= new CargaMasivaResultado();
		ResultadoCargaMasiva resultado = new ResultadoCargaMasiva();
		int corectos=0;
		int incorrectos=0;
		boolean isCorrecto;
		int totalTramites=cm.getTransmision().size();
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
			for (Transmision trasnsmision : cm.getTransmision()) {
				isCorrecto=true;
				TramiteRes tramiteRes= new TramiteRes();
				tramite = new Tramite();
				try{
					tramite.setIdArchivo(idArchivo);
					tramite.setIdUsuario(usuario.getPersona().getIdPersona());
					tramite.setConsecutivo(consecutivo++);
					tramite.setIdGarantia(trasnsmision.getIdentificadorGarantia().getIdGarantia().intValue());
					tramite.setIdTipoTramite(8);
					tramite.setIdEstatus(5);
					tramite.setIdAcreedor(acreedor.getIdPersona());
					tramite.setClaveRastreo(trasnsmision.getIdentificadorGarantia().getClaveRastreo());
					
					tramiteRes.setClaveRastreo(tramite.getClaveRastreo());
					
					masivaService.verifyAcreedorOfGarantia(tramite.getIdAcreedor(), tramite.getIdGarantia());
					
					masivaService.validaGarantia(tramite.getIdGarantia(), idGarantiasInXML);
					
					masivaDAO.altaParteTramIncRast(tramite);
					masivaDAO.spCopiaGarantia(tramite);
					
					
					//Anotacion que instruye 
					juezDAO.insertAutoridadInstruyeAsiento(tramite.getIdTramite(), trasnsmision.getPersonaSolicitaAutoridadInstruyeAsiento());
					
					
					//validacion y transmision de Acreedor
					if(trasnsmision.getAcreedor()!=null&&trasnsmision.getAcreedor().getIdAcreedor()!=null){
						masivaService.changeAcreedorGarantia(trasnsmision.getAcreedor().getIdAcreedor().intValue(),
								tramite.getIdAcreedor(), tramite.getIdUsuario(), tramite.getIdTramite());
					}
					
					
					//PARTES
					if(trasnsmision.getEliminarPartesTransmision()!=null){
						
						if(trasnsmision.getEliminarPartesTransmision().isEliminaOtorgantes()!=null&&trasnsmision.getEliminarPartesTransmision().isEliminaOtorgantes()){
							if(trasnsmision.getOtorgante()!=null&&trasnsmision.getOtorgante().size()>0){
								masivaDAO.executeBajaParteLogicaMasiva(usuario.getPersona().getIdPersona(), 1, tramite.getIdTramite());
							}else{
								throw new CargaMasivaException(100);
							}
						}
						
						if(trasnsmision.getEliminarPartesTransmision().isEliminaAcreedorAdicional()!=null&&trasnsmision.getEliminarPartesTransmision().isEliminaAcreedorAdicional()){
							masivaDAO.executeBajaParteLogicaMasiva(usuario.getPersona().getIdPersona(), 3, tramite.getIdTramite());
						}
						
					}
					
					if(trasnsmision.getOtorgante()!=null&&trasnsmision.getOtorgante().size()>0){
						masivaService.addOtorgantes(trasnsmision.getOtorgante(),usuario,tramite);
					}
					if(trasnsmision.getAcreedorAdicional()!=null&&trasnsmision.getAcreedorAdicional().size()>0){
						masivaService.addAcreedorAdicional(trasnsmision.getAcreedorAdicional(),usuario,tramite);
					}
				
					

					//Contrato Convenio
						
					if(trasnsmision.getConvenio()!=null){
						//Validacion Datos
						garantiaServiceImpl.validaDatos(trasnsmision.getConvenio().getActoConvenio(), "acto-convenio", false, true, null);
						garantiaServiceImpl.validaDatos(trasnsmision.getConvenio().getTerminosCondiciones(), "terminos-condiciones", false, false, null);
						
						//VAlidacion Fechas
						Date fechaInicio= null;
						if(trasnsmision.getConvenio().getFechaCelebracion()!=null){
							fechaInicio= new Date(trasnsmision.getConvenio().getFechaCelebracion().toGregorianCalendar().getTime().getTime());
							garantiaServiceImpl.validaDatos(fechaInicio, "fecha-celebracion", true, false, 1);
						}
						
						Date fechaTerminacion= null;
						if(trasnsmision.getConvenio().getFechaTerminacion()!=null){
							fechaTerminacion= new Date(trasnsmision.getConvenio().getFechaTerminacion().toGregorianCalendar().getTime().getTime());
						garantiaServiceImpl.validaDatos(fechaTerminacion, "fecha-terminacion", false, false, 2);
						}
						
						
						
						
						//insert Contrato
						masivaDAO.insertaContratoOConvenio(
								fechaInicio, 
								fechaTerminacion,
								trasnsmision.getConvenio().getTerminosCondiciones(), 
								trasnsmision.getConvenio().getActoConvenio(), 
								tramite.getIdGarantiaPendiente(),
								tramite.getIdTramite(),
								usuario);
						
					}
					
					//Resumen
					
					corectos= corectos+1;
					tramiteRes.setCodigoError("0");
					tramiteRes.setMensajeError("La Transmision se agrego correctamente"); 
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
					
				}finally{
					
					if(trasnsmision.getIdentificadorGarantia().getClaveRastreo()!=null){
						tramiteRes.setClaveRastreo(trasnsmision.getIdentificadorGarantia().getClaveRastreo());
					}
					
					resultado.getTramites().getTramite().add(tramiteRes);
					
					if(!isCorrecto){
						trasnsmision=null;
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
