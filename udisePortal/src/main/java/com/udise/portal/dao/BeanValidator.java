package com.udise.portal.dao;

public interface BeanValidator {
    public abstract <T> void validate(T model, Class<?>... groups);
}
