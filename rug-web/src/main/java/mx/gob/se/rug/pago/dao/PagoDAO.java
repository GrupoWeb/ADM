package mx.gob.se.rug.pago.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.pago.to.CarroCompraTO;
import mx.gob.se.rug.pago.to.PagoTO;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;

public class PagoDAO {
	public List<CarroCompraTO> getCarroCompra(UsuarioTO usuarioTO) {
		List<CarroCompraTO> carroCompraTOs = new ArrayList<CarroCompraTO>();
		carroCompraTOs.add(getCarroCompraPL(usuarioTO, 3));
		carroCompraTOs.add(getCarroCompraPL(usuarioTO, 1));
		carroCompraTOs.add(getCarroCompraPL(usuarioTO, 7));
		carroCompraTOs.add(getCarroCompraPL(usuarioTO, 8));
		carroCompraTOs.add(getCarroCompraPL(usuarioTO, 6));
		carroCompraTOs.add(getCarroCompraPL(usuarioTO, 9));
		carroCompraTOs.add(getCarroCompraPL(usuarioTO, 4));
		carroCompraTOs.add(getCarroCompraPL(usuarioTO, 10));
		carroCompraTOs.add(getCarroCompraPL(usuarioTO, 11));
		carroCompraTOs.add(getCarroCompraPL(usuarioTO, 5));
		return carroCompraTOs;
	}

