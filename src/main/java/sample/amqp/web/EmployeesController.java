package sample.amqp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sample.amqp.employees.Employee;
import sample.amqp.employees.EmployeeRepository;
import sample.amqp.employees.EmployeeService;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping(name = "employees", path = "/employees")
public class EmployeesController {
    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeesController(EmployeeService employeeService, EmployeeRepository employeeRepository) {
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping
    public ResponseEntity addEmployee(@RequestBody Map<String, String> requestBody) {
        final String name = requestBody.getOrDefault("name", "");
        final String employeeUuid = employeeService.add(name);
        return ResponseEntity.ok()
                .body(Collections.singletonMap("uuid", employeeUuid));
    }

    @GetMapping(path = "/{employeeUuid}")
    public ResponseEntity showEmployee(@PathVariable String employeeUuid) {
        final Employee employee = employeeRepository.find(employeeUuid);
        return ResponseEntity.ok()
                .body(employee);
    }
}
