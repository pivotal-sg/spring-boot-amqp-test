package sample.amqp.employees;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EmployeeRepository {
    final private List<Employee> employeeList = new ArrayList<Employee>();

    public boolean save(Employee employee) {
        return employeeList.add(employee);
    }

    public Employee find(final String uuid) {
        return employeeList.stream()
                .filter(e -> e.getUuid().equals(uuid))
                .findFirst()
                .get();
    }
}