	public CarroCompraTO getCarroCompraPL(UsuarioTO usuarioTO,
			Integer idTipoTramite) {
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CarroCompraTO carroCompraTO = new CarroCompraTO();
		CallableStatement cs = null;
		String sql = "{call RUG.SP_CARRITO_COMPRA (?,?,?,?,?,?,?)}";
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, usuarioTO.getPersona().getIdPersona());
			cs.setInt(2, idTipoTramite);
			cs.registerOutParameter(3, Types.VARCHAR);
			cs.registerOutParameter(4, Types.NUMERIC);
			cs.registerOutParameter(5, Types.NUMERIC);
			cs.registerOutParameter(6, Types.INTEGER);
			cs.registerOutParameter(7, Types.VARCHAR);
			cs.execute();
			carroCompraTO.setDescripcionTramite(cs.getString(3));
			carroCompraTO.setPrecioUnitario(cs.getDouble(4));
			carroCompraTO.setCantidad(cs.getInt(5));
			carroCompraTO.setPrecioTotal(carroCompraTO.getPrecioUnitario()	* carroCompraTO.getCantidad());

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection,null,cs);
		}
		return carroCompraTO;
	}

	public List<PagoTO> getPendientePago(UsuarioTO usuarioTO) {
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		PreparedStatement ps =null;
		ResultSet rs =null;
		List<PagoTO> pagoTOs = new ArrayList<PagoTO>();
		String sql = " select  ID_TRAMITE_TEMP,  TIPO_TRAMITE, "
				+ " FECHA_STATUS,ID_GARANTIA_PEND,NOMBRE, FOLIO_MERCANTIL, "
				+ " DESCRIP_STATUS,DESC_GARANTIA,PRECIO"
				+ " from V_TRAMITES_PENDIENTES"
				+ " where ID_STATUS=2 and ID_PERSONA_LOGIN=?";
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, usuarioTO.getPersona().getIdPersona());
			 rs = ps.executeQuery();
			PagoTO pagoTO;
			while (rs.next()) {
				pagoTO = new PagoTO();
				pagoTO.setIdTramite(rs.getInt("ID_TRAMITE_TEMP"));
				pagoTO.setTipoTramite(rs.getString("TIPO_TRAMITE"));
				pagoTO.setFechaCelebracion(rs.getString("FECHA_STATUS"));
				pagoTO.setFechaTermino(rs.getString("FECHA_STATUS"));
				pagoTO.setIdGarantia(rs.getInt("ID_GARANTIA_PEND"));
				pagoTO.setNombre(rs.getString("NOMBRE"));
				pagoTO.setFolioMercantil(rs.getString("FOLIO_MERCANTIL"));
				pagoTO.setStatus(rs.getString("DESCRIP_STATUS"));
				pagoTO.setDescGeneral(rs.getString("DESC_GARANTIA"));
				pagoTO.setPrecio(rs.getDouble("PRECIO"));
				pagoTOs.add(pagoTO);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection,rs,ps);
		}
		return pagoTOs;
	}

	public List<PagoTO> getPendientePago(UsuarioTO usuarioTO, PagoTO pagoTO) {
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs =null;
		PreparedStatement ps = null;
		List<PagoTO> pagoTOs = new ArrayList<PagoTO>();
		String sql = " select  ID_TRAMITE_TEMP,  TIPO_TRAMITE, "
				+ " FECHA_STATUS,ID_GARANTIA_PEND,NOMBRE, FOLIO_MERCANTIL, "
				+ " DESCRIP_STATUS,DESC_GARANTIA,PRECIO"
				+ " from V_TRAMITES_PENDIENTES"
				+ " where ID_STATUS=2 and ID_PERSONA_LOGIN=? and ID_TRAMITE_TEMP=?";
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, usuarioTO.getPersona().getIdPersona());
			ps.setInt(2, pagoTO.getIdTramite());
			rs = ps.executeQuery();
			while (rs.next()) {
				pagoTO = new PagoTO();
				pagoTO.setIdTramite(rs.getInt("ID_TRAMITE_TEMP"));
				pagoTO.setTipoTramite(rs.getString("TIPO_TRAMITE"));
				pagoTO.setFechaCelebracion(rs.getString("FECHA_STATUS"));
				pagoTO.setFechaTermino(rs.getString("FECHA_STATUS"));
				pagoTO.setIdGarantia(rs.getInt("ID_GARANTIA_PEND"));
				pagoTO.setNombre(rs.getString("NOMBRE"));
				pagoTO.setFolioMercantil(rs.getString("FOLIO_MERCANTIL"));
				pagoTO.setStatus(rs.getString("DESCRIP_STATUS"));
				pagoTO.setDescGeneral(rs.getString("DESC_GARANTIA"));
				pagoTO.setPrecio(rs.getDouble("PRECIO"));
				pagoTOs.add(pagoTO);
			}
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection,rs,ps);
		}
		return pagoTOs;
	}
	//--temporal
	
	public Integer getTipoTramiteByIdTramitePendiente(Integer idTramite){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		Integer idTipoTramite= null;
		PreparedStatement ps =null;
		ResultSet rs =null;
		String sql = " SELECT ID_TIPO_TRAMITE from TRAMITES_RUG_INCOMP where ID_TRAMITE_TEMP=?";
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idTramite);
			rs = ps.executeQuery();
			if (rs.next()) {
				idTipoTramite= new Integer(rs.getInt("ID_TIPO_TRAMITE"));
				MyLogger.Logger.log(Level.WARNING, "Id TRAMITE que trae " + rs.getInt("ID_TIPO_TRAMITE") + " " + idTramite);
			}
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection,rs,ps);
		}
		return idTipoTramite;
	}
	
	public Boolean firmaTramite(Integer idTipoTramite, String esFactoraje) {
		MyLogger.Logger.log(Level.WARNING, "factoraje pago  " + esFactoraje );
		if(esFactoraje == null){
			esFactoraje = "";
		}
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs =null;
		Boolean resultado = false;
		
		String sql = "{ call RUG.SP_Alta_Bitacora_Tramite2 (" +
				"?, " +//1
				"?, " +//2
				"?, " +//3
				"?, " +//4
				"?, " +//5
				"?, " +//6
				"?, " +//7
				"? ) }";//8
		
// 1 peIdTramiteTemp           IN RUG_BITAC_TRAMITES.ID_TRAMITE_TEMP%TYPE,
// 2 peIdStatus                IN RUG_BITAC_TRAMITES.ID_STATUS%TYPE,
// 3 peIdPaso                  IN RUG_BITAC_TRAMITES.ID_PASO%TYPE,
// 4 peFechaCreacion           IN TRAMITES.FECHA_CREACION%TYPE,
// 5 peBanderaFecha            IN CHAR, --BANDERA QUE INDICA SI EL PL PLASMA LA FECHA CON EL SYSDATE O USA LA QUE MANDA EN peFechaCreacion, VALORES POSIBLES V o F
// 6 psResult                  OUT  INTEGER,
// 7 psTxResult                OUT  VARCHAR2
		try {
			cs = connection.prepareCall(sql);
			MyLogger.Logger.log(Level.WARNING, "Aca probando el tramite::::"+idTipoTramite);
			cs.setInt(1, idTipoTramite);
			cs.setInt(2, 3);
			cs.setInt(3, 0);
			cs.setNull(4, Types.NULL);
			cs.setString(5, "V");
			cs.setString(6, esFactoraje);
			cs.registerOutParameter(7, Types.INTEGER);
			cs.registerOutParameter(8, Types.VARCHAR);
			cs.execute();
			MyLogger.Logger.log(Level.INFO, "psResult::"+cs.getString(7));
			MyLogger.Logger.log(Level.INFO, "psTxResult::"+cs.getString(8));
			
			if(cs.getString(7).trim().equalsIgnoreCase("0") || cs.getString(7).trim().equalsIgnoreCase("11")) {
				resultado = true;
			}

		} catch (SQLException e) {
			System.out.println("Error en sql pagoDao" );
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Error en sql pagoDao 2" );
			e.printStackTrace();
		} finally {
			bd.close(connection,null,cs);
		}
		
		return resultado;
	}
}
