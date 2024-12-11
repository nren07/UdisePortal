package com.udise.portal.controller;


import com.udise.portal.common.JwtUtil;
import com.udise.portal.entity.SuperAdmin;
import com.udise.portal.service.client.ClientManager;
import com.udise.portal.service.super_admin.SuperAdminManager;
import com.udise.portal.vo.client.CreditUpdateReqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController("SuperAdminController")
@RequestMapping("/v1/admin")
public class SuperAdminController {
    @Autowired
    private SuperAdminManager superAdminManager;

    @RequestMapping(value = "/{id}/addCredit", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> addCredit(@PathVariable UUID id, @RequestHeader("Authorization") String token, @RequestBody CreditUpdateReqVo obj) throws Exception {
        System.out.println(token);
        if (!isValidToken(token,id)) {
            return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        }
        superAdminManager.addCredit(obj);
        return new ResponseEntity<>("Credit Points Added", HttpStatus.OK);
    }

    private boolean isValidToken(String token,UUID id) {
        return superAdminManager.isValidSuperUser(token,id);
    }


}
