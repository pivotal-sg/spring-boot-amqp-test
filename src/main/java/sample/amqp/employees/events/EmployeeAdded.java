package sample.amqp.employees.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import sample.amqp.event.AbstractEvent;

import javax.persistence.*;
import java.util.List;

@Entity
public class EmployeeAdded extends AbstractEvent {
    private EmployeeAddedSerializedData serializedData = new EmployeeAddedSerializedData();

    // for serialization
    public EmployeeAdded() {}

    @JsonCreator
    public EmployeeAdded(@JsonProperty("name") String name,
                         @JsonProperty("dates") List<String> dates,
                         @JsonProperty("uuid") String uuid) {
        super(uuid);
        serializedData = new EmployeeAddedSerializedData(name, dates);
    }

    @Transient
    public String getName() {
        return serializedData.getName();
    }

    @Transient
    public List<String> getDates() {
        return serializedData.getDates();
    }

    @Column(name = "data")
    @Convert(converter = EmployeeAddedSerializedDataConverter.class)
    @JsonIgnore
    public EmployeeAddedSerializedData getSerializedData() {
        return serializedData;
    }

    public void setSerializedData(EmployeeAddedSerializedData serializedData) {
        this.serializedData = serializedData;
    }

    @Override
    public String toString() {
        return "EmployeeAdded{" +
                "serializedData=" + serializedData +
                ", uuid=" + getUuid() +
                '}';
    }
}
