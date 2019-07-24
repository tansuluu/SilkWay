package com.example.SilkWay.controller;

import com.example.SilkWay.model.Tour;
import com.example.SilkWay.model.User;
import com.example.SilkWay.service.StorageService;
import com.example.SilkWay.service.TourService;
import com.example.SilkWay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.List;

@Controller
@Transactional
public class TourController {

    private StorageService storageService;

    private UserService userService;

    private TourService tourService;

    @Autowired
    public TourController(StorageService storageService, UserService userService, TourService tourService) {
        this.storageService = storageService;
        this.userService = userService;
        this.tourService = tourService;
    }

    @PreAuthorize("hasRole('SUPER_ADMIN, ADMIN')")
    @RequestMapping("/addTour")
    public String addTour(){
        return "regTour";
    }

    @PreAuthorize("hasRole('SUPER_ADMIN, ADMIN')")
    @RequestMapping(value="/regTour", method = RequestMethod.POST)
    public RedirectView saveNewTour(@RequestParam("file") MultipartFile file,
                                    Model model,
                                    @RequestParam(name = "title") String title,
                                    @RequestParam(name = "price") long price,
                                    @RequestParam(name = "country") String country,
                                    @RequestParam(name = "description") String description,
                                    @RequestParam(name = "dateFrom") @DateTimeFormat(pattern="yyyy-MM-dd") Date dateFrom,
                                    @RequestParam(name = "dateTo") @DateTimeFormat(pattern="yyyy-MM-dd") Date dateTo,
                                    HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        try {
            storageService.store(file);
            model.addAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
            tourService.saveTour(title, price, country, description, dateFrom, dateTo, file);


        } catch (Exception e) {
            System.out.println(e.getMessage());
            model.addAttribute("message", "FAIL to upload " + file.getOriginalFilename() + "!");
        }

        return new RedirectView(request.getHeader("referer"));
    }

    @PreAuthorize("hasRole('SUPER_ADMIN, ADMIN')")
    @RequestMapping("/updateTour/{id}")
    public String update(Model model, @PathVariable("id")long id){
        model.addAttribute("tour", tourService.getTourById(id));
        return "updateTour";
    }

    @PreAuthorize("hasRole('SUPER_ADMIN, ADMIN')")
    @RequestMapping(value = "/updateTour",method = RequestMethod.POST)
    public String update(@Valid Tour tour, MultipartFile file){
        storageService.store(file);
        tourService.updateTour(tour, file);
        return "redirect:/tourInfo?id="+tour.getId();
    }

    @RequestMapping("/findTours")
    public String find(Model model){
        List<Tour> list=tourService.getAllTours();
        model.addAttribute("tours", list);
        return "allTours";
    }

    @RequestMapping("/tourInfo/{id}")
    public String showApplications(Model model, @PathVariable("id")long id, Principal principal){
        Tour popular=tourService.getTourById(id);
        model.addAttribute("tour", popular);
        return "tours";
    }

    @PreAuthorize("hasRole('SUPER_ADMIN, ADMIN')")
    @RequestMapping("/deleteTour/{id}")
    public String showApplications( @PathVariable("id")long id){
        tourService.deleteTour(tourService.getTourById(id));
        return "redirect:/findTours";
    }

    @RequestMapping("/buyTour/{id}")
    public String buyTour( @PathVariable("id")long id, HttpServletRequest request){
        tourService.buyTour(tourService.getTourById(id), request);
        return "redirect:/myTours";
    }

    @RequestMapping("/myTours")
    public String myTours(HttpServletRequest request, Model model){
        List<Tour> list = userService.findUserByEmail(request.getUserPrincipal().getName()).getTours();
        model.addAttribute("tours", list);
        return "myTours";
    }

    @RequestMapping(value = "/filterTour", method = RequestMethod.POST)
    public String filterTour(@RequestParam(name = "country") String country,
                             @RequestParam(name = "priceMin") long priceMin,
                             @RequestParam(name = "priceMax") long priceMax,
                             @RequestParam(name = "dateFrom") @DateTimeFormat(pattern="yyyy-MM-dd") Date dateFrom,
                             @RequestParam(name = "dateTo") @DateTimeFormat(pattern="yyyy-MM-dd") Date dateTo,
                             Model model){
        List<Tour> list = tourService.filterTour(country, priceMin, priceMax, dateFrom, dateTo);
        model.addAttribute("tours", list);
        return "filterTours";
    }

    @RequestMapping("/findTour")
    public String findTour(){
        return "findTour";
    }

}
