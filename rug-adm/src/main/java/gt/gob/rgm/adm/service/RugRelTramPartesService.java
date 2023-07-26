package gt.gob.rgm.adm.service;

import java.util.List;

import gt.gob.rgm.adm.model.RugRelTramPartes;

public interface RugRelTramPartesService {
	List<RugRelTramPartes> getPartes(RugRelTramPartes filter, Integer page, Integer size);
}
