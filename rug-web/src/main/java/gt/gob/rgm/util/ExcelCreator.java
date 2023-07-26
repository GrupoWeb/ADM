package gt.gob.rgm.util;

import java.util.List;
import mx.gob.se.rug.common.util.NullsFree;
import mx.gob.se.rug.inscripcion.to.OtorganteTO;
import mx.gob.se.rug.operaciones.to.OperacionesTO;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import mx.gob.se.rug.to.AccionTO;

public class ExcelCreator {
	public XSSFWorkbook createTramitesWorkbook(List<AccionTO> tramites) throws Exception {
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("Tramites");

		int i = 1;
		if(tramites != null) {
			XSSFRow row = sheet.createRow(0);
			XSSFCell cell = row.createCell(0);
			cell.setCellValue("Nï¿½mero de Garantï¿½ï¿½a");
			cell = row.createCell(1);
			cell.setCellValue("Trï¿½mite");
			cell = row.createCell(2);
			cell.setCellValue("Fecha");
			cell = row.createCell(3);
			cell.setCellValue("Valor");
			cell = row.createCell(4);
			cell.setCellValue("Usuario");
			
			for(AccionTO tramite : tramites) {
				row = sheet.createRow(i);
				cell = row.createCell(0);
				cell.setCellValue(tramite.getGarantia().getIdgarantia());
				cell = row.createCell(1);
				cell.setCellValue(tramite.getGarantia().getDescripcion());
				CellStyle cellStyle = wb.createCellStyle();
				CreationHelper createHelper = wb.getCreationHelper();
				cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy hh:mm:ss"));
				cell = row.createCell(2);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(tramite.getFecha());
				cell = row.createCell(3);
				cell.setCellValue(tramite.getImporte());
				cell = row.createCell(4);
				cell.setCellValue(tramite.getUsuario().getNombre());
				i++;
			}
		}
		
		return wb;
	}
        
        public XSSFWorkbook createOperacionesWorkbook(List<OperacionesTO> operaciones) throws Exception {
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("Operaciones");

		int i = 1;
		if(operaciones != null) {
			XSSFRow row = sheet.createRow(0);
			XSSFCell cell = row.createCell(0);
			cell.setCellValue("Número de Operación");
			cell = row.createCell(1);
			cell.setCellValue("Transacción");
			cell = row.createCell(2);
			cell.setCellValue("Fecha");
			cell = row.createCell(3);
			cell.setCellValue("Número de Garaní­a");
                        cell = row.createCell(4);
			cell.setCellValue("Deudor");
                        cell = row.createCell(5);
			cell.setCellValue("Número de Identificación");
			cell = row.createCell(6);
			cell.setCellValue("Usuario");
		
                
                for(OperacionesTO operacion : operaciones) {
				row = sheet.createRow(i);
				cell = row.createCell(0);
				cell.setCellValue(operacion.getIdInscripcion());
                                cell = row.createCell(1);
				cell.setCellValue(operacion.getTipoTransaccion());
				CellStyle cellStyle = wb.createCellStyle();
				CreationHelper createHelper = wb.getCreationHelper();
				cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy hh:mm:ss"));
				cell = row.createCell(2);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(operacion.getFechaOperacionInicio());
				cell = row.createCell(3);
				cell.setCellValue(operacion.getNumGarantia());
                                
                            List<OtorganteTO> otorgantes = operacion.getOtorgantes();
                            
                                if(otorgantes.size() > 0){
                                    for(OtorganteTO otorgante: otorgantes){
                                        cell = row.createCell(4);
                                        cell.setCellValue(otorgante.getNombreCompleto());
                                        cell = row.createCell(5);
                                        cell.setCellValue(otorgante.getTipoPersona().equalsIgnoreCase("PF")?NullsFree.getNotNullValue(otorgante.getCurp()):NullsFree.getNotNullValue(otorgante.getRfc()));
                                    }
                                }
                                cell = row.createCell(6);
                                cell.setCellValue(operacion.getUsuario());
				
				i++;
			}
                
                }
		          System.out.println("excel" + wb);
		return wb;
	}
}
