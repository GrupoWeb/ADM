package mx.gob.se.rug.operaciones.service;

import java.util.List;

import mx.gob.se.rug.exception.InfrastructureException;
import mx.gob.se.rug.operaciones.serviceimpl.OperacionesServiceImpl;
import mx.gob.se.rug.operaciones.to.CargaMasivaResumenTO;
import mx.gob.se.rug.operaciones.to.OperacionesTO;
import mx.gob.se.rug.to.UsuarioTO;

public class OperacionesService {
	
	public List<OperacionesTO> getOpPendientes(UsuarioTO usuarioTo){
		return new OperacionesServiceImpl().getOpPendientes(usuarioTo);
	}
	//service para obtener tramites pendientes por el id de la persona
	public List<OperacionesTO> getOpPendientes(Integer idPersona){
		return new OperacionesServiceImpl().getOpPendientes(idPersona);
	}
	
	//service para obtener tramites pendientes por el id de la persona con un inicio y un fin para el paginador
		public List<OperacionesTO> getOpPendientes(Integer idPersona, Integer inicio, Integer fin){
			return new OperacionesServiceImpl().getOpPendientes(idPersona, inicio, fin);
		}
	
	public List<OperacionesTO> getOpPendientesPago(UsuarioTO usuarioTo){
		return new OperacionesServiceImpl().getOpPendientesPago(usuarioTo);
	}
	public List<OperacionesTO> getOpTerminadas(UsuarioTO usuarioTo) throws InfrastructureException{
		return new OperacionesServiceImpl().getOpTerminadas(usuarioTo);
	}
	
	public List<OperacionesTO> getOpTerminadas(Integer idPersona){
		return new OperacionesServiceImpl().getOpTerminadas(idPersona);
	}
	
	public List<OperacionesTO> getOpTerminadas(Integer idPersona, Integer inicio, Integer fin){
		return new OperacionesServiceImpl().getOpTerminadas(idPersona, inicio, fin);
	}
	
	public List<OperacionesTO> getOpPendientesFirma(UsuarioTO usuarioTo){
		return new OperacionesServiceImpl().getOpPendientesFirma(usuarioTo);
	}
	
	public List<OperacionesTO> getOpPendientesFirma(Integer idPersona){
		return new OperacionesServiceImpl().getOpPendientesFirma(idPersona);
	}
	
	public List<OperacionesTO> getOpPendientesFirma(Integer idPersona, Integer inicio, Integer fin){
		return new OperacionesServiceImpl().getOpPendientesFirma(idPersona, inicio, fin);
	}
	
	public boolean eliminaTramiteIncompleto(Integer idTramiteIncompleto){
		return new OperacionesServiceImpl().eliminaTramiteIncompleto(idTramiteIncompleto);
	}
	
	public List<CargaMasivaResumenTO> muestraOpPendientesFirmaMasiva(UsuarioTO usuarioTo){
		return  new OperacionesServiceImpl().muestraOpPendientesFirmaMasiva( new Integer(usuarioTo.getPersona().getIdPersona()));
	}
	
	public List<CargaMasivaResumenTO> muestraOpPendientesFirmaMasiva(Integer idPersona){
		return  new OperacionesServiceImpl().muestraOpPendientesFirmaMasiva(idPersona);
	}
	
	public List<CargaMasivaResumenTO> muestraOpPendientesFirmaMasiva(Integer idPersona, Integer inicio, Integer fin){
		return  new OperacionesServiceImpl().muestraOpPendientesFirmaMasiva(idPersona, inicio, fin);
	}
	public Integer getCountOpPendCap(Integer idPersona){
		return  new OperacionesServiceImpl().getCountOpPendCap(idPersona);
	}
	public Integer getCountOpPendFirma(Integer idPersona){
		return  new OperacionesServiceImpl().getCountOpPendFirma(idPersona);
	}
	public Integer getCountOpPendFirmaMasiva(Integer idPersona){
		return  new OperacionesServiceImpl().getCountOpPendFirmaMasiva(idPersona);
	}
	public Integer getCountOpTerminadasFecha(Integer idPersona){
		return  new OperacionesServiceImpl().getCountOpTerminadasFecha(idPersona);
	}

	public Integer getCantidadTramitesSoyAcreedor(Integer idPersona){
		return new OperacionesServiceImpl().getCantidadTramitesSoyAcreedor(idPersona);
	}

	public Integer getCountAcreedorFilter(Integer idPersona, String filtro)
	{
		return new OperacionesServiceImpl().getCantidadTramitesSoyAcreedorFiltro(idPersona,filtro);
	}

