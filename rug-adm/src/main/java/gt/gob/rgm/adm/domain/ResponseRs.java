package gt.gob.rgm.adm.domain;

import java.io.Serializable;

public class ResponseRs implements Serializable {
	Object data;
	Long total;
	
	public ResponseRs() {
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}
}
