package mx.gob.se.rug.util.to;

import java.util.logging.Level;

import mx.gob.se.rug.util.MyLogger;

public class PalabrasUtil {
	
	
	public String getLetraByNum(Integer num){
		String [] letras = {	
				"A","B","C","D","E"
			   ,"F","G","H","I","J"
			   ,"K","L","M","N","O"
			   ,"P","Q","R","S","T"
			   ,"U","V","W","X","Y"
			   ,"Z","0","1","2","3"
			   ,"4","5","6","7","8"
			   ,"9"
			   
		};
		
		return letras[num];
	}
	
	public static void main (String args[]){
		PalabrasUtil palabrasUtil = new PalabrasUtil();
		MyLogger.Logger.log(Level.INFO,"numero 0 : " + palabrasUtil.getLetraByNum(0));
		MyLogger.Logger.log(Level.INFO,"numero 25 : " + palabrasUtil.getLetraByNum(25));
	}
}
