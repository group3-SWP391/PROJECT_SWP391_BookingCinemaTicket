package org.group3.project_swp391_bookingmovieticket.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode
public class MovieActorId implements Serializable {
    private Integer movie;
    private Integer actor;
} 