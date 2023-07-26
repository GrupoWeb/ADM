package mx.gob.se.rug.inscripcion.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.dao.BaseRugDao;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.dto.Domicilio;
import mx.gob.se.rug.exception.AltaParteException;
import mx.gob.se.rug.exception.InfrastructureException;
import mx.gob.se.rug.inscripcion.to.AltaParteTO;
import mx.gob.se.rug.inscripcion.to.DeudorTO;
import mx.gob.se.rug.inscripcion.to.OtorganteTO;
import mx.gob.se.rug.masiva.exception.CargaMasivaException;
import mx.gob.se.rug.to.PlSql;
import mx.gob.se.rug.util.CharSetUtil;
import mx.gob.se.rug.util.MyLogger;
import org.hibernate.event.spi.SaveOrUpdateEvent;

public class AltaParteDAO extends BaseRugDao{
	
	public boolean esAutoridad(Integer idPersona){
		boolean regresa = false;
		String sql = "SELECT ID_PERFIL FROM V_USUARIO_SESION_RUG WHERE ID_PERSONA = ? AND ID_PERFIL = 4 AND SIT_USUARIO = 'AC'";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			rs = ps.executeQuery();
			if (rs.next()){
				regresa = true;
			}
		} catch (SQLException e) {
			MyLogger.Logger.log(Level.WARNING, "RUG-SQL:AltaParteDao.esAutoridad------Sucedio una exception en el metodo ::::");
			e.printStackTrace();
		} finally{
			bd.close(connection, rs, ps);
		}
		return regresa;
	}
	
	public boolean relParte(Integer idPersona, Integer idTramiteParte, Integer idParte, String rfc){
		boolean regresa = false;
		String sql ="{call RUG.SP_REL_PARTE( ?,?,?,?,?,? ) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs =null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, idPersona);
			cs.setInt(2, idTramiteParte);
			cs.setInt(3, idParte);
			cs.setString(4, rfc);
			cs.registerOutParameter(5, Types.INTEGER);
			cs.registerOutParameter(6, Types.VARCHAR);
			cs.execute();
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:AltaParteDao.relParte------int ::::" + cs.getInt(5));
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:AltaParteDao.relParte------String ::::" + cs.getString(6));
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:AltaParteDao.relParte------String RFC ::::" + rfc);
			if (cs.getInt(4)==0){				
				regresa = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:AltaParteDao.relParte------Sucedio una exception en el metodo ::::");
			e.printStackTrace();
		}finally{
			
			bd.close(connection,null,cs);
		}
		
		return regresa;
	}
	public void relacionaParte(Integer idPersona, Integer idTramiteParte, Integer idParte, String rfc) throws InfrastructureException, CargaMasivaException{
		String sql ="{call RUG.SP_REL_PARTE( ?,?,?,?,?,? ) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs =null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, idPersona);
			cs.setInt(2, idTramiteParte);
			cs.setInt(3, idParte);
			cs.setString(4, rfc);
			cs.registerOutParameter(5, Types.INTEGER);
			cs.registerOutParameter(6, Types.VARCHAR);
			cs.execute();
			if (cs.getInt(5)!=0){
				throw new CargaMasivaException(97);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InfrastructureException("SP_REL_PARTE",e);
		}finally{
			bd.close(connection,null,cs);
		}
		
	}
	
	public boolean insertElMismo(Integer idTramite, Integer idOtorgante){
		boolean regresa = false;
		String sql ="{call RUG.SP_AGREGAR_MISMO_DEUDOR( ?,?,?,? ) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs =null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, idTramite);
			cs.setInt(2, idOtorgante);
			cs.registerOutParameter(3, Types.INTEGER);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.execute();
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:AltaParteDao.insertElMismo------int ::::" + cs.getInt(3));
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:AltaParteDao.insertElMismo------String ::::" + cs.getString(4));
			if (cs.getInt(3)==0){
				regresa = true;
			}			
		} catch (SQLException e) {
			MyLogger.Logger.log(Level.WARNING, "RUG-SQL:AltaParteDao.insertElMismo------Sucedio una exception en el metodo ::::");
			e.printStackTrace();
		}finally{
			bd.close(connection,null,cs);
		}
		return regresa;
	}

//=========SP_AltaParte_Ac
	
	public PlSql AltaParteAc(AltaParteTO altaParteTO){
		PlSql regresa = new PlSql();
		String sql = "{call RUG.SP_AltaParte_Ac ( " +
				" ?,?,?,?,?, " +
				" ?,?,?,?,?, " +
				" ?,?,?,?,?," +
				" ?,?,?,?,?," +
				" ?,?,?,?,?" +
				" ,?,?"+
				" ,?,?,?,?,?) }";	 
		ConexionBD conexionBD = new ConexionBD();
		Connection connection = conexionBD.getConnection();
		CallableStatement cs =null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, altaParteTO.getIdTramite());
			cs.setInt(2, altaParteTO.getIdParte());
			cs.setString(3, altaParteTO.getTipo());
			cs.setString(4, altaParteTO.getTipoPersona());
			cs.setString(5, altaParteTO.getRazonSocial());
			cs.setString(6, altaParteTO.getNombre());
			cs.setString(7, altaParteTO.getApellidoPaterno());
			cs.setString(8, altaParteTO.getApellidoMaterno());	
			setStringCS(cs,altaParteTO.getFolioMercantil(),9);
			setStringCS(cs,altaParteTO.getRfc(),10);
			setStringCS(cs,altaParteTO.getCurp(),11);
			cs.setString(12, altaParteTO.getHayDomicilio());			
			setStringCS(cs,altaParteTO.getCalle(),13);
			setStringCS(cs,altaParteTO.getNumeroExterior(),14);
			setStringCS(cs,altaParteTO.getNumeroInterior(),15);			
			setIntCS(cs, altaParteTO.getIdColonia(), 16);
			setIntCS(cs, altaParteTO.getIdLocalidad(), 17);			
			cs.setInt(18, altaParteTO.getIdPersona());
			cs.setInt(19, altaParteTO.getIdNacionalidad());			
			setStringCS(cs,altaParteTO.getTelefono(),20);
			setStringCS(cs,altaParteTO.getExtencion(),21);
			setStringCS(cs,altaParteTO.getCorreoElectronico(),22);
			setStringCS(cs,altaParteTO.getDomicilioUno(),23);
			setStringCS(cs,altaParteTO.getDomicilioDos(),24);
			setStringCS(cs,altaParteTO.getPoblacion(),25);
			setStringCS(cs,altaParteTO.getZonaPostal(),26);	
			setIntCS(cs, altaParteTO.getIdPaisResidencia(), 27);
			cs.registerOutParameter(28, Types.INTEGER);
			cs.registerOutParameter(29, Types.INTEGER);
			cs.registerOutParameter(30, Types.VARCHAR);
			cs.registerOutParameter(31, Types.VARCHAR);
			setStringCS(cs,"F",32);
			cs.execute();
			regresa.setIntPl(cs.getInt(29));
			regresa.setStrPl(cs.getString(30));
			regresa.setResIntPl(cs.getInt(28));
			regresa.setResStrPl(cs.getString(31));
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:AltaParteDao.insert------int 1 ::::" + cs.getInt(29));
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:AltaParteDao.insert------String ::::" + cs.getString(30));
		} catch (SQLException e) {
			regresa.setIntPl(999);
			regresa.setStrPl(e.getMessage()+", "+ e.getCause());
			regresa.setResIntPl(0);
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:AltaParteDao.insert------Sucedio una exception en el metodo ::::");
			e.printStackTrace();
		} finally{
			conexionBD.close(connection,null,cs);
		}
		return regresa;
	}
	
	
