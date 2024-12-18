package com.sevar.ecommerce.controller;

import com.sevar.ecommerce.exception.OrderException;
import com.sevar.ecommerce.model.Orders;
import com.sevar.ecommerce.response.ApiResponse;
import com.sevar.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/")
    public ResponseEntity<List<Orders>> gteAllOrders(){
        List<Orders> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/confirmed")
    public ResponseEntity<Orders> confirmOrder(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws OrderException
    {
        Orders orders = orderService.confirmedOrders(orderId);
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }

    @PutMapping("/{orderId}/shipped")
    public ResponseEntity<Orders> shipOrder(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws OrderException
    {
        Orders orders = orderService.shippedOrders(orderId);
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }

    @PutMapping("/{orderId}/deliver")
    public ResponseEntity<Orders> deliverOrder(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws OrderException
    {
        Orders orders = orderService.deliveredOrders(orderId);
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Orders> cancelOrder(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws OrderException
    {
        Orders orders = orderService.canceledOrders(orderId);
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }
    @PutMapping("/{orderId}/delete")
    public ResponseEntity<ApiResponse> deleteOrder(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws OrderException
    {
        orderService.deleteOrders(orderId);
        ApiResponse response = new ApiResponse();
        response.setMessage("Order Deleted Successfully");
        response.setStatus(true);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
