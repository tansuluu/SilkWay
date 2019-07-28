package com.example.SilkWay.repository;

import com.example.SilkWay.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("hotelRepository")
public interface HotelRepository extends JpaRepository<Hotel, Long>,
        PagingAndSortingRepository<Hotel, Long> {
    List<Hotel> getAllByTitle(String title);
    Hotel findByTitle(String title);
    List<Hotel> findAllByStars(long stars);
    List<Hotel> findAllByCategory(String category);
    Hotel findById(long id);
}
