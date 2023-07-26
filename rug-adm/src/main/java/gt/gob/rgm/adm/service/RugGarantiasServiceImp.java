package gt.gob.rgm.adm.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
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

import org.apache.commons.math3.util.Precision;

import gt.gob.rgm.adm.dao.RugGarantiasBienesRepository;
import gt.gob.rgm.adm.dao.RugGarantiasRepository;
import gt.gob.rgm.adm.domain.GarantiaStats;
import gt.gob.rgm.adm.domain.Guarantee;
import gt.gob.rgm.adm.domain.Transaction;
import gt.gob.rgm.adm.model.GenericCount;
import gt.gob.rgm.adm.model.RugGarantias;
import gt.gob.rgm.adm.model.RugGarantiasBienes;
import gt.gob.rgm.adm.model.Tramites;

@Stateless
public class RugGarantiasServiceImp implements RugGarantiasService {
	
	@Inject
	private RugGarantiasRepository garantiasDao;
	
	@Inject
	private RugGarantiasBienesRepository garantiasBienesDao;
	
	public RugGarantias getGarantia(Long idGarantia) {
		return garantiasDao.findById(idGarantia);
	}

	@Override
	public List<Tramites> listGarantias(Transaction filter, Integer page, Integer size, String fechaInicio, String fechaFin) {
		return garantiasDao.findWithFilter(filter, page, size, fechaInicio, fechaFin);
	}

	public Long countGarantias(Transaction filter, String fechaInicio, String fechaFin) {
		return garantiasDao.countWithFilter(filter, fechaInicio, fechaFin);
	}
	
	@Override
	public List<GenericCount> countGarantias(String fechaInicio, String fechaFin, Boolean migracion) {
		//return garantiasDao.countGarantiasByTramite(fechaInicio, fechaFin, migracion);
	    
	    String additionalWhere = "";
	    if (migracion == null || !migracion) {
	    	additionalWhere += "AND t.b_carga_masiva != -1\n";
	    }
	    
		String baseSql = "SELECT\n" +
				"t.id_tipo_tramite as id,\n" +
				"ctt.descripcion,\n" + 
				"count(1) AS total\n" + 
				"FROM tramites t, rug_cat_tipo_tramite ctt, status_tramite st, rug_bitac_tramites bt, rug_rel_tram_garan rtg\n" + 
				"WHERE t.id_tipo_tramite = ctt.id_tipo_tramite\n" + 
				"AND t.id_status_tram = st.id_status_tram\n" + 
				"AND t.id_tramite_temp = bt.id_tramite_temp\n" + 
				"AND t.id_tramite = rtg.id_tramite\n" + 
				"AND bt.id_status = 3\n" + 
				"AND t.status_reg = 'AC'\n" + 
				"AND rtg.status_reg = 'AC'\n" + 
				"AND t.fecha_creacion BETWEEN TO_DATE('" + fechaInicio + " 00:00:00', 'YYYY-MM-DD HH24:MI:SS') and TO_DATE('" + fechaFin + " 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n" +
				additionalWhere + 
				"GROUP BY t.id_tipo_tramite, ctt.descripcion\n" +
				"UNION\n" + 
				"SELECT\n" + 
				"5, \n" + 
				"'Certificación' AS DESCRIPCION,\n" + 
				"COUNT(1)\n" + 
				"FROM RUG_CERTIFICACIONES\n" + 
				"WHERE PRECIO IS NOT NULL\n" + 
				"AND ID_PERSONA_LOGIN IS NOT NULL\n" + 
				"AND FECHA_CERT BETWEEN TO_DATE('" + fechaInicio + " 00:00:00', 'YYYY-MM-DD HH24:MI:SS') and TO_DATE('" + fechaFin + " 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n" +
				"UNION\n" + 
				"SELECT\n" + 
				"11, \n" + 
				"'Consulta' AS DESCRIPCION, \n" + 
				"COUNT(1)\n" + 
				"FROM RUG_CONSULTA_REGISTRO CR, RUG_CAT_TIPO_TRAMITE TT\n" + 
				"WHERE TT.ID_TIPO_TRAMITE = CR.ID_TIPO_TRAMITE\n" + 
				"AND CR.FECHAHORA BETWEEN TO_DATE('" + fechaInicio + " 00:00:00', 'YYYY-MM-DD HH24:MI:SS') and TO_DATE('" + fechaFin + " 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n" +
				"UNION\n" +
				"SELECT\n" +
				"77,\n" +
				"'Vinculación' AS descripcion,\n" +
				"count(1)\n" +
				"FROM homologado h\n" +
				"WHERE id_garantia IS NOT NULL\n" +
				"AND h.FECHA BETWEEN TO_DATE('" + fechaInicio + " 00:00:00', 'YYYY-MM-DD HH24:MI:SS') and TO_DATE('" + fechaFin + " 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n" +
				"ORDER BY 2";
	    
		List<GenericCount> stats = garantiasDao.statsGarantias(baseSql);
		
		return stats;
	}
        
