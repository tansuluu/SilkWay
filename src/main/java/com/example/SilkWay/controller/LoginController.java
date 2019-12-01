package com.example.SilkWay.controller;

import com.example.SilkWay.model.Role;
import com.example.SilkWay.model.User;
import com.example.SilkWay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;

@Controller
@Transactional
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(value={"/"}, method = RequestMethod.GET)
    public String homePage(){
        return "index";
    }


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loginPage(@RequestParam(value = "error",required = false) String error,
                                  @RequestParam(value = "logout",	required = false) String logout) {

        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("error", "Почта или пароль неверны");
        }

        if (logout != null) {
            model.addObject("message", "Logged out from JournalDEV successfully.");
        }

        model.setViewName("loginPage");
        //System.out.println("I am Login");
        return model;
    }

    @RequestMapping(value="/home", method = RequestMethod.GET)
    public ModelAndView userHome(){
        ModelAndView modelAndView = new ModelAndView();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        System.out.println(user.getRoles());

        final PageRequest page = new PageRequest(
                0, 4, new Sort(
                new Sort.Order(Sort.Direction.DESC, "likes")
        )
        );
        for (Role role:user.getRoles()) {
            System.out.println(role.getRole());
            if(role.getRole().equals("USER")){
                //System.out.println("geldi");
                modelAndView.setViewName("index");
                return modelAndView;
            }
        }

        modelAndView.addObject("userName", "Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        modelAndView.setViewName("home");
        return modelAndView;
    }

    @RequestMapping(value="/homeCompany", method = RequestMethod.GET)
    public ModelAndView companyHome(){
        ModelAndView modelAndView = new ModelAndView();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        System.out.println(user.getRoles());
        for (Role role:user.getRoles()) {
            System.out.println(role.getRole());
            if(role.getRole().equals("COMPANY")){
                //System.out.println("geldi");
                modelAndView.setViewName("homeCompany");
                return modelAndView;
            }
        }
        modelAndView.addObject("userName", "Welcome " + user.getTitle() + " " + user.getBrand() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        modelAndView.setViewName("homeCompany");
        return modelAndView;
    }


    @PreAuthorize("hasRole('SUPER_ADMIN, ADMIN')")
    @RequestMapping(value="/homeAdmin", method = RequestMethod.GET)
    public ModelAndView adminHome(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        System.out.println(user.getRoles());
        for (Role role:user.getRoles()) {
            System.out.println(role.getRole());
            if(role.getRole().equals("ADMIN")){
                //System.out.println("geldi");
                modelAndView.setViewName("homeAdmin");
                return modelAndView;
            }
        }
        modelAndView.setViewName("homeAdmin");
        return modelAndView;
    }

//    private String getPrincipal(){
//        String userName = null;
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        if (principal instanceof UserDetails) {
//            userName = ((UserDetails)principal).getUsername();
//        } else {
//            userName = principal.toString();
//        }
//        return userName;
//    }

}
