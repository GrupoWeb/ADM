package mx.gob.se.rug.util.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.util.to.CodigoPostalTO;
import mx.gob.se.rug.util.to.ColoniaTO;
import mx.gob.se.rug.util.to.EstadoTO;
import mx.gob.se.rug.util.to.LocalidadTO;
import mx.gob.se.rug.util.to.MunicipioTO;

public class CodigoPostalDAO {

	public CodigoPostalTO getByCodigoPostal(String codigoPostal) {
		CodigoPostalTO codigoPostalTO = null;
		String sql = "SELECT distinct cve_estado, cve_municip_deleg"
				+ " FROM SE_CAT_COLONIAS WHERE CODIGO_POSTAL = ? ";
		ConexionBD conexionBD = new ConexionBD();
		Connection connection = conexionBD.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, codigoPostal);
			rs = ps.executeQuery();
			if (rs.next()) {
				codigoPostalTO = new CodigoPostalTO();
				codigoPostalTO.setCve_estado(rs.getString("cve_estado"));
				codigoPostalTO.setCve_municipio(rs
						.getString("cve_municip_deleg"));
				codigoPostalTO.setEstadoTOs(getEstadoByCve());
				codigoPostalTO.setMunicipioTOs(getMunicipiosByCve(rs
						.getString("cve_estado")));
				codigoPostalTO.setColoniaTOs(getColoniasByCP(codigoPostal));
				codigoPostalTO
						.setLocalidadTOs(getLocalidadesByCP(codigoPostal));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			conexionBD.close(connection, rs, ps);
		}
		return codigoPostalTO;
	}

	public CodigoPostalTO getCodigoPostalByIdColonia(Integer idColonia) {
		CodigoPostalTO codigoPostalTO = null;
		String sql = "SELECT CODIGO_POSTAL FROM RUG.V_SE_CAT_COLONIAS_RUG WHERE ID_COLONIA=?";
		ConexionBD conexionBD = new ConexionBD();
		Connection connection = conexionBD.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idColonia);
			rs = ps.executeQuery();
			if (rs.next()) {
				codigoPostalTO = new CodigoPostalTO();
				codigoPostalTO.setCodigoPostal(rs.getString("CODIGO_POSTAL"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			conexionBD.close(connection, rs, ps);
		}
		return codigoPostalTO;
	}

	public CodigoPostalTO getCodigoPostalByIdLocalidad(Integer idLocalidad) {
		CodigoPostalTO codigoPostalTO = null;
		String sql = "SELECT CODIGO_POSTAL FROM RUG.V_SE_CAT_LOCALIDADES_RUG  WHERE ID_LOCALIDAD=?";
		ConexionBD conexionBD = new ConexionBD();
		Connection connection = conexionBD.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idLocalidad);
			rs = ps.executeQuery();
			if (rs.next()) {
				codigoPostalTO = new CodigoPostalTO();
				codigoPostalTO.setCodigoPostal(rs.getString("CODIGO_POSTAL"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			conexionBD.close(connection, rs, ps);
		}
		return codigoPostalTO;
	}

	public List<LocalidadTO> getLocalidadesByCP(String codigoPostal) {
		List<LocalidadTO> lista = new ArrayList<LocalidadTO>();
		String sql = "SELECT ID_LOCALIDAD AS idLocalidad, "
				+ "CVE_LOCALIDAD AS cveLocalidad, "
				+ "UPPER(DESC_LOCALIDAD_CP) AS descLocalidad, "
				+ " CVE_PAIS AS cvePais,"
				+ " CVE_MUNICIP_DELEG AS cveMunicipDeleg,"
				+ " CVE_ESTADO AS cveEstado, "
				+ " CODIGO_POSTAL AS codigoPostal FROM RUG.V_SE_CAT_LOCALIDADES_RUG "
				+ " WHERE CODIGO_POSTAL = ? ";
		ConexionBD conexionBD = new ConexionBD();
		Connection connection = conexionBD.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, codigoPostal);
			rs = ps.executeQuery();
			LocalidadTO localidadTO;
			while (rs.next()) {
				localidadTO = new LocalidadTO();
				localidadTO.setIdLocalidad(rs.getInt("idLocalidad"));
				localidadTO.setDescLocalidad(rs.getString("descLocalidad"));
				lista.add(localidadTO);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			conexionBD.close(connection, rs, ps);
		}
		return lista;
	}

	public List<LocalidadTO> getLocalidadesByMunicipio(Integer idMunicipio,
			String idEstado) {
		List<LocalidadTO> lista = new ArrayList<LocalidadTO>();
		String sql = "SELECT ID_LOCALIDAD AS idLocalidad, "
				+ "CVE_LOCALIDAD AS cveLocalidad, "
				+ "UPPER(DESC_LOCALIDAD_CP) AS descLocalidad, "
				+ " CVE_MUNICIP_DELEG AS cveMunicipDeleg"
				+ " FROM RUG.V_SE_CAT_LOCALIDADES_RUG "
				+ " WHERE CVE_MUNICIP_DELEG = ? and CVE_ESTADO=? ";
		ConexionBD conexionBD = new ConexionBD();
		Connection connection = conexionBD.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idMunicipio);
			ps.setString(2, idEstado);
			rs = ps.executeQuery();
			LocalidadTO localidadTO;
			while (rs.next()) {
				localidadTO = new LocalidadTO();
				localidadTO.setIdLocalidad(rs.getInt("idLocalidad"));
				localidadTO.setDescLocalidad(rs.getString("descLocalidad"));
				lista.add(localidadTO);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			conexionBD.close(connection, rs, ps);
		}
		return lista;
	}

	public List<ColoniaTO> getColoniasByCP(String codigoPostal) {
		List<ColoniaTO> lista = new ArrayList<ColoniaTO>();
		String sql = "SELECT cve_estado, cve_municip_deleg, id_colonia, UPPER(desc_colonia) AS desc_colonia"
				+ " FROM RUG.V_SE_CAT_COLONIAS_RUG WHERE CODIGO_POSTAL = ? ";
		ConexionBD conexionBD = new ConexionBD();
		Connection connection = conexionBD.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, codigoPostal);
			rs = ps.executeQuery();
			ColoniaTO coloniaTO;
			while (rs.next()) {
				coloniaTO = new ColoniaTO();
				coloniaTO.setIdColonia(rs.getInt("id_colonia"));
				coloniaTO.setDescColonia(rs.getString("desc_colonia"));
				lista.add(coloniaTO);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			conexionBD.close(connection, rs, ps);
		}
		return lista;
	}

	public List<ColoniaTO> getColoniasByMunicipio(Integer idMunicipio,
			String idEstado) {
		List<ColoniaTO> lista = new ArrayList<ColoniaTO>();
		String sql = "SELECT cve_estado, cve_municip_deleg, id_colonia, UPPER(desc_colonia) AS desc_colonia"
				+ " FROM RUG.V_SE_CAT_COLONIAS_RUG WHERE cve_municip_deleg = ? and cve_estado=? ";
		ConexionBD conexionBD = new ConexionBD();
		Connection connection = conexionBD.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idMunicipio);
			ps.setString(2, idEstado);
			rs = ps.executeQuery();
			ColoniaTO coloniaTO;
			while (rs.next()) {
				coloniaTO = new ColoniaTO();
				coloniaTO.setIdColonia(rs.getInt("id_colonia"));
				coloniaTO.setDescColonia(rs.getString("desc_colonia"));
				lista.add(coloniaTO);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			conexionBD.close(connection, rs, ps);
		}
		return lista;
	}

	public List<EstadoTO> getEstadoByCve() {
		List<EstadoTO> estadoTOs = new ArrayList<EstadoTO>();
		String sql = "SELECT CVE_PAIS,DESC_ESTADO,ID_ESTADO,CVE_ESTADO "
				+ " FROM RUG.V_SE_CAT_ESTADOS_RUG WHERE CVE_PAIS = ? ";
		ConexionBD conexionBD = new ConexionBD();
		Connection connection = conexionBD.getConnection();
		EstadoTO estadoTO = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, "MEX");
			rs = ps.executeQuery();

			while (rs.next()) {
				estadoTO = new EstadoTO();
				estadoTO.setDescEstado(rs.getString("DESC_ESTADO"));
				estadoTO.setIdEstado(rs.getString("CVE_ESTADO"));
				estadoTOs.add(estadoTO);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			conexionBD.close(connection, rs, ps);
		}

		return estadoTOs;
	}

	public List<MunicipioTO> getMunicipiosByCve(String cveEstado) {
		List<MunicipioTO> municipioTOs = new ArrayList<MunicipioTO>();
		String sql = "SELECT CVE_ESTADO AS cveEstado, "
				+ " CVE_MUNICIP_DELEG AS cveMunicipDeleg, DESC_MUNICIP_DELEG AS descMunicipDeleg"
				+ " FROM RUG.V_SE_CAT_MUNICIP_DELEG_RUG "
				+ " WHERE CVE_ESTADO = ? ";
		ConexionBD conexionBD = new ConexionBD();
		Connection connection = conexionBD.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, cveEstado);
			rs = ps.executeQuery();

			MunicipioTO municipioTO = null;
			while (rs.next()) {
				municipioTO = new MunicipioTO();
				municipioTO.setDescMunicipio(rs.getString("descMunicipDeleg"));
				municipioTO.setIdMunicipio(rs.getInt("cveMunicipDeleg"));
				municipioTO.setClaveEstado(rs.getString("cveEstado"));
				municipioTOs.add(municipioTO);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			conexionBD.close(connection, rs, ps);
		}
		return municipioTOs;
	}

}
