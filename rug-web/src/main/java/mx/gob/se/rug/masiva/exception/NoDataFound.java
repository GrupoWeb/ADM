package mx.gob.se.rug.masiva.exception;

import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.to.PlSql;

public class NoDataFound extends Exception{

	private static final long serialVersionUID = 1L;
	
	
	
	
	public PlSql getPlSql() {
		PlSql plSql= new PlSql();
		plSql.setIntPl(getCodeError());
		plSql.setStrPl(getMessage());
		return plSql;
	}

	
	@Override
	public String getMessage(){
		 String mesg=Constants.MESASAGE_EXCEPTION_NO_DATA_FOUND; 
		 
		return mesg.replace("x", super.getMessage())	 ;
		
	}
	public Integer getCodeError() {
		return Constants.CODE_EXCEPTION_NO_DATA_FOUND;
	}
	public NoDataFound() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NoDataFound(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NoDataFound(String campoNoEncontrado) {
		
		super(campoNoEncontrado);
		// TODO Auto-generated constructor stub
	}


	public NoDataFound(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}