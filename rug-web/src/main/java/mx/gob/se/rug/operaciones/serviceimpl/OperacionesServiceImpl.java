package mx.gob.se.rug.operaciones.serviceimpl;

import java.util.List;

import mx.gob.se.rug.exception.InfrastructureException;
import mx.gob.se.rug.inscripcion.dao.InscripcionDAO;
import mx.gob.se.rug.operaciones.dao.OperacionesDAO;
import mx.gob.se.rug.operaciones.to.CargaMasivaResumenTO;
import mx.gob.se.rug.operaciones.to.OperacionesTO;
import mx.gob.se.rug.to.UsuarioTO;

public class OperacionesServiceImpl {
	
	public List<OperacionesTO> getOpPendientes(UsuarioTO usuarioTo){
		return new OperacionesDAO().muestraOpPendientes(usuarioTo.getPersona().getIdPersona());
	}
	//Implementacion del m�todo para obtener los tramites pendientes via id de la persona
	public List<OperacionesTO> getOpPendientes(Integer idPersona){
		return new OperacionesDAO().muestraOpPendientes(idPersona);
	}
	
	public List<OperacionesTO> getOpPendientes(Integer idPersona, Integer inicio, Integer fin){
		return new OperacionesDAO().muestraOpPendientes(idPersona, inicio, fin);
	}
	
	public List<OperacionesTO> getOpPendientesPago(UsuarioTO usuarioTo){
		return new OperacionesDAO().muestraOpPendientesPago(usuarioTo.getPersona().getIdPersona());
	}
	public List<OperacionesTO> getOpTerminadas(UsuarioTO usuarioTo) throws InfrastructureException{
		return new OperacionesDAO().muestraOpTerminadas(usuarioTo.getPersona().getIdPersona());
	}
	public List<OperacionesTO> getOpTerminadas(Integer idPersona){
		return new OperacionesDAO().muestraOpTerminadasPag(idPersona);
	}
	public List<OperacionesTO> getOpTerminadas(Integer idPersona, Integer inicio, Integer fin){
		return new OperacionesDAO().muestraOpTerminadasPagInicioFin(idPersona, inicio, fin);
	}
	
	public List<OperacionesTO> getOpPendientesFirma(UsuarioTO usuarioTo){
		return new OperacionesDAO().muestraOpPendientesFirma(usuarioTo.getPersona().getIdPersona());
	}
	
	public List<OperacionesTO> getOpPendientesFirma(Integer idPersona, Integer inicio, Integer fin){
		return new OperacionesDAO().muestraOpPendientesFirma(idPersona, inicio, fin);
	}
	
	public List<OperacionesTO> getOpPendientesFirma(Integer idPersona){
		return new OperacionesDAO().muestraOpPendientesFirma(idPersona);
	}
	
	public boolean eliminaTramiteIncompleto(Integer idTramiteIncompleto){
		return new InscripcionDAO().bajaTramiteIncompleto(idTramiteIncompleto).getIntPl().intValue()==0?true:false;
	}
	
	public List<CargaMasivaResumenTO> muestraOpPendientesFirmaMasiva(int idPersona){
		return new OperacionesDAO().muestraOpPendientesFirmaMasiva( idPersona);
	}
	public List<CargaMasivaResumenTO> muestraOpPendientesFirmaMasiva(Integer idPersona, Integer inicio, Integer fin){
		return new OperacionesDAO().muestraOpPendientesFirmaMasiva( idPersona, inicio, fin);
	}
	
	public Integer getCountOpPendCap(Integer idPersona){
		return new OperacionesDAO().getCountOpPendCap(idPersona);
	}
	
	public Integer getCountOpPendFirma(Integer idPersona){
		return new OperacionesDAO().getCountOpPendFirma(idPersona);
	}
	public Integer getCountOpPendFirmaMasiva(Integer idPersona){
		return new OperacionesDAO().getCountOpPendFirmaMasiva(idPersona);
	}
	public Integer getCountOpTerminadasFecha(Integer idPersona){
		return new OperacionesDAO().getCountOpTerminadasFecha(idPersona);
	}

	public Integer getCantidadTramitesSoyAcreedor(Integer idPersona){
		return new OperacionesDAO().getCantidadTramitesAcreedor(idPersona);
	}

	public Integer getCantidadTramitesSoyAcreedorFiltro(Integer idPersona, String filtro){
		return new OperacionesDAO().getCantidadTramitesAcreedorFiltro(idPersona, filtro);
	}

