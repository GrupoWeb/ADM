package mx.gob.se.rug.boleta.action;

import java.io.FileNotFoundException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import mx.gob.economia.dgi.framework.security.user.dto.User;
import mx.gob.se.rug.acceso.dao.AccesoDAO;
import mx.gob.se.rug.anotacion.tramites.dao.TramitesDAO;
import mx.gob.se.rug.boleta.dao.BoletaDAO;
import mx.gob.se.rug.boleta.serviceImpl.BoletaServiceImpl;
import mx.gob.se.rug.boleta.to.DetalleTO;
import mx.gob.se.rug.boleta.to.FirmaMasiva;
import mx.gob.se.rug.boletaservice.BoletaServices;
import mx.gob.se.rug.common.exception.UserException;
import mx.gob.se.rug.common.service.HomeService;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.detallegarantia.to.PartesTO;
import mx.gob.se.rug.dto.PersonaFisica;
import mx.gob.se.rug.exception.PendienteException;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.inscripcion.dao.FirmaMasivaDAO;
import mx.gob.se.rug.inscripcion.service.InscripcionService;
import mx.gob.se.rug.inscripcion.service.impl.InscripcionServiceImpl;
import mx.gob.se.rug.masiva.dao.MasivaDAO;
import mx.gob.se.rug.masiva.to.ArchivoTO;
import mx.gob.se.rug.to.PersonaTO;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;
import mx.gob.se.rug.util.pdf.to.HistoriaTO;
import mx.gob.se.rug.util.pdf.to.PdfTO;

public class BoletaAction extends RugBaseAction {

    private static final long serialVersionUID = 1L;
    private static final String SUCCESS = "sucess";

    private PdfTO pdfTO;
    private HistoriaTO historiaTO;
    private String idTramite;
    private String idGarantia;
    private String idTipoTramite;
    private List<PartesTO> OtorganteTOs;
    private List<PartesTO> AcreedorTOs;
    private List<PartesTO> DeudorTOs;
    private String messageKey;
    private String messageError;
    private Integer idArchivoRes;
    private Integer idTipoTramiteMasiva;
    private String typeData;

    private ArrayList<String> htmlLista = new ArrayList<String>();
    private HomeService homeService;

    public String getTypeData() {
        return typeData;
    }

    public void setTypeData(String typeData) {
        this.typeData = typeData;
    }

    public Integer getIdTipoTramiteMasiva() {
        return idTipoTramiteMasiva;
    }

    public void setIdTipoTramiteMasiva(Integer idTipoTramiteMasiva) {
        this.idTipoTramiteMasiva = idTipoTramiteMasiva;
    }

    public void setHomeService(HomeService homeService) {
        this.homeService = homeService;
    }

