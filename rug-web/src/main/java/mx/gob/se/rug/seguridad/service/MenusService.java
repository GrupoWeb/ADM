/**
 * 
 */
package mx.gob.se.rug.seguridad.service;

import mx.gob.se.rug.seguridad.to.MenuTO;
import mx.gob.se.rug.to.UsuarioTO;

/**
 * @author prove.desa
 *
 */
public interface MenusService  {
	
	public MenuTO cargaMenuPrincipal( MenuTO menu, UsuarioTO usuario);
	
	public MenuTO cargaMenuSecundario(Integer idAcreedor,UsuarioTO usuario , MenuTO menu, Boolean vigencia);

	public MenuTO cargaMenuAnotacionSinGarantia(UsuarioTO usuario,	MenuTO menu);
	
	public boolean cargaMenuJudicial(UsuarioTO usuario);

}
