package org.group3.project_swp391_bookingmovieticket.service;

import org.group3.project_swp391_bookingmovieticket.entity.Comment;
import org.group3.project_swp391_bookingmovieticket.entity.User;

public interface ICommentReactionService {
    void toggleReaction(Comment comment, User user, boolean isLike);

    long countLikes(Comment comment);

    long countDislikes(Comment comment);
}
