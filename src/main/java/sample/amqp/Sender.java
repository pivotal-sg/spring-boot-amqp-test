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

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Arrays;
import java.util.UUID;

public class Sender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private EventRepository eventRepository;

    @Scheduled(fixedDelay = 10000L)
    public void send() {
        final String uuid = UUID.randomUUID().toString();
        final AbstractEvent employeeAdded = new EmployeeAdded("Added an employee", Arrays.asList("2016-01-01", "2017-02-02"), uuid);

        eventRepository.save(employeeAdded);

        System.out.println("Saved in database as: " + eventRepository.findOne(uuid));

        this.rabbitTemplate.convertAndSend("event", employeeAdded);
    }

}
