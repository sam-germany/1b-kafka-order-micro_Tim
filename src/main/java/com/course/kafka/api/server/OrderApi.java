package com.course.kafka.api.server;

import com.course.kafka.api.request.OrderRequest;
import com.course.kafka.api.response.OrderResponse;
import com.course.kafka.command.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderApi {

    @Autowired
    private OrderService orderService;

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {

        String orderNumber = orderService.saveOrder(request);
        OrderResponse orderResponse = new OrderResponse(orderNumber);
        return  ResponseEntity.ok().body(orderResponse);
    }


}
