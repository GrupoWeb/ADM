package mx.gob.se.rug.captcha;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import gt.gob.rgm.captcha.service.CaptchaService;
//import mx.gob.se.captcha.CaptchaBeanLocal;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.util.MyLogger;


public class Captcha extends HttpServlet{

	private static final long serialVersionUID = 1L;

	/*@EJB
	private CaptchaBeanLocal beanRemote;*/
	
	@Autowired
	private CaptchaService captchaService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		HttpSession session = req.getSession(false);
		
		//beanRemote.generateCapcha();
		captchaService.generateCapcha();

		//session.setAttribute(Constants.CAPTCHA_KEY, beanRemote.getKeyCapcha());
		session.setAttribute(Constants.CAPTCHA_KEY, captchaService.getKeyCapcha());
		
		//BufferedImage bufferedImage = imageToBufferedImage(beanRemote.getImage().getImage());
		BufferedImage bufferedImage = imageToBufferedImage(captchaService.getImage().getImage());
		resp.setContentType("image/jpg");
		//MyLogger.Logger.log(Level.INFO, beanRemote.getKeyCapcha());
		MyLogger.Logger.log(Level.INFO, captchaService.getKeyCapcha());
		OutputStream out = resp.getOutputStream();
		ImageIO.write(bufferedImage, "jpg", out);
		out.close();
	}
	
	public  BufferedImage imageToBufferedImage(Image img) {
		// if it's already a buffered image, return it (assume it's fully loaded already)
		if(img instanceof BufferedImage) {
			return (BufferedImage)img;
		}
		// create a new buffered image and draw the specified image on it
		BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), 
			BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bi.createGraphics();
		g2d.drawImage(img, 0, 0, null);
		g2d.dispose();
		return bi;
	}
	
	public CaptchaService getCaptchaService() {
		return captchaService;
	}
	
	public void setCaptchaService(CaptchaService captchaService) {
		this.captchaService = captchaService;
	}
}
