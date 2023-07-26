package mx.gob.se.rug.boleta.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import mx.gob.se.rug.boleta.to.BoletaAnotacionSnGarantia;
import mx.gob.se.rug.boleta.to.BoletaAvisoPreventivoTO;
import mx.gob.se.rug.boleta.to.BoletaCertificacionTO;
import mx.gob.se.rug.boleta.to.BoletaTO;
import mx.gob.se.rug.boleta.to.DetalleTO;
import mx.gob.se.rug.boleta.to.GarantiaTO;
import mx.gob.se.rug.boleta.to.PersonaTO;
import mx.gob.se.rug.dao.BaseRugDao;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.detallegarantia.to.PartesTO;
import mx.gob.se.rug.exception.NoDataFoundException;
import mx.gob.se.rug.exception.NoDateInfrastructureException;
import mx.gob.se.rug.inscripcion.to.OtorganteTO;
//import mx.gob.se.rug.to.AnotacionSinGarantiaTO;
import mx.gob.se.rug.to.AnotacionSinGarantiaTO;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;
import mx.gob.se.rug.util.pdf.to.PdfTO;
import mx.gob.se.rug.util.to.DateUtilRug;

public class BoletaDAO extends BaseRugDao{
	public String  getProcedenciaByTramite(Integer idTramite){
		String procedecia = null;
		
		String sql = "SELECT NOMBRE_TABLA FROM V_TRAMITES_FIRMA where ID_TRAMITE = ?";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idTramite);
			rs = ps.executeQuery();
			while (rs.next()){
				procedecia=rs.getString("NOMBRE_TABLA");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return procedecia;
	}
	public List <OtorganteTO> getOtorganteByTramite(Integer idInscripcion){
		List <OtorganteTO> lista = new ArrayList<OtorganteTO>();
		OtorganteTO otorganteTO = null;
		String sql = "SELECT NOMBRE,FOLIO_MERCANTIL, CURP from V_TRAMITES_TERM_PARTES " +
				" WHERE ID_TRAMITE = ? AND ID_PARTE = 1";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idInscripcion);
			rs = ps.executeQuery();
			while (rs.next()){
				otorganteTO = new OtorganteTO();				
				otorganteTO.setNombreCompleto(rs.getString("NOMBRE"));
				otorganteTO.setFolioMercantil(rs.getString("FOLIO_MERCANTIL"));
				if (rs.getString("CURP")==null){
					otorganteTO.setCurp("N/A");
				}else{
					otorganteTO.setCurp(rs.getString("CURP"));
				}
				lista.add(otorganteTO);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return lista;
	}
	
	public String getNombreAcreedorByTramite(Integer idInscripcion){
		String regresa = null;
		String sql = "SELECT NOMBRE from V_TRAMITES_TERM_PARTES " +
				" WHERE ID_TRAMITE = ? AND ID_PARTE = 4";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idInscripcion);
			rs = ps.executeQuery();
			if (rs.next()){
								
				regresa = rs.getString("NOMBRE");
			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return regresa;
	}
	
	public Integer getTipoTramitebyIdTramiteTemporal(Integer idTramiteTemporal) {
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		Integer idTipoTramite= null;
		PreparedStatement ps =null;
		String sql = "SELECT ID_TIPO_TRAMITE FROM RUG.TRAMITES_RUG_INCOMP WHERE ID_TRAMITE_TEMP = ? ";
		ResultSet rs= null;
		try {
			connection = bd.getConnection();
		 ps = connection.prepareStatement(sql);
			ps.setInt(1, idTramiteTemporal);

			rs = ps.executeQuery();

			if (rs.next()) {
				idTipoTramite = new Integer(rs.getInt("ID_TIPO_TRAMITE"));
			}else{
				MyLogger.Logger.log(Level.INFO, "No se encontro tipo tramite del idTramite::::"+idTramiteTemporal);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection,rs,ps);
		}
		return idTipoTramite;
	}
	public Integer getIdTramiteTempByIdTramite(Integer idTramite) {
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		Integer idTramiteTemp= null;
		
		String sql = "SELECT ID_TRAMITE_TEMP   FROM TRAMITES   WHERE ID_TRAMITE = ?";
		ResultSet rs= null;
		PreparedStatement ps =null;
		try {
			connection = bd.getConnection();
			 ps = connection.prepareStatement(sql);
			ps.setInt(1, idTramite);

			rs = ps.executeQuery();

			if (rs.next()) {
				idTramiteTemp = new Integer(rs.getInt("ID_TRAMITE_TEMP"));
			}else{
				MyLogger.Logger.log(Level.INFO, "No se encontro ID_TRAMITE_TEMP del idTramite::::"+idTramite);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection,rs,ps);
		}
		return idTramiteTemp;
	}
	
	public String getTramitesByGarantia(Integer idGarantia){
		String regresa = "";
		String sql = "SELECT ID_TRAMITE FROM RUG.V_OPERACIONES_GARANTIA WHERE ID_GARANTIA = ?";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idGarantia);
			rs = ps.executeQuery();
			if (rs.next()){
				regresa = rs.getString("ID_TRAMITE");
			}else{
				regresa = "NODATOS";
 			}
			while (rs.next()){
				regresa += ("|" +rs.getString("ID_TRAMITE"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection, rs, ps);
		}	
		
		
		return regresa;		
	}
	
