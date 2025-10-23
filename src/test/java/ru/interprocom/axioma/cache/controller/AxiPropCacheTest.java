package ru.interprocom.axioma.cache.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;
import ru.interprocom.axioma.cache.component.MapContainer;
import ru.interprocom.axioma.cache.core.AxiPropCache;
import ru.interprocom.axioma.cache.exception.ResourceNotFoundException;
import ru.interprocom.axioma.cache.mapper.AxiPropMapper;
import ru.interprocom.axioma.cache.model.AxiProp;
import ru.interprocom.axioma.cache.model.AxiPropValue;
import ru.interprocom.axioma.cache.repository.AxiPropRepository;
import ru.interprocom.axioma.prime.server.PropertyValueInfo;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AxiPropCacheTest {
	private final MapContainer<String, Map<String, PropertyValueInfo>> container = new MapContainer<>(new HashMap<>());
	private AxiPropCache axiPropCache;
	private AxiProp axiProp;
	private Map<String, PropertyValueInfo> cacheMap;

	@Autowired
	private AxiPropRepository axiPropRepository;
	@Autowired
	private AxiPropMapper mapper;

	@BeforeAll
	static void createDBStructure(@Autowired DataSource dataSource) throws SQLException {
		try (Connection conn = dataSource.getConnection()) {
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("insert_axiprop.sql"));
		}
	}

	@BeforeEach
	void setup() {
		axiProp = getRandomRecord(axiPropRepository.findAll());
		container.setMap("axiprop");
		axiPropCache = new AxiPropCache(container, axiPropRepository, mapper);
		axiPropCache.load();
		cacheMap = axiPropCache.getCacheContainer().getMap(axiPropCache.getCacheName());
	}

	@Test
	@Order(1)
	public void testLoad() {
		checkResult();
	}

/*	@Test
	@Order(2)
	public void testSyncWithCreateAndDelete() {
		Map<String, PropertyValueInfo> cacheMap = axiPropCache.getCacheContainer().getMap(axiPropCache.getCacheName());
		assertThat(cacheMap.size()).isEqualTo(axiPropRepository.countAllRecord());
		assertThat(cacheMap.containsKey(axiPropRepository.findAll().get(0).getPropname())).isTrue();
	}*/

	@Test
	@Order(3)
	public void testSyncWithUpdateParentRecord() {
		axiProp.setDescription(axiProp.getDescription() + LocalTime.now());
		axiPropRepository.save(axiProp);
		axiPropCache.sync();

		checkResult();
	}

	@Test
	@Order(4)
	public void testSyncWithUpdateChildRecord() {
		axiProp = axiPropRepository.findByPropname("axe.cache.redis")
				.orElseThrow(() -> new ResourceNotFoundException("Property with propname axe.cache.redis not found"));
		AxiPropValue axiPropValue = axiProp.getAxipropvalue();
		axiPropValue.setPropvalue("redis://localhost:6378");
		axiPropRepository.save(axiProp);
		axiPropCache.sync();

		assertThat(cacheMap.get("axe.cache.redis").getPropvalue()).isEqualTo("redis://localhost:6378");
		checkResult();
	}

	private AxiProp getRandomRecord(List<AxiProp> allProps) {
		return allProps.get(new Random().nextInt(allProps.size()));
	}

	private void checkResult() {
		assertThat(cacheMap).satisfies(map -> {
			assertThat(map.size()).isEqualTo(axiPropRepository.findAll().size());
			assertThat(map.containsKey(axiProp.getPropname())).isTrue();
		});
		assertThat(axiPropCache).satisfies((cache) -> {
			assertThat(cache.getMaxRowstamp()).isEqualTo(axiPropRepository.maxRowStamp());
			assertThat(cache.getMaxValueRowstamp()).isEqualTo(axiPropRepository.maxValueRowStamp());
		});
	}
}

