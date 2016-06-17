package sample.amqp;

import java.util.ArrayList;
import java.util.List;

public class EmployeeAddedSerializedData {
    private String name;
    private List<String> dates = new ArrayList<String>();

    public EmployeeAddedSerializedData() {}

    public EmployeeAddedSerializedData(String name, List<String> dates) {
        this.name = name;
        this.dates = dates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    @Override
    public String toString() {
        return "EmployeeAddedSerializedData{" +
                "name='" + name + '\'' +
                ", dates=" + dates +
                '}';
    }
}
