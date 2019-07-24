package com.example.SilkWay.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hotel")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_id")
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "stars")
    private long stars;

    @Column(name = "price")
    private Integer price;

    @Column(name = "description")
    private String description;
    @Column(name = "category")
    private String category;

    private Date created;
    @PrePersist
    protected void onCreate() {
        created = new Date();
    }

    @Column(name = "img_name")
    private String img_name;

    @NotBlank
    private String email;
}
