package com.example.SilkWay.controller;

import com.example.SilkWay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @RequestMapping("/adminUsers")
    public String admin(Model model){
        model.addAttribute("users",userService.getAll());
        return "adminUsers";
    }
}
