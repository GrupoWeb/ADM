package gt.gob.rgm.service;

import java.util.List;

import gt.gob.rgm.model.Boleta;

public interface BoletaService {
	void add(Boleta boleta);
	
    List<Boleta> listBoletas();
    
    void update(Long id, Integer estado);
}
