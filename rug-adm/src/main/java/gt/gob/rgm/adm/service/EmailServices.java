package gt.gob.rgm.adm.service;

import java.io.IOException;

public interface EmailServices {

    void sendEmailBitacora(String Url) throws IOException;

    void RechazoEmail(String subject, String message, String url,  String destino, String cc, String cco) throws IOException;

    void AprobacionEmail(String subject, String message, String url,  String destino, String cc, String cco) throws IOException;


}
