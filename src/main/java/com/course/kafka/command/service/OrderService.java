package com.course.kafka.command.service;

import com.course.kafka.api.request.OrderRequest;
import com.course.kafka.command.action.OrderAction;
import com.course.kafka.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderAction orderAction;

    public String saveOrder(OrderRequest request) {

        Order order = orderAction.convertToOrder(request);// convert orderRequest to Order

        orderAction.saveToDatabase(order);                     // save order to db

        order.getItems().forEach(orderAction::publishToKafka); // flatten the item and order as kafka message and publish it

        return order.getOrderNumber(); // return order number ( auto generated)
    }
}
