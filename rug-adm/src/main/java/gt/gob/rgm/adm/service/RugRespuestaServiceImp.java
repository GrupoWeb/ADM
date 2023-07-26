package gt.gob.rgm.adm.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import gt.gob.rgm.adm.dao.RugRespuestaRepository;
import gt.gob.rgm.adm.domain.SurveyStatsMulti;

@Stateless
public class RugRespuestaServiceImp implements RugRespuestaService {

	@Inject
	private RugRespuestaRepository respuestaDao;
	
	@Override
	public Long findTotalByParams(Long pIdPregunta, String pRespuesta, Date pFechaDesde, Date pFechaHasta) {
		return respuestaDao.findTotalByParams(pIdPregunta, pRespuesta, pFechaDesde, pFechaHasta);
	}

	@Override
	public List<SurveyStatsMulti> findGroupByParam(Long pIdPregunta, Date pFechaDesde, Date pFechaHasta) {
		return respuestaDao.findGroupByParam(pIdPregunta, pFechaDesde, pFechaHasta);
	}
	
	@Override
	public List<String> findRespuestaByParam(Long pIdPregunta, Date pFechaDesde, Date pFechaHasta) {
		return respuestaDao.findRespuestaByParam(pIdPregunta, pFechaDesde, pFechaHasta);
	}
}
