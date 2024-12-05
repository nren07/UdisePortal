package com.udise.portal.dao.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import com.udise.portal.entity.AppUser;
import com.udise.portal.enums.Role;
import com.udise.portal.exception.SystemFailureException;
import com.udise.portal.utils.UserContextResolver;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

	protected static final String DOT = ".";

	@Autowired
	private UserContextResolver userContextResolver;

	@Override
	public <T> T save(T entity) {
		Assert.notNull(entity, "'entity' is required");
		this.getCurrentSession().save(entity);
		return entity;
	}

	@Override
	public <T> T update(T entity) {
		Assert.notNull(entity, "'entity' is required");
		this.getCurrentSession().update(entity);
		return entity;
	}

	@Override
	public <T> T saveOrUpdate(T entity) {
		Assert.notNull(entity, "'entity' is required");
		this.getCurrentSession().saveOrUpdate(entity);
		return entity;
	}

	@Override
	public <T> void delete(T entity) {
		Assert.notNull(entity, "'entity' is required");
		// this.getCurrentSession().delete(entity);
		em.remove(em.contains(entity) ? entity : em.merge(entity));
	}

	@Override
	public <T> T getById(Class<T> entityClass, Serializable id) {
		Assert.notNull(entityClass, "'entityClass' is required");
		Assert.notNull(id, "'id' is required");

		@SuppressWarnings("unchecked")
		T obj = (T) getCurrentSession().get(entityClass, id);
		return obj;
	}

	@Override
	public Session getCurrentSession() {
		return em.unwrap(SessionImplementor.class);
	}

	@Override
	public Connection getConnection() {
		Connection connection = null;
		Session session = getCurrentSession();
		SessionFactoryImpl sessionFactory = (SessionFactoryImpl) session.getSessionFactory();
		try {
			connection = sessionFactory.getJdbcServices().getBootstrapJdbcConnectionAccess().obtainConnection();
		} catch (SQLException sqlex) {
			throw new SystemFailureException("Error while retrieving connection from hibernate session", sqlex);
		}
		return connection;
	}

//	protected Criteria createCriteria(@SuppressWarnings("rawtypes") Class clazz) {
//		Assert.notNull(clazz, "'clazz' is required");
//		return getCurrentSession().createCriteria(clazz);
//	}

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

//	@Override
//	public RoleName getLoggedInUserRoleName() {
//		return userContextResolver.resolveLoggedInUserRole();
//	}

//	@Override
//	public AppUser getLoggedInUser() {
//		Long userId = userContextResolver.resolveLoggedInUserId();
//		AppUser appUser = userId != null ? this.getById(AppUser.class, userId) : null;
//		return appUser;
//	}


	@Override
	public Role getLoggedInUserRole() {
//		RoleName userRoleName = getLoggedInUserRoleName();

//		Criteria criteria = createCriteria(Role.class);
//		criteria.add(Restrictions.eq(Role.ACTIVE, true));
//		criteria.add(Restrictions.eq(Role.NAME, userRoleName.name()));

//		Role role = (Role) criteria.uniqueResult();
		return null;
	}

	@Override
	public String getIpAddress() {
		String ipAddress = userContextResolver.resolveIpAddress();
		return ipAddress;
	}

	@Override
	public <T> T saveAll(Collection<T> collection) {

		for (T t : collection) {
			save(t);
		}
		//flush when size is equal to batch size
		flush();
		clear();
		return (T) collection;
	}
}
