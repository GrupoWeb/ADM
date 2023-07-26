package gt.gob.rgm.service;

import java.io.ByteArrayInputStream;
import java.util.List;

import gt.gob.rgm.model.RugGarantiasBienesPend;
import gt.gob.rgm.util.FormTramites;
import gt.gob.rgm.util.ReporteTramites;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.ws.rs.core.Response;

public interface RugGarantiasBienesPendService {
    List<RugGarantiasBienesPend> getValorBienesByTramiteTemp(Integer idTramiteTemp);

    List<String> getTramitesByGarantia(Integer idGarantia);

    List<String> getBienesTramites(Integer idTramite);

    List<String> getCountFacturasByTramite(Integer idTramite);

    List<String> getCountFacturaByGarantia(Integer idGarantia);

    Integer getPadre(Integer idUsuario);

    List<ReporteTramites>  getTramites(Integer idUsuario, String anio);
    List<ReporteTramites>  getTramitesNew(Integer idUsuario, String anio);

    List<FormTramites> getAnioByUser(Integer idUsuario);

    XSSFWorkbook getDataExcel(Integer idUsuario, String anio);

//    List<FormTramites> getTramitesReporte(Integer idUsuario);
}
