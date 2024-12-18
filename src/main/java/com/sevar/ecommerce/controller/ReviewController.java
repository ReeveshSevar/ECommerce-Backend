package com.sevar.ecommerce.controller;

import com.sevar.ecommerce.exception.ProductException;
import com.sevar.ecommerce.exception.UserException;
import com.sevar.ecommerce.model.Review;
import com.sevar.ecommerce.model.Users;
import com.sevar.ecommerce.request.ReviewRequest;
import com.sevar.ecommerce.service.ReviewService;
import com.sevar.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/")
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequest request , @RequestHeader("Authorization") String jwt) throws UserException , ProductException{
        Users users = userService.findUserProfileByJwt(jwt);
        Review review = reviewService.createReview(request,users);
        return new ResponseEntity<Review>(review, HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getProductReview(@PathVariable Long productId, @RequestHeader("Authorization") String jwt) throws ProductException, UserException
    {
        Users users = userService.findUserProfileByJwt(jwt);
        List<Review> reviews = reviewService.getAllReviews(productId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }
}
