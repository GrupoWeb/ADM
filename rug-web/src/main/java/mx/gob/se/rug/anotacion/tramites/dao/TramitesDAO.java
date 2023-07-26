package mx.gob.se.rug.anotacion.tramites.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import mx.gob.se.exception.InfrastructureException;
import mx.gob.se.rug.anotacion.tramites.to.AnotacionSnGarantiaTO;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.exception.NoDataFoundException;
import mx.gob.se.rug.exception.RugException;

public class TramitesDAO {
	
	
	 public Integer getTramitePadreByIdTramite(Integer idTramite) throws NoDataFoundException, InfrastructureException{
		 Integer idTramitePadre= new Integer(0);
			String sql="Select nvl(id_anotacion_padre,id_tramite) ID_TRAMITE_PADRE from V_ANOTACION_TRAMITES where id_tramite= ? ";
			ConexionBD bd = new ConexionBD();
			Connection connection = bd.getConnection();
			PreparedStatement ps = null;	
			ResultSet rs =null;
			try{
				ps = connection.prepareCall(sql);
				ps.setInt(1, idTramite);
				rs =ps.executeQuery();
				if(rs.next()){
					idTramitePadre = rs.getInt("ID_TRAMITE_PADRE");
					
				}else{
							throw new NoDataFoundException("No se encontro getTramitePadreByIdTramite anotacionSG  con el idtramite::"+idTramite);
				}
			}catch(SQLException e){
				e.printStackTrace();
				throw new InfrastructureException(e.getCause());
			}finally{
				bd.close(connection,rs,ps);
			}
			return idTramitePadre;
		}
	 
	 public Boolean haveCancelacion(Integer idTramitePadre) throws InfrastructureException{
		 Boolean tieneCancelacion = false;
		 String sql="SELECT count(V.ID_TRAMITE) tiene_cancelacion FROM RUG.V_ANOTACION_TRAMITES V where V.id_anotacion_padre= ? and V.ID_STATUS_TRAM=3 and V.ID_TIPO_TRAMITE=27 ";
		 ConexionBD bd = new ConexionBD();
		 Connection connection = bd.getConnection();
		 PreparedStatement ps = null;	
		 ResultSet rs =null;
		 try{
			 ps = connection.prepareCall(sql);
			 ps.setInt(1, idTramitePadre);
			 rs =ps.executeQuery();
			 if(rs.next()){
				if(rs.getInt("tiene_cancelacion")!=0){
					tieneCancelacion = true;
				}
				 
			 }
		 }catch(SQLException e){
			 e.printStackTrace();
			 throw new InfrastructureException(e.getCause());
		 }finally{
			 bd.close(connection,rs,ps);
		 }
		 return tieneCancelacion;
	 }
	 
