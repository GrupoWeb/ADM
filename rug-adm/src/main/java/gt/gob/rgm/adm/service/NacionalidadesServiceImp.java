package gt.gob.rgm.adm.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import gt.gob.rgm.adm.dao.RugCatNacionalidadesRepository;
import gt.gob.rgm.adm.model.RugCatNacionalidades;

@Stateless
public class NacionalidadesServiceImp {
	@Inject
	RugCatNacionalidadesRepository nacionalidadesDao;
	
	public List<RugCatNacionalidades> getNacionalidadesByStatus(String status) {
		return nacionalidadesDao.findByStatus(status);
	}
}
