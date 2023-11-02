package mx.gob.se.rug.firma.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.Date;
import java.text.SimpleDateFormat;
import gt.gob.rgm.service.JmsSolrMessageSenderService;
import gt.gob.rgm.service.RugSignatureImp;
import mx.gob.se.firma.co.to.ResponseWs;
import mx.gob.se.rug.acreedores.service.AcreedoresService;
import mx.gob.se.rug.anotacion.tramites.dao.TramitesDAO;
import mx.gob.se.rug.anotacion.tramites.to.AnotacionSnGarantiaTO;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.dao.UtilDAO;
import mx.gob.se.rug.exception.NoDataFoundException;
import mx.gob.se.rug.firma.dao.FirmaDAO;
import mx.gob.se.rug.firma.service.FirmaService;
import mx.gob.se.rug.firma.service.FooterAcuse;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.inscripcion.service.InscripcionService;
import mx.gob.se.rug.masiva.dao.MasivaDAO;
import mx.gob.se.rug.operaciones.dao.OperacionesDAO;
import mx.gob.se.rug.operaciones.to.OperacionesTO;
import mx.gob.se.rug.pago.dao.PagoDAO;
import mx.gob.se.rug.seguridad.dao.PrivilegiosDAO;
import mx.gob.se.rug.seguridad.service.PrivilegiosService;
import mx.gob.se.rug.seguridad.serviceimpl.PrivilegiosServiceImpl;
import mx.gob.se.rug.seguridad.to.PrivilegioTO;
import mx.gob.se.rug.seguridad.to.PrivilegiosTO;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.ExpresionesRegularesUtil;
import mx.gob.se.rug.util.MyLogger;

public class FirmaElectronicaAction extends RugBaseAction {

	private static final long serialVersionUID = 1L;
	
	private JmsSolrMessageSenderService solrMessageSender;
	
	private Integer idTramite;
	private Integer idTipoTramite;
	private String urlBack;
	private String rfc;
	private String txtFirma;
	private String messageKey;
	private String messageError;
	private String bvalida;
	private String idGarantia;
	private String idTramiteTemporal;
	private String errorFirma;

	private String urlServiceAdvantage;
	private String entidadServiceAdvantage;
	private String operacionServiceAdvantage;
	private String loginServiceAdvantage;
	private String passwordServiceAdvantage;

	private String cadenaOriginal;
	private String modoFirma;
	private String modoApplet = "2";
	private String serieNumero;
	
	//Nuevos valores
	private String debug;
	private String acsBase;

	private String base64UserSign;
	private String base64UserCertificate;

	private String esFactoraje;
	
	private InscripcionService inscripcionService;
	
        public RugSignatureImp signatureFile;
	public String confirmacion() {
		return SUCCESS;
	}

