/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.rgm.adm.service;

/**
 *
 * @author jjolon
 */
public interface PdfBoletaService {
    
    public byte[] getBoletaPdf(Long pIdTramite, Long pIdGarantia);
    
}