	 public ArrayList<AnotacionSnGarantiaTO> getTramitesHByIdTramite(Integer idTramitePadre) throws NoDataFoundException, InfrastructureException{
		 AnotacionSnGarantiaTO anotacionSnGarantiaTO;
		 ArrayList<AnotacionSnGarantiaTO> anotacionSnGarantiaTOs = new ArrayList<AnotacionSnGarantiaTO>();
		 String sql="SELECT V.ID_TRAMITE, V.ID_TRAMITE_TEMP, V.ID_ANOTACION_PADRE, V.ID_ANOTACION, V.ID_GARANTIA, V.ID_TIPO_TRAMITE," +
		 		" V.DESCRIPCION, V.ID_STATUS_TRAM, V.DESCRIP_STATUS,V.ID_USUARIO, V.USUARIO, V.ID_PERSONA, V.PER_JURIDICA, V.FOLIO_MERCANTIL, V.RFC," +
		 		" V.CURP, V.ID_NACIONALIDAD, V.NOMBRE_PERSONA, V.AP_PATERNO, V.AP_MATERNO, V.RAZON_SOCIAL, V.AUTORIDAD_AUTORIZA, V.ANOTACION, V.RESOLUCION," +
		 		" V.VIGENCIA_ANOTACION, V.SOLICITANTE_RECTIFICA, V.FECHA_STATUS, V.FECHA_INSCRIPCION " +
		 		" FROM RUG.V_ANOTACION_TRAMITES V WHERE " +
		 		" (V.ID_TRAMITE= ? and V.ID_STATUS_TRAM=3)  OR " +
		 		" (V.id_anotacion_padre= ? and V.ID_STATUS_TRAM=3) order by V.ID_TRAMITE ";
		 ConexionBD bd = new ConexionBD();
		 Connection connection = bd.getConnection();
		 PreparedStatement ps = null;	
		 ResultSet rs =null;
		 try{
			 ps = connection.prepareCall(sql);
			 ps.setInt(1, idTramitePadre);
			 ps.setInt(2, idTramitePadre);
			 rs =ps.executeQuery();
			 while(rs.next()){
				 anotacionSnGarantiaTO = new AnotacionSnGarantiaTO();
				 anotacionSnGarantiaTO.setIdTramiteFinal(rs.getInt("ID_TRAMITE"));
				 anotacionSnGarantiaTO.setIdTramitePadre(idTramitePadre);
				 
				 anotacionSnGarantiaTO.setIdTipoTramite(rs.getInt("ID_TIPO_TRAMITE"));
				 anotacionSnGarantiaTO.setAnotacion(rs.getString("ANOTACION"));
				 anotacionSnGarantiaTO.setAutoridadAutoriza(rs.getString("AUTORIDAD_AUTORIZA"));
				 anotacionSnGarantiaTO.setResolucion(rs.getString("RESOLUCION"));
				 anotacionSnGarantiaTO.setVigenciaStr(rs.getString("VIGENCIA_ANOTACION"));
				 
				 anotacionSnGarantiaTO.setFechaFirma(rs.getString("FECHA_STATUS"));
				 anotacionSnGarantiaTO.setFechaAnotacion(rs.getString("FECHA_INSCRIPCION"));
				 anotacionSnGarantiaTO.setTipoTramiteStr(rs.getString("DESCRIPCION"));

				 anotacionSnGarantiaTO.setOtorganteFolioElectronico(rs.getString("FOLIO_MERCANTIL"));
				 anotacionSnGarantiaTO.setOtorganteAMaterno(rs.getString("AP_MATERNO"));
				 anotacionSnGarantiaTO.setOtorganteAPaterno(rs.getString("AP_PATERNO"));
				 anotacionSnGarantiaTO.setOtorganteNombre(rs.getString("NOMBRE_PERSONA"));
				 anotacionSnGarantiaTO.setOtorganteDenominacion(rs.getString("RAZON_SOCIAL"));
				 
				 
				 anotacionSnGarantiaTOs.add(anotacionSnGarantiaTO);
			 }
			 
			 if(anotacionSnGarantiaTOs.size()==0){
				 throw new NoDataFoundException("No se encontro anotacion sin garantia  con el idTramitePadre::"+idTramitePadre);
			 }
		 }catch(SQLException e){
			 e.printStackTrace();
			 throw new InfrastructureException(e.getCause());
		 }finally{
			 bd.close(connection,rs,ps);
		 }
		 return anotacionSnGarantiaTOs;
	 }
	 public String getListTramitesHByIdTramitePadre(Integer idTramitePadre) throws NoDataFoundException, InfrastructureException{
		 StringBuffer tramites = new StringBuffer();
		 String sql="SELECT V.ID_TRAMITE "+
				 " FROM RUG.V_ANOTACION_TRAMITES V WHERE " +
				 " (V.ID_TRAMITE= ? and V.ID_STATUS_TRAM=3)  OR " +
				 " (V.id_anotacion_padre= ? and V.ID_STATUS_TRAM=3) order by V.ID_TRAMITE ";
		 ConexionBD bd = new ConexionBD();
		 Connection connection = bd.getConnection();
		 PreparedStatement ps = null;	
		 ResultSet rs =null;
		 try{
			 ps = connection.prepareCall(sql);
			 ps.setInt(1, idTramitePadre);
			 ps.setInt(2, idTramitePadre);
			 rs =ps.executeQuery();
			 while(rs.next()){
				 
				 tramites.append(rs.getInt("ID_TRAMITE"));
				 tramites.append(",");
			 }
			 
			 
			 
			 if(tramites.length()==0){
				 throw new NoDataFoundException("No se encontro anotacion sin garantia  con el idTramitePadre::"+idTramitePadre);
			 }
		 }catch(SQLException e){
			 e.printStackTrace();
			 throw new InfrastructureException(e.getCause());
		 }finally{
			 bd.close(connection,rs,ps);
		 }
		 return tramites.substring(0, tramites.length()-1);
	 }

