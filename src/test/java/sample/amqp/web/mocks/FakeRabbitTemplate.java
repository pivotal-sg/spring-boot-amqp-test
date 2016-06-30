package sample.amqp.web.mocks;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.util.Assert;
import sample.amqp.EmployeeEventHandler;
import sample.amqp.EventHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FakeRabbitTemplate extends RabbitTemplate {
    private final List<EventHandler> eventHandlers;

    public FakeRabbitTemplate(ConnectionFactory connectionFactory, EventHandler... eventHandlers) {
        super(connectionFactory);
        this.eventHandlers = Arrays.asList(eventHandlers);
    }

    @Override
    public void convertAndSend(String routingKey, Object object) throws AmqpException {
        if (routingKey.equals("event")) {
            invokeMethod(getEventHandler(EmployeeEventHandler.class), object);
        }
        else {
            throw new UnsupportedOperationException("No supported event handlers available for routing key: " + routingKey);
        }

    }

    @SuppressWarnings("unchecked")
    private <E extends EventHandler> E getEventHandler(Class<E> eventHandlerType) {
        RuntimeException exception = new RuntimeException("Event handler not found for type: " + eventHandlerType.getCanonicalName());
        return this.eventHandlers.stream()
                .filter(eventHandlerType::isInstance)
                .map(e -> (E) e)
                .findFirst()
                .orElseThrow(() -> exception);
    }

    private void invokeMethod(EventHandler eventHandler, Object object) {
        try {
            getMethod(object, eventHandler).invoke(eventHandler, object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private Method getMethod(Object object, EventHandler eventHandler) {
        List<Method> matchedMethods = getMethodsWithMatchingParameterType(eventHandler, object);

        assertMethodsExist(eventHandler, matchedMethods);
        assertOnlyOneMethod(eventHandler, matchedMethods);

        return matchedMethods.get(0);
    }

    private void assertOnlyOneMethod(EventHandler eventHandler, List<Method> matchedMethods) {
        String eventHandlerClassName = eventHandler.getClass().getCanonicalName();
        Assert.state(matchedMethods.size() == 1,
                     eventHandlerClassName + " has more than 1 annotated @RabbitHandler method with the same parameter type");
    }

    private void assertMethodsExist(EventHandler eventHandler, List<Method> matchedMethods) {
        String eventHandlerClassName = eventHandler.getClass().getCanonicalName();
        Assert.notEmpty(matchedMethods,
                        eventHandlerClassName + " does not have any methods annotated with @RabbitHandler");
    }

    private List<Method> getMethodsWithMatchingParameterType(EventHandler eventHandler, Object object) {
        Method[] methods = eventHandler.getClass().getMethods();
        return Arrays.stream(methods)
                .filter(m -> m.isAnnotationPresent(RabbitHandler.class))
                .filter(m -> hasPayloadAnnotatedParameter(m))
                .filter(m -> isParameterTypeOf(m, object))
                .collect(Collectors.toList());
    }

    private boolean hasPayloadAnnotatedParameter(Method m) {
        return new MethodParameter(m, 0)
                .hasParameterAnnotation(Payload.class);
    }

    private boolean isParameterTypeOf(Method m, Object object) {
        return new MethodParameter(m, 0)
                .getParameterType()
                .isInstance(object);
    }
}
