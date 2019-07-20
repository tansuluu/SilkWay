package com.example.SilkWay.service;

import com.example.SilkWay.model.Tour;
import com.example.SilkWay.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("tourService")
public class TourService {

    @Qualifier("tourRepository")
    @Autowired
    private TourRepository tourRepository;

    public Tour saveTour(String title, long price, String country, String description, Date dateFrom, Date dateTo, MultipartFile file) {
        Tour tour = new Tour();
        tour.setTitle(title);
        tour.setPrice(price);
        tour.setCountry(country);
        tour.setDescription(description);
        tour.setDateFrom(dateFrom);
        tour.setDateTo(dateTo);
        tour.setImg_name(file.getOriginalFilename());
        return tourRepository.save(tour);
    }

    public Tour getTourById(long id) {
        return tourRepository.findById(id);
    }

    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    public void deleteTour(Tour tour) {
        tourRepository.delete(tour);
    }


    public Tour updateTour(Tour tour, MultipartFile file) {
        Tour newTour = tourRepository.findByTitle(tour.getTitle());
        newTour.setImg_name(file.getOriginalFilename());
        newTour.setCreated(tour.getCreated());
        newTour.setCountry(tour.getCountry());
        newTour.setDescription(tour.getDescription());
        newTour.setTitle(tour.getTitle());
        return tourRepository.save(newTour);
    }

    public List<Tour> filterTour(String country, long priceMin, long priceMax, Date dateFrom, Date dateTo) {
        List<Tour> searchArray;
        List<Tour> finalSearch = new ArrayList<>();
        List<Tour> finalLastSearch = new ArrayList<>();
        if (!country.isEmpty() & (priceMin == 0L & priceMax == 0L)) {
            finalLastSearch = tourRepository.getAllByCountry(country);
        }
        else if (!country.isEmpty() & (priceMin == 0L & priceMax!=0L)) {
            searchArray = tourRepository.getAllByCountry(country);
            for (Tour tour : searchArray) {
                if (tour.getPrice() > priceMax) {
                    searchArray.remove(tour);
                }
            }
            finalLastSearch = searchArray;
        }
        else if (!country.isEmpty() & (priceMax == 0L & priceMin!=0L)) {
            searchArray = tourRepository.getAllByCountry(country);
            for (Tour tour : searchArray) {
                if (tour.getPrice() < priceMin) {
                    searchArray.remove(tour);
                }
            }
            finalLastSearch = searchArray;
        }
        else if (country.isEmpty() & (priceMax != 0L & priceMin!=0L)) {
            searchArray = getAllTours();
            for (Tour tour : searchArray) {
                if (tour.getPrice() < priceMax & tour.getPrice()>priceMin) {
                    finalLastSearch.add(tour);
                }
            }
        }
        else if (country.isEmpty() & (priceMax != 0L & priceMin==0L)) {
            searchArray = getAllTours();
            for (Tour tour : searchArray) {
                if (tour.getPrice() < priceMax) {
                    finalLastSearch.add(tour);
                }
            }
        }
        else if (country.isEmpty() & (priceMax == 0L & priceMin!=0L)) {
            searchArray = getAllTours();
            for (Tour tour : searchArray) {
                if (tour.getPrice()>priceMin) {
                    finalLastSearch.add(tour);
                }
            }
        }
        else {
            finalLastSearch = getAllTours();
        }

        if (dateFrom != null & dateTo != null) {
            for (Tour tour : finalLastSearch) {
                Date date1 = tour.getDateFrom();
                Date date2 = tour.getDateTo();
                if ((dateFrom.after(date1) || dateFrom.equals(date1)) & (dateTo.before(date2) || dateTo.equals(date2))) {
                    finalSearch.add(tour);
                }
            }
        }
        else if(dateFrom!=null & dateTo==null){
            for (Tour tour : finalLastSearch) {
                Date date1 = tour.getDateFrom();
                if ((dateFrom.after(date1) || dateFrom.equals(date1))) {
                    finalSearch.add(tour);
                }
            }
        }
        else if(dateFrom==null & dateTo!=null){
            for (Tour tour : finalLastSearch) {
                Date date1 = tour.getDateTo();
                if ((dateTo.before(date1) || dateTo.equals(date1))) {
                    finalSearch.add(tour);
                }
            }
        }
        else {
            return finalLastSearch;
        }
        return finalSearch;
    }
}