	public Integer initTramite(Integer idUsuario,Integer idTipoTramite,Integer idTramitePadre) throws RugException {
		Integer idTramiteNuevo = null;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		/*
		 * PROCEDURE     SP_Alta_Tramite_Anotaci_Incom(
                               peIdPersona            IN     NUMBER, -- IDENTIFICADOR DE LA PESONA K SeLOGUEA
                               peIdTipoTramite        IN     NUMBER, --INSCRIPCION , ALTA ACREEDORES, AVISO PREVENTIVO
                               P_ID_TRAMITE_PADRE     IN     NUMBER, -- ANOTACION PADRE DEL TRAMITE        
                               peIdTramiteIncompleto  OUT    NUMBER, --IDENTIFICADOR UNICO DEL REGISTRO
                               psResult               OUT    INTEGER,   
                               psTxResult             OUT    VARCHAR2)
		 * */
		String sql = "{call SP_Alta_Tramite_Anotaci_Incom ( ?,?,?,?,?,?) } ";
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, idUsuario);
			cs.setInt(2, idTipoTramite);
			cs.setInt(3, idTramitePadre);
			cs.registerOutParameter(4, Types.INTEGER);
			cs.registerOutParameter(5, Types.INTEGER);
			cs.registerOutParameter(6, Types.VARCHAR);
			
