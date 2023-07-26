package mx.gob.rug.curp.consulta.ws;

import java.io.UnsupportedEncodingException;
import java.nio.channels.NoConnectionPendingException;
import java.util.logging.Level;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.http.NoHttpResponseException;

import mx.gob.se.renapo.curp.consulta.ConsultaCurpBean;
import mx.gob.se.renapo.curp.consulta.ConsultaCurpBeanRemote;
import mx.gob.se.renapo.curp.consulta.ResponseCurp;
import mx.gob.se.renapo.curp.consulta.exception.NoCurpFound;
import mx.gob.se.renapo.curp.consulta.exception.NoServiceRenapoAvailable;
import mx.gob.se.rug.util.MyLogger;

public class RenapoConsultaCurp {
	
	private ConsultaCurpBeanRemote getBean() throws NoCurpFound, NoServiceRenapoAvailable {
		ConsultaCurpBeanRemote curpBean = null;
		InitialContext ctx;
		try {
			ctx = new InitialContext();
			curpBean = (ConsultaCurpBeanRemote) ctx.lookup("ejb/CurpBeanJNDI");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return curpBean;
	}
	
	public ResponseCurp getDataFromCurp(String curp) throws NoCurpFound, NoServiceRenapoAvailable{

			ResponseCurp  responseCurp = getBean().consultaCurp(curp);
			return responseCurp;
	
	}

}
