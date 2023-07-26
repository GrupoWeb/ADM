package gt.gob.rgm.adm.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import gt.gob.rgm.adm.dao.RugRelTramPartesRepository;
import gt.gob.rgm.adm.model.RugRelTramPartes;

@Stateless
public class RugRelTramPartesServiceImp implements RugRelTramPartesService {
	
	@Inject
	private RugRelTramPartesRepository partesDao;

	@Override
	public List<RugRelTramPartes> getPartes(RugRelTramPartes filter, Integer page, Integer size) {
		return partesDao.findWithFilter(filter, page, size);
	}
}
