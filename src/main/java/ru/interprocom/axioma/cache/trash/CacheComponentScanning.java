package ru.interprocom.axioma.cache.trash;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Annotation;
import java.util.Collection;

@Configuration
@ComponentScan(basePackages = "ru.interprocom.axioma.cache.core")
public class CacheComponentScanning {
	private ApplicationContext applicationContext;
	@Autowired
	public CacheComponentScanning(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public Collection<Object> getAnnotatedBeans(Class<? extends Annotation> annotation) {
		return applicationContext.getBeansWithAnnotation(annotation).values();
	}
}