        public String original(Long idGarantia) {
		//return garantiasDao.countGarantiasByTramiteAndPerJuridica(fechaInicio, fechaFin);
		
		String additionalWhere = "";
	     
	    
		String baseSql = "SELECT rp.NOMBRE_PERSONA \n" +
                    "FROM HOMOLOGADO h INNER JOIN RUG_PERSONAS_FISICAS rp ON rp.ID_PERSONA  = h.ID_PERSONA_MIGRACION \n" +
                    "WHERE ID_GARANTIA = " + String.valueOf(idGarantia) +"\n" +
                    "AND HOMOLOGADO_ID = (SELECT min(HOMOLOGADO_ID) FROM HOMOLOGADO h2 WHERE h2.ID_GARANTIA = h.ID_GARANTIA) ";
	    
		String original = garantiasDao.original(baseSql);
		if (original.length()> 0)
                    return original;
                else 
                    return "";
	}
	
        
	
	public List<GenericCount> countGarantiasByTipoPersona(String fechaInicio, String fechaFin, Boolean migracion) {
		//return garantiasDao.countGarantiasByTramiteAndPerJuridica(fechaInicio, fechaFin);
		
		String additionalWhere = "";
	    if (migracion == null || !migracion) {
	    	additionalWhere += "AND t.b_carga_masiva != -1\n";
	    }
	    
		String baseSql = "SELECT rownum AS id, sub.* FROM (SELECT\n" +
				"t.id_tipo_tramite as id,\n" +
				"ctt.descripcion,\n" + 
				"rtp.per_juridica AS subdescripcion,\n" +
				"count(1) AS total\n" + 
				"FROM tramites t, rug_cat_tipo_tramite ctt, status_tramite st, rug_bitac_tramites bt, rug_rel_tram_garan rtg, rug_rel_tram_partes rtp\n" + 
				"WHERE t.id_tipo_tramite = ctt.id_tipo_tramite\n" + 
				"AND t.id_status_tram = st.id_status_tram\n" + 
				"AND t.id_tramite_temp = bt.id_tramite_temp\n" + 
				"AND t.id_tramite = rtg.id_tramite\n" +
				"AND t.id_tramite = rtp.id_tramite\n" +
				"AND bt.id_status = 3\n" + 
				"AND t.status_reg = 'AC'\n" + 
				"AND rtg.status_reg = 'AC'\n" + 
				"AND rtp.id_parte = 2\n" +
				"AND t.fecha_creacion BETWEEN TO_DATE('" + fechaInicio + " 00:00:00', 'YYYY-MM-DD HH24:MI:SS') and TO_DATE('" + fechaFin + " 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n" +
				additionalWhere + 
				"GROUP BY t.id_tipo_tramite, ctt.descripcion, rtp.per_juridica\n" +
				") sub ORDER BY 2, 3";
	    
		List<GenericCount> stats = garantiasDao.statsGarantias(baseSql);
		
		return stats;
	}
	
