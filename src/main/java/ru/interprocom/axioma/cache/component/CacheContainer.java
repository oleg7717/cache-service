package ru.interprocom.axioma.cache.component;

import org.redisson.api.RBucket;

import java.util.Map;

public interface CacheContainer<K, V> {
	Map<K, V> getMap(String mapName);

	RBucket<K> getBucket(String bucketName);
}
