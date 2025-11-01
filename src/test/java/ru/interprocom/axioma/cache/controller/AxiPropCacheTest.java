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
import org.springframework.jdbc.core.JdbcTemplate;
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
	private JdbcTemplate jdbcTemplate;

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
		cacheMap = axiPropCache.getCacheContainer().getMap(axiPropCache.getCacheName());
	}

	@Test
	@Order(1)
	public void testLoad() {
		axiPropCache.load();

		checkResult();
	}

	@Test
	@Order(2)
	public void testSyncWithCreateAndDelete() {
		axiPropCache.load();

		String propname = "axe.cache.scripts.newProp";
		String sql = "INSERT INTO axiprop (axipropid,propname,description,axitype,globalonly,instanceonly,axiomadefault," +
				"liverefresh,encrypted,domainid,nullsallowed,securelevel,userdefined,onlinechanges,changeby,changedate," +
				"masked,accesstype,valuerules) VALUES\n" +
				"(1000,'axe.cache.scripts.newProp','new property','YORN',0,0,'0',0,0,NULL,0,'SECURE',0,0,'AXIOMA'," +
				"'2024-03-04 12:00:00',0,2,NULL)";
		jdbcTemplate.update(sql);
		sql = "INSERT INTO axipropvalue (axipropvalueid,propname,servername,serverhost,propvalue,encryptedvalue," +
				"changeby,changedate,accesstype) VALUES\n" +
				"(1000,'axe.cache.scripts.newProp','COMMON',NULL,'true',NULL,'AXIOMA','2024-03-04 12:00:00',0)";
		jdbcTemplate.update(sql);

		axiPropRepository.delete(axiProp);
		axiPropCache.sync();
		axiProp = axiPropRepository.findByPropname(propname)
				.orElseThrow(() -> new ResourceNotFoundException("Property with propname " + propname + " not found"));

		checkResult();
	}

	@Test
	@Order(3)
	public void testSyncWithUpdateParentRecord() {
		axiPropCache.load();

		axiProp.setDescription(axiProp.getDescription() + LocalTime.now());
		axiPropRepository.save(axiProp);
		axiPropCache.sync();

		checkResult();
	}

	@Test
	@Order(4)
	public void testSyncWithUpdateChildRecord() {
		axiPropCache.load();

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

