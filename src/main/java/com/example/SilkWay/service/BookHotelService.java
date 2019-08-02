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

    public BookHotel bookHotel(long hotelId, BookHotel bookHotel) {
        bookHotel.setHotelName(hotelService.getHotelById(hotelId).getTitle());
        bookHotel.setUserStatus("user");
        bookHotel.setAnsweredYesNo("no");
        bookHotelRepository.save(bookHotel);
        emailService.sendMailHotel(bookHotel);
        return bookHotel;
    }

    public BookHotel bookHotelUser(long hotelId, HttpServletRequest request) {
        BookHotel bookHotel = new BookHotel();
        User user = userService.findUserByEmail(request.getUserPrincipal().getName());
        if (user.getStatus().equals("company")) {

            bookHotel.setCompanyTitle(user.getTitle());

        } else {

            bookHotel.setUserName(user.getFirstName() + " " + user.getLastName());

        }
        bookHotel.setUserEmail(user.getEmail());
        bookHotel.setUserStatus(user.getStatus());
        bookHotel.setAnsweredYesNo("no");
        bookHotel.setHotelName(hotelService.getHotelById(hotelId).getTitle());
        bookHotel.setContacts(user.getContacts());
        bookHotelRepository.save(bookHotel);
        emailService.sendMailHotel(bookHotel);
        return bookHotel;
    }

    public List<BookHotel> getBookedHotel(String userEmail) {
        return bookHotelRepository.findByUserEmail(userEmail);
    }

    public void changeAnswered(long bookHotelId){
        BookHotel bookHotel = bookHotelRepository.findById(bookHotelId);
        if(bookHotel.getAnsweredYesNo().equals("no")){
            bookHotel.setAnsweredYesNo("yes");
        }
        bookHotelRepository.save(bookHotel);
    }

    public List<BookHotel> getAll(){
        return bookHotelRepository.findAll();
    }

}
