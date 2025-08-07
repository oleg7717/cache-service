package ru.interprocom.axioma.cache.repository;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

public class RefreshRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
		implements RefreshRepository<T, ID> {
	@Autowired
	EntityManager entityManager;

	public RefreshRepositoryImpl(JpaEntityInformation entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
	}

	@Override
	@Transactional
	public void refresh(T t) {
		entityManager.refresh(t);
	}
}
