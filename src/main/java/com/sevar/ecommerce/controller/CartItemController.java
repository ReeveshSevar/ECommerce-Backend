package com.sevar.ecommerce.controller;

import com.sevar.ecommerce.model.CartItem;
import com.sevar.ecommerce.model.Users;
import com.sevar.ecommerce.response.ApiResponse;
import com.sevar.ecommerce.service.CartItemService;
import com.sevar.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart_items")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private UserService userService;

    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItem(
            @PathVariable Long cartItemId,
            @RequestBody CartItem cartItems,
            @RequestHeader("Authorization") String jwt) {
        Users users = userService.findUserProfileByJwt(jwt);
        CartItem cartItem = cartItemService.updateCartItem(users.getUserId(),cartItemId,cartItems);
        if (cartItem.getQuantity() == 0)
            cartItemService.removeCartItem(users.getUserId(),cartItemId);
        return new ResponseEntity<>(cartItem, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<ApiResponse> removeCartItem(@PathVariable Long cartItemId, @RequestHeader("Authorization") String jwt){
        Users users = userService.findUserProfileByJwt(jwt);
        cartItemService.removeCartItem(users.getUserId(),cartItemId);
        ApiResponse response = new ApiResponse();
        response.setMessage("Item Removed Successfully");
        response.setStatus(true);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