//==============================================
	
	
	
	//Inicia nuevo metodo alta parte acreedor adicional
	
	public PlSql insertAcreedorAD(AltaParteTO altaParteTO){
		PlSql regresa = new PlSql();
		String sql = "{call RUG.SP_AltaParte_Ac ( " +
				" ?,?,?,?,?,?, " +
				" ?,?,?,?,?, " +
				" ?,?,?,?,?," +
				" ?,?,?,?,?," +
				" ?,?,?,?" +
				" ,?"+
				" ,?,?,?,?,?) }";	 
		ConexionBD conexionBD = new ConexionBD();
		Connection connection = conexionBD.getConnection();
		CallableStatement cs =null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, altaParteTO.getIdTramite());
			cs.setInt(2, altaParteTO.getIdParte());
			cs.setString(3, altaParteTO.getTipo());
			cs.setString(4, altaParteTO.getTipoPersona());
			cs.setString(5, altaParteTO.getRazonSocial());
			cs.setString(6, altaParteTO.getNombre());
			cs.setString(7, altaParteTO.getApellidoPaterno());
			cs.setString(8, altaParteTO.getApellidoMaterno());	
			setStringCS(cs,altaParteTO.getFolioMercantil(),9);
			setStringCS(cs,altaParteTO.getRfc(),10);
			setStringCS(cs,altaParteTO.getCurp(),11);
			cs.setString(12, altaParteTO.getHayDomicilio());			
			setStringCS(cs,altaParteTO.getCalle(),13);
			setStringCS(cs,altaParteTO.getNumeroExterior(),14);
			setStringCS(cs,altaParteTO.getNumeroInterior(),15);			
			setIntCS(cs, altaParteTO.getIdColonia(), 16);
			setIntCS(cs, altaParteTO.getIdLocalidad(), 17);			
			cs.setInt(18, altaParteTO.getIdPersona());
			cs.setInt(19, altaParteTO.getIdNacionalidad());			
			setStringCS(cs,altaParteTO.getTelefono(),20);
			setStringCS(cs,altaParteTO.getExtencion(),21);
			setStringCS(cs,altaParteTO.getCorreoElectronico(),22);
			setStringCS(cs,altaParteTO.getDomicilioUno(),23);
			setStringCS(cs,altaParteTO.getDomicilioDos(),24);
			setStringCS(cs,altaParteTO.getPoblacion(),25);
			setStringCS(cs,altaParteTO.getZonaPostal(),26);	
			setIntCS(cs, altaParteTO.getIdPaisResidencia(), 27);
			cs.registerOutParameter(28, Types.INTEGER);
			cs.registerOutParameter(29, Types.INTEGER);
			cs.registerOutParameter(30, Types.VARCHAR);
			cs.registerOutParameter(31, Types.VARCHAR);
			cs.execute();
			regresa.setIntPl(cs.getInt(29));
			regresa.setStrPl(cs.getString(30));
			regresa.setResIntPl(cs.getInt(28));
			regresa.setResFolio(cs.getString(31));
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:AltaParteDao.insert------int ::::" + cs.getInt(28));
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:AltaParteDao.insert------String ::::" + cs.getString(29));
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:AltaParteDao.insert------String Folio ::::" + cs.getString(31));
		} catch (SQLException e) {
			regresa.setIntPl(999);
			regresa.setStrPl(e.getMessage()+", "+ e.getCause());
			regresa.setResIntPl(0);
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:AltaParteDao.insert------Sucedio una exception en el metodo ::::");
			e.printStackTrace();
		} finally{
			conexionBD.close(connection,null,cs);
		}
		return regresa;
	}
	
	//Inicia nuevo metodo alta parte acreedor adicional
	
	
	public PlSql insert(AltaParteTO altaParteTO){
		PlSql regresa = new PlSql();
		String sql = "{call RUG.SP_AltaParte ( " +
				" ?,?,?,?,?," +
				" ?,?,?,?,?," +
				" ?,?,?,?,?," +
				" ?,?,?,?,?," +
				" ?,?,?,?,?," +
				" ?,?,?,?,?," +
				" ?,?,?,?,?," +
				" ?) }";	 
		ConexionBD conexionBD = new ConexionBD();
		Connection connection = conexionBD.getConnection();
		CallableStatement cs =null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, altaParteTO.getIdTramite());
			cs.setInt(2, altaParteTO.getIdParte());
			cs.setString(3, altaParteTO.getTipoPersona());
			cs.setString(4, altaParteTO.getRazonSocial());
			cs.setString(5, altaParteTO.getNombre());
			cs.setString(6, altaParteTO.getApellidoPaterno());
			cs.setString(7, altaParteTO.getApellidoMaterno());	
			setStringCS(cs,altaParteTO.getFolioMercantil(),8);
			setStringCS(cs,altaParteTO.getRfc(),9);
			setStringCS(cs,altaParteTO.getCurp(),10);			
			cs.setString(11, altaParteTO.getHayDomicilio());			
			setStringCS(cs,altaParteTO.getCalle(),12);
			setStringCS(cs,altaParteTO.getNumeroExterior(),13);
			setStringCS(cs,altaParteTO.getNumeroInterior(),14);			
			setIntCS(cs, altaParteTO.getIdColonia(), 15);
			setIntCS(cs, altaParteTO.getIdLocalidad(), 16);			
			cs.setInt(17, altaParteTO.getIdPersona());			
			setIntCS(cs, altaParteTO.getIdNacionalidad(), 18);
			setStringCS(cs,altaParteTO.getTelefono(),19);
			setStringCS(cs,altaParteTO.getExtencion(),20);
			setStringCS(cs,altaParteTO.getCorreoElectronico(),21);
			setStringCS(cs,altaParteTO.getDomicilioUno(),22);
			setStringCS(cs,altaParteTO.getDomicilioDos(),23);
			setStringCS(cs,altaParteTO.getPoblacion(),24);
			setStringCS(cs,altaParteTO.getZonaPostal(),25);	
			setIntCS(cs, altaParteTO.getIdPaisResidencia(), 26);
			setStringCS(cs, altaParteTO.getInscrita(), 27);
			setStringCS(cs, altaParteTO.getFolioInscrito(), 28);
			setStringCS(cs, altaParteTO.getLibroInscrito(), 29);
			setStringCS(cs, altaParteTO.getUbicacionInscrito(), 30);
			setStringCS(cs, altaParteTO.getEdad(), 31);
			setStringCS(cs, altaParteTO.getEstadoCivil(), 32);
			setStringCS(cs, altaParteTO.getProfesion(), 33);
			cs.registerOutParameter(34, Types.INTEGER); //27
			cs.registerOutParameter(35, Types.INTEGER); //28
			cs.registerOutParameter(36, Types.VARCHAR); //29
			cs.execute();
			regresa.setIntPl(cs.getInt(35));
			regresa.setStrPl(cs.getString(36));
			regresa.setResIntPl(cs.getInt(34));
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:AltaParteDao.insert------int 2::::" + cs.getInt(35));
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:AltaParteDao.insert------String ::::" + cs.getString(36));
		} catch (SQLException e) {
			regresa.setIntPl(999);
			regresa.setStrPl(e.getMessage()+", "+ e.getCause());
			regresa.setResIntPl(0);
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:AltaParteDao.insert------Sucedio una exception en el metodo ::::");
			e.printStackTrace();
		} finally{
			conexionBD.close(connection,null,cs);
		}
		return regresa;
	}
	
/**
 * @throws AltaParteException 
 * @throws InfrastructureException *************************************************/
	public PlSql registroMorales(AltaParteTO altaParteTO) throws AltaParteException, InfrastructureException{
		PlSql regresa = new PlSql();
		String sql = "{call RUG.SP_AltaParte1 ( " +
				" ?,?,?,?,?, " +
				" ?,?,?,?,?, " +
				" ?,?,?,?,?," +
				" ?,?,?,?,?," +
				" ?,?,?,?" +
				" ,?"+
				" ,?,?,?,?,?) }";	 
		ConexionBD conexionBD = new ConexionBD();
		Connection connection =null;
		CallableStatement cs =null;
		try {
			connection = conexionBD.getConnection();
			cs = connection.prepareCall(sql);
			cs.setInt(1, altaParteTO.getIdTramite());
			cs.setInt(2, altaParteTO.getIdParte());
			cs.setString(3, altaParteTO.getTipoPersona());
			cs.setString(4, altaParteTO.getTipo());
			cs.setString(5, altaParteTO.getRazonSocial());
			cs.setString(6, altaParteTO.getNombre());
			cs.setString(7, altaParteTO.getApellidoPaterno());
			cs.setString(8, altaParteTO.getApellidoMaterno());	
			setStringCS(cs,altaParteTO.getFolioMercantil(),9);
			setStringCS(cs,altaParteTO.getRfc(),10);
			setStringCS(cs,altaParteTO.getCurp(),11);
			cs.setString(12, altaParteTO.getHayDomicilio());
			setStringCS(cs,altaParteTO.getCalle(),13);
			setStringCS(cs,altaParteTO.getNumeroExterior(),14);
			setStringCS(cs,altaParteTO.getNumeroInterior(),15);
			setIntCS(cs, altaParteTO.getIdColonia(), 16);
			setIntCS(cs, altaParteTO.getIdLocalidad(), 17);
			cs.setInt(18, altaParteTO.getIdPersona());
			cs.setInt(19, altaParteTO.getIdNacionalidad());
			setStringCS(cs,altaParteTO.getTelefono(),20);
			setStringCS(cs,altaParteTO.getExtencion(),21);
			setStringCS(cs,altaParteTO.getCorreoElectronico(),22);
			setStringCS(cs,altaParteTO.getDomicilioUno(),23);
			setStringCS(cs,altaParteTO.getDomicilioDos(),24);
			setStringCS(cs,altaParteTO.getPoblacion(),25);
			setStringCS(cs,altaParteTO.getZonaPostal(),26);
			setIntCS(cs, altaParteTO.getIdPaisResidencia(), 27);
			cs.registerOutParameter(28, Types.INTEGER);
			cs.registerOutParameter(29, Types.INTEGER);
			cs.registerOutParameter(30, Types.VARCHAR);
			cs.execute();
			regresa.setResIntPl(cs.getInt(28));
			regresa.setIntPl(cs.getInt(29));
			regresa.setStrPl(cs.getString(30));
			
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:AltaParteDao.insert------int 3::::" + cs.getInt(29));
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:AltaParteDao.insert------String ::::" + cs.getString(30));
			
			if(cs.getInt(29)!=0){
				throw new AltaParteException(cs.getInt(29), cs.getString(30));
			}
			
			
		} catch (SQLException e) {
			regresa.setIntPl(999);
			regresa.setStrPl(e.getMessage()+", "+ e.getCause());
			regresa.setResIntPl(0);
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:AltaParteDao.insert------Sucedio una exception en el metodo ::::");
			e.printStackTrace();
			throw new InfrastructureException(e);
		} finally{
			conexionBD.close(connection,null,cs);
		}
		return regresa;
	}
/***************************************************/	
	
	public PlSql modificaMoralesRegistro(AltaParteTO altaParteTO) throws AltaParteException, InfrastructureException{
		PlSql regresa = new PlSql();
		MyLogger.Logger.log(Level.INFO," ENTRA A  aLTAPARTEDAO.modificaMoralesRegistro");
		String sql = "{call RUG.SP_MODIFICA_PARTE ( " +
				" ?,?,?,?,?, " +
				" ?,?,?,?,?, " +
				" ?,?,?,?,?," +
				" ?,?,?,?,?," +
				" ?,?,?,?" +
				" ,?)}";	 
		ConexionBD conexionBD = new ConexionBD();
		Connection connection =null;
		CallableStatement cs =null;
		try {
			connection = conexionBD.getConnection();
			cs = connection.prepareCall(sql);
			
			cs.setInt(1, altaParteTO.getIdPersona());
			cs.setInt(2, altaParteTO.getIdTramite());
			cs.setInt(3, 0);
			cs.setInt(4, altaParteTO.getIdParte());
			cs.setString(5, altaParteTO.getTipoPersona());
			cs.setString(6, altaParteTO.getRazonSocial());
			cs.setString(7, altaParteTO.getNombre());
			cs.setString(8, altaParteTO.getApellidoPaterno());
			cs.setString(9, altaParteTO.getApellidoMaterno());
			setStringCS(cs,altaParteTO.getFolioMercantil(),10);
			setStringCS(cs,altaParteTO.getRfc(),11);
			setStringCS(cs,altaParteTO.getCurp(),12);
			//setStringCS(cs,altaParteTO.getNif(),12);
			
			cs.setString(13, altaParteTO.getHayDomicilio());
			setStringCS(cs,altaParteTO.getCalle(),14);
			setStringCS(cs,altaParteTO.getNumeroExterior(),15);
			setStringCS(cs,altaParteTO.getNumeroInterior(),16);
			setIntCS(cs, altaParteTO.getIdColonia(), 17);
			setIntCS(cs, altaParteTO.getIdLocalidad(), 18);
			
			cs.setInt(19, 0);
			cs.setInt(20, altaParteTO.getIdNacionalidad());
				
			setStringCS(cs,altaParteTO.getTelefono(),21);
			setStringCS(cs,altaParteTO.getExtencion(),22);
			setStringCS(cs,altaParteTO.getCorreoElectronico(),23);
			
			cs.registerOutParameter(24, Types.INTEGER);
			cs.registerOutParameter(25, Types.VARCHAR);
			cs.execute();
			
			regresa.setIntPl(cs.getInt(24));
			regresa.setStrPl(cs.getString(25));
			
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:modificaMoralesRegistro.update!------int ::::" + cs.getInt(24));
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:modificaMoralesRegistro.update!------String ::::" + cs.getString(25));
			
			if(cs.getInt(24)!=0){
				throw new AltaParteException(cs.getInt(24), cs.getString(25));
			}
			
			
		} catch (SQLException e) {
			regresa.setIntPl(999);
			regresa.setStrPl(e.getMessage()+", "+ e.getCause());
			regresa.setResIntPl(0);
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:modificaMoralesRegistro.update------Sucedio una exception en el metodo ::::");
			e.printStackTrace();
			throw new InfrastructureException(e);
		} finally{
			conexionBD.close(connection,null,cs);
		}
		return regresa;
	}
