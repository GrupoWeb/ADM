package gt.gob.rgm.adm.service;

import java.io.IOException;
import java.util.List;

import gt.gob.rgm.adm.domain.Attachment;
import gt.gob.rgm.adm.exception.EntityAlreadyExistsException;

public interface AdjuntoService {

	List<Attachment> findByIncidente(Long incidenteId);
	
	Attachment save(Attachment adjunto) throws EntityAlreadyExistsException, IOException;
	
	Attachment getAdjunto(Long id);
}
