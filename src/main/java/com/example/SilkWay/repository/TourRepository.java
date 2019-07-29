package com.example.SilkWay.repository;

import com.example.SilkWay.model.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("tourRepository")
public interface TourRepository extends JpaRepository<Tour, Long>,PagingAndSortingRepository<Tour, Long> {

    Tour findByTitle(String title);

    Tour findById(long id);

    List<Tour> getAllByCountry(String country);

}
