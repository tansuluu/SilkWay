package com.example.SilkWay.controller;

import com.example.SilkWay.service.EmailService;
import com.example.SilkWay.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class EmailController {

    private HotelService hotelService;

    private EmailService emailService;

    @Autowired
    public EmailController(HotelService hotelService, EmailService emailService) {
        this.hotelService = hotelService;
        this.emailService = emailService;
    }

    @RequestMapping(value = "/sendMail", method = RequestMethod.POST)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String sendMailToHotel(HttpServletRequest request,
                           @RequestParam(name = "id") long hotelId,
                           @RequestParam(name = "numAdult") int numAdult,
                           @RequestParam(name = "numChild") int numChild,
                           @RequestParam(name = "numOfRoom") int numOfRoom,
                           @RequestParam(name = "checkIn") @DateTimeFormat(pattern="yyyy-MM-dd") Date dateFrom,
                           @RequestParam(name = "checkOut") @DateTimeFormat(pattern="yyyy-MM-dd") Date dateTo) {

        return emailService.sendMail(numAdult, numChild, numOfRoom, hotelId, dateFrom, dateTo, request);
    }

    @RequestMapping("/sendEmail/{id}")
    public String findTour(Model model, @PathVariable("id")long id){
        model.addAttribute("hotel", hotelService.getHotelById(id));
        return "sendEmail";
    }
}
