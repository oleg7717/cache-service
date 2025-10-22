package ru.interprocom.axioma.cache.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.interprocom.axioma.cache.component.MapContainer;
import ru.interprocom.axioma.cache.core.AxiPropCache;
import ru.interprocom.axioma.cache.mapper.AxiPropMapper;
import ru.interprocom.axioma.cache.repository.AxiPropRepository;
import ru.interprocom.axioma.prime.server.PropertyValueInfo;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
public class AxiPropCacheTest {
	@Autowired
	private AxiPropRepository axiPropRepository;
	@Autowired
	private AxiPropMapper mapper;

	@Test
	@Sql("/insert_axiprop.sql")
	public void load() {
		var container = new MapContainer<String, Map<String, PropertyValueInfo>>(new HashMap<>());
		container.setMap("axiprop");
		var axiPropCache = new AxiPropCache(container, axiPropRepository, mapper);
		axiPropCache.load();
		Map<String, PropertyValueInfo> cacheMap = axiPropCache.getCacheContainer().getMap(axiPropCache.getCacheName());
		assertThat(cacheMap.containsKey("axe.cache.redis")).isTrue();
	}
}
