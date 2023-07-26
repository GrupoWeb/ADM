package mx.gob.se.rug.acreedores.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import mx.gob.economia.dgi.framework.dao.exception.JdbcDaoException;
import mx.gob.se.rug.acreedores.dao.AcreedoresCatalogosDao;
import mx.gob.se.rug.acreedores.to.GrupoPerfilTO;
import mx.gob.se.rug.acreedores.to.PerfilTO;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.dto.DatosContacto;
import mx.gob.se.rug.dto.PersonaFisica;
import mx.gob.se.rug.fwk.dao.spring.RugBaseJdbcDao;
import mx.gob.se.rug.util.MyLogger;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

public class AcreedoresCatalogosDaoJdbcImpl extends RugBaseJdbcDao implements AcreedoresCatalogosDao {

	@Override
	public List<PerfilTO> getPerfiles() throws JdbcDaoException {
		StringBuffer sqlQuery = new StringBuffer("");
		sqlQuery.append(" SELECT ");
		sqlQuery.append(" ID_PRIVILEGIO, ");
		sqlQuery.append(" DESC_PRIVILEGIO ");
		sqlQuery.append(" FROM V_PRIVILEGIOS ");
		
		List<PerfilTO> lista = new ArrayList<PerfilTO>();
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			 ps = connection.prepareStatement(sqlQuery.toString());
			 rs = ps.executeQuery();
			PerfilTO perfilTO;
			while (rs.next()){
				perfilTO = new PerfilTO();
				perfilTO.setId(rs.getString("ID_PRIVILEGIO"));
				perfilTO.setDescripcion(rs.getString("DESC_PRIVILEGIO"));
				lista.add(perfilTO);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection, rs, ps);
		}

