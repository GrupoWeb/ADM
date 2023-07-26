package mx.gob.se.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.swing.ImageIcon;

import mx.gob.se.utils.Random;

/**
 * Session Bean implementation class CapchaBean
 */
@Stateless
public class CaptchaBean implements CaptchaBeanRemote, CaptchaBeanLocal {

	private String keyCapcha;
	private ImageIcon image;

	public void generateCapcha() {
		this.keyCapcha = getSelloKey();
		int x = 202;// x=202;//ancho de la imagen
		int y = 50;// y=30;//Alto imagen

		// Separar el codigo con espacios
		// Creamos imagen de tipo RGB (Lienzo)
		BufferedImage bufferedImage = new BufferedImage(x, y,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();// createGraphics();
		// Creamos el fondo con textura (cuadriculado)
		BufferedImage bi = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
		Graphics2D big = (Graphics2D) bi.getGraphics();// .createGraphics();
		// Render into the BufferedImage graphics to create the texture
		big.setColor(Color.WHITE);
		//big.fillRect(0, 0, 5, 5);
		//big.setColor(Color.LIGHT_GRAY);
		//big.fillOval(0, 0, 5, 5);
		// Create a texture paint from the buffered image
		Rectangle r = new Rectangle(0, 0, 5, 5);
		TexturePaint tp = new TexturePaint(bi, r);
		// Add the texture paint to the graphics context.
		graphics2D.setPaint(tp);
		// Create and render a rectangle filled with the texture.
		graphics2D.fillRect(0, 0, x, y);
		// Agregamos el TEXTO a la imagen ya con Fondo
		graphics2D.setColor(Color.WHITE);
		graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
		RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics2D.setFont(new Font("sansserif", Font.ITALIC, 27));
		//int gradientx = Random.generateRandom(30);
		//int gradienty = Random.generateRandom(30);
		// Gradiente
	//	graphics2D.setPaint(new GradientPaint(x + 400 + gradientx, y + 70
	//			+ gradienty, Color.DARK_GRAY, x + 435 + gradientx, y + 85
	//			+ gradienty, Color.GREEN, true));
		// Dibuja el texto
		int desplazamientoX = 20;
		int angle = Random.generateRandom(5);
		for (int i = 0; i < keyCapcha.length(); i++) {
			graphics2D.rotate(angle * Math.PI / 180.0);
			graphics2D.drawString(String.valueOf(keyCapcha.charAt(i)),
					desplazamientoX, 25); // 25);
			angle = angle * -1;
			desplazamientoX += 30;
		}
		graphics2D.dispose();
		// Regresa el bufferImage
		ImageIcon icon = new ImageIcon(bufferedImage);
		this.image=icon;
		//return bufferedImage;

	}

	private String getSelloKey() {
		StringBuffer sb = new StringBuffer();
		sb.append(getCharByNum(Random.generateRandom(10)));
		sb.append(getCharByNum(Random.generateRandom(10)));
		sb.append(getCharByNum(Random.generateRandom(10)));
		sb.append(getCharByNum(Random.generateRandom(10)));
		sb.append(getCharByNum(Random.generateRandom(10)));
		return sb.toString();
	}
	
	private char getCharByNum(Integer i){
		char charKey[] = {
			'F','G','H','I','J',
			'6','7','8','9',
			'A','B','C','D','E',
			'U','V','X','Y','Z',
			'K','L','M','N','O',
			'1','2','3','4','5',
			'P','Q','R','S','T'
			
		};
		
		return charKey[i];
		
	}

	public String getKeyCapcha() {
		return keyCapcha;
	}

	public ImageIcon getImage() {
		return image;
	}

	


	
}
