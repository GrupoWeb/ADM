package mx.gob.se.rug.util;

import java.util.logging.Level;

public class ExpresionesRegularesUtil {
	public boolean validarRfcMoral(String rfc) {
		rfc = rfc.toUpperCase().trim();
		return rfc.toUpperCase().matches("[A-Z,Ñ]{3}[0-9]{6}[A-Z0-9]{3}");
	}

	public boolean validarRfcFisica(String rfc) {
		rfc = rfc.toUpperCase().trim();
		return rfc.toUpperCase().matches("[A-Z,Ñ]{4}[0-9]{6}[A-Z0-9]{3}");
	}
	
	public boolean validarCurp(String rfc) {
		rfc = rfc.toUpperCase().trim();
		return rfc.toUpperCase().matches("^([a-zA-Z]{4,4}[0"+"}-9]{6}[a-zA-Z]{6,6}[0-9]{2})");
	}
	
	public static void main(String args[]){
		ExpresionesRegularesUtil e = new ExpresionesRegularesUtil();
		MyLogger.Logger.log(Level.INFO, "" + e.validarRfcFisica("asdf120123few"));
	}
}
