/**
 * 
 */
package mx.gob.se.rug.common.constants;

/**
 * @author Erika Astorga
 * 
 */
public interface Constants {

	String SE = "SE";
	String CIUDADANO_PORTAL = "CiudadanoPortal";
	String REGEX = "(?=.*[0-9])(?=.+[a-z])(?=.*[A-Z]).{8,16}";
	String CURP_REGEX = "(([abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ&]){4})([0-9]{6})([hmHM]{1})(([abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ]|\\s){5})([0-9]{2})";
	String RFC_PERSONA_FISICA_REGEX = "(([abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ&]){4})([0-9]{6})(([abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ]){2})([0-9]{1})";
	String SESSION_USER = "User";
	String SESSION_PERSONA_MORAL = "PersonaMoral";
	String COOKIE_LOGEADO = "logeado";
	String CAPTCHA = "captcha";
	String CAPTCHA_ERROR = "captchaError";

	String DATA_SOURCE = "jdbc/rugDS";
	
	String EMAIL_REGEX = "(^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$)";
	
	String MASTER_USER = "masterUser";
}
