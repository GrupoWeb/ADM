package mx.gob.se.rug.common.util;

import java.util.HashMap;
import java.util.Map;

public class RequestContext {

	public static final String KEY_STARTTIME = "startTime";
	public static final String KEY_REQUESTID = "requestId";
	public static final String KEY_PARAMETERS = "parameters";
	public static final String KEY_REMOTEADDR = "remoteaddr";
	public static final String KEY_REQUESTURI = "requesturi";

	private static final ThreadLocal<Map<String, Object>> attributes = new ThreadLocal<Map<String, Object>>() {
		protected java.util.Map<String, Object> initialValue() {
			return new HashMap<String, Object>();
		}
	};

	public static Object getAttribute(String key) {
		return attributes.get().get(key);
	}

	public static void setAttribute(String key, Object value) {
		attributes.get().put(key, value);
	}

	public static void release() {
		attributes.remove();
	}
}
