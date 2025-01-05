package com.crazy.filmzip.controller;

import com.crazy.filmzip.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainViewController {

    UserService userService;

    @GetMapping("/main")
    public String main() {
        System.out.println("/main");
        return "main";
    }
}