			cs.executeQuery();
			if(cs.getInt(5)==0){
				idTramiteNuevo = cs.getInt(4);
			}else{
				throw new RugException(cs.getInt(5), cs.getString(6));
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RugException(e);
		} finally {
			bd.close(connection,null,cs);
		}
		return idTramiteNuevo;

	}
	
	 public AnotacionSnGarantiaTO getAnotacionSnGarantia_H(Integer idTramite) throws NoDataFoundException{
		 AnotacionSnGarantiaTO anotacionSnGarantia = null;
			String sql="select ID_TRAMITE, VIGENCIA_ANOTACION,  ID_USUARIO,NOMBRE_USUARIO, PERFIL,FOLIO_OTORGANTE,RESOLUCION,  ANOTACION, AUTORIDAD_INSTRUYE, PERFIL,FECHA_CREACION FROM V_BOLETA_ANOTACION  where ID_TRAMITE=?";
			ConexionBD bd = new ConexionBD();
			Connection connection = bd.getConnection();
			PreparedStatement ps = null;	
			ResultSet rs =null;
			try{
				ps = connection.prepareCall(sql);
				ps.setInt(1, idTramite);
				rs =ps.executeQuery();
				if(rs.next()){
					anotacionSnGarantia = new AnotacionSnGarantiaTO();
					anotacionSnGarantia.setIdUsuario(rs.getInt("ID_USUARIO"));
					anotacionSnGarantia.setIdTramitePadre(rs.getInt("ID_TRAMITE"));
					anotacionSnGarantia.setVigenciaStr(rs.getString("VIGENCIA_ANOTACION"));
					anotacionSnGarantia.setVigencia(Integer.parseInt(anotacionSnGarantia.getVigenciaStr().split(" ")[0]));
					anotacionSnGarantia.setAnotacion(rs.getString("ANOTACION"));
					anotacionSnGarantia.setResolucion(rs.getString("RESOLUCION"));
					anotacionSnGarantia.setAutoridadAutoriza(rs.getString("AUTORIDAD_INSTRUYE"));
					anotacionSnGarantia.setFechaFirma(rs.getString("FECHA_CREACION"));
					anotacionSnGarantia.setNombreUsario(rs.getString("NOMBRE_USUARIO"));
					anotacionSnGarantia.setPerfil(rs.getString("PERFIL"));
					anotacionSnGarantia.setFoliomercantil(rs.getString("FOLIO_OTORGANTE"));
					
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
	 public AnotacionSnGarantiaTO getAnotacionSnGarantiaTemp(Integer idTramiteTemporal) throws NoDataFoundException{
		 AnotacionSnGarantiaTO anotacionSnGarantia = null;
		 String sql="SELECT  V.ID_TRAMITE_TEMP, V.ID_ANOTACION_PADRE, V.ID_ANOTACION, V.ID_GARANTIA, V.ID_TIPO_TRAMITE, V.DESCRIPCION, " +
		 		"V.ID_STATUS_TRAM, V.DESCRIP_STATUS, V.ID_USUARIO, V.ID_PERSONA,V.PER_JURIDICA, V.FOLIO_MERCANTIL, V.RFC, V.CURP, " +
		 		"V.ID_NACIONALIDAD, V.NOMBRE_PERSONA, V.AP_PATERNO, V.AP_MATERNO, V.RAZON_SOCIAL,V.AUTORIDAD_AUTORIZA, V.ANOTACION, V.RESOLUCION, " +
		 		"V.VIGENCIA_ANOTACION, V.SOLICITANTE_RECTIFICA, V.FECHA_STATUS, V.FECHA_INSCRIPCION FROM RUG.V_ANOTACION_TRAMITES V "+
		 		" where ID_TRAMITE_TEMP=?";

		 ConexionBD bd = new ConexionBD();
		 Connection connection = bd.getConnection();
		 PreparedStatement ps = null;	
		 ResultSet rs =null;
		 try{
			 ps = connection.prepareCall(sql);
			 ps.setInt(1, idTramiteTemporal);
			 rs =ps.executeQuery();
			 if(rs.next()){
				 anotacionSnGarantia = new AnotacionSnGarantiaTO();
				 anotacionSnGarantia.setIdTramiteNuevo(idTramiteTemporal);
				 anotacionSnGarantia.setIdUsuario(rs.getInt("ID_USUARIO"));
				 anotacionSnGarantia.setIdTramitePadre(rs.getInt("ID_ANOTACION_PADRE"));
				 anotacionSnGarantia.setIdTipoTramite(rs.getInt("ID_TIPO_TRAMITE"));
				 
				 anotacionSnGarantia.setVigenciaStr(rs.getString("VIGENCIA_ANOTACION"));
				 anotacionSnGarantia.setAnotacion(rs.getString("ANOTACION"));
				 anotacionSnGarantia.setAutoridadAutoriza(rs.getString("AUTORIDAD_AUTORIZA"));
				 anotacionSnGarantia.setOrigenTramite(rs.getString("RESOLUCION"));
				 
				 anotacionSnGarantia.setOtorganteId(rs.getInt("ID_PERSONA"));
				 anotacionSnGarantia.setOtorganteFolioElectronico(rs.getString("FOLIO_MERCANTIL"));
				 anotacionSnGarantia.setOtorganteNombre(rs.getString("NOMBRE_PERSONA"));
				 anotacionSnGarantia.setOtorganteAPaterno(rs.getString("AP_PATERNO"));
				 anotacionSnGarantia.setOtorganteAMaterno(rs.getString("AP_MATERNO"));
				 anotacionSnGarantia.setOtorganteDenominacion(rs.getString("RAZON_SOCIAL"));
				 
			 }else{
				 throw new NoDataFoundException("No se encontro anotacion sin garantia  con el idtramite::"+idTramiteTemporal);
			 }
			 
		 }catch(Exception e){
			 
			 e.printStackTrace();
		
		 }finally{
			 bd.close(connection,rs,ps);
		 }
		 return anotacionSnGarantia;
	 }
	 
	 public Integer getLastTramiteByIdTramitePadre(Integer idTramitePadre) throws NoDataFoundException{
			String sql="Select nvl(MAX(ID_TRAMITE),?) ID_TRAMITE From V_ANOTACION_SG_TRAMITES_HIST where ID_TRAMITE is not null and ID_TRAMITE_PADRE=?";
			Integer idTramiteUltimo=0;
			ConexionBD bd = new ConexionBD();
			Connection connection = bd.getConnection();
			PreparedStatement ps = null;	
			ResultSet rs =null;
			try{
				ps = connection.prepareCall(sql);
				ps.setInt(1, idTramitePadre);
				ps.setInt(2, idTramitePadre);
				rs =ps.executeQuery();
				if(rs.next()){
					idTramiteUltimo = rs.getInt("ID_TRAMITE");
				}else{
							throw new NoDataFoundException("No se encontro ultimo tramite::"+idTramitePadre);
				}
			}catch(SQLException e){
				e.printStackTrace();
			}finally{
				bd.close(connection,rs,ps);
			}
			return idTramiteUltimo;
		}
	 public AnotacionSnGarantiaTO getAnotacionSnGarantiaFinal(Integer idTramite) throws NoDataFoundException{
		 AnotacionSnGarantiaTO anotacionSnGarantia = null;
		 String sql="SELECT  V.ID_TRAMITE_TEMP, V.ID_ANOTACION_PADRE, V.ID_ANOTACION, V.ID_GARANTIA, V.ID_TIPO_TRAMITE, V.DESCRIPCION, " +
				 "V.ID_STATUS_TRAM, V.DESCRIP_STATUS, V.ID_USUARIO, V.ID_PERSONA,V.PER_JURIDICA, V.FOLIO_MERCANTIL, V.RFC, V.CURP, " +
				 "V.ID_NACIONALIDAD, V.NOMBRE_PERSONA, V.AP_PATERNO, V.AP_MATERNO, V.RAZON_SOCIAL,V.AUTORIDAD_AUTORIZA, V.ANOTACION, V.RESOLUCION, " +
				 "V.VIGENCIA_ANOTACION, V.SOLICITANTE_RECTIFICA, V.FECHA_STATUS, V.FECHA_INSCRIPCION FROM RUG.V_ANOTACION_TRAMITES V "+
				 " where ID_TRAMITE=?";
		 
		 ConexionBD bd = new ConexionBD();
		 Connection connection = bd.getConnection();
		 PreparedStatement ps = null;	
		 ResultSet rs =null;
		 try{
			 ps = connection.prepareCall(sql);
			 ps.setInt(1, idTramite);
			 rs =ps.executeQuery();
			 if(rs.next()){
				 anotacionSnGarantia = new AnotacionSnGarantiaTO();
				 anotacionSnGarantia.setIdTramiteNuevo(rs.getInt("ID_TRAMITE_TEMP"));
				 anotacionSnGarantia.setIdUsuario(rs.getInt("ID_USUARIO"));
				 anotacionSnGarantia.setIdTramitePadre(rs.getInt("ID_ANOTACION_PADRE"));
				 anotacionSnGarantia.setIdTipoTramite(rs.getInt("ID_TIPO_TRAMITE"));
				 anotacionSnGarantia.setTipoTramiteStr(rs.getString("DESCRIPCION"));
				 
				 anotacionSnGarantia.setVigenciaStr(rs.getString("VIGENCIA_ANOTACION"));
				 anotacionSnGarantia.setAnotacion(rs.getString("ANOTACION"));
				 anotacionSnGarantia.setAutoridadAutoriza(rs.getString("AUTORIDAD_AUTORIZA"));
				 anotacionSnGarantia.setOrigenTramite(rs.getString("RESOLUCION"));
				 
				 anotacionSnGarantia.setFechaAnotacion(rs.getString("FECHA_INSCRIPCION"));
				 anotacionSnGarantia.setFechaFirma(rs.getString("FECHA_STATUS"));
				 
				 anotacionSnGarantia.setOtorganteId(rs.getInt("ID_PERSONA"));
				 anotacionSnGarantia.setOtorganteFolioElectronico(rs.getString("FOLIO_MERCANTIL"));
				 anotacionSnGarantia.setOtorganteNombre(rs.getString("NOMBRE_PERSONA"));
				 anotacionSnGarantia.setOtorganteAPaterno(rs.getString("AP_PATERNO"));
				 anotacionSnGarantia.setOtorganteAMaterno(rs.getString("AP_MATERNO"));
				 anotacionSnGarantia.setOtorganteDenominacion(rs.getString("RAZON_SOCIAL"));
				 
			 }else{
				 throw new NoDataFoundException("No se encontro anotacion sin garantia  con el idtramite::"+idTramite);
			 }
			 
		 }catch(Exception e){
			 
			 e.printStackTrace();
			 
		 }finally{
			 bd.close(connection,rs,ps);
		 }
		 return anotacionSnGarantia;
	 }
	 
	public Integer saveModificacionSnGarantia(AnotacionSnGarantiaTO snGarantiaTO) throws InfrastructureException {
		Integer regresa = new Integer(0);
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call SP_ANOTAC_SG_MODIFICA_UPD ( ?,?,?,?,?,?,?,?) } ";
		
		CallableStatement cs =  null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, snGarantiaTO.getIdTramiteNuevo());
			cs.setInt(2, snGarantiaTO.getIdTramitePadre());
			cs.setInt(3, snGarantiaTO.getIdUsuario());
			cs.setString(4, snGarantiaTO.getAutoridadAutorizaTramite());
			cs.setString(5 , snGarantiaTO.getAnotacion());
			cs.setString(6, snGarantiaTO.getOrigenTramite());
			cs.registerOutParameter(7, Types.INTEGER);
			cs.registerOutParameter(8, Types.VARCHAR);
			
			cs.executeQuery();
			regresa = cs.getInt(7);

		} catch (Exception e) {
			e.printStackTrace();
			throw new InfrastructureException("saveModificacionSnGarantia-"+regresa);
			
		} finally {
			bd.close(connection,null,cs);
		}
		return regresa;

	}
	public Integer saveRectificacionSnGarantia(AnotacionSnGarantiaTO snGarantiaTO) throws InfrastructureException {
		Integer regresa = new Integer(0);
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call SP_ANOTAC_SG_RECTIFICA_UPD ( ?,?,?,?,?,?,?,?) } ";
		
	
		CallableStatement cs =  null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, snGarantiaTO.getIdTramiteNuevo());
			cs.setInt(2, snGarantiaTO.getIdTramitePadre());
			cs.setInt(3, snGarantiaTO.getIdUsuario());
			cs.setString(4 ,snGarantiaTO.getAutoridadAutorizaTramite());
			cs.setString(5,snGarantiaTO.getAutoridadAutoriza());
			cs.setString(6,snGarantiaTO.getAnotacion());
			cs.registerOutParameter(7, Types.INTEGER);
			cs.registerOutParameter(8, Types.VARCHAR);
			
			cs.executeQuery();
			regresa = cs.getInt(7);
			
		
		} catch (Exception e) {
			e.printStackTrace();
			throw new InfrastructureException("saveRectificacionSnGarantia-"+regresa);
			
		} finally {
			bd.close(connection,null,cs);
		}
		return regresa;
		
	}
	public Integer saveCancelacionSnGarantia(AnotacionSnGarantiaTO snGarantiaTO) throws InfrastructureException {
		Integer regresa = new Integer(0);
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call SP_ANOTAC_SG_CANCELA_UPD ( ?,?,?,?,?,?) } ";
		
