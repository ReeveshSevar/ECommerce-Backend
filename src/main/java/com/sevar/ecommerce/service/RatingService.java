package com.sevar.ecommerce.service;

import com.sevar.ecommerce.exception.ProductException;
import com.sevar.ecommerce.model.Rating;
import com.sevar.ecommerce.model.Users;
import com.sevar.ecommerce.request.RatingRequest;

import java.util.List;

public interface RatingService {
    Rating createRating(RatingRequest request, Users users)throws ProductException;

    List<Rating> getProductRating(Long productId);

}
