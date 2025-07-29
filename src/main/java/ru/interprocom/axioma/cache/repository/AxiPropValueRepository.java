package ru.interprocom.axioma.cache.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.interprocom.axioma.cache.model.AxiProp;
import ru.interprocom.axioma.cache.model.AxiPropValue;

import java.util.Optional;

@Repository
public interface AxiPropValueRepository extends JpaRepository<AxiPropValue, Long> {
	Optional<AxiProp> findByPropname(String propname);

	Optional<AxiProp> findByAxipropvalueid(Long id);

	@Query(value = "select max(cast((rowstamp) as decimal)) from axipropvalue", nativeQuery = true)
	Long maxRowStamp();

	@Query(value = "select count(1) from axipropvalue", nativeQuery = true)
	Long countAllRecord();
}
