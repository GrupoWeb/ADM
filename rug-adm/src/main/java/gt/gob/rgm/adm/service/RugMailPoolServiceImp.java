package gt.gob.rgm.adm.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import gt.gob.rgm.adm.dao.RugMailPoolRepository;
import gt.gob.rgm.adm.model.RugMailPool;

@Stateless
public class RugMailPoolServiceImp implements RugMailPoolService {
	
	@Inject
	private RugMailPoolRepository mailPoolDao;

	@Override
	public RugMailPool updateEstado(Long id, Long estado) {
		RugMailPool mailEncontrado = mailPoolDao.findById(id);
		if(mailEncontrado != null) {
			mailEncontrado.setIdStatusMail(estado);
			mailPoolDao.save(mailEncontrado);
			return mailEncontrado;
		} else {
			// mail no encontrado
			return null;
		}
	}
	
	@Override
	public List<RugMailPool> listMailUsuario(String emailUsuario, Long estado) {
		return mailPoolDao.findByEmailAndStatus(emailUsuario, estado);
	}

	@Override
	public Long countMailUsuario(String emailUsuario, Long estado) {
		return mailPoolDao.countByEmailAndStatus(emailUsuario, estado);
	}

	@Override
	public RugMailPool find(Long id) {
		RugMailPool mailEncontrado = mailPoolDao.findById(id);
		return mailEncontrado;
	}
}
