package gt.gob.rgm.adm.service;

import java.util.List;

import gt.gob.rgm.adm.domain.GarantiaStats;
import gt.gob.rgm.adm.domain.Guarantee;
import gt.gob.rgm.adm.domain.Transaction;
import gt.gob.rgm.adm.model.GenericCount;
import gt.gob.rgm.adm.model.RugGarantias;
import gt.gob.rgm.adm.model.Tramites;

public interface RugGarantiasService {
	RugGarantias getGarantia(Long idGarantia);
	
	List<Tramites> listGarantias(Transaction filter, Integer page, Integer size, String fechaInicio, String fechaFin);
	
	Long countGarantias(Transaction filter, String fechaInicio, String fechaFin);
        
        String original(Long idGarantia);
	
	List<GenericCount> countGarantias(String fechaInicio, String fechaFin, Boolean migracion);
	
	List<GenericCount> countGarantiasByTipoPersona(String fechaInicio, String fechaFin, Boolean migracion);
	
	List<GenericCount> listTipoPersona(String fechaInicio, String fechaFin, Boolean migracion);
	
	List<GarantiaStats> statsGarantias(String fechaInicio, String fechaFin, String cfecha, String fields, Integer diaInicioSemana, Boolean migracion);
	
	Boolean vincularGarantia(Long solicitante, Long idGarantia, String causa, Long usuario);
	
	List<Guarantee> listGarantiasByDescripcionAndNotStatus(String descripcion, String status);
}
