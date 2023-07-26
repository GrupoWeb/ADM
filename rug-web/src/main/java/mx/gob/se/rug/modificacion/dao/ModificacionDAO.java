package mx.gob.se.rug.modificacion.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.detallegarantia.to.DetalleTO;
import mx.gob.se.rug.detallegarantia.to.TramiteHTO;
import mx.gob.se.rug.exception.InfrastructureException;
import mx.gob.se.rug.exception.NoDataFoundException;
import mx.gob.se.rug.inscripcion.dao.AltaParteDAO;
import mx.gob.se.rug.inscripcion.to.OtorganteTO;
import mx.gob.se.rug.modificacion.to.AutoridadInstruye;
import mx.gob.se.rug.modificacion.to.ModificacionTO;
import mx.gob.se.rug.to.PlSql;
import mx.gob.se.rug.util.MyLogger;
import mx.gob.se.rug.util.to.DateUtilRug;

public class ModificacionDAO {
//	  peIdTramiteTemp          IN  RUG_REL_TRAM_INC_GARAN.ID_TRAMITE_TEMP%TYPE,  --  IDENTIFICADOR DEL TRAMITE ASOCIADO A LA GARANTIA
//    peIdGarantia             IN  RUG_GARANTIAS_PENDIENTES.ID_GARANTIA_PEND%TYPE,    --  IDENTIFICADOR DE LA GARANTIA
//    peIdTipoGarantia         IN  RUG_GARANTIAS_PENDIENTES.ID_TIPO_GARANTIA%TYPE,  --IDENTIFICADOR DEL TIPO DE GARANTIA QUE SE INSCRIBE
//    peFechaCelebGarantia      IN  RUG_GARANTIAS_PENDIENTES.FECHA_INSCR %TYPE, -- Fecha de celebraci�n del Acto o Contrato, que crea la garantia
//    peCambiosBienesMonto     IN  RUG_GARANTIAS_PENDIENTES.CAMBIOS_BIENES_MONTO%TYPE, --  El Acto o Contrato prev� incrementos, reducciones o sustituciones de los bienes muebles o del monto garantizado
//    peIdPerson               IN  RUG_PERSONAS.ID_PERSONA%TYPE, --   IDENTIFICADOR DE LA PERSONA QUE  HACE LA INSCRIPCION
//    peTipoBien               IN  VARCHAR2,  --CADENA QUE CONTIENE LOS IDS TIPOS DE BIENES QUE INTEGRA UNA GARANTIA, SEPARADOS POR EL CARACTER |  
//    peDescGarantia           IN  RUG_GARANTIAS_PENDIENTES.DESC_GARANTIA%TYPE,
//    peTipoContratoOb         IN  RUG_CONTRATO.TIPO_CONTRATO%TYPE,
//    peFechaCelebContOb       IN  RUG_CONTRATO.FECHA_INICIO%TYPE,
//    peOtrosTerminosCOb       IN  RUG_CONTRATO.OTROS_TERMINOS_CONTRATO%TYPE,
//    peMontoMaxGarantizado    IN  RUG_GARANTIAS_PENDIENTES.MONTO_MAXIMO_GARANTIZADO%TYPE,
//    peOtrosTerminosG         IN  RUG_GARANTIAS_PENDIENTES.OTROS_TERMINOS_GARANTIA%TYPE,
//    peTipoContratoBasa       IN  RUG_CONTRATO.TIPO_CONTRATO%TYPE,  --TIPO DE ACTO O CONTRATO EN QUE SE BASA LA MODIFICACION O RECTIFICACION POR ERROR
//    peFechaCelebBasa         IN  RUG_CONTRATO.FECHA_FIN%TYPE, -- FECHA DE CELEBRACION DEL ACTO O CONVENIO EN QUE SE BASA LA MODIFICACION
//    peOtrosTerminosBasa      IN  RUG_CONTRATO.OTROS_TERMINOS_CONTRATO%TYPE, --OTROS TERMINOS Y CONDICIONES DEL ACTO O CONVENIO EN QUE SE BASA LA MODIFICACION   
//    peIdMoneda               IN  RUG_CAT_MONEDAS.ID_MONEDA%TYPE,
//    peFechaFinBasa           IN  RUG_CONTRATO.FECHA_FIN%TYPE,
//    peInstrumentoPublico     IN  RUG_GARANTIAS_PENDIENTES.INSTRUMENTO_PUBLICO%TYPE,
//    peFechaFinOb             IN  RUG_CONTRATO.FECHA_FIN%TYPE,
//    psResult                 OUT  INTEGER,  
//    psTxResult               OUT  VARCHAR2
	
