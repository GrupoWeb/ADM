package mx.gob.se.renapo.curp.consulta;

import javax.ejb.Remote;

import mx.gob.se.renapo.curp.consulta.exception.NoCurpFound;
import mx.gob.se.renapo.curp.consulta.exception.NoServiceRenapoAvailable;

@Remote
public interface ConsultaCurpBeanRemote {
	public ResponseCurp consultaCurp(String curp)throws NoCurpFound, NoServiceRenapoAvailable;

}



