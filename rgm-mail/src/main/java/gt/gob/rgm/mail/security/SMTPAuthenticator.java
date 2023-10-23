package gt.gob.rgm.mail.security;

import javax.mail.PasswordAuthentication;


public  class SMTPAuthenticator extends javax.mail.Authenticator

{
	private String usuario;
	private String password;
	public SMTPAuthenticator(String user, String pass) {
		this.password=pass;
		this.usuario=user;
	}
    public PasswordAuthentication getPasswordAuthentication()
    {
        return new PasswordAuthentication(usuario, password);
    }
}