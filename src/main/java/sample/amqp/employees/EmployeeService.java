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

package sample.amqp.employees;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import sample.amqp.event.AbstractEvent;
import sample.amqp.event.EventRepository;
import sample.amqp.employees.events.EmployeeAdded;

import java.util.Arrays;
import java.util.UUID;

public class EmployeeService {

    private RabbitTemplate rabbitTemplate;
    private EventRepository eventRepository;

    public EmployeeService(RabbitTemplate rabbitTemplate, EventRepository eventRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.eventRepository = eventRepository;
    }

    public String add(String name) {
        final String uuid = UUID.randomUUID().toString();
        final AbstractEvent employeeAdded = new EmployeeAdded(name, Arrays.asList("2016-01-01", "2017-02-02"), uuid);

        eventRepository.save(employeeAdded);

        System.out.println("Saved in database as: " + eventRepository.findOne(uuid));

        this.rabbitTemplate.convertAndSend("event", employeeAdded);

        return uuid;
    }

}
