package com.meli.charge.configuration;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

@EnableJms
@Configuration
public class JMSBillQueueConfig {

	@Bean
	public Queue queue() {
		return new ActiveMQQueue("bill.queue");
	}

}
