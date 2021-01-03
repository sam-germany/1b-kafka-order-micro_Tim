package com.course.kafka.command.action;

import com.course.kafka.api.request.FeedbackRequest;
import com.course.kafka.broker.message.FeedbackMessage;
import com.course.kafka.broker.producer.FeedbackProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class FeedbackAction {

	@Autowired
	private FeedbackProducer producer;

	public void publishToKafka(FeedbackRequest request) {
		var message = new FeedbackMessage();
		message.setFeedback(request.getFeedback());
		message.setLocation(request.getLocation());
		message.setRating(request.getRating());

		// random date time between last 7 days - now
message.setFeedbackDateTime(LocalDateTime.now()
				                         .minusHours(ThreadLocalRandom.current().nextLong(7 * 7))
		                   );

		producer.publish(message);
	}
}
/*
ThreadLocalRandom  <- it is same as Math.random()  for getting any random number, it is better to
                      use then Math.random() as it improves performance see  www.tutorialspoint.com

ThreadLocalRandom.current().nextLong(100);  <-- return a Long value between 0 till 99
                                                    just for understanding i put this example

 */
