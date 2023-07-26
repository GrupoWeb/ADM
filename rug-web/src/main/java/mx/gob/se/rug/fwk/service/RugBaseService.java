package mx.gob.se.rug.fwk.service;

import mx.gob.economia.dgi.framework.base.service.AbstractBaseService;
import mx.gob.economia.dgi.framework.security.user.dto.User;

/**
 * Clase base de los servicios de negocio del proyecto tuempresa.
 * 
 * @author Alfonso Esquivel
 * @version 0.1
 * 
 */
public abstract class RugBaseService extends AbstractBaseService {
	private User user;

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

}
