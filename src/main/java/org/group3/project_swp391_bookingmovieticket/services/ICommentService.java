package org.group3.project_swp391_bookingmovieticket.services;

import org.group3.project_swp391_bookingmovieticket.entities.Comment;
import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICommentService {
    void addComment(Integer movieId, User user, String content);

    boolean canUserComment(Integer userId, String movieName);

    void replyToComment(Integer movieId, Integer parentCommentId, User user, String content);

    Page<Comment> getCommentsByMovieId(Integer movieId, Pageable pageable);

}
