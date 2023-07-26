package gt.gob.rgm.adm.constants.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import gt.gob.rgm.adm.dao.conectiondB;

public class ConstantsDAO {
    public Map<String, String> getConstants() {
        conectiondB bd = new conectiondB();
        Connection connection = bd.getConnection();
        ResultSet rs =null;
        PreparedStatement ps= null;
        Map<String, String> constantes = new HashMap<String, String>();
        try {
            String sqlQuery = "SELECT CVE_PARAMETRO,VALOR_PARAMETRO FROM RUG_PARAMETRO_CONF";

            ps = connection.prepareStatement(sqlQuery);
            rs = ps.executeQuery();
            while (rs.next()) {
                constantes.put(rs.getString("CVE_PARAMETRO"), rs.getString("VALOR_PARAMETRO"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            bd.close(connection, rs, ps);
        }
        return constantes;
    }
    public Map<Integer, String> getErrores() {
        conectiondB bd = new conectiondB();
        Connection connection = bd.getConnection();
        ResultSet rs =null;
        PreparedStatement ps= null;
        Map<Integer, String> errores = new HashMap<Integer, String>();
        try {
            String sqlQuery = "SELECT ID_CODIGO, DESC_CODIGO from rug_cat_mensajes_errores";

            ps = connection.prepareStatement(sqlQuery);
            rs = ps.executeQuery();
            while (rs.next()) {
                errores.put(rs.getInt("ID_CODIGO"), rs.getString("DESC_CODIGO"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            bd.close(connection, rs, ps);
        }
        return errores;
    }

}
