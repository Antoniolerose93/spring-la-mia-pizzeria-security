package org.pizzeria.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Logoutcontroller {

    @GetMapping("/logout-success")
    public String logoutSuccess(){
        return"logout-success";
    }
}
