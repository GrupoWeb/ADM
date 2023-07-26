package mx.gob.se.mail.to;

public class AccountMailTO {

	private Integer idAccountSmtp;
	private String smtpHost;
	private String smtpPort;
	private String smtpUserMail;
	private String smtpPasswordMail;
	private String smtpAuth;
	private String smtpSslEnable;
	private String mailContentType;
	
	
	public Integer getIdAccountSmtp() {
		return idAccountSmtp;
	}
	public void setIdAccountSmtp(Integer idAccountSmtp) {
		this.idAccountSmtp = idAccountSmtp;
	}
	public String getSmtpHost() {
		return smtpHost;
	}
	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}
	public String getSmtpPort() {
		return smtpPort;
	}
	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}
	public String getSmtpUserMail() {
		return smtpUserMail;
	}
	public void setSmtpUserMail(String smtpUserMail) {
		this.smtpUserMail = smtpUserMail;
	}
	public String getSmtpPasswordMail() {
		return smtpPasswordMail;
	}
	public void setSmtpPasswordMail(String smtpPasswordMail) {
		this.smtpPasswordMail = smtpPasswordMail;
	}
	public String getSmtpAuth() {
		return smtpAuth;
	}
	public void setSmtpAuth(String smtpAuth) {
		this.smtpAuth = smtpAuth;
	}
	public String getSmtpSslEnable() {
		return smtpSslEnable;
	}
	public void setSmtpSslEnable(String smtpSslEnable) {
		this.smtpSslEnable = smtpSslEnable;
	}
	public String getMailContentType() {
		return mailContentType;
	}
	public void setMailContentType(String mailContentType) {
		this.mailContentType = mailContentType;
	}
	
	
}
