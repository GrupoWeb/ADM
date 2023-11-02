package mx.gob.se.rug.util.pdf;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import mx.gob.se.rug.boleta.dao.BoletaDAO;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;
import mx.gob.se.rug.util.pdf.to.PdfTO;

//import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.SAXException;

import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfNumber;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.text.BadElementException;
//import com.itextpdf.text.Image;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.HorizontalAlignment;
//import com.itextpdf.text.pdf.BarcodeQRCode;
import com.lowagie.text.DocumentException;

import gt.gob.rgm.captcha.utils.Random;

import gt.gob.rgm.security.domain.SignatureInfo;
import gt.gob.rgm.security.service.DigitalSignatureServiceImp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.*;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import mx.gob.se.rug.util.pdf.CreateSignature;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;



public class PdfServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) {
        generatePdf(req, resp);

    }

    private void generatePdf(HttpServletRequest req, HttpServletResponse resp)  {

        System.out.println("Firma Antigua ");

        // Variables Firma
        String signText = "";
        String signImage = "";
        String signFile = "";
        String signPassword = "";
        String signLocation = "";
        String signLlx = "";
        String signLly = "";
        String signUrx = "";
        String signUry = "";
        String signPage = "";
        String signFieldname = "";
        String archivoNombre = "";
        Integer idGarantiaTO;
        HttpSession session = req.getSession(false);
        byte file[] = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        
        


        if (session.getAttribute("Consulta") != null && (Integer) session.getAttribute("Consulta") == 1) {
            PdfTO pdfTO = (PdfTO) session.getAttribute("pdfTO");
            
            PdfWriter writer = new PdfWriter(os);
            PdfDocument pdf = new PdfDocument(writer);
            try {
                Document doc = HtmlConverter.convertToDocument(pdfTO.getHtml(), pdf, null);
                doc.close();
                file = os.toByteArray();
                pdfTO.setFile(file);
                System.out.println("Garanti TO " + pdfTO.getIdGarantiaTO());
                showPdf(pdfTO, resp, "Consulta",0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Integer idTramite = (Integer) session.getAttribute(Constants.ID_TRAMITE_NUEVO);
            UsuarioTO usuario = (UsuarioTO) session.getAttribute(Constants.USUARIO);
            PdfTO pdfTO = (PdfTO) session.getAttribute("pdfTO");
            System.out.println("REtorno n " + pdfTO.getMassive() + " tramite n "+ idTramite + " usuario n "+ usuario);
            try {
                if (pdfTO != null) {
                    if (pdfTO.getMassive() != "False") {
                        DigitalSignatureServiceImp digitalSignatureSvc = new DigitalSignatureServiceImp();
                        SignatureInfo info = new SignatureInfo();
                        if (pdfTO.getIdTramite() == null) {
                            pdfTO.setIdTramite(idTramite);
                        }
                        if (idTramite != null) {
                            pdfTO.setIdTramite(idTramite);
                        }
                        String signEnabled = Constants.getParamValue(Constants.SIGN_ENABLED);
                        String signDev = Constants.getParamValue(Constants.SIGN_LOCAL);
                        
                        
                        
                        if (Boolean.valueOf(signEnabled)) {
                            if (Boolean.valueOf(signDev)) {
                                signText = Constants.getParamValue(Constants.SIGN_TEXT);
                                signImage = Constants.getParamValue(Constants.SIGN_IMAGE_LOCAL);
                                signFile = Constants.getParamValue(Constants.SIGN_FILE_LOCAL);
                                signPassword = Constants.getParamValue(Constants.SIGN_PASSWORD);
                                signLocation = Constants.getParamValue(Constants.SIGN_LOCATION);
                                signLlx = Constants.getParamValue(Constants.SIGN_LLX);
                                signLly = Constants.getParamValue(Constants.SIGN_LLY);
                                signUrx = Constants.getParamValue(Constants.SIGN_URX);
                                signUry = Constants.getParamValue(Constants.SIGN_URY);
                                signPage = Constants.getParamValue(Constants.SIGN_PAGE);
                                signFieldname = Constants.getParamValue(Constants.SIGN_FIELDNAME);
                            } else {
                                signText = Constants.getParamValue(Constants.SIGN_TEXT);
                                signImage = Constants.getParamValue(Constants.SIGN_IMAGE);
                                signFile = Constants.getParamValue(Constants.SIGN_FILE);
                                signPassword = Constants.getParamValue(Constants.SIGN_PASSWORD);
                                signLocation = Constants.getParamValue(Constants.SIGN_LOCATION);
                                signLlx = Constants.getParamValue(Constants.SIGN_LLX);
                                signLly = Constants.getParamValue(Constants.SIGN_LLY);
                                signUrx = Constants.getParamValue(Constants.SIGN_URX);
                                signUry = Constants.getParamValue(Constants.SIGN_URY);
                                signPage = Constants.getParamValue(Constants.SIGN_PAGE);
                                signFieldname = Constants.getParamValue(Constants.SIGN_FIELDNAME);
                            }

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
                            byte filepdf[] = null;
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
                            
                            
                            if (Boolean.valueOf(signEnabled)) {
                                info.setSignText(signText);
                                info.setGraphicSignature(signImage);
                                info.setKeyFile(signFile);
                                info.setKeyPassword(signPassword);
                                info.setLocation(signLocation);
                                info.setLlx(Integer.valueOf(signLlx));
                                info.setLly(Integer.valueOf(signLly));
                                info.setUrx(Integer.valueOf(signUrx));
                                info.setUry(Integer.valueOf(signUry));
                                info.setSignPage(Integer.valueOf(signPage));
                                info.setFieldName(signFieldname);
                                if (pdfTO.getTypeValue() == null) {
                                    info.setTypeDocument("Consulta");
                                } else {
                                    info.setTypeDocument(pdfTO.getTypeValue());
                                }
                                info.setReason("Tramite #");
                                info.setDocument(filepdf);

                                try {
                                    ByteArrayOutputStream signedOs = digitalSignatureSvc.signDocument(info);
                                    filepdf = signedOs.toByteArray();
                                } catch (GeneralSecurityException | com.itextpdf.text.DocumentException e) {
                                    e.printStackTrace();
                                }
                            }

                            try {
                                String fileName = "Boleta_" + iteracionB + "_" + PDFtime + ".pdf";
                                String path = filePathToBeServed+"/" + fileName;
                                System.out.println("path = " + path);
                                FileOutputStream FOS = new FileOutputStream(path);
                                FOS.write(filepdf);
                                FOS.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                        String pathOutputZip = Constants.getParamValue(Constants.SIGN_PDF_URL);
                        System.out.println("pathOutputZip = " + pathOutputZip);
                        System.out.println("filePathToBeServed = " + filePathToBeServed);
                        try {
                            zipFolder(filePathToBeServed, pathOutputZip+"/boleta_zip/"+PDFIndex+"_"+PDFtime+".zip");
                        } catch (Exception ex) {
                            System.out.println("Error = " + ex);
                        }
                        File zipFile = new File(pathOutputZip+"/boleta_zip/"+PDFIndex+"_"+PDFtime+".zip");
                        resp.setContentType("application/zip");
                        resp.addHeader("Content-Disposition", "attachment; filename=" + ("pdf_"+PDFIndex+"_"+PDFtime+".zip"));
                        resp.setContentLength((int) zipFile.length());
                        try {
                            FileInputStream fileInputStream = new FileInputStream(zipFile);
                            OutputStream responseOutputStream = resp.getOutputStream();
                            int bytes;
                            while ((bytes = fileInputStream.read()) != -1) {
                                responseOutputStream.write(bytes);
                            }
                        } catch (IOException e) {
                            System.out.println("err = " + e);
                        }
                        BoletaDAO boleta = new BoletaDAO();
                        boleta.insertBoletaPdf(pdfTO, usuario);
                    } 
                    else {
                        if (pdfTO.getIdTramite() == null) {
                            pdfTO.setIdTramite(idTramite);
                        }
                        pdfTO.setKey("" + pdfTO.getIdTramite() + Random.generateRandom(100000));
                        PdfWriter writer = new PdfWriter(os);
                        ConverterProperties converterProperties = new ConverterProperties();
                        PdfDocument pdf = new PdfDocument(writer);
                        
                        PageXofY footerHandler = new PageXofY(pdfTO.getKey());
                        
                                
                        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, footerHandler);
                    
                        
                       pdf.setDefaultPageSize(PageSize.A3);


                        System.out.println("Html Error " + pdfTO.getHtml());
                        Document doc = HtmlConverter.convertToDocument(pdfTO.getHtml(), pdf, converterProperties);
                        
                 
                        
                        if (idTramite != null) {
                            pdfTO.setIdTramite(idTramite);
                        }

                        doc.close();
                        file = os.toByteArray();
                        String signEnabled = Constants.getParamValue(Constants.SIGN_ENABLED);
                        String signDev = Constants.getParamValue(Constants.SIGN_LOCAL);
                        DigitalSignatureServiceImp digitalSignatureSvc = new DigitalSignatureServiceImp();
                        SignatureInfo info = new SignatureInfo();

                        try {
                            signText = Constants.getParamValue(Constants.SIGN_TEXT);
                            signImage = Constants.getParamValue(Constants.SIGN_IMAGE);
                            signFile = Constants.getParamValue(Constants.SIGN_FILE);
                            signPassword = Constants.getParamValue(Constants.SIGN_PASSWORD);
                            signLocation = Constants.getParamValue(Constants.SIGN_LOCATION);
                            signLlx = Constants.getParamValue(Constants.SIGN_LLX);
                            signLly = Constants.getParamValue(Constants.SIGN_LLY);
                            signUrx = Constants.getParamValue(Constants.SIGN_URX);
                            signUry = Constants.getParamValue(Constants.SIGN_URY);
                            signPage = Constants.getParamValue(Constants.SIGN_PAGE);
                            signFieldname = Constants.getParamValue(Constants.SIGN_FIELDNAME);

                            info.setSignText(signText);
                            info.setGraphicSignature(signImage);
                            info.setKeyFile(signFile);
                            info.setKeyPassword(signPassword);
                            info.setLocation(signLocation);
                            info.setLlx(Integer.valueOf(signLlx));
                            info.setLly(Integer.valueOf(signLly));
                            info.setUrx(Integer.valueOf(signUrx));
                            info.setUry(Integer.valueOf(signUry));
                            info.setSignPage(Integer.valueOf(signPage));
                            info.setFieldName(signFieldname);
                            if (pdfTO.getTypeValue() == null) {
                                info.setTypeDocument("Consulta");
                                archivoNombre = "Consulta";
                                idGarantiaTO = 0;
                            } else {
                                info.setTypeDocument(pdfTO.getTypeValue());
                                archivoNombre = pdfTO.getTypeValue();
                            }
                            System.out.println("Garanti TO " + pdfTO.getIdGarantiaTO());
                            idGarantiaTO= pdfTO.getIdGarantiaTO();
                            info.setReason("Tramite #");
                            info.setDocument(file);

                            
                             //corellana desactivar para el ambiente local, ya que no tengo el p12 
                            
                            if (Boolean.valueOf(signEnabled)) {
                                try {
                                    ByteArrayOutputStream signedOs = digitalSignatureSvc.signDocument(info);
                                    file = signedOs.toByteArray();
                                } catch (GeneralSecurityException | com.itextpdf.text.DocumentException e) {
                                    MyLogger.Logger.log(Level.INFO, " no tiene parametros....." + e);
                                    e.printStackTrace();
                                }
                            }

                            pdfTO.setFile(file);
                            BoletaDAO boleta = new BoletaDAO();
                            boleta.insertBoletaPdf(pdfTO, usuario);
                            
                            showPdf(pdfTO, resp, archivoNombre,idGarantiaTO);

                        } catch (Exception e) {
                            MyLogger.Logger.log(Level.INFO, " Error en exception" + e);
                            signText = Constants.getParamValue(Constants.SIGN_TEXT);
                            signImage = Constants.getParamValue(Constants.SIGN_IMAGE);
                            // signImage = "C:/certificado_RGM/firma.png";
					        // signFile = "C:/certificado_RGM/rgm.p12";
                            signText = Constants.getParamValue(Constants.SIGN_TEXT);
                            signPassword = Constants.getParamValue(Constants.SIGN_PASSWORD);
                            signLocation = Constants.getParamValue(Constants.SIGN_LOCATION);
                            signLlx = Constants.getParamValue(Constants.SIGN_LLX);
                            signLly = Constants.getParamValue(Constants.SIGN_LLY);
                            signUrx = Constants.getParamValue(Constants.SIGN_URX);
                            signUry = Constants.getParamValue(Constants.SIGN_URY);
                            signPage = Constants.getParamValue(Constants.SIGN_PAGE);
                            signFieldname = Constants.getParamValue(Constants.SIGN_FIELDNAME);

                            info.setSignText(signText);
                            info.setGraphicSignature(signImage);
                            info.setKeyFile(signFile);
                            info.setKeyPassword(signPassword);
                            info.setLocation(signLocation);
                            info.setLlx(Integer.valueOf(signLlx));
                            info.setLly(Integer.valueOf(signLly));
                            info.setUrx(Integer.valueOf(signUrx));
                            info.setUry(Integer.valueOf(signUry));
                            info.setSignPage(Integer.valueOf(signPage));
                            info.setFieldName(signFieldname);
                            if (pdfTO.getTypeValue() == null) {
                                info.setTypeDocument("Consulta");
                                archivoNombre = "Consulta";
                                idGarantiaTO = 0;
                            } else {
                                info.setTypeDocument(pdfTO.getTypeValue());
                                archivoNombre = pdfTO.getTypeValue();
                            }
                            System.out.println("Garanti TO " + pdfTO.getIdGarantiaTO());
                            idGarantiaTO= pdfTO.getIdGarantiaTO();
                            info.setReason("Tramite #");
                            info.setDocument(file);

                            try {
                                ByteArrayOutputStream signedOs = digitalSignatureSvc.signDocument(info);
                                file = signedOs.toByteArray();
                            } catch (GeneralSecurityException | com.itextpdf.text.DocumentException ee) {
                                MyLogger.Logger.log(Level.INFO, " no tiene parametros....." + ee);
                                e.printStackTrace();
                            }
                            System.out.println("Tramite Firma" + pdfTO.getIdTramite());

                            pdfTO.setFile(file);
                            BoletaDAO boleta = new BoletaDAO();
                            boleta.insertBoletaPdf(pdfTO, usuario);
                            
                            showPdf(pdfTO, resp, archivoNombre,idGarantiaTO);
                        }
                        // if (Boolean.valueOf(signEnabled)) {
                        //     if (Boolean.valueOf(signDev)) {
                        //         signText = Constants.getParamValue(Constants.SIGN_TEXT);
                        //         signImage = Constants.getParamValue(Constants.SIGN_IMAGE_LOCAL);
                        //         signFile = Constants.getParamValue(Constants.SIGN_FILE_LOCAL);
                        //         signPassword = Constants.getParamValue(Constants.SIGN_PASSWORD);
                        //         signLocation = Constants.getParamValue(Constants.SIGN_LOCATION);
                        //         signLlx = Constants.getParamValue(Constants.SIGN_LLX);
                        //         signLly = Constants.getParamValue(Constants.SIGN_LLY);
                        //         signUrx = Constants.getParamValue(Constants.SIGN_URX);
                        //         signUry = Constants.getParamValue(Constants.SIGN_URY);
                        //         signPage = Constants.getParamValue(Constants.SIGN_PAGE);
                        //         signFieldname = Constants.getParamValue(Constants.SIGN_FIELDNAME);
                        //     } else {
                        //         signText = Constants.getParamValue(Constants.SIGN_TEXT);
                        //         signImage = Constants.getParamValue(Constants.SIGN_IMAGE);
                        //         signFile = Constants.getParamValue(Constants.SIGN_FILE);
                        //         signPassword = Constants.getParamValue(Constants.SIGN_PASSWORD);
                        //         signLocation = Constants.getParamValue(Constants.SIGN_LOCATION);
                        //         signLlx = Constants.getParamValue(Constants.SIGN_LLX);
                        //         signLly = Constants.getParamValue(Constants.SIGN_LLY);
                        //         signUrx = Constants.getParamValue(Constants.SIGN_URX);
                        //         signUry = Constants.getParamValue(Constants.SIGN_URY);
                        //         signPage = Constants.getParamValue(Constants.SIGN_PAGE);
                        //         signFieldname = Constants.getParamValue(Constants.SIGN_FIELDNAME);
                        //     }
                        // }
                        
                    }
                }
            } catch (IOException e) {
                MyLogger.Logger.log(Level.WARNING, "HTML_NO_PARSE::::::" + pdfTO.getHtml());
                e.printStackTrace();
            }
        }
    }

    public String getUrl(String url) {
        String urlBase = System.getProperty(Constants.URL_WEB_SERVICE_FRIMA_TRAMITE);
        String uri[] = urlBase.split("/");
        StringBuffer sb = new StringBuffer();
        sb.append(uri[0]);
        sb.append("//");
        sb.append(uri[2]);
        return sb.toString() + url;

    }
    
    private void showPdf(PdfTO pdfTO, HttpServletResponse resp, String name, Integer idGarantia) {
        try {
            if (idGarantia == null){
                //            \"Boleta2.pdf\"
                resp.setContentType("application/pdf");
                resp.setCharacterEncoding("UTF-8");
                resp.setHeader("Content-Disposition", "attachment; filename="+ name  + ".pdf");
                OutputStream os = resp.getOutputStream();
                os.write(pdfTO.getFile());
                os.close();
            }else{
                //            \"Boleta2.pdf\"
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

    static private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip)
            throws Exception {
        File folder = new File(srcFolder);

        for (String fileName : folder.list()) {
            if (path.equals("")) {
                addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
            } else {
                addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip);
            }
        }
    }
    
    public static void recursiveDelete(File file) {
        String[] myFiles;    
          if(file.isDirectory()){
              myFiles = file.list();
              for (int i=0; i<myFiles.length; i++) {
                  File myFile = new File(file, myFiles[i]); 
                  myFile.delete();
                  System.out.println("Deleted " + myFile);
              }
           }
    }
}