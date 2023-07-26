package mx.gob.se.rug.busqueda.dao;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import mx.gob.se.rug.busqueda.to.BusquedaTO;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.util.MyLogger;

public class BusquedaDAO {
	public List<BusquedaTO> busquedaVehiculo(String pNoSerie){
		List<BusquedaTO> busquedaTOs= new ArrayList<BusquedaTO>();
		ConexionBD bd = new ConexionBD();
		
		//String sql = "select gar.ID_GARANTIA from RUG.RUG_GARANTIAS gar where gar.ID_ULTIMO_TRAMITE in ( select id_tramite from RUG_GARANTIAS_BIENES bien where (bien.IDENTIFICADOR like '%"+pNoSerie+"%')) and garantia_status <> 'CA'";
                String sql = "select gar.ID_GARANTIA from RUG.RUG_GARANTIAS gar where gar.ID_ULTIMO_TRAMITE in ( select id_tramite from RUG_GARANTIAS_BIENES bien where (trim(bien.IDENTIFICADOR) = '"+pNoSerie+"')) and garantia_status <> 'CA'";
		//String sql = "select gar.ID_GARANTIA from RUG.RUG_GARANTIAS gar where gar.ID_ULTIMO_TRAMITE in ( select id_tramite from RUG_GARANTIAS_BIENES bien where (trim(bien.IDENTIFICADOR) = '"+pNoSerie+"')) and garantia_status <> 'CA'";
		          System.out.println("sql = " + sql);
                Connection connection = bd.getConnection();
		ResultSet rs = null;
		PreparedStatement cs = null;
		try {
			cs = connection.prepareStatement(sql);        
			rs=cs.executeQuery();
			BusquedaTO busquedaTO;
			while(rs.next()){
				busquedaTO= new BusquedaTO();
				busquedaTO.setIdGarantia(rs.getString("ID_GARANTIA"));				
				
				busquedaTOs.add(busquedaTO);
			}
		} catch (SQLException e) {
        MyLogger.Logger.log(Level.INFO,"EXCEPTION: " +e.getMessage());
        // TODO Auto-generated catch block
        e.printStackTrace();
		} finally{
        bd.close(connection, rs, cs);
		}
		//MyLogger.Logger.log(Level.INFO,"RREESSUULLTTAADDOO: " +busquedaTOs.toString());
		return busquedaTOs;
	}
	
	public List<BusquedaTO> busquedaOtorgantesPrevios(BusquedaTO busquedaInTO, Integer inicio, Integer fin){
		List<BusquedaTO> busquedaTOs= new ArrayList<BusquedaTO>();
		ConexionBD bd = new ConexionBD();
		String sql = "Select ID_GARANTIA, ID_TRAMITE, NOMBRE, CURP, FOLIO_MERCANTIL from V_OTORGANTES_PREVIOS where 1=1 ";
		    if (busquedaInTO.getNombre().length()!=0) 
		    	sql+=" and ? like NOMBRE ";
		    if (busquedaInTO.getCurpOtorgante().length()!=0)
		    	sql+=" and CURP = ? ";
		    if (busquedaInTO.getFolioMercantil().length()!=0)
		    	sql+=" and FOLIO_MERCANTIL= ? ";
    	MyLogger.Logger.log(Level.INFO,"BusquedaDao.busquedaOtorgantesPrevios: xx**xx ->" +sql);
		Connection connection = bd.getConnection();
		ResultSet rs = null;
		PreparedStatement cs = null;
		try {
        int c=1;
        cs = connection.prepareStatement(sql);
        if (busquedaInTO.getNombre().length()!=0){
            cs.setString(c, busquedaInTO.getNombre());
            c++;
        }
        if (busquedaInTO.getCurpOtorgante().length()!=0){
            cs.setString(c, busquedaInTO.getCurpOtorgante());
            c++;
        }
        if (busquedaInTO.getFolioMercantil().length()!=0){
            cs.setString(c, busquedaInTO.getFolioMercantil());
            c++;
        }
		  rs=cs.executeQuery();
			BusquedaTO busquedaTO;
			while(rs.next()){
				busquedaTO= new BusquedaTO();
				busquedaTO.setIdGarantia(rs.getString("ID_GARANTIA"));
				busquedaTO.setIdTramite(rs.getString("ID_TRAMITE"));
				busquedaTO.setNombre(rs.getString("NOMBRE"));
				busquedaTO.setCurpOtorgante(rs.getString("CURP"));
				busquedaTO.setFolioMercantil(rs.getString("FOLIO_MERCANTIL"));
				busquedaTOs.add(busquedaTO);
			}
		} catch (SQLException e) {
        MyLogger.Logger.log(Level.INFO,"EXCEPTION: " +e.getMessage());
        // TODO Auto-generated catch block
        e.printStackTrace();
		} finally{
        bd.close(connection, rs, cs);
		}
		MyLogger.Logger.log(Level.INFO,"RREESSUULLTTAADDOO: " +busquedaTOs.toString());
		return busquedaTOs;
	}
        
