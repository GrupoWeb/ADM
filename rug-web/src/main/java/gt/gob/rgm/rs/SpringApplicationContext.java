package gt.gob.rgm.rs;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringApplicationContext implements ApplicationContextAware {

	private static ApplicationContext applicationContext;
	
	private SpringApplicationContext() {
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	public static Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}
}
