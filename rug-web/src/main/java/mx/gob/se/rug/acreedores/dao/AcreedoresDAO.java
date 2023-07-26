package mx.gob.se.rug.acreedores.dao;

import java.sql.Connection;
import java.util.List;

import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.acreedores.to.TramitesMasivosTO;
import mx.gob.se.rug.inscripcion.to.AltaParteTO;
import mx.gob.se.rug.to.PlSql;

public interface AcreedoresDAO{
	
	public PlSql modificarAcreedorRepresentado(AltaParteTO altaParteTO);
	
	public boolean altaAcreedor(AcreedorTO acreedorTO, Integer idUsuario);

	public PlSql altaAcreedorRep(AcreedorTO acreedorTO, Integer idUsuario);
	
	public PlSql altaAcreedorRepFinal(AcreedorTO acreedorTO, Integer idUsuario);

	public boolean bajaAcreedor(Integer idPersona, Integer idAcreedor);

	public boolean bajaTramiteMasivo(Integer idTramiteFirma,Integer idPersona);

	public boolean tieneTramites(Integer idAcreedor, Integer idUsuario);

	public boolean tieneUsuarios(Integer idAcreedor, Integer idUsuario);
	public List<AcreedorTO> getAcreedoresByPersona(Integer idPersona);

	
	public List<AcreedorTO> getAcreedoresDisponiblesTransmision(
		String idUsuarioLoggeado, String idAcreedorNoDisponible);

	public boolean sonAsociados(Connection connection, Integer idPersona,
			Integer idAcreedor) ;

	public List<AcreedorTO> getAcreedoresSinFirmaByPersona(Integer idPersona);
	
	public List<TramitesMasivosTO> getTramitesCargaMasivaSinFirmar(Integer idPersona);

	public AcreedorTO getARByID(Integer idPersona);
	
	public AcreedorTO getAcreRepById(Integer idPersona);
	
	public AcreedorTO getByIDTramite(Integer idTramite);
	
	public boolean isAutoridad(Integer idPersona);
	
	public long getCuentaMaestra(Integer idPersona);
}
