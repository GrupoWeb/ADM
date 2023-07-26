package mx.gob.se.mail.to;

import java.io.Serializable;

public class MailTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idMail;
	private Integer idStatus;
	private Integer idTipoMensaje;

	private String exceptionMail;
	private MessageTO messageTO;
	private AccountMailTO accountMailTO;
	
	
	public Integer getIdTipoMensaje() {
		return idTipoMensaje;
	}
	public void setIdTipoMensaje(Integer idTipoMensaje) {
		this.idTipoMensaje = idTipoMensaje;
	}
	public Integer getIdStatus() {
		return idStatus;
	}
	public void setIdStatus(Integer idStatus) {
		this.idStatus = idStatus;
	}
	public Integer getIdMail() {
		return idMail;
	}
	public void setIdMail(Integer idMail) {
		this.idMail = idMail;
	}
	public String getExceptionMail() {
		return exceptionMail;
	}
	public void setExceptionMail(String exceptionMail) {
		this.exceptionMail = exceptionMail;
	}
	public MessageTO getMessageTO() {
		return messageTO;
	}
	public void setMessageTO(MessageTO messageTO) {
		this.messageTO = messageTO;
	}
	public AccountMailTO getAccountMailTO() {
		return accountMailTO;
	}
	public void setAccountMailTO(AccountMailTO accountMailTO) {
		this.accountMailTO = accountMailTO;
	}
	
	
	
	
}
