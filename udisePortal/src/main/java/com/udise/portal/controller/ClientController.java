package com.udise.portal.controller;

import com.udise.portal.service.client.ClientManager;
import com.udise.portal.service.login.LoginManager;
import com.udise.portal.vo.client.ClientLoginReqVo;
import com.udise.portal.vo.client.ClientLoginResVo;
import com.udise.portal.vo.client.CreditUpdateReqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("ClientController")
@RequestMapping("/v1/client")
public class ClientController {
    @Autowired
    private ClientManager clientManager;

    @RequestMapping(value = "/addCredit", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> clientLogin(@RequestBody CreditUpdateReqVo obj) throws Exception {
        clientManager.addCredit(obj);
        return new ResponseEntity<>("Credit Points Added", HttpStatus.OK);
    }

}
