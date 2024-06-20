package com.example.demo.repository;

import com.example.demo.entity.Promotions;
import com.example.demo.entity.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewsRepository extends JpaRepository<Reviews,Long> {
    List<Reviews> findByComment(String comment);
    List<Reviews> findByCommentContainingIgnoreCase(String comment);
    void deleteById(Long id);
}
