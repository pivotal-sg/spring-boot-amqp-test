package sample.amqp.event;

import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

@Component
public interface EventRepository extends Repository<AbstractEvent, String> {
    AbstractEvent save(AbstractEvent event);
    AbstractEvent findOne(String uuid);
}
