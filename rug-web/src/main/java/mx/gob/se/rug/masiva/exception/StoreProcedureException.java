package mx.gob.se.rug.masiva.exception;

import mx.gob.se.rug.to.PlSql;

public class StoreProcedureException extends Exception {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	private PlSql plSql = new PlSql();

	
	
	public PlSql getPlSql() {
		return plSql;
	}

	public StoreProcedureException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StoreProcedureException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public StoreProcedureException(Integer codeError,String message) {
		super(message);
		this.plSql.setIntPl(codeError);
		this.plSql.setStrPl(message);
		// TODO Auto-generated constructor stub
	}
	public StoreProcedureException(PlSql plSql) {
		super(plSql.getStrPl());
		this.plSql=plSql;
		// TODO Auto-generated constructor stub
	}

	public StoreProcedureException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	
	
	

}
