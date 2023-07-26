/**
 * 
 */
package mx.gob.se.rug.common.util;

/**
 * @author Alfonso Esquivel
 *
 */
public class NullsFree {
	
	public static String getNotNullValue(String value){
		return value == null ? "" : value;
	}

}
