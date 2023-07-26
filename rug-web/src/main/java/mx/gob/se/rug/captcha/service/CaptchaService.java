package mx.gob.se.rug.captcha.service;

import java.awt.image.BufferedImage;
import java.util.logging.Level;

import javax.ejb.EJB;

import mx.gob.se.captcha.CaptchaBeanRemote;
import mx.gob.se.rug.util.MyLogger;

public class CaptchaService {

	@EJB
	private CaptchaBeanRemote capchaBean;
	
	public String getCapchaKey(){
	return capchaBean.getKeyCapcha();
	}
	
	public BufferedImage getCapchaImage(){
		return (BufferedImage)capchaBean.getImage().getImage();
	}

	public void generateCapcha(){
		if(capchaBean==null){
			MyLogger.Logger.log(Level.INFO, "null no ejb");
		}else{
		capchaBean.generateCapcha();
		}
	}
	
}
