package org.group3.project_swp391_bookingmovieticket.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "movie")
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "small_imageurl", length = 1000)
    private String smallImageUrl;

    @Column(name = "large_imageurl", length = 1000)
    private String largeImageUrl;

    @Column(name = "short_description", length = 1000)
    private String shortDescription;

    @Column(name = "long_description", length = 1000)
    private String longDescription;

    @Column(name = "director")
    private String director;

    @Column(name = "actors")
    private String actors;

    @Column(name = "categories")
    private String categories;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "language")
    private String language;

    @Column(name = "trailerurl", length = 1000)
    private String trailerUrl;

    @Column(name = "rated")
    private String rated;

    @Column(name = "is_showing")
    private Integer isShowing;

    @Column(name = "format")
    private String format;

    @Column(name = "views")
    private Integer views;

    @Column(name = "trailerurl_watch_link", length = 1000)
    private String trailerUrlWatchLink;

    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    private List<Schedule> scheduleList;
}