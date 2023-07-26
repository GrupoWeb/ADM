package gt.gob.rgm.adm.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Date;
import java.util.logging.Level;

public class CharSetUtil {
	
	public  String charsetEncode(String charSet, String txtToConvert) throws CharacterCodingException{
		StringBuffer buffer= new StringBuffer();
		
		Charset charset = Charset.forName(charSet);
		CharsetEncoder encoder = charset.newEncoder();
		
		buffer.append(txtToConvert);
		 java.nio.ByteBuffer byBuffer= encoder.encode(CharBuffer.wrap(buffer.toString()));
		 StringBuilder cadena=new StringBuilder(byBuffer.limit());
		 
		 byBuffer.rewind();
		 while(byBuffer.hasRemaining()){
		     cadena.append((char)byBuffer.get());
		 }
		 return cadena.toString();
	}
	
	public String presentacionMaximaDeTexto(String cadena, int presentacion, String cadenaModificada){
		if (cadena.length()<presentacion){
			return cadena;
		}
		if (!cadena.contains(" ")){
			if(cadena.length()>presentacion){
				cadenaModificada = cadena.substring(0,presentacion) + "- " + cadena.substring(presentacion);				
				return presentacionMaximaDeTexto(cadenaModificada, presentacion, "");
			}else{
				return cadenaModificada;
			}
		}else{
			if (hayMayorPorEspacio(cadena, presentacion)){
				String cadenas[] = cadena.split(" ");
				for (String cadenaita:cadenas){
					cadenaModificada += divideCadena(cadenaita, presentacion);
				}
				return cadenaModificada;
			}else{
				return cadena;
			}			
		}
		
	}
	
	public boolean hayMayorPorEspacio(String palabra, int logitudMaxima){
		boolean regresa = false;
		String cadenas[] = palabra.split(" ");
		for (String cadena:cadenas){
			if (cadena.length()>logitudMaxima){
				regresa = true;
			}
		}
		return regresa;
	}

public String longitudMaximaPorPalabra1(String palabra,int longitudCadenaMaxima) {
	StringBuffer cadenaModificada = new StringBuffer();
	if (palabra.length() > longitudCadenaMaxima) {
		String cadenas[] = palabra.split(" ");
		for (String cadena : cadenas) {
			if (cadena.length() <= longitudCadenaMaxima) {
				cadenaModificada.append(cadena);
			} else {
				if (cadena.contains(",")) {
					cadenaModificada.append(insertaEspaciosComa(cadena,
							longitudCadenaMaxima));
				} else {
					//MyLogger.Logger.log(Level.WARNING, "cadena mayor sin coma:" + cadena);
					cadenaModificada.append(divideCadena(cadena,
							longitudCadenaMaxima));
				}
			}
			cadenaModificada.append(" ");
		}
	} else {
		cadenaModificada.append(palabra);
	}
	return cadenaModificada.toString();
}
	
	public String longitudMaximaPorPalabra(String cadena,int longitudCadenaMaxima) {
	StringBuffer cadenaFinal = new StringBuffer();
	
	String cadenaPartida;
	String cadenaPartidaAux;
	int indexOfLastSpace;
	
	int cadenaLength=cadena.length();
	
	if(cadenaLength>longitudCadenaMaxima){
		
		int beginIndex=0;
		int endIndex=0;
		boolean procesando=true;
		
		while(procesando){
			
			endIndex=endIndex+longitudCadenaMaxima;
			
			if(endIndex>=cadenaLength){
				endIndex=cadenaLength;
			}
			
			cadenaPartidaAux= cadena.substring(beginIndex, endIndex);
			indexOfLastSpace=cadenaPartidaAux.lastIndexOf(" ");
			
			if(indexOfLastSpace>0){
				//hay Espacios 
				endIndex=beginIndex+indexOfLastSpace;
				cadenaPartida=cadena.substring(beginIndex, endIndex);
				cadenaFinal.append(cadenaPartida);
			}else{//No hay espacios
				if (cadenaPartidaAux.contains(",")) {
					cadenaPartida=insertaEspaciosComa(cadenaPartidaAux,longitudCadenaMaxima);
					cadenaFinal.append(cadenaPartida);
					cadenaPartida= null;
				}else{
					cadenaFinal.append(cadenaPartidaAux);
					cadenaFinal.append(" ");
				}
			}
			cadenaPartidaAux= null;
			cadenaPartida=null;
			
			beginIndex=endIndex;
			
			if(endIndex==cadenaLength){
				procesando=false;
			}
			
			
		}
		
	}else{
		cadenaFinal.append(cadena);
	}
	return cadenaFinal.toString();
}
	

	
	

	private String divideCadena(String palabra,int longitudCadenaMaxima ){
		String cadenaModificada = "";
		
		while (palabra.length()>longitudCadenaMaxima){			
			cadenaModificada += palabra.substring(0,longitudCadenaMaxima)+" ";
			palabra = palabra.substring(longitudCadenaMaxima,palabra.length());
		}
		cadenaModificada += palabra;
		return cadenaModificada;
	}
	
	private String insertaEspaciosComa(String palabra,int longitudCadenaMaxima ){
		StringBuffer cadenaModificada = new StringBuffer();
		String cadenas[] = palabra.split(",");
		int contCadenas = 0;
		int cadenasMax = cadenas.length-1;
		String cadena;
		while (contCadenas < cadenasMax){			
			cadena = cadenas[contCadenas++];
			if (cadena.length()>longitudCadenaMaxima){
				cadenaModificada.append(divideCadena(cadena,longitudCadenaMaxima ));
				cadenaModificada.append(", ");
			}else{
				cadenaModificada.append(cadena);
				cadenaModificada.append(", ");
			}
			
		}
		cadena = cadenas[contCadenas];
		cadenaModificada.append(cadena);
		//MyLogger.Logger.log(Level.WARNING, "Cadena Modificada:" + cadenaModificada);
		return cadenaModificada.toString();
	}	
	
	public static void main (String args[]){
		Date fechaInicio = new Date();
		StringBuffer cadena = new StringBuffer() ;
		

	    FileReader input;
		try {
			input = new FileReader("C:\\Users\\prove.desa\\Documents\\RUG\\prueba.txt");
	
	               
	                /* Filter FileReader through a Buffered read to read a line at a
	                 time */
	                BufferedReader bufRead = new BufferedReader(input);
	                
	              String line;    // String that holds current file line
	                int count = 0;  // Line number of count 
	                
	               // Read first line
	               line = bufRead.readLine();
	               count++;
	               
	                // Read through file one line at time. Print line # and line
	               while (line != null){
	              // MyLogger.Logger.log(Level.INFO, count+": "+line);
	            	   cadena.append(line);
	                  line = bufRead.readLine();
	                
	                   count++;
          }
	              bufRead.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		CharSetUtil csu = new CharSetUtil();
		//MyLogger.Logger.log(Level.INFO, "valorO:"+cadena.toString());
		//MyLogger.Logger.log(Level.INFO, "valor :"+csu.longitudMaximaPorPalabra(cadena.toString(),50));
		//MyLogger.Logger.log(Level.INFO, csu.longitudMaximaPorPalabra(cadena.toString(),132));
		Date fechaFin = new Date();

		long tiempoInicial=fechaInicio.getTime();
		long tiempoFinal=fechaFin.getTime();
		long resta=tiempoFinal - tiempoInicial;
		//el metodo getTime te devuelve en mili segundos para saberlo en mins debes hacer
		//MyLogger.Logger.log(Level.INFO, "" + resta);
		resta=resta /(1000);
		//MyLogger.Logger.log(Level.INFO, "" + resta);
	}


}
