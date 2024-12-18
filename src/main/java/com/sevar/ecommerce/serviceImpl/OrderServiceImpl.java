package com.sevar.ecommerce.serviceImpl;

import com.sevar.ecommerce.exception.OrderException;
import com.sevar.ecommerce.model.*;
import com.sevar.ecommerce.repository.*;
import com.sevar.ecommerce.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService
{
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderItemService orderItemService;

    @Override
    public Orders createOrders(Users user, Address shippingAddress) {
        Address address;
        Optional<Address> isExisted = addressRepository.findByUserAddress(
                shippingAddress.getStreetAddress(),
                shippingAddress.getCity(),
                shippingAddress.getState(),
                shippingAddress.getZipCode(),
                user
        );
        if (isExisted.isPresent()) {
            address = isExisted.get();
        } else {
            shippingAddress.setUsers(user);
            address = addressRepository.save(shippingAddress);
        }
        if (!user.getAddress().contains(address)) {
            user.getAddress().add(address);
            userRepository.save(user);
        }
        Cart cart = cartService.findUserCart(user.getUserId());
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem item : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setPrice(item.getPrice());
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setSize(item.getSize());
            orderItem.setUserId(item.getUserId());
            orderItem.setDiscountedPrice(item.getDiscountedPrice());

            OrderItem createdOrderItem = orderItemRepository.save(orderItem);
            orderItems.add(createdOrderItem);
        }
        Orders orders = new Orders();
        orders.setUser(user);
        orders.setItems(orderItems);
        orders.setTotalPrice(cart.getTotalPrice());
        orders.setTotalDiscountedPrice(cart.getTotalDiscountedPrice());
        orders.setDiscount(cart.getDiscount());
        orders.setTotalItem(cart.getTotalItem());
        orders.setShippingAddress(address);
        orders.setOrderDate(LocalDateTime.now());
        orders.setOrderStatus("PENDING");
        if (orders.getPaymentDetails() == null) {
            orders.setPaymentDetails(new PaymentDetails());
        }
        orders.getPaymentDetails().setStatus("PENDING");
        orders.setCreatedAt(LocalDateTime.now());
        Orders savedOrder = orderRepository.save(orders);
        for (OrderItem item : orderItems) {
            item.setOrderItem(savedOrder);
            orderItemRepository.save(item);
        }
        return savedOrder;
    }

    @Override
    public Orders placedOrders(Long OrdersId) throws OrderException {
        Orders orders = findOrdersById(OrdersId);
        orders.setOrderStatus("PLACED");
        orders.getPaymentDetails().setStatus("COMPLETED");
        return orders;
    }

    @Override
    public Orders confirmedOrders(Long OrdersId) throws OrderException {
        Orders orders = findOrdersById(OrdersId);
        orders.setOrderStatus("CONFIRMED");
        return orderRepository.save(orders);
    }

    @Override
    public Orders shippedOrders(Long OrdersId) throws OrderException {
        Orders orders = findOrdersById(OrdersId);
        orders.setOrderStatus("SHIPPED");
        return orderRepository.save(orders);
    }

    @Override
    public Orders deliveredOrders(Long OrdersId) throws OrderException {
        Orders orders = findOrdersById(OrdersId);
        orders.setOrderStatus("DELIVERED");
        return orderRepository.save(orders);
    }

    @Override
    public Orders canceledOrders(Long OrdersId) throws OrderException {
        Orders orders = findOrdersById(OrdersId);
        orders.setOrderStatus("CANCELLED");
        return orderRepository.save(orders);
    }

    @Override
    public void deleteOrders(Long OrdersId) throws OrderException {
        Orders orders = findOrdersById(OrdersId);
        orderRepository.deleteById(OrdersId);
    }

    @Override
    public Orders findOrdersById(Long OrdersId) throws OrderException {
        Optional<Orders> opt = orderRepository.findById(OrdersId);
        if (opt.isPresent())
            return opt.get();
        throw new OrderException("Order Not Exist With : "+OrdersId);
    }

    @Override
    public List<Orders> userOrdersHistory(Long userId) {
        return orderRepository.getUsersOrder(userId);
    }


    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }
}
