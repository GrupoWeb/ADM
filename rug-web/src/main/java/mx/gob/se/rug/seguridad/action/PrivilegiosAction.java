/**
 * 
 */
package mx.gob.se.rug.seguridad.action;

import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;



import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.seguridad.serviceimpl.PrivilegiosServiceImpl;
import mx.gob.se.rug.seguridad.to.PrivilegiosTO;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;

/**
 * @author prove.desa
 *
 */
public class PrivilegiosAction extends RugBaseAction implements Serializable {
	
	private PrivilegiosServiceImpl privilegiosService;
	
	
	/**
	 * @param privilegiosService the privilegiosService to set
	 */
	public void setPrivilegiosService(PrivilegiosServiceImpl privilegiosService) {
		this.privilegiosService = privilegiosService;
	}


	public String execute(){
		MyLogger.Logger.log(Level.INFO,"START PRIVILEGIOS ACTION....."+this.privilegiosService.toString());
		MyLogger.Logger.log(Level.INFO,"Class.........." + this.privilegiosService.getClass());
		UsuarioTO usuario = (UsuarioTO)sessionMap.get(Constants.USUARIO);
		MyLogger.Logger.log(Level.INFO,"User in Session..." + usuario.getPersona().getIdPersona());
		PrivilegiosTO privilegiosTO = new PrivilegiosTO();
		privilegiosTO.setIdRecurso(new Integer(1));
	
		privilegiosTO=	(PrivilegiosTO)this.privilegiosService.cargaPrivilegios(privilegiosTO, usuario);
		
		MyLogger.Logger.log(Level.INFO,"Privilegios......ok" + privilegiosTO.getPrivilegioTOs().toString());
		return SUCCESS;
	}
	
	public String menu(){
		return SUCCESS;
	}

	
}
