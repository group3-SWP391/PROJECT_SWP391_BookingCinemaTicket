package org.group3.project_swp391_bookingmovieticket.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String actors;
    private String categories;
    private String director;
    private int duration;

    @Column(name = "is_showing")
    private int isShowing;

    private String language;

    @Column(name = "large_imageurl")
    private String largeImageUrl;

    @Column(name = "long_description")
    private String longDescription;

    private String name;
    private String rated;

    @Column(name = "release_date")
    private Date releaseDate;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "small_imageurl")
    private String smallImageUrl;

    @Column(name = "trailerurl")
    private String trailerUrl;
}
