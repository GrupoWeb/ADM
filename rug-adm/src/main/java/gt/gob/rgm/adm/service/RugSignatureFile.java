package gt.gob.rgm.adm.service;

import gt.gob.rgm.adm.domain.ResponseRs;

public interface RugSignatureFile {
    ResponseRs signatureFiles(Integer idTramite, Integer idGarantia, String email);

    Long getIdUsuarioByEmail(String email);
    
}
