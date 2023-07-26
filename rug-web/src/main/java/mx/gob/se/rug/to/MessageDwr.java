package mx.gob.se.rug.to;

public class MessageDwr {
	
	private Integer codeError=new Integer(0);
	private String message;
	private Object data;
	
	public Integer getCodeError() {
		return codeError;
	}
	public void setCodeError(Integer codeError) {
		this.codeError = codeError;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