	public Integer getCountOpTerminadasFechaFiltro(Integer idPersona, String filtro){
		return  new OperacionesServiceImpl().getCountOpTerminadasFechaFiltro(idPersona, filtro);
	}
	public Integer getCountBusOpPendCap(Integer idPersona, String dateStart, String dateEnd){
		return  new OperacionesServiceImpl().getCountBusOpPendCap(idPersona, dateStart, dateEnd);
	}
	public Integer getCountBusOpPendCapByOtorgante(Integer idPersona, String nomOtorgante){
		return  new OperacionesServiceImpl().getCountBusOpPendCapByOtorgante(idPersona, nomOtorgante);
	}
	public List<OperacionesTO> getBusOpPendientes(Integer idPersona, Integer inicio, Integer fin, String dateStart, String dateEnd){
		return new OperacionesServiceImpl().getBusOpPendientes(idPersona, inicio, fin, dateStart, dateEnd);
	}
	public List<OperacionesTO> getBusOpPenByOtorgante(Integer idPersona, Integer inicio, Integer fin, String nomOtorgante){
		return new OperacionesServiceImpl().getBusOpPenByOtorgante(idPersona, inicio, fin, nomOtorgante);
	}
	public Integer getCountBusOpPendFirma(Integer idPersona, String dateStart, String dateEnd){
		return  new OperacionesServiceImpl().getCountBusOpPendFirma(idPersona, dateStart, dateEnd);
	}
	public Integer getCountBusOpPendFirmaByOtorgante(Integer idPersona, String nomOtorgante){
		return  new OperacionesServiceImpl().getCountBusOpPendFirmaByOtorgante(idPersona, nomOtorgante);
	}
	public List<OperacionesTO> getBusOpPenFirma(Integer idPersona, Integer inicio, Integer fin, String dateStart, String dateEnd){
		return new OperacionesServiceImpl().getBusOpPendFirma(idPersona, inicio, fin, dateStart, dateEnd);
	}
	public List<OperacionesTO> getBusOpPenFirmaByOtorgante(Integer idPersona, Integer inicio, Integer fin, String nomOtorgante){
		return new OperacionesServiceImpl().getBusOpPendFirmaByOtorgante(idPersona, inicio, fin, nomOtorgante);
	}
	public Integer getCountBusOpTerminadas(Integer idPersona, String dateStart, String dateEnd){
		return  new OperacionesServiceImpl().getCountBusOpTerminadas(idPersona, dateStart, dateEnd);
	}
	public Integer getCountBusOpTermByOtorgante(Integer idPersona, String nomOtorgante){
		return  new OperacionesServiceImpl().getCountBusOpTermByOtorgante(idPersona, nomOtorgante);
	}
	public List<OperacionesTO> getBusOpTerminadas(Integer idPersona, Integer inicio, Integer fin, String dateStart, String dateEnd){
		return new OperacionesServiceImpl().getBusOpTerminadas(idPersona, inicio, fin, dateStart, dateEnd);
	}
	public List<OperacionesTO> getBusOpTermByOtorgante(Integer idPersona, Integer inicio, Integer fin, String nomOtorgante){
		return new OperacionesServiceImpl().getBusOpTermByOtorgante(idPersona, inicio, fin, nomOtorgante);
	}
	public Integer getCountBusOpPendFirmaMasiva(Integer idPersona, String dateStart, String dateEnd){
		return  new OperacionesServiceImpl().getCountBusOpPenFirMasiva(idPersona, dateStart, dateEnd);
	}
	public Integer getCountBusOpPendFirMasivaByClvRastreo(Integer idPersona, String clvRastreo){
		return  new OperacionesServiceImpl().getCountBusOpPenFirMasByClvRastreo(idPersona, clvRastreo);
	}
	public List<CargaMasivaResumenTO> getBusOpPenFirmaMasiva(Integer idPersona, Integer inicio, Integer fin, String dateStart, String dateEnd){
		return new OperacionesServiceImpl().muestraBusOpPenFirmaMasiva(idPersona, inicio, fin, dateStart, dateEnd);
	}
	public List<CargaMasivaResumenTO> getBusOpPenFirmaMasByClvRastreo(Integer idPersona, Integer inicio, Integer fin, String clvRastreo){
		return new OperacionesServiceImpl().muestraBusOpPenFirmaMasByClvRastreo(idPersona, inicio, fin, clvRastreo);
	}
}
