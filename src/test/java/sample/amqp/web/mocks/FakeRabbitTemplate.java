package sample.amqp.web.mocks;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import sample.amqp.EmployeeEventHandler;
import sample.amqp.employees.events.EmployeeAdded;

public class FakeRabbitTemplate extends RabbitTemplate {
    private final EmployeeEventHandler employeeEventHandler;

    public FakeRabbitTemplate(ConnectionFactory connectionFactory, EmployeeEventHandler employeeEventHandler) {
        super(connectionFactory);
        this.employeeEventHandler = employeeEventHandler;
    }

    @Override
    public void convertAndSend(String routingKey, Object object) throws AmqpException {
        this.employeeEventHandler.process((EmployeeAdded) object);
    }
}
