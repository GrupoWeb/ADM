package mx.gob.se.rug.boleta.service;

import java.util.List;

import mx.gob.se.rug.detallegarantia.to.PartesTO;
import mx.gob.se.rug.boleta.to.BoletaTO;

public interface BoletaService {
	public String getProcedencia(Integer idTramiteTemp);
	public BoletaTO Tramiteterminados(Integer idtramite);
	public String getTablaBienes(int idgarantia, Integer idTramite);
	public String getDeudor(Integer idtramite);
	public String getAcreedor(Integer idtramite, String tipo);
	public List<BoletaTO> getHistorico(Integer idgarantia,Integer idtramite);
	public String getTipoBienes(Integer relacion);
	public BoletaTO AnotacionC(Integer idtramite);
	public BoletaTO AnotacionS(Integer idtramite);
	public PartesTO getOtorgante(Integer idtramite, Integer idgarantia);
	public BoletaTO getTramitetrans(Integer idTramiteinc,Integer idTipoTramite);
}
