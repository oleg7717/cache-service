package ru.interprocom.axioma.cache.component;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.interprocom.axioma.cache.annotation.AxiCache;
import ru.interprocom.axioma.cache.core.AxiomaCache;

import java.util.*;

@Slf4j
@Component
public class SynchronizeCache implements ApplicationContextAware {
	private static final ArrayList<Object> cacheClasses = new ArrayList<>();
	private ApplicationContext applicationContext;

	@PostConstruct
	public void loadToCacheDB() {
		cacheClasses.addAll(applicationContext.getBeansWithAnnotation(AxiCache.class).values());
		cacheClasses.forEach(aClass -> {((AxiomaCache) aClass).load();});
	}

	@Scheduled(fixedDelayString = "PT30M")
	@SchedulerLock(name = "syncCache", lockAtLeastFor = "PT30M", lockAtMostFor = "PT30M")
	public void syncCache() {
		cacheClasses.forEach(aClass -> {((AxiomaCache) aClass).sync();});
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
