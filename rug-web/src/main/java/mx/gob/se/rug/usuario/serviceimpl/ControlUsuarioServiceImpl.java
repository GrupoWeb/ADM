package mx.gob.se.rug.usuario.serviceimpl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import mx.gob.se.rug.acreedores.dao.AcreedoresDAO;
import mx.gob.se.rug.acreedores.dao.impl.AcreedoresDaoJdbcImpl;
import mx.gob.se.rug.acreedores.to.UsuarioTO;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.usuario.dao.ControlUsuarioDao;
import mx.gob.se.rug.usuario.dao.impl.ControlUsuarioDaoJdbcImpl;
import mx.gob.se.rug.usuario.service.ControlUsuarioService;
import mx.gob.se.rug.util.MyLogger;

public class ControlUsuarioServiceImpl implements ControlUsuarioService {
	@Override
	public boolean estanRelacionados(Integer idPersona, Integer idAcreedor) {
		// TODO Auto-generated method stub
		boolean regresa = false;
		ConexionBD bd = new ConexionBD(); 
		Connection connection = bd.getConnection();
		try{
			bd = new ConexionBD();
			AcreedoresDAO acreedoresDAO = new AcreedoresDaoJdbcImpl();
			regresa = acreedoresDAO.sonAsociados(connection, idPersona, idAcreedor);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			bd.close(connection, null, null);
		}
		
		return regresa;
		
	}
	
	@Override
	public boolean saveUsuario(UsuarioTO usuarioTO, Integer idPersona, String operacion) {
		boolean regresa = false;


		try{
			
			ControlUsuarioDao usuarioDao = new ControlUsuarioDaoJdbcImpl();
			regresa = usuarioDao.saveUsuario(usuarioTO, idPersona, operacion);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return regresa;
	}

	@Override
	public List<UsuarioTO> getListaUsuariosFirmados(Integer idPersona,
			Integer idAcreedor) {
		// TODO Auto-generated method stub
		List <UsuarioTO> lista = new ArrayList<UsuarioTO>();
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		try{
			MyLogger.Logger.log(Level.INFO, "Entro a getLista usuaroi"+ idPersona +"-" + idAcreedor);
			ControlUsuarioDao usuarioDao = new ControlUsuarioDaoJdbcImpl();
			lista = usuarioDao.getUsuariosFirmados(connection, idPersona, idAcreedor);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			bd.close(connection, null, null);
		}
		return lista;
	}

	@Override
	public List<UsuarioTO> getListaUsuariosNoFirmados(Integer idPersona,
			Integer idAcreedor) {
		// TODO Auto-generated method stub
		List <UsuarioTO> lista = new ArrayList<UsuarioTO>();
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		try{
			ControlUsuarioDao usuarioDao = new ControlUsuarioDaoJdbcImpl();
			lista = usuarioDao.getUsuariosNoFirmados(connection, idPersona, idAcreedor);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			bd.close(connection, null, null);
		}
		return lista;
	}

	@Override
	public UsuarioTO getByCorreoElectronico(String correoElectronico) {
		// TODO Auto-generated method stub
		UsuarioTO usuarioTO = null;
		ControlUsuarioDao controlUsuarioDao = new ControlUsuarioDaoJdbcImpl();
		ConexionBD bd = new  ConexionBD();
		Connection connection = bd.getConnection();
		try{
			usuarioTO = controlUsuarioDao.getByCorreoElectronico(connection, correoElectronico);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			bd.close(connection, null, null);
		}
		return usuarioTO;
	}
	
	@Override
	public UsuarioTO getByCorreoElectronicoAcreedor(String correoElectronico, String idAcreedor) {
		// TODO Auto-generated method stub
		UsuarioTO usuarioTO = null;
		ControlUsuarioDao controlUsuarioDao = new ControlUsuarioDaoJdbcImpl();
		ConexionBD bd = new  ConexionBD();
		Connection connection = bd.getConnection();
		try{
			usuarioTO = controlUsuarioDao.getByCorreoElectronicoAcredor(connection, correoElectronico, idAcreedor);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			bd.close(connection, null, null);
		}
		return usuarioTO;
	}

	@Override
	public boolean relationUsuario(UsuarioTO usuarioTO, Integer idPersonaHijo, Integer idGrupos) {
		boolean regresa = false;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		try{
			 
			ControlUsuarioDao usuarioDao = new ControlUsuarioDaoJdbcImpl();
			regresa = usuarioDao.relationUsuario(usuarioTO, idPersonaHijo, idGrupos);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			bd.close(connection, null, null);
		}
		
		return regresa;
	}



}
