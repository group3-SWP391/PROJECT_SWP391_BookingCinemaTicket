package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entities.Comment;
import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.group3.project_swp391_bookingmovieticket.repositories.CommentRepository;
import org.group3.project_swp391_bookingmovieticket.services.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
public class CommentController {

    @Autowired
    private ICommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @PostMapping("/addComment")
    public String addComment(@RequestParam("movieId") Integer movieId,
                             @RequestParam("content") String content,
                             HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userLogin") == null) {
            return "redirect:/login";
        }

        User user = (User) session.getAttribute("userLogin");
        commentService.addComment(movieId, user, content);
        return "redirect:/movie-details?id=" + movieId;
    }

    @PostMapping("/replyComment")
    public String replyComment(@RequestParam("movieId") Integer movieId,
                               @RequestParam("parentCommentId") Integer parentCommentId,
                               @RequestParam("content") String content,
                               HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userLogin") == null) {
            return "redirect:/login";
        }

        User user = (User) session.getAttribute("userLogin");

        try {
            commentService.replyToComment(movieId, parentCommentId, user, content);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/movie-details?id=" + movieId;
    }
    @PostMapping("/editComment")
    public String editComment(@RequestParam("commentId") Integer commentId,
                              @RequestParam("content") String content,
                              @RequestParam("movieId") Integer movieId,
                              HttpSession session) {
        User user = (User) session.getAttribute("userLogin");
        if (user != null) {
            Comment comment = commentRepository.findById(commentId).orElse(null);
            if (comment != null && comment.getUser().getId().equals(user.getId())) {
                comment.setContent(content);
                comment.setUpdatedAt(LocalDateTime.now());
                commentRepository.save(comment);
            }
        }
        return "redirect:/movie-details?id=" + movieId;
    }

    @PostMapping("/deleteComment")
    public String deleteComment(@RequestParam("commentId") Integer commentId,
                                @RequestParam("movieId") Integer movieId,
                                HttpSession session) {
        User user = (User) session.getAttribute("userLogin");
        if (user != null) {
            Comment comment = commentRepository.findById(commentId).orElse(null);
            if (comment != null && comment.getUser().getId().equals(user.getId())) {
                System.out.println("Xóa comment: " + commentId + " của phim " + movieId);
                commentRepository.delete(comment);
            }
        }
        return "redirect:/movie-details?id=" + movieId;
    }


}