	public DetalleTO getCertificacion(Integer idTramite) {
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		DetalleTO		detalleTO = new DetalleTO();
		String sql = "SELECT  ID_TRAMITE, ID_GARANTIA, ID_TIPO_TRAMITE  FROM RUG_CERTIFICACIONES    WHERE ID_TRAMITE_CERT = ? ";
		ResultSet rs= null;
		PreparedStatement ps =null;
		try {
			connection = bd.getConnection();
			 ps=connection.prepareStatement(sql);
			ps.setInt(1, idTramite);

			rs = ps.executeQuery();

			if (rs.next()) {
				detalleTO.setIdTramite(rs.getInt("ID_TRAMITE"));
				detalleTO.setIdTipoTramite(rs.getInt("ID_TIPO_TRAMITE"));
				if(detalleTO.getIdTipoTramite()!=3||detalleTO.getIdTipoTramite()==10){//AvisoPreventivo
					detalleTO.setIdGarantia(rs.getInt("ID_GARANTIA"));
				}
				
			}else{
				MyLogger.Logger.log(Level.INFO, "No se encontro tipo tramite del idTramiteCERT::::"+idTramite);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection,rs,ps);
		}
		return detalleTO;
	}
	public Integer getTipoTramitebyIdTramiteTerminado(Integer idTramite) {
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		Integer idTipoTramite= null;
		
		String sql = " SELECT ID_TIPO_TRAMITE FROM TRAMITES WHERE ID_TRAMITE = ? and status_reg='AC'";
		ResultSet rs= null;
		PreparedStatement ps =null;
		try {
			connection = bd.getConnection();
			 ps = connection.prepareStatement(sql);
			ps.setInt(1, idTramite);

			rs = ps.executeQuery();

			if (rs.next()) {
				idTipoTramite = new Integer(rs.getInt("ID_TIPO_TRAMITE"));
			}else{
				MyLogger.Logger.log(Level.INFO, "No se encontro tipo tramite del idTramiteTerminado::::"+idTramite);
				idTipoTramite=0;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection,rs,ps);
		}
		return idTipoTramite;
	}
	
	
	public String getTablaBienes(int idgarantia, Integer idtramite){
		String tabla = null;
		
		ConexionBD bd = new ConexionBD();		
		Connection connection = null;
		ResultSet rs =null;
		PreparedStatement ps =null;
		String sqlQuery = "SELECT ID_TIPO_BIEN,DESC_TIPO_BIEN FROM V_GARANTIAS_BIENES WHERE ID_GARANTIA = ? AND RELACION_BIEN=?";		
		try{			
			String tabla2 = null;	
			connection = bd.getConnection();
			 ps=connection.prepareStatement(sqlQuery);
			ps.setInt(1, idgarantia);
			ps.setInt(2, idtramite);
			MyLogger.Logger.log(Level.INFO, "sql v1" + ps);
			rs = ps.executeQuery();
					
			while(rs.next()){					
				if(tabla2 != null)tabla2= tabla2 + ",";
				if(tabla2==null){
					tabla2 = rs.getString("DESC_TIPO_BIEN");
				}else{
					tabla2 = tabla2 + rs.getString("DESC_TIPO_BIEN");
				}
				MyLogger.Logger.log(Level.INFO, "IMPRIME bienes"+tabla2+">>>>>>");
				
			}
			
			tabla= tabla2;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection, rs, ps);
		}
		return tabla;
	}

	public String getDeudor(Integer idtramite) {
		
		String tabla = null;

		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		MyLogger.Logger.log(Level.INFO, "idtramite " + idtramite);
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			String sqlQuery = "";
			String tabla2 = null;	
			sqlQuery = "SELECT ID_PERSONA,DESC_PARTE,FOLIO_MERCANTIL,NOMBRE,PER_JURIDICA,RFC FROM V_TRAMITES_TERM_PARTES WHERE ID_PARTE=2 AND ID_TRAMITE = ?";
			 ps = connection.prepareStatement(sqlQuery);
			ps.setInt(1, idtramite);
			rs = ps.executeQuery();
			
			while (rs.next()) {		
				if(tabla2 != null)tabla2= tabla2 + ",";
				
				if(tabla2==null){
					tabla2= rs.getString("NOMBRE");
				}else{
					tabla2= tabla2 + rs.getString("NOMBRE");
				}	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection, rs, ps);
		}
		return tabla;
	}
	public String getTipoTramite(Integer idtramite,String tipo) {		
		String tabla = null;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		PreparedStatement ps =null;
		try {
			String sqlQuery = "";
			String tabla2 = null;	
			sqlQuery = "SELECT ID_PERSONA,DESC_PARTE,FOLIO_MERCANTIL,NOMBRE,PER_JURIDICA,RFC FROM V_TRAMITES_TERM_PARTES WHERE ID_PARTE =3 AND ID_TRAMITE = ?";
			ps = connection.prepareStatement(sqlQuery);
			ps.setInt(1, idtramite);
			MyLogger.Logger.log(Level.INFO, "sql v2" + ps);
			rs = ps.executeQuery();
			if (rs.next()) {
					if(tabla2==null){
						tabla2= rs.getString("NOMBRE");
					}
			}
			if(tipo.equalsIgnoreCase("h")){
				tabla2=tabla2 + "</table> ";}			
			tabla= tabla2;
			MyLogger.Logger.log(Level.INFO, "tabla " + tabla);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection, rs, ps);
		}
		return tabla;			
	}
	public String getAcreedor(Integer idtramite,String tipo) {		
		String tabla = null;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		PreparedStatement ps =null;
		try {
			String sqlQuery = "";
			String tabla2 = null;	
			sqlQuery = "SELECT ID_PERSONA,DESC_PARTE,FOLIO_MERCANTIL,NOMBRE,PER_JURIDICA,RFC FROM V_TRAMITES_TERM_PARTES WHERE ID_PARTE =3 AND ID_TRAMITE = ?";
			ps = connection.prepareStatement(sqlQuery);
			ps.setInt(1, idtramite);
			MyLogger.Logger.log(Level.INFO, "sql v3" + ps);
			rs = ps.executeQuery();
			if (rs.next()) {
					if(tabla2==null){
						tabla2= rs.getString("NOMBRE");
					}
			}
			if(tipo.equalsIgnoreCase("h")){
				tabla2=tabla2 + "</table> ";}			
			tabla= tabla2;
			MyLogger.Logger.log(Level.INFO, "tabla " + tabla);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection, rs, ps);
		}
		return tabla;			
	}
	public String getTipoTramite(Integer idtramite) {		
		String tabla = null;
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs =null;
		PreparedStatement ps =null;
		String sql = "SELECT DISTINCT C.DESCRIPCION as DESC_TRAM_FIRMA FROM TRAMITES_RUG_INCOMP A,  RUG_FIRMA_MASIVA B, RUG_CAT_TIPO_TRAMITE C," +
				" TRAMITES_RUG_INCOMP D  WHERE A.ID_TIPO_TRAMITE = 18  AND B.ID_FIRMA_MASIVA = A.ID_TRAMITE_TEMP" +
				" AND B.ID_TRAMITE_TEMP = D.ID_TRAMITE_TEMP    AND D.ID_TIPO_TRAMITE = C.ID_TIPO_TRAMITE" +
				" AND A.ID_TRAMITE_TEMP =  ?";
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idtramite);
			rs = ps.executeQuery();
			if (rs.next()) {
				tabla= rs.getString("DESC_TRAM_FIRMA");				
			}else{
				MyLogger.Logger.log(Level.WARNING, "BoletaDAO.getTipoTramite("+idtramite+")--no se encontro el tipo de tramite --");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection, rs, ps);
		}
		return tabla;			
	}
	public PartesTO getOtorgante(Integer idtramite, Integer idgarantia) {		
		PartesTO tabla = new PartesTO();
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			String sqlQuery = "";
			PartesTO tabla2= new PartesTO();	
			sqlQuery = "SELECT ID_PERSONA,DESC_PARTE,FOLIO_MERCANTIL,NOMBRE,PER_JURIDICA,RFC FROM V_TRAMITES_TERM_PARTES WHERE ID_PARTE =1 AND ID_TRAMITE = ?";
			ps = connection.prepareStatement(sqlQuery);
			ps.setInt(1, idtramite);
			MyLogger.Logger.log(Level.INFO, "sql v5" + ps);
			rs = ps.executeQuery();
			if (rs.next()) {
				tabla2.setNombre(rs.getString("NOMBRE"));
				tabla2.setFoliomercantil(rs.getString("FOLIO_MERCANTIL"));
			}
								
			tabla= tabla2;
			MyLogger.Logger.log(Level.INFO, "tabla " + tabla);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection, rs, ps);
		}
		return tabla;			
	}
	
	public String getTipoBienes(Integer relacion){
		String tabla = null;
		
		ConexionBD bd = new ConexionBD();		
		Connection connection = null;
		ResultSet rs =null;
		PreparedStatement ps =null;
		String sqlQuery = "SELECT t.DESC_TIPO_BIEN FROM RUG_CAT_TIPO_BIEN t,V_GARANTIAS_BIENES b WHERE t.ID_TIPO_BIEN = b.ID_TIPO_BIEN AND RELACION_BIEN=?";		
		try{			
			String tabla2 = null;	
			connection = bd.getConnection();
			ps = connection.prepareStatement(sqlQuery);
			ps.setInt(1, relacion);
			
			rs = ps.executeQuery();
					
			while(rs.next()){					
				if(tabla2 != null)tabla2= tabla2 + ",";
				
				if(tabla2==null){
					tabla2= rs.getString("DESC_TIPO_BIEN");
				}else{
					tabla2= tabla2 + rs.getString("DESC_TIPO_BIEN");
				}	
				MyLogger.Logger.log(Level.INFO, "IMPRIME tipo de bienes"+tabla2+">>>>>>");
			}
			
			tabla= tabla2;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection, rs, ps);
		}
		return tabla;
	}
	
	public void insertBoletaPdf(PdfTO pdfTO,UsuarioTO usuarioTO){
//                        MyLogger.Logger.log(Level.INFO, "boleta PDF SAVE " + pdfTO.getIdTramite());
			ConexionBD bd = new ConexionBD();
			Connection connection = bd.getConnection();
			CallableStatement cs = null;		
			String sql ="{call RUG.SP_Alta_Boleta (?,?,?,?,?,? ) }";		
			try{
				cs = connection.prepareCall(sql);
				cs.setInt(1, pdfTO.getIdTramite());
				cs.setBytes(2, pdfTO.getFile());
				//MyLogger.Logger.log(Level.INFO, "file::"+ pdfTO.getFile().toString());
				cs.setInt(3, usuarioTO.getPersona().getIdPersona());
				cs.setString(4, pdfTO.getKey());
				cs.registerOutParameter(5, Types.INTEGER);
				cs.registerOutParameter(6, Types.VARCHAR);
				cs.execute();
				
			}catch(Exception e){
				//e.printStackTrace();
			}finally{
				bd.close(connection,null,cs);
			}
			}

	public BoletaCertificacionTO findBoletaByToken(String pToken) {
		BoletaCertificacionTO boleta = null;
		
		boolean respuesta = false;
		String vToken = "";
		
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		PreparedStatement ps = null;		
		ResultSet rs =null;
		String sql ="select PDF_KEY, ID_TRAMITE, ID_PERSONA, FECHA_REG FROM RUG_BOLETA_PDF where PDF_KEY=?";		
		try{
			ps = connection.prepareCall(sql);
			ps.setString(1, pToken);
			rs =ps.executeQuery();
			if(rs.next()){
				boleta = new BoletaCertificacionTO();
				boleta.setIdTramite(rs.getInt("ID_TRAMITE"));
				boleta.setIdTramiteCert(rs.getInt("ID_PERSONA"));
				boleta.setFechaCert(rs.getDate("FECHA_REG"));
			}
		}catch(Exception e){
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		
		return boleta;
	}
	
	public byte[] getPdfBoleta(Integer pIdTramite) {
		boolean respuesta = false;
		byte file[]= null;
		
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		PreparedStatement ps = null;		
		ResultSet rs =null;
		String sql ="select RB.ARCHIVO from RUG_BOLETA_PDF RB, TRAMITES TR where TR.ID_TRAMITE = ? and TR.ID_TRAMITE_TEMP = RB.ID_TRAMITE";		
		try{
			ps = connection.prepareCall(sql);
			ps.setInt(1, pIdTramite);
			rs =ps.executeQuery();
			if(rs.next()){
				file=rs.getBytes("ARCHIVO");
			}
		}catch(Exception e){
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return file;
	}
	
	public byte[] getBoletaPdf(PdfTO pdfTO){
		MyLogger.Logger.log(Level.INFO, "boleta PDF GET " + pdfTO.getIdTramite() );
			ConexionBD bd = new ConexionBD();
			Connection connection = bd.getConnection();
			PreparedStatement ps = null;
			byte file[]= null;
			ResultSet rs =null;
			String sql ="select PDF FROM  RUG_BOLETAS where ID_TRAMITE=?";		
			try{
				ps = connection.prepareCall(sql);
				ps.setInt(1, pdfTO.getIdTramite());
				rs =ps.executeQuery();
				if(rs.next()){
					file=rs.getBytes("PDF");
				}
			}catch(Exception e){
				try {
					connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}finally{
				bd.close(connection,rs,ps);
			}
			return file;
		}
	
	public BoletaTO AnotacionS(Integer idtramite) {
		ConexionBD bd = new ConexionBD();		
		Connection connection = null;
		BoletaTO boleta2 = null;
		MyLogger.Logger.log(Level.INFO, "idtramite anotacion: " + idtramite);
		
		String sql = "SELECT DESC_TRAMITE_TEMP, ANOTACION, AUTORIDAD_AUTORIZA, VIGENCIA_ANOTACION, VIGENCIA_GARANTIA,"
				+ "ID_GARANTIA, DESC_TIPO_GARANTIA, MONTO_MAXIMO_GARANTIZADO, OTROS_TERMINOS_GARANTIA,"
				+ "ID_TIPO_BIEN,  TIPOS_BIENES_MUEBLES, DESC_BIENES_MUEBLES, UBICACION_BIENES,"
				+ "FECHA_CELEB_ACTO, ID_PERSONA, PER_JURIDICA, NOMBRE_OTORGANTE, AP_PATERNO_OTORGANTE, AP_MATERNO_OTORGANTE,"
				+ "RAZON_SOCIAL_OTORGANTE, RFC, FOLIO_MERCANTIL, CURP,DESC_PARTE FRP FROM V_ANOTACION_FIRMA"
				+ " WHERE ID_TRAMITE_TEMP = ? ";
		ResultSet rs =null;
		PreparedStatement ps =null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idtramite);
			MyLogger.Logger.log(Level.INFO, "" + ps);
			rs = ps.executeQuery();
			MyLogger.Logger.log(Level.INFO, "la consulta tramites terminados: " + sql);
			BoletaTO boleta = null;
			if (rs.next()) {
				boleta = new BoletaTO();
			
				boleta.setIdtramite(idtramite);
				boleta.setTipotransaccion(rs.getString("DESC_TRAMITE_TEMP"));
				boleta.setNombreOtorgante(rs.getString("NOMBRE_OTORGANTE") + " " + rs.getString("AP_PATERNO_OTORGANTE") + " " + rs.getString("AP_MATERNO_OTORGANTE"));
				boleta.setFolio(rs.getString("FOLIO_MERCANTIL"));
				boleta.setAutoridad(rs.getString("AUTORIDAD_AUTORIZA"));
				boleta.setFechacreacion(rs.getDate("FECHA_CELEB_ACTO").toString());				
				boleta.setVigencia(new Integer(rs.getInt("VIGENCIA_ANOTACION")));
				boleta.setCurp(rs.getString("CURP"));			
    
			}
			boleta2 = boleta;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection,rs,ps);
		}
		return boleta2;
	}
	
	public BoletaTO AnotacionC(Integer idtramite) {
		ConexionBD bd = new ConexionBD();		
		Connection connection = null;
		DateUtilRug dateUtil = new DateUtilRug();
		BoletaTO boleta2 = null;
		MyLogger.Logger.log(Level.INFO, "idtramite anotacion: " + idtramite);
		
		String sql = "select A.ID_GARANTIA, A.AUTORIDAD_AUTORIZA, A.ANOTACION, A.ID_TRAMITE_TEMP, A.ID_USUARIO,"				
				+ "G.VIGENCIA,G.ID_TIPO_GARANTIA,G.TIPO_GARANTIA,G.FECHA_CELEB_CONTRATO,G.MONTO_LIMITE,G.FECHA_CELEB_ACTO,"
				+ "G.OTROS_TERMINOS_GARANTIA,G.RELACION_BIEN,G.DESC_BIENES_MUEBLES,G.TIPO_CONTRATO,G.OTROS_TERMINOS_CONTRATO"
				+ " from V_ANOTACION_CON_GARANTIA A,V_DETALLE_GARANTIA G "
				+ " WHERE G.ID_GARANTIA= A.ID_GARANTIA AND A.ID_TRAMITE_TEMP = ? ";
		ResultSet rs = null;
		PreparedStatement ps =null;
		try {
			connection = bd.getConnection();
			 ps = connection.prepareStatement(sql);
			ps.setInt(1, idtramite);
			MyLogger.Logger.log(Level.INFO, ""+ps);
			rs = ps.executeQuery();
			MyLogger.Logger.log(Level.INFO, "la consulta anotacion con garantia: " + sql);
			BoletaTO boleta = null;
			if (rs.next()) {
				boleta = new BoletaTO();
			
				boleta.setIdgarantia(new Integer(rs.getInt("ID_GARANTIA")));
				boleta.setIdtramite(rs.getInt("ID_TRAMITE"));
				boleta.setTipotransaccion(rs.getString("DESCRIPCION"));
				if(rs.getDate("FECHA_INSCR")== null){
					boleta.setFechainicio("");
				}else{boleta.setFechainicio(dateUtil.parseDateToStr(rs.getDate("FECHA_INSCR")));}
				boleta.setNombreOtorgante(rs.getString("NOMBRE"));
				boleta.setFolio(rs.getString("FOLIO_MERCANTIL"));
				boleta.setPrecio(new BigDecimal(rs.getString("PRECIO")));
				boleta.setFechacreacion(rs.getDate("FECHA_CREACION").toString());
				MyLogger.Logger.log(Level.INFO, "vigencia a:"+rs.getInt("VIGENCIA"));
				boleta.setVigencia(new Integer(rs.getInt("VIGENCIA")));
				boleta.setIdtipogarantia(new Integer(rs.getInt("ID_TIPO_GARANTIA")));
				boleta.setTipogarantia(rs.getString("TIPO_GARANTIA"));
				if(rs.getDate("FECHA_CELEB_CONTRATO")==null){
					boleta.setFecacelebcontrato("");
				}else{boleta.setFecacelebcontrato(dateUtil.formatDate(rs.getDate("FECHA_CELEB_CONTRATO")));}
				boleta.setMonto(new BigDecimal(rs.getString("MONTO_LIMITE")));
				if(rs.getDate("FECHA_CELEB_ACTO")==null){
					boleta.setFecha_celeb("");
				}else{boleta.setFecha_celeb(dateUtil.formatDate(rs.getDate("FECHA_CELEB_ACTO")));}
				String otrosTerminosGarantia=clobToString(rs.getClob("OTROS_TERMINOS_GARANTIA"));
				if(otrosTerminosGarantia!=null){
					boleta.setOtrosgarantia("");
				}else{boleta.setOtrosgarantia(otrosTerminosGarantia);}
				if (rs.getInt("RELACION_BIEN") == 0){
					boleta.setRelacionbien(0);
				}else{boleta.setRelacionbien(new Integer(rs.getInt("RELACION_BIEN")));}
				boleta.setDescbienes(rs.getString("DESC_BIENES_MUEBLES"));
				if(rs.getString("TIPO_CONTRATO")==null){
					boleta.setTipocontrato("");
				}else{boleta.setTipocontrato(rs.getString("TIPO_CONTRATO"));}
    
			}
			boleta2 = boleta;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NoDateInfrastructureException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection,rs,ps);
		}
		return boleta2;
	}
	
	public BoletaTO getTramitetrans(Integer idTramiteinc, Integer idTipoTramite){
		
		ConexionBD bd = new ConexionBD();		
		Connection connection = null;
		BoletaTO boleta = new BoletaTO();
		String sql ="SELECT ID_CANCELACION, ID_INSCRIPCION FROM V_CANCELACION_TRANSMISION WHERE ID_TRAMITE_TEMP= ? AND ID_TIPO_TRAMITE= ?";
		ResultSet rs =null;
		PreparedStatement ps =null;
		try {
			connection = bd.getConnection();
			 ps = connection.prepareStatement(sql);
			ps.setInt(1, idTramiteinc);
			ps.setInt(2, idTipoTramite);
			
			rs = ps.executeQuery();
			if (rs.next()) {
				boleta.setIdcancelacion(new Integer(rs.getInt("ID_CANCELACION")));
				boleta.setIdinscripcion(new Integer(rs.getInt("ID_INSCRIPCION")));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection,rs,ps);
		}	
		
		return boleta;
	}
	
	
 public List<PersonaTO> getPartes(Integer idTramite,Integer idParte) throws NoDataFoundException{
		List<PersonaTO > personaTOs = new ArrayList<PersonaTO>();
		String sql="SELECT ID_TRAMITE, ID_PERSONA, ID_PARTE,OTORGANTE_IGUAL_DEUDOR, PER_JURIDICA, NOMBRE_PARTE, FOLIO_ELECTRONICO,RFC, CURP ,E_MAIL,TELEFONO,EXTENSION,DOMICILIO FROM V_DETALLE_BOLETA_PARTES WHERE ID_TRAMITE=? AND ID_PARTE=?";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		PreparedStatement ps = null;	
		ResultSet rs =null;
		try{
			ps = connection.prepareCall(sql);
			ps.setInt(1, idTramite);
			ps.setInt(2, idParte);
			rs =ps.executeQuery();
			while(rs.next()){
				System.out.println("Nombre " + rs.getString("NOMBRE_PARTE") + " RFC " + rs.getString("RFC") + " CURP " + rs.getString("CURP") + " EMAIL " + rs.getString("E_MAIL") + " TELEFONO " + rs.getString("TELEFONO") + " EXTENSION " + rs.getString("EXTENSION") +
						" FOLIO " + rs.getString("FOLIO_ELECTRONICO") +  " PER " + rs.getString("PER_JURIDICA") + " DOMICILIO " + rs.getString("DOMICILIO") + " OTORGANTE " + rs.getString("OTORGANTE_IGUAL_DEUDOR"));
				PersonaTO personaTO= new PersonaTO();
				personaTO.setNombre(rs.getString("NOMBRE_PARTE"));
				personaTO.setRfc(rs.getString("RFC"));
				personaTO.setCurp(rs.getString("CURP"));
				personaTO.setCorreoElectronico(rs.getString("E_MAIL"));
				if(rs.getString("TELEFONO") == null){
					personaTO.setTelefono("");
				}else{
					personaTO.setTelefono(rs.getString("TELEFONO"));
				}
				if(rs.getString("EXTENSION") == null){
					personaTO.setExtension("");
				}else{
					personaTO.setExtension(rs.getString("EXTENSION"));
				}
				if(rs.getString("FOLIO_ELECTRONICO") == null){
					personaTO.setFolioElectronico("");
				}else{
					personaTO.setFolioElectronico(rs.getString("FOLIO_ELECTRONICO"));
				}
				personaTO.setPersoalidadJuridica(rs.getString("PER_JURIDICA"));
				if(rs.getString("DOMICILIO") == null){
					personaTO.setDomicilio("");
				}else{
					personaTO.setDomicilio(rs.getString("DOMICILIO"));
				}
				personaTO.setMismoOtorgante(rs.getString("OTORGANTE_IGUAL_DEUDOR"));
				personaTOs.add(personaTO);
			}
//			if(personaTOs.size()==0){
//				MyLogger.Logger.log(Level.INFO, ":::No tiene parte ::" + idParte+" el tramite::"+idTramite);
//				throw new NoDataFoundException(":::No tiene parte ::" + idParte+" el tramite::"+idTramite);
//			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return personaTOs;
	}

 public List<PersonaTO> getPartes(Integer idTramite,Integer idParte,Connection connection) throws NoDataFoundException{
		List<PersonaTO > personaTOs = new ArrayList<PersonaTO>();
		String sql="SELECT ID_TRAMITE, ID_PERSONA, ID_PARTE, PER_JURIDICA, NOMBRE_PARTE, FOLIO_ELECTRONICO,RFC ,E_MAIL,TELEFONO,EXTENSION FROM V_DETALLE_BOLETA_PARTES WHERE ID_TRAMITE=? AND ID_PARTE=?";
		PreparedStatement ps = null;	
		ResultSet rs =null;
		try{
			ps = connection.prepareCall(sql);
			ps.setInt(1, idTramite);
			ps.setInt(2, idParte);
			rs =ps.executeQuery();
			while(rs.next()){
				PersonaTO personaTO= new PersonaTO();
				personaTO.setNombre(rs.getString("NOMBRE_PARTE"));
				personaTO.setRfc(rs.getString("RFC"));
				personaTO.setCorreoElectronico(rs.getString("E_MAIL"));
				personaTO.setTelefono(rs.getString("TELEFONO"));
				personaTO.setExtension(rs.getString("EXTENSION"));
				personaTO.setFolioElectronico(rs.getString("FOLIO_ELECTRONICO"));
				personaTOs.add(personaTO);
			}
			if(personaTOs.size()==0){
				MyLogger.Logger.log(Level.WARNING, ":::No tiene parte ::" + idParte+" el tramite::"+idTramite);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				MyLogger.Logger.log(Level.WARNING, "No cerro RS");
			}finally{
				try {
					ps.close();
				} catch (SQLException e) {
					MyLogger.Logger.log(Level.WARNING, "No cerro RS");
				}	
			}
			
		}
		return personaTOs;
	}

 public List <GarantiaTO> getGarantiaTramite(Integer idTramite){
	 List <GarantiaTO> regresa = new ArrayList<GarantiaTO>();
	   GarantiaTO garantiaTO= null;
		String sql=" select id_garantia, cve_rastreo, id_tramite, FECHA_REG, VIGENCIA from v_detalle_tramite_masivo where ID_FIRMA_MASIVA = ?"; 
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		PreparedStatement ps = null;	
		ResultSet rs =null;
		try{
			ps = connection.prepareCall(sql);
			ps.setInt(1, idTramite);
			rs =ps.executeQuery();
			while(rs.next()){
				garantiaTO = new GarantiaTO();
				garantiaTO.setIdTramite(rs.getString("id_tramite"));
				garantiaTO.setIdGarantia(rs.getString("id_garantia"));	
				garantiaTO.setClaveRastreo(rs.getString("cve_rastreo"));	
				garantiaTO.setVigencia(rs.getString("VIGENCIA"));
				garantiaTO.setFechaInscripcion(rs.getString("FECHA_REG"));	
				regresa.add(garantiaTO);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return regresa;
	}
 
 public DetalleTO getDetalleGarantiaTramite(DetalleTO detalleTO) throws NoDataFoundException{
	   GarantiaTO garantiaTO= null;
		String sql=" SELECT CONTENIDO_RESOL,ANOTACION_JUEZ,ID_TRAMITE, ID_GARANTIA,TIPO_TRAMITE, TIPO_GARANTIA, FECHA_ACTO_CONVENIO, "+
								"MONTO_MAXIMO_GARANTIZADO, OTROS_TERMINOS_GARANTIA, TIPOS_BIENES_GARANTIA, DESC_GARANTIA, "+
								"VIGENCIA, ID_USUARIO, CVE_PERFIL,NOMBRE_USUARIO, FECHA_CREACION, FOLIO_OTORGANTE, " +
								" CAMBIOS_BIENES_MONTO, FECHA_FIN_CONTRATO, OBSERVACIONES, "+
								"FECHA_INICIO_CONTRATO,INSTRUMENTO_PUBLICO, OTROS_TERMINOS_CONTRATO, "+
								"TIPO_CONTRATO,TIPO_CONTRATO_FU,FECHA_INICIO_CONTRATO_FU,FECHA_FIN_CONTRATO_FU, "+
								"OTROS_TERMINOS_CONTRATO_FU, ID_TIPO_GARANTIA,NO_GARANTIA_PREVIA_OT FROM V_DETALLE_BOLETA_NUEVO WHERE ID_TRAMITE=?"; 
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		PreparedStatement ps = null;	
		ResultSet rs =null;
		try{
			ps = connection.prepareCall(sql);
			ps.setInt(1, detalleTO.getIdTramite());
			rs =ps.executeQuery();
			if(rs.next()){
				garantiaTO = new GarantiaTO();
				garantiaTO.setIdTramite(rs.getString("ID_TRAMITE"));
				garantiaTO.setIdGarantia(rs.getString("ID_GARANTIA"));
				detalleTO.setTipoTramite(rs.getString("TIPO_TRAMITE"));
				garantiaTO.setTipoGarantia(rs.getString("TIPO_GARANTIA"));
				garantiaTO.setFechaActoConvenioGarantia(rs.getString("FECHA_ACTO_CONVENIO"));
				garantiaTO.setMontoMaximo(rs.getString("MONTO_MAXIMO_GARANTIZADO"));
				garantiaTO.setOtrosTerminosCondiciones(clobToString(rs.getClob("OTROS_TERMINOS_GARANTIA")));
				garantiaTO.setTipoBienes(rs.getString("TIPOS_BIENES_GARANTIA"));
				garantiaTO.setDescripcionBienes(clobToString(rs.getClob("DESC_GARANTIA")));
				garantiaTO.setVigencia(rs.getString("VIGENCIA"));
				garantiaTO.setFechaInscripcion(rs.getString("FECHA_CREACION"));
				garantiaTO.setFolioOtorgante(rs.getString("FOLIO_OTORGANTE"));
				garantiaTO.setCambiosBienesMonto(rs.getString("CAMBIOS_BIENES_MONTO"));
				garantiaTO.setFechaFinContrato(rs.getString("FECHA_FIN_CONTRATO"));
				garantiaTO.setFechaInicioContrato(rs.getString("FECHA_INICIO_CONTRATO"));
				garantiaTO.setInstrumentoPublico(rs.getString("INSTRUMENTO_PUBLICO"));	
				garantiaTO.setOtrosTerminosContrato(clobToString(rs.getClob("OTROS_TERMINOS_CONTRATO")));
				garantiaTO.setOtrosTerminosGarantia(clobToString(rs.getClob("OTROS_TERMINOS_GARANTIA")));
				garantiaTO.setTipoContrato(rs.getString("TIPO_CONTRATO"));
				garantiaTO.setJuezAnotacion(rs.getString("ANOTACION_JUEZ"));
				garantiaTO.setContenidoResolucion(clobToString(rs.getClob("CONTENIDO_RESOL")));
				garantiaTO.setTipoContratoBasa(rs.getString("OBSERVACIONES"));
				garantiaTO.setFechaInicioContratoBasa(rs.getString("FECHA_INICIO_CONTRATO_FU"));
				garantiaTO.setFechaFinContratoBasa(rs.getString("FECHA_FIN_CONTRATO_FU"));
				garantiaTO.setOtrosTerminosContratoBasa(clobToString(rs.getClob("OTROS_TERMINOS_CONTRATO_FU")));
				garantiaTO.setIdTipoGarantia(rs.getInt("ID_TIPO_GARANTIA"));
				garantiaTO.setNoGarantiaPreviaOt(rs.getString("NO_GARANTIA_PREVIA_OT"));				
				
				PersonaTO userInscribe= new PersonaTO();
				userInscribe.setNombre(rs.getString("NOMBRE_USUARIO"));
				userInscribe.setIdPersona(rs.getInt("ID_USUARIO"));
				userInscribe.setPerfil(rs.getString("CVE_PERFIL"));
				detalleTO.setGarantiaTO(garantiaTO);
				detalleTO.setPersonaInscribe(userInscribe);
				
				
			}else{
				throw new NoDataFoundException("No encontro el tramite en V_DETALLE_BOLETA el idTramite::"+detalleTO.getIdTramite());
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return detalleTO;
	}
 public Integer getIdTramitebyIdTramiteNuevo(Integer idTramiteNuevo) throws NoDataFoundException{
		String sql="Select ID_TRAMITE FROM TRAMITES where ID_TRAMITE_TEMP=?";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		PreparedStatement ps = null;	
		Integer idTramite= null;
		ResultSet rs =null;
		try{
			ps = connection.prepareCall(sql);
			ps.setInt(1, idTramiteNuevo);
			rs =ps.executeQuery();
			if(rs.next()){
				idTramite= new Integer(rs.getInt("ID_TRAMITE"));
			}else{
				throw new NoDataFoundException("No existe idTramite con el idtramiteNuevo::"+idTramiteNuevo);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return idTramite;
	}
 public Integer getIdBoletaByIdTipoTramite(Integer idTipoTramite) throws NoDataFoundException{
		String sql="select ID_BOLETA,DESC_BOLETA,ID_TIPO_TRAMITE,ID_DOCTO  from RUG_CAT_BOLETA where ID_TIPO_TRAMITE=? and ID_DOCTO=1";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		PreparedStatement ps = null;	
		Integer idBoleta= null;
		ResultSet rs =null;
		try{
			ps = connection.prepareCall(sql);
			ps.setInt(1, idTipoTramite);
			rs =ps.executeQuery();
			if(rs.next()){
				idBoleta= new Integer(rs.getInt("ID_BOLETA"));
			}else{
				throw new NoDataFoundException("No existe idBoleta con el idTipoTramite::"+idTipoTramite);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return idBoleta;
	}
 public String getCvePerfilByIdUser(Integer idUser) throws NoDataFoundException{
		String sql="select CVE_PERFIL from v_usuario_sesion_rug where ID_PERSONA=?";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		PreparedStatement ps = null;	
		String cve_perfil=null; 
		ResultSet rs =null;
		try{
			ps = connection.prepareCall(sql);
			ps.setInt(1, idUser);
			rs =ps.executeQuery();
			if(rs.next()){
				cve_perfil= new String(rs.getString("CVE_PERFIL"));
			}else{
				throw new NoDataFoundException("No existe idTramite con el idtramiteNuevo::"+cve_perfil);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return cve_perfil;
	}
 public List<DetalleTO> getHistoricoDetalle(Integer idGarantia, Integer idTramite) throws NoDataFoundException{
	 List<DetalleTO> detalleTOs = new ArrayList<DetalleTO>();
	String sql="SELECT ID_TRAMITE,TIPO_TRAMITE,ID_TIPO_TRAMITE FROM V_DETALLE_BOLETA_NUEVO WHERE  ID_GARANTIA =? and ID_TRAMITE <=? order by ID_TRAMITE desc";
	ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		PreparedStatement ps = null;	
		DetalleTO detalleTO= null;
		ResultSet rs =null;
		try{
			ps = connection.prepareCall(sql);
			ps.setInt(1, idGarantia);
			ps.setInt(2, idTramite);
			rs =ps.executeQuery();
			while(rs.next()){
				detalleTO= new DetalleTO();
				detalleTO.setIdTramite(rs.getInt("ID_TRAMITE"));
				detalleTO.setTipoTramite(rs.getString("TIPO_TRAMITE"));
				detalleTO.setIdTipoTramite(rs.getInt("ID_TIPO_TRAMITE"));
				detalleTOs.add(detalleTO);
			}
			if(detalleTOs.size()==0){
				throw new NoDataFoundException("No se encontraron tramites con ell idGarantia::"+idGarantia);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return detalleTOs;
	}

 public BoletaAvisoPreventivoTO getAvisoPreventivo(Integer idTramite) throws NoDataFoundException{
	 BoletaAvisoPreventivoTO avisoPreventivoTO= null;
		String sql="SELECT PERFIL, ID_TRAMITE, DESC_BIENES, NOMBRE_USUARIO, FECHA_CREACION,VIGENCIA,ANOTACION_JUEZ,FOLIO_OTORGANTE FROM V_DETALLE_AVISO_PREV where ID_TRAMITE=?";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		PreparedStatement ps = null;	
		ResultSet rs =null;
		try{
			ps = connection.prepareCall(sql);
			ps.setInt(1, idTramite);
			rs =ps.executeQuery();
			if(rs.next()){
				avisoPreventivoTO= new BoletaAvisoPreventivoTO();
				avisoPreventivoTO.setIdTramite(rs.getInt("ID_TRAMITE"));
				avisoPreventivoTO.setDescripcionBienes(rs.getString("DESC_BIENES"));
				avisoPreventivoTO.setNombreUsuarioInscribe(rs.getString("NOMBRE_USUARIO"));
				avisoPreventivoTO.setFechaCreacion(rs.getString("FECHA_CREACION"));
				avisoPreventivoTO.setPerfil(rs.getString("PERFIL"));
				avisoPreventivoTO.setVigencia(rs.getString("VIGENCIA"));
				avisoPreventivoTO.setAnotacionJuez(rs.getString("ANOTACION_JUEZ"));
				avisoPreventivoTO.setFolioOtorgante(rs.getString("FOLIO_OTORGANTE"));
				
			}else{
						throw new NoDataFoundException("No se encontro aviso preventivo  con ell idtramite::"+idTramite);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return avisoPreventivoTO;
	}
 
 public BoletaAnotacionSnGarantia getAnotacionSnGarantia(Integer idTramite) throws NoDataFoundException{
	 BoletaAnotacionSnGarantia anotacionSnGarantia= null;
		String sql="select ID_TRAMITE, VIGENCIA_ANOTACION, FECHA_CREACION, ID_USUARIO, NOMBRE_USUARIO, ANOTACION, AUTORIDAD_INSTRUYE, PERFIL, FOLIO_OTORGANTE FROM V_BOLETA_ANOTACION  where ID_TRAMITE=?";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		PreparedStatement ps = null;	
		ResultSet rs =null;
		try{
			ps = connection.prepareCall(sql);
			ps.setInt(1, idTramite);
			rs =ps.executeQuery();
			if(rs.next()){
				anotacionSnGarantia = new BoletaAnotacionSnGarantia();
				anotacionSnGarantia.setIdTramite(rs.getInt("ID_TRAMITE"));
				anotacionSnGarantia.setAnotacion(rs.getString("ANOTACION"));
				anotacionSnGarantia.setVigencia(rs.getString("VIGENCIA_ANOTACION"));
				anotacionSnGarantia.setFechaInscripcion(rs.getString("FECHA_CREACION"));
				anotacionSnGarantia.setNombreUsuario(rs.getString("NOMBRE_USUARIO"));
				anotacionSnGarantia.setAutoridadInstruye(rs.getString("AUTORIDAD_INSTRUYE"));
				anotacionSnGarantia.setPerfil(rs.getString("PERFIL"));
				anotacionSnGarantia.setFolioOtorgante(rs.getString("FOLIO_OTORGANTE"));
			}else{
						throw new NoDataFoundException("No se encontro anotacion sin garantia  con el idtramite::"+idTramite);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return anotacionSnGarantia;
	}
 
 public BoletaCertificacionTO getcertificacion(Integer idTramite) throws NoDataFoundException{
	 BoletaCertificacionTO certificacion= null;
		String sql="select ID_TRAMITE_CERT, ID_TRAMITE, ID_GARANTIA, ID_TIPO_TRAMITE, FECHA_CERT FROM V_RUG_CERTIFICACIONES  WHERE ID_TRAMITE_CERT=?";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		PreparedStatement ps = null;	
		ResultSet rs =null;
		try{
			ps = connection.prepareCall(sql);
			ps.setInt(1, idTramite);
			rs =ps.executeQuery();
			if(rs.next()){
				certificacion = new BoletaCertificacionTO();
				certificacion.setIdTramiteCert(rs.getInt("ID_TRAMITE_CERT"));
				certificacion.setIdTramite(rs.getInt("ID_TRAMITE"));
				certificacion.setIdGarantia(rs.getInt("ID_GARANTIA"));
				certificacion.setIdTipoTramite(rs.getInt("ID_TIPO_TRAMITE"));
				certificacion.setFechaCert(rs.getDate("FECHA_CERT"));
			}else{
						throw new NoDataFoundException("No se encontro certificacion con el idtramiteCert::"+idTramite);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return certificacion;
	}
 

 
 public AnotacionSinGarantiaTO getDatosAnotacionSinGarantia(Integer idTramite) throws NoDataFoundException{
	 MyLogger.Logger.log(Level.INFO, "El tramite tiene : "+idTramite);
	 AnotacionSinGarantiaTO certificacion= null;
					String sql="SELECT ID_TRAMITE, ID_TRAMITE_TEMP, ID_ANOTACION_PADRE, ID_ANOTACION, ID_GARANTIA, ID_TIPO_TRAMITE," +
							" DESCRIPCION, ID_STATUS_TRAM, DESCRIP_STATUS, ID_USUARIO, USUARIO, ID_PERSONA, PER_JURIDICA, FOLIO_MERCANTIL," +
							" RFC, CURP, ID_NACIONALIDAD, NOMBRE_PERSONA, AP_PATERNO, AP_MATERNO, RAZON_SOCIAL, AUTORIDAD_AUTORIZA, ANOTACION," +
							" RESOLUCION, VIGENCIA_ANOTACION, SOLICITANTE_RECTIFICA, FECHA_STATUS, FECHA_INSCRIPCION" +
							" FROM V_ANOTACION_TRAMITES" +
							" WHERE ID_TRAMITE = ?";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		PreparedStatement ps = null;	
		ResultSet rs =null;
		try{
			ps = connection.prepareCall(sql);
			ps.setInt(1, idTramite);
			rs =ps.executeQuery();
			if(rs.next()){
				certificacion = new AnotacionSinGarantiaTO();	
				certificacion.setIdTramite(rs.getInt("ID_TRAMITE"));
				certificacion.setDescripcionTram(rs.getString("DESCRIPCION"));
				certificacion.setIdAnotacion(rs.getInt("ID_ANOTACION"));
				certificacion.setAutoridad(rs.getString("AUTORIDAD_AUTORIZA"));
				certificacion.setIdUsuario(rs.getInt("ID_PERSONA"));
				certificacion.setTipoPersona(rs.getString("PER_JURIDICA"));
				certificacion.setFolioMercantil(rs.getString("FOLIO_MERCANTIL"));
				certificacion.setAnotacion(rs.getString("ANOTACION"));
				certificacion.setNombre(rs.getString("NOMBRE_PERSONA"));
				certificacion.setaPaterno(rs.getString("AP_PATERNO"));	
				certificacion.setaMaterno(rs.getString("AP_MATERNO"));
				certificacion.setRazonSocial(rs.getString("RAZON_SOCIAL"));
				certificacion.setMeses(rs.getInt("VIGENCIA_ANOTACION"));
				certificacion.setFechaStatus(rs.getString("FECHA_STATUS"));
				certificacion.setFechaInscripcion(rs.getString("FECHA_INSCRIPCION"));
			}else{
						throw new NoDataFoundException("No se encontro anotaci�n sin garant�a::"+idTramite);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return certificacion;
	}
}
