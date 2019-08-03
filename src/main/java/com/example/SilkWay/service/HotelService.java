package com.example.SilkWay.service;

import com.example.SilkWay.model.Hotel;
import com.example.SilkWay.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("hotelService")
public class HotelService {
    @Qualifier("hotelRepository")
    @Autowired
    private HotelRepository hotelRepository;

    public Hotel saveHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public Page<Hotel> getAll(Pageable pageable){
        return hotelRepository.findAll(pageable);
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

    public Hotel updateHotel(Hotel hotel){
        Hotel newHotel = hotelRepository.findById(hotel.getId());
        hotel.setImg_name(newHotel.getImg_name());
        return hotelRepository.save(hotel);
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
        return  getAll();
    }

    public List<Hotel> getAll(){
        return hotelRepository.findAll();
    }
}