    public HomeService getHomeService() {
        return homeService;
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

    public String getIdTipoTramite() {
        return idTipoTramite;
    }

    public void setIdTipoTramite(String idTipoTramite) {
        this.idTipoTramite = idTipoTramite;
    }

    public String getIdGarantia() {
        return idGarantia;
    }

    public void setIdGarantia(String idGarantia) {
        this.idGarantia = idGarantia;
    }

    public PdfTO getPdfTO() {
        return pdfTO;
    }

    public void setPdfTO(PdfTO pdfTO) {
        this.pdfTO = pdfTO;
    }

    public String getBoleta2() {
        MyLogger.Logger.log(Level.INFO, "Entre a boletas 2");
        return Constants.SUCCESS;
    }

    public String signatureFiles(){
        UsuarioTO usuarioTO = (UsuarioTO) sessionMap.get(Constants.USUARIO);
        String regresa = "";
        Integer idTramiteVar = null;
        Integer idTipoTramiteVar = null;

        if(usuarioTO == null){
            try {
                UsuarioTO userLoggin = new UsuarioTO();
                userLoggin.setNombre(principal.getUserPrincipal().toString());

                PersonaTO persona = new PersonaTO();
                AccesoDAO udao = new AccesoDAO();
                User user = homeService.getUser(principal.getUserPrincipal().toString());

                logger.debug(sessionMap);
                if (principal.getUserPrincipal().toString().equals(userLoggin.getNombre())) {
                    PersonaFisica personaFisica = udao.getIdPersona(userLoggin.getNombre());

                    persona.setIdPersona(personaFisica.getIdPersona());
                    persona.setPerfil(personaFisica.getPerfil());

                    userLoggin.setPersona(persona);
                    sessionMap.put(mx.gob.se.rug.constants.Constants.USUARIO, userLoggin);
                    
                    user.getProfile().setEmail(userLoggin.getNombre());
                    sessionMap.put(mx.gob.se.rug.common.constants.Constants.SESSION_USER, user);
                    usuarioTO = (UsuarioTO) sessionMap.get(Constants.USUARIO);

                }
            } catch (UserException e) {
                e.printStackTrace();
            }
        }
        
        sessionMap.remove("pdfTO");

        try {
            BoletaDAO boletaDAO = new BoletaDAO();

            if (getIdTramite() != null) {
                idTramiteVar = new Integer(getIdTramite());
                idTipoTramiteVar = new Integer(getIdTipoTramite());
            } else {
                idTramiteVar = new Integer((Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO));
                idTipoTramiteVar = boletaDAO.getTipoTramitebyIdTramiteTemporal(idTramiteVar);
            }

            BoletaServiceImpl boletaServiceImpl = new BoletaServiceImpl();

            pdfTO = new PdfTO();
            pdfTO.setMassive("False");
            pdfTO.setSave("0");

            if (idTipoTramiteVar != null) {
                if (idTipoTramiteVar.intValue() == 15 || idTipoTramiteVar.intValue() == 16) {
                    idTipoTramiteVar = 1;
                }
                PdfTO pdfTOInfo = null;
                BoletaServices boletaServices = new BoletaServices();
                    System.out.println("dato de tramites " + idTipoTramiteVar);
                switch (idTipoTramiteVar) {
                    case 1:// Inscripcion
                        mx.gob.se.rug.boleta.to.DetalleTO detalleTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                        detalleTO.setIdTramite(boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar));
                        detalleTO.setIdTipoTramite(idTipoTramiteVar);
                        pdfTO = boletaServiceImpl.getBoletaInscripcion(detalleTO);
                        pdfTO.setIdTramite(detalleTO.getIdTramite());
                        pdfTO.setHtml("[*cert*]", "");
                        pdfTO.setHtml("[*fechaCert*]", "");
                        break;
                    case 31:// Migracion Inscripcion
                        mx.gob.se.rug.boleta.to.DetalleTO detalleMiTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                        detalleMiTO.setIdTramite(boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar));
                        detalleMiTO.setIdTipoTramite(idTipoTramiteVar);
                        pdfTO = boletaServiceImpl.getBoletaInscripcion(detalleMiTO);
                        pdfTO.setIdTramite(detalleMiTO.getIdTramite());
                        pdfTO.setValue("[*operacion*]", "Inscripci\u00f3n Traslado");
                        pdfTO.setTypeValue("Inscripci\u00f3n Traslado");
                        pdfTO.setHtml("[*fechaCert*]", "");
                        break;
                    case 2:// Anotacioncongarantiaanota
                        mx.gob.se.rug.boleta.to.DetalleTO detalleAnotaTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                        detalleAnotaTO.setIdTramite(boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar));
                        detalleAnotaTO.setIdTipoTramite(idTipoTramiteVar);
                        pdfTO = boletaServiceImpl.getBoletaAnotacion(detalleAnotaTO);
                        pdfTO.setIdTramite(detalleAnotaTO.getIdTramite());
                        pdfTO.setValue("[*tipoTramite*]", "Anotaci�n");
                        pdfTO.setHtml("[*fechaCert*]", "");
                        break;
                    case 3:// Aviso preventivo
                        Integer idTramiteAviso = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar);
                        pdfTOInfo = boletaServiceImpl.getAvisoPreventivo(idTramiteAviso);
                        pdfTO.setHtml(boletaServices.getBoletaHtml());
                        pdfTO.setHtml("[*boletaHtml*]", pdfTOInfo.getHtml());
                        pdfTO.setIdTramite(idTramiteAviso);
                        pdfTO.setValue("[*tipoTramite*]", "Aviso Preventivo");
                        pdfTO.setHtml("[*fechaCert*]", "");
                        break;
                    case 4:// Cancelacion
                        mx.gob.se.rug.boleta.to.DetalleTO detallecancelaTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                        detallecancelaTO.setIdTramite(boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar));
                        detallecancelaTO.setIdTipoTramite(idTipoTramiteVar);
                        mx.gob.se.rug.boleta.to.DetalleTO detallecancelaTOF = boletaDAO.getDetalleGarantiaTramite(detallecancelaTO);
                        System.out.println("ID Garantia No. 4 " + detallecancelaTOF.getIdGarantia());
                        pdfTO = boletaServiceImpl.getBoletaCancelacion(detallecancelaTOF);
                        pdfTO.setIdTramite(detallecancelaTO.getIdTramite());
                        pdfTO.setValue("[*operacion*]", "Cancelaci\u00f3n");
                        pdfTO.setTypeValue("Cancelaci\u00f3n");
                        pdfTO.setHtml("[*fechaCert*]", "");
                        break;
                    case 35:// Certificacion Nuevo Arancel
                        Integer idTramiteNuevo = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar);
                        DetalleTO detalleCertNuevo = boletaDAO.getCertificacion(idTramiteNuevo);
                        pdfTO.setIdGarantiaTO(Integer.valueOf(detalleCertNuevo.getIdGarantia()));
                        byte myFileNuevo[] = null;
                        if (myFileNuevo == null) {
                            StringBuffer sbCert = new StringBuffer();
                            LocalDateTime now = LocalDateTime.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                            sbCert.append("<div class=\"input-field col s6\">");
                            sbCert.append("<span class=\"blue-text text-darken-2\">Fecha Certificaci&oacute;n: </span>");
                            sbCert.append("<span>" + now.format(formatter) + "</span></div>");
                            sbCert.append("<div class=\"input-field col s6\"></div>");
                            if (detalleCertNuevo.getIdTipoTramite() == 1) {
                                pdfTO = boletaServiceImpl.getBoletaInscripcion(detalleCertNuevo);
                                pdfTO.setHtml("[*cert*]", "Certificaci\u00f3n de ");
                                pdfTO.setHtml("[*fechaCert*]", sbCert.toString());
                            } else if (detalleCertNuevo.getIdTipoTramite() == 31) {
                                pdfTO = boletaServiceImpl.getBoletaInscripcion(detalleCertNuevo);
                                pdfTO.setValue("[*operacion*]", "Certificaci\u00f3n de Inscripci\u00f3n");
                                pdfTO.setTypeValue("Certificaci\u00f3n de Inscripci\u00f3n");
                                pdfTO.setHtml("[*fechaCert*]", sbCert.toString());
                            } else if (detalleCertNuevo.getIdTipoTramite() == 4) {
                                pdfTO = boletaServiceImpl.getBoletaCancelacion(detalleCertNuevo);
                                pdfTO.setValue("[*operacion*]", "Certificaci\u00f3n de Cancelaci\u00f3n");
                                pdfTO.setTypeValue("Certificaci\u00f3n de Cancelaci\u00f3n");
                                pdfTO.setHtml("[*fechaCert*]", sbCert.toString());
                            } else if (detalleCertNuevo.getIdTipoTramite() == 7 || detalleCertNuevo.getIdTipoTramite() == 17 || detalleCertNuevo.getIdTipoTramite() == 13) {
                                pdfTO = boletaServiceImpl.getBoletaModificacion(detalleCertNuevo);
                                pdfTO.setHtml("[*fechaCert*]", sbCert.toString());
                                if (detalleCertNuevo.getIdTipoTramite() == 7) {
                                    pdfTO.setValue("[*title*]", "MODIFICACI\u00d3N DE LA");
                                    pdfTO.setValue("[*operacion*]", "Certificaci\u00f3n de Modificaci\u00f3n");
                                    pdfTO.setTypeValue("Certificaci\u00f3n de Modificaci\u00f3n");
                                } else if (detalleCertNuevo.getIdTipoTramite() == 17) {
                                    pdfTO.setValue("[*title*]", "ANOTACI\u00d3N DE LEVANTADO DE EMBARGO DE LA");
                                    pdfTO.setValue("[*operacion*]",
                                            "Certificaci\u00f3n de Anotaci\u00f3n de Levantado de Embargo");
                                    pdfTO.setTypeValue("Certificaci\u00f3n de Anotaci\u00f3n de Levantado de Embargo");
                                } else {
                                    pdfTO.setValue("[*title*]", "ANOTACI\u00d3N DE EMBARGO DE LA");
                                    pdfTO.setValue("[*operacion*]", "Certificaci\u00f3n de Anotaci\u00f3n de Embargo");
                                    pdfTO.setTypeValue("Certificaci\u00f3n de Anotaci\u00f3n de Embargo");
                                }
                            } else if (detalleCertNuevo.getIdTipoTramite() == 9) {
                                pdfTO = boletaServiceImpl.getBoletaRenovacion(detalleCertNuevo);
                                pdfTO.setValue("[*operacion*]", "Certificaci\u00f3n de Pr\u00f3rroga");
                                pdfTO.setTypeValue("Certificaci\u00f3n de Pr\u00f3rroga");
                                pdfTO.setHtml("[*fechaCert*]", sbCert.toString());
                            } else if (detalleCertNuevo.getIdTipoTramite() == 30) {
                                pdfTO = boletaServiceImpl.getBoletaEjecucion(detalleCertNuevo);
                                pdfTO.setValue("[*operacion*]", "Certificaci\u00f3n de Ejecuci\u00f3n");
                                pdfTO.setTypeValue("Certificaci\u00f3n de Ejecuci\u00f3n");
                                pdfTO.setHtml("[*fechaCert*]", sbCert.toString());
                            }
                            pdfTO.setSave("1");
                        }
                        pdfTO.setIdTramite(idTramiteNuevo);
                        break;
                    case 5:// Certificacion
                        Integer idTramiteF = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar);
                        DetalleTO detalleCert = boletaDAO.getCertificacion(idTramiteF);
                        pdfTO.setIdGarantiaTO(Integer.valueOf(detalleCert.getIdGarantia()));
                        byte myFile[] = null;
                        if (myFile == null) {
                            StringBuffer sbCert = new StringBuffer();
                            LocalDateTime now = LocalDateTime.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                            sbCert.append("<div class=\"input-field col s6\">");
                            sbCert.append("<span class=\"blue-text text-darken-2\">Fecha Certificaci&oacute;n: </span>");
                            sbCert.append("<span>" + now.format(formatter) + "</span></div>");
                            sbCert.append("<div class=\"input-field col s6\"></div>");
                            if (detalleCert.getIdTipoTramite() == 1) {
                                pdfTO = boletaServiceImpl.getBoletaInscripcion(detalleCert);
                                pdfTO.setHtml("[*cert*]", "Certificaci\u00f3n de ");
                                pdfTO.setHtml("[*fechaCert*]", sbCert.toString());
                            } else if (detalleCert.getIdTipoTramite() == 31) {
                                pdfTO = boletaServiceImpl.getBoletaInscripcion(detalleCert);
                                pdfTO.setValue("[*operacion*]", "Certificaci\u00f3n de Inscripci\u00f3n");
                                pdfTO.setTypeValue("Certificaci\u00f3n de Inscripci\u00f3n");
                                pdfTO.setHtml("[*fechaCert*]", sbCert.toString());
                            } else if (detalleCert.getIdTipoTramite() == 4) {
                                pdfTO = boletaServiceImpl.getBoletaCancelacion(detalleCert);
                                pdfTO.setValue("[*operacion*]", "Certificaci\u00f3n de Cancelaci\u00f3n");
                                pdfTO.setTypeValue("Certificaci\u00f3n de Cancelaci\u00f3n");
                                pdfTO.setHtml("[*fechaCert*]", sbCert.toString());
                            } else if (detalleCert.getIdTipoTramite() == 7 || detalleCert.getIdTipoTramite() == 17 || detalleCert.getIdTipoTramite() == 13) {
                                pdfTO = boletaServiceImpl.getBoletaModificacion(detalleCert);
                                pdfTO.setHtml("[*fechaCert*]", sbCert.toString());
                                if (detalleCert.getIdTipoTramite() == 7) {
                                    pdfTO.setValue("[*title*]", "MODIFICACI\u00d3N DE LA");
                                    pdfTO.setValue("[*operacion*]", "Certificaci\u00f3n de Modificaci\u00f3n");
                                    pdfTO.setTypeValue("Certificaci\u00f3n de Modificaci\u00f3n");
                                } else if (detalleCert.getIdTipoTramite() == 17) {
                                    pdfTO.setValue("[*title*]", "ANOTACI\u00d3N DE LEVANTADO DE EMBARGO DE LA");
                                    pdfTO.setValue("[*operacion*]",
                                            "Certificaci\u00f3n de Anotaci\u00f3n de Levantado de Embargo");
                                    pdfTO.setTypeValue("Certificaci\u00f3n de Anotaci\u00f3n de Levantado de Embargo");
                                } else {
                                    pdfTO.setValue("[*title*]", "ANOTACI\u00d3N DE EMBARGO DE LA");
                                    pdfTO.setValue("[*operacion*]", "Certificaci\u00f3n de Anotaci\u00f3n de Embargo");
                                    pdfTO.setTypeValue("Certificaci\u00f3n de Anotaci\u00f3n de Embargo");
                                }
                            } else if (detalleCert.getIdTipoTramite() == 9) {
                                pdfTO = boletaServiceImpl.getBoletaRenovacion(detalleCert);
                                pdfTO.setValue("[*operacion*]", "Certificaci\u00f3n de Pr\u00f3rroga");
                                pdfTO.setTypeValue("Certificaci\u00f3n de Pr\u00f3rroga");
                                pdfTO.setHtml("[*fechaCert*]", sbCert.toString());
                            } else if (detalleCert.getIdTipoTramite() == 30) {
                                pdfTO = boletaServiceImpl.getBoletaEjecucion(detalleCert);
                                pdfTO.setValue("[*operacion*]", "Certificaci\u00f3n de Ejecuci\u00f3n");
                                pdfTO.setTypeValue("Certificaci\u00f3n de Ejecuci\u00f3n");
                                pdfTO.setHtml("[*fechaCert*]", sbCert.toString());
                            }
                            pdfTO.setSave("1");
                        }
                        pdfTO.setIdTramite(idTramiteF);
                        break;
                    case 6:// Rectificacion por error
                        mx.gob.se.rug.boleta.to.DetalleTO detalleRectificaTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                        detalleRectificaTO.setIdTramite(boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar));
                        detalleRectificaTO.setIdTipoTramite(idTipoTramiteVar);
                        pdfTO = boletaServiceImpl.getBoletaRectificacion(detalleRectificaTO);
                        pdfTO.setIdTramite(detalleRectificaTO.getIdTramite());
                        pdfTO.setValue("[*tipoTramite*]", "Rectificaci�n por Error");
                        break;
                    case 7:// Modification
                        mx.gob.se.rug.boleta.to.DetalleTO detalleModificaTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                        detalleModificaTO.setIdTramite(boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar));
                        detalleModificaTO.setIdTipoTramite(idTipoTramiteVar);
                        pdfTO = boletaServiceImpl.getBoletaModificacion(detalleModificaTO);
                        pdfTO.setIdTramite(detalleModificaTO.getIdTramite());
                        pdfTO.setValue("[*operacion*]", "Modificaci\u00f3n");
                        pdfTO.setTypeValue("Modificaci\u00f3n");
                        pdfTO.setValue("[*title*]", "MODIFICACI\u00d3N DE LA");
                        pdfTO.setHtml("[*fechaCert*]", "");

                        break;
                    case 13:// Embargo
                        mx.gob.se.rug.boleta.to.DetalleTO detalleEmbargoTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                        detalleEmbargoTO.setIdTramite(boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar));
                        detalleEmbargoTO.setIdTipoTramite(idTipoTramiteVar);
                        pdfTO = boletaServiceImpl.getBoletaModificacion(detalleEmbargoTO);
                        pdfTO.setIdTramite(detalleEmbargoTO.getIdTramite());
                        pdfTO.setValue("[*operacion*]", "Anotaci\u00f3n de Embargo");
                        pdfTO.setTypeValue("Anotaci\u00f3n de Embargo");
                        pdfTO.setValue("[*title*]", "ANOTACI\u00d3N DE EMBARGO DE LA");
                        pdfTO.setHtml("[*fechaCert*]", "");
                        break;
                    case 17:
                        mx.gob.se.rug.boleta.to.DetalleTO detalleDEmbargoTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                        detalleDEmbargoTO.setIdTramite(boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar));
                        detalleDEmbargoTO.setIdTipoTramite(idTipoTramiteVar);
                        pdfTO = boletaServiceImpl.getBoletaModificacion(detalleDEmbargoTO);
                        pdfTO.setIdTramite(detalleDEmbargoTO.getIdTramite());
                        pdfTO.setValue("[*operacion*]", "Anotaci\u00f3n de Levantado de Embargo");
                        pdfTO.setTypeValue("Anotaci\u00f3n de Levantado de Embargo");
                        pdfTO.setValue("[*title*]", "ANOTACI\u00d3N DE LEVANTADO DE EMBARGO DE LA");
                        pdfTO.setHtml("[*fechaCert*]", "");
                        break;
                    case 8:// Transmision
                        mx.gob.se.rug.boleta.to.DetalleTO detalleTransmisionTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                        detalleTransmisionTO.setIdTramite(boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar));
                        detalleTransmisionTO.setIdTipoTramite(idTipoTramiteVar);
                        pdfTO = boletaServiceImpl.getBoletaTransmision(detalleTransmisionTO);
                        pdfTO.setIdTramite(detalleTransmisionTO.getIdTramite());
                        pdfTO.setValue("[*tipoTramite*]", "Transmisi�n");
                        break;
                    case 9:// Renovacion o reduccion de vigencia
                        mx.gob.se.rug.boleta.to.DetalleTO detalleRenovacionTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                        detalleRenovacionTO.setIdTramite(boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar));
                        detalleRenovacionTO.setIdTipoTramite(idTipoTramiteVar);
                        pdfTO = boletaServiceImpl.getBoletaRenovacion(detalleRenovacionTO);
                        pdfTO.setIdTramite(detalleRenovacionTO.getIdTramite());
                        pdfTO.setValue("[*operacion*]","Pr\u00f3rroga de vigencia");
                        pdfTO.setTypeValue("Pr\u00f3rroga de vigencia");
                        pdfTO.setHtml("[*fechaCert*]", "");
                        break;
                    case 30:// Ejecucion
                        mx.gob.se.rug.boleta.to.DetalleTO detalleEjecucionTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                        detalleEjecucionTO.setIdTramite(boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar));
                        detalleEjecucionTO.setIdTipoTramite(idTipoTramiteVar);
                        pdfTO = boletaServiceImpl.getBoletaEjecucion(detalleEjecucionTO);
                        pdfTO.setIdTramite(detalleEjecucionTO.getIdTramite());
                        pdfTO.setValue("[*operacion*]","Ejecuci\u00f3n");
                        pdfTO.setTypeValue("Ejecuci\u00f3n");
                        pdfTO.setHtml("[*fechaCert*]", "");
                        break;
                    case 38: // Transmision despues de prorroga
                        mx.gob.se.rug.boleta.to.DetalleTO detalleTransmisionTOs = new mx.gob.se.rug.boleta.to.DetalleTO();
                        detalleTransmisionTOs.setIdTramite(boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar));
                        detalleTransmisionTOs.setIdTipoTramite(idTipoTramiteVar);
                        pdfTO = boletaServiceImpl.getBoletaTransmision(detalleTransmisionTOs);
                        pdfTO.setIdTramite(detalleTransmisionTOs.getIdTramite());
                        pdfTO.setValue("[*tipoTramite*]", "Transmisi�n");
                        break;
                    case 10:// Anotacion sin garantia
                        Integer idTramiteFinal = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar);
                        pdfTOInfo = boletaServiceImpl.getAnotacionSNGarantia(idTramiteFinal);
                        pdfTO.setHtml(boletaServices.getBoletaHtml());
                        pdfTO.setHtml("[*boletaHtml*]", pdfTOInfo.getHtml());
                        pdfTO.setIdTramite(idTramiteFinal);
                        pdfTO.setValue("[*tipoTramite*]", "Anotaci�n");
                        break;
                    case 28:// Anotacion sin garantia Modificacion
                        Integer idTramiteFinal1 = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar);
                        pdfTOInfo = boletaServiceImpl.getAnotacionSNGarantiaModificacion(idTramiteFinal1);
                        pdfTO.setHtml(boletaServices.getBoletaHtml());
                        pdfTO.setHtml("[*boletaHtml*]", pdfTOInfo.getHtml());
                        pdfTO.setIdTramite(idTramiteFinal1);
                        pdfTO.setValue("[*tipoTramite*]", "Anotaci�n Modificaci�n");
                        break;
                    case 27:// Anotacion sin garantia CAncelacion
                        Integer idTramiteFinal2 = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar);
                        pdfTOInfo = boletaServiceImpl.getAnotacionSNGarantiaCancelacion(idTramiteFinal2);
                        pdfTO.setHtml(boletaServices.getBoletaHtml());
                        pdfTO.setHtml("[*boletaHtml*]", pdfTOInfo.getHtml());
                        pdfTO.setIdTramite(idTramiteFinal2);
                        pdfTO.setValue("[*tipoTramite*]", "Anotaci�n Cancelaci�n");
                        break;
                    case 29:// Anotacion sin garantia
                        Integer idTramiteFinal3 = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar);
                        pdfTOInfo = boletaServiceImpl.getAnotacionSNGarantiaRectificacion(idTramiteFinal3);
                        pdfTO.setHtml(boletaServices.getBoletaHtml());
                        pdfTO.setHtml("[*boletaHtml*]", pdfTOInfo.getHtml());
                        pdfTO.setIdTramite(idTramiteFinal3);
                        pdfTO.setValue("[*tipoTramite*]", "Anotaci�n Rectificaci�n");
                        break;
                    case 26:// Anotacion sin garantia Renovacion
                        Integer idTramiteFinal4 = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar);
                        pdfTOInfo = boletaServiceImpl.getAnotacionSNGarantiaRenovacion(idTramiteFinal4);
                        pdfTO.setHtml(boletaServices.getBoletaHtml());
                        pdfTO.setHtml("[*boletaHtml*]", pdfTOInfo.getHtml());
                        pdfTO.setIdTramite(idTramiteFinal4);
                        pdfTO.setValue("[*tipoTramite*]", "Anotaci�n Renovaci�n o Reducci�n de Vigencia");

                        break;
                    case 18:// Firma masiva
                        InscripcionService inscripcionService = new InscripcionServiceImpl();
                        int idEstatus = new FirmaMasivaDAO().getEstatusByTramiteTemporal(idTramiteVar);
                        MasivaDAO masivaDAO = new MasivaDAO();
                        setIdTipoTramiteMasiva(masivaDAO.getIdTipoTramiteMasiva(idTramiteVar));
                        
                        switch (idEstatus) {
                        case 3:
                            Integer idTramiteFirma = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar);
                            /**
                             * lista de tramites *
                             */
                            List<Integer> tramitesMasivos = new ArrayList<Integer>();
                            tramitesMasivos = masivaDAO.getIdTramitesMasivos(idTramiteVar);

                            int iteracion = 0;
                            for (Iterator<Integer> it = tramitesMasivos.iterator(); it.hasNext();) {
                                Integer tramMas = it.next();
                                mx.gob.se.rug.boleta.to.DetalleTO detalleMasTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                                PdfTO pdfMas = new PdfTO();
                                detalleMasTO.setIdTramite(boletaDAO.getIdTramitebyIdTramiteNuevo(tramMas));
                                detalleMasTO.setIdTipoTramite(boletaDAO.getTipoTramitebyIdTramiteTemporal(tramMas));
                                iteracion++;
                                if (detalleMasTO.getIdTipoTramite() == 1 || detalleMasTO.getIdTipoTramite() == 31) {
                                    pdfMas = boletaServiceImpl.getBoletaInscripcion(detalleMasTO);
                                    pdfMas.setValue("[*operacion*]", "Inscripci\u00f3n");
                                    pdfMas.setTypeValue("Inscripci\u00f3n");
                                    pdfMas.setHtml("[*fechaCert*]", "");
                                } else if (detalleMasTO.getIdTipoTramite() == 7) {
                                    pdfMas = boletaServiceImpl.getBoletaModificacion(detalleMasTO);
                                    pdfMas.setValue("[*operacion*]", "Modificaci\u00f3n");
                                    pdfMas.setTypeValue("Modificaci\u00f3n");
                                    pdfMas.setHtml("[*fechaCert*]", "");
                                } else if (detalleMasTO.getIdTipoTramite() == 9) {
                                    pdfMas = boletaServiceImpl.getBoletaRenovacion(detalleMasTO);
                                    pdfMas.setValue("[*operacion*]","Pr\u00f3rroga de vigencia");
                                    pdfMas.setTypeValue("Pr\u00f3rroga de vigencia");
                                    pdfMas.setHtml("[*fechaCert*]", "");
                                } else if (detalleMasTO.getIdTipoTramite() == 4) {
                                    pdfMas = boletaServiceImpl.getBoletaCancelacion(detalleMasTO);
                                    pdfMas.setValue("[*operacion*]", "Cancelaci\u00f3n");
                                    pdfMas.setTypeValue("Cancelaci\u00f3n");
                                    pdfMas.setHtml("[*fechaCert*]", "");
                                } else if (detalleMasTO.getIdTipoTramite() == 30) {
                                    pdfMas = boletaServiceImpl.getBoletaEjecucion(detalleMasTO);
                                    pdfMas.setValue("[*operacion*]","Ejecuci\u00f3n");
                                    pdfMas.setTypeValue("Ejecuci\u00f3n");
                                    pdfMas.setHtml("[*fechaCert*]", "");
                                }
                                StringBuilder stringBuilder = new StringBuilder();
                                this.setTypeData(pdfMas.getTypeValue());
                                if (pdfTO.getHtml() != null) {
                                    stringBuilder.append(pdfTO.getHtml());
                                    stringBuilder.append("<br /><h1 style=\"page-break-before: always\">&nbsp;</h1><br />");
                                }
                                stringBuilder.append(pdfMas.getHtml());
                                htmlLista.add(pdfMas.getHtml());
                                pdfTO.setHtml(stringBuilder.toString());
                                pdfTO.setHtmlList(htmlLista);
                                pdfTO.setMassive("True");
                            }
                            pdfTO.setTypeValue(this.getTypeData());
                            pdfTO.setIdTramite(idTramiteFirma);
                            FirmaMasiva firmaMasiva = new FirmaMasiva();
                            byte[] bytes = convertXMLObjetc(firmaMasiva);
                            ArchivoTO archivoN = new ArchivoTO();
                            archivoN.setAlgoritoHash("N/A");
                            archivoN.setArchivo(bytes);
                            archivoN.setDescripcion("Archivo de resultado de firma masiva del tramite :" + idTramiteVar);
                            archivoN.setIdUsuario(usuarioTO.getPersona().getIdPersona());
                            archivoN.setNombreArchivo("cmResnuevo");
                            archivoN.setTipoArchivo("xml");
                            setIdArchivoRes(inscripcionService.insertArchivo(archivoN).getResIntPl());
                            regresa = "firmaMasiva";
                            break;
                        case 5:
                            regresa = "errpendiente";
                            setMessageKey("La boleta se esta generando se notificara a su correo con un mail cuando este lista");
                            setMessageError("No existe la boleta");
                            throw new PendienteException();
                        
                        case 8:
                        regresa = "errpendiente";
                        setMessageKey(
                                "La boleta se esta generando se notificara a su correo con un mail cuando este lista");
                        setMessageError("No existe la boleta");
                        throw new PendienteException();
                    default:
                        MyLogger.Logger.log(Level.INFO, "El tramite no tiene estatus");
                        regresa = "errtramite";
                        setMessageKey("El tramite no tiene estatus favor de verificar e intentarlo nuevamente");
                        setMessageError("No existe el tramite");
                        throw new PendienteException();
                    }
                    break;
                }

                if (pdfTO.getIdTramite() != null) {
                    pdfTO.setValue("[*idTramite*]", pdfTO.getIdTramite().toString());
                    pdfTO.setValue("[*GMTExplica*]", "");

                }
                sessionMap.put("pdfTO", pdfTO);
                if (!regresa.equals("firmaMasiva")) {
                    regresa = SUCCESS;
                }
                // System.out.println("PDF " +  pdfTO.getHtml());
            } else {
                regresa = "failure";
                setMessageKey("No se pudo generar la boleta");
                setMessageError("No existe el tramite");
            }
        } catch (PendienteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            setMessageKey("No se pudo generar la boleta");
            setMessageError(e.getMessage());
            regresa = "failure";
            e.printStackTrace();
        } finally {
            setIdTramite(null);
        }

        return regresa;

    }

    public String getBoleta() {

        UsuarioTO usuarioTO = (UsuarioTO) sessionMap.get(Constants.USUARIO);

        if (usuarioTO == null) {
            try {

                UsuarioTO u = new UsuarioTO();
                PersonaTO persona = new PersonaTO();
                AccesoDAO udao = new AccesoDAO();

                User user = homeService.getUser(principal.getUserPrincipal().toString());
                u.setNombre(principal.getUserPrincipal().toString());

                logger.debug(sessionMap);

                if (principal.getUserPrincipal().toString().equals(u.getNombre())) {
                    PersonaFisica personaFisica = udao.getIdPersona(u.getNombre());

                    persona.setIdPersona(personaFisica.getIdPersona());
                    persona.setPerfil(personaFisica.getPerfil());

                    u.setPersona(persona);
                    sessionMap.put(mx.gob.se.rug.constants.Constants.USUARIO, u);


                    user.getProfile().setEmail(u.getNombre());
                    sessionMap.put(mx.gob.se.rug.common.constants.Constants.SESSION_USER, user);
                    usuarioTO = (UsuarioTO) sessionMap.get(Constants.USUARIO);

                }

            } catch (UserException e) {
                MyLogger.Logger.log(Level.SEVERE, "Error al tratar de iniciar la session:", e);
            }
        }

        // String regresa = "failure";
        String regresa = "errpendiente";

        // MyLogger.Logger.log(Level.INFO, "--getBoleta--");
        // MyLogger.Logger.log(Level.INFO, "--IdTramite--" + idTramite);
        // MyLogger.Logger.log(Level.INFO, "--IdTipoTramite--" + idTipoTramite);

        Integer idTramiteVar = null;
        Integer idTipoTramiteVar = null;
        sessionMap.remove("pdfTO");
        try {
            BoletaDAO boletaDAO = new BoletaDAO();

            if (getIdTramite() != null) {
                idTramiteVar = new Integer(getIdTramite());
                idTipoTramiteVar = new Integer(getIdTipoTramite());
            } else {
                idTramiteVar = new Integer(
                        (Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO));
                idTipoTramiteVar = boletaDAO
                        .getTipoTramitebyIdTramiteTemporal(idTramiteVar);
            }

            // MyLogger.Logger.log(Level.INFO, "idTramiteVar:::" + idTramiteVar);
            // MyLogger.Logger.log(Level.INFO, "Este es el tipo de tramite
            // idTipoTramiteVar:::" + idTipoTramiteVar);

            BoletaServiceImpl boletaServiceImpl = new BoletaServiceImpl();
            pdfTO = new PdfTO();
            pdfTO.setMassive("False");
            pdfTO.setSave("0");

            if (idTipoTramiteVar != null) {
                if (idTipoTramiteVar.intValue() == 15
                        || idTipoTramiteVar.intValue() == 16) {
                    idTipoTramiteVar = 1;
                }
                /*
                 * if (idTipoTramiteVar.intValue() == 13
                 * || idTipoTramiteVar.intValue() == 17) {
                 * idTipoTramiteVar = 4;
                 * }
                 */


                PdfTO pdfTOInfo = null;
                BoletaServices boletaServices = new BoletaServices();
                switch (idTipoTramiteVar) {
                    case 1:// Inscripcion
                        mx.gob.se.rug.boleta.to.DetalleTO detalleTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                        detalleTO.setIdTramite(boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar));
                        detalleTO.setIdTipoTramite(idTipoTramiteVar);
                        pdfTO = boletaServiceImpl.getBoletaInscripcion(detalleTO);
                        pdfTO.setIdTramite(detalleTO.getIdTramite());
                        // pdfTO.setValue("[*operacion*]", "Inscripci\u00f3n" + anexo);
                        pdfTO.setHtml("[*cert*]", "");
                        pdfTO.setHtml("[*fechaCert*]", "");

                        break;
                    case 31:// Migracion Inscripcion

                        mx.gob.se.rug.boleta.to.DetalleTO detalleMiTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                        detalleMiTO.setIdTramite(boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar));
                        detalleMiTO.setIdTipoTramite(idTipoTramiteVar);
                        pdfTO = boletaServiceImpl.getBoletaInscripcion(detalleMiTO);
                        pdfTO.setIdTramite(detalleMiTO.getIdTramite());
                        pdfTO.setValue("[*operacion*]", "Inscripci\u00f3n Traslado");
                        pdfTO.setTypeValue("Inscripci\u00f3n Traslado");
                        pdfTO.setHtml("[*fechaCert*]", "");

                        break;
                    case 2:// Anotacioncongarantiaanota
                        MyLogger.Logger.log(Level.INFO, "-Anotacion con garantia action-");
                        mx.gob.se.rug.boleta.to.DetalleTO detalleAnotaTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                        detalleAnotaTO.setIdTramite(boletaDAO
                                .getIdTramitebyIdTramiteNuevo(idTramiteVar));
                        detalleAnotaTO.setIdTipoTramite(idTipoTramiteVar);
                        pdfTO = boletaServiceImpl
                                .getBoletaAnotacion(detalleAnotaTO);
                        pdfTO.setIdTramite(detalleAnotaTO.getIdTramite());
                        pdfTO.setValue("[*tipoTramite*]", "Anotaci�n");
                        pdfTO.setHtml("[*fechaCert*]", "");

                        break;
                    case 3:// Aviso preventivo
                        MyLogger.Logger.log(Level.INFO, "-----------Aviso Preventivo Boleta-------");
                        Integer idTramiteAviso = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar);
                        pdfTOInfo = boletaServiceImpl.getAvisoPreventivo(idTramiteAviso);
                        pdfTO.setHtml(boletaServices.getBoletaHtml());
                        pdfTO.setHtml("[*boletaHtml*]", pdfTOInfo.getHtml());
                        pdfTO.setIdTramite(idTramiteAviso);
                        pdfTO.setValue("[*tipoTramite*]", "Aviso Preventivo");
                        pdfTO.setHtml("[*fechaCert*]", "");

                        break;
                    case 4:// Cancelacion
                        mx.gob.se.rug.boleta.to.DetalleTO detallecancelaTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                        detallecancelaTO.setIdTramite(boletaDAO
                                .getIdTramitebyIdTramiteNuevo(idTramiteVar));
                        detallecancelaTO.setIdTipoTramite(idTipoTramiteVar);
                        mx.gob.se.rug.boleta.to.DetalleTO detallecancelaTOF = boletaDAO
                                .getDetalleGarantiaTramite(detallecancelaTO);
                        pdfTO = boletaServiceImpl
                                .getBoletaCancelacion(detallecancelaTOF);
                        pdfTO.setIdTramite(detallecancelaTO.getIdTramite());
                        pdfTO.setValue("[*operacion*]", "Cancelaci\u00f3n");
                        pdfTO.setTypeValue("Cancelaci\u00f3n");
                        pdfTO.setHtml("[*fechaCert*]", "");

                        break;
                    case 35:// Certificacion Nuevo Arancel
                        Integer idTramiteNuevo = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar);
                        DetalleTO detalleCertNuevo = boletaDAO.getCertificacion(idTramiteNuevo);
                        pdfTO.setIdGarantiaTO(Integer.valueOf(detalleCertNuevo.getIdGarantia()));
                        byte myFileNuevo[] = null;
                        if (myFileNuevo == null) {
                            StringBuffer sbCert = new StringBuffer();
                            LocalDateTime now = LocalDateTime.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                            sbCert.append("<div class=\"input-field col s6\">");
                            sbCert.append("<span class=\"blue-text text-darken-2\">Fecha Certificaci&oacute;n: </span>");
                            sbCert.append("<span>" + now.format(formatter) + "</span></div>");
                            sbCert.append("<div class=\"input-field col s6\"></div>");
                            if (detalleCertNuevo.getIdTipoTramite() == 1) {
                                pdfTO = boletaServiceImpl.getBoletaInscripcion(detalleCertNuevo);
                                pdfTO.setHtml("[*cert*]", "Certificaci\u00f3n de ");
                                pdfTO.setHtml("[*fechaCert*]", sbCert.toString());
                            } else if (detalleCertNuevo.getIdTipoTramite() == 31) {
                                pdfTO = boletaServiceImpl.getBoletaInscripcion(detalleCertNuevo);
                                pdfTO.setValue("[*operacion*]", "Certificaci\u00f3n de Inscripci\u00f3n");
                                pdfTO.setTypeValue("Certificaci\u00f3n de Inscripci\u00f3n");
                                pdfTO.setHtml("[*fechaCert*]", sbCert.toString());
                            } else if (detalleCertNuevo.getIdTipoTramite() == 4) {
                                pdfTO = boletaServiceImpl.getBoletaCancelacion(detalleCertNuevo);
                                pdfTO.setValue("[*operacion*]", "Certificaci\u00f3n de Cancelaci\u00f3n");
                                pdfTO.setTypeValue("Certificaci\u00f3n de Cancelaci\u00f3n");
                                pdfTO.setHtml("[*fechaCert*]", sbCert.toString());
                            } else if (detalleCertNuevo.getIdTipoTramite() == 7 || detalleCertNuevo.getIdTipoTramite() == 17 || detalleCertNuevo.getIdTipoTramite() == 13) {
                                pdfTO = boletaServiceImpl.getBoletaModificacion(detalleCertNuevo);
                                pdfTO.setHtml("[*fechaCert*]", sbCert.toString());
                                if (detalleCertNuevo.getIdTipoTramite() == 7) {
                                    pdfTO.setValue("[*title*]", "MODIFICACI\u00d3N DE LA");
                                    pdfTO.setValue("[*operacion*]", "Certificaci\u00f3n de Modificaci\u00f3n");
                                    pdfTO.setTypeValue("Certificaci\u00f3n de Modificaci\u00f3n");
                                } else if (detalleCertNuevo.getIdTipoTramite() == 17) {
                                    pdfTO.setValue("[*title*]", "ANOTACI\u00d3N DE LEVANTADO DE EMBARGO DE LA");
                                    pdfTO.setValue("[*operacion*]",
                                            "Certificaci\u00f3n de Anotaci\u00f3n de Levantado de Embargo");
                                    pdfTO.setTypeValue("Certificaci\u00f3n de Anotaci\u00f3n de Levantado de Embargo");
                                } else {
                                    pdfTO.setValue("[*title*]", "ANOTACI\u00d3N DE EMBARGO DE LA");
                                    pdfTO.setValue("[*operacion*]", "Certificaci\u00f3n de Anotaci\u00f3n de Embargo");
                                    pdfTO.setTypeValue("Certificaci\u00f3n de Anotaci\u00f3n de Embargo");
                                }
                            } else if (detalleCertNuevo.getIdTipoTramite() == 9) {
                                pdfTO = boletaServiceImpl.getBoletaRenovacion(detalleCertNuevo);
                                pdfTO.setValue("[*operacion*]", "Certificaci\u00f3n de Pr\u00f3rroga");
                                pdfTO.setTypeValue("Certificaci\u00f3n de Pr\u00f3rroga");
                                pdfTO.setHtml("[*fechaCert*]", sbCert.toString());
                            } else if (detalleCertNuevo.getIdTipoTramite() == 30) {
                                pdfTO = boletaServiceImpl.getBoletaEjecucion(detalleCertNuevo);
                                pdfTO.setValue("[*operacion*]", "Certificaci\u00f3n de Ejecuci\u00f3n");
                                pdfTO.setTypeValue("Certificaci\u00f3n de Ejecuci\u00f3n");
                                pdfTO.setHtml("[*fechaCert*]", sbCert.toString());
                            }
                            pdfTO.setSave("1");
                        }
                        pdfTO.setIdTramite(idTramiteNuevo);
                        break;
                    case 5:// Certificacion
                        // MyLogger.Logger.log(Level.INFO, "BoletaAction::> case 5 -- Certificacion :
                        // valoir del idTramiteVar::::" + idTramiteVar);
                        Integer idTramiteF = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar);
                        DetalleTO detalleCert = boletaDAO.getCertificacion(idTramiteF);

                        pdfTO.setIdGarantiaTO(Integer.valueOf(detalleCert.getIdGarantia()));
                        // byte myFile[] = boletaDAO.getPdfBoleta(detalleCert.getIdTramite());
                        byte myFile[] = null;

                        /*
                         * if (detalleCert.getIdTipoTramite() == 3) {// AvisoPreventivo
                         * pdfTOInfo = boletaServiceImpl
                         * .getAvisoPreventivo(detalleCert.getIdTramite());
                         * html = pdfTOInfo.getHtml();
                         * html = "<b>Aviso Preventivo:</b><BR/>" + html;
                         * tramites = detalleCert.getIdTramite().toString();
                         *
                         * } else if (detalleCert.getIdTipoTramite().intValue()==27 ||
                         * detalleCert.getIdTipoTramite().intValue()==28 ||
                         * detalleCert.getIdTipoTramite().intValue()==29 ||
                         * detalleCert.getIdTipoTramite().intValue()==26||
                         * detalleCert.getIdTipoTramite().intValue()==10 ){// Anotacion sn Garantia
                         *
                         * TramitesDAO anotacionDao= new TramitesDAO();
                         * tramites =
                         * anotacionDao.getListTramitesHByIdTramitePadre(detalleCert.getIdGarantia());
                         * pdfTOInfo =
                         * boletaServiceImpl.getCertificacionDetalleAnotacion(detalleCert.getIdGarantia(
                         * ));
                         * html = pdfTOInfo.getHtml();
                         * } else {// tramite con garantia
                         * pdfTOInfo =
                         * boletaServiceImpl.getCertificacionDetalle(detalleCert.getIdGarantia(),
                         * detalleCert.getIdTramite());
                         * tramites =
                         * boletaServiceImpl.getTramitesByGarantia(Integer.valueOf(detalleCert.
                         * getIdGarantia()));
                         * html = pdfTOInfo.getHtml();
                         * }
                         */
                        if (myFile == null) {

                            StringBuffer sbCert = new StringBuffer();
                            LocalDateTime now = LocalDateTime.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

                            sbCert.append("<div class=\"input-field col s6\">");
                            sbCert.append(
                                    "<span class=\"blue-text text-darken-2\">Fecha Certificaci&oacute;n: </span>");
                            sbCert.append("<span>" + now.format(formatter) + "</span></div>");
                            sbCert.append("<div class=\"input-field col s6\"></div>");

                            if (detalleCert.getIdTipoTramite() == 1) {
                                pdfTO = boletaServiceImpl.getBoletaInscripcion(detalleCert);
                                pdfTO.setHtml("[*cert*]", "Certificaci\u00f3n de ");
                                pdfTO.setHtml("[*fechaCert*]", sbCert.toString());
                            } else if (detalleCert.getIdTipoTramite() == 31) {
                                pdfTO = boletaServiceImpl.getBoletaInscripcion(detalleCert);
                                pdfTO.setValue("[*operacion*]", "Certificaci\u00f3n de Inscripci\u00f3n");
                                pdfTO.setTypeValue("Certificaci\u00f3n de Inscripci\u00f3n");
                                pdfTO.setHtml("[*fechaCert*]", sbCert.toString());
                            } else if (detalleCert.getIdTipoTramite() == 4) {
                                pdfTO = boletaServiceImpl.getBoletaCancelacion(detalleCert);
                                pdfTO.setValue("[*operacion*]", "Certificaci\u00f3n de Cancelaci\u00f3n");
                                pdfTO.setTypeValue("Certificaci\u00f3n de Cancelaci\u00f3n");
                                pdfTO.setHtml("[*fechaCert*]", sbCert.toString());
                            } else if (detalleCert.getIdTipoTramite() == 7 || detalleCert.getIdTipoTramite() == 17
                                    || detalleCert.getIdTipoTramite() == 13) {
                                pdfTO = boletaServiceImpl.getBoletaModificacion(detalleCert);
                                pdfTO.setHtml("[*fechaCert*]", sbCert.toString());
                                if (detalleCert.getIdTipoTramite() == 7) {
                                    pdfTO.setValue("[*title*]", "MODIFICACI\u00d3N DE LA");
                                    pdfTO.setValue("[*operacion*]", "Certificaci\u00f3n de Modificaci\u00f3n");
                                    pdfTO.setTypeValue("Certificaci\u00f3n de Modificaci\u00f3n");
                                } else if (detalleCert.getIdTipoTramite() == 17) {
                                    pdfTO.setValue("[*title*]", "ANOTACI\u00d3N DE LEVANTADO DE EMBARGO DE LA");
                                    pdfTO.setValue("[*operacion*]",
                                            "Certificaci\u00f3n de Anotaci\u00f3n de Levantado de Embargo");
                                    pdfTO.setTypeValue("Certificaci\u00f3n de Anotaci\u00f3n de Levantado de Embargo");
                                } else {
                                    pdfTO.setValue("[*title*]", "ANOTACI\u00d3N DE EMBARGO DE LA");
                                    pdfTO.setValue("[*operacion*]", "Certificaci\u00f3n de Anotaci\u00f3n de Embargo");
                                    pdfTO.setTypeValue("Certificaci\u00f3n de Anotaci\u00f3n de Embargo");
                                }
                            } else if (detalleCert.getIdTipoTramite() == 9) {
                                pdfTO = boletaServiceImpl.getBoletaRenovacion(detalleCert);
                                pdfTO.setValue("[*operacion*]", "Certificaci\u00f3n de Pr\u00f3rroga");
                                pdfTO.setTypeValue("Certificaci\u00f3n de Pr\u00f3rroga");
                                pdfTO.setHtml("[*fechaCert*]", sbCert.toString());
                            } else if (detalleCert.getIdTipoTramite() == 30) {
                                pdfTO = boletaServiceImpl.getBoletaEjecucion(detalleCert);
                                pdfTO.setValue("[*operacion*]", "Certificaci\u00f3n de Ejecuci\u00f3n");
                                pdfTO.setTypeValue("Certificaci\u00f3n de Ejecuci\u00f3n");
                                pdfTO.setHtml("[*fechaCert*]", sbCert.toString());
                            }
                            pdfTO.setSave("1");
                        }
                        /*
                         * else {
                         * pdfTO.setFile(myFile);
                         * pdfTO.setSave("1");
                         * }
                         */

                        // pdfTO = boletaServiceImpl.getBoletaInscripcion(detalleCert);
                        // pdfTO.setIdTramite(detalleCert.getIdTramite());
                        // pdfTO.setValue("[*tipoTramite*]", "Inscripci�n");
                        // pdfTO.setHtml(boletaServices.getCertificacionHtml());

                        /*
                         * pdfTO.setHtml("[*boletaHtml*]", html);
                         * String vDeudores = "";
                         * for(Iterator<mx.gob.se.rug.boleta.to.PersonaTO>
                         * it=detalleCert.getAcreedorDeudor().iterator(); it.hasNext();) {
                         * mx.gob.se.rug.boleta.to.PersonaTO person = it.next();
                         * vDeudores += "," + person.getNombre().toUpperCase();
                         * }
                         * pdfTO.setHtml("[*deudor*]", vDeudores);
                         *
                         * String VAcreedores = "";
                         * for(Iterator<mx.gob.se.rug.boleta.to.PersonaTO>
                         * it=detalleCert.getAcreedorRepresentado().iterator(); it.hasNext();) {
                         * mx.gob.se.rug.boleta.to.PersonaTO person = it.next();
                         * VAcreedores += "," + person.getNombre().toUpperCase();
                         * }
                         * pdfTO.setHtml("[*acreedor*]", VAcreedores);
                         *
                         * pdfTO.setHtml("[*fechaInicio*]",
                         * detalleCert.getGarantiaTO().getFechaInicioContrato());
                         * pdfTO.setHtml("[*fechaFin*]",
                         * detalleCert.getGarantiaTO().getFechaFinContrato());
                         * pdfTO.setHtml("[*garantia*]",
                         * detalleCert.getGarantiaTO().getDescripcionBienes());
                         * pdfTO.setHtml("[*instrumentoPublico*]",
                         * detalleCert.getGarantiaTO().getInstrumentoPublico());
                         * pdfTO.setHtml("[*fechaInscripcion*]",
                         * detalleCert.getGarantiaTO().getFechaInscripcion());
                         */
                        // MyLogger.Logger.log(Level.INFO, "HtmlPreview::"+html);
                        // pdfTO.setHtml(html);
                        pdfTO.setIdTramite(idTramiteF);
                        /*
                         * pdfTO = boletaServiceImpl.setFirmaCert(pdfTO, idTramiteVar,
                         * tramites, usuarioTO);
                         */

                        break;
                    case 6:// Rectificacion por error
                        mx.gob.se.rug.boleta.to.DetalleTO detalleRectificaTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                        detalleRectificaTO.setIdTramite(boletaDAO
                                .getIdTramitebyIdTramiteNuevo(idTramiteVar));
                        detalleRectificaTO.setIdTipoTramite(idTipoTramiteVar);
                        pdfTO = boletaServiceImpl
                                .getBoletaRectificacion(detalleRectificaTO);
                        pdfTO.setIdTramite(detalleRectificaTO.getIdTramite());
                        pdfTO.setValue("[*tipoTramite*]", "Rectificaci�n por Error");
                        break;
                    case 7:// Modification
                        mx.gob.se.rug.boleta.to.DetalleTO detalleModificaTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                        detalleModificaTO.setIdTramite(boletaDAO
                                .getIdTramitebyIdTramiteNuevo(idTramiteVar));
                        detalleModificaTO.setIdTipoTramite(idTipoTramiteVar);
                        pdfTO = boletaServiceImpl
                                .getBoletaModificacion(detalleModificaTO);
                        pdfTO.setIdTramite(detalleModificaTO.getIdTramite());
                        pdfTO.setValue("[*operacion*]", "Modificaci\u00f3n");
                        pdfTO.setTypeValue("Modificaci\u00f3n");
                        pdfTO.setValue("[*title*]", "MODIFICACI\u00d3N DE LA");
                        pdfTO.setHtml("[*fechaCert*]", "");

                        break;
                    case 13:// Embargo
                        MyLogger.Logger.log(Level.INFO, "Embargo ");
                        mx.gob.se.rug.boleta.to.DetalleTO detalleEmbargoTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                        detalleEmbargoTO.setIdTramite(boletaDAO
                                .getIdTramitebyIdTramiteNuevo(idTramiteVar));
                        detalleEmbargoTO.setIdTipoTramite(idTipoTramiteVar);
                        pdfTO = boletaServiceImpl
                                .getBoletaModificacion(detalleEmbargoTO);
                        pdfTO.setIdTramite(detalleEmbargoTO.getIdTramite());
                        pdfTO.setValue("[*operacion*]", "Anotaci\u00f3n de Embargo");
                        pdfTO.setTypeValue("Anotaci\u00f3n de Embargo");
                        pdfTO.setValue("[*title*]", "ANOTACI\u00d3N DE EMBARGO DE LA");
                        pdfTO.setHtml("[*fechaCert*]", "");

                        break;
                    case 17:
                        MyLogger.Logger.log(Level.INFO, "DesEmbargo ");
                        mx.gob.se.rug.boleta.to.DetalleTO detalleDEmbargoTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                        detalleDEmbargoTO.setIdTramite(boletaDAO
                                .getIdTramitebyIdTramiteNuevo(idTramiteVar));
                        detalleDEmbargoTO.setIdTipoTramite(idTipoTramiteVar);
                        pdfTO = boletaServiceImpl
                                .getBoletaModificacion(detalleDEmbargoTO);
                        pdfTO.setIdTramite(detalleDEmbargoTO.getIdTramite());
                        pdfTO.setValue("[*operacion*]", "Anotaci\u00f3n de Levantado de Embargo");
                        pdfTO.setTypeValue("Anotaci\u00f3n de Levantado de Embargo");
                        pdfTO.setValue("[*title*]", "ANOTACI\u00d3N DE LEVANTADO DE EMBARGO DE LA");
                        pdfTO.setHtml("[*fechaCert*]", "");

                        break;
                    case 8:// Transmision
                        mx.gob.se.rug.boleta.to.DetalleTO detalleTransmisionTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                        detalleTransmisionTO.setIdTramite(boletaDAO
                                .getIdTramitebyIdTramiteNuevo(idTramiteVar));
                        detalleTransmisionTO.setIdTipoTramite(idTipoTramiteVar);
                        pdfTO = boletaServiceImpl
                                .getBoletaTransmision(detalleTransmisionTO);
                        pdfTO.setIdTramite(detalleTransmisionTO.getIdTramite());
                        pdfTO.setValue("[*tipoTramite*]", "Transmisi�n");
                        break;
                    case 9:// Renovacion o reduccion de vigencia
                        mx.gob.se.rug.boleta.to.DetalleTO detalleRenovacionTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                        detalleRenovacionTO.setIdTramite(boletaDAO
                                .getIdTramitebyIdTramiteNuevo(idTramiteVar));
                        detalleRenovacionTO.setIdTipoTramite(idTipoTramiteVar);
                        pdfTO = boletaServiceImpl
                                .getBoletaRenovacion(detalleRenovacionTO);
                        pdfTO.setIdTramite(detalleRenovacionTO.getIdTramite());
                        pdfTO.setValue("[*operacion*]",
                                "Pr\u00f3rroga de vigencia");
                        pdfTO.setTypeValue("Pr\u00f3rroga de vigencia");
                        pdfTO.setHtml("[*fechaCert*]", "");

                        break;

                    case 30:// Ejecucion
                        mx.gob.se.rug.boleta.to.DetalleTO detalleEjecucionTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                        detalleEjecucionTO.setIdTramite(boletaDAO
                                .getIdTramitebyIdTramiteNuevo(idTramiteVar));
                        detalleEjecucionTO.setIdTipoTramite(idTipoTramiteVar);
                        pdfTO = boletaServiceImpl
                                .getBoletaEjecucion(detalleEjecucionTO);
                        pdfTO.setIdTramite(detalleEjecucionTO.getIdTramite());
                        pdfTO.setValue("[*operacion*]",
                                "Ejecuci\u00f3n");
                        pdfTO.setTypeValue("Ejecuci\u00f3n");
                        pdfTO.setHtml("[*fechaCert*]", "");

                        break;

                    case 38: // Transmision despues de prorroga
                        mx.gob.se.rug.boleta.to.DetalleTO detalleTransmisionTOs = new mx.gob.se.rug.boleta.to.DetalleTO();
                        detalleTransmisionTOs.setIdTramite(boletaDAO
                                .getIdTramitebyIdTramiteNuevo(idTramiteVar));
                        detalleTransmisionTOs.setIdTipoTramite(idTipoTramiteVar);
                        pdfTO = boletaServiceImpl
                                .getBoletaTransmision(detalleTransmisionTOs);
                        pdfTO.setIdTramite(detalleTransmisionTOs.getIdTramite());
                        pdfTO.setValue("[*tipoTramite*]", "Transmisi�n");
                        break;

                    case 10:// Anotacion sin garantia
                        Integer idTramiteFinal = boletaDAO
                                .getIdTramitebyIdTramiteNuevo(idTramiteVar);

                        pdfTOInfo = boletaServiceImpl
                                .getAnotacionSNGarantia(idTramiteFinal);

                        pdfTO.setHtml(boletaServices.getBoletaHtml());
                        pdfTO.setHtml("[*boletaHtml*]", pdfTOInfo.getHtml());
                        pdfTO.setIdTramite(idTramiteFinal);
                        pdfTO.setValue("[*tipoTramite*]", "Anotaci�n");

                        break;
                    case 28:// Anotacion sin garantia Modificacion
                        Integer idTramiteFinal1 = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar);

                        pdfTOInfo = boletaServiceImpl.getAnotacionSNGarantiaModificacion(idTramiteFinal1);

                        pdfTO.setHtml(boletaServices.getBoletaHtml());
                        pdfTO.setHtml("[*boletaHtml*]", pdfTOInfo.getHtml());
                        pdfTO.setIdTramite(idTramiteFinal1);
                        pdfTO.setValue("[*tipoTramite*]", "Anotaci�n Modificaci�n");

                        break;
                    case 27:// Anotacion sin garantia CAncelacion
                        Integer idTramiteFinal2 = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar);

                        pdfTOInfo = boletaServiceImpl.getAnotacionSNGarantiaCancelacion(idTramiteFinal2);

                        pdfTO.setHtml(boletaServices.getBoletaHtml());
                        pdfTO.setHtml("[*boletaHtml*]", pdfTOInfo.getHtml());
                        pdfTO.setIdTramite(idTramiteFinal2);
                        pdfTO.setValue("[*tipoTramite*]", "Anotaci�n Cancelaci�n");

                        break;
                    case 29:// Anotacion sin garantia
                        Integer idTramiteFinal3 = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar);

                        pdfTOInfo = boletaServiceImpl.getAnotacionSNGarantiaRectificacion(idTramiteFinal3);

                        pdfTO.setHtml(boletaServices.getBoletaHtml());
                        pdfTO.setHtml("[*boletaHtml*]", pdfTOInfo.getHtml());
                        pdfTO.setIdTramite(idTramiteFinal3);
                        pdfTO.setValue("[*tipoTramite*]", "Anotaci�n Rectificaci�n");

                        break;
                    case 26:// Anotacion sin garantia Renovacion
                        Integer idTramiteFinal4 = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar);

                        pdfTOInfo = boletaServiceImpl.getAnotacionSNGarantiaRenovacion(idTramiteFinal4);

                        pdfTO.setHtml(boletaServices.getBoletaHtml());
                        pdfTO.setHtml("[*boletaHtml*]", pdfTOInfo.getHtml());
                        pdfTO.setIdTramite(idTramiteFinal4);
                        pdfTO.setValue("[*tipoTramite*]", "Anotaci�n Renovaci�n o Reducci�n de Vigencia");

                        break;
                    case 18:// Firma masiva

                        InscripcionService inscripcionService = new InscripcionServiceImpl();
                        int idEstatus = new FirmaMasivaDAO().getEstatusByTramiteTemporal(idTramiteVar);

                        MasivaDAO masivaDAO = new MasivaDAO();
                        setIdTipoTramiteMasiva(masivaDAO.getIdTipoTramiteMasiva(idTramiteVar));

                        switch (idEstatus) {

                            case 3:
                                Integer idTramiteFirma = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar);
                                MyLogger.Logger.log(Level.INFO, "Masiva::idTramiteFirma --" + idTramiteVar + "--");
                                /**
                                 * lista de tramites *
                                 */
                                List<Integer> tramitesMasivos = new ArrayList<Integer>();
                                tramitesMasivos = masivaDAO.getIdTramitesMasivos(idTramiteVar);

                                int iteracion = 0;
                                for (Iterator<Integer> it = tramitesMasivos.iterator(); it.hasNext();) {
                                    Integer tramMas = it.next();
                                    mx.gob.se.rug.boleta.to.DetalleTO detalleMasTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                                    PdfTO pdfMas = new PdfTO();
                                    detalleMasTO.setIdTramite(boletaDAO.getIdTramitebyIdTramiteNuevo(tramMas));
                                    MyLogger.Logger.log(Level.INFO,
                                            "Masiva::idTramite --" + detalleMasTO.getIdTramite() + "--");
                                    detalleMasTO.setIdTipoTramite(boletaDAO.getTipoTramitebyIdTramiteTemporal(tramMas));
                                    MyLogger.Logger.log(Level.INFO,
                                            "Masiva::idTipoTramite --" + detalleMasTO.getIdTipoTramite() + "--");
                                    iteracion++;
                                    if (detalleMasTO.getIdTipoTramite() == 1 || detalleMasTO.getIdTipoTramite() == 31) {
                                        pdfMas = boletaServiceImpl.getBoletaInscripcion(detalleMasTO);
                                        pdfMas.setValue("[*operacion*]", "Inscripci\u00f3n");
                                        pdfMas.setTypeValue("Inscripci\u00f3n");
                                        pdfMas.setHtml("[*fechaCert*]", "");
                                    } else if (detalleMasTO.getIdTipoTramite() == 7) {
                                        pdfMas = boletaServiceImpl.getBoletaModificacion(detalleMasTO);
                                        pdfMas.setValue("[*operacion*]", "Modificaci\u00f3n");
                                        pdfMas.setTypeValue("Modificaci\u00f3n");
                                        pdfMas.setHtml("[*fechaCert*]", "");
                                    } else if (detalleMasTO.getIdTipoTramite() == 9) {
                                        pdfMas = boletaServiceImpl.getBoletaRenovacion(detalleMasTO);
                                        pdfMas.setValue("[*operacion*]",
                                                "Pr\u00f3rroga de vigencia");
                                        pdfMas.setTypeValue("Pr\u00f3rroga de vigencia");
                                        pdfMas.setHtml("[*fechaCert*]", "");
                                    } else if (detalleMasTO.getIdTipoTramite() == 4) {
                                        pdfMas = boletaServiceImpl.getBoletaCancelacion(detalleMasTO);
                                        pdfMas.setValue("[*operacion*]", "Cancelaci\u00f3n");
                                        pdfMas.setTypeValue("Cancelaci\u00f3n");
                                        pdfMas.setHtml("[*fechaCert*]", "");
                                    } else if (detalleMasTO.getIdTipoTramite() == 30) {
                                        pdfMas = boletaServiceImpl.getBoletaEjecucion(detalleMasTO);
                                        pdfMas.setValue("[*operacion*]",
                                                "Ejecuci\u00f3n");
                                        pdfMas.setTypeValue("Ejecuci\u00f3n");
                                        pdfMas.setHtml("[*fechaCert*]", "");
                                    }

                                    StringBuilder stringBuilder = new StringBuilder();

                                    this.setTypeData(pdfMas.getTypeValue());

                                    if (pdfTO.getHtml() != null) {

                                        stringBuilder.append(pdfTO.getHtml());
                                        stringBuilder.append(
                                                "<br /><h1 style=\"page-break-before: always\">&nbsp;</h1><br />");

                                    }

                                    stringBuilder.append(pdfMas.getHtml());
                                    htmlLista.add(pdfMas.getHtml());
                                    pdfTO.setHtml(stringBuilder.toString());
                                    // htmlLista.add(stringBuilder.toString());
                                    pdfTO.setHtmlList(htmlLista);
                                    pdfTO.setMassive("True");
                                    /* agregra la lista del html */
                                    // pdfTO.setHtmlList(pdfMas.getHtml());
                                }

                                pdfTO.setTypeValue(this.getTypeData());
                                pdfTO.setIdTramite(idTramiteFirma);
                                // pdfTO.setValue("[*nInscripciones*]",
                                // bs.getSizeAsientos(idTramiteVar).toString());
                                // pdfTO.setHtml("[*parteFirma*]",constants.getParamValue(Constants.BOLETA_PARTE_FIRMA));
                                // bs.setFirma(pdfTO);
                                FirmaMasiva firmaMasiva = new FirmaMasiva();

                                byte[] bytes = convertXMLObjetc(firmaMasiva);

                                ArchivoTO archivoN = new ArchivoTO();
                                archivoN.setAlgoritoHash("N/A");
                                archivoN.setArchivo(bytes);
                                archivoN.setDescripcion(
                                        "Archivo de resultado de firma masiva del tramite :" + idTramiteVar);
                                archivoN.setIdUsuario(usuarioTO.getPersona().getIdPersona());
                                archivoN.setNombreArchivo("cmResnuevo");
                                archivoN.setTipoArchivo("xml");
                                setIdArchivoRes(inscripcionService.insertArchivo(archivoN).getResIntPl());
                                regresa = "firmaMasiva";

                                break;
                            case 5:
                                regresa = "errpendiente";
                                setMessageKey(
                                        "La boleta se esta generando se notificara a su correo con un mail cuando este lista");
                                setMessageError("No existe la boleta");
                                throw new PendienteException();
                            case 8:
                                regresa = "errpendiente";
                                setMessageKey(
                                        "La boleta se esta generando se notificara a su correo con un mail cuando este lista");
                                setMessageError("No existe la boleta");
                                throw new PendienteException();
                            default:
                                MyLogger.Logger.log(Level.INFO, "El tramite no tiene estatus");
                                regresa = "errtramite";
                                setMessageKey("El tramite no tiene estatus favor de verificar e intentarlo nuevamente");
                                setMessageError("No existe el tramite");
                                throw new PendienteException();
                        }

                        break;
                }
