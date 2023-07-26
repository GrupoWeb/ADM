package mx.gob.se.rug.masiva.thread;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import mx.gob.se.rug.acreedores.service.AcreedoresService;
import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.inscripcion.dao.AltaParteDAO;
import mx.gob.se.rug.inscripcion.dao.FirmaMasivaDAO;
import mx.gob.se.rug.inscripcion.service.InscripcionService;
import mx.gob.se.rug.inscripcion.to.AltaParteTO;
import mx.gob.se.rug.inscripcion.to.FirmaMasivaTO;
import mx.gob.se.rug.inscripcion.to.OtorganteTO;
import mx.gob.se.rug.masiva.dao.ArchivoDAO;
import mx.gob.se.rug.masiva.dao.MasivaDAO;
import mx.gob.se.rug.masiva.resultado.to.ResultadoCargaMasiva;
import mx.gob.se.rug.masiva.resultado.to.Resumen;
import mx.gob.se.rug.masiva.resultado.to.TramiteRes;
import mx.gob.se.rug.masiva.resultado.to.Tramites;
import mx.gob.se.rug.masiva.service.AvisoPreventivoMasivaService;
import mx.gob.se.rug.masiva.service.MasivaService;
import mx.gob.se.rug.masiva.to.AcreedorAutoridad;
import mx.gob.se.rug.masiva.to.Anotacion;
import mx.gob.se.rug.masiva.to.AnotacionGarantia;
import mx.gob.se.rug.masiva.to.ArchivoTO;
import mx.gob.se.rug.masiva.to.CargaMasiva;
import mx.gob.se.rug.masiva.to.Otorgante;
import mx.gob.se.rug.masiva.to.ResCargaMasiva;
import mx.gob.se.rug.masiva.to.Tramite;
import mx.gob.se.rug.partes.dao.FolioElectronicoDAO;
import mx.gob.se.rug.to.PlSql;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;

public class AutoridadMasivaThread extends Thread {
	private static final int ID_TRAMITE_ACREEDORES = 12;
	private static final int ID_TRAMITE_ANOTACION = 2;
	private static final int ID_TRAMITE_ANOTACION_SIN_GARANTIA = 10;
	private static final int ID_TRAMITE_AVISO_PREVENTIVO = 3;
	//atributos
	private Integer idArchivo;
	private Integer idTipoTramite;
	private Integer idAcreedor;
	private String sha1;
	private UsuarioTO usuario;
	private CargaMasiva cargaMasiva;
	
	// servicios
	private InscripcionService inscripcionService;
	private MasivaService masivaService;
	private AcreedoresService acreedoresService;
	private AvisoPreventivoMasivaService avisoPreventivoMasivaService;
	
	// daos
	private MasivaDAO masivaDAO = new MasivaDAO();
	private ArchivoDAO archivoDAO = new ArchivoDAO();
	
	public void setInscripcionService(InscripcionService inscripcionService) {
		this.inscripcionService = inscripcionService;
	}

	public void setMasivaService(MasivaService masivaService) {
		this.masivaService = masivaService;
	}

	public void setAcreedoresService(AcreedoresService acreedoresService) {
		this.acreedoresService = acreedoresService;
	}
	
	
	public void setAvisoPreventivoMasivaService(
			AvisoPreventivoMasivaService avisoPreventivoMasivaService) {
		this.avisoPreventivoMasivaService = avisoPreventivoMasivaService;
	}

	public AutoridadMasivaThread(Integer idArchivo, Integer idTipoTramite,
			Integer idAcreedor, String sha1, UsuarioTO usuario,
			CargaMasiva cargaMasiva) {
		super();
		this.idArchivo = idArchivo;
		this.idTipoTramite = idTipoTramite;
		this.idAcreedor = idAcreedor;
		this.sha1 = sha1;
		this.usuario = usuario;
		this.cargaMasiva = cargaMasiva;
	}

