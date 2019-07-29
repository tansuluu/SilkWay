package com.example.SilkWay.controller;

import com.example.SilkWay.model.Hotel;
import com.example.SilkWay.model.Role;
import com.example.SilkWay.model.Tour;
import com.example.SilkWay.model.User;
import com.example.SilkWay.service.HotelService;
import com.example.SilkWay.service.TourService;
import com.example.SilkWay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private TourService tourService;

    @Autowired
    private HotelService hotelService;

    @RequestMapping("/allCompanies")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String admin(Model model){
        model.addAttribute("companies",userService.getAllCompany());
        model.addAttribute("user", new User());
        return "adminCompany";
    }

    @RequestMapping("/adminPage")
    public String adminPage(Model model){
        model.addAttribute("users",userService.getAllUser());
        model.addAttribute("user", new User());
        return "adminIndex";
    }

    @RequestMapping("/allTours")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String tours(Model model){
        model.addAttribute("tours",tourService.getAll());
        model.addAttribute("tour", new Tour());
        return "adminTour";
    }

    @RequestMapping("/allHotels")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String hotel(Model model){
        model.addAttribute("hotels",hotelService.getAll());
        model.addAttribute("hotel", new Hotel());
        return "adminHotel";
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
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
}
