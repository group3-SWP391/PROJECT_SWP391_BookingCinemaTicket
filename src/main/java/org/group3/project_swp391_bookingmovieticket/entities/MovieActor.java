package org.group3.project_swp391_bookingmovieticket.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "movie_actor")
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

