package gt.gob.rgm.adm.service;

import gt.gob.rgm.adm.domain.Principal;

public interface SecurityService {
	public Principal login(Principal principal);
}
