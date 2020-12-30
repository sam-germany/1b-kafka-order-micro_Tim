package com.course.kafka.broker.producer;

import com.course.kafka.broker.message.OrderMessage;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderProducer {
    private static final Logger log = LoggerFactory.getLogger(OrderProducer.class);

    @Autowired
    private KafkaTemplate<String, OrderMessage> kafkaTemplate;


    private ProducerRecord<String, OrderMessage> buildProducerRecord22(OrderMessage message) {
        int surpriseBonus = StringUtils.startsWithIgnoreCase(message.getOrderLocation(),"A") ? 25 : 15;

        System.out.println(StringUtils.startsWithIgnoreCase(message.getOrderLocation(),"A")+"----------");

        List<Header> headers22 = new ArrayList<>();
        var surpriseBonusHeader = new RecordHeader("surpriseBonus", Integer.toString(surpriseBonus).getBytes());
        headers22.add(surpriseBonusHeader);

        return new ProducerRecord<String, OrderMessage>("t.commodity.order", null,message.getOrderNumber(), message, headers22);
    }



    public void publish22(OrderMessage message) {
      //  kafkaTemplate.send("t.commodity.order",message.getOrderNumber(), message) <-- without header but we change in vidoe 69

        var producerRecord22  = buildProducerRecord22(message);

        kafkaTemplate.send(producerRecord22)
                     .addCallback(new ListenableFutureCallback<SendResult<String, OrderMessage>>() {

                         @Override
                         public void onSuccess(SendResult<String, OrderMessage> result) {
                                 log.info("Order {}, item {} published successfully+++++++++",
                                            message.getOrderNumber(), message.getItemName());
                         }
                         @Override
                         public void onFailure(Throwable ex) {
                              log.error("Order {}, item {} failed to publish, because, {}-------",
                                  message.getOrderNumber(), message.getItemName(), ex.getMessage());
                        }
                     });
        System.out.println("----------------------------------");
    }




}
