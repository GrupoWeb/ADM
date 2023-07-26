package gt.gob.rgm.adm.solr;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
public class SolrField {

	public static final String ID_PERSONA = "id";
	public static final String NOMBRE = "nombre";
	public static final String TIPO = "tipo";
	public static final String NIT = "nit";
	public static final String DOCID = "docid";
	public static final String EMAIL = "email";
	
	@XmlAttribute
	String name;

	@XmlValue
	String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
