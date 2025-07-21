package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entity.Comment;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.repository.ICommentRepository;
import org.group3.project_swp391_bookingmovieticket.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
public class CommentController {

    @Autowired
    private ICommentService commentService;

    @Autowired
    private ICommentRepository ICommentRepository;

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
            Comment comment = ICommentRepository.findById(commentId).orElse(null);
            if (comment != null && comment.getUser().getId().equals(user.getId())) {
                comment.setContent(content);
                comment.setUpdatedAt(LocalDateTime.now());
                ICommentRepository.save(comment);
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
            Comment comment = ICommentRepository.findById(commentId).orElse(null);
            if (comment != null) {
                boolean isOwner = comment.getUser().getId().equals(user.getId());
                boolean isAdmin = "ADMIN".equalsIgnoreCase(user.getRole().getName());

                if (isOwner || isAdmin) {
                    ICommentRepository.delete(comment);
                    System.out.println("Xóa comment: " + commentId + " của phim " + movieId);
                }
            }
        }

        return "redirect:/movie-details?id=" + movieId;
    }



}
