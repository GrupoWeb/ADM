package gt.gob.rgm.adm.util;

public class PdfTO {

	private String html;
	private String pathRoot;
	private Integer idTramite;
	private byte file[];
	private String key;
	private String save;
	private Integer idTipoGarantia;
	
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public String getPathRoot() {
		return pathRoot;
	}
	public void setPathRoot(String pathRoot) {
		this.pathRoot = pathRoot;
	}
	public Integer getIdTramite() {
		return idTramite;
	}
	public void setIdTramite(Integer idTramite) {
		this.idTramite = idTramite;
	}
	public byte[] getFile() {
		return file;
	}
	public void setFile(byte[] file) {
		this.file = file;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getSave() {
		return save;
	}
	public void setSave(String save) {
		this.save = save;
	}
	public Integer getIdTipoGarantia() {
		return idTipoGarantia;
	}
	public void setIdTipoGarantia(Integer idTipoGarantia) {
		this.idTipoGarantia = idTipoGarantia;
	}
	
	public void setValue(String key,String value){
		if(value==null){
			value="N/A";
		}else if(value.trim().equalsIgnoreCase("null")){
			value="N/A";
		}
		if(this.html!=null){
			CharSetUtil charSetUtil = new CharSetUtil();
			value= "<![CDATA["+charSetUtil.longitudMaximaPorPalabra(value, Constants.MAX_LENGHT_LINE) +"]]>";
		this.html=this.html.replace(key, value);
		}else{
			//MyLogger.Logger.log(Level.WARNING,"parametro de BD no esta:::"+key);
		}
	}
	public void setValueSmall(String key,String value){
		if(value==null){
			value="N/A";
		}else if(value.trim().equalsIgnoreCase("null")){
			value="N/A";
		}
		if(this.html!=null){
			CharSetUtil charSetUtil = new CharSetUtil();
			value= "<![CDATA["+charSetUtil.longitudMaximaPorPalabra(value, Constants.MAX_LENGHT_LINE_SMALL) +"]]>";
		this.html=this.html.replace(key, value);
		}else{
			//MyLogger.Logger.log(Level.WARNING,"parametro de BD no esta:::"+key);
		}
	}
	
	public void setHtml(String key,String value){
		if(value==null){
			value="N/A";
		}else if(value.trim().equalsIgnoreCase("null")){
			value="N/A";
		}
		if(this.html!=null){
		this.html=this.html.replace(key, value);
		}else{
			//MyLogger.Logger.log(Level.WARNING,"parametro de BD no esta:::"+key);
		}
	}
	
}
