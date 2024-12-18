package com.sevar.ecommerce.controller;

import com.sevar.ecommerce.exception.ProductException;
import com.sevar.ecommerce.exception.UserException;
import com.sevar.ecommerce.model.Rating;
import com.sevar.ecommerce.model.Review;
import com.sevar.ecommerce.model.Users;
import com.sevar.ecommerce.request.RatingRequest;
import com.sevar.ecommerce.service.RatingService;
import com.sevar.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {
    @Autowired
    private UserService userService;

    @Autowired
    private RatingService ratingService;

    @PostMapping
    public ResponseEntity<Rating> createRating(@RequestBody RatingRequest request , @RequestHeader("Authorization") String jwt) throws UserException , ProductException{
        Users users = userService.findUserProfileByJwt(jwt);
        Rating rating = ratingService.createRating(request,users);
        return new ResponseEntity<Rating>(rating, HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Rating>> getProductRating(@PathVariable Long productId , @RequestHeader("Authorization") String jwt) throws UserException , ProductException{
        Users users = userService.findUserProfileByJwt(jwt);
        List<Rating> ratings = ratingService.getProductRating(productId);
        return new ResponseEntity<>(ratings,HttpStatus.OK);
    }
}