	public Integer getCountOpTerminadasFechaFiltro(Integer idPersona, String filtro){
		return new OperacionesDAO().getCountOpTerminadasFechaFiltro(idPersona, filtro);
	}
	public Integer getCountBusOpPendCap(Integer idPersona, String dateStart, String dateEnd){
		return new OperacionesDAO().getCountBusOpPendCap(idPersona, dateStart, dateEnd);
	}
	public Integer getCountBusOpPendCapByOtorgante(Integer idPersona, String nomOtorgante){
		return new OperacionesDAO().getCountBusOpPendCapByOtorgante(idPersona, nomOtorgante);
	}
	public Integer getCountBusOpPendFirmaByOtorgante(Integer idPersona, String nomOtorgante){
		return new OperacionesDAO().getCountBusOpPendFirmaByOtorgante(idPersona, nomOtorgante);
	}
	public List<OperacionesTO> getBusOpPendientes(Integer idPersona, Integer inicio, Integer fin, String dateStart, String dateEnd){
		return new OperacionesDAO().muestraBusOpPendientes(idPersona, inicio, fin, dateStart, dateEnd);
	}
	public List<OperacionesTO> getBusOpPenByOtorgante(Integer idPersona, Integer inicio, Integer fin, String nomOtorgante){
		return new OperacionesDAO().muestraBusOpPenByOtorgante(idPersona, inicio, fin, nomOtorgante);
	}
	public Integer getCountBusOpPendFirma(Integer idPersona, String dateStart, String dateEnd){
		return new OperacionesDAO().getCountBusOpPendFirma(idPersona, dateStart, dateEnd);
	}
	public List<OperacionesTO> getBusOpPendFirma(Integer idPersona, Integer inicio, Integer fin, String dateStart, String dateEnd){
		return new OperacionesDAO().muestraBusOpPendFirma(idPersona, inicio, fin, dateStart, dateEnd);
	}
	public List<OperacionesTO> getBusOpPendFirmaByOtorgante(Integer idPersona, Integer inicio, Integer fin, String nomOtorgante){
		return new OperacionesDAO().muestraBusOpPendFirmaByOtorgante(idPersona, inicio, fin, nomOtorgante);
	}
	public Integer getCountBusOpTerminadas(Integer idPersona, String dateStart, String dateEnd){
		return new OperacionesDAO().getCountBusOpTerminadas(idPersona, dateStart, dateEnd);
	}
	public Integer getCountBusOpTermByOtorgante(Integer idPersona, String nomOtorgante){
		return new OperacionesDAO().getCountBusOpTermByOtorgante(idPersona, nomOtorgante);
	}
	public List<OperacionesTO> getBusOpTerminadas(Integer idPersona, Integer inicio, Integer fin, String dateStart, String dateEnd){
		return new OperacionesDAO().muestraBusOpTerminadas(idPersona, inicio, fin, dateStart, dateEnd);
	}
	public List<OperacionesTO> getBusOpTermByOtorgante(Integer idPersona, Integer inicio, Integer fin, String nomOtorgante){
		return new OperacionesDAO().muestraBusOpTermByOtorgante(idPersona, inicio, fin, nomOtorgante);
	}
	public Integer getCountBusOpPenFirMasiva(Integer idPersona, String dateStart, String dateEnd){
		return new OperacionesDAO().getCountBusOpPenFirmaMasiva(idPersona, dateStart, dateEnd);
	}
	public Integer getCountBusOpPenFirMasByClvRastreo(Integer idPersona, String clvRastreo){
		return new OperacionesDAO().getCountBusOpPenFirMasByClvRastreo(idPersona, clvRastreo);
	}
	public List<CargaMasivaResumenTO> muestraBusOpPenFirmaMasiva(Integer idPersona, Integer inicio, Integer fin, String dateStart, String dateEnd){
		return new OperacionesDAO().muestraBusOpPenFirmaMasiva( idPersona, inicio, fin, dateStart, dateEnd);
	}
	public List<CargaMasivaResumenTO> muestraBusOpPenFirmaMasByClvRastreo(Integer idPersona, Integer inicio, Integer fin, String clvRastreo){
		return new OperacionesDAO().muestraBusOpPenFirMasByClvRastreo( idPersona, inicio, fin, clvRastreo);
	}
}
