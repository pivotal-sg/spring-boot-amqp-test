package sample.amqp.employees;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import sample.amqp.employees.events.EmployeeAdded;
import sample.amqp.event.EventRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class EmployeeServiceTest {
    // collaborators
    private RabbitTemplate rabbitTemplate;
    private EventRepository eventRepository;

    private EmployeeService employeeService;

    @Before
    public void setUp() throws Exception {
        rabbitTemplate = mock(RabbitTemplate.class);
        eventRepository = mock(EventRepository.class);

        employeeService = new EmployeeService(rabbitTemplate, eventRepository);
    }

    @Test
    public void addPersistsEmployeeAddedEvent() throws Exception {
        String employeeUuid = employeeService.add("John Tan");

        ArgumentCaptor<EmployeeAdded> eventCaptor = ArgumentCaptor.forClass(EmployeeAdded.class);
        verify(eventRepository, times(1)).save(eventCaptor.capture());

        EmployeeAdded event = eventCaptor.getValue();
        assertThat(event.getName()).isEqualTo("John Tan");
        assertThat(event.getUuid()).isEqualTo(employeeUuid);
    }

    @Test
    public void addPublishesToEventQueue() throws Exception {
        String employeeUuid = employeeService.add("Foo Bar");

        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<EmployeeAdded> eventCaptor = ArgumentCaptor.forClass(EmployeeAdded.class);
        verify(rabbitTemplate, times(1)).convertAndSend(queueNameCaptor.capture(), eventCaptor.capture());

        assertThat(queueNameCaptor.getValue()).isEqualTo("event");
        EmployeeAdded employeeAdded = eventCaptor.getValue();
        assertThat(employeeAdded.getName()).isEqualTo("Foo Bar");
        assertThat(employeeAdded.getUuid()).isEqualTo(employeeUuid);
    }

}