/***************************************************/	
	
	
	public void excuteAltaParte(AltaParteTO altaParteTO) throws CargaMasivaException, InfrastructureException{
		PlSql regresa = new PlSql();
		String sql = "{call RUG.SP_AltaParte ( " +
				" ?,?,?,?,?, " +
				" ?,?,?,?,?, " +
				" ?,?,?,?,?," +
				" ?,?,?,?,?," +
				" ?,?,?,?" +
				" ,?"+
				" ,?,?,?,?) }";	 
		ConexionBD conexionBD = new ConexionBD();
		Connection connection = conexionBD.getConnection();
		CallableStatement cs =null;
		try {
			cs = connection.prepareCall(sql);
			
			resetIndexData();
			
			setDataInPreparedStatemet(altaParteTO.getIdTramite(), cs);
			setDataInPreparedStatemet(altaParteTO.getIdParte(), cs);
			setDataInPreparedStatemet(altaParteTO.getTipoPersona(), cs);
			setDataInPreparedStatemet(altaParteTO.getRazonSocial(), cs);
			setDataInPreparedStatemet(altaParteTO.getNombre(), cs);
			setDataInPreparedStatemet(altaParteTO.getApellidoPaterno(), cs);
			setDataInPreparedStatemet(altaParteTO.getApellidoMaterno(), cs);
			setDataInPreparedStatemet(altaParteTO.getFolioMercantil(), cs);
			setDataInPreparedStatemet(altaParteTO.getRfc(), cs);
			setDataInPreparedStatemet(altaParteTO.getCurp(), cs);
			//setDataInPreparedStatemet(altaParteTO.getNif(), cs);
			setDataInPreparedStatemet(altaParteTO.getHayDomicilio(), cs);
			setDataInPreparedStatemet(altaParteTO.getCalle(), cs);
			setDataInPreparedStatemet(altaParteTO.getNumeroExterior(), cs);
			setDataInPreparedStatemet(altaParteTO.getNumeroInterior(), cs);
			setDataInPreparedStatemet(altaParteTO.getIdColonia(), cs);
			setDataInPreparedStatemet(altaParteTO.getIdLocalidad(), cs);
			setDataInPreparedStatemet(altaParteTO.getIdPersona(), cs);
			setDataInPreparedStatemet(altaParteTO.getIdNacionalidad(), cs);
			setDataInPreparedStatemet(altaParteTO.getTelefono(), cs);
			setDataInPreparedStatemet(altaParteTO.getExtencion(), cs);
			setDataInPreparedStatemet(altaParteTO.getCorreoElectronico(), cs);
			setDataInPreparedStatemet(altaParteTO.getDomicilioUno(), cs);
			setDataInPreparedStatemet(altaParteTO.getDomicilioDos(), cs);
			setDataInPreparedStatemet(altaParteTO.getPoblacion(), cs);
			setDataInPreparedStatemet(altaParteTO.getZonaPostal(), cs);
			setDataInPreparedStatemet(altaParteTO.getIdPaisResidencia(), cs);
			
			cs.registerOutParameter(27, Types.INTEGER);
			cs.registerOutParameter(28, Types.INTEGER);
			cs.registerOutParameter(29, Types.VARCHAR);
			cs.execute();
			regresa.setIntPl(cs.getInt(28));
			regresa.setStrPl(cs.getString(29));
			regresa.setResIntPl(cs.getInt(27));
			
			if(cs.getInt(28)==0){
				altaParteTO.setIdPersona(cs.getInt(27));
			}else{
				throw new CargaMasivaException(cs.getInt(28));
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InfrastructureException("No se pudo ejecutar SP_AltaParte",e);
		} finally{
			conexionBD.close(connection,null,cs);
		}
	}
	
	public void excuteAltaParteMasiva(AltaParteTO altaParteTO) throws CargaMasivaException, InfrastructureException{
		PlSql regresa = new PlSql();
		String sql = "{call RUG.SP_AltaParte ( " +
				" ?,?,?,?,?, " +
				" ?,?,?,?,?, " +
				" ?,?,?,?,?," +
				" ?,?,?,?,?," +
				" ?,?,?,?,?," +
				" ?,?,?,?,?," +
				" ?,?,?,?,?," +
				" ?) }";	 
		ConexionBD conexionBD = new ConexionBD();
		Connection connection = conexionBD.getConnection();
		CallableStatement cs =null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, altaParteTO.getIdTramite());
			cs.setInt(2, altaParteTO.getIdParte());
			cs.setString(3, altaParteTO.getTipoPersona());
			cs.setString(4, altaParteTO.getRazonSocial());
			cs.setString(5, altaParteTO.getNombre());
			cs.setString(6, altaParteTO.getApellidoPaterno());
			cs.setString(7, altaParteTO.getApellidoMaterno());	
			setStringCS(cs,altaParteTO.getFolioMercantil(),8);
			setStringCS(cs,altaParteTO.getRfc(),9);
			setStringCS(cs,altaParteTO.getCurp(),10);			
			cs.setString(11, altaParteTO.getHayDomicilio());			
			setStringCS(cs,altaParteTO.getCalle(),12);
			setStringCS(cs,altaParteTO.getNumeroExterior(),13);
			setStringCS(cs,altaParteTO.getNumeroInterior(),14);			
			setIntCS(cs, altaParteTO.getIdColonia(), 15);
			setIntCS(cs, altaParteTO.getIdLocalidad(), 16);			
			cs.setInt(17, altaParteTO.getIdPersona());			
			setIntCS(cs, altaParteTO.getIdNacionalidad(), 18);
			setStringCS(cs,altaParteTO.getTelefono(),19);
			setStringCS(cs,altaParteTO.getExtencion(),20);
			setStringCS(cs,altaParteTO.getCorreoElectronico(),21);
			setStringCS(cs,altaParteTO.getDomicilioUno(),22);
			setStringCS(cs,altaParteTO.getDomicilioDos(),23);
			setStringCS(cs,altaParteTO.getPoblacion(),24);
			setStringCS(cs,altaParteTO.getZonaPostal(),25);	
			setIntCS(cs, altaParteTO.getIdPaisResidencia(), 26);
			setStringCS(cs, altaParteTO.getInscrita(), 27);
			setStringCS(cs, altaParteTO.getFolioInscrito(), 28);
			setStringCS(cs, altaParteTO.getLibroInscrito(), 29);
			setStringCS(cs, altaParteTO.getUbicacionInscrito(), 30);
			setStringCS(cs, altaParteTO.getEdad(), 31);
			setStringCS(cs, altaParteTO.getEstadoCivil(), 32);
			setStringCS(cs, altaParteTO.getProfesion(), 33);
			cs.registerOutParameter(34, Types.INTEGER); //27
			cs.registerOutParameter(35, Types.INTEGER); //28
			cs.registerOutParameter(36, Types.VARCHAR); //29
			
			cs.execute();
			
			regresa.setIntPl(cs.getInt(35));
			regresa.setStrPl(cs.getString(36));
			regresa.setResIntPl(cs.getInt(34));			
			
			if(cs.getInt(35)==0){
				altaParteTO.setIdPersona(cs.getInt(34));
			}else{
				throw new CargaMasivaException(cs.getString(36),cs.getInt(35));
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InfrastructureException("No se pudo ejecutar SP_AltaParte",e);
		} finally{
			conexionBD.close(connection,null,cs);
		}
	}
	
	public List <OtorganteTO> getUltimoOtorganteByGarantia(Integer idGarantia){
		OtorganteTO otorganteTO = null;
		List <OtorganteTO> lista = new ArrayList<OtorganteTO>();
		String sql =" SELECT ID_TRAMITE, ID_GARANTIA, ID_PERSONA, ID_PARTE, DESC_PARTE, PER_JURIDICA, NOMBRE, E_MAIL, CLAVE_PAIS, " +
		" TELEFONO, EXTENSION, FOLIO_MERCANTIL,  RFC FROM   V_GARANTIA_PARTES WHERE   ID_GARANTIA = ? " +
		" AND ID_TRAMITE = (SELECT   ID_TRAMITE    FROM   (  SELECT   ID_TRAMITE" +
		" FROM   V_GARANTIA_PARTES WHERE   ID_GARANTIA = ? ORDER BY   ID_TRAMITE DESC)" +
		"  WHERE   ROWNUM < 2 ) AND ID_PARTE = 1 ";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idGarantia);
			ps.setInt(2, idGarantia);
			rs = ps.executeQuery();
			while (rs.next()){
				otorganteTO = new OtorganteTO();
				otorganteTO.setIdPersona(rs.getInt("ID_PERSONA"));
				otorganteTO.setIdOtorgante(rs.getInt("ID_PERSONA"));
				otorganteTO.setFolioMercantil(rs.getString("FOLIO_MERCANTIL"));
				otorganteTO.setRfc(rs.getString("RFC"));
				otorganteTO.setNombreCompleto(rs.getString("NOMBRE"));
				MyLogger.Logger.log(Level.INFO, "Doc o Curp del otorgante:-"+otorganteTO.getCurp());
				String perJur =  rs.getString("PER_JURIDICA");
				
				String personaJuridica = "Persona Juridica";
				if (perJur.equals("PF")){
					personaJuridica ="Persona Fisica";
				}
				otorganteTO.setTipoPersona(personaJuridica);
				lista.add(otorganteTO);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			MyLogger.Logger.log(Level.WARNING, "RUG-SQL:AltaParteDao.getUltimoOtorganteByGarantia------Sucedio una exception en el metodo ::::");
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
			
		}
		
		return lista;
	}
	
	public List <OtorganteTO> getDeudorByInscripcion(Integer idInscripcion){
		List <OtorganteTO> lista = new ArrayList<OtorganteTO>();
		OtorganteTO otorganteTO = null;
		String sql = "SELECT ID_TRAMITE, ID_PERSONA, DESC_PARTE, PER_JURIDICA," +
				" NOMBRE, NOMBRE_PERSONA, AP_PATERNO_PERSONA, AP_MATERNO_PERSONA, RAZON_SOCIAL, CURP, FOLIO_MERCANTIL, RFC, ID_NACIONALIDAD, CODIGO_POSTAL, CURP_DOC, " +
				" UBICA_DOMICILIO_1, UBICA_DOMICILIO_2, POBLACION, ZONA_POSTAL FROM V_TRAMITES_INCOMP_PARTES " +
				" WHERE ID_TRAMITE = ? AND ID_PARTE = 2";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		PreparedStatement ps = null;
		CharSetUtil csu = new CharSetUtil();
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idInscripcion);
			rs = ps.executeQuery();
			while (rs.next()){
				otorganteTO = new OtorganteTO();
				otorganteTO.setIdPersona(rs.getInt("ID_PERSONA"));
				otorganteTO.setIdOtorgante(rs.getInt("ID_PERSONA"));
				//otorganteTO.setFolioMercantil(new CharSetUtil().longitudMaximaPorPalabra(rs.getString("FOLIO_MERCANTIL"),25));
				otorganteTO.setRfc(rs.getString("RFC"));
				otorganteTO.setNombre(rs.getString("NOMBRE_PERSONA"));
				otorganteTO.setApellidoPaterno(rs.getString("AP_PATERNO_PERSONA"));
				otorganteTO.setApellidoMaterno(rs.getString("AP_MATERNO_PERSONA"));
				otorganteTO.setRazon(rs.getString("RAZON_SOCIAL"));
				otorganteTO.setIdNacionalidad(rs.getInt("ID_NACIONALIDAD"));
				otorganteTO.setNombreCompleto(new CharSetUtil().longitudMaximaPorPalabra(rs.getString("NOMBRE"),25));
				otorganteTO.setCurp(rs.getString("CURP"));
				MyLogger.Logger.log(Level.INFO, "Doc o Curp del otorgante:-"+otorganteTO.getCurp());
				/*String perJur =  rs.getString("PER_JURIDICA");
				
				String personaJuridica = "Persona Juridica";
				if (perJur.equals("PF")){
					personaJuridica ="Persona Fisica";
				}*/
				otorganteTO.setTipoPersona(rs.getString("PER_JURIDICA"));
				lista.add(otorganteTO);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return lista;
	}
	
	public List <OtorganteTO> getOtorganteByInscripcion(Integer idInscripcion){
		List <OtorganteTO> lista = new ArrayList<OtorganteTO>();
		OtorganteTO otorganteTO = null;
		String sql = "SELECT ID_TRAMITE, ID_PERSONA, DESC_PARTE, PER_JURIDICA, UBICADA, " +
				" NOMBRE, NOMBRE_PERSONA, AP_PATERNO_PERSONA, AP_MATERNO_PERSONA, RAZON_SOCIAL, CURP, FOLIO_MERCANTIL, RFC, ID_NACIONALIDAD, CODIGO_POSTAL, CURP_DOC, " +
				" UBICA_DOMICILIO_1, UBICA_DOMICILIO_2, POBLACION, ZONA_POSTAL FROM V_TRAMITES_INCOMP_PARTES " +
				" WHERE ID_TRAMITE = ? AND ID_PARTE = 1";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		PreparedStatement ps = null;
		CharSetUtil csu = new CharSetUtil();
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idInscripcion);
			rs = ps.executeQuery();
			while (rs.next()){
				otorganteTO = new OtorganteTO();
				otorganteTO.setIdPersona(rs.getInt("ID_PERSONA"));
				otorganteTO.setIdOtorgante(rs.getInt("ID_PERSONA"));
				otorganteTO.setRfc(rs.getString("RFC"));
				otorganteTO.setNombre(rs.getString("NOMBRE_PERSONA"));
				otorganteTO.setApellidoPaterno(rs.getString("AP_PATERNO_PERSONA"));
				otorganteTO.setApellidoMaterno(rs.getString("AP_MATERNO_PERSONA"));
				otorganteTO.setRazon(rs.getString("RAZON_SOCIAL"));
				otorganteTO.setIdNacionalidad(rs.getInt("ID_NACIONALIDAD"));
				otorganteTO.setNombreCompleto(rs.getString("NOMBRE"));
				otorganteTO.setCurp(rs.getString("CURP"));
				otorganteTO.setUbicacionInscrito(rs.getString("UBICADA"));	
				otorganteTO.setTipoPersona(rs.getString("PER_JURIDICA"));
				lista.add(otorganteTO);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return lista;
	}
	public OtorganteTO getOtorganteById(Integer idInscripcion, Integer idOtorgante){
		
		OtorganteTO otorganteTO = null;
		String sql = "SELECT ID_TRAMITE, ID_PERSONA, DESC_PARTE, PER_JURIDICA, ID_PAIS_RESIDENCIA," +
				" NOMBRE, NOMBRE_PERSONA, AP_PATERNO_PERSONA, AP_MATERNO_PERSONA, RAZON_SOCIAL, CURP, FOLIO_MERCANTIL, RFC, ID_NACIONALIDAD, CODIGO_POSTAL, CURP_DOC, " +
				" UBICA_DOMICILIO_1, UBICA_DOMICILIO_2, POBLACION, ZONA_POSTAL, NUM_INSCRITA, E_MAIL, UBICADA FROM V_TRAMITES_INCOMP_PARTES " +
				" WHERE ID_TRAMITE = ? AND ID_PARTE = 1 AND ID_PERSONA = ?";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idInscripcion);
			ps.setInt(2, idOtorgante);
			rs = ps.executeQuery();
			CharSetUtil csu = new CharSetUtil();
			if (rs.next()){
				otorganteTO = new OtorganteTO();
				otorganteTO.setIdPersona(rs.getInt("ID_PERSONA"));
				otorganteTO.setIdOtorgante(rs.getInt("ID_PERSONA"));
				otorganteTO.setRfc(rs.getString("RFC"));
				otorganteTO.setNombre(rs.getString("NOMBRE_PERSONA"));
				otorganteTO.setApellidoPaterno(rs.getString("AP_PATERNO_PERSONA"));
				otorganteTO.setApellidoMaterno(rs.getString("AP_MATERNO_PERSONA"));
				otorganteTO.setRazon(rs.getString("RAZON_SOCIAL"));
				otorganteTO.setIdNacionalidad(rs.getInt("ID_NACIONALIDAD"));
				otorganteTO.setNombreCompleto(rs.getString("NOMBRE"));
				otorganteTO.setCurp(rs.getString("CURP"));
				otorganteTO.setUbicacionInscrito(rs.getString("UBICADA"));	
				otorganteTO.setTipoPersona(rs.getString("PER_JURIDICA"));
				otorganteTO.setInscrita(rs.getString("NUM_INSCRITA"));
				otorganteTO.setCorreo(rs.getString("E_MAIL"));
				otorganteTO.setIdResidencia(rs.getInt("ID_PAIS_RESIDENCIA"));
				otorganteTO.setUbicacion(rs.getString("UBICA_DOMICILIO_1"));
				otorganteTO.setExtendido(rs.getString("EXTENSION"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return otorganteTO;
	}
	public OtorganteTO getOtorganteByFolioElectronico(String folioElectronico){
		OtorganteTO otorganteTO = null;
		String sql = "SELECT Distinct ID_PERSONA, NOMBRE_PERSONA, " +
		"  AP_PATERNO, AP_MATERNO, CURP,RFC,    " +
		"  PER_JURIDICA,  FOLIO_MERCANTIL,     " +
		"  NACIONALIDAD, ID_NACIONALIDAD " +
		" FROM V_BUSQUEDA_FOLIO_E_PF " +
		" WHERE FOLIO_MERCANTIL = ? ORDER BY ID_PERSONA DESC";
		MyLogger.Logger.log(Level.INFO, "SQL:"+ sql + "\\n valor de ? : " + folioElectronico);
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		PreparedStatement ps  = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, folioElectronico);
			rs = ps.executeQuery();
			if (rs.next()){
				otorganteTO = new OtorganteTO();
				otorganteTO.setIdPersona(rs.getInt("ID_PERSONA"));
				otorganteTO.setIdOtorgante(rs.getInt("ID_PERSONA"));
				otorganteTO.setFolioMercantil(rs.getString("FOLIO_MERCANTIL"));
				
				otorganteTO.setNombre(rs.getString("NOMBRE_PERSONA"));
				otorganteTO.setApellidoPaterno(rs.getString("AP_PATERNO"));
				otorganteTO.setApellidoMaterno(rs.getString("AP_MATERNO"));
				String nombreCompleto = nonNull(otorganteTO.getNombre()) + " " + nonNull(otorganteTO.getApellidoPaterno()) +" "+ nonNull(otorganteTO.getApellidoMaterno());
				otorganteTO.setIdNacionalidad(rs.getInt("ID_NACIONALIDAD"));
				otorganteTO.setNombreCompleto(nombreCompleto);
				otorganteTO.setCurp(rs.getString("CURP"));
				otorganteTO.setRfc(rs.getString("RFC"));
				otorganteTO.setDescNacionalidad(rs.getString("NACIONALIDAD"));
				String perJur =  rs.getString("PER_JURIDICA");
				
				String personaJuridica = "Persona Juridica";
				if (perJur.equals("PF")){
					personaJuridica ="Persona Fisica";
				}
				otorganteTO.setTipoPersona(personaJuridica);
			
			}
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
			bd.close(connection,rs,ps);
			
		}
		
		return otorganteTO;
	}
	
	public OtorganteTO getOtorganteFisicoByFolioElectronico(String folioElectronico){
		OtorganteTO otorganteTO = null;
		String sql = "SELECT PR.ID_PERSONA, PR.PER_JURIDICA, PR.ID_NACIONALIDAD, PF.NOMBRE_PERSONA, PF.CURP, PR.RFC, PR.E_MAIL, DM.UBICA_DOMICILIO_1 " + 
				"FROM RUG_PERSONAS PR, RUG_PERSONAS_FISICAS PF, RUG_DOMICILIOS_EXT DM " + 
				"WHERE PF.CURP = ? " + 
				"AND PER_JURIDICA = 'PF' " + 
				"AND PF.ID_PERSONA = PR.ID_PERSONA " + 
				"AND DM.ID_DOMICILIO = PR.ID_DOMICILIO " + 
				"AND ROWNUM = 1 " +
				"ORDER BY PR.ID_PERSONA DESC";
		
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, folioElectronico);
			rs = ps.executeQuery();
			if (rs.next()){
				otorganteTO = new OtorganteTO();
				otorganteTO.setIdPersona(rs.getInt("ID_PERSONA"));
				otorganteTO.setIdOtorgante(rs.getInt("ID_PERSONA"));
				//otorganteTO.setFolioMercantil(rs.getString("FOLIO_MERCANTIL"));				
				otorganteTO.setNombre(rs.getString("NOMBRE_PERSONA"));
				//otorganteTO.setApellidoPaterno(rs.getString("AP_PATERNO"));
				//otorganteTO.setApellidoMaterno(rs.getString("AP_MATERNO"));
				//String nombreCompleto = nonNull(otorganteTO.getNombre()) + " " + nonNull(otorganteTO.getApellidoPaterno()) +" "+ nonNull(otorganteTO.getApellidoMaterno());
				otorganteTO.setIdNacionalidad(rs.getInt("ID_NACIONALIDAD"));
				otorganteTO.setNombreCompleto(rs.getString("NOMBRE_PERSONA"));
				otorganteTO.setCurp(rs.getString("CURP"));
				otorganteTO.setRfc(rs.getString("RFC"));
				//otorganteTO.setDescNacionalidad(rs.getString("NACIONALIDAD"));
				
				//otorganteTO.setEdad(rs.getString("EDAD"));
				//otorganteTO.setEstadoCivil(rs.getString("ESTADO_CIVIL"));
				//otorganteTO.setProfesion(rs.getString("OCUPACION_ACTUAL"));
				otorganteTO.setCorreo(rs.getString("E_MAIL"));
				//otorganteTO.setTelefono(rs.getString("TELEFONO"));
				//otorganteTO.setExtendido(rs.getString("EXTENSION"));					
				otorganteTO.setUbicacion(rs.getString("UBICA_DOMICILIO_1"));				
				
				String perJur =  rs.getString("PER_JURIDICA");
				
				/*String personaJuridica = "Persona Juridica";
				if (perJur.equals("PF")){
					personaJuridica ="Persona Individual";
				}*/
				otorganteTO.setTipoPersona(perJur);
			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
			
		}
		
		return otorganteTO;
	}
	
	
	/****************************************/
	public OtorganteTO getOtorganteMoralByFolioElectronico(String folioElectronico){
		System.out.println("El folio "+folioElectronico);
		OtorganteTO otorganteTO = null;
		String sql = "SELECT PR.RFC, PR.ID_PERSONA, PR.PER_JURIDICA, PR.ID_NACIONALIDAD, PM.RAZON_SOCIAL, PM.NUM_INSCRITA, PM.UBICADA, PR.E_MAIL, DM.UBICA_DOMICILIO_1 " + 
				"FROM RUG_PERSONAS PR, RUG_PERSONAS_MORALES PM, RUG_DOMICILIOS_EXT DM " + 
				"WHERE PR.RFC = ? " + 
				"AND PER_JURIDICA = 'PM' " + 
				"AND PM.ID_PERSONA = PR.ID_PERSONA " + 
				"AND DM.ID_DOMICILIO = PR.ID_DOMICILIO " + 
				"AND ROWNUM = 1 " +
				"ORDER BY PR.ID_PERSONA DESC" ;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, folioElectronico);
			rs = ps.executeQuery();
			if (rs.next()){
				otorganteTO = new OtorganteTO();
				otorganteTO.setIdPersona(rs.getInt("ID_PERSONA"));
				otorganteTO.setIdOtorgante(rs.getInt("ID_PERSONA"));
				//otorganteTO.setFolioMercantil(rs.getString("FOLIO_MERCANTIL"));
				otorganteTO.setCurp("");
				otorganteTO.setRfc(rs.getString("RFC"));
				otorganteTO.setRazon(rs.getString("RAZON_SOCIAL"));//nombre de sociedad mercanti, porque una persona moral no cuenta con nombre_persona
				otorganteTO.setIdNacionalidad(rs.getInt("ID_NACIONALIDAD"));
				//otorganteTO.setDescNacionalidad(rs.getString("DESC_NACIONALIDAD"));
				otorganteTO.setTipoPersona(rs.getString("PER_JURIDICA"));								
				otorganteTO.setTipoSociedad(rs.getString("NUM_INSCRITA"));
				otorganteTO.setExtendido(rs.getString("UBICADA"));
				otorganteTO.setCorreo(rs.getString("E_MAIL"));
				otorganteTO.setUbicacion(rs.getString("UBICA_DOMICILIO_1"));
			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
			
		}
		
		return otorganteTO;
	}
	/****************************************/
	
	public OtorganteTO getOtorganteFisicoByFolio(String folioElectronico) throws CargaMasivaException, InfrastructureException{
		OtorganteTO otorganteTO = null;
		String sql = "SELECT Distinct ID_PERSONA, NOMBRE_PERSONA, " +
		"  AP_PATERNO, AP_MATERNO, CURP, RFC,   " +
		"  PER_JURIDICA,  FOLIO_MERCANTIL,     " +
		"  NACIONALIDAD, ID_NACIONALIDAD " +
		" FROM V_BUSQUEDA_FOLIO_E_PF " +
		" WHERE FOLIO_MERCANTIL = ? ORDER BY ID_PERSONA DESC";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, folioElectronico);
			rs = ps.executeQuery();
			if (rs.next()){
				otorganteTO = new OtorganteTO();
				otorganteTO.setIdPersona(rs.getInt("ID_PERSONA"));
				otorganteTO.setIdOtorgante(rs.getInt("ID_PERSONA"));
				otorganteTO.setFolioMercantil(rs.getString("FOLIO_MERCANTIL"));				
				otorganteTO.setNombre(rs.getString("NOMBRE_PERSONA"));
				otorganteTO.setApellidoPaterno(rs.getString("AP_PATERNO"));
				otorganteTO.setApellidoMaterno(rs.getString("AP_MATERNO"));
				String nombreCompleto = nonNull(otorganteTO.getNombre()) + " " + nonNull(otorganteTO.getApellidoPaterno()) +" "+ nonNull(otorganteTO.getApellidoMaterno());
				otorganteTO.setIdNacionalidad(rs.getInt("ID_NACIONALIDAD"));
				otorganteTO.setNombreCompleto(nombreCompleto);
				otorganteTO.setCurp(rs.getString("CURP"));
				otorganteTO.setRfc(rs.getString("RFC"));
				otorganteTO.setDescNacionalidad(rs.getString("NACIONALIDAD"));
				otorganteTO.setTipoPersona(rs.getString("PER_JURIDICA"));
			}else{
				throw new CargaMasivaException(96, " "+folioElectronico);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new InfrastructureException("Error al intentar retraer otorgante por folio electronico " + folioElectronico );
		}finally{
			bd.close(connection,rs,ps);
			
		}
		
		return otorganteTO;
	}
	
	public OtorganteTO getOtorganteFisicoByCURP(String curp){
		OtorganteTO otorganteTO = null;
		String sql = "SELECT Distinct ID_PERSONA, NOMBRE_PERSONA, " +
				"  AP_PATERNO, AP_MATERNO, CURP, RFC,   " +
				"  PER_JURIDICA,  FOLIO_MERCANTIL,     " +
				"  NACIONALIDAD, ID_NACIONALIDAD " +
				" FROM V_BUSQUEDA_FOLIO_E_PF " +
				" WHERE CURP = ? AND FOLIO_MERCANTIL IS NOT NULL AND STATUS = 'AC' ORDER BY ID_PERSONA DESC ";
		 
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, curp);
			rs = ps.executeQuery();
			if (rs.next()){
				otorganteTO = new OtorganteTO();
				otorganteTO.setIdPersona(rs.getInt("ID_PERSONA"));
				otorganteTO.setIdOtorgante(rs.getInt("ID_PERSONA"));
				otorganteTO.setFolioMercantil(rs.getString("FOLIO_MERCANTIL"));
				
				otorganteTO.setNombre(rs.getString("NOMBRE_PERSONA"));
				otorganteTO.setApellidoPaterno(rs.getString("AP_PATERNO"));
				otorganteTO.setApellidoMaterno(rs.getString("AP_MATERNO"));
				String nombreCompleto = nonNull(otorganteTO.getNombre()) + " " + nonNull(otorganteTO.getApellidoPaterno()) +" "+ nonNull(otorganteTO.getApellidoMaterno());
				otorganteTO.setIdNacionalidad(rs.getInt("ID_NACIONALIDAD"));
				otorganteTO.setNombreCompleto(nombreCompleto);
				otorganteTO.setCurp(rs.getString("CURP"));
				otorganteTO.setRfc(rs.getString("RFC"));
				otorganteTO.setDescNacionalidad(rs.getString("NACIONALIDAD"));
				String perJur =  rs.getString("PER_JURIDICA");
				
				String personaJuridica = "Persona Juridica";
				if (perJur.equals("PF")){
					personaJuridica ="Persona Fisica";
				}
				otorganteTO.setTipoPersona(personaJuridica);
			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
			bd.close(connection,rs,ps);
			
		}
		
		return otorganteTO;
	}
	
	public OtorganteTO getOtorganteFisicoByCURPAcreedor(String curp){
		OtorganteTO otorganteTO = null;
		String sql = "SELECT Distinct ID_PERSONA, NOMBRE_PERSONA, " +
				"  AP_PATERNO, AP_MATERNO, CURP,RFC,    " +
				"  PER_JURIDICA,  FOLIO_MERCANTIL,     " +
				"  NACIONALIDAD, ID_NACIONALIDAD " +
				" FROM V_BUSQUEDA_FOLIO_E_PF " +
				" WHERE CURP = ? AND FOLIO_MERCANTIL IS NOT NULL AND STATUS = 'AC' ORDER BY ID_PERSONA DESC ";
		 
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, curp);
			rs = ps.executeQuery();
			if (rs.next()){
				otorganteTO = new OtorganteTO();
				otorganteTO.setIdPersona(rs.getInt("ID_PERSONA"));
				otorganteTO.setIdOtorgante(rs.getInt("ID_PERSONA"));
				otorganteTO.setFolioMercantil(rs.getString("FOLIO_MERCANTIL"));
				
				otorganteTO.setNombre(rs.getString("NOMBRE_PERSONA"));
				otorganteTO.setApellidoPaterno(rs.getString("AP_PATERNO"));
				otorganteTO.setApellidoMaterno(rs.getString("AP_MATERNO"));
				String nombreCompleto = nonNull(otorganteTO.getNombre()) + " " + nonNull(otorganteTO.getApellidoPaterno()) +" "+ nonNull(otorganteTO.getApellidoMaterno());
				otorganteTO.setIdNacionalidad(rs.getInt("ID_NACIONALIDAD"));
				otorganteTO.setNombreCompleto(nombreCompleto);
				otorganteTO.setCurp(rs.getString("CURP"));
				otorganteTO.setRfc(rs.getString("RFC"));
				otorganteTO.setDescNacionalidad(rs.getString("NACIONALIDAD"));
				String perJur =  rs.getString("PER_JURIDICA");
				
				String personaJuridica = "Persona Juridica";
				if (perJur.equals("PF")){
					personaJuridica ="Persona Fisica";
				}
				otorganteTO.setTipoPersona(personaJuridica);
			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
			bd.close(connection,rs,ps);
			
		}
		
		return otorganteTO;
	}
	
	/*******************************************************/
	public OtorganteTO getOtorganteMoralByNIPOrigen(String rfc,String nacionalidad){
		OtorganteTO otorganteTO = null;
		
		String sql = "SELECT RAZON_SOCIAL, FOLIO_MERCANTIL, RFC, DESC_NACIONALIDAD, ID_NACIONALIDAD FROM V_TRAMITES_INCOMP_PARTES WHERE RFC = ? AND id_nacionalidad=?";
		
		
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, rfc);
			ps.setString(2, nacionalidad);
			rs = ps.executeQuery();
			if (rs.next()){
				otorganteTO = new OtorganteTO();
				otorganteTO.setRazon(rs.getString("RAZON_SOCIAL"));
				otorganteTO.setFolioMercantil(rs.getString("FOLIO_MERCANTIL"));
				otorganteTO.setRfc(rs.getString("RFC"));
				otorganteTO.setDescNacionalidad(rs.getString("DESC_NACIONALIDAD"));
				otorganteTO.setIdNacionalidad(rs.getInt("ID_NACIONALIDAD"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
			bd.close(connection,rs,ps);
			
		}
		
		return otorganteTO;
	}	
	
	/**************************************/
	
	public OtorganteTO getOtorganteMoralByNIPOrigenExtranjero(String rfc,String nacionalidad, String nombre){
		OtorganteTO otorganteTO = null;
		
		String sql = "SELECT RAZON_SOCIAL, FOLIO_MERCANTIL, RFC, DESC_NACIONALIDAD, ID_NACIONALIDAD FROM V_TRAMITES_INCOMP_PARTES WHERE RFC = ? AND id_nacionalidad=? AND razon_social=?";
		
		
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, rfc);
			ps.setString(2, nacionalidad);
			ps.setString(3, nombre);
			rs = ps.executeQuery();
			if (rs.next()){
				otorganteTO = new OtorganteTO();
				otorganteTO.setRazon(rs.getString("RAZON_SOCIAL"));
				otorganteTO.setFolioMercantil(rs.getString("FOLIO_MERCANTIL"));
				otorganteTO.setRfc(rs.getString("RFC"));
				otorganteTO.setDescNacionalidad(rs.getString("DESC_NACIONALIDAD"));
				otorganteTO.setIdNacionalidad(rs.getInt("ID_NACIONALIDAD"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
			bd.close(connection,rs,ps);
			
		}
		
		return otorganteTO;
	}	
	
	/**************************************/
	
	public OtorganteTO getOtorganteMoralByNIPOrigenFENULL(String rfc,String nacionalidad){
		OtorganteTO otorganteTO = null;
		
		String sql = "SELECT RAZON_SOCIAL, FOLIO_MERCANTIL, RFC, DESC_NACIONALIDAD, ID_NACIONALIDAD FROM V_TRAMITES_INCOMP_PARTES WHERE RFC = ? AND id_nacionalidad=? and FOLIO_MERCANTIL IS NOT NULL";
		
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, rfc);
			ps.setString(2, nacionalidad);
			rs = ps.executeQuery();
			if (rs.next()){
				otorganteTO = new OtorganteTO();
				otorganteTO.setRazon(rs.getString("RAZON_SOCIAL"));
				otorganteTO.setFolioMercantil(rs.getString("FOLIO_MERCANTIL"));
				otorganteTO.setRfc(rs.getString("RFC"));
				otorganteTO.setDescNacionalidad(rs.getString("DESC_NACIONALIDAD"));
				otorganteTO.setIdNacionalidad(rs.getInt("ID_NACIONALIDAD"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
			bd.close(connection,rs,ps);
			
		}
		
		return otorganteTO;
	}	
	/*******************************************************/
	private String nonNull(String cadena) {
		if (cadena == null) {
			return "";
		} else {
			return cadena;
		}
	}
	
	public List <AcreedorTO> getListaUltimosAcreedores(Integer idGarantia){
		List <AcreedorTO> lista = new ArrayList<AcreedorTO>();
		
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql =" SELECT ID_TRAMITE, ID_GARANTIA, ID_PERSONA, ID_PARTE, DESC_PARTE, PER_JURIDICA, NOMBRE, E_MAIL, CLAVE_PAIS, " +
		" TELEFONO, EXTENSION, FOLIO_MERCANTIL,  RFC FROM   V_GARANTIA_PARTES WHERE   ID_GARANTIA = ? " +
		" AND ID_TRAMITE = (SELECT   ID_TRAMITE    FROM   (  SELECT   ID_TRAMITE" +
		" FROM   V_GARANTIA_PARTES WHERE   ID_GARANTIA = ? ORDER BY   ID_TRAMITE DESC)" +
		"  WHERE   ROWNUM < 2 ) AND ID_PARTE = 3 ";
		ResultSet rs =null;
		PreparedStatement ps = null;
		
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idGarantia);
			ps.setInt(2, idGarantia);
			rs = ps.executeQuery();
			AcreedorTO acreedorTO ;
			while (rs.next()){
				acreedorTO = new AcreedorTO();
				acreedorTO.setNombreCompleto(rs.getString("NOMBRE"));
//				acreedorTO.setCurp(rs.getString("CURP"));
				acreedorTO.setFolioMercantil(rs.getString("FOLIO_MERCANTIL"));
				acreedorTO.setRfc(rs.getString("RFC"));
				String perJur =  rs.getString("PER_JURIDICA");				
				String personaJuridica = "Persona Juridica";
				if (perJur.equals("PF")){
					personaJuridica ="Persona Fisica";
				}
				acreedorTO.setTipoPersona(personaJuridica);
				acreedorTO.setIdAcreedor(rs.getString("ID_PERSONA"));
//				acreedorTO.setCalle(rs.getString("CALLE"));
//				acreedorTO.setCalleNumero(rs.getString("NUM_EXTERIOR"));
//				acreedorTO.setCalleNumeroInterior(rs.getString("NUM_INTERIOR"));
				lista.add(acreedorTO);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return lista;
	}
	
	
	public List <DeudorTO> getUltimosDeudorByIdGarantia(Integer idGarantia){
		List <DeudorTO> lista = new ArrayList<DeudorTO>();
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql =" SELECT ID_TRAMITE, ID_GARANTIA, ID_PERSONA, ID_PARTE, DESC_PARTE, PER_JURIDICA, NOMBRE, E_MAIL, CLAVE_PAIS, " +
		" TELEFONO, EXTENSION, FOLIO_MERCANTIL,  RFC FROM   V_GARANTIA_PARTES WHERE   ID_GARANTIA = ? " +
		" AND ID_TRAMITE = (SELECT   ID_TRAMITE    FROM   (  SELECT   ID_TRAMITE" +
		" FROM   V_GARANTIA_PARTES WHERE   ID_GARANTIA = ? ORDER BY   ID_TRAMITE DESC)" +
		"  WHERE   ROWNUM < 2 ) AND ID_PARTE = 2 ";
		ResultSet rs =null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idGarantia);
			ps.setInt(2, idGarantia);
			rs = ps.executeQuery();
			DeudorTO deudorTO = null;
			while (rs.next()){
				deudorTO = new DeudorTO();				
				deudorTO.setCurp(rs.getString("CURP"));
				deudorTO.setNombreCompleto(rs.getString("NOMBRE"));
				deudorTO.setIdNacionalidad(rs.getInt("ID_NACIONALIDAD"));
				String perJur =  rs.getString("PER_JURIDICA");			
				deudorTO.setTipoPersona(perJur);
				deudorTO.setIdPersona(rs.getInt("ID_PERSONA"));				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return lista;
		
	}
	public DeudorTO getDeudorID(Integer idTramite, Integer idDeudor){
		DeudorTO deudorTO = null;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "SELECT ID_TRAMITE, ID_PERSONA, ID_PARTE, DESC_PARTE, PER_JURIDICA," +
				" NOMBRE, NOMBRE_PERSONA, AP_PATERNO_PERSONA, AP_MATERNO_PERSONA, ID_NACIONALIDAD, RAZON_SOCIAL, CURP, RFC," +
				" TELEFONO, E_MAIL, ID_PAIS_RESIDENCIA, UBICA_DOMICILIO_1, EXTENSION," + 
				" NUM_INSCRITA, FOLIO, LIBRO, UBICADA, EDAD, ESTADO_CIVIL, OCUPACION_ACTUAL" +
				" FROM RUG.V_TRAMITES_INCOMP_PARTES " +
				" WHERE ID_TRAMITE = ? AND ID_PARTE = 2 AND ID_PERSONA = ?";
		ResultSet rs =null;
		PreparedStatement ps =null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idTramite);
			ps.setInt(2, idDeudor);
			rs = ps.executeQuery();
			
			if (rs.next()){
				deudorTO = new DeudorTO();
				deudorTO.setNombre(rs.getString("NOMBRE_PERSONA"));
				deudorTO.setApellidoMaterno(rs.getString("AP_MATERNO_PERSONA"));
				deudorTO.setApellidoPaterno(rs.getString("AP_PATERNO_PERSONA"));
				deudorTO.setRazon(rs.getString("RAZON_SOCIAL"));
				deudorTO.setCurp(rs.getString("CURP"));
				deudorTO.setRfc(rs.getString("RFC"));
				deudorTO.setNombreCompleto(rs.getString("NOMBRE"));
				deudorTO.setIdNacionalidad(rs.getInt("ID_NACIONALIDAD"));				
				deudorTO.setTipoPersona(rs.getString("PER_JURIDICA"));
				deudorTO.setIdPersona(rs.getInt("ID_PERSONA"));
				
				deudorTO.setTelefono(rs.getString("TELEFONO"));
				deudorTO.setCorreo(rs.getString("E_MAIL"));
				deudorTO.setIdResidencia(rs.getInt("ID_PAIS_RESIDENCIA"));
				deudorTO.setUbicacion(rs.getString("UBICA_DOMICILIO_1"));
				deudorTO.setExtendido(rs.getString("EXTENSION"));
				
				deudorTO.setInscrita(rs.getString("NUM_INSCRITA"));
				deudorTO.setFolioInscrito(rs.getString("FOLIO"));
				deudorTO.setLibroInscrito(rs.getString("LIBRO"));
				deudorTO.setUbicacionInscrito(rs.getString("UBICADA"));
				deudorTO.setEdad(rs.getString("EDAD"));
				deudorTO.setEstadoCivil(rs.getString("ESTADO_CIVIL"));
				deudorTO.setProfesion(rs.getString("OCUPACION_ACTUAL"));
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return deudorTO;
		
	}
	
	public List<DeudorTO> getListaOtorgantes(Integer idInscripcion){
		List<DeudorTO> listaDeudores = new ArrayList<DeudorTO>();
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "SELECT ID_TRAMITE, ID_PERSONA, ID_PARTE, DESC_PARTE, PER_JURIDICA, UBICADA, " +
				" NOMBRE, NOMBRE_PERSONA, AP_PATERNO_PERSONA, AP_MATERNO_PERSONA, RAZON_SOCIAL, CURP,RFC, ID_NACIONALIDAD " +
				" FROM RUG.V_TRAMITES_INCOMP_PARTES " +
				" WHERE ID_TRAMITE = ? AND ID_PARTE = 1";
		ResultSet rs = null;
		PreparedStatement ps =null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idInscripcion);
			rs = ps.executeQuery();
			DeudorTO deudorTO ;
			while (rs.next()){
				deudorTO = new DeudorTO();
				deudorTO.setNombre(rs.getString("NOMBRE_PERSONA"));
				deudorTO.setApellidoMaterno(rs.getString("AP_MATERNO_PERSONA"));
				deudorTO.setApellidoPaterno(rs.getString("AP_PATERNO_PERSONA"));
				deudorTO.setRazon(rs.getString("RAZON_SOCIAL"));
				deudorTO.setCurp(rs.getString("CURP"));
				deudorTO.setRfc(rs.getString("RFC"));
				deudorTO.setNombreCompleto(new CharSetUtil().longitudMaximaPorPalabra(rs.getString("NOMBRE"),25));
				deudorTO.setIdNacionalidad(rs.getInt("ID_NACIONALIDAD"));
				deudorTO.setUbicacionInscrito(rs.getString("UBICADA"));	
				deudorTO.setTipoPersona(rs.getString("PER_JURIDICA"));
				deudorTO.setIdPersona(rs.getInt("ID_PERSONA"));
				listaDeudores.add(deudorTO);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return listaDeudores;
		
	}
	
	public List<DeudorTO> getListaDeudores(Integer idInscripcion){
		List<DeudorTO> listaDeudores = new ArrayList<DeudorTO>();
		System.out.println("idInscripcion = " + idInscripcion);
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "SELECT ID_TRAMITE, ID_PERSONA, ID_PARTE, DESC_PARTE, PER_JURIDICA, UBICADA, " +
				" NOMBRE, NOMBRE_PERSONA, AP_PATERNO_PERSONA, AP_MATERNO_PERSONA, RAZON_SOCIAL, CURP,RFC, ID_NACIONALIDAD " +
				" FROM RUG.V_TRAMITES_INCOMP_PARTES " +
				" WHERE ID_TRAMITE = ? AND ID_PARTE = 2";
		ResultSet rs = null;
		PreparedStatement ps =null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idInscripcion);
			rs = ps.executeQuery();
			DeudorTO deudorTO ;
			while (rs.next()){
				deudorTO = new DeudorTO();
				deudorTO.setNombre(rs.getString("NOMBRE_PERSONA"));
				deudorTO.setApellidoMaterno(rs.getString("AP_MATERNO_PERSONA"));
				deudorTO.setApellidoPaterno(rs.getString("AP_PATERNO_PERSONA"));
				deudorTO.setRazon(rs.getString("RAZON_SOCIAL"));
				deudorTO.setCurp(rs.getString("CURP"));
				deudorTO.setRfc(rs.getString("RFC"));
				deudorTO.setNombreCompleto(new CharSetUtil().longitudMaximaPorPalabra(rs.getString("NOMBRE"),25));
				deudorTO.setIdNacionalidad(rs.getInt("ID_NACIONALIDAD"));
				deudorTO.setUbicacionInscrito(rs.getString("UBICADA"));	
				deudorTO.setTipoPersona(rs.getString("PER_JURIDICA"));
				deudorTO.setIdPersona(rs.getInt("ID_PERSONA"));
				listaDeudores.add(deudorTO);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return listaDeudores;
		
	}
	
	public List <AcreedorTO> getListaAcreedores(Integer idInscripcion){
		List <AcreedorTO> lista = new ArrayList<AcreedorTO>();
		
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "SELECT ID_TRAMITE, ID_PERSONA, ID_PARTE, DESC_PARTE, PER_JURIDICA, UBICADA, " +
				" NOMBRE, NOMBRE_PERSONA, AP_PATERNO_PERSONA, AP_MATERNO_PERSONA, RAZON_SOCIAL, CURP, " +
				" ID_NACIONALIDAD, FOLIO_MERCANTIL, RFC, CURP, CALLE, NUM_EXTERIOR, NUM_INTERIOR" +
				" , UBICA_DOMICILIO_1, UBICA_DOMICILIO_2, POBLACION, ZONA_POSTAL, E_MAIL " +
				" FROM RUG.V_TRAMITES_INCOMP_PARTES " +
				" WHERE ID_TRAMITE = ? AND ID_PARTE = 3";
		ResultSet rs =null;
		PreparedStatement ps =null;
		
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idInscripcion);
			rs = ps.executeQuery();
			AcreedorTO acreedorTO ;
			while (rs.next()){
				acreedorTO = new AcreedorTO();
				acreedorTO.setDomicilioUno(rs.getString("UBICA_DOMICILIO_1"));
				acreedorTO.setDomicilioDos(rs.getString("UBICA_DOMICILIO_2"));
				acreedorTO.setPoblacion(rs.getString("POBLACION"));
				acreedorTO.setZonaPostal(rs.getString("ZONA_POSTAL"));
				acreedorTO.setNombreCompleto(rs.getString("NOMBRE"));
				acreedorTO.setNombre(rs.getString("NOMBRE_PERSONA"));
				acreedorTO.setApellidoMaterno(rs.getString("AP_MATERNO_PERSONA"));
				acreedorTO.setApellidoPaterno(rs.getString("AP_PATERNO_PERSONA"));
				acreedorTO.setRazonSocial(rs.getString("RAZON_SOCIAL"));
				acreedorTO.setCorreoElectronico(rs.getString("E_MAIL"));				
				acreedorTO.setCurp(rs.getString("CURP"));
				acreedorTO.setTipoPersona(rs.getString("PER_JURIDICA"));
				acreedorTO.setFolioMercantil(rs.getString("FOLIO_MERCANTIL"));
				acreedorTO.setRfc(rs.getString("RFC"));
				acreedorTO.setIdAcreedor(rs.getString("ID_PERSONA"));
				acreedorTO.setIdPersona(new Integer(rs.getString("ID_PERSONA")));
				acreedorTO.setCalle(rs.getString("CALLE"));
				acreedorTO.setCalleNumero(rs.getString("NUM_EXTERIOR"));
				acreedorTO.setCalleNumeroInterior(rs.getString("NUM_INTERIOR"));
				acreedorTO.setUbicacionInscrito(rs.getString("UBICADA"));
				
				lista.add(acreedorTO);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		
		return lista;
	}
	
	public AcreedorTO getAcreedoresByID(Integer idInscripcion, Integer idAcreedor){
		AcreedorTO acreedorTO = null;
		
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "SELECT ID_TRAMITE, ID_PERSONA, ID_PARTE, DESC_PARTE, PER_JURIDICA," +
				" NOMBRE, NOMBRE_PERSONA, AP_PATERNO_PERSONA, AP_MATERNO_PERSONA, RAZON_SOCIAL, CURP," +
				" ID_NACIONALIDAD, FOLIO_MERCANTIL, RFC, CALLE, NUM_EXTERIOR, NUM_INTERIOR," +
				" CODIGO_POSTAL, E_MAIL, TELEFONO, EXTENSION, ID_LOCALIDAD, ID_COLONIA,"+
				" UBICA_DOMICILIO_1, UBICA_DOMICILIO_2, POBLACION, ZONA_POSTAL, ID_PAIS_RESIDENCIA," +
				" NUM_INSCRITA, FOLIO, LIBRO, UBICADA, EDAD, ESTADO_CIVIL, OCUPACION_ACTUAL" +
				" FROM RUG.V_TRAMITES_INCOMP_PARTES " +
				" WHERE ID_TRAMITE = ? AND ID_PARTE = 3 AND ID_PERSONA = ?";
		ResultSet rs = null;
		PreparedStatement ps =null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idInscripcion);
			ps.setInt(2, idAcreedor);
			rs = ps.executeQuery();
			
			while (rs.next()){
				acreedorTO = new AcreedorTO();
				String nombre = rs.getString("NOMBRE");// codigo postal, id colonia, id localidad, telefono, extencion, email
				acreedorTO.setDomicilioUno(rs.getString("UBICA_DOMICILIO_1"));
				acreedorTO.setDomicilioDos(rs.getString("UBICA_DOMICILIO_2"));
				acreedorTO.setPoblacion(rs.getString("POBLACION"));
				acreedorTO.setZonaPostal(rs.getString("ZONA_POSTAL"));
				acreedorTO.setNombreCompleto(nombre);
				acreedorTO.setNombre(rs.getString("NOMBRE_PERSONA"));
				acreedorTO.setApellidoMaterno(rs.getString("AP_MATERNO_PERSONA"));
				acreedorTO.setApellidoPaterno(rs.getString("AP_PATERNO_PERSONA"));
				acreedorTO.setRazonSocial(rs.getString("RAZON_SOCIAL"));
				acreedorTO.setCodigoPostal(rs.getString("CODIGO_POSTAL"));
				acreedorTO.setIdPersona(rs.getInt("ID_PERSONA"));
				MyLogger.Logger.log(Level.INFO, "id Persona"+acreedorTO.getIdPersona());
				acreedorTO.setRfc(rs.getString("RFC"));
				acreedorTO.setFolioMercantil(rs.getString("FOLIO_MERCANTIL"));
				acreedorTO.setCalle(rs.getString("CALLE"));
				acreedorTO.setCalleNumero(rs.getString("NUM_EXTERIOR"));
				acreedorTO.setCalleNumeroInterior(rs.getString("NUM_INTERIOR"));
				acreedorTO.setIdColonia(rs.getInt("ID_COLONIA"));
				acreedorTO.setIdLocalidad(rs.getInt("ID_LOCALIDAD"));
				acreedorTO.setTipoPersona(rs.getString("PER_JURIDICA"));
				acreedorTO.setCurp(rs.getString("CURP"));
				acreedorTO.setIdNacionalidad(rs.getInt("ID_NACIONALIDAD"));
				acreedorTO.setTelefono(rs.getString("TELEFONO"));
				acreedorTO.setExtencion(rs.getString("EXTENSION"));
				acreedorTO.setCorreoElectronico(rs.getString("E_MAIL"));
				acreedorTO.setCodigoPostal(rs.getString("CODIGO_POSTAL"));
				acreedorTO.setIdPaisResidencia(rs.getInt("ID_PAIS_RESIDENCIA"));
				/*String perJur =  rs.getString("PER_JURIDICA");				
				String personaJuridica = "Persona Juridica";
				if (perJur.equals("PF")){
					personaJuridica ="Persona Individual";
				}
				acreedorTO.setTipoPersona(personaJuridica);*/
				
				acreedorTO.setInscrita(rs.getString("NUM_INSCRITA"));
				acreedorTO.setFolioInscrito(rs.getString("FOLIO"));
				acreedorTO.setLibroInscrito(rs.getString("LIBRO"));
				acreedorTO.setUbicacionInscrito(rs.getString("UBICADA"));
				acreedorTO.setEdad(rs.getString("EDAD"));
				acreedorTO.setEstadoCivil(rs.getString("ESTADO_CIVIL"));
				acreedorTO.setProfesion(rs.getString("OCUPACION_ACTUAL"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		
		return acreedorTO;
	}
	
	public boolean actualizaParte(AltaParteTO altaParteTO){
		System.out.println("entro a SP_MODIFICA_PARTE ");
		System.out.println(
				" 1:  = " + altaParteTO.getRazonSocial() +
				" 2:  = " + altaParteTO.getTipoPersona() +
				" 3:  = " + altaParteTO.getIdPersona() +
				" 4:  = " + altaParteTO.getInscrita());
		boolean regresa = false;
		String sql = "{ call RUG.SP_MODIFICA_PARTE " +
		" ( " +
		" ?,?,?,?,?," +
		" ?,?,?,?,?," +
		" ?,?,?,?,?," +
		" ?,?,?,?,?," +
		" ?,?,?,?,?," +
		" ?,?,?,?,?," +
		" ?,? " +
		" ) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs =null;
		try {
			
			cs = connection.prepareCall(sql);
			cs.setInt(1, altaParteTO.getIdPersona());
			cs.setInt(2, altaParteTO.getIdTramite());
			cs.setInt(3, altaParteTO.getIdParte());
			cs.setInt(4, altaParteTO.getIdParte());
			cs.setString(5, altaParteTO.getTipoPersona());
			cs.setString(6, altaParteTO.getRazonSocial());
			cs.setString(7, altaParteTO.getNombre());
			cs.setString(8, altaParteTO.getApellidoPaterno());
			cs.setString(9, altaParteTO.getApellidoMaterno());
		
			
			if (altaParteTO.getFolioMercantil()==null){
				cs.setNull(10, Types.NULL);
			}else{
				cs.setString(10, altaParteTO.getFolioMercantil());
			}
			
			
			if (altaParteTO.getRfc()==null){
				cs.setNull(11, Types.NULL);
			}else{
				cs.setString(11, altaParteTO.getRfc());
			}
			
			
			if (altaParteTO.getCurp()==null){
				cs.setNull(12, Types.NULL);
			}else{
				cs.setString(12, altaParteTO.getCurp());
			}
			
			cs.setString(13, altaParteTO.getHayDomicilio());
			
			
			if (altaParteTO.getDomicilioUno()==null){
				cs.setNull(14, Types.NULL);
			}else{
				cs.setString(14, altaParteTO.getDomicilioUno());
			}
			if (altaParteTO.getNumeroExterior()==null){
				cs.setNull(15, Types.NULL);
			}else{
				cs.setString(15, altaParteTO.getNumeroExterior());
			}
			if (altaParteTO.getNumeroInterior()==null){
				cs.setNull(16, Types.NULL);
			}else{
				cs.setString(16, altaParteTO.getNumeroInterior());
			}
			
			if (altaParteTO.getIdColonia()==null){
				cs.setNull(17, Types.NULL);
			}else{
				cs.setInt(17,altaParteTO.getIdColonia());
			}			
			if (altaParteTO.getIdPaisResidencia()==null){
				cs.setNull(18, Types.NULL);
			}else{
				cs.setInt(18,altaParteTO.getIdPaisResidencia());
			}
			cs.setInt(19, altaParteTO.getIdUsuario());
			if(altaParteTO.getIdNacionalidad()==null) {
				cs.setNull(20, Types.NULL);
			} else {
				cs.setInt(20, altaParteTO.getIdNacionalidad());
			}
		
			if (altaParteTO.getTelefono()==null){
				cs.setNull(21, Types.NULL);
			}else{
				cs.setString(21, altaParteTO.getTelefono());
			}
			if (altaParteTO.getExtencion()==null){
				cs.setNull(22, Types.NULL);
			}else{
				cs.setString(22, altaParteTO.getExtencion());
			}
			if (altaParteTO.getCorreoElectronico()==null){
				cs.setNull(23, Types.NULL);
			}else{
				cs.setString(23, altaParteTO.getCorreoElectronico());
			}
			
			/** Campos nuevos **/
			if (altaParteTO.getInscrita()==null){
				cs.setNull(24, Types.NULL);
			}else{
				cs.setString(24, altaParteTO.getInscrita());
			}
			if (altaParteTO.getFolioInscrito()==null){
				cs.setNull(25, Types.NULL);
			}else{
				cs.setString(25, altaParteTO.getFolioInscrito());
			}
			if (altaParteTO.getLibroInscrito()==null){
				cs.setNull(26, Types.NULL);
			}else{
				cs.setString(26, altaParteTO.getLibroInscrito());
			}
			if (altaParteTO.getUbicacionInscrito()==null){
				cs.setNull(27, Types.NULL);
			}else{
				cs.setString(27, altaParteTO.getUbicacionInscrito());
			}
			if (altaParteTO.getEdad()==null){
				cs.setNull(28, Types.NULL);
			}else{
				cs.setString(28, altaParteTO.getEdad());
			}
			if (altaParteTO.getEstadoCivil()==null){
				cs.setNull(29, Types.NULL);
			}else{
				cs.setString(29, altaParteTO.getEstadoCivil());
			}
			if (altaParteTO.getProfesion()==null){
				cs.setNull(30, Types.NULL);
			}else{
				cs.setString(30, altaParteTO.getProfesion());
			}
			
			cs.registerOutParameter(31, Types.INTEGER);
			cs.registerOutParameter(32, Types.VARCHAR);
			
			cs.execute();
			MyLogger.Logger.log(Level.INFO, "" + cs.getInt(31));
			MyLogger.Logger.log(Level.INFO, cs.getString(32));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			bd.close(connection,null,cs);
		}
		return regresa;
	}
	
	public boolean bajaParte(Integer idTramite, Integer idPersona, 
			Integer idParte, String bandera){
		boolean regresa = false;
		String sql = "{ call RUG.SP_BAJA_PARTE_LOGICA " +
				" (?,?,?,?,?,?) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs =null;
		try {
			connection.setAutoCommit(false);
			cs = connection.prepareCall(sql);
			cs.setInt(1, idTramite);
			cs.setInt(2, idPersona);
			cs.setInt(3, idParte);
			cs.setString(4, bandera);
			cs.registerOutParameter(5, Types.INTEGER);
			cs.registerOutParameter(6, Types.VARCHAR);
			cs.execute();
			connection.commit();
			regresa = true;			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection,null,cs);
		}
		return regresa;
	}


	//====Franco  Consultar dependiendo del tipo de garantia, si los acreedores tienen folio
	public Integer validaFolios(Integer idTramite){
		Integer regresa = 0;
		String sql = "{ call RUG.SP_CONSULTA_FOLIO_ACREEDOR " +
				" (?,?,?) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs =null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, idTramite);
			cs.registerOutParameter(2, Types.INTEGER);
			cs.registerOutParameter(3, Types.VARCHAR);
			cs.execute();
				regresa = cs.getInt(2);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection,null,cs);
		}
		return regresa;
		}
}


