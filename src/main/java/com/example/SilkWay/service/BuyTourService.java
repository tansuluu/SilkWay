package com.example.SilkWay.service;

import com.example.SilkWay.model.BuyTour;
import com.example.SilkWay.model.User;
import com.example.SilkWay.repository.BuyTourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class BuyTourService {

    @Autowired
    private BuyTourRepository buyTourRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TourService tourService;

    @Autowired
    private EmailService emailService;

    public BuyTour buyTour(long tourId, BuyTour buyTour, HttpServletRequest request){
        User user = userService.findUserByEmail(request.getUserPrincipal().getName());

        buyTour.setTourName(tourService.getTourById(tourId).getTitle());
        buyTour.setUserId(user.getId());
        buyTourRepository.save(buyTour);
        emailService.sendMailTour(buyTour, user);
        return buyTour;
    }

    public List<BuyTour> getBoughtTours(long userId){
        return buyTourRepository.findByUserId(userId);
    }
}
