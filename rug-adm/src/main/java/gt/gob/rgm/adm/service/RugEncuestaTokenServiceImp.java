package gt.gob.rgm.adm.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import gt.gob.rgm.adm.dao.RugEncuestaTokenRepository;
import gt.gob.rgm.adm.model.RugEncuestaToken;

@Stateless
public class RugEncuestaTokenServiceImp implements RugEncuestaTokenService{

	@Inject
	private RugEncuestaTokenRepository rugEncuestaTokenDao;
	
	public RugEncuestaToken save(RugEncuestaToken encuesta) {
		rugEncuestaTokenDao.save(encuesta);
		
		return encuesta;
	}
}
