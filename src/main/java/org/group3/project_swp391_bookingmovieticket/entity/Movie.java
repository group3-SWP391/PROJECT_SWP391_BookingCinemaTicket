package org.group3.project_swp391_bookingmovieticket.entity;

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
    private int duration;
    private String language;
    @Column(name = "large_imageurl")
    private String largeImageUrl;
    @Column(name = "long_description")
    private String longDescription;
    private String name;
    @Column(name = "rating_id")
    private String rated;
    @Column(name = "release_date")
    private Date releaseDate;
    @Column(name = "short_description")
    private String shorDescription;
    @Column(name = "small_imageurl")
    private String smallImageurl;
    @Column(name = "trailerurl_watch_link")
    private String trailerurlWatchLink;
    @Column(name = "format")
    private String format;

    @ManyToOne
    @JoinColumn(name = "director_id")
    private Director director;
    private String categories;
    private int views;
    private Date end_date;
    @Column(name = "is_showing")
    private int isShowing;












}
