package mx.gob.se.rug.seguridad.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.seguridad.to.FormularioAltaTO;
import mx.gob.se.rug.to.UsuarioTO;

public class FormularioDAO {

	public FormularioAltaTO getFormulario(FormularioAltaTO formularioAltaTO, UsuarioTO usuarioTO){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs =null;
		PreparedStatement ps = null;
		String sql = "SELECT ID_PRIVILEGIO FROM RUG_PRIVILEGIOS WHERE ID_RECURSO=? ORDER BY ORDEN";
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setString(1, formularioAltaTO.getIdFormulario());
			rs = ps.executeQuery();
			while(rs.next()){
				formularioAltaTO.setIdPrivilegio(rs.getString("ID_PRIVILEGIO"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return formularioAltaTO;
	}

}
