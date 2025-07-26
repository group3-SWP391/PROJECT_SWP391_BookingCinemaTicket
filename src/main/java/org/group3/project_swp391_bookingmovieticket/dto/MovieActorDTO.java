package org.group3.project_swp391_bookingmovieticket.dto;

import lombok.Data;
import org.group3.project_swp391_bookingmovieticket.entity.Actor;
import org.group3.project_swp391_bookingmovieticket.entity.Movie;

@Data
public class MovieActorDTO {
    private Movie movie;
    private Actor actor;
    private String nameInMovie;
}

