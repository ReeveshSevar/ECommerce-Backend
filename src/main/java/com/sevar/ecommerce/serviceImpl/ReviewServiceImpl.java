package com.sevar.ecommerce.serviceImpl;

import com.sevar.ecommerce.exception.ProductException;
import com.sevar.ecommerce.model.Product;
import com.sevar.ecommerce.model.Review;
import com.sevar.ecommerce.model.Users;
import com.sevar.ecommerce.repository.ReviewRepository;
import com.sevar.ecommerce.request.ReviewRequest;
import com.sevar.ecommerce.service.ProductService;
import com.sevar.ecommerce.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class ReviewServiceImpl implements ReviewService
{
    @Autowired
    private ProductService productService;

    @Autowired
    private ReviewRepository reviewRepository;
    @Override
    public Review createReview(ReviewRequest request, Users users) throws ProductException {
        Product product = productService.findProductById(request.getProductId());
        Review review = new Review();
        review.setProduct(product);
        review.setUsers(users);
        review.setReview(request.getReview());
        review.setCreatedAt(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getAllReviews(Long productId) {
        return reviewRepository.getAllReviewsForProduct(productId);
    }
}
