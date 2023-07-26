package gt.gob.rgm.adm.service;

import java.util.Date;
import java.util.List;

import gt.gob.rgm.adm.domain.SurveyStatsMulti;

public interface RugRespuestaService {

	public Long findTotalByParams(Long pIdPregunta, String pRespuesta, Date pFechaDesde, Date pFechaHasta);
	
	public List<SurveyStatsMulti> findGroupByParam(Long pIdPregunta, Date pFechaDesde, Date pFechaHasta);
	
	public List<String> findRespuestaByParam(Long pIdPregunta, Date pFechaDesde, Date pFechaHasta) ;
}
