package com.example.SilkWay.controller;

import com.example.SilkWay.model.Hotel;
import com.example.SilkWay.model.User;
import com.example.SilkWay.service.HotelService;
import com.example.SilkWay.service.StorageService;
import com.example.SilkWay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@Transactional
public class HotelController {

    private StorageService storageService;

    private HotelService hotelService;

    private UserService userService;
    
    @Autowired
    public HotelController(StorageService storageService, HotelService hotelService, UserService userService) {
        this.storageService = storageService;
        this.hotelService = hotelService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('SUPER_ADMIN, ADMIN')")
    @RequestMapping("/addHotel")
    public String addHotel()
    {
        return "regHotel";
    }

    @PreAuthorize("hasRole('SUPER_ADMIN, ADMIN')")
    @RequestMapping(value="/regHotel", method = RequestMethod.POST)
    public RedirectView saveNewHotel(@RequestParam("file") MultipartFile file, Model model, Hotel hotel, HttpServletRequest request) {
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

        //hotel.setImg_name(file.getOriginalFilename());
        hotelService.saveHotel(hotel);
        return new RedirectView(request.getHeader("referer"));
    }

    @PreAuthorize("hasRole('SUPER_ADMIN, ADMIN')")
    @RequestMapping("/updateHotel/{id}")
    public String updateHotel(Model model, @PathVariable("id")long id){
        model.addAttribute("hotel", hotelService.getHotelById(id));
        return "updateHotel";
    }

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN, ROLE_ADMIN')")
    @RequestMapping(value = "/updateHotel",method = RequestMethod.POST)
    public String updateHotel(@Valid Hotel hotel, MultipartFile file){
        storageService.store(file);
        hotelService.updateHotel(hotel, file);
        return "redirect:/findHotel="+hotel.getId();
    }

    @RequestMapping("/findHotels")
    public String findHotel(Model model){
        List<Hotel> list=hotelService.getAllHotels();
        model.addAttribute("hotels", list);
        return "allHotels";
    }

    @RequestMapping("/hotelInfo/{id}")
    public String showHotel(Model model, @PathVariable("id")long id, Principal principal){
        Hotel popular=hotelService.getHotelById(id);
        model.addAttribute("hotel", popular);
        return "hotels";
    }

    @PreAuthorize("hasRole('SUPER_ADMIN, ADMIN')")
    @RequestMapping("/deleteHotel/{id}")
    public String deleteHotel( @PathVariable("id")long id){
        hotelService.deleteHotel(hotelService.getHotelById(id));
        return "redirect:/findHotel";
    }

    @ModelAttribute("hotel")
    public Hotel createModel() {
        return new Hotel();
    }

    @RequestMapping(value = "/filterHotel", method = RequestMethod.POST)
    public String filterTour(@RequestParam(name = "title") String title,
                             @RequestParam(name = "category") String category,
                             @RequestParam(name = "stars") long stars,
                             Model model){
        List<Hotel> hotels = hotelService.filterHotels(title, category, stars);
        model.addAttribute("hotels", hotels);
        return "filterHotels";
    }

    @RequestMapping("/findHotel")
    public String findTour(){
        return "findHotel";
    }
}
