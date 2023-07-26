package gt.gob.rgm.rs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import gt.gob.rgm.service.RugGarantiasBienesPendService;
import gt.gob.rgm.util.FormTramites;
import gt.gob.rgm.util.ReporteTramites;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.to.AccionTO;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Component
@Path("/tramites")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class TramitesRs {

    private RugGarantiasBienesPendService rugGarantiasBienesPendService;

    public TramitesRs(){
        rugGarantiasBienesPendService = (RugGarantiasBienesPendService) SpringApplicationContext.getBean("rugGarantiasBienesPendService");
    }

    @POST
    @Path("/costo-reporte")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AccionTO>  getCostoTramite(FormTramites tramites){
        String json="";
        ConexionBD bd = new ConexionBD();
        Connection connection = null;
        PreparedStatement psTramites = null;
        ResultSet rsTramites = null;
        String sqlTramites = "SELECT ID_GARANTIA, Translate(DESCRIPCION,'¡·…ÈÕÌ”Û⁄˙','AaEeIiOoUu') AS DESCRIPCION, Translate(NOMBRE_PERSONA,'¡·…ÈÕÌ”Û⁄˙','AaEeIiOoUu') AS NOMBRE_PERSONA, PRECIO, FECHA, row_number  FROM ( " +
                "SELECT SUB.*, PF.NOMBRE_PERSONA, ROW_NUMBER() OVER (ORDER BY TO_DATE(SUB.FECHA, 'dd/MM/YYYY hh24:mi:ss')) AS row_number FROM ( " +
                "	SELECT ID_GARANTIA, DESCRIPCION, PRECIO, FECHA, ID_PERSONA_LOGIN " +
                "	FROM RUG.V_TRAMITES_PAGADOS TRA " +
                "	WHERE ID_PERSONA_LOGIN IN (" + tramites.getIdUsuario() + ") " +
                ") SUB, RUG_PERSONAS_FISICAS PF " +
                "WHERE SUB.ID_PERSONA_LOGIN = PF.ID_PERSONA " +
                "AND TO_DATE(SUB.FECHA, 'dd/MM/YYYY hh24:mi:ss') BETWEEN TO_DATE('"+ tramites.getFechaInicial() +" 00:00:00', 'YYYY-MM-DD hh24:mi:ss') AND TO_DATE('"+ tramites.getFechaFinal() +" 23:59:59', 'YYYY-MM-DD hh24:mi:ss') order by row_number desc";

        if(!tramites.getNombre().isEmpty()) {
            sqlTramites += " AND (UPPER(descripcion) LIKE '%" + tramites.getNombre().toUpperCase() + "%' " +
                    "OR UPPER(nombre_persona) LIKE '%" + tramites.getNombre().toUpperCase() + "%')";
        }
        sqlTramites += ")";

        try {
            System.out.println(" SQL Tabla Tramites Realizados " + sqlTramites);
            List<AccionTO> lista = new ArrayList<>();
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
            connection = bd.getConnection();
            psTramites = connection.prepareStatement(sqlTramites);
            rsTramites = psTramites.executeQuery();
            while(rsTramites.next()) {
                AccionTO accion = new AccionTO();
                mx.gob.se.rug.to.GarantiaTO garantia = new mx.gob.se.rug.to.GarantiaTO();
                garantia.setRow(rsTramites.getInt("row_number"));
                garantia.setIdgarantia(rsTramites.getInt("ID_GARANTIA"));
                garantia.setDescripcion(rsTramites.getString("DESCRIPCION"));
                mx.gob.se.rug.to.PersonaTO usuario = new mx.gob.se.rug.to.PersonaTO();
                usuario.setNombre(rsTramites.getString("NOMBRE_PERSONA"));
                accion.setGarantia(garantia);
                accion.setUsuario(usuario);
//                accion.setValor(rsTramites.getString("PRECIO"));
                accion.setImporte(rsTramites.getDouble("PRECIO"));
                accion.setFechaRegistro(rsTramites.getString("FECHA"));

                lista.add(accion);
            }
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bd.close(connection, rsTramites, psTramites);
        }

        return new ArrayList<>();
    }


    @POST
    @Path("/costo-reported")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AccionTO>  getCostoTramited(FormTramites tramites){
        String json="";
        ConexionBD bd = new ConexionBD();
        Connection connection = null;
        PreparedStatement psTramites = null;
        ResultSet rsTramites = null;
        String sqlTramites = "SELECT ID_GARANTIA, Translate(DESCRIPCION,'¡·…ÈÕÌ”Û⁄˙','AaEeIiOoUu') AS DESCRIPCION, Translate(NOMBRE_PERSONA,'¡·…ÈÕÌ”Û⁄˙','AaEeIiOoUu') AS NOMBRE_PERSONA, PRECIO, FECHA, row_number FROM ( " +
                "SELECT SUB.*, PF.NOMBRE_PERSONA, ROW_NUMBER() OVER (ORDER BY TO_DATE(SUB.FECHA, 'dd/MM/YYYY hh24:mi:ss')) AS row_number FROM ( " +
                "	SELECT ID_GARANTIA, DESCRIPCION, PRECIO, FECHA, ID_PERSONA_LOGIN " +
                "	FROM RUG.V_TRAMITES_PAGADOS TRA " +
                "	WHERE ID_PERSONA_LOGIN IN (" + tramites.getIdUsuario() + ") " +
                ") SUB, RUG_PERSONAS_FISICAS PF " +
                "WHERE SUB.ID_PERSONA_LOGIN = PF.ID_PERSONA " +
                "AND TO_DATE(SUB.FECHA, 'dd/MM/YYYY hh24:mi:ss') BETWEEN TO_DATE('"+ tramites.getFechaInicial() +" 00:00:00', 'YYYY-MM-DD hh24:mi:ss') AND TO_DATE('"+ tramites.getFechaFinal() +" 23:59:59', 'YYYY-MM-DD hh24:mi:ss') order by row_number desc";

        if(!tramites.getNombre().isEmpty()) {
            sqlTramites += " AND (UPPER(descripcion) LIKE '%" + tramites.getNombre().toUpperCase() + "%' " +
                    "OR UPPER(nombre_persona) LIKE '%" + tramites.getNombre().toUpperCase() + "%')";
        }
        sqlTramites += ")";

        try {
            List<AccionTO> lista = new ArrayList<>();
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
            connection = bd.getConnection();
            psTramites = connection.prepareStatement(sqlTramites);
            rsTramites = psTramites.executeQuery();
            while(rsTramites.next()) {
                JSONObject row = new JSONObject();
                AccionTO accion = new AccionTO();
                mx.gob.se.rug.to.GarantiaTO garantia = new mx.gob.se.rug.to.GarantiaTO();
                garantia.setRow(rsTramites.getInt("row_number"));
                garantia.setIdgarantia(rsTramites.getInt("ID_GARANTIA"));
                garantia.setDescripcion(rsTramites.getString("DESCRIPCION"));
                mx.gob.se.rug.to.PersonaTO usuario = new mx.gob.se.rug.to.PersonaTO();
                usuario.setNombre(rsTramites.getString("NOMBRE_PERSONA"));
                accion.setGarantia(garantia);
                accion.setUsuario(usuario);
                accion.setImporte(rsTramites.getDouble("PRECIO"));
                try {
                    Date date = format.parse(rsTramites.getString("FECHA"));
                    accion.setFecha(date);
                } catch(ParseException ex) {
                    ex.printStackTrace();
                }
                lista.add(accion);
            }
            System.out.println("Resultad " + lista);
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bd.close(connection, rsTramites, psTramites);
        }

        return new ArrayList<>();
    }

    @POST
    @Path("/reporte")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<ReporteTramites>getTramitesCosto(FormTramites tramites){
        return rugGarantiasBienesPendService.getTramites(tramites.getIdUsuario(), tramites.getAnio());
    }

    @POST
    @Path("/reporte-anio")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<FormTramites> getAnio(FormTramites tramites){
        return rugGarantiasBienesPendService.getAnioByUser(tramites.getIdUsuario());
    }

    @POST
    @Path("/reporte-excel")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response reporteExcel(FormTramites tramites) throws IOException {
        XSSFWorkbook workbook = rugGarantiasBienesPendService.getDataExcel(tramites.getIdUsuario(), tramites.getAnio());
        ServletActionContext.getResponse().setHeader("Content-Disposition", "attachment; filename=Reporte.xlsx");
        ServletOutputStream out = ServletActionContext.getResponse().getOutputStream();
        workbook.write(out);
        out.flush();
        out.close();
        return Response.ok().build();
    }


}
