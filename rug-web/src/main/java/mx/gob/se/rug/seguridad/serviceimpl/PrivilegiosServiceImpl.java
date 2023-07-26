package mx.gob.se.rug.seguridad.serviceimpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

//import com.sun.xml.rpc.processor.modeler.j2ee.xml.trueFalseType;

import mx.gob.se.rug.seguridad.dao.PrivilegiosDAO;
import mx.gob.se.rug.seguridad.service.PrivilegiosService;
import mx.gob.se.rug.seguridad.to.PrivilegioTO;
import mx.gob.se.rug.seguridad.to.PrivilegiosTO;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;

public class PrivilegiosServiceImpl implements PrivilegiosService {

	private PrivilegiosDAO privilegiosDAO;

	private PrivilegiosTO privilegiosTO;

	/**
	 * @param privilegiosDAO
	 *            the privilegiosDAO to set
	 */
	public void setPrivilegiosDAO(PrivilegiosDAO privilegiosDAO) {
		this.privilegiosDAO = privilegiosDAO;
	}

	/**
	 * @param privilegiosTO
	 *            the privilegiosTO to set
	 */
	public void setPrivilegiosTO(PrivilegiosTO privilegiosTO) {
		this.privilegiosTO = privilegiosTO;
	}

	@Override
	public PrivilegiosTO cargaPrivilegios(PrivilegiosTO privilegios,
			UsuarioTO usuario) {
		try {
			this.privilegiosTO = this.privilegiosDAO.getPrivilegios(
					privilegios, usuario);

			for(PrivilegioTO priv : this.privilegiosTO.getPrivilegioTOs()){
				MyLogger.Logger.log(Level.INFO,"Item on list..." + priv.toString());
			}
			
		} catch (Exception e) {
			MyLogger.Logger.log(Level.INFO,"Exception on PrivilegiosService"
					+ e.getMessage());
		}

		return this.privilegiosTO;
	}
	public String getPrivilegios(Integer idPersona) {		
		StringBuffer acs=new StringBuffer();
		
		List<String>  privilegio = new ArrayList<String>();		
		PrivilegiosDAO acsPrivilegio = new PrivilegiosDAO();
		
		privilegio=acsPrivilegio.getPrivilegios(idPersona);
		
		Iterator<String> iterator= privilegio.iterator();
		
		while (iterator.hasNext()) {
			String string = (String) iterator.next();
			acs.append(string);
			acs.append("|");
		}
		return acs.toString().substring(0,acs.toString().length()-1);
	}

}
