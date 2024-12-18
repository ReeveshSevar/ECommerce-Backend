package com.sevar.ecommerce.serviceImpl;

import com.sevar.ecommerce.exception.ProductException;
import com.sevar.ecommerce.model.Product;
import com.sevar.ecommerce.model.Rating;
import com.sevar.ecommerce.model.Users;
import com.sevar.ecommerce.repository.RatingRepository;
import com.sevar.ecommerce.request.RatingRequest;
import com.sevar.ecommerce.service.ProductService;
import com.sevar.ecommerce.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RatingServiceImpl implements RatingService
{
    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private ProductService productService;

    @Override
    public Rating createRating(RatingRequest request, Users users) throws ProductException {
        Product product = productService.findProductById(request.getProductId());
        Rating rating = new Rating();
        rating.setProduct(product);
        rating.setUsers(users);
        rating.setRating(request.getRating());
        rating.setCreatedAt(LocalDateTime.now());
        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getProductRating(Long productId) {
        return ratingRepository.getAllProductRating(productId);
    }

}
