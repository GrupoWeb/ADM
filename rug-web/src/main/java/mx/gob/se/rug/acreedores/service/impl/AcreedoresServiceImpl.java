package mx.gob.se.rug.acreedores.service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import mx.gob.se.rug.acreedores.dao.AcreedoresDAO;
import mx.gob.se.rug.acreedores.dao.impl.AcreedoresDaoJdbcImpl;
import mx.gob.se.rug.acreedores.service.AcreedoresService;
import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.acreedores.to.TramitesMasivosTO;
import mx.gob.se.rug.inscripcion.dao.AltaParteDAO;
import mx.gob.se.rug.inscripcion.dao.NacionalidadDAO;
import mx.gob.se.rug.inscripcion.to.AltaParteTO;
import mx.gob.se.rug.inscripcion.to.NacionalidadTO;
import mx.gob.se.rug.to.PlSql;
import mx.gob.se.rug.to.TipoPersona;

public class AcreedoresServiceImpl implements AcreedoresService{
	private AcreedoresDAO acreedoresDAO = new AcreedoresDaoJdbcImpl();
	public boolean insertAcreedorRep(AcreedorTO acreedorTO, Integer idUsuario){
		return acreedoresDAO.altaAcreedor(acreedorTO, idUsuario);
	}
	
	public List<AcreedorTO> getAcreedores(Integer idPersona){
		return acreedoresDAO.getAcreedoresByPersona(idPersona);
	}
	public AcreedorTO getAcreedor(Integer idPersona){
		return acreedoresDAO.getARByID(idPersona);		
	}
	
	public List<NacionalidadTO> getNacionalidades(){
		return new NacionalidadDAO().getNacionalidades();
	}
	
	public List<TipoPersona> getTiposPersona(){
		List<TipoPersona> lista = new ArrayList<TipoPersona>();
		TipoPersona persona = new TipoPersona();
		persona.setId("PM");
		persona.setDesc("Persona Moral");
		lista.add(persona);
		persona = new TipoPersona();
		persona.setId("PF");
		persona.setDesc("Persona Fisica");
		lista.add(persona);
		return lista;
	}
	
	public boolean bajaAcreedorRep(Integer idPersona, Integer idAcreedor){
		return acreedoresDAO.bajaAcreedor(idPersona, idAcreedor);
	}
	
	public String saveGrupo(){
		return null;
		
	}
	
	public List<AcreedorTO> getAcreedoresDisponiblesTransmision(
			String idUsuarioLoggeado, String idAcreedorNoDisponible) {
		return acreedoresDAO.getAcreedoresDisponiblesTransmision(
				idUsuarioLoggeado, idAcreedorNoDisponible);
	}

	@Override
	public PlSql modificarAcreedorRepresentado(AltaParteTO altaParteTO) {
		// TODO Auto-generated method stub
		return acreedoresDAO.modificarAcreedorRepresentado(altaParteTO);
	}

	@Override
	public boolean altaAcreedor(AcreedorTO acreedorTO, Integer idUsuario) {
		// TODO Auto-generated method stub
		return acreedoresDAO.altaAcreedor(acreedorTO, idUsuario);
	}

	@Override
	public PlSql altaAcreedorRep(AcreedorTO acreedorTO, Integer idUsuario) {
		// TODO Auto-generated method stub
		return acreedoresDAO.altaAcreedorRep(acreedorTO, idUsuario);
	}
	
	@Override
	public PlSql altaAcreedorRepFinal(AcreedorTO acreedorTO, Integer idUsuario) {
		// TODO Auto-generated method stub
		return acreedoresDAO.altaAcreedorRepFinal(acreedorTO, idUsuario);
	}

	@Override
	public boolean bajaAcreedor(Integer idPersona, Integer idAcreedor) {
		// TODO Auto-generated method stub
		return acreedoresDAO.bajaAcreedor(idPersona, idAcreedor);
	}
	@Override
	public boolean bajaTramiteMasivo(Integer idTramiteFirma, Integer idUsuario) {
		// TODO Auto-generated method stub
		return acreedoresDAO.bajaTramiteMasivo(idTramiteFirma, idUsuario);
	}

	@Override
	public boolean tieneTramites(Integer idAcreedor, Integer idUsuario) {
		// TODO Auto-generated method stub
		return acreedoresDAO.tieneTramites(idAcreedor, idUsuario);
	}

	@Override
	public boolean tieneUsuarios(Integer idAcreedor, Integer idUsuario) {
		// TODO Auto-generated method stub
		return acreedoresDAO.tieneUsuarios(idAcreedor, idUsuario);
	}

	@Override
	public List<AcreedorTO> getAcreedoresByPersona(Integer idPersona) {
		// TODO Auto-generated method stub
		return acreedoresDAO.getAcreedoresByPersona(idPersona);
	}

	@Override
	public boolean sonAsociados(Connection connection, Integer idPersona,
			Integer idAcreedor) {
		// TODO Auto-generated method stub
		return acreedoresDAO.sonAsociados(connection, idPersona, idAcreedor);
	}

	@Override
	public List<AcreedorTO> getAcreedoresSinFirmaByPersona(Integer idPersona) {
		// TODO Auto-generated method stub
		return acreedoresDAO.getAcreedoresSinFirmaByPersona(idPersona);
	}
	
	@Override
	public List<TramitesMasivosTO> getTramitesCargaMasivaSinFirmar(Integer idPersona) {
		// TODO Auto-generated method stub
		return acreedoresDAO.getTramitesCargaMasivaSinFirmar(idPersona);
	} 
	@Override
	public AcreedorTO getARByID(Integer idPersona) {
		// TODO Auto-generated method stub
		return acreedoresDAO.getARByID(idPersona);
	}
	
	@Override
	public AcreedorTO getAcreRepById(Integer idPersona) {
		// TODO Auto-generated method stub
		return acreedoresDAO.getAcreRepById(idPersona);
	}

	@Override
	public AcreedorTO getByIDTramite(Integer idTramite) {
		// TODO Auto-generated method stub
		return acreedoresDAO.getByIDTramite(idTramite);
	}

	@Override
	public boolean isAutoridad(Integer idPersona) {
		// TODO Auto-generated method stub
		return new AltaParteDAO().esAutoridad(Integer.valueOf(idPersona));
	}
}
