/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.rgm.adm.util;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.CrlClient;
import com.itextpdf.text.pdf.security.CrlClientOnline;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.PrivateKeySignature;
import gt.gob.rgm.adm.util.signatureInfo;
//import gt.gob.rgm.security.domain.SignatureInfo;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
//import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author jjolon
 */
public class signaturePDF {
    
        
    
    public ByteArrayOutputStream signDocument(signatureInfo info) throws GeneralSecurityException, IOException, DocumentException {

        // info.getDocument() donde esta el documento

            
        
            BouncyCastleProvider provider = new BouncyCastleProvider();
            Security.addProvider(provider);
            KeyStore ks = KeyStore.getInstance("pkcs12", provider.getName());
            ks.load(new FileInputStream(info.getKeyFile()), info.getKeyPassword().toCharArray());
            String alias = (String) ks.aliases().nextElement();
            PrivateKey pk = (PrivateKey) ks.getKey(alias, info.getKeyPassword().toCharArray());
            Certificate[] chain = ks.getCertificateChain(alias);
            
            // X509Certificate oPublicCertificate = (X509Certificate) chain[0];
            
            PdfReader reader = new PdfReader(new ByteArrayInputStream(info.getDocument()));

            

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            PdfStamper stamper = PdfStamper.createSignature(reader, os, '\000',null,true);

            // System.out.println("Documento " + new ByteArrayInputStream(info.getDocument()));
            
            PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
            
            Date fechaFirma=new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fechaFirma);
            appearance.setSignDate(calendar);
            SimpleDateFormat dateformatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss Z");
            String fecha = dateformatter.format(fechaFirma);
            String URL = "https://www.rgm.gob.gt";
            
            
            appearance.setReason(info.getReason());
            appearance.setLocation(info.getLocation());
            int signingPage = info.getSignPage() == 0 ? reader.getNumberOfPages() : info.getSignPage();
            appearance.setVisibleSignature(new Rectangle(info.getLlx(), info.getLly(), info.getUrx(), info.getUry()), signingPage, info.getFieldName());
            // appearance.setVisibleSignature(new Rectangle(info.getLlx(), info.getLly(), info.getUrx(), info.getUry()), iteration, info.getFieldName());
            appearance.setLayer2Text(info.getSignText() + "\n" + "FECHA: " + fecha + " \nTipo de Operación: " + info.getTypeDocument() + " \n" + URL);
            appearance.setLayer2Font(new Font(Font.FontFamily.TIMES_ROMAN, 12));
            appearance.setSignatureGraphic(Image.getInstance(info.getGraphicSignature()));
            appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC_AND_DESCRIPTION);
            appearance.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_NO_CHANGES_ALLOWED);
            
            ExternalDigest digest = new BouncyCastleDigest();
            ExternalSignature signature = new PrivateKeySignature(pk, DigestAlgorithms.RIPEMD160, provider.getName());
            MakeSignature.signDetached(appearance, digest, signature, chain, null, null, null, 0, MakeSignature.CryptoStandard.CADES);
            


        return os;

//        Date date = new Date();
//        DateFormat fullDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        String URL = "https://www.rgm.gob.gt";
//
//        BouncyCastleProvider provider = new BouncyCastleProvider();
//        Security.addProvider(provider);
//        System.out.println("provider " + provider.getName());
//        KeyStore ks = KeyStore.getInstance("pkcs12", provider.getName());
//
//        ks.load(new FileInputStream(info.getKeyFile()), info.getKeyPassword().toCharArray());
//        String alias = (String) ks.aliases().nextElement();
//
//        PrivateKey pk = (PrivateKey) ks.getKey(alias, info.getKeyPassword().toCharArray());
//        Certificate[] chain = ks.getCertificateChain(alias);
//
////         CRL URL SING
////        for (int iteration = 0; iteration < chain.length; iteration++) {
////            X509Certificate cert = (X509Certificate) chain[iteration];
////            //System.out.println("data crl" + String.format("[%s] %s", iteration, cert.getSubjectDN()));
////            //System.out.println(CertificateUtil.getCRLURL(cert));
////            List<CrlClient> crlList = new ArrayList<CrlClient>();
////            crlList.add(new CrlClientOnline(chain));
////        }
//
////         CrlClient crlClient = new CrlClientOnline("http://www.certicamara.com/repositoriorevocaciones/ac_subordinada_certicamara_2014.crl?crl=crl");
////         List<CrlClient> crlList = new ArrayList<CrlClient>();
////         crlList.add(crlClient);
//        /**
//         * ********************
//         */
//        PdfReader reader = new PdfReader(new ByteArrayInputStream(info.getDocument()));
//        
//        
//        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');
//
//        PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
//        appearance.setReason(info.getReason());
//        appearance.setLocation(info.getLocation());
//        int signingPage = info.getSignPage() == 0 ? reader.getNumberOfPages() : info.getSignPage();
//        appearance.setVisibleSignature(new Rectangle(info.getLlx(), info.getLly(), info.getUrx(), info.getUry()), signingPage, info.getFieldName());
//        // appearance.setVisibleSignature(new Rectangle(info.getLlx(), info.getLly(), info.getUrx(), info.getUry()), iteration, info.getFieldName());
//        appearance.setLayer2Text(info.getSignText() + "\n" + "FECHA: " + fullDate.format(date) + " \nTipo de Operación: " + info.getTypeDocument() + " \n" + URL);
//        appearance.setLayer2Font(new Font(Font.FontFamily.TIMES_ROMAN, 12));
//        appearance.setSignatureGraphic(Image.getInstance(info.getGraphicSignature()));
//        appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC_AND_DESCRIPTION);
//        appearance.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_NO_CHANGES_ALLOWED);
//        ExternalDigest digest = new BouncyCastleDigest();
////        ExternalSignature signature = new PrivateKeySignature(pk, DigestAlgorithms.RIPEMD160, provider.getName());
//        ExternalSignature signature = new PrivateKeySignature(pk, DigestAlgorithms.SHA512, provider.getName());
//        MakeSignature.signDetached(appearance, digest, signature, chain, null, null, null, 0, MakeSignature.CryptoStandard.CADES);
//        
//        
//      
//        return os;
    }
    
}