	public List<GenericCount> listTipoPersona(String fechaInicio, String fechaFin, Boolean migracion) {
		String additionalWhere = "";
	    if (migracion == null || !migracion) {
	    	additionalWhere += "AND t.b_carga_masiva != -1\n";
	    }
	    
		String baseSql = "SELECT rownum AS id, sub.descripcion, sub.subdescripcion, sub.total FROM (\n" + 
				"	SELECT nombre AS descripcion, id_tipo_tramite, '('||decode(per_juridica, 'PF', 'INDIVIDUAL', 'PM', 'JURIDICA')||') '||descripcion AS subdescripcion, per_juridica, COUNT(1) AS total \n" + 
				"	FROM (\n" + 
				"		SELECT\n" + 
				"			NVL(ph.nombre_persona, ph.razon_social) AS nombre,\n" + 
				"			t.id_tipo_tramite,\n" + 
				"			ctt.descripcion, \n" + 
				"			rtp.per_juridica\n" + 
				"		FROM tramites t, rug_cat_tipo_tramite ctt, status_tramite st, rug_bitac_tramites bt, rug_rel_tram_garan rtg, rug_rel_tram_partes rtp, rug_personas_h ph \n" + 
				"		WHERE t.id_tipo_tramite = ctt.id_tipo_tramite \n" + 
				"		AND t.id_status_tram = st.id_status_tram \n" + 
				"		AND t.id_tramite_temp = bt.id_tramite_temp \n" + 
				"		AND t.id_tramite = rtg.id_tramite\n" + 
				"		AND t.id_tramite = rtp.id_tramite\n" + 
				"		AND rtp.id_persona = ph.id_persona\n" + 
				"		AND t.id_tramite = ph.id_tramite\n" + 
				"		AND bt.id_status = 3 \n" + 
				"		AND t.status_reg = 'AC' \n" + 
				"		AND rtg.status_reg = 'AC' \n" + 
				"		AND rtp.id_parte = 2\n" + 
				"		AND t.fecha_creacion BETWEEN TO_DATE('" + fechaInicio + " 00:00:00', 'YYYY-MM-DD HH24:MI:SS') and TO_DATE('" + fechaFin + " 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n" +
				additionalWhere +
				"	)\n" + 
				"	GROUP BY nombre, id_tipo_tramite, descripcion, per_juridica\n" + 
				"	ORDER BY 1, 2\n" + 
				") sub";
				
		List<GenericCount> stats = garantiasDao.statsGarantias(baseSql);
		
		return stats;
	}
	
