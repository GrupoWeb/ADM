package mx.gob.se.rug.masiva.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;

import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.masiva.to.ArchivoTO;
import mx.gob.se.rug.to.PlSql;
import mx.gob.se.rug.util.MyLogger;

public class ArchivoDAO {
	
	public boolean existeSha1(String sha1){
		boolean regresa = false;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "SELECT ID_ARCHIVO FROM RUG.RUG_ARCHIVO WHERE ALGORITMO_HASH = ?";
		ResultSet rs = null;
		PreparedStatement ps  = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, sha1);
			rs = ps.executeQuery();
			if (rs.next()){
				regresa = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection, rs, ps);
		}
			
		return regresa;
	}
	
	public PlSql insertArchivo(ArchivoTO archivoTO){
		PlSql regresa = new PlSql();
		String sql ="{ call RUG.SP_GUARDA_ARCHIVO( " +
				" ?,?,?," +
				" ?,?,?," +
				" ?,?,? ) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs =null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, archivoTO.getIdUsuario());
			cs.setString(2, archivoTO.getAlgoritoHash());
			cs.setString(3, archivoTO.getNombreArchivo());
			cs.setBytes(4, archivoTO.getArchivo());
			cs.setString(5, archivoTO.getTipoArchivo());
			cs.setString(6, archivoTO.getDescripcion());
			cs.registerOutParameter(7, Types.INTEGER);
			cs.registerOutParameter(8, Types.INTEGER);
			cs.registerOutParameter(9, Types.VARCHAR);
			cs.executeQuery();
			regresa.setIntPl(cs.getInt(8));
			regresa.setStrPl(cs.getString(9));
			regresa.setResIntPl(cs.getInt(7));
		} catch (SQLException e) {
			e.printStackTrace();
			regresa.setIntPl(999);
			regresa.setStrPl("sucedio un error en el sistema:"+e.getMessage());
			regresa.setResIntPl(0);
		}finally{
			bd.close(connection,null,cs);
		}
		MyLogger.Logger.log(Level.INFO,"insertArchivo ID----" + regresa.getIntPl());
		MyLogger.Logger.log(Level.INFO,"insertArchivo Mensaje----" + regresa.getStrPl());

		return regresa;
	}
	
	public Integer insertArchivoFirmaMasiva(ArchivoTO archivoTO){
		Integer integer = new Integer(0);
		String sql ="{ call RUG.SP_ALTA_REL_FIRMA_ARCHIVO( " +
				" ?,?,?,?,?  ) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs =null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, archivoTO.getIdFirmaMasiva());
			cs.setBytes(2, archivoTO.getArchivo());
			cs.setInt(3, archivoTO.getIdUsuario());
			cs.registerOutParameter(4, Types.INTEGER);
			cs.registerOutParameter(5, Types.VARCHAR);
			cs.executeQuery();
			MyLogger.Logger.log(Level.INFO,"Rug-ArchivoDAO::--------------------Respuesta Int : " + cs.getInt(4));
			MyLogger.Logger.log(Level.INFO,"Rug-ArchivoDAO::--------------------Respuesta Str : " + cs.getString(5));
			integer = cs.getInt(4);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,null,cs);
		}

		return integer;
	}
	
	public byte[] getBytesToIdFirma(Integer idFirmaMasiva) throws SQLException{
		byte[] regresa = {};
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "SELECT ARCHIVO FROM RUG_ARCHIVO WHERE ID_ARCHIVO = ?";
		ResultSet rs = null;
		PreparedStatement ps  = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idFirmaMasiva);
			rs = ps.executeQuery();
			if (rs.next()){
				regresa = rs.getBytes("ARCHIVO");
			}else{
				throw new SQLException("No se encontro el archivo");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection, rs, ps);
		}
			
		return regresa;
	}
	 

}
