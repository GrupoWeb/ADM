package gt.gob.rgm.adm.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import gt.gob.rgm.adm.dao.RugPreguntaRepository;
import gt.gob.rgm.adm.model.RugPregunta;

@Stateless
public class RugPreguntaServiceImp implements RugPreguntaService{

	@Inject
	private RugPreguntaRepository preguntaDao;
	
	public void savePregunta(RugPregunta pPregunta) {
		preguntaDao.save(pPregunta);
	}
}
