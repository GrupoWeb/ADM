package mx.gob.se.rug.masiva.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.apache.commons.lang.ArrayUtils;

import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.detallegarantia.dao.DetalleDAO;
import mx.gob.se.rug.detallegarantia.to.DetalleTO;
import mx.gob.se.rug.exception.InfrastructureException;
import mx.gob.se.rug.inscripcion.dao.AltaParteDAO;
import mx.gob.se.rug.inscripcion.dao.InscripcionDAO;
import mx.gob.se.rug.inscripcion.to.AltaParteTO;
import mx.gob.se.rug.inscripcion.to.BienEspecialTO;
import mx.gob.se.rug.inscripcion.to.DeudorTO;
import mx.gob.se.rug.juez.dao.JuezDAO;
import mx.gob.se.rug.masiva.dao.MasivaDAO;
import mx.gob.se.rug.masiva.exception.CargaMasivaException;
import mx.gob.se.rug.masiva.exception.GarantiaRepetidaException;
import mx.gob.se.rug.masiva.resultado.to.CargaMasivaResultado;
import mx.gob.se.rug.masiva.resultado.to.ResultadoCargaMasiva;
import mx.gob.se.rug.masiva.resultado.to.Resumen;
import mx.gob.se.rug.masiva.resultado.to.TramiteRes;
import mx.gob.se.rug.masiva.resultado.to.Tramites;
import mx.gob.se.rug.masiva.service.impl.MasivaServiceImpl;
import mx.gob.se.rug.masiva.service.impl.ModificaGarantiaServiceImpl;
import mx.gob.se.rug.masiva.to.AcreedorAdicional;
import mx.gob.se.rug.masiva.to.BienEspecial;
import mx.gob.se.rug.masiva.to.CargaMasiva;
import mx.gob.se.rug.masiva.to.Deudor;
import mx.gob.se.rug.masiva.to.Modificacion;
import mx.gob.se.rug.masiva.to.Tramite;
import mx.gob.se.rug.modificacion.service.impl.ModificacionServiceImp;
import mx.gob.se.rug.modificacion.to.ModificacionTO;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;

public class ModificacionService {
	