	public int actualiza (ModificacionTO modifica, DetalleTO detalleTO){
		int regresa = 0;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();		
		String sql = "{call RUG.SP_MODIFICA_GARANTIA ( ?,?,?,?,?," +
													  "?,?,?,?,?," +
													  "?,?,?,?,?," +
													  "?,?,?,?,?," +
													  "?,?,?,?," +
													  "?,?) } ";
		CallableStatement cs =null;
		try {
			cs = connection.prepareCall(sql);
			DateUtilRug dateUtil = new DateUtilRug();
			MyLogger.Logger.log(Level.INFO, "\nParametros para el PL:\n1  modifica.getIdtramite() " + modifica.getIdtramite()
			+ "\n2  modifica.getIdgarantia() " + modifica.getIdgarantia()
			+ "\n3  modifica.getTipogarantia() " + modifica.getTipogarantia()
			+ "\n4  modifica.getFechacelebgarantia() " + modifica.getFechacelebgarantia()
			+ "\n5 modifica.getCambioBienesMonto(); " + modifica.getCambioBienesMonto()
			+ "\n6  modifica.getIdpersona() " + modifica.getIdpersona()
			+ "\n7  modifica.getModtipobien() " + modifica.getModtipobien()
			+ "\n8  modifica.getModdescripcion() " + modifica.getModdescripcion()
			+ "\n9  modifica.getTipocontrato() TIPO CONTRATO OBLIGA " + modifica.getTipocontrato()
			+ "\n10  modifica.getFechainiob FECHA INICIAL OBLIGA " + modifica.getFechainiob()//
			+ "\n11  modifica.getModotroscontrato OTROS TERMINOS OBLIGA " + modifica.getModotroscontrato()
//			+ "\n12 modifica.getModmonto() " + Float.valueOf(modifica.getModmonto().replace(",",""))
			+ "\n13 modifica.getModotrosgarantia() " + modifica.getModotrosgarantia()
			+ "\n14 modifica.getModtipoacto() TIPO CONTRATO BASA " + modifica.getModtipoacto()
			+ "\n15 modifica.getModfechaceleb() FECHA INICIAL BASA " + modifica.getModfechaceleb()
			+ "\n16 modifica.getModterminos() OTROS TERMINOS BASA " + modifica.getModterminos()
			+ "\n17 modifica.getIdmoneda() " +  modifica.getIdmoneda()
			+ "\n18 modifica.getFechatermino() FECHA FIN BASA " + modifica.getFechatermino()
			+ "\n19 modifica.getModinstrumento() " + modifica.getModinstrumento()
			+ "\n20 modifica.getFechafinob()) FECHA FIN OBLIGA " + modifica.getFechafinob());
			
			
			cs.setInt(1, Integer.valueOf(modifica.getIdtramite()));
			cs.setInt(2, modifica.getIdgarantia());
			
			if(modifica.getTipogarantia()== null){
				cs.setNull(3,0);
			}else{
				cs.setInt(3, Integer.valueOf(modifica.getTipogarantia()));
			}
			
			if(modifica.getFechacelebgarantia()==null){
				cs.setNull(4,0);
			}else{cs.setDate(4,dateUtil.parseToSQLDate(modifica.getFechacelebgarantia()));}
			if(modifica.getCambioBienesMonto()==null){
				cs.setNull(5,0);
			}else{cs.setString(5,modifica.getCambioBienesMonto());}
			if( modifica.getIdpersona()==null){
				cs.setInt(6,0);
			}else{cs.setInt(6, modifica.getIdpersona());}
			if(modifica.getModtipobien()==null){
				cs.setString(7," |");
			}else{cs.setString(7, modifica.getModtipobien().replace(",", "|"));}	 	
			if(modifica.getModdescripcion()==null){
				cs.setString(8,"" );
			}else{cs.setString(8, modifica.getModdescripcion());}
			cs.setString(9, modifica.getTipocontrato());
			
			if(modifica.getFechainiob() == null){
				cs.setDate(10, null);
			}else{
				cs.setDate(10, dateUtil.parseToSQLDate(modifica.getFechainiob()));
			}
			cs.setString(11, modifica.getModotroscontrato());
			if( modifica.getModmonto()==null){
				cs.setNull(12, Types.NULL);
			}else{
				try{
					cs.setBigDecimal(12, new BigDecimal(modifica.getModmonto().replace(",","")));
				}catch(Exception e){
					cs.setNull(12, Types.NULL);
				}
				
				
			}
			if(modifica.getModotrosgarantia()==null){
				cs.setNull(13,0);
			}else{cs.setString(13, modifica.getModotrosgarantia());}
			if(modifica.getModtipoacto()== null){
				cs.setNull(14,0);
			}else{cs.setString(14, modifica.getModtipoacto());}
			if(modifica.getModfechaceleb()!=null){
				cs.setDate(15, dateUtil.parseToSQLDate(modifica.getModfechaceleb()));				
			}else{cs.setNull(15, 0);}
			if(modifica.getModterminos()==null){
				cs.setNull(16, 0);				
			}else{cs.setString(16, modifica.getModterminos());}
			if(modifica.getIdmoneda() == null){
				cs.setInt(17, 1); //Moneda Default
			}else{cs.setInt(17, modifica.getIdmoneda());}
			if(modifica.getFechatermino() == null){
				cs.setDate(18, null);
			}else{cs.setDate(18, dateUtil.parseToSQLDate(modifica.getFechatermino()));}
			if(modifica.getModinstrumento() == null){
				cs.setNull(19, 0);
			}else{cs.setString(19, modifica.getModinstrumento());}
			if(modifica.getFechafinob()==null){
				cs.setDate(20, null);
			}else{cs.setDate(20, dateUtil.parseToSQLDate(modifica.getFechafinob()));}
			
			//Nuevos campos
			if(modifica.getEsPrioritaria()==null){
				cs.setNull(21,0);
			}else{cs.setString(21,modifica.getEsPrioritaria());}
			if(modifica.getEnRegistro()==null){
				cs.setNull(22,0);
			}else{cs.setString(22,modifica.getEnRegistro());}
			if(modifica.getTxtRegistro()==null){
				cs.setNull(23,0);
			}else{cs.setString(23,modifica.getTxtRegistro());}
			if(modifica.getNoGarantiaPreviaOt()==null){
				cs.setNull(24,0);
			}else{cs.setString(24,modifica.getNoGarantiaPreviaOt());}
			
			cs.registerOutParameter(25, Types.INTEGER);
			cs.registerOutParameter(26, Types.VARCHAR);		
			cs.executeQuery();
			regresa = cs.getInt(25);
			MyLogger.Logger.log(Level.INFO, "Id mensaje PL = " + cs.getInt(25));
			MyLogger.Logger.log(Level.INFO, "Mensaje de la BD = " + cs.getString(26));

			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection,null,cs);
		}
		return regresa;
		
	}

	public int insertatramiteinc (Integer idPersona, Integer idTipoTramite){
		int regresa = 0;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call RUG.SP_Alta_Tramite_Incompleto ( ?,?,?,?,?) } ";
		CallableStatement cs =null;
		try {
			 cs = connection.prepareCall(sql);
			cs.setInt(1, idPersona);
			cs.setInt(2, idTipoTramite);
			cs.registerOutParameter(3, Types.INTEGER);
			cs.registerOutParameter(4, Types.INTEGER);
			cs.registerOutParameter(5, Types.VARCHAR);	
			
		    cs.executeQuery();
			regresa = cs.getInt(3);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,null,cs);
		}
		return regresa;
		
	}	
	
	public int bajaparte (Integer idPersona, Integer idParte, Integer idTramite){
		int regresa = 0;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call RUG.SP_BAJA_PARTE_LOGICA ( ?,?,?,?,?,?) } ";
		CallableStatement cs =null;
		try {
			cs = connection.prepareCall(sql);
			MyLogger.Logger.log(Level.INFO, "1,"+ idTramite);
			MyLogger.Logger.log(Level.INFO, "2,"+ idPersona);			
			MyLogger.Logger.log(Level.INFO, "3,"+ idParte);
			    
			cs.setInt(1, idTramite);
			cs.setInt(2, idPersona);			
			cs.setInt(3, idParte);
			cs.setString(4, "F");
			cs.registerOutParameter(5, Types.INTEGER);
			cs.registerOutParameter(6, Types.VARCHAR);	
			
			 cs.executeQuery();
			regresa = cs.getInt(5);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,null,cs);
		}
		return regresa;
		
	}	
	
	public int altaTransmmision (Integer idPersona, Integer idGarantia){
		int regresa = 0;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call RUG.SP_Alta_Transmision( ?,?,?,?,?) } ";
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
			MyLogger.Logger.log(Level.INFO, "1,"+ idPersona);
			MyLogger.Logger.log(Level.INFO, "2,"+ idGarantia);			
			MyLogger.Logger.log(Level.INFO, "id Personav = " + idPersona);
			MyLogger.Logger.log(Level.INFO, "id garantia = " + idGarantia);
			
			cs.setInt(1, idPersona);
			cs.setInt(2, idGarantia);			
			cs.registerOutParameter(3, Types.INTEGER);
			cs.registerOutParameter(4, Types.INTEGER);
			cs.registerOutParameter(5, Types.VARCHAR);	
			
			cs.executeQuery();
			MyLogger.Logger.log(Level.INFO, "3 "+ cs.getInt(3));
			MyLogger.Logger.log(Level.INFO, "4 "+ cs.getInt(4));
			regresa = cs.getInt(3);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,null,cs);
		}
		return regresa;
		
	}
	
	public Integer altapartesBienesInc (Integer idUltimoTramite, Integer idNuevoTramite){
		int regresa = 0;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call RUG.SP_ALTA_BIEN_INCOMPLETO( ?,?,?,? ) } ";
		CallableStatement cs =null;
		try {
			 cs = connection.prepareCall(sql);
	
			cs.setInt(1, idNuevoTramite);
			cs.setInt(2, idUltimoTramite);				
			cs.registerOutParameter(3, Types.INTEGER);
			cs.registerOutParameter(4, Types.VARCHAR);				
			
			cs.executeQuery();			
			MyLogger.Logger.log(Level.INFO, "res "+ cs.getInt(3));
			MyLogger.Logger.log(Level.INFO, "tx "+ cs.getString(4));
			int idGenerado = cs.getInt(3);
			if (idGenerado == 0){
				regresa= 1;
			}else{
				throw new SQLException("sucedio un error al ejecutar RUG.SP_ALTA_BIEN_INCOMPLETO oracle error :" + cs.getString(6));							
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,null,cs);
		}
		return regresa;
	}
	
	public Integer altapartesTramiteInc (Integer idPersona,Integer idTipoPersona, Integer idGarantia){
		int regresa = 0;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call RUG.SP_Alta_Partes_Tramites_Incomp( ?,?,?,?,?,?) } ";
		CallableStatement cs =null;
		try {
			 cs = connection.prepareCall(sql);
			 // MyLogger.Logger.log(Level.INFO, "1,"+ idPersona);
			 // MyLogger.Logger.log(Level.INFO, "2,"+ idTipoPersona);
			 // MyLogger.Logger.log(Level.INFO, "3,"+ idGarantia);			
			 // MyLogger.Logger.log(Level.INFO, "id Personav = " + idPersona);
			 // MyLogger.Logger.log(Level.INFO, "id garantia = " + idGarantia);
			// MyLogger.Logger.log(Level.INFO, "id tipopersona = " + idTipoPersona);
			
			cs.setInt(1, idPersona);
			cs.setInt(2, idTipoPersona);	
			cs.setInt(3, idGarantia);
			cs.registerOutParameter(4, Types.INTEGER);
			cs.registerOutParameter(5, Types.INTEGER);
			cs.registerOutParameter(6, Types.VARCHAR);	
			// cs.registerOutParameter(6, Types.VARCHAR);	
			
			cs.executeQuery();
			// MyLogger.Logger.log(Level.INFO, "4 "+ cs.getInt(4)); 82547
			// MyLogger.Logger.log(Level.INFO, "5 "+ cs.getInt(5)); 
			// MyLogger.Logger.log(Level.INFO, "6 "+ cs.getString(6));
			int idGenerado = cs.getInt(5);
			MyLogger.Logger.log(Level.INFO, "Parametros de los Querys para alterar tramite: " + cs);
			MyLogger.Logger.log(Level.INFO, "Parametro 1: " + idPersona + " Parametro 2: "+ idTipoPersona + " parametro 3: " + idGarantia + " parametro 4" + cs.getInt(4) + " parametro 5: " + cs.getInt(5) + " parametro 6: " + cs.getString(6));
			if (idGenerado == 0){
				regresa= cs.getInt(4);
			}else{
				throw new SQLException("sucedio un error al ejecutar RUG.SP_Alta_Partes_Tramites_Incomp oracle error :" + cs.getString(6));							
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,null,cs);
		}
		return regresa;
		
	}
	public PlSql executeSPAltaPartesTramitesIncomp(Integer idPersona,Integer idTipoTramite, Integer idGarantia){
		PlSql regresa = null;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call RUG.SP_Alta_Partes_Tramites_Incomp( ?,?,?,?,?,?) } ";
		CallableStatement cs =null;
		try {
			 cs = connection.prepareCall(sql);
			
			cs.setInt(1, idPersona);
			cs.setInt(2, idTipoTramite);	
			cs.setInt(3, idGarantia);
			cs.registerOutParameter(4, Types.INTEGER);
			cs.registerOutParameter(5, Types.INTEGER);
			cs.registerOutParameter(6, Types.VARCHAR);	
			
			cs.executeQuery();

			regresa = new PlSql();
			regresa.setIntPl(cs.getInt(17));
			regresa.setStrPl(cs.getString(18));
			regresa.setResIntPl(cs.getInt(16));	
			
			MyLogger.Logger.log(Level.INFO, "RugDAO::ModificacionDAO.executeSPAltaPartesTramitesIncomp-------------------Integer PL  = " + cs.getInt(4));
			MyLogger.Logger.log(Level.INFO, "RugDAO::ModificacionDAO.executeSPAltaPartesTramitesIncomp-------------------Varchar PL  = " + cs.getInt(5));
			MyLogger.Logger.log(Level.INFO, "RugDAO::ModificacionDAO.executeSPAltaPartesTramitesIncomp-------------------Integer Result  = " + cs.getString(6));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			regresa = new PlSql();
			regresa.setIntPl(999);
			regresa.setStrPl(e.getErrorCode()+"errorcode|"+e.getSQLState()+"sqlsate|"+e.getStackTrace().toString());
			regresa.setResIntPl(0);			
			e.printStackTrace();
			
		}finally{
			bd.close(connection,null,cs);
		}
		return regresa;
		
	}
	//FNValidaModificaOtorgante(peIdGarantia     NUMBER,  peIdTramiteTemp  NUMBER )
	public int getModparte (Integer idGarantia,Integer idTramite ){
		int regresa = 0;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		MyLogger.Logger.log(Level.INFO, "entre");
		String sql = "SELECT  FNValidaModificaOtorgante(?,?) as CAMBIO FROM DUAL ";
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {			
			ps = connection.prepareStatement(sql);			
			ps.setInt(1, idGarantia);
			ps.setInt(2, idTramite);			
			
			rs = ps.executeQuery();
			if(rs.next()){
				regresa=rs.getInt("CAMBIO");
				MyLogger.Logger.log(Level.INFO, "4 "+ rs.getInt("CAMBIO"));
				//MyLogger.Logger.log(Level.INFO, "5 "+ rs.getInt(5));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return regresa;
		
	}
	
	public int altaCancelTramite(Integer idTramite,Integer idGarantia, Integer idUsuario){
		int regresa = 0;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call RUG.SP_ALTA_CANCEL_TRANSMISION( ?,?,?,?,?) } ";
		CallableStatement cs =null;
		try {
			 cs = connection.prepareCall(sql);
			MyLogger.Logger.log(Level.INFO, "1,"+ idTramite);
			MyLogger.Logger.log(Level.INFO, "2,"+ idGarantia);
			MyLogger.Logger.log(Level.INFO, "3,"+ idUsuario);			
			
			cs.setInt(1, idTramite);
			cs.setInt(2, idGarantia);	
			cs.setInt(3, idUsuario);
			cs.registerOutParameter(4, Types.INTEGER);
			cs.registerOutParameter(5, Types.VARCHAR);
			
			cs.executeQuery();
			MyLogger.Logger.log(Level.INFO, "4 "+ cs.getInt(4));
			MyLogger.Logger.log(Level.INFO, "5 "+ cs.getString(5));			
			regresa = cs.getInt(4);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,null,cs);
		}
		return regresa;
		
	}
	
	// TODO: Nuevo metodo para saber si se modifico laparte de acredores adicionales
	public int cambioAcreedores(Integer idTramite, Integer idGarantia){
		int regresa = 0;
		AltaParteDAO dao = new AltaParteDAO();
		List <OtorganteTO> otorganteTO = dao.getOtorganteByInscripcion(idTramite);
		List <OtorganteTO> oto2 = dao.getUltimoOtorganteByGarantia(idGarantia);
		if (!otorgantesIguales(oto2, otorganteTO)){
			MyLogger.Logger.log(Level.WARNING, "otorgantes fueron distintos");
			regresa = 1;
		}else{
			MyLogger.Logger.log(Level.WARNING, "otorgantes fueron iguales");
		}
		if (regresa == 0){
			List <AcreedorTO> acredores1 = dao.getListaAcreedores(idTramite);
			List <AcreedorTO> acreedores2 = dao.getListaUltimosAcreedores(idGarantia);
			MyLogger.Logger.log(Level.INFO, "lista acredores 1--" + acredores1.size());
			MyLogger.Logger.log(Level.INFO, "lista acredores 1--" + acreedores2.size() );
			if (acredores1.size() != acreedores2.size()){				
				regresa = 1;
			}else if (!comparaListaAcreedores(acredores1, acreedores2)){
				MyLogger.Logger.log(Level.WARNING, "datos de las listas fueron distintas");
				regresa = 1;
			}else{
				MyLogger.Logger.log(Level.WARNING, "las listas fueron iguales");
			}
		}
		return regresa;

	}
	
	public boolean comparaListaAcreedores(List <AcreedorTO> acredores1, List <AcreedorTO> acredores2){
		boolean regresa  = true;
		Iterator<AcreedorTO> it = acredores1.iterator();
		Iterator<AcreedorTO> it2;
		while (it.hasNext() && regresa){
			AcreedorTO acre = it.next();
			it2 = acredores2.iterator();
			boolean existio = false;
			while (it2.hasNext() && !existio){
				AcreedorTO acre2 = it2.next();
				if (acreedoresIguales(acre, acre2)){
					existio = true;
				}
			}
			if (!existio){
				regresa = false;
			}
		}
		return regresa;
	}
	
	public boolean otorgantesIguales(List <OtorganteTO> deudorTramite, List <OtorganteTO> deudorGarantia){
		boolean regresa = true;
		for (OtorganteTO oto:deudorTramite){
			for (OtorganteTO oto2: deudorGarantia){
				if (!oto.getNombreCompleto().equals(oto2.getNombreCompleto())){
					regresa = false;
				}else if (!oto.getTipoPersona().equals(oto2.getTipoPersona())){
					regresa = false;
				}
			}
			
		}
		
		return regresa;
	}
	
	public boolean acreedoresIguales(AcreedorTO deudorTramite, AcreedorTO deudorGarantia){
		boolean regresa = true;
		if (!deudorTramite.getNombreCompleto().equals(deudorGarantia.getNombreCompleto())){			
			regresa = false;
		}else if (!deudorTramite.getTipoPersona().equals(deudorGarantia.getTipoPersona())){			
			regresa = false;
		}
		return regresa;
	}
	