	public String firma() {

		
		MyLogger.Logger.log(Level.INFO, getEsFactoraje());
		
		String regresa = Constants.FAILED;
		FirmaDAO firmaDAO = new FirmaDAO();
		PagoDAO pagoDAO = new PagoDAO();
		FirmaService firmaService = new FirmaService();
		PrivilegiosService privilegiosService= new PrivilegiosServiceImpl();
		
		Integer idUsuario=null;
		
		try {
					
			UsuarioTO usuarioTO = (UsuarioTO) sessionMap.get(Constants.USUARIO);
			idUsuario = usuarioTO.getPersona().getIdPersona();
			acsBase = privilegiosService.getPrivilegios(idUsuario);

			if (acsBase != null) {
				ExpresionesRegularesUtil eru = new ExpresionesRegularesUtil();
				UtilDAO utilDAO = new UtilDAO();

				
				if (idTramite != null) {
					sessionMap.put(Constants.ID_TRAMITE_NUEVO, new Integer(idTramite));
				}else{
					setIdTramite((Integer)sessionMap.get(Constants.ID_TRAMITE_NUEVO));
				}
				
				MyLogger.ErrorFirma.log(Level.INFO,"Firmando idtramite= "+getIdTramite());
				boolean puedePasar = false;
				int idTipoTramite = utilDAO.tipoTramiteByIdTramite(idTramite);
				switch (idTipoTramite) {
				case 0:
					regresa = "errorIdTramite";
					break;
				case 12:
					puedePasar = true;
					break;
				default:
					puedePasar = true;
					
					break;
				}
				if (puedePasar) {
					if (firmaService.validaFecha(idTramite).booleanValue()) {
						
						if (firmaService.validateTramite(idTramite).booleanValue()) {
						//if(puedePasar) {
							setBvalida("valida");
							setIdTipoTramite(pagoDAO.getTipoTramiteByIdTramitePendiente(idTramite) + 200);
							String docXML = new String();
							PrivilegiosDAO privilegiosDAO = new PrivilegiosDAO();
							PrivilegiosTO privilegiosTO = new PrivilegiosTO();
							privilegiosTO.setIdRecurso(new Integer(3));
							privilegiosTO = privilegiosDAO.getPrivilegios(privilegiosTO,usuarioTO);
							Map<Integer, PrivilegioTO> priv = privilegiosTO.getMapPrivilegio();
							if (priv.get(new Integer(36)) == null){
								//setRfc(usuarioTO.getPersona().getRFC().toUpperCase());
							}
						
							this.cadenaOriginal = "ABCDEFGH";
							sessionMap.put("cadenaOriginal", this.cadenaOriginal);
							sessionMap.put("esFactoraje", getEsFactoraje());
							MyLogger.ErrorFirma.log(Level.INFO,"Success 1");
							
							regresa = Constants.SUCCESS;
						} else {
							regresa = Constants.FAILED;
							setMessageKey("El tramite que esta intentando firmar ya ha sido firmado");
							setMessageError("tramite firmado");
							
							MyLogger.ErrorFirma.log(Level.INFO, "ERROR::: firmaService.validaFecha(idTramite).booleanValue()= False idTramite::"+getIdTramite());
							
						}
					} else if(idTipoTramite == 12){
						this.cadenaOriginal = "ABCDEFGH";
						sessionMap.put("cadenaOriginal", this.cadenaOriginal);
						MyLogger.ErrorFirma.log(Level.INFO,"SUCCESS TRAMITE 12");
						
						regresa = Constants.SUCCESS;
					} else {
						OperacionesDAO dao = new OperacionesDAO();
						OperacionesTO operacionesTO = dao.getById(idTramite);
						setIdGarantia(operacionesTO.getIdGarantiaModificar());
						setIdTramiteTemporal(dao.getIdTramitebyIdGarantia(Integer.valueOf(operacionesTO.getIdGarantiaModificar())).toString());
						setBvalida("1");
						// setIdTramiteTemporal();
						regresa = "fechainv";
					}
				}
			} else {
				regresa = "rfc";
			}

			String url = new String("/firma/firmaApplet.do");
			setUrlBack(url);

			setTxtFirma("<b>Con su firma electrónica, usted, bajo protesta de decir verdad:</b>"
					+ "<br/> 1. Reconoce que ha leído y que acepta todos y cada uno de los <a onclick=\"showTerminos()\" style=\"cursor: pointer; text-decoration:underline\">Términos y Condiciones de Uso</a> y <a onclick=\"showPoliticas()\"  style=\"cursor: pointer; text-decoration:underline\">Políticas de Privacidad</a> de este sitio de Internet; "
					+ "<br/> 2. Reconoce la existencia y veracidad de la información ingresada; y"
					+ "<br/> 3. Manifiesta que conoce la responsabilidad civil, administrativa y penal en que podría incurrir en caso de falsedad.");
		} catch (Exception e) {

			e.printStackTrace();
		}
//		MyLogger.Logger.log(Level.WARNING, "Valor de error firma: " + getErrorFirma());
//		MyLogger.Logger.log(Level.INFO, "valor de regresa :" + regresa);
		return regresa;
	}

