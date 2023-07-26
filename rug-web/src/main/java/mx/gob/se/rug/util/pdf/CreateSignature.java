
package mx.gob.se.rug.util.pdf;

import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.signatures.PdfSigner;
import com.itextpdf.signatures.IExternalSignatureContainer;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import com.itextpdf.signatures.PrivateKeySignature;
import com.itextpdf.signatures.BouncyCastleDigest;
import com.itextpdf.signatures.DigestAlgorithms;
import com.itextpdf.signatures.PdfPKCS7;



public class CreateSignature {
    public void createSignatureNew(String src, String dest, String fieldName, PrivateKey pk, Certificate[] chain)
            throws IOException, GeneralSecurityException {
        PdfReader reader = new PdfReader(src);
        try (FileOutputStream os = new FileOutputStream(dest)) {
            PdfSigner signer = new PdfSigner(reader, os, new StampingProperties());

            IExternalSignatureContainer external = new MyExternalSignatureContainer(pk, chain);

            // Signs a PDF where space was already reserved. The field must cover the whole document.
            signer.signDeferred(signer.getDocument(), fieldName, os, external);
        }
    }
    
    class MyExternalSignatureContainer implements IExternalSignatureContainer {

        protected PrivateKey pk;
        protected Certificate[] chain;

        public MyExternalSignatureContainer(PrivateKey pk, Certificate[] chain) {
            this.pk = pk;
            this.chain = chain;
        }

        public byte[] sign(InputStream is) throws GeneralSecurityException {
            try {
                PrivateKeySignature signature = new PrivateKeySignature(pk, "SHA256", "BC");
                String hashAlgorithm = signature.getHashAlgorithm();
                BouncyCastleDigest digest = new BouncyCastleDigest();

                PdfPKCS7 sgn = new PdfPKCS7(null, chain, hashAlgorithm, null, digest, false);
                byte hash[] = DigestAlgorithms.digest(is, digest.getMessageDigest(hashAlgorithm));
                byte[] sh = sgn.getAuthenticatedAttributeBytes(hash, PdfSigner.CryptoStandard.CMS, null, null);
                byte[] extSignature = signature.sign(sh);
                sgn.setExternalDigest(extSignature, null, signature.getEncryptionAlgorithm());

                return sgn.getEncodedPKCS7(hash, PdfSigner.CryptoStandard.CMS, null, null, null);
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }

        public void modifySigningDictionary(PdfDictionary signDic) {
        }
    }
}