	public void run(){
		try{
			ResCargaMasiva resCargaMasiva = proceso(cargaMasiva, sha1, usuario);
			archivoDAO.insertArchivoFirmaMasiva(resCargaMasiva.getArchivoTO());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private ResCargaMasiva proceso(CargaMasiva cargaMasiva,
			String sha1, UsuarioTO usuario) {
		ResCargaMasiva respuestaCM = new ResCargaMasiva();
		try {
			respuestaCM.setIdArchivo(idArchivo);
			switch (idTipoTramite.intValue()) {
			case ID_TRAMITE_ACREEDORES:
				// alta de acreedores.
				MyLogger.Logger.log(Level.INFO,":::Se inicia el alta de acreedores por thread:::");
				respuestaCM = cargaAcreedores(respuestaCM, cargaMasiva, usuario);
				break;
			case ID_TRAMITE_ANOTACION:
				// anotacion.
				MyLogger.Logger.log(Level.INFO,":::Se inicia el alta de anotacion por thread:::");
				respuestaCM = cargaAnotacionGarantia(respuestaCM,
						cargaMasiva, usuario);
				break;
			case ID_TRAMITE_ANOTACION_SIN_GARANTIA:
				// anotacion sin garantia.
				MyLogger.Logger.log(Level.INFO,":::Se inicia el alta de anotacion sin garantia por thread:::");
				respuestaCM = cargaAnotacion(respuestaCM, cargaMasiva,
						usuario);
				break;
//			case ID_TRAMITE_AVISO_PREVENTIVO:
//				// aviso preventivo.
//				AcreedorTO acreedor = inscripcionService.getAcreedorByID(idAcreedor);
//				Map s= new HashMap<String, Object>();
//				avisoPreventivoMasivaService.setSessionMap(s);
//				respuestaCM =avisoPreventivoMasivaService.cargaAvisoPreventivo(respuestaCM,cargaMasiva, usuario, acreedor);
//				break;
//
			default:				
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return respuestaCM;
	}

//	// alta de avisos preventivos masivas
//	private ResCargaMasiva cargaAvisoPreventivo(ResCargaMasiva resCargaMasiva,
//			CargaMasiva cargaMasiva, UsuarioTO usuario, AcreedorTO acreedor) {
//		PlSql plSql;
//		resCargaMasiva.setListaTramites(new ArrayList<PlSql>());
//		resCargaMasiva.setListaTramitesErroneos(new ArrayList<PlSql>());
//		try {
//			// recorremos los elementos de anotacion
//			Tramite tramite = new Tramite();
//			tramite.setIdUsuario(usuario.getPersona().getIdPersona());
//			List<AvisoPreventivo> avisosPreventivosCorrectos = new ArrayList<AvisoPreventivo>();
//			for (AvisoPreventivo avisoPreventivo : cargaMasiva
//					.getAvisoPreventivo()) {
//				tramite.setIdAcreedor(Integer.valueOf(acreedor.getIdAcreedor()));
//				plSql = masivaDAO.executeAltaTramiteAcreedorRep(tramite,
//						acreedor);
//				if (plSql.getIntPl().intValue() == 0) {
//					tramite.setIdTramite(plSql.getResIntPl());
//					plSql = masivaDAO.executeAltaAvisoPreventivo(
//							avisoPreventivo, tramite);
//					if (plSql.getIntPl().intValue() == 0) {
//						plSql = masivaDAO.executeAltaBitacoraTramite2(
//								tramite.getIdTramite(), 5, 0, null, "V");
//						if (plSql.getIntPl().intValue() == 0) {
//							plSql.setIntPl(0);
//							plSql.setStrPl("El aviso preventivo fue procesado correctamente");
//							plSql.setResIntPl(tramite.getIdTramite());
//							resCargaMasiva.getListaTramites().add(plSql);
//						} else {
//							resCargaMasiva.getListaTramitesErroneos()
//									.add(plSql);
//						}
//					} else {
//						resCargaMasiva.getListaTramitesErroneos().add(plSql);
//					}
//				} else {
//					resCargaMasiva.getListaTramitesErroneos().add(plSql);
//				}
//			}
//			// preguntamos si uvieron tramites correctos
//			if (resCargaMasiva.getListaTramites().size() > 0) {
//				// preguntamos si uvieron tramites erroneos
//				if (resCargaMasiva.getListaTramitesErroneos().size() > 0) {
//					// si existen tramites erroneos generamos el nuevo XML que
//					// se va a firmar
//					CargaMasiva cargaMasiva2 = new CargaMasiva();
//					cargaMasiva.getAvisoPreventivo().addAll(
//							avisosPreventivosCorrectos);
//					ArchivoTO archivoN = new ArchivoTO();
//					archivoN.setAlgoritoHash(masivaService
//							.getSha1FromFile(masivaService
//									.convertXMLObjetc(cargaMasiva2)));
//					archivoN.setArchivo(masivaService
//							.convertXMLObjetc(cargaMasiva2));
//					archivoN.setDescripcion("Archivo nuevo de carga masiva del usuario : "
//							+ usuario.getNombre()
//							+ ", con el id :"
//							+ usuario.getPersona().getIdPersona()
//							+ ", resultado de una carga que contenia archivos incorrectos");
//					archivoN.setIdUsuario(usuario.getPersona().getIdPersona());
//					archivoN.setNombreArchivo("cmResnuevo");
//					archivoN.setTipoArchivo("xml");
//					resCargaMasiva.setIdArchivo(inscripcionService
//							.insertArchivo(archivoN).getResIntPl());
//				}
//			}
//
//			// generamos el XML de resultado
//			ArchivoTO archivoRes = new ArchivoTO();
//			ResultadoCargaMasiva resultado = new ResultadoCargaMasiva();
//			List<TramiteRes> tramitesResultados = new ArrayList<TramiteRes>();
//			List<Integer> tramitesId = new ArrayList<Integer>();
//			for (PlSql plSqlRes : resCargaMasiva.getListaTramites()) {
//				TramiteRes e = new TramiteRes();
//				e.setCodigoError(plSqlRes.getIntPl() + "");
//				e.setMensajeError(plSqlRes.getStrPl());
//				tramitesId.add(plSqlRes.getResIntPl());
//				tramitesResultados.add(e);
//			}
//			for (PlSql plSqlRes : resCargaMasiva.getListaTramitesErroneos()) {
//				TramiteRes e = new TramiteRes();
//				e.setCodigoError(plSqlRes.getIntPl() + "");
//				e.setMensajeError(plSqlRes.getStrPl());
//				tramitesResultados.add(e);
//			}
//			resultado.setResumen(new Resumen());
//			resultado.getResumen().setCorrectos(
//					resCargaMasiva.getListaTramites().size() + "");
//			resultado.getResumen().setErroneos(
//					resCargaMasiva.getListaTramitesErroneos().size() + "");
//			resultado.setTramites(new Tramites());
//			resultado.getTramites().getTramite().addAll(tramitesResultados);
//
//			byte[] bytes2 = masivaService.convertXMLObjetc(resultado);
//			archivoRes.setAlgoritoHash(masivaService.getSha1FromFile(bytes2));
//			archivoRes.setArchivo(bytes2);
//			archivoRes
//					.setDescripcion("Archivo nuevo de carga masiva de aviso preventivo del usuario : "
//							+ usuario.getNombre()
//							+ ", con el id :"
//							+ usuario.getPersona().getIdPersona()
//							+ ", resultado de una carga que contenia archivos incorrectos");
//			archivoRes.setIdUsuario(usuario.getPersona().getIdPersona());
//			archivoRes.setNombreArchivo("cmResnuevo");
//			archivoRes.setTipoArchivo("xml");
//			resCargaMasiva.setArchivoTO(archivoRes);
//			// terminamos de generar el XML de resultado
//
//			// generamos el tramite de firma masiva en caso de aver correctos
//			if (resCargaMasiva.getListaTramites().size() > 0) {
//				Integer idUsuario = usuario.getPersona().getIdPersona();
//				FirmaMasivaTO firmaMasivaTO = new FirmaMasivaTO();
//				firmaMasivaTO.setIdUsuario(idUsuario);
//				firmaMasivaTO.setIdArchivo(resCargaMasiva.getIdArchivo());
//				firmaMasivaTO.setIdAcreedor(0);
//				String tramites = masivaService.listToString(tramitesId);
//				firmaMasivaTO.setTramites(tramites);
//				FirmaMasivaDAO firmaDao = new FirmaMasivaDAO();
//				int valor = firmaDao.crearFirmaMasiva(firmaMasivaTO);
//				resCargaMasiva.setIdTramiteNuevo(valor);
//			}
//			// terminamos el tramite de firma masiva
//		} catch (Exception e) {
//			e.printStackTrace();
//			
//			for (PlSql sql : resCargaMasiva.getListaTramites()) {
//				if (sql.getResIntPl() != null) {
//					inscripcionService.bajaTramiteIncompleto(sql.getResIntPl());
//				}
//			}
//		} finally {
//			for (PlSql sql : resCargaMasiva.getListaTramitesErroneos()) {
//				if (sql.getResIntPl() != null) {
//					inscripcionService.bajaTramiteIncompleto(sql.getResIntPl());
//				}
//			}
//		}
//
//		return resCargaMasiva;
//	}

	// alta de Anotaciones con Garantia masivas
	private ResCargaMasiva cargaAnotacionGarantia(
			ResCargaMasiva resCargaMasiva, CargaMasiva cargaMasiva,
			UsuarioTO usuario) {
		PlSql plSql;
		resCargaMasiva.setListaTramites(new ArrayList<PlSql>());
		resCargaMasiva.setListaTramitesErroneos(new ArrayList<PlSql>());
		try {
			// recorremos los elementos de anotacion
			Tramite tramite = new Tramite();
			tramite.setIdUsuario(usuario.getPersona().getIdPersona());
			List<AnotacionGarantia> anotacionesCorrectas = new ArrayList<AnotacionGarantia>();
			for (AnotacionGarantia anotacion : cargaMasiva
					.getAnotacionGarantia()) {
				plSql = masivaDAO.executeAltaTramiteIncompleto(
						tramite.getIdUsuario(), 2);
				if (plSql.getIntPl().intValue() == 0) {
					tramite.setIdTramite(plSql.getResIntPl());
					plSql = masivaDAO.executeAltaBitacoraTramite2(
							tramite.getIdTramite(), 5, 0, null, "V");
					if (plSql.getIntPl().intValue() == 0) {
						plSql = masivaDAO.executeAltaAnotacion(tramite,
								anotacion);
						if (plSql.getIntPl().intValue() == 0) {
							plSql.setIntPl(0);
							plSql.setResIntPl(tramite.getIdTramite());
							anotacionesCorrectas.add(anotacion);
							resCargaMasiva.getListaTramites().add(plSql);
						} else {
							resCargaMasiva.getListaTramitesErroneos()
									.add(plSql);
						}
					} else {
						resCargaMasiva.getListaTramitesErroneos().add(plSql);
					}
				} else {
					resCargaMasiva.getListaTramitesErroneos().add(plSql);
				}
			}
			// preguntamos si uvieron tramites correctos
			if (resCargaMasiva.getListaTramites().size() > 0) {
				// preguntamos si uvieron tramites erroneos
				if (resCargaMasiva.getListaTramitesErroneos().size() > 0) {
					// si existen tramites erroneos generamos el nuevo XML que
					// se va a firmar
					CargaMasiva cargaMasiva2 = new CargaMasiva();
					cargaMasiva.getAnotacionGarantia().addAll(
							anotacionesCorrectas);
					ArchivoTO archivoN = new ArchivoTO();
					archivoN.setAlgoritoHash(masivaService
							.getSha1FromFile(masivaService
									.convertXMLObjetc(cargaMasiva2)));
					archivoN.setArchivo(masivaService
							.convertXMLObjetc(cargaMasiva2));
					archivoN.setDescripcion("Archivo nuevo de carga masiva del usuario : "
							+ usuario.getNombre()
							+ ", con el id :"
							+ usuario.getPersona().getIdPersona()
							+ ", resultado de una carga que contenia archivos incorrectos");
					archivoN.setIdUsuario(usuario.getPersona().getIdPersona());
					archivoN.setNombreArchivo("cmResnuevo");
					archivoN.setTipoArchivo("xml");
					resCargaMasiva.setIdArchivo(inscripcionService
							.insertArchivo(archivoN).getResIntPl());
				}
			} 

			// generamos el XML de resultado
			ArchivoTO archivoRes = new ArchivoTO();
			ResultadoCargaMasiva resultado = new ResultadoCargaMasiva();
			List<TramiteRes> tramitesResultados = new ArrayList<TramiteRes>();
			List<Integer> tramitesId = new ArrayList<Integer>();
			for (PlSql plSqlRes : resCargaMasiva.getListaTramites()) {
				TramiteRes e = new TramiteRes();
				e.setCodigoError(plSqlRes.getIntPl() + "");
				e.setMensajeError(plSqlRes.getStrPl());
				tramitesId.add(plSqlRes.getResIntPl());
				tramitesResultados.add(e);
			}
			for (PlSql plSqlRes : resCargaMasiva.getListaTramitesErroneos()) {
				TramiteRes e = new TramiteRes();
				e.setCodigoError(plSqlRes.getIntPl() + "");
				e.setMensajeError(plSqlRes.getStrPl());
				tramitesResultados.add(e);
			}
			resultado.setResumen(new Resumen());
			resultado.getResumen().setCorrectos(
					resCargaMasiva.getListaTramites().size() + "");
			resultado.getResumen().setErroneos(
					resCargaMasiva.getListaTramitesErroneos().size() + "");
			resultado.setTramites(new Tramites());
			resultado.getTramites().getTramite().addAll(tramitesResultados);

			byte[] bytes2 = masivaService.convertXMLObjetc(resultado);
			archivoRes.setAlgoritoHash(masivaService.getSha1FromFile(bytes2));
			archivoRes.setArchivo(bytes2);
			archivoRes
					.setDescripcion("Archivo nuevo de carga masiva de anotacion del usuario : "
							+ usuario.getNombre()
							+ ", con el id :"
							+ usuario.getPersona().getIdPersona()
							+ ", resultado de una carga que contenia archivos incorrectos");
			archivoRes.setIdUsuario(usuario.getPersona().getIdPersona());
			archivoRes.setNombreArchivo("cmResnuevo");
			archivoRes.setTipoArchivo("xml");
			resCargaMasiva.setArchivoTO(archivoRes);
			// terminamos de generar el XML de resultado

			// generamos el tramite de firma masiva en caso de aver correctos
			if (resCargaMasiva.getListaTramites().size() > 0) {
				Integer idUsuario = usuario.getPersona().getIdPersona();
				FirmaMasivaTO firmaMasivaTO = new FirmaMasivaTO();
				firmaMasivaTO.setIdUsuario(idUsuario);
				firmaMasivaTO.setIdArchivo(resCargaMasiva.getIdArchivo());
				firmaMasivaTO.setIdAcreedor(0);
				String tramites = masivaService.listToString(tramitesId);
				firmaMasivaTO.setTramites(tramites);
				FirmaMasivaDAO firmaDao = new FirmaMasivaDAO();
				int valor = firmaDao.crearFirmaMasiva(firmaMasivaTO);
				resCargaMasiva.setIdTramiteNuevo(valor);
			}
			// terminamos el tramite de firma masiva
		} catch (Exception e) {
			e.printStackTrace();
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

	// alta de Anotaciones masivas
	private ResCargaMasiva cargaAnotacion(ResCargaMasiva resCargaMasiva,
			CargaMasiva cargaMasiva, UsuarioTO usuario) {
		PlSql plSql;
		resCargaMasiva.setListaTramites(new ArrayList<PlSql>());
		resCargaMasiva.setListaTramitesErroneos(new ArrayList<PlSql>());
		try {
			// recorremos los elementos de anotacion
			Tramite tramite = new Tramite();
			tramite.setIdUsuario(usuario.getPersona().getIdPersona());
			List<Anotacion> anotacionesCorrectas = new ArrayList<Anotacion>();
			for (Anotacion anotacion : cargaMasiva.getAnotacion()) {
				// creamos el tramite incompleto
				plSql = masivaDAO.executeAltaTramiteIncompleto(
						tramite.getIdUsuario(), 10);
				if (plSql.getIntPl().intValue() == 0) {
					tramite.setIdTramite(plSql.getResIntPl());
					// tratamos de agregar al otorgante
					if (anotacion.getOtorgante() != null) {
						plSql = agregaOtorganteInd(anotacion.getOtorgante(),
								tramite);
						if (plSql.getIntPl().intValue() != 0) {
							plSql = masivaDAO.executeAltaAnotacionSinGarantia(
									anotacion, tramite);
							if (plSql.getIntPl().intValue() == 0) {
								plSql = masivaDAO
										.executeAltaBitacoraTramite2(
												tramite.getIdTramite(), 5, 0,
												null, "V");
								if (plSql.getIntPl().intValue() == 0) {
									anotacionesCorrectas.add(anotacion);
									plSql.setStrPl("La Anotacion fue procesada correctamente");
									plSql.setIntPl(0);
									plSql.setResIntPl(tramite.getIdTramite());
									resCargaMasiva.getListaTramites()
											.add(plSql);
								} else {
									resCargaMasiva.getListaTramitesErroneos()
											.add(plSql);
								}
							} else {
								resCargaMasiva.getListaTramitesErroneos().add(
										plSql);
							}
						} else {
							resCargaMasiva.getListaTramitesErroneos()
									.add(plSql);
						}
					} else {
						plSql.setIntPl(961);
						plSql.setStrPl("La anotacion debe contener un otorgante");
					}
				} else {
					plSql.setIntPl(966);
					plSql.setStrPl("Sucedio un error al crear el tramite temporal");
					resCargaMasiva.getListaTramitesErroneos().add(plSql);
				}

			}
			// preguntamos si uvieron tramites correctos
			if (resCargaMasiva.getListaTramites().size() > 0) {
				// preguntamos si uvieron tramites erroneos
				if (resCargaMasiva.getListaTramitesErroneos().size() > 0) {
					// si existen tramites erroneos generamos el nuevo XML que
					// se va a firmar
					CargaMasiva cargaMasiva2 = new CargaMasiva();
					cargaMasiva.getAnotacion().addAll(anotacionesCorrectas);
					ArchivoTO archivoN = new ArchivoTO();
					archivoN.setAlgoritoHash(masivaService
							.getSha1FromFile(masivaService
									.convertXMLObjetc(cargaMasiva2)));
					archivoN.setArchivo(masivaService
							.convertXMLObjetc(cargaMasiva2));
					archivoN.setDescripcion("Archivo nuevo de carga masiva del usuario : "
							+ usuario.getNombre()
							+ ", con el id :"
							+ usuario.getPersona().getIdPersona()
							+ ", resultado de una carga que contenia archivos incorrectos");
					archivoN.setIdUsuario(usuario.getPersona().getIdPersona());
					archivoN.setNombreArchivo("cmResnuevo");
					archivoN.setTipoArchivo("xml");
					resCargaMasiva.setIdArchivo(inscripcionService
							.insertArchivo(archivoN).getResIntPl());
				}
			} 

			// generamos el XML de resultado
			ArchivoTO archivoRes = new ArchivoTO();
			ResultadoCargaMasiva resultado = new ResultadoCargaMasiva();
			List<TramiteRes> tramitesResultados = new ArrayList<TramiteRes>();
			List<Integer> tramitesId = new ArrayList<Integer>();
			for (PlSql plSqlRes : resCargaMasiva.getListaTramites()) {
				TramiteRes e = new TramiteRes();
				e.setCodigoError(plSqlRes.getIntPl() + "");
				e.setMensajeError(plSqlRes.getStrPl());
				tramitesId.add(plSqlRes.getResIntPl());
				tramitesResultados.add(e);
			}
			for (PlSql plSqlRes : resCargaMasiva.getListaTramitesErroneos()) {
				TramiteRes e = new TramiteRes();
				e.setCodigoError(plSqlRes.getIntPl() + "");
				e.setMensajeError(plSqlRes.getStrPl());
				tramitesResultados.add(e);
			}
			resultado.setResumen(new Resumen());
			resultado.getResumen().setCorrectos(
					resCargaMasiva.getListaTramites().size() + "");
			resultado.getResumen().setErroneos(
					resCargaMasiva.getListaTramitesErroneos().size() + "");
			resultado.setTramites(new Tramites());
			resultado.getTramites().getTramite().addAll(tramitesResultados);

			byte[] bytes2 = masivaService.convertXMLObjetc(resultado);
			archivoRes.setAlgoritoHash(masivaService.getSha1FromFile(bytes2));
			archivoRes.setArchivo(bytes2);
			archivoRes
					.setDescripcion("Archivo nuevo de carga masiva de anotacion del usuario : "
							+ usuario.getNombre()
							+ ", con el id :"
							+ usuario.getPersona().getIdPersona()
							+ ", resultado de una carga que contenia archivos incorrectos");
			archivoRes.setIdUsuario(usuario.getPersona().getIdPersona());
			archivoRes.setNombreArchivo("cmResnuevo");
			archivoRes.setTipoArchivo("xml");
			resCargaMasiva.setArchivoTO(archivoRes);
			// terminamos de generar el XML de resultado

			// generamos el tramite de firma masiva en caso de aver correctos
			if (resCargaMasiva.getListaTramites().size() > 0) {

				Integer idUsuario = usuario.getPersona().getIdPersona();
				FirmaMasivaTO firmaMasivaTO = new FirmaMasivaTO();
				firmaMasivaTO.setIdUsuario(idUsuario);
				firmaMasivaTO.setIdArchivo(resCargaMasiva.getIdArchivo());
				firmaMasivaTO.setIdAcreedor(0);
				String tramites = masivaService.listToString(tramitesId);
				firmaMasivaTO.setTramites(tramites);
				FirmaMasivaDAO firmaDao = new FirmaMasivaDAO();
				int valor = firmaDao.crearFirmaMasiva(firmaMasivaTO);
				resCargaMasiva.setIdTramiteNuevo(valor);
			}
			// terminamos el tramite de firma masiva
		} catch (Exception e) {
			e.printStackTrace();
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

	// agregar otorgantes
	private PlSql agregaOtorgante(List<Otorgante> otorgantes, Tramite tramite) {
		PlSql regresa = new PlSql();
		for (Otorgante otorgante : otorgantes) {
			regresa = agregaOtorganteInd(otorgante, tramite);
			if (regresa != null) {
				break;
			}
		}

		return regresa;

	}

	private PlSql agregaOtorganteInd(Otorgante otorgante, Tramite tramite) {
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
					MyLogger.Logger.log(Level.INFO,"es una persona moral");
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
					if (plSql.getIntPl().intValue() != 0) {
						plSql.setResStrPl("Error en el Otorgante - No se pudo agregar al otorgante");
						plSql.setStrPl("Error en el Otorgante - No se pudo agregar al otorgante");
						plSql.setIntPl(964);
					} else {
						FolioElectronicoDAO folioElectronicoDAO = new FolioElectronicoDAO();
						String strMsj = folioElectronicoDAO
								.creaFolioElectronico(plSql.getResIntPl()
										.intValue());
						if (strMsj != null) {
							mx.gob.se.rug.masiva.resultado.to.Otorgante otorgante2 = new mx.gob.se.rug.masiva.resultado.to.Otorgante();
							otorgante2.setCurp(altaParteTO.getCurp());
							otorgante2.setFolioElectronico(strMsj);
							MyLogger.Logger.log(Level.INFO,"--se genero el siguiente folio electronico de persona fisica -- "
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

	// termina agregar otorgantes

	// alta de acreedores masivos
	private ResCargaMasiva cargaAcreedores(ResCargaMasiva resCargaMasiva,
			CargaMasiva cargaMasiva, UsuarioTO usuario) {
		PlSql plSql;
		resCargaMasiva.setListaTramites(new ArrayList<PlSql>());
		resCargaMasiva.setListaTramitesErroneos(new ArrayList<PlSql>());
		try {
			List<AcreedorAutoridad> acreedoresCorrectos = new ArrayList<AcreedorAutoridad>();
			// recorremos los elementos de acreedores
			for (AcreedorAutoridad acreedor : cargaMasiva.getAcreedores().getAcreedorAutoridad()) {

				plSql = validaDatosCargaAcreedores(acreedor);
				if (plSql.getIntPl() == 0) {
					plSql = validaAcreedor(acreedor, usuario);
					if (plSql.getIntPl() == 0) {
						plSql = altaAcreedor(usuario, acreedor);
						if (plSql.getIntPl() == 0) {
							MyLogger.Logger.log(Level.INFO,"correcto.plSql.id:"
									+ plSql.getIntPl());
							MyLogger.Logger.log(Level.INFO,"correcto.plSql.mensaje:"
									+ plSql.getStrPl());
							acreedoresCorrectos.add(acreedor);
							resCargaMasiva.getListaTramites().add(plSql);
						} else {
							MyLogger.Logger.log(Level.WARNING,"erroneo.plSql.id:"
									+ plSql.getIntPl());
							MyLogger.Logger.log(Level.WARNING,"erroneo.plSql.mensaje:"
									+ plSql.getStrPl());
							resCargaMasiva.getListaTramitesErroneos()
									.add(plSql);
						}
					} else {
						MyLogger.Logger.log(Level.WARNING,"erroneo.plSql.id:"
								+ plSql.getIntPl());
						MyLogger.Logger.log(Level.WARNING,"erroneo.plSql.mensaje:"
								+ plSql.getStrPl());
						resCargaMasiva.getListaTramitesErroneos().add(plSql);
					}
				} else {
					MyLogger.Logger.log(Level.WARNING,"erroneo.plSql.id:" + plSql.getIntPl());
					MyLogger.Logger.log(Level.WARNING,"erroneo.plSql.mensaje:"
							+ plSql.getStrPl());
					resCargaMasiva.getListaTramitesErroneos().add(plSql);
				}
			}
			// preguntamos si uvieron tramites correctos
			if (resCargaMasiva.getListaTramites().size() > 0) {
				// preguntamos si uvieron tramites erroneos
				if (resCargaMasiva.getListaTramitesErroneos().size() > 0) {
					// si existen tramites erroneos generamos el nuevo XML que
					// se va a firmar
					CargaMasiva cargaMasiva2 = new CargaMasiva();
					cargaMasiva2.getAcreedores().getAcreedorAutoridad()
							.addAll(acreedoresCorrectos);
					ArchivoTO archivoN = new ArchivoTO();
					archivoN.setAlgoritoHash(masivaService
							.getSha1FromFile(masivaService
									.convertXMLObjetc(cargaMasiva2)));
					archivoN.setArchivo(masivaService
							.convertXMLObjetc(cargaMasiva2));
					archivoN.setDescripcion("Archivo nuevo de carga masiva del usuario : "
							+ usuario.getNombre()
							+ ", con el id :"
							+ usuario.getPersona().getIdPersona()
							+ ", resultado de una carga que contenia archivos incorrectos");
					archivoN.setIdUsuario(usuario.getPersona().getIdPersona());
					archivoN.setNombreArchivo("cmResnuevo");
					archivoN.setTipoArchivo("xml");
					resCargaMasiva.setIdArchivo(inscripcionService
							.insertArchivo(archivoN).getResIntPl());
				}
			} 

			// generamos el XML de resultado
			ArchivoTO archivoRes = new ArchivoTO();
			ResultadoCargaMasiva resultado = new ResultadoCargaMasiva();
			List<TramiteRes> tramitesResultados = new ArrayList<TramiteRes>();
			List<Integer> tramitesId = new ArrayList<Integer>();
			for (PlSql plSqlRes : resCargaMasiva.getListaTramites()) {
				TramiteRes e = new TramiteRes();
				e.setCodigoError(plSqlRes.getIntPl() + "");
				e.setMensajeError(plSqlRes.getStrPl());
				tramitesId.add(plSqlRes.getResIntPl());
				tramitesResultados.add(e);
			}
			for (PlSql plSqlRes : resCargaMasiva.getListaTramitesErroneos()) {
				TramiteRes e = new TramiteRes();
				e.setCodigoError(plSqlRes.getIntPl() + "");
				e.setMensajeError(plSqlRes.getStrPl());
				tramitesResultados.add(e);
			}
			resultado.setResumen(new Resumen());
			resultado.getResumen().setCorrectos(
					resCargaMasiva.getListaTramites().size() + "");
			resultado.getResumen().setErroneos(
					resCargaMasiva.getListaTramitesErroneos().size() + "");
			resultado.setTramites(new Tramites());
			resultado.getTramites().getTramite().addAll(tramitesResultados);

			byte[] bytes2 = masivaService.convertXMLObjetc(resultado);
			archivoRes.setAlgoritoHash(masivaService.getSha1FromFile(bytes2));
			archivoRes.setArchivo(bytes2);
			archivoRes
					.setDescripcion("Archivo nuevo de carga masiva de modificacion del usuario : "
							+ usuario.getNombre()
							+ ", con el id :"
							+ usuario.getPersona().getIdPersona()
							+ ", resultado de una carga que contenia archivos incorrectos");
			archivoRes.setIdUsuario(usuario.getPersona().getIdPersona());
			archivoRes.setNombreArchivo("cmResnuevo");
			archivoRes.setTipoArchivo("xml");
			resCargaMasiva.setArchivoTO(archivoRes);
			// terminamos de generar el XML de resultado

			// generamos el tramite de firma masiva en caso de aver correctos
			if (resCargaMasiva.getListaTramites().size() > 0) {

				Integer idUsuario = usuario.getPersona().getIdPersona();
				FirmaMasivaTO firmaMasivaTO = new FirmaMasivaTO();
				firmaMasivaTO.setIdUsuario(idUsuario);
				firmaMasivaTO.setIdArchivo(resCargaMasiva.getIdArchivo());
				firmaMasivaTO.setIdAcreedor(0);
				String tramites = masivaService.listToString(tramitesId);
				firmaMasivaTO.setTramites(tramites);
				FirmaMasivaDAO firmaDao = new FirmaMasivaDAO();
				int valor = firmaDao.crearFirmaMasiva(firmaMasivaTO);
				MyLogger.Logger.log(Level.INFO,"valor :" + valor);
				resCargaMasiva.setIdTramiteNuevo(valor);
			}
			// terminamos el tramite de firma masiva
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			for (PlSql sql : resCargaMasiva.getListaTramitesErroneos()) {
				if (sql.getResIntPl() != null) {
					inscripcionService.bajaTramiteIncompleto(sql.getResIntPl());
				}
			}
		}
		return resCargaMasiva;
	}

	// damos de alta al acreedor
	private PlSql altaAcreedor(UsuarioTO usuario,
			AcreedorAutoridad acreedorAutoridad) {
		PlSql plSql = new PlSql();
		plSql.setIntPl(0);
		try {
			plSql = masivaDAO.executeAltaTramiteIncompleto(usuario.getPersona()
					.getIdPersona(), 12);
			if (plSql.getIntPl() == 0) {
				AcreedorTO acreedorTO = new AcreedorTO();
				// parseamos los datos
				acreedorTO.setIdTramitePendiente(plSql.getResIntPl());
				acreedorTO.setDomicilioUno(notNullString(acreedorAutoridad
						.getDomicilioExtranjeroUno()));
				acreedorTO.setDomicilioDos(notNullString(acreedorAutoridad
						.getDomicilioExtranjeroDos()));
				acreedorTO.setPoblacion(notNullString(acreedorAutoridad
						.getPoblacion()));
				acreedorTO.setZonaPostal(notNullString(acreedorAutoridad
						.getZonaPostal()));
				acreedorTO.setTipoPersona(notNullString(acreedorAutoridad
						.getTipoPersona()));
				acreedorTO.setRazonSocial(notNullString(acreedorAutoridad
						.getDenominacionRazonSocial()));
				acreedorTO.setRfc(notNullString(acreedorAutoridad.getRfc()));
				acreedorTO.setFolioMercantil("");
				acreedorTO
						.setCalle(notNullString(acreedorAutoridad.getCalle()));
				acreedorTO.setCalleNumero(notNullString(acreedorAutoridad
						.getNumeroExterior()));
				acreedorTO
						.setCalleNumeroInterior(notNullString(acreedorAutoridad
								.getNumeroInterior()));
				acreedorTO.setNombre(notNullString(acreedorAutoridad
						.getNombre()));
				acreedorTO.setCurp("");
				acreedorTO.setApellidoPaterno(notNullString(acreedorAutoridad
						.getApellidoPaterno()));
				acreedorTO.setApellidoMaterno(notNullString(acreedorAutoridad
						.getApellidoMaterno()));
				acreedorTO.setIdColonia(acreedorAutoridad.getIdColonia().intValue());
				acreedorTO.setIdLocalidad(acreedorAutoridad.getIdLocalidad().intValue());
				acreedorTO.setIdNacionalidad(acreedorAutoridad
						.getIdNacionalidad().intValue());
				acreedorTO.setTelefono(notNullBigtoString(acreedorAutoridad
						.getTelefono()));
				acreedorTO.setCorreoElectronico(notNullString(acreedorAutoridad
						.getCorreoElectronico()));
				acreedorTO.setExtencion(notNullBigtoString(acreedorAutoridad
						.getTelefonoExtension()));
				acreedorTO.setIdPaisResidencia(acreedorAutoridad
						.getIdPaisResidencia().intValue());
				plSql = acreedoresService.altaAcreedorRep(acreedorTO, usuario
						.getPersona().getIdPersona());
				if (plSql.getIntPl() == 0) {
					plSql.setStrPl("Alta del acreedor satisfactoriamente");
					plSql.setResIntPl(acreedorTO.getIdTramitePendiente());
				} else {
					plSql.setIntPl(906);
					plSql.setStrPl("Sucedio un error inesperado en el alta del acreedor");
				}
			}

		} catch (Exception e) {
			plSql.setIntPl(999);
			plSql.setStrPl("Sucedio un error inesperado en el alta del acreedor");
			e.printStackTrace();
		}
		return plSql;
	}

	// validamos que el acreedor no este dado de alta para el usuario
	private PlSql validaAcreedor(AcreedorAutoridad acreedor, UsuarioTO usuario) {
		PlSql plSql = new PlSql();
		plSql.setIntPl(0);
		try {
			List<AcreedorTO> listaAcreedores = acreedoresService
					.getAcreedoresByPersona(Integer.valueOf(usuario
							.getPersona().getIdPersona()));
			List<AcreedorTO> listaAcreedoresSinFirma = acreedoresService
					.getAcreedoresSinFirmaByPersona(Integer.valueOf(usuario
							.getPersona().getIdPersona()));
			List<AcreedorTO> listaTotal = new ArrayList<AcreedorTO>();
			listaTotal.addAll(listaAcreedores);
			listaTotal.addAll(listaAcreedoresSinFirma);
			Iterator<AcreedorTO> itAcreedor = listaTotal.iterator();
			AcreedorTO acre;
			boolean esta = false;
			while (itAcreedor.hasNext() && (!esta)) {
				acre = itAcreedor.next();
				if (acre.getRfc() != null
						&& acre.getRfc().equals(acreedor.getRfc().trim())) {
					esta = true;

				}
				if (!acreedor.getDenominacionRazonSocial().isEmpty()) {
					if (acreedor.getDenominacionRazonSocial().trim()
							.equals(acre.getRazonSocial())) {
						esta = true;
					}
				} else {
					if ((acre.getNombre() != null && acre.getNombre().equals(
							acreedor.getNombre().trim()))
							&& (acre.getApellidoPaterno() != null && acre
									.getApellidoPaterno().equals(
											acreedor.getApellidoPaterno()
													.trim()))
							&& (acre.getApellidoMaterno() != null && acre
									.getApellidoMaterno().equals(
											acreedor.getApellidoMaterno()
													.trim()))) {
						esta = true;
					}
					if ((acre.getNombre() != null && acre.getNombre().equals(
							acreedor.getNombre().trim()))
							&& (acre.getApellidoPaterno() != null && acre
									.getApellidoPaterno().equals(
											acreedor.getApellidoPaterno()
													.trim()))) {
						esta = true;
					}
				}

			}

			if (esta) {
				plSql.setIntPl(905);
				plSql.setStrPl("El RFC o el Nombre esta en uso");
			}

		} catch (Exception e) {
			plSql.setIntPl(999);
			plSql.setStrPl("Sucedio un error inesperado en el alta del acreedor");
			e.printStackTrace();
		}
		return plSql;
	}

	// validamos los datos del acreedor
	private PlSql validaDatosCargaAcreedores(AcreedorAutoridad acreedor) {
		PlSql plSql = new PlSql();
		plSql.setIntPl(0);
		try {
			if (acreedor.getIdPaisResidencia().intValue() == 1) {
				if (acreedor.getCalle() == null) {
					plSql.setIntPl(901);
					plSql.setStrPl("La calle es un atributo obligatorio");
				} else if (acreedor.getNumeroExterior() == null) {
					plSql.setIntPl(902);
					plSql.setStrPl("El numero exterior es un atributo obligatorio");
				} else if (acreedor.getIdColonia() == null
						|| acreedor.getIdLocalidad() == null) {
					plSql.setIntPl(903);
					plSql.setStrPl("La colonia ó Poblacion es un atributo obligatorio");
				}
			} else {
				if (acreedor.getDomicilioExtranjeroUno() == null) {
					plSql.setIntPl(904);
					plSql.setStrPl("La atributo domicilio extranjero uno es un atributo obligatorio");
				}
			}
		} catch (Exception e) {
			plSql.setIntPl(999);
			plSql.setStrPl("Sucedio un error inesperado en el alta del acreedor");
			e.printStackTrace();
		}
		return plSql;
	}

	// ayudas

	private String notNullString(String atributo) {
		return atributo == null ? "" : atributo;
	}
	private String notNullBigtoString(BigInteger atributo) {
		return atributo == null ? "" : atributo.toString();
	}
}
