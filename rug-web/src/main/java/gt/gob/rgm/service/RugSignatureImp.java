package gt.gob.rgm.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.io.ByteArrayInputStream;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import gt.gob.rgm.dao.RugSecuUsuariosRepository;
import gt.gob.rgm.model.RugSecuUsuarios;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import gt.gob.rgm.captcha.utils.Random;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;

import mx.gob.se.rug.boleta.dao.BoletaDAO;
import mx.gob.se.rug.boleta.serviceImpl.BoletaServiceImpl;
import mx.gob.se.rug.boleta.to.DetalleTO;
import mx.gob.se.rug.boleta.to.FirmaMasiva;
import mx.gob.se.rug.boletaservice.BoletaServices;
import mx.gob.se.rug.certificacion.dao.CertificacionDAO;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.exception.PendienteException;
import mx.gob.se.rug.garantia.dao.GarantiasDAO;
import mx.gob.se.rug.inscripcion.dao.FirmaMasivaDAO;
import mx.gob.se.rug.inscripcion.dao.InscripcionDAO;
import mx.gob.se.rug.inscripcion.service.InscripcionService;
import mx.gob.se.rug.inscripcion.service.impl.InscripcionServiceImpl;
import mx.gob.se.rug.masiva.dao.MasivaDAO;
import mx.gob.se.rug.masiva.to.ArchivoTO;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.pdf.PageXofY;
import mx.gob.se.rug.util.pdf.to.PdfTO;
import org.apache.pdfbox.pdmodel.PDDocument;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RugSignatureImp implements RugSignature {

//    public static final String SIGN_URL = 	 "http://128.5.101.19:8080/api/signature";
//    public static final String SIGN_BYTES =  "http://128.5.101.19:8080/api/toBytes";
//    public static final String SIGN_VERIFY = "http://128.5.101.19:8080/api/verifyFile";

   /* public static final String SIGN_URL = 	 "https://operaciones.rgm.gob.gt/api/signature";
    public static final String SIGN_BYTES =  "https://operaciones.rgm.gob.gt/api/toBytes";
    public static final String SIGN_VERIFY = "https://operaciones.rgm.gob.gt/api/verifyFile";*/
    
    public static final String SIGN_URL = 	 "https://qasistema.rgm.gob.gt/api/signature";
    public static final String SIGN_BYTES =  "https://qasistema.rgm.gob.gt/api/toBytes";
    public static final String SIGN_VERIFY = "https://qasistema.rgm.gob.gt/api/verifyFile";


    private Integer idTipoTramiteMasiva;
    private String typeData;
    private ArrayList<String> htmlLista = new ArrayList<String>();
    private Integer idArchivoRes;



    public Integer getIdTipoTramiteMasiva() {
        return idTipoTramiteMasiva;
    }

    public void setIdTipoTramiteMasiva(Integer idTipoTramiteMasiva) {
        this.idTipoTramiteMasiva = idTipoTramiteMasiva;
    }

    public String getTypeData() {
        return typeData;
    }

    public void setTypeData(String typeData) {
        this.typeData = typeData;
    }

    public Integer getIdArchivoRes() {
        return idArchivoRes;
    }

    public void setIdArchivoRes(Integer idArchivoRes) {
        this.idArchivoRes = idArchivoRes;
    }

    private RugSecuUsuariosRepository usuarioDao;

    public void setUsuarioDao(RugSecuUsuariosRepository usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    /**
     * @author: Juan José Jolón Granados
     * @return: Response file signature
     * @version: 26.09.2022
     */
    public Response signatureFiles(Integer idGarantia, Integer idTramite, Integer idUsuario, Integer idTipoTramiteRest){    
        System.out.println("INGRESA A signatureFiles DE RugSignatureImp.java");
        System.out.println("idGarantia:::"+idGarantia);
        System.out.println("idTramite:::"+idTramite);
        System.out.println("idUsuario:::"+idUsuario);
        System.out.println("idTipoTramiteRest:::"+idTipoTramiteRest);
        try {


            Integer idTramiteVar = (Integer) idTramite;
            Integer idGarantiaVar = (Integer) idGarantia;
            Integer idTipoTramiteVar = null;

            // Integer usuarioTO = (Integer) idUsuario;
            // Integer idTipoTramite = (Integer) idGarantia;
            String regresa = "";
        
            InscripcionDAO inscripcionDAO = new InscripcionDAO();
            GarantiasDAO garantiasDAO = new GarantiasDAO();
            BoletaDAO boletaDAO = new BoletaDAO();
            CertificacionDAO certificacionDAO = new CertificacionDAO();
            InscripcionServiceImpl inscripcionService = new InscripcionServiceImpl();
        
            byte file[] = null;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
        
        
            
            if(!inscripcionService.getSaldoByUsuario(idUsuario.toString(), idTipoTramiteRest, 0, idGarantiaVar)){
                return Response.ok("Sin Saldo").build();
            }
            else{
                Integer idTramiteNuevo = inscripcionDAO.insert(idUsuario, idTipoTramiteRest); 
                garantiasDAO.altaBitacoraTramite(idTramiteNuevo, new Integer(3), new Integer(0), null, "V");

                try {
                    idTipoTramiteVar = boletaDAO.getTipoTramitebyIdTramiteTemporal(idTramiteVar);
                    Integer idTramiteCert = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteNuevo);
                    Integer idTTramite = boletaDAO.getTipoTramitebyIdTramiteTerminado(idTramiteVar);
                    certificacionDAO.setCertificacion(idTramiteCert, idTramiteVar, idTTramite, idGarantiaVar, idUsuario);

                    BoletaServiceImpl boletaServiceImpl = new BoletaServiceImpl();
                        PdfTO pdfTO = new PdfTO();
                        pdfTO.setMassive("False");
                        pdfTO.setSave("0");

                        PdfTO pdfTOInfo = null;
                        BoletaServices boletaServices = new BoletaServices();

                        if(idTipoTramiteRest != null){
                            if (idTipoTramiteRest.intValue() == 15 || idTipoTramiteRest.intValue() == 16) {
                                idTipoTramiteRest = 1;
                            }
                            switch(idTipoTramiteRest){
                                case 1:// Inscripcion
                                    mx.gob.se.rug.boleta.to.DetalleTO detalleTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                                    detalleTO.setIdTramite(boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteNuevo));
                                    detalleTO.setIdTipoTramite(idTipoTramiteRest);
                                    pdfTO = boletaServiceImpl.getBoletaInscripcion(detalleTO);
                                    pdfTO.setIdTramite(detalleTO.getIdTramite());
                                    pdfTO.setHtml("[*cert*]", "");
                                    pdfTO.setHtml("[*fechaCert*]", "");

                                    break;
                                case 31:// Migracion Inscripcion
                                    mx.gob.se.rug.boleta.to.DetalleTO detalleMiTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                                    detalleMiTO.setIdTramite(boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteNuevo));
                                    detalleMiTO.setIdTipoTramite(idTipoTramiteRest);
                                    pdfTO = boletaServiceImpl.getBoletaInscripcion(detalleMiTO);
                                    pdfTO.setIdTramite(detalleMiTO.getIdTramite());
                                    pdfTO.setValue("[*operacion*]", "Inscripci\u00f3n Traslado");
                                    pdfTO.setTypeValue("Inscripci\u00f3n Traslado");
                                    pdfTO.setHtml("[*fechaCert*]", "");
                                    break;
                                case 2:// Anotacioncongarantiaanota
                                    mx.gob.se.rug.boleta.to.DetalleTO detalleAnotaTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                                    detalleAnotaTO.setIdTramite(boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteNuevo));
                                    detalleAnotaTO.setIdTipoTramite(idTipoTramiteRest);
                                    pdfTO = boletaServiceImpl.getBoletaAnotacion(detalleAnotaTO);
                                    pdfTO.setIdTramite(detalleAnotaTO.getIdTramite());
                                    pdfTO.setValue("[*tipoTramite*]", "Anotaci�n");
                                    pdfTO.setHtml("[*fechaCert*]", "");
                                    break;
                                case 3:// Aviso preventivo
                                    Integer idTramiteAviso = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteNuevo);
                                    pdfTOInfo = boletaServiceImpl.getAvisoPreventivo(idTramiteAviso);
                                    pdfTO.setHtml(boletaServices.getBoletaHtml());
                                    pdfTO.setHtml("[*boletaHtml*]", pdfTOInfo.getHtml());
                                    pdfTO.setIdTramite(idTramiteAviso);
                                    pdfTO.setValue("[*tipoTramite*]", "Aviso Preventivo");
                                    pdfTO.setHtml("[*fechaCert*]", "");
                                    break;
                                case 4:// Cancelacion
                                    mx.gob.se.rug.boleta.to.DetalleTO detallecancelaTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                                    detallecancelaTO.setIdTramite(boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteNuevo));
                                    detallecancelaTO.setIdTipoTramite(idTipoTramiteRest);
                                    mx.gob.se.rug.boleta.to.DetalleTO detallecancelaTOF = boletaDAO.getDetalleGarantiaTramite(detallecancelaTO);
                                    pdfTO = boletaServiceImpl.getBoletaCancelacion(detallecancelaTOF);
                                    pdfTO.setIdTramite(detallecancelaTO.getIdTramite());
                                    pdfTO.setValue("[*operacion*]", "Cancelaci\u00f3n");
                                    pdfTO.setTypeValue("Cancelaci\u00f3n");
                                    pdfTO.setHtml("[*fechaCert*]", "");
                                    break;
                                case 35:// Certificacion Nuevo Arancel
                                    Integer idTramiteCertificacion = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteNuevo);
                                    System.out.println("Valor 2 " + idTramiteNuevo);
                                    DetalleTO detalleCertNuevo = boletaDAO.getCertificacion(idTramiteCertificacion);
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
                                    pdfTO.setIdTramite(idTramiteCertificacion);
                                    break;
                                case 5:// Certificacion
                                    Integer idTramiteF = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteNuevo);
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
                                    detalleRectificaTO.setIdTramite(boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteNuevo));
                                    detalleRectificaTO.setIdTipoTramite(idTipoTramiteRest);
                                    pdfTO = boletaServiceImpl.getBoletaRectificacion(detalleRectificaTO);
                                    pdfTO.setIdTramite(detalleRectificaTO.getIdTramite());
                                    pdfTO.setValue("[*tipoTramite*]", "Rectificaci�n por Error");
                                    break;
                                case 7:// Modification
                                    mx.gob.se.rug.boleta.to.DetalleTO detalleModificaTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                                    detalleModificaTO.setIdTramite(boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteNuevo));
                                    detalleModificaTO.setIdTipoTramite(idTipoTramiteRest);
                                    pdfTO = boletaServiceImpl.getBoletaModificacion(detalleModificaTO);
                                    pdfTO.setIdTramite(detalleModificaTO.getIdTramite());
                                    pdfTO.setValue("[*operacion*]", "Modificaci\u00f3n");
                                    pdfTO.setTypeValue("Modificaci\u00f3n");
                                    pdfTO.setValue("[*title*]", "MODIFICACI\u00d3N DE LA");
                                    pdfTO.setHtml("[*fechaCert*]", "");
                                    break;
                                case 13:// Embargo
                                    mx.gob.se.rug.boleta.to.DetalleTO detalleEmbargoTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                                    detalleEmbargoTO.setIdTramite(boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteNuevo));
                                    detalleEmbargoTO.setIdTipoTramite(idTipoTramiteRest);
                                    pdfTO = boletaServiceImpl.getBoletaModificacion(detalleEmbargoTO);
                                    pdfTO.setIdTramite(detalleEmbargoTO.getIdTramite());
                                    pdfTO.setValue("[*operacion*]", "Anotaci\u00f3n de Embargo");
                                    pdfTO.setTypeValue("Anotaci\u00f3n de Embargo");
                                    pdfTO.setValue("[*title*]", "ANOTACI\u00d3N DE EMBARGO DE LA");
                                    pdfTO.setHtml("[*fechaCert*]", "");
                                    break;
                                case 17:
                                    mx.gob.se.rug.boleta.to.DetalleTO detalleDEmbargoTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                                    detalleDEmbargoTO.setIdTramite(boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteNuevo));
                                    detalleDEmbargoTO.setIdTipoTramite(idTipoTramiteRest);
                                    pdfTO = boletaServiceImpl.getBoletaModificacion(detalleDEmbargoTO);
                                    pdfTO.setIdTramite(detalleDEmbargoTO.getIdTramite());
                                    pdfTO.setValue("[*operacion*]", "Anotaci\u00f3n de Levantado de Embargo");
                                    pdfTO.setTypeValue("Anotaci\u00f3n de Levantado de Embargo");
                                    pdfTO.setValue("[*title*]", "ANOTACI\u00d3N DE LEVANTADO DE EMBARGO DE LA");
                                    pdfTO.setHtml("[*fechaCert*]", "");
                                    break;
                                case 8:// Transmision
                                    mx.gob.se.rug.boleta.to.DetalleTO detalleTransmisionTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                                    detalleTransmisionTO.setIdTramite(boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteNuevo));
                                    detalleTransmisionTO.setIdTipoTramite(idTipoTramiteRest);
                                    pdfTO = boletaServiceImpl.getBoletaTransmision(detalleTransmisionTO);
                                    pdfTO.setIdTramite(detalleTransmisionTO.getIdTramite());
                                    pdfTO.setValue("[*tipoTramite*]", "Transmisi�n");
                                    break;
                                case 9:// Renovacion o reduccion de vigencia
                                    mx.gob.se.rug.boleta.to.DetalleTO detalleRenovacionTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                                    detalleRenovacionTO.setIdTramite(boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteNuevo));
                                    detalleRenovacionTO.setIdTipoTramite(idTipoTramiteRest);
                                    pdfTO = boletaServiceImpl.getBoletaRenovacion(detalleRenovacionTO);
                                    pdfTO.setIdTramite(detalleRenovacionTO.getIdTramite());
                                    pdfTO.setValue("[*operacion*]","Pr\u00f3rroga de vigencia");
                                    pdfTO.setTypeValue("Pr\u00f3rroga de vigencia");
                                    pdfTO.setHtml("[*fechaCert*]", "");
                                    break;
                                case 30:// Ejecucion
                                    mx.gob.se.rug.boleta.to.DetalleTO detalleEjecucionTO = new mx.gob.se.rug.boleta.to.DetalleTO();
                                    detalleEjecucionTO.setIdTramite(boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteNuevo));
                                    detalleEjecucionTO.setIdTipoTramite(idTipoTramiteRest);
                                    pdfTO = boletaServiceImpl.getBoletaEjecucion(detalleEjecucionTO);
                                    pdfTO.setIdTramite(detalleEjecucionTO.getIdTramite());
                                    pdfTO.setValue("[*operacion*]","Ejecuci\u00f3n");
                                    pdfTO.setTypeValue("Ejecuci\u00f3n");
                                    pdfTO.setHtml("[*fechaCert*]", "");
                                    break;
                                case 38: // Transmision despues de prorroga
                                    mx.gob.se.rug.boleta.to.DetalleTO detalleTransmisionTOs = new mx.gob.se.rug.boleta.to.DetalleTO();
                                    detalleTransmisionTOs.setIdTramite(boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteNuevo));
                                    detalleTransmisionTOs.setIdTipoTramite(idTipoTramiteRest);
                                    pdfTO = boletaServiceImpl.getBoletaTransmision(detalleTransmisionTOs);
                                    pdfTO.setIdTramite(detalleTransmisionTOs.getIdTramite());
                                    pdfTO.setValue("[*tipoTramite*]", "Transmisi�n");
                                    break;
                                case 10:// Anotacion sin garantia
                                    Integer idTramiteFinal = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteNuevo);
                                    pdfTOInfo = boletaServiceImpl.getAnotacionSNGarantia(idTramiteFinal);
                                    pdfTO.setHtml(boletaServices.getBoletaHtml());
                                    pdfTO.setHtml("[*boletaHtml*]", pdfTOInfo.getHtml());
                                    pdfTO.setIdTramite(idTramiteFinal);
                                    pdfTO.setValue("[*tipoTramite*]", "Anotaci�n");
                                    break;
                                case 28:// Anotacion sin garantia Modificacion
                                    Integer idTramiteFinal1 = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteNuevo);
                                    pdfTOInfo = boletaServiceImpl.getAnotacionSNGarantiaModificacion(idTramiteFinal1);
                                    pdfTO.setHtml(boletaServices.getBoletaHtml());
                                    pdfTO.setHtml("[*boletaHtml*]", pdfTOInfo.getHtml());
                                    pdfTO.setIdTramite(idTramiteFinal1);
                                    pdfTO.setValue("[*tipoTramite*]", "Anotaci�n Modificaci�n");
                                    break;
                                case 27:// Anotacion sin garantia CAncelacion
                                    Integer idTramiteFinal2 = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteNuevo);
                                    pdfTOInfo = boletaServiceImpl.getAnotacionSNGarantiaCancelacion(idTramiteFinal2);
                                    pdfTO.setHtml(boletaServices.getBoletaHtml());
                                    pdfTO.setHtml("[*boletaHtml*]", pdfTOInfo.getHtml());
                                    pdfTO.setIdTramite(idTramiteFinal2);
                                    pdfTO.setValue("[*tipoTramite*]", "Anotaci�n Cancelaci�n");
                                    break;
                                case 29:// Anotacion sin garantia
                                    Integer idTramiteFinal3 = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteNuevo);
                                    pdfTOInfo = boletaServiceImpl.getAnotacionSNGarantiaRectificacion(idTramiteFinal3);
                                    pdfTO.setHtml(boletaServices.getBoletaHtml());
                                    pdfTO.setHtml("[*boletaHtml*]", pdfTOInfo.getHtml());
                                    pdfTO.setIdTramite(idTramiteFinal3);
                                    pdfTO.setValue("[*tipoTramite*]", "Anotaci�n Rectificaci�n");
                                    break;
                                case 26:// Anotacion sin garantia Renovacion
                                    Integer idTramiteFinal4 = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteNuevo);
                                    pdfTOInfo = boletaServiceImpl.getAnotacionSNGarantiaRenovacion(idTramiteFinal4);
                                    pdfTO.setHtml(boletaServices.getBoletaHtml());
                                    pdfTO.setHtml("[*boletaHtml*]", pdfTOInfo.getHtml());
                                    pdfTO.setIdTramite(idTramiteFinal4);
                                    pdfTO.setValue("[*tipoTramite*]", "Anotaci�n Renovaci�n o Reducci�n de Vigencia");

                                    break;
                                case 18:// Firma masiva
                                    System.out.println("PARA CREAR ZIP 1");
                                    InscripcionService inscripcionServiceV = new InscripcionServiceImpl();
                                    int idEstatus = new FirmaMasivaDAO().getEstatusByTramiteTemporal(idTramiteNuevo);
                                    MasivaDAO masivaDAO = new MasivaDAO();
                                    setIdTipoTramiteMasiva(masivaDAO.getIdTipoTramiteMasiva(idTramiteNuevo));
                                    switch (idEstatus) {
                                        case 3:
                                            Integer idTramiteFirma = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteNuevo);
                                            /**
                                            * lista de tramites *
                                            */
                                            List<Integer> tramitesMasivos = new ArrayList<Integer>();
                                            tramitesMasivos = masivaDAO.getIdTramitesMasivos(idTramiteNuevo);

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
                                            archivoN.setDescripcion("Archivo de resultado de firma masiva del tramite :" + idTramiteNuevo);
                                            archivoN.setIdUsuario(idUsuario);
                                            archivoN.setNombreArchivo("cmResnuevo");
                                            archivoN.setTipoArchivo("xml");
                                            setIdArchivoRes(inscripcionService.insertArchivo(archivoN).getResIntPl());
                                            regresa = "firmaMasiva";
                                        break;
                                        case 5:
                                        regresa = "errpendiente";
                                        // setMessageKey("La boleta se esta generando se notificara a su correo con un mail cuando este lista");
                                        // setMessageError("No existe la boleta");
                                        throw new PendienteException();
                                    
                                        case 8:
                                    regresa = "errpendiente";
                                    // setMessageKey(
                                    //         "La boleta se esta generando se notificara a su correo con un mail cuando este lista");
                                    // setMessageError("No existe la boleta");
                                    throw new PendienteException();
                                        default:
                                                
                                        // MyLogger.Logger.log(Level.INFO, "El tramite no tiene estatus");
                                        // regresa = "errtramite";
                                        // setMessageKey("El tramite no tiene estatus favor de verificar e intentarlo nuevamente");
                                        // setMessageError("No existe el tramite");
                                        throw new PendienteException();
                                    }
                                break;
                            }

                            if (pdfTO.getIdTramite() != null) {
                                pdfTO.setValue("[*idTramite*]", pdfTO.getIdTramite().toString());
                                pdfTO.setValue("[*GMTExplica*]", "");

                            }
                            if (!regresa.equals("firmaMasiva")) {
                                regresa = "SUCCESS";
                            }

                            /**** FIRMA DE DOCUMENTOS  */

                            if (idTipoTramiteRest == 1) {

                                PdfWriter writer = new PdfWriter(os);
                                PdfDocument pdf = new PdfDocument(writer);
                                try {
                                    Document doc = HtmlConverter.convertToDocument(pdfTO.getHtml(), pdf, null);
                                    doc.close();
                                    file = os.toByteArray();
                                    pdfTO.setFile(file);
                                    ResponseBuilder resp =  Response.ok(pdfTO.getFile());
                                    resp.header("Content-Disposition", "attachment; filename=descarga.pdf");
                                    return resp.build();
                                    // showPdf(pdfTO, resp, "Consulta",0);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            else{
                                try {
                                    if (pdfTO != null) {
                                        if (pdfTO.getMassive() != "False") {
                                            if (pdfTO.getIdTramite() == null) {
                                                pdfTO.setIdTramite(idTramite);
                                            }
                                            if (idTramite != null) {
                                                pdfTO.setIdTramite(idTramite);
                                            }            
                                            String filePathToBeServed = Constants.getParamValue(Constants.SIGN_ZIP_URL);
                                            Date date = new Date();
                                            DateFormat datePDF = new SimpleDateFormat("dd-MM-yyyy");
                                            DateFormat timePDF = new SimpleDateFormat("HH_mm_ss");

                                            String PDFIndex = datePDF.format(date);
                                            String PDFtime = timePDF.format(date);
                                            File directory = new File(filePathToBeServed);
                                            directory.mkdir();
                                            recursiveDelete(new File(filePathToBeServed));
                                            recursiveDelete(new File(filePathToBeServed +"/boleta_zip/"));

                                            for (int iteracionB = 0; iteracionB < pdfTO.getHtmlList().size(); iteracionB++) {
                                                
                                                System.out.println("PARA CREAR ZIP 2");
                                                byte filepdf[] = null;
                                                byte filesSignature[] = null;
                                                ByteArrayOutputStream ospdf = new ByteArrayOutputStream();
                                                pdfTO.setKey("" + pdfTO.getIdTramite() + Random.generateRandom(100000));
                                                PdfWriter writer = new PdfWriter(ospdf);
                                                ConverterProperties converterProperties = new ConverterProperties();
                                                PdfDocument pdf = new PdfDocument(writer);
                                                PageXofY footerHandler = new PageXofY(pdfTO.getKey());
                                                pdf.addEventHandler(PdfDocumentEvent.START_PAGE, footerHandler);
                                                Document doc = HtmlConverter.convertToDocument(pdfTO.getHtmlList().get(iteracionB), pdf, converterProperties);
                                                doc.close();

                                                filepdf = ospdf.toByteArray();

                                                String retorno = sendPDF(TimeStampFile(), true, file, idUsuario.toString());
                                                while (true) {
                                                    if(verifyFiles(retorno) == 1){
                                                        filesSignature = getBytesFile(retorno);
                                                        pdfTO.setFile(filesSignature);
                                                        break;
                                                    }	
                                                }

                                                try {
                                                    String fileName = "Boleta_" + iteracionB + "_" + PDFtime + ".pdf";
                                                    String path = filePathToBeServed+"/" + fileName;
                                                    FileOutputStream FOS = new FileOutputStream(path);
                                                    FOS.write(filesSignature);
                                                    FOS.close();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                } 
                                            }

                                            String pathOutputZip = Constants.getParamValue(Constants.SIGN_PDF_URL);
                                            try {
                                                zipFolder(filePathToBeServed, pathOutputZip+"/boleta_zip/"+PDFIndex+"_"+PDFtime+".zip");
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }

                                            File zipFile = new File(pathOutputZip+"/boleta_zip/"+PDFIndex+"_"+PDFtime+".zip");
                                            // resp.setContentType("application/zip");
                                            // resp.addHeader("Content-Disposition", "attachment; filename=" + ("pdf_"+PDFIndex+"_"+PDFtime+".zip"));
                                            // resp.setContentLength((int) zipFile.length());

                                            // try {
                                            //     FileInputStream fileInputStream = new FileInputStream(zipFile);
                                            //     // OutputStream responseOutputStream = resp.getOutputStream();
                                            //     int bytes;
                                            //     while ((bytes = fileInputStream.read()) != -1) {
                                            //         responseOutputStream.write(bytes);
                                            //     }
                                            // } catch (IOException e) {
                                            //     e.printStackTrace();
                                            // }

                                            BoletaDAO boleta = new BoletaDAO();
                                            // UsuarioTO usuario = (UsuarioTO) idUsuario;
                                            // boleta.insertBoletaPdf(pdfTO, usuario);
                                        }
                                        else{
                                            byte filesSignature[] = null;
                                            if (pdfTO.getIdTramite() == null) {
                                                pdfTO.setIdTramite(idTramite);
                                            }
                                            if (idTramite != null) {
                                                pdfTO.setIdTramite(idTramite);
                                            }
                                            pdfTO.setKey("" + pdfTO.getIdTramite() + Random.generateRandom(100000));
                                            PdfWriter writer = new PdfWriter(os);
                                            ConverterProperties converterProperties = new ConverterProperties();
                                            PdfDocument pdf = new PdfDocument(writer);
                                            PageXofY footerHandler = new PageXofY(pdfTO.getKey());  
                                            pdf.addEventHandler(PdfDocumentEvent.START_PAGE, footerHandler);
                                            pdf.setDefaultPageSize(PageSize.A3);
                                            Document doc = HtmlConverter.convertToDocument(pdfTO.getHtml(), pdf, converterProperties);
                                            doc.close();
                                            file = os.toByteArray();
                                            // BoletaDAO boleta = new BoletaDAO();
                                            // boleta.insertBoletaPdf(pdfTO, usuario);

                                            String archivoNombre = "Consulta";
                                            Integer idGarantiaTO = 0;
                                            
                                            try {
                                                pdfTO.setFile(file);
                                                String retorno = sendPDF(TimeStampFile(), true, file, idUsuario.toString());
                                                while (true) {
                                                    System.out.println(verifyFiles(retorno));
                                                    if(verifyFiles(retorno) == 1){
                                                        filesSignature = getBytesFile(retorno);
                                                        pdfTO.setFile(filesSignature);
                                                        regresa =  retorno;
                                                        break;
                                                    }	
                                                }
                                                // showPdf(pdfTO, resp, archivoNombre,idGarantiaTO);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

        
                   
        
                    }
                    
        
        
                } catch (Exception e) {
                    e.printStackTrace();
                    return Response.serverError().build();
                }
            }
        
            return Response.ok(regresa).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ok("error").build();
        }

        
    }

    public String TimeStampFile()
    {
        Date date = new Date();
        DateFormat hourDateFormat = new SimpleDateFormat("yyyyMMdd-HH-mm-ss");
        String history = hourDateFormat.format(date);
        return history;
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
    

    private void showPdf(PdfTO pdfTO, HttpServletResponse resp, String name, Integer idGarantia) {
        try {
            if (idGarantia == null){

                resp.setContentType("application/pdf");
                resp.setCharacterEncoding("UTF-8");
                resp.setHeader("Content-Disposition", "attachment; filename="+ name  + ".pdf");
                OutputStream os = resp.getOutputStream();
                os.write(pdfTO.getFile());
                os.close();
            }else{
                resp.setContentType("application/pdf");
                resp.setCharacterEncoding("UTF-8");
                resp.setHeader("Content-Disposition", "attachment; filename="+ name + "-" + idGarantia  + ".pdf");
                OutputStream os = resp.getOutputStream();
                os.write(pdfTO.getFile());
                os.close();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void recursiveDelete(File file) {
        String[] myFiles;    
          if(file.isDirectory()){
              myFiles = file.list();
              for (int i=0; i<myFiles.length; i++) {
                  File myFile = new File(file, myFiles[i]); 
                  myFile.delete();
              }
           }
    }

    /**
     * @param email
     * @return
     */
    @Override
    public Long getIdUsuarioByEmail(String email) {
        return usuarioDao.getIdUsuarioByEmail(email).getIdPersona();
    }


    public Integer countPage(byte[] files) throws IOException {
        PDDocument doc = null;
        doc = PDDocument.load(files);
        return doc.getNumberOfPages();
    }


    private String sendPDF(String pIdGarantia, boolean local, byte[] files, String email) throws ClientProtocolException, IOException{
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SIGN_URL);
        String fileName = pIdGarantia;

        String pageNumber = Integer.toString(countPage(files));

        System.out.println("Cantidad " + pageNumber);

        Long idPersona = getIdUsuarioByEmail(email);

        ContentBody cd = new InputStreamBody(new ByteArrayInputStream(files), "files.pdf");
        MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart("file", cd);
        reqEntity.addPart("name", new StringBody(fileName));
        reqEntity.addPart("page", new StringBody(pageNumber));
        reqEntity.addPart("user", new StringBody(idPersona.toString()));
        reqEntity.addPart("email", new StringBody(email));

                
        httpPost.setEntity(reqEntity);


        HttpResponse response = httpclient.execute(httpPost);
        String data = new BasicResponseHandler().handleResponse(response);
        return fileName;

    }


    private byte[] getBytesFile(String pIdGarantia) throws ClientProtocolException, IOException{
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SIGN_BYTES);
        String fileName = pIdGarantia;


        MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart("garantia", new StringBody(fileName));
        httpPost.setEntity(reqEntity);

        HttpResponse response = httpclient.execute(httpPost);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        response.getEntity().writeTo(baos);
        byte[] bytes = baos.toByteArray();
        return bytes;

    }
    private Response getBytesFileDownload(String pIdGarantia) throws ClientProtocolException, IOException{
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SIGN_BYTES);
        String fileName = pIdGarantia + ".pdf";


        MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart("name", new StringBody(fileName));
        httpPost.setEntity(reqEntity);

        HttpResponse response = httpclient.execute(httpPost);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        response.getEntity().writeTo(baos);
        // byte[] bytes = baos.toByteArray();

        ByteArrayInputStream inStream = new ByteArrayInputStream(baos.toByteArray());

        ResponseBuilder resp = Response.ok((Object) inStream);
        resp.header("Content-Disposition", "attachment;filename=archivo.pdf");
        return resp.build();
    }

    

    private Integer verifyFiles(String pIdGarantia) throws ClientProtocolException, IOException{
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SIGN_VERIFY);
        String fileName = pIdGarantia;
        Integer very = 0;


        MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart("garantia", new StringBody(fileName));
        httpPost.setEntity(reqEntity);

        HttpResponse response = httpclient.execute(httpPost);
        String data = new BasicResponseHandler().handleResponse(response);
        System.out.println("Response " + data);
        if(data.equals("true"))
        {
            very = new Integer(1);
            return very;
        }else{
            very = new Integer(0);
            return very;

        }

    }


    static public void zipFolder(String srcFolder, String destZipFile) throws Exception {
        ZipOutputStream zip = null;
        FileOutputStream fileWriter = null;
        fileWriter = new FileOutputStream(destZipFile);
        zip = new ZipOutputStream(fileWriter);
        addFolderToZip("", srcFolder, zip);
        zip.flush();
        zip.close();
    }


    static private void addFileToZip(String path, String srcFile, ZipOutputStream zip)
        throws Exception {
        File folder = new File(srcFile);
        if (folder.isDirectory()) {
            addFolderToZip(path, srcFile, zip);
        } else {
            byte[] buf = new byte[1024];
            int len;
            FileInputStream in = new FileInputStream(srcFile);
            zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
            while ((len = in.read(buf)) > 0) {
                zip.write(buf, 0, len);
            }
        }
    }

    static private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip)throws Exception {
        File folder = new File(srcFolder);
        for (String fileName : folder.list()) {
            if (path.equals("")) {
                addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
            } else {
                addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip);
            }
        }
    }





}
