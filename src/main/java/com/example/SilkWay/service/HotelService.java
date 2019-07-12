package com.example.SilkWay.service;

import com.example.SilkWay.model.Hotel;
import com.example.SilkWay.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service("hotelService")
public class HotelService {
    @Qualifier("hotelRepository")
    @Autowired
    private HotelRepository hotelRepository;

    public Hotel saveHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public List<Hotel> getAllHotels(){
        return hotelRepository.findAll();
    }

    public Hotel getHotelById(long id){
        return hotelRepository.findById(id);
    }

    public Hotel getHotelByTitle(String title){
        return hotelRepository.findByTitle(title);
    }

    public List<Hotel> getAllByStars(long star){
        return hotelRepository.findAllByStars(star);
    }

    public void deleteHotel(Hotel hotel){
        hotelRepository.delete(hotel);
    }

    public Hotel updateHotel(Hotel hotel, MultipartFile file){
        Hotel newHotel = new Hotel();
        newHotel.setImg_name(file.getOriginalFilename());
        newHotel.setCreated(hotel.getCreated());
        newHotel.setStars(hotel.getStars());
        newHotel.setDescription(hotel.getDescription());
        newHotel.setTitle(hotel.getTitle());
        newHotel.setCategory(hotel.getCategory());
        newHotel.setPrice(hotel.getPrice());
        hotelRepository.delete(hotel);
        return hotelRepository.save(newHotel);
    }

//    public Hotel filter(Hotel hotel){
//        if(hotel.getTitle()!=null){
//            return hotelRepository.findByTitle(hotel.getTitle());
//        }
//        else if(hotel.getTitle()==null){
//            if()
//        }
//    }
}