	public CargaMasivaResultado cargaMasivaModificacion(UsuarioTO usuario, CargaMasiva cm, AcreedorTO acreedor, Integer idArchivo) {
		System.out.println("entro a carga masiva cargaMasivaModificacion");
		CargaMasivaResultado cargaMasivaResultado= new CargaMasivaResultado();
		ResultadoCargaMasiva resultado = new ResultadoCargaMasiva();
		int corectos=0;
		int incorrectos=0;
		boolean isCorrecto;
		int totalTramites=cm.getModificacion().size();
		List<Tramite> tramitesCorrectos =new ArrayList<Tramite>();
		List<Tramite> tramitesIncorrectos =new ArrayList<Tramite>();
		
		int consecutivo = 1;
		Tramite tramite;
		resultado.setTramites(new Tramites());
		resultado.setResumen(new Resumen());
		
		MasivaDAO masivaDAO= new MasivaDAO();
		AltaParteDAO altaParteDAO = new AltaParteDAO();
		MasivaService masivaService= new MasivaServiceImpl();
		ModificaGarantiaServiceImpl garantiaServiceImpl= new ModificaGarantiaServiceImpl();
		ModificacionServiceImp detserv = new ModificacionServiceImp();
		DetalleDAO detalleDAO = new DetalleDAO();
		InscripcionDAO inscripcionDAO = new InscripcionDAO();
		
		JuezDAO juezDAO= new JuezDAO();
	
		Set<Integer> idGarantiasInXML = new HashSet<Integer>();
		try {
			for (Modificacion modificacion : cm.getModificacion()) {
				isCorrecto=true;
				TramiteRes tramiteRes= new TramiteRes();
				tramite = new Tramite();
				try{
					tramite.setIdArchivo(idArchivo);
					tramite.setIdUsuario(usuario.getPersona().getIdPersona());
					tramite.setConsecutivo(consecutivo++); 
					tramite.setIdGarantia(modificacion.getIdentificadorGarantia().getIdGarantia().intValue());
					tramite.setIdTipoTramite(7);//Modificacion
					tramite.setIdEstatus(5);//Pendiente de Firma
					tramite.setIdAcreedor(acreedor.getIdPersona());
					tramite.setClaveRastreo(modificacion.getIdentificadorGarantia().getClaveRastreo());
					
					tramiteRes.setClaveRastreo(tramite.getClaveRastreo());
					
					masivaService.verifyAcreedorOfGarantia(tramite.getIdAcreedor(), tramite.getIdGarantia());
					
					masivaService.validaGarantia(tramite.getIdGarantia(), idGarantiasInXML);
					
					masivaDAO.altaParteTramIncRast(tramite);
					masivaDAO.spCopiaGarantia(tramite);
					
					if(modificacion.getPartes().getDeudor().size()>0) {
						// deudores actuales
						List<DeudorTO> deudoresAct = new ArrayList<DeudorTO>();
						deudoresAct = altaParteDAO.getListaDeudores(tramite.getIdTramite());
						
						//TODO info de los deudores						
						for(Deudor deudor:modificacion.getPartes().getDeudor()) {
							
							DeudorTO deudorFound;
							if(deudor.getTipoPersona().equalsIgnoreCase("PF")) {
								deudorFound = deudoresAct.stream()
									  .filter(deudorTO -> deudor.getCurp().equals(deudorTO.getCurp()))
									  .findAny()
									  .orElse(null);
							} else {
								deudorFound = deudoresAct.stream()
										  .filter(deudorTO -> deudor.getRfc().equals(deudorTO.getRfc()))
										  .findAny()
										  .orElse(null);
							}
							
							if(deudor.getOperacion().longValueExact()==1L) {
								
								if(deudorFound!=null) {
									throw new CargaMasivaException("El deudor " + deudor.getTipoPersona()=="PF"?deudor.getCurp():deudor.getRfc()+ " a ingresar ya existe.");
								}
								
								AltaParteTO altaParteTO = new AltaParteTO();
								altaParteTO.setIdParte(2);
								altaParteTO.setIdTramite(tramite.getIdTramite());
								altaParteTO.setIdPersona(0);

								altaParteTO.setCurp(deudor.getCurp());
								altaParteTO.setNombre(deudor.getNombre());								
								altaParteTO.setRazonSocial(deudor.getDenominacionRazonSocial());
								altaParteTO.setIdNacionalidad(deudor.getIdNacionalidad()!=null?deudor.getIdNacionalidad().intValue():null);
								altaParteTO.setTipoPersona(deudor.getTipoPersona());
								altaParteTO.setHayDomicilio("V");								
								altaParteTO.setCorreoElectronico(deudor.getEmail());								
								altaParteTO.setIdUsuario(tramite.getIdUsuario());								
								altaParteTO.setDomicilioUno(deudor.getDomicilio());
								//campos nuevos
								altaParteTO.setInscrita(deudor.getInfoInscripcion());								
								altaParteTO.setRfc(deudor.getRfc());
								
								altaParteDAO.insert(altaParteTO);
							} else if(deudor.getOperacion().longValueExact()==2L) {
								
								if(deudorFound==null) {
									throw new CargaMasivaException("El deudor " + deudor.getTipoPersona()=="PF"?deudor.getCurp():deudor.getRfc()+ " a modificar no existe.");
								}
								
								AltaParteTO altaParteTO = new AltaParteTO();
								altaParteTO.setIdParte(2);
								altaParteTO.setIdTramite(tramite.getIdTramite());
								altaParteTO.setIdPersona(deudorFound.getIdPersona());
								
								altaParteTO.setCurp(deudor.getCurp());
								altaParteTO.setNombre(deudor.getNombre());								
								altaParteTO.setRazonSocial(deudor.getDenominacionRazonSocial());
								altaParteTO.setIdNacionalidad(deudor.getIdNacionalidad()!=null?deudor.getIdNacionalidad().intValue():null);
								altaParteTO.setTipoPersona(deudor.getTipoPersona());
								altaParteTO.setHayDomicilio("V");								
								altaParteTO.setCorreoElectronico(deudor.getEmail());								
								altaParteTO.setIdUsuario(tramite.getIdUsuario());								
								altaParteTO.setDomicilioUno(deudor.getDomicilio());
								//campos nuevos
								altaParteTO.setInscrita(deudor.getInfoInscripcion());								
								altaParteTO.setRfc(deudor.getRfc());
								
								altaParteDAO.actualizaParte(altaParteTO);
							} else if(deudor.getOperacion().longValueExact()==3L) {
								
								if(deudorFound==null) {
									throw new CargaMasivaException("El deudor " + deudor.getTipoPersona()=="PF"?deudor.getCurp():deudor.getRfc()+ " a eliminar no existe.");
								}
								
								altaParteDAO.bajaParte(tramite.getIdTramite(),deudorFound.getIdPersona(),2,"F");
							} 
						}	
						
						List<DeudorTO> deudoresFinal = new ArrayList<DeudorTO>();
						deudoresFinal = altaParteDAO.getListaDeudores(tramite.getIdTramite());
						
						if(deudoresFinal.size()==0) {
							throw new CargaMasivaException("Debe haber al menos un deudor en la garantía.");
						}
						
					}
					
					if(modificacion.getPartes().getAcreedorAdicional().size()>0) {
						// info de los acreedores
						// deudores actuales
						List<AcreedorTO> acreedoresAct = new ArrayList<AcreedorTO>();
						acreedoresAct = altaParteDAO.getListaAcreedores(tramite.getIdTramite());
						
						//TODO info de los deudores
						for(AcreedorAdicional acreedorAdicional:modificacion.getPartes().getAcreedorAdicional()) {
							
							AcreedorTO acreedorFound;
							if(acreedorAdicional.getTipoPersona().equalsIgnoreCase("PF")) {
								acreedorFound = acreedoresAct.stream()
									  .filter(acreedorTO -> acreedorAdicional.getCurp().equals(acreedorTO.getCurp()))
									  .findAny()
									  .orElse(null);
							} else {
								acreedorFound = acreedoresAct.stream()
										  .filter(acreedorTO -> acreedorAdicional.getRfc().equals(acreedorTO.getRfc()))
										  .findAny()
										  .orElse(null);
							}
							
							if(acreedorAdicional.getOperacion().longValueExact()==1L) {
								
								if(acreedorFound!=null) {
									throw new CargaMasivaException("El acreedor " + acreedorAdicional.getTipoPersona()=="PF"?acreedorAdicional.getCurp():acreedorAdicional.getRfc()+ " a ingresar ya existe.");
								}
								
								AltaParteTO altaParteTO = new AltaParteTO();
								altaParteTO.setIdParte(3);
								altaParteTO.setIdTramite(tramite.getIdTramite());
								altaParteTO.setIdPersona(0);

								altaParteTO.setCurp(acreedorAdicional.getCurp());
								altaParteTO.setNombre(acreedorAdicional.getNombre());								
								altaParteTO.setRazonSocial(acreedorAdicional.getDenominacionRazonSocial());
								altaParteTO.setIdNacionalidad(acreedorAdicional.getIdNacionalidad()!=null?acreedorAdicional.getIdNacionalidad().intValue():null);
								altaParteTO.setTipoPersona(acreedorAdicional.getTipoPersona());
								altaParteTO.setHayDomicilio("V");								
								altaParteTO.setCorreoElectronico(acreedorAdicional.getCorreoElectronico());								
								altaParteTO.setIdUsuario(tramite.getIdUsuario());								
								altaParteTO.setDomicilioUno(acreedorAdicional.getDomicilioExtranjeroUno());
								//campos nuevos
								altaParteTO.setInscrita(acreedorAdicional.getInfoInscripcion());								
								altaParteTO.setRfc(acreedorAdicional.getRfc());
								
								altaParteDAO.insert(altaParteTO);
							} else if(acreedorAdicional.getOperacion().longValueExact()==2L) {
								
								if(acreedorFound==null) {
									throw new CargaMasivaException("El acreedor " + acreedorAdicional.getTipoPersona()=="PF"?acreedorAdicional.getCurp():acreedorAdicional.getRfc()+ " a modificar no existe.");
								}
								
								AltaParteTO altaParteTO = new AltaParteTO();
								altaParteTO.setIdParte(3);
								altaParteTO.setIdTramite(tramite.getIdTramite());
								altaParteTO.setIdPersona(acreedorFound.getIdPersona());
								
								altaParteTO.setCurp(acreedorAdicional.getCurp());
								altaParteTO.setNombre(acreedorAdicional.getNombre());								
								altaParteTO.setRazonSocial(acreedorAdicional.getDenominacionRazonSocial());
								altaParteTO.setIdNacionalidad(acreedorAdicional.getIdNacionalidad()!=null?acreedorAdicional.getIdNacionalidad().intValue():null);
								altaParteTO.setTipoPersona(acreedorAdicional.getTipoPersona());
								altaParteTO.setHayDomicilio("V");								
								altaParteTO.setCorreoElectronico(acreedorAdicional.getCorreoElectronico());								
								altaParteTO.setIdUsuario(tramite.getIdUsuario());								
								altaParteTO.setDomicilioUno(acreedorAdicional.getDomicilioExtranjeroUno());
								//campos nuevos
								altaParteTO.setInscrita(acreedorAdicional.getInfoInscripcion());								
								altaParteTO.setRfc(acreedorAdicional.getRfc());
								
								altaParteDAO.actualizaParte(altaParteTO);
							} else if(acreedorAdicional.getOperacion().longValueExact()==3L) {
								
								if(acreedorFound==null) {
									throw new CargaMasivaException("El acreedor " + acreedorAdicional.getTipoPersona()=="PF"?acreedorAdicional.getCurp():acreedorAdicional.getRfc()+ " a eliminar no existe.");
								}
								
								altaParteDAO.bajaParte(tramite.getIdTramite(),acreedorFound.getIdPersona(),3,"F");
							} 
						}	
												
						List<AcreedorTO> acreedoresFinal = new ArrayList<AcreedorTO>();
						acreedoresFinal = altaParteDAO.getListaAcreedores(tramite.getIdTramite());
						
						if(acreedoresFinal.size()==0) {
							throw new CargaMasivaException("Debe haber al menos un acreedor en la garantía.");
						}

					}										
					
					if(modificacion.getGarantia()!=null) {
						if(modificacion.getGarantia().getCreacion()!=null) {
							garantiaServiceImpl.validaDatos(modificacion.getGarantia().getCreacion().getDescripcionBienesMuebles(), "descripcion-bienes-muebles", false, false, null);
							garantiaServiceImpl.validaDatos(modificacion.getGarantia().getCreacion().getDatosInstrumentoPublico(), "info-general-contrato", false, false, null);
							garantiaServiceImpl.validaDatos(modificacion.getGarantia().getCreacion().getTerminosCondiciones(), "observaciones-adicionales", false, false, null);						
							garantiaServiceImpl.validaDatos(modificacion.getGarantia().getCreacion().getInfoRegistro(), "info-registro", false, false, null);
							garantiaServiceImpl.validaDatos(modificacion.getGarantia().getCreacion().getbNoBienesOtorgados(), "b-no-bienes-otorgados", false, false, null);
							garantiaServiceImpl.validaDatos(modificacion.getGarantia().getCreacion().getbEsPrioritaria(), "b-es-prioritaria", false, false, null);
							garantiaServiceImpl.validaDatos(modificacion.getGarantia().getCreacion().getbEnOtroRegistro(), "b-en-otro-registro", false, false, null);
							garantiaServiceImpl.validaDatos(modificacion.getGarantia().getCreacion().getbDatosModificables(), "b-atribuibles-no-afectos", false, false, null);
							
							masivaDAO.actualizaDatosGantiaCarga(tramite.getIdGarantiaPendiente(), 
									modificacion.getGarantia().getCreacion().getDatosInstrumentoPublico(),
									modificacion.getGarantia().getCreacion().getDescripcionBienesMuebles(),								 
									modificacion.getGarantia().getCreacion().getTerminosCondiciones(), 
									modificacion.getGarantia().getCreacion().getInfoRegistro(),
									modificacion.getGarantia().getCreacion().getbNoBienesOtorgados(),
									modificacion.getGarantia().getCreacion().getbEsPrioritaria(), 
									modificacion.getGarantia().getCreacion().getbEnOtroRegistro(),
									modificacion.getGarantia().getCreacion().getbDatosModificables());
							
							if(modificacion.getGarantia().getCreacion().getBienesEspeciales()!=null && modificacion.getGarantia().getCreacion().getBienesEspeciales().getBienEspecial().size()>0) {
								List<BienEspecialTO> bienEspecialAct = new ArrayList<BienEspecialTO>();
								bienEspecialAct = detalleDAO.getListaBienes(tramite.getIdTramite(), 1);
								
								for(BienEspecial bienEspecial:modificacion.getGarantia().getCreacion().getBienesEspeciales().getBienEspecial()) {
									BienEspecialTO bienFound;
									bienFound = bienEspecialAct.stream()
												  .filter(bienEspecialTO -> bienEspecial.getIdentificador().equals(bienEspecialTO.getIdentificador()))
												  .findAny()
												  .orElse(null);
									if(bienEspecial.getOperacion().equalsIgnoreCase("1")) {									
										if(bienFound!=null) {
											throw new CargaMasivaException("El bien " + bienEspecial.getIdentificador() + " a ingresar ya existe.");
										}
										
										BienEspecialTO bienEspecialTO = new BienEspecialTO();
										bienEspecialTO.setIdTramite(tramite.getIdTramite());
										bienEspecialTO.setDescripcion(bienEspecial.getDescripcion());
										bienEspecialTO.setTipoBien(new Integer(bienEspecial.getTipoBienEspecial()));
										bienEspecialTO.setIdentificador(bienEspecial.getIdentificador());
										bienEspecialTO.setTipoIdentificador(new Integer(bienEspecial.getTipoIdentificador()));
										
										inscripcionDAO.registrarBien(bienEspecialTO);
									}
									else if(bienEspecial.getOperacion().equalsIgnoreCase("2")) {
										
										if(bienFound==null) {
											throw new CargaMasivaException("El bien " + bienEspecial.getIdentificador() + " a modificar no existe.");
										}
										
										BienEspecialTO bienEspecialTO = new BienEspecialTO();
										bienEspecialTO.setIdTramite(tramite.getIdTramite());
										bienEspecialTO.setDescripcion(bienEspecial.getDescripcion());
										bienEspecialTO.setTipoBien(new Integer(bienEspecial.getTipoBienEspecial()));
										bienEspecialTO.setIdentificador(bienEspecial.getIdentificador());
										bienEspecialTO.setTipoIdentificador(new Integer(bienEspecial.getTipoIdentificador()));
										bienEspecialTO.setIdTramiteGarantia(bienFound.getIdTramiteGarantia());
										
										inscripcionDAO.modificarBien(bienEspecialTO);
									}
									else if(bienEspecial.getOperacion().equalsIgnoreCase("3")) {
										
										if(bienFound==null) {
											throw new CargaMasivaException("El bien " + bienEspecial.getIdentificador() + " a eliminar no existe.");
										}
										
										inscripcionDAO.eliminarBien(bienFound.idTramiteGarantia);
									}
								}
							}
						}
						
						String infoRepresentante = null;
						
						if(modificacion.getGarantia().getObligacion()!=null) {
							garantiaServiceImpl.validaDatos(modificacion.getGarantia().getObligacion().getTerminos(), "info-representante", false, false, null);
							infoRepresentante = modificacion.getGarantia().getObligacion().getTerminos();
						}
						
						garantiaServiceImpl.validaDatos(modificacion.getRazon(), "razon-modificacion", false, false, null);
						
						masivaDAO.actualizaContrato(
								null,//Fecha Celebracion 
								null,
								infoRepresentante, 
								modificacion.getRazon(), 
								tramite.getIdGarantiaPendiente());
						
					}					
					
					//Resumen
					
					corectos= corectos+1;
					tramiteRes.setCodigoError("0");
					tramiteRes.setMensajeError("La Modificacion se agrego correctamente"); 
					tramitesCorrectos.add(tramite);
				
				}catch (CargaMasivaException e) {
					e.printStackTrace();
					incorrectos= incorrectos+1;
					tramiteRes.setCodigoError(e.getCodeError().toString());
					tramiteRes.setMensajeError(e.getMessage());
					tramitesIncorrectos.add(tramite);
					isCorrecto=false;
				}catch(GarantiaRepetidaException e){
					e.printStackTrace();
					incorrectos= incorrectos+1;
					tramiteRes.setCodigoError(e.getCodeError().toString());
					tramiteRes.setMensajeError(e.getMessage());
					tramitesIncorrectos.add(tramite);
					isCorrecto=false;
					
				}catch (InfrastructureException e) {
					e.printStackTrace();
					incorrectos= incorrectos+1;
					tramiteRes.setCodigoError("999");
					tramiteRes.setMensajeError(e.getMessage());
					tramitesIncorrectos.add(tramite);
					isCorrecto=false;
					
				}catch (Exception e) {
					e.printStackTrace();
					incorrectos= incorrectos+1;
					tramiteRes.setCodigoError("999");
					tramiteRes.setMensajeError(e.getMessage());
					tramitesIncorrectos.add(tramite);
					isCorrecto=false;
					
				}finally{
					
					if(modificacion.getIdentificadorGarantia().getClaveRastreo()!=null){
						tramiteRes.setClaveRastreo(modificacion.getIdentificadorGarantia().getClaveRastreo());
					}
					
					resultado.getTramites().getTramite().add(tramiteRes);
					
					if(!isCorrecto){
						modificacion=null;
					}
				}
				
			}
		} catch (Exception e) {
			java.util.Date date = new java.util.Date();
			resultado.getResumen().setMensajeError("Sucedio un error en el sistema la fecha :"
					+ date.toString() + ", enviar el XML a soporte. codError["
					+ date.getTime() + "]");
			System.out
					.println("ThreadCargaMasiva::cargaMasivaRectificacion:archivo ["
							+ idArchivo + "]::: Sucedio un error el proceso:::");
			e.printStackTrace();
		} finally {

			resultado.getResumen().setCorrectos(String.valueOf(corectos));
			resultado.getResumen().setErroneos(String.valueOf(incorrectos));
			resultado.getResumen().setNumeroRegistros(String.valueOf(totalTramites));
			cargaMasivaResultado.setResultadoCargaMasiva(resultado);
			cargaMasivaResultado.setTramitesCorrectos(tramitesCorrectos);
			cargaMasivaResultado.setTramitesIncorrectos(tramitesIncorrectos);
		}

		return cargaMasivaResultado;
	}
}
