package com.example.SilkWay.repository;

import com.example.SilkWay.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("hotelRepository")
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> getAllByTitle(String title);
    Hotel findByTitle(String title);
    List<Hotel> findAllByStars(Integer stars);
    Hotel findAllByPrice(Integer pricce);
    Hotel findById(int id);
}
