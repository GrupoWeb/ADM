package gt.gob.rgm.adm.solr;

import java.io.Serializable;

public class SolrResponseHeader implements Serializable {
	Integer status;
	Double QTime;
	
	public SolrResponseHeader() {
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getQTime() {
		return QTime;
	}

	public void setQTime(Double qTime) {
		QTime = qTime;
	}
}
