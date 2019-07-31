package com.example.SilkWay.controller;

import com.example.SilkWay.model.BookHotel;
import com.example.SilkWay.service.BookHotelService;
import com.example.SilkWay.service.HotelService;
import com.example.SilkWay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@Controller
@Transactional
public class BookHotelController {

    @Autowired
    private BookHotelService bookHotelService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private UserService userService;

    @RequestMapping("/bookHotel/{id}")
    public String bookHotelId(Model model, @PathVariable("id")long id){
        model.addAttribute("hotel", hotelService.getHotelById(id));
        return "bookHotel";
    }

    @RequestMapping(value = "/bookingHotel", method = RequestMethod.POST)
    public String bookHotel(HttpServletRequest request,
                            @Valid BookHotel bookHotel,
                            BindingResult bindingResult,
                            @RequestParam(name = "hotelId") long hotelId){
        if (bindingResult.hasErrors()) {
            return "errorBookingHotel";
        } else {
            bookHotelService.bookHotel(hotelId, bookHotel, request);
            return "redirect:/findHotels";
        }
    }

    @RequestMapping("/bookedHotels")
    public String getBookedHotels(HttpServletRequest request, Model model){
        int id = userService.findUserByEmail(request.getUserPrincipal().getName()).getId();
        List<BookHotel> bookedHotels = bookHotelService.getBookedHotel(id);
        model.addAttribute("bookHotels", bookedHotels);
        return "myBookedHotels";
    }
}
