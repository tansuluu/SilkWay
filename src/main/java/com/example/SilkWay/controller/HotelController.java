package com.example.SilkWay.controller;

import com.example.SilkWay.model.Hotel;
import com.example.SilkWay.model.User;
import com.example.SilkWay.service.HotelService;
import com.example.SilkWay.service.StorageService;
import com.example.SilkWay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private StorageService storageService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private UserService userService;


    @RequestMapping("/addHotel")
    public String addHotel()
    {
        return "regHotel";
    }

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

    @RequestMapping("/updateHotel/{id}")
    public String updateHotel(Model model, @PathVariable("id")long id){
        model.addAttribute("hotel", hotelService.getHotelById(id));
        return "updateHotel";
    }

    @RequestMapping(value = "/updateHotel",method = RequestMethod.POST)
    public String updateHotel(@Valid Hotel hotel, MultipartFile file){
        storageService.store(file);
        hotelService.updateHotel(hotel, file);
        return "redirect:/findHotel="+hotel.getId();
    }

    @RequestMapping("/findHotel")
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

    @RequestMapping("/deleteHotel/{id}")
    public String deleteHotel( @PathVariable("id")long id){
        hotelService.deleteHotel(hotelService.getHotelById(id));
        return "redirect:/findHotel";
    }

    @ModelAttribute("hotel")
    public Hotel createModel() {
        return new Hotel();
    }
}
