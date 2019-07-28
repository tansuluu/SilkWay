package com.example.SilkWay.controller;

import com.example.SilkWay.model.User;
import com.example.SilkWay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;

@Controller
@Transactional
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @RequestMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id")int id){
        User user=userService.findUserById(id);
        if(user.getStatus().equals("admin")){
            return "redirect:/admin";
        }
        userService.deleteUser(user);
        return "redirect:/logout";
    }

    @RequestMapping("/updateUser/{id}")
    public String updateUser(Model model, @PathVariable("id")int id){
        model.addAttribute("user", userService.getUserById(id));
        return "updateUser";
    }

    @RequestMapping(value = "/updateUser",method = RequestMethod.POST)
    public String update(User user){
        userService.updateUser(user);
        return "redirect:/users";
    }

    @RequestMapping("/userInfo/{id}")
    public String showUsers(Model model, @PathVariable("id")int id){
        User user=userService.getUserById(id);
        model.addAttribute("user", user);
        return "users";
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @RequestMapping("/deleteCompany/{id}")
    public String deleteCompany(@PathVariable("id")int id){
        User user=userService.findUserById(id);
        if(user.getStatus().equals("admin")){
            return "redirect:/admin";
        }
        userService.deleteUser(user);
        return "redirect:/logout";
    }

    @RequestMapping("/updateCompany/{id}")
    public String updateCompany(Model model, @PathVariable("id")int id){
        model.addAttribute("user", userService.getUserById(id));
        return "updateCompany";
    }

    @RequestMapping(value = "/updateCompany",method = RequestMethod.POST)
    public String updateCompany(User user){
        userService.updateCompany(user);
        return "redirect:/companies";
    }

    @RequestMapping("/companyInfo/{id}")
    public String showCompanies(Model model, @PathVariable("id")int id){
        User user=userService.getUserById(id);
        model.addAttribute("user", user);
        return "companies";
    }

    @RequestMapping("/userPage")
    public String showUsers(Model model, @RequestParam("username") String username){
        User user=userService.findUserByEmail(username);
        model.addAttribute("user", user);
        return "home";
    }
}
