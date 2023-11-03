package gt.gob.rgm.rug.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConexionDB implements Serializable{

    private Context ctx = null;
    private DataSource ds = null;
    private Connection connection = null;

    public ConexionDB(){}

    public Connection getConnection(){
        try {
            this.ctx = null;
            this.ds = null;
            this.ctx = new InitialContext();
            this.ds = (DataSource) this.ctx.lookup("jdbc/rugDS");
            this.connection = this.ds.getConnection();
        } catch (NamingException e) {
            System.out.println("Error de conexion");
            // e.printStackTrace();
        } catch (SQLException e){
            System.out.println("Error de conexion catch");
            // e.printStackTrace();
        }
        return this.connection;
    }

    public void close(Connection connection, ResultSet rs, Statement stmt){
        if(rs != null){
            try {
                if (!rs.isClosed()){
                    rs.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error de conexion close");
                // ex.printStackTrace();
            }
        }

        if (stmt != null){
            try {
                if (!stmt.isClosed()){
                    stmt.close();
                }
            } catch (SQLException e) {
                System.out.println("Error de conexion stmt");
                // e.printStackTrace();
            }
        }

        if (connection != null){
            try {
                if (!connection.isClosed()){
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("Error de conexion connection 2");
                // e.printStackTrace();
            }
        }
    }
    
}
