package org.group3.project_swp391_bookingmovieticket.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Objects;

@Data
@EqualsAndHashCode
public class MovieActorId implements Serializable {
    private Integer movie;
    private Integer actor;
}