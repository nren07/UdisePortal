package com.udise.portal.dao;

import com.udise.portal.entity.Client;

import java.util.List;

public interface ClientDao extends AbstractDao{

    Client findByMobileAndEmail(final String mobile, String email);

    Client findByEmail(String email);

    Client findByUserName(final String userName);

    Client findOldPassByEmail(final String oldPassword);

    List<Client> findByModuleName(String module);

    List<Client> findByModuleId(Long moduleId);

    List<Client> findByRoleId(Long roleId);

}
