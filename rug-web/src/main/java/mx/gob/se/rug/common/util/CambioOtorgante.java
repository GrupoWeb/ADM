/*
 * CambioOtorgante.java        03/09/2010
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
package mx.gob.se.rug.common.util;

import java.util.logging.Level;

import mx.gob.se.rug.modificacion.service.impl.ModificacionServiceImp;
import mx.gob.se.rug.util.MyLogger;

public class CambioOtorgante {

	public String cambioOtorgante(int idTramiteNuevo, int idGarantia,
			int idUsusarioLoggeado) {

		ModificacionServiceImp detserv = new ModificacionServiceImp();

		int cambio = detserv.cambioAcreedores(idTramiteNuevo, idGarantia);

		return cambio + "";
	}

	public String cambioAcreedores(int idTramiteNuevo, int idGarantia) {
		ModificacionServiceImp detserv = new ModificacionServiceImp();
		MyLogger.Logger.log(Level.INFO, "id del tramite nuevo :" + idTramiteNuevo);
		MyLogger.Logger.log(Level.INFO, "id de la garantia :" + idGarantia);
		int cambio = detserv.cambioAcreedores(idTramiteNuevo, idGarantia);
		MyLogger.Logger.log(Level.INFO, "valor final de cambio" + cambio) ;
		return cambio + "";
	}

}
