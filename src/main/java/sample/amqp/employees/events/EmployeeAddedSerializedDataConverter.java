package sample.amqp.employees.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;

@Converter
public class EmployeeAddedSerializedDataConverter implements AttributeConverter<EmployeeAddedSerializedData, String> {
    @Override
    public String convertToDatabaseColumn(EmployeeAddedSerializedData attribute) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public EmployeeAddedSerializedData convertToEntityAttribute(String dbData) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(dbData, EmployeeAddedSerializedData.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
