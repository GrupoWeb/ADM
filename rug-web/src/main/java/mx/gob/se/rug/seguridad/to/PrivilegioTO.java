package mx.gob.se.rug.seguridad.to;

public class PrivilegioTO {

	
	private Integer idPrivilegio;
	private String descripcion;
	private String html;
	
	public Integer getIdPrivilegio() {
		return idPrivilegio;
	}
	public void setIdPrivilegio(Integer idPrivilegio) {
		this.idPrivilegio = idPrivilegio;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PrivilegioTO [idPrivilegio=");
		builder.append(idPrivilegio);
		builder.append(", descripcion=");
		builder.append(descripcion);
		builder.append(", html=");
		builder.append(html);
		builder.append("]");
		return builder.toString();
	}
	
	
	
	
}
