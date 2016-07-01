/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sample.amqp;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import sample.amqp.employees.EmployeeRepository;
import sample.amqp.employees.EmployeeService;
import sample.amqp.event.EventRepository;

@SpringBootApplication
@EnableScheduling
public class SampleAmqpApplication {

    @Autowired
	@Bean
	public EmployeeService mySender(RabbitTemplate rabbitTemplate, EventRepository eventRepository) {
		return new EmployeeService(rabbitTemplate, eventRepository);
	}

    @Bean
    public EmployeeEventHandler myReceiver(EmployeeRepository employeeRepository) {
        return new EmployeeEventHandler(employeeRepository);
    }

    @Bean
    public Queue eventQueue() {
        return new Queue("event");
    }

    @Bean
    public Binding binding() {
        return BindingBuilder
                .bind(eventQueue())
                .to(topicExchange())
                .with("event.*");
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("sample_amqp_topic_exchange");
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleAmqpApplication.class, args);
    }

}
