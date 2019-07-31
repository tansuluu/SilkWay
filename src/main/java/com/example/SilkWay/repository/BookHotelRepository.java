package com.example.SilkWay.repository;

import com.example.SilkWay.model.BookHotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookHotelRepository extends JpaRepository<BookHotel, Long> {

    List<BookHotel> findByUserId(long userId);
}
