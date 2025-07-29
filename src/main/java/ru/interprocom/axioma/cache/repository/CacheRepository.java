package ru.interprocom.axioma.cache.repository;

public interface CacheRepository<T, ID> {
	Long maxRowStamp();

	Long countAllRecord();
}
