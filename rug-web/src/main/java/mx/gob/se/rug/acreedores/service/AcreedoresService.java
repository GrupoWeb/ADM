package mx.gob.se.rug.acreedores.service;

import java.sql.Connection;
import java.util.List;

import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.acreedores.to.TramitesMasivosTO;
import mx.gob.se.rug.inscripcion.to.AltaParteTO;
import mx.gob.se.rug.inscripcion.to.NacionalidadTO;
import mx.gob.se.rug.to.PlSql;
import mx.gob.se.rug.to.TipoPersona;

public interface AcreedoresService {
	
	public boolean insertAcreedorRep(AcreedorTO acreedorTO, Integer idUsuario);

	public List<NacionalidadTO> getNacionalidades();

	public List<AcreedorTO> getAcreedores(Integer idPersona);

	public AcreedorTO getAcreedor(Integer idPersona);

	public List<TipoPersona> getTiposPersona();

	public boolean bajaAcreedorRep(Integer idPersona, Integer idAcreedor);

	public PlSql modificarAcreedorRepresentado(AltaParteTO altaParteTO);

	public boolean altaAcreedor(AcreedorTO acreedorTO, Integer idUsuario);

	public PlSql altaAcreedorRep(AcreedorTO acreedorTO, Integer idUsuario);
	
	public PlSql altaAcreedorRepFinal(AcreedorTO acreedorTO, Integer idUsuario);

	public boolean bajaAcreedor(Integer idPersona, Integer idAcreedor);

	public boolean bajaTramiteMasivo(Integer idTramiteFirma, Integer idUsuario);
	
	public boolean tieneTramites(Integer idAcreedor, Integer idUsuario);

	public boolean tieneUsuarios(Integer idAcreedor, Integer idUsuario);

	public List<AcreedorTO> getAcreedoresByPersona(Integer idPersona);

	public List<AcreedorTO> getAcreedoresDisponiblesTransmision(
			String idUsuarioLoggeado, String idAcreedorNoDisponible);

	public boolean sonAsociados(Connection connection, Integer idPersona,
			Integer idAcreedor);

	public List<AcreedorTO> getAcreedoresSinFirmaByPersona(Integer idPersona);

	public AcreedorTO getARByID(Integer idPersona);
	
	public AcreedorTO getAcreRepById(Integer idPersona);
	
	public AcreedorTO getByIDTramite(Integer idTramite);
	
	public boolean isAutoridad(Integer idPersona);

	public List<TramitesMasivosTO> getTramitesCargaMasivaSinFirmar(Integer idPersona);

}
