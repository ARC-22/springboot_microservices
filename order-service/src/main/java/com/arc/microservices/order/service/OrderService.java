package com.arc.microservices.order.service;

import com.arc.microservices.order.dto.OrderRequest;
import com.arc.microservices.order.model.Order;
import com.arc.microservices.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest) {
        //map OrderRequest to Order object
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setSkuCode(orderRequest.skuCode());
        order.setPrice(orderRequest.price());
        order.setQuantity(orderRequest.quantity());

        //save Order object to OrderRepository
        orderRepository.save(order);

        System.out.println("Order Placed Successfully");
    }


}
