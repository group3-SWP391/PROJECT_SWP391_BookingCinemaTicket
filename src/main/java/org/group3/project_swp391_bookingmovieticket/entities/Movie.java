package org.group3.project_swp391_bookingmovieticket.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "movie")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String actors;
    private String categories;
    private String director;
    private Integer duration;

    @Column(name = "is_showing")
    private Integer isShowing;

    private String language;

    @Column(name = "large_imageurl")
    private String largeImageUrl;

    @Column(name = "long_description")
    private String longDescription;

    @Column(name = "name")
    private String name;

    private String rated;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "small_imageurl")
    private String smallImageUrl;

    @Column(name = "trailerurl")
    private String trailerUrl;
}
