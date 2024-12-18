package com.sevar.ecommerce.service;

import com.sevar.ecommerce.exception.ProductException;
import com.sevar.ecommerce.model.Cart;
import com.sevar.ecommerce.model.Users;
import com.sevar.ecommerce.request.AddItemRequest;

public interface CartService {
    Cart createCart(Users users);

    String addCartItem(Long userId, AddItemRequest req) throws ProductException;

    Cart findUserCart(Long userId) ;
}

