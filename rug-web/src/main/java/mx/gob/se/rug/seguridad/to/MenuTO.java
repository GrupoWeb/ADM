package mx.gob.se.rug.seguridad.to;

import java.util.List;

public class MenuTO {

	private Integer idMenu;
	private String contextPath;
	private List<String> html;
	
	
	public MenuTO(){
		
	}
	public MenuTO(Integer idMenu, String contextPath) {
		super();
		this.idMenu = idMenu;
		this.contextPath = contextPath;
	}
	public String getContextPath() {
		return contextPath;
	}
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	public Integer getIdMenu() {
		return idMenu;
	}
	public void setIdMenu(Integer idMenu) {
		this.idMenu = idMenu;
	}
	public List<String> getHtml() {
		return html;
	}
	public void setHtml(List<String> html) {
		this.html = html;
	}
	
}
