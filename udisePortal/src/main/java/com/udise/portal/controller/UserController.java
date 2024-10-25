package com.udise.portal.controller;

import com.udise.portal.service.user.UserManager;
import com.udise.portal.vo.user.UserResVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("UserController")
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    private UserManager userManager;

    @RequestMapping(value = "/{clientId}/getUsers", method = RequestMethod.GET)
    public ResponseEntity<List<UserResVo>> getUsers(@PathVariable Long clientId) {
        List<UserResVo> res=userManager.getUsers(clientId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