	public String firmaUsuario() {

		urlServiceAdvantage = Constants.getParamValue(Constants.URL_SERVICE_ADVANTAGE);
		entidadServiceAdvantage = Constants.getParamValue(Constants.ENTIDAD_SERVICE_ADVANTAGE);
		operacionServiceAdvantage = Constants.getParamValue(Constants.OPERACION_SERVICE_ADVANTAGE);
		loginServiceAdvantage = Constants.getParamValue(Constants.LOGIN_SERVICE_ADVANTAGE);
		passwordServiceAdvantage = Constants.getParamValue(Constants.PASSWORD_SERVICE_ADVANTAGE);
		debug = Constants.getParamValue(Constants.FIRMA_DEBUG);

		String regresa = Constants.FAILED;
		FirmaDAO firmaDAO = new FirmaDAO();
		PagoDAO pagoDAO = new PagoDAO();
		FirmaService firmaService = new FirmaService();
		PrivilegiosService privilegiosService= new PrivilegiosServiceImpl();
		Integer idUsuario;
		
		try {
			
			UsuarioTO usuarioTO = (UsuarioTO) sessionMap.get(Constants.USUARIO);
			idUsuario = usuarioTO.getPersona().getIdPersona();
			
			serieNumero=firmaDAO.getNumSeriebyIdPersona(idUsuario);
			acsBase = privilegiosService.getPrivilegios(idUsuario);
			usuarioTO.getPersona().setRFC(firmaDAO.getRFCbyIdPersona(idUsuario));
			
			PrivilegiosDAO privilegiosDAO = new PrivilegiosDAO();
			PrivilegiosTO privilegiosTO = new PrivilegiosTO();
			privilegiosTO.setIdRecurso(new Integer(3));
			privilegiosTO = privilegiosDAO.getPrivilegios(privilegiosTO,usuarioTO);
			Map<Integer, PrivilegioTO> priv = privilegiosTO.getMapPrivilegio();
			
			
			if (usuarioTO.getPersona().getRFC() != null|| priv.get(new Integer(36))!=null ) {
				MyLogger.Logger.log(Level.INFO, "::FirmaElectronicaAction.firmaUsuario()::El usuario tiene asigando RFC ");
				if (idTramite == null) {
					MyLogger.Logger.log(Level.INFO, "::FirmaElectronicaAction.firmaUsuario()::IdTramite que se va a firmar -"+(Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO));
				} else {
					sessionMap.put(Constants.ID_TRAMITE_NUEVO, new Integer(idTramite));
					MyLogger.Logger.log(Level.INFO, "::FirmaElectronicaAction.firmaUsuario()::IdTramite que se va a firmar -"+idTramite);
				}
				
				MyLogger.ErrorFirma.log(Level.INFO,"Firmando idtramite= "+(Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO));
				if (firmaService.validateTramite(
						(Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO))
						.booleanValue()) {
					setIdTipoTramite(pagoDAO
							.getTipoTramiteByIdTramitePendiente((Integer) sessionMap
									.get(Constants.ID_TRAMITE_NUEVO)) + 200);
					setRfc(usuarioTO.getPersona().getRFC().toUpperCase());

					// Cadena original
					FooterAcuse acuse = new FooterAcuse();
					this.cadenaOriginal = acuse
							.getAcuseBean()
							.getCO(Constants.getParamValue(Constants.URL_SERVICE_FIRMA),
									usuarioTO.getPersona().getIdPersona(),
									this.idTipoTramite, this.idTramite);
					
					String docXML= acuse.getAcuseBean().getDocXML(Constants.getParamValue(Constants.URL_SERVICE_FIRMA),
							usuarioTO.getPersona()
									.getIdPersona(),
							this.idTipoTramite, this.idTramite);
					
					sessionMap.put(Constants.FIRMA_XML, docXML);

					regresa = Constants.SUCCESS;

				} else {
					regresa = Constants.FAILED;
					setMessageKey("El tramite que esta intentando firmar ya ha sido firmado");
					setMessageError("tramite firmado");
					MyLogger.ErrorFirma.log(Level.INFO, "ERROR::: firmaService.validaFecha(idTramite).booleanValue()= False idTramite::"+getIdTramite());
				}

			} else {
				MyLogger.Logger.log(Level.INFO, "::FirmaElectronicaAction.firmaUsuario()::El usuario no tiene RFC ");
				regresa = "rfc";
			}

			setUrlBack("/controlusuario/iniciaAltaUsuario.do");
			setTxtFirma("<b>Con su firma electrónica, usted, bajo protesta de decir verdad:</b>"
					+ "<br/> 1. Reconoce que ha leído y que acepta todos y cada uno de los <a onclick=\"showTerminos()\" style=\"cursor: pointer; text-decoration:underline\">Términos y Condiciones de Uso</a> y <a onclick=\"showPoliticas()\"  style=\"cursor: pointer; text-decoration:underline\">Políticas de Privacidad</a> de este sitio de Internet; "
					+ "<br/> 2. Reconoce la existencia y veracidad de la información ingresada; y"
					+ "<br/> 3. Manifiesta que conoce la responsabilidad civil, administrativa y penal en que podría incurrir en caso de falsedad.");
		} catch (Exception e) {
			regresa = "rfc";
			e.printStackTrace();
		}
		MyLogger.Logger.log(Level.INFO, "::FirmaElectronicaAction.firmaUsuario():: Valor que regresa --"+regresa);
		return regresa;
	}

