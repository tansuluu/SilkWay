package com.example.SilkWay.controller;

import com.example.SilkWay.model.BookHotel;
import com.example.SilkWay.service.BookHotelService;
import com.example.SilkWay.service.HotelService;
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
import java.security.Principal;
import java.util.List;

@Controller
@Transactional
public class BookHotelController {

    @Autowired
    private BookHotelService bookHotelService;

    @Autowired
    private HotelService hotelService;

    @RequestMapping("/bookHotel/{id}")
    public String bookHotelId(Model model, @PathVariable("id")long id, HttpServletRequest request, Principal user){
        if(user == null){
            model.addAttribute("hotel", hotelService.getHotelById(id));
            return "bookHotel";
        }else {
            bookHotelService.bookHotelUser(id, request);
            return "redirect:/findHotels";
        }
    }

    @RequestMapping(value = "/bookingHotel", method = RequestMethod.POST)
    public String bookHotel(@Valid BookHotel bookHotel,
                            BindingResult bindingResult,
                            @RequestParam(name = "hotelId") long hotelId){
        if (bindingResult.hasErrors()) {
            return "errorBookingHotel";
        } else {
            bookHotelService.bookHotel(hotelId, bookHotel);
            return "redirect:/findHotels";
        }
    }

    @RequestMapping("/bookedHotels")
    public String getBookedHotels(HttpServletRequest request, Model model){
        List<BookHotel> bookedHotels = bookHotelService.getBookedHotel(request.getUserPrincipal().getName());
        model.addAttribute("bookHotels", bookedHotels);
        return "myBookedHotels";
    }

    @RequestMapping("/allBookHotels")
    public String getAllBookHotels(Model model){
        List<BookHotel> bookHotels = bookHotelService.getAll();
        model.addAttribute("bookHotels", bookHotels);
        return "allBookHotels";
    }

    @RequestMapping("/answerBookHotel/{id}")
    public String changeAnswer(@PathVariable("id") long id){
        bookHotelService.changeAnswered(id);
        return "redirect:/allBookHotels";
    }
}
