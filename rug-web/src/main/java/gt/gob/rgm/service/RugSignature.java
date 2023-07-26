package gt.gob.rgm.service;

import javax.ws.rs.core.Response;


public interface RugSignature {
    Response signatureFiles(Integer idGarantia, Integer idTramite, Integer idUsuario,  Integer idTipoTramiteRest);

    Long getIdUsuarioByEmail(String email);

}
