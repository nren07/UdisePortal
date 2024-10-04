package com.udise.portal.controller;

import com.udise.portal.service.common_impl.ChromeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RestController
@RequestMapping("/")
public class UdiseJobController {
    @Autowired
    private ChromeService chromeService;

    @GetMapping("start")
    public void start() throws Exception {
        chromeService.startChrome("http://localhost:4444/wd/hub","12345");
//        chromeService.sshWithRSAKeyMina();
    }

    @GetMapping("start2")
    public void start2() throws MalformedURLException, InterruptedException {
        chromeService.startChrome("http://localhost:4445/wd/hub","67890");
    }
    @GetMapping("start3")
    public void start3() throws MalformedURLException, InterruptedException {
        chromeService.startChrome("http://localhost:4444/wd/hub","12345");
    }
}
