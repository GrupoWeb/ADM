package mx.gob.se.rug.seguridad.to;

public class FormularioAltaTO {
	
	
	private String idFormulario;
	private String idPrivilegio;
	
	public FormularioAltaTO(String idFormulario) {
		super();
		this.idFormulario = idFormulario;
	}

	public String getIdFormulario() {
		return idFormulario;
	}

	public void setIdFormulario(String idFormulario) {
		this.idFormulario = idFormulario;
	}

	public String getIdPrivilegio() {
		return idPrivilegio;
	}

	public void setIdPrivilegio(String idPrivilegio) {
		this.idPrivilegio = idPrivilegio;
	}
	
	
}
