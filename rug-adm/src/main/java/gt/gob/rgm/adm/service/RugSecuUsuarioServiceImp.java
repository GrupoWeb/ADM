package gt.gob.rgm.adm.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.criteria.Path;

import gt.gob.rgm.adm.model.RugPersonasFisicas;
import org.apache.commons.math3.util.Precision;

import gt.gob.rgm.adm.dao.RugSecuUsuarioRepository;
import gt.gob.rgm.adm.domain.ExternalUser;
import gt.gob.rgm.adm.domain.GarantiaStats;
import gt.gob.rgm.adm.domain.SecuUsuarioStats;
import gt.gob.rgm.adm.model.GenericCount;
import gt.gob.rgm.adm.model.RugPersonas;
import gt.gob.rgm.adm.model.RugSecuUsuario;

@Stateless
public class RugSecuUsuarioServiceImp implements RugSecuUsuarioService {
	
	@Inject
	private RugSecuUsuarioRepository usuarioDao;

	@Override
	public int update(Long id, String estado) {
		RugSecuUsuario usuarioEncontrado = usuarioDao.findById(id);
		if(usuarioEncontrado != null) {
			usuarioEncontrado.setSitUsuario(estado);
			usuarioEncontrado.setFhUltActualizacion(Date.valueOf(LocalDate.now()));
			usuarioDao.save(usuarioEncontrado);
			return 1;
		} else {
			// usuario no encontrado
			return 0;
		}
	}
	
