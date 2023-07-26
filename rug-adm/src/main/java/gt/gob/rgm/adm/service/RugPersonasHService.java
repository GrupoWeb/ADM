package gt.gob.rgm.adm.service;

import java.util.List;

import gt.gob.rgm.adm.model.RugPersonasH;
import gt.gob.rgm.adm.solr.PersonaSolr;

public interface RugPersonasHService {
	List<PersonaSolr> listPersonas(RugPersonasH filter, Integer page, Integer size);
}
