package com.example.SilkWay.repository;

import com.example.SilkWay.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("tourRepository")
public interface TourRepository extends JpaRepository<Tour, Long> {
    List<Tour> getAllByTitle(String title);
    Tour findByTitle(String title);
    Tour findAllByCountry(String country);
    Tour findById(int id);
}
