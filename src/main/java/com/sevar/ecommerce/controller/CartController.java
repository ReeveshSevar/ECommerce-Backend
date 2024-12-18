package com.sevar.ecommerce.controller;

import com.sevar.ecommerce.exception.ProductException;
import com.sevar.ecommerce.exception.UserException;
import com.sevar.ecommerce.model.Cart;
import com.sevar.ecommerce.model.Users;
import com.sevar.ecommerce.request.AddItemRequest;
import com.sevar.ecommerce.response.ApiResponse;
import com.sevar.ecommerce.service.CartService;
import com.sevar.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization") String jwt) throws UserException{
        Users users = userService.findUserProfileByJwt(jwt);
        Cart cart = cartService.findUserCart(users.getUserId());
        return new ResponseEntity<Cart>(cart, HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestBody AddItemRequest request, @RequestHeader("Authorization") String jwt) throws ProductException,UserException{
        Users users = userService.findUserProfileByJwt(jwt);
        cartService.addCartItem(users.getUserId(),request);
        ApiResponse response = new ApiResponse();
        response.setMessage("Item Added To Cart");
        response.setStatus(true);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
