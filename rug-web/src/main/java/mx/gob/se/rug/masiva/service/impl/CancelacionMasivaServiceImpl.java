package mx.gob.se.rug.masiva.service.impl;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.xml.bind.JAXBException;

import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.inscripcion.dao.FirmaMasivaDAO;
import mx.gob.se.rug.inscripcion.dao.InscripcionDAO;
import mx.gob.se.rug.inscripcion.service.InscripcionService;
import mx.gob.se.rug.inscripcion.to.FirmaMasivaTO;
import mx.gob.se.rug.masiva.resultado.to.ResultadoCargaMasiva;
import mx.gob.se.rug.masiva.resultado.to.Resumen;
import mx.gob.se.rug.masiva.resultado.to.Tramites;
import mx.gob.se.rug.masiva.service.CancelacionMasivaService;
import mx.gob.se.rug.masiva.to.ArchivoTO;
import mx.gob.se.rug.masiva.to.Cancelacion;
import mx.gob.se.rug.masiva.to.CargaMasiva;
import mx.gob.se.rug.masiva.to.ControlError;
import mx.gob.se.rug.masiva.to.ResCargaMasiva;
import mx.gob.se.rug.masiva.to.Tramite;
import mx.gob.se.rug.to.PlSql;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;

public class CancelacionMasivaServiceImpl implements CancelacionMasivaService{
	
	private InscripcionService inscripcionService;

