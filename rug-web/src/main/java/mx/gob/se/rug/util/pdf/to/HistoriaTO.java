package mx.gob.se.rug.util.pdf.to;

public class HistoriaTO {
	
	private String html;

	public void setHtml(String html) {
		this.html = html;
	}

	public String getHtml() {
		return html;
	}

	public void setValue(String key,String value){
		this.html=this.html.replace(key, value);
	}
	
}
