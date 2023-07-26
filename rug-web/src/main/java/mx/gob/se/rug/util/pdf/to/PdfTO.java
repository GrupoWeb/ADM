package mx.gob.se.rug.util.pdf.to;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.util.CharSetUtil;
import mx.gob.se.rug.util.MyLogger;

public class PdfTO {

    private String html;
    private String pathRoot;
    private Integer idTramite;
    private byte file[];
    private String key;
    private String save;
    private Integer idTipoGarantia;
    private String TypeValue;
    private List<String> htmlList;
    private String massive = "False";
    private Integer idGarantiaTO;

    public String getMassive() {
        return massive;
    }

    public void setMassive(String massive) {
        this.massive = massive;
    }

    public List<String> getHtmlList() {
        return htmlList;
    }

    public void setHtmlList(List<String> htmlList) {
        this.htmlList = htmlList;
    }

    
    public String getHtml() {
        return html;
    }

    public String getTypeValue() {
        return TypeValue;
    }

    public void setTypeValue(String TypeValue) {
        this.TypeValue = TypeValue;
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

    public void setValue(String key, String value) {
        if (value == null) {
            value = "N/A";
        } else if (value.trim().equalsIgnoreCase("null")) {
            value = "N/A";
        }
        if (this.html != null) {
            CharSetUtil charSetUtil = new CharSetUtil();
            value = "<![CDATA[" + charSetUtil.longitudMaximaPorPalabra(value, Constants.MAX_LENGHT_LINE) + "]]>";
            this.html = this.html.replace(key, value);
        } else {
            MyLogger.Logger.log(Level.WARNING, "parametro de BD no esta:::" + key);
        }
    }

    public void setValueSmall(String key, String value) {
        if (value == null) {
            value = "N/A";
        } else if (value.trim().equalsIgnoreCase("null")) {
            value = "N/A";
        }
        if (this.html != null) {
            CharSetUtil charSetUtil = new CharSetUtil();
            value = "<![CDATA[" + charSetUtil.longitudMaximaPorPalabra(value, Constants.MAX_LENGHT_LINE_SMALL) + "]]>";
            this.html = this.html.replace(key, value);
        } else {
            MyLogger.Logger.log(Level.WARNING, "parametro de BD no esta:::" + key);
        }
    }

    public void setHtml(String key, String value) {
        if (value == null) {
            value = "N/A";
        } else if (value.trim().equalsIgnoreCase("null")) {
            value = "N/A";
        }
        if (this.html != null) {
//            List<String> Lista = new ArrayList<String>();
//            Lista.add(this.html.replace(key, value));
//            this.setHtmlList(Lista);
//            System.out.println("Lista: " + Lista);
            this.html = this.html.replace(key, value);
        } else {
            MyLogger.Logger.log(Level.WARNING, "parametro de BD no esta:::" + key);
        }
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
        this.file = file.clone();
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

    public Integer getIdGarantiaTO() {
        return idGarantiaTO;
    }

    public void setIdGarantiaTO(Integer idGarantiaTO) {
        this.idGarantiaTO = idGarantiaTO;
    }
}
