package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.entities.Comment;
import org.group3.project_swp391_bookingmovieticket.entities.Movie;
import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.group3.project_swp391_bookingmovieticket.repositories.CommentRepository;
import org.group3.project_swp391_bookingmovieticket.repositories.IMovieRepository;
import org.group3.project_swp391_bookingmovieticket.repositories.OrderRepository;
import org.group3.project_swp391_bookingmovieticket.services.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService implements ICommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private IMovieRepository movieRepository;

    @Override
    public void addComment(Integer movieId, User user, String content) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Phim không tồn tại"));

        if (!canUserComment(user.getId(), movie.getName())) {
            throw new IllegalStateException("Bạn cần mua vé và xem phim để bình luận.");
        }

        Comment comment = new Comment();
        comment.setMovie(movie);
        comment.setUser(user);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setParent(null); // bình luận gốc

        commentRepository.save(comment);
    }

    @Override
    public boolean canUserComment(Integer userId, String movieName) {
        return orderRepository.existsByUserIdAndMovieTitleAndTransactionDateBefore(
                userId, movieName, LocalDateTime.now());

    }

    @Override
    public void replyToComment(Integer movieId, Integer parentCommentId, User user, String content) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Phim không tồn tại"));

        if (!canUserComment(user.getId(), movie.getName())) {
            throw new IllegalStateException("Bạn cần mua vé và xem phim để trả lời bình luận.");
        }

        Comment parent = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new IllegalArgumentException("Bình luận cha không tồn tại."));

        Comment reply = new Comment();
        reply.setMovie(movie);
        reply.setUser(user);
        reply.setContent(content);
        reply.setCreatedAt(LocalDateTime.now());
        reply.setParent(parent);

        commentRepository.save(reply);
    }


    @Override
    public List<Comment> getCommentsByMovieId(Integer movieId) {
        return commentRepository.findByMovieId(movieId);
    }
}
