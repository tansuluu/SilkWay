package com.example.SilkWay.controller;

import com.example.SilkWay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@Transactional
public class MainController {

    @Autowired
    UserService userService;

    @RequestMapping(path = "/")
    public String index(Model model){
        model.addAttribute("login");

        return "login";
    }
    @RequestMapping(path = "/home")
    public String homeUser(){
        return "home";
    }
    @RequestMapping(path = "/homeCompany")
    public String homeCompany(){
        return "homeCompany";
    }

    @RequestMapping(path = "/superAdmin")
    public String superAdmin(){
        return "superAdmin";
    }

    @RequestMapping(path = "/homeAdmin")
    public String homeAdmin(){
        return "homeAdmin";
    }

}