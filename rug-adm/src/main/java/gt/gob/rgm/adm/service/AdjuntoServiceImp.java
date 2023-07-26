/**
 * 
 */
package gt.gob.rgm.adm.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.io.IOUtils;

import gt.gob.rgm.adm.dao.AdjuntoRepository;
import gt.gob.rgm.adm.domain.Attachment;
import gt.gob.rgm.adm.exception.EntityAlreadyExistsException;
import gt.gob.rgm.adm.model.Adjunto;
import gt.gob.rgm.adm.util.Constants;

/**
 * @author Raed
 *
 */
@Stateless
public class AdjuntoServiceImp implements AdjuntoService {

	@Inject
	private AdjuntoRepository adjuntoDao;
	
	@Inject
	RugParametroConfService parametroService;
	
	@Override
	public List<Attachment> findByIncidente(Long incidenteId) {
		List<Attachment> adjuntos = new ArrayList<>();
		List<Adjunto> adjuntosBD = adjuntoDao.findByIncidente(incidenteId);
		
		String hostUrl = parametroService.getParam(Constants.HOST_URL_ADM).getValorParametro();
		
		for(Adjunto adjuntoBD: adjuntosBD) {
			Attachment adjunto = new Attachment();
			
			adjunto.setAdjuntoId(adjuntoBD.getAdjuntoId());
			adjunto.setIncidenteId(adjuntoBD.getIncidenteId().longValue());
			adjunto.setTipo(adjuntoBD.getTipo()!=null?adjuntoBD.getTipo():"");
			adjunto.setDescripcion(adjuntoBD.getDescripcion());
			//adjunto.setAdjunto(new InputStream(adjuntoBD.getAdjunto()));
			adjunto.setUrl(hostUrl + "attachmenti/" + adjuntoBD.getAdjuntoId());
			
			adjuntos.add(adjunto);
		}
		
		return adjuntos;
	}
	
	@Override
	public Attachment save(Attachment adjunto) throws EntityAlreadyExistsException, IOException {
		Adjunto adjuntoBD = new Adjunto();
		
		//adjuntoBD.setAdjuntoId(adjunto.getAdjuntoId());
		adjuntoBD.setIncidenteId(BigDecimal.valueOf(adjunto.getIncidenteId()));
		adjuntoBD.setTipo(adjunto.getTipo()!=null?adjunto.getTipo():"");
		adjuntoBD.setDescripcion(adjunto.getDescripcion()!=null?adjunto.getDescripcion():"");
		adjuntoBD.setAdjunto(IOUtils.toByteArray(adjunto.getAdjunto()));
		
		adjuntoDao.save(adjuntoBD);
		
		String hostUrl = parametroService.getParam(Constants.HOST_URL_ADM).getValorParametro();
		
		adjunto.setAdjuntoId(adjuntoBD.getAdjuntoId());
		adjunto.setUrl(hostUrl + "attachmenti/" + adjuntoBD.getAdjuntoId());
		return adjunto;
	}
	
	@Override
	public Attachment getAdjunto(Long id) {
		Adjunto adjuntoBD = adjuntoDao.findById(id);
		
		Attachment adjunto = new Attachment();
		
		adjunto.setAdjuntoId(adjuntoBD.getAdjuntoId());
		adjunto.setIncidenteId(adjuntoBD.getIncidenteId().longValue());
		adjunto.setTipo(adjuntoBD.getTipo()!=null?adjuntoBD.getTipo():"");
		adjunto.setDescripcion(adjuntoBD.getDescripcion()!=null?adjuntoBD.getDescripcion():"");
		//adjunto.setAdjunto(Arrays.toString(adjuntoBD.getAdjunto()));
		
		return adjunto;
	}

}
