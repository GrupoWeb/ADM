package gt.gob.rgm.service;

import java.util.Optional;

import org.springframework.context.annotation.Scope;

import gt.gob.rgm.dao.RechazoCuentaRepository;
import gt.gob.rgm.model.RechazoCuenta;

@Scope("prototype")
public class RechazoCuentaServiceImp implements RechazoCuentaService {
	
	private RechazoCuentaRepository rechazoDao;

	@Override
	public RechazoCuenta getRechazo(Long id) {
		Optional<RechazoCuenta> rechazo = rechazoDao.findById(id);
		return rechazo.get();
	}

	@Override
	public RechazoCuenta getRechazoByToken(String token) {
		return rechazoDao.findByToken(token);
	}

	@Override
	public int update(Long id, RechazoCuenta rechazo) {
		Optional<RechazoCuenta> rechazoEncontrado = rechazoDao.findById(id);
		if(rechazoEncontrado.isPresent()) {
			RechazoCuenta rechazoActual = rechazoEncontrado.get();
			rechazoActual.setToken(rechazo.getToken());
			rechazoDao.save(rechazoActual);
			return 1;
		}
		return 0;
	}

	public void setRechazoDao(RechazoCuentaRepository rechazoDao) {
		this.rechazoDao = rechazoDao;
	}
}
