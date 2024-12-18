package com.sevar.ecommerce.service;

import com.sevar.ecommerce.exception.OrderException;
import com.sevar.ecommerce.model.Address;
import com.sevar.ecommerce.model.Orders;
import com.sevar.ecommerce.model.Users;

import java.util.List;

public interface OrderService {
    public Orders createOrders(Users user , Address shippingAddress);

    public Orders findOrdersById(Long OrdersId) throws OrderException;

    public List<Orders> userOrdersHistory(Long userId);

    public Orders placedOrders(Long OrdersId) throws OrderException;

    public Orders confirmedOrders(Long OrdersId) throws OrderException;

    public Orders shippedOrders(Long OrdersId) throws OrderException;

    public Orders deliveredOrders(Long OrdersId) throws OrderException;

    public Orders canceledOrders(Long OrdersId) throws OrderException;

    public void deleteOrders(Long OrdersId) throws OrderException;

    public List<Orders> getAllOrders();
}
