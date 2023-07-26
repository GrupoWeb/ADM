package mx.gob.se.rug.masiva.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.common.dto.Mensaje;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.inscripcion.dao.FirmaMasivaDAO;
import mx.gob.se.rug.inscripcion.service.InscripcionService;
import mx.gob.se.rug.inscripcion.service.impl.InscripcionServiceImpl;
import mx.gob.se.rug.inscripcion.to.FirmaMasivaTO;
import mx.gob.se.rug.masiva.dao.MasivaDAO;
import mx.gob.se.rug.masiva.exception.StoreProcedureException;
import mx.gob.se.rug.masiva.resultado.to.ResultadoCargaMasiva;
import mx.gob.se.rug.masiva.resultado.to.Resumen;
import mx.gob.se.rug.masiva.resultado.to.TramiteRes;
import mx.gob.se.rug.masiva.resultado.to.Tramites;
import mx.gob.se.rug.masiva.service.AvisoPreventivoMasivaService;
import mx.gob.se.rug.masiva.service.MasivaService;
import mx.gob.se.rug.masiva.to.ArchivoTO;
import mx.gob.se.rug.masiva.to.AvisoPreventivo;
import mx.gob.se.rug.masiva.to.CargaMasiva;
import mx.gob.se.rug.masiva.to.CargaMasivaProcess;
import mx.gob.se.rug.masiva.to.ResCargaMasiva;
import mx.gob.se.rug.masiva.to.Tramite;
import mx.gob.se.rug.masiva.to.string.CargaMasivaPreProcesed;
import mx.gob.se.rug.to.PlSql;
import mx.gob.se.rug.to.UsuarioTO;

public class AvisoPreventivoMasivaServiceImpl implements AvisoPreventivoMasivaService{
	//
	
	private Mensaje mensaje;
	private Map<String, Object> sessionMap;
	
	
	public Mensaje getMensaje() {
		return mensaje;
	}
	public void setMensaje(Mensaje mensaje) {
		this.mensaje = mensaje;
	}

