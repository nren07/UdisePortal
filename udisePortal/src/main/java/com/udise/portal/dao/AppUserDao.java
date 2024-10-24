package com.udise.portal.dao;

import com.udise.portal.entity.AppUser;

import java.util.List;

public interface AppUserDao extends AbstractDao{

    AppUser findByEmail(String email);

    AppUser findByUserName(final String userName);

    AppUser getById(Long id);

    AppUser findOldPassByEmail(final String oldPassword);

    List<AppUser> findByRoleId(Long roleId);

    AppUser getUserByUserName(String email);

    List<AppUser> getUserList(Long clientId);


}