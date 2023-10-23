package gt.gob.rgm.mail.domain;

public class MessageTO {
	
	private String mailTo;
	private String mailCc;
	private String mailCco;
	private String mailSubject;
	private String mailText;
	public String getMailTo() {
		return mailTo;
	}
	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}
	public String getMailCc() {
		return mailCc;
	}
	public void setMailCc(String mailCc) {
		this.mailCc = mailCc;
	}
	public String getMailCco() {
		return mailCco;
	}
	public void setMailCco(String mailCco) {
		this.mailCco = mailCco;
	}
	public String getMailSubject() {
		return mailSubject;
	}
	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}
	public String getMailText() {
		return mailText;
	}
	public void setMailText(String mailText) {
		this.mailText = mailText;
	}

	
	
}
