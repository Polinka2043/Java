package com.example.demo.service;

import com.example.demo.entity.Reviews;
import com.example.demo.entity.Shipping;
import com.example.demo.repository.ReviewsRepository;
import com.example.demo.repository.ShippingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Scope("singleton")
public class ReviewsService {
    private final ReviewsRepository reviewsRepository;

    @Autowired
    public ReviewsService(ReviewsRepository reviewsRepository) {
        this.reviewsRepository = reviewsRepository;
    }

    public void saveReviews(Reviews reviews) {
        reviewsRepository.save(reviews);
    }

    public List<Reviews> getReviews() {
        return reviewsRepository.findAll();
    }


    public Reviews getReviewsById(Long id) {
        return reviewsRepository.findById(id).orElse(null);
    }


    public Reviews updateReviews(Long id, Reviews reviews) {
        Reviews existingReviews = reviewsRepository.findById(id).orElse(null);
        if (existingReviews != null) {
            existingReviews.setComment(reviews.getComment());
            existingReviews.setBirthDate(reviews.getBirthDate());
            return reviewsRepository.save(existingReviews);
        }
        return null;
    }


    public String deleteReviews(Long id) {
        reviewsRepository.deleteById(id);
        return "Reviews with ID " + id + " has been deleted.";
    }
    public List<Reviews> findByComment(String name) {
        return reviewsRepository.findByCommentContainingIgnoreCase(name);
    }
    public List<Reviews> findAll() {
        return reviewsRepository.findAll();
    }
}
