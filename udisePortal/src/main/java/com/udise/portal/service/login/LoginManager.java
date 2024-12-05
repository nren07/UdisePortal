package com.udise.portal.service.login;

import com.udise.portal.vo.client.ClientLoginReqVo;
import com.udise.portal.vo.client.ClientLoginResVo;
import com.udise.portal.vo.admin.SuperLoginResVo;
import com.udise.portal.vo.user.UserLoginReqVo;
import com.udise.portal.vo.user.UserLoginResVo;

public interface LoginManager{
    ClientLoginResVo clientLogin(ClientLoginReqVo obj);
    UserLoginResVo userLogin(UserLoginReqVo obj);
    ClientLoginResVo get(ClientLoginReqVo obj);
    SuperLoginResVo superLogin(UserLoginReqVo obj);
}
