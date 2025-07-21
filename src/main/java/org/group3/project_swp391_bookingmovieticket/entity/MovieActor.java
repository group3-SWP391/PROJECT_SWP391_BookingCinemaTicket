package org.group3.project_swp391_bookingmovieticket.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "movie_actor")
@IdClass(MovieActorId.class)
public class MovieActor {
    
    @Id
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Id
    @ManyToOne
    @JoinColumn(name = "actor_id")
    private Actor actor;

    @Column(name = "name_in_movie")
    private String nameInMovie;
} 