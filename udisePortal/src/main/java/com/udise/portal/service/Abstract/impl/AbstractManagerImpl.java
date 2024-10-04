package com.udise.portal.service.Abstract.impl;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udise.portal.dao.AbstractDao;
import com.udise.portal.service.Abstract.AbstractManager;

@Service
@Transactional
public class AbstractManagerImpl implements AbstractManager {

	@Autowired
	private AbstractDao abstractDao;
	
	public AbstractManagerImpl(@Qualifier("abstractDaoImpl") AbstractDao abstractDao) {
        this.abstractDao = abstractDao;
    }

	@Override
	public <T> T save(T entity) {
		return abstractDao.save(entity);
	}

	@Override
	public <T> T update(T entity) {
		return abstractDao.update(entity);
	}

	@Override
	public <T> T saveOrUpdate(T entity) {
		return abstractDao.saveOrUpdate(entity);
	}

	@Override
	public <T> void delete(T entity) {
		abstractDao.delete(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public <T> T getById(Class<T> entityClass, Serializable id) {
		return abstractDao.getById(entityClass, id);
	}

	@Override
	public void flush() {
		abstractDao.flush();
	}

	@Override
	public void commit() {
		abstractDao.commit();
	}

	@Override
	public void clear() {
		abstractDao.clear();
	}

	@Override
	public <T> T saveAll(Collection<T> entity) {
		return abstractDao.saveAll(entity);
	}
}
