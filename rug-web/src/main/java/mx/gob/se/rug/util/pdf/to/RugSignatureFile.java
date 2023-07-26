package mx.gob.se.rug.util.pdf.to;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.entity.mime.content.InputStreamBody;

import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import gt.gob.rgm.captcha.utils.Random;
import mx.gob.se.rug.boleta.dao.BoletaDAO;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.pdf.PageXofY;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import org.apache.pdfbox.pdmodel.PDDocument;


public class RugSignatureFile extends HttpServlet {
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
        signatureFiles(req, resp);

    }


//    public static final String SIGN_URL = 	 "http://128.5.101.19:8080/api/signature";
//    public static final String SIGN_BYTES =  "http://128.5.101.19:8080/api/toBytes";
//    public static final String SIGN_VERIFY = "http://128.5.101.19:8080/api/verifyFile";

    public static final String SIGN_URL = 	 "https://operaciones.rgm.gob.gt/api/signature";
	public static final String SIGN_BYTES =  "https://operaciones.rgm.gob.gt/api/toBytes";
	public static final String SIGN_VERIFY = "https://operaciones.rgm.gob.gt/api/verifyFile";

    private void signatureFiles(HttpServletRequest req, HttpServletResponse resp){
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
                showPdf(pdfTO, resp, "Consulta",0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Integer idTramite = (Integer) session.getAttribute(Constants.ID_TRAMITE_NUEVO);
            UsuarioTO usuario = (UsuarioTO) session.getAttribute(Constants.USUARIO);
            PdfTO pdfTO = (PdfTO) session.getAttribute("pdfTO");
            try {
                if (pdfTO != null) {
                    if (pdfTO.getMassive() != "False") {
                        if (pdfTO.getIdTramite() == null) {
                            pdfTO.setIdTramite(idTramite);
                        }
                        if (idTramite != null) {
                            pdfTO.setIdTramite(idTramite);
                        }
                        String signEnabled = Constants.getParamValue(Constants.SIGN_ENABLED);
                        String signDev = Constants.getParamValue(Constants.SIGN_LOCAL);

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

                            String retorno = sendPDF(TimeStampFile(), true, file,usuario.getPersona().getCorreoElectronico(), usuario.getPersona().getIdPersona());
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
                            e.printStackTrace();
                        }

                        BoletaDAO boleta = new BoletaDAO();
                        boleta.insertBoletaPdf(pdfTO, usuario);
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
                        BoletaDAO boleta = new BoletaDAO();
                        boleta.insertBoletaPdf(pdfTO, usuario);

                        String archivoNombre = "Consulta";
                        Integer idGarantiaTO = 0;
                        
                        try {
                            pdfTO.setFile(file);
                            System.out.println(" usuario firma File" + usuario.getPersona().getCorreoElectronico() + " otro " + pdfTO.getIdGarantiaTO());
                            String retorno = sendPDF(TimeStampFile(), true, file, usuario.getPersona().getCorreoElectronico(), usuario.getPersona().getIdPersona());
                            while (true) {
                                if(verifyFiles(retorno) == 1){
                                    filesSignature = getBytesFile(retorno);
                                    pdfTO.setFile(filesSignature);
                                    break;
                                }	
                            }
                            showPdf(pdfTO, resp, archivoNombre,idGarantiaTO);
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

    public String TimeStampFile()
    {
        Date date = new Date();
        DateFormat hourDateFormat = new SimpleDateFormat("yyyyMMdd-HH-mm-ss");
        String history = hourDateFormat.format(date);
        return history;
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

    public Integer countPage(byte[] files) throws IOException {
        PDDocument doc = null;
        doc = PDDocument.load(files);
        return doc.getNumberOfPages();
    }


    private String sendPDF(String pIdGarantia, boolean local, byte[] files, String email, Integer idUsuario) throws ClientProtocolException, IOException, NoSuchAlgorithmException {



        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SIGN_URL);
        String fileName = pIdGarantia;
        System.out.println("Garantia " + fileName);

        String pageNumber = Integer.toString(countPage(files));

        System.out.println("Cantidad " + pageNumber);


        ContentBody cd = new InputStreamBody(new ByteArrayInputStream(files), "files.pdf");
        MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart("file", cd);
        reqEntity.addPart("name", new StringBody(fileName));
        reqEntity.addPart("page", new StringBody(pageNumber));
        reqEntity.addPart("user", new StringBody(idUsuario.toString()));
        reqEntity.addPart("email", new StringBody(email));

                
        httpPost.setEntity(reqEntity);


        HttpResponse response = httpclient.execute(httpPost);
        String data = new BasicResponseHandler().handleResponse(response);
        System.out.println("Respuesta desde RUG " + data);
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
