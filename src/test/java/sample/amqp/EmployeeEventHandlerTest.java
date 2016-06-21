package sample.amqp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sample.amqp.employees.Employee;
import sample.amqp.employees.EmployeeRepository;
import sample.amqp.employees.events.EmployeeAdded;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class EmployeeEventHandlerTest {
    // collaborator
    private EmployeeRepository employeeRepository;

    private EmployeeEventHandler employeeEventHandler;

    @Before
    public void setUp() throws Exception {
        employeeRepository = new EmployeeRepository();
        employeeEventHandler = new EmployeeEventHandler(employeeRepository);
    }

    @Test
    public void processAddsEmployeeToRepository() throws Exception {
        EmployeeAdded employeeAdded = new EmployeeAdded("Foo Bar",
                                                        Arrays.asList("2016-01-01"),
                                                        "some-uuid");
        employeeEventHandler.process(employeeAdded);

        Employee employee = employeeRepository.find("some-uuid");
        assertThat(employee.getName()).isEqualTo("Foo Bar");
    }
}
