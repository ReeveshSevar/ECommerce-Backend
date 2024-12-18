package com.sevar.ecommerce.serviceImpl;

import com.sevar.ecommerce.exception.CartItemException;
import com.sevar.ecommerce.exception.UserException;
import com.sevar.ecommerce.model.Cart;
import com.sevar.ecommerce.model.CartItem;
import com.sevar.ecommerce.model.Product;
import com.sevar.ecommerce.model.Users;
import com.sevar.ecommerce.repository.CartItemRepository;
import com.sevar.ecommerce.service.CartItemService;
import com.sevar.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService
{
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserService userService;

    @Override
    public CartItem createCartItem(CartItem cartItem) {
        System.err.println(cartItem.getQuantity());
        if (cartItem.getQuantity() == 0)
            cartItem.setQuantity(1);
        else
            cartItem.setQuantity(cartItem.getQuantity());
        cartItem.setPrice(cartItem.getQuantity() * cartItem.getProduct().getPrice());
        cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice()*cartItem.getQuantity());
        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException {
        CartItem item = findCartItemById(id);
        Users users = userService.findUserByUserId(item.getUserId());
        if (users.getUserId().equals(userId)){
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(item.getQuantity()*item.getProduct().getPrice());
            item.setDiscountedPrice(item.getQuantity() * item.getProduct().getDiscountedPrice());
        }
        return cartItemRepository.save(item);
    }

    @Override
    public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId) {
        return cartItemRepository.isCartItemExist(cart,product,size,userId);
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {
        CartItem cartItem = findCartItemById(cartItemId);
        Users users = userService.findUserByUserId(cartItem.getUserId());
        Users reqUser = userService.findUserByUserId(userId);
        if (users.getUserId().equals(reqUser.getUserId())){
            cartItemRepository.deleteById(cartItemId);
        }
        else
            throw new UserException("This Cart Doesn't Belong To Your Account");
    }

    @Override
    public CartItem findCartItemById(Long cartItemId) throws CartItemException {
        Optional<CartItem> opt = cartItemRepository.findById(cartItemId);
        if (opt.isPresent())
            return opt.get();
        throw new CartItemException("Cart Item Not Found With "+cartItemId);
    }
}
