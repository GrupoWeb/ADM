package gt.gob.rgm.adm.service;

import java.util.List;

import gt.gob.rgm.adm.domain.BoletaStats;
import gt.gob.rgm.adm.domain.Deposit;
import gt.gob.rgm.adm.model.Boleta;
import gt.gob.rgm.adm.model.BoletaSum;
import gt.gob.rgm.adm.model.GenericCount;

public interface BoletaService {
	List<Boleta> listBoletas(Deposit filter, Integer page, Integer size);
	
    List<Boleta> listBoletasStatus(Integer status);
    
    List<Boleta> listBoletas();
    
    int updateEstado(Long id, Integer estado);
    
    int updateCausa(Long id, String causa);
    
    Boleta getBoleta(Long id);
    
    List<GenericCount> sumBoletas(String fechaInicio, String fechaFin);
    
    Long countBoletas(Deposit filter);
    
    List<BoletaStats> statsBoletas(String fechaInicio, String fechaFin, String cfecha, String fields, Integer diaInicioSemana);
}
