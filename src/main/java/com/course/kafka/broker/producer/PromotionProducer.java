package com.course.kafka.broker.producer;

import com.course.kafka.broker.message.PromotionMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class PromotionProducer {
    private static final Logger log = LoggerFactory.getLogger(PromotionProducer.class);

    @Autowired
    private KafkaTemplate<String, PromotionMessage> kafkaTemplate;

    public void publish22(PromotionMessage message){
        try {
        var sendResult = kafkaTemplate.send("t.commodity.promotion", message).get();
         log.info("Send Result ----{}",sendResult);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            log.error(" Error publishing ---{} {}",message, e.getMessage());
        }
    }
}
