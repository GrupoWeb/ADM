/**
 * 
 */
package mx.gob.se.rug.fwk.action;

import java.math.BigInteger;

import mx.gob.economia.dgi.framework.security.user.dto.User;
import mx.gob.economia.dgi.framework.struts2.action.AbstractBaseAction;
import mx.gob.se.rug.constants.AllowedLanguages;
import mx.gob.se.rug.common.constants.Constants;
import mx.gob.se.rug.common.dto.Mensaje;
import mx.gob.se.rug.dto.PersonaMoral;

/**
 * Action base para el proyecto Rug.
 * 
 * @author Abraham Stalin Aguilar
 */
@SuppressWarnings("serial")
public abstract class RugBaseAction extends AbstractBaseAction{

	private Mensaje mensaje;
	private String next;
	private String back;
	public static final String SUCCESS = "success";
	public static final String FAILED = "failed";
	private String messageError;
	private String messageKey="Sucedio un Error, Disculpe las molestias; ";
	
	

	public String getMessageError() {
		return messageError;
	}

	public void setMessageError(String messageError) {
		this.messageError = messageError;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	/**
	 * @return the mensaje
	 */
	public Mensaje getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje
	 *            the mensaje to set
	 */
	public void setMensaje(Mensaje mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @return the next
	 */
	public String getNext() {
		return next;
	}

	/**
	 * @param next
	 *            the next to set
	 */
	public void setNext(String next) {
		this.next = next;
	}

	/**
	 * @return the back
	 */
	public String getBack() {
		return back;
	}

	/**
	 * @param back
	 *            the back to set
	 */
	public void setBack(String back) {
		this.back = back;
	}

	public User getUser() {
		return (User) sessionMap.get(Constants.SESSION_USER);
	}

	public PersonaMoral getPersonaMoralFromSession() {
		return (PersonaMoral) sessionMap.get(Constants.SESSION_PERSONA_MORAL);
	}

	public String getLenguaje() {
		String lenguaje = getLocale().getLanguage();
		if (!isLenguajeValido(lenguaje)) {
			// el lenguaje default
			lenguaje = AllowedLanguages.es.getLanguage();
		}
		logger.debug("lenguaje: " + lenguaje);
		return lenguaje;
	}

	private boolean isLenguajeValido(String lenguaje) {
		logger.debug("lenguaje: " + lenguaje);
		boolean isLenguajeValido = true;
		for (AllowedLanguages languages : AllowedLanguages.values()) {
			if (!languages.getLanguage().equals(lenguaje)) {
				isLenguajeValido = false;
			}
		}
		return isLenguajeValido;
	}

	public Integer getIntegerFromBigIntger(BigInteger bigInteger){
		Integer resultado=null;
		if (bigInteger!=null){
			resultado= new Integer(bigInteger.intValue());
		}
		
		return resultado;
	}

}
