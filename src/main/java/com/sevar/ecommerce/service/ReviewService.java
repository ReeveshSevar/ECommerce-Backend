package com.sevar.ecommerce.service;

import com.sevar.ecommerce.exception.ProductException;
import com.sevar.ecommerce.model.Review;
import com.sevar.ecommerce.model.Users;
import com.sevar.ecommerce.request.ReviewRequest;

import java.util.List;

public interface ReviewService {
    Review createReview(ReviewRequest request, Users users) throws ProductException;

    List<Review> getAllReviews(Long productId);
}