//                switch (idTipoTramiteVar) {
//                    case 1:// Inscripcion
//
//                        mx.gob.se.rug.boleta.to.DetalleTO detalleTO = new mx.gob.se.rug.boleta.to.DetalleTO();
//                        detalleTO.setIdTramite(boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar));
//                        detalleTO.setIdTipoTramite(idTipoTramiteVar);
//                        pdfTO = boletaServiceImpl.getBoletaInscripcion(detalleTO);
//                        pdfTO.setIdTramite(detalleTO.getIdTramite());
//                        // pdfTO.setValue("[*operacion*]", "Inscripci\u00f3n" + anexo);
//                        pdfTO.setHtml("[*cert*]", "");
//                        pdfTO.setHtml("[*fechaCert*]", "");
//
//                        break;
//                    case 31:// Migracion Inscripcion
//
//                        mx.gob.se.rug.boleta.to.DetalleTO detalleMiTO = new mx.gob.se.rug.boleta.to.DetalleTO();
//                        detalleMiTO.setIdTramite(boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar));
//                        detalleMiTO.setIdTipoTramite(idTipoTramiteVar);
//                        pdfTO = boletaServiceImpl.getBoletaInscripcion(detalleMiTO);
//                        pdfTO.setIdTramite(detalleMiTO.getIdTramite());
//                        pdfTO.setValue("[*operacion*]", "Inscripci\u00f3n Traslado");
//                        pdfTO.setTypeValue("Inscripci\u00f3n Traslado");
//                        pdfTO.setHtml("[*fechaCert*]", "");
//
//                        break;
//                    case 2:// Anotacioncongarantiaanota
//                        MyLogger.Logger.log(Level.INFO, "-Anotacion con garantia action-");
//                        mx.gob.se.rug.boleta.to.DetalleTO detalleAnotaTO = new mx.gob.se.rug.boleta.to.DetalleTO();
//                        detalleAnotaTO.setIdTramite(boletaDAO
//                                .getIdTramitebyIdTramiteNuevo(idTramiteVar));
//                        detalleAnotaTO.setIdTipoTramite(idTipoTramiteVar);
//                        pdfTO = boletaServiceImpl
//                                .getBoletaAnotacion(detalleAnotaTO);
//                        pdfTO.setIdTramite(detalleAnotaTO.getIdTramite());
//                        pdfTO.setValue("[*tipoTramite*]", "Anotaci�n");
//                        pdfTO.setHtml("[*fechaCert*]", "");
//
//                        break;
//                    case 3:// Aviso preventivo
//                        MyLogger.Logger.log(Level.INFO, "-----------Aviso Preventivo Boleta-------");
//                        Integer idTramiteAviso = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar);
//                        pdfTOInfo = boletaServiceImpl.getAvisoPreventivo(idTramiteAviso);
//                        pdfTO.setHtml(boletaServices.getBoletaHtml());
//                        pdfTO.setHtml("[*boletaHtml*]", pdfTOInfo.getHtml());
//                        pdfTO.setIdTramite(idTramiteAviso);
//                        pdfTO.setValue("[*tipoTramite*]", "Aviso Preventivo");
//                        pdfTO.setHtml("[*fechaCert*]", "");
//
//                        break;
//                    case 4:// Cancelacion
//                        mx.gob.se.rug.boleta.to.DetalleTO detallecancelaTO = new mx.gob.se.rug.boleta.to.DetalleTO();
//                        detallecancelaTO.setIdTramite(boletaDAO
//                                .getIdTramitebyIdTramiteNuevo(idTramiteVar));
//                        detallecancelaTO.setIdTipoTramite(idTipoTramiteVar);
//                        mx.gob.se.rug.boleta.to.DetalleTO detallecancelaTOF = boletaDAO
//                                .getDetalleGarantiaTramite(detallecancelaTO);
//                        pdfTO = boletaServiceImpl
//                                .getBoletaCancelacion(detallecancelaTOF);
//                        pdfTO.setIdTramite(detallecancelaTO.getIdTramite());
//                        pdfTO.setValue("[*operacion*]", "Cancelaci\u00f3n");
//                        pdfTO.setTypeValue("Cancelaci\u00f3n");
//                        pdfTO.setHtml("[*fechaCert*]", "");
//
//                        break;
//                    case 5:// Certificacion
//                        // MyLogger.Logger.log(Level.INFO, "BoletaAction::> case 5 -- Certificacion :
//                        // valoir del idTramiteVar::::" + idTramiteVar);
//                        Integer idTramiteF = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar);
//                        DetalleTO detalleCert = boletaDAO.getCertificacion(idTramiteF);
//
//                        System.out.println("Garantia " + detalleCert.getIdGarantia());
//                        pdfTO.setIdGarantiaTO(Integer.valueOf(detalleCert.getIdGarantia()));
//                        // byte myFile[] = boletaDAO.getPdfBoleta(detalleCert.getIdTramite());
//                        byte myFile[] = null;
//
//                        /*
//                         * if (detalleCert.getIdTipoTramite() == 3) {// AvisoPreventivo
//                         * pdfTOInfo = boletaServiceImpl
//                         * .getAvisoPreventivo(detalleCert.getIdTramite());
//                         * html = pdfTOInfo.getHtml();
//                         * html = "<b>Aviso Preventivo:</b><BR/>" + html;
//                         * tramites = detalleCert.getIdTramite().toString();
//                         *
//                         * } else if (detalleCert.getIdTipoTramite().intValue()==27 ||
//                         * detalleCert.getIdTipoTramite().intValue()==28 ||
//                         * detalleCert.getIdTipoTramite().intValue()==29 ||
//                         * detalleCert.getIdTipoTramite().intValue()==26||
//                         * detalleCert.getIdTipoTramite().intValue()==10 ){// Anotacion sn Garantia
//                         *
//                         * TramitesDAO anotacionDao= new TramitesDAO();
//                         * tramites =
//                         * anotacionDao.getListTramitesHByIdTramitePadre(detalleCert.getIdGarantia());
//                         * pdfTOInfo =
//                         * boletaServiceImpl.getCertificacionDetalleAnotacion(detalleCert.getIdGarantia(
//                         * ));
//                         * html = pdfTOInfo.getHtml();
//                         * } else {// tramite con garantia
//                         * pdfTOInfo =
//                         * boletaServiceImpl.getCertificacionDetalle(detalleCert.getIdGarantia(),
//                         * detalleCert.getIdTramite());
//                         * tramites =
//                         * boletaServiceImpl.getTramitesByGarantia(Integer.valueOf(detalleCert.
//                         * getIdGarantia()));
//                         * html = pdfTOInfo.getHtml();
//                         * }
//                         */
//                        if (myFile == null) {
//
//                            StringBuffer sbCert = new StringBuffer();
//                            LocalDateTime now = LocalDateTime.now();
//                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
//
//                            sbCert.append("<div class=\"input-field col s6\">");
//                            sbCert.append(
//                                    "<span class=\"blue-text text-darken-2\">Fecha Certificaci&oacute;n: </span>");
//                            sbCert.append("<span>" + now.format(formatter) + "</span></div>");
//                            sbCert.append("<div class=\"input-field col s6\"></div>");
//
//                            if (detalleCert.getIdTipoTramite() == 1) {
//                                pdfTO = boletaServiceImpl.getBoletaInscripcion(detalleCert);
//                                pdfTO.setHtml("[*cert*]", "Certificaci\u00f3n de ");
//                                pdfTO.setHtml("[*fechaCert*]", sbCert.toString());
//                            } else if (detalleCert.getIdTipoTramite() == 31) {
//                                pdfTO = boletaServiceImpl.getBoletaInscripcion(detalleCert);
//                                pdfTO.setValue("[*operacion*]", "Certificaci\u00f3n de Inscripci\u00f3n");
//                                pdfTO.setTypeValue("Certificaci\u00f3n de Inscripci\u00f3n");
//                                pdfTO.setHtml("[*fechaCert*]", sbCert.toString());
//                            } else if (detalleCert.getIdTipoTramite() == 4) {
//                                pdfTO = boletaServiceImpl.getBoletaCancelacion(detalleCert);
//                                pdfTO.setValue("[*operacion*]", "Certificaci\u00f3n de Cancelaci\u00f3n");
//                                pdfTO.setTypeValue("Certificaci\u00f3n de Cancelaci\u00f3n");
//                                pdfTO.setHtml("[*fechaCert*]", sbCert.toString());
//                            } else if (detalleCert.getIdTipoTramite() == 7 || detalleCert.getIdTipoTramite() == 17
//                                    || detalleCert.getIdTipoTramite() == 13) {
//                                pdfTO = boletaServiceImpl.getBoletaModificacion(detalleCert);
//                                pdfTO.setHtml("[*fechaCert*]", sbCert.toString());
//                                if (detalleCert.getIdTipoTramite() == 7) {
//                                    pdfTO.setValue("[*title*]", "MODIFICACI\u00d3N DE LA");
//                                    pdfTO.setValue("[*operacion*]", "Certificaci\u00f3n de Modificaci\u00f3n");
//                                    pdfTO.setTypeValue("Certificaci\u00f3n de Modificaci\u00f3n");
//                                } else if (detalleCert.getIdTipoTramite() == 17) {
//                                    pdfTO.setValue("[*title*]", "ANOTACI\u00d3N DE LEVANTADO DE EMBARGO DE LA");
//                                    pdfTO.setValue("[*operacion*]",
//                                            "Certificaci\u00f3n de Anotaci\u00f3n de Levantado de Embargo");
//                                    pdfTO.setTypeValue("Certificaci\u00f3n de Anotaci\u00f3n de Levantado de Embargo");
//                                } else {
//                                    pdfTO.setValue("[*title*]", "ANOTACI\u00d3N DE EMBARGO DE LA");
//                                    pdfTO.setValue("[*operacion*]", "Certificaci\u00f3n de Anotaci\u00f3n de Embargo");
//                                    pdfTO.setTypeValue("Certificaci\u00f3n de Anotaci\u00f3n de Embargo");
//                                }
//                            } else if (detalleCert.getIdTipoTramite() == 9) {
//                                pdfTO = boletaServiceImpl.getBoletaRenovacion(detalleCert);
//                                pdfTO.setValue("[*operacion*]", "Certificaci\u00f3n de Pr\u00f3rroga");
//                                pdfTO.setTypeValue("Certificaci\u00f3n de Pr\u00f3rroga");
//                                pdfTO.setHtml("[*fechaCert*]", sbCert.toString());
//                            } else if (detalleCert.getIdTipoTramite() == 30) {
//                                pdfTO = boletaServiceImpl.getBoletaEjecucion(detalleCert);
//                                pdfTO.setValue("[*operacion*]", "Certificaci\u00f3n de Ejecuci\u00f3n");
//                                pdfTO.setTypeValue("Certificaci\u00f3n de Ejecuci\u00f3n");
//                                pdfTO.setHtml("[*fechaCert*]", sbCert.toString());
//                            }
//                            pdfTO.setSave("1");
//                        }
//                        /*
//                         * else {
//                         * pdfTO.setFile(myFile);
//                         * pdfTO.setSave("1");
//                         * }
//                         */
//
//                        // pdfTO = boletaServiceImpl.getBoletaInscripcion(detalleCert);
//                        // pdfTO.setIdTramite(detalleCert.getIdTramite());
//                        // pdfTO.setValue("[*tipoTramite*]", "Inscripci�n");
//                        // pdfTO.setHtml(boletaServices.getCertificacionHtml());
//
//                        /*
//                         * pdfTO.setHtml("[*boletaHtml*]", html);
//                         * String vDeudores = "";
//                         * for(Iterator<mx.gob.se.rug.boleta.to.PersonaTO>
//                         * it=detalleCert.getAcreedorDeudor().iterator(); it.hasNext();) {
//                         * mx.gob.se.rug.boleta.to.PersonaTO person = it.next();
//                         * vDeudores += "," + person.getNombre().toUpperCase();
//                         * }
//                         * pdfTO.setHtml("[*deudor*]", vDeudores);
//                         *
//                         * String VAcreedores = "";
//                         * for(Iterator<mx.gob.se.rug.boleta.to.PersonaTO>
//                         * it=detalleCert.getAcreedorRepresentado().iterator(); it.hasNext();) {
//                         * mx.gob.se.rug.boleta.to.PersonaTO person = it.next();
//                         * VAcreedores += "," + person.getNombre().toUpperCase();
//                         * }
//                         * pdfTO.setHtml("[*acreedor*]", VAcreedores);
//                         *
//                         * pdfTO.setHtml("[*fechaInicio*]",
//                         * detalleCert.getGarantiaTO().getFechaInicioContrato());
//                         * pdfTO.setHtml("[*fechaFin*]",
//                         * detalleCert.getGarantiaTO().getFechaFinContrato());
//                         * pdfTO.setHtml("[*garantia*]",
//                         * detalleCert.getGarantiaTO().getDescripcionBienes());
//                         * pdfTO.setHtml("[*instrumentoPublico*]",
//                         * detalleCert.getGarantiaTO().getInstrumentoPublico());
//                         * pdfTO.setHtml("[*fechaInscripcion*]",
//                         * detalleCert.getGarantiaTO().getFechaInscripcion());
//                         */
//                        // MyLogger.Logger.log(Level.INFO, "HtmlPreview::"+html);
//                        // pdfTO.setHtml(html);
//                        pdfTO.setIdTramite(idTramiteF);
//                        /*
//                         * pdfTO = boletaServiceImpl.setFirmaCert(pdfTO, idTramiteVar,
//                         * tramites, usuarioTO);
//                         */
//
//                        break;
//                    case 6:// Rectificacion por error
//                        mx.gob.se.rug.boleta.to.DetalleTO detalleRectificaTO = new mx.gob.se.rug.boleta.to.DetalleTO();
//                        detalleRectificaTO.setIdTramite(boletaDAO
//                                .getIdTramitebyIdTramiteNuevo(idTramiteVar));
//                        detalleRectificaTO.setIdTipoTramite(idTipoTramiteVar);
//                        pdfTO = boletaServiceImpl
//                                .getBoletaRectificacion(detalleRectificaTO);
//                        pdfTO.setIdTramite(detalleRectificaTO.getIdTramite());
//                        pdfTO.setValue("[*tipoTramite*]", "Rectificaci�n por Error");
//                        break;
//                    case 7:// Modification
//                        MyLogger.Logger.log(Level.INFO, "Modificacion ");
//                        mx.gob.se.rug.boleta.to.DetalleTO detalleModificaTO = new mx.gob.se.rug.boleta.to.DetalleTO();
//                        detalleModificaTO.setIdTramite(boletaDAO
//                                .getIdTramitebyIdTramiteNuevo(idTramiteVar));
//                        detalleModificaTO.setIdTipoTramite(idTipoTramiteVar);
//                        pdfTO = boletaServiceImpl
//                                .getBoletaModificacion(detalleModificaTO);
//                        pdfTO.setIdTramite(detalleModificaTO.getIdTramite());
//                        pdfTO.setValue("[*operacion*]", "Modificaci\u00f3n");
//                        pdfTO.setTypeValue("Modificaci\u00f3n");
//                        pdfTO.setValue("[*title*]", "MODIFICACI\u00d3N DE LA");
//                        pdfTO.setHtml("[*fechaCert*]", "");
//
//                        break;
//                    case 13:// Embargo
//                        MyLogger.Logger.log(Level.INFO, "Embargo ");
//                        mx.gob.se.rug.boleta.to.DetalleTO detalleEmbargoTO = new mx.gob.se.rug.boleta.to.DetalleTO();
//                        detalleEmbargoTO.setIdTramite(boletaDAO
//                                .getIdTramitebyIdTramiteNuevo(idTramiteVar));
//                        detalleEmbargoTO.setIdTipoTramite(idTipoTramiteVar);
//                        pdfTO = boletaServiceImpl
//                                .getBoletaModificacion(detalleEmbargoTO);
//                        pdfTO.setIdTramite(detalleEmbargoTO.getIdTramite());
//                        pdfTO.setValue("[*operacion*]", "Anotaci\u00f3n de Embargo");
//                        pdfTO.setTypeValue("Anotaci\u00f3n de Embargo");
//                        pdfTO.setValue("[*title*]", "ANOTACI\u00d3N DE EMBARGO DE LA");
//                        pdfTO.setHtml("[*fechaCert*]", "");
//
//                        break;
//                    case 17:
//                        MyLogger.Logger.log(Level.INFO, "DesEmbargo ");
//                        mx.gob.se.rug.boleta.to.DetalleTO detalleDEmbargoTO = new mx.gob.se.rug.boleta.to.DetalleTO();
//                        detalleDEmbargoTO.setIdTramite(boletaDAO
//                                .getIdTramitebyIdTramiteNuevo(idTramiteVar));
//                        detalleDEmbargoTO.setIdTipoTramite(idTipoTramiteVar);
//                        pdfTO = boletaServiceImpl
//                                .getBoletaModificacion(detalleDEmbargoTO);
//                        pdfTO.setIdTramite(detalleDEmbargoTO.getIdTramite());
//                        pdfTO.setValue("[*operacion*]", "Anotaci\u00f3n de Levantado de Embargo");
//                        pdfTO.setTypeValue("Anotaci\u00f3n de Levantado de Embargo");
//                        pdfTO.setValue("[*title*]", "ANOTACI\u00d3N DE LEVANTADO DE EMBARGO DE LA");
//                        pdfTO.setHtml("[*fechaCert*]", "");
//
//                        break;
//                    case 8:// Transmision
//                        mx.gob.se.rug.boleta.to.DetalleTO detalleTransmisionTO = new mx.gob.se.rug.boleta.to.DetalleTO();
//                        detalleTransmisionTO.setIdTramite(boletaDAO
//                                .getIdTramitebyIdTramiteNuevo(idTramiteVar));
//                        detalleTransmisionTO.setIdTipoTramite(idTipoTramiteVar);
//                        pdfTO = boletaServiceImpl
//                                .getBoletaTransmision(detalleTransmisionTO);
//                        pdfTO.setIdTramite(detalleTransmisionTO.getIdTramite());
//                        pdfTO.setValue("[*tipoTramite*]", "Transmisi�n");
//                        break;
//                    case 9:// Renovacion o reduccion de vigencia
//                        mx.gob.se.rug.boleta.to.DetalleTO detalleRenovacionTO = new mx.gob.se.rug.boleta.to.DetalleTO();
//                        detalleRenovacionTO.setIdTramite(boletaDAO
//                                .getIdTramitebyIdTramiteNuevo(idTramiteVar));
//                        detalleRenovacionTO.setIdTipoTramite(idTipoTramiteVar);
//                        pdfTO = boletaServiceImpl
//                                .getBoletaRenovacion(detalleRenovacionTO);
//                        pdfTO.setIdTramite(detalleRenovacionTO.getIdTramite());
//                        pdfTO.setValue("[*operacion*]",
//                                "Pr\u00f3rroga de vigencia");
//                        pdfTO.setTypeValue("Pr\u00f3rroga de vigencia");
//                        pdfTO.setHtml("[*fechaCert*]", "");
//
//                        break;
//
//                    case 30:// Ejecucion
//                        mx.gob.se.rug.boleta.to.DetalleTO detalleEjecucionTO = new mx.gob.se.rug.boleta.to.DetalleTO();
//                        detalleEjecucionTO.setIdTramite(boletaDAO
//                                .getIdTramitebyIdTramiteNuevo(idTramiteVar));
//                        detalleEjecucionTO.setIdTipoTramite(idTipoTramiteVar);
//                        pdfTO = boletaServiceImpl
//                                .getBoletaEjecucion(detalleEjecucionTO);
//                        pdfTO.setIdTramite(detalleEjecucionTO.getIdTramite());
//                        pdfTO.setValue("[*operacion*]",
//                                "Ejecuci\u00f3n");
//                        pdfTO.setTypeValue("Ejecuci\u00f3n");
//                        pdfTO.setHtml("[*fechaCert*]", "");
//
//                        break;
//
//                    case 38: // Transmision despues de prorroga
//                        mx.gob.se.rug.boleta.to.DetalleTO detalleTransmisionTOs = new mx.gob.se.rug.boleta.to.DetalleTO();
//                        detalleTransmisionTOs.setIdTramite(boletaDAO
//                                .getIdTramitebyIdTramiteNuevo(idTramiteVar));
//                        detalleTransmisionTOs.setIdTipoTramite(idTipoTramiteVar);
//                        pdfTO = boletaServiceImpl
//                                .getBoletaTransmision(detalleTransmisionTOs);
//                        pdfTO.setIdTramite(detalleTransmisionTOs.getIdTramite());
//                        pdfTO.setValue("[*tipoTramite*]", "Transmisi�n");
//                        break;
//
//                    case 10:// Anotacion sin garantia
//                        Integer idTramiteFinal = boletaDAO
//                                .getIdTramitebyIdTramiteNuevo(idTramiteVar);
//
//                        pdfTOInfo = boletaServiceImpl
//                                .getAnotacionSNGarantia(idTramiteFinal);
//
//                        pdfTO.setHtml(boletaServices.getBoletaHtml());
//                        pdfTO.setHtml("[*boletaHtml*]", pdfTOInfo.getHtml());
//                        pdfTO.setIdTramite(idTramiteFinal);
//                        pdfTO.setValue("[*tipoTramite*]", "Anotaci�n");
//
//                        break;
//                    case 28:// Anotacion sin garantia Modificacion
//                        Integer idTramiteFinal1 = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar);
//
//                        pdfTOInfo = boletaServiceImpl.getAnotacionSNGarantiaModificacion(idTramiteFinal1);
//
//                        pdfTO.setHtml(boletaServices.getBoletaHtml());
//                        pdfTO.setHtml("[*boletaHtml*]", pdfTOInfo.getHtml());
//                        pdfTO.setIdTramite(idTramiteFinal1);
//                        pdfTO.setValue("[*tipoTramite*]", "Anotaci�n Modificaci�n");
//
//                        break;
//                    case 27:// Anotacion sin garantia CAncelacion
//                        Integer idTramiteFinal2 = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar);
//
//                        pdfTOInfo = boletaServiceImpl.getAnotacionSNGarantiaCancelacion(idTramiteFinal2);
//
//                        pdfTO.setHtml(boletaServices.getBoletaHtml());
//                        pdfTO.setHtml("[*boletaHtml*]", pdfTOInfo.getHtml());
//                        pdfTO.setIdTramite(idTramiteFinal2);
//                        pdfTO.setValue("[*tipoTramite*]", "Anotaci�n Cancelaci�n");
//
//                        break;
//                    case 29:// Anotacion sin garantia
//                        Integer idTramiteFinal3 = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar);
//
//                        pdfTOInfo = boletaServiceImpl.getAnotacionSNGarantiaRectificacion(idTramiteFinal3);
//
//                        pdfTO.setHtml(boletaServices.getBoletaHtml());
//                        pdfTO.setHtml("[*boletaHtml*]", pdfTOInfo.getHtml());
//                        pdfTO.setIdTramite(idTramiteFinal3);
//                        pdfTO.setValue("[*tipoTramite*]", "Anotaci�n Rectificaci�n");
//
//                        break;
//                    case 26:// Anotacion sin garantia Renovacion
//                        Integer idTramiteFinal4 = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar);
//
//                        pdfTOInfo = boletaServiceImpl.getAnotacionSNGarantiaRenovacion(idTramiteFinal4);
//
//                        pdfTO.setHtml(boletaServices.getBoletaHtml());
//                        pdfTO.setHtml("[*boletaHtml*]", pdfTOInfo.getHtml());
//                        pdfTO.setIdTramite(idTramiteFinal4);
//                        pdfTO.setValue("[*tipoTramite*]", "Anotaci�n Renovaci�n o Reducci�n de Vigencia");
//
//                        break;
//                    case 18:// Firma masiva
//
//                        InscripcionService inscripcionService = new InscripcionServiceImpl();
//                        int idEstatus = new FirmaMasivaDAO().getEstatusByTramiteTemporal(idTramiteVar);
//
//                        MasivaDAO masivaDAO = new MasivaDAO();
//                        setIdTipoTramiteMasiva(masivaDAO.getIdTipoTramiteMasiva(idTramiteVar));
//
//                        switch (idEstatus) {
//
//                            case 3:
//                                Integer idTramiteFirma = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteVar);
//                                MyLogger.Logger.log(Level.INFO, "Masiva::idTramiteFirma --" + idTramiteVar + "--");
//                                /**
//                                 * lista de tramites *
//                                 */
//                                List<Integer> tramitesMasivos = new ArrayList<Integer>();
//                                tramitesMasivos = masivaDAO.getIdTramitesMasivos(idTramiteVar);
//
//                                int iteracion = 0;
//                                for (Iterator<Integer> it = tramitesMasivos.iterator(); it.hasNext();) {
//                                    Integer tramMas = it.next();
//                                    mx.gob.se.rug.boleta.to.DetalleTO detalleMasTO = new mx.gob.se.rug.boleta.to.DetalleTO();
//                                    PdfTO pdfMas = new PdfTO();
//                                    detalleMasTO.setIdTramite(boletaDAO.getIdTramitebyIdTramiteNuevo(tramMas));
//                                    MyLogger.Logger.log(Level.INFO,
//                                            "Masiva::idTramite --" + detalleMasTO.getIdTramite() + "--");
//                                    detalleMasTO.setIdTipoTramite(boletaDAO.getTipoTramitebyIdTramiteTemporal(tramMas));
//                                    MyLogger.Logger.log(Level.INFO,
//                                            "Masiva::idTipoTramite --" + detalleMasTO.getIdTipoTramite() + "--");
//                                    iteracion++;
//                                    if (detalleMasTO.getIdTipoTramite() == 1 || detalleMasTO.getIdTipoTramite() == 31) {
//                                        pdfMas = boletaServiceImpl.getBoletaInscripcion(detalleMasTO);
//                                        pdfMas.setValue("[*operacion*]", "Inscripci\u00f3n");
//                                        pdfMas.setTypeValue("Inscripci\u00f3n");
//                                        pdfMas.setHtml("[*fechaCert*]", "");
//                                    } else if (detalleMasTO.getIdTipoTramite() == 7) {
//                                        pdfMas = boletaServiceImpl.getBoletaModificacion(detalleMasTO);
//                                        pdfMas.setValue("[*operacion*]", "Modificaci\u00f3n");
//                                        pdfMas.setTypeValue("Modificaci\u00f3n");
//                                        pdfMas.setHtml("[*fechaCert*]", "");
//                                    } else if (detalleMasTO.getIdTipoTramite() == 9) {
//                                        pdfMas = boletaServiceImpl.getBoletaRenovacion(detalleMasTO);
//                                        pdfMas.setValue("[*operacion*]",
//                                                "Pr\u00f3rroga de vigencia");
//                                        pdfMas.setTypeValue("Pr\u00f3rroga de vigencia");
//                                        pdfMas.setHtml("[*fechaCert*]", "");
//                                    } else if (detalleMasTO.getIdTipoTramite() == 4) {
//                                        pdfMas = boletaServiceImpl.getBoletaCancelacion(detalleMasTO);
//                                        pdfMas.setValue("[*operacion*]", "Cancelaci\u00f3n");
//                                        pdfMas.setTypeValue("Cancelaci\u00f3n");
//                                        pdfMas.setHtml("[*fechaCert*]", "");
//                                    } else if (detalleMasTO.getIdTipoTramite() == 30) {
//                                        pdfMas = boletaServiceImpl.getBoletaEjecucion(detalleMasTO);
//                                        pdfMas.setValue("[*operacion*]",
//                                                "Ejecuci\u00f3n");
//                                        pdfMas.setTypeValue("Ejecuci\u00f3n");
//                                        pdfMas.setHtml("[*fechaCert*]", "");
//                                    }
//
//                                    StringBuilder stringBuilder = new StringBuilder();
//
//                                    this.setTypeData(pdfMas.getTypeValue());
//
//                                    if (pdfTO.getHtml() != null) {
//
//                                        stringBuilder.append(pdfTO.getHtml());
//                                        stringBuilder.append(
//                                                "<br /><h1 style=\"page-break-before: always\">&nbsp;</h1><br />");
//
//                                    }
//
//                                    stringBuilder.append(pdfMas.getHtml());
//                                    htmlLista.add(pdfMas.getHtml());
//                                    pdfTO.setHtml(stringBuilder.toString());
//                                    // htmlLista.add(stringBuilder.toString());
//                                    pdfTO.setHtmlList(htmlLista);
//                                    pdfTO.setMassive("True");
//                                    /* agregra la lista del html */
//                                    // pdfTO.setHtmlList(pdfMas.getHtml());
//                                }
//
//                                // System.out.println("Html normal = " + htmlLista.size());
//                                // System.out.println("Html Lista = " + pdfTO.getHtmlList().size());
//
//                                // System.out.println("iteracion = " + pdfTO.getHtmlList());
//                                pdfTO.setTypeValue(this.getTypeData());
//                                pdfTO.setIdTramite(idTramiteFirma);
//                                // pdfTO.setValue("[*nInscripciones*]",
//                                // bs.getSizeAsientos(idTramiteVar).toString());
//                                // pdfTO.setHtml("[*parteFirma*]",constants.getParamValue(Constants.BOLETA_PARTE_FIRMA));
//                                // bs.setFirma(pdfTO);
//                                FirmaMasiva firmaMasiva = new FirmaMasiva();
//
//                                byte[] bytes = convertXMLObjetc(firmaMasiva);
//
//                                ArchivoTO archivoN = new ArchivoTO();
//                                archivoN.setAlgoritoHash("N/A");
//                                archivoN.setArchivo(bytes);
//                                archivoN.setDescripcion(
//                                        "Archivo de resultado de firma masiva del tramite :" + idTramiteVar);
//                                archivoN.setIdUsuario(usuarioTO.getPersona().getIdPersona());
//                                archivoN.setNombreArchivo("cmResnuevo");
//                                archivoN.setTipoArchivo("xml");
//                                setIdArchivoRes(inscripcionService.insertArchivo(archivoN).getResIntPl());
//                                regresa = "firmaMasiva";
//
//                                break;
//                            case 5:
//                                regresa = "errpendiente";
//                                setMessageKey(
//                                        "La boleta se esta generando se notificara a su correo con un mail cuando este lista");
//                                setMessageError("No existe la boleta");
//                                throw new PendienteException();
//                            case 8:
//                                regresa = "errpendiente";
//                                setMessageKey(
//                                        "La boleta se esta generando se notificara a su correo con un mail cuando este lista");
//                                setMessageError("No existe la boleta");
//                                throw new PendienteException();
//                            default:
//                                MyLogger.Logger.log(Level.INFO, "El tramite no tiene estatus");
//                                regresa = "errtramite";
//                                setMessageKey("El tramite no tiene estatus favor de verificar e intentarlo nuevamente");
//                                setMessageError("No existe el tramite");
//                                throw new PendienteException();
//                        }
//
//                        break;
//                }

                if (pdfTO.getIdTramite() != null) {
                    pdfTO.setValue("[*idTramite*]", pdfTO.getIdTramite()
                            .toString());

                    /*
                     * Constants constants= new Constants();
                     * if (boletaServiceImpl.getProcedencia(
                     * pdfTO.getIdTramite()).equalsIgnoreCase("RUG_FIRMA_DOCTOS")){//Nuevo
                     * pdfTO.setValue("[*GMTExplica*]",constants.getParamValue(Constants.
                     * BOLETA_GMT_EXPLICA));
                     * }else{//Viejo
                     * pdfTO.setValue("[*GMTExplica*]", "");
                     * }
                     */
                    pdfTO.setValue("[*GMTExplica*]", "");

                }

                sessionMap.put("pdfTO", pdfTO);
                if (!regresa.equals("firmaMasiva")) {
                    regresa = SUCCESS;
                }
            } else {
                regresa = "failure";
                setMessageKey("No se pudo generar la boleta");
                setMessageError("No existe el tramite");
            }
        } catch (PendienteException e) {

        } catch (Exception e) {
            setMessageKey("No se pudo generar la boleta");
            setMessageError(e.getMessage());
            regresa = "failure";
            e.printStackTrace();
        } finally {
            setIdTramite(null);
        }
        // MyLogger.Logger.log(Level.INFO, "Valor de regresa --" + regresa + "--");
        return regresa;
    }

    public byte[] convertXMLObjetc(Object obj)
            throws JAXBException, FileNotFoundException, UnsupportedEncodingException {
        JAXBContext context = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
        StringWriter stringWriter = new StringWriter();
        marshaller.marshal(obj, stringWriter);
        return stringWriter.toString().getBytes();
    }

    public void setIdTramite(String idTramite) {
        this.idTramite = idTramite;
    }

    public String getIdTramite() {
        return idTramite;
    }

    public void setOtorganteTOs(List<PartesTO> otorganteTOs) {
        OtorganteTOs = otorganteTOs;
    }

    public List<PartesTO> getOtorganteTOs() {
        return OtorganteTOs;
    }

    public void setAcreedorTOs(List<PartesTO> acreedorTOs) {
        AcreedorTOs = acreedorTOs;
    }

    public List<PartesTO> getAcreedorTOs() {
        return AcreedorTOs;
    }

    public void setDeudorTOs(List<PartesTO> deudorTOs) {
        DeudorTOs = deudorTOs;
    }

    public List<PartesTO> getDeudorTOs() {
        return DeudorTOs;
    }

    public void setHistoriaTO(HistoriaTO historiaTO) {
        this.historiaTO = historiaTO;
    }

    public HistoriaTO getHistoriaTO() {
        return historiaTO;
    }

    public Integer getIdArchivoRes() {
        return idArchivoRes;
    }

    public void setIdArchivoRes(Integer idArchivoRes) {
        this.idArchivoRes = idArchivoRes;
    }

}
