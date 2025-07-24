package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.entity.Comment;
import org.group3.project_swp391_bookingmovieticket.entity.CommentReaction;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.repository.ICommentReactionRepository;
import org.group3.project_swp391_bookingmovieticket.service.ICommentReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentReactionService implements ICommentReactionService {

    @Autowired
    private ICommentReactionRepository reactionRepo;

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
