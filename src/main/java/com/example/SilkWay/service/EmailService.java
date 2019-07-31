package com.example.SilkWay.service;

import com.example.SilkWay.model.BookHotel;
import com.example.SilkWay.model.BuyTour;
import com.example.SilkWay.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service("emailService")
public class EmailService {

    private HotelService hotelService;

    private JavaMailSender sender;

    private TourService tourService;

    @Autowired
    public EmailService(JavaMailSender mailSender,
                        HotelService hotelService,
                        TourService tourService) {
        this.sender = mailSender;
        this.hotelService = hotelService;
        this.tourService = tourService;
    }

    @Async
    public void sendEmail(SimpleMailMessage email) {
        sender.send(email);
    }

    @Async
    public String sendMailHotel(BookHotel bookHotel, User user) {

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo("mederbek.abdyldaev@iaau.edu.kg");
            helper.setText("Which Hotel: "+ bookHotel.getHotelName() + "\n"+
                    "Number of Adults: "+ bookHotel.getNumOfAdults()+"\n"+
                    "Number of Child: "+ bookHotel.getNumOfChildren() + "\n"+
                    "Number of Rooms: "+ bookHotel.getNumOfRooms() + "\n"+
                    "Check In: "+ bookHotel.getCheckIn() + "\n"+
                    "Check Out: "+ bookHotel.getCheckOut() + "\n"+
                    "Email of User: " + user.getEmail() + "\n"+
                    "Contact of this User: " + user.getContacts());
            helper.setSubject("Mail From: "+ user.getFirstName()+
                    " "+user.getLastName());
        } catch (MessagingException e) {
            e.printStackTrace();
            return "Error while sending mail ..";
        }
        sender.send(message);
        return "redirect:/hotelInfo/"+hotelService.getHotelByTitle(bookHotel.getHotelName()).getId();
    }

    @Async
    public String sendMailTour(BuyTour buyTour, User user) {

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo("mederbek.abdyldaev@iaau.edu.kg");
            helper.setText("Which Tour: "+ buyTour.getTourName() + "\n"+
                    "Number of Tours: "+ buyTour.getNumOfTour()+"\n"+
                    "Email of User: " + user.getEmail() + "\n"+
                    "Contact of this User: " + user.getContacts());
            helper.setSubject("Mail From: "+ user.getFirstName()+
                    " "+user.getLastName());
        } catch (MessagingException e) {
            e.printStackTrace();
            return "Error while sending mail ..";
        }
        sender.send(message);
        return "redirect:/tourInfo/"+tourService.getTourByTitle(buyTour.getTourName()).getId();
    }
}