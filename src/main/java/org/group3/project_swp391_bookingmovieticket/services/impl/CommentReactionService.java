package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.entities.Comment;
import org.group3.project_swp391_bookingmovieticket.entities.CommentReaction;
import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.group3.project_swp391_bookingmovieticket.repositories.CommentReactionRepository;
import org.group3.project_swp391_bookingmovieticket.services.ICommentReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentReactionService implements ICommentReactionService {

    @Autowired
    private CommentReactionRepository reactionRepo;

    @Override
    public void toggleReaction(Comment comment, User user, boolean isLike) {
        reactionRepo.deleteByCommentAndUser(comment, user);
        CommentReaction reaction = new CommentReaction();
        reaction.setComment(comment);
        reaction.setUser(user);
        reaction.setLiked(isLike);
        reactionRepo.save(reaction);
    }

    @Override
    public long countLikes(Comment comment) {
        return reactionRepo.countByCommentAndLiked(comment, true);
    }

    @Override
    public long countDislikes(Comment comment) {
        return reactionRepo.countByCommentAndLiked(comment, false);
    }

}
