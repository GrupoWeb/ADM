package mx.gob.se.rug.detallegarantia.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import mx.gob.economia.dgi.framework.dao.exception.JdbcDaoException;
import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.detallegarantia.to.DatosAnotacion;
import mx.gob.se.rug.detallegarantia.to.DatosGaranTO;
import mx.gob.se.rug.detallegarantia.to.DetalleTO;
import mx.gob.se.rug.detallegarantia.to.PartesTO;
import mx.gob.se.rug.exception.NoDataFoundException;
import mx.gob.se.rug.inscripcion.to.BienEspecialTO;
import mx.gob.se.rug.inscripcion.to.TipoBienTO;
import mx.gob.se.rug.util.MyLogger;

public class DetalleDAO{
	
	
	
	 public DatosAnotacion getDatosAnotacion(Integer idTramite) throws NoDataFoundException{
		 DatosAnotacion  datosAnotacion= null;
			String sql=" SELECT CONTENIDO_RESOL,ANOTACION_JUEZ,VIGENCIA FROM V_DETALLE_BOLETA_NUEVO WHERE ID_TRAMITE=?"; 
			ConexionBD bd = new ConexionBD();
			Connection connection = bd.getConnection();
			PreparedStatement ps = null;	
			ResultSet rs =null;
			try{
				ps = connection.prepareCall(sql);
				ps.setInt(1,idTramite);
				rs =ps.executeQuery();
				if(rs.next()){
					datosAnotacion = new DatosAnotacion();
					datosAnotacion.setContenidoResolucion(rs.getString("CONTENIDO_RESOL"));
					datosAnotacion.setPersonaAutoridadInstruye(rs.getString("ANOTACION_JUEZ"));
					datosAnotacion.setVigenciaAnotacion(rs.getString("VIGENCIA"));
					
				}else{
					throw new NoDataFoundException("No encontro el tramite en V_DETALLE_BOLETA el idTramite::"+idTramite);
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				bd.close(connection,rs,ps);
			}
			return datosAnotacion;
		}
	
	
	
	public DatosGaranTO datosGaran (Integer idGarantia, Integer idTramite){
		DatosGaranTO datosTO = new DatosGaranTO();
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		System.out.println("idgarantia " + idGarantia);
		String sql = "select FECHA_INSCRIPCION, DESCRIPCION, FECHA_CREACION, FECHA_ULTIMO,ID_TIPO_TRAMITE from V_GARANTIA_UTRAM where ID_GARANTIA = ? AND ID_TRAMITE= ?";
		ResultSet rs =null;
		PreparedStatement ps =null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idGarantia);
			ps.setInt(2, idTramite);
			rs = ps.executeQuery();
			while(rs.next()){
				datosTO.setFechaInsc(rs.getDate("FECHA_INSCRIPCION"));
				datosTO.setFechUltAsiento(rs.getDate("FECHA_ULTIMO"));
				datosTO.setTipoAsiento(rs.getString("DESCRIPCION"));
				datosTO.setFechaAsiento(rs.getDate("FECHA_CREACION"));
				datosTO.setIdTipoAsiento(rs.getInt("ID_TIPO_TRAMITE"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return datosTO;
	}

	public Boolean noHayCancel (Integer idGarantia){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		Boolean regresa=true;
		System.out.println("idgarantia " + idGarantia);
		String sql = "select ID_TIPO_TRAMITE from V_TRAMITES_TERMINADOS where ID_GARANTIA = ? order by fecha_creacion asc";
		ResultSet rs =null;
		PreparedStatement ps =null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idGarantia);
			rs = ps.executeQuery();
			while(rs.next()){
//				System.out.println("el tipo de tramite " + rs.getInt("ID_TIPO_TRAMITE"));
				if ((rs.getInt("ID_TIPO_TRAMITE")==4) || (rs.getInt("ID_TIPO_TRAMITE")==13)){
					regresa=false;
				}
				else if (rs.getInt("ID_TIPO_TRAMITE")==17){
					regresa=true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return regresa;
	}
	
//-------------------------vigenciaValida--------------------------------------------------------------------------------------------------------
	public Boolean vigenciaValida (Integer idGarantia){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		Boolean regresa=false;
		System.out.println("idgarantia vigencia " + idGarantia);
		String sql = "select count(*) VIGENCIAS from V_TRAMITES_TERM_VIGENCIA where id_garantia = ?";  
		ResultSet rs =null;
		PreparedStatement ps =null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idGarantia);
			rs = ps.executeQuery();
			if(rs.next()){
				MyLogger.Logger.log(Level.INFO, "El resultado del query Vigencia " + rs.getInt("VIGENCIAS"));
				MyLogger.Logger.log(Level.INFO, "El parametro es: " +idGarantia );
				if ((rs.getInt("VIGENCIAS") == 0)){
				regresa=true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return regresa;
	}	
//---------------------------------------------------------------------------------------------------------------------------------------------------------
	
	public Integer getIdAcreedorByIdTramite (Integer idTramite){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		Integer idAcreedor= null	;
		System.out.println("Tramites jj22 " + idTramite);
		String sql = "Select ID_PERSONA from RUG_REL_TRAM_PARTES Where ID_TRAMITE = ?  AND ID_PARTE = 4";
		ResultSet rs =null;
		PreparedStatement ps =null;
		try {
			connection = bd.getConnection();
			 ps = connection.prepareStatement(sql);
			ps.setInt(1, idTramite);
			rs = ps.executeQuery();
			if(rs.next()){
				idAcreedor= new Integer(rs.getInt("ID_PERSONA"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return idAcreedor;
	}
	
	public String showAcreedorR(Integer idGarantia){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		String nomAcreedor="";
		System.out.println("idgarantia " + idGarantia);
		
		String sql = "select NOMBRE_ACREEDOR from V_GARANTIA_ACREEDOR_REP where ID_GARANTIA = ?";
		ResultSet rs =null;
		PreparedStatement ps =null;
		try {
			connection = bd.getConnection();
			 ps = connection.prepareStatement(sql);
			ps.setInt(1, idGarantia);
			rs = ps.executeQuery();
			while(rs.next()){
				nomAcreedor= rs.getString("NOMBRE_ACREEDOR");
			}
			ps.close();
		} catch (SQLException e) {
			MyLogger.Logger.log(Level.SEVERE,"Error al intentar loclizar Acreedor",e);
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return nomAcreedor;
	} 
	
	public String showAcreedorRepresentado(Integer idTramite){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		String nomAcreedor="";
		MyLogger.Logger.log(Level.FINEST,"sacando acreedor representado por idTramite " + idTramite);
		
		String sql = "SELECT nombre_parte FROM v_detalle_boleta_partes WHERE id_tramite = ? and id_parte = 4";
		ResultSet rs =null;
		PreparedStatement ps =null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idTramite);
			rs = ps.executeQuery();
			while(rs.next()){
				nomAcreedor= rs.getString("nombre_parte");
				MyLogger.Logger.log(Level.FINEST,"sacando acreedor representado::" + rs.getString("nombre_parte"));
			}
			ps.close();
		} catch (SQLException e) {
			MyLogger.Logger.log(Level.SEVERE,"Error al intentar loclizar Acreedor",e);
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return nomAcreedor;
	} 

	
	public String showAcreedorRepresentadoNew(Integer idTramite){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		String nomAcreedor="";
		MyLogger.Logger.log(Level.FINEST,"sacando acreedor representado por idTramite " + idTramite);
                System.out.println("Tramite " + idTramite);
		//String sql = "SELECT nombre_parte FROM v_detalle_boleta_partes WHERE id_tramite = ? and id_parte = 4";
		String sql = "SELECT CASE WHEN PER_JURIDICA = 'PM' " +
					"THEN RPH.RAZON_SOCIAL ELSE RPH.NOMBRE_PERSONA || ' ' || " +
					"RPH.AP_PATERNO || ' ' ||" +
					" RPH.AP_MATERNO END AS NOMBRE_PARTE" +
					" FROM RUG.RUG_PERSONAS_H RPH " +
					"WHERE ID_TRAMITE = ? AND ID_PARTE = 4";
		ResultSet rs =null;
		PreparedStatement ps =null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idTramite);
			rs = ps.executeQuery();
			while(rs.next()){
				nomAcreedor= rs.getString("NOMBRE_PARTE");
				MyLogger.Logger.log(Level.FINEST,"sacando acreedor representado::" + rs.getString("NOMBRE_PARTE"));
				System.out.println("::::::::::nombre::::: "+nomAcreedor);
			}
			ps.close();
		} catch (SQLException e) {
			MyLogger.Logger.log(Level.SEVERE,"Error al intentar loclizar Acreedor",e);
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return nomAcreedor;
	} 
	
	
	
	
	
	// TODO: Hugo D. JimenezJuarez. 02/09/2010
	// Metodo que regresa un tipo AcreedorTO el cual
	// trae la informacion basica del acreedor representado
	public AcreedorTO showAcreedorDetalle(Integer idGarantia){
		AcreedorTO acreedorRepresentado = new AcreedorTO();
		
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs =null;
		PreparedStatement ps =null;
				
		String sql = " SELECT ID_PERSONA, ";
			sql += " NOMBRE_ACREEDOR, PERSONA_JURIDICA ";
			sql += " FROM V_GARANTIA_ACREEDOR_REP WHERE ID_GARANTIA = ? ";
			
		try {
			connection = bd.getConnection();
			 ps = connection.prepareStatement(sql);
			
			ps.setInt(1, idGarantia);
			rs = ps.executeQuery();
			
			while(rs.next()){
				acreedorRepresentado.setIdAcreedor(rs.getString("ID_PERSONA"));
				acreedorRepresentado.setNombreCompleto(rs.getString("NOMBRE_ACREEDOR"));
				acreedorRepresentado.setTipoPersona(rs.getString("PERSONA_JURIDICA").equals("PF")?"Persona Fisica" : "Persona Moral" );
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		finally{
			bd.close(connection,rs,ps);
		}
		return acreedorRepresentado;
	} 
	
	//CONSULTAS MAESTRO-DETALLE
	public DetalleTO getDetalle(Integer idgarantia, Integer idtramite) {
		DetalleTO detalle = new DetalleTO();
		//String s = formatter.format(1234.56); 
		
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs = null;
		PreparedStatement ps =null;
		//DateUtilRug dur = new DateUtilRug();
		try{
			String sqlQuery = "";
			sqlQuery = "SELECT ID_GARANTIA,ID_TIPO_GARANTIA,MONTO_LIMITE,DESC_BIENES_MUEBLES,FECHA_CELEB_ACTO,FECHA_CELEB_CONTRATO,OTROS_TERMINOS_CONTRATO,OTROS_TERMINOS_GARANTIA,TIPO_GARANTIA,VIGENCIA,RELACION_BIEN,TIPO_CONTRATO,ID_CONTRATO,CAMBIOS_BIENES_MONTO," +
					  "INSTRUMENTO_PUBLICO,ID_MONEDA,DESC_MONEDA,FECHA_FIN_OB,FECHA_FIN_ACTO,NO_GARANTIA_PREVIA_OT,ES_PRIORITARIA,OTROS_REGISTROS,TXT_REGISTROS,OBSERVACIONES "
					  + "FROM V_DETALLE_GARANTIA WHERE ID_GARANTIA = ? AND ID_TRAMITE = ? ORDER BY ID_CONTRATO DESC";
			
			 ps=connection.prepareStatement(sqlQuery);
			ps.setInt(1, idgarantia);	
			ps.setInt(2, idtramite);
			System.out.println("sql:  " + sqlQuery);
			System.out.println("parametros: " + idgarantia + " " + idtramite);
			rs = ps.executeQuery();
			if(rs.next()){
				detalle.setIdgarantia(idgarantia);
				System.out.println("ID_TIPO_GARANTIA" +  rs.getInt("ID_TIPO_GARANTIA"));
				detalle.setIdtipogarantia(new Integer(rs.getInt("ID_TIPO_GARANTIA")));
				System.out.println("detalle ID_TIPO_GARANTIA " +  detalle.getIdtipogarantia());
				detalle.setTipogarantia(rs.getString("TIPO_GARANTIA"));				
				if(rs.getDate("FECHA_CELEB_ACTO")==null){
					detalle.setFechaCelebracionObligacion(null);
				//}else{detalle.setFechaCelebracionObligacion(dur.cambioDeFormato(rs.getString("FECHA_CELEB_ACTO")));}	
				}else{detalle.setFechaCelebracionObligacion(rs.getDate("FECHA_CELEB_ACTO"));}
				System.out.println("MONTO_LIMITE " + rs.getString("MONTO_LIMITE"));
				detalle.setMonto(rs.getString("MONTO_LIMITE"));
				System.out.println("MONTO_LIMITE " + detalle.getMonto());
				if(rs.getString("OTROS_TERMINOS_GARANTIA")==null){
					detalle.setOtrosgarantia("");
				}else{detalle.setOtrosgarantia(rs.getString("OTROS_TERMINOS_GARANTIA"));}
				if(rs.getString("DESC_BIENES_MUEBLES")==null){
					detalle.setDescbienes("");
				}else{detalle.setDescbienes(rs.getString("DESC_BIENES_MUEBLES"));}
				detalle.setTipocontrato(rs.getString("TIPO_CONTRATO"));
				if(rs.getDate("FECHA_CELEB_CONTRATO")==null){
					detalle.setFecacelebcontrato(null);	
				//}else{detalle.setFecacelebcontrato(dur.cambioDeFormato(rs.getString("FECHA_CELEB_CONTRATO")));}
				}else{detalle.setFecacelebcontrato(rs.getDate("FECHA_CELEB_CONTRATO"));}
				if(rs.getString("OTROS_TERMINOS_CONTRATO")==null){
					detalle.setOtroscontrato("");
				}else{detalle.setOtroscontrato(rs.getString("OTROS_TERMINOS_CONTRATO"));}
				if(rs.getString("OBSERVACIONES")==null){
					detalle.setOtrosterminos("");
				}else{detalle.setOtrosterminos(rs.getString("OBSERVACIONES"));}
				detalle.setVigencia(rs.getInt("VIGENCIA"));
				System.out.println("rs.getInt('RELACION_BIEN') " + rs.getInt("RELACION_BIEN"));
				if(rs.getInt("RELACION_BIEN")==0 ){
					detalle.setRelacionbien(0);
				}else{detalle.setRelacionbien(new Integer(rs.getInt("RELACION_BIEN")));}			
				if(rs.getString("INSTRUMENTO_PUBLICO")==null){
					detalle.setInstrumento("");
				}else{detalle.setInstrumento(rs.getString("INSTRUMENTO_PUBLICO"));}
				if(rs.getString("CAMBIOS_BIENES_MONTO")==null){
					detalle.setCambio("");
				}else{detalle.setCambio(rs.getString("CAMBIOS_BIENES_MONTO"));}				
				detalle.setIdcontrato(new Integer(rs.getInt("ID_CONTRATO")));
				detalle.setDesmoneda(rs.getString("DESC_MONEDA"));
				detalle.setIdmoneda(rs.getInt("ID_MONEDA"));	
				if(rs.getDate("FECHA_FIN_OB")==null){
					detalle.setFechaFinOb(null);
				//}else{detalle.setFechaFinOb(dur.cambioDeFormato(rs.getString("FECHA_FIN_OB")));}
				}else{detalle.setFechaFinOb(rs.getDate("FECHA_FIN_OB"));}
				if(rs.getDate("FECHA_FIN_ACTO")==null){
					detalle.setFechaModificacion(null);
				//}else{detalle.setFechaFinOb(dur.cambioDeFormato(rs.getString("FECHA_FIN_ACTO")));}
				}else{detalle.setFechaFinOb(rs.getDate("FECHA_FIN_ACTO"));}
				
				if(rs.getString("NO_GARANTIA_PREVIA_OT")==null){
					detalle.setNoGarantiaPreviaOt("");
				}else{detalle.setNoGarantiaPreviaOt(rs.getString("NO_GARANTIA_PREVIA_OT"));}	
				
				//nuevos campos
				if(rs.getString("ES_PRIORITARIA")==null){
					detalle.setEsPrioritaria("");
				}else{detalle.setEsPrioritaria(rs.getString("ES_PRIORITARIA"));}
				if(rs.getString("OTROS_REGISTROS")==null){
					detalle.setEnRegistro("");
				}else{detalle.setEnRegistro(rs.getString("OTROS_REGISTROS"));}
				if(rs.getString("TXT_REGISTROS")==null){
					detalle.setTxtRegistro("");
				}else{detalle.setTxtRegistro(rs.getString("TXT_REGISTROS"));}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return detalle;
	}
	
	// TODO: Revisar si este metodo aplicaria tanto para modificacion, rectificacion,
	// 		 Transmision.
	public DetalleTO getDetalleModificacion(Integer idgarantia, Integer idtramite) {
		DetalleTO detalle = new DetalleTO();
		
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs = null;
		PreparedStatement ps =null;
		try{
			String sqlQuery = "";
			sqlQuery = "SELECT ID_GARANTIA,ID_TIPO_GARANTIA,MONTO_LIMITE,DESC_BIENES_MUEBLES,FECHA_CELEB_ACTO,FECHA_CELEB_CONTRATO,OTROS_TERMINOS_CONTRATO,OTROS_TERMINOS_GARANTIA,TIPO_GARANTIA,VIGENCIA,RELACION_BIEN,TIPO_CONTRATO,ID_CONTRATO,CAMBIOS_BIENES_MONTO," +
					  "INSTRUMENTO_PUBLICO,ID_MONEDA,DESC_MONEDA,FECHA_FIN_OB,FECHA_FIN_ACTO,NO_GARANTIA_PREVIA_OT, ES_PRIORITARIA,OTROS_REGISTROS,TXT_REGISTROS, OBSERVACIONES "
					  + "FROM V_DETALLE_GARANTIA WHERE ID_GARANTIA = ? AND ID_TRAMITE = ? AND CLASIF_CONTRATO = 'OB' ";
			
			ps = connection.prepareStatement(sqlQuery);
			ps.setInt(1, idgarantia);	
			ps.setInt(2, idtramite);
			System.out.println("sql:  " + sqlQuery);
			//System.out.println("params update: " + ps);
                        System.out.println("params update:  garantia->" + idgarantia + " tramite->" + idtramite);
			//DateUtilRug dur = new DateUtilRug();
			rs = ps.executeQuery();
                        
			if(rs.next()){
                                
				detalle.setIdgarantia(idgarantia);
				detalle.setIdtipogarantia(new Integer(rs.getInt("ID_TIPO_GARANTIA")));
				detalle.setTipogarantia(rs.getString("TIPO_GARANTIA"));				
				if(rs.getDate("FECHA_CELEB_ACTO")==null) {
					detalle.setFechaCelebracionObligacion(null);
				//}else{detalle.setFechaCelebracionObligacion(dur.cambioDeFormato(rs.getString("FECHA_CELEB_ACTO")));}
				} else {
					detalle.setFechaCelebracionObligacion(rs.getDate("FECHA_CELEB_ACTO"));
				}
				detalle.setMonto(rs.getString("MONTO_LIMITE"));
				if(rs.getString("OTROS_TERMINOS_GARANTIA")==null){
					detalle.setOtrosgarantia("");
				} else { 
					detalle.setOtrosgarantia(rs.getString("OTROS_TERMINOS_GARANTIA"));
				}
				if(rs.getString("DESC_BIENES_MUEBLES")==null){
					detalle.setDescbienes("");
				} else { 
					detalle.setDescbienes(rs.getString("DESC_BIENES_MUEBLES"));
				}
				detalle.setTipocontrato(rs.getString("TIPO_CONTRATO"));
				if(rs.getDate("FECHA_CELEB_CONTRATO")==null){
					detalle.setFecacelebcontrato(null);	
				//}else{detalle.setFecacelebcontrato(dur.cambioDeFormato(rs.getString("FECHA_CELEB_CONTRATO")));}
				} else { 
					detalle.setFecacelebcontrato(rs.getDate("FECHA_CELEB_CONTRATO"));
				}
				if(rs.getString("OTROS_TERMINOS_CONTRATO")==null){
					detalle.setOtroscontrato("");
				} else { 
					detalle.setOtroscontrato(rs.getString("OTROS_TERMINOS_CONTRATO"));
				}
				if(rs.getString("OBSERVACIONES")==null){
					detalle.setOtrosterminos("");
				} else { 
					detalle.setOtrosterminos(rs.getString("OBSERVACIONES"));
				}
				System.out.println("-------------- Vigencia"+rs.getInt("VIGENCIA"));
				detalle.setVigencia(rs.getInt("VIGENCIA"));
				//System.out.println("rs.getInt('RELACION_BIEN') " + rs.getInt("RELACION_BIEN"));
				if(rs.getInt("RELACION_BIEN")==0 ){
					detalle.setRelacionbien(0);
				}else{detalle.setRelacionbien(new Integer(rs.getInt("RELACION_BIEN")));}			
				if(rs.getString("INSTRUMENTO_PUBLICO")==null){
					detalle.setInstrumento("");
				}else{detalle.setInstrumento(rs.getString("INSTRUMENTO_PUBLICO"));}
				if(rs.getString("CAMBIOS_BIENES_MONTO")==null){
					detalle.setCambio("");
				}else{detalle.setCambio(rs.getString("CAMBIOS_BIENES_MONTO"));}				
				detalle.setIdcontrato(new Integer(rs.getInt("ID_CONTRATO")));
				if (rs.getString("DESC_MONEDA")== null){
					detalle.setDesmoneda("");
				}else{detalle.setDesmoneda(rs.getString("DESC_MONEDA"));}
				detalle.setIdmoneda(rs.getInt("ID_MONEDA"));	
				if(rs.getDate("FECHA_FIN_OB")==null){
					detalle.setFechaFinOb(null);
				//}else{detalle.setFechaFinOb(dur.cambioDeFormato(rs.getString("FECHA_FIN_OB")));}
				}else{detalle.setFechaFinOb(rs.getDate("FECHA_FIN_OB"));}
				if(rs.getDate("FECHA_FIN_ACTO")==null){
					detalle.setFechaModificacion(null);
				}else{detalle.setFechaFinOb(rs.getDate("FECHA_FIN_ACTO"));}
				//nuevos campos
				if(rs.getString("ES_PRIORITARIA")==null){
					detalle.setEsPrioritaria("");
				}else{detalle.setEsPrioritaria(rs.getString("ES_PRIORITARIA"));}
				if(rs.getString("OTROS_REGISTROS")==null){
					detalle.setEnRegistro("");
				}else{detalle.setEnRegistro(rs.getString("OTROS_REGISTROS"));}
				if(rs.getString("TXT_REGISTROS")==null){
					detalle.setTxtRegistro("");
				}else{detalle.setTxtRegistro(rs.getString("TXT_REGISTROS"));}
				if(rs.getString("NO_GARANTIA_PREVIA_OT")==null){
					detalle.setNoGarantiaPreviaOt("");
				}else{detalle.setNoGarantiaPreviaOt(rs.getString("NO_GARANTIA_PREVIA_OT"));}
			}
                           
                        
			
		} catch (SQLException e) {
			e.printStackTrace();
                        //System.out.println("detalle:  = " + detalle );
		}finally{
			bd.close(connection,rs,ps);
		}
                
		return detalle;
	}

	public DetalleTO getTramiteCancelacion(Integer idtramite) {
		DetalleTO detalle = new DetalleTO();
		
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs = null;
		PreparedStatement ps =null;
		try{
			String sqlQuery = "";
			
			sqlQuery = " SELECT A.ID_INSCRIPCION FROM RUG_CANCELACION_TRANSMISION A, V_TRAMITES_TERMINADOS B " +
					" WHERE A.ID_CANCELACION = B.ID_TRAMITE_TEMP " +
					" AND B.ID_TRAMITE = ? " ;
			System.out.println("sqlQuery " + sqlQuery);
			
			ps = connection.prepareStatement(sqlQuery);
			ps.setInt(1, idtramite);
			
			System.out.println("params: " + ps);
			rs = ps.executeQuery();
			if(rs.next()){
				if(rs.getInt("ID_INSCRIPCION") == 0){
					detalle.setIdTramite(0);
				}else{
					detalle.setIdTramite(rs.getInt("ID_INSCRIPCION"));
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,null);
		}
		return detalle;
	}
	
	public List<DetalleTO> getTramitesFinalizados(Integer idInscripcion) {
		List<DetalleTO> lista = new ArrayList<DetalleTO>();
		DetalleTO tramites = new DetalleTO();
		
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs = null;
		PreparedStatement ps =null;
		try{
			String sqlQuery = "";
			
			sqlQuery = " SELECT ID_GARANTIA, DESCRIPCION, to_char(FECHA_CREACION,'dd/MM/yyyy hh24:mi:ss') AS FECHA_CREACION FROM V_TRAMITES_QR " +
					" WHERE ID_TRAMITE = ? " +
					" UNION " + 
					" SELECT TER.ID_GARANTIA, TER.DESCRIPCION, to_char(TER.FECHA_CREACION,'dd/MM/yyyy hh24:mi:ss') AS FECHA_CREACION " + 
					" FROM RUG_FIRMA_MASIVA MAS, V_TRAMITES_QR TER " +
					" WHERE MAS.ID_FIRMA_MASIVA = ? " + 
					" AND MAS.ID_TRAMITE_TEMP = TER.ID_TRAMITE_TEMP " ;			
			
			ps = connection.prepareStatement(sqlQuery);
			ps.setInt(1, idInscripcion);			
			ps.setInt(2, idInscripcion);			
			rs = ps.executeQuery();
			while(rs.next()){		
				tramites = new DetalleTO();				
				tramites.setDescripcionGarantia(rs.getString("FECHA_CREACION"));				
				tramites.setDescripcionTramite(rs.getString("DESCRIPCION"));				
				tramites.setIdgarantia(new Integer(rs.getInt("ID_GARANTIA")));				
				lista.add(tramites);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return lista;
	}

	public DetalleTO getTramiteActualizado(Integer idInscripcion) {
		DetalleTO detalle = new DetalleTO();
		
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs = null;
		PreparedStatement ps =null;
		try{
			String sqlQuery = "";
			
			sqlQuery = " SELECT ID_TRAMITE, ID_GARANTIA FROM V_TRAMITES_TERMINADOS " +
					" WHERE ID_TRAMITE_TEMP = ? " ;
			System.out.println("sqlQuery  NEW xD" + sqlQuery);
			
			ps = connection.prepareStatement(sqlQuery);
			ps.setInt(1, idInscripcion);
			System.out.println("params: " + ps);
			rs = ps.executeQuery();
			if(rs.next()){
				if(rs.getInt("ID_TRAMITE") == 0){
					detalle.setIdTramite(0);
				}else{
					detalle.setIdTramite(rs.getInt("ID_TRAMITE"));
				}
				if(rs.getInt("ID_GARANTIA") == 0){
					detalle.setIdgarantia(0);
				}else{
					detalle.setIdgarantia(rs.getInt("ID_GARANTIA"));
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return detalle;
	}	
	
	public List<PartesTO> getOtorgante(Integer idgarantia, Integer idtramite) {
		PartesTO partes = new PartesTO();
		List <PartesTO> lista  = new ArrayList<PartesTO>();
		
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs = null;
		PreparedStatement ps =null;
		try{
			String sqlQuery = "";
			sqlQuery = "SELECT ID_GARANTIA,ID_PERSONA,DESC_PARTE,FOLIO_MERCANTIL,NOMBRE,PER_JURIDICA,RFC,CURP, UBICA_DOMICILIO_1,ID_NACIONALIDAD,NUM_INSCRITA FROM V_GARANTIA_PARTES  WHERE ID_PARTE=1 AND ID_GARANTIA = ? AND ID_TRAMITE = ?";
			ps = connection.prepareStatement(sqlQuery);
			ps.setInt(1, idgarantia);
			ps.setInt(2, idtramite);	
			System.out.println("sql p " + idgarantia + " tramite " + idtramite);
			rs = ps.executeQuery();
			while(rs.next()){
				partes= new PartesTO();
				partes.setIdgarantia(idgarantia);
				partes.setIdpersona(rs.getInt("ID_PERSONA"));
				partes.setDescribe(rs.getString("DESC_PARTE"));
				partes.setNombre(rs.getString("NOMBRE"));
				partes.setFoliomercantil(rs.getString("FOLIO_MERCANTIL"));
				partes.setPerjuridica(rs.getString("PER_JURIDICA"));				
				partes.setRfc(rs.getString("RFC"));
				partes.setCurp(rs.getString("CURP"));
				partes.setDomicilio(rs.getString("UBICA_DOMICILIO_1"));
				partes.setIdNacionalidad(rs.getInt("ID_NACIONALIDAD"));
				partes.setTxtInscrita(rs.getString("NUM_INSCRITA"));
				lista.add(partes);
			}
			return lista;
		} catch (Exception e) {
			throw new JdbcDaoException(e);
		}finally{
			bd.close(connection,rs,ps);
		}
	}
	
	public List <PartesTO> getDeudor(Integer idgarantia, Integer idtramite) {
		PartesTO partes = new PartesTO();
		List <PartesTO> lista  = new ArrayList<PartesTO>();
		
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		PreparedStatement ps = null;
		try{
			String sqlQuery = "";
			sqlQuery = "SELECT ID_GARANTIA,ID_PERSONA,DESC_PARTE,FOLIO_MERCANTIL,NOMBRE,PER_JURIDICA,RFC,CURP, UBICA_DOMICILIO_1,ID_NACIONALIDAD,NUM_INSCRITA FROM V_GARANTIA_PARTES  WHERE ID_PARTE=2 AND ID_GARANTIA = ? AND ID_TRAMITE = ?";
			ps = connection.prepareStatement(sqlQuery);
			ps.setInt(1, idgarantia);				
			ps.setInt(2, idtramite);
			rs = ps.executeQuery();
			while(rs.next()){
				partes= new PartesTO();
				partes.setIdgarantia(idgarantia);
				partes.setIdpersona(rs.getInt("ID_PERSONA"));
				partes.setDescribe(rs.getString("DESC_PARTE"));
				partes.setNombre(rs.getString("NOMBRE"));
				partes.setFoliomercantil(rs.getString("FOLIO_MERCANTIL"));
				partes.setPerjuridica(rs.getString("PER_JURIDICA"));				
				partes.setRfc(rs.getString("RFC"));
				
				partes.setCurp(rs.getString("CURP"));
				partes.setDomicilio(rs.getString("UBICA_DOMICILIO_1"));
				partes.setIdNacionalidad(rs.getInt("ID_NACIONALIDAD"));
				partes.setTxtInscrita(rs.getString("NUM_INSCRITA"));
				
				lista.add(partes);
			}
			return lista;
		} catch (Exception e) {
			throw new JdbcDaoException(e);
		}finally{
			bd.close(connection,rs,ps);
		}
	}
	
	public List<BienEspecialTO> getListaBienes(Integer idTramite, Integer pQuery){
		List<BienEspecialTO> listaBienes = new ArrayList<BienEspecialTO>();
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "";
                System.out.println("Tramite Query = " + idTramite + " " + pQuery);
		// cambio para factoraje
		if(pQuery == 1) {
			sql = "SELECT ID_GARAN_BIEN_PEND,ID_TRAMITE_TEMP,TIPO_BIEN_ESPECIAL, TIPO_IDENTIFICADOR,IDENTIFICADOR,DESCRIPCION_BIEN,SERIE " +
				  "FROM RUG_GARANTIAS_BIENES_PEND " + 
				  "WHERE ID_TRAMITE_TEMP = ?";
		} else if(pQuery == 2) {
			sql = "SELECT ID_GARAN_BIEN,ID_TRAMITE,TIPO_BIEN_ESPECIAL, TIPO_IDENTIFICADOR,IDENTIFICADOR,DESCRIPCION_BIEN,SERIE " +
					  "FROM RUG_GARANTIAS_BIENES " + 
					  "WHERE ID_TRAMITE = ?";
		} else {
			sql = "SELECT ID_GARAN_BIEN,ID_TRAMITE,TIPO_BIEN_ESPECIAL, TIPO_IDENTIFICADOR,IDENTIFICADOR,DESCRIPCION_BIEN,SERIE " +
					  "FROM RUG_GARANTIAS_BIENES_H " + 
					  "WHERE ID_TRAMITE = ?";
		}
				
		ResultSet rs = null;
		PreparedStatement ps =null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idTramite);
			rs = ps.executeQuery();
			BienEspecialTO bienEspecialTO ;
			while (rs.next()){
				bienEspecialTO = new BienEspecialTO();
				if(pQuery == 1) {
					bienEspecialTO.setIdTramiteGarantia(rs.getInt("ID_GARAN_BIEN_PEND"));
				} else {
					bienEspecialTO.setIdTramiteGarantia(rs.getInt("ID_GARAN_BIEN"));
				}
				bienEspecialTO.setTipoBien(rs.getInt("TIPO_BIEN_ESPECIAL"));
				bienEspecialTO.setTipoIdentificador(rs.getInt("TIPO_IDENTIFICADOR"));
				bienEspecialTO.setIdentificador(rs.getString("IDENTIFICADOR"));
				bienEspecialTO.setDescripcion(rs.getString("DESCRIPCION_BIEN"));
				bienEspecialTO.setSerie(rs.getString("SERIE"));
				
				listaBienes.add(bienEspecialTO);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return listaBienes;
	}
	
	public List <PartesTO> getAcreedor(Integer idgarantia, Integer idtramite) {
		PartesTO partes = new PartesTO();
		List <PartesTO> lista  = new ArrayList<PartesTO>();
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try{
			String sqlQuery = "";
			sqlQuery = "SELECT ID_GARANTIA,ID_PERSONA,DESC_PARTE,FOLIO_MERCANTIL,NOMBRE,PER_JURIDICA,RFC,E_MAIL ,CLAVE_PAIS,TELEFONO,EXTENSION, CURP FROM V_GARANTIA_PARTES  WHERE ID_PARTE =3 AND ID_GARANTIA = ? AND ID_TRAMITE = ? ";
					
			 ps = connection.prepareStatement(sqlQuery);
			ps.setInt(1, idgarantia);
			ps.setInt(2, idtramite);
			System.out.println("sql p2" + ps);
			rs = ps.executeQuery();
			while(rs.next()){
				partes= new PartesTO();
				partes.setIdgarantia(idgarantia);
				partes.setIdpersona(new Integer(rs.getInt("ID_PERSONA")));
				partes.setDescribe(rs.getString("DESC_PARTE"));
				partes.setNombre(rs.getString("NOMBRE"));
				partes.setFoliomercantil(rs.getString("FOLIO_MERCANTIL"));
				/*if(rs.getString("PER_JURIDICA").equalsIgnoreCase("PM")){
					partes.setPerjuridica("Persona Moral");
				}else{partes.setPerjuridica("Persona Fisica");}*/
				partes.setPerjuridica(rs.getString("PER_JURIDICA"));
				partes.setRfc(rs.getString("RFC"));
				partes.setCurp(rs.getString("CURP"));
				if(rs.getString("E_MAIL")==null){
					partes.setEmail("");
				}else{partes.setEmail(rs.getString("E_MAIL"));}
				if(rs.getString("EXTENSION")==null){
					partes.setExt("");
				}else{partes.setExt(rs.getString("EXTENSION"));}
				if(rs.getString("TELEFONO")==null){
					partes.setTelefono("");
				}else{partes.setTelefono(rs.getString("TELEFONO"));}
				lista.add(partes);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return lista;
	}
	
	//TERMINA CONSULTA MAESTRO-DELLATE
		
	//Consulta de Tr�mites
	public List <DetalleTO> getTramites(Integer idgarantia,Integer idtramite) {
		DetalleTO tramites = new DetalleTO();
		List <DetalleTO> lista  = new ArrayList<DetalleTO>();
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try{
			String sqlQuery = "";
			sqlQuery = "SELECT ID_GARANTIA, DESC_GARANTIA, ID_TIPO_TRAMITE , DESCRIPCION, FECHA_CREACION, ID_TRAMITE, NOMBRE_PERSONA FROM V_OPERACIONES_GARANTIA WHERE ID_GARANTIA =?";
			ps = connection.prepareStatement(sqlQuery);
			ps.setInt(1, idgarantia);
			//ps.setInt(2, idtramite);
			rs = ps.executeQuery();
			while(rs.next()){
				tramites = new DetalleTO();
				tramites.setIdgarantia(idgarantia);
				tramites.setDescripcionGarantia(rs.getString("DESC_GARANTIA"));
				tramites.setIdTipoTramite(new Integer(rs.getInt("ID_TIPO_TRAMITE")));
				tramites.setDescripcionTramite(rs.getString("DESCRIPCION"));
				tramites.setFechaModificacion(rs.getDate("FECHA_CREACION"));
				tramites.setIdTramite(new Integer(rs.getInt("ID_TRAMITE")));
				tramites.setNombrePersona(rs.getString("NOMBRE_PERSONA"));
				lista.add(tramites);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();		
		}finally{
			bd.close(connection,rs,ps);
		}
		return lista;
	}
	
	public List <PartesTO> getAcreedorAdd(Integer idgarantia) {
		PartesTO partes = new PartesTO();
		List <PartesTO> lista  = new ArrayList<PartesTO>();
		
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try{
			String sqlQuery = "";
			sqlQuery = "SELECT ID_GARANTIA,ID_PERSONA,DESC_PARTE,FOLIO_MERCANTIL,NOMBRE,PER_JURIDICA,RFC FROM V_GARANTIA_PARTES  WHERE DESC_PARTE ='ACREEDOR REPRESENTADO' AND ID_GARANTIA = ?";
			ps = connection.prepareStatement(sqlQuery);
			ps.setInt(1, idgarantia);
			System.out.println("sql p3" + ps);
			rs = ps.executeQuery();
			while(rs.next()){
				partes= new PartesTO();
				partes.setIdgarantia(idgarantia);
				partes.setIdpersona(new Integer(rs.getInt("ID_PERSONA")));
				partes.setDescribe(rs.getString("DESC_PARTE"));
				partes.setNombre(rs.getString("NOMBRE"));
				partes.setFoliomercantil(rs.getString("FOLIO_MERCANTIL"));
				if(rs.getString("PER_JURIDICA").equalsIgnoreCase("PM")){
					partes.setPerjuridica("Persona Moral");
				}else{partes.setPerjuridica("Persona Fisica");}
				partes.setRfc(rs.getString("RFC"));
				lista.add(partes);
			}
			return lista;
		} catch (Exception e) {
			throw new JdbcDaoException(e);
		}finally{
			bd.close(connection,rs,ps);
		}
	}
	
	public List<TipoBienTO> getBienes(Integer idgarantia, Integer idtramite) {
		TipoBienTO bienes = new TipoBienTO();
		List <TipoBienTO> lista  = new ArrayList<TipoBienTO>();
		
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		System.out.println("idgarantia "+ idgarantia);
		System.out.println("idtramite "+ idtramite);
		ResultSet rs = null;
		PreparedStatement ps = null;
		try{
			String sqlQuery = "";
			sqlQuery = "SELECT ID_TIPO_BIEN,DESC_TIPO_BIEN FROM V_GARANTIAS_BIENES WHERE ID_GARANTIA = ? AND RELACION_BIEN=?";
			 ps = connection.prepareStatement(sqlQuery);
			ps.setInt(1, idgarantia);
			ps.setInt(2, idtramite);
			System.out.println("sql p4" + ps);
			rs = ps.executeQuery();
			while(rs.next()){
						bienes = new TipoBienTO();
						bienes.setIdTipoBien(new Integer(rs.getInt("ID_TIPO_BIEN")));
						bienes.setDescripcion(rs.getString("DESC_TIPO_BIEN"));
						
						lista.add(bienes);
						System.out.println("IMPRIME bienes"+lista+">>>>>>"+bienes);
					}
//			}		
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return lista;
	}
	
	public DetalleTO getDatosBasa(Integer idgarantia, Integer idtramite) {
		DetalleTO basa = new DetalleTO();
				
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		System.out.println("idgarantia "+ idgarantia);
		System.out.println("idtramite "+ idtramite);
		ResultSet rs = null;
		PreparedStatement ps = null;
		try{
			String sqlQuery = "";
			sqlQuery = "SELECT d.CLASIF_CONTRATO,d.ID_TRAMITE, d.ID_GARANTIA, t.DESCRIPCION, d.TIPO_CONTRATO,d.FECHA_CELEB_CONTRATO,"
					 	+ " d.FECHA_FIN_OB, d.OTROS_TERMINOS_CONTRATO FROM V_TRAMITES_TERMINADOS t, V_DETALLE_GARANTIA d "
					 	+ " WHERE t.ID_GARANTIA = d.ID_GARANTIA	AND d.ID_GARANTIA = ? "
					 	+ " AND D.ID_TIPO_GARANTIA = t.ID_TIPO_TRAMITE AND D.CLASIF_CONTRATO = 'FU'";
			
			ps = connection.prepareStatement(sqlQuery);
			ps.setInt(1, idgarantia);
//			ps.setInt(2, idtramite);
			System.out.println("sql p5" + ps);
			rs = ps.executeQuery();
			while(rs.next()){
					basa = new DetalleTO();
					if(rs.getString("CLASIF_CONTRATO").equalsIgnoreCase("FU")){
						basa.setTipoContratobasa(rs.getString("TIPO_CONTRATO"));
						basa.setFechainibasa(rs.getDate("FECHA_CELEB_CONTRATO"));
						basa.setFechafinbasa(rs.getDate("FECHA_FIN_OB"));	
						basa.setTerminosbasa(rs.getString("OTROS_TERMINOS_CONTRATO"));
						System.out.println("IMPRIME basa >>>>>>"+basa);
					}
			}		
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return basa;
	}
	
	public Integer getIdUltimoTramite(Integer idGarantia){
		Integer idUltimoTramite = 0;
		
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs =null;
		PreparedStatement ps = null;
		String sql = " SELECT ID_ULTIMO_TRAMITE ";
			sql += " FROM RUG_GARANTIAS ";
			sql += " WHERE ID_GARANTIA = ? ";
			
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idGarantia);
			rs = ps.executeQuery();
			
			if(rs.next()){
				idUltimoTramite= new Integer(rs.getInt("ID_ULTIMO_TRAMITE"));
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return idUltimoTramite;

	}
}
