package ru.interprocom.axioma.cache.component;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Setter
@Getter
@ConfigurationProperties(prefix = "redis")
public class CacheDBManager {
	private String url;
	private String user;
	private String password;
	private boolean clustered;
	private static RedissonClient connection;

	public RedissonClient getConnection() {
		if (connection == null) {
			Config config = new Config();

			if (clustered) {
				config.useClusterServers().addNodeAddress(url);
				if (user != null && !user.isEmpty() && password != null && !password.isEmpty()) {
					config.useClusterServers().setUsername(user);
					config.useClusterServers().setPassword(password);
				}
			} else {
				config.useSingleServer().setAddress(url);
				if (user != null && !user.isEmpty() && password != null && !password.isEmpty()) {
					config.useSingleServer().setUsername(user);
					config.useSingleServer().setPassword(password);
				}
			}

			connection = Redisson.create(config);
		}

		return connection;
	}

	public String getRedisAddress() {
		return url;
	}

	public <K, V> RMap<K, V> getMap(String mapName) {
		return getConnection().getMap(mapName);
	}

	public <K, V> RBucket<K> getBucket(String bucketName) {
		return getConnection().getBucket(bucketName);
	}
}
