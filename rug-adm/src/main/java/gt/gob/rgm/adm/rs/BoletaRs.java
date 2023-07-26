
package gt.gob.rgm.adm.rs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import gt.gob.rgm.adm.annotation.SecuredResource;
import gt.gob.rgm.adm.domain.BoletaStats;
import gt.gob.rgm.adm.domain.Deposit;
import gt.gob.rgm.adm.domain.ExternalUser;
import gt.gob.rgm.adm.domain.ResponseRs;
import gt.gob.rgm.adm.model.Boleta;
import gt.gob.rgm.adm.model.BoletaSum;
import gt.gob.rgm.adm.model.GenericCount;
import gt.gob.rgm.adm.model.RugSecuUsuario;
import gt.gob.rgm.adm.service.*;
import gt.gob.rgm.adm.util.Constants;
import gt.gob.rgm.adm.util.MailUtils;
import gt.gob.rgm.mail.service.MailService;

@Path("/boletas")
@RequestScoped
public class BoletaRs {

	@Inject
	BoletaService boletaService;

	@Inject
	RugSecuUsuarioService usuarioService;

	@Inject
	RugParametroConfService parametroService;

	@Inject
	BitacoraOperacionesService bitacoraService;

	@Inject
	RugReinicio reinicio;

	@Inject
	EmailServices emailServices;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@SecuredResource
	public ResponseRs getBoletas(@QueryParam(value = "status") Integer status, @QueryParam(value = "page") Integer page,
			@QueryParam(value = "size") Integer size, @QueryParam(value = "agencia") String agencia,
			@QueryParam(value = "numero") String numero, @QueryParam(value = "externalUser") String persona,
			@QueryParam(value = "tipoPago") String tipoPago) {
		ResponseRs response = new ResponseRs();
		List<Deposit> deposits = new ArrayList<>();
		List<Boleta> boletas;
		Long depositsCount = 0L;
		/*
		 * if(status != null) {
		 * boletas = boletaService.listBoletasStatus(status);
		 * } else {
		 * boletas = boletaService.listBoletas();
		 * }
		 */
		Deposit depositFilter = new Deposit();
		depositFilter.setUsada(status);
		depositFilter.setAgencia(agencia);
		depositFilter.setNumero(numero);
		ExternalUser externalUserFilter = null;
		if (persona != null) {
			externalUserFilter = new ExternalUser();
			externalUserFilter.setName(persona);
		}
		depositFilter.setExternalUser(externalUserFilter);
		depositFilter.setTipoPago(tipoPago);
		boletas = boletaService.listBoletas(depositFilter, page, size);
		depositsCount = boletaService.countBoletas(depositFilter);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String hostUrl = parametroService.getParam(Constants.HOST_URL_ADM).getValorParametro();
		for (Boleta boleta : boletas) {
			Deposit deposit = new Deposit();
			deposit.setId(boleta.getId());
			deposit.setAgencia(boleta.getAgencia());
			deposit.setCodigoTramite(boleta.getCodigoTramite().intValue());
			deposit.setFechaHora(formatter.format(boleta.getFechaHora()));
			deposit.setIdentificador(boleta.getIdentificador());
			deposit.setMonto(boleta.getMonto().doubleValue());
			deposit.setMontoOtrosBancos(
					boleta.getMontoOtrosBancos() == null ? 0L : boleta.getMontoOtrosBancos().doubleValue());
			deposit.setNumero(boleta.getNumero());
			deposit.setResolucion(boleta.getResolucion());
			deposit.setSerie(boleta.getSerie());
			deposit.setUsada(boleta.getUsada().intValue());
			if (status != null && status.intValue() == 0 && boletas.size() <= 10) {
				deposit.setTieneArchivo(boleta.getBytes() != null);
			}
			deposit.setUrl(hostUrl + "/attachmentb/" + deposit.getId());
			deposit.setTipoPago(boleta.getTipoPago());
			// persona
			if (boleta.getSecuUser() != null) {
				ExternalUser externalUser = new ExternalUser();
				externalUser.setDocId(boleta.getSecuUser().getPersona().getCurpDoc());
				externalUser.setNit(boleta.getSecuUser().getPersona().getRfc());
				externalUser.setName(boleta.getSecuUser().getPersonaFisica().getNombrePersona());
				externalUser.setPersonaId(boleta.getSecuUser().getIdPersona());
				externalUser.setEmail(boleta.getSecuUser().getCveUsuario());
				externalUser.setRegistered(formatter.format(boleta.getSecuUser().getFhRegistro().getTime()));
				externalUser.setStatus(boleta.getSecuUser().getSitUsuario());

				if (boleta.getSecuUser().getPersona().getCodigoRegistro() != null) {
					externalUser.setRegistryCode(String.valueOf(boleta.getSecuUser().getPersona().getCodigoRegistro())
							+ (boleta.getSecuUser().getPersona().getPerJuridica().equals("PF") ? "I" : "J"));
				}
				deposit.setExternalUser(externalUser);
			}

			deposits.add(deposit);
		}
		response.setTotal(depositsCount);
		response.setData(deposits);
		return response;
	}

