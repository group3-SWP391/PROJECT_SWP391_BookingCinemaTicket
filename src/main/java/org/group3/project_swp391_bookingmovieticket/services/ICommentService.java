package org.group3.project_swp391_bookingmovieticket.services;

import org.group3.project_swp391_bookingmovieticket.entities.Comment;
import org.group3.project_swp391_bookingmovieticket.entities.User;

import java.util.List;

public interface ICommentService {
    void addComment(Integer movieId, User user, String content);

    boolean canUserComment(Integer userId, String movieName);

    void replyToComment(Integer movieId, Integer parentCommentId, User user, String content);

    List<Comment> getCommentsByMovieId(Integer movieId);
}
