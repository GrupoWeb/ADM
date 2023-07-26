package gt.gob.rgm.adm.service;

import java.math.BigDecimal;
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

import gt.gob.rgm.adm.dao.BoletaRepository;
import gt.gob.rgm.adm.domain.BoletaStats;
import gt.gob.rgm.adm.domain.Deposit;
import gt.gob.rgm.adm.domain.SecuUsuarioStats;
import gt.gob.rgm.adm.model.Boleta;
import gt.gob.rgm.adm.model.BoletaSum;
import gt.gob.rgm.adm.model.GenericCount;


@Stateless
public class BoletaServiceImp implements BoletaService {
	
	@Inject
	private BoletaRepository boletaDao;

	@Override
	public int updateEstado(Long id, Integer estado) {
		Boleta boletaEncontrada = boletaDao.findById(id);
		if(boletaEncontrada != null) {
			boletaEncontrada.setUsada(estado);
			boletaDao.save(boletaEncontrada);
			return 1;
		} else {
			// boleta no encontrada
			return 0;
		}
	}
	
	@Override
	public int updateCausa(Long id, String causa) {
		Boleta boletaEncontrada = boletaDao.findById(id);
		if(boletaEncontrada != null) {
			boletaEncontrada.setCausa(causa);
			boletaDao.save(boletaEncontrada);
			return 1;
		} else {
			// boleta no encontrada
			return 0;
		}
	}

	@Override
	public List<Boleta> listBoletasStatus(Integer status) {
		return boletaDao.findByStatusOrderedByFecha(status);
	}
	
	public List<Boleta> listBoletas(Deposit filter, Integer page, Integer size) {
		return boletaDao.findWithFilter(filter, page, size);
	}

	@Override
	public List<Boleta> listBoletas() {
		return boletaDao.findAllOrderedByFecha();
	}
	
	@Override
	public Boleta getBoleta(Long id) {
		return boletaDao.findById(id);
	}

	public void setBoletaDao(BoletaRepository boletaDao) {
		this.boletaDao = boletaDao;
	}

	public BoletaRepository getBoletaDao() {
		return boletaDao;
	}

	@Override
	public List<GenericCount> sumBoletas(String fechaInicio, String fechaFin) {
		//return boletaDao.sumMontoByTipoAndUsada();
		
		String baseSql = "SELECT 	rownum AS id, sub.* FROM (\n" +
				"SELECT\n" +
	    		"	tipo_pago AS descripcion,\n" + 
	    		"	usada AS subdescripcion,\n" + 
	    		"	SUM(monto) AS total\n" + 
	    		"FROM rug_util.boleta\n" + 
	    		"WHERE fecha_hora BETWEEN TO_DATE('" + fechaInicio + " 00:00:00', 'YYYY-MM-DD HH24:MI:SS') and TO_DATE('" + fechaFin + " 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n" +
	    		"GROUP BY " +
	    		"	tipo_pago,\n" + 
	    		"	usada\n" +
	    		") sub";
		
		List<GenericCount> stats = boletaDao.statsBoletas(baseSql);
		
		return stats;
	}
	
	public Long countBoletas(Deposit filter) {
		return boletaDao.countWithFilter(filter);
	}
	
	public List<BoletaStats> statsBoletas(String fechaInicio, String fechaFin, String cfecha, String fields, Integer diaInicioSemana) {
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
	        if (fieldsArray.contains("eb")) {
	            projection += "descripcion, ";
	            projectionGroup += "descripcion, ";
	        } else {
	            projection += "null as descripcion, ";
	        }
	        if (fieldsArray.contains("tp")) {
	            projection += "subdescripcion, ";
	            projectionGroup += "subdescripcion, ";
	        } else {
	            projection += "null as subdescripcion, ";
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
		    	fieldFecha = "TO_char(fecha_hora, '" + periodoTiempo + "') as str_fecha, ";
		    	fieldFechaGroup = "TO_char(fecha_hora, '" + periodoTiempo + "'), ";
	        } else if (cfecha.equals("S")) {
	            periodoTiempo = "YYYY-IW";
	            period = Period.ofWeeks(1);
	            tu = ChronoUnit.WEEKS;
	            TemporalField fieldISO = WeekFields.of(Locale.US).dayOfWeek();
	            inicio = inicio.with(fieldISO, 1);
	            format = "dd/MM/yyyy";
	            dbFormatter = DateTimeFormatter.ofPattern("yyyy-ww");
		    	fieldFecha = "TO_char(fecha_hora + " + dia + ", '" + periodoTiempo + "') as str_fecha, ";
		    	fieldFechaGroup = "TO_char(fecha_hora + " + dia + ", '" + periodoTiempo + "'), ";
	        } else if (cfecha.equals("M")) {
	            periodoTiempo = "YYYY-MM";
	            period = Period.ofMonths(1);
	            tu = ChronoUnit.MONTHS;
	            inicio = LocalDate.of(inicio.getYear(), inicio.getMonth(), 1);
	            dbFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	            format = "MM/yyyy";
		    	fieldFecha = "TO_char(fecha_hora, '" + periodoTiempo + "')||'-01' as str_fecha, ";
		    	fieldFechaGroup = "TO_char(fecha_hora, '" + periodoTiempo + "')||'-01', ";
	        } else {
	            periodoTiempo = "YYYY";
	            period = Period.ofYears(1);
	            tu = ChronoUnit.YEARS;
	            inicio = LocalDate.of(inicio.getYear(), 1, 1);
	            dbFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	            format = "yyyy";
	            fieldFecha = "TO_char(fecha_hora, '" + periodoTiempo + "')||'-01-01' as str_fecha, ";
		    	fieldFechaGroup = "TO_char(fecha_hora, '" + periodoTiempo + "')||'-01-01', ";
	        }
	        resultFormatter = DateTimeFormatter.ofPattern(format);
	    }
	    if(periodoTiempo.isEmpty()) {
	        fieldFecha = "null as str_fecha, ";
	    }
	    
