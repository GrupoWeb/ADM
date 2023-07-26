package mx.gob.se.captcha;
import java.awt.image.BufferedImage;

import javax.ejb.Local;
import javax.swing.ImageIcon;

@Local
public interface CaptchaBeanLocal {

	public String getKeyCapcha();
	public ImageIcon getImage();
	public void generateCapcha();
}
