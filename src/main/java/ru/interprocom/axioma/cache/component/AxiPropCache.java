package ru.interprocom.axioma.cache.component;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.interprocom.axioma.cache.annotation.AxiCache;
import ru.interprocom.axioma.cache.core.AxiomaCache;
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
public class AxiPropCache extends AxiomaCache {
	private final String cacheName = "axiprop";

	//Используется собственная реализация подключения к keyDB на основе библиотеки Redisson для	совместимости с Аксиомой
	@Autowired
	CacheDBManager cacheDBManager;

	@Autowired
	AxiPropRepository axiPropRepository;

	@Autowired
	AxiPropMapper axiPropMapper;

	//ToDo Перезагрузка кэша при изменении связанных записей
	@Override
	public void load() {
		log.info("Start loading {}", this.getClass().getSimpleName());
		System.out.println("Start loading " + this.getClass().getSimpleName());
		Map<String, PropertyValueInfo> cache = cacheDBManager.getMap(cacheName);

		if (cache.isEmpty()) {
			List<PropertyValueInfo> dbRecords = axiPropRepository.findAll().stream()
					.map(axiPropMapper::map)
					.toList();
			dbRecords.forEach(axiProp -> cache.put(axiProp.getPropname(), axiProp));
		}

		updateCountAndMaxRowstamp(cache);
	}

	@Override
	public void sync() {
		log.info("Sync {}", this.getClass().getSimpleName());
		System.out.println("Sync " + this.getClass().getSimpleName());
		if (axiPropRepository.countAllRecord() != getRecordCount()
				|| axiPropRepository.maxRowStamp() != getMaxRowstamp()) {
			Map<String, PropertyValueInfo> cache = cacheDBManager.getMap(cacheName);
			Map<String, PropertyValueInfo> dbRecords = axiPropRepository.findAll().stream()
					.map(axiPropMapper::map)
					.collect(Collectors.toMap(PropertyValueInfo::getPropname, Function.identity()));

			//Добавляем в кэш новые записи и обновляем существующие в кэше, но изменённые в БД
			dbRecords.forEach((propname, propValue) ->
					cache.merge(propname, propValue, (oldValue, newValue) ->
							oldValue.equals(newValue) ? oldValue : newValue));
			//Удаляем из кэша по ключу все записи, которых уже нет в БД
			cache.keySet().retainAll(dbRecords.keySet());

			updateCountAndMaxRowstamp(cache);
		}
	}

	@Override
	public void reload(String propname) {

	}

	@Override
	public void remove(String propname) {

	}

	private void updateCountAndMaxRowstamp(Map<String, PropertyValueInfo> cache) {
		setRecordCount(cache.size());
		setMaxRowstamp(cache.values().stream()
				.mapToLong(PropertyValueInfo::getRowstamp)
				.max()
				.orElseGet(axiPropRepository::maxRowStamp)
		);
	}
}
