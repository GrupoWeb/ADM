/**
 * 
 */
package mx.gob.se.rug.common.util;

import java.util.Map;

import mx.gob.economia.dgi.framework.spring.jdbc.AbstractBaseStoredProcedure;
import mx.gob.se.rug.common.dto.Mensaje;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Alfonso Esquivel
 * 
 */
public class DaoUtil {

	protected static Log logger = LogFactory.getLog(DaoUtil.class);

	public static Mensaje getMensaje(Map<String, Object> map) {
		if (map != null && !map.isEmpty()) {
			return new Mensaje((Integer) map
					.get(AbstractBaseStoredProcedure.RESULT), (String) map
					.get(AbstractBaseStoredProcedure.TX_RESULT));
		}
		return null;
	}
	
}
