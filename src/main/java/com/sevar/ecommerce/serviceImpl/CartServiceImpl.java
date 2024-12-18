package com.sevar.ecommerce.serviceImpl;

import com.sevar.ecommerce.exception.CartException;
import com.sevar.ecommerce.exception.ProductException;
import com.sevar.ecommerce.model.Cart;
import com.sevar.ecommerce.model.CartItem;
import com.sevar.ecommerce.model.Product;
import com.sevar.ecommerce.model.Users;
import com.sevar.ecommerce.repository.CartRepository;
import com.sevar.ecommerce.request.AddItemRequest;
import com.sevar.ecommerce.service.CartItemService;
import com.sevar.ecommerce.service.CartService;
import com.sevar.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService
{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private ProductService productService;


    @Override
    public Cart createCart(Users users) {
        Cart cart = new Cart();
        cart.setUsers(users);
        return cartRepository.save(cart);
    }

    @Override
    public String addCartItem(Long userId, AddItemRequest req) throws ProductException {
        Cart cart = cartRepository.findUsersByUserId(userId);
        if (cart == null)
            throw new CartException("Cart Not Found");
        Product product = productService.findProductById(req.getProductId());
        if (product == null)
            throw new ProductException("Product Not Found");
        CartItem isPresent = cartItemService.isCartItemExist(cart,product,req.getSize(),userId);
        if (isPresent == null){
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(req.getQuantity());
            cartItem.setUserId(userId);
            int price = req.getQuantity() * product.getDiscountedPrice();
            cartItem.setPrice(price);
            cartItem.setSize(req.getSize());
            CartItem createdCartItem = cartItemService.createCartItem(cartItem);
            cart.getCartItems().add(createdCartItem);
        }else{
            isPresent.setQuantity(req.getQuantity() + isPresent.getQuantity());
            isPresent.setPrice(req.getQuantity()* product.getDiscountedPrice() + isPresent.getPrice());
            cartItemService.updateCartItem(userId, isPresent.getId(),isPresent);
            cart.getCartItems().add(isPresent);
        }
        return "Item Added To Cart";
    }

    @Override
    public Cart findUserCart(Long userId) {
        Cart cart = cartRepository.findUsersByUserId(userId);
        int totalPrice = 0;
        int totalDiscountedPrice = 0;
        int totalItem = 0;
        for (CartItem cartItem : cart.getCartItems()){
            totalPrice = totalPrice + cartItem.getPrice();
            totalDiscountedPrice = totalDiscountedPrice + cartItem.getDiscountedPrice();
            totalItem = totalItem + cartItem.getQuantity();
        }
        cart.setTotalItem(totalItem);
        cart.setTotalPrice(totalPrice);
        cart.setTotalDiscountedPrice(totalDiscountedPrice);
        cart.setDiscount(totalPrice-totalDiscountedPrice);
        return cartRepository.save(cart);
    }
}
