package mx.gob.se.renapo.curp.consulta;

import java.io.UnsupportedEncodingException;

import javax.ejb.Stateless;

import mx.gob.se.constants.Constantes;
import mx.gob.se.ms.curp.client.ConsultaCurpResponse.Response;
import mx.gob.se.ms.curp.client.MsConsultaCurpClient;

import mx.gob.se.renapo.curp.consulta.exception.NoCurpFound;
import mx.gob.se.renapo.curp.consulta.exception.NoServiceRenapoAvailable;



/**
 * Session Bean implementation class ConsultaCurpBean
 */
@Stateless(mappedName = "ejb/CurpBeanJNDI")
public class ConsultaCurpBean implements ConsultaCurpBeanLocal, ConsultaCurpBeanRemote {
	
	@Override
	public ResponseCurp consultaCurp(String curp) throws NoCurpFound,
	NoServiceRenapoAvailable {
		
		Constantes constantes = new Constantes();
		
			//String urlMsCurp="http://10.235.235.196:8080/MsCurp/ConsultaCurpService";
			//String urlMsCurp="http://172.16.251.74:4980/MsCurp/ConsultaCurpService"; //produccion ip interna
			//String urlMsCurp="http://10.235.235.90:4980/MsCurp/ConsultaCurpService";	//produccion ip publica	
			String urlMsCurp = constantes.getParamValue(Constantes.URL_MSCURP);
			String seUsuario="";
			String sePassword="";
			MsConsultaCurpClient client=null;
			Response response=null;
		 
			try{
			client = new MsConsultaCurpClient(urlMsCurp, seUsuario, sePassword);
			response = client.getInfoByCurp(curp);
			}catch(Exception e){
				//System.out.println("Problema");
				throw new NoServiceRenapoAvailable(constantes.getParamValue(Constantes.FUERA_SERVICIO), new Integer(99));
			}
			
			ResponseCurp rcEjb= new ResponseCurp();
			
			//System.out.println("Aqui "+response.getResponseCode());
			
			/*if (response.getResponseCode().equals(2)){
				
				response.setResponseCode(3);
			}*/
			if(response.getResponseCode().equals(4)){
				rcEjb.setRespuesta(4);
				System.out.println("Fuera de Servicio**");
				throw new NoServiceRenapoAvailable(constantes.getParamValue("fueraDeServicioRenapo"), new Integer(99));
				
			}else if(response.getResponseCode().equals(0)){
			rcEjb.setNombre(datosSalida(response.getNombres()));
			rcEjb.setApellidoPaterno(datosSalida(response.getApellidoPaterno()));
			rcEjb.setApellidoMaterno(datosSalida(response.getApellidoMaterno()));
			rcEjb.setCurp(response.getCurp());
			rcEjb.setRespuesta(0);
			return rcEjb;
			//System.out.println("Encontrado");
			}else if(response.getResponseCode().equals(3)){
				//System.out.println("No encontrado");
				rcEjb.setRespuesta(3);
				throw new NoCurpFound(constantes.getParamValue(Constantes.CURP_NO_ENCONTRADO).replace("@curp", curp), new Integer(3));
			}
			
			return null;
			
	 }
	
	
	
	public static String datosSalida(String cadena){
		cadena=cadena.replace("¥", "Ñ");
		return cadena;
	}
	
	
	public static void main(String arg[]){
		try {
			ConsultaCurpBean bean = new ConsultaCurpBean();
			ResponseCurp curp=	bean.consultaCurp("COML550812HOCNGS02");
			
			System.out.println(curp.getNombre() +" Apellido "+ datosSalida(curp.getApellidoPaterno()));
			try {
				String utf8String = new String ( "peña".getBytes(), "ISO-8859-1" );
				System.out.println("Oey "+utf8String);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (NoCurpFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoServiceRenapoAvailable e) {
			// TODO Auto-generated catch block
			System.out.println("no service ");
			e.printStackTrace();
			
		}
	}

}
