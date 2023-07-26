/*
 * HomeDaoJdbcImpl.java        01/08/2010
 *
 * Copyright (c) 2009 Secretaría de Economía
 * Alfonso Reyes No. 30 Col. Hipódromo Condesa C.P. 06140,
 * Delegación Cuauhtémoc, México, D.F.
 * Todos los Derechos Reservados.
 *
 * Este software es confidencial y de uso exclusivo de la
 * Secretaría de Economía.
 *
 */
package mx.gob.se.rug.common.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import mx.gob.economia.dgi.framework.dao.exception.DaoException;
import mx.gob.economia.dgi.framework.dao.exception.JdbcDaoException;
import mx.gob.economia.dgi.framework.security.user.dto.Citizen;
import mx.gob.economia.dgi.framework.security.user.dto.Credentials;
import mx.gob.economia.dgi.framework.security.user.dto.Profile;
import mx.gob.economia.dgi.framework.security.user.dto.Role;
import mx.gob.economia.dgi.framework.security.user.dto.User;
import mx.gob.economia.dgi.framework.spring.jdbc.AbstractBaseJdbcDao;
import mx.gob.economia.dgi.framework.util.UserUtil;
import mx.gob.se.rug.common.constants.AllowedRoles;
import mx.gob.se.rug.common.dao.HomeDao;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author Alfonso Esquivel
 * 
 */
public class HomeDaoJdbcImpl extends AbstractBaseJdbcDao implements HomeDao {

	@Override
	public User getUser(final String principal) throws DaoException {
		logger.debug("-- getUser --");
		logger.debug("principal: " + principal);

		try {
			StringBuffer sqlQuery = new StringBuffer("");
			sqlQuery
					.append("SELECT DISTINCT CVE_USUARIO as cveUsuario, ID_PERSONA as idUsuario, ");
			sqlQuery
					.append("NOMBRE_PERSONA as nombre, AP_PATERNO as apellidoPaterno, AP_MATERNO as apellidoMaterno ");
			sqlQuery.append("FROM V_USUARIO_SESION_RUG ");
			sqlQuery.append("WHERE CVE_USUARIO = ?");
			logger.debug("sqlQuery: " + sqlQuery);

			User user = (User) getJdbcTemplate().queryForObject(
					sqlQuery.toString(), new Object[] { principal },
					new UserRowMapper());
			logger.debug("user=" + user);

			user.setRoles(getRole(principal));
			logger.debug("user=" + user);
			
			UserUtil userUtil = new UserUtil();
			if(userUtil.isUserInRole(user, AllowedRoles.AuxiliarFedatario.getRole())){
				String parentPrincipal = getParentPrincipal(user);
				User parent = getUser(parentPrincipal);
				user.setParent(parent);
			}
			return user;
		} catch (Exception e) {
			throw new JdbcDaoException(e);
		}
	}

	private String getParentPrincipal(User user) throws Exception{
		StringBuffer sqlQuery = new StringBuffer("");
		sqlQuery.append("SELECT 'SE'||CVE_USUARIO_PADRE as cveUsuarioPadre ");
		sqlQuery.append("FROM V_SECU_REL_USUARIOS ");
		sqlQuery.append("WHERE CVE_USUARIO = ? ");
		logger.debug("sqlQuery: " + sqlQuery);
		String parentPrincipal = (String)getJdbcTemplate().queryForObject(
				sqlQuery.toString(), new Object[] { user.getCredentials().getPrincipal() }, new StringRowMapper());
		logger.debug("parentPrincipal: " + parentPrincipal);
		return parentPrincipal;
	}
	
	public class StringRowMapper implements RowMapper {
		@Override
		public Object mapRow(ResultSet rs, int line) throws SQLException {
			String principal = rs.getString("cveUsuarioPadre");
			return principal;
		}
	}

	@SuppressWarnings("unchecked")
	public Set<Role> getRole(final String principal) throws DaoException {
		logger.debug("-- getRole --");
		logger.debug("principal: " + principal);

		Set<Role> roles = null;
		StringBuffer sqlQuery = new StringBuffer("");
		sqlQuery.append("SELECT CVE_PERFIL as rol ");
		sqlQuery.append("FROM V_USUARIO_SESION_RUG ");
		sqlQuery.append("WHERE CVE_USUARIO = ?");
		logger.debug("sqlQuery: " + sqlQuery);

		try {
			List<Role> listaRoles = (List<Role>) getJdbcTemplate().query(
					sqlQuery.toString(), new Object[] { principal },
					new RoleRowMapper());
			logger.debug("listaRoles=" + listaRoles);

			roles = new LinkedHashSet<Role>(listaRoles);
			logger.debug("roles=" + roles);

			return roles;
		} catch (Exception e) {
			throw new JdbcDaoException(e);
		}

	}

	public class UserRowMapper implements RowMapper {
		@Override
		public Object mapRow(ResultSet rs, int line) throws SQLException {

			Credentials credentials = new Credentials(rs
					.getString("cveUsuario"));
			User user = new User(credentials);
			user.setIdUser(rs.getString("idUsuario"));
			Profile profile = new Citizen();
			profile.setNombre(rs.getString("nombre"));
			profile.setApellidoPaterno(rs.getString("apellidoPaterno"));
			profile.setApellidoMaterno(rs.getString("apellidoMaterno"));
			user.setProfile(profile);
			return user;
		}
	}

	public class RoleRowMapper implements RowMapper {

		@Override
		public Object mapRow(ResultSet rs, int line) throws SQLException {
			RoleResultSetExtractor rrse = new RoleResultSetExtractor();
			return rrse.extractData(rs);
		}
	}

	public class RoleResultSetExtractor implements ResultSetExtractor {

		@Override
		public Object extractData(ResultSet rs) throws SQLException,
				DataAccessException {
			Role role = new Role(rs.getString("rol"));
			return role;
		}

	}

}