	public List<RugSecuUsuario> listUsuarios(ExternalUser filter, Integer page, Integer size) {
		//return usuarioDao.findWithFilter(filter, page, size);
		
		String where = "";
		if(filter.getStatus() != null) {
        	where += "AND RSU.SIT_USUARIO = '" + filter.getStatus() + "'";
        }
        if(filter.getName() != null) {
    		where += "AND UPPER(RPF.NOMBRE_PERSONA) LIKE '%" + filter.getName().toUpperCase() + "%'";
        }
        if(filter.getEmail() != null) {
    		where += "AND LOWER(RSU.CVE_USUARIO) LIKE '%" + filter.getEmail().toLowerCase() + "%'";
        }
        if(filter.getDocId() != null) {
    		where += "AND RP.CURP_DOC LIKE '%" + filter.getDocId() + "%'";
        }
        if(filter.getNit() != null) {
    		where += "AND RP.RFC LIKE '%" + filter.getNit() + "%'";
        }
        String additionalWhere = "";
	    if(filter.getCorreosError() != null && filter.getCorreosError().longValue() == 1) {
	    	additionalWhere += "WHERE (SUB.CORREOS_ERROR > 0\n" + 
	    			"OR SUB.CORREOS_NO_ENVIADOS > 0)\n";
	    }
	    
		// String baseSql = "SELECT * FROM (\n" + 
		// 		"SELECT \n" + 
		// 		"	RSU.ID_PERSONA,\n" + 
		// 		"	RSU.B_FIRMADO,\n" + 
		// 		"	RSU.CVE_ACREEDOR,\n" + 
		// 		"	RSU.CVE_INSTITUCION,\n" + 
		// 		"	RSU.CVE_USU_AUTENTICACION,\n" + 
		// 		"	RSU.CVE_USUARIO,\n" + 
		// 		"	RSU.CVE_USUARIO_PADRE,\n" + 
		// 		"	RSU.F_ASIGNA_PSW,\n" + 
		// 		"	RSU.F_PROX_CAMB_PSW,\n" + 
		// 		"	RSU.F_VENCE_PSW,\n" + 
		// 		"	RSU.FH_BAJA,\n" + 
		// 		"	RSU.FH_REGISTRO,\n" + 
		// 		"	RSU.FH_ULT_ACCESO,\n" + 
		// 		"	RSU.FH_ULT_ACTUALIZACION,\n" + 
		// 		"	RSU.ID_GRUPO,\n" + 
		// 		"	RSU.NOM_ALIAS,\n" + 
		// 		"	RSU.NUM_ERRORES_PSW,\n" + 
		// 		"	RSU.NUM_LOG_INVALIDO,\n" + 
		// 		"	RSU.PASSWORD,\n" + 
		// 		"	RSU.PREG_RECUPERA_PSW,\n" + 
		// 		"	RSU.RESP_RECUPERA_PSW,\n" + 
		// 		"	RSU.SIT_USUARIO,\n" + 
		// 		"	RSU.TOKEN,\n" + 
		// 		"	(SELECT COUNT(1) FROM RUG_MAIL_POOL RMP WHERE RMP.DESTINATARIO = RSU.CVE_USUARIO AND RMP.ID_STATUS_MAIL = 1) AS CORREOS_NO_ENVIADOS,\n" + 
		// 		"	(SELECT COUNT(1) FROM RUG_MAIL_POOL RMP WHERE RMP.DESTINATARIO = RSU.CVE_USUARIO AND RMP.ID_STATUS_MAIL = 3) AS CORREOS_ERROR\n" + 
		// 		"FROM RUG_SECU_USUARIOS RSU, RUG_PERSONAS RP, RUG_PERSONAS_FISICAS RPF\n" + 
		// 		"WHERE RSU.ID_PERSONA = RP.ID_PERSONA\n" +
		// 		"AND RSU.ID_PERSONA = RPF.ID_PERSONA\n" +
		String baseSql = " SELECT * FROM  \n" +
							" (SELECT  \n" +
								" RSU.ID_PERSONA, \n" +
								" RSU.B_FIRMADO, \n" +
								" RSU.CVE_ACREEDOR, \n" +
								" RSU.CVE_INSTITUCION, \n" +
								" RSU.CVE_USU_AUTENTICACION, \n" +
								" RSU.CVE_USUARIO, \n" +
								" RSU.CVE_USUARIO_PADRE, \n" +
								" RSU.F_ASIGNA_PSW, \n" +
								" RSU.F_PROX_CAMB_PSW, \n" +
								" RSU.F_VENCE_PSW, \n" +
								" RSU.FH_BAJA, \n" +
								" RSU.FH_REGISTRO, \n" +
								" RSU.FH_ULT_ACCESO, \n" +
								" RSU.FH_ULT_ACTUALIZACION, \n" +
								" RSU.ID_GRUPO, \n" +
								" RSU.NOM_ALIAS, \n" +
								" RSU.NUM_ERRORES_PSW, \n" +
								" RSU.NUM_LOG_INVALIDO, \n" +
								" RSU.PASSWORD, \n" +
								" RSU.PREG_RECUPERA_PSW, \n" +
								" RSU.RESP_RECUPERA_PSW, \n" +
								" RSU.SIT_USUARIO, \n" +
								" RSU.TOKEN, \n" +
								" (SELECT COUNT(1) FROM RUG_MAIL_POOL RMP WHERE RMP.DESTINATARIO = RSU.CVE_USUARIO AND RMP.ID_STATUS_MAIL = 1) AS CORREOS_NO_ENVIADOS, \n" +
								" (SELECT COUNT(1) FROM RUG_MAIL_POOL RMP WHERE RMP.DESTINATARIO = RSU.CVE_USUARIO AND RMP.ID_STATUS_MAIL = 3) AS CORREOS_ERROR \n" +
							"  FROM RUG_SECU_USUARIOS RSU \n" +
								" INNER JOIN RUG_PERSONAS RP \n" +
								"  ON RSU.ID_PERSONA = RP.ID_PERSONA  \n" +
								" INNER JOIN RUG_PERSONAS_FISICAS RPF \n" +
									" ON RSU.ID_PERSONA = RPF.ID_PERSONA \n" +
				where +
				") SUB \n" + 
				additionalWhere;
				// "ORDER BY SUB.FH_REGISTRO ASC";
		List<RugSecuUsuario> usuarios = usuarioDao.findNative(baseSql, page, size, RugSecuUsuario.class);
		return usuarios;
	}

