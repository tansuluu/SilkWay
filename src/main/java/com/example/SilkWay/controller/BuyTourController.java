package com.example.SilkWay.controller;

import com.example.SilkWay.model.BuyTour;
import com.example.SilkWay.service.BuyTourService;
import com.example.SilkWay.service.TourService;
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
import java.security.Principal;
import java.util.List;

@Controller
@Transactional
public class BuyTourController {

    @Autowired
    private UserService userService;

    @Autowired
    private TourService tourService;

    @Autowired
    private BuyTourService buyTourService;

    @RequestMapping("/buyTour/{id}")
    public String bookHotelId(Model model, @PathVariable("id")long id, HttpServletRequest request, Principal user){
        model.addAttribute("tour", tourService.getTourById(id));
        if(user == null){
            model.addAttribute("tour", tourService.getTourById(id));
            return "buyTour";
        }else {
            buyTourService.buyTourUser(id, request);
            return "redirect:/findTours";
        }
    }

    @RequestMapping(value = "/buyingTour", method = RequestMethod.POST)
    public String bookHotel(@Valid BuyTour buyTour,
                            BindingResult bindingResult,
                            @RequestParam(name = "tourId") long tourId){
        if (bindingResult.hasErrors()) {
            return "errorBuyingTour";
        } else {
            buyTourService.buyTour(tourId, buyTour);
            return "redirect:/tourInfo/"+tourId;
        }
    }

    @RequestMapping("/boughtTours")
    public String getBoughtTours(HttpServletRequest request, Model model){
        List<BuyTour> boughtTours = buyTourService.getBoughtTours(request.getUserPrincipal().getName());
        model.addAttribute("buyTours", boughtTours);
        return "myBoughtTours";
    }

    @RequestMapping("/allBoughtTours")
    public String getAllBookHotels(Model model){
        List<BuyTour> buyTours = buyTourService.getAll();
        model.addAttribute("buyTours", buyTours);
        return "allBoughtTours";
    }

    @RequestMapping("/answerBoughtTour/{id}")
    public String changeAnswer(@PathVariable("id") long id){
        buyTourService.changeAnswered(id);
        return "redirect:/allBoughtTours";
    }
}