	@GET
	@Path("/reporte")
	@Produces(MediaType.APPLICATION_JSON)
	@SecuredResource
	public List<GenericCount> getSumBoletas(@QueryParam(value = "fechaInicio") String fechaInicio,
			@QueryParam(value = "fechaFin") String fechaFin) {
		// List<Deposit> deposits = new ArrayList<>();
		List<GenericCount> boletas = boletaService.sumBoletas(fechaInicio, fechaFin);
		/*
		 * for(BoletaSum boleta : boletas) {
		 * Deposit deposit = new Deposit();
		 * deposit.setMonto(boleta.getMonto().doubleValue());
		 * deposit.setUsada(boleta.getUsada().intValue());
		 * deposit.setTipoPago(boleta.getTipoPago());
		 * 
		 * deposits.add(deposit);
		 * }
		 */

		return boletas;
	}

	@PUT
	@Path("/{id}/state")
	@Produces(MediaType.APPLICATION_JSON)
	@SecuredResource
	public Boleta resolveDepositRequest(@HeaderParam("Authorization") String authorization,
			@PathParam(value = "id") Long id, Deposit deposit) {
		Boleta boleta = boletaService.getBoleta(id);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// actualizar boleta
		if (boletaService.updateEstado(id, deposit.getUsada()) > 0) {
			// enviar correo con resultado exitoso
			String hostUrl = parametroService.getParam(Constants.HOST_URL).getValorParametro();
			// obtener al usuario
			RugSecuUsuario secuUser = usuarioService.getUsuario(Long.valueOf(boleta.getIdentificador()));
			boleta.setUsada(deposit.getUsada());

			int idTipoMensaje = 1;
			int idAccountSmtp = Integer
					.valueOf(parametroService.getParam(Constants.ID_SMTP_MAIL_REGISTRO_USUARIOS).getValorParametro());
			String to = secuUser.getCveUsuario();
			String cc = null;
			String cco = null;
			String subject = "";
			String message = "";
			boolean enviar = false;
			if (deposit.getUsada().intValue() == 1) {
				subject = parametroService.getParam(Constants.MAIL_SUBJECT_BOLETA_APROBADA).getValorParametro();
				message = parametroService.getParam(Constants.TEMPLATE_BOLETA_APPROVE).getValorParametro();
				enviar = true;
			} else {
				// grabar la causa
				if (boletaService.updateCausa(id, deposit.getCause()) > 0) {
					subject = parametroService.getParam(Constants.MAIL_SUBJECT_BOLETA_RECHAZADA).getValorParametro();
					message = parametroService.getParam(Constants.TEMPLATE_BOLETA_REBOUND).getValorParametro();
					enviar = true;
				}
			}
			if (enviar) {
				subject = subject.replace("@nombreCompleto", secuUser.getPersonaFisica().getNombrePersona());
				message = message.replace("@nombreCompleto", secuUser.getPersonaFisica().getNombrePersona());
				message = message.replace("@boleta", boleta.getNumero());
				message = message.replace("@monto", boleta.getMonto().toString());
				if (deposit.getUsada().intValue() == -1) {
					message = message.replace("@causa", deposit.getCause());
				}
				try {
					emailServices.AprobacionEmail(subject, message, parametroService.getParam(Constants.SEND_PATH).getValorParametro(), to, "", "");
//					MailUtils.getMailServiceInstance().sendMail(idTipoMensaje, idAccountSmtp, to, cc, cco, subject,
//							message);
				} catch (Exception e) {
					// si el envio del correo falla, se captura la excepcion pero continua el
					// proceso
					e.printStackTrace();
				}
			}
			// agregar a bitacora
			String resultado = (deposit.getUsada().intValue() == 1 ? "aprobado" : "rechazado");
			String token = authorization.split(" ")[1];
			bitacoraService.createEntry(token, "Se ha " + resultado + " la boleta [" + deposit.getNumero()
					+ "] del usuario " + secuUser.getCveUsuario());

			if(deposit.getUsada().intValue() == 1){
				reinicio.UpdateSaldoBoletas(boleta.getMonto().intValue(), (int) boleta.getSecuUser().getIdPersona());
			}

		} else {
			// enviar correo con resultado error
		}

		return boleta;
	}

	@GET
	@Path("/stats")
	@Produces(MediaType.APPLICATION_JSON)
	// @SecuredResource
	public List<BoletaStats> getStats(@QueryParam(value = "fechaInicio") String fechaInicio,
			@QueryParam(value = "fechaFin") String fechaFin,
			@QueryParam(value = "cf") String cfecha,
			@QueryParam(value = "fields") String fields,
			@QueryParam(value = "dis") Integer diaInicioSemana) {

		return boletaService.statsBoletas(fechaInicio, fechaFin, cfecha, fields, diaInicioSemana);
	}
}
