package org.group3.project_swp391_bookingmovieticket.service;

import org.group3.project_swp391_bookingmovieticket.entity.Comment;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICommentService {
    void addComment(Integer movieId, User user, String content);

    boolean canUserComment(Integer userId, String movieName);

    void replyToComment(Integer movieId, Integer parentCommentId, User user, String content);

    Page<Comment> getCommentsByMovieId(Integer movieId, Pageable pageable);

}
