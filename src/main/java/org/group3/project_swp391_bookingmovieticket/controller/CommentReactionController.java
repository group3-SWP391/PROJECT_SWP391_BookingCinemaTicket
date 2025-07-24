package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entity.Comment;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.repository.ICommentRepository;
import org.group3.project_swp391_bookingmovieticket.service.ICommentReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class CommentReactionController {

    @Autowired
    private ICommentReactionService reactionService;

    @Autowired
    private ICommentRepository ICommentRepository;

    @PostMapping("/like")
    public String likeComment(@RequestParam("commentId") Integer commentId,
                              @RequestParam("movieId") Integer movieId,
                              HttpServletRequest request) {
        return handleReaction(commentId, movieId, request, true);
    }

    @PostMapping("/dislike")
    public String dislikeComment(@RequestParam("commentId") Integer commentId,
                                 @RequestParam("movieId") Integer movieId,
                                 HttpServletRequest request) {
        return handleReaction(commentId, movieId, request, false);
    }

    private String handleReaction(Integer commentId, Integer movieId, HttpServletRequest request, boolean isLike) {
        HttpSession session = request.getSession(false);
        if (session == null) return "redirect:/login";

        User user = (User) session.getAttribute("userLogin");
        if (user == null) return "redirect:/login";

        Optional<Comment> optionalComment = ICommentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            reactionService.toggleReaction(comment, user, isLike);
        } else {
            throw new IllegalArgumentException("Comment không tồn tại với ID: " + commentId);
        }
        return "redirect:/movie-details?id=" + movieId;
    }

}