	public List<GarantiaStats> statsGarantias(String fechaInicio, String fechaFin, String cfecha, String fields, Integer diaInicioSemana, Boolean migracion) {
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
	    String projectionGroup = "rownum, ";
	    if (fields != null && !fields.isEmpty()) {
	        String[] fieldsParams = fields.split(",");
	        List<String> fieldsArray = Arrays.asList(fieldsParams);
	        if (fieldsArray.contains("cf")) {
	            projection += "str_fecha, ";
	            projectionGroup += "str_fecha, ";
	        } else {
	            projection += "null as str_fecha, ";
	        }
	        if (fieldsArray.contains("tt")) {
	            projection += "subdescripcion, descripcion, ";
	            projectionGroup += "subdescripcion, descripcion, ";
	        } else {
	            projection += "null as subdescripcion, null as descripcion, ";
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
		    	fieldFecha = "TO_char(t.fecha_creacion, '" + periodoTiempo + "') as str_fecha, ";
		    	fieldFechaGroup = "TO_char(t.fecha_creacion, '" + periodoTiempo + "'), ";
	        } else if (cfecha.equals("S")) {
	            periodoTiempo = "YYYY-IW";
	            period = Period.ofWeeks(1);
	            tu = ChronoUnit.WEEKS;
	            TemporalField fieldISO = WeekFields.of(Locale.US).dayOfWeek();
	            inicio = inicio.with(fieldISO, 1);
	            format = "dd/MM/yyyy";
	            dbFormatter = DateTimeFormatter.ofPattern("yyyy-ww");
		    	fieldFecha = "TO_char(t.fecha_creacion + " + dia + ", '" + periodoTiempo + "') as str_fecha, ";
		    	fieldFechaGroup = "TO_char(t.fecha_creacion + " + dia + ", '" + periodoTiempo + "'), ";
	        } else if (cfecha.equals("M")) {
	            periodoTiempo = "YYYY-MM";
	            period = Period.ofMonths(1);
	            tu = ChronoUnit.MONTHS;
	            inicio = LocalDate.of(inicio.getYear(), inicio.getMonth(), 1);
	            dbFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	            format = "MM/yyyy";
		    	fieldFecha = "TO_char(t.fecha_creacion, '" + periodoTiempo + "')||'-01' as str_fecha, ";
		    	fieldFechaGroup = "TO_char(t.fecha_creacion, '" + periodoTiempo + "')||'-01', ";
	        } else {
	            periodoTiempo = "YYYY";
	            period = Period.ofYears(1);
	            tu = ChronoUnit.YEARS;
	            inicio = LocalDate.of(inicio.getYear(), 1, 1);
	            dbFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	            format = "yyyy";
	            fieldFecha = "TO_char(t.fecha_creacion, '" + periodoTiempo + "')||'-01-01' as str_fecha, ";
		    	fieldFechaGroup = "TO_char(t.fecha_creacion, '" + periodoTiempo + "')||'-01-01', ";
	        }
	        resultFormatter = DateTimeFormatter.ofPattern(format);
	    }
	    String fieldFechaGroupTwo = "";
	    String fieldFechaGroupThree = "";
	    String fieldFechaGroupFour = "";
	    if (!periodoTiempo.isEmpty()) {
	    	fieldFechaGroupTwo = fieldFechaGroup.replace("t.fecha_creacion", "FECHA_CERT");
	    	fieldFechaGroupTwo = fieldFechaGroupTwo.substring(0, fieldFechaGroupTwo.length() - 2);
	    	fieldFechaGroupThree = fieldFechaGroup.replace("t.fecha_creacion", "CR.FECHAHORA");
	    	fieldFechaGroupThree = fieldFechaGroupThree.substring(0, fieldFechaGroupThree.length() - 2);
	    	fieldFechaGroupFour = fieldFechaGroup.replace("t.fecha_creacion", "h.FECHA");
	    	fieldFechaGroupFour = fieldFechaGroupFour.substring(0, fieldFechaGroupFour.length() - 2);
	    } else {
	        fieldFecha = "null as str_fecha, ";
	    }
	    String additionalWhere = "";
	    if (migracion == null || !migracion) {
	    	additionalWhere += "AND t.b_carga_masiva != -1\n";
	    }
	    
		String baseSql = "SELECT\n" +
				fieldFecha + 
				"t.id_tipo_tramite as subdescripcion,\n" +
				"ctt.descripcion,\n" + 
				"count(1) AS total\n" + 
				"FROM tramites t, rug_cat_tipo_tramite ctt, status_tramite st, rug_bitac_tramites bt, rug_rel_tram_garan rtg\n" + 
				"WHERE t.id_tipo_tramite = ctt.id_tipo_tramite\n" + 
				"AND t.id_status_tram = st.id_status_tram\n" + 
				"AND t.id_tramite_temp = bt.id_tramite_temp\n" + 
				"AND t.id_tramite = rtg.id_tramite\n" + 
				"AND bt.id_status = 3\n" + 
				"AND t.status_reg = 'AC'\n" + 
				"AND rtg.status_reg = 'AC'\n" + 
				"AND t.fecha_creacion BETWEEN TO_DATE('" + fechaInicio + " 00:00:00', 'YYYY-MM-DD HH24:MI:SS') and TO_DATE('" + fechaFin + " 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n" +
				additionalWhere + 
				"GROUP BY " + fieldFechaGroup + "t.id_tipo_tramite, ctt.descripcion\n" +
				"UNION\n" + 
				"SELECT\n" + 
				fieldFecha.replace("t.fecha_creacion", "FECHA_CERT") + "\n" +
				"5, \n" + 
				"'Certificación' AS DESCRIPCION,\n" + 
				"COUNT(1)\n" + 
				"FROM RUG_CERTIFICACIONES\n" + 
				"WHERE PRECIO IS NOT NULL\n" + 
				"AND ID_PERSONA_LOGIN IS NOT NULL\n" + 
				"AND FECHA_CERT BETWEEN TO_DATE('" + fechaInicio + " 00:00:00', 'YYYY-MM-DD HH24:MI:SS') and TO_DATE('" + fechaFin + " 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n" +
				"GROUP BY " + fieldFechaGroupTwo + "\n" + 
				"UNION\n" + 
				"SELECT\n" + 
				fieldFecha.replace("t.fecha_creacion", "CR.FECHAHORA") + "\n" +
				"11, \n" + 
				"'Consulta' AS DESCRIPCION, \n" + 
				"COUNT(1)\n" + 
				"FROM RUG_CONSULTA_REGISTRO CR, RUG_CAT_TIPO_TRAMITE TT\n" + 
				"WHERE TT.ID_TIPO_TRAMITE = CR.ID_TIPO_TRAMITE\n" + 
				"AND CR.FECHAHORA BETWEEN TO_DATE('" + fechaInicio + " 00:00:00', 'YYYY-MM-DD HH24:MI:SS') and TO_DATE('" + fechaFin + " 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n" +
				"GROUP BY " + fieldFechaGroupThree + "\n" +
				"UNION\n" +
				"SELECT\n" +
				fieldFecha.replace("t.fecha_creacion", "h.FECHA") + "\n" +
				"77,\n" +
				"'Vinculación' AS descripcion,\n" +
				"count(1)\n" +
				"FROM homologado h\n" +
				"WHERE id_garantia IS NOT NULL\n" +
				"AND h.FECHA BETWEEN TO_DATE('" + fechaInicio + " 00:00:00', 'YYYY-MM-DD HH24:MI:SS') and TO_DATE('" + fechaFin + " 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n" +
				"GROUP BY " + fieldFechaGroupFour;
	    
		String groupSql = "";
	    String endGroupSql = "";
	    if (!projection.isEmpty()) {
	        groupSql = "select rownum as id, " + projection + ", sum(total) as total from (";
	        endGroupSql = ") "
	                + "GROUP BY " + projectionGroup + " "
	                + "ORDER BY str_fecha, subdescripcion, descripcion";
	    }
	    
		List<GenericCount> stats = garantiasDao.statsGarantias(groupSql + baseSql + endGroupSql);
		List<GarantiaStats> completeResults = new ArrayList<>();
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
	            GarantiaStats empty = new GarantiaStats();
	            
	            GenericCount emptyCount = new GenericCount();
	            emptyCount.setStrFecha(actual.format(dbFormatter));
	            if(!stats.contains(emptyCount)) {
	                empty.setFecha(actual.format(formatter));
	                empty.setLabelFecha(actual.format(resultFormatter));
		            
	                completeResults.add(empty);
	            }
	            actual = actual.plus(period);
	        }
		    
