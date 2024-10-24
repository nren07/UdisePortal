package com.udise.portal.user.impl;

import com.udise.portal.dao.AppUserDao;
import com.udise.portal.entity.AppUser;
import com.udise.portal.user.UserManager;
import com.udise.portal.vo.user.UserResVo;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserManagerImpl implements UserManager {

    @Autowired
    private AppUserDao appUserDao;

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
}
