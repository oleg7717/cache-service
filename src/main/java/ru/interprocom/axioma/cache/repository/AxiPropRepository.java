package ru.interprocom.axioma.cache.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.interprocom.axioma.cache.model.AxiProp;

import java.util.Optional;

@Repository
public interface AxiPropRepository extends JpaRepository<AxiProp, Long>, CacheRepository<AxiProp, Long> {
/*	@Query(value = "select " +
			"a1_0.axipropid," +
			"a1_0.accesstype," +
			"a1_0.axiomadefault," +
			"a1_0.axitype," +
			"a1_0.changeby," +
			"a1_0.changedate," +
			"a1_0.description," +
			"a1_0.domainid," +
			"a1_0.encrypted," +
			"a1_0.globalonly," +
			"a1_0.instanceonly," +
			"a1_0.liverefresh," +
			"a1_0.masked," +
			"a1_0.nullsallowed," +
			"a1_0.onlinechanges," +
			"a1_0.propname," +
			"a1_0.securelevel," +
			"a1_0.userdefined," +
			"a1_0.valuerules," +
			"a1_0.rowstamp," +
			"a2_0.axipropvalueid," +
			"a2_0.accesstype as accesstypeValue," +
			"a2_0.changeby as changebyValue," +
			"a2_0.changedate as changedateValue," +
			"a2_0.encryptedvalue," +
			"a2_0.propname as propnameValue," +
			"a2_0.propvalue," +
			"a2_0.rowstamp as rowstampValue," +
			"a2_0.serverhost," +
			"a2_0.servername " +
			"from axiprop a1_0 left join axipropvalue a2_0 on a1_0.propname=a2_0.propname " +
			"where a2_0.propname=:propname", nativeQuery = true)*/
	Optional<AxiProp> findByPropname(/*@Param("propname") */String propname);

	Optional<AxiProp> deleteByPropname(String propname);

	Optional<AxiProp> findByAxipropid(Long id);

	@Query(value = "select max(cast((rowstamp) as decimal)) from axiprop", nativeQuery = true)
	Long maxRowStamp();

	@Query(value = "select count(1) from axiprop", nativeQuery = true)
	Long countAllRecord();
}
