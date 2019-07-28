package com.example.SilkWay.controller;

import com.example.SilkWay.model.User;
import com.example.SilkWay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

@Controller
@Transactional
public class RegistrationController {

    @Autowired
    private UserService userService;

    @RequestMapping(value="/regUser", method = RequestMethod.GET)
    public ModelAndView registrationUser(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("userRegistration");
        return modelAndView;
    }
    @RequestMapping(value = "/regUser", method = RequestMethod.POST)
    public ModelAndView saveNewUser(@Valid User user, BindingResult bindingResult, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        System.out.println(123);
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "*There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("userRegistration");
        } else {
            user.setStatus("user");
            userService.saveUser(user, "USER");
            userService.sendTokenToConfirm(user,request);
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("loginPage");
            modelAndView.addObject("error", "Ссылка на потверждение, отправлена на Вашу почту ");
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('SUPER_ADMIN, ADMIN')")
    @RequestMapping(value="/regCompany", method = RequestMethod.GET)
    public ModelAndView registrationCompany(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("regCompany");
        return modelAndView;
    }

    @PreAuthorize("hasRole('SUPER_ADMIN, ADMIN')")
    @RequestMapping(value = "/regCompany", method = RequestMethod.POST)
    public ModelAndView createNewCompany(@Valid User user, BindingResult bindingResult, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "*There is already a company registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("regCompany");
        } else {
            user.setStatus("company");
            userService.saveUser(user, "COMPANY");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("login");

        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @RequestMapping(value="/regAdmin", method = RequestMethod.GET)
    public ModelAndView registrationAdmin(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("regAdmin");
        return modelAndView;
    }
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @RequestMapping(value = "/regAdmin", method = RequestMethod.POST)
    public ModelAndView saveNewAdmin(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "*There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("regAdmin");
        } else {
            user.setStatus("admin");
            userService.saveAdmin(user, "ADMIN");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("superAdmin");

        }
        return modelAndView;
    }

    @RequestMapping("/confirm")
    public String  confirm(@RequestParam("token") String token, Model model){
        User user=userService.findByToken(token);
        user.setActive(1);
        userService.save(user);
        return "redirect:/login";
    }

}
