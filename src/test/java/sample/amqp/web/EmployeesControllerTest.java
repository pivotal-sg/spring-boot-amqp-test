package sample.amqp.web;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sample.amqp.employees.Employee;
import sample.amqp.employees.EmployeeRepository;
import sample.amqp.employees.EmployeeService;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EmployeesControllerTest {
    // collaborators
    private EmployeeService employeeService;
    private EmployeeRepository employeeRepository;

    private EmployeesController employeesController;

    @Before
    public void setUp() throws Exception {
        employeeService = mock(EmployeeService.class);
        employeeRepository = mock(EmployeeRepository.class);

        employeesController = new EmployeesController(employeeService, employeeRepository);
    }

    @Test
    public void addEmployee() throws Exception {
        when(employeeService.add("John Tan")).thenReturn("some random uuid");

        ResponseEntity responseEntity = employeesController.addEmployee(Collections.singletonMap("name", "John Tan"));

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(Collections.singletonMap("uuid", "some random uuid"));
    }

    @Test
    public void showEmployee() throws Exception {
        String employeeUuid = "another random uuid";
        Employee employee = new Employee("Paul Lim", employeeUuid);
        when(employeeRepository.find(employeeUuid)).thenReturn(employee);

        ResponseEntity responseEntity = employeesController.showEmployee(employeeUuid);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(employee);
    }

}
