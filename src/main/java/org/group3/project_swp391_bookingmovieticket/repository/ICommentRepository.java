package org.group3.project_swp391_bookingmovieticket.repository;

import org.group3.project_swp391_bookingmovieticket.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, Integer> {
    Page<Comment> findByMovieIdAndParentIsNull(Integer movieId, Pageable pageable);
}