		return lista;
	}

	public class PerfilesBRowMapper implements RowMapper {
		@Override
		public Object mapRow(ResultSet resultSet, int line) throws SQLException {
			PerfilesResultSetExtractor extractor = new PerfilesResultSetExtractor();
			return extractor.extractData(resultSet);
		}
	}

	public class PerfilesResultSetExtractor implements ResultSetExtractor {
		@Override
		public Object extractData(ResultSet resultSet) throws SQLException,
				DataAccessException {
			PerfilTO perfil = new PerfilTO();
			perfil.setId(resultSet.getString("ID_PRIVILEGIO"));
			perfil.setDescripcion(resultSet.getString("DESC_PRIVILEGIO"));
			return perfil;
		}
	}
	
	public List<GrupoPerfilTO> getPrivilegiosByGrupo(Integer idGrupo) throws JdbcDaoException {
		MyLogger.Logger.log(Level.INFO, "--getGrupos DAO--");
		MyLogger.Logger.log(Level.INFO, "IMPRIME ID PERSONA EN DAO: "+idGrupo);
		ConexionBD bd = new ConexionBD();
		StringBuffer sqlQuery = new StringBuffer("");
		sqlQuery.append(" SELECT ");
		sqlQuery.append(" ID_PRIVILEGIO, ");
		sqlQuery.append(" DESC_PRIVILEGIO ");
		sqlQuery.append(" FROM V_GRUPOS ");
		sqlQuery.append(" WHERE ID_GRUPO = "+idGrupo+" AND VISIBLES = 1"); 
		List <GrupoPerfilTO> lista = new ArrayList<GrupoPerfilTO>();
		Connection connection= null;
		PreparedStatement ps = null;
		ResultSet rs =null;
		try {
			connection=bd.getConnection();
			 ps = connection.prepareStatement(sqlQuery.toString());
			GrupoPerfilTO grupoPerfilTO;
			rs = ps.executeQuery();
			while (rs.next()){
				grupoPerfilTO = new GrupoPerfilTO();
				grupoPerfilTO.setId(rs.getString("ID_PRIVILEGIO"));
				grupoPerfilTO.setDescPrivilegio(rs.getString("DESC_PRIVILEGIO"));
				lista.add(grupoPerfilTO);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally{
			bd.close(connection, rs, ps);
		}
		return lista;
	}

	@Override
	public GrupoPerfilTO getGrupo(int idGrupo) throws JdbcDaoException {
		MyLogger.Logger.log(Level.INFO, "--getGrupos DAO--");
		MyLogger.Logger.log(Level.INFO, "IMPRIME ID PERSONA EN DAO: "+idGrupo);
		ConexionBD bd = new ConexionBD();
		StringBuffer sqlQuery = new StringBuffer("");
		sqlQuery.append(" SELECT   ID_GRUPO, DESC_GRUPO FROM RUG_GRUPOS WHERE ID_GRUPO = ? "); 
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection= null;
		GrupoPerfilTO grupoPerfilTO=null;
		try {
			connection= bd.getConnection();
			ps = connection.prepareStatement(sqlQuery.toString());
			ps.setInt(1, idGrupo);
			rs = ps.executeQuery();
			grupoPerfilTO = new GrupoPerfilTO();
			if (rs.next()){
				grupoPerfilTO.setId(rs.getString("ID_GRUPO"));
				grupoPerfilTO.setDescripcion(rs.getString("DESC_GRUPO"));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally{
			bd.close(connection, rs, ps);
		}
		return grupoPerfilTO;
	}
	
	public List<GrupoPerfilTO> getGrupos(int idAcreedor) throws JdbcDaoException {
		MyLogger.Logger.log(Level.INFO, "--getGrupos DAO--");
		MyLogger.Logger.log(Level.INFO, "IMPRIME ID PERSONA EN DAO: "+idAcreedor);
		ConexionBD bd = new ConexionBD();
		StringBuffer sqlQuery = new StringBuffer("");
		sqlQuery.append(" SELECT ");
		sqlQuery.append(" DISTINCT ID_GRUPO, ");
		sqlQuery.append(" DESC_GRUPO ");
		sqlQuery.append(" FROM V_GRUPOS ");
		sqlQuery.append(" WHERE ID_ACREEDOR ="+idAcreedor); 
		List <GrupoPerfilTO> lista = new ArrayList<GrupoPerfilTO>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection= null;
		try {
			connection= bd.getConnection();
			ps = connection.prepareStatement(sqlQuery.toString());
			GrupoPerfilTO grupoPerfilTO;
			rs = ps.executeQuery();
			while (rs.next()){
				grupoPerfilTO = new GrupoPerfilTO();
				grupoPerfilTO.setId(rs.getString("ID_GRUPO"));
				grupoPerfilTO.setDescripcion(rs.getString("DESC_GRUPO"));
				lista.add(grupoPerfilTO);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally{
		bd.close(connection, rs, ps);
		}
		return lista;
	}

	public class GruposBRowMapper implements RowMapper {
		@Override
		public Object mapRow(ResultSet resultSet, int line) throws SQLException {
			GruposResultSetExtractor extractor = new GruposResultSetExtractor();
			return extractor.extractData(resultSet);
		}
	}

	public class GruposResultSetExtractor implements ResultSetExtractor {
		@Override
		public Object extractData(ResultSet resultSet) throws SQLException,
				DataAccessException {
			GrupoPerfilTO grupo = new GrupoPerfilTO();
			grupo.setId(resultSet.getString("ID_GRUPO"));
			grupo.setDescripcion(resultSet.getString("DESC_GRUPO"));
			return grupo;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PersonaFisica> getUsuariosAfiliados(Integer idUsarioPadre )
			throws JdbcDaoException {
		logger.debug("--Metodo busqueda dao afiliados--");
		StringBuffer sqlQuery = new StringBuffer("");
		sqlQuery.append("select ID_TRAMITE_TEMP,ID_STATUS_TRAM, NOMBRE_SUBUSUARIO||APELLIDO_PATERNO_SUBUSUARIO||AP_MATERNO_LOGIN as nombre,CVE_USUARIO_SUB,GRUPO_USUARIO_SUB  from v_usuario_firma_todos where USUARIO_LOGIN= ? ");
		Object[] params = new Object[] { idUsarioPadre};
		try {
			return (List<PersonaFisica>) getJdbcTemplate().query(
					sqlQuery.toString(), params, new AfiliadosRowMapper());
		} catch (Exception e) {
			throw new JdbcDaoException(e);
		}
	}

	public class AfiliadosRowMapper implements RowMapper {
		@Override
		public Object mapRow(ResultSet resultSet, int line) throws SQLException {
			logger.debug("--Metodo busqueda dao afiliados 2--");
			AfiliadosResultSetExtractor extractor = new AfiliadosResultSetExtractor();
			return extractor.extractData(resultSet);
		}
	}

	public class AfiliadosResultSetExtractor implements ResultSetExtractor {
		@Override
		public Object extractData(ResultSet resultSet) throws SQLException,
				DataAccessException {
			PersonaFisica persona = new PersonaFisica();
			DatosContacto datos = new DatosContacto();
			persona.setDatosContacto(datos);
			GrupoPerfilTO grupoPerfilTO = new GrupoPerfilTO();
			persona.setGrupo(grupoPerfilTO);
		    persona.getDatosContacto().setIdTramite(resultSet.getInt("ID_TRAMITE_TEMP"));
			persona.getDatosContacto().setIdStatus(resultSet.getInt("ID_STATUS_TRAM"));
			persona.getDatosContacto().setEmailPersonal(resultSet.getString("CVE_USUARIO_SUB"));
			persona.setNombre(resultSet.getString("nombre"));
			persona.getGrupo().setDescripcion(resultSet.getString("GRUPO_USUARIO_SUB"));
			return persona;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PerfilTO> getUsuariosPerfiles(String perfil) throws JdbcDaoException {
		StringBuffer sqlQuery = new StringBuffer("");
		MyLogger.Logger.log(Level.INFO, "IMPRIME LA CLAVE DEL USUARIO AUTENTICADO: --"+perfil+"--");		
		//ACREEDOR
		if(perfil.equals("ACREEDOR")){
			sqlQuery.append(" SELECT ");
			sqlQuery.append(" CVE_PERFIL, ");
			sqlQuery.append(" DESC_PERFIL ");
			sqlQuery.append(" FROM RUG_CAT_PERFILES ");
			sqlQuery.append(" WHERE CVE_PERFIL = 'OPERATIVO' OR CVE_PERFIL = 'ADMINISTRADOR' ");
		}
		
		//ADMINITRADOR
		if(perfil.equals("ADMINISTRADOR")){
			sqlQuery.append(" SELECT ");
			sqlQuery.append(" CVE_PERFIL, ");
			sqlQuery.append(" DESC_PERFIL ");
			sqlQuery.append(" FROM RUG_CAT_PERFILES ");
			sqlQuery.append(" WHERE CVE_PERFIL = 'OPERATIVO'");
		}

		if(perfil.equals("CIUDADANO")){
			sqlQuery.append(" SELECT ");
			sqlQuery.append(" CVE_PERFIL, ");
			sqlQuery.append(" DESC_PERFIL ");
			sqlQuery.append(" FROM RUG_CAT_PERFILES ");
			sqlQuery.append(" WHERE CVE_PERFIL = 'OPERATIVO' OR CVE_PERFIL = 'ADMINISTRADOR' OR CVE_PERFIL = 'CIUDADANO' ");
		}
		
		if(perfil.equals("OPERATIVO")){
			sqlQuery.append(" SELECT ");
			sqlQuery.append(" CVE_PERFIL, ");
			sqlQuery.append(" DESC_PERFIL ");
			sqlQuery.append(" FROM RUG_CAT_PERFILES ");
			sqlQuery.append(" WHERE CVE_PERFIL = 'OPERATIVO' ");
		}
		
		try {
			return (List<PerfilTO>) getJdbcTemplate().query(sqlQuery.toString(),
					new UsuariosPerfilesBRowMapper());
		} catch (Exception e) {
			throw new JdbcDaoException(e);
		}
	}

	public class UsuariosPerfilesBRowMapper implements RowMapper {
		@Override
		public Object mapRow(ResultSet resultSet, int line) throws SQLException {
			UsuariosPerfilesResultSetExtractor extractor = new UsuariosPerfilesResultSetExtractor();
			return extractor.extractData(resultSet);
		}
	}

	public class UsuariosPerfilesResultSetExtractor implements ResultSetExtractor {
		@Override
		public Object extractData(ResultSet resultSet) throws SQLException,
				DataAccessException {
			PerfilTO perfil = new PerfilTO();
			perfil.setClave(resultSet.getString("CVE_PERFIL"));
			perfil.setDescripcion(resultSet.getString("DESC_PERFIL"));
			return perfil;
		}
	}

	@Override
	public PersonaFisica getRegistro(String claveUsuario)
			throws JdbcDaoException {
		StringBuffer query = new StringBuffer();
		query.append(" SELECT CVE_ACREEDOR as claveAcreedor, CVE_USUARIO as claveUsuario,  ");
		query.append(" ID_PERSONA idPersona, NOMBRE_PERSONA nombre, AP_PATERNO apPaterno, AP_MATERNO apMaterno, RFC rfc, SIT_USUARIO situacion, NUM_SERIE, ");
		query.append(" CURP_DOC docId, ");
		query.append(" DECODE(per_juridica,'PF', 'Persona Individual', 'PM', 'Persona Jurídica') tipoPersona, ");
		query.append(" DECODE(inscrito, 'N', 'Nacional', 'E', 'Extranjero') inscritoComo, ");
		query.append(" preg_recupera_psw ");
		query.append(" FROM V_USUARIO_SESION_RUG ");
		query.append(" WHERE CVE_USUARIO = " + "'"+ claveUsuario + "'");
		

		try {
			return (PersonaFisica) getJdbcTemplate().query(query.toString(),
					new PersonaFisicaResultSetExtractor());
		} catch (Exception e) {
			throw new JdbcDaoException(e);
		}
	}

	public class PersonaFisicaResultSetExtractor implements ResultSetExtractor {
		@Override
		public Object extractData(ResultSet resultSet) throws SQLException,
				DataAccessException {
			PersonaFisica personaFisica = new PersonaFisica();
			if (resultSet.next()) {
				DatosContacto datos = new DatosContacto();
				personaFisica.setDatosContacto(datos);
				personaFisica.getDatosContacto().setEmailPersonal(resultSet.getString("claveUsuario"));
				personaFisica.setClaveAcreedor(resultSet.getString("claveAcreedor"));
				personaFisica.setIdPersona(resultSet.getInt("idPersona"));
				personaFisica.setNombre(resultSet.getString("nombre"));
				personaFisica.setApellidoPaterno(resultSet.getString("apPaterno"));
				personaFisica.setApellidoMaterno(resultSet.getString("apMaterno"));
				personaFisica.setRfc(resultSet.getString("rfc"));
				personaFisica.setSituacion(resultSet.getString("situacion"));
				personaFisica.setnSerieCert(resultSet.getString("NUM_SERIE"));
				personaFisica.setDocumentoIdentificacion(resultSet.getString("docId"));
				personaFisica.setTipoPersona(resultSet.getString("tipoPersona"));
				personaFisica.setNacionalidadInscrito(resultSet.getString("inscritoComo"));
			}
			return personaFisica;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PerfilTO> busquedaGrupo(PersonaFisica personaFisica)
			throws JdbcDaoException {
		MyLogger.Logger.log(Level.INFO, "--Metodo busqueda grupos dao--");
		StringBuffer sqlQuery = new StringBuffer("");
		sqlQuery.append(" SELECT ID_PRIVILEGIO as idPrivilegio, DESC_PRIVILEGIO as descPrivilegio ");
		sqlQuery.append(" FROM V_GRUPOS ");
		sqlQuery.append(" WHERE ID_GRUPO = ? ");

		Object[] params = new Object[] { personaFisica.getGrupo().getId()};
		try {
			return (List<PerfilTO>) getJdbcTemplate().query(
					sqlQuery.toString(), params, new ConsultaGruposRowMapper());
		} catch (Exception e) {
			throw new JdbcDaoException(e);
		}
	}

	public class ConsultaGruposRowMapper implements RowMapper {
		@Override
		public Object mapRow(ResultSet resultSet, int line) throws SQLException {
			ConsultaGruposResultSetExtractor extractor = new ConsultaGruposResultSetExtractor();
			return extractor.extractData(resultSet);
		}
	}

	public class ConsultaGruposResultSetExtractor implements ResultSetExtractor {
		@Override
		public Object extractData(ResultSet resultSet) throws SQLException,
				DataAccessException {
			PerfilTO perfilTO = new PerfilTO();
			perfilTO.setId(resultSet.getString("idPrivilegio"));
			perfilTO.setDescripcion(resultSet.getString("descPrivilegio"));
			return perfilTO ;
		}
	}

	@Override
	public PersonaFisica getRegistroByToken(String token) throws JdbcDaoException {
		StringBuffer query = new StringBuffer();
		query.append(" SELECT CVE_ACREEDOR as claveAcreedor, CVE_USUARIO as claveUsuario,  ");
		query.append(" ID_PERSONA idPersona, NOMBRE_PERSONA nombre, AP_PATERNO apPaterno, AP_MATERNO apMaterno, RFC rfc, SIT_USUARIO situacion, NUM_SERIE, ");
		query.append(" CURP_DOC AS docId, PER_JURIDICA AS tipoPersona, INSCRITO AS inscritoComo FROM V_USUARIO_SESION_RUG ");
		query.append(" WHERE TOKEN = '" + token + "'");

		try {
			return (PersonaFisica) getJdbcTemplate().query(query.toString(), new PersonaFisicaResultSetExtractor());
		} catch (Exception e) {
			throw new JdbcDaoException(e);
		}
	}

}
