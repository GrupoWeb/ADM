/*
 * FirmaMasivaDAO.java        28/10/2010
 *
 * Copyright (c) 2009 Secretaría de Economía
 * Alfonso Reyes No. 30 Col. Hipódromo Condesa C.P. 06140, 
 * Delegación Cuauhtémoc, México, D.F.
 * Todos los Derechos Reservados.
 *
 * Este software es confidencial y contiene información perteneciente
 * a la Secretaría de Economía.
 * 
 */
package mx.gob.se.rug.inscripcion.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;

import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.inscripcion.to.FirmaMasivaTO;
import mx.gob.se.rug.util.MyLogger;

/**
 * 
 * 
 * @version		0.1
 * @author 		Abraham Stalin Aguilar Valencia
 *
 */
public class FirmaMasivaDAO {
	
	public Integer getEstatusByTramiteTemporal(Integer tramiteTemporal){
		Integer regresa = 0;
		String sql = "select id_status from rug_bitac_tramites where status_reg = 'AC' and id_tramite_temp = ?";
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, tramiteTemporal);
			rs = ps.executeQuery();
			if (rs.next()){
				regresa = rs.getInt("id_status");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			bd.close(connection, rs, ps);
		}
		
		return regresa;
	}
	
	
	/*
	 * Debera construirse el metodo de que se nos entrege el PL
	 */
	public Integer crearFirmaMasiva(FirmaMasivaTO firmaMasivaTO){
		Integer integer = new Integer(0);
		String sql ="{ call RUG.SP_FIRMA_MASIVA( " +
				" ?,?,?,?," +
				" ?,?,? ) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs =null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, firmaMasivaTO.getIdUsuario());
			cs.setString(2, firmaMasivaTO.getTramites());
			cs.setInt(3, firmaMasivaTO.getIdArchivo());		
			cs.setInt(4, firmaMasivaTO.getIdAcreedor());
			cs.registerOutParameter(5, Types.INTEGER);
			cs.registerOutParameter(6, Types.INTEGER);
			cs.registerOutParameter(7, Types.VARCHAR);
			cs.executeQuery();
			MyLogger.Logger.log(Level.INFO, "Respuesta Int : " + cs.getInt(6));
			MyLogger.Logger.log(Level.INFO, "Respuesta Str : " + cs.getString(7));
			if (cs.getInt(6)==0){				
				integer = cs.getInt(5);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,null,cs);
		}

		return integer;
	}

}
