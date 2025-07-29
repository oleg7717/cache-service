package ru.interprocom.axioma.cache.component;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.interprocom.axioma.cache.annotation.AxiCache;
import ru.interprocom.axioma.cache.config.CacheComponentScanning;
import ru.interprocom.axioma.cache.core.AxiomaCache;
import ru.interprocom.axioma.cache.repository.CacheRepository;

import java.util.HashSet;

@Slf4j
@Component
public class SynchronizeCache {
	private static final HashSet<Object> cacheClasses = new HashSet<>();

	@Autowired
	private CacheComponentScanning cacheScanning;

	@PostConstruct
	public void loadToCacheDB() {
		cacheClasses.addAll(cacheScanning.getAnnotatedBeans(AxiCache.class));

		cacheClasses.forEach(aClass -> {
//			CacheRepository<aClass, Long> cacheRepository;
			((AxiomaCache) aClass).load();
		});
	}

	@Scheduled(fixedDelayString = "PT30M")
	@SchedulerLock(name = "syncCache", lockAtLeastFor = "PT30M", lockAtMostFor = "PT30M")
	public void syncCache() {
		cacheClasses.forEach(aClass -> {
			((AxiomaCache) aClass).sync();
		});
	}
}
