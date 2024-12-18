package com.sevar.ecommerce.controller;

import com.sevar.ecommerce.exception.OrderException;
import com.sevar.ecommerce.exception.UserException;
import com.sevar.ecommerce.model.Address;
import com.sevar.ecommerce.model.Orders;
import com.sevar.ecommerce.model.Users;
import com.sevar.ecommerce.service.OrderService;
import com.sevar.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Orders> createOrder(@RequestBody Address address , @RequestHeader("Authorization") String jwt) throws UserException{
        Users users = userService.findUserProfileByJwt(jwt);
        Orders orders = orderService.createOrders(users,address);
        return new ResponseEntity<Orders>(orders, HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Orders>> userOrderHistory(@RequestHeader("Authorization") String jwt) throws UserException{
        Users users = userService.findUserProfileByJwt(jwt);
        List<Orders> orders = orderService.userOrdersHistory(users.getUserId());
        return new ResponseEntity<>(orders,HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Orders> findOrderById(@PathVariable Long orderId , @RequestHeader("Authorization") String jwt) throws UserException, OrderException{
        Users users = userService.findUserProfileByJwt(jwt);
        Orders orders = orderService.findOrdersById(orderId);
        return new ResponseEntity<>(orders,HttpStatus.ACCEPTED);
    }
}
