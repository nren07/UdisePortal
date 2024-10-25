package com.udise.portal.service.sign_up;

import com.udise.portal.vo.client.ClientSignUpReqVo;
import com.udise.portal.vo.client.ClientSignUpResVo;
import com.udise.portal.vo.user.UserSignUpReqVo;
import com.udise.portal.vo.user.UserSignUpResVo;

public interface SignUpManager{
    ClientSignUpResVo registerClient(ClientSignUpReqVo obj) throws Exception;
    UserSignUpResVo registerUser(Long schoolId,UserSignUpReqVo obj) throws Exception;
}
