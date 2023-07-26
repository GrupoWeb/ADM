package mx.gob.se.rug.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.exception.InfrastructureException;
import mx.gob.se.rug.exception.NoDateInfrastructureException;
import mx.gob.se.rug.masiva.to.TipoBienMueble;
import mx.gob.se.rug.util.to.DateUtilRug;
import oracle.sql.CLOB;

/**
 * 
 * 
 * @version 0.1
 * @author Abraham Stalin Aguilar Valencia
 * 
 */
public abstract class BaseRugDao {
	
	private Integer indexData;  
	
	public void setStringCS(CallableStatement cs, String cadena, int posicion)
			throws SQLException {
		if (cadena == null) {
			cs.setNull(posicion, Types.NULL);
		} else {
			cs.setString(posicion, cadena);

		}
	}

	public void setDoubleCS(CallableStatement cs, Double valor, int posicion)
			throws SQLException {
		if (valor == null) {
			cs.setNull(posicion, Types.NULL);
		} else {
			cs.setDouble(posicion, valor);
		}
	}

	public void setIntCS(CallableStatement cs, Integer valor, int posicion)
			throws SQLException {
		if (valor == null) {
			cs.setNull(posicion, Types.NULL);
		} else {
			cs.setInt(posicion, valor);
		}
	}

	public void setDateCS(CallableStatement cs, String valor, int posicion)
			throws SQLException, NoDateInfrastructureException {

		if (valor == null||valor.trim().isEmpty()) {
			cs.setNull(posicion, Types.NULL);
		} else {
			try {
				DateUtilRug dateUtil = new DateUtilRug();
				Date date = dateUtil.parseToSQLDate(dateUtil
						.parseStrDate1(valor));
				cs.setDate(posicion, date);
			} catch (Exception e) {
				cs.setNull(posicion, Types.NULL);
			}

		}
	}
	public void setDateCS(CallableStatement cs, XMLGregorianCalendar gregorianCalendar, int posicion)
			throws SQLException, NoDateInfrastructureException {
		
		if (gregorianCalendar == null) {
			cs.setNull(posicion, Types.NULL);
		} else {
			try {
				cs.setDate(posicion, getXMLGregorianCalendartoDate(gregorianCalendar));
			} catch (Exception e) {
				cs.setNull(posicion, Types.NULL);
			}
			
		}
	}

	public void setBigDecimalCS(CallableStatement cs, String valor, int posicion)
			throws SQLException {
		if (valor == null) {
			cs.setNull(posicion, Types.NULL);
		} else {
			try {
				cs.setBigDecimal(posicion, new BigDecimal(valor));
			} catch (NumberFormatException nfe) {
				cs.setNull(posicion, Types.NULL);
			}
		}
	}

	public void setForV(CallableStatement cs, boolean valor, int posicion)
			throws SQLException {
		if (valor == false) {
			cs.setString(posicion, "F");
		} else {
			cs.setString(posicion, "V");
		}
	}

	public String clobToString(Clob data) {
		StringBuilder sb = new StringBuilder();
		try {
			if (data != null) {

				Reader reader = data.getCharacterStream();
				BufferedReader br = new BufferedReader(reader);

				String line;
				while (null != (line = br.readLine())) {
					sb.append(line);
				}
				br.close();
			} else {
				sb.append("");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public String getTipoGarantiasStr(List<TipoBienMueble> idTipoGarantias) {
		String regresa = "";
		Iterator<TipoBienMueble> it = idTipoGarantias.iterator();
		if (it.hasNext()) {
			regresa = ""+it.next().getId();
		}
		while (it.hasNext()) {
			regresa += ("|" + it.next().getId());
		}
		return regresa;
	}
	
	
	public void setDataInPreparedStatemet(Object objetoValidar,PreparedStatement preparedStatement) throws InfrastructureException{
		
		if(indexData==null){
			indexData= new Integer(0);
		}
		indexData=indexData+1;
		try{
			if(objetoValidar!=null){
				String nombreClase=objetoValidar.getClass().getName().split("\\.")[objetoValidar.getClass().getName().split("\\.").length-1];
				
					switch (Constants.getIdDataType(nombreClase)) {
					case 1://String
						preparedStatement.setString(indexData, (String) objetoValidar);
						break;
					case 2://Integer
						preparedStatement.setInt(indexData, ((Integer) objetoValidar).intValue());
						break;
					case 3://Double
						preparedStatement.setDouble(indexData, ((Double) objetoValidar).doubleValue());
						break;
					case 4://Float
						preparedStatement.setFloat(indexData, ((Float) objetoValidar).floatValue());
						break;
					case 5://Date
						preparedStatement.setDate(indexData, new java.sql.Date(((java.util.Date) objetoValidar).getTime()) );
						break;
					case 6://Timestamp
						preparedStatement.setTimestamp(indexData,(java.sql.Timestamp) objetoValidar);
						break;
					case 7://CLOB
						preparedStatement.setClob(indexData,(CLOB) objetoValidar);
						break;
					case 8://Boolean
						if((Boolean) objetoValidar){//true
							preparedStatement.setString(indexData, "V");
						}else{//False
							preparedStatement.setString(indexData, "F");
						}
						break;
					case 9://XMLGregorianCalendar
						preparedStatement.setDate(indexData, new java.sql.Date(((XMLGregorianCalendar) objetoValidar).toGregorianCalendar().getTime().getTime()) );
						break;
					}
			
			}else{
				
				preparedStatement.setNull(indexData, Types.NULL);
			}
			
		}catch (Exception e) {
			throw new InfrastructureException("Error al capturar parametro", e);
		}
	}
	
	public void resetIndexData(){
		indexData=null;
	}
	
	public XMLGregorianCalendar dateToXML(final java.util.Date fecha) throws DatatypeConfigurationException {
		XMLGregorianCalendar x;
		
        Calendar c = new GregorianCalendar();
        c.setTime(fecha);
        DatatypeFactory df = DatatypeFactory.newInstance();
        return df.newXMLGregorianCalendarDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH), c.getTimeZone().getOffset(fecha.getTime()) / (60 * 60 * 1000));
    }
	public Date getXMLGregorianCalendartoDate(XMLGregorianCalendar fecha) throws DatatypeConfigurationException {
		
		java.util.Date dt = fecha.toGregorianCalendar().getTime();
		Date fechaNueva= new Date(dt.getTime());
		
		return fechaNueva;
	}

 
}
