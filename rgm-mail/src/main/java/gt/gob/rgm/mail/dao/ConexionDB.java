package gt.gob.rgm.mail.dao;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import gt.gob.rgm.mail.exception.InfrastructureException;

public class ConexionDB implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private CallableStatement callableStatement = null;
	private DataSource ds = null;
	private Context ctx = null;

	public ConexionDB() {
		this.connection = getConnection();
	}

	public Connection getConnection() {

		try {
			this.ctx = new InitialContext();
			this.ds = (DataSource) this.ctx.lookup("jdbc/rugDS");
			this.connection = this.ds.getConnection();

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.connection;
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		this.preparedStatement = connection.prepareStatement(sql);
		return preparedStatement;
	}

	public CallableStatement prepareCall(String sql)
			throws InfrastructureException {
		try {
			this.callableStatement = connection.prepareCall(sql);
		} catch (SQLException e) {
			throw new InfrastructureException(
					"Problema al intentar crear CallableSatement");
		}
		return this.callableStatement;
	}

	public ResultSet executeQuery() throws InfrastructureException {
		try {
			if (preparedStatement != null) {
				this.resultSet = this.preparedStatement.executeQuery();
			} else if (callableStatement != null) {
				this.resultSet = this.callableStatement.executeQuery();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InfrastructureException(
					"Problema al intentar ejecutar el Query.");
		}
		return this.resultSet;
	}

	public void destroy() {
			if (this.resultSet!=null){
				try{
				if (!this.resultSet.isClosed()) {
					this.resultSet.close();
				}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (this.preparedStatement!=null){
				try{
				if (!this.preparedStatement.isClosed()) {
						this.preparedStatement.close();
				}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (this.callableStatement != null){
				try{
					if (!this.callableStatement.isClosed()) {
						this.callableStatement.close();
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (this.connection != null){
				try{
				if (!this.connection.isClosed()) {
						this.connection.close();
				}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		try {
			this.resultSet = null;
			this.preparedStatement = null;
			this.callableStatement = null;
			this.connection = null;
			this.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void setInt(int parameterIndex, int value) throws SQLException {
		this.preparedStatement.setInt(parameterIndex, value);
	}
}
