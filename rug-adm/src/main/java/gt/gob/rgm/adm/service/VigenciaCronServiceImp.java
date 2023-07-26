package gt.gob.rgm.adm.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

import gt.gob.rgm.adm.dao.VTramitesMailVigenciaRepository;
import gt.gob.rgm.adm.util.Constants;
import gt.gob.rgm.adm.util.MailUtils;

import gt.gob.rgm.adm.model.VTramitesMailVigencia;

@Singleton
public class VigenciaCronServiceImp {
	
	@Inject
	private VTramitesMailVigenciaRepository vTramitesMailVigenciaDao;
	
	@Inject
	RugParametroConfService parametroService;
	
	public VigenciaCronServiceImp() {
		
	}
	
	//@Schedule(minute="1", hour="6", dayOfWeek="*")
	public void enviarAvisoVencimiento() {
		System.out.println("-->JOB ENVIO AVISOS VENCIMIENTO");
		List<VTramitesMailVigencia> tramites = new ArrayList<VTramitesMailVigencia>();
		tramites = vTramitesMailVigenciaDao.getTramitesVigencia();
								
		for(VTramitesMailVigencia tramite : tramites) {
			try {								
				int idTipoMensaje = 1;
				int idAccountSmtp = Integer.valueOf(parametroService.getParam(Constants.ID_SMTP_MAIL_REGISTRO_USUARIOS).getValorParametro());
				String to = tramite.getUsuarioMail();
				String cc = null;
				String cco = null;
				String subject = "AVISO DE FIN DE VIGENCIA DE INSCRIPCION DE GARANTIAS MOBILIARIAS";
				String message = parametroService.getParam(Constants.MAIL_THEME_VENCIMIENTO).getValorParametro();
				message = message.replace("@cve_usuario", tramite.getAcreedor());
				message = message.replace("@id_garantia", tramite.getIdGarantia().toString());
				message = message.replace("@id_tramite", tramite.getIdTramite().toString());
				message = message.replace("@fecha_term", tramite.getFechaTerm());				
				
				MailUtils.getMailServiceInstance().sendMail(idTipoMensaje, idAccountSmtp, to, cc, cco, subject, message);
			} catch(Exception e) {
				// si el envio del correo falla, se captura la excepcion pero continua el proceso
		    	e.printStackTrace();
			}
		}
	}
	
	@Schedule(minute="1", hour="2", dayOfWeek="*")
	public void caducidadTramites() {
		System.out.println("-->JOB CADUCIDAD TRAMITES");
		vTramitesMailVigenciaDao.callCaducidadTramites();
	}
	
	@Schedule(minute="1", hour="5", dayOfWeek="*")
	public void caducidadTramitesInc() {
		System.out.println("-->JOB CADUCIDAD TRAMITES INCOMPLETOS");
		vTramitesMailVigenciaDao.callCaducidadTramitesInc();
	}

}
