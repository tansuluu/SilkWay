package com.example.SilkWay.service;

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

    @Autowired
    public EmailService(JavaMailSender mailSender,
                        HotelService hotelService) {
        this.sender = mailSender;
        this.hotelService = hotelService;
    }

    @Async
    public void sendEmail(SimpleMailMessage email) {
        sender.send(email);
    }

    @Async
    public String sendMail(int numAdult, int numChild, int numOfRoom,
                          long hotelId, Date dateFrom, Date dateTo,
                          HttpServletRequest request) {

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo("mederbek.abdyldaev@iaau.edu.kg");
            helper.setText("Number of Adults: "+ numAdult+"\n"+
                    "Number of Child: "+ numChild + "\n"+
                    "Number of Rooms: "+ numOfRoom + "\n"+
                    "Check In: "+ dateFrom + "\n"+
                    "Check Out: "+ dateTo);
            helper.setSubject("Mail From: "+ request.getUserPrincipal().getName());
        } catch (MessagingException e) {
            e.printStackTrace();
            return "Error while sending mail ..";
        }
        sender.send(message);
        return "redirect:/hotelInfo/"+hotelId;
    }
}