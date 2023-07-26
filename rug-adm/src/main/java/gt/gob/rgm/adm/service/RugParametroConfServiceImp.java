package gt.gob.rgm.adm.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import gt.gob.rgm.adm.dao.RugParametroConfRepository;
import gt.gob.rgm.adm.model.RugParametroConf;

@Stateless
public class RugParametroConfServiceImp implements RugParametroConfService {
	
	@Inject
	private RugParametroConfRepository parametroDao;

	@Override
	public RugParametroConf getParam(String key) {
		return parametroDao.findByKey(key);
	}
}
