package ru.interprocom.axioma.cache.core;

import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.interprocom.axioma.cache.annotation.AxiCache;
import ru.interprocom.axioma.cache.component.CacheDBManager;
import ru.interprocom.axioma.prime.server.PropertyValueInfo;
import ru.interprocom.axioma.cache.mapper.AxiPropMapper;
import ru.interprocom.axioma.cache.repository.AxiPropRepository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@AxiCache
@Slf4j
@Component
@Setter
@Getter
@ConfigurationProperties(prefix = "redis")
public class AxiPropCache extends AxiomaCache {
	private final String cacheName = "axiprop";
	private long maxValueRowstamp;

	//Используется собственная реализация подключения к keyDB на основе библиотеки Redisson для	совместимости с Аксиомой
	@Autowired
	CacheDBManager cacheDBManager;

	@Autowired
	AxiPropRepository repository;

	@Autowired
	AxiPropMapper mapper;

	/**
	 * Первичная загрузка свойств системы в хранилище кэша. Выполняется после создания бинов и внедрения всех
	 * зависимостей и до момента, когда сервис будет доступен для получения запросов.
	 */
	@Override
	public void load() {
		log.info("Start loading {}", this.getClass().getSimpleName());
		Map<String, PropertyValueInfo> cache = cacheDBManager.getMap(cacheName);

		if (cache.isEmpty()) {
			repository.findAllProperties().forEach(axiProp -> cache.put(axiProp.getPropname(), axiProp));
		}

		updateCountAndMaxRowstamp(cache);
	}

	/**
	 * Синхронизация свойств системы при добавлении или удалении записи в таблице axiprop, а также при изменении записи
	 * axiprop или axipropvalue, что характеризуется изменением значения rowstamp записи.
	 */
	@Override
	public void sync() {
		log.info("Sync {}", this.getClass().getSimpleName());
		if (repository.countAllRecord() != getRecordCount() || repository.maxRowStamp() != getMaxRowstamp()
				|| repository.maxValueRowStamp() != getMaxValueRowstamp()) {
			Map<String, PropertyValueInfo> cache = cacheDBManager.getMap(cacheName);

			List<String> propnames = repository.propnameList();
			Map<String, PropertyValueInfo> updatedDBRecords = repository
					.updatedRecords(getMaxRowstamp(), getMaxValueRowstamp())
					.stream()
					.collect(Collectors.toMap(PropertyValueInfo::getPropname, Function.identity()));

			//Добавляем в кэш новые записи и обновляем существующие в кэше, но изменённые в БД
			cache.putAll(updatedDBRecords);

			//Удаляем из кэша по ключу все записи, которых уже нет в БД
			cache.keySet().retainAll(propnames);

			updateCountAndMaxRowstamp(cache);
		}
	}

	@Override
	@Synchronized
	public void reloadAll() {
		Map<String, PropertyValueInfo> dbRecords = repository
				.findAllProperties()
				.stream()
				.collect(Collectors.toMap(PropertyValueInfo::getPropname, Function.identity()));;
		Map<String, PropertyValueInfo> cache = cacheDBManager.getMap(cacheName);

		//Добавляем в кэш новые записи и обновляем существующие
		cache.putAll(dbRecords);
/*		dbRecords.forEach((propname, propValue) ->
				cache.merge(propname, propValue, (oldValue, newValue) ->
						oldValue.equals(newValue) ? oldValue : newValue));*/

		//Удаляем из кэша по ключу все записи, которых уже нет в БД
		cache.keySet().retainAll(dbRecords.keySet());

		updateCountAndMaxRowstamp(cache);
	}

	@Override
	public void reload(String propname) {
		Map<String, PropertyValueInfo> cache = cacheDBManager.getMap(cacheName);
		repository.findByPropname(propname)
				.map(mapper::map)
				.ifPresentOrElse(prop -> cache.put(propname, prop), () -> cache.remove(propname));

		updateCountAndMaxRowstamp(cache);
	}

	private void updateCountAndMaxRowstamp(Map<String, PropertyValueInfo> cache) {
		setRecordCount(cache.size());
		setMaxRowstamp(cache.values().stream()
				.mapToLong(PropertyValueInfo::getRowstamp)
				.max()
				.orElseGet(repository::maxRowStamp)
		);
		setMaxValueRowstamp(cache.values().stream()
				.mapToLong(PropertyValueInfo::getAxipropvalueRowstamp)
				.max()
				.orElseGet(repository::maxValueRowStamp)
		);
	}
}
