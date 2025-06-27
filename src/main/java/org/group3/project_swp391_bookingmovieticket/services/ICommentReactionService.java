package org.group3.project_swp391_bookingmovieticket.services;

import org.group3.project_swp391_bookingmovieticket.entities.Comment;
import org.group3.project_swp391_bookingmovieticket.entities.User;

public interface ICommentReactionService {
    void toggleReaction(Comment comment, User user, boolean isLike);

    long countLikes(Comment comment);

    long countDislikes(Comment comment);
}
