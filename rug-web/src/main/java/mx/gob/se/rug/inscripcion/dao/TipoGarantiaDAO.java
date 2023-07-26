package mx.gob.se.rug.inscripcion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.inscripcion.to.TipoGarantiaTO;

public class TipoGarantiaDAO {
	
	public List<TipoGarantiaTO> getTiposGarantia(){
		List<TipoGarantiaTO> lista = new ArrayList<TipoGarantiaTO>();
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql="SELECT " +
				" ID_TIPO_GARANTIA, DESC_TIPO_GARANTIA" +
				" FROM RUG_CAT_TIPO_GARANTIA ORDER BY ID_TIPO_GARANTIA";
		ResultSet rs =null;
		PreparedStatement ps =null;
		try {
			ps= connection.prepareStatement(sql);
			rs = ps.executeQuery();
			TipoGarantiaTO garantiaTO;
			while (rs.next()){
				garantiaTO = new TipoGarantiaTO();
				garantiaTO.setIdTipoGarantia(rs.getInt("ID_TIPO_GARANTIA"));
				garantiaTO.setDescripcion(rs.getString("DESC_TIPO_GARANTIA"));
//				garantiaTO.setDescripcionN(rs.getString("DESC_TIPO_GARANTIA_EN"));
				lista.add(garantiaTO);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return lista;
	}
	
	public String getDescByID(Integer idTipoGarantia){
		String regresa = null;
		String sql = "SELECT DESC_TIPO_GARANTIA FROM RUG.RUG_CAT_TIPO_GARANTIA WHERE ID_TIPO_GARANTIA = ?";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		PreparedStatement ps = null;
		try {
			ps=connection.prepareStatement(sql);
			ps.setInt(1, idTipoGarantia);
			rs = ps.executeQuery();
			if  (rs.next()){
				regresa = rs.getString("DESC_TIPO_GARANTIA");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return regresa;
	}

}
