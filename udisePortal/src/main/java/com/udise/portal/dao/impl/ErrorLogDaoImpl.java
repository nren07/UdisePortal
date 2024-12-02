package com.udise.portal.dao.impl;

import com.udise.portal.dao.ErrorLogDao;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ErrorLogDaoImpl extends AbstractDaoImpl implements ErrorLogDao {
}
