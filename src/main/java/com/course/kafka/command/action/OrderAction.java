package com.course.kafka.command.action;

import com.course.kafka.api.request.OrderItemRequest;
import com.course.kafka.api.request.OrderRequest;
import com.course.kafka.broker.message.OrderMessage;
import com.course.kafka.broker.producer.OrderProducer;
import com.course.kafka.entity.Order;
import com.course.kafka.entity.OrderItem;
import com.course.kafka.repository.OrderItemRepository;
import com.course.kafka.repository.OrderRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderAction {

    @Autowired
    private OrderProducer orderProducer;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;


    public Order convertToOrder(OrderRequest request) {   // in the copy i draw the tables after looking the tables
        // it is easy to understand
        var result22 = new Order();
        result22.setCreditCardNumber(request.getCreditCardNumber());
        result22.setOrderDateTime(LocalDateTime.now());
        result22.setOrderLocation(request.getOrderLocation());
        result22.setOrderNumber(RandomStringUtils.randomAlphanumeric(8).toLowerCase());

        List<OrderItem> items = request.getItems().stream().map(this::convertToOrderItem22).collect(Collectors.toList());
        items.forEach(x -> x.setOrder(result22));

        result22.setItems(items);
        return result22;
    }

    private OrderItem convertToOrderItem22(OrderItemRequest itemRequest) {
        var result = new OrderItem();
        result.setItemName(itemRequest.getItemName());
        result.setPrice(itemRequest.getPrice());
        result.setQuantity(itemRequest.getQuantity());

        return result;
    }

    public void saveToDatabase(Order order) {
        orderRepository.save(order);
        order.getItems().forEach(orderItemRepository::save);
/*Note: in the Order table we do not have  List<OrderItems>  so this is the reason he is seperately saving both
      orderRepository.save(order);       here only  the Order class properties will be saved in db
      order.getItems().forEach(orderItemRepository::save);  here we are taking List<OrderItems> items
      and saving it to db table  order_items
*/
    }

    public void publishToKafka(OrderItem orderItem) {

        var orderMessage = new OrderMessage();
        orderMessage.setItemName(orderItem.getItemName());
        orderMessage.setPrice(orderItem.getPrice());
        orderMessage.setQuantity(orderItem.getQuantity());

        orderMessage.setCreditCardNumber(orderItem.getOrder().getCreditCardNumber()); // line 1
        orderMessage.setOrderDateTime(orderItem.getOrder().getOrderDateTime());       // line 2
        orderMessage.setOrderLocation(orderItem.getOrder().getOrderLocation());       // line 3
        orderMessage.setOrderNumber(orderItem.getOrder().getOrderNumber());           // line 4

        orderProducer.publish22(orderMessage);
   /* OrderItem class
         @ManyToOne
         private Order order;

      the main point in this method start from line 1 till 4
      here we are fetching the data from  OrderItem table but from line 1 till 4 we are fetching the data
      through OrderItem class from Order class  as we have defined  @ManyToOne mapping so
      many OrderItems objects can have one Order object relation as showing here and after that we are
      trying to publish all the messages to the publisher
   */
    }


}