	@Override
	public ResCargaMasiva cargaMasivaCancelacion(UsuarioTO usuario, CargaMasiva cm,
			AcreedorTO acreedor, Integer idArchivo, Integer idAcreedorTO) {
		// TODO Auto-generated method stub
//		ResCargaMasiva regresa = new ResCargaMasiva();
//		regresa.setResultado(new ResultadoCargaMasiva());
//		regresa.setListaTramites(new ArrayList<Integer>());	
//		regresa.setListaErrores(new ArrayList<ControlError>());		
//		regresa.setRegresa("failed");
//		int consecutivo = 1;
//		Tramite tramite;
//		ControlError cerror;
//		try{
//			regresa.setIdArchivo(idArchivo);
//			regresa.setIdACreedorRepresentado(idAcreedorTO);
//			acreedor = inscripcionService.getAcreedorByID(idAcreedorTO);
//			for (Cancelacion cancelacion : cm.getCancelacion()){
//				tramite = new Tramite();
//				tramite.setIdArchivo(idArchivo);
//				tramite.setIdUsuario(usuario.getPersona().getIdPersona());
//				tramite.setConsecutivo(consecutivo++);
//				tramite.setIdGarantia(Integer.valueOf(cancelacion.getIdentificador().getIdGarantia()));
//				tramite.setIdTipoTramite(7);
//				tramite.setIdEstatus(5);
//				tramite.setIdAcreedor(idAcreedorTO);
//				tramite.setClaveRastreo(cancelacion.getIdentificador().getClaveRastreo());
//				cerror = agregaCancelacion(cancelacion, tramite);
//				if (cerror != null) {
//					regresa.getListaErrores().add(cerror);
//				}
//			}
//		}catch(Exception e){
//			java.util.Date date = new java.util.Date();
//			Resumen resumen = new Resumen();
//			resumen.setMensajeError("Sucedio un error en el sistema la fecha :" + date.toString() +", enviar el XML a soporte. codError["+date.getTime()+"]");
//			regresa.getResultado().setResumen(resumen);
//			e.printStackTrace();
//		}finally{
//			
//			try {
//				tramitesErroneos = listaTramitesErrores.size();
//				ArchivoTO archivoRes = new ArchivoTO();
//				byte[] bytes2 = convertXMLObjetc(resultado);
//				archivoRes.setAlgoritoHash(getSha1FromFile(bytes2));
//				archivoRes.setArchivo(bytes2);
//				archivoRes.setDescripcion("Archivo nuevo de carga masiva de modificacion del usuario : "
//						+ usuario.getNombre()
//						+ ", con el id :"
//						+ usuario.getPersona().getIdPersona()+", resultado de una carga que contenia archivos incorrectos");
//				archivoRes.setIdUsuario(usuario.getPersona().getIdPersona());
//				archivoRes.setNombreArchivo("cmResnuevo");
//				archivoRes.setTipoArchivo("xml");
//				setIdArchivoRes(inscripcionService.insertArchivo(archivoRes));
//				if (listaTramitesErrores.size()>0){
//					MyLogger.Logger.log(Level.WARNING,"#### sucedieron errores en la carga");
//					// crear nuevo archivo XML y guardarlo para el tramite de firma
//					List<Cancelacion> listaCorrectos = new ArrayList<Cancelacion>();
//					for(mx.gob.se.rug.masiva.resultado.to.TramiteRes inscripcionRes : resultado.getTramites().getTramite()){
//						if (inscripcionRes.getCodigoError().equals("0")){
//							for (Cancelacion ins : cm.getCancelacion()){
//								if (ins.getIdentificador().getClaveRastreo().trim().equals(inscripcionRes.getClaveRastreo().trim())){
//									listaCorrectos.add(ins);
//									break;
//								}
//							}
//						}
//					}
//					
//					if (listaCorrectos.size()>0){
//						
//						MyLogger.Logger.log(Level.INFO,"### se genera un nuevo archivo XML");
//						CargaMasiva carga = new CargaMasiva();
//						carga.getCancelacion().addAll(listaCorrectos);
//						byte[] bytes = convertXMLObjetc(carga);
//						
//						ArchivoTO archivoN = new ArchivoTO();
//						archivoN.setAlgoritoHash(getSha1FromFile(bytes));
//						archivoN.setArchivo(bytes);
//						archivoN.setDescripcion("Archivo nuevo de carga masiva del usuario : "
//								+ usuario.getNombre()
//								+ ", con el id :"
//								+ usuario.getPersona().getIdPersona()+", resultado de una carga que contenia archivos incorrectos");
//						archivoN.setIdUsuario(usuario.getPersona().getIdPersona());
//						archivoN.setNombreArchivo("cmResnuevo");
//						archivoN.setTipoArchivo("xml");
//						idArchivo = inscripcionService.insertArchivo(archivoN);
//						sessionMap.put(Constants.ID_ARCHIVO, idArchivo);
//						MyLogger.Logger.log(Level.INFO,"### se genero el nuevo archivo con el id :" +idArchivo);
//					}
//					
//				}
//				sessionMap.put(Constants.TRAMITES, listaTramites);
//				if (listaTramites.size()>0){
//					idArchivo = (Integer) sessionMap.get(Constants.ID_ARCHIVO);
//					Integer idAcreedor = (Integer) sessionMap
//							.get(Constants.ID_ACREEDOR_REPRESENTADO);
//					Integer idUsuario = usuario.getPersona().getIdPersona();
//					FirmaMasivaTO firmaMasivaTO = new FirmaMasivaTO();
//					firmaMasivaTO.setIdUsuario(idUsuario);
//					firmaMasivaTO.setIdArchivo(idArchivo);
//					firmaMasivaTO.setIdAcreedor(idAcreedor);
//					String tramites = listToString(listaTramites);
//					firmaMasivaTO.setTramites( tramites);
//					FirmaMasivaDAO firmaDao = new FirmaMasivaDAO();
//					int valor = firmaDao.crearFirmaMasiva(firmaMasivaTO);
//					MyLogger.Logger.log(Level.INFO,"firmaDao.crearFirmaMasiva(firmaMasivaTO)--valor--"+valor);
//					if (valor != 0) {
//						sessionMap.put(Constants.ID_TRAMITE_NUEVO, valor);
//					}
//				}
//				regresa = "success";
//				
//
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				MyLogger.Logger.log(Level.WARNING,"ThreadCargaMasiva:::archivo ["+idArchivo+"]::: sucedio un error faltal:::");
//				e.printStackTrace();
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				MyLogger.Logger.log(Level.WARNING, "ThreadCargaMasiva:::archivo ["+idArchivo+"]::: sucedio un error faltal:::");
//				e.printStackTrace();
//			} catch (JAXBException e) {
//				// TODO Auto-generated catch block
//				MyLogger.Logger.log(Level.WARNING, "ThreadCargaMasiva:::archivo ["+idArchivo+"]::: sucedio un error faltal:::");
//				e.printStackTrace();
//			}catch(Exception e){
//				// TODO Auto-generated catch block
//				MyLogger.Logger.log(Level.WARNING, "ThreadCargaMasiva:::archivo ["+idArchivo+"]::: sucedio un error faltal:::");
//				e.printStackTrace();
//			}
//			for (Integer tramiteErroneo:listaTramitesErrores){
//				inscripcionDAO.bajaTramiteIncompleto(tramiteErroneo);
//			}
//			
//			
//		}
//		
//		return regresa;
//	}
//	
//	private ControlError agregaCancelacion(Cancelacion cancelacion, Tramite tramite) {
//		ControlError regresa = null;
//		mx.gob.se.rug.masiva.resultado.to.TramiteRes inscripcionRes = new mx.gob.se.rug.masiva.resultado.to.TramiteRes();
//		regresa = verificaDatosCancelacion(cancelacion, tramite,inscripcionRes);
//		resultado = new ResultadoCargaMasiva();
//		resultado.setTramites(new Tramites());
//		if (regresa == null){
//			PlSql plSql = masivaDAO.executeAltaCancelacionMasiva(tramite,cancelacion);
//			if (plSql.getIntPl().intValue() == 0) {
//				listaTramites.add(plSql.getResIntPl());
//				inscripcionRes.setMensajeError("La cancelacion fue procesada correctamente");
//				inscripcionRes.setClaveRastreo(cancelacion.getIdentificador().getClaveRastreo());
//				inscripcionRes.setCodigoError("0");
//				resultado.getTramites().getTramite().add(inscripcionRes);
//				regresa = null;
//			}else{
//				listaTramitesErrores.add(plSql.getResIntPl().intValue());
//				inscripcionRes.setMensajeError(plSql.getStrPl());
//				inscripcionRes.setClaveRastreo(cancelacion.getIdentificador().getClaveRastreo());
//				inscripcionRes.setCodigoError(plSql.getIntPl().toString());
//				inscripcionesErroneas.add(inscripcionRes);
//				resultado.getTramites().getTramite().add(inscripcionRes);
//			}
//		}
//		if (regresa != null) {
//			regresa.setClaveRastreo(tramite.getClaveRastreo());
//		}
		return null;
		
	}
}
