/**
 * 
 */
package mx.gob.se.rug.seguridad.serviceimpl;

import java.util.logging.Level;

import mx.gob.se.rug.seguridad.dao.MenuDAO;
import mx.gob.se.rug.seguridad.service.MenusService;
import mx.gob.se.rug.seguridad.to.MenuTO;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;

/**
 * @author prove.desa
 * 
 */
public class MenusServiceImpl implements MenusService {

	private MenuDAO menuDAO;

	private MenuTO menuTO;

	/**
	 * @param menuDAO
	 *            the menuDAO to set
	 */
	public void setMenuDAO(MenuDAO menuDAO) {
		this.menuDAO = menuDAO;
	}

	/**
	 * @param menuTO
	 *            the menuTO to set
	 */
	public void setMenuTO(MenuTO menuTO) {
		this.menuTO = menuTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mx.gob.se.rug.seguridad.service.MenusService#cargaMenuPrincipal(mx.gob
	 * .se.rug.seguridad.to.MenuTO, mx.gob.se.rug.to.UsuarioTO)
	 */
	@Override
	public MenuTO cargaMenuPrincipal(MenuTO menu, UsuarioTO usuario) {
		MyLogger.Logger.log(Level.INFO,"Start  Menu Principal on Menu Service...");

		try {
			return this.menuTO = this.menuDAO.getMenuPrincipal(menu, usuario);
		} catch (Exception e) {
			MyLogger.Logger.log(Level.INFO,"Exeption on carga menu principal service"+ e.getStackTrace());
			return null;
		}
		
	}

	@Override
	public MenuTO cargaMenuSecundario(Integer idAcreedor,UsuarioTO usuario, MenuTO menu, Boolean vigencia) {
		try {

		 return	this.menuTO = this.menuDAO.getMenuSub(idAcreedor, usuario, menu, vigencia);
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}
		
	}


	public MenuTO cargaMenuSecundario(Integer idAcreedor,UsuarioTO usuario, MenuTO menu, Boolean vigencia, Integer idTramite, Integer idGarantia) {
		MyLogger.Logger.log(Level.INFO,"Menu Secundario Nuevo " + vigencia);
		try {

		 return	this.menuTO = this.menuDAO.getMenuSub(idAcreedor, usuario, menu, vigencia, idTramite, idGarantia);
		} catch (Exception e) {
			e.printStackTrace();
			MyLogger.Logger.log(Level.INFO,"Exception on carga menu Secundario service"
					+ e.getStackTrace());
			return null;
		}

	}
	
	@Override
	public MenuTO cargaMenuAnotacionSinGarantia(UsuarioTO usuario, MenuTO menu
			) {
		MyLogger.Logger.log(Level.INFO,"Start Menu Anotacion Sin Garantia Service");
		try {
		 return	this.menuTO = this.menuDAO.getMenuAnotacionSinGarantia(usuario, menu);
		} catch (Exception e) {
			MyLogger.Logger.log(Level.INFO,"Exception on carga menu Anotacion Sin Garantia service"
					+ e.getStackTrace());
			return null;
		}
		
	}
	
	public boolean cargaMenuJudicial(UsuarioTO usuario) {
		return this.menuDAO.getJudicialMenu(usuario.getPersona().getIdPersona());
	}
	

}
