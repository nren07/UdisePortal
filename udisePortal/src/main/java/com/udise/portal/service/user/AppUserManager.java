package com.udise.portal.service.user;

import com.udise.portal.common.ListResponse;
import com.udise.portal.common.SearchCriteria;
import com.udise.portal.entity.AppUser;
import com.udise.portal.service.Abstract.AbstractManager;
import com.udise.portal.vo.user.AppUserGetAllVo;
import com.udise.portal.vo.user.AppUserVo;
import com.udise.portal.vo.user.UserResVo;

import java.util.List;

public interface AppUserManager extends AbstractManager {
    public List<UserResVo> getUsers(Long clientId);
    public abstract ListResponse<AppUserGetAllVo> getAll(final SearchCriteria searchCriteria);


}