	    String baseSql = "SELECT\n" + 
	    		fieldFecha + 
	    		"	usada AS descripcion,\n" + 
	    		"	tipo_pago AS subdescripcion,\n" + 
	    		"	agencia,\n" + 
	    		"	id_persona_carga,\n" + 
	    		"	SUM(monto) AS total\n" + 
	    		"FROM rug_util.boleta\n" + 
	    		"WHERE fecha_hora BETWEEN TO_DATE('" + fechaInicio + " 00:00:00', 'YYYY-MM-DD HH24:MI:SS') and TO_DATE('" + fechaFin + " 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n" +
	    		"GROUP BY " + fieldFechaGroup +
	    		"	agencia,\n" + 
	    		"	monto,\n" + 
	    		"	usada,\n" + 
	    		"	tipo_pago,\n" + 
	    		"	id_persona_carga";
	    
	    String groupSql = "";
	    String endGroupSql = "";
	    if (!projection.isEmpty()) {
	        groupSql = "SELECT ROWNUM AS id, sub.* FROM (SELECT " + projection + ", SUM(total) AS total FROM (";
	        endGroupSql = ") "
	                + "GROUP BY " + projectionGroup + ") sub "
	                + "ORDER BY str_fecha, descripcion";
	    }
	    
	    List<GenericCount> stats = boletaDao.statsBoletas(groupSql + baseSql + endGroupSql);
	    List<BoletaStats> completeResults = new ArrayList<>();
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

		    while(actual.isBefore(fin)) {
		    	BoletaStats empty = new BoletaStats();
		    	
		    	GenericCount emptyCount = new GenericCount();
		    	emptyCount.setStrFecha(actual.format(dbFormatter));
		    	if(!stats.contains(emptyCount)) {
		    		empty.setFecha(actual.format(formatter));
		    		empty.setLabelFecha(actual.format(resultFormatter));
		    		
		    		completeResults.add(empty);
		    	}
		    	actual = actual.plus(period);
		    }
		    
		    Map<String, BoletaStats> mapStats = new HashMap<>();
		    for(GenericCount result : stats) {
		    	BoletaStats newStat = new BoletaStats();
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
	            case "0":
	            	newStat.setPendientesAprobacion(newStat.getPendientesAprobacion() + result.getTotal());
	            	break;
	            case "1":
	            	newStat.setAprobadas(newStat.getAprobadas() + result.getTotal());
	            	break;
	            case "-1":
	            	newStat.setRechazadas(newStat.getRechazadas() + result.getTotal());
	            	break;
	            }
	            mapStats.put(newStat.getFecha(), newStat);
		    }
		    
		    for(String key : mapStats.keySet()) {
		    	completeResults.add(mapStats.get(key));
		    }
		    
		    Collections.sort(completeResults, new Comparator<BoletaStats>() {
	            public int compare(BoletaStats bs1, BoletaStats bs2) {
	                return LocalDate.parse(bs1.getFecha(), formatter).isBefore(LocalDate.parse(bs2.getFecha(), formatter)) ? -1 : 1;
	            }
	        });
		    
		    long aprobadasAnt = 0;
			long pendientesAprobacionAnt = 0;
			long rechazadasAnt = 0;
			for(BoletaStats boleta : completeResults) {
				if(aprobadasAnt > 0) {
		    		boleta.setVariacionAprobadas(Precision.round(boleta.getAprobadas() / (double) aprobadasAnt * 100, 2));
		    	}
		    	if(pendientesAprobacionAnt > 0) {
		    		boleta.setVariacionPendientesAprobacion(Precision.round(boleta.getPendientesAprobacion() / (double) pendientesAprobacionAnt * 100, 2));
		    	}
		    	if(rechazadasAnt > 0) {
		    		boleta.setVariacionRechazadas(Precision.round(boleta.getRechazadas() / (double) rechazadasAnt * 100, 2));
		    	}

		    	aprobadasAnt += boleta.getAprobadas();
				pendientesAprobacionAnt += boleta.getPendientesAprobacion();
				rechazadasAnt += boleta.getRechazadas();
			}
	    }
	    
	    return completeResults;
	}
}
