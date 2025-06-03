package org.group3.project_swp391_bookingmovieticket.dtos;

import lombok.Data;
import org.group3.project_swp391_bookingmovieticket.entities.Actor;
import org.group3.project_swp391_bookingmovieticket.entities.Movie;

@Data
public class MovieActorDTO {
    private Movie movie;
    private Actor actor;
    private String nameInMovie;
}

