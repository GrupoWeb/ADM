package gt.gob.rgm.adm.solr;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="add")
@XmlAccessorType(XmlAccessType.FIELD)
public class SolrAdd {
	
	@XmlElement(name="doc")
	List<SolrDoc> docs;
	
	public SolrAdd() {
		docs = new ArrayList<SolrDoc>();
	}

	public List<SolrDoc> getDocs() {
		return docs;
	}

	public void setDocs(List<SolrDoc> docs) {
		this.docs = docs;
	}
}
