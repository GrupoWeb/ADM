package gt.gob.rgm.service;

import gt.gob.rgm.model.RechazoCuenta;

public interface RechazoCuentaService {
	RechazoCuenta getRechazo(Long id);
	
	RechazoCuenta getRechazoByToken(String token);
	
	int update(Long id, RechazoCuenta rechazo);
}
