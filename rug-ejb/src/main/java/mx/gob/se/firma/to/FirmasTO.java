package mx.gob.se.firma.to;

import mx.gob.se.firma.co.to.MessageResponse;


public class FirmasTO {
	private String stringOriginalUser;
	private String signUser;
	private String certUser;
	
	private String tsHuman;
	
	private String stringOriginalCentral;
	private String signCentral;
	private String certNumber;
	private String certCentral;
	
	
	private MessageResponse mr;
	
	
	public String getStringOriginalUser() {
		return stringOriginalUser;
	}
	public void setStringOriginalUser(String stringOriginalUser) {
		this.stringOriginalUser = stringOriginalUser;
	}
	public String getSignUser() {
		return signUser;
	}
	public void setSignUser(String signUser) {
		this.signUser = signUser;
	}
	public String getCertUser() {
		return certUser;
	}
	public void setCertUser(String certUser) {
		this.certUser = certUser;
	}
	public String getTsHuman() {
		return tsHuman;
	}
	public void setTsHuman(String tsHuman) {
		this.tsHuman = tsHuman;
	}
	public String getStringOriginalCentral() {
		return stringOriginalCentral;
	}
	public void setStringOriginalCentral(String stringOriginalCentral) {
		this.stringOriginalCentral = stringOriginalCentral;
	}
	public String getSignCentral() {
		return signCentral;
	}
	public void setSignCentral(String signCentral) {
		this.signCentral = signCentral;
	}
	public String getCertNumber() {
		return certNumber;
	}
	public void setCertNumber(String certNumber) {
		this.certNumber = certNumber;
	}
	public String getCertCentral() {
		return certCentral;
	}
	public void setCertCentral(String certCentral) {
		this.certCentral = certCentral;
	}
	public MessageResponse getMr() {
		return mr;
	}
	public void setMr(MessageResponse mr) {
		this.mr = mr;
	}
	
	

}
