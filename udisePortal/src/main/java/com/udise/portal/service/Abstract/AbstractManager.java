package com.udise.portal.service.Abstract;

import java.io.Serializable;
import java.util.Collection;

public interface AbstractManager {
	<T> T save(T entity);

	<T> T update(T entity);

	<T> T saveOrUpdate(T entity);

	<T> void delete(T entity);

	<T> T getById(Class<T> entityClass, Serializable id);

	void flush();

	void commit();

	void clear();

	<T> T saveAll(Collection<T> entity);
}
