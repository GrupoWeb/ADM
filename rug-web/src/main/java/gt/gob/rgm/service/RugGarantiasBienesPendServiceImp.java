package gt.gob.rgm.service;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import gt.gob.rgm.util.FormTramites;
import gt.gob.rgm.util.ReporteTramites;
import mx.gob.economia.dgi.framework.dao.exception.JdbcDaoException;
import mx.gob.se.rug.dao.ConexionBD;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Scope;

import gt.gob.rgm.model.RugGarantiasBienesPend;
import gt.gob.rgm.dao.RugGarantiasBienesPendRepository;
import org.springframework.expression.spel.ast.NullLiteral;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

@Scope("prototype")
public class RugGarantiasBienesPendServiceImp implements RugGarantiasBienesPendService{
	static String SHEET = "Tramites";


    private RugGarantiasBienesPendRepository bienDao;

//	private RugTramitesRepository tramitesDao;

    public List<RugGarantiasBienesPend> getValorBienesByTramiteTemp(Integer idTramiteTemp){
		return bienDao.findByTramiteTemp(idTramiteTemp);
	} 

    public void setBienDao(RugGarantiasBienesPendRepository bienDao) {
		this.bienDao = bienDao;
	}

//	public void setTramitesDao(RugTramitesRepository tramitesDao){ this.tramitesDao = tramitesDao; }

	public List<String> getTramitesByGarantia(Integer idGarantia){
		return bienDao.getBienesByGarantia(idGarantia);
	}
    

	public List<String> getBienesTramites(Integer idTramite){
		return bienDao.getBienesTramite(idTramite);
	}


	public List<String> getCountFacturasByTramite(Integer idTramite){ return bienDao.getCountFacByTramite(idTramite);}

	public List<String> getCountFacturaByGarantia(Integer idGarantia){ return bienDao.getCountFacturaByGarantia(idGarantia);}

	/**
	 * @param idUsuario
	 * @return
	 */
	@Override
	public Integer getPadre(Integer idUsuario) {
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String query = "SELECT (CASE WHEN S.CVE_USUARIO_PADRE IS NULL THEN ID_PERSONA ELSE (SELECT ID_PERSONA FROM RUG_SECU_USUARIOS WHERE CVE_USUARIO = S.CVE_USUARIO_PADRE)  END) AS CODIGO\n" +
				"                    FROM RUG_SECU_USUARIOS S WHERE S.ID_PERSONA = ?";
		try{
			connection = bd.getConnection();
			ps = connection.prepareStatement(query);
			ps.setInt(1, idUsuario);
			rs = ps.executeQuery();
			if (rs.next()) {
				Integer cuenta = rs.getInt("CODIGO");
				return cuenta;

			}
		}catch (Exception e){
			e.printStackTrace();
			throw new JdbcDaoException(e);
		}finally {
			bd.close(connection, rs, ps);
		}
		return null;
	}

	/**
	 * @param idUsuario
	 * @param anio
	 * @return
	 */
	@Override
	public List<ReporteTramites> getTramites(Integer idUsuario, String anio) {
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sqlTramites = "SELECT T.GARANTIA, Translate(T.DESCRIPCION,'¡·…ÈÕÌ”Û⁄˙','AaEeIiOoUu') AS DESCRIPCION, Translate(T.PERSONA,'¡·…ÈÕÌ”Û⁄˙','AaEeIiOoUu') AS PERSONA, CONCAT(CONCAT('Q ',T.PRECIO),'.00') PRECIO, T.FECHA FROM (SELECT TRA.ID_GARANTIA GARANTIA, TRA.DESCRIPCION, PF.NOMBRE_PERSONA PERSONA, TRA.PRECIO, TRA.FECHA\n" +
				"        FROM RUG.V_TRAMITES_PAGADOS TRA\n" +
				"        INNER JOIN RUG_PERSONAS_FISICAS PF\n" +
				"            ON TRA.ID_PERSONA_LOGIN = PF.ID_PERSONA\n" +
				"        WHERE TRA.ID_PERSONA_LOGIN = ?\n" +
				"        ORDER BY TRA.ID_TRAMITE DESC) T \n" +
				"       WHERE TO_DATE(T.FECHA, 'dd/MM/YYYY hh24:mi:ss') BETWEEN TO_DATE('"+ anio +"-01-01 00:00:00', 'YYYY-MM-DD hh24:mi:ss') AND TO_DATE('"+ anio +"-12-31 23:59:59','YYYY-MM-DD hh24:mi:ss')";
		try{
			List<ReporteTramites> lista = new ArrayList<>();
			connection = bd.getConnection();
			ps = connection.prepareStatement(sqlTramites);
			ps.setInt(1, getPadre(idUsuario));
			rs = ps.executeQuery();
			while(rs.next()){
				ReporteTramites rTramites = new ReporteTramites();
				rTramites.setGarantia(rs.getString("GARANTIA"));
				rTramites.setDescripcion(rs.getString("DESCRIPCION"));
				rTramites.setFecha(rs.getString("FECHA"));
				rTramites.setPersona(rs.getString("PERSONA"));
				rTramites.setPrecio(rs.getString("PRECIO"));
				lista.add(rTramites);
			}
			return lista;
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			bd.close(connection, rs, ps);
		}
		return new ArrayList<>();
	}

