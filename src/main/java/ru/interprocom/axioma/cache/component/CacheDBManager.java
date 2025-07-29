package ru.interprocom.axioma.cache.component;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CacheDBManager {
	private static String url;
	private static RedissonClient redisson = null;

	public CacheDBManager() {
		url = "redis://localhost:6379";
		//ToDo вынести в свойста переменные для подключения к keyDB
		boolean isCluster = false;

		Config config = new Config();

		if (isCluster) {
			config.useClusterServers().addNodeAddress(url);
/*                if (user != null && password != null) {
                    config.useClusterServers().setUsername(user);
                    config.useClusterServers().setPassword(password);
                }*/
		} else {
			config.useSingleServer().setAddress(url);
/*                if (user != null && password != null) {
                    config.useSingleServer().setUsername(user);
                    config.useSingleServer().setPassword(password);
                }*/
		}

		redisson = Redisson.create(config);
	}

	private RedissonClient getConnection() {
		return redisson;
	}

	public String getRedisAddress() {
		return url;
	}

	public <K, V> RMap<K, V> getMap(String mapName) {
		return redisson.getMap(mapName);
	}

	public <K, V> RBucket<K> getBucket(String bucketName) {
		return redisson.getBucket(bucketName);
	}

/*	public <K, V> RMap<K, V> getBucket(String backetName) {
		return redisson.getgetMap(backetName);
	}*/

//	Multimap
}
