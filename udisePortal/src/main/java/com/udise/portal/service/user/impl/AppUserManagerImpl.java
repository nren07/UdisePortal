package com.udise.portal.service.user.impl;

import com.udise.portal.common.ListResponse;
import com.udise.portal.common.SearchCriteria;
import com.udise.portal.dao.AbstractDao;
import com.udise.portal.dao.AppUserDao;
import com.udise.portal.entity.AppUser;
import com.udise.portal.service.Abstract.AbstractManager;
import com.udise.portal.service.Abstract.impl.AbstractManagerImpl;
import com.udise.portal.service.user.AppUserManager;
import com.udise.portal.vo.user.AppUserGetAllVo;
import com.udise.portal.vo.user.UserResVo;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AppUserManagerImpl extends AbstractManagerImpl implements AppUserManager {

    @Autowired
    private AppUserDao appUserDao;

    public AppUserManagerImpl(AbstractDao abstractDao) {
        super(abstractDao);
    }

    @Override
    public List<UserResVo> getUsers(Long clientId) {
        List<AppUser> userList= appUserDao.getUserList(clientId);
        List<UserResVo> res=new ArrayList<>();
        for(AppUser user:userList){
            System.out.println(user);
            UserResVo obj=new UserResVo();
            BeanUtils.copyProperties(user, obj);
            res.add(obj);
        }
        return res;
    }

    @Override
    public ListResponse<AppUserGetAllVo> getAll(SearchCriteria searchCriteria) {
        return null;
    }
}