	/**
	 * @param idUsuario
	 * @param anio
	 * @return
	 */
	@Override
	public List<ReporteTramites> getTramitesNew(Integer idUsuario, String anio) {
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sqlTramites = "SELECT T.GARANTIA, Translate(T.DESCRIPCION,'¡·…ÈÕÌ”Û⁄˙','AaEeIiOoUu') AS DESCRIPCION, Translate(T.PERSONA,'¡·…ÈÕÌ”Û⁄˙','AaEeIiOoUu') AS PERSONA, CONCAT(CONCAT('Q ',T.PRECIO),'.00') PRECIO, T.FECHA FROM (SELECT TRA.ID_GARANTIA GARANTIA, TRA.DESCRIPCION, PF.NOMBRE_PERSONA PERSONA, TRA.PRECIO, TRA.FECHA\n" +
				"        FROM RUG.V_TRAMITES_PAGADOS TRA\n" +
				"        INNER JOIN RUG_PERSONAS_FISICAS PF\n" +
				"            ON TRA.ID_PERSONA_LOGIN = PF.ID_PERSONA\n" +
				"        WHERE TRA.ID_PERSONA_LOGIN = ? \n" +
				"        ORDER BY TRA.ID_TRAMITE DESC) T  \n" +
				"       WHERE TO_DATE(T.FECHA, 'dd/MM/YYYY hh24:mi:ss') BETWEEN TO_DATE('"+ anio +"-01-01 00:00:00', 'YYYY-MM-DD hh24:mi:ss') AND TO_DATE('"+ anio +"-12-31 23:59:59','YYYY-MM-DD hh24:mi:ss')";
		try{
			List<ReporteTramites> lista = new ArrayList<>();
			connection = bd.getConnection();
			ps = connection.prepareStatement(sqlTramites);
			ps.setInt(1, getPadre(idUsuario));
			rs = ps.executeQuery();
			while(rs.next()){
				ReporteTramites rTramites = new ReporteTramites();
				rTramites.setGarantia(rs.getString("GARANTIA"));
				rTramites.setDescripcion(rs.getString("DESCRIPCION"));
				rTramites.setFecha(rs.getString("FECHA"));
				rTramites.setPersona(rs.getString("PERSONA"));
				rTramites.setPrecio(rs.getString("PRECIO"));
				lista.add(rTramites);
			}
			return lista;
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			bd.close(connection, rs, ps);
		}
		return new ArrayList<>();
	}

	/**
	 * @param idUsuario
	 * @return
	 */
	@Override
	public List<FormTramites> getAnioByUser(Integer idUsuario) {
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String query = "SELECT t.anio ANIO from V_TRAMITES_PAGADOS t WHERE t.ID_PERSONA_LOGIN = ? GROUP BY t.ANIO ORDER BY t.ANIO DESC";
		try{
			List<FormTramites> lista = new ArrayList<>();
			connection = bd.getConnection();
			ps = connection.prepareStatement(query);
			ps.setInt(1, getPadre(idUsuario));
			rs = ps.executeQuery();
			while(rs.next()) {
				FormTramites rTramites = new FormTramites();
				rTramites.setAnio(rs.getString("ANIO"));
				lista.add(rTramites);

			}
			return lista;
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			bd.close(connection, rs, ps);
		}
		return new ArrayList<>();
	}

	/**
	 * @return
	 */
	@Override
	public XSSFWorkbook getDataExcel(Integer idUsuario, String anio) {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(SHEET);

			// Header
			List<ReporteTramites> tramite = getTramites(idUsuario, anio);

			int i = 1;
			if(tramite != null){
				XSSFRow Row = sheet.createRow(0);
				XSSFCell cell = Row.createCell(0);
				cell.setCellValue("Numero de Garantia");
				cell = Row.createCell(1);
				cell.setCellValue("Tramite");
				cell = Row.createCell(2);
				cell.setCellValue("Fecha");
				cell = Row.createCell(3);
				cell.setCellValue("Precio");
				cell = Row.createCell(4);
				cell.setCellValue("Usuario");
				for (ReporteTramites tr : tramite) {
					Row = sheet.createRow(i);
					cell = Row.createCell(0);
					cell.setCellValue(tr.getGarantia());
					cell = Row.createCell(1);
					cell.setCellValue(tr.getDescripcion());
					CellStyle cellStyle = workbook.createCellStyle();
					CreationHelper createHelper = workbook.getCreationHelper();
					cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy hh:mm:ss"));
					cell = Row.createCell(2);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(tr.getFecha());
					cell = Row.createCell(3);
					cell.setCellValue(tr.getPrecio());
					cell = Row.createCell(4);
					cell.setCellValue(tr.getPersona());
					i++;
				}

			}



			return workbook;
	}


//	public List<String> getTramitesReporte(Integer idUsuario){ return tramitesDao.findTramitesRealizadosByUsuario(idUsuario);}
}