	@Override
	public List<RugSecuUsuario> listUsuarios(String estado, Integer page, Integer size) {
		return usuarioDao.findByStatusOrderedByFecha(estado, page, size);
	}
	
	public List<RugSecuUsuario> listAllUsuarios(Integer page, Integer size) {
		return usuarioDao.findAll(page, size);
	}
	
	public List<RugSecuUsuario> listUsuariosNoMigracion(Integer page, Integer size) {
		return usuarioDao.findNotMigracion(page, size);
	}

	@Override
	public RugSecuUsuario getUsuario(Long id) {
		return usuarioDao.findById(id);
	}

	@Override
	public List<GenericCount> summaryUsuarios(String fechaInicio, String fechaFin, Boolean migracion) {
		//return usuarioDao.summaryUsuariosByStatus(fechaInicio, fechaFin, migracion);
		String additionalWhere = "";
	    if (migracion == null || !migracion) {
	    	additionalWhere += "AND su.cve_usuario not like '%correo%'\n" + 
    			"AND su.cve_institucion <> 'RGM'\n";
	    }
	    
		String baseSql = "SELECT ROWNUM AS id, sub.* FROM (SELECT\n" +
				"su.sit_usuario AS descripcion,\n" + 
				"COUNT(1) AS total\n" + 
				"FROM rug_secu_usuarios su, rug_personas p\n" + 
				"WHERE su.id_persona = p.id_persona\n" + 
				"AND NVL(su.fh_ult_actualizacion, su.fh_registro) BETWEEN TO_DATE('" + fechaInicio + " 00:00:00', 'YYYY-MM-DD HH24:MI:SS') and TO_DATE('" + fechaFin + " 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n" +
				additionalWhere + 
				"GROUP BY su.sit_usuario) SUB\n" +
				"ORDER BY 2";
		
		List<GenericCount> stats = usuarioDao.statsUsuarios(baseSql);
		
		return stats;
	}
	
	public Long countUsuarios(ExternalUser filter) {
		//return usuarioDao.countWithFilter(filter);
		String where = "";
		if(filter.getStatus() != null) {
        	where += "AND RSU.SIT_USUARIO = '" + filter.getStatus() + "'";
        }
        if(filter.getName() != null) {
    		where += "AND UPPER(RPF.NOMBRE_PERSONA) LIKE '%" + filter.getName().toUpperCase() + "%'";
        }
        if(filter.getEmail() != null) {
    		where += "AND LOWER(RSU.CVE_USUARIO) LIKE '%" + filter.getEmail().toLowerCase() + "%'";
        }
        if(filter.getDocId() != null) {
    		where += "AND RP.CURP_DOC LIKE '%" + filter.getDocId() + "%'";
        }
        if(filter.getNit() != null) {
    		where += "AND RP.RFC LIKE '%" + filter.getNit() + "%'";
        }
        String additionalWhere = "";
	    if(filter.getCorreosError() != null && filter.getCorreosError().longValue() == 1) {
	    	additionalWhere += "WHERE (SUB.CORREOS_ERROR > 0\n" + 
	    			"OR SUB.CORREOS_NO_ENVIADOS > 0)\n";
	    }
	    
		String baseSql = "SELECT COUNT(1) AS ID, COUNT(1) AS TOTAL FROM (\n" + 
				"SELECT \n" + 
				"	RSU.ID_PERSONA,\n" + 
				"	RSU.B_FIRMADO,\n" + 
				"	RSU.CVE_ACREEDOR,\n" + 
				"	RSU.CVE_INSTITUCION,\n" + 
				"	RSU.CVE_USU_AUTENTICACION,\n" + 
				"	RSU.CVE_USUARIO,\n" + 
				"	RSU.CVE_USUARIO_PADRE,\n" + 
				"	RSU.F_ASIGNA_PSW,\n" + 
				"	RSU.F_PROX_CAMB_PSW,\n" + 
				"	RSU.F_VENCE_PSW,\n" + 
				"	RSU.FH_BAJA,\n" + 
				"	RSU.FH_REGISTRO,\n" + 
				"	RSU.FH_ULT_ACCESO,\n" + 
				"	RSU.FH_ULT_ACTUALIZACION,\n" + 
				"	RSU.ID_GRUPO,\n" + 
				"	RSU.NOM_ALIAS,\n" + 
				"	RSU.NUM_ERRORES_PSW,\n" + 
				"	RSU.NUM_LOG_INVALIDO,\n" + 
				"	RSU.PASSWORD,\n" + 
				"	RSU.PREG_RECUPERA_PSW,\n" + 
				"	RSU.RESP_RECUPERA_PSW,\n" + 
				"	RSU.SIT_USUARIO,\n" + 
				"	RSU.TOKEN,\n" + 
				"	(SELECT COUNT(1) FROM RUG_MAIL_POOL RMP WHERE RMP.DESTINATARIO = RSU.CVE_USUARIO AND RMP.ID_STATUS_MAIL = 1) AS CORREOS_NO_ENVIADOS,\n" + 
				"	(SELECT COUNT(1) FROM RUG_MAIL_POOL RMP WHERE RMP.DESTINATARIO = RSU.CVE_USUARIO AND RMP.ID_STATUS_MAIL = 3) AS CORREOS_ERROR\n" + 
				"FROM RUG_SECU_USUARIOS RSU, RUG_PERSONAS RP, RUG_PERSONAS_FISICAS RPF\n" + 
				"WHERE RSU.ID_PERSONA = RP.ID_PERSONA\n" +
				"AND RSU.ID_PERSONA = RPF.ID_PERSONA\n" +
				where +
				") SUB \n" + 
				additionalWhere;
		GenericCount count = usuarioDao.countNative(baseSql);
		return count.getTotal();
	}