        public List<BusquedaTO> searchInvoice2(BusquedaTO busquedaTO, Integer start, Integer finish,String consultaNombre, String consultaId){
        List<BusquedaTO> busquedaTOs= new ArrayList<BusquedaTO>();
        ConexionBD bd = new ConexionBD();

		System.out.println("busquedaTOs = " + busquedaTO.getInvoice());
		System.out.println("busquedaTOs = " + busquedaTO.getSet());

		// datos que se envian al SP
		System.out.println("1" + busquedaTO.getDescGarantia());
		System.out.println("2" + busquedaTO.getNombre());
		System.out.println("3" + busquedaTO.getIdGarantia());
		System.out.println("4" + busquedaTO.getFolioMercantil());
		System.out.println("5" + busquedaTO.getCurpOtorgante());
		System.out.println("6" + busquedaTO.getRfcOtorgante());
		System.out.println("7" + start);
		System.out.println("8" + finish);
		System.out.println("9" + busquedaTO.getNoSerial());
		System.out.println("10" + busquedaTO.getIdPersona());
		System.out.println("11" + busquedaTO.getIdTipoTramite());
		System.out.println("12" + Types.INTEGER);
		System.out.println("13" + oracle.jdbc.OracleTypes.CURSOR);

        String sql = "{ call RUG.SP_CONSULTA_GARANTIAS_FACT_ANO("+"?,?,?,?,?,"+"?,?,?,?,?,?)}";
        Connection connection = bd.getConnection();
        ResultSet rs = null;
        CallableStatement cs = null;
        try {
            cs = connection.prepareCall(sql);


            cs.setString(1, busquedaTO.getNit());
            cs.setString(2, busquedaTO.getSet());
            
             
            cs.setString(3, busquedaTO.getInvoice());
            
            
            cs.setInt(4, start);
            cs.setInt(5, finish);            
            cs.setInt(6, new Integer(busquedaTO.getIdPersona()));
            cs.setInt(7, new Integer(busquedaTO.getIdTipoTramite()));
            cs.registerOutParameter(8, Types.INTEGER);
            cs.registerOutParameter(9, oracle.jdbc.OracleTypes.CURSOR);
            cs.setString(10, consultaNombre);
            cs.setString(11,consultaId);
            cs.execute();
            
            
            rs = (ResultSet)cs.getObject(9);
            int numReg = (Integer) cs.getObject(8);
            
            BusquedaTO busquedaTO2;
            
            if (numReg == 0) {
                busquedaTO2= new BusquedaTO();
                busquedaTO.setNumReg(numReg);
                busquedaTOs.add(busquedaTO);
            }else {
                while (rs.next()){
                    busquedaTO= new BusquedaTO();
                    busquedaTO.setIdTramite(rs.getString("ID_TRAMITE"));
                    busquedaTO.setIdTipoTramite(rs.getString("ID_TIPO_TRAMITE"));
                    busquedaTO.setFechaCreacion(rs.getString("FECHA_CREACION").replace(".0", ""));
                    busquedaTO.setDescripcion(rs.getString("DESCRIPCION"));
                    busquedaTO.setNombre(rs.getString("NOMBRE"));
                    //busquedaTO.setCurpOtorgante(rs.getString("CURP"));
                    busquedaTO.setRfcOtorgante(rs.getString("FOLIO_MERCANTIL"));
                    busquedaTO.setIdGarantia(rs.getString("ID_GARANTIA"));
                    busquedaTO.setDescGarantia(rs.getString("DESC_TIPOS_BIENES"));
                    busquedaTO.setNumReg(numReg);
                    busquedaTOs.add(busquedaTO);
                }
            }
        } catch (SQLException e) {
            MyLogger.Logger.log(Level.INFO,"EXCEPTION: " +e.getMessage());
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            bd.close(connection, rs, cs);
        }
        //MyLogger.Logger.log(Level.INFO,"RREESSUULLTTAADDOO: " +busquedaTOs.toString());
        return busquedaTOs;
    }


