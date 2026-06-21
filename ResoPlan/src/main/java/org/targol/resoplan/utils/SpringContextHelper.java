package org.targol.resoplan.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextHelper implements ApplicationContextAware {
	private static ApplicationContext context;

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	public static <T> T getBean(final Class<T> beanClass) {
		if (context == null) {
			throw new IllegalStateException("Le contexte Spring n'est pas encore initialisé !"); //$NON-NLS-1$
		}
		return context.getBean(beanClass);
	}
}
