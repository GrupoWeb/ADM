/*
 * BaseRugDao.java        06/12/2010
 *
 * Copyright (c) 2009 Secretar�a de Econom�a
 * Alfonso Reyes No. 30 Col. Hip�dromo Condesa C.P. 06140, 
 * Delegaci�n Cuauht�moc, M�xico, D.F.
 * Todos los Derechos Reservados.
 *
 * Este software es confidencial y contiene informaci�n perteneciente
 * a la Secretar�a de Econom�a.
 * 
 */
package gt.gob.rgm.adm.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import java.sql.Clob;

import java.sql.SQLException;






/**
 * 
 * 
 * @version 0.1
 * @author Abraham Stalin Aguilar Valencia
 * 
 */
public abstract class BaseRugDao {


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



 
}
