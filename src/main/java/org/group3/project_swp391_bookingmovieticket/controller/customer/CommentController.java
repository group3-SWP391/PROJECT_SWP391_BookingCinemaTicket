package org.group3.project_swp391_bookingmovieticket.controller.customer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entity.Comment;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.repository.ICommentRepository;
import org.group3.project_swp391_bookingmovieticket.service.impl.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ICommentRepository commentRepository;



    @PostMapping("/addComment")
    public String addComment(@RequestParam("movieId") Integer movieId,
                             @RequestParam("content") String content,
                             HttpServletRequest request,
                             RedirectAttributes redirectAttributes) {
        System.out.println(content + movieId + "debug comment");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userLogin") == null) {
            return "redirect:/login";
        }

        User user = (User) session.getAttribute("userLogin");

        try {
            System.out.println("comment");
            commentService.addComment(movieId, user, content);
            redirectAttributes.addFlashAttribute("successMessage", "Bình luận đã được gửi.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/movie/detail?movieId=" + movieId;
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

        return "redirect:/movie/detail?movieId=" + movieId;
    }
    @PostMapping("/editComment")
    public String editComment(@RequestParam("commentId") Integer commentId,
                              @RequestParam("content") String content,
                              @RequestParam("movieId") Integer movieId,
                              HttpSession session) {
        User user = (User) session.getAttribute("userLogin");
        if (user != null) {
            Comment comment = commentService.findById(commentId);
            if (comment != null && comment.getUser().getId() == user.getId()) {
                comment.setContent(content);
                comment.setUpdatedAt(LocalDateTime.now());
                commentService.save(comment);
            }
        }
        return "redirect:/movie/detail?movieId=" + movieId;
    }

    @PostMapping("/deleteComment")
    public String deleteComment(@RequestParam("commentId") Integer commentId,
                                @RequestParam("movieId") Integer movieId,
                                HttpSession session) {
        User user = (User) session.getAttribute("userLogin");

        if (user != null) {
            Comment comment = commentService.findById(commentId);
            if (comment != null) {
                boolean isOwner = comment.getUser().getId() == user.getId();
                boolean isAdmin = "ADMIN".equalsIgnoreCase(user.getRole().getName());

                if (isOwner || isAdmin) {
                    commentService.delete(comment);
                    System.out.println("Xóa comment: " + commentId + " của phim " + movieId);
                }
            }
        }

        return "redirect:/movie/detail?moiveId=" + movieId;
    }



}
