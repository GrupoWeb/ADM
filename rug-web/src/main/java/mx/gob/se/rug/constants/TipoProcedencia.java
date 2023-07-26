/*
 * ProcedenciaDomicilio.java        04/05/2009
 *
 * Copyright (c) 2009 Secretaría de Economía
 * Alfonso Reyes No. 30 Col. Hipódromo Condesa C.P. 06140, 
 * Delegación Cuauhtémoc, México, D.F.
 * Todos los Derechos Reservados.
 *
 * Este software es confidencial y contiene información perteneciente
 * a la Secretaría de Economía.
 * 
 */
package mx.gob.se.rug.constants;

/**
 * 
 * 
 * @version 0.1
 * @author Alfonso Esquivel
 * 
 */
public enum TipoProcedencia {
	NACIONAL("NACIONAL"), EXTRANJERO("EXTRANJERO"), EXTRANJERA("EXTRANJERA");

	private TipoProcedencia(String id) {
		this.id = id;
	}

	private String id;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

}