//	RUG.SP_REL_PARTE_RECTIFICA(
//
//	   peIdPersona     IN NUMBER,
//
//	   peIdTramiteTemp 
	
	public int alta_Parte_Rectifica (Integer idPersona, Integer idTramite){
		int regresa = 0;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call RUG.SP_REL_PARTE_RECTIFICA( ?,?,?,?) } ";
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
			MyLogger.Logger.log(Level.INFO, "1,"+ idPersona);
			MyLogger.Logger.log(Level.INFO, "2,"+ idTramite);			
			
			
			cs.setInt(1, idPersona);
			cs.setInt(2, idTramite);			
			cs.registerOutParameter(3, Types.INTEGER);
			cs.registerOutParameter(4, Types.VARCHAR);	
			
			cs.executeQuery();
			MyLogger.Logger.log(Level.INFO, "3 "+ cs.getInt(3));
			MyLogger.Logger.log(Level.INFO, "4 "+ cs.getString(4));
			regresa = cs.getInt(3);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,null,cs);
		}
		return regresa;
		
	}
	
	
//	RUG.SP_MODIFICA_GARANTIA_RECTIFICA
//	(
//	    peIdTramiteTemp            IN  RUG_REL_TRAM_INC_GARAN.ID_TRAMITE_TEMP%TYPE,  --  IDENTIFICADOR DEL TRAMITE ASOCIADO A LA GARANTIA
//	    peIdGarantia                   IN  RUG_GARANTIAS_PENDIENTES.ID_GARANTIA_PEND%TYPE,    --  IDENTIFICADOR DE LA GARANTIA
//	    peIdTipoGarantia             IN  RUG_GARANTIAS_PENDIENTES.ID_TIPO_GARANTIA%TYPE,  --IDENTIFICADOR DEL TIPO DE GARANTIA QUE SE INSCRIBE
//	    peFechaCelebGarantia     IN  RUG_GARANTIAS_PENDIENTES.FECHA_INSCR %TYPE, -- Fecha de celebraci�n del Acto o Contrato, que crea la garantia
//	    peMontoMaxGarantizado    IN  RUG_GARANTIAS_PENDIENTES.MONTO_MAXIMO_GARANTIZADO%TYPE,
//	    peIdMoneda                      IN  RUG_CAT_MONEDAS.ID_MONEDA%TYPE,
//	    peTipoBien                        IN  VARCHAR2,  --CADENA QUE CONTIENE LOS IDS TIPOS DE BIENES QUE INTEGRA UNA GARANTIA,
//	    peDescGarantia                 IN  RUG_GARANTIAS_PENDIENTES.DESC_GARANTIA%TYPE,
//	    peCambiosBienesMonto     IN  RUG_GARANTIAS_PENDIENTES.CAMBIOS_BIENES_MONTO%TYPE, --  El Acto o Contrato prev� incrementos, reducciones o 
//	    peInstrumentoPublico         IN  RUG_GARANTIAS_PENDIENTES.INSTRUMENTO_PUBLICO%TYPE,
//	    peOtrosTerminosG            IN  RUG_GARANTIAS_PENDIENTES.OTROS_TERMINOS_GARANTIA%TYPE,
//	    peTipoContratoOb             IN  RUG_CONTRATO.TIPO_CONTRATO%TYPE,
//	    peFechaCelebContOb       IN  RUG_CONTRATO.FECHA_INICIO%TYPE,
//	    peFechaFinOb                 IN  RUG_CONTRATO.FECHA_FIN%TYPE,
//	    peOtrosTerminosCOb       IN  RUG_CONTRATO.OTROS_TERMINOS_CONTRATO%TYPE,  --PEFECHATERMCELBECONTR    
//	    peIdPerson                    IN  RUG_PERSONAS.ID_PERSONA%TYPE, --   IDENTIFICADOR DE LA PERSONA QUE  HACE LA INSCRIPCION   
//	    psResult                      OUT  INTEGER,  
//	    psTxResult                   OUT  VARCHAR2
//	)
	
	public int modificaRectificacion(ModificacionTO modifica){
		int regresa = 0;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call RUG.SP_MODIFICA_GARANTIA_RECTIFICA(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) } ";
		CallableStatement cs =null;
		try {
			cs = connection.prepareCall(sql);
			DateUtilRug dateUtil = new DateUtilRug();

	
//			MyLogger.Logger.log(Level.INFO,"\nParametros para el PL:\n1  modifica.getIdtramite() " + modifica.getIdtramite()
//			+ "\n2  modifica.getIdgarantia() " + modifica.getIdgarantia()
//			+ "\n3  modifica.getTipogarantia() " + modifica.getTipogarantia()
//			+ "\n4  modifica.getFechacelebgarantia() " + modifica.getFechacelebgarantia()
//			+ "\n5 modifica.getModmonto() " + Float.valueOf(modifica.getModmonto().replace(",",""))
//			+ "\n6 modifica.getIdmoneda() " +  modifica.getIdmoneda()
//			+ "\n7  modifica.getModtipobien() " + modifica.getModtipobien()
//			+ "\n8  modifica.getModdescripcion() " + modifica.getModdescripcion()
//			+ "\n9 modifica.getCambioBienesMonto(); " + modifica.getCambioBienesMonto()
//			+ "\n10 modifica.getModinstrumento() " + modifica.getModinstrumento()
//			+ "\n11 modifica.getModotrosgarantia() " + modifica.getModotrosgarantia()
//			+ "\n12  modifica.getTipocontrato() TIPO CONTRATO OBLIGA " + modifica.getTipocontrato()
//			+ "\n13  modifica.getFechainiob FECHA INICIAL OBLIGA " + modifica.getFechainiob()
//			+ "\n14 modifica.getFechafinob()) FECHA FIN OBLIGA " + modifica.getFechafinob()
//			+ "\n15  modifica.getModotroscontrato OTROS TERMINOS OBLIGA " + modifica.getModotroscontrato()
//			+ "\n16  modifica.getIdpersona() " + modifica.getIdpersona());
			
			
			cs.setInt(1, Integer.valueOf(modifica.getIdtramite()));
			cs.setInt(2, modifica.getIdgarantia());
			
			if(modifica.getTipogarantia()== null){
				cs.setNull(3,0);
			}else{
				cs.setInt(3, Integer.valueOf(modifica.getTipogarantia()));
			}
			
			if(modifica.getFechacelebgarantia()==null){
				cs.setNull(4,0);
			}else{cs.setDate(4,dateUtil.parseToSQLDate(modifica.getFechacelebgarantia()));}
			if( modifica.getModmonto()==null){
				cs.setNull(5, Types.NULL);
			}else{
				try{
					cs.setBigDecimal(5,new BigDecimal(modifica.getModmonto().replace(",","")));
				}catch(Exception e){
					cs.setNull(5, Types.NULL);
				}
				
				
			}
			
			if(modifica.getIdmoneda() == null){
				cs.setInt(6, 0);
			}else{cs.setInt(6, modifica.getIdmoneda());}
			if(modifica.getModtipobien()==null){
				cs.setString(7," |");
			}else{cs.setString(7, modifica.getModtipobien().replace(",", "|"));}		
			if(modifica.getModdescripcion()==null){
				cs.setString(8,"" );
			}else{cs.setString(8, modifica.getModdescripcion());}
			if(modifica.getCambioBienesMonto()==null){
				cs.setNull(9,0);
			}else{cs.setString(9,modifica.getCambioBienesMonto());}
			if(modifica.getModinstrumento() == null){
				cs.setDate(10, null);
			}else{cs.setString(10, modifica.getModinstrumento());}
			if(modifica.getModotrosgarantia()==null){
				cs.setNull(11,0);
			}else{cs.setString(11, modifica.getModotrosgarantia());}
			
			cs.setString(12, modifica.getTipocontrato());
			if(modifica.getFechainiob() == null){
				cs.setDate(13, null);
			}else{
				cs.setDate(13, dateUtil.parseToSQLDate(modifica.getFechainiob()));}
			if(modifica.getFechafinob()==null){
				cs.setDate(14, null);
			}else{cs.setDate(14, dateUtil.parseToSQLDate(modifica.getFechafinob()));}
			cs.setString(15, modifica.getModotroscontrato());
						
			if( modifica.getIdpersona()==null){
				cs.setInt(16,0);
			}else{cs.setInt(16, modifica.getIdpersona());}
			
//			if(modifica.getModtipoacto()== null){
//				cs.setNull(14,0);
//			}else{cs.setString(14, modifica.getModtipoacto());}
//			if(modifica.getModfechaceleb()!=null){
//				cs.setDate(15, dateUtil.parseToSQLDate(modifica.getModfechaceleb()));				
//			}else{cs.setNull(15, 0);}
//			if(modifica.getModterminos()==null){
//				cs.setNull(16, 0);				
//			}else{cs.setString(16, modifica.getModterminos());}
//			if(modifica.getFechatermino() == null){
//				cs.setDate(18, null);
//			}else{cs.setDate(18, dateUtil.parseToSQLDate(modifica.getFechatermino()));}
			
			cs.registerOutParameter(17, Types.INTEGER);
			cs.registerOutParameter(18, Types.VARCHAR);		
			cs.executeQuery();
			regresa = cs.getInt(17);
			MyLogger.Logger.log(Level.INFO, "Id mensaje PL = " + cs.getString(17));
			MyLogger.Logger.log(Level.INFO, "Mensaje de la BD = " + cs.getString(18));

			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection,null,cs);
		}
		return regresa;
		
	}
	public List<TramiteHTO> getHtramite (Integer idTramite, Integer idgarantia ){
		
		List <TramiteHTO> regresa = new ArrayList<TramiteHTO>();
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		MyLogger.Logger.log(Level.INFO, "entre " + idgarantia);
		
		String DATE_FORMAT = "dd/MM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		
		String sql = "SELECT ID_TRAMITE,DESCRIPCION,FECHA_CELEB,FECHA_TERMINACION,OTROS_TERMINOS_CONTRATO FROM V_GARANTIA_CONTRATOS " + 
                     " WHERE ID_GARANTIA =? ";
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {			
			ps = connection.prepareStatement(sql);			
			ps.setInt(1, idgarantia);
			rs = ps.executeQuery();
			while(rs.next()){
				TramiteHTO tramite = new TramiteHTO();
				
				tramite.setIdtramite(new Integer(rs.getInt("ID_TRAMITE")));
				MyLogger.Logger.log(Level.INFO, "id tramite " + tramite.getIdtramiteh());
				if(rs.getString("DESCRIPCION")==null){
					tramite.setTipocontrato("");
				}else{tramite.setTipocontrato(rs.getString("DESCRIPCION"));}				
				if(rs.getDate("FECHA_CELEB")==null){
					tramite.setFechacelebra(null);
				}else{tramite.setFechacelebra(sdf.format(rs.getDate("FECHA_CELEB")));}
				if(rs.getDate("FECHA_TERMINACION")==null){
					tramite.setFechatermino(null);
				}else{tramite.setFechatermino(sdf.format(rs.getDate("FECHA_TERMINACION")));}
				if(rs.getString("OTROS_TERMINOS_CONTRATO")==null){
					tramite.setOtrosterminos("");
				}else{tramite.setOtrosterminos(rs.getString("OTROS_TERMINOS_CONTRATO"));}
				
				regresa.add(tramite);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return regresa;
		
	}
	public AutoridadInstruye getAutoridadInstruyeTramite (Integer idGarantia ) throws InfrastructureException, NoDataFoundException{
		
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		MyLogger.Logger.log(Level.INFO, "entre " + idGarantia);
		AutoridadInstruye autoridadInstruye = new  AutoridadInstruye();
		
		String sql = "SELECT ID_TIPO_TRAMITE,ANOTACION_JUEZ FROM ( "+
						"SELECT T.ID_TIPO_TRAMITE, RA.ANOTACION_JUEZ "+
						 " FROM RUG_GARANTIAS_H RGH, "+
						    "   TRAMITES T, "+
						    "   RUG_AUTORIDAD RA, "+
						    "   RUG_BITAC_TRAMITES RBT "+
						 "WHERE RA.ID_TRAMITE_TEMP = T.ID_TRAMITE_TEMP "+ 
						 "AND RBT.ID_STATUS = 3 "+
						 "AND RBT.STATUS_REG = 'AC' "+
						 "AND RBT.ID_TRAMITE_TEMP = T.ID_TRAMITE_TEMP "+ 
						 "AND T.ID_TRAMITE = RGH.ID_ULTIMO_TRAMITE "+
						 "AND RGH.ID_GARANTIA = ? "+
						 "ORDER BY RBT.FECHA_STATUS DESC "+
					 ")WHERE ROWNUM = 1" ;
		
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {			
			ps = connection.prepareStatement(sql);			
			ps.setInt(1, idGarantia);
			rs = ps.executeQuery();
			if(rs.next()){
				autoridadInstruye.setIdTipoTramite(rs.getInt("ID_TIPO_TRAMITE"));
				autoridadInstruye.setAnotacionJuez(rs.getString("ANOTACION_JUEZ"));
			}else{
				throw new NoDataFoundException("No se encontraron Autoridad para el idGarantia"+idGarantia);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InfrastructureException("Hubo un problema en la Base de datos");
		}finally{
			bd.close(connection,rs,ps);
		}
		return autoridadInstruye;
		
	}
	public Boolean saveAutoridadInstruyeTramite (Integer idTramite , String autoridadInstruye) throws InfrastructureException, NoDataFoundException{
		Boolean flagUpdateOk=false;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		MyLogger.Logger.log(Level.INFO, "entre " + idTramite);
		
		String sql = "UPDATE RUG_AUTORIDAD_PEND SET ANOTACION_JUEZ = ? WHERE ID_TRAMITE_TEMP = ( "+
                            "SELECT ID_TRAMITE_TEMP FROM RUG_AUTORIDAD_PEND WHERE ID_TRAMITE_TEMP_NVO =  ?)" ;
		
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {			
			ps = connection.prepareStatement(sql);			
			ps.setString(1, autoridadInstruye);
			ps.setInt(2, idTramite);
			int nRows = ps.executeUpdate();
			
			if(nRows>0){
				flagUpdateOk=true;
			}else{
				throw new NoDataFoundException("No se encontraron Autoridad para el idGarantia"+idTramite);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InfrastructureException("Hubo un problema en la Base de datos");
		}finally{
			bd.close(connection,rs,ps);
		}
		return flagUpdateOk;
		
	}
}
