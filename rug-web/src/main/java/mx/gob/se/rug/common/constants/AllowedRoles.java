/*
 * AllowedRoles.java        04/08/2010
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
package mx.gob.se.rug.common.constants;

/**
 * @author Alfonso Esquivel
 * 
 */
public enum AllowedRoles {

	Default("Default"), AuxiliarFedatario("AuxiliarFedatario"), Fedatario(
			"Fedatario");

	private String role;

	private AllowedRoles(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}

}
