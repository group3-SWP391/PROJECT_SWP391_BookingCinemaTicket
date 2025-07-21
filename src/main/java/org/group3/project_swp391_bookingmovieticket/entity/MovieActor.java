package org.group3.project_swp391_bookingmovieticket.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Table(name = "movie_actor")
@NoArgsConstructor
public class MovieActor {

    @EmbeddedId
    private MovieActorId id = new MovieActorId();

    @ToString.Exclude
    @ManyToOne
    @MapsId("movieId")
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ToString.Exclude
    @ManyToOne
    @MapsId("actorId")
    @JoinColumn(name = "actor_id")
    private Actor actor;

    @Column(name = "name_in_movie", length = 255)
    private String nameInMovie;
}

