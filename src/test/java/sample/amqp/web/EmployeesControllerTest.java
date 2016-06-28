package sample.amqp.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sample.amqp.EmployeeEventHandler;
import sample.amqp.employees.events.EmployeeAdded;
import sample.amqp.event.AbstractEvent;

import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeesControllerTest {
    @Autowired
    private WebApplicationContext context;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private EmployeeEventHandler employeeEventHandler;

    private MockMvc mvc;

    @Before
    public void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context)
                .alwaysDo(print())
                .build();
    }

    @Test
    public void addEmployee() throws Exception {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                EmployeeAdded event = (EmployeeAdded) args[1];
                employeeEventHandler.process(event);
                return null;
            }
        }).when(rabbitTemplate)
                .convertAndSend(eq("event"), any(AbstractEvent.class));

        String contentAsString = mvc.perform(
                post("/employees")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"John Tan\"}")
            )
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        String employeeUuid = (String) new JacksonJsonParser()
                                            .parseMap(contentAsString)
                                            .get("uuid");

        mvc.perform(
            get("/employees/" + employeeUuid)
        ).andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("John Tan")));
    }

}
