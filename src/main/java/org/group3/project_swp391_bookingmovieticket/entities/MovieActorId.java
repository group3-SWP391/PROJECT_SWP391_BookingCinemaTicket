package org.group3.project_swp391_bookingmovieticket.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MovieActorId implements Serializable {

    @Column(name = "movie_id")
    private Integer movieId;

    @Column(name = "actor_id")
    private Integer actorId;

    // Bắt buộc: equals() & hashCode()

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieActorId)) return false;
        MovieActorId that = (MovieActorId) o;
        return Objects.equals(movieId, that.movieId) &&
                Objects.equals(actorId, that.actorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId, actorId);
    }
}
