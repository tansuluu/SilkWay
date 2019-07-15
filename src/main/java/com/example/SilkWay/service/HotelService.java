package com.example.SilkWay.service;

import com.example.SilkWay.model.Hotel;
import com.example.SilkWay.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

@Service("hotelService")
public class HotelService {
    @Autowired
    private HotelRepository hotelRepository;


    public Hotel saveHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }
}
