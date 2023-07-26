package gt.gob.rgm.service;

import java.util.Optional;

import org.springframework.context.annotation.Scope;

import gt.gob.rgm.dao.RugCatTipoTramiteRepository;

import gt.gob.rgm.model.RugCatTipoTramite;

@Scope("prototype")
public class TiposTramiteServiceImp implements TiposTramiteService {
	private RugCatTipoTramiteRepository tipoTramiteDao;
	
	
	public RugCatTipoTramite getTipoTramite(Integer idTipoTramite) {
		Optional<RugCatTipoTramite> tipoTramite = tipoTramiteDao.findById(idTipoTramite);
		return tipoTramite.get();
	}

	public void setTipoTramiteDao(RugCatTipoTramiteRepository tipoTramiteDao) {
		this.tipoTramiteDao = tipoTramiteDao;
	}


}
