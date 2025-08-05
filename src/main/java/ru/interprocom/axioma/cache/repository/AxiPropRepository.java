package ru.interprocom.axioma.cache.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.interprocom.axioma.cache.model.AxiProp;
import ru.interprocom.axioma.prime.server.PropertyValueInfo;

import java.util.List;
import java.util.Optional;

@Repository
public interface AxiPropRepository extends JpaRepository<AxiProp, Long> {
	/*
	* Для решения проблемы N + 1 используем EntityGraph. Допускается указывать количество запсией для выборки
	* при использовании join'а записей со связью один к одному
	* */
	@Override
	@EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = "axipropvalue")
	Page<AxiProp> findAll(Pageable pageable);

	@EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = "axipropvalue")
	Optional<AxiProp> findByPropname(String propname);

	/*
	* Для сохранение выборки в объект класса для хранения кэша системы используется мэппинг с указанием класса и именами
	* таблиц соответствующим именам entity в коде
	* */
	@Query(value = "select new ru.interprocom.axioma.prime.server.PropertyValueInfo(" +
			"p.propname, p.axitype, p.liverefresh, p.encrypted, p.domainid, p.securelevel, p.rowstamp," +
			"v.propvalue, v.serverhost, v.servername, v.encryptedvalue, v.rowstamp as axipropvalueRowstamp) " +
			"from AxiProp p " +
			"left join AxiPropValue v on p.propname = v.propname " +
			"where p.rowstamp > :rowstamp or v.rowstamp > :valuerowstamp")
	List<PropertyValueInfo> updatedRecords(@Param("rowstamp") Long rowstamp, @Param("valuerowstamp") Long valuerowstamp);

	void deleteByPropname(String propname);

	@Query(value = "select propname from axiprop", nativeQuery = true)
	List<String> propnameList();

	@Query(value = "select max(cast((rowstamp) as decimal)) from axiprop", nativeQuery = true)
	Long maxRowStamp();

	@Query(value = "select max(cast((rowstamp) as decimal)) from axipropvalue", nativeQuery = true)
	Long maxValueRowStamp();

	@Query(value = "select count(1) from axiprop", nativeQuery = true)
	Long countAllRecord();
}
