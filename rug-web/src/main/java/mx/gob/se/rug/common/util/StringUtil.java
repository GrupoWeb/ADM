/*
 * StringUtil.java        02/12/2009
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

package mx.gob.se.rug.common.util;

/**
 * Utilerias para el manejo de Strings.
 * 
 * @version 0.1
 * @author Erika Astorga
 * 
 */
public class StringUtil {

	/**
	 * Regresa una cadena de texto en mayúsculas, si la cadena es nula regresa
	 * vacio.
	 * 
	 * @param chain
	 *            Cadena a transformar
	 * @return
	 */
	public static String toUpperCase(String chain) {

		String resultado = "";
		if (chain != null) {
			resultado = chain.toUpperCase();
		}

		return resultado;
	}

}
