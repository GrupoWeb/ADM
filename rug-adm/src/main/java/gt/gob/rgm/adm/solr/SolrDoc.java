package gt.gob.rgm.adm.solr;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class SolrDoc {
	@XmlElement(name="field")
	List<SolrField> fields;

	public List<SolrField> getFields() {
		return fields;
	}

	public void setFields(List<SolrField> fields) {
		this.fields = fields;
	}
}
