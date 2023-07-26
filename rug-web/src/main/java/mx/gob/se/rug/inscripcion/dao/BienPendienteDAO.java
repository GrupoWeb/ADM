package mx.gob.se.rug.inscripcion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
//days_15girls@hotmail.com
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.inscripcion.to.BienPendienteTO;

public class BienPendienteDAO {
	/*
	 * RUG.V_GARANTIAS_PEND_BIENES
	(
	ID_GARANTIA_PEND,
	ID_TIPO_BIEN,
	DESC_TIPO_BIEN
	)
	 */
	public List<BienPendienteTO> bienesPendientesById(Integer idGaranitaPendiente){
		List<BienPendienteTO> lista = new ArrayList<BienPendienteTO>();
		String sql = "SELECT ID_GARANTIA_PEND, ID_TIPO_BIEN, DESC_TIPO_BIEN FROM RUG.V_GARANTIAS_PEND_BIENES WHERE ID_GARANTIA_PEND = ?";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		PreparedStatement ps =null;
		try {
			 ps = connection.prepareStatement(sql);
			ps.setInt(1, idGaranitaPendiente);
			rs = ps.executeQuery();
			BienPendienteTO bienPendienteTO;
			while (rs.next()){
				bienPendienteTO = new BienPendienteTO();
				bienPendienteTO.setIdGarntiaPendiente(rs.getInt("ID_GARANTIA_PEND"));
				bienPendienteTO.setIdTipoBien(rs.getInt("ID_TIPO_BIEN"));
				bienPendienteTO.setDescripcion(rs.getString("DESC_TIPO_BIEN"));
				lista.add(bienPendienteTO);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			bd.close(connection,rs,ps);
		}
		return lista;
	}

}
