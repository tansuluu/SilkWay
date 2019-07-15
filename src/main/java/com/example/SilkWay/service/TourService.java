package com.example.SilkWay.service;

import com.example.SilkWay.model.Tour;
import com.example.SilkWay.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("tourService")
public class TourService {

    @Qualifier("tourRepository")
    @Autowired
    private TourRepository tourRepository;

    public Tour saveTour(Tour tour){
        return tourRepository.save(tour);
    }
}
