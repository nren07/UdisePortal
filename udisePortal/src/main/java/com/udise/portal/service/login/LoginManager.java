package com.udise.portal.service.login;

import com.udise.portal.entity.Client;
import com.udise.portal.vo.client.ClientLoginReqVo;
import com.udise.portal.vo.client.ClientLoginResVo;
import com.udise.portal.vo.user.UserLoginReqVo;
import com.udise.portal.vo.user.UserLoginResVo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface LoginManager{
    ClientLoginResVo clientLogin(ClientLoginReqVo obj);
    UserLoginResVo userLogin(UserLoginReqVo obj);
    ClientLoginResVo get(ClientLoginReqVo obj);
}
