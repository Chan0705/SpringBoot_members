package com.example.spring_friends.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/noneaccess")
    public String accessDenied() {
        return "error/noneaccess"; // templates/error/noneaccess.html
    }
}
