package com.example.SilkWay.controller;

import com.example.SilkWay.model.Hotel;
import com.example.SilkWay.model.Role;
import com.example.SilkWay.model.User;
import com.example.SilkWay.service.HotelService;
import com.example.SilkWay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@Transactional
public class LoginController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private UserService userService;

    @RequestMapping(value={"/"}, method = RequestMethod.GET)
    public String homePage(){
        return "login";
    }


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loginPage(@RequestParam(value = "error",required = false) String error,
                                  @RequestParam(value = "logout",	required = false) String logout) {

        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("error", "Invalid Credentials provided.");
        }

        if (logout != null) {
            model.addObject("message", "Logged out from JournalDEV successfully.");
        }

        model.setViewName("login");
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
                modelAndView.setViewName("home");
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

    @RequestMapping(value="/superAdmin", method = RequestMethod.GET)
    public ModelAndView superAdmin(){
        ModelAndView modelAndView = new ModelAndView();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        System.out.println(user.getRoles());
        for (Role role:user.getRoles()) {
            System.out.println(role.getRole());
            if(role.getRole().equals("SUPER_ADMIN")){
                //System.out.println("geldi");
                modelAndView.setViewName("superAdmin");
                return modelAndView;
            }
        }
        modelAndView.setViewName("superAdmin");
        return modelAndView;
    }

    @RequestMapping(value="/regUser", method = RequestMethod.GET)
    public ModelAndView registrationUser(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("regUser");
        return modelAndView;
    }
    @RequestMapping(value = "/regUser", method = RequestMethod.POST)
    public ModelAndView saveNewUser(@Valid User user, BindingResult bindingResult, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "*There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("regUser");
        } else {
            user.setStatus("user");
            userService.saveUser(user, "USER");
            userService.sendTokenToConfirm(user,request);
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("login");

        }

        return modelAndView;
    }

    @RequestMapping(value="/regCompany", method = RequestMethod.GET)
    public ModelAndView registrationCompany(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("regCompany");
        return modelAndView;
    }
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

    @RequestMapping(value="/regAdmin", method = RequestMethod.GET)
    public ModelAndView registrationAdmin(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("regAdmin");
        return modelAndView;
    }
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
            userService.saveUser(user, "ADMIN");
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

    private String getPrincipal(){
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails)principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

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
    public String update(Model model, @PathVariable("id")int id){
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




}
