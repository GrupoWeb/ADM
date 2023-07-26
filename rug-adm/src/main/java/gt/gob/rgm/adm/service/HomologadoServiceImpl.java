package gt.gob.rgm.adm.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import gt.gob.rgm.adm.dao.HomologadoRepository;
import gt.gob.rgm.adm.dao.RugConsultaRegistroRepository;
import gt.gob.rgm.adm.domain.QueryRegistry;
import gt.gob.rgm.adm.domain.Vinculacion;
import gt.gob.rgm.adm.model.Homologado;
import gt.gob.rgm.adm.model.RugConsultaRegistro;

@Stateless
public class HomologadoServiceImpl {
	
	 @Inject
	 private HomologadoRepository homologadoDao;
	 
	 public List<Vinculacion> listVinculaciones(String solicitante, String fechaInicio, String fechaFin, Integer page, Integer size, String garantia) {
		 List<Vinculacion> vinculaciones = new ArrayList<>();
		 List<Homologado> homologados = homologadoDao.findVinculaciones(solicitante, fechaInicio, fechaFin, page, size,garantia);
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 for(Homologado homologado : homologados) {
			 Vinculacion vinculacion = new Vinculacion();
			 vinculacion.setCausa(homologado.getCausa());
			 vinculacion.setUsuario(homologado.getUsuarioId().longValue());
			 vinculacion.setHomologadoId(homologado.getHomologadoId());
			 vinculacion.setIdGarantia(homologado.getIdGarantia().longValue());
			 vinculacion.setIdPersonaMigracion(homologado.getIdPersonaMigracion().longValue());
			 vinculacion.setIdPersonaRug(homologado.getIdPersonaRug().longValue());
			 vinculacion.setFecha(sdf.format(homologado.getFecha()));
			 vinculacion.setOperadaPor(homologado.getOperadaPor().getNombre());
			 vinculacion.setSolicitanteOriginal(homologado.getSolicitanteOriginal().getCveUsuario());
			 vinculacion.setSolicitanteNuevo(homologado.getSolicitanteNuevo().getCveUsuario());
			 vinculaciones.add(vinculacion);
		 }
		 return vinculaciones;
	 }
	 
	 public Long countVinculaciones(String solicitante, String fechaInicio, String fechaFin, String garantia) {
		 return homologadoDao.countVinculaciones(solicitante, fechaInicio, fechaFin,garantia);
	 }

   
}
