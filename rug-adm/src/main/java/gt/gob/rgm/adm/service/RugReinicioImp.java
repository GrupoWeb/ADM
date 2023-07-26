package gt.gob.rgm.adm.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import javax.ejb.Stateless;
import gt.gob.rgm.adm.dao.conectiondB;


@Stateless
public class RugReinicioImp implements RugReinicio{
    public String reinicioSaldoByCode(String codigo){

        String result = "";
        conectiondB bd = new conectiondB();
        Connection connection = bd.getConnection();
        CallableStatement cs = null;

        String query = "{CALL reinicio_saldo(?,?)}";
        try {
            cs = connection.prepareCall(query);
            cs.setString(1, codigo);
            cs.registerOutParameter(2, Types.VARCHAR);
            cs.execute();
            result = cs.getString(2);
            System.out.println("Resultado " + cs.getString(2));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            bd.close(connection, null, cs);
        }

        return result;
    }


    public boolean UpdateSaldoBoletas(Integer costo, Integer usuario){
		boolean regresa = false;
		Integer flag = 1;
		String sql = "{ call RUG.SP_SUMAR_SALDO( " + " ?, ?, ?) }";
		conectiondB db = new conectiondB();
		Connection connection = db.getConnection();
		CallableStatement cs = null;

		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, costo);
			cs.setInt(2, usuario);
			cs.setInt(3, flag);
			cs.execute();
			regresa = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close(connection, null, cs);
		}


		return regresa;
	}

}