	public String firmaAcreedores() {

		//urlServiceAdvantage = Constants.getParamValue(Constants.URL_SERVICE_ADVANTAGE);
		//entidadServiceAdvantage = Constants.getParamValue(Constants.ENTIDAD_SERVICE_ADVANTAGE);
		//operacionServiceAdvantage = Constants.getParamValue(Constants.OPERACION_SERVICE_ADVANTAGE);
		//loginServiceAdvantage = Constants.getParamValue(Constants.LOGIN_SERVICE_ADVANTAGE);
		//passwordServiceAdvantage = Constants.getParamValue(Constants.PASSWORD_SERVICE_ADVANTAGE);
		//debug = Constants.getParamValue(Constants.FIRMA_DEBUG);
		
		String regresa = Constants.FAILED;
		String url = new String("/acreedor/inicia.do");
		PagoDAO pagoDAO = new PagoDAO();
		FirmaDAO firmaDAO = new FirmaDAO();
		FirmaService firmaService = new FirmaService();
		setUrlBack(url);
		PrivilegiosService privilegiosService= new PrivilegiosServiceImpl();
		Integer idUsuario;
		try {
			UsuarioTO usuarioTO = (UsuarioTO) sessionMap.get(Constants.USUARIO);
			idUsuario = usuarioTO.getPersona().getIdPersona();
			usuarioTO.getPersona().setRFC(firmaDAO.getRFCbyIdPersona(idUsuario));
			
			PrivilegiosDAO privilegiosDAO = new PrivilegiosDAO();
			PrivilegiosTO privilegiosTO = new PrivilegiosTO();
			privilegiosTO.setIdRecurso(new Integer(3));
			privilegiosTO = privilegiosDAO.getPrivilegios(privilegiosTO,usuarioTO);
			Map<Integer, PrivilegioTO> priv = privilegiosTO.getMapPrivilegio();
			
			//serieNumero=firmaDAO.getNumSeriebyIdPersona(idUsuario);
			acsBase = privilegiosService.getPrivilegios(idUsuario);
			
			if (usuarioTO.getPersona().getRFC() != null || priv.get(new Integer(36))!=null ) {
				if (idTramite == null) {
					MyLogger.Logger.log(Level.INFO, "ID del tramite nuevo: "+ ((Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO)).toString());
					setIdTramite((Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO));
					
				} else {
					sessionMap.put(Constants.ID_TRAMITE_NUEVO, new Integer(
							idTramite));
				}
				MyLogger.ErrorFirma.log(Level.INFO,"Firmando idtramite= "+(Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO));
				if (firmaService.validateTramite((Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO)).booleanValue()) {				
					boolean rfcNecesario = true;
					
					if (priv.get(new Integer(36)) != null){
						rfcNecesario = false;
					}
					if (rfcNecesario) {
						
						if (priv.get(new Integer (51)) != null ){//Firmar con RFC de USUARIO
							setRfc(usuarioTO.getPersona().getRFC().toUpperCase());
						}else{
							String rfcAcreedor = firmaDAO.getRFCbyIdTramite((Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO));
							setRfc(rfcAcreedor.toUpperCase());
						}
					}
	
					setTxtFirma("<b>El titular de este certificado digital, bajo protesta de decir verdad:</b>"
						+ "<br/> 1. Reconoce que ha leido y que acepta todos y cada uno de los <a onclick=\"showTerminos()\" style=\"cursor: pointer; text-decoration:underline\">Términos y Condiciones de Uso</a> y <a onclick=\"showPoliticas()\"  style=\"cursor: pointer; text-decoration:underline\">Políticas de Privacidad </a> de este sitio de Internet;"
						+ "<br/> 2. Reconoce la existencia y veracidad de la información ingresada;"
						+ "<br/> 3. Manifiesta que conoce la responsabilidad civil, administrativa y penal en que podría incurrir en caso de falsedad;"
						+ "<br/> 4. Autoriza a la persona Usuario de la presente sesión para que en su nombre y por su cuenta realice avisos preventivos, inscripciones, modificaciones, transmisiones, rectificaciones por error, renovaciones de vigencia y cancelaciones a través del sistema;"
						+ "<br/> 5. Autoriza a la persona Usuario de la presente sesión para que en su nombre y por su cuenta autorice a terceros para realizar todas o solo algunas de las operaciones referidas en el numeral 4. anterior; y"
							+ "<br/> 6. Reconoce como suyas desde este momento las operaciones que las personas referidas en los numerales 4. Y 5. anteriores realicen en el futuro.");
					setIdTipoTramite(pagoDAO
							.getTipoTramiteByIdTramitePendiente((Integer) sessionMap
									.get(Constants.ID_TRAMITE_NUEVO)) + 200);
	
					// Cadena original
					/*FooterAcuse acuse = new FooterAcuse();
					this.cadenaOriginal = acuse.getAcuseBean().getCO(Constants.getParamValue(Constants.URL_SERVICE_FIRMA),
							usuarioTO.getPersona().getIdPersona(),
							this.idTipoTramite, ((Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO)));
					
					String docXML= acuse.getAcuseBean().getDocXML(Constants
							.getParamValue(Constants.URL_SERVICE_FIRMA),
							usuarioTO.getPersona()
									.getIdPersona(),
							this.idTipoTramite, ((Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO)));
					
					sessionMap.put(Constants.FIRMA_XML, docXML);*/
					this.cadenaOriginal = "ABCDEFGH";					
					MyLogger.ErrorFirma.log(Level.INFO,"cadena original= "+this.cadenaOriginal);
					
					regresa = Constants.SUCCESS;
				} else {
					regresa = Constants.FAILED;
					setMessageKey("El tramite que esta intentando firmar ya ha sido firmado");
					setMessageError("tramite firmado");
					MyLogger.ErrorFirma.log(Level.INFO, "ERROR::: firmaService.validaFecha(idTramite).booleanValue()= False idTramite::"+getIdTramite());
				}
			}else{
				MyLogger.Logger.log(Level.INFO, "::FirmaElectronicaAction.firmaUsuario()::El usuario no tiene RFC ");
				regresa = "rfc";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return regresa;
	}

	public String guardaRfc() {
		String regresa = Constants.FAILED;
		FirmaDAO firmaDAO = new FirmaDAO();
		UsuarioTO usuarioTO = (UsuarioTO) sessionMap.get(Constants.USUARIO);

		try {
			MyLogger.Logger.log(Level.INFO, "usuarios"
					+ usuarioTO.getPersona().getIdPersona() + "" + getRfc());
			firmaDAO.setRFCbyIdPersona(usuarioTO.getPersona().getIdPersona(),
					getRfc());
		} catch (NoDataFoundException e) {
			e.printStackTrace();
		}
		regresa = Constants.SUCCESS;
		return regresa;

	}

	public String firmaApplet() {
		String regresa = Constants.FAILED;
		idTramite = null;
		try {
			Integer idTramiteNuevo = ((Integer) sessionMap
					.get(Constants.ID_TRAMITE_NUEVO));
			MyLogger.Logger.log(Level.INFO, "ID del tramite firmar: " + idTramiteNuevo);
			regresa = Constants.SUCCESS;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return regresa;
	}

	public String validarFirma() {
		String regresa = Constants.FAILED;
		UsuarioTO usuarioTO = null;
		setUrlBack(null);
		idTramite = null;
		try {
			Integer idTramiteNuevo = ((Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO));
			Integer idGarantiaArancel = ((Integer) sessionMap.get(Constants.ID_GARANTIA_ARANCEL));
			String esFactoraje = ((String) sessionMap.get("esFactoraje"));
			MyLogger.Logger.log(Level.INFO, "ID del tramite firmar: " + idTramiteNuevo);
			MyLogger.Logger.log(Level.INFO, "Tipo factoraje " + esFactoraje);
			MyLogger.Logger.log(Level.INFO, "Tipo Leasing  " + idGarantiaArancel);
                        Date date = new Date();
			usuarioTO = (UsuarioTO) sessionMap.get(Constants.USUARIO);
			 Date date1 = new Date();
			// carga masiva
			if(getIdTipoTramite().intValue()-200==18) {
                             Date date2 = new Date();
				MasivaDAO masivaDAO = new MasivaDAO();
                                 Date date3 = new Date();
				PagoDAO pagoDAO = new PagoDAO();
                                 Date date4 = new Date();
				List<Integer> tramites = new ArrayList<Integer>();
                                
				tramites = masivaDAO.getIdTramitesMasivos(new Integer(idTramiteNuevo));
				 Date date5 = new Date();
				if(inscripcionService.getSaldoMasivoByUsuario(new Integer(usuarioTO.getPersona().getIdPersona()).toString(),getIdTipoTramite().intValue()-200,new Integer(idTramiteNuevo),tramites.size())) {
                                     Date date6 = new Date();
					for(Iterator<Integer> it = tramites.iterator(); it.hasNext();) {
						pagoDAO.firmaTramite(it.next(),esFactoraje);
                                                 Date date7 = new Date();
					}
                                        
					//firmo el masivo
					pagoDAO.firmaTramite(new Integer(idTramiteNuevo),esFactoraje);
                                         Date date8 = new Date();
                        //signatureFile = new RugSignatureImp();
                        //signatureFile.signatureFiles((idTramiteNuevo-1), new Integer(idTramiteNuevo), new Integer(usuarioTO.getPersona().getIdPersona()), 218);
                        
                                    
					regresa = Constants.SUCCESS;
				} else {
					regresa = "nosaldo";
				}
			} else {
				MyLogger.Logger.log(Level.INFO, "ID Tipo tramite: " + (getIdTipoTramite().intValue()-200));
				if(inscripcionService.getSaldoByUsuario(new Integer(usuarioTO.getPersona().getIdPersona()).toString(),getIdTipoTramite().intValue()-200,new Integer(new Integer(idTramiteNuevo)), idGarantiaArancel != null ? idGarantiaArancel : 0 )) {
					PagoDAO pagoDAO = new PagoDAO();
					if(inscripcionService.tienePartes(new Integer(idTramiteNuevo))) {						
						if(pagoDAO.firmaTramite(new Integer(idTramiteNuevo),esFactoraje)) {
							// enviar mensaje para indexar tramite nuevo
							solrMessageSender.sendMessage("INDEXAR:" + idTramiteNuevo);
							regresa = Constants.SUCCESS;
						} else {
							this.setMessageKey("A ocurrido un error, no se pudo ejecutar la operacion. Favor intente nuevamete.");
						}
					} else {
						this.setMessageKey("La operacion que desea realizar no es valida, favor intente nuevamente.");
					}
				} else {
					regresa = "nosaldo";
				}
				
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return regresa;
	}

	public String firmaGuarda(){
		String regresa = Constants.FAILED;
		UsuarioTO usuarioTO = null;
		String docXML = null;
		try{
			regresa = validarFirma();
		}catch(Exception e){
			
			MyLogger.ErrorFirma.log(Level.INFO, "::: Inicia Error Guarda  Firma:::");
			MyLogger.ErrorFirma.log(Level.INFO,":::INFO:::: getCadenaOriginal()="+getCadenaOriginal()+", base64UserCertificate=" +base64UserCertificate+", base64UserSign" +base64UserSign+",docXML" +docXML+", getIdTramite()" +getIdTramite()+"," );		
			try{
			MyLogger.ErrorFirma.log(Level.INFO," usuarioTO.getPersona().getIdPersona()" +usuarioTO.getPersona().getIdPersona());
			}catch (Exception en) {
				MyLogger.ErrorFirma.log(Level.INFO," usuarioTO.getPersona().getIdPersona()= NULL" );
			}
			MyLogger.ErrorFirma.log(Level.INFO,"Exception::::::",e);
			MyLogger.ErrorFirma.log(Level.INFO, "::: FIN Error Guarda  Firma:::");
			
			
			regresa=Constants.FAILED;
			setMessageKey("El tramite que esta intentando firmar ya ha sido firmado");
			setMessageError("tramite firmado");
		}
		
		//redireccion
		if (regresa.equalsIgnoreCase(Constants.SUCCESS)){

			switch(getIdTipoTramite().intValue()-200){
			case 18:
				regresa = "masiva";
				break;
			case 12:
				regresa = "acreedor";
				break;
			case 19:
				regresa = "acreedor";
				break;
			case 14:
				regresa = "usuario";
				break;
			default: break;
			}
		}
		
		return regresa;
	}

	public Integer getIdTramite() {
		return idTramite;
	}

	public void setIdTramite(Integer idTramite) {
		this.idTramite = idTramite;
	}

	public Integer getIdTipoTramite() {
		return idTipoTramite;
	}

	public void setIdTipoTramite(Integer idTipoTramite) {
		this.idTipoTramite = idTipoTramite;
	}

	public String getUrlBack() {
		return urlBack;
	}

	public void setUrlBack(String urlBack) {
		this.urlBack = urlBack;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getTxtFirma() {
		return txtFirma;
	}

	public void setTxtFirma(String txtFirma) {
		this.txtFirma = txtFirma;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public String getMessageError() {
		return messageError;
	}

	public void setMessageError(String messageError) {
		this.messageError = messageError;
	}

	public void setBvalida(String bvalida) {
		this.bvalida = bvalida;
	}

	public String getBvalida() {
		return bvalida;
	}

	public void setIdGarantia(String idGarantia) {
		this.idGarantia = idGarantia;
	}

	public String getIdGarantia() {
		return idGarantia;
	}

	public void setIdTramiteTemporal(String idTramiteTemporal) {
		this.idTramiteTemporal = idTramiteTemporal;
	}

	public String getIdTramiteTemporal() {
		return idTramiteTemporal;
	}

	public void setErrorFirma(String errorFirma) {
		this.errorFirma = errorFirma;
	}

	public String getErrorFirma() {
		return errorFirma;
	}

	public String getUrlServiceAdvantage() {
		return urlServiceAdvantage;
	}

	public void setUrlServiceAdvantage(String urlServiceAdvantage) {
		this.urlServiceAdvantage = urlServiceAdvantage;
	}

	public String getEntidadServiceAdvantage() {
		return entidadServiceAdvantage;
	}

	public void setEntidadServiceAdvantage(String entidadServiceAdvantage) {
		this.entidadServiceAdvantage = entidadServiceAdvantage;
	}

	public String getOperacionServiceAdvantage() {
		return operacionServiceAdvantage;
	}

	public void setOperacionServiceAdvantage(String operacionServiceAdvantage) {
		this.operacionServiceAdvantage = operacionServiceAdvantage;
	}

	public String getLoginServiceAdvantage() {
		return loginServiceAdvantage;
	}

	public void setLoginServiceAdvantage(String loginServiceAdvantage) {
		this.loginServiceAdvantage = loginServiceAdvantage;
	}

	public String getPasswordServiceAdvantage() {
		return passwordServiceAdvantage;
	}

	public void setPasswordServiceAdvantage(String passwordServiceAdvantage) {
		this.passwordServiceAdvantage = passwordServiceAdvantage;
	}

	public String getCadenaOriginal() {
		return this.cadenaOriginal;
	}

	public void setCadenaOriginal(String cadenaOriginal) {
		this.cadenaOriginal = cadenaOriginal;
	}

	public String getModoFirma() {
		this.modoFirma=new String("1");
		return this.modoFirma;
	}

	public void setModoFirma(String modoFirma) {
		this.modoFirma = modoFirma;
	}

	public String getBase64UserSign() {
		return base64UserSign;
	}

	public void setBase64UserSign(String base64UserSign) {
		this.base64UserSign = base64UserSign;
	}

	public String getBase64UserCertificate() {
		return base64UserCertificate;
	}

	public void setBase64UserCertificate(String base64UserCertificate) {
		this.base64UserCertificate = base64UserCertificate;
	}

	public String getDebug() {
		return debug;
	}

	public void setDebug(String debug) {
		this.debug = debug;
	}
	
	public String getAcsBase() {
		return acsBase;
	}

	public void setAcsBase(String acsBase) {
		this.acsBase = acsBase;
	}

	public String getSerieNumero() {
		return serieNumero;
	}

	public void setSerieNumero(String serieNumero) {
		this.serieNumero = serieNumero;
	}

	public String getModoApplet() {
		return modoApplet;
	}

	public void setModoApplet(String modoApplet) {
		this.modoApplet = modoApplet;
	}
	
	public void setInscripcionService(InscripcionService inscripcionService) {
		this.inscripcionService = inscripcionService;
	}
        public void setRugSignatureImp(RugSignatureImp signatureFile) {
		this.signatureFile = signatureFile;
	}
	public JmsSolrMessageSenderService getSolrMessageSender() {
		return solrMessageSender;
	}

	public void setSolrMessageSender(JmsSolrMessageSenderService solrMessageSender) {
		this.solrMessageSender = solrMessageSender;
	}	

	 /**
     * @return String return the esFactoraje
     */
    public String getEsFactoraje() {
        return esFactoraje;
    }

    /**
     * @param esFactoraje the esFactoraje to set
     */
    public void setEsFactoraje(String esFactoraje) {
        this.esFactoraje = esFactoraje;
    }
}
