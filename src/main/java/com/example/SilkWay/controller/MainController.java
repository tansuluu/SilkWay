package com.example.SilkWay.controller;

import com.example.SilkWay.service.TourService;
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
    @Autowired
    TourService tourService;


    @RequestMapping("/hotelPage")
    public String hotel(Model model){
        return "hotelsPage";
    }
    @RequestMapping("/transportPage")
    public String transportPage(Model model){
        return "transport";
    }

    @RequestMapping("/aboutUs")
    public String aboutUs(Model model){
        return "about";
    }
}