package com.security.securityImpl.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class classController {

    @GetMapping("/")
    public String greet(){
        return "Welcome to Genboot!!";

    }
}