		    Map<String, GarantiaStats> mapStats = new HashMap<>();
		    for(GenericCount result : stats) {
	            GarantiaStats newStat = new GarantiaStats();
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
	            Integer tipoTramite = Integer.parseInt(result.getSubdescripcion());
	            switch(tipoTramite) {
	            case 1:
	            	newStat.setInscripciones(newStat.getInscripciones() + result.getTotal());
	            	break;
	            case 4:
	            	newStat.setCancelaciones(newStat.getCancelaciones() + result.getTotal());
	            	break;
	            case 5:
	            	newStat.setCertificaciones(newStat.getCertificaciones() + result.getTotal());
	            	break;
	            case 7:
	            	newStat.setModificaciones(newStat.getModificaciones() + result.getTotal());
	            	break;
	            case 9:
	            	newStat.setRenovaciones(newStat.getRenovaciones() + result.getTotal());
	            	break;
	            case 11:
	            	newStat.setConsultas(newStat.getConsultas() + result.getTotal());
	            	break;
	            case 30:
	            	newStat.setEjecuciones(newStat.getEjecuciones() + result.getTotal());
	            	break;
	            case 31:
	            	newStat.setTraslados(newStat.getTraslados() + result.getTotal());
	            	break;
	            case 77:
	            	newStat.setVinculaciones(newStat.getVinculaciones() + result.getTotal());
	            	break;
	            }
	            mapStats.put(newStat.getFecha(), newStat);
	        }
		    
