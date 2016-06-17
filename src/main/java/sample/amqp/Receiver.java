package sample.amqp;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.Date;

@RabbitListener(queues = "event")
public class Receiver {

    @RabbitHandler
    public void process(@Payload EmployeeAdded employeeAdded) {
        System.out.println(new Date() + ": " + employeeAdded.toString());
    }
}
