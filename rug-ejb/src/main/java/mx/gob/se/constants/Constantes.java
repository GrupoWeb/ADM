package mx.gob.se.constants;

import java.util.Map;

public class Constantes {
	public static Map<String, String> constantValue ;
	private static String usercurp;
	private static String passwordcurp;
	
	public String getParamValue(String cve_constante){
		System.out.println("constante a buscar 1:"+ cve_constante);
		
		if(constantValue==null){
			this.constantValue= new ConstantesDAO().getConstants();
	      }
		String regresa = null; 
		try{
			System.out.println("constante a buscar:"+ cve_constante);
			regresa = new String (this.constantValue.get(cve_constante));
			
		}catch(Exception e){
			System.out.println("No se encontro el parametro con la clave "+cve_constante);
			e.printStackTrace();
		}
		System.out.println(":::::::::::::PARAM "+cve_constante+" VALOR "+regresa);
		return regresa;
	}
	
	public static final String ENDPOINT_FIRMACENTRAL="endPointFirmaCentral";
	public static final String ENDPOINT_CERTIFICACION="endPointCertificacion";
	public static final String ENDPOINT_HISTORICO="endPointHistorico";
	public static final String FUERA_SERVICIO="fueraDeServicioRenapo";
	public static final String CURP_NO_ENCONTRADO="curpNoEncontrado";
	public static final String USER_CURP = usercurp;
	public static final String PASSWORD_CURP = passwordcurp;
	public static final String URL_MSCURP = "urlMsCurp";
	public static final String PERSONA_FISICA = "Persona F\u00edsica";
	public static final String PERSONA_JURIDICA = "Persona Jur\u00eddica";
}
