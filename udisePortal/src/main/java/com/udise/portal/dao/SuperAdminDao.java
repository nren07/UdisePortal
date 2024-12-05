package com.udise.portal.dao;

import com.udise.portal.entity.AppUser;
import com.udise.portal.entity.SuperAdmin;

public interface SuperAdminDao extends AbstractDao {
    SuperAdmin findByEmail(String email);
}
