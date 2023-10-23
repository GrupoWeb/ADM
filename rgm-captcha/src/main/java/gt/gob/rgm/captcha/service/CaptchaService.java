package gt.gob.rgm.captcha.service;

import javax.swing.ImageIcon;

public interface CaptchaService {
	public String getKeyCapcha();
	
	public ImageIcon getImage();
	
	public void generateCapcha();
}