	public List<BusquedaTO> searchInvoice(BusquedaTO busquedaTO, Integer start, Integer finish){
        List<BusquedaTO> busquedaTOs= new ArrayList<BusquedaTO>();
        ConexionBD bd = new ConexionBD();

		System.out.println("busquedaTOs = " + busquedaTO.getInvoice());
		System.out.println("busquedaTOs = " + busquedaTO.getSet());

		// datos que se envian al SP
		System.out.println("1" + busquedaTO.getDescGarantia());
		System.out.println("2" + busquedaTO.getNombre());
		System.out.println("3" + busquedaTO.getIdGarantia());
		System.out.println("4" + busquedaTO.getFolioMercantil());
		System.out.println("5" + busquedaTO.getCurpOtorgante());
		System.out.println("6" + busquedaTO.getRfcOtorgante());
		System.out.println("7" + start);
		System.out.println("8" + finish);
		System.out.println("9" + busquedaTO.getNoSerial());
		System.out.println("10" + busquedaTO.getIdPersona());
		System.out.println("11" + busquedaTO.getIdTipoTramite());
		System.out.println("12" + Types.INTEGER);
		System.out.println("13" + oracle.jdbc.OracleTypes.CURSOR);

        String sql = "{ call RUG.SP_CONSULTA_GARANTIAS_FACT("+"?,?,?,?,?,"+"?,?,?,?)}";
        Connection connection = bd.getConnection();
        ResultSet rs = null;
        CallableStatement cs = null;
        try {
            cs = connection.prepareCall(sql);


            cs.setString(1, busquedaTO.getNit());
            cs.setString(2, busquedaTO.getSet());
            
             
            cs.setString(3, busquedaTO.getInvoice());
            
            
            cs.setInt(4, start);
            cs.setInt(5, finish);            
            cs.setInt(6, new Integer(busquedaTO.getIdPersona()));
            cs.setInt(7, new Integer(busquedaTO.getIdTipoTramite()));
            cs.registerOutParameter(8, Types.INTEGER);
            cs.registerOutParameter(9, oracle.jdbc.OracleTypes.CURSOR);
            cs.execute();
            
            
            rs = (ResultSet)cs.getObject(9);
            int numReg = (Integer) cs.getObject(8);
            
            BusquedaTO busquedaTO2;
            
            if (numReg == 0) {
                busquedaTO2= new BusquedaTO();
                busquedaTO.setNumReg(numReg);
                busquedaTOs.add(busquedaTO);
            }else {
                while (rs.next()){
                    busquedaTO= new BusquedaTO();
                    busquedaTO.setIdTramite(rs.getString("ID_TRAMITE"));
                    busquedaTO.setIdTipoTramite(rs.getString("ID_TIPO_TRAMITE"));
                    busquedaTO.setFechaCreacion(rs.getString("FECHA_CREACION"));
                    busquedaTO.setDescripcion(rs.getString("DESCRIPCION"));
                    busquedaTO.setNombre(rs.getString("NOMBRE"));
                    //busquedaTO.setCurpOtorgante(rs.getString("CURP"));
                    busquedaTO.setRfcOtorgante(rs.getString("FOLIO_MERCANTIL"));
                    busquedaTO.setIdGarantia(rs.getString("ID_GARANTIA"));
                    busquedaTO.setDescGarantia(rs.getString("DESC_TIPOS_BIENES"));
                    busquedaTO.setNumReg(numReg);
                    busquedaTOs.add(busquedaTO);
                }
            }
        } catch (SQLException e) {
            MyLogger.Logger.log(Level.INFO,"EXCEPTION: " +e.getMessage());
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            bd.close(connection, rs, cs);
        }
        //MyLogger.Logger.log(Level.INFO,"RREESSUULLTTAADDOO: " +busquedaTOs.toString());
        return busquedaTOs;
    }

