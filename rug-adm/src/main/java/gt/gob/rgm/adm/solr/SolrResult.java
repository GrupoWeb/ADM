package gt.gob.rgm.adm.solr;

import java.io.Serializable;

public class SolrResult implements Serializable {
	SolrResponseHeader responseHeader;
	SolrResponse response;
	
	public SolrResult() {
	}

	public SolrResponseHeader getResponseHeader() {
		return responseHeader;
	}

	public void setResponseHeader(SolrResponseHeader responseHeader) {
		this.responseHeader = responseHeader;
	}

	public SolrResponse getResponse() {
		return response;
	}

	public void setResponse(SolrResponse response) {
		this.response = response;
	}
}
