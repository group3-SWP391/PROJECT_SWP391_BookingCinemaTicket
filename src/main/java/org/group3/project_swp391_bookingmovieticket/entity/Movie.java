package org.group3.project_swp391_bookingmovieticket.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Data
@Table(name = "movie")
@Entity
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "small_imageurl", length = 1000)
    private String smallImageURL;

    @Column(name = "large_imageurl", length = 1000)
    private String largeImageURL;

    @Column(name = "short_description", length = 1000)
    private String shortDescription;

    @Column(name = "long_description", length = 1000)
    private String longDescription;

    @Column(name = "categories")
    private String categories;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "duration")
    private int duration;

    @Column(name = "language")
    private String language;

    @Column(name = "trailerurl_watch_link", length = 1000)
    private String trailerURLWatchLink;

    @Column(name = "is_showing")
    private int statusShowing;

    @Column(name = "format")
    private String format;

    @Column(name = "views")
    private int views;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "rating_id", referencedColumnName = "id")
    private Rating ratingId;

    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    private List<Schedule> scheduleList;

    @ManyToOne
    @JoinColumn(name = "director_id", referencedColumnName = "id")
    private Director director;

    @ToString.Exclude
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovieActor> movieActors;

}
