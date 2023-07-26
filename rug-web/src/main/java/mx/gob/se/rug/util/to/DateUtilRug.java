package mx.gob.se.rug.util.to;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import mx.gob.se.rug.exception.NoDateInfrastructureException;
import mx.gob.se.rug.util.MyLogger;

public class DateUtilRug {
	public Date parseStrDate(String strDate){
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = formater.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	public Date parseStrDate1(String strDate) throws NoDateInfrastructureException{
		SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
		Date date = null;
		MyLogger.Logger.log(Level.INFO,strDate);
		try {
			date = formater.parse(strDate);
		} catch (ParseException e) {
		}
		MyLogger.Logger.log(Level.WARNING, "" + date);
		return date;
	}
	
	public String parseDateToStr(Date date){
		SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
		String sDate = null;
		sDate = formater.format(date);
		return sDate;
	}
	
	public String formatDate(Date date) throws NoDateInfrastructureException{
		SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
		String sDate = null;
		sDate = formater.format(date);
		return sDate;		
	}
	
	public Date formatInputDate(String strDate){
		SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
		Date date = null;
		try {
			date = formater.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	
	public java.sql.Date parseToSQLDate(java.util.Date date) {
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		return sqlDate;
	}
	
	public Date parseToUtilDate(java.sql.Date date){
		return new Date(date.getTime());
	}
	
	public String cambioDeFormato(String fecha){
		String cadena = "";
		String [] cadenas;
		if (fecha.length()>10){
			cadenas = fecha.substring(0, 10).split("-");
		}else{
			cadenas = fecha.split("-");
		}
		if (cadenas.length > 1){
			String ano = cadenas[0];
			String mes = cadenas[1];
			String dia = cadenas[2];
			cadena = dia + "/" + mes + "/" + ano;
		}else{
			cadena  = fecha;
		}
		
		return cadena;
	}
	
	public Date sumaVigencia (Date fech, int vig){
		Calendar cal=Calendar.getInstance();
	    cal.setTime(fech);
	    cal.add(Calendar.MONTH, + vig);
	    Date fecha = cal.getTime();
		return fecha;
	}
	
	public static void main (String args[]){
		
		DateUtilRug dateUtil = new DateUtilRug();
		Date date;
		try {
			date = dateUtil.parseToSQLDate(dateUtil.parseStrDate1("04/55/2011"));
			if ((new java.util.Date(date.getTime())).after(Calendar.getInstance().getTime())){
				MyLogger.Logger.log(Level.WARNING,"La fecha es posterior");
			}else{
				MyLogger.Logger.log(Level.WARNING,"La fecha es anterior a la actual");
			}
		} catch (NoDateInfrastructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
