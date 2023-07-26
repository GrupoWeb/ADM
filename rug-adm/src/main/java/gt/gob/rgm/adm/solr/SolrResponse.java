package gt.gob.rgm.adm.solr;

import java.io.Serializable;
import java.util.List;

public class SolrResponse implements Serializable {
	Long numFound;
	Integer start;
	List<SolrResponseDoc> docs;
	
	public SolrResponse() {
	}

	public Long getNumFound() {
		return numFound;
	}

	public void setNumFound(Long numFound) {
		this.numFound = numFound;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public List<SolrResponseDoc> getDocs() {
		return docs;
	}

	public void setDocs(List<SolrResponseDoc> docs) {
		this.docs = docs;
	}
}
