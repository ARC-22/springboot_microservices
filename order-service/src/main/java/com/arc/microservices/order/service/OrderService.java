package com.arc.microservices.order.service;

import com.arc.microservices.order.client.InventoryClient;
import com.arc.microservices.order.dto.OrderRequest;
import com.arc.microservices.order.event.OrderPlacedEvent;
import com.arc.microservices.order.model.Order;
import com.arc.microservices.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public void placeOrder(OrderRequest orderRequest) {

        // to test use Mockito(do not send http request) or WireMock(sends http request)
        System.out.println(orderRequest.quantity());

        System.out.println(orderRequest.skuCode());
        var isProductInStock = inventoryClient.isInStock("laptop", orderRequest.quantity());

        //map OrderRequest to Order object
       if(isProductInStock){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setSkuCode(orderRequest.skuCode());
        order.setPrice(orderRequest.price());
        order.setQuantity(orderRequest.quantity());

        //save Order object to OrderRepository
        orderRepository.save(order);
        System.out.println("Order Placed Successfully");

        // Send message to Kafka topic(order number, email)
           //order.getOrderNumber(), orderRequest.userDetails().email()

        OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();

        orderPlacedEvent.setOrderNumber(order.getOrderNumber());
        orderPlacedEvent.setEmail(orderRequest.userDetails().email());
        orderPlacedEvent.setFirstName(orderRequest.userDetails().firstName());
        orderPlacedEvent.setLastName(orderRequest.userDetails().lastName());

        log.info("Sending OrderPlacedEvent to Kafka topic order-placed: {}", orderPlacedEvent);
        kafkaTemplate.send("order-placed", orderPlacedEvent);
        log.info("OrderPlacedEvent sent successfully");
        }
        else{
            throw new RuntimeException("Product is out of stock skuCode: "+orderRequest.skuCode());
        }
    }


}
