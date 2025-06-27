package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.group3.project_swp391_bookingmovieticket.services.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CommentController {

    @Autowired
    private ICommentService commentService;

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


}
