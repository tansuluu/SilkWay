package com.example.SilkWay.controller;

import com.example.SilkWay.model.Hotel;
import com.example.SilkWay.model.User;
import com.example.SilkWay.service.HotelService;
import com.example.SilkWay.service.StorageService;
import com.example.SilkWay.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Transactional
@Slf4j
public class HotelController {

    private StorageService storageService;

    private HotelService hotelService;

    @Autowired
    public HotelController(StorageService storageService,
                           HotelService hotelService) {
        this.storageService = storageService;
        this.hotelService = hotelService;
    }

    @PreAuthorize("hasRole('SUPER_ADMIN, ADMIN')")
    @RequestMapping("/addHotel")
    public String addHotel() {
        return "regHotel";
    }

    @PreAuthorize("hasRole('SUPER_ADMIN, ADMIN')")
    @RequestMapping(value = "/regHotel", method = RequestMethod.POST)
    public String saveNewHotel(@RequestParam("file") MultipartFile file,
                               Model model, @Valid Hotel hotel,
                               BindingResult bindingResult,
                               HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("message","Не удалось добавить отель");
            return "adminHotel";
        } else {
            try {
                storageService.store(file);
                model.addAttribute("message", "You successfully uploaded " +
                        file.getOriginalFilename() + "!");
                hotel.setImg_name(file.getOriginalFilename());
                hotelService.saveHotel(hotel);

            } catch (Exception e) {
                System.out.println(e.getMessage());
                model.addAttribute("message", "Не удалось добавить отель из за фотографии "+file.getOriginalFilename() + "!");
                return "adminHotel";
            }
        }

        return "redirect:/allHotels";
    }

    @PreAuthorize("hasRole('SUPER_ADMIN, ADMIN')")
    @RequestMapping("/updateHotel/{id}")
    public String updateHotel(Model model, @PathVariable("id") long id) {
        model.addAttribute("hotel", hotelService.getHotelById(id));
        return "updateHotel";
    }

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN, ROLE_ADMIN')")
    @RequestMapping(value = "/updateHotel", method = RequestMethod.POST)
    public String updateHotel(@Valid Hotel hotel) {
        hotelService.updateHotel(hotel);
        return "redirect:/hotelInfo/" + hotel.getId();
    }

    @RequestMapping("/findHotels")
    public String findHotel(Model model,
                            @RequestParam(value = "page",defaultValue = "1") int page) {
        PageRequest pageRequest= PageRequest.of(page-1,5);
        Page<Hotel> adminPage=hotelService.getAll(pageRequest);
        int total=adminPage.getTotalPages();
        if(total>0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1,total).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("hotels", adminPage.getContent());
        return "allHotels";
    }

    @RequestMapping("/hotelInfo/{id}")
    public String showHotel(Model model, @PathVariable("id") long id) {
        Hotel popular = hotelService.getHotelById(id);
        model.addAttribute("hotel", popular);
        return "hotels";
    }

    @PreAuthorize("hasRole('SUPER_ADMIN, ADMIN')")
    @RequestMapping("/deleteHotel/{id}")
    public String deleteHotel(@PathVariable("id") long id) {
        hotelService.deleteHotel(hotelService.getHotelById(id));
        return "redirect:/allHotels";
    }

    @ModelAttribute("hotel")
    public Hotel createModel() {
        return new Hotel();
    }

    @RequestMapping(value = "/filterHotel", method = RequestMethod.POST)
    public String filterTour(@RequestParam(name = "title") String title,
                             @RequestParam(name = "category") String category,
                             @RequestParam(name = "stars") long stars,
                             @RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "limit", defaultValue = "15") int limit,
                             Model model) {
        List<Hotel> hotels = hotelService.filterHotels(title, category, stars);
        model.addAttribute("hotels", hotels);
        return "filterHotels";
    }

    @RequestMapping("/findHotel")
    public String findTour() {
        return "findHotel";
    }
}
