package ru.interprocom.axioma.cache.core;

import lombok.Data;
import ru.interprocom.axioma.cache.repository.CacheRepository;

@Data
public abstract class AxiomaCache {
	private long maxRowstamp;
	private long recordCount;
	private CacheRepository<Object, Long> cacheRepository;

	public abstract void load();

	public abstract void sync();

	public abstract void reload(String recordName);

	public abstract void remove(String recordName);
}
