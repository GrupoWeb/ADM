package mx.gob.se.rug.inscripcion.to;

import java.io.Serializable;

public class WSExternoTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String CVE_PARAMETRO;
	private String VALOR_PARAMETRO;

    public String getCVE_PARAMETRO() {
        return CVE_PARAMETRO;
    }

    public void setCVE_PARAMETRO(String CVE_PARAMETRO) {
        this.CVE_PARAMETRO = CVE_PARAMETRO;
    }

    public String getVALOR_PARAMETRO() {
        return VALOR_PARAMETRO;
    }

    public void setVALOR_PARAMETRO(String VALOR_PARAMETRO) {
        this.VALOR_PARAMETRO = VALOR_PARAMETRO;
    }
	
	
	

}
