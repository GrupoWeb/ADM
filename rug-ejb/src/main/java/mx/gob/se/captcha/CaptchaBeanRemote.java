package mx.gob.se.captcha;
import java.awt.image.BufferedImage;

import javax.ejb.Remote;
import javax.swing.ImageIcon;

@Remote
public interface CaptchaBeanRemote {

	public String getKeyCapcha();
	public ImageIcon getImage() ;
	public void generateCapcha();
}
