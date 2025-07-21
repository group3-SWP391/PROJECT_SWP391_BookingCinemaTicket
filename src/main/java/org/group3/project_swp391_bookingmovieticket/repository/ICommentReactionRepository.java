package org.group3.project_swp391_bookingmovieticket.repository;

import org.group3.project_swp391_bookingmovieticket.entity.Comment;
import org.group3.project_swp391_bookingmovieticket.entity.CommentReaction;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICommentReactionRepository extends JpaRepository<CommentReaction, Integer> {

    long countByCommentAndLiked(Comment comment, boolean liked);

    boolean existsByCommentAndUser(Comment comment, User user);

    void deleteByCommentAndUser(Comment comment, User user);
}
