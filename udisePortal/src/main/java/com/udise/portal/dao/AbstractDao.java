package com.udise.portal.dao;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import io.micrometer.common.util.StringUtils;
import org.hibernate.Session;
import org.openqa.selenium.InvalidArgumentException;

import java.sql.Connection;
import java.util.regex.Pattern;

public interface AbstractDao {
	<T> T save(T entity);

	<T> T saveAll(Collection<T> entity);

	<T> T update(T entity);

	<T> T saveOrUpdate(T entity);

	<T> void delete(T entity);

	<T> T getById(Class<T> entityClass, Serializable id);

	Session getCurrentSession();

	void flush();

	void commit();

	void clear();


	Connection getConnection();

	void setSessionReadOnly();

}