		    for(String key : mapStats.keySet()) {
		    	completeResults.add(mapStats.get(key));
		    }
		    
		    Collections.sort(completeResults, new Comparator<GarantiaStats>() {
	            public int compare(GarantiaStats gs1, GarantiaStats gs2) {
	                return LocalDate.parse(gs1.getFecha(), formatter).isBefore(LocalDate.parse(gs2.getFecha(), formatter)) ? -1 : 1;
	            }
	        });
		    
		    long inscripcionesAnt = 0;
			long cancelacionesAnt = 0;
			long certificacionesAnt = 0;
			long modificacionesAnt = 0;
			long renovacionesAnt = 0;
			long consultasAnt = 0;
			long ejecucionesAnt = 0;
			long trasladosAnt = 0;
			long vinculacionesAnt = 0;
		    for(GarantiaStats garantia : completeResults) {
		    	if(inscripcionesAnt > 0) {
			    	garantia.setVariacionInscripciones(Precision.round(garantia.getInscripciones() / (double) inscripcionesAnt * 100, 2));
		    	}
		    	if(cancelacionesAnt > 0) {
			    	garantia.setVariacionCancelaciones(Precision.round(garantia.getCancelaciones() / (double) cancelacionesAnt * 100, 2));
		    	}
		    	if(certificacionesAnt > 0) {
			    	garantia.setVariacionCertificaciones(Precision.round(garantia.getCertificaciones() / (double) certificacionesAnt * 100, 2));
		    	}
		    	if(modificacionesAnt > 0) {
			    	garantia.setVariacionModificaciones(Precision.round(garantia.getModificaciones() / (double) modificacionesAnt * 100, 2));
		    	}
		    	if(renovacionesAnt > 0) {
			    	garantia.setVariacionRenovaciones(Precision.round(garantia.getRenovaciones() / (double) renovacionesAnt * 100, 2));
		    	}
		    	if(consultasAnt > 0) {
			    	garantia.setVariacionConsultas(Precision.round(garantia.getConsultas() / (double) consultasAnt * 100, 2));
		    	}
		    	if(ejecucionesAnt > 0) {
			    	garantia.setVariacionEjecuciones(Precision.round(garantia.getEjecuciones() / (double) ejecucionesAnt * 100, 2));
		    	}
		    	if(trasladosAnt > 0) {
			    	garantia.setVariacionTraslados(Precision.round(garantia.getTraslados() / (double) trasladosAnt * 100, 2));
		    	}
		    	if(vinculacionesAnt > 0) {
		    		garantia.setVariacionVinculaciones(Precision.round(garantia.getVinculaciones() / (double) vinculacionesAnt * 100, 2));
		    	}

			    inscripcionesAnt += garantia.getInscripciones();
				cancelacionesAnt += garantia.getCancelaciones();
				certificacionesAnt += garantia.getCertificaciones();
				modificacionesAnt += garantia.getModificaciones();
				renovacionesAnt += garantia.getRenovaciones();
				consultasAnt += garantia.getConsultas();
				ejecucionesAnt += garantia.getEjecuciones();
				trasladosAnt += garantia.getTraslados();
				vinculacionesAnt += garantia.getVinculaciones();
		    }
		}
		
		return completeResults;
	}
	
	public Boolean vincularGarantia(Long solicitante, Long idGarantia, String causa, Long usuario) {
		// llamar a procedimiento VINCULAR_GARANTIA
		Map<Integer, Object> params = new HashMap<>();
		params.put(1, solicitante);
		params.put(2, idGarantia);
		params.put(3, causa);
		params.put(4, usuario);
		
		garantiasDao.callProcedure("VINCULAR_GARANTIA", params);
		// verificar el solicitante de la garantia

		RugGarantias garantia = garantiasDao.findByIdRefresh(idGarantia);
				
		return garantia.getIdPersona().longValue() == solicitante.longValue();
	}
	
	public List<Guarantee> listGarantiasByDescripcionAndNotStatus(String descripcion, String status) {
		List<Guarantee> guarantees = new ArrayList<>();
		List<RugGarantias> garantias = new ArrayList<>();
		// buscar por garantias_bienes
		List<RugGarantiasBienes> garantiasBienes = garantiasBienesDao.findByIdentificador(descripcion);
		for(RugGarantiasBienes garantiaBienes : garantiasBienes) {
			Tramites tramite = garantiaBienes.getTramite();
			if(tramite != null && tramite.getRelGarantia() != null && !tramite.getRelGarantia().isEmpty()) {
				RugGarantias garantia = tramite.getRelGarantia().get(0).getGarantia();
				if(!garantia.getStatusReg().equals(status)) {
	    			garantias.add(garantia);
				}
			}
		}
		
		// buscar por garantias
		List<RugGarantias> garantiasDesc = garantiasDao.findByDescripcionAndStatus(descripcion, status);
		garantias.addAll(garantiasDesc);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(RugGarantias garantia : garantias) {
			Guarantee guarantee = new Guarantee();
    		guarantee.setIdGarantia(garantia.getIdGarantia());
    		guarantee.setDescGarantia(garantia.getDescGarantia());
    		guarantee.setEsPrioritaria(garantia.getEsPrioritaria());
    		if(garantia.getFechaFinGar() != null) {
        		guarantee.setFechaFinGar(formatter.format(garantia.getFechaFinGar()));
    		}
    		if(garantia.getFechaInscr() != null) {
        		guarantee.setFechaInscr(formatter.format(garantia.getFechaInscr()));
    		}
    		if(garantia.getFechaReg() != null) {
        		guarantee.setFechaReg(formatter.format(garantia.getFechaReg()));
    		}
    		guarantee.setFolioMercantil(garantia.getFolioMercantil());
    		guarantee.setGarantiaStatus(garantia.getGarantiaStatus());
    		guarantee.setIdTipoGarantia(garantia.getIdTipoGarantia());
    		guarantee.setInstrumentoPublico(garantia.getInstrumentoPublico());
    		guarantee.setMesesGarantia(garantia.getMesesGarantia());
    		guarantee.setNumGarantia(garantia.getNumGarantia());
    		guarantee.setOtrosTerminosGarantia(garantia.getOtrosTerminosGarantia());
    		guarantee.setStatusReg(garantia.getStatusReg());
    		guarantee.setTiposBienesMuebles(garantia.getTiposBienesMuebles());
    		guarantee.setTxtRegistros(garantia.getTxtRegistros());
    		guarantee.setValorBienes(garantia.getValorBienes());
    		guarantee.setVigencia(garantia.getVigencia());
    		
    		guarantees.add(guarantee);
		}
		
		return guarantees;
	}
}
