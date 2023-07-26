package mx.gob.se.rug.common.environment.service.impl;

import java.util.Map;
import java.util.WeakHashMap;

import mx.gob.se.rug.common.environment.dao.EnvironmentDao;
import mx.gob.se.rug.common.environment.service.EnvironmentService;
import mx.gob.se.rug.fwk.service.RugBaseService;

public class EnvironmentServicePojoImpl extends RugBaseService implements
		EnvironmentService {

	private EnvironmentDao environmentDao;
	private Map<String, String> cache;

	public EnvironmentServicePojoImpl() {
		cache = new WeakHashMap<String, String>();
	}

	@Override
	public String getValue(String name) {
		String value = cache.get(name);
		if (value == null) {
			synchronized (this) {
				value = cache.get(name);
				if (value == null) {
					value = environmentDao.getValue(name);
					cache.put(name, value);
				}
			}
		}
		return value;
	}

	public void setEnvironmentDao(EnvironmentDao environmentDao) {
		this.environmentDao = environmentDao;
	}

}
