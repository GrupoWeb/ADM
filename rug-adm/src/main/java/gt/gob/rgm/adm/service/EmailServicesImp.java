package gt.gob.rgm.adm.service;

import gt.gob.rgm.adm.util.Constants;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.ejb.Stateless;
import java.io.IOException;

@Stateless
public class EmailServicesImp implements EmailServices{


    /**
     * @param Url - Path for URL
     * @throws IOException - Excepción del Método
     */
    @Override
    public void sendEmailBitacora(String Url) throws IOException{
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(Url + Constants.SEND_API + Constants.SEND_MAIL);
        httpclient.execute(httpGet);
    }

    /**
     * @param subject - Asunto del Email
     * @param message - Mensaje del Email
     * @param url - Path del Email
     * @param destino - Destino del Email
     * @param cc - Copia del Email
     * @param cco - Copia Oculta del Email
     * @throws IOException - Excepción del Método
     */
    @Override
    public void RechazoEmail(String subject, String message, String url, String destino, String cc, String cco) throws IOException {
        String tipo = "1", idmail = "1";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url + Constants.SEND_API + Constants.SEND_BITACORA);
        MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart("idTipo", new StringBody(tipo));
        reqEntity.addPart("idMail", new StringBody(idmail));
        reqEntity.addPart("destino", new StringBody(destino));
        reqEntity.addPart("cc", new StringBody(cc));
        reqEntity.addPart("cco", new StringBody(cco));
        reqEntity.addPart("asunto", new StringBody(subject));
        reqEntity.addPart("mensaje", new StringBody(message));

        httpPost.setEntity(reqEntity);

        httpclient.execute(httpPost);
        sendEmailBitacora(url);
    }

    /**
     * @param subject - Asunto del Email
     * @param message - Mensaje del Email
     * @param url - Path del Email
     * @param destino - Destino del Email
     * @param cc - Copia del Email
     * @param cco - Copia Oculta del Email
     * @throws IOException - Excepción del Método
     */
    @Override
    public void AprobacionEmail(String subject, String message, String url, String destino, String cc, String cco) throws IOException {
        String tipo = "1", idmail = "1";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url + Constants.SEND_API + Constants.SEND_BITACORA);
        MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart("idTipo", new StringBody(tipo));
        reqEntity.addPart("idMail", new StringBody(idmail));
        reqEntity.addPart("destino", new StringBody(destino));
        reqEntity.addPart("cc", new StringBody(cc));
        reqEntity.addPart("cco", new StringBody(cco));
        reqEntity.addPart("asunto", new StringBody(subject));
        reqEntity.addPart("mensaje", new StringBody(message));

        httpPost.setEntity(reqEntity);

        httpclient.execute(httpPost);
        sendEmailBitacora(url);
    }
}
