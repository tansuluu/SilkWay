package com.example.SilkWay.service;

import com.example.SilkWay.model.Hotel;
import com.example.SilkWay.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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

    public List<Hotel> getAllHotelsByTitle(String title){
        return hotelRepository.getAllByTitle(title);
    }

    public List<Hotel> getAllHotelsByCategory(String category){

        return hotelRepository.findAllByCategory(category);
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

    public List<Hotel> filterHotels(String title, String category, long stars){

        List<Hotel> hotels;

        if(!title.isEmpty()){
            return getAllHotelsByTitle(title);
        }
        else if(title.isEmpty() && category.isEmpty()){
            return getAllByStars(stars);
        }
        else if(title.isEmpty() && stars ==0){
            return getAllHotelsByCategory(category);
        }

        else if(title.isEmpty() && !category.isEmpty() && stars != 0){
            hotels = getAllHotelsByCategory(category);
            for (Hotel hotel : hotels) {
                if(hotel.getStars() != stars){
                    hotels.remove(hotel);
                }
            }
            return hotels;
        }
        return  getAllHotels();
    }
}