//	     P_ID_ANOTACION_TEMP         IN RUG_ANOTACIONES_SEG_INC_CSG.ID_ANOTACION_TEMP%TYPE
//	      , P_ID_TRAMITE_PADRE          IN RUG_ANOTACIONES_SEG_INC_CSG.ID_TRAMITE_PADRE %TYPE
//	      , P_ID_USUARIO                IN TRAMITES_RUG_INCOMP.ID_PERSONA%TYPE
//	      , P_AUTORIDAD_AUTORIZA        IN RUG_ANOTACIONES_SEG_INC_CSG.AUTORIDAD_AUTORIZA%TYPE
//	      , psResult                    OUT   NUMBER
//	      , psTxResult                  OUT   VARCHAR2
//	      
		CallableStatement cs =  null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, snGarantiaTO.getIdTramiteNuevo());
			cs.setInt(2, snGarantiaTO.getIdTramitePadre());
			cs.setInt(3, snGarantiaTO.getIdUsuario());
			cs.setString(4, snGarantiaTO.getAutoridadAutorizaTramite());
			cs.registerOutParameter(5, Types.INTEGER);
			cs.registerOutParameter(6, Types.VARCHAR);
			
			cs.executeQuery();
			regresa = cs.getInt(5);
			
		
		} catch (Exception e) {
			e.printStackTrace();
			throw new InfrastructureException("saveCancelacionSnGarantia-"+regresa);
		} finally {
			bd.close(connection,null,cs);
		}
		return regresa;
		
	}
	
	
	public Integer saveRenovacionSnGarantia(AnotacionSnGarantiaTO snGarantiaTO) throws InfrastructureException {
		Integer regresa = new Integer(0);
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call SP_ANOTAC_SG_REDUC_VIGEN_UPD ( ?,?,?,?,?,?,?) } ";
		CallableStatement cs = null;
		try {

			cs = connection.prepareCall(sql);
			cs.setInt(1, snGarantiaTO.getIdTramiteNuevo());
			cs.setInt(2, snGarantiaTO.getIdTramitePadre());
			cs.setInt(3, snGarantiaTO.getIdUsuario());
			cs.setString(4, snGarantiaTO.getAutoridadAutorizaTramite());
			cs.setInt(5, snGarantiaTO.getVigenciaNueva());
			cs.registerOutParameter(6, Types.INTEGER);
			cs.registerOutParameter(7, Types.VARCHAR);
			cs.executeQuery();
			regresa = cs.getInt(6);

		} catch (Exception e) {
			e.printStackTrace();
			throw new InfrastructureException("saveRenovacionSnGarantia-"+regresa);
		} finally {
			bd.close(connection,null,cs);
		}
		return regresa;

	}
}
