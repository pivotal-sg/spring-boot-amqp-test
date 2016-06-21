package sample.amqp.employees;

public class Employee {
    final private String name;
    final private String uuid;

    public Employee(String name, String uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }
}
