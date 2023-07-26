package gt.gob.rgm.adm.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import gt.gob.rgm.adm.dao.RugPersonasRepository;
import gt.gob.rgm.adm.model.RugPersonas;

@Stateless
public class RugPersonasServiceImp implements RugPersonasService {
	
	@Inject
	private RugPersonasRepository personasDao;

	@Override
	public int updateRegistro(Long id, long codigoRegistro) {
		RugPersonas personaEncontrada = personasDao.findById(id);
		if(personaEncontrada != null) {
			personaEncontrada.setCodigoRegistro(codigoRegistro);
			personasDao.save(personaEncontrada);
			return 1;
		} else {
			// persona no encontrada
			return 0;
		}
	}

	@Override
	public RugPersonas getPersona(Long id) {
		return personasDao.findById(id);
	}

	@Override
	public long getNextRegistry(String perJuridica) {
		return personasDao.getNextRegistry(perJuridica);
	}
}
