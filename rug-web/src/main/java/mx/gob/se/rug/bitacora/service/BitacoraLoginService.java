/*
 * BitacoraLoginService.java        10/08/2009
 *
 * Copyright (c) 2009 Secretaría de Economía
 * Alfonso Reyes No. 30 Col. Hipódromo Condesa C.P. 06140,
 * Delegación Cuauhtémoc, México, D.F.
 * Todos los Derechos Reservados.
 *
 * Este software es confidencial y de uso exclusivo de la
 * Secretaría de Economía.
 *
 */

package mx.gob.se.rug.bitacora.service;

import mx.gob.se.rug.bitacora.dto.BitacoraLogin;
import mx.gob.se.rug.bitacora.exception.UsuarioNoBitacoradoException;

public interface BitacoraLoginService {
	public boolean bitacoraLogin(BitacoraLogin bitacoraLogin)
			throws UsuarioNoBitacoradoException;
}
