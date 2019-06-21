package com.example.SilkWay.controller;

import com.example.SilkWay.model.Hotel;
import com.example.SilkWay.model.Tour;
import com.example.SilkWay.model.User;
import com.example.SilkWay.service.HotelService;
import com.example.SilkWay.service.StorageService;
import com.example.SilkWay.service.TourService;
import com.example.SilkWay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

@Controller
@Transactional
public class ContentController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private UserService userService;

    @Autowired
    private TourService tourService;

    @RequestMapping("/addHotel")
    public String addHotel()
    {
        return "regHotel";
    }

    @RequestMapping("/addTour")
    public String addTour(){
        return "regTour";
    }

    @RequestMapping(value="/regHotel", method = RequestMethod.POST)
    public RedirectView saveNewHotel(@RequestParam("file") MultipartFile file,Model model, Hotel hotel, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        try {
            storageService.store(file);
            model.addAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");

            hotel.setImg_name(file.getOriginalFilename());


        } catch (Exception e) {
            System.out.println(e.getMessage());
            model.addAttribute("message", "FAIL to upload " + file.getOriginalFilename() + "!");
        }

        hotelService.saveHotel(hotel);
        return new RedirectView(request.getHeader("referer"));
    }

    @RequestMapping(value="/regTour", method = RequestMethod.POST)
    public RedirectView saveNewTour(@RequestParam("file") MultipartFile file, Model model, Tour tour, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        try {
            storageService.store(file);
            model.addAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
            tour.setImg_name(file.getOriginalFilename());


        } catch (Exception e) {
            System.out.println(e.getMessage());
            model.addAttribute("message", "FAIL to upload " + file.getOriginalFilename() + "!");
        }

        tourService.saveTour(tour);
        return new RedirectView(request.getHeader("referer"));
    }

}
