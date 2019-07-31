package com.example.SilkWay.service;

import com.example.SilkWay.model.BookHotel;
import com.example.SilkWay.model.User;
import com.example.SilkWay.repository.BookHotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class BookHotelService {

    @Autowired
    private BookHotelRepository bookHotelRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private EmailService emailService;

    public BookHotel bookHotel(long hotelId, BookHotel bookHotel, HttpServletRequest request){
        User user = userService.findUserByEmail(request.getUserPrincipal().getName());
        bookHotel.setUserId((long) user.getId());
        bookHotel.setHotelName(hotelService.getHotelById(hotelId).getTitle());
        bookHotelRepository.save(bookHotel);
        emailService.sendMailHotel(bookHotel, user);
        return bookHotel;
    }

    public List<BookHotel> getBookedHotel(long userId){
        return bookHotelRepository.findByUserId(userId);
    }
}
