package mx.gob.se.rug.masiva.service.impl;

import java.util.List;
import java.util.logging.Level;

import mx.gob.se.rug.inscripcion.dao.AltaParteDAO;
import mx.gob.se.rug.inscripcion.to.AltaParteTO;
import mx.gob.se.rug.inscripcion.to.OtorganteTO;
import mx.gob.se.rug.masiva.resultado.to.TramiteRes;
import mx.gob.se.rug.masiva.service.PartesMasivaService;
import mx.gob.se.rug.masiva.to.ControlError;
import mx.gob.se.rug.masiva.to.Otorgante;
import mx.gob.se.rug.masiva.to.Tramite;
import mx.gob.se.rug.partes.dao.FolioElectronicoDAO;
import mx.gob.se.rug.to.PlSql;
import mx.gob.se.rug.util.MyLogger;

public class PartesMasivaServiceImpl implements PartesMasivaService {
	@Override
	public ControlError agregaOtorgantes(List<Otorgante> otorgantes,
			Tramite tramite, TramiteRes tramiteRes) {
		// TODO Auto-generated method stub
		ControlError regresa = null;
		for (Otorgante otorgante :otorgantes){
			regresa =agregaOtorganteInd(otorgante,tramite,tramiteRes);
			if (regresa!=null){
				break;
			}
		}

		return regresa;
	}
	//Agrega otorgantes individual
	private ControlError agregaOtorganteInd(Otorgante otorgante,Tramite tramite, TramiteRes tramiteRes) {
		ControlError regresa = null;
		try {
			AltaParteDAO altaParteDAO = new AltaParteDAO();
			if (otorgante == null) {
				regresa = new ControlError();
				PlSql plSql = new PlSql();
				plSql.setResStrPl("La inscripcion no cuenta con un Otorgante");
				regresa.setPlSql(plSql);
				regresa.setIdInscripcion(tramite.getConsecutivo());
			} else {
				if (otorgante.getTipoPersona().trim().equals("PF")
						&& otorgante.getFolioElectronico() != null
						&& !otorgante.getFolioElectronico().trim().equals("")) {
					OtorganteTO otorganteTO = altaParteDAO
							.getOtorganteFisicoByFolioElectronico(otorgante
									.getFolioElectronico());
					if (otorganteTO != null) {
						if (!altaParteDAO.relParte(otorganteTO.getIdOtorgante(),tramite.getIdTramite(), 1, null)){
							regresa = new ControlError();
							PlSql plSql = new PlSql();
							plSql.setResStrPl("No se pudo relacionar al otorgante");
							plSql.setStrPl("No se pudo relacionar al otorgante");
							plSql.setIntPl(591);
							plSql.setResIntPl(887);
							regresa.setPlSql(plSql);
							regresa.setIdInscripcion(tramite.getConsecutivo());
							
						}
					} else {
						regresa = new ControlError();
						PlSql plSql = new PlSql();
						plSql.setResStrPl("El Folio Electronico no existe");
						plSql.setStrPl("El Folio Electronico no existe");
						plSql.setIntPl(591);
						plSql.setResIntPl(887);
						regresa.setPlSql(plSql);
						regresa.setIdInscripcion(tramite.getConsecutivo());
						
					}
				} else {
					AltaParteTO altaParteTO = new AltaParteTO();
					altaParteTO.setIdParte(1);
					altaParteTO.setIdTramite(tramite.getIdTramite());
					altaParteTO.setFolioMercantil(otorgante.getFolioElectronico());
					altaParteTO.setCurp(otorgante.getCurp());
					altaParteTO.setNombre(otorgante.getNombre());
					altaParteTO.setApellidoMaterno(otorgante.getApellidoMaterno());
					altaParteTO.setApellidoPaterno(otorgante.getApellidoPaterno());
					altaParteTO.setRazonSocial(otorgante.getDenominacionRazonSocial());
					altaParteTO.setRfc(otorgante.getRfc());
					altaParteTO.setIdNacionalidad(otorgante.getIdNacionalidad().intValue());
					altaParteTO.setTipoPersona(otorgante.getTipoPersona());
					altaParteTO.setHayDomicilio("F");
					altaParteTO.setIdUsuario(tramite.getIdUsuario());
					altaParteTO.setIdPersona(tramite.getIdUsuario());
					if (altaParteTO.getTipoPersona().trim().equals("PM")) {
						MyLogger.Logger.log(Level.INFO,"es una persona moral");
						if (altaParteTO.getFolioMercantil() == null
								|| altaParteTO.getFolioMercantil().trim()
										.equals("")) {
							regresa = new ControlError();
							PlSql plSql = new PlSql();
							plSql.setResStrPl("Error en el Otorgante:  El campo folio electrónico es obligatorio.");
							plSql.setStrPl("Error en el Otorgante:  El campo folio electrónico es obligatorio.");
							plSql.setIntPl(2);
							regresa.setPlSql(plSql);
							regresa.setIdInscripcion(tramite.getConsecutivo());
						} else {
							PlSql intPer = altaParteDAO.insert(altaParteTO);
							if (intPer == null) {
								regresa = new ControlError();
								PlSql plSql = new PlSql();
								plSql.setResStrPl("Error en el Otorgante - No se pudo agregar al otorgante");
								plSql.setStrPl("Error en el Otorgante - No se pudo agregar al otorgante");
								regresa.setPlSql(plSql);
								regresa.setIdInscripcion(tramite.getConsecutivo());
							} else {
								if (intPer.getIntPl().intValue() != 0) {
									regresa = new ControlError();
									regresa.setPlSql(intPer);
								}
							}
						}

					} else {
						PlSql intPer = altaParteDAO.insert(altaParteTO);
						if (intPer == null) {
							regresa = new ControlError();
							PlSql plSql = new PlSql();
							plSql.setResStrPl("Error en el Otorgante - No se pudo agregar al otorgante");
							plSql.setStrPl("Error en el Otorgante - No se pudo agregar al otorgante");
							regresa.setPlSql(plSql);
							regresa.setIdInscripcion(tramite.getConsecutivo());
						} else {
							if (intPer.getIntPl().intValue() != 0) {							
								regresa = new ControlError();
								regresa.setPlSql(intPer);
							} else {
								FolioElectronicoDAO folioElectronicoDAO = new FolioElectronicoDAO();
								String strMsj = folioElectronicoDAO
										.creaFolioElectronico(intPer
												.getResIntPl().intValue());
								if (strMsj!=null){
									mx.gob.se.rug.masiva.resultado.to.Otorgante otorgante2 = new mx.gob.se.rug.masiva.resultado.to.Otorgante();
									otorgante2.setCurp(altaParteTO.getCurp());
									otorgante2.setFolioElectronico(strMsj);
									otorgante2.setNombreCompleto(altaParteTO.getNombre() +" " + altaParteTO.getApellidoPaterno() +" "+ altaParteTO.getApellidoMaterno());
									tramiteRes.getOtorgante().add(otorgante2);
								}else{
									regresa = new ControlError();
									PlSql plSql = new PlSql();
									plSql.setIntPl(501);
									plSql.setResStrPl("Error al tratar de generar folio del RUG, reportarlo al area de sistemas");
									plSql.setStrPl("Error al tratar de generar folio del RUG, reportarlo al area de sistemas");
									regresa.setPlSql(plSql);
									regresa.setIdInscripcion(tramite.getConsecutivo());
								}
								
							}
						}
					}
				}

			}
		} catch (NumberFormatException e) {
			regresa = new ControlError();
			PlSql plSql = new PlSql();
			plSql.setIntPl(999);
			plSql.setResStrPl("Error al tratar de convertir a entero:"
					+ e.getMessage() + ", " + e.getCause());
			plSql.setStrPl("Error al tratar de convertir a entero:"
					+ e.getMessage() + ", " + e.getCause());
			regresa.setPlSql(plSql);
			regresa.setIdInscripcion(tramite.getConsecutivo());
			e.printStackTrace();
		} catch (Exception e) {
			regresa = new ControlError();
			PlSql plSql = new PlSql();
			plSql.setIntPl(999);
			plSql.setResStrPl(e.getMessage() + ", " + e.getCause());
			plSql.setStrPl(e.getMessage() + ", " + e.getCause());
			regresa.setPlSql(plSql);
			regresa.setIdInscripcion(tramite.getConsecutivo());
			e.printStackTrace();
		}
			return regresa;

	}
}
