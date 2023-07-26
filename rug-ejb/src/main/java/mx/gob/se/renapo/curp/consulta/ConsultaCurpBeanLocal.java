package mx.gob.se.renapo.curp.consulta;

import javax.ejb.Local;

import mx.gob.se.renapo.curp.consulta.exception.NoCurpFound;
import mx.gob.se.renapo.curp.consulta.exception.NoServiceRenapoAvailable;

@Local
public interface ConsultaCurpBeanLocal {
	public ResponseCurp consultaCurp(String curp) throws NoCurpFound, NoServiceRenapoAvailable;

}