	public List<BusquedaTO> busqueda(BusquedaTO busquedaInTO, Integer inicio, Integer fin, int pagineo){
		List<BusquedaTO> busquedaTOs= new ArrayList<BusquedaTO>();

		System.out.println("1" + busquedaInTO.getDescGarantia());
		System.out.println("2" + busquedaInTO.getNombre());
		System.out.println("3" + busquedaInTO.getIdGarantia());
		System.out.println("4" + busquedaInTO.getFolioMercantil());
		System.out.println("5" + busquedaInTO.getCurpOtorgante());
		System.out.println("6" + busquedaInTO.getRfcOtorgante());
		System.out.println("7" + inicio);
		System.out.println("8" + fin);
		System.out.println("9" + busquedaInTO.getNoSerial());
		System.out.println("10" + busquedaInTO.getIdPersona());
		System.out.println("11" + busquedaInTO.getIdTipoTramite());
		System.out.println("12" + Types.INTEGER);
		System.out.println("13" + oracle.jdbc.OracleTypes.CURSOR);

		ConexionBD bd = new ConexionBD();
		String sql = "{ call RUG.SP_CONSULTA_GARANTIAS_REG("+"?,?,?,?,?,"+"?,?,?,?,?,?,"+"?,?,?)}";
		Connection connection = bd.getConnection();
		ResultSet rs = null;
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
			
			
			cs.setString(1, busquedaInTO.getDescGarantia());
			cs.setString(2, busquedaInTO.getNombre());
			if (busquedaInTO.getIdGarantia() != null && !busquedaInTO.getIdGarantia().equals("") ){				
				cs.setInt(3, Integer.valueOf(busquedaInTO.getIdGarantia()));
			}else{
				cs.setNull(3, Types.NULL);
			}
			cs.setString(4, busquedaInTO.getFolioMercantil());
			cs.setString(5, busquedaInTO.getCurpOtorgante());
			cs.setString(6, busquedaInTO.getRfcOtorgante());
			cs.setInt(7, inicio);
			cs.setInt(8, fin);
			cs.setString(9, busquedaInTO.getNoSerial());
			cs.setInt(10, new Integer(busquedaInTO.getIdPersona()));
			cs.setInt(11, new Integer(busquedaInTO.getIdTipoTramite()));
                        
                        cs.setInt(12,pagineo);
                        
			cs.registerOutParameter(13, Types.INTEGER);
			cs.registerOutParameter(14, oracle.jdbc.OracleTypes.CURSOR);
			cs.execute();
			rs = (ResultSet)cs.getObject(14);
			int numReg = (Integer) cs.getObject(13);
			BusquedaTO busquedaTO;
			if (numReg == 0) {
				busquedaTO= new BusquedaTO();
				busquedaInTO.setNumReg(numReg);
				busquedaTOs.add(busquedaTO);
			}else {
				while (rs.next()){
				busquedaTO= new BusquedaTO();
				busquedaTO.setIdTramite(rs.getString("ID_TRAMITE"));
				busquedaTO.setIdTipoTramite(rs.getString("ID_TIPO_TRAMITE"));
				busquedaTO.setFechaCreacion(rs.getString("FECHA_CREACION"));
				busquedaTO.setDescripcion(rs.getString("DESCRIPCION"));
				busquedaTO.setNombre(rs.getString("NOMBRE"));
				//busquedaTO.setCurpOtorgante(rs.getString("CURP"));
				busquedaTO.setRfcOtorgante(rs.getString("FOLIO_MERCANTIL"));
				busquedaTO.setIdGarantia(rs.getString("ID_GARANTIA"));
				busquedaTO.setDescGarantia(rs.getString("DESC_TIPOS_BIENES"));
				busquedaInTO.setNumReg(numReg);
				busquedaTOs.add(busquedaTO);
				}
			}
		} catch (SQLException e) {
        MyLogger.Logger.log(Level.INFO,"EXCEPTION: " +e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			bd.close(connection, rs, cs);
		}
		//MyLogger.Logger.log(Level.INFO,"RREESSUULLTTAADDOO: " +busquedaTOs.toString());
		return busquedaTOs;
	}
        
        
        public List<BusquedaTO> busquedaSinSaldo(BusquedaTO busquedaInTO, Integer inicio, Integer fin, String consultaNombre, String consultaId ){
		List<BusquedaTO> busquedaTOs= new ArrayList<BusquedaTO>();

		System.out.println("1" + busquedaInTO.getDescGarantia());
		System.out.println("2" + busquedaInTO.getNombre());
		System.out.println("3" + busquedaInTO.getIdGarantia());
		System.out.println("4" + busquedaInTO.getFolioMercantil());
		System.out.println("5" + busquedaInTO.getCurpOtorgante());
		System.out.println("6" + busquedaInTO.getRfcOtorgante());
		System.out.println("7" + inicio);
		System.out.println("8" + fin);
		System.out.println("9" + busquedaInTO.getNoSerial());
		System.out.println("10" + busquedaInTO.getIdPersona());
		System.out.println("11" + busquedaInTO.getIdTipoTramite());
		System.out.println("12" + Types.INTEGER);
		System.out.println("13" + oracle.jdbc.OracleTypes.CURSOR);

		ConexionBD bd = new ConexionBD();
		String sql = "{ call RUG.SP_CONSULTA_GARANTIAS_SINSALDO("+"?,?,?,?,?,"+"?,?,?,?,?,?,"+"?,?,?,?)}";
		Connection connection = bd.getConnection();
		ResultSet rs = null;
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
			
			
			cs.setString(1, busquedaInTO.getDescGarantia());
			cs.setString(2, busquedaInTO.getNombre());
			if (busquedaInTO.getIdGarantia() != null && !busquedaInTO.getIdGarantia().equals("") ){				
				cs.setInt(3, Integer.valueOf(busquedaInTO.getIdGarantia()));
			}else{
				cs.setNull(3, Types.NULL);
			}
			cs.setString(4, busquedaInTO.getFolioMercantil());
			cs.setString(5, busquedaInTO.getCurpOtorgante());
			cs.setString(6, busquedaInTO.getRfcOtorgante());
			cs.setInt(7, inicio);
			cs.setInt(8, fin);
			cs.setString(9, busquedaInTO.getNoSerial());
			cs.setInt(10, new Integer(busquedaInTO.getIdPersona()));
			cs.setInt(11, new Integer(busquedaInTO.getIdTipoTramite()));
			cs.registerOutParameter(12, Types.INTEGER);
			cs.registerOutParameter(13, oracle.jdbc.OracleTypes.CURSOR);
                        
                        cs.setString(14,consultaNombre);
                        cs.setString(15,consultaId);
                        
			cs.execute();
			rs = (ResultSet)cs.getObject(13);
			int numReg = (Integer) cs.getObject(12);
			BusquedaTO busquedaTO;
			if (numReg == 0) {
				busquedaTO= new BusquedaTO();
				busquedaInTO.setNumReg(numReg);
				busquedaTOs.add(busquedaTO);
			}else {
				while (rs.next()){
				busquedaTO= new BusquedaTO();
				busquedaTO.setIdTramite(rs.getString("ID_TRAMITE"));
				busquedaTO.setIdTipoTramite(rs.getString("ID_TIPO_TRAMITE"));
				busquedaTO.setFechaCreacion(rs.getString("FECHA_CREACION"));
				busquedaTO.setDescripcion(rs.getString("DESCRIPCION"));
				busquedaTO.setNombre(rs.getString("NOMBRE"));
				//busquedaTO.setCurpOtorgante(rs.getString("CURP"));
				busquedaTO.setRfcOtorgante(rs.getString("FOLIO_MERCANTIL"));
				busquedaTO.setIdGarantia(rs.getString("ID_GARANTIA"));
				busquedaTO.setDescGarantia(rs.getString("DESC_TIPOS_BIENES"));
				busquedaInTO.setNumReg(numReg);
				busquedaTOs.add(busquedaTO);
				}
			}
		} catch (SQLException e) {
        MyLogger.Logger.log(Level.INFO,"EXCEPTION: " +e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			bd.close(connection, rs, cs);
		}
		//MyLogger.Logger.log(Level.INFO,"RREESSUULLTTAADDOO: " +busquedaTOs.toString());
		return busquedaTOs;
	}
}
