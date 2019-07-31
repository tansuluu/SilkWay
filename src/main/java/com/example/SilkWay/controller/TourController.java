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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
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
    public ModelAndView addTour(){

        ModelAndView modelAndView = new ModelAndView();
        Tour tour = new Tour();
        modelAndView.addObject("tour", tour);
        modelAndView.setViewName("regTour");
        return modelAndView;
    }

    @PreAuthorize("hasRole('SUPER_ADMIN, ADMIN')")
    @RequestMapping(value="/regTour", method = RequestMethod.POST)
    public String saveNewTour(@RequestParam("file") MultipartFile file,
                              Model model, @Valid Tour tour,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "adminTour";
        } else {
        try {
            storageService.store(file);
            model.addAttribute("message", "You successfully uploaded " +
                    file.getOriginalFilename() + "!");
            tour.setImg_name(file.getOriginalFilename());
            tourService.saveTour(tour);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            model.addAttribute("message", "FAIL to upload " + file.getOriginalFilename() + "!");
            return "adminTour";
        }
        }
        return "redirect:/allTours";
    }

    @PreAuthorize("hasRole('SUPER_ADMIN, ADMIN')")
    @RequestMapping("/updateTour/{id}")
    public String update(Model model, @PathVariable("id")long id){
        model.addAttribute("tour", tourService.getTourById(id));
        return "updateTour";
    }

    @PreAuthorize("hasRole('SUPER_ADMIN, ADMIN')")
    @RequestMapping(value = "/updateTour",method = RequestMethod.POST)
    public String update(@Valid Tour tour){
        tourService.updateTour(tour);
        return "redirect:/tourInfo/"+tour.getId();
    }

    @RequestMapping("/tourPage")
    public String find(Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "limit", defaultValue = "15") int limit){
        List<Tour> list=tourService.getAllTours(page, limit);
        model.addAttribute("tours", list);
        return "tour-place";
    }

    @RequestMapping("/tourInfo/{id}")
    public String showApplications(Model model, @PathVariable("id")long id){
        Tour popular=tourService.getTourById(id);
        model.addAttribute("tour", popular);
        return "tours";
    }

    @PreAuthorize("hasRole('SUPER_ADMIN, ADMIN')")
    @RequestMapping("/deleteTour/{id}")
    public String showApplications( @PathVariable("id")long id){
        tourService.deleteTour(tourService.getTourById(id));
        return "redirect:/allTours";
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

    @RequestMapping(value = "/filterTour", method = RequestMethod.GET)
    public String filterTour(@RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "limit", defaultValue = "15") int limit,
                             @RequestParam(name = "country") String country,
                             @RequestParam(name = "priceMin") long priceMin,
                             @RequestParam(name = "priceMax") long priceMax,
                             @RequestParam(name = "dateFrom") @DateTimeFormat(pattern="yyyy-MM-dd") Date dateFrom,
                             @RequestParam(name = "dateTo") @DateTimeFormat(pattern="yyyy-MM-dd") Date dateTo,
                             Model model){
        List<Tour> list = tourService.filterTour(country, priceMin, priceMax, dateFrom, dateTo, page, limit);
        model.addAttribute("tours", list);
        model.addAttribute("text", "Результат поиска");
        return "tour-place";
    }

    @RequestMapping("/findTour")
    public String findTour(){
        return "findTour";
    }

}
