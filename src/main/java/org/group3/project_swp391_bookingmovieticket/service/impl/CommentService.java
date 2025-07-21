package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.entity.Comment;
import org.group3.project_swp391_bookingmovieticket.entity.Movie;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.repository.ICommentReactionRepository;
import org.group3.project_swp391_bookingmovieticket.repository.ICommentRepository;
import org.group3.project_swp391_bookingmovieticket.repository.IMovieRepository;
import org.group3.project_swp391_bookingmovieticket.repository.IPaymentLinkRepository;
import org.group3.project_swp391_bookingmovieticket.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommentService implements ICommentService {

    @Autowired
    private ICommentReactionRepository reactionRepository;

    @Autowired
    private ICommentRepository ICommentRepository;

    @Autowired
    private IPaymentLinkRepository IPaymentLinkRepository;

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
        comment.setParent(null);

        ICommentRepository.save(comment);
    }

    @Override
    public boolean canUserComment(Integer userId, String movieName) {
        return IPaymentLinkRepository.existsByUserIdAndMovieTitleAndTransactionDateBefore(
                userId, movieName, LocalDateTime.now());

    }
    @Override
    public Page<Comment> getCommentsByMovieId(Integer movieId, Pageable pageable) {
        return ICommentRepository.findByMovieId(movieId, pageable);
    }

    @Override
    public void replyToComment(Integer movieId, Integer parentCommentId, User user, String content) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Phim không tồn tại"));

        if (!canUserComment(user.getId(), movie.getName())) {
            throw new IllegalStateException("Bạn cần mua vé và xem phim để trả lời bình luận.");
        }

        Comment parent = ICommentRepository.findById(parentCommentId)
                .orElseThrow(() -> new IllegalArgumentException("Bình luận cha không tồn tại."));

        Comment reply = new Comment();
        reply.setMovie(movie);
        reply.setUser(user);
        reply.setContent(content);
        reply.setCreatedAt(LocalDateTime.now());
        reply.setParent(parent);

        ICommentRepository.save(reply);
    }

    public void deleteCommentById(Integer commentId) {
        Optional<Comment> commentOpt = ICommentRepository.findById(commentId);
        if (commentOpt.isPresent()) {
            Comment comment = commentOpt.get();

            // Xóa reactions trước
            if (comment.getReactions() != null && !comment.getReactions().isEmpty()) {
                reactionRepository.deleteAll(comment.getReactions());
            }

            // Xóa replies (nếu có)
            if (comment.getReplies() != null && !comment.getReplies().isEmpty()) {
                for (Comment reply : comment.getReplies()) {
                    ICommentRepository.delete(reply);
                }
            }

            // Xóa comment chính
            ICommentRepository.delete(comment);
        }
    }

}
