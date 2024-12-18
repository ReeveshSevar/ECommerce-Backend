package com.sevar.ecommerce.service;

import com.sevar.ecommerce.exception.CartItemException;
import com.sevar.ecommerce.exception.UserException;
import com.sevar.ecommerce.model.Cart;
import com.sevar.ecommerce.model.CartItem;
import com.sevar.ecommerce.model.Product;

public interface CartItemService {
    CartItem createCartItem(CartItem cartItem);

    CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException;

    CartItem isCartItemExist(Cart cart, Product product, String size, Long userId);

    void removeCartItem(Long userId, Long cartItemId)throws CartItemException, UserException;

    CartItem findCartItemById(Long cartItemId) throws CartItemException;

}