	@Override
	public Long countUsuarios(String estado) {
		return usuarioDao.countByStatus(estado);
	}

	@Override
	public Long countAllUsuarios() {
		return usuarioDao.countAll();
	}

	@Override
	public Long countUsuariosNoMigracion() {
		return usuarioDao.countNotMigracion();
	}
	
	public List<SecuUsuarioStats> statsSecuUsuarios(String fechaInicio, String fechaFin, String cfecha, String fields, Integer diaInicioSemana, Boolean migracion) {
		int dia = 1;
		switch(diaInicioSemana) {
		case 7: // domingo
			break;
		case 1: // lunes
			dia = 6;
			break;
		case 2: // martes
			dia = 5;
			break;
		case 3: // miercoles
			dia = 4;
			break;
		case 4: // jueves
			dia = 3;
			break;
		case 5: // viernes
			dia = 2;
			break;
		case 6: // sabado
			dia = 1;
			break;
		}
		String projection = "";
	    String projectionGroup = "";
	    if (fields != null && !fields.isEmpty()) {
	        String[] fieldsParams = fields.split(",");
	        List<String> fieldsArray = Arrays.asList(fieldsParams);
	        if (fieldsArray.contains("cf")) {
	            projection += "str_fecha, ";
	            projectionGroup += "str_fecha, ";
	        } else {
	            projection += "null as str_fecha, ";
	        }
	        if (fieldsArray.contains("su")) {
	            projection += "descripcion, ";
	            projectionGroup += "descripcion, ";
	        } else {
	            projection += "null as descripcion, ";
	        }
	        projection = projection.substring(0, projection.length() - 2);
	        projectionGroup = projectionGroup.substring(0, projectionGroup.length() - 2);
	    }
	    
	    String periodoTiempo = "";
	    Period period = Period.ofDays(1);
	    TemporalUnit tu = ChronoUnit.DAYS;
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		DateTimeFormatter dbFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    DateTimeFormatter resultFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	    String format = "dd/MM/yyyy";
	    LocalDate inicio = LocalDate.parse(fechaInicio);
	    String fieldFecha = "";
	    String fieldFechaGroup = "str_fecha, ";
	    if (cfecha != null) {
	        if (cfecha.equals("D")) {
	            periodoTiempo = "YYYY-DDD";
	            dbFormatter = DateTimeFormatter.ofPattern("yyyy-D");
		    	fieldFecha = "TO_char(NVL(su.fh_ult_actualizacion, su.fh_registro), '" + periodoTiempo + "') as str_fecha, ";
		    	fieldFechaGroup = "TO_char(NVL(su.fh_ult_actualizacion, su.fh_registro), '" + periodoTiempo + "'), ";
	        } else if (cfecha.equals("S")) {
	            periodoTiempo = "YYYY-IW";
	            period = Period.ofWeeks(1);
	            tu = ChronoUnit.WEEKS;
	            TemporalField fieldISO = WeekFields.of(Locale.US).dayOfWeek();
	            inicio = inicio.with(fieldISO, 1);
	            format = "dd/MM/yyyy";
	            dbFormatter = DateTimeFormatter.ofPattern("yyyy-ww");
		    	fieldFecha = "TO_char(NVL(su.fh_ult_actualizacion, su.fh_registro) + " + dia + ", '" + periodoTiempo + "') as str_fecha, ";
		    	fieldFechaGroup = "TO_char(NVL(su.fh_ult_actualizacion, su.fh_registro) + " + dia + ", '" + periodoTiempo + "'), ";
	        } else if (cfecha.equals("M")) {
	            periodoTiempo = "YYYY-MM";
	            period = Period.ofMonths(1);
	            tu = ChronoUnit.MONTHS;
	            inicio = LocalDate.of(inicio.getYear(), inicio.getMonth(), 1);
	            dbFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	            format = "MM/yyyy";
		    	fieldFecha = "TO_char(NVL(su.fh_ult_actualizacion, su.fh_registro), '" + periodoTiempo + "')||'-01' as str_fecha, ";
		    	fieldFechaGroup = "TO_char(NVL(su.fh_ult_actualizacion, su.fh_registro), '" + periodoTiempo + "')||'-01', ";
	        } else {
	            periodoTiempo = "YYYY";
	            period = Period.ofYears(1);
	            tu = ChronoUnit.YEARS;
	            inicio = LocalDate.of(inicio.getYear(), 1, 1);
	            dbFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	            format = "yyyy";
	            fieldFecha = "TO_char(NVL(su.fh_ult_actualizacion, su.fh_registro), '" + periodoTiempo + "')||'-01-01' as str_fecha, ";
		    	fieldFechaGroup = "TO_char(NVL(su.fh_ult_actualizacion, su.fh_registro), '" + periodoTiempo + "')||'-01-01', ";
	        }
	        resultFormatter = DateTimeFormatter.ofPattern(format);
	    }
	    if(periodoTiempo.isEmpty()) {
	        fieldFecha = "null as str_fecha, ";
	    }
	    String additionalWhere = "";
	    if (migracion == null || !migracion) {
	    	additionalWhere += "AND su.cve_usuario not like '%correo%'\n" + 
    			"AND su.cve_institucion <> 'RGM'\n";
	    }
	    
		String baseSql = "SELECT\n" +
				fieldFecha + 
				"su.sit_usuario AS descripcion,\n" + 
				"DECODE(su.cve_usuario_padre, NULL, 1, 0),\n" + 
				"p.per_juridica AS subdescripcion,\n" + 
				"p.inscrito,\n" + 
				"COUNT(1) AS total\n" + 
				"FROM rug_secu_usuarios su, rug_personas p\n" + 
				"WHERE su.id_persona = p.id_persona\n" + 
				"AND NVL(su.fh_ult_actualizacion, su.fh_registro) BETWEEN TO_DATE('" + fechaInicio + " 00:00:00', 'YYYY-MM-DD HH24:MI:SS') and TO_DATE('" + fechaFin + " 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n" +
				additionalWhere + 
				"GROUP BY " + fieldFechaGroup + "su.sit_usuario, DECODE(su.cve_usuario_padre, NULL, 1, 0), p.per_juridica, p.inscrito";
	    
		String groupSql = "";
	    String endGroupSql = "";
	    if (!projection.isEmpty()) {
	        groupSql = "SELECT ROWNUM AS id, sub.* FROM (SELECT " + projection + ", SUM(total) AS total FROM (";
	        endGroupSql = ") "
	                + "GROUP BY " + projectionGroup + ") sub "
	                + "ORDER BY str_fecha, descripcion";
	    }
	    
		List<GenericCount> stats = usuarioDao.statsUsuarios(groupSql + baseSql + endGroupSql);
		List<SecuUsuarioStats> completeResults = new ArrayList<>();
		if(stats != null && stats.size() > 0) {
			if(cfecha != null && cfecha.equals("S")) {
			    inicio = LocalDate.parse(stats.get(0).getStrFecha(), 
			    		new DateTimeFormatterBuilder().appendPattern("YYYY-ww")
			    	    .parseDefaulting(WeekFields.ISO.dayOfWeek(), diaInicioSemana)
			    	    .toFormatter());
			} else {
			    inicio = LocalDate.parse(stats.get(0).getStrFecha(), dbFormatter);
			}
			
		    LocalDate fin = LocalDate.parse(fechaFin).plus(Period.ofDays(1));
		    LocalDate actual = inicio;

		    while (actual.isBefore(fin)) {
		    	SecuUsuarioStats empty = new SecuUsuarioStats();
	            
	            GenericCount emptyCount = new GenericCount();
	            emptyCount.setStrFecha(actual.format(dbFormatter));
	            if(!stats.contains(emptyCount)) {
	                empty.setFecha(actual.format(formatter));
	                empty.setLabelFecha(actual.format(resultFormatter));
		            
	                completeResults.add(empty);
	            }
	            actual = actual.plus(period);
	        }
		    
		    Map<String, SecuUsuarioStats> mapStats = new HashMap<>();
		    for(GenericCount result : stats) {
		    	SecuUsuarioStats newStat = new SecuUsuarioStats();
	            if(cfecha != null && cfecha.equals("S")) {
	            	newStat.setFecha(LocalDate.parse(result.getStrFecha(), 
				    		new DateTimeFormatterBuilder().appendPattern("YYYY-ww")
				    	    .parseDefaulting(WeekFields.ISO.dayOfWeek(), diaInicioSemana)
				    	    .toFormatter()).format(formatter));
	            	newStat.setLabelFecha(LocalDate.parse(result.getStrFecha(), 
				    		new DateTimeFormatterBuilder().appendPattern("YYYY-ww")
				    	    .parseDefaulting(WeekFields.ISO.dayOfWeek(), diaInicioSemana)
				    	    .toFormatter()).format(resultFormatter));
				} else {
					newStat.setFecha(LocalDate.parse(result.getStrFecha(), dbFormatter).format(formatter));
					newStat.setLabelFecha(LocalDate.parse(result.getStrFecha(), dbFormatter).format(resultFormatter));
				}
	            if(mapStats.containsKey(newStat.getFecha())) {
	            	newStat = mapStats.get(newStat.getFecha());
	            }
	            String estado = result.getDescripcion();
	            switch(estado) {
	            case "AC":
	            	newStat.setActivos(newStat.getActivos() + result.getTotal());
	            	break;
	            case "IN":
	            	newStat.setPendientesActivacion(newStat.getPendientesActivacion() + result.getTotal());
	            	break;
	            case "PA":
	            	newStat.setPendientesAprobacion(newStat.getPendientesAprobacion() + result.getTotal());
	            	break;
	            case "RE":
	            	newStat.setRechazados(newStat.getRechazados() + result.getTotal());
	            	break;
	            }
	            mapStats.put(newStat.getFecha(), newStat);
	        }
		    
		    for(String key : mapStats.keySet()) {
		    	completeResults.add(mapStats.get(key));
		    }
		    
		    Collections.sort(completeResults, new Comparator<SecuUsuarioStats>() {
	            public int compare(SecuUsuarioStats us1, SecuUsuarioStats us2) {
	                return LocalDate.parse(us1.getFecha(), formatter).isBefore(LocalDate.parse(us2.getFecha(), formatter)) ? -1 : 1;
	            }
	        });
		    
		    long activosAnt = 0;
			long pendientesActivacionAnt = 0;
			long pendientesAprobacionAnt = 0;
			long rechazadosAnt = 0;
		    for(SecuUsuarioStats usuario : completeResults) {
		    	if(activosAnt > 0) {
		    		usuario.setVariacionActivos(Precision.round(usuario.getActivos() / (double) activosAnt * 100, 2));
		    	}
		    	if(pendientesActivacionAnt > 0) {
		    		usuario.setVariacionPendientesActivacion(Precision.round(usuario.getPendientesActivacion() / (double) pendientesActivacionAnt * 100, 2));
		    	}
		    	if(pendientesAprobacionAnt > 0) {
		    		usuario.setVariacionPendientesAprobacion(Precision.round(usuario.getPendientesAprobacion() / (double) pendientesAprobacionAnt * 100, 2));
		    	}
		    	if(rechazadosAnt > 0) {
		    		usuario.setVariacionRechazados(Precision.round(usuario.getRechazados() / (double) rechazadosAnt * 100, 2));
		    	}

			    activosAnt += usuario.getActivos();
				pendientesActivacionAnt += usuario.getPendientesActivacion();
				pendientesAprobacionAnt += usuario.getPendientesAprobacion();
				rechazadosAnt += usuario.getRechazados();
		    }
		}
		
		return completeResults;
	}
	
