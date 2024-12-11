package com.udise.portal.controller;

import com.udise.portal.common.ListResponse;
import com.udise.portal.common.SearchCriteria;
import com.udise.portal.common.WebUtils;
import com.udise.portal.dao.AppUserDao;
import com.udise.portal.service.user.AppUserManager;
import com.udise.portal.vo.user.AppUserGetAllVo;
import com.udise.portal.vo.user.UserResVo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("UserController")
@RequestMapping("/v1/users")
public class UserController  {


    @Autowired
    private AppUserManager appUserManager;
    @Autowired
    private AppUserDao appUserDao;

    @RequestMapping(value = "/{clientId}/getUsers", method = RequestMethod.GET)
    public ResponseEntity<List<UserResVo>> getUsers(@PathVariable Long clientId) throws Exception {
        List<UserResVo> res= appUserManager.getUsers(clientId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