	public Map<String, Object> getSessionMap() {
		return sessionMap;
	}
	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}



	//servicios
	private MasivaService masivaService = new MasivaServiceImpl();
	private InscripcionService inscripcionService = new  InscripcionServiceImpl();
	
	//Inyeccion
	public void setMasivaService(MasivaService masivaService) {
		this.masivaService = masivaService;
	}
	public void setInscripcionService(InscripcionService inscripcionService) {
		this.inscripcionService = inscripcionService;
	}
	
	
	//Logica
	


	public ResCargaMasiva cargaAvisoPreventivo(ResCargaMasiva resCargaMasiva,CargaMasivaPreProcesed cargaMasivaPreProcesed, CargaMasiva cargaMasiva,UsuarioTO usuario, AcreedorTO acreedor,Integer idArchivo,String nombreArchivo) {
		MasivaDAO masivaDAO = new MasivaDAO();
		PlSql plSql;
		resCargaMasiva.setListaTramites(new ArrayList<PlSql>());
		resCargaMasiva.setListaTramitesErroneos(new ArrayList<PlSql>());
		try {
			// recorremos los elementos de anotacion
			//sessionMap.put(Constants.ID_ARCHIVO, idArchivo);
			//sessionMap.put(Constants.ID_ACREEDOR_REPRESENTADO, acreedor);
			Tramite tramite = new Tramite();
			tramite.setIdUsuario(usuario.getPersona().getIdPersona());
			tramite.setIdArchivo(idArchivo);
			tramite.setIdTipoTramite(Constants.ID_TIPO_TRAMITE_AVISO_PREVENTIVO);
			tramite.setIdPaso(0);
			tramite.setIdEstatus(0);
			
			List<AvisoPreventivo> avisosPreventivosCorrectos = new ArrayList<AvisoPreventivo>();
			for (AvisoPreventivo avisoPreventivo : cargaMasiva.getAvisoPreventivo()) {
				plSql = new PlSql();
				 
				tramite.setIdAcreedor(Integer.valueOf(acreedor.getIdAcreedor()));
				tramite.setClaveRastreo(avisoPreventivo.getIdentificador().getClaveRastreo());
				try{
					
					masivaDAO.executeAltaTramiteRestreo(tramite);
					if(avisoPreventivo.getOtorgante()!=null){
						plSql =masivaService.agregaOtorganteInd(avisoPreventivo.getOtorgante(), tramite);
						if (plSql.getIntPl().intValue() == 0) {
							plSql = masivaDAO.executeAltaAvisoPreventivoMasivo(avisoPreventivo, tramite);
								
							if (plSql.getIntPl().intValue() ==  0){
								plSql= masivaDAO.executeAltaBitacoraTramite2(tramite.getIdTramite(), 5, 0, null, "V");
								if (plSql.getIntPl().intValue()==0){
									plSql.setIntPl(0);
									plSql.setStrPl("El aviso preventivo fue procesado correctamente");
									plSql.setResIntPl(tramite.getIdTramite());
									plSql.setResStrPl(avisoPreventivo.getIdentificador().getClaveRastreo());
									resCargaMasiva.getListaTramites().add(plSql);
									avisosPreventivosCorrectos.add(avisoPreventivo);
								}else{
									plSql.setResStrPl(avisoPreventivo.getIdentificador().getClaveRastreo());
									resCargaMasiva.getListaTramitesErroneos().add(plSql);
								}
							}else{
								plSql.setResStrPl(avisoPreventivo.getIdentificador().getClaveRastreo());
								resCargaMasiva.getListaTramitesErroneos().add(plSql);
							}
						}else{
							plSql.setResStrPl(avisoPreventivo.getIdentificador().getClaveRastreo());
							resCargaMasiva.getListaTramitesErroneos().add(plSql);
						}
					}else{//Mas de un otorgante o no otorgante
						plSql.setIntPl(85);
						plSql.setStrPl("El numero de otorgantes debe ser 1");
						plSql.setResStrPl(avisoPreventivo.getIdentificador().getClaveRastreo());
						resCargaMasiva.getListaTramitesErroneos().add(plSql);
					}
					
				
				}catch(StoreProcedureException exception){
					plSql= exception.getPlSql();
					plSql.setResStrPl(avisoPreventivo.getIdentificador().getClaveRastreo());
					resCargaMasiva.getListaTramitesErroneos().add(plSql);
					
				}
	
			}
			
				
			// preguntamos si hubieron tramites correctos
			if (resCargaMasiva.getListaTramites().size() > 0) {
				// preguntamos si uvieron tramites erroneos
				if (resCargaMasiva.getListaTramitesErroneos().size() > 0) {
					// si existen tramites erroneos generamos el nuevo XML que
					// se va a firmar
					CargaMasiva cargaMasiva2 = new CargaMasiva();
					cargaMasiva2.getAvisoPreventivo().addAll(
							avisosPreventivosCorrectos);
					ArchivoTO archivoN = new ArchivoTO();
					archivoN.setAlgoritoHash(masivaService.getSha1FromFile(masivaService.convertXMLObjetc(cargaMasiva2)));
					archivoN.setArchivo(masivaService.convertXMLObjetc(cargaMasiva2));
					archivoN.setDescripcion("Archivo nuevo de carga masiva del usuario : "
							+ usuario.getNombre()
							+ ", con el id :"
							+ usuario.getPersona().getIdPersona()
							+ ", resultado de una carga que contenia archivos incorrectos");
					archivoN.setIdUsuario(usuario.getPersona().getIdPersona());
					archivoN.setNombreArchivo(nombreArchivo+"-Correctos");
					archivoN.setTipoArchivo("xml");
					resCargaMasiva.setIdArchivo(inscripcionService.insertArchivo(archivoN).getResIntPl());
				}
			} else {
				setMensaje(new Mensaje());
				getMensaje().setMensaje("No existieron tramites correctos");
			}

			// generamos el XML de resultado
			ResultadoCargaMasiva resultado = new ResultadoCargaMasiva();
			List<TramiteRes> tramitesResultados = new ArrayList<TramiteRes>();
			List<Integer> tramitesId = new ArrayList<Integer>();
			for (PlSql plSqlRes : resCargaMasiva.getListaTramites()) {
				TramiteRes e = new TramiteRes();
				e.setCodigoError(plSqlRes.getIntPl() + "");
				e.setMensajeError(plSqlRes.getStrPl());
				e.setClaveRastreo(plSqlRes.getResStrPl());
				tramitesId.add(plSqlRes.getResIntPl());
				tramitesResultados.add(e);
			}
			for (PlSql plSqlRes : resCargaMasiva.getListaTramitesErroneos()) {
				TramiteRes e = new TramiteRes();
				e.setCodigoError(plSqlRes.getIntPl() + "");
				e.setMensajeError(plSqlRes.getStrPl());
				e.setClaveRastreo(plSqlRes.getResStrPl());
				tramitesResultados.add(e);
			}
			resultado.setResumen(new Resumen());
			resultado.getResumen().setNumeroRegistros(String.valueOf(cargaMasivaPreProcesed.getTotalTramites()));
			resultado.getResumen().setCorrectos(resCargaMasiva.getListaTramites().size() + "");
			resultado.getResumen().setErroneos(resCargaMasiva.getListaTramitesErroneos().size() + "");
			resultado.setTramites(new Tramites());
			resultado.getTramites().getTramite().addAll(tramitesResultados);
			resultado.getTramites().getTramite().addAll(cargaMasivaPreProcesed.getTramiteIncorrectos());

			
			
//			byte[] bytes2 = masivaService.convertXMLObjetc(resultado);
//			archivoRes.setAlgoritoHash(masivaService.getSha1FromFile(bytes2));
//			archivoRes.setArchivo(bytes2);
//			archivoRes.setDescripcion("Archivo nuevo de carga masiva de aviso preventivo del usuario : "
//							+ usuario.getNombre()
//							+ ", con el id :"
//							+ usuario.getPersona().getIdPersona()
//							+ ", resultado de una carga que contenia archivos incorrectos");
//			archivoRes.setIdUsuario(usuario.getPersona().getIdPersona());
//			archivoRes.setNombreArchivo(nombreArchivo+"-Resumen");
//			archivoRes.setTipoArchivo("xml");
//			archivoRes.setIdArchivo(inscripcionService.insertArchivo(archivoRes).getResIntPl());
//			resCargaMasiva.setIdArchivoXML(archivoRes.getIdArchivo());
//			idArchivo=resCargaMasiva.getIdArchivo();
//			resCargaMasiva.setTramitesResultado(tramitesResultados);
			
			ArchivoTO archivoViejo= new ArchivoTO();
			archivoViejo.setNombreArchivo(nombreArchivo);
			archivoViejo.setIdArchivo(idArchivo);
			CargaMasivaProcess masivaProcess = new CargaMasivaProcess();
			resultado.setResumen(getResumen(cargaMasivaPreProcesed, resCargaMasiva.getListaTramites().size() , resCargaMasiva.getListaTramitesErroneos().size()));
			masivaService.generaArchivoResumen(resultado, usuario, archivoViejo, masivaProcess);
			// terminamos de generar el XML de resultado

			// generamos el tramite de firma masiva en caso de aver correctos
			if (resCargaMasiva.getListaTramites().size() > 0) {
				Integer idUsuario = usuario.getPersona().getIdPersona();
				masivaProcess.setIdArchivoFirma(resCargaMasiva.getIdArchivo());
				masivaDAO.actualizaProcessCargaIdFirma(masivaProcess);
				FirmaMasivaTO firmaMasivaTO = new FirmaMasivaTO();
				firmaMasivaTO.setIdUsuario(idUsuario);
				firmaMasivaTO.setIdArchivo(resCargaMasiva.getIdArchivo());
				firmaMasivaTO.setIdAcreedor(tramite.getIdAcreedor());
				String tramites = masivaService.listToString(tramitesId);
				firmaMasivaTO.setTramites(tramites);
				FirmaMasivaDAO firmaDao = new FirmaMasivaDAO();
				int valor = firmaDao.crearFirmaMasiva(firmaMasivaTO);
				System.out.println("valor :" + valor);
				if (valor != 0) {
					//sessionMap.put(Constants.ID_TRAMITE_NUEVO, valor);
					resCargaMasiva.setIdTramiteNuevo(valor);
				}
			}
			// terminamos el tramite de firma masiva
			resCargaMasiva.setRegresa(Constants.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			setMensaje(new Mensaje());
			getMensaje().setId("999");
			getMensaje().setMensaje(
					"Sucedio un error en las avi0so peventivo, el proceso sera revertido. Mensaje :"
							+ e.getMessage());
			for (PlSql sql : resCargaMasiva.getListaTramites()) {
				if (sql.getResIntPl() != null) {
					inscripcionService.bajaTramiteIncompleto(sql.getResIntPl());
				}
			}
		} finally {
			for (PlSql sql : resCargaMasiva.getListaTramitesErroneos()) {
				if (sql.getResIntPl() != null) {
					inscripcionService.bajaTramiteIncompleto(sql.getResIntPl());
				}
			}
		}

		return resCargaMasiva;
	}

	private Resumen getResumen(CargaMasivaPreProcesed cMasivaPreProcesed,Integer buenos,Integer malos){
		java.util.Date date = new java.util.Date();
		int tramitesIncorrectosFiltro = getTramitesIncorrectosFiltro(cMasivaPreProcesed);
		
		Resumen resumen = new Resumen();
		resumen.setMensajeError("Archivo generado en la fecha :" + date.toString());
		resumen.setCorrectos(String.valueOf(buenos));
		resumen.setErroneos(String.valueOf(malos + tramitesIncorrectosFiltro));
		//resumen.setNumeroRegistros(String.valueOf(listaTramites.size() + listaTramitesErrores.size()));
		resumen.setNumeroRegistros(String.valueOf(cMasivaPreProcesed.getTotalTramites()));
		return resumen;
	}
	
	private Integer getTramitesIncorrectosFiltro(CargaMasivaPreProcesed cMasivaPreProcesed){
		int contTramitesIncorrectos = 0;
		if(cMasivaPreProcesed.getTramiteIncorrectos() != null && cMasivaPreProcesed.getTramiteIncorrectos().size() > 0){
			contTramitesIncorrectos = cMasivaPreProcesed.getTramiteIncorrectos().size();
		}
		return contTramitesIncorrectos;
	}


}