	public Double getSaldo(Long idPersona) {
		// obtener cuenta principal
		RugSecuUsuario usuario = getUsuario(idPersona);
		Long cuentaPrincipal = idPersona;
		if(usuario == null) {
			return -1.0;
		}
		
		String sqlQuery = "SELECT RUG.FN_CALCULAR_SALDO(" + idPersona + ") FROM dual";
		
		// obtener saldo de cuenta principal
		List saldo = usuarioDao.findNative(sqlQuery, null, null, null);
		
		if(saldo == null || saldo.isEmpty() || saldo.get(0) == null) {
			return 0.0;
		}
		
		return ((BigDecimal) saldo.get(0)).doubleValue();
	}
	
	public Boolean modificarMigrado(Long idPersona, String causa, Long usuario) {
	    // llamar MODIFICAR_USUARIO_MIGRADO (pEmailMigrado IN VARCHAR2, pCausa IN VARCHAR2, pUsuario IN NUMBER)
		Map<Integer, Object> params = new HashMap<>();
		params.put(1,  idPersona);
		params.put(2, causa);
		params.put(3,  usuario);
		
		usuarioDao.callProcedure("MODIFICAR_USUARIO_MIGRADO", params);
		
		RugSecuUsuario usuarioMod = usuarioDao.findByIdRefresh(idPersona);
		
		return usuarioMod.getPersona().getRfc().endsWith("_MOD");
	}

	public Boolean modificarCorreo(Long idPersona, String nuevoCorreo, Long usuario) {
		// llamar MODIFICAR_CORREO (pIdPersona IN NUMBER, pNuevoCorreo IN VARCHAR2, pUsuario IN NUMBER)
		Map<Integer, Object> params = new HashMap<>();
		params.put(1, idPersona);
		params.put(2, nuevoCorreo);
		params.put(3, usuario);
		usuarioDao.callProcedure("MODIFICAR_CORREO", params);
		
		RugSecuUsuario usuarioMod = usuarioDao.findByIdRefresh(idPersona);
		
		return usuarioMod.getCveUsuario().equals(nuevoCorreo);
	}
}
