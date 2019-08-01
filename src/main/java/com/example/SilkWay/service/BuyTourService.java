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

    public BuyTour buyTour(long tourId, BuyTour buyTour){
        buyTour.setTourName(tourService.getTourById(tourId).getTitle());
        buyTour.setUserStatus("user");
        buyTourRepository.save(buyTour);
        emailService.sendMailTour(buyTour);
        return buyTour;
    }

    public BuyTour buyTourUser(long tourId, HttpServletRequest request){

        BuyTour buyTour = new BuyTour();
        User user = userService.findUserByEmail(request.getUserPrincipal().getName());

        if (user.getStatus().equals("company")) {

            buyTour.setCompanyTitle(user.getTitle());

        } else {

            buyTour.setUserName(user.getFirstName() + " " + user.getLastName());

        }

        buyTour.setTourName(tourService.getTourById(tourId).getTitle());
        buyTour.setUserEmail(user.getEmail());
        buyTour.setUserStatus(user.getStatus());
        buyTour.setContacts(user.getContacts());
        buyTourRepository.save(buyTour);
        emailService.sendMailTour(buyTour);
        return buyTour;
    }

    public List<BuyTour> getBoughtTours(String userEmail){
        return buyTourRepository.findByUserEmail(userEmail);
    }
}
