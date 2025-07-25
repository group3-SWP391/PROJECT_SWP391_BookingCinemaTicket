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

    private String categories;
    private Integer duration;

    @Column(name = "is_showing")
    private Integer isShowing;

    private String language;

    @Column(name = "large_imageurl")
    private String largeImageUrl;

    @Column(name = "long_description", length = 1000)
    private String longDescription;

    private String name;

    @ManyToOne
    @JoinColumn(name = "rating_id")
    private Rating rating;

    @Column(name = "release_date")
    private Date releaseDate;

    @Column(name = "short_description", length = 500)
    private String shortDescription;

    @Column(name = "small_imageurl")
    private String smallImageUrl;

    @Column(name = "trailerurl_watch_link")
    private String trailerUrlWatchLink;

    @Column(name = "format", length = 10)
    private String format;

    @ManyToOne
    @JoinColumn(name = "director_id")
    private Director director;

    private Integer views;

    @Column(name = "end_date")
    private Date endDate;
}
