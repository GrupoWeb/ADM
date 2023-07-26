package gt.gob.rgm.adm.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import gt.gob.rgm.adm.dao.RugPersonasHRepository;
import gt.gob.rgm.adm.model.RugPersonasH;
import gt.gob.rgm.adm.solr.PersonaSolr;

@Stateless
public class RugPersonasHServiceImp implements RugPersonasHService {
	
	@Inject
	private RugPersonasHRepository personaDao;

	@Override
	public List<PersonaSolr> listPersonas(RugPersonasH filter, Integer page, Integer size) {
		String where = "";
		if(filter.getId() != null) {
			where += "WHERE ph.id_persona = " + filter.getId().getIdPersona();
		}
		
		String baseSql = "SELECT DISTINCT\n" + 
				"	ph.id_persona,\n" + 
				"	NVL(ph.nombre_persona, ph.razon_social) AS nombre,\n" + 
				"	DECODE(ph.per_juridica, 'PF', 'INDIVIDUAL', 'PM', 'JURIDICA') AS tipo_persona,\n" + 
				"	ph.rfc,\n" + 
				"	NVL(ph.curp, ph.curp_doc) AS curp,\n" + 
				"	ph.e_mail\n" + 
				"FROM rug_personas_h ph\n" +
				where;
		
		List<PersonaSolr> personas = personaDao.findNative(baseSql, page, size);
		
		return personas;
	}

}
