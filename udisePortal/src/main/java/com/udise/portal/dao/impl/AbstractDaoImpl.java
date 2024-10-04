package com.udise.portal.dao.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import org.hibernate.Session;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.udise.portal.dao.AbstractDao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
@Primary
@Transactional
public class AbstractDaoImpl implements AbstractDao {

	@PersistenceContext
	private EntityManager em;

	public EntityManager getEm() {
		return this.em;
	}

	@Override
	public <T> T save(T entity) {
		Assert.notNull(entity, "'entity' is required");
		getCurrentSession().save(entity);
		return entity;
	}

	@Override
	public <T> T update(T entity) {
		Assert.notNull(entity, "'entity' is required");
		getCurrentSession().update(entity);
		return entity;
	}

	@Override
	public <T> T saveOrUpdate(T entity) {
		Assert.notNull(entity, "'entity' is required");
		getCurrentSession().saveOrUpdate(entity);
		return entity;
	}

	@Override
	public <T> void delete(T entity) {
		Assert.notNull(entity, "'entity' is required");
		em.remove(em.contains(entity) ? entity : em.merge(entity));
	}

	@Override
	public <T> T getById(Class<T> entityClass, Serializable id) {
		Assert.notNull(entityClass, "'entityClass' is required");
		Assert.notNull(id, "'id' is required");
		return getCurrentSession().get(entityClass, id);
	}


	@Override
	@Transactional
	public Session getCurrentSession() {
		return em.unwrap(Session.class);
	}

	@Override
	public Connection getConnection() {
		Connection connection = null;
		Session session = getCurrentSession();
		SessionFactoryImpl sessionFactory = (SessionFactoryImpl) session.getSessionFactory();
		try {
			connection = sessionFactory.getJdbcServices().getBootstrapJdbcConnectionAccess().obtainConnection();
		} catch (SQLException e) {
			System.out.println("Error while retrieving connection from Hibernate session: " + e.getMessage());
		}
		return connection;
	}

	@Override
	public void flush() {
		getCurrentSession().flush();
	}

	@Override
	public void commit() {
		getCurrentSession().getTransaction().commit();
	}

	@Override
	public void clear() {
		getCurrentSession().clear();
	}

	@Override
	public void setSessionReadOnly() {
		getCurrentSession().setDefaultReadOnly(true);
	}

	@Override
	public <T> T saveAll(Collection<T> collection) {
		for (T entity : collection) {
			save(entity);
		}
		flush();
		clear();
		return (T) collection;
	}
}
