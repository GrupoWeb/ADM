package mx.gob.se.rug.boleta.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mx.gob.se.rug.dao.ConexionBD;

public class FirmaTramiteDAO {
	public String getNombreUsuarioByTramite(Integer idTramite){
		String regresa = null;
		String sql ="select nombre_usuario from v_detalle_tramite_masivo where id_firma_masiva = ?";
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idTramite);
			rs = ps.executeQuery();
			while (rs.next()){
				regresa = rs.getString("nombre_usuario");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			bd.close(connection, rs, ps);
		}
		return regresa;
	}
	public List<Integer> idTramitesByTramite(Integer idTramite){
		List<Integer> regresa = new ArrayList<Integer>();
		String sql ="select id_tramite from v_detalle_tramite_masivo where id_firma_masiva = ?";
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idTramite);
			rs = ps.executeQuery();
			
			while (rs.next()){
				
				regresa.add(rs.getInt("id_tramite"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			bd.close(connection, rs, ps);
		}
		return regresa;
	}

}
