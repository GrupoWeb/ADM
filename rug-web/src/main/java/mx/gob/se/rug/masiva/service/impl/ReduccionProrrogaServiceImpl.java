package mx.gob.se.rug.masiva.service.impl;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.xml.bind.JAXBException;

import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.inscripcion.dao.FirmaMasivaDAO;
import mx.gob.se.rug.inscripcion.service.InscripcionService;
import mx.gob.se.rug.inscripcion.to.FirmaMasivaTO;
import mx.gob.se.rug.masiva.resultado.to.ResultadoCargaMasiva;
import mx.gob.se.rug.masiva.resultado.to.Resumen;
import mx.gob.se.rug.masiva.resultado.to.TramiteRes;
import mx.gob.se.rug.masiva.service.MasivaService;
import mx.gob.se.rug.masiva.service.ReduccionProrrogaService;
import mx.gob.se.rug.masiva.to.ArchivoTO;
import mx.gob.se.rug.masiva.to.Cancelacion;
import mx.gob.se.rug.masiva.to.CargaMasiva;
import mx.gob.se.rug.masiva.to.ControlError;
import mx.gob.se.rug.masiva.to.RenovacionReduccion;
import mx.gob.se.rug.masiva.to.ResCargaMasiva;
import mx.gob.se.rug.masiva.to.Tramite;
import mx.gob.se.rug.to.PlSql;
import mx.gob.se.rug.util.MyLogger;

/**
 *	@author Abraham Stalin
 *	codigos de error 610
 */
public class ReduccionProrrogaServiceImpl implements ReduccionProrrogaService {
	private InscripcionService inscripcionService;
	private MasivaService masivaService;
	
	public void setMasivaService(MasivaService masivaService) {
		this.masivaService = masivaService;
	}

	public void setInscripcionService(InscripcionService inscripcionService) {
		this.inscripcionService = inscripcionService;
	}
	/*
	 *	Codigos de errores:
	 * 610 --> el elemento no tiene la parte de <datos-Renovacion-reduccion> 
	 */

