package com.sevar.ecommerce.serviceImpl;

import com.sevar.ecommerce.model.OrderItem;
import com.sevar.ecommerce.repository.OrderItemRepository;
import com.sevar.ecommerce.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl implements OrderItemService
{

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public OrderItem createOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }
}
