package mx.gob.se.rug.util.action;

import mx.gob.economia.dgi.framework.dwr.action.AbstractBaseDwrAction;
import mx.gob.se.rug.util.dao.CodigoPostalDAO;
import mx.gob.se.rug.util.to.CodigoPostalTO;

public class DireccionesDwrAction extends AbstractBaseDwrAction {
	private CodigoPostalTO codigoPostalTO;
	public String codigoPostal(String codigoPostal){
		String regresa = "failed";
		try{
			CodigoPostalDAO codigoPostalDAO = new CodigoPostalDAO();
			CodigoPostalTO codigoPostalTO = codigoPostalDAO.getByCodigoPostal(codigoPostal);
			regresa = "success";
			setCodigoPostalTO(codigoPostalTO);
		}catch(Exception e){
			e.printStackTrace();
		}
		return regresa;
	}
	public void setCodigoPostalTO(CodigoPostalTO codigoPostalTO) {
		this.codigoPostalTO = codigoPostalTO;
	}
	public CodigoPostalTO getCodigoPostalTO() {
		return codigoPostalTO;
	}
	
}
