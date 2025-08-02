package ru.interprocom.axioma.cache.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.interprocom.axioma.cache.model.AxiProp;

import java.util.List;
import java.util.Optional;

@Repository
public interface AxiPropRepository extends JpaRepository<AxiProp, Long> {
	Optional<AxiProp> findByPropname(String propname);

	void deleteByPropname(String propname);

	@Query(value = "select propname from axiprop", nativeQuery = true)
	List<String> propnameList();

	@Query(value = "select prop.* from axiprop prop " +
			"left join axipropvalue value on prop.propname = value.propname " +
			"where prop.rowstamp > :rowstamp or value.rowstamp > :valuerowstamp", nativeQuery = true)
	List<AxiProp> updatedRecords(@Param("rowstamp") Long rowstamp, @Param("valuerowstamp") Long valuerowstamp);

	@Query(value = "select max(cast((rowstamp) as decimal)) from axiprop", nativeQuery = true)
	Long maxRowStamp();

	@Query(value = "select max(cast((rowstamp) as decimal)) from axipropvalue", nativeQuery = true)
	Long maxValueRowStamp();

	@Query(value = "select count(1) from axiprop", nativeQuery = true)
	Long countAllRecord();
}
