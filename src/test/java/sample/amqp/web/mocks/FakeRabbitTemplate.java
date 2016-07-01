package sample.amqp.web.mocks;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.util.Assert;
import sample.amqp.EventHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FakeRabbitTemplate extends RabbitTemplate {
    private final Map<String, EventHandler> routingKeyAndEventHandlerMap;

    public FakeRabbitTemplate(ConnectionFactory connectionFactory,
                              Map<String, EventHandler> routingKeyAndEventHandlerMap) {
        super(connectionFactory);
        this.routingKeyAndEventHandlerMap = routingKeyAndEventHandlerMap;
    }

    @Override
    public void convertAndSend(String routingKey, Object object) throws AmqpException {
        Assert.isTrue(routingKeyAndEventHandlerMap.containsKey(routingKey),
                      "No supported event handlers available for routing key: " + routingKey);
        invokeMethod(routingKeyAndEventHandlerMap.get(routingKey), object);
    }

    private void invokeMethod(EventHandler eventHandler, Object object) {
        try {
            getRabbitHandlerMethod(eventHandler, object)
                    .invoke(eventHandler, object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private Method getRabbitHandlerMethod(EventHandler eventHandler, Object object) {
        List<Method> matchedMethods = getRabbitHandlerMethodsWithMatchingParameterType(eventHandler, object);

        Assert.notEmpty(matchedMethods,
                        getEventHandlerCanonicalName(eventHandler) + " does not have any methods annotated with @RabbitHandler");
        Assert.state(matchedMethods.size() == 1,
                     getEventHandlerCanonicalName(eventHandler) + " has more than 1 annotated @RabbitHandler method with the same parameter type");

        return matchedMethods.get(0);
    }

    private String getEventHandlerCanonicalName(EventHandler eventHandler) {
        return eventHandler.getClass().getCanonicalName();
    }

    private List<Method> getRabbitHandlerMethodsWithMatchingParameterType(EventHandler eventHandler, Object object) {
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
