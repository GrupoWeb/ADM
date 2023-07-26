package mx.gob.se.rug.to;

import java.io.Serializable;

public class PlSql implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer intPl;
	private String  strPl;
	private Integer resIntPl;
	private String  resStrPl;
	private String resFolio;
	private boolean bolres;
	
	
	public Integer getIntPl() {
		return intPl;
	}
	public void setIntPl(Integer intPl) {
		this.intPl = intPl;
	}
	public String getStrPl() {
		return strPl;
	}
	public void setStrPl(String strPl) {
		this.strPl = strPl;
	}
	public Integer getResIntPl() {
		return resIntPl;
	}
	public void setResIntPl(Integer resIntPl) {
		this.resIntPl = resIntPl;
	}
	public String getResStrPl() {
		return resStrPl;
	}
	public void setResStrPl(String resStrPl) {
		this.resStrPl = resStrPl;
	}
	public void setBolres(boolean bolres) {
		this.bolres = bolres;
	}
	public boolean isBolres() {
		return bolres;
	}
	public String getResFolio() {
		return resFolio;
	}
	public void setResFolio(String resFolio) {
		this.resFolio = resFolio;
	}
	
	
}
