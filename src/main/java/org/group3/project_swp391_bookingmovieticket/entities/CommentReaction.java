package org.group3.project_swp391_bookingmovieticket.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class CommentReaction {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    private Comment comment;

    @ManyToOne
    private User user;

    private boolean liked;

}

