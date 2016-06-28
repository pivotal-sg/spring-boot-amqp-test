package sample.amqp;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import sample.amqp.employees.Employee;
import sample.amqp.employees.EmployeeRepository;
import sample.amqp.employees.events.EmployeeAdded;

import java.util.Date;

@RabbitListener(queues = "event")
public class EmployeeEventHandler implements EventHandler {

    private final EmployeeRepository employeeRepository;

    public EmployeeEventHandler(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @RabbitHandler
    public void process(@Payload EmployeeAdded employeeAdded) {
        System.out.println(new Date() + ": " + employeeAdded.toString());

        Employee employee = new Employee(employeeAdded.getName(), employeeAdded.getUuid());
        employeeRepository.save(employee);
    }
}
