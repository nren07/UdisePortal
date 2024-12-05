package com.udise.portal.controller;

import com.udise.portal.service.login.LoginManager;
import com.udise.portal.vo.client.ClientLoginReqVo;
import com.udise.portal.vo.client.ClientLoginResVo;
import com.udise.portal.vo.admin.SuperLoginResVo;
import com.udise.portal.vo.user.UserLoginReqVo;
import com.udise.portal.vo.user.UserLoginResVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("LoginController")
@RequestMapping("/v1/login")
public class LoginController {

    @Autowired
    private LoginManager loginManager;

    @RequestMapping(value = "/client", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ClientLoginResVo> clientLogin(@RequestBody ClientLoginReqVo obj) {
        ClientLoginResVo res=loginManager.clientLogin(obj);
        System.out.println(res);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<UserLoginResVo> userLogin(@RequestBody UserLoginReqVo obj) {
        System.out.println("login request received");
        UserLoginResVo res=loginManager.userLogin(obj);
        System.out.println(res);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
    @RequestMapping(value = "/admin", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<SuperLoginResVo> adminLogin(@RequestBody UserLoginReqVo obj) {
        System.out.println("login request received");
        SuperLoginResVo res=loginManager.superLogin(obj);
        System.out.println(res);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
