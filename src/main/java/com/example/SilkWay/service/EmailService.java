package com.example.SilkWay.service;

import com.example.SilkWay.model.BookHotel;
import com.example.SilkWay.model.BuyTour;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
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
    public String sendMailHotel(BookHotel bookHotel) {

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo("mederbek.abdyldaev@iaau.edu.kg");
            if(bookHotel.getUserStatus().equals("company")){
                helper.setText("Which Hotel To Book: "+ bookHotel.getHotelName() + "\n"+
                                "Email of Company: " + bookHotel.getUserEmail());
                helper.setSubject("Mail From: " + bookHotel.getCompanyTitle());
            }else {
                helper.setText("Which Hotel To Book: " + bookHotel.getHotelName() + "\n" +
                        "User status: " + bookHotel.getUserStatus() + "\n" +
                        "Email of User: " + bookHotel.getUserEmail() + "\n" +
                        "Contact of this User: " + bookHotel.getContacts());
                helper.setSubject("Mail From: " + bookHotel.getUserName());
            }
        } catch (MessagingException e) {
            e.printStackTrace();
            log.error("Error while sending mail");
            return "Error while sending mail ..";
        }
        sender.send(message);
        return "redirect:/hotelInfo/"+hotelService.getHotelByTitle(bookHotel.getHotelName()).getId();
    }

    @Async
    public String sendMailTour(BuyTour buyTour) {

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo("mederbek.abdyldaev@iaau.edu.kg");
            if(buyTour.getUserStatus().equals("company")){
                helper.setText("Which Tour To Buy: "+ buyTour.getTourName() + "\n"+
                        "Email of Company: " + buyTour.getUserEmail());
                helper.setSubject("Mail From: " + buyTour.getCompanyTitle());
            }else {
                helper.setText("Which Tour To Buy: " + buyTour.getTourName() + "\n" +
                        "User status: " + buyTour.getUserStatus() + "\n" +
                        "Email of User: " + buyTour.getUserEmail() + "\n" +
                        "Contact of this User: " + buyTour.getContacts());
                helper.setSubject("Mail From: " + buyTour.getUserName());
            }
        } catch (MessagingException e) {
            e.printStackTrace();
            log.error("Error while sending mail");
            return "Error while sending mail ..";
        }
        sender.send(message);
        return "redirect:/tourInfo/"+tourService.getTourByTitle(buyTour.getTourName()).getId();
    }
}