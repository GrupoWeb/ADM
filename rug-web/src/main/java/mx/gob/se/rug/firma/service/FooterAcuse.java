package mx.gob.se.rug.firma.service;

import java.util.logging.Level;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import mx.gob.se.firma.FooterAcuseBeanRemote;
import mx.gob.se.rug.util.MyLogger;

public class FooterAcuse {

	private FooterAcuseBeanRemote acuseBean;

	public FooterAcuseBeanRemote getAcuseBean() {
		return acuseBean;
	}

	public void setAcuseBean(FooterAcuseBeanRemote acuseBean) {
		this.acuseBean = acuseBean;
	}

	public FooterAcuse() {
		this.acuseBean =null;
		InitialContext ctx;
		try {
			ctx = new InitialContext();
			MyLogger.Logger.log(Level.INFO, "--lookup firma Acuse--");
			acuseBean = (FooterAcuseBeanRemote) ctx.lookup("ejb/FooterAcuseBeanJNDI");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
