package org.group3.project_swp391_bookingmovieticket.repositories;

import org.group3.project_swp391_bookingmovieticket.entities.Comment;
import org.group3.project_swp391_bookingmovieticket.entities.CommentReaction;
import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentReactionRepository extends JpaRepository<CommentReaction, Integer> {

    long countByCommentAndLiked(Comment comment, boolean liked);

    boolean existsByCommentAndUser(Comment comment, User user);

    void deleteByCommentAndUser(Comment comment, User user);
}
