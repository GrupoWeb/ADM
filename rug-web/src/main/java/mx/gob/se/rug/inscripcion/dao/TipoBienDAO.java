package mx.gob.se.rug.inscripcion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.inscripcion.to.TipoBienTO;

public class TipoBienDAO {
	
	
	public List<TipoBienTO> getTipoBienesByIdGarantiaPendiente(Connection connection, Integer idGarantiaPendiente){
		List<TipoBienTO> lista = new ArrayList<TipoBienTO>();
		
		String sql = "SELECT DESC_TIPO_BIEN, ID_TIPO_BIEN, RELACION_BIEN, ID_GARANTIA_PEND FROM V_GARANTIAS_BIENES_PENDIENTES WHERE ID_GARANTIA_PEND = ? ";
		PreparedStatement ps = null;
		ResultSet rs =null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idGarantiaPendiente);
			rs = ps.executeQuery();
			TipoBienTO bienTO;
			while (rs.next()){
				bienTO = new TipoBienTO();
				bienTO.setDescripcion(rs.getString("DESC_TIPO_BIEN"));
				bienTO.setIdTipoBien(rs.getInt("ID_TIPO_BIEN"));
				lista.add(bienTO);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return lista;
	}
	
	public List<TipoBienTO> getTiposBien(){
		List<TipoBienTO> lista = new ArrayList<TipoBienTO>();
		String sql = "select ID_TIPO_BIEN, DESC_TIPO_BIEN, DESC_TIPO_BIEN_EN, ID_PADRE, STATUS_REG from RUG_CAT_TIPO_BIEN where STATUS_REG = 'AC' order by ID_TIPO_BIEN";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			TipoBienTO bienTO ;
			while (rs.next()){
				bienTO = new TipoBienTO();
				bienTO.setIdTipoBien(rs.getInt("ID_TIPO_BIEN"));
				bienTO.setDescripcion(rs.getString("DESC_TIPO_BIEN"));
				bienTO.setDescripcionEN(rs.getString("DESC_TIPO_BIEN_EN"));
				bienTO.setIdPadre(rs.getInt("ID_PADRE"));
				bienTO.setStatusReg(rs.getString("STATUS_REG"));
				lista.add(bienTO);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,null);
		}
		
		return lista;
	}
	
	public String getDescByID(Integer idBien){
		String regresa = null;
		String sql = "SELECT DESC_TIPO_BIEN FROM RUG.RUG_CAT_TIPO_BIEN WHERE ID_TIPO_BIEN = ?";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, idBien);
			 rs = ps.executeQuery();
			if  (rs.next()){
				regresa = rs.getString("DESC_TIPO_BIEN");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,null);
		}
		return regresa;
	}
}