	@Override	
	public ResCargaMasiva cargaMasivaReduccionProrrogaService(
			Tramite tramite, CargaMasiva cm) {
		// TODO Auto-generated method stub
		ResCargaMasiva resCargaMasiva = new ResCargaMasiva();
		String regresa = "failed";
		int idArchivo = 0;
		MyLogger.Logger.log(Level.INFO,"entro a carga masiva cancelacion");
		try{
			for (RenovacionReduccion renovacionReduccion : cm.getRenovacionReduccion()){
				agregaRenovacionReduccion(renovacionReduccion, tramite, resCargaMasiva);
			}
		}catch(Exception e){
			java.util.Date date = new java.util.Date();
			Resumen resumen = new Resumen();
			resumen.setMensajeError("Sucedio un error en el sistema la fecha :" + date.toString() +", enviar el XML a soporte. codError["+date.getTime()+"]");
			resCargaMasiva.getResultado().setResumen(resumen);
			e.printStackTrace();
		}finally{
			
			try {
				int tramitesErroneos = resCargaMasiva.getListaErrores().size();
				ArchivoTO archivoRes = new ArchivoTO();
				byte[] bytes2 = masivaService.convertXMLObjetc(resCargaMasiva.getResultado());
				archivoRes.setAlgoritoHash(masivaService.getSha1FromFile(bytes2));
				archivoRes.setArchivo(bytes2);
				archivoRes.setDescripcion("Archivo nuevo de carga masiva de modificacion del usuario : "
						+ tramite.getUsuario().getNombre()
						+ ", con el id :"
						+ + tramite.getUsuario().getPersona().getIdPersona()+", resultado de una carga que contenia archivos incorrectos");
				archivoRes.setIdUsuario(+ tramite.getUsuario().getPersona().getIdPersona());
				archivoRes.setNombreArchivo("cmResnuevo");
				archivoRes.setTipoArchivo("xml");
				if (tramitesErroneos>0){
					MyLogger.Logger.log(Level.WARNING,"#### sucedieron errores en la carga");
					// crear nuevo archivo XML y guardarlo para el tramite de firma
					List<RenovacionReduccion> listaCorrectos = new ArrayList<RenovacionReduccion>();
					for(TramiteRes tramiteRes : resCargaMasiva.getResultado().getTramites().getTramite()){
						if (tramiteRes.getCodigoError().equals("0")){
							for (RenovacionReduccion ins : cm.getRenovacionReduccion()){
								if (ins.getIdentificadorGarantia().getClaveRastreo().trim().equals(tramiteRes.getClaveRastreo().trim())){
									listaCorrectos.add(ins);
									break;
								}
							}
						}
					}
					
					if (listaCorrectos.size()>0){
						CargaMasiva carga = new CargaMasiva();
						carga.getRenovacionReduccion().addAll(listaCorrectos);
						byte[] bytes = masivaService.convertXMLObjetc(carga);
						
						ArchivoTO archivoN = new ArchivoTO();
						archivoN.setAlgoritoHash(masivaService.getSha1FromFile(bytes));
						archivoN.setArchivo(bytes);
						archivoN.setDescripcion("Archivo nuevo de carga masiva del usuario : "
								+ tramite.getUsuario().getNombre()
								+ ", con el id :"
								+ tramite.getUsuario().getPersona().getIdPersona()+", resultado de una carga que contenia archivos incorrectos");
						archivoN.setIdUsuario(tramite.getUsuario().getPersona().getIdPersona());
						archivoN.setNombreArchivo("cmResnuevo");
						archivoN.setTipoArchivo("xml");
						idArchivo = inscripcionService.insertArchivo(archivoN).getResIntPl();
					}
					
				}
				if (resCargaMasiva.getListaTramites().size()>0){
					FirmaMasivaTO firmaMasivaTO = new FirmaMasivaTO();
					firmaMasivaTO.setIdUsuario(tramite.getIdUsuario());
					firmaMasivaTO.setIdArchivo(idArchivo);
					firmaMasivaTO.setIdAcreedor(tramite.getIdAcreedor());
					List<Integer> listStr = new ArrayList<Integer>();
					for(PlSql plSql:resCargaMasiva.getListaTramites()){
						listStr.add(plSql.getResIntPl());
					}
					String tramites = masivaService.listToString(listStr);
					firmaMasivaTO.setTramites( tramites);
					FirmaMasivaDAO firmaDao = new FirmaMasivaDAO();
					int valorTramiteNuevo = firmaDao.crearFirmaMasiva(firmaMasivaTO);
					resCargaMasiva.setIdTramiteNuevo(valorTramiteNuevo);
				}
				regresa = "success";
				

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
			List<Integer> listStr = new ArrayList<Integer>();
			for(PlSql plSql:resCargaMasiva.getListaTramitesErroneos()){
				listStr.add(plSql.getResIntPl());
			}
			for (Integer tramiteErroneo:listStr){
				inscripcionService.bajaTramiteIncompleto(tramiteErroneo);
			}
			
			
		}
		
		return resCargaMasiva;
	}
	private ControlError agregaRenovacionReduccion(RenovacionReduccion renovacionReduccion, Tramite tramite,ResCargaMasiva resCargaMasiva) {
		ControlError regresa = null;
		regresa = verificaDatosRenovacionReduccion(renovacionReduccion, tramite, resCargaMasiva);
		
		return regresa;
	}
	private ControlError verificaDatosRenovacionReduccion(RenovacionReduccion renovacionReduccion, Tramite tramite,ResCargaMasiva resCargaMasiva){
		ControlError regresa = null;
		TramiteRes res = new TramiteRes();
		if (renovacionReduccion.getDatosRenovacionReduccion()==null){
			regresa = new ControlError();			
			res.setClaveRastreo(tramite.getClaveRastreo());
			res.setCodigoError("610");
			res.setMensajeError("Los datos de la renovacion y reduccion son obligatorios.");
			resCargaMasiva.getTramitesResultado().add(res);
		}			
		if (regresa != null){
			regresa.setClaveRastreo(tramite.getClaveRastreo());
			PlSql plSql = new PlSql();
			plSql.setIntPl(Integer.valueOf(res.getCodigoError()));
			plSql.setStrPl(res.getMensajeError());			
			regresa.setPlSql(plSql);
			regresa.setIdInscripcion(tramite.getIdTramite());
		}
		return regresa;
	}
	
	
}
