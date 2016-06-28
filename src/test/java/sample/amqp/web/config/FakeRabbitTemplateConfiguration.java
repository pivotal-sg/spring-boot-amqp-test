package sample.amqp.web.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import sample.amqp.EmployeeEventHandler;
import sample.amqp.web.mocks.FakeRabbitTemplate;

import static org.mockito.Mockito.mock;

@Configuration
public class FakeRabbitTemplateConfiguration {
    @Bean
    @Primary
    @Autowired
    public RabbitTemplate rabbitTemplate(EmployeeEventHandler employeeEventHandler) {
        return new FakeRabbitTemplate(connectionFactory(), employeeEventHandler);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        return mock(ConnectionFactory.class);
    }
}
