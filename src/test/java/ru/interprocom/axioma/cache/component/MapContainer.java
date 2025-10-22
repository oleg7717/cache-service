package ru.interprocom.axioma.cache.component;

import org.redisson.api.RBucket;

import java.util.HashMap;
import java.util.Map;

public class MapContainer<K, V> implements CacheContainer<K, V> {
	private final Map<String, Map<K, V>> map;
	public MapContainer(HashMap<String, Map<K, V>> map) {
		this.map = map;
	}

	public void setMap(String mapName) {
		map.computeIfAbsent(mapName, k -> new HashMap<>());
	}

	@Override
	public Map<K, V> getMap(String mapName) {
		return map.get(mapName);
	}

	@Override
	public RBucket<K> getBucket(String bucketName) {
		throw new RuntimeException("Operation does not support.");
	}
}
