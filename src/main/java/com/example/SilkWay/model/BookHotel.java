package com.example.SilkWay.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BookHotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String hotelName;

    private Long userId;

    private int numOfAdults;

    private int numOfChildren;

    private int numOfRooms;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date checkIn;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date checkOut;
}
