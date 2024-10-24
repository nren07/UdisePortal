package com.udise.portal.controller;

import com.udise.portal.sign_up.SignUpManager;

import com.udise.portal.vo.client.ClientSignUpReqVo;
import com.udise.portal.vo.client.ClientSignUpResVo;
import com.udise.portal.vo.user.UserSignUpReqVo;
import com.udise.portal.vo.user.UserSignUpResVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/sign_up")
public class SignUpController {

    @Autowired
    private SignUpManager signUpManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/reg-client")
    public ResponseEntity<ClientSignUpResVo> registerClient(@RequestBody ClientSignUpReqVo obj) throws Exception {
        ClientSignUpResVo res=signUpManager.registerClient(obj);
        if(res.getId()==null){
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/{schoolId}/reg-user")
    public ResponseEntity<UserSignUpResVo> registerUser(@PathVariable Long schoolId,@RequestBody UserSignUpReqVo obj) throws Exception {
        UserSignUpResVo res=signUpManager.registerUser(schoolId,obj);
        if(res.getPass()==null){
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }
}
