package gt.gob.rgm.adm.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import gt.gob.rgm.adm.dao.RechazoCuentaRepository;
import gt.gob.rgm.adm.model.RechazoCuenta;


@Stateless
public class RechazoCuentaServiceImp implements RechazoCuentaService {
	
	@Inject
	private RechazoCuentaRepository rechazoDao;

	@Override
	public RechazoCuenta save(RechazoCuenta rechazo) {
		rechazoDao.save(rechazo);
		return rechazo;
	}
}
