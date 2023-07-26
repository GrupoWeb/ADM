package gt.gob.rgm.adm.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import gt.gob.rgm.adm.dao.RugConsultaRegistroRepository;
import gt.gob.rgm.adm.domain.QueryRegistry;
import gt.gob.rgm.adm.model.RugConsultaRegistro;

@Stateless
public class RugConsultaRegistroServiceImpl {
	
	 @Inject
	 private RugConsultaRegistroRepository consultaDao;
	 
	 public List<QueryRegistry> listConsultas(String solicitante, String fechaInicio, String fechaFin, Integer page, Integer size) {
		 List<QueryRegistry> queries = new ArrayList<>();
		 List<RugConsultaRegistro> consultas = consultaDao.findConsultas(solicitante, fechaInicio, fechaFin, page, size);
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 for(RugConsultaRegistro consulta : consultas) {
			 QueryRegistry query = new QueryRegistry();
			 query.setIdConsultaReg(consulta.getIdConsultaReg());
			 query.setIdPersona(consulta.getIdPersona().longValue());
			 query.setIdTipoTramite(consulta.getIdTipoTramite().intValue());
			 query.setNombrePersona(consulta.getUsuario().getPersonaFisica().getNombrePersona());
			 query.setFechaHora(sdf.format(consulta.getFechahora()));
			 queries.add(query);
		 }
		 return queries;
	 }
	 
	 public Long countConsultas(String solicitante, String fechaInicio, String fechaFin) {
		 return consultaDao.countConsultas(solicitante, fechaInicio, fechaFin);
	 }
}
