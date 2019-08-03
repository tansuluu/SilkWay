package com.example.SilkWay.service;

import com.example.SilkWay.model.Tour;
import com.example.SilkWay.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("tourService")
public class TourService {

    @Qualifier("tourRepository")
    @Autowired
    private TourRepository tourRepository;

    public Tour saveTour(Tour tour) {
        return tourRepository.save(tour);
    }

    public Tour getTourById(long id) {
        return tourRepository.findById(id);
    }

    public Tour getTourByTitle(String title) {
        return tourRepository.findByTitle(title);
    }

    public Page<Tour> getAllTours(Pageable pageable) {
        return tourRepository.findAll(pageable);
    }

    public void deleteTour(Tour tour) {
        tourRepository.delete(tour);
    }


    public Tour updateTour(Tour tour) {
        Tour newTour = tourRepository.findById(tour.getId());
        tour.setImg_name(newTour.getImg_name());
        return tourRepository.save(tour);
    }

    public List<Tour> filterTour(String country, long priceMin, long priceMax, Date dateFrom, Date dateTo, int page, int limit) {
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
            searchArray = getAll();
            for (Tour tour : searchArray) {
                if (tour.getPrice() < priceMax & tour.getPrice()>priceMin) {
                    finalLastSearch.add(tour);
                }
            }
        }
        else if (country.isEmpty() & (priceMax != 0L & priceMin==0L)) {
            searchArray = getAll();
            for (Tour tour : searchArray) {
                if (tour.getPrice() < priceMax) {
                    finalLastSearch.add(tour);
                }
            }
        }
        else if (country.isEmpty() & (priceMax == 0L & priceMin!=0L)) {
            searchArray = getAll();
            for (Tour tour : searchArray) {
                if (tour.getPrice()>priceMin) {
                    finalLastSearch.add(tour);
                }
            }
        }
        else {
            finalLastSearch = getAll();
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

    public List<Tour> getAll(){
        return tourRepository.findAll();
    }

    public List<Tour> getAllToursByHotTour(String text){
        return tourRepository.getAllByHotTourYesNo(text);
    }

    public void changeHotYesToNo(long tourId){
        Tour tour = tourRepository.findById(tourId);
        if(tour.getHotTourYesNo().equals("yes")){
            tour.setHotTourYesNo("no");
        }
    }

    public void changeHotNoToYes(long tourId){
        Tour tour = tourRepository.findById(tourId);
        if(tour.getHotTourYesNo().equals("no")){
            tour.setHotTourYesNo("yes");
        }
    }
}
