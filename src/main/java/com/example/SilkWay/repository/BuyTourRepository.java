package com.example.SilkWay.repository;

import com.example.SilkWay.model.BuyTour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuyTourRepository extends JpaRepository<BuyTour, Long> {

    List<BuyTour> findByUserEmail(String userEmail);

    BuyTour findById(long id);
}
